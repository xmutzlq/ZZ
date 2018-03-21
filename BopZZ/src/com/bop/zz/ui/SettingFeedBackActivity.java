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
 * 意见反馈
 * @author zlq
 * @date 2016年12月23日 下午3:56:16
 */
public class SettingFeedBackActivity extends BaseActivity implements OnClickListener{

	private static final int ID_TITLE_RIGHT_BTN = 0;
	
	public static void openSettingFeedBackActivity(Context context) {
		Intent intent = new Intent(context, SettingFeedBackActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_setting_feed_back;
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
		textView.setText(getResources().getString(R.string.str_title_setting_feed_back_send));
		textView.setOnClickListener(this);
		return textView;
	}
	
	@Override
	protected void findViews() {
		setCenter(getResources().getString(R.string.str_title_setting_feed_back));
	}

	@Override
	protected void setListener() {
		
	}

	@Override
	protected void loadData() {
		
	}

	@Override
	public void onClick(View v) {
		
	}
}
