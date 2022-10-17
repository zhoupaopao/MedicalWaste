package com.arch.demo.core.glide;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

/**
 * created by pqc
 * on 2020/3/18
 */
public class ImageLoaderProxy implements LoadPicture {
    private LoadPicture loadPicture;
    private static ImageLoaderProxy instance;
    public static ImageLoaderProxy getInstance(){
        if (instance == null) {
            synchronized (ImageLoaderProxy.class) {
                if (instance == null) {
                    instance = new ImageLoaderProxy();
                }
            }
        }
        return instance;
    }
    private ImageLoaderProxy(){
    }
    public void setLoaderMode(LoadPicture loadPicture){
        this.loadPicture=loadPicture;
    }


    @Override
    public void displayImage(String imageUrl, ImageView imageView, int defaultImage) {
        loadPicture.displayImage(imageUrl,imageView,defaultImage);
    }

    @Override
    public void displayImageGif(int id, ImageView imageView) {
        loadPicture.displayImageGif(id,imageView);
    }

    @Override
    public void displayImageUri(Uri uri, ImageView imageView) {
        loadPicture.displayImageUri(uri,imageView);
    }

    public void displayImage(String imageUrl, ImageView imageView) {
        loadPicture.displayImage(imageUrl,imageView,0);
    }
}
