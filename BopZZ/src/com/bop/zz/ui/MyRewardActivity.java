package com.bop.zz.ui;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 我的打赏
 * @author zlq
 * @date 2016年12月13日 下午6:14:55
 */
public class MyRewardActivity extends BaseActivity implements OnClickListener{

	private ViewGroup mBtnAccumulateWithdrawsRecord;
	private ViewGroup mBtnAccumulateWithdrawsAll;
	
	private TextView mTvBalance;
	private TextView mTvBalanceTip;
	private TextView mTvAccumulateWithdraws;
	private TextView mTvAccumulateRecieveRewards;
	
	private TextView mTvCheckAccount;
	private TextView mTvAccountName;
	
	public static void openMyRewardActivity(Context context) {
		Intent intent = new Intent(context, MyRewardActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_my_reward;
	}

	@Override
	protected void setTitle(String title) {
		super.setTitle(getResources().getString(R.string.str_back));
	}
	
	@Override
	protected void findViews() {
		setCenter(getResources().getString(R.string.str_my_rewards));
		mBtnAccumulateWithdrawsRecord = (FrameLayout)findViewById(R.id.tv_accumulate_withdraws_record);
		mBtnAccumulateWithdrawsAll = (FrameLayout)findViewById(R.id.tv_accumulate_withdraws_all);
		
		mTvBalance = (TextView) findViewById(R.id.tv_balance);
		mTvBalanceTip = (TextView) findViewById(R.id.tv_balance_tip);
		mTvAccumulateWithdraws = (TextView) findViewById(R.id.tv_accumulate_withdraws);
		mTvAccumulateRecieveRewards = (TextView) findViewById(R.id.tv_accumulate_recieve_rewards);
		
		mTvCheckAccount = (TextView) findViewById(R.id.tv_check_account); 
		mTvAccountName = (TextView) findViewById(R.id.tv_account_name); 
	}

	@Override
	protected void setListener() {
		mBtnAccumulateWithdrawsRecord.setOnClickListener(this);
		mBtnAccumulateWithdrawsAll.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		Resources res = getResources();
		mTvBalance.setText(res.getString(R.string.str_balance, "0"));
		mTvBalanceTip.setText(res.getString(R.string.str_balance_tip, "10"));
		mTvAccumulateWithdraws.setText(res.getString(R.string.str_accumulate_withdraws, "0"));
		mTvAccumulateRecieveRewards.setText(res.getString(R.string.str_accumulate_recieve_rewards, "0"));
	}

	@Override
	public void onClick(View v) {
		
	}
}
