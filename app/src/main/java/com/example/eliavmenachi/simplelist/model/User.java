package com.example.eliavmenachi.simplelist.model;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class User {
    String mId;
    String mName;
    String mImageName;

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

    public String getName() {
        return mName;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setImageName(String imageName) {
        this.mImageName = imageName;
    }
}
