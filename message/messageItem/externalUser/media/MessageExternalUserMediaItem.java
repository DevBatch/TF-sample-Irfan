package com.taskforce.app.messaging.message.messageItem.externalUser.media;

import android.content.Context;

import com.taskforce.app.messaging.message.MediaMessage;
import com.taskforce.app.messaging.message.messageItem.master.media.MessageMediaItem;

/**
 * init all the Media massages views item for local user
 */
public class MessageExternalUserMediaItem extends MessageMediaItem {

    public MessageExternalUserMediaItem(MediaMessage messageMedia, Context context) {
        super(messageMedia, context);
    }
}
