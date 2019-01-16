package com.izk.rxjavaseven.rximageloader;

import android.content.Context;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by Malong
 * on 19/1/16.
 * 获取图片的操作
 */
public class RequestCreator {

    private MemoryObservable memoryObservable;
    private DiskObservable diskObservable;
    private NetworkObservable networkObservable;

    public RequestCreator(Context context) {
        memoryObservable = new MemoryObservable();
        diskObservable = new DiskObservable(context);
        networkObservable = new NetworkObservable();
    }


    //从缓存请求图片
    public Observable<ImageBean> getImageFromMeory(String url) {
        return memoryObservable.getImage(url)
                .filter(new Predicate<ImageBean>() {
                    @Override
                    public boolean test(ImageBean image) throws Exception {
                        return image.getBitmap() != null;
                    }
                })
                .doOnNext(new Consumer<ImageBean>() {
                    @Override
                    public void accept(ImageBean image) throws Exception {
                        Log.d("RequestCreator", "get data from memory");
                    }
                });
    }


    //从文件请求图片
    public Observable<ImageBean> getImageFromDisk(String url) {
        return diskObservable.getImage(url)
                .filter(new Predicate<ImageBean>() {
                    @Override
                    public boolean test(ImageBean image) throws Exception {
                        return image.getBitmap() != null;
                    }
                })
                //文件请求到图片后，保存到内存：doOnNext表示在调用next方法前，先调用这个方法
                .doOnNext(new Consumer<ImageBean>() {
                    @Override
                    public void accept(ImageBean image) throws Exception {
                        Log.d("RequestCreator", "get data from disk");
                        memoryObservable.putDataToCache(image);
                    }
                });
    }


    //从网络请求图片
    public Observable<ImageBean> getImageFromNetwork(String url) {
        return networkObservable.getImage(url)

                .filter(new Predicate<ImageBean>() {
                    @Override
                    public boolean test(ImageBean image) throws Exception {
                        return image.getBitmap() != null;
                    }
                })
                //网络请求到图片后，保存到文件和内存：doOnNext表示在调用next方法前，先调用这个方法
                .doOnNext(new Consumer<ImageBean>() {
                    @Override
                    public void accept(ImageBean image) throws Exception {
                        Log.d("RequestCreator", "get data from network");
                        diskObservable.putDataToCache(image);
                        memoryObservable.putDataToCache(image);
                    }
                });
    }


}
