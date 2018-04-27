package com.taskforce.app.messaging.message.messageItem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskforce.app.messaging.utils.CustomSettings;


public class MessageViewHolder extends RecyclerView.ViewHolder {
    public ImageView avatar;
    public TextView initials;
    public TextView timestamp;
    public ViewGroup avatarContainer;
    public CustomSettings customSettings;

    public MessageViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView);
        this.customSettings = customSettings;
    }
}
