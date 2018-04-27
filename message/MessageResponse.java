package com.taskforce.app.messaging.message;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taskforce.app.communication.network.rersponse.BaseResponse;



public class MessageResponse extends BaseResponse {

    @Expose()
    @SerializedName("Message")
    public TextMessage Message = new TextMessage();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.Message, flags);
    }

    public MessageResponse() {
    }

    protected MessageResponse(Parcel in) {
        super(in);
        this.Message = in.readParcelable(TextMessage.class.getClassLoader());
    }

    public static final Creator<MessageResponse> CREATOR = new Creator<MessageResponse>() {
        @Override
        public MessageResponse createFromParcel(Parcel source) {
            return new MessageResponse(source);
        }

        @Override
        public MessageResponse[] newArray(int size) {
            return new MessageResponse[size];
        }
    };
}
