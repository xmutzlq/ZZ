package com.bop.zz.refresh.core.ui.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;
import com.bop.zz.base.BaseFragmentActivity;
import com.bop.zz.photo.utils.ILogger;
import com.bop.zz.photo.utils.TextUtil;
import com.bop.zz.refresh.core.Global;
import com.bop.zz.refresh.core.event.RestartLoadMoreFragmentEvent;
import com.bop.zz.refresh.core.http.Api;
import com.bop.zz.refresh.core.http.MyBaseHttpRequestCallback;
import com.bop.zz.refresh.core.http.PostValues;
import com.bop.zz.refresh.core.http.model.MyAlbumsInfo;
import com.bop.zz.refresh.core.http.model.MyAlbumsInfos;
import com.bop.zz.refresh.core.http.model.MyAlbumsResponse;
import com.bop.zz.refresh.core.http.params.MyAlbumsParams;
import com.bop.zz.refresh.core.ui.adapter.NewGameListAdapter;
import com.bop.zz.refresh.core.utils.EmptyViewUtils;
import com.bop.zz.refresh.core.utils.MD5Util;
import com.bop.zz.refresh.loadingviewfinal.style.AVLoadMoreView;
import com.bop.zz.refresh.loadingviewfinal.style.AVLoadingIndicatorView;
import com.bop.zz.refresh.loadingviewfinal.style.LoadMoreStyle;
import com.bop.zz.refresh.loadingviewfinal.swipe.SwipeRefreshLayoutFinal;
import com.bop.zz.refresh.loadingviewfinal.view.DefaultLoadMoreView;
import com.bop.zz.refresh.loadingviewfinal.view.ILoadMoreView;
import com.bop.zz.refresh.loadingviewfinal.view.ListViewFinal;
import com.bop.zz.refresh.loadingviewfinal.view.OnLoadMoreListener;
import com.ypy.eventbus.EventBus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.HttpTaskHandler;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.AppCacheUtils;

/**
 * 我的相册
 * @author zlq
 * @date 2016年12月16日 下午3:18:00
 */
public class LoadMoreStyleActivity extends BaseFragmentActivity implements HttpCycleContext {

	protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

	private ViewGroup mAlbumsImgContentView;
	
	private ListViewFinal mLvAlbums;
	private SwipeRefreshLayoutFinal mRefreshLayout;
	private FrameLayout mFlEmptyView;

	private TextView albumsBuildTv, albumsRewardTv;
	
	private List<MyAlbumsInfo> mAlbumsList;
	private NewGameListAdapter mAlbumsListAdapter;

	private int mPage = 1;

	private AVLoadMoreView mAvLoadMoreView;

	private PostValues mPostValues;
	
	public static void openLoadMoreStyleActivity(Context context) {
		Intent intent = new Intent(context, LoadMoreStyleActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_albums_img;
	}

	@Override
	protected void setTitle(String title) {
		title = "返回";
		super.setTitle(title);
	}
	
	@Override
	protected void findViews() {
		setCenter("我的相册");
		mAlbumsImgContentView = (FrameLayout)findViewById(R.id.albums_img_content);
		mAlbumsImgContentView.removeAllViews();
		View refreshView = LayoutInflater.from(this).inflate(R.layout.fragment_srl_listview, null);
		mAlbumsImgContentView.addView(refreshView);
		mLvAlbums = (ListViewFinal) findViewById(R.id.lv_games);
		mLvAlbums.setDivider(new ColorDrawable(getResources().getColor(R.color.color_EDEDED)));
		mLvAlbums.setHeaderDividersEnabled(true);
		mLvAlbums.setDividerHeight(30);
		View v = LayoutInflater.from(this).inflate(R.layout.layout_loadmorestyle_head, null);
		albumsBuildTv = (TextView) v.findViewById(R.id.albums_build_tv);
		albumsRewardTv = (TextView) v.findViewById(R.id.albums_reward_tv);
		mLvAlbums = (ListViewFinal) findViewById(R.id.lv_games);
		mLvAlbums.addHeaderView(v);
		mRefreshLayout = (SwipeRefreshLayoutFinal) findViewById(R.id.refresh_layout);
		mFlEmptyView = (FrameLayout) findViewById(R.id.fl_empty_view);
	}

	@Override
	protected void setListener() {
		
	}

	@Override
	protected void loadData() {
		String defaultBuildValue = getResources().getString(R.string.str_albums_imgs_build_value, 0);
		SpannableString buildSS = TextUtil.formatTextColorBySpannableString(this, defaultBuildValue, 
				getResources().getColor(R.color.color_34aaf3), 0, defaultBuildValue.length() - 3);
		albumsBuildTv.setText(buildSS);
		String defaultRewardValue = getResources().getString(R.string.str_albums_imgs_reward_value, "0.00");
		SpannableString rewardSS = TextUtil.formatTextColorBySpannableString(this, defaultRewardValue, 
				getResources().getColor(R.color.color_34aaf3), 0, defaultRewardValue.length() - 1);
		albumsRewardTv.setText(rewardSS);
		
		mAlbumsList = new ArrayList<MyAlbumsInfo>();
		mAlbumsListAdapter = new NewGameListAdapter(this, mAlbumsList);
		mAvLoadMoreView = LoadMoreStyle.getAVLoadMoreViewFactory(this);
		mAvLoadMoreView.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
		int which = AppCacheUtils.getInstance(this).getInt("loadmorestyle_which_cache", 0);
		// 设置setLoadMoreView要在setAdapter之前
		mLvAlbums.setLoadMoreView(getLoadMoreByIndex(which));
		mLvAlbums.setAdapter(mAlbumsListAdapter);
		mLvAlbums.setEmptyView(mFlEmptyView);
		setSwipeRefreshInfo();
	}

	private ILoadMoreView getLoadMoreByIndex(int index) {
		switch (index) {
		case 0:
			return new DefaultLoadMoreView(this);
		case 1:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallPulse);
			break;
		case 2:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallGridPulse);
			break;
		case 3:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallClipRotate);
			break;
		case 4:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallClipRotatePulse);
			break;
		case 5:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.SquareSpin);
			break;
		case 6:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallClipRotateMultiple);
			break;
		case 7:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallPulseRise);
			break;
		case 8:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.CubeTransition);
			break;
		case 9:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallZigZag);
			break;
		case 10:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallZigZagDeflect);
			break;
		case 11:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallTrianglePath);
			break;
		case 12:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallScale);
			break;
		case 13:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineScale);
			break;
		case 14:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineScaleParty);
			break;
		case 15:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallScaleMultiple);
			break;
		case 16:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallPulseSync);
			break;
		case 17:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallBeat);
			break;
		case 18:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineScalePulseOut);
			break;
		case 19:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineScalePulseOutRapid);
			break;
		case 20:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallScaleRipple);
			break;
		case 21:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallScaleRippleMultiple);
			break;
		case 22:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallSpinFadeLoader);
			break;
		case 23:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineSpinFadeLoader);
			break;
		case 24:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.TriangleSkewSpin);
			break;
		case 25:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.Pacman);
			break;
		case 26:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallGridBeat);
			break;
		case 27:
			mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.SemiCircleSpin);
			break;
		}

		return mAvLoadMoreView;
	}

	private void setSwipeRefreshInfo() {
		mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				requestData(1);
			}
		});
		mLvAlbums.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void loadMore() {
				requestData(mPage);
			}
		});
		mRefreshLayout.autoRefresh();
	}

	private void requestData(final int page) {
		MyAlbumsParams params = new MyAlbumsParams(this, String.valueOf(page));
		HttpRequest.post(Api.MY_ALBUMS, params, new MyBaseHttpRequestCallback<MyAlbumsResponse>() {

			@Override
			public void onStart() {
				super.onStart();
				if (mAlbumsList.size() == 0) {
					EmptyViewUtils.showLoading(mFlEmptyView);
				}
			}

			@Override
			public void onLogicSuccess(MyAlbumsResponse newGameResponse) {
				if (page == 1) {
					mAlbumsList.clear();
				}
				mPage = page + 1;

				mAlbumsList.addAll(newGameResponse.getData().list);
				if (mPage >= newGameResponse.getData().totalpages) {
					mLvAlbums.setHasLoadMore(false);
				} else {
					mLvAlbums.setHasLoadMore(true);
				}
			}

			@Override
			public void onLogicFailure(MyAlbumsResponse newGameResponse) {
				if (mAlbumsList.size() == 0) {
					EmptyViewUtils.showNoDataEmpty(mFlEmptyView);
				} else {
					Toast.makeText(LoadMoreStyleActivity.this, newGameResponse.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				if (mAlbumsList.size() == 0) {
					EmptyViewUtils.showNetErrorEmpty(mFlEmptyView);
				} else {
					mLvAlbums.showFailUI();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();

				if (page == 1) {
					mRefreshLayout.onRefreshComplete();
				} else {
					mLvAlbums.onLoadMoreComplete();
				}

				mAlbumsListAdapter.notifyDataSetChanged();
			}
		});
	}
	
	@Override
	public String getHttpTaskKey() {
		return HTTP_TASK_KEY;
	}
	
	@Override
	protected void onDestroy() {
		HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
		super.onDestroy();
	}
}
