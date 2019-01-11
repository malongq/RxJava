package com.izk.rxjavatwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Retrofit retrofit;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv_show);

        //创建Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.map.baidu.com/")
                ////增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //将Retrofit实例与对应的api结合
        api = retrofit.create(Api.class);

    }

    /**
     * button点击事件
     * @param view
     */
    public void obClick(View view) {

        //被观察者
        Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {

                //请求网络
                User userInfo = api.getUserInfo("北京","JSON","FK9mkfdQsloEngodbFl4FeY3").execute().body();

                //将请求到的结果放入onNext函数执行
                e.onNext(userInfo);

                //执行完毕，与onError互斥
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())//将被观察者里面的请求放入IO（子线程）执行
                .observeOn(AndroidSchedulers.mainThread())//将观察者里的更新UI操作放到主线程执行
                //将被观察者发生改变通过该subscribe订阅方法告知观察者
                .subscribe(
                        //观察者，被动观察，被观察者有变化主动告诉观察者
                        new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //肯定是先走这个方法
                        Toast.makeText(MainActivity.this,"onSubscribe",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(User data) {
                        //接收被观察者中的onNext信息
                        textView.setText(data.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        //接收被观察者中的onError信息
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //接收被观察者中的onComplete信息
                        Toast.makeText(MainActivity.this,"onComplete",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void onClick(View view) {
        startActivity(new Intent(MainActivity.this,MapActivity.class));
    }
}
