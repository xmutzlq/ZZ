package com.bop.zz.refresh.core.http.params;

import com.bop.zz.refresh.core.http.Api;
import com.bop.zz.refresh.core.http.ParamsConstructor;

import android.content.Context;

public class MyAlbumsParams extends ParamsConstructor{

	public MyAlbumsParams(Context context, Object... objects) {
		super(context, objects);
	}

	@Override
	protected void addParams(Object... objects) {
		mPostValues.put("cat_type", "1");
		mPostValues.put("p", (String)objects[0]);
		mPostValues.put("uid", "");
	}

	@Override
	protected String getUrl() {
		return Api.MY_ALBUMS;
	}
}
