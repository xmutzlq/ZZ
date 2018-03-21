package com.bop.zz.refresh.loadingviewfinal.view;

import android.view.View;

public interface ILoadMoreView {

    /**
     * 显示普通布局
     */
    void showNormal();

    /**
     * 显示已经加载完成，没有更多数据的布局
     */
    void showNoMore();

    /**
     * 显示正在加载中的布局
     */
    void showLoading();

    /**
     * 显示加载失败的布局
     */
    void showFail();

    /**
     * 获取footerview
     * @return
     */
    View getFooterView();
}
