package com.micste.improveme;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by micste on 2017-08-30.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;

    public User() {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
