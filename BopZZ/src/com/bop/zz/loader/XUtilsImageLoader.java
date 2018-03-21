package com.bop.zz.loader;

import com.bop.zz.photo.widget.GFImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class XUtilsImageLoader implements com.bop.zz.photo.ImageLoader {

    private Bitmap.Config mImageConfig;

    public XUtilsImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public XUtilsImageLoader(Bitmap.Config config) {
        this.mImageConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
//        ImageOptions options = new ImageOptions.Builder()
//                .setLoadingDrawable(defaultDrawable)
//                .setFailureDrawable(defaultDrawable)
//                .setConfig(mImageConfig)
//                .setSize(width, height)
//                .setCrop(true)
//                .setUseMemCache(false)
//                .build();
//        x.image().bind(imageView, "file://" + path, options);

    }

    @Override
    public void clearMemoryCache() {
    }
}
