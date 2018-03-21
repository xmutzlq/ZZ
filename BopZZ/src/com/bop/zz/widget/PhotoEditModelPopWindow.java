package com.bop.zz.widget;

import com.bop.zz.R;
import com.bop.zz.base.SimpleMenuItem;
import com.bop.zz.photo.GalleryFinal;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class PhotoEditModelPopWindow extends BasePopupWindow implements OnClickListener {

	private View contentView;
	private ItemClickListener mListener;
	
	public PhotoEditModelPopWindow(View parent, ItemClickListener listener) {
		super(parent, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mListener = listener;
		contentView = getLayoutInflater().inflate(R.layout.pop_photo_edit, null);
		contentView.findViewById(R.id.tv_lj).setOnClickListener(this);
		contentView.findViewById(R.id.tv_rtbx).setOnClickListener(this);
		contentView.findViewById(R.id.tv_bk).setOnClickListener(this);
		contentView.findViewById(R.id.tv_ty).setOnClickListener(this);
		contentView.findViewById(R.id.tv_msk).setOnClickListener(this);
		contentView.findViewById(R.id.tv_jq).setOnClickListener(this);
		contentView.findViewById(R.id.tv_tjsy).setOnClickListener(this);
		contentView.findViewById(R.id.tv_txzq).setOnClickListener(this);
		contentView.findViewById(R.id.tv_xz).setOnClickListener(this);
		contentView.findViewById(R.id.tv_tjwz).setOnClickListener(this);
	}

	@Override
	protected View onCreateContentView() {
		return contentView;
	}

	@Override
	protected void onShow() {
		super.onShow();
	}
	
	@Override
	protected void onDismiss() {
		super.onDismiss();
	}
	
	@Override
	public void showAsDropDown() {
		super.showAsDropDown();
	}

	@Override
	protected Drawable getDefaultBackgroundDrawable() {
		return new ColorDrawable(GalleryFinal.getGalleryTheme().getTitleBarBgColor());
	}

	@Override
	protected int getDefaultAnimationStyle() {
		return R.style.BottomDialog_AnimationStyle;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		MenuItem item = null;
		int id = v.getId();
		if(id == R.id.tv_lj) {
			item = new SimpleMenuItem(R.id.action_filter);
		} else if(id == R.id.tv_rtbx) {
			item = new SimpleMenuItem(R.id.action_wrap);
		} else if(id == R.id.tv_bk) {
			item = new SimpleMenuItem(R.id.action_frame);
		} else if(id == R.id.tv_ty) {
			item = new SimpleMenuItem(R.id.action_draw);
		} else if(id == R.id.tv_msk) {
			item = new SimpleMenuItem(R.id.action_mosaic);
		} else if(id == R.id.tv_jq) {
			item = new SimpleMenuItem(R.id.action_crop);
		} else if(id == R.id.tv_tjsy) {
			item = new SimpleMenuItem(R.id.action_addwm);
		} else if(id == R.id.tv_txzq) {
			item = new SimpleMenuItem(R.id.action_enchance);
		} else if(id == R.id.tv_xz) {
			item = new SimpleMenuItem(R.id.action_rotate);
		} else if(id == R.id.tv_tjwz) {
			item = new SimpleMenuItem(R.id.action_addtv);
		}
		if(mListener != null) {
			mListener.onPopItemClick(item);
		}
	}
	
	public interface ItemClickListener {
		public void onPopItemClick(MenuItem item);
	}
}
