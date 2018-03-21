package com.bop.zz.ui;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;
import com.bop.zz.photo.GalleryFinal;
import com.bop.zz.photo.utils.ILogger;
import com.bop.zz.refresh.core.ui.fragment.LoadMoreStyleActivity;
import com.bop.zz.share.LoginUtil;
import com.bop.zz.share.login.LoginListener;
import com.bop.zz.share.login.LoginPlatform;
import com.bop.zz.share.login.LoginResult;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnClickListener {

	private long mExitTime;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applyKitKatTranslucency(Color.argb(0xff, 0x00, 0x00, 0x00));
		
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_main_layout;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void setListener() {
		getView(R.id.make_album_img_iv).setOnClickListener(this);
		getView(R.id.albums_img_iv).setOnClickListener(this);
		getView(R.id.reward_img_iv).setOnClickListener(this);
		getView(R.id.setting_img_iv).setOnClickListener(this);
		getView(R.id.login_btn).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		super.setTitleBarEnable(false);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.make_album_img_iv:
			GalleryFinal.openGalleryMuti(0, GalleryFinal.copyGlobalFuncationConfig(), null);
			break;
		case R.id.albums_img_iv:
			LoadMoreStyleActivity.openLoadMoreStyleActivity(this);
			break;
		case R.id.reward_img_iv:
			MyRewardActivity.openMyRewardActivity(this);
			break;
		case R.id.setting_img_iv:
			SettingActivity.openSettingActivity(this);
			break;
		case R.id.login_btn:
			LoginUtil.login(this, LoginPlatform.WX, listener);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
            if ((System.currentTimeMillis() - mExitTime) > 2000) {  
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
                    mExitTime = System.currentTimeMillis();  
            } else {  
               finish();
            }  
            return true;  
		}  
		return super.onKeyDown(keyCode, event);
	}
	
	private LoginListener listener = new LoginListener() {
		
		@Override
		public void loginSuccess(LoginResult result) {
			 Toast.makeText(MainActivity.this,
                     result.getUserInfo() != null ? result.getUserInfo().getNickname()
                             : "" + "登录成功", Toast.LENGTH_SHORT).show();
			 ILogger.e(result.getUserInfo() != null ? result.getUserInfo().getNickname()
                             : "" + "登录成功");
		}
		
		@Override
		public void loginFailure(Exception e) {
			Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void loginCancel() {
			Toast.makeText(MainActivity.this, "登录取消", Toast.LENGTH_SHORT).show();
		}
	};
}
