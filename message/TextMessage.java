package com.taskforce.app.messaging.message;

import android.content.Context;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.externalUser.text.MessageExternalUserTextItem;
import com.taskforce.app.messaging.message.messageItem.internalUser.text.MessageInternalUserTextItem;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * bind one message item to list
 */
public class TextMessage extends Message implements Serializable {


    public String getText() {
        return message;
    }

    public void setText(String text) {
        this.message = text;
    }

    @Override
    public MessageItem toMessageItem(Context context){
        if (this.source == MessageSource.EXTERNAL_USER)
            return new MessageExternalUserTextItem(this, context);
        else
            return new MessageInternalUserTextItem(this, context);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public TextMessage() {
    }

    protected TextMessage(Parcel in) {
        super(in);
    }

    public static final Creator<TextMessage> CREATOR = new Creator<TextMessage>() {
        @Override
        public TextMessage createFromParcel(Parcel source) {
            return new TextMessage(source);
        }

        @Override
        public TextMessage[] newArray(int size) {
            return new TextMessage[size];
        }
    };

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
}