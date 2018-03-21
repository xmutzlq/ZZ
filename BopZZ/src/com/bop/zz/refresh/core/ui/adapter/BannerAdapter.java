package com.bop.zz.refresh.core.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bop.zz.R;
import com.bop.zz.refresh.core.Global;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.finalteam.toolsfinal.adapter.RecyclingPagerAdapter;

public class BannerAdapter extends RecyclingPagerAdapter {
    private Context mContext;
    private int mBannerHeight = Global.SCREEN_HEIGHT / 3;
    private List<Integer> mImageList;

    public BannerAdapter(Context context) {
        this.mContext = context;
        mImageList = new ArrayList<Integer>();
        mImageList.add(R.mipmap.banner01);
        mImageList.add(R.mipmap.banner02);
        mImageList.add(R.mipmap.banner03);
        mImageList.add(R.mipmap.banner04);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder holder;
        if (convertView == null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mBannerHeight);
            ImageView ivCover = new ImageView(mContext);
            ivCover.setImageResource(R.mipmap.ic_launcher);
            ivCover.setLayoutParams(params);
            holder = new ViewHolder();
            holder.imageView = ivCover;
            convertView = ivCover;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int pos = getPosition(position);
        int resId = mImageList.get(pos);
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(mContext)
                .load(resId)
                .placeholder(R.drawable.ic_gf_default_photo)
                .error(R.drawable.ic_gf_default_photo)
                .resize(Global.SCREEN_WIDTH, mBannerHeight)
                .config(Bitmap.Config.ARGB_8888)
                .centerCrop()
                .into(holder.imageView);
        holder.imageView.setFocusable(false);
        holder.imageView.setFocusableInTouchMode(false);
        return convertView;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getPosition(int position) {
        return position % mImageList.size();
    }

    public static class ViewHolder {
        private ImageView imageView;
    }
}
