package com.taskforce.app.messaging.message.messageItem;


public enum MessageItemType {
    INCOMING_MEDIA,
    INCOMING_TEXT,
    OUTGOING_MEDIA,
    OUTGOING_TEXT,
    SPINNER,
    GENERAL_TEXT,
    GENERAL_OPTIONS;

    public static final MessageItemType values[] = values();
}
