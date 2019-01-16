package com.izk.rxjavaseven.rximageloader;

import android.graphics.Bitmap;

/**
 * Created by Malong
 * on 19/1/16.
 */
public class ImageBean {

    private String url;
    private Bitmap bitmap;

    public ImageBean(String url, Bitmap bitmap) {
        this.url = url;
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
