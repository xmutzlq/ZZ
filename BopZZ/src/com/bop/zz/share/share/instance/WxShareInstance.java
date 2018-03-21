package com.bop.zz.share.share.instance;

import com.bop.zz.share.ShareUtil;
import com.bop.zz.share.share.ImageDecoder;
import com.bop.zz.share.share.ShareImageObject;
import com.bop.zz.share.share.ShareListener;
import com.bop.zz.share.share.SharePlatform;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Pair;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WxShareInstance implements ShareInstance {

    /**
     * 微信分享限制thumb image必须小于32Kb，否则点击分享会没有反应
     */

    private IWXAPI mIWXAPI;

    private static final int THUMB_SIZE = 144;

    private static final int TARGET_SIZE = 200;

    public WxShareInstance(Context context, String appId) {
        mIWXAPI = WXAPIFactory.createWXAPI(context, appId, true);
        mIWXAPI.registerApp(appId);
    }

    @Override
    public void shareText(int platform, String text, Activity activity, ShareListener listener) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;

        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = textObject;
        message.description = text;

        sendMessage(platform, message, buildTransaction("text"));
    }

    @Override
    public void shareMedia(
            final int platform, final String title, final String targetUrl, final String summary,
            final ShareImageObject shareImageObject, final Activity activity, final ShareListener listener) {
        Observable.fromEmitter(new Action1<Emitter<byte[]>>() {

            @Override
            public void call(Emitter<byte[]> emitter) {
                if (shareImageObject.getBitmap() != null) {
                    byte[] data = ImageDecoder.bmp2ByteArray(
                            ImageDecoder.compress(shareImageObject.getBitmap(), TARGET_SIZE));

                    emitter.onNext(data);
                    emitter.onCompleted();
                } else if (!TextUtils.isEmpty(shareImageObject.getPathOrUrl())) {
                    String path = ImageDecoder.decode(activity, shareImageObject.getPathOrUrl());
                    Bitmap bitmap = ImageDecoder.compress(path, TARGET_SIZE, TARGET_SIZE);
                    emitter.onNext(ImageDecoder.bmp2ByteArray(bitmap));
                    bitmap.recycle();
                    emitter.onCompleted();
                } else {
                    emitter.onError(new IllegalArgumentException());
                }
            }
        }, Emitter.BackpressureMode.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.shareRequest();
                    }
                })
                .subscribe(new Action1<byte[]>() {
                    @Override
                    public void call(byte[] bytes) {
                        WXWebpageObject webpageObject = new WXWebpageObject();
                        webpageObject.webpageUrl = targetUrl;

                        WXMediaMessage message = new WXMediaMessage(webpageObject);
                        message.title = title;
                        message.description = summary;
                        message.thumbData = bytes;

                        sendMessage(platform, message, buildTransaction("webPage"));
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
        Observable.fromEmitter(new Action1<Emitter<Pair<Bitmap, Bitmap>>>() {
            @Override
            public void call(Emitter<Pair<Bitmap, Bitmap>> emitter) {
                if (shareImageObject.getBitmap() != null) {
                    Bitmap thumb = ImageDecoder.compress(shareImageObject.getBitmap(), TARGET_SIZE);

                    emitter.onNext(Pair.create(shareImageObject.getBitmap(), thumb));
                    emitter.onCompleted();
                } else if (!TextUtils.isEmpty(shareImageObject.getPathOrUrl())) {
                    String filePath =
                            ImageDecoder.decode(activity, shareImageObject.getPathOrUrl());
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    Bitmap thumb = ImageDecoder.compress(bitmap, TARGET_SIZE);

                    emitter.onNext(Pair.create(bitmap, thumb));
                    emitter.onCompleted();
                } else {
                    emitter.onError(new IllegalArgumentException());
                }
            }
        }, Emitter.BackpressureMode.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.shareRequest();
                    }
                })
                .subscribe(new Action1<Pair<Bitmap, Bitmap>>() {
                    @Override
                    public void call(Pair<Bitmap, Bitmap> pair) {
                        WXImageObject imageObject = new WXImageObject(pair.first);

                        WXMediaMessage message = new WXMediaMessage();
                        message.mediaObject = imageObject;
                        message.thumbData = ImageDecoder.bmp2ByteArray(pair.second);
                        pair.second.recycle();

                        sendMessage(platform, message, buildTransaction("image"));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startFailed(activity, listener, new Exception(throwable));
                    }
                });
    }

    @Override
    public void handleResult(Intent data) {
        mIWXAPI.handleIntent(data, new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {
            }

            @Override
            public void onResp(BaseResp baseResp) {
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        ShareUtil.mShareListener.doShareSuccess();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        ShareUtil.mShareListener.doShareCancel();
                        break;
                    default:
                        ShareUtil.mShareListener.doShareFailure(new Exception(baseResp.errStr));
                }
            }
        });
    }

    @Override
    public boolean isInstall(Context context) {
        return mIWXAPI.isWXAppInstalled();
    }

    private void startFailed(Activity activity, ShareListener listener, Exception e) {
        activity.finish();
        listener.doShareFailure(e);
    }

    @Override
    public void recycle() {
        mIWXAPI.detach();
    }

    private void sendMessage(int platform, WXMediaMessage message, String transaction) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = message;
        req.scene = platform == SharePlatform.WX_TIMELINE ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        mIWXAPI.sendReq(req);
    }

    private String buildTransaction(String type) {
        return System.currentTimeMillis() + type;
    }

}
