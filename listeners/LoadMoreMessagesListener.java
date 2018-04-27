package com.taskforce.app.messaging.listeners;

import com.taskforce.app.messaging.message.Message;

import java.util.List;

public interface LoadMoreMessagesListener {
    List<Message> loadMoreMessages();
}
