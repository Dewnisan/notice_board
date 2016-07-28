package com.example.eliavmenachi.simplelist.model;

import java.util.Date;

/**
 * Created by talni on 20/07/2016.
 */
public class Post implements Comparable<Post> {
    private String mId;
    private String mOwner;
    private String mGroup;
    private long mTimestamp;
    private String mMessage;
    private String mImageName;

    private String mLastUpdated;

    public Post(String id, String owner, String group, long timestamp, String message, String imageName) {
        this.mId = id;
        this.mOwner = owner;
        this.mGroup = group;
        this.mTimestamp = timestamp;
        this.mMessage = message;
        this.mImageName = imageName;

        this.mTimestamp = (new Date()).getTime();
    }

    public Post(String id, String owner, String group, String message, String imageName) {
        this.mId = id;
        this.mOwner = owner;
        this.mGroup = group;
        this.mMessage = message;
        this.mImageName = imageName;

        this.mTimestamp = (new Date()).getTime();
    }

    public Post() {
        this.mTimestamp = (new Date()).getTime();
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

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
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

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }

    @Override
    public int compareTo(Post post) {
        return (int) (this.getTimestamp() - post.getTimestamp());
    }
}
