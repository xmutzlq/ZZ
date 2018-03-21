package com.bop.zz;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.bop.zz.loader.FrescoImageLoader;
import com.bop.zz.photo.CoreConfig;
import com.bop.zz.photo.FunctionConfig;
import com.bop.zz.photo.GalleryFinal;
import com.bop.zz.photo.ThemeConfig;
import com.bop.zz.photo.utils.ILogger;
import com.bop.zz.share.ShareConfig;
import com.bop.zz.share.ShareManager;
import com.sina.weibo.sdk.utils.LogUtil;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.finalteam.okhttpfinal.Part;
import okhttp3.Interceptor;
import okhttp3.Headers;

/**
 * 微薄
 * App Key：579526287
 * App Secret：c202c948d0dbc0764965f18fdfbd9d2e
 * @author zlq
 * @date 2016年12月15日 下午12:00:57
 */
public class IApplication extends Application {

	private static IApplication application;
	
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //建议在application中配置
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        //设置主题
        ThemeConfig theme = ThemeConfig.CYAN;
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
        		.setMutiSelectMaxSize(99)
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(false)
                .setCropSquare(false)
                .setEnablePreview(false)
                .setForceCrop(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new FrescoImageLoader(this), theme)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        initOkHttpFinal();
        initShareConfig();
    }
    
    public static IApplication getInstance() {
		return application;
	}
    
    private void initOkHttpFinal() {
		try {
			Class<?> cl = Class.forName("cn.finalteam.okhttpfinal.ILogger");
			Object r = cl.newInstance();  
	        Field f = cl.getDeclaredField("DEBUG");  
	        f.setAccessible(true);  
	        f.set(r, true); 
		} catch (Exception e) {
			e.printStackTrace();
			ILogger.e("反射失败：" + e.getMessage());
		}  
    	
        List<Part> commomParams = new ArrayList<Part>();
        Headers commonHeaders = new Headers.Builder().build();

        List<Interceptor> interceptorList = new ArrayList<Interceptor>();
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setCommenParams(commomParams)
                .setCommenHeaders(commonHeaders)
                .setTimeout(35000)
                .setInterceptors(interceptorList)
                .setDebug(true);
        OkHttpFinal.getInstance().init(builder.build());
    }
    
    private void initShareConfig() {
    	// 初始化shareUtil
    	LogUtil.enableLog();
        ShareConfig config = ShareConfig.instance()
        		.debug(true)
                .qqId("1104996142")
                .weiboId("579526287")
                .wxId("wxcf0bc0a76a5c5a66")
                .wxSecret("wxcf0bc0a76a5c5a66");
        ShareManager.init(config);
    }
    
    @Override
    public void onTerminate() {
    	super.onTerminate();
    }
    
    @Override
    protected void attachBaseContext(Context base) {
    	super.attachBaseContext(base);
    }
}
