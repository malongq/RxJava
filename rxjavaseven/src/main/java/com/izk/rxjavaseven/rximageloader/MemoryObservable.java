package com.izk.rxjavaseven.rximageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Malong
 * on 19/1/16.
 * 内存
 */
public class MemoryObservable extends CacheObservable {

    private int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);//kb
    private int cacheSize = maxMemory / 8;


    LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }
    };





    @Override
    public ImageBean getDataFromCache(String url) {
        Bitmap bitmap = lruCache.get(url);
        if (bitmap != null){
            return new ImageBean(url,bitmap);
        }
        return null;
    }





    @Override
    public void putDataToCache(ImageBean image) {
        lruCache.put(image.getUrl(),image.getBitmap());
    }





}
