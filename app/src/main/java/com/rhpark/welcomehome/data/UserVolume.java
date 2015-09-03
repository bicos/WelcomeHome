package com.rhpark.welcomehome.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rhpark on 2015. 9. 3..
 */
public class UserVolume extends UserContent implements Parcelable {

    private int indoorMediaVolume;
    private int indoorRingVolume;
    private int outdoorMediaVolume;
    private int outdoorRingVolume;

    public UserVolume(int indoorMediaVolumn, int indoorRingVolumn, int outdoorMediaVolumn, int outdoorRingVolumn) {
        super(Constants.TYPE_VOLUMN);
        this.indoorMediaVolume = indoorMediaVolumn;
        this.indoorRingVolume = indoorRingVolumn;
        this.outdoorMediaVolume = outdoorMediaVolumn;
        this.outdoorRingVolume = outdoorRingVolumn;
    }

    public int getIndoorMediaVolume() {
        return indoorMediaVolume;
    }

    public void setIndoorMediaVolume(int indoorMediaVolume) {
        this.indoorMediaVolume = indoorMediaVolume;
    }

    public int getIndoorRingVolume() {
        return indoorRingVolume;
    }

    public void setIndoorRingVolume(int indoorRingVolume) {
        this.indoorRingVolume = indoorRingVolume;
    }

    public int getOutdoorMediaVolume() {
        return outdoorMediaVolume;
    }

    public void setOutdoorMediaVolume(int outdoorMediaVolume) {
        this.outdoorMediaVolume = outdoorMediaVolume;
    }

    public int getOutdoorRingVolume() {
        return outdoorRingVolume;
    }

    public void setOutdoorRingVolume(int outdoorRingVolume) {
        this.outdoorRingVolume = outdoorRingVolume;
    }

    @Override
    public int describeContents() {
        return type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(indoorMediaVolume);
        dest.writeInt(indoorRingVolume);
        dest.writeInt(outdoorMediaVolume);
        dest.writeInt(outdoorRingVolume);
    }

    protected UserVolume(Parcel in) {
        type = in.readInt();
        indoorMediaVolume = in.readInt();
        indoorRingVolume = in.readInt();
        outdoorMediaVolume = in.readInt();
        outdoorRingVolume = in.readInt();
    }

    public static final Creator<UserVolume> CREATOR = new Creator<UserVolume>() {
        @Override
        public UserVolume createFromParcel(Parcel in) {
            return new UserVolume(in);
        }

        @Override
        public UserVolume[] newArray(int size) {
            return new UserVolume[size];
        }
    };

    @Override
    public String toString() {
        return "UserVolume{" +
                "type=" + type +
                ", indoorMediaVolume=" + indoorMediaVolume +
                ", indoorRingVolume=" + indoorRingVolume +
                ", outdoorMediaVolume=" + outdoorMediaVolume +
                ", outdoorRingVolume=" + outdoorRingVolume +
                '}';
    }
}
