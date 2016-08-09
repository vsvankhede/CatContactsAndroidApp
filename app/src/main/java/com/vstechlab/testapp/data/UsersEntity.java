package com.vstechlab.testapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UsersEntity {
    @SerializedName("users")
    @Expose
    private List<User> users = new ArrayList<User>();

    /**
     *
     * @return
     * The users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     *
     * @param users
     * The users
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

}
