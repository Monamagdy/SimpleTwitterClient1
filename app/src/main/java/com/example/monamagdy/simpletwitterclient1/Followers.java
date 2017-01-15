package com.example.monamagdy.simpletwitterclient1;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by monamagdy on 1/8/17.
 */
public class Followers {
    @SerializedName("users")
    public final List<User> users;

    public Followers(List<User> users) {
        this.users = users;
    }
}