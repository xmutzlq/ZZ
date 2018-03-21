package com.bop.zz.share.share.instance;

import com.bop.zz.share.share.ShareImageObject;
import com.bop.zz.share.share.ShareListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface ShareInstance {

    void shareText(int platform, String text, Activity activity, ShareListener listener);

    void shareMedia(int platform, String title, String targetUrl, String summary,
            ShareImageObject shareImageObject, Activity activity, ShareListener listener);

    void shareImage(int platform, ShareImageObject shareImageObject, Activity activity,
            ShareListener listener);

    void handleResult(Intent data);

    boolean isInstall(Context context);

    void recycle();
}
