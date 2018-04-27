package com.taskforce.app.messaging.listeners;

import android.net.Uri;


public interface UserSendsMessageListener {
    void onUserSendsTextMessage(String text);
    void onUserSendsMediaMessage(Uri imageUri);
}
