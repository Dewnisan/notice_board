package com.example.eliavmenachi.simplelist.model;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class User {
    private String mId;
    private String mName;
    private String mImageName;

    public User(String id, String mName, String imageName) {
        setId(id);
        setName(mName);
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
}
