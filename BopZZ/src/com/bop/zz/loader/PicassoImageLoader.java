package com.bop.zz.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bop.zz.photo.ImageLoader;
import com.bop.zz.photo.widget.GFImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PicassoImageLoader implements ImageLoader {

    private Bitmap.Config mConfig;

    public PicassoImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public PicassoImageLoader(Bitmap.Config config) {
        this.mConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Picasso.with(activity)
                .load(new File(path))
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .config(mConfig)
                .resize(width, height)
                .centerInside()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
