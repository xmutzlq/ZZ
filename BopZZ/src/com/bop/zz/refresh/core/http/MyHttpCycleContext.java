package com.bop.zz.refresh.core.http;

import android.content.Context;

import cn.finalteam.okhttpfinal.HttpCycleContext;

public interface MyHttpCycleContext extends HttpCycleContext{
     Context getContext();
}
