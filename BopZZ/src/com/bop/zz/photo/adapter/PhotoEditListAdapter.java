package com.bop.zz.photo.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import cn.finalteam.toolsfinal.adapter.ViewHolderAdapter;

import java.util.ArrayList;
import java.util.List;

import com.bop.zz.R;
import com.bop.zz.photo.GalleryFinal;
import com.bop.zz.photo.PhotoEditActivity;
import com.bop.zz.photo.PhotoSelectActivity;
import com.bop.zz.photo.model.PhotoInfo;
import com.bop.zz.photo.widget.GFImageView;

public class PhotoEditListAdapter extends ViewHolderAdapter<PhotoEditListAdapter.ViewHolder, PhotoInfo> {

    private Activity mActivity;
    private int mRowWidth;

    public PhotoEditListAdapter(Activity activity, ArrayList<PhotoInfo> list, int screenWidth) {
        super(activity, list);
        mActivity = activity;
        this.mRowWidth = screenWidth/5;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_edit_list, parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = "";
        PhotoInfo photoInfo = getDatas().get(position);
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }
        holder.mIvPhoto.setImageResource(R.drawable.ic_gf_default_photo);
        holder.mIvDelete.setImageResource(GalleryFinal.getGalleryTheme().getIconDelete());
        Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
        GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mIvPhoto, defaultDrawable, 100, 100);
        if (!GalleryFinal.getFunctionConfig().isMutiSelect()) {
            holder.mIvDelete.setVisibility(View.GONE);
        } else {
            holder.mIvDelete.setVisibility(View.VISIBLE);
        }
        holder.mIvDelete.setOnClickListener(new OnDeletePhotoClickListener(position));
    }

    public class ViewHolder extends ViewHolderAdapter.ViewHolder {
        GFImageView mIvPhoto;
        ImageView mIvDelete;
        public ViewHolder(View view) {
            super(view);
            mIvPhoto = (GFImageView) view.findViewById(R.id.iv_photo);
            mIvDelete = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }

    private class OnDeletePhotoClickListener implements View.OnClickListener {

        private int position;

        public OnDeletePhotoClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            PhotoInfo photoInfo = null;
            try {
                photoInfo = getDatas().remove(position);
            } catch (Exception e){
                e.printStackTrace();
            }
            notifyDataSetChanged();
            if(mActivity instanceof PhotoSelectActivity) {
            	PhotoSelectActivity activity = (PhotoSelectActivity)mActivity;
            	activity.deleteSelect(photoInfo.getPhotoId());
            } else if(mActivity instanceof PhotoEditActivity) {
            	((PhotoEditActivity)mActivity).deleteIndex(position, photoInfo);
            }
        }
    }
    
    /**
	 * 局部更新数据，调用一次getView()方法；Google推荐的做法
	 *
	 * @param listView
	 *            要更新的listview
	 * @param position
	 *            要更新的位置
	 */
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
}
