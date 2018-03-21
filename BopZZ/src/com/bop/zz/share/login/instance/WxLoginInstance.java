package com.bop.zz.share.login.instance;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.bop.zz.share.ShareManager;
import com.bop.zz.share.login.LoginListener;
import com.bop.zz.share.login.LoginPlatform;
import com.bop.zz.share.login.LoginResult;
import com.bop.zz.share.login.result.BaseToken;
import com.bop.zz.share.login.result.WxToken;
import com.bop.zz.share.login.result.WxUser;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WxLoginInstance extends LoginInstance {

    public static final String SCOPE_USER_INFO = "snsapi_userinfo";
    private static final String SCOPE_BASE = "snsapi_base";

    private static final String BASE_URL = "https://api.weixin.qq.com/sns/";

    private IWXAPI mIWXAPI;

    private LoginListener mLoginListener;

    private OkHttpClient mClient;

    private boolean fetchUserInfo;

    public WxLoginInstance(Activity activity, LoginListener listener, boolean fetchUserInfo) {
        super(activity, listener, fetchUserInfo);
        mLoginListener = listener;
        mIWXAPI = WXAPIFactory.createWXAPI(activity, ShareManager.CONFIG.getWxId());
        mClient = new OkHttpClient();
        this.fetchUserInfo = fetchUserInfo;
    }

    @Override
    public void doLogin(Activity activity, LoginListener listener, boolean fetchUserInfo) {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = SCOPE_USER_INFO;
        req.state = String.valueOf(System.currentTimeMillis());
        mIWXAPI.sendReq(req);
    }

    @Override
    public void fetchUserInfo(final BaseToken token) {
        Observable.fromEmitter(new Action1<Emitter<WxUser>>() {
            @Override
            public void call(Emitter<WxUser> wxUserEmitter) {
                Request request = new Request.Builder().url(buildUserInfoUrl(token)).build();
                try {
                    Response response = mClient.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    WxUser user = WxUser.parse(jsonObject);
                    wxUserEmitter.onNext(user);
                } catch (IOException e) {
                    wxUserEmitter.onError(e);
                } catch (JSONException e) {
                	wxUserEmitter.onError(e);
                }
            }
        }, Emitter.BackpressureMode.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WxUser>() {
                    @Override
                    public void call(WxUser wxUser) {
                        mLoginListener.doLoginSuccess(
                                new LoginResult(LoginPlatform.WX, token, wxUser));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mLoginListener.doLoginFailure(new Exception(throwable));
                    }
                });
    }

    @Override
    public void handleResult(int requestCode, int resultCode, Intent data) {
        mIWXAPI.handleIntent(data, new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {
            }

            @Override
            public void onResp(BaseResp baseResp) {
                if (baseResp instanceof SendAuth.Resp && baseResp.getType() == 1) {
                    SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                    switch (resp.errCode) {
                        case BaseResp.ErrCode.ERR_OK:
                            getToken(resp.code);
                            break;
                        case BaseResp.ErrCode.ERR_USER_CANCEL:
                            mLoginListener.doLoginCancel();
                            break;
                        case BaseResp.ErrCode.ERR_SENT_FAILED:
                            mLoginListener.doLoginFailure(new Exception("wx_err_sent_failed"));
                            break;
                        case BaseResp.ErrCode.ERR_UNSUPPORT:
                            mLoginListener.doLoginFailure(new Exception("wx_err_unsupport"));
                            break;
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                            mLoginListener.doLoginFailure(new Exception("wx_auth_denied"));
                            break;
                        default:
                            mLoginListener.doLoginFailure(new Exception("wx_login_error"));
                    }
                }
            }
        });
    }

    @Override
    public boolean isInstall(Context context) {
        return mIWXAPI.isWXAppInstalled();
    }

    private void getToken(final String code) {
        Observable.fromEmitter(new Action1<Emitter<WxToken>>() {
            @Override
            public void call(Emitter<WxToken> wxTokenEmitter) {
                Request request = new Request.Builder().url(buildTokenUrl(code)).build();
                try {
                    Response response = mClient.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    WxToken token = WxToken.parse(jsonObject);
                    wxTokenEmitter.onNext(token);
                } catch (IOException e) {
                    wxTokenEmitter.onError(e);
                } catch (JSONException e) {
                	wxTokenEmitter.onError(e);
                }
            }
        }, Emitter.BackpressureMode.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WxToken>() {
                    @Override
                    public void call(WxToken wxToken) {
                        if (fetchUserInfo) {
                            mLoginListener.beforeFetchUserInfo(wxToken);
                            fetchUserInfo(wxToken);
                        } else {
                            mLoginListener.doLoginSuccess(new LoginResult(LoginPlatform.WX, wxToken));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mLoginListener.doLoginFailure(new Exception(throwable.getMessage()));
                    }
                });
    }

    @Override
    public void recycle() {
        if (mIWXAPI != null) {
            mIWXAPI.detach();
        }
    }

    private String buildTokenUrl(String code) {
        return BASE_URL
                + "oauth2/access_token?appid="
                + ShareManager.CONFIG.getWxId()
                + "&secret="
                + ShareManager.CONFIG.getWxSecret()
                + "&code="
                + code
                + "&grant_type=authorization_code";
    }

    private String buildUserInfoUrl(BaseToken token) {
        return BASE_URL
                + "userinfo?access_token="
                + token.getAccessToken()
                + "&openid="
                + token.getOpenid();
    }
}
