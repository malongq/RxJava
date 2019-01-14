package com.izk.rxjavasix;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * rxjava操作符 倒计时使用
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.et_phone);
        button = findViewById(R.id.btn_send);

        button.setOnClickListener(this);
        button.setTextColor(Color.BLACK);
        button.setBackgroundColor(Color.GRAY);
        button.setEnabled(true);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_send:

                String s = editText.getText().toString().trim();
                if (TextUtils.isEmpty(s)) {
                    Toast.makeText(MainActivity.this, "没有输入信息,请先输入号码！", Toast.LENGTH_SHORT).show();
                } else {
                    final long count = 10;
                    Observable.interval(0, 1000, TimeUnit.MILLISECONDS)//每隔 1s 执行一次任务，立即执行第一次任务，执行无限次

                            .map(new Function<Long, Long>() {//转换操作符，把正计时变成倒计时
                                @Override
                                public Long apply(Long aLong) throws Exception {
                                    return count - aLong;
                                }
                            })


                            .take(count + 1)//带上take操作符，可以控制执行的次数

                            .observeOn(AndroidSchedulers.mainThread())//切换到UI线程变化UI

                            .doOnSubscribe(new Consumer<Disposable>() {//在倒计时期间不可点击
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    button.setEnabled(false);
                                }
                            })

                            .subscribe(new Observer<Long>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    Log.d(TAG, "onSubscribe: Disposable");
                                }

                                @Override
                                public void onNext(Long value) {
                                    Log.d(TAG, "onNext: " + value);
                                    button.setTextColor(Color.WHITE);
                                    button.setBackgroundColor(Color.RED);
                                    button.setText("倒计时 " + value + " 秒");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onComplete() {
                                    Log.d(TAG, "onComplete: ");
                                    button.setEnabled(true);
                                    button.setTextColor(Color.BLACK);
                                    button.setBackgroundColor(Color.GRAY);
                                    button.setText("发送验证码");
                                }
                            });
                }

                break;
        }

    }

}
