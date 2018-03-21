
package com.bop.zz.photo.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Desction:图片信息
 * Author:zlq
 */
public class PhotoInfo implements Serializable {

    private int photoId;
    private String photoPath;
    //private String thumbPath;
    private int width;
    private int height;

    public PhotoInfo() {}

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
    //
    //public String getThumbPath() {
    //    return thumbPath;
    //}
    //
    //public void setThumbPath(String thumbPath) {
    //    this.thumbPath = thumbPath;
    //}


    @Override
    public boolean equals(Object o) {
        if ( o == null || !(o instanceof PhotoInfo)) {
            return false;
        }
        PhotoInfo info = (PhotoInfo) o;
        if (info == null) {
            return false;
        }

        return TextUtils.equals(info.getPhotoPath(), getPhotoPath());
    }
}
