package com.rhpark.welcomehome.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class User implements Parcelable {

    private final Object mLock = new Object();

    private List<UserContentImpl> contents;

    public User() {
        this.contents = new ArrayList<>();
    }

    public User(List<UserContentImpl> contents) {
        this.contents = contents;
    }


    public List<UserContentImpl> getContents() {
        return contents;
    }

    public UserContentImpl getContent(int position) {
        return contents.size() > position ? contents.get(position) : null;
    }

    public UserContentImpl getContentFromType(int typeVolumn) {
        for (UserContentImpl content : contents) {
            if (content.getType() == typeVolumn) {
                return content;
            }
        }
        return null;
    }

    public void addContent(UserContentImpl contents) {
        synchronized (mLock) {
            this.contents.add(contents);
        }
    }

    public void replaceContent(UserContentImpl content) {
        synchronized (mLock) {
            this.contents.set(this.contents.indexOf(content), content);
        }
    }

    protected User(Parcel in) {
        this.contents = in.readArrayList(UserContentImpl.class.getClassLoader());
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
