package com.bop.zz.refresh.core.http;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * 封装PostValue
 * @author zlq
 * @date 2016年11月17日 下午2:16:25
 */
public class PostValues {
	private TreeMap<String, String> mPostValues; //组装器
	private List<String> filterMap; //过滤器
	
	public PostValues() {
		mPostValues = new TreeMap<String, String>(new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				return lhs.compareTo(rhs);
			}
		});
		filterMap = new ArrayList<String>();
		prepareFilters();
	}
	
	public TreeMap<String, String> postValues() {
		return mPostValues;
	}
	
	public void put(String key, String value) {
		mPostValues.put(key, value);
	}
	
	private void prepareFilters() {
		filterMap.add("m");
		filterMap.add("a");
	}
}
