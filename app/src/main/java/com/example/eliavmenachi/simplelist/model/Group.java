package com.example.eliavmenachi.simplelist.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Group {
    private String mId;
    private String mName;
    private Map<String, String> mMembers = new HashMap<String, String>();
    private String mImageName;

    private String mLastUpdated;

    public Group(String id, String name, String imageName) {
        setId(id);
        setName(name);
        setImageName(imageName);
    }

    public Group() {
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

    public Map<String, String> getMembers() {
        return mMembers;
    }

    public void setMembers(Map<String, String> members) {
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

    public void addMember(String memberId) {
        mMembers.put(memberId, memberId);
    }

    public void addMembers(Collection<String> members) {
        for (String member : members) {
            addMember(member);
        }
    }

    public void removeMember(String memberId) {
        mMembers.remove(memberId);
    }
}
