package com.bop.zz.refresh.core.http.model;

import android.text.TextUtils;

public class MyAlbumsInfo {

	public String id; // 资讯id

	public String title; // 标题

	public String addtime; // 日期

	public String source; // 内容
	
	/**类型 1系统 2交易 3活动**/
	public int access;
	
	public String logo;

	@Override
	public String toString() {
		return "MyAlbumsInfo [id=" + id + ", title=" + title + ", addtime=" + addtime + ", source=" + source
				+ ", access=" + access + ", logo=" + logo + "]";
	}
}

