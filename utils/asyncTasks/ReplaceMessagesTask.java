package com.taskforce.app.messaging.utils.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.taskforce.app.messaging.message.Message;
import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.MessageRecyclerAdapter;
import com.taskforce.app.messaging.utils.MessageUtils;
import com.taskforce.app.messaging.utils.Refresher;

import java.util.ConcurrentModificationException;
import java.util.List;

public class ReplaceMessagesTask extends AsyncTask {
    private List<Message> mMessages;
    private List<MessageItem> mMessageItems;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private Context context;
    private Refresher mRefresher;
    private int upTo;

    public ReplaceMessagesTask(List<Message> messages, List<MessageItem> messageitems,
                               MessageRecyclerAdapter mRecyclerAdapter, RecyclerView recyclerView, Context context, Refresher refresher, int upTo) {
        this.mMessages = messages;
        this.mRecyclerAdapter = mRecyclerAdapter;
        this.upTo = upTo;
        mRecyclerView = recyclerView;
        this.mRefresher = refresher;
        this.mMessageItems = messageitems;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRefresher.setIsRefreshing(true);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            for (int i = mMessageItems.size() - 1; i >= 0; i--) {
                mMessageItems.remove(i);
            }

            for (Message message : mMessages) {
                if (context == null)
                    return null;
                mMessageItems.add(message.toMessageItem(context)); // this call is why we need the AsyncTask
            }

            for (int i = 0; i < mMessageItems.size(); i++) {
                MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(i, mMessageItems);
            }
        } catch (ConcurrentModificationException e) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o != null) {
            return;
        }
        if (upTo >= 0 && upTo < mMessageItems.size()) {
            mRecyclerAdapter.notifyItemRangeInserted(0, upTo);
            mRecyclerAdapter.notifyItemChanged(upTo);
        } else {
            mRecyclerAdapter.notifyDataSetChanged();
        }
        if (mMessageItems.size() > 0) {
            mRecyclerView.scrollToPosition(mRecyclerAdapter.getItemCount() - 1);
        }
        mRefresher.setIsRefreshing(false);
    }
}
