package com.taskforce.app.messaging.utils;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.taskforce.app.communication.event.BusProvider;
import com.taskforce.app.communication.event.MessageReceiveEvent;
import com.taskforce.app.communication.event.MessageReplaceEvent;
import com.taskforce.app.communication.event.RefreshInboxEvent;
import com.taskforce.app.communication.network.Api;
import com.taskforce.app.communication.network.rersponse.MessageSendingFailed;
import com.taskforce.app.messaging.message.Message;
import com.taskforce.app.messaging.message.MessageResponse;
import com.taskforce.app.utils.LogUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MessageJob extends Job {
    private Message mMessage;
    private int count;

    public MessageJob(Message message) {
        super(new Params(Priority.HIGH).groupBy(String.valueOf(message.user.id)).persist().requireNetwork());
        message.uniqueId = Long.toString(-System.currentTimeMillis());
        mMessage = message;

    }

    @Override
    public void onAdded() {
        mMessage.status = Message.SENDING;
         BusProvider.getInstance().post(new MessageReceiveEvent(mMessage));
    }

    @Override
    public void onRun() throws Throwable {
        try {

//            MessageResponse mReceived = Api.SERVICE.sendMessage( mMessage);
            Api.SERVICE.sendMessage(mMessage, new Callback<MessageResponse>() {
                @Override
                public void success(MessageResponse messageResponse, Response response) {
                    if (messageResponse.responseHeader.responseCode == 1) {
                        Message mReceived = messageResponse.Message;
                        mReceived.status = Message.SENT;
                        BusProvider.getInstance().post(new MessageReplaceEvent(mReceived, mMessage));
                        BusProvider.getInstance().post(new RefreshInboxEvent(null));
                    } else {
                        Message old = mMessage;
                        mMessage.status = Message.FAILED;
                        BusProvider.getInstance().post(new MessageReplaceEvent(mMessage, old));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Message old = mMessage;
                    mMessage.status = Message.FAILED;
                    BusProvider.getInstance().post(new MessageReplaceEvent(mMessage, old));
                }
            });

            count++;
        } catch (Exception e) {
            throw new MessageSendingFailed("test", 213, e);
        }


    }

    @Override
    protected void onCancel() {
        LogUtils.LOGD(">>MessageJob", "onCancel");
        Message old = mMessage;
        mMessage.status = Message.FAILED;
        BusProvider.getInstance().post(new MessageReplaceEvent(mMessage, old));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return throwable instanceof MessageSendingFailed && count < 3;
    }
}
