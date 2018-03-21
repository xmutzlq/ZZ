package com.bop.zz.photo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

import com.bop.zz.R;
import com.bop.zz.loader.FrescoImageLoader;
import com.bop.zz.photo.adapter.FrescoPhotoPreviewAdapter;
import com.bop.zz.photo.adapter.PhotoPreviewAdapter;
import com.bop.zz.photo.model.PhotoInfo;
import com.bop.zz.photo.widget.GFViewPager;

public class PhotoPreviewActivity extends PhotoBaseActivity implements ViewPager.OnPageChangeListener {

	public static final String PHOTO_LIST = "photo_list";
	public static final String PHOTO_INDEX = "photo_index";
	public static final String PHOTO_SELETED_LIST = "photo_seleted_list";
	
	private CheckBox checkBox;
	private GFViewPager mVpPager;
	private int photoIndex;
	private List<PhotoInfo> mPhotoList;
	private ArrayList<PhotoInfo> mPhotoSelectedIndexs;
	private PhotoInfo curPhotoInfo;
	private PhotoPreviewAdapter mPhotoPreviewAdapter;
	private FrescoPhotoPreviewAdapter mFrescoPhotoPreviewAdapter;

	private boolean hasChanged;
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.gf_activity_photo_preview;
	}

	@Override
	protected void setTitle(String title) {
		super.setTitle("返回");
	}

	@Override
	public void findViews() {
		mVpPager = (GFViewPager) findViewById(R.id.vp_pager);
	}

	@Override
	public void setListener() {
		mVpPager.addOnPageChangeListener(this);
	}

	@Override
	protected void setTheme() {
		super.setTheme();
		if (mThemeConfig.getPreviewBg() != null) {
			mVpPager.setBackgroundDrawable(mThemeConfig.getPreviewBg());
		}
	}

	@Override
	protected View buildTitleRight() {
		checkBox = new CheckBox(this);
		checkBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		checkBox.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		checkBox.setBackgroundResource(R.drawable.photo_checkbox_bg);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hasChanged = true;
				if (isChecked) {
					if (mPhotoSelectedIndexs != null) {
						if (!mPhotoSelectedIndexs.contains(curPhotoInfo)) {
							mPhotoSelectedIndexs.add(curPhotoInfo);
						}
					}
				} else {
					if (mPhotoSelectedIndexs != null) {
						if (mPhotoSelectedIndexs.contains(curPhotoInfo)) {
							mPhotoSelectedIndexs.remove(curPhotoInfo);
						} 
					}
				}
			}
		});
		return checkBox;

	}

	@Override
	public void loadData() {
		mPhotoSelectedIndexs = (ArrayList<PhotoInfo>) getIntent().getSerializableExtra(PHOTO_SELETED_LIST);
		if(mPhotoSelectedIndexs == null) {
			mPhotoSelectedIndexs = new ArrayList<PhotoInfo>();
			mPhotoSelectedIndexs.clear();
		}
		photoIndex = getIntent().getIntExtra(PHOTO_INDEX, 0);
		mPhotoList = (List<PhotoInfo>) getIntent().getSerializableExtra(PHOTO_LIST);
		curPhotoInfo = mPhotoList.get(photoIndex);
		mPhotoPreviewAdapter = new PhotoPreviewAdapter(this, mPhotoList);
		mFrescoPhotoPreviewAdapter = new FrescoPhotoPreviewAdapter(this, mPhotoList);
		if (GalleryFinal.getCoreConfig().getImageLoader() instanceof FrescoImageLoader) {
			mVpPager.setAdapter(mFrescoPhotoPreviewAdapter);
		} else {
			mVpPager.setAdapter(mPhotoPreviewAdapter);
		}
		if(photoIndex == 0) {
			setCenter(1 + "/" + mPhotoList.size());
			refreshCheckBoxStatus();
		} else {
			mVpPager.setCurrentItem(photoIndex);
		}
	}

	@Override
	protected void setResultFailureDelayed(String errormsg, boolean delayFinish) {
		resultFailureDelayed(getString(R.string.please_reopen_gf), true);
	}

	@Override
	protected void takeResult(PhotoInfo info) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		curPhotoInfo = mPhotoList.get(position);
		setCenter((position + 1) + "/" + mPhotoList.size());
		refreshCheckBoxStatus();
	}

	/**刷新复选框状态**/
	private void refreshCheckBoxStatus() {
		if (mPhotoSelectedIndexs.contains(curPhotoInfo)) {
			if (checkBox != null) {
				checkBox.setChecked(true);
			}
		} else {
			if (checkBox != null) {
				checkBox.setChecked(false);
			}
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {

	}
	
	@Override
	public void finish() {
		if(hasChanged) {
			Intent intent = new Intent();
			intent.putExtra(PHOTO_SELETED_LIST, mPhotoSelectedIndexs);
			setResult(RESULT_OK, intent);
		}
		super.finish();
	}
}
