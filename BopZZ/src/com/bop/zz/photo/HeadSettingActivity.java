package com.bop.zz.photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bop.zz.R;
import com.bop.zz.photo.adapter.PhotoListAdapter;
import com.bop.zz.photo.model.PhotoFolderInfo;
import com.bop.zz.photo.model.PhotoInfo;
import com.bop.zz.photo.permission.AfterPermissionGranted;
import com.bop.zz.photo.permission.EasyPermissions;
import com.bop.zz.photo.utils.DimensionsUtil;
import com.bop.zz.photo.utils.ILogger;
import com.bop.zz.photo.utils.PhotoTools;
import com.bop.zz.widget.ListPopupWindow;
import com.bop.zz.widget.ListPopupWindow.INotifyRefreshGrid;
import com.bop.zz.widget.sheet.core.BottomSheet;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.StringUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeadSettingActivity extends PhotoBaseActivity implements OnItemClickListener, OnClickListener, INotifyRefreshGrid {

	private final int HANDLER_REFRESH_LIST_EVENT = 1002;
	
	private GridView mGvPhotoList;
	private List<PhotoFolderInfo> mAllPhotoFolderList;
	private List<PhotoInfo> mCurPhotoList;
	private ArrayList<PhotoInfo> mSelectPhotoList;
	private PhotoListAdapter mPhotoListAdapter;

	private TextView mTvEmptyView;
	private ViewGroup mBtnPhotoFolder;
	private ImageView mIvArrow;
	private TextView mTvArrow;
	
	private ListPopupWindow mPop;
	
	private BottomSheet sheet;
	protected int mScreenWidth = 720;
	public int mScreenHeight = 500;
	
	public static void openHeadSettingActivity(Context context) {
		GalleryFinal.openGallerySingle(0, GalleryFinal.copyGlobalFuncationConfig(), null);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("selectPhotoMap", mSelectPhotoList);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	private Handler mHanlder = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == HANDLER_REFRESH_LIST_EVENT) {
				mPhotoListAdapter.notifyDataSetChanged();
				if (mAllPhotoFolderList.get(0).getPhotoList() == null
						|| mAllPhotoFolderList.get(0).getPhotoList().size() == 0) {
					mTvEmptyView.setText(R.string.no_photo);
				}
				mGvPhotoList.setEnabled(true);
				if(mAllPhotoFolderList != null && mAllPhotoFolderList.size() > 0) {
					mTvArrow.setText(mAllPhotoFolderList.get(0).getFolderName());
					if(mAllPhotoFolderList.size() * DimensionsUtil.dip2px(HeadSettingActivity.this, 76) > mScreenHeight / 2) {
						mPop = new ListPopupWindow(HeadSettingActivity.this, mBtnPhotoFolder, mAllPhotoFolderList,
								mScreenHeight / 2);
						mPop.setRefreshGridListener(HeadSettingActivity.this);
					} else {
						mPop = new ListPopupWindow(HeadSettingActivity.this, mBtnPhotoFolder, mAllPhotoFolderList,
								LayoutParams.WRAP_CONTENT);
						mPop.setRefreshGridListener(HeadSettingActivity.this);
					}
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = DeviceUtils.getScreenPix(this);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		ILogger.e("mScreenWidth = " + mScreenWidth + ",mScreenHeight = " + mScreenHeight);
	}
	
	@Override
	protected void setTitle(String title) {
		super.setTitle(getResources().getString(R.string.str_back));
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_setting_head;
	}

	@Override
	protected void findViews() {
		setCenter(getResources().getString(R.string.str_title_select_photos));
		mGvPhotoList = (GridView) findViewById(R.id.gv_photo_list);
		mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
		mBtnPhotoFolder = (LinearLayout)findViewById(R.id.btn_photo_folder_info);
		mIvArrow = (ImageView) findViewById(R.id.iv_arrow);
		mTvArrow = (TextView) findViewById(R.id.tv_arrow);
		mBtnPhotoFolder.setBackgroundColor(mThemeConfig.getTitleBarBgColor());
		mBtnPhotoFolder.getBackground().setAlpha(150);
		mTvArrow.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void setListener() {
		mGvPhotoList.setOnItemClickListener(this);
		mBtnPhotoFolder.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		mPhotoTargetFolder = null;
		mAllPhotoFolderList = new ArrayList<PhotoFolderInfo>();
		mCurPhotoList = new ArrayList<PhotoInfo>();
		mSelectPhotoList = new ArrayList<PhotoInfo>();
		mPhotoListAdapter = new PhotoListAdapter(this, mCurPhotoList, mSelectPhotoList, mScreenWidth, 4);
		mGvPhotoList.setAdapter(mPhotoListAdapter);
		mGvPhotoList.setEmptyView(mTvEmptyView);
		requestGalleryPermission();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mSelectPhotoList.clear();
		mSelectPhotoList.add(mCurPhotoList.get(position));
		toPhotoEdit();
	}
	
	/**
	 * 执行裁剪
	 */
	protected void toPhotoEdit() {
		Intent intent = new Intent(this, PhotoEditActivity.class);
		intent.putExtra(PhotoEditActivity.SELECT_MAP, mSelectPhotoList);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_photo_folder_info:
//			showDialog(0);
			if(mPop != null && !mPop.isShowing()) {
				mPop.showAsDropDown();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		sheet = new BottomSheet.Builder(this).icon(R.drawable.default_image).title("To " + "zlq").sheet(R.menu.list).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        }).build();
		return sheet;
	}
	
	@Override
	public void onPermissionsGranted(List<String> list) {
		getPhotos();
	}

	@Override
	public void onPermissionsDenied(List<String> list) {
		mTvEmptyView.setText(R.string.permissions_denied_tips);
	}
	
	/**
	 * 获取所有图片
	 */
	@AfterPermissionGranted(GalleryFinal.PERMISSIONS_CODE_GALLERY)
	private void requestGalleryPermission() {
		if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
			getPhotos();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tips_gallery),
					GalleryFinal.PERMISSIONS_CODE_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE);
		}
	}
	
	private void getPhotos() {
		mTvEmptyView.setText(R.string.waiting);
		mGvPhotoList.setEnabled(false);
		new Thread() {
			@Override
			public void run() {
				super.run();

				mAllPhotoFolderList.clear();
				List<PhotoFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(HeadSettingActivity.this,
						mSelectPhotoList);
				mAllPhotoFolderList.addAll(allFolderList);

				mCurPhotoList.clear();
				if (allFolderList.size() > 0) {
					if (allFolderList.get(0).getPhotoList() != null) {
						mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
					}
				}

				refreshAdapter();
			}
		}.start();
	}
	
	private void refreshAdapter() {
		mHanlder.sendEmptyMessageDelayed(HANDLER_REFRESH_LIST_EVENT, 100);
	}

	public void toggleArrow(boolean isUp) {
		Animation anim = null;
		if(isUp) {
			anim = AnimationUtils.loadAnimation(this, R.anim.arrow_up);
		} else {
			anim = AnimationUtils.loadAnimation(this, R.anim.arrow_right);
		}
		anim.setFillAfter(true);
		mIvArrow.startAnimation(anim);
	}
	
	public void notifyArrawTextChange(String folderName) {
		mTvArrow.setText(folderName);
	}
	
	@Override
	protected void takeResult(PhotoInfo info) {
		
	}
	
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if (GalleryFinal.getCoreConfig() != null && GalleryFinal.getCoreConfig().getImageLoader() != null) {
			GalleryFinal.getCoreConfig().getImageLoader().clearMemoryCache();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPhotoTargetFolder = null;
		mSelectPhotoList.clear();
		System.gc();
	}

	@Override
	public void onRefreshGrid(int position, PhotoFolderInfo photoFolderInfo) {
		mCurPhotoList.clear();
		
		if (photoFolderInfo.getPhotoList() != null) {
			mCurPhotoList.addAll(photoFolderInfo.getPhotoList());
		}
		
		mPhotoListAdapter.notifyDataSetChanged();

		if (position == 0) {
			mPhotoTargetFolder = null;
		} else {
			PhotoInfo photoInfo = photoFolderInfo.getCoverPhoto();
			if (photoInfo != null && !StringUtils.isEmpty(photoInfo.getPhotoPath())) {
				mPhotoTargetFolder = new File(photoInfo.getPhotoPath()).getParent();
			} else {
				mPhotoTargetFolder = null;
			}
		}
		
		if (mCurPhotoList.size() == 0) {
			mTvEmptyView.setText(R.string.no_photo);
		}
	}
}
