package com.bop.zz.photo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bop.zz.R;
import com.bop.zz.photo.adapter.FolderListAdapter;
import com.bop.zz.photo.adapter.PhotoEditListAdapter;
import com.bop.zz.photo.adapter.PhotoListAdapter;
import com.bop.zz.photo.adapter.PhotoListAdapter.OnItemCheckChangeListener;
import com.bop.zz.photo.edit.MainActivity;
import com.bop.zz.photo.model.PhotoFolderInfo;
import com.bop.zz.photo.model.PhotoInfo;
import com.bop.zz.photo.permission.AfterPermissionGranted;
import com.bop.zz.photo.permission.EasyPermissions;
import com.bop.zz.photo.utils.PhotoTools;
import com.bop.zz.photo.utils.ToastUtil;
import com.bop.zz.photo.widget.FloatingActionButton;
import com.bop.zz.photo.widget.HorizontalListView;
import com.bop.zz.utils.ListUtils;

import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.StringUtils;
import cn.finalteam.toolsfinal.io.FilenameUtils;

public class PhotoSelectActivity extends PhotoBaseActivity
		implements View.OnClickListener, AdapterView.OnItemClickListener {

	private final int REQUEST_CODE_GET_SELECTED_PHTOTOS = 0x01;

	private final int HANLDER_TAKE_PHOTO_EVENT = 1000;
	private final int HANDLER_REFRESH_LIST_EVENT = 1002;
	private final int HANDLER_PARTY_REFRESH_LIST_EVENT = 1003;

	private TextView selectedPhotosTV, emptyStatusTV;
	private GridView mGvPhotoList;
	private ListView mLvFolderList;
	private LinearLayout mLlFolderPanel;
	private ImageView mIvTakePhoto;
	private ImageView mIvClear;
	private ImageView mIvPreView;
	private TextView mTvChooseCount;
	private TextView mTvSubTitle;
	private LinearLayout mLlTitle;
	private FloatingActionButton mFabOk;
	private TextView mTvEmptyView;
	private RelativeLayout mTitlebar;
	private ImageView mIvFolderArrow;

	/** 制作相册 **/
	private TextView makePhotos;

	private List<PhotoFolderInfo> mAllPhotoFolderList;
	private FolderListAdapter mFolderListAdapter;

	private List<PhotoInfo> mCurPhotoList;
	private PhotoListAdapter mPhotoListAdapter;

	protected int mScreenWidth = 720;
	private ArrayList<PhotoInfo> mSelectedPhotoList;
	private PhotoEditListAdapter mPhotoEditListAdapter;
	private HorizontalListView mLvGallery;

	// 是否需要刷新相册
	private int mRefreshPosition;
	private boolean mHasRefreshGallery = false;
	private ArrayList<PhotoInfo> mSelectPhotoList = new ArrayList<PhotoInfo>();

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("selectPhotoMap", mSelectPhotoList);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// mSelectPhotoList = (ArrayList<PhotoInfo>)
		// getIntent().getSerializableExtra("selectPhotoMap");
	}

	private Handler mHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == HANLDER_TAKE_PHOTO_EVENT) {
				PhotoInfo photoInfo = (PhotoInfo) msg.obj;
				takeRefreshGallery(photoInfo);
				refreshSelectCount();
			} else if (msg.what == HANDLER_REFRESH_LIST_EVENT) {
				refreshSelectCount();
				mPhotoListAdapter.notifyDataSetChanged();
				mFolderListAdapter.notifyDataSetChanged();
				if (mAllPhotoFolderList.get(0).getPhotoList() == null
						|| mAllPhotoFolderList.get(0).getPhotoList().size() == 0) {
					mTvEmptyView.setText(R.string.no_photo);
				}

				mGvPhotoList.setEnabled(true);
				mLlTitle.setEnabled(true);
				mIvTakePhoto.setEnabled(true);
			} else if (msg.what == HANDLER_PARTY_REFRESH_LIST_EVENT) {
				refreshSelectCount();
				if (mRefreshPosition != -1) {
					mPhotoListAdapter.notifyDataSetChanged(mGvPhotoList, mRefreshPosition);
				}
				mFolderListAdapter.notifyDataSetChanged();
				if (mAllPhotoFolderList.get(0).getPhotoList() == null
						|| mAllPhotoFolderList.get(0).getPhotoList().size() == 0) {
					mTvEmptyView.setText(R.string.no_photo);
				}

				mGvPhotoList.setEnabled(true);
				mLlTitle.setEnabled(true);
				mIvTakePhoto.setEnabled(true);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = DeviceUtils.getScreenPix(this);
		mScreenWidth = dm.widthPixels;
		Global.mPhotoSelectActivity = this;
	}

	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.gf_activity_photo_select;
	}

	@Override
	public void setTheme() {

		mIvFolderArrow.setImageResource(GalleryFinal.getGalleryTheme().getIconFolderArrow());
		if (GalleryFinal.getGalleryTheme().getIconFolderArrow() == R.drawable.ic_gf_triangle_arrow) {
			mIvFolderArrow.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
		}

		mIvClear.setImageResource(GalleryFinal.getGalleryTheme().getIconClear());
		if (GalleryFinal.getGalleryTheme().getIconClear() == R.drawable.ic_gf_clear) {
			mIvClear.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
		}

		mIvPreView.setImageResource(GalleryFinal.getGalleryTheme().getIconPreview());
		if (GalleryFinal.getGalleryTheme().getIconPreview() == R.drawable.ic_gf_preview) {
			mIvPreView.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
		}

		mIvTakePhoto.setImageResource(GalleryFinal.getGalleryTheme().getIconCamera());
		if (GalleryFinal.getGalleryTheme().getIconCamera() == R.drawable.ic_gf_camera) {
			mIvTakePhoto.setColorFilter(GalleryFinal.getGalleryTheme().getTitleBarIconColor());
		}
		mFabOk.setIcon(GalleryFinal.getGalleryTheme().getIconFab());

		mTitlebar.setBackgroundColor(GalleryFinal.getGalleryTheme().getTitleBarBgColor());
		mTvSubTitle.setTextColor(GalleryFinal.getGalleryTheme().getTitleBarTextColor());
		mTvChooseCount.setTextColor(GalleryFinal.getGalleryTheme().getTitleBarTextColor());
		mFabOk.setColorPressed(GalleryFinal.getGalleryTheme().getFabPressedColor());
		mFabOk.setColorNormal(GalleryFinal.getGalleryTheme().getFabNornalColor());
	}

	@Override
	public void findViews() {
		mGvPhotoList = (GridView) findViewById(R.id.gv_photo_list);
		mLvFolderList = (ListView) findViewById(R.id.lv_folder_list);
		mTvSubTitle = (TextView) findViewById(R.id.tv_sub_title);
		mLlFolderPanel = (LinearLayout) findViewById(R.id.ll_folder_panel);
		mIvTakePhoto = (ImageView) findViewById(R.id.iv_take_photo);
		mTvChooseCount = (TextView) findViewById(R.id.tv_choose_count);
		mFabOk = (FloatingActionButton) findViewById(R.id.fab_ok);
		mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
		mLlTitle = (LinearLayout) findViewById(R.id.ll_title);
		mIvClear = (ImageView) findViewById(R.id.iv_clear);
		mTitlebar = (RelativeLayout) findViewById(R.id.titlebar);
		mIvFolderArrow = (ImageView) findViewById(R.id.iv_folder_arrow);
		mIvPreView = (ImageView) findViewById(R.id.iv_preview);
		mLvGallery = (HorizontalListView) findViewById(R.id.lv_gallery);
		selectedPhotosTV = (TextView) findViewById(R.id.selected_photos);
		emptyStatusTV = (TextView) findViewById(R.id.empty_state_view);
		makePhotos = (TextView) findViewById(R.id.make_photos);
	}

	@Override
	public void setListener() {
		mLlTitle.setOnClickListener(this);
		mIvTakePhoto.setOnClickListener(this);
		mIvFolderArrow.setOnClickListener(this);
		mLvFolderList.setOnItemClickListener(this);
		mGvPhotoList.setOnItemClickListener(this);
		mFabOk.setOnClickListener(this);
		mIvClear.setOnClickListener(this);
		mIvPreView.setOnClickListener(this);
		makePhotos.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		mPhotoTargetFolder = null;

		findViewById(R.id.ll_gallery)
				.setBackgroundColor(GalleryFinal.getCoreConfig().getThemeConfig().getTitleBarBgColor());

		mAllPhotoFolderList = new ArrayList<PhotoFolderInfo>();
		mFolderListAdapter = new FolderListAdapter(this, mAllPhotoFolderList, GalleryFinal.getFunctionConfig());
		mLvFolderList.setAdapter(mFolderListAdapter);

		mSelectedPhotoList = new ArrayList<PhotoInfo>();
		mPhotoEditListAdapter = new PhotoEditListAdapter(this, mSelectedPhotoList, mScreenWidth);
		mLvGallery.setAdapter(mPhotoEditListAdapter);

		mCurPhotoList = new ArrayList<PhotoInfo>();
		mPhotoListAdapter = new PhotoListAdapter(this, mCurPhotoList, mSelectPhotoList, mScreenWidth);
		mPhotoListAdapter.setOnItemCheckChangeListener(mCheckListener);
		mGvPhotoList.setAdapter(mPhotoListAdapter);

		if (GalleryFinal.getFunctionConfig().isMutiSelect()) {
			mTvChooseCount.setVisibility(View.VISIBLE);
//			mFabOk.setVisibility(View.VISIBLE);
		}

		setTheme();
		mGvPhotoList.setEmptyView(mTvEmptyView);

		if (GalleryFinal.getFunctionConfig().isCamera()) {
			mIvTakePhoto.setVisibility(View.VISIBLE);
		} else {
			mIvTakePhoto.setVisibility(View.GONE);
		}

		refreshSelectCount();
		requestGalleryPermission();

		mGvPhotoList.setOnScrollListener(GalleryFinal.getCoreConfig().getPauseOnScrollListener());
	}

	@Override
	protected void setResultFailureDelayed(String errormsg, boolean delayFinish) {
		resultFailureDelayed(getString(R.string.please_reopen_gf), true);
	}

	public void deleteSelect(int photoId) {
		mRefreshPosition = findPositionInAllPhotos(photoId);
		try {
			for (Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator(); iterator.hasNext();) {
				PhotoInfo info = iterator.next();
				if (info != null && info.getPhotoId() == photoId) {
					iterator.remove();
					break;
				}
			}
		} catch (Exception e) {
		}

		refreshPartyAdapter();
	}

	private int findPositionInAllPhotos(int photoId) {
		for (int i = 0; i < mCurPhotoList.size(); i++) { // 查询图片所在所有图片的索引,实现局部刷新
			PhotoInfo info = mCurPhotoList.get(i);
			if (info.getPhotoId() == photoId) {
				return i;
			}
		}
		return -1;
	}

	private void refreshPartyAdapter() {
		mHanlder.sendEmptyMessageDelayed(HANDLER_PARTY_REFRESH_LIST_EVENT, 100);
	}

	private void refreshAdapter() {
		mHanlder.sendEmptyMessageDelayed(HANDLER_REFRESH_LIST_EVENT, 100);
	}

	protected void takeRefreshGallery(PhotoInfo photoInfo, boolean selected) {
		if (isFinishing() || photoInfo == null) {
			return;
		}

		Message message = mHanlder.obtainMessage();
		message.obj = photoInfo;
		message.what = HANLDER_TAKE_PHOTO_EVENT;
		mSelectPhotoList.add(photoInfo);
		mHanlder.sendMessageDelayed(message, 100);
	}

	/**
	 * 解决在5.0手机上刷新Gallery问题，
	 * 从startActivityForResult回到Activity把数据添加到集合中然后理解跳转到下一个页面，
	 * adapter的getCount与list.size不一致，所以我这里用了延迟刷新数据
	 * 
	 * @param photoInfo
	 */
	private void takeRefreshGallery(PhotoInfo photoInfo) {
		mCurPhotoList.add(0, photoInfo);
		mPhotoListAdapter.notifyDataSetChanged();

		// 添加到集合中
		List<PhotoInfo> photoInfoList = mAllPhotoFolderList.get(0).getPhotoList();
		if (photoInfoList == null) {
			photoInfoList = new ArrayList<PhotoInfo>();
		}
		photoInfoList.add(0, photoInfo);
		mAllPhotoFolderList.get(0).setPhotoList(photoInfoList);

		if (mFolderListAdapter.getSelectFolder() != null) {
			PhotoFolderInfo photoFolderInfo = mFolderListAdapter.getSelectFolder();
			List<PhotoInfo> list = photoFolderInfo.getPhotoList();
			if (list == null) {
				list = new ArrayList<PhotoInfo>();
			}
			list.add(0, photoInfo);
			if (list.size() == 1) {
				photoFolderInfo.setCoverPhoto(photoInfo);
			}
			mFolderListAdapter.getSelectFolder().setPhotoList(list);
		} else {
			String folderA = new File(photoInfo.getPhotoPath()).getParent();
			for (int i = 1; i < mAllPhotoFolderList.size(); i++) {
				PhotoFolderInfo folderInfo = mAllPhotoFolderList.get(i);
				String folderB = null;
				if (!StringUtils.isEmpty(photoInfo.getPhotoPath())) {
					folderB = new File(photoInfo.getPhotoPath()).getParent();
				}
				if (TextUtils.equals(folderA, folderB)) {
					List<PhotoInfo> list = folderInfo.getPhotoList();
					if (list == null) {
						list = new ArrayList<PhotoInfo>();
					}
					list.add(0, photoInfo);
					folderInfo.setPhotoList(list);
					if (list.size() == 1) {
						folderInfo.setCoverPhoto(photoInfo);
					}
				}
			}
		}

		mFolderListAdapter.notifyDataSetChanged();
	}

	@Override
	protected void takeResult(PhotoInfo photoInfo) {

		Message message = mHanlder.obtainMessage();
		message.obj = photoInfo;
		message.what = HANLDER_TAKE_PHOTO_EVENT;

		if (!GalleryFinal.getFunctionConfig().isMutiSelect()) { // 单选
			mSelectPhotoList.clear();
			mSelectPhotoList.add(photoInfo);

			if (GalleryFinal.getFunctionConfig().isEditPhoto()) {// 裁剪
				mHasRefreshGallery = true;
				toPhotoEdit();
			} else {
				ArrayList<PhotoInfo> list = new ArrayList<PhotoInfo>();
				list.add(photoInfo);
				resultData(list);
			}

			mHanlder.sendMessageDelayed(message, 100);
		} else {// 多选
			mSelectPhotoList.add(photoInfo);
			mHanlder.sendMessageDelayed(message, 100);
		}
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
		if (id == R.id.make_photos) {
			if (mSelectPhotoList.size() < 3) {
				ToastUtil.showToastWithIcon(this, R.drawable.toast_fail_icon, R.string.str_less_than_three_photos);
				return;
			} else {
				// TODO
				ToastUtil.showToast(this, "开始上传图片并制作相册");
				MainActivity.openPhotoEditTestActivity(this, mSelectPhotoList.get(0).getPhotoPath());
			}
		} else if (id == R.id.ll_title || id == R.id.iv_folder_arrow) {
			if (mLlFolderPanel.getVisibility() == View.VISIBLE) {
				mLlFolderPanel.setVisibility(View.GONE);
				mLlFolderPanel.setAnimation(AnimationUtils.loadAnimation(this, R.anim.gf_flip_horizontal_out));
			} else {
				mLlFolderPanel.setAnimation(AnimationUtils.loadAnimation(this, R.anim.gf_flip_horizontal_in));
				mLlFolderPanel.setVisibility(View.VISIBLE);
			}
		} else if (id == R.id.iv_take_photo) {
			// 判断是否达到多选最大数量
			if (GalleryFinal.getFunctionConfig().isMutiSelect()
					&& mSelectPhotoList.size() == GalleryFinal.getFunctionConfig().getMaxSize()) {
				toast(getString(R.string.select_max_tips));
				return;
			}

			if (!DeviceUtils.existSDCard()) {
				toast(getString(R.string.empty_sdcard));
				return;
			}

			takePhotoAction();
		} else if (id == R.id.iv_back) {
			if (mLlFolderPanel.getVisibility() == View.VISIBLE) {
				mLlTitle.performClick();
			} else {
				finish();
			}
		} else if (id == R.id.fab_ok) {
			if (mSelectPhotoList.size() > 0) {
				if (!GalleryFinal.getFunctionConfig().isEditPhoto()) {
					resultData(mSelectPhotoList);
				} else {
					toPhotoEdit();
				}
			}
		} else if (id == R.id.iv_clear) {
			mSelectPhotoList.clear();
			mPhotoListAdapter.notifyDataSetChanged();
			refreshSelectCount();
		} else if (id == R.id.iv_preview) {
			Intent intent = new Intent(this, PhotoPreviewActivity.class);
			intent.putExtra(PhotoPreviewActivity.PHOTO_LIST, mSelectPhotoList);
			startActivity(intent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int parentId = parent.getId();
		if (parentId == R.id.lv_folder_list) {
			folderItemClick(position);
		} else {
			ArrayList<PhotoInfo> infos = new ArrayList<PhotoInfo>(mCurPhotoList);
			ArrayList<PhotoInfo> selectedInfos = new ArrayList<PhotoInfo>(mSelectPhotoList);
			Intent intent = new Intent(this, PhotoPreviewActivity.class);
			intent.putExtra(PhotoPreviewActivity.PHOTO_INDEX, position);
			intent.putExtra(PhotoPreviewActivity.PHOTO_LIST, infos);
			intent.putExtra(PhotoPreviewActivity.PHOTO_SELETED_LIST, selectedInfos);
			startActivityForResult(intent, REQUEST_CODE_GET_SELECTED_PHTOTOS);
		}
	}

	private OnItemCheckChangeListener mCheckListener = new OnItemCheckChangeListener() {

		@Override
		public void onItemCheckChange(View view, int position) {
			photoItemClick(view, position);
		}
	};

	private void folderItemClick(int position) {
		mLlFolderPanel.setVisibility(View.GONE);
		mCurPhotoList.clear();
		PhotoFolderInfo photoFolderInfo = mAllPhotoFolderList.get(position);
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

		mTvSubTitle.setText(photoFolderInfo.getFolderName());
		mFolderListAdapter.setSelectFolder(photoFolderInfo);
		mFolderListAdapter.notifyDataSetChanged();

		if (mCurPhotoList.size() == 0) {
			mTvEmptyView.setText(R.string.no_photo);
		}
	}

	private void photoItemClick(View view, int position) {
		PhotoInfo info = mCurPhotoList.get(position);
		if (!GalleryFinal.getFunctionConfig().isMutiSelect()) {
			mSelectPhotoList.clear();
			mSelectPhotoList.add(info);
			String ext = FilenameUtils.getExtension(info.getPhotoPath());
			if (GalleryFinal.getFunctionConfig().isEditPhoto()
					&& (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))) {
				toPhotoEdit();
			} else {
				ArrayList<PhotoInfo> list = new ArrayList<PhotoInfo>();
				list.add(info);
				resultData(list);
			}
			return;
		}
		boolean checked = false;
		if (!mSelectPhotoList.contains(info)) {
			if (GalleryFinal.getFunctionConfig().isMutiSelect()
					&& mSelectPhotoList.size() == GalleryFinal.getFunctionConfig().getMaxSize()) {
				toast(getString(R.string.select_max_tips));
				return;
			} else {
				mSelectPhotoList.add(info);
				checked = true;
			}
		} else {
			try {
				for (Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator(); iterator.hasNext();) {
					PhotoInfo pi = iterator.next();
					if (pi != null && TextUtils.equals(pi.getPhotoPath(), info.getPhotoPath())) {
						iterator.remove();
						break;
					}
				}
			} catch (Exception e) {
			}
			checked = false;
		}
		refreshSelectCount();

		PhotoListAdapter.PhotoViewHolder holder = (PhotoListAdapter.PhotoViewHolder) view.getTag();
		if (holder != null) {
			if (checked) {
				holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
			} else {
				holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckNornalColor());
			}
		} else {
			mPhotoListAdapter.notifyDataSetChanged();
		}

		if (checked) {
			mSelectedPhotoList.add(info);
		} else {
			mSelectedPhotoList.remove(info);
		}
		mPhotoEditListAdapter.notifyDataSetChanged();
	}

	public void refreshSelectCount() {
		emptyStatusTV.setVisibility(mSelectPhotoList.size() > 0 ? View.GONE : View.VISIBLE);
		selectedPhotosTV.setText(getString(R.string.photo_selected, mSelectPhotoList.size()));
		mTvChooseCount.setText(
				getString(R.string.selected, mSelectPhotoList.size(), GalleryFinal.getFunctionConfig().getMaxSize()));
		if (mSelectPhotoList.size() > 0 && GalleryFinal.getFunctionConfig().isMutiSelect()) {
			mIvClear.setVisibility(View.VISIBLE);
		} else {
			mIvClear.setVisibility(View.GONE);
		}

		if (GalleryFinal.getFunctionConfig().isEnablePreview()) {
			mIvPreView.setVisibility(View.VISIBLE);
		} else {
			mIvPreView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPermissionsGranted(List<String> list) {
		getPhotos();
	}

	@Override
	public void onPermissionsDenied(List<String> list) {
		mTvEmptyView.setText(R.string.permissions_denied_tips);
		mIvTakePhoto.setVisibility(View.GONE);
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
		mLlTitle.setEnabled(false);
		mIvTakePhoto.setEnabled(false);
		new Thread() {
			@Override
			public void run() {
				super.run();

				mAllPhotoFolderList.clear();
				List<PhotoFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(PhotoSelectActivity.this,
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_GET_SELECTED_PHTOTOS) {
				if (data != null) {
					ArrayList<PhotoInfo> selectedPhotos = (ArrayList<PhotoInfo>) data
							.getSerializableExtra(PhotoPreviewActivity.PHOTO_SELETED_LIST);
					ArrayList<PhotoInfo> tmpSelectedPhotos = null;
					if (selectedPhotos.size() >= mSelectPhotoList.size()) {
						tmpSelectedPhotos = new ArrayList<PhotoInfo>(selectedPhotos);
						ListUtils.integerArrayListDifference(tmpSelectedPhotos, mSelectPhotoList);
					} else {
						tmpSelectedPhotos = new ArrayList<PhotoInfo>(mSelectPhotoList);
						ListUtils.integerArrayListDifference(tmpSelectedPhotos, selectedPhotos);
					}
					if (!(mSelectPhotoList.containsAll(selectedPhotos)
							&& mSelectPhotoList.size() == selectedPhotos.size())) { // 不同就刷新
						mSelectPhotoList.clear();
						mSelectedPhotoList.clear();
						mSelectPhotoList.addAll(selectedPhotos);
						mSelectedPhotoList.addAll(selectedPhotos);
						refreshSelectCount();
						for (PhotoInfo photoInfo : tmpSelectedPhotos) { // 局部刷新
							mPhotoListAdapter.notifyDataSetChanged(mGvPhotoList,
									findPositionInAllPhotos(photoInfo.getPhotoId()));
						}
						mPhotoEditListAdapter.notifyDataSetChanged();
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mLlFolderPanel.getVisibility() == View.VISIBLE) {
				mLlTitle.performClick();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mHasRefreshGallery) {
			mHasRefreshGallery = false;
			requestGalleryPermission();
		}
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
}
