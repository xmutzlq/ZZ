package com.bop.zz.share.login.result;

import org.json.JSONException;
import org.json.JSONObject;

public class QQToken extends BaseToken {

    public static QQToken parse(JSONObject jsonObject) {
        QQToken token = new QQToken();
        try {
            token.setAccessToken(jsonObject.getString("access_token"));
            token.setOpenid(jsonObject.getString("openid"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return token;
    }

}
