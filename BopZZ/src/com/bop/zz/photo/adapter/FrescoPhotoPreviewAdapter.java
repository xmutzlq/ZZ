package com.bop.zz.photo.adapter;

import java.util.List;

import com.bop.zz.R;
import com.bop.zz.photo.FrescoImageLoader;
import com.bop.zz.photo.GalleryFinal;
import com.bop.zz.photo.model.PhotoInfo;
import com.bop.zz.photo.widget.photodraweeview.PhotoDraweeView;
import com.bop.zz.photo.widget.zoonview.PhotoView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import cn.finalteam.toolsfinal.DeviceUtils;

public class FrescoPhotoPreviewAdapter extends ViewHolderRecyclingPagerAdapter<FrescoPhotoPreviewAdapter.PreviewViewHolder, PhotoInfo> {

    private Activity mActivity;
    private DisplayMetrics mDisplayMetrics;

    public FrescoPhotoPreviewAdapter(Activity activity, List<PhotoInfo> list) {
        super(activity, list);
        this.mActivity = activity;
        this.mDisplayMetrics = DeviceUtils.getScreenPix(mActivity);
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = getLayoutInflater().inflate(R.layout.gf_adapter_fresco_preview_viewpgaer_item, null);
        return new PreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        PhotoInfo photoInfo = getDatas().get(position);
        String path = "";
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }
        holder.mImageView.setImageResource(R.drawable.ic_gf_default_photo);
        Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
        
        ((FrescoImageLoader)GalleryFinal.getCoreConfig().getImageLoader()).displayImage(mActivity, path, holder.mImageView, defaultDrawable, mDisplayMetrics.widthPixels/2, mDisplayMetrics.heightPixels/2);
    }

    static class PreviewViewHolder extends ViewHolderRecyclingPagerAdapter.ViewHolder{
    	PhotoDraweeView mImageView;
        public PreviewViewHolder(View view) {
            super(view);
            mImageView = (PhotoDraweeView) view;
        }
    }
}
