package com.taskforce.app.messaging.message;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taskforce.app.communication.network.rersponse.User;
import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import static com.taskforce.app.utils.DateUtils.FORMAT_LONG_INPUT;
import static com.taskforce.app.utils.DateUtils.GMT;
import static com.taskforce.app.utils.DateUtils.stringtoDate;

/**
 * Model class to parse the json data
 * @param <VH>
 */
public abstract class Message<VH extends FlexibleViewHolder> extends AbstractFlexibleItem<VH> implements Parcelable, Serializable {

    public static final int SENDING = 0;
    public static final int SENT = 1;
    public static final int FAILED = 2;
    /**
     * id : 13
     * chat_id : 12
     * from_user : 179
     * to_user : 187
     * message : Hi Nayab how are you
     * unique_id : 1-23a
     * created_at : 2017-09-19 09:17:03
     * updated_at : 2017-09-19 09:17:03
     * created_by : 0
     * task_id : 3
     * User : {"name":"Irfan","last_name":"Arshad","id":"187","profile_picture":"assets/profileimage/1503661071180.jpeg"}
     */
    @Expose()
    @SerializedName("id")
    public long id;
    @Expose()
    @SerializedName("chat_id")
    public String chatId;
    @Expose()
    @SerializedName("from_user")
    public long fromUser;
    @Expose()
    @SerializedName("to_user")
    public long toUser;
    @Expose()
    @SerializedName("message")
    public String message;
    @Expose()
    @SerializedName("unique_id")
    public String uniqueId;
    @Expose()
    @SerializedName("created_at")
    public String createdAt;
    @Expose()
    @SerializedName("updated_at")
    public String updatedAt;
    @Expose()
    @SerializedName("created_by")
    public String createdBy;
    @Expose()
    @SerializedName("task_id")
    public long taskId;
    @Expose()
    @SerializedName("User")
    public User user = new User();
    @Expose()
    @SerializedName("receiver")
    public long receiver;
    @Expose()
    @SerializedName("task_title")
    public String taskTitle;

    MessageSource source;
    String initials;
    /**
     * 0-> Sending
     * 1->Sent
     * 2->Failed
     */
    @Expose
    public int status = 1;//default value is sent@Expose
    @Expose()
    @SerializedName("is_read")
    public int isRead;

    public long getDate() {
        Date date = stringtoDate(createdAt, FORMAT_LONG_INPUT, GMT);
        return date.getTime();
    }

    public void setDate(String date) {
        this.createdAt = date;
    }

    public MessageSource getSource() {
        return source;
    }

    public void setSource(MessageSource source) {
//        User user = AccountUtils.getUser();
//        if(user.)
        this.source = source;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getInitials() {
        return StringUtils.getInitials(user.name,user.lastName);
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public abstract MessageItem toMessageItem(Context context);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.chatId);
        dest.writeLong(this.fromUser);
        dest.writeLong(this.toUser);
        dest.writeString(this.message);
        dest.writeString(this.uniqueId);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.createdBy);
        dest.writeLong(this.taskId);
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.receiver);
        dest.writeInt(this.source == null ? -1 : this.source.ordinal());
        dest.writeString(this.initials);
        dest.writeString(this.taskTitle);
        dest.writeInt(this.status);
        dest.writeInt(this.isRead);
    }

    public Message() {
    }

    protected Message(Parcel in) {
        this.id = in.readLong();
        this.chatId = in.readString();
        this.fromUser = in.readLong();
        this.toUser = in.readLong();
        this.message = in.readString();
        this.uniqueId = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.createdBy = in.readString();
        this.taskId = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.receiver = in.readLong();
        int tmpSource = in.readInt();
        this.source = tmpSource == -1 ? null : MessageSource.values()[tmpSource];
        this.initials = in.readString();
        this.taskTitle = in.readString();
        this.status = in.readInt();
        this.isRead = in.readInt();
    }

    public boolean isRead() {
        return isRead == 1;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof Message) {
            Message inItem = (Message) inObject;
            return this.id == inItem.id;
        }
        return false;
    }

    /**
     * Override this method too, when using functionalities like StableIds, Filter or CollapseAll.
     * FlexibleAdapter is making use of HashSet to improve performance, especially in big list.
     */
    @Override
    public int hashCode() {
        return Long.toString(id).hashCode();
    }
}
