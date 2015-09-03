package com.rhpark.welcomehome.data;

/**
 * Created by 래형 on 2015-09-04.
 */
public class UserContent implements UserContentImpl {
    protected int type;

    public UserContent() {
    }

    public UserContent(int type) {
        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Object getContents() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserContent) {
            return ((UserContent) o).getType() == this.getType();
        }
        return super.equals(o);
    }
}
