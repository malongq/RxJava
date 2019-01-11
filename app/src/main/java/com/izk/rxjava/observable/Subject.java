package com.izk.rxjava.observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malong
 * on 18/12/27.
 */
public abstract class Subject {

    private List<Observable> observables = new ArrayList<>();
    private String s1;
    private String s2;

    public void addAtach(Observable observable) {
        observables.add(observable);
        System.out.println("我新交的女朋友,叫：" + observable.getName());
        s1 = "我新交的女朋友,叫：" + observable.getName();
    }

    public void delAtach(Observable observable) {
        observables.remove(observable);
        System.out.println("我和"+observable.getName()+"分手了");
        s2 = "我和"+observable.getName()+"分手了";

    }

    public void notifyObservable(String status) {
        for (Observable observable : observables) {
            observable.update(status);
        }
    }

    public String getaddAtachMsg() {
        return s1;
    }

    public String getdelAtachMsg() {
        return s2;
    }

}
