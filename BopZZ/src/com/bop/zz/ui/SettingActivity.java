package com.bop.zz.ui;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;
import com.bop.zz.photo.HeadSettingActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 设置界面
 * @author zlq
 * @date 2016年12月20日 下午6:01:24
 */
public class SettingActivity extends BaseActivity implements OnClickListener {

	public static void openSettingActivity(Context context) {
		Intent intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected void setTitle(String title) {
		super.setTitle(getResources().getString(R.string.str_back));
	}
	
	@Override
	protected int getContentViewId() {
		return R.layout.activity_setting;
	}

	@Override
	protected void findViews() {
		setCenter(getResources().getString(R.string.str_setting));
	}

	@Override
	protected void setListener() {
		getView(R.id.btn_setting_head).setOnClickListener(this);
		getView(R.id.btn_setting_nick_name).setOnClickListener(this);
		getView(R.id.btn_setting_feed_back).setOnClickListener(this);
		getView(R.id.btn_setting_remaind).setOnClickListener(this);
		getView(R.id.btn_setting_check_version).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_setting_head:
			HeadSettingActivity.openHeadSettingActivity(this);
			break;
		case R.id.btn_setting_nick_name:
			SettingNickNameActivity.openSettingNickNameActivity(this);
			break;
		case R.id.btn_setting_feed_back:
			SettingFeedBackActivity.openSettingFeedBackActivity(this);	
			break;
		case R.id.btn_setting_remaind:
			SettingRemaindActivity.openSettingRemaindActivity(this);
			break;
		case R.id.btn_setting_check_version:
			
			break;
		}
	}
}
