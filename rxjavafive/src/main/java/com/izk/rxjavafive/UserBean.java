package com.izk.rxjavafive;

/**
 * Created by Malong
 * on 19/1/10.
 */
public class UserBean {

    public UserBean(String reason) {
        this.reason = reason;
    }

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
