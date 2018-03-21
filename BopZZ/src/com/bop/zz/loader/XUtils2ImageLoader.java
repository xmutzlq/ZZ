package com.bop.zz.loader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bop.zz.photo.ImageLoader;
import com.bop.zz.photo.widget.GFImageView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;

public class XUtils2ImageLoader implements ImageLoader {

    private BitmapUtils bitmapUtils;

    public XUtils2ImageLoader(Context context) {
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(defaultDrawable);
        config.setLoadingDrawable(defaultDrawable);
        config.setBitmapConfig(Bitmap.Config.RGB_565);
        config.setBitmapMaxSize(new BitmapSize(width, height));
        bitmapUtils.display(imageView, "file://" + path, config);
    }

    @Override
    public void clearMemoryCache() {
    }
}
