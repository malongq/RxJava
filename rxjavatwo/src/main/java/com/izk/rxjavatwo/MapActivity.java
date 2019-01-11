package com.izk.rxjavatwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * map操作符可以将发射的数据类型转换成想要的数据类型
 * flatMap操作符可以将发射数据的observable变换为多个Observable,然后将他们发射的数据合并后放进一个单独的Observable
 */

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    private TextView tv_map;
    private Api api1;
    private Api api2;
    Retrofit retrofit1;
    Retrofit retrofit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        tv_map = findViewById(R.id.tv_map);

        //创建Retrofit
        retrofit1 = new Retrofit.Builder().baseUrl("http://apis.juhe.cn/mobile/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //将Retrofit实例与对应的api结合
        api1 = retrofit1.create(Api.class);


        //创建Retrofit
        retrofit2 = new Retrofit.Builder()
                .baseUrl("http://api.map.baidu.com/")
                ////增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //将Retrofit实例与对应的api结合
        api2 = retrofit2.create(Api.class);

    }

    //获取用户账号密码
    private UserParam getUsrParam() {
        UserParam userParam = new UserParam("17600298586", "53571dcc6966b339045157ea379754e7");
        return userParam;
    }

    //点击事件
    public void obClick(View view) {

        //flatMap操作符可以将发射数据的observable变换为多个Observable,然后将他们发射的数据合并后放进一个单独的Observable
        Observable.just(getUsrParam())
                .flatMap(new Function<UserParam, ObservableSource<BaseResult>>() {
                    @Override
                    public ObservableSource<BaseResult> apply(UserParam userParam) throws Exception {

                        BaseResult baseResult = api1.login("17600298586", "53571dcc6966b339045157ea379754e7").execute().body();

                        Log.d(TAG, baseResult.toString());

                        return Observable.just(baseResult);
                    }
                }).flatMap(new Function<BaseResult, ObservableSource<User>>() {
            @Override
            public ObservableSource<User> apply(BaseResult baseResult) throws Exception {

                Log.d(TAG, "你的电话归属地" + baseResult.getResult().getCity() + ":" + baseResult.getResult().getCompany());

                User user = api2.getUserInfo("北京", "JSON", "FK9mkfdQsloEngodbFl4FeY3").execute().body();

                return Observable.just(user);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        tv_map.setText(user.getMessage());
                    }
                });



        //map操作符可以将发射的数据类型转换成想要的数据类型
//        Observable observable = Observable.just(1);
//
//        observable.subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                Log.d("accept", "accept: ");
//            }
//        });

//
//        Observable map = observable.map(new Function<Integer,String>() {
//            @Override
//            public String apply(Integer integer) throws Exception {
//                return 1+"";
//            }
//        });
//
//        map.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String o) throws Exception {
//                Log.d("accept", "accept: ");
//            }
//        });

        //第一种写法
//        Observable.just(1).map(new Function<Integer, String>() {
//            @Override
//            public String apply(Integer integer) throws Exception {
//                return 1+"aaa";
//            }
//        }).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d("accept", s);
//            }
//        });

        //第二种写法
//        try {
//            Observable.create(new ObservableOnSubscribe<Integer>() {
//                @Override
//                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                    e.onNext(1);
//                }
//            }).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
//                @Override
//                public void onSubscribe(Disposable d) {
//
//                }
//
//                @Override
//                public void onNext(Integer value) {
//                    Log.d("value:  ", value+"");
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
