package com.bop.zz.photo.edit.core.filter;

import java.util.Stack;

import com.bop.zz.R;
import com.bop.zz.photo.utils.ToastUtil;
import com.kdroid.photoedit.filters.FilterType;
import com.kdroid.photoedit.filters.NativeFilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

/**
 * 滤镜
 * @author zlq
 * @date 2017年1月4日 下午6:22:17
 */
public class ImageFilter implements IPhotoFilter {

	private int filterType = -1;
	private float degree = 1;
	
	private NativeFilter nativeFilters = new NativeFilter();
	

	public void setFilterType(int filterType, float degree) {
		this.filterType = filterType;
		this.degree = degree;
	}
	
	@Override
	public void onPhotoFilter(Context context, View imgView, Stack<Bitmap> bitmapStackp) {
		if(!(imgView instanceof ImageView) || bitmapStackp == null || context == null) return;
		if(filterType == -1) {
			ToastUtil.showToast(context, "请选择滤镜模式");
			return;
		}
		
		Bitmap topStackBitmap = bitmapStackp.peek();
		
		int[] dataResult = getPixels(filterType, degree, topStackBitmap);

		Bitmap resultImg = Bitmap.createBitmap(dataResult, topStackBitmap.getWidth(), topStackBitmap.getHeight(), Bitmap.Config.ARGB_8888);

		((ImageView)imgView).setImageBitmap(resultImg);
		
		imgView.invalidate();

		bitmapStackp.push(resultImg);
		
	}
	
	/**
	 * 获取改变后的像素
	 * @param filterType 滤镜类型
	 * @param degree 滤镜深度
	 * @param width 图片宽
	 * @param height 图片高
	 * @return
	 */
	private int[] getPixels(int filterType, float degree, Bitmap topStackBitmap) {
		int width = topStackBitmap.getWidth();
		int height = topStackBitmap.getHeight();
		int[] dataResult = null;
		int[] pix = new int[width * height];
		topStackBitmap.getPixels(pix, 0, width, 0, 0, width, height);
		switch (filterType) {
		case FilterType.FILTER4GRAY:
			dataResult = nativeFilters.gray(pix, width, height, degree);
			break;
			
		case FilterType.FILTER4MOSATIC:
			int mosatic = (int) (degree * 30);
			dataResult = nativeFilters.mosatic(pix, width, height, mosatic);
			break;

		case FilterType.FILTER4LOMO:
			dataResult = nativeFilters.lomo(pix, width, height, degree);
			break;
			
		case FilterType.FILTER4NOSTALGIC:
			dataResult = nativeFilters.nostalgic(pix, width, height, degree);
			break;
			
		case FilterType.FILTER4COMICS:
			dataResult = nativeFilters.comics(pix, width, height, degree);
			break;
			
		case FilterType.FILTER4BlackWhite:
			break;

		case FilterType.FILTER4NEGATIVE:
			break;
			
		case FilterType.FILTER4BROWN:
			dataResult = nativeFilters.brown(pix, width, height, degree);
			break;

		case FilterType.FILTER4SKETCH_PENCIL:
			dataResult = nativeFilters.sketchPencil(pix, width, height, degree);
			break;

		case FilterType.FILTER4OVEREXPOSURE:
			break;
			
		case FilterType.FILTER4WHITELOG:
			break;

		case FilterType.FILTER4SOFTNESS:
			break;

		case FilterType.FILTER4NiHong:
			break;
			
		case FilterType.FILTER4SKETCH:
			break;
			
		case FilterType.FILTER4RELIEF:
			break;

		default:
			break;
		}
		
		return dataResult;
	}
}
