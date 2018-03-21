package com.bop.zz.ui;

import com.bop.zz.R;
import com.bop.zz.share.ShareUtil;
import com.bop.zz.share.share.ShareListener;
import com.bop.zz.share.share.SharePlatform;
import com.bop.zz.widget.BaseBottomDialog;

import android.view.View;
import android.widget.Toast;

public class ShareBottomDialog extends BaseBottomDialog implements View.OnClickListener {

    private ShareListener mShareListener;

    @Override
    public int getLayoutRes() {
        return R.layout.layout_bottom_share;
    }

    @Override
    public void bindView(final View v) {
        v.findViewById(R.id.share_qq).setOnClickListener(this);
        v.findViewById(R.id.share_qzone).setOnClickListener(this);
        v.findViewById(R.id.share_weibo).setOnClickListener(this);
        v.findViewById(R.id.share_wx).setOnClickListener(this);
        v.findViewById(R.id.share_wx_timeline).setOnClickListener(this);

        mShareListener = new ShareListener() {
            @Override
            public void shareSuccess() {
                Toast.makeText(v.getContext(), "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void shareFailure(Exception e) {
                Toast.makeText(v.getContext(), "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void shareCancel() {
                Toast.makeText(v.getContext(), "取消分享", Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_qq:
                ShareUtil.shareImage(getContext(), SharePlatform.QQ,
                        "https://www.dhibank.com/static/app/images/share_logo.jpg", mShareListener);
                break;
            case R.id.share_qzone:
                ShareUtil.shareMedia(getContext(), SharePlatform.QZONE, "Title", "summary",
                        "http://www.baidu.com", "https://www.dhibank.com/static/app/images/share_logo.jpg",
                        mShareListener);
                break;
            case R.id.share_weibo:
                ShareUtil.shareImage(getContext(), SharePlatform.WEIBO,
                        "https://www.dhibank.com/static/app/images/share_logo.jpg", mShareListener);
                break;
            case R.id.share_wx_timeline:
                ShareUtil.shareText(getContext(), SharePlatform.WX_TIMELINE, "测试分享文字",
                        mShareListener);
                break;
            case R.id.share_wx:
                ShareUtil.shareImage(getContext(), SharePlatform.WX, "https://www.dhibank.com/static/app/images/share_logo.jpg",
                        mShareListener);
                break;
        }
        dismiss();
    }
}
