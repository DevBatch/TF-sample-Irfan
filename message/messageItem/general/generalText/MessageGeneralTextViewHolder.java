package com.taskforce.app.messaging.message.messageItem.general.generalText;

import android.view.View;
import android.widget.TextView;

import com.taskforce.app.R;
import com.taskforce.app.messaging.message.messageItem.MessageViewHolder;
import com.taskforce.app.messaging.utils.CustomSettings;

public class MessageGeneralTextViewHolder extends MessageViewHolder {
    public TextView messageTextView;

    public MessageGeneralTextViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);

        messageTextView = itemView.findViewById(R.id.message_general_text_text_view);
        messageTextView.setTextColor(customSettings.timestampColor);
    }
}
