package com.example.eliavmenachi.simplelist.model;

import java.util.LinkedList;
import java.util.List;

public class Group {
    private String mId;
    private String mName;
    private List<String> mMembers = new LinkedList<String>();
    private String mImageName;

    private String mLastUpdated;

    public Group(String id, String name, List<String> members, String imageName) {
        this(id, name, imageName);
        setMembers(members);
    }

    public Group(String id, String name, String imageName) {
        setId(id);
        setName(name);
        setImageName(imageName);
    }

    public Group() {
        this.mMembers = new LinkedList<String>();
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

    public List<String> getMembers() {
        return mMembers;
    }

    public void setMembers(List<String> members) {
        this.mMembers = members;
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
