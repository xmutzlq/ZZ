package com.bop.zz;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.finalteam.toolsfinal.DeviceUtils;

import com.bop.zz.photo.model.PhotoInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class ChoosePhotoListAdapter extends BaseAdapter {
    private List<PhotoInfo> mList;
    private LayoutInflater mInflater;
    private int mScreenWidth;

    public ChoosePhotoListAdapter(Activity activity, List<PhotoInfo> list) {
        this.mList = list;
        this.mInflater = LayoutInflater.from(activity);
        this.mScreenWidth = DeviceUtils.getScreenPix(activity).widthPixels;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_gf_default_photo)
                .showImageForEmptyUri(R.drawable.ic_gf_default_photo)
                .showImageOnLoading(R.drawable.ic_gf_default_photo).build();

        ImageView ivPhoto = (ImageView) mInflater.inflate(R.layout.adapter_photo_list_item, null);
        setHeight(ivPhoto);

        PhotoInfo photoInfo = mList.get(position);
        ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(), ivPhoto, options);
        return ivPhoto;
    }

    private void setHeight(final View convertView) {
        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }
}
