package com.taskforce.app.messaging.message;

import android.content.Context;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.general.generalText.MessageGeneralTextItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class GeneralTextMessage extends Message {
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public MessageItem toMessageItem(Context context) {
        return new MessageGeneralTextItem(this);
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
        dest.writeString(this.text);
    }

    public GeneralTextMessage() {
    }

    protected GeneralTextMessage(Parcel in) {
        super(in);
        this.text = in.readString();
    }

    public static final Creator<GeneralTextMessage> CREATOR = new Creator<GeneralTextMessage>() {
        @Override
        public GeneralTextMessage createFromParcel(Parcel source) {
            return new GeneralTextMessage(source);
        }

        @Override
        public GeneralTextMessage[] newArray(int size) {
            return new GeneralTextMessage[size];
        }
    };
}
