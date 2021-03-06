package com.example.eliavmenachi.simplelist.model;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class User {
    private String mId;
    private String mName;
    private String mImageName;

    private String mLastUpdated;

    public User(String id, String name, String imageName) {
        this(name, imageName);
        setId(id);
    }

    public User(String name, String imageName) {
        setName(name);
        setImageName(imageName);
    }

    public User() {

    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
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
}
