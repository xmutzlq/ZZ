package com.bop.zz.photo.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import cn.finalteam.toolsfinal.adapter.ViewHolderAdapter;

import java.util.List;

import com.bop.zz.R;
import com.bop.zz.photo.GalleryFinal;
import com.bop.zz.photo.model.PhotoInfo;
import com.bop.zz.photo.widget.GFImageView;

public class PhotoListAdapter extends ViewHolderAdapter<PhotoListAdapter.PhotoViewHolder, PhotoInfo> {
	private static final int DEFAULT_ROW_COUNT = 3;
	private List<PhotoInfo> mSelectList;
	private int mScreenWidth;
	private int mRowWidth;
	private int mRowCount = DEFAULT_ROW_COUNT;
	private Activity mActivity;
	private OnItemCheckChangeListener mCheckListener;

	public PhotoListAdapter(Activity activity, List<PhotoInfo> list, List<PhotoInfo> selectList, int screenWidth) {
		super(activity, list);
		this.mSelectList = selectList;
		this.mScreenWidth = screenWidth;
		this.mRowWidth = mScreenWidth / mRowCount;
		this.mActivity = activity;
	}
	
	public PhotoListAdapter(Activity activity, List<PhotoInfo> list, List<PhotoInfo> selectList, int screenWidth, int rowCount) {
		super(activity, list);
		this.mSelectList = selectList;
		this.mScreenWidth = screenWidth;
		this.mRowCount = rowCount;
		this.mRowWidth = mScreenWidth / rowCount;
		this.mActivity = activity;
	}

	public void setOnItemCheckChangeListener(OnItemCheckChangeListener clickListener) {
		mCheckListener = clickListener;
	}

	@Override
	public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		View view = inflate(R.layout.gf_adapter_photo_list_item, parent);
		setHeight(view);
		return new PhotoViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
		PhotoInfo photoInfo = getDatas().get(position);

		String path = "";
		if (photoInfo != null) {
			path = photoInfo.getPhotoPath();
		}

		holder.mIvThumb.setImageResource(R.drawable.ic_gf_default_photo);
		Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
		GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mIvThumb, defaultDrawable,
				mRowWidth, mRowWidth);
		holder.mView.setAnimation(null);
		if (GalleryFinal.getCoreConfig().getAnimation() > 0) {
			holder.mView
					.setAnimation(AnimationUtils.loadAnimation(mActivity, GalleryFinal.getCoreConfig().getAnimation()));
		}
		holder.mFlCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mCheckListener != null) {
					mCheckListener.onItemCheckChange(holder.mView, position);
				}
			}
		});
		holder.mIvCheck.setImageResource(GalleryFinal.getGalleryTheme().getIconCheck());
		if (GalleryFinal.getFunctionConfig().isMutiSelect()) {
			holder.mIvCheck.setVisibility(View.VISIBLE);
			if (mSelectList.contains(photoInfo)) {
				holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
			} else {
				holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckNornalColor());
			}
		} else {
			holder.mIvCheck.setVisibility(View.GONE);
		}
	}

	private void setHeight(final View convertView) {
		int height = mScreenWidth / mRowCount - 5;
		convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
	}

	/**
	 * 局部更新数据，调用一次getView()方法；Google推荐的做法
	 *
	 * @param listView
	 *            要更新的listview
	 * @param position
	 *            要更新的位置
	 */
	public boolean notifyDataSetChanged(GridView listView, int position) {
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

	public static class PhotoViewHolder extends ViewHolderAdapter.ViewHolder {

		public GFImageView mIvThumb;
		public ImageView mIvCheck;
		public ViewGroup mFlCheck;
		View mView;

		public PhotoViewHolder(View view) {
			super(view);
			mView = view;
			mIvThumb = (GFImageView) view.findViewById(R.id.iv_thumb);
			mIvCheck = (ImageView) view.findViewById(R.id.iv_check);
			mFlCheck = (FrameLayout) view.findViewById(R.id.fl_check);
		}
	}

	public static interface OnItemCheckChangeListener {
		public void onItemCheckChange(View view, int position);
	}
}
