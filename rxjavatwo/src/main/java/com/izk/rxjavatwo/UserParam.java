package com.izk.rxjavatwo;

import com.google.gson.Gson;

/**
 * Created by Malong
 * on 18/12/28.
 */
public class UserParam {

    private String username;
    private String password;

    public UserParam(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new Gson().toString();
    }

}
