package com.izk.rxjavaseven.rximageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Malong
 * on 19/1/16.
 * 文件
 * DiskLru
 */
public class DiskObservable extends CacheObservable {

    private DiskLruCache mDiskLruCache;
    private Context mContext;

    //diskLruCache 中对于图片的最大缓存值
    private int maxSize = 20 * 1024 * 1024;






    public DiskObservable(Context context) {
        this.mContext = context;
        initDiskLruCache();
    }







    @Override
    public ImageBean getDataFromCache(String url) {
        Bitmap bitmap = getDataFromDiskLruCache(url);
        return new ImageBean(url, bitmap);
    }








    @Override
    public void putDataToCache(final ImageBean image) {

        Observable.create(new ObservableOnSubscribe<ImageBean>() {
            @Override
            public void subscribe(ObservableEmitter<ImageBean> e) throws Exception {
                putDataToDiskLruCache(image);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }









    /**
     * 初始化DiskLrucache
     */
    private void initDiskLruCache() {
        try {
            //获取App 缓存路径
            File cacheDir = DiskCacheUtil.getDiskCacheDir(this.mContext, "imge_cache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            int versionCode = DiskCacheUtil.getAppVersionCode(mContext);
            mDiskLruCache = DiskLruCache.open(cacheDir, versionCode, 1, maxSize);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }








    //从文件获取图片
    private Bitmap getDataFromDiskLruCache(String url) {

        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;
        DiskLruCache.Snapshot snapShot = null;
        try {
            // 生成图片URL对应的key
            final String key = DiskCacheUtil.getMd5String(url);
            // 查找key对应的缓存
            snapShot = mDiskLruCache.get(key);

            if (snapShot != null) {
                fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }
            // 将缓存数据解析成Bitmap对象
            Bitmap bitmap = null;
            if (fileDescriptor != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileDescriptor == null && fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;

    }












    //将获取到的图片放入文件缓存
    private void putDataToDiskLruCache(ImageBean img) {

        try {
            //第一步:获取将要缓存的图片的对应唯一key值.
            String key = DiskCacheUtil.getMd5String(img.getUrl());
            //第二步:获取DiskLruCache的Editor
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);

            if (editor != null) {
                //第三步:从Editor中获取OutputStream
                OutputStream outputStream = editor.newOutputStream(0);
                //第四步:下载网络图片且保存至DiskLruCache图片缓存中

                boolean isSuccessfull = download(img.getUrl(), outputStream);

                if (isSuccessfull) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                mDiskLruCache.flush();
            }
        } catch (Exception e) {

        }
    }











    //下载
    private boolean download(String urlString, OutputStream outputStream) {

        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;

        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }









}
