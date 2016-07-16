package com.example.eliavmenachi.simplelist.model;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class User {
    String id;
    String name;

    public User() {

    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
