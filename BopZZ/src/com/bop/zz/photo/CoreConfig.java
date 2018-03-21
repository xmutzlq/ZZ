package com.bop.zz.photo;

import android.content.Context;
import android.os.Environment;
import android.widget.AbsListView;

import java.io.File;
import java.io.Serializable;

import com.bop.zz.R;

public class CoreConfig {
    private Context context;
    private ImageLoader imageLoader;
    private File takePhotoFolder;
    private File editPhotoCacheFolder;
    private ThemeConfig themeConfig;
    private FunctionConfig functionConfig;
    private int animRes;
    private AbsListView.OnScrollListener onScrollListener;

    private CoreConfig(Builder builder) {
        this.context = builder.context;
        this.imageLoader = builder.imageLoader;
        this.takePhotoFolder = builder.takePhotoFolder;
        this.editPhotoCacheFolder = builder.editPhotoCacheFolder;
        this.themeConfig = builder.themeConfig;
        this.functionConfig = builder.functionConfig;
        if(builder.noAnimcation) {
            this.animRes = -1;
        } else {
            this.animRes = builder.animRes;
        }
        this.onScrollListener = builder.onScrollListener;

        if ( takePhotoFolder == null ) {
            takePhotoFolder = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
        }
        if(!takePhotoFolder.exists()) {
            takePhotoFolder.mkdirs();
        }

        if ( editPhotoCacheFolder == null ) {
            editPhotoCacheFolder = new File(Environment.getExternalStorageDirectory() + "/GalleryFinal/edittemp/");
        }
        if (!editPhotoCacheFolder.exists()) {
            editPhotoCacheFolder.mkdirs();
        }
    }

    public static class Builder {
        private Context context;
        private ThemeConfig themeConfig;
        private ImageLoader imageLoader;
        private File takePhotoFolder;//閰嶇疆鎷嶇収缂撳瓨鐩綍
        private File editPhotoCacheFolder;//閰嶇疆缂栬緫鍥剧墖浜х敓鐨勬枃浠剁紦瀛樼洰褰�
        private FunctionConfig functionConfig;
        private int animRes;
        private boolean noAnimcation;
        private AbsListView.OnScrollListener onScrollListener;

        public Builder(Context context, ImageLoader imageLoader, ThemeConfig themeConfig) {
            this.context = context;
            this.imageLoader = imageLoader;
            this.themeConfig = themeConfig;
            this.animRes = R.anim.gf_flip_horizontal_in;
        }

        public Builder setTakePhotoFolder(File takePhotoFolder) {
            this.takePhotoFolder = takePhotoFolder;
            return this;
        }

        public Builder setEditPhotoCacheFolder(File editPhotoCacheFolder) {
            this.editPhotoCacheFolder = editPhotoCacheFolder;
            return this;
        }

        public Builder setFunctionConfig(FunctionConfig functionConfig) {
            this.functionConfig = functionConfig;
            return this;
        }

        public Builder setAnimation(int animRes) {
            this.animRes = animRes;
            return this;
        }

        /**
         *  绂佹鍔ㄧ敾
         * @return
         */
        public Builder setNoAnimcation(boolean noAnimcation) {
            this.noAnimcation = noAnimcation;
            return this;
        }

        /**
         * 娣诲姞婊戝姩浜嬩欢鐢ㄤ簬浼樺寲鍥剧墖鍔犺浇锛屽彧鏈夊仠姝㈡粦鍔ㄤ簡鎵嶅幓鍔犺浇鍥剧墖
         * @param listener
         * @return
         */
        public Builder setPauseOnScrollListener(AbsListView.OnScrollListener listener) {
            this.onScrollListener = listener;
            return this;
        }

        public CoreConfig build() {
            return new CoreConfig(this);
        }
    }

    public Context getContext() {
        return context;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public File getTakePhotoFolder() {
        return takePhotoFolder;
    }

    public File getEditPhotoCacheFolder() {
        return editPhotoCacheFolder;
    }

    public int getAnimation() {
        return animRes;
    }

    public ThemeConfig getThemeConfig() {
        return themeConfig;
    }

    public FunctionConfig getFunctionConfig() {
        return functionConfig;
    }

    AbsListView.OnScrollListener getPauseOnScrollListener() {
        return onScrollListener;
    }
}
