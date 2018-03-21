package com.bop.zz.refresh.loadingviewfinal.view;

import com.bop.zz.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DefaultLoadMoreView extends RelativeLayout implements ILoadMoreView {

    private TextView mTvMessage;
    private ProgressBar mPbLoading;

    public DefaultLoadMoreView(Context context) {
        super(context);
        init(context);
    }

    public DefaultLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DefaultLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.loading_view_final_footer_default, this);;
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        mTvMessage = (TextView) findViewById(R.id.tv_loading_msg);
    }

    @Override
    public void showNormal() {
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.loading_view_click_loading_more);
    }

    @Override
    public void showNoMore() {
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.loading_view_no_more);
    }

    @Override
    public void showLoading() {
        mPbLoading.setVisibility(View.VISIBLE);
        mTvMessage.setText(R.string.loading_view_loading);
    }

    @Override
    public void showFail() {
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.loading_view_net_error);
    }

    @Override
    public View getFooterView() {
        return this;
    }

}
