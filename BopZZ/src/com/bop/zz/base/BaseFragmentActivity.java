package com.bop.zz.base;

import java.lang.reflect.Field;

import com.bop.zz.R;
import com.bop.zz.photo.GalleryFinal;
import com.bop.zz.photo.ThemeConfig;
import com.bop.zz.statubar.SystemBarTintManager;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;
import cn.finalteam.toolsfinal.ActivityManager;

public abstract class BaseFragmentActivity extends FragmentActivity{
	private View statuBar;
	private RelativeLayout mTitleBar;
	private ImageView mIvBack;
	private TextView mTvTitle;
	private TextView mTvIndicator;
	private ViewGroup mTitleRight;
	
	public ThemeConfig mThemeConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ActivityManager.getActivityManager().addActivity(this);
		mThemeConfig = GalleryFinal.getGalleryTheme();
		if (mThemeConfig == null) {
			setResultFailureDelayed(getString(R.string.please_reopen_gf), true);
		} else {
			applyKitKatTranslucency(mThemeConfig.getTitleBarBgColor());
			if (getContentView() == null) {
				if (getContentViewId() == 0) {
					finish();
				} else {
					setContentView(getContentViewId());
				}
			} else {
				setContentView(getContentView());
			}
			initStatuBar();
			initView();
			findViews();
			setListener();
			setCommTheme();
			loadData();
			setTitle(getResources().getString(R.string.app_name));
			mIvBack.setOnClickListener(mBackListener);
			if(buildTitleRight() != null) {
				mTitleRight.removeAllViews();
				mTitleRight.addView(buildTitleRight());
			}
		}
	}

	private void initStatuBar() {
		//配置StatuBar
		statuBar = findViewById(R.id.statu_bar);
    	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	lp.height = getStatusBarHeight();
    	if(statuBar != null) {
    		statuBar.setLayoutParams(lp);
    	}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.getActivityManager().finishActivity(this);
	}
	
	protected void setTitleBarEnable(boolean isEnable) {
		if (isEnable) {
			if (mTitleBar != null) {
				mTitleBar.setVisibility(View.VISIBLE);
			}
		} else {
			if (mTitleBar != null) {
				mTitleBar.setVisibility(View.GONE);
				statuBar.setVisibility(View.GONE);
			}
		}
	}

	protected void setResultFailureDelayed(String errormsg, boolean delayFinish) {
		// 由子类完成
	}

	protected void setTheme() {

	}

	protected void setTitle(String title) {
		mTvTitle.setText(title);
	}

	protected void setCenter(String centerStr) {
		mTvIndicator.setText(centerStr);
	}

	protected View buildTitleRight() {
		return null;
	}

	protected abstract View getContentView();

	protected abstract int getContentViewId();

	protected abstract void findViews();

	protected abstract void setListener();

	protected abstract void loadData();

	private void setCommTheme() {
		mIvBack.setImageResource(mThemeConfig.getIconBack());
		if (mThemeConfig.getIconBack() == R.drawable.ic_gf_back) {
			mIvBack.setColorFilter(mThemeConfig.getTitleBarIconColor());
		}

		mTitleBar.setBackgroundColor(mThemeConfig.getTitleBarBgColor());
		mTvTitle.setTextColor(mThemeConfig.getTitleBarTextColor());
		setTheme();
	}

	private void initView() {
		mTitleBar = (RelativeLayout) findViewById(R.id.titlebar);
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvIndicator = (TextView) findViewById(R.id.tv_indicator);
		mTitleRight = (FrameLayout) findViewById(R.id.title_right);
	}

	/**
	 * Apply KitKat specific translucency.
	 */
	public void applyKitKatTranslucency(int color) {
		// KitKat translucent navigation/status bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager mTintManager = new SystemBarTintManager(this);
			mTintManager.setStatusBarTintEnabled(true);
			mTintManager.setStatusBarTintColor(color);// 通知栏所需颜色
		}
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	public final <E extends View> E getView(int id) {
		try {
			return (E) findViewById(id);
		} catch (ClassCastException ex) {
			throw ex;
		}
	}

	public final <E extends View> E getView(View parent, int id) {
		try {
			return (E) parent.findViewById(id);
		} catch (ClassCastException ex) {
			throw ex;
		}
	}

	private View.OnClickListener mBackListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	public int getStatusBarHeight() {  
        Class<?> c = null;  
        Object obj = null;  
        Field field = null;  
        int x = 0;  
        try {  
            c = Class.forName("com.android.internal.R$dimen");  
            obj = c.newInstance();  
            field = c.getField("status_bar_height");  
            x = Integer.parseInt(field.get(obj).toString());  
            return getResources().getDimensionPixelSize(x);  
        } catch (Exception e1) {  
            e1.printStackTrace();  
            return 75;  
        }  
    }
}
