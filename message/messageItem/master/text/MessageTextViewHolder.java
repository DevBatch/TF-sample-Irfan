package com.taskforce.app.messaging.message.messageItem.master.text;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskforce.app.messaging.message.messageItem.MessageViewHolder;
import com.taskforce.app.messaging.utils.CustomSettings;


public abstract class MessageTextViewHolder extends MessageViewHolder {
    public ImageView carrot;
    public TextView text;
    public FrameLayout bubble;

    public MessageTextViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);
    }
}