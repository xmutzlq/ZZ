package com.bop.zz.photo.edit.core.text;

import java.util.Stack;

import com.bop.zz.photo.utils.ToastUtil;
import com.kdroid.photoedit.operate.OperateUtils;
import com.kdroid.photoedit.operate.OperateView;
import com.kdroid.photoedit.operate.TextObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

public class ImageAddText implements IPhotoAddText {

	private OperateUtils operateUtils;
	
	private String text;
	private int color;
	private String typeface;
	
	public void setTextParams(String text, int color, String typeface) {
		this.text = text;
		this.color = color;
		this.typeface = typeface;
	}
	
	@Override
	public void onPhotoAddText(Context context, View imgView, Stack<Bitmap> bitmapStackp) {
		if(!(imgView instanceof OperateView) || bitmapStackp == null) return;
		if(TextUtils.isEmpty(text)) {
			ToastUtil.showToast(context, "请输入文字");
			return;
		}
		
		TextObject textObj = operateUtils.getTextObject(text, (OperateView)imgView, 5, 150, 100);
		if (textObj != null) {
			textObj.setColor(color);
			textObj.setTypeface(typeface);
			textObj.commit();
			((OperateView)imgView).addItem(textObj);
			((OperateView)imgView).setOnListener(new OperateView.MyListener() {
				public void onClick(TextObject tObject) {
				}
			});
		}
	}
}
