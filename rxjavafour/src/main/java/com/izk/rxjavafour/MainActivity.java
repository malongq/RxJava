package com.izk.rxjavafour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 防止用户重复点击按钮--rxjava
 */
public class MainActivity extends AppCompatActivity {

    private Button btn_click;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_click = findViewById(R.id.btn_click);
        tv_show = findViewById(R.id.tv_show);


        //第一种
//        RxView.clicks(btn_click)
//                .debounce(500, TimeUnit.MILLISECONDS)//rxjava操作符--每隔（第一个参数XX时间，第二个参数表示单位）固定时间单位去请求一次网络
//                //订阅
//                .subscribe(new Observer<Void>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Void aVoid) {
//                        Log.d("aaa", "onNext: " + System.currentTimeMillis());
//                    }
//                });


        //第二种
        RxView.clicks(btn_click)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)//防止按钮重复点击
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Log.d("aaa", "onNext: " + System.currentTimeMillis());
                        tv_show.setText("变变变： "+System.currentTimeMillis());
                    }
                });


    }
}
