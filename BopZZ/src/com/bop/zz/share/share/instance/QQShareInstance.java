package com.bop.zz.share.share.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.bop.zz.share.ShareUtil;
import com.bop.zz.share.share.ImageDecoder;
import com.bop.zz.share.share.ShareImageObject;
import com.bop.zz.share.share.ShareListener;
import com.bop.zz.share.share.SharePlatform;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QQShareInstance implements ShareInstance {

    private Tencent mTencent;

    public QQShareInstance(Context context, String app_id) {
        mTencent = Tencent.createInstance(app_id, context.getApplicationContext());
    }

    @Override
    public void shareText(int platform, String text, Activity activity, ShareListener listener) {
        if (platform == SharePlatform.QZONE) {
            shareToQZoneForText(text, activity, listener);
        } else {
            startFailed(activity, listener, new Exception("qq not support share text"));
        }
    }

    @Override
    public void shareMedia(final int platform, final String title, final String targetUrl,
            final String summary, final ShareImageObject shareImageObject, final Activity activity,
            final ShareListener listener) {
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (!TextUtils.isEmpty(shareImageObject.getPathOrUrl())) {
                    return ImageDecoder.decode(activity, shareImageObject.getPathOrUrl());
                } else if (shareImageObject.getBitmap() != null) {
                    return ImageDecoder.decode(activity, shareImageObject.getBitmap());
                } else {
                    return null;
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.shareRequest();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (mTencent != null) {
                            mTencent.releaseResource();
                            mTencent = null;
                        }
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            if (platform == SharePlatform.QZONE) {
                                shareToQZoneForMedia(title, targetUrl, summary, s, activity,
                                        listener);
                            } else {
                                shareToQQForMedia(title, summary, targetUrl, s, activity, listener);
                            }
                        } else {
                            startFailed(activity, listener, new Exception("image fetch error"));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startFailed(activity, listener, new Exception(throwable));
                    }
                });
    }

    @Override
    public void shareImage(final int platform, final ShareImageObject shareImageObject,
            final Activity activity, final ShareListener listener) {
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (!TextUtils.isEmpty(shareImageObject.getPathOrUrl())) {
                    return ImageDecoder.decode(activity, shareImageObject.getPathOrUrl());
                } else if (shareImageObject.getBitmap() != null) {
                    return ImageDecoder.decode(activity, shareImageObject.getBitmap());
                } else {
                    return null;
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.shareRequest();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (mTencent != null) {
                            mTencent.releaseResource();
                            mTencent = null;
                        }
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String localPath) {
                        if (!TextUtils.isEmpty(localPath)) {
                            if (platform == SharePlatform.QZONE) {
                                shareToQzoneForImage(localPath, activity, listener);
                            } else {
                                shareToQQForImage(localPath, activity, listener);
                            }
                        } else {
                            startFailed(activity, listener, new Exception("image fetch error"));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startFailed(activity, listener, new Exception(throwable));
                    }
                });
    }

    private void startFailed(Activity activity, ShareListener listener, Exception e) {
        activity.finish();
        listener.doShareFailure(e);
    }

    @Override
    public void handleResult(Intent data) {
        Tencent.handleResultData(data, ShareUtil.mShareListener);
    }

    @Override
    public boolean isInstall(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }

        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), "com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void recycle() {

    }

    private void shareToQQForMedia(String title, String summary, String targetUrl, String thumbUrl,
            Activity activity, ShareListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumbUrl);
        mTencent.shareToQQ(activity, params, listener);
    }

    private void shareToQQForImage(String localUrl, Activity activity, ShareListener listener) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, localUrl);
        mTencent.shareToQQ(activity, params, listener);
    }

    private void shareToQZoneForText(String text, Activity activity, ShareListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, text);
        mTencent.publishToQzone(activity, params, listener);
    }

    private void shareToQZoneForMedia(String title, String targetUrl, String summary,
            String imageUrl, Activity activity, ShareListener listener) {
        final Bundle params = new Bundle();
        final ArrayList<String> image = new ArrayList<String>();
        image.add(imageUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, image);
        mTencent.shareToQzone(activity, params, listener);
    }

    private void shareToQzoneForImage(String imagePath, Activity activity, ShareListener listener) {
        final Bundle params = new Bundle();
        final ArrayList<String> image = new ArrayList<String>();
        image.add(imagePath);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, image);
        mTencent.publishToQzone(activity, params, listener);
    }
}
