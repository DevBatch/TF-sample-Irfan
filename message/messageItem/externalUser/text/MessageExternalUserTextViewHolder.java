package com.taskforce.app.messaging.message.messageItem.externalUser.text;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskforce.app.R;
import com.taskforce.app.messaging.message.messageItem.master.text.MessageTextViewHolder;
import com.taskforce.app.messaging.utils.CustomSettings;
/**
 * View holder for all media view and init all UI element of view
 */
public class MessageExternalUserTextViewHolder extends MessageTextViewHolder {

    public MessageExternalUserTextViewHolder(View itemView, final CustomSettings customSettings) {
        super(itemView, customSettings);

        avatar = itemView.findViewById(R.id.message_scout_text_image_view_avatar);
        carrot = itemView.findViewById(R.id.message_scout_text_image_view_carrot);
        text = itemView.findViewById(R.id.message_scout_text_text_view_text);
        timestamp = itemView.findViewById(R.id.message_scout_text_text_view_timestamp);
        avatarContainer = itemView.findViewById(R.id.message_scout_text_image_view_avatar_group);
        initials = itemView.findViewById(R.id.message_scout_text_text_view_initials);
        bubble = itemView.findViewById(R.id.message_scout_text_view_group_bubble);

        Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.shape_rounded_rectangle_white);
        // Drawable drawable = itemView.getContext().getDrawable();
        drawable.setColorFilter(customSettings.externalBubbleBackgroundColor, PorterDuff.Mode.SRC_ATOP);
        bubble.setBackground(drawable);
        carrot.setColorFilter(customSettings.externalBubbleBackgroundColor);
        text.setTextColor(customSettings.externalBubbleTextColor);
        timestamp.setTextColor(customSettings.timestampColor);
    }
}
