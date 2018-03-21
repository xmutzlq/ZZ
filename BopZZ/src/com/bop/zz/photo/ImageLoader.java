package com.bop.zz.photo;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

import com.bop.zz.photo.widget.GFImageView;

/**
 * Desction:imageloader抽象类，外部需要实现这个类去加载图片， GalleryFinal尽力减少对第三方库的依赖，所以这么干了
 */
public interface ImageLoader extends Serializable{
    void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height);
    void clearMemoryCache();
}
