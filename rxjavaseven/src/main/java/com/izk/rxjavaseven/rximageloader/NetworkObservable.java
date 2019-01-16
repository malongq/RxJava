package com.izk.rxjavaseven.rximageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Malong
 * on 19/1/16.
 * 网络
 */
public class NetworkObservable extends CacheObservable {

    @Override
    public ImageBean getDataFromCache(String url) {
        Bitmap bitmap = downloadImg(url);
        return new ImageBean(url,bitmap);
    }








    @Override
    public void putDataToCache(ImageBean image) {}








    //下载图片
    private Bitmap downloadImg(String url){
        //获取到inputStream，再讲inputStream转换成bitmap，在return回去
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            final URLConnection con = new URL(url).openConnection();
            inputStream = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }




}
