package com.izk.rxjavaseven;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.izk.rxjavaseven.rximageloader.RxImgLoader;

public class ImgActivity extends AppCompatActivity {

    private TextView btn_click_two;
    private ImageView iv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        btn_click_two = findViewById(R.id.btn_click_two);
        iv_img = findViewById(R.id.iv_img);

        btn_click_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断是否连击
                if (isFastClick()) {
                    return;
                }

                RxImgLoader.with(ImgActivity.this).load("http://img5.mtime.cn/mt/2018/10/22/104316.77318635_180X260X4.jpg").into(iv_img);

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
