package com.taskforce.app.messaging.message;

import android.content.Context;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.externalUser.media.MessageExternalUserMediaItem;
import com.taskforce.app.messaging.message.messageItem.internalUser.media.MessageInternalUserMediaItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class MediaMessage extends Message {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public MessageItem toMessageItem(Context context){
        if (this.source == MessageSource.EXTERNAL_USER)
            return new MessageExternalUserMediaItem(this, context);
        else
            return new MessageInternalUserMediaItem(this, context);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return null;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RecyclerView.ViewHolder holder, int position, List payloads) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.url);
    }

    public MediaMessage() {
    }

    protected MediaMessage(Parcel in) {
        super(in);
        this.url = in.readString();
    }

    public static final Creator<MediaMessage> CREATOR = new Creator<MediaMessage>() {
        @Override
        public MediaMessage createFromParcel(Parcel source) {
            return new MediaMessage(source);
        }

        @Override
        public MediaMessage[] newArray(int size) {
            return new MediaMessage[size];
        }
    };
}