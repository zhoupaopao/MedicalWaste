package com.arch.demo.core.glide;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * created by pqc
 * on 2020/3/18
 */
public class GlideImageLode implements LoadPicture {
    private Context context;

    public GlideImageLode(Context context) {
        this.context = context;
    }

    @Override
    public void displayImage(String imageUrl, ImageView imageView, int defaultImage) {
//        RequestOptions options = new RequestOptions()
//                .placeholder(defaultImage)
////                .error(R.drawable.error)
//                .diskCacheStrategy(DiskCacheStrategy.NONE);
//        .override(200, 200);
//        Glide.with(context).load(imageUrl).apply(options).into(imageView);
        Glide.with(context).load(imageUrl).into(imageView);
    }

    @Override
    public void displayImageGif(int id, ImageView imageView) {
        Glide.with(context).asGif().load(id).into(imageView);
    }

    @Override
    public void displayImageUri(Uri uri, ImageView imageView) {
        Glide.with(context).load(uri).into(imageView);
    }

}
