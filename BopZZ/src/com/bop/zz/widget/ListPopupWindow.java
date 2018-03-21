package com.bop.zz.widget;

import java.util.List;

import com.bop.zz.R;
import com.bop.zz.photo.GalleryFinal;
import com.bop.zz.photo.HeadSettingActivity;
import com.bop.zz.photo.adapter.FolderListAdapter;
import com.bop.zz.photo.model.PhotoFolderInfo;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListPopupWindow extends BasePopupWindow implements OnItemClickListener {

	private HeadSettingActivity mActivity;
	private View contentView;
	private ListView mLvFolderList;
	private FolderListAdapter mFolderListAdapter;
	private List<PhotoFolderInfo> mAllPhotoFolderList;
	
	private PhotoFolderInfo curPhotoFolderInfo;
	
	public INotifyRefreshGrid mRefreshGridListener;
	
	public ListPopupWindow(HeadSettingActivity activity, View parent, List<PhotoFolderInfo> allPhotoFolderList, int popHeight) {
		super(parent, LayoutParams.MATCH_PARENT, popHeight);
		contentView = getLayoutInflater().inflate(R.layout.popwindow_list, null);
		initView(contentView);
		initListener();
		this.mActivity = activity;
		this.mAllPhotoFolderList = allPhotoFolderList;
		loadData();
	}

	@Override
	protected View onCreateContentView() {
		return contentView;
	}

	public void setRefreshGridListener(INotifyRefreshGrid listener) {
		mRefreshGridListener = listener;
	}
	
	private void initView(View view) {
		mLvFolderList = (ListView) view.findViewById(R.id.lv_folder_list);
	}

	private void initListener() {
		mLvFolderList.setOnItemClickListener(this);
	}

	private void loadData() {
		mFolderListAdapter = new FolderListAdapter(mActivity, mAllPhotoFolderList, GalleryFinal.getFunctionConfig());
		mLvFolderList.setAdapter(mFolderListAdapter);
		mFolderListAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onShow() {
		super.onShow();
		mActivity.toggleArrow(true);
	}
	
	@Override
	protected void onDismiss() {
		super.onDismiss();
		mActivity.toggleArrow(false);
	}
	
	@Override
	public void showAsDropDown() {
		super.showAsDropDown();
	}

	@Override
	protected Drawable getDefaultBackgroundDrawable() {
		return new ColorDrawable(getResources().getColor(android.R.color.transparent));
	}

	@Override
	protected int getDefaultAnimationStyle() {
		return R.style.BottomDialog_AnimationStyle;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		curPhotoFolderInfo = mAllPhotoFolderList.get(position);
		mActivity.notifyArrawTextChange(curPhotoFolderInfo.getFolderName());
		mFolderListAdapter.setSelectFolder(curPhotoFolderInfo);
		mFolderListAdapter.notifyDataSetChanged();
		dismiss();
		if(mRefreshGridListener != null) {
			mRefreshGridListener.onRefreshGrid(position, curPhotoFolderInfo);
		}
	}
	
	public static interface INotifyRefreshGrid {
		public void onRefreshGrid(int position, PhotoFolderInfo photoFolderInfo);
	}
}
