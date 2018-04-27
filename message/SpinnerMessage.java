package com.taskforce.app.messaging.message;

import android.content.Context;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.spinner.SpinnerItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Show loading message in view.
 */
public class SpinnerMessage extends Message {
    @Override
    public MessageItem toMessageItem(Context context) {
        return new SpinnerItem();
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
    }

    public SpinnerMessage() {
    }

    protected SpinnerMessage(Parcel in) {
        super(in);
    }

    public static final Creator<SpinnerMessage> CREATOR = new Creator<SpinnerMessage>() {
        @Override
        public SpinnerMessage createFromParcel(Parcel source) {
            return new SpinnerMessage(source);
        }

        @Override
        public SpinnerMessage[] newArray(int size) {
            return new SpinnerMessage[size];
        }
    };
}
