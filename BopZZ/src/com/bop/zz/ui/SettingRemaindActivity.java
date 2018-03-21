package com.bop.zz.ui;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;
import com.bop.zz.widget.ToggleButton;
import com.bop.zz.widget.ToggleButton.OnToggleChanged;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class SettingRemaindActivity extends BaseActivity implements OnToggleChanged{

	private ToggleButton mRecieveNewMessageTb;
	
	public static void openSettingRemaindActivity(Context context) {
		Intent intent = new Intent(context, SettingRemaindActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_setting_remaind;
	}

	@Override
	protected void setTitle(String title) {
		super.setTitle(getResources().getString(R.string.str_back));
	}
	
	@Override
	protected void findViews() {
		setCenter(getResources().getString(R.string.str_title_setting_remaind));
		mRecieveNewMessageTb = (ToggleButton) findViewById(R.id.tb_remaind_recieve_message);
	}

	@Override
	protected void setListener() {
		mRecieveNewMessageTb.setOnToggleChanged(this);
	}

	@Override
	protected void loadData() {
		
	}

	@Override
	public void onToggle(ToggleButton button, boolean on) {
		int id = button.getId();
		switch (id) {
		case R.id.tb_remaind_recieve_message:
			
			break;

		default:
			break;
		}
	}
}
