package com.izk.rxjavaone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView tv_rx;
    private Disposable disposable;
    private String value1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_rx = findViewById(R.id.tv_rx);

    }

    public void onClick(View view) {
        //通过subscribe关联
        getObservable().subscribe(getObserver());
        tv_rx.setText(value1);//设置的是最后一个
    }

    //被观察者(我)
    public Observable<String> getObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("第一个");
                e.onNext("第2个");
                e.onNext("第3个");
                e.onComplete();
            }
        });
    }

    //观察者(女友)
    public Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                disposable = d;
            }

            @Override
            public void onNext(String value) {
                // onSubscribe（Disposable d）里面的Disposable对象要说一下，
                // Disposable英文意思是可随意使用的，这里就相当于读者和连载小说的订阅关系
                // 如果读者不想再订阅该小说了，可以调用 mDisposable.dispose()取消订阅
                // 此时连载小说更新的时候就不会再推送给读者了
//                if(value.contains("2")){
//                    disposable.dispose();
//                    return;
//                }

                value1 = value;
                Log.d(TAG, "onNext: " + value1);

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }


}
