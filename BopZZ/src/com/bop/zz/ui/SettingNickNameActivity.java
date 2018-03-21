package com.bop.zz.ui;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 设置昵称
 * @author zlq
 * @date 2016年12月23日 下午3:56:03
 */
public class SettingNickNameActivity extends BaseActivity implements OnClickListener {

	private static final int ID_TITLE_RIGHT_BTN = 0;
	
	public static void openSettingNickNameActivity(Context context) {
		Intent intent = new Intent(context, SettingNickNameActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_setting_nick_name;
	}

	@Override
	protected void setTitle(String title) {
		super.setTitle(getResources().getString(R.string.str_back));
	}
	
	@Override
	protected View buildTitleRight() {
		TextView textView = new TextView(this);
		textView.setId(ID_TITLE_RIGHT_BTN);
		textView.setTextColor(getResources().getColor(R.color.white));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textView.setText(getResources().getString(R.string.str_setting_nick_name_finish));
		textView.setOnClickListener(this);
		return textView;
	}
	
	@Override
	protected void findViews() {
		setCenter(getResources().getString(R.string.str_title_setting_nick_name));
	}

	@Override
	protected void setListener() {
		
	}

	@Override
	protected void loadData() {
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case ID_TITLE_RIGHT_BTN:
			
			break;
		default:
			break;
		}
	}
}
