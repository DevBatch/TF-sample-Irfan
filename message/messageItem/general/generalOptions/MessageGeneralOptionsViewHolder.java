package com.taskforce.app.messaging.message.messageItem.general.generalOptions;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taskforce.app.R;
import com.taskforce.app.messaging.message.messageItem.MessageViewHolder;
import com.taskforce.app.messaging.utils.CustomSettings;

public class MessageGeneralOptionsViewHolder extends MessageViewHolder {
    public TextView titleTextView;
    public LinearLayout optionsLinearLayout;

    public MessageGeneralOptionsViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);

        this.titleTextView = itemView.findViewById(R.id.message_general_options_text_view);
        this.optionsLinearLayout = itemView.findViewById(R.id.message_general_options_options_linear_layout);
    }
}
