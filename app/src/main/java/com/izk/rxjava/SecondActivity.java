package com.izk.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.izk.rxjava.observable.DaBaoJianSubject;
import com.izk.rxjava.observable.GFObservable;
import com.izk.rxjava.observable.ZuLiaoSubject;

/**
 * Created by Malong
 * on 18/12/27.
 */
public class SecondActivity extends AppCompatActivity{

    private LinearLayout tv_one;
    private LinearLayout tv_two;
    private String msg1;
    private String msg2;
    private String msg3;
    private String updateMsg1;
    private String updateMsg2;
    private String msg4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        tv_one = findViewById(R.id.tv_one);
        tv_two = findViewById(R.id.tv_two);


        GFObservable gf1 = new GFObservable("高圆圆");
        GFObservable gf2 = new GFObservable("范冰冰");
        GFObservable gf3 = new GFObservable("杨超越");

        DaBaoJianSubject dabaojian = new DaBaoJianSubject();


        dabaojian.addAtach(gf1);
        msg1 = dabaojian.getaddAtachMsg();


        dabaojian.addAtach(gf2);
        msg2 = dabaojian.getaddAtachMsg();

        dabaojian.delAtach(gf3);
        msg3 = dabaojian.getdelAtachMsg();

        dabaojian.change("大宝剑");


        updateMsg1 = gf1.getUpdateMsg();
        updateMsg2 = gf2.getUpdateMsg();

        ZuLiaoSubject zuLiao = new ZuLiaoSubject();
        zuLiao.delAtach(gf1);
        msg4 = zuLiao.getdelAtachMsg();
        zuLiao.change("足疗");

        initView();
    }

    private void initView() {

        if(!TextUtils.isEmpty(msg1)){
            TextView textView = new TextView(this);
            textView.setText(msg1);
            tv_one.addView(textView);
        }

        if(!TextUtils.isEmpty(msg2)){
            TextView textView = new TextView(this);
            textView.setText(msg2);
            tv_one.addView(textView);
        }

        if(!TextUtils.isEmpty(updateMsg1)){
            TextView textView = new TextView(this);
            textView.setText(updateMsg1);
            tv_two.addView(textView);
        }

        if(!TextUtils.isEmpty(updateMsg2)){
            TextView textView = new TextView(this);
            textView.setText(updateMsg2);
            tv_two.addView(textView);
        }

        if(!TextUtils.isEmpty(msg3)){
            TextView textView = new TextView(this);
            textView.setText(msg3);
            tv_two.addView(textView);
        }

        if(!TextUtils.isEmpty(msg4)){
            TextView textView = new TextView(this);
            textView.setText(msg4);
            tv_two.addView(textView);
        }

    }

}
