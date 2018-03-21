package com.bop.zz.refresh.core.http;

import com.bop.zz.photo.utils.ILogger;
import com.bop.zz.refresh.core.http.model.BaseApiResponse;

import android.text.TextUtils;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;

public abstract class MyBaseHttpRequestCallback<T extends BaseApiResponse> extends BaseHttpRequestCallback<T>{

    @Override
    protected void onSuccess(T t) {
    	if(!TextUtils.isEmpty(t.getResult())) {
    		int code = Integer.parseInt(t.getResult());
            if ( code == 1 ) {
                onLogicSuccess(t);
            } else {
                onLogicFailure(t);
            }
    	} else {
    		if(t.getData() != null) {
    			onLogicSuccess(t);
    		}
    	}
    }

    public abstract void onLogicSuccess(T t);

    public abstract void onLogicFailure(T t) ;
}
