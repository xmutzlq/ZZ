package com.bop.zz.photo;

import java.io.Serializable;

import com.bop.zz.photo.widget.GFImageView;
import com.bop.zz.photo.widget.photodraweeview.PhotoDraweeView;

import android.app.Activity;
import android.graphics.drawable.Drawable;

public interface FrescoImageLoader extends ImageLoader {
    public void displayImage(Activity activity, String path, PhotoDraweeView imageView, Drawable defaultDrawable, int width, int height);
}
