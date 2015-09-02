package com.rhpark.welcomehome.data;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class UserHomeMap implements UserContent, Parcelable {

    private int type = Constants.TYPE_HOME_MAP;
    private String homeImgPath;
    private String homeImgDesc;
    private LatLng homeLatLng;

    public UserHomeMap(String homeImgPath, String homeImgDesc) {
        this.homeImgPath = homeImgPath;
        this.homeImgDesc = homeImgDesc;
    }

    public String getHomeImgPath() {
        return homeImgPath;
    }

    public void setHomeImgPath(String homeImgPath) {
        this.homeImgPath = homeImgPath;
    }

    public String getHomeImgDesc() {
        return homeImgDesc;
    }

    public void setHomeImgDesc(String homeImgDesc) {
        this.homeImgDesc = homeImgDesc;
    }

    public LatLng getHomeLatLng() {
        return homeLatLng;
    }

    public Location getHomeLocation(){
        if (homeLatLng != null) {
            Location location = new Location("home_location");
            location.setLatitude(homeLatLng.latitude);
            location.setLongitude(homeLatLng.longitude);
            return location;
        } else {
            return null;
        }
    }

    public void setHomeLatLng(LatLng homeLatLng) {
        this.homeLatLng = homeLatLng;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Object getContents() {
        return this;
    }

    protected UserHomeMap(Parcel in) {
        type = in.readInt();
        homeImgPath = in.readString();
        homeImgDesc = in.readString();
        homeLatLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(homeImgPath);
        dest.writeString(homeImgDesc);
        dest.writeParcelable(homeLatLng, flags);
    }

    public static final Creator<UserHomeMap> CREATOR = new Creator<UserHomeMap>() {
        @Override
        public UserHomeMap createFromParcel(Parcel in) {
            return new UserHomeMap(in);
        }

        @Override
        public UserHomeMap[] newArray(int size) {
            return new UserHomeMap[size];
        }
    };

    @Override
    public int describeContents() {
        return type;
    }

    @Override
    public String toString() {
        return "UserHomeMap{" +
                "type=" + type +
                ", homeImgPath='" + homeImgPath + '\'' +
                ", homeImgDesc='" + homeImgDesc + '\'' +
                ", homeLatLng=" + homeLatLng +
                '}';
    }
}
