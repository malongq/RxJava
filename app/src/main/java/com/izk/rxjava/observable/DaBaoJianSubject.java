package com.izk.rxjava.observable;

/**
 * Created by Malong
 * on 18/12/27.
 */
public class DaBaoJianSubject extends Subject {

    public void change(String status){
        notifyObservable(status);
    }

}
