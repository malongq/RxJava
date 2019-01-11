package com.izk.rxjavafive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btn_click;
    private RecyclerView recyclerView;
    private MlAdapter adapter;
    private List<UserBean> ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_click = findViewById(R.id.btn_click);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ll = new ArrayList<>();
        adapter = new MlAdapter(ll);

        /**
         * 优化问题：
         *
         * 注意：merge / concat / mergeWith 操作符都是会走两次，也就是合并几组数据源就会走几次onNext
         *      或者call方法，不能再改方法里面创建adapter或者setAdapter
         *
         */
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 可能无序
                 */
//                Observable.merge(getDataFromLocal(), getDataFromInterNet())
//                        .subscribe(new Observer<List<UserBean>>() {
//                            @Override
//                            public void onCompleted() {
//                                Log.d(TAG, "onCompleted: ");
//                                recyclerView.setAdapter(adapter);
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                e.printStackTrace();
//                            }
//
//                            @Override
//                            public void onNext(List<UserBean> userBeans) {
//                                Log.d(TAG, "onNext: 走几次");
//                                for (UserBean u : userBeans) {
//                                    Log.d(TAG, "onNext: " + u.getReason());
//                                    ll.add(u);
//                                }
//                            }
//                        });


                /**
                 * 有序
                 */
//                Observable.concat(getDataFromLocal(), getDataFromInterNet())
//                        .subscribe(new Observer<List<UserBean>>() {
//                            @Override
//                            public void onCompleted() {
//                                Log.d(TAG, "onCompleted: ");
//                                recyclerView.setAdapter(adapter);
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.d(TAG, "onError: start");
//                                e.printStackTrace();
//                                Log.d(TAG, "onError: end");
//                            }
//
//                            @Override
//                            public void onNext(List<UserBean> userBeans) {
//                                Log.d(TAG, "onNext: 走几次");
//                                for (UserBean u : userBeans) {
//                                    Log.d(TAG, "onNext: " + u.getReason());
//                                    ll.add(u);
//                                }
//                            }
//                        });


                //用Action1函数没有onCompleted方法,这样会造成资源浪费，不建议这么写
//                Observable.concat(getDataFromLocal(), getDataFromInterNet())
//                        .subscribe(new Action1<List<UserBean>>() {
//                            @Override
//                            public void call(List<UserBean> userBeans) {
//                                Log.d(TAG, "call: 走几次");
//                                for (UserBean u : userBeans) {
//                                    Log.d(TAG, "call: " + u.getReason());
//                                    ll.add(u);
//                                }
//                                recyclerView.setAdapter(adapter);
//                            }
//                        }, new Action1<Throwable>() {
//                            @Override
//                            public void call(Throwable throwable) {
//                                Log.d(TAG, "onError: start");
//                                throwable.printStackTrace();
//                                Log.d(TAG, "onError: end");
//                            }
//                        });



                getDataFromLocal().mergeWith(getDataFromInterNet()).subscribe(new Observer<List<UserBean>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: start");
                        e.printStackTrace();
                        Log.d(TAG, "onError: end");
                    }

                    @Override
                    public void onNext(List<UserBean> userBeans) {
                        Log.d(TAG, "onNext: 走几次");
                        for (UserBean u : userBeans) {
                            Log.d(TAG, "onNext: " + u.getReason());
                            ll.add(u);
                        }
                    }
                });

                //用Action1函数没有onCompleted方法,这样会造成资源浪费，不建议这么写
//                getDataFromLocal().mergeWith(getDataFromInterNet()).subscribe(new Action1<List<UserBean>>() {
//                    @Override
//                    public void call(List<UserBean> userBeans) {
//                        Log.d(TAG, "call: 走几次");
//                        for (UserBean u : userBeans) {
//                            Log.d(TAG, "call: " + u.getReason());
//                            ll.add(u);
//                        }
//                        recyclerView.setAdapter(adapter);
//                    }
//                });


            }

        });

        /**
         * 这个一进入页面就会运行，纳闷
         */
//        RxView.clicks(btn_click)
//                .throttleFirst(1000, TimeUnit.MILLISECONDS)
//                .merge(getDataFromLocal(),getDataFromInterNet())
//                .subscribe(new Observer<List<UserBean>>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted: ");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(List<UserBean> userBeans) {
//
//                        for (UserBean u : userBeans){
//                            Log.d(TAG, "onNext: "+u.getReason());
//                        }
//
//                        MlAdapter adapter = new MlAdapter(userBeans);
//                        recyclerView.setAdapter(adapter);
//                    }
//                });

    }

    //从本地获取数据
    private Observable<List<UserBean>> getDataFromLocal() {
        List<UserBean> list = new ArrayList<>();
        list.add(new UserBean("哈哈1"));
        list.add(new UserBean("哈哈2"));
        list.add(new UserBean("哈哈3"));
        return Observable.just(list);
    }

    //从网络获取数据
    private Observable<List<UserBean>> getDataFromInterNet() {
        List<UserBean> list = new ArrayList<>();
        list.add(new UserBean("嘿嘿1"));
        list.add(new UserBean("嘿嘿2"));
        list.add(new UserBean("嘿嘿3"));
        return Observable.just(list);
    }

}
