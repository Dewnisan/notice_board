package com.example.eliavmenachi.simplelist.model;

/**
 * Created by talni on 20/07/2016.
 */
public class Post {
    private String mId;
    private String mOwner;
    private String mGroup;
    private String mMessage;
    private String mImageName;

    public Post(String id, String owner, String group, String message, String imageName) {
        this.mId = id;
        this.mOwner = owner;
        this.mGroup = group;
        this.mMessage = message;
        this.mImageName = imageName;
    }

    public Post() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        this.mOwner = owner;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        this.mGroup = group;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String imageName) {
        this.mImageName = imageName;
    }
}
