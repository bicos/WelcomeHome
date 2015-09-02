package com.rhpark.welcomehome.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class User implements Parcelable {

    private List<UserContent> contents;

    public User() {
        this.contents = new ArrayList<>();
    }

    public User(List<UserContent> contents) {
        this.contents = contents;
    }


    public List<UserContent> getContents() {
        return contents;
    }

    public void setContent(List<UserContent> contents) {
        this.contents = contents;
    }

    public UserContent getContent(int position) {
        return contents.size() > position ? contents.get(position) : null;
    }

    public void addContent(UserContent contents) {
        this.contents.add(contents);
    }

    protected User(Parcel in) {
        this.contents = in.readArrayList(UserContent.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(contents.toArray());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "contents=" + contents +
                '}';
    }
}
