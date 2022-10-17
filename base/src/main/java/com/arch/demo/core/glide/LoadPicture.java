package com.arch.demo.core.glide;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

/**
 * created by pqc
 * on 2020/3/18
 */
public interface LoadPicture {
    public void displayImage(String imageUrl, ImageView imageView, int defaultImage);
    public void displayImageGif(int id,ImageView imageView);
    public void displayImageUri(Uri uri, ImageView imageView);
}
