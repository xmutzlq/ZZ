package com.bop.zz.photo.edit.core.frame;

import java.util.Stack;

import com.bop.zz.R;
import com.bop.zz.photo.utils.ToastUtil;
import com.kdroid.photoedit.photoframe.PhotoFrame;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public class ImagePhotoFrame implements IPhotoFrame{

	private static final int FRAME_PHOTO_1 = 1;
	private static final int FRAME_PHOTO_2 = 2;
	private static final int FRAME_PHOTO_3 = 3;
	
	private PhotoFrame mImageFrame;
	
	private int photoFrameType = -1;
	
	public void setPhotoFrame(int photoFrameType) {
		this.photoFrameType = photoFrameType;
	}
	
	@Override
	public void onPhotoFrame(Context context, View imgView, Stack<Bitmap> bitmapStackp) {
		if(!(imgView instanceof ImageView) || bitmapStackp == null || context == null) return;
		if(photoFrameType == -1) {
			ToastUtil.showToast(context, "请选择相框模式");
			return;
		}
		
		if(mImageFrame == null) {
			mImageFrame = new PhotoFrame(context, bitmapStackp.peek());
		}
		
		Bitmap tmpBitmap = getFramePhoto(photoFrameType);
		
		((ImageView)imgView).setImageBitmap(tmpBitmap);
		imgView.invalidate(); //刷新draw
		
		bitmapStackp.push(tmpBitmap);
	}
	
	/**
	 * 获取相框合成后的图片
	 * @param frameType
	 * @return
	 */
	private Bitmap getFramePhoto(int frameType) {
		
		Bitmap tmpBitmap = null;
		
		switch (frameType) {

		case FRAME_PHOTO_1:

			mImageFrame.setFrameType(PhotoFrame.FRAME_SMALL);
			mImageFrame.setFrameResources(R.drawable.frame_around1_left_top, R.drawable.frame_around1_left,
					R.drawable.frame_around1_left_bottom, R.drawable.frame_around1_bottom,
					R.drawable.frame_around1_right_bottom, R.drawable.frame_around1_right,
					R.drawable.frame_around1_right_top, R.drawable.frame_around1_top);
			tmpBitmap = mImageFrame.combineFrameRes();

			break;

		case FRAME_PHOTO_2:

			mImageFrame.setFrameType(PhotoFrame.FRAME_SMALL);
			mImageFrame.setFrameResources(R.drawable.frame_around2_left_top, R.drawable.frame_around2_left,
					R.drawable.frame_around2_left_bottom, R.drawable.frame_around2_bottom,
					R.drawable.frame_around2_right_bottom, R.drawable.frame_around2_right,
					R.drawable.frame_around2_right_top, R.drawable.frame_around2_top);
			tmpBitmap = mImageFrame.combineFrameRes();

			break;

		case FRAME_PHOTO_3:
			mImageFrame.setFrameType(PhotoFrame.FRAME_BIG);
			mImageFrame.setFrameResources(R.drawable.frame_big1);

			tmpBitmap = mImageFrame.combineFrameRes();

			break;

		default:
			break;

		}
		
		return tmpBitmap;
	}
}
