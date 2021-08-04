package com.mdcbeta.data.model;

/**
 * Created by Shakil Karim on 5/16/17.
 */

public class GroupAndUser {

    public String id;
    public String type;
    public String name;
    public String email;

    public GroupAndUser(String id, String type, String name, String email) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return name;
    }
}
