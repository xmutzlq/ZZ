package com.bop.zz.photo.utils;

import com.bop.zz.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/** 对话框工具
 * @author zlq
 * @date 2016-08-15
 */
public final class ToastUtil {
	
	private static final int TOAST_BG_COMMON = 0;
	private static final int TOAST_BG_SUCCESS = 1;
	private static final int TOAST_BG_FAIL = 2;
	
	private static Toast toast;
	
	public static void showToast(Context context, int contentId){
		showToast(context, context.getString(contentId));
	}
	
	public static void showLongToast(Context context, int contentId){
		createToast(context, 0, null, context.getString(contentId), Gravity.CENTER_HORIZONTAL, Toast.LENGTH_LONG).show();
	}
	
	public static void showLongToast(Context context, int resId, int contentId){
		createToast(context, resId, null, context.getString(contentId), Gravity.CENTER_HORIZONTAL, Toast.LENGTH_LONG).show();
	}
	
	public static void showToast(Context context, int titleId, int contentId){
		showToast(context, context.getString(titleId), context.getString(contentId));
	}
	
	public static void showToast(Context context, String contentStr){
		if(TextUtils.isEmpty(contentStr)){
			return;
		}
		showToast(context, null, contentStr);
	}
	
	public static void showToastWithIcon(Context context, int resId, int contentId) {
		showToast(context, resId, null, context.getString(contentId));
	}
	
	public static void showToastWithIcon(Context context, int resId, String contentStr) {
		showToast(context, resId, null, contentStr);
	}
	
	public static void showToastWithStatus(Context context, int contentId, boolean isSuccess){
		showToastWithStatus(context, context.getString(contentId), isSuccess);
	}
	
	public static void showToastWithStatus(Context context, String contentStr, boolean isSuccess){
		showToastWithStatus(context, 0, contentStr, isSuccess);
	}
	
	public static void showToastWithStatus(Context context, int resId, String contentStr, boolean isSuccess){
		if(TextUtils.isEmpty(contentStr)){
			return;
		}
		int toastBg = TOAST_BG_COMMON;
		if(isSuccess){
			toastBg = TOAST_BG_SUCCESS;
		}else{
			toastBg = TOAST_BG_FAIL;
		}
		createToast(context, resId, null, contentStr, Gravity.CENTER_HORIZONTAL, Toast.LENGTH_SHORT, toastBg).show();
	}
	
	public static void showToast(Context context, String titleStr, String contentStr){
		createToast(context, 0, titleStr, contentStr, Gravity.CENTER_HORIZONTAL).show();
	}
	
	public static void showToast(Context context, int resId, String titleStr, String contentStr){
		createToast(context, resId, titleStr, contentStr, Gravity.CENTER_HORIZONTAL).show();
	}
	
	/** 自定义toast
	 * @param context
	 * @param titleStr 标题
	 * @param contentStr 内容
	 * @param gravity 在屏幕显示的位置
	 * @return
	 */
	private static Toast createToast(Context context, int resId, String titleStr, String contentStr, int gravity){
		return createToast(context, resId, titleStr, contentStr, gravity, Toast.LENGTH_SHORT);
	}
	
	private static Toast createToast(Context context, int resId, String titleStr, String contentStr, int gravity, int duration){
		return createToast(context, resId, titleStr, contentStr, gravity, duration, TOAST_BG_COMMON);
	}
	
	private static Toast createToast(Context context, int resId, String titleStr, String contentStr, int gravity,int duration, int toastBG){
		if(toast == null){
			toast = new Toast(context.getApplicationContext());
		}
		toast.setDuration(duration);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, context.getResources().getDimensionPixelSize(R.dimen.gf_title_bar_height));
		LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
		View view = inflater.inflate(R.layout.toast_common_layout, null);
		toast.setView(view);
		ImageView icon = (ImageView) view.findViewById(R.id.toast_icon);
		icon.setImageResource(resId);
		TextView content = (TextView) view.findViewById(R.id.toast_content);
		if(toastBG == TOAST_BG_COMMON){
			view.setBackgroundResource(R.drawable.toast_bg);
		} else if(toastBG == TOAST_BG_SUCCESS){
			view.setBackgroundResource(R.drawable.toast_successs_bg);
			content.setTextColor(context.getResources().getColor(R.color.toast_success_content));
		} else if(toastBG == TOAST_BG_FAIL){
			view.setBackgroundResource(R.drawable.toast_fail_bg);
		}
		TextView title = (TextView) view.findViewById(R.id.toast_title);
		if(!TextUtils.isEmpty(titleStr)){
			title.setVisibility(View.VISIBLE);
			title.setText(titleStr);
		}else{
			title.setVisibility(View.GONE);
		}
		content.setGravity(gravity);
		if(!TextUtils.isEmpty(contentStr)){
			content.setVisibility(View.VISIBLE);
			content.setText(contentStr);
		}else{
			content.setVisibility(View.GONE);
		}
		return toast;
	}
	
	public static void dismissToast(){
		if(toast != null)
			toast.cancel();
	}
}
