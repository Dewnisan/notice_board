package com.example.eliavmenachi.simplelist.model;

/**
 * Created by talni on 20/07/2016.
 */
public class Post implements Comparable<Post> {
    private String mId;
    private String mOwner;
    private String mGroup;
    private String mMessage;
    private String mImageName;

    private String mLastUpdated;

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

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }

    @Override
    public int compareTo(Post post) {
        return this.getLastUpdated().compareTo(post.getLastUpdated());
    }
}
