package com.example.eliavmenachi.simplelist.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class Group {
    private String mName;
    private String mOwner;

    private List<String> mMembers;

    public Group(String name, String owner, List<String> mMembers) {
        this(name, owner);
        setMembers(mMembers);
    }

    public Group(String name, String owner) {
        setName(name);
        setOwner(owner);

        this.mMembers = new LinkedList<String>();
        this.mMembers.add(mOwner);
    }

    public Group() {
        this.mMembers = new LinkedList<String>();
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

    public void setName(String name) {
        this.mName = name;
    }

    public void setOwner(String owner) {
        this.mOwner = owner;
    }

    public void setMembers(List<String> members) {
        this.mMembers = members;
    }
}
