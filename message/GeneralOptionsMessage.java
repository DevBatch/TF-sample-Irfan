package com.taskforce.app.messaging.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taskforce.app.messaging.listeners.OnOptionSelectedListener;
import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.general.generalOptions.MessageGeneralOptionsItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class GeneralOptionsMessage extends Message {
    private String title;
    private String[] options;
    private OnOptionSelectedListener onOptionSelectedListener;
    private boolean selected;
    private String finalText;

    public GeneralOptionsMessage() {
        this.selected = false;
    }

    public OnOptionSelectedListener getOnOptionSelectedListener() {
        return onOptionSelectedListener;
    }

    /**
     * set onOptionselected lister
     * @param onOptionSelectedListener
     */
    public void setOnOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener) {
        this.onOptionSelectedListener = onOptionSelectedListener;
    }

    /**
     *
     * @return no of option items
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * set option arrar
     * @param options
     */
    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public MessageItem toMessageItem(Context context) {
        return new MessageGeneralOptionsItem(this, context);
    }

    public void setSelected() {
        this.selected = true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setFinalText(String finalText) {
        this.finalText = finalText;
    }

    public String getFinalText() {
        return finalText;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return null;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RecyclerView.ViewHolder holder, int position, List payloads) {

    }
}
