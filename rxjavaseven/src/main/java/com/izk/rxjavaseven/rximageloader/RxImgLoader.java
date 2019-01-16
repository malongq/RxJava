package com.izk.rxjavaseven.rximageloader;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * Created by Malong
 * on 19/1/16.
 * 图片三级缓存主类
 */
public class RxImgLoader {

    static RxImgLoader singleton;
    private String mUrl;
    private RequestCreator creator;


    //创建构造函数
    private RxImgLoader(Builder builder) {
        creator = new RequestCreator(builder.mContext);
    }


    //模仿市面上图片加载框架结构，写出 .with()方法
    public static RxImgLoader with(Context context) {
        //同步判断--仿照Picasso代码写
        if (singleton == null) {
            synchronized (RxImgLoader.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }


    //创建with函数里需要的Builder内部类
    public static class Builder {

        private Context mContext;

        //所需上下文
        public Builder(Context context) {
            this.mContext = context;
        }

        //.build方法返回的是该主类本身
        public RxImgLoader build() {
            return new RxImgLoader(this);
        }
    }


    //模仿写出load函数，返回的是该主类本身
    public RxImgLoader load(String url) {
        this.mUrl = url;
        return singleton;
    }


    //模仿写出into函数，主要的三级缓存在这里
    public void into(final ImageView imageView) {

        //concat操作符写三级缓存，顺醋不能错，一次从 内存，文件，网络查找，只要有就直接返回，后续不会继续查找
        Observable.concat(creator.getImageFromMeory(mUrl), creator.getImageFromDisk(mUrl), creator.getImageFromNetwork(mUrl))
                .filter(new Predicate<ImageBean>() {//过滤操作符，只要有就直接返回，后续不会继续查找
                    @Override
                    public boolean test(ImageBean image) throws Exception {
                        return image != null;
                    }
                }).firstElement().toObservable()//过滤操作符，只要有就直接返回，后续不会继续查找
                .subscribe(new Observer<ImageBean>() {//订阅
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("RxImgLoader", "onSubscribe: 先走这个方法");
                    }

                    @Override
                    public void onNext(ImageBean value) {
                        imageView.setImageBitmap(value.getBitmap());//设置
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RxImgLoader", "onComplete: ");
                    }
                });

    }


}
