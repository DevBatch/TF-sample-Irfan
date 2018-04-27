package com.taskforce.app.messaging.message;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taskforce.app.communication.network.rersponse.BaseResponse;

import java.util.ArrayList;
import java.util.List;


public class MessageConversationResponse extends BaseResponse {

    @Expose()
    @SerializedName("List")
    public List<TextMessage> messages = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.messages);
    }

    public MessageConversationResponse() {
    }

    private MessageConversationResponse(Parcel in) {
        super(in);
        this.messages = in.createTypedArrayList(TextMessage.CREATOR);
    }

    public static final Creator<MessageConversationResponse> CREATOR = new Creator<MessageConversationResponse>() {
        @Override
        public MessageConversationResponse createFromParcel(Parcel source) {
            return new MessageConversationResponse(source);
        }

        @Override
        public MessageConversationResponse[] newArray(int size) {
            return new MessageConversationResponse[size];
        }
    };
}
