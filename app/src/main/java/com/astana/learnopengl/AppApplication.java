package com.astana.learnopengl;

import android.app.Application;

/**
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/27
 */
public class AppApplication extends Application {

    private static Application mBaseApplication = null;

    /**
     * 获取Application上下文
     * @return Application上下文
     */
    public static Application getApplication() {
        return mBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
    }

}
