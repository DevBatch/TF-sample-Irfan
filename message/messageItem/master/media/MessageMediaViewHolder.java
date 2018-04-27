package com.taskforce.app.messaging.message.messageItem.master.media;

import android.view.View;

import com.taskforce.app.messaging.message.messageItem.MessageViewHolder;
import com.taskforce.app.messaging.utils.CustomSettings;
import com.taskforce.app.widgets.GlideRoundedImageView;

public class MessageMediaViewHolder extends MessageViewHolder {
    public GlideRoundedImageView media;

    public MessageMediaViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);
    }
}
