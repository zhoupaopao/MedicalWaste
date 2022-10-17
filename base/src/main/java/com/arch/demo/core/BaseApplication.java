package com.arch.demo.core;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 *
 * @author pqc
 * @date 2020/7/1
 */
public class BaseApplication extends Application {

    public static BaseApplication sApplication;
    public static boolean sIsDebug;
    private static Context mContext;

    public static void setIsDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        mContext = this;
    }

    /**
     * 获取进程名
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }
    public static Context getAppContext() {
        return mContext;
    }
}
