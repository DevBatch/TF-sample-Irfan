package com.taskforce.app.messaging.message.messageItem.internalUser.media;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskforce.app.R;
import com.taskforce.app.messaging.message.messageItem.master.media.MessageMediaViewHolder;
import com.taskforce.app.messaging.utils.CustomSettings;
import com.taskforce.app.widgets.GlideRoundedImageView;


public class MessageInternalUserViewHolder extends MessageMediaViewHolder {

    public MessageInternalUserViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);

        avatar = itemView.findViewById(R.id.message_user_media_image_view_avatar);
        media = itemView.findViewById(R.id.message_user_media_picasso_rounded_image_view_media);
        initials = itemView.findViewById(R.id.message_user_media_text_view_initials);
        timestamp = itemView.findViewById(R.id.message_user_media_text_view_timestamp);
        avatarContainer = itemView.findViewById(R.id.message_user_media_view_group_avatar);
    }
}
