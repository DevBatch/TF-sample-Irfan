package com.taskforce.app.messaging.message.messageItem.spinner;

import com.taskforce.app.messaging.message.MessageSource;
import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.MessageItemType;
import com.taskforce.app.messaging.message.messageItem.MessageViewHolder;


public class SpinnerItem extends MessageItem {
    public SpinnerItem() {
        super(null);
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {

    }

    @Override
    public MessageItemType getMessageItemType() {
        return MessageItemType.SPINNER;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.EXTERNAL_USER;
    }
}
