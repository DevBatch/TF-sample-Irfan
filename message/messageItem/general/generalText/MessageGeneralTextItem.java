package com.taskforce.app.messaging.message.messageItem.general.generalText;

import com.taskforce.app.messaging.message.GeneralTextMessage;
import com.taskforce.app.messaging.message.MessageSource;
import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.MessageItemType;
import com.taskforce.app.messaging.message.messageItem.MessageViewHolder;

public class MessageGeneralTextItem extends MessageItem {
    public MessageGeneralTextItem(GeneralTextMessage message) {
        super(message);
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder) {
        MessageGeneralTextViewHolder viewHolder = (MessageGeneralTextViewHolder) messageViewHolder;
        GeneralTextMessage generalTextMessage = (GeneralTextMessage) message;
        viewHolder.messageTextView.setText(generalTextMessage.getText());
    }

    @Override
    public MessageItemType getMessageItemType() {
        return MessageItemType.GENERAL_TEXT;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }
}
