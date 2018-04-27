package com.taskforce.app.messaging.message.messageItem.master.media;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.taskforce.app.R;
import com.taskforce.app.messaging.message.MediaMessage;
import com.taskforce.app.messaging.message.MessageSource;
import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.MessageItemType;
import com.taskforce.app.messaging.message.messageItem.MessageViewHolder;
import com.taskforce.app.utils.DateUtils;

import static android.support.v4.content.ContextCompat.getDrawable;
import static com.taskforce.app.Application.context;
import static com.taskforce.app.glide.GlideUtils.setCircleImage;
import static com.taskforce.app.utils.DateUtils.getTimestamp;
import static com.taskforce.app.utils.ImageUtil.getWidthToHeightRatio;

/**
 * This class fill all the view for media  and used an controller for view
 */
public abstract class MessageMediaItem extends MessageItem {
    private Context context;

    public MessageMediaItem(MediaMessage mediaMessage, Context context) {
        super(mediaMessage);
        this.context = context;
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder) {

        if (message != null && messageViewHolder != null && messageViewHolder instanceof MessageMediaViewHolder) {

            final MessageMediaViewHolder messageMediaViewHolder = (MessageMediaViewHolder) messageViewHolder;

            // Get content
            float widthToHeightRatio = getWidthToHeightRatio(getMediaMessage().getUrl(), context);
            date = getTimestamp(context, message.getDate());
            final String mediaUrl = getMediaMessage().getUrl();
            this.avatarUrl = message.getUser().getImage();

            // Populate views with content
            messageMediaViewHolder.timestamp.setText(date != null ? date : "");
            messageMediaViewHolder.initials.setText(initials != null ? initials : "");

            messageMediaViewHolder.media.setWidthToHeightRatio(widthToHeightRatio);
            messageMediaViewHolder.media.setImageUrlToLoadOnLayout(mediaUrl);

            if (isFirstConsecutiveMessageFromSource) {
                setCircleImage(context(), messageMediaViewHolder.avatar, avatarUrl, getDrawable(context(), R.drawable.person_image));
            }

            messageViewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageMediaViewHolder.customSettings.userClicksAvatarPictureListener != null)
                        messageMediaViewHolder.customSettings.userClicksAvatarPictureListener.userClicksAvatarPhoto(message.user.id);
                }
            });

            messageMediaViewHolder.media.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, ViewImageActivity.class);
//                    intent.putExtra("URL", mediaUrl);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                }
            });

            messageMediaViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource && !TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.initials.setVisibility(isFirstConsecutiveMessageFromSource && TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.GONE);
            messageMediaViewHolder.media.setVisibility(!TextUtils.isEmpty(mediaUrl) ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.timestamp.setVisibility(isLastConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public MessageItemType getMessageItemType() {
        if (message.getSource() == MessageSource.EXTERNAL_USER) {
            return MessageItemType.INCOMING_MEDIA;
        } else {
            return MessageItemType.OUTGOING_MEDIA;
        }
    }

    @Override
    public MessageSource getMessageSource() {
        return message.getSource();
    }

    public MediaMessage getMediaMessage() {
        return (MediaMessage) message;
    }

    public boolean dateNeedsUpdated(long time) {
        return DateUtils.dateNeedsUpdated(context, time, date);
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }
}
