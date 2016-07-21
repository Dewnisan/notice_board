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
    private List<String> mMembers = new LinkedList<String>();
    private String mImageName;

    private List<String> mMessages = new LinkedList<String>();

    private String mLastUpdated;

    public Group(String id, String name, String owner, List<String> members, String imageName, List<String> messages) {
        this(id, name, owner, imageName);
        setMembers(members);
        setMessages(messages);
    }

    public Group(String id, String name, String owner, String imageName) {
        setId(id);
        setName(name);
        setOwner(owner);

        this.mMembers.add(mOwner);

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

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        this.mOwner = owner;
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

    public List<String> getMessages() {
        return mMessages;
    }

    public void setMessages(List<String> messages) {
        this.mMessages = messages;
    }

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }
}
