package com.izk.rxjava.observable;

/**
 * Created by Malong
 * on 18/12/27.
 */
public class GFObservable implements Observable {

    private String s;
    private String name;

    public GFObservable(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(String status) {
        System.out.println(this.name + "接收到最新消息: 我在" + status);
        s = this.name + "接收到最新消息: 我在 " + status;
    }

    public String getUpdateMsg() {
        return s;
    }
}
