package com.bop.zz.photo.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.finalteam.toolsfinal.adapter.ViewHolderAdapter;
import java.util.List;

import com.bop.zz.R;
import com.bop.zz.photo.FunctionConfig;
import com.bop.zz.photo.GalleryFinal;
import com.bop.zz.photo.model.PhotoFolderInfo;
import com.bop.zz.photo.model.PhotoInfo;
import com.bop.zz.photo.widget.GFImageView;

public class FolderListAdapter extends ViewHolderAdapter<FolderListAdapter.FolderViewHolder, PhotoFolderInfo> {

    private PhotoFolderInfo mSelectFolder;
    private FunctionConfig mFunctionConfig;
    
    private Activity mActivity;

    public FolderListAdapter(Activity activity, List<PhotoFolderInfo> list, FunctionConfig FunctionConfig) {
        super(activity, list);
        this.mFunctionConfig = FunctionConfig;
        this.mActivity = activity;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_folder_list_item, parent);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        PhotoFolderInfo photoFolderInfo = getDatas().get(position);

        String path = "";
        PhotoInfo photoInfo = photoFolderInfo.getCoverPhoto();
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }

    	holder.mIvCover.setImageResource(R.drawable.ic_gf_default_photo);
    	Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
    	GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mIvCover, defaultDrawable, 200, 200);

        holder.mTvFolderName.setText(photoFolderInfo.getFolderName());
        int size = 0;
        if ( photoFolderInfo.getPhotoList() != null ) {
            size = photoFolderInfo.getPhotoList().size();
        }
        holder.mTvPhotoCount.setText(mActivity.getString(R.string.folder_photo_size, size));
        if (GalleryFinal.getCoreConfig().getAnimation() > 0) {
            holder.mView.startAnimation(AnimationUtils.loadAnimation(mActivity, GalleryFinal.getCoreConfig().getAnimation()));
        }
        holder.mIvFolderCheck.setImageResource(GalleryFinal.getGalleryTheme().getIconCheck());
        if (mSelectFolder == photoFolderInfo || (mSelectFolder == null && position == 0)) {
            holder.mIvFolderCheck.setVisibility(View.VISIBLE);
            holder.mIvFolderCheck.setColorFilter(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
        } else {
            holder.mIvFolderCheck.setVisibility(View.GONE);
        }
    }

    public void setSelectFolder(PhotoFolderInfo photoFolderInfo) {
        this.mSelectFolder = photoFolderInfo;
    }

    public PhotoFolderInfo getSelectFolder() {
        return mSelectFolder;
    }
    
    public boolean notifyDataSetChanged(ListView listView, int position) {
		/** 第一个可见的位置 **/
		int firstVisiblePosition = listView.getFirstVisiblePosition();
		/** 最后一个可见的位置 **/
		int lastVisiblePosition = listView.getLastVisiblePosition();
		/** 在看见范围内才更新，不可见的滑动后自动会调用getView方法更新 **/
		if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
			/** 获取指定位置view对象 **/
			View view = listView.getChildAt(position - firstVisiblePosition);
			getView(position, view, listView);
		}
		return true;
	}

    static class FolderViewHolder extends ViewHolderAdapter.ViewHolder {
        GFImageView mIvCover;
        ImageView mIvFolderCheck;
        TextView mTvFolderName;
        TextView mTvPhotoCount;
        View mView;
        public FolderViewHolder(View view) {
            super(view);
            this.mView = view;
            mIvCover = (GFImageView) view.findViewById(R.id.iv_cover);
            mTvFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
            mTvPhotoCount = (TextView) view.findViewById(R.id.tv_photo_count);
            mIvFolderCheck = (ImageView) view.findViewById(R.id.iv_folder_check);
        }
    }
}
