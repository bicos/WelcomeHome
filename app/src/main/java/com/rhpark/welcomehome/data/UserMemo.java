package com.rhpark.welcomehome.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rhpark on 2015. 9. 3..
 */
public class UserMemo extends UserContent implements Parcelable {

    private String memo;

    public UserMemo(String memo) {
        super(Constants.TYPE_MEMO);
        this.memo = memo;
    }

    protected UserMemo(Parcel in) {
        type = in.readInt();
        memo = in.readString();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public int describeContents() {
        return type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(memo);
    }

    public static final Creator<UserMemo> CREATOR = new Creator<UserMemo>() {
        @Override
        public UserMemo createFromParcel(Parcel in) {
            return new UserMemo(in);
        }

        @Override
        public UserMemo[] newArray(int size) {
            return new UserMemo[size];
        }
    };
}
