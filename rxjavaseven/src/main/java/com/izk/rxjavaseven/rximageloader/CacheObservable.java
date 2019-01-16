package com.izk.rxjavaseven.rximageloader;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Malong
 * on 19/1/16.
 * 抽象类，主要就是让三个途径的缓存继承，其中有主要的方法，
 */
public abstract class CacheObservable {


    //获取图片
    public Observable<ImageBean> getImage(final String url) {

        return Observable.create(new ObservableOnSubscribe<ImageBean>() {
            @Override
            public void subscribe(ObservableEmitter<ImageBean> e) throws Exception {

                ImageBean image = getDataFromCache(url);
                Log.d("CacheObservable", "image: " + image);
                e.onNext(image);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }


    //从缓存获取图片
    public abstract ImageBean getDataFromCache(String url);


    //将图片放入缓存
    public abstract void putDataToCache(ImageBean image);


}
