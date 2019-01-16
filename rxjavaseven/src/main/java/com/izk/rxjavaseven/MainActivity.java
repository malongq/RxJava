package com.izk.rxjavaseven;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

public class MainActivity extends AppCompatActivity {

    private TextView btn_click;
    private TextView btn_clicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_click = findViewById(R.id.btn_click);
        btn_clicks = findViewById(R.id.btn_clicks);


        //一级缓存
        final Observable<String> observable_memory = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                e.onNext("一级缓存");
                e.onComplete();
            }
        });

        //二级内存
        final Observable<String> observable_disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("二级内存");
                e.onComplete();
            }
        });

        //三级网络
        final Observable<String> observable_network = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("三级网络");
                e.onComplete();
            }
        });

        btn_clicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImgActivity.class);
                startActivity(intent);
            }
        });


        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断是否连击
                if (isFastClick()) {
                    return;
                }

                Observable
                        .concat(observable_memory, observable_disk, observable_network)//合并发射源

                        .throttleFirst(10000, TimeUnit.MILLISECONDS)

                        //filter / firstElement 与rxjava1 中的first()效果相同
                        .filter(new Predicate<String>() {//判断只要合并的发射源，第一个部位null,就不用执行后面的数据源
                            @Override
                            public boolean test(String s) throws Exception {
                                return !TextUtils.isEmpty(s);
                            }
                        })
                        .firstElement().toObservable()//本来这里还有一个 .publish(),但是加上就只走onSubscribe 去掉可以正常走后面

                        .subscribe(new Observer<String>() {//订阅
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d("rxjava_seven", "onSubscribe: 先走这个方法");
                            }

                            @Override
                            public void onNext(String value) {
                                Log.d("rxjava_seven", "onNext: " + value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.d("rxjava_seven", "onComplete: ");
                            }
                        });
            }
        });

    }


    private static final int MIN_DELAY_TIME = 2000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    //用于判断是否快速点击
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

}
