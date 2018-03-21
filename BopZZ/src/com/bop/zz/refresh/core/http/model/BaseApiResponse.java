package com.bop.zz.refresh.core.http.model;

import com.bop.zz.photo.utils.ILogger;
import com.bop.zz.photo.utils.LogUtils;

public class BaseApiResponse<T> {
    private String result;
    private String msg;
    private T data;

    public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
    	ILogger.e(msg);
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "BaseApiResponse [result=" + result + ", msg=" + msg + ", data=" + data.toString() + "]";
	}
}
