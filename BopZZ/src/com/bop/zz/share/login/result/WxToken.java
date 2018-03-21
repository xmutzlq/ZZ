package com.bop.zz.share.login.result;

import org.json.JSONException;
import org.json.JSONObject;

public class WxToken extends BaseToken {

    private String refresh_token;

    public static WxToken parse(JSONObject jsonObject) throws JSONException {
        WxToken wxToken = new WxToken();
        wxToken.setOpenid(jsonObject.getString("openid"));
        wxToken.setAccessToken(jsonObject.getString("access_token"));
        wxToken.setRefreshToken(jsonObject.getString("refresh_token"));
        return wxToken;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public void setRefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
