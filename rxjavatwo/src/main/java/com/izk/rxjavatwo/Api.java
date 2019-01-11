package com.izk.rxjavatwo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Malong
 * on 18/12/27.
 */
public interface Api {

    //Mainactivity的
    @GET("/telematics/v3/weather")
    Call<User> getUserInfo(@Query("location") String location, @Query("output") String ouput, @Query("ak") String ak);

    //mapActivity的
    @POST("get")
    Call<BaseResult> login(@Query("phone") String phone, @Query("key") String key);

}
