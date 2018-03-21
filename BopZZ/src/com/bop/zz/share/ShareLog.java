package com.bop.zz.share;

import android.util.Log;

public class ShareLog {

    public static void i(String info) {
        if (ShareManager.CONFIG.isDebug()) {
            Log.i("share_util_log", info);
        }
    }

    public static void e(String error) {
        if (ShareManager.CONFIG.isDebug()) {
            Log.e("share_util_log", error);
        }
    }

}
