package com.izk.rxjavatwo;

import com.google.gson.Gson;

/**
 * Created by Malong
 * on 18/12/27.
 */
class User {

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new Gson().toString();
    }

}
