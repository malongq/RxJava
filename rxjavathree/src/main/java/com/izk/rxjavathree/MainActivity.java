package com.izk.rxjavathree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText et_serach = findViewById(R.id.et_serach);
        lv = findViewById(R.id.lv);


        /**
         * 传统写法--这种写法的问题有两个：
         */
        // 1.每次输入结束都会请求接口，浪费流量和资源
        // 2.由于返回有误差，比如：先输入“ab”,然后又输入“cd”,用户想要“cd”结果，
        // 但是由于返回先后不确定，所以会给用户造成体验差错觉
        et_serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                search(s.toString().trim());
            }
        });


        /**
         * rxbinding+rxjava写法--这种写法简单
         */
        RxTextView.textChanges(et_serach)//监听EditText输入，返回Observable<CharSequence>对象

                .debounce(500, TimeUnit.MILLISECONDS)//rxjava操作符--每隔（第一个参数XX时间，第二个参数表示单位）固定时间单位去请求一次网络

                .subscribeOn(AndroidSchedulers.mainThread())//由于下面在flatMap后有subscribeOn(Schedulers.io())//切换到io现成请求数据,所以在输入的时候也就放入io线程了，这里改方法是输入端额时候切换到UI线程

                //rxjava过滤操作符，没有输入内容的时候，不请求网络
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        Log.d("filter", charSequence.toString().trim().length()+"");
                        return charSequence.toString().trim().length() > 0;
                    }
                })

                //rxjava操作符--将输入的词组当做参数传入再去请求服务端，并转换为返回后的结果
                //-- 2.0后叫 new Observables
                //但是用这个方法不会解决这个问题--2.由于返回有误差，比如：先输入“ab”,然后又输入“cd”,用户想要“cd”结果，但是由于返回先后不确定，所以会给用户造成体验差错觉
                //所以使用另一个rxjava操作符--switchMap
//                .flatMap(new Func1<CharSequence, Observable<List<String>>>() {
//                    @Override
//                    public Observable<List<String>> call(CharSequence charSequence) {
//
//                        //网络操作，这里模拟数据
//                        //search(charSequence.toString().trim());
//
//                        List<String> list = new ArrayList<>();
//                        list.add("你好");
//                        list.add("你好a");
//                        list.add("你好b");
//                        list.add("你好c");
//                        list.add("你好d");
//
//                        Log.d("flatMap", "list: "+list);
//                        return Observable.just(list);//这里用 Observable. 因为返回的还是一个Observable对象
//                    }
//                })

                //rxjava操作符--将输入的最后的词组去请求服务端，并转换为返回后的结果
                .switchMap(new Func1<CharSequence, Observable<List<String>>>() {
                    @Override
                    public Observable<List<String>> call(CharSequence charSequence) {
                        //网络操作，这里模拟数据
                        //search(charSequence.toString().trim());

                        List<String> list = new ArrayList<>();
                        list.add("你好");
                        list.add("你好a");
                        list.add("你好b");
                        list.add("你好c");
                        list.add("你好d");

                        Log.d("switchMap", "list: "+list);
                        return Observable.just(list);//这里用 Observable. 因为返回的还是一个Observable对象
                    }
                })

                .subscribeOn(Schedulers.io())//切换到io现成请求数据
                .observeOn(AndroidSchedulers.mainThread())//切换到UI现成展示数据

                //接收返回数据，展示到页面 -- 2.0后叫 new consunmer
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(final List<String> strings) {
                        Log.d("subscribe", "call: "+strings);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,strings);
                        lv.setAdapter(adapter);
                    }
                }, new Action1<Throwable>() {//出错后的处理
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();//输出错误原因
                    }
                });

    }

    //填入关键词后开始搜索
    private void search(String trim) {
        //网络请求操作
        //...
    }

}
