package com.bop.zz.refresh.core.http;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.bop.zz.refresh.core.Global;
import com.bop.zz.refresh.core.utils.MD5Util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import cn.finalteam.okhttpfinal.RequestParams;

/**
 * 重新封装Http-Params请求
 * @author zlq
 * @date 2016年12月26日 下午4:08:11
 */
public abstract class ParamsConstructor extends RequestParams {
	
	public PostValues mPostValues;
	
	public ParamsConstructor(Context context, Object... objects) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		String DEVICE_ID = tm.getDeviceId(); 
		mPostValues = new PostValues();
		mPostValues.put("device_code", DEVICE_ID);
		mPostValues.put("version_code", "36");
		addParams(objects);
		checkParam(getUrl());
		// 时间戳
		Long current = System.currentTimeMillis() / 1000;
		Long mTimestamp = current;
		
		int index = (int) (mTimestamp / 10 % 10);
		mPostValues.put("stamp", mTimestamp + "");
		if (mPostValues.postValues().containsKey("bd_sig")) {
			mPostValues.postValues().remove("bd_sig");
		}
		//构建MD5加密
		Set<Entry<String, String>> entrySet = mPostValues.postValues().entrySet();
		// Mac 参数构建macStr
		StringBuilder macStr = new StringBuilder();
		for (Map.Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (TextUtils.isEmpty(value)) {
				value = "";
			}
			macStr.append(key);
			macStr.append("=");
			macStr.append(value);
			macStr.append("&");
		}
		macStr.deleteCharAt(macStr.length() - 1);
		String token_key = "";
		token_key = Global.token_key[index];
		macStr.append(token_key);
		String md5Str = MD5Util.getMD5String(macStr.toString());
		mPostValues.postValues().remove("a");
		mPostValues.postValues().remove("m");
		mPostValues.put("bd_sig", md5Str);
		prepareParams();
	}
	
	protected abstract void addParams(Object... objects);
	protected abstract String getUrl();
	
	private void prepareParams() {
		Iterator<String> ir = mPostValues.postValues().keySet().iterator();//获取hashMap的键值，并进行遍历
		while(ir.hasNext()){
			Object key = ir.next();
			addFormDataPart((String)key, mPostValues.postValues().get(key));
		}
	}
	
	/** 组装a,m参数**/
	private void checkParam(String url) {
		if(mPostValues != null) {
			if(mPostValues.postValues().containsKey("a")) {
				mPostValues.postValues().remove("a");
			}
			if(mPostValues.postValues().containsKey("m")) {
				mPostValues.postValues().remove("m");
			}
		}
		
		String contains[] = url.split("/");
		
		if(contains.length >= 2) {
			mPostValues.put("a", contains[contains.length - 1]);
			mPostValues.put("m", contains[contains.length - 2]);
		} else {
		}
	}
}
