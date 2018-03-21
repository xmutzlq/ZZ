package com.bop.zz.photo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import android.util.Log;

/**
 * 通用日志工具类
 * 
 * @author zlq
 */
public class LogUtils {

	private static String TAG = "DeHong";
	public static boolean DEBUG = false; // 日志开关,打包时需要设置该开关
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.ms");
	private static String LOG_FILE = "";

	public static void init(String tag, boolean debug, String logFile) {
		TAG = tag;
		DEBUG = debug;
		LOG_FILE = logFile;
	}

	public static void i(String msg) {
		i(TAG, msg);
	}

	public static void i(String tag, String msg) {
		i(tag, msg, null);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.i(tag, msg, tr);
		}
	}

	public static void v(String msg) {
		v(TAG, msg);
	}

	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void e(String msg) {
		e(TAG, msg);
	}

	public static void e(String tag, String msg) {
		e(tag, msg, null);
	}

	public static void e(String msg, Throwable tr) {
		e(TAG, msg, tr);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.e(tag, msg, tr);
		}
	}

}
