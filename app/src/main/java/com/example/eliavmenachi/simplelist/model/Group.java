package com.example.eliavmenachi.simplelist.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class Group {
    private String mName;
    private String mOwner;

    private List<User> mMembers;

    public Group(String name, String owner) {
        this.mName = name;
        this.mOwner = owner;

        this.mMembers = new LinkedList<User>();
    }

    public String getName() {
        return mName;
    }

    public String getOwner() {
        return mOwner;
    }
}
