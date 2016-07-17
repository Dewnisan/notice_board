package com.example.eliavmenachi.simplelist.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class Group {
    private String mId;
    private String mName;
    private String mOwner;
    private List<String> mMembers;
    private String mImageName;

    private String mLastUpdated;

    public Group(String id, String name, String owner, List<String> members, String imageName) {
        this(id, name, owner, imageName);
        setMembers(members);
    }

    public Group(String id, String name, String owner, String imageName) {
        setId(id);
        setName(name);
        setOwner(owner);

        this.mMembers = new LinkedList<String>();
        this.mMembers.add(mOwner);

        setImageName(imageName);
    }

    public Group() {
        this.mMembers = new LinkedList<String>();
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getOwner() {
        return mOwner;
    }

    public List<String> getMembers() {
        return mMembers;
    }

    public String getImageName() {
        return mImageName;
    }

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setOwner(String owner) {
        this.mOwner = owner;
    }

    public void setMembers(List<String> members) {
        this.mMembers = members;
    }

    public void setImageName(String imageName) {
        this.mImageName = imageName;
    }

    public void setLastUpdated(String lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }
}
