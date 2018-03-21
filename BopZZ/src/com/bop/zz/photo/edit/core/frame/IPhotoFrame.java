package com.bop.zz.photo.edit.core.frame;

import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

public interface IPhotoFrame {
	/**
	 * 图片编辑
	 * @param context
	 * @param imgView 容器
	 * @param topStackBitmap 
	 * @param canvas
	 */
	public void onPhotoFrame(Context context, View imgView, Stack<Bitmap> bitmapStackp);
}
