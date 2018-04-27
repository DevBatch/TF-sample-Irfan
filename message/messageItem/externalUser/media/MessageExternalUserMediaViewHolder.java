package com.taskforce.app.messaging.message.messageItem.externalUser.media;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskforce.app.R;
import com.taskforce.app.messaging.message.messageItem.master.media.MessageMediaViewHolder;
import com.taskforce.app.messaging.utils.CustomSettings;
import com.taskforce.app.widgets.GlideRoundedImageView;

/**
 * View holder for all media view and init all UI element of view
 */
public class MessageExternalUserMediaViewHolder extends MessageMediaViewHolder {

    public MessageExternalUserMediaViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);

        this.avatarContainer = itemView.findViewById(R.id.message_scout_media_image_view_avatar_group);
        avatar = itemView.findViewById(R.id.message_scout_media_image_view_avatar);
        media = itemView.findViewById(R.id.message_scout_media_picasso_rounded_image_view_media);
        timestamp = itemView.findViewById(R.id.message_scout_media_text_view_timestamp);
        initials = itemView.findViewById(R.id.message_scout_media_text_view_initials);
    }
}
