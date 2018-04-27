package com.taskforce.app.messaging.message.messageItem.spinner;

import android.view.View;
import android.widget.ProgressBar;

import com.taskforce.app.R;
import com.taskforce.app.messaging.message.messageItem.MessageViewHolder;
import com.taskforce.app.messaging.utils.CustomSettings;

public class SpinnerViewHolder extends MessageViewHolder {
    public ProgressBar spinner;

    public SpinnerViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);
        this.spinner = itemView.findViewById(R.id.loading_bar);
//        this.spinner.getIndeterminateDrawable().setColorFilter(R.color.textColorsPrimary, PorterDuff.Mode.MULTIPLY);
    }
}