package com.bop.zz.utils;

import java.util.ArrayList;
import java.util.HashSet;

public class ListUtils {
	/**两个整数集求差集
	 * @param <T>**/ 
	public static <T> ArrayList<T> integerArrayListDifference(ArrayList<T> arraylist1, ArrayList<T> arraylist2) {
		arraylist1.removeAll(arraylist2);
		return arraylist1;
	}

	/**两个整数集求并集
	 * @param <T>**/ 
	public static <T> ArrayList<T> integerArrayListUnion(ArrayList<T> arraylist1, ArrayList<T> arraylist2) {
		ArrayList<T> arraylist = new ArrayList<T>();
		arraylist.addAll(arraylist1);
		arraylist.addAll(arraylist2);
		arraylist = new ArrayList<T>(new HashSet<T>(arraylist));
		return arraylist;
	}

	/**两个整数集求交集
	 * @param <T>**/ 
	public static <T> ArrayList<T> integerArrayListIntersections(ArrayList<T> arraylist1,
			ArrayList<T> arraylist2) {
		arraylist1.retainAll(arraylist2);
		return arraylist1;
	}
}
