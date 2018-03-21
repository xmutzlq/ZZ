package com.bop.zz.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bop.zz.photo.widget.GFImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class UILImageLoader implements com.bop.zz.photo.ImageLoader {

    private Bitmap.Config mImageConfig;

    public UILImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public UILImageLoader(Bitmap.Config config) {
        this.mImageConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(false)
                .bitmapConfig(mImageConfig)
                .build();
        ImageSize imageSize = new ImageSize(width, height);
        ImageLoader.getInstance().displayImage("file://" + path, new ImageViewAware(imageView), options, null, null);
    }

    @Override
    public void clearMemoryCache() {

    }
}
