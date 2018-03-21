package com.bop.zz.photo.edit.core;

import java.util.Stack;

import com.bop.zz.photo.edit.core.filter.ImageFilter;
import com.bop.zz.photo.edit.core.frame.IPhotoFrame;
import com.bop.zz.photo.edit.core.frame.ImagePhotoFrame;
import com.bop.zz.photo.edit.core.text.IPhotoAddText;
import com.bop.zz.photo.edit.core.text.ImageAddText;
import com.kdroid.photoedit.filters.FilterType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class PhotoEditController {
	
	public static final String EDIT_TYPE_FILTER = "filter";
	public static final String EDIT_TYPE_FRAME = "frame";
	public static final String EDIT_TYPE_ADD_TEXT = "add_text";
	
	private Context context;
	private ImageView imgView;
	private Stack<Bitmap> bitmapStackp;
	
	private ImageFilter mPhotoFilter;
	private ImagePhotoFrame mPhotoFrame;
	private IPhotoAddText mPhotoAddText;
	
	public PhotoEditController(Context context, ImageView imgView, String imgPath) {
		this.context = context;
		this.imgView = imgView;
		mPhotoFilter = new ImageFilter();
		mPhotoFrame = new ImagePhotoFrame();
		mPhotoAddText = new ImageAddText();
		bitmapStackp = new Stack<Bitmap>();
		
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = 1;
		bitmapStackp.add(BitmapFactory.decodeFile(imgPath, option));
	}
	
	/**
	 * 编辑图片
	 * @param editType
	 */
	public void editPhoto(String editType) {
		if(EDIT_TYPE_FILTER.equals(editType)) {
			mPhotoFilter.setFilterType(FilterType.FILTER4MOSATIC, 1);
			mPhotoFilter.onPhotoFilter(context, imgView, bitmapStackp);
		} else if(EDIT_TYPE_FRAME.equals(editType)) {
			mPhotoFrame.setPhotoFrame(1);
			mPhotoFrame.onPhotoFrame(context, imgView, bitmapStackp);
		} else if(EDIT_TYPE_ADD_TEXT.equals(editType)) {
			mPhotoAddText.onPhotoAddText(context, imgView, bitmapStackp);
		}
	}
	
	public boolean backToPre() {
		if(bitmapStackp != null && bitmapStackp.size() > 1) {
			bitmapStackp.remove(bitmapStackp.size() - 1);
			Bitmap bitmap = bitmapStackp.peek();
			if(bitmap != null) {
				imgView.setImageBitmap(bitmap);
				return true;
			}
		}
		return false;
	}
}
