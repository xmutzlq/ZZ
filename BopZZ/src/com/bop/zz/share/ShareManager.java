package com.bop.zz.share;

public class ShareManager {

    private static boolean isInit = false;

    public static ShareConfig CONFIG;

    public static void init(ShareConfig config) {
        isInit = true;
        CONFIG = config;
    }
}
