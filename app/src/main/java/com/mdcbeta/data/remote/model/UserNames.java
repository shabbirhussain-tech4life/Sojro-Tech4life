package com.mdcbeta.data.remote.model;

/**
 * Created by Shakil Karim on 4/22/17.
 */

public class UserNames {

    public String id;
    public String name;
    public String email;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserNames(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
