package com.astana.learnopengl.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * 权限请求工具类
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/27
 */
public class PermissionUtils {
    /**
     * 请求权限
     *
     * @param context
     * @param permissions
     * @param req
     * @param runnable
     */
    public static void askPermission(Activity context, String[] permissions, int req, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ActivityCompat.checkSelfPermission(context, permissions[0]);
            if (result == PackageManager.PERMISSION_GRANTED) {
                runnable.run();
            } else {
                ActivityCompat.requestPermissions(context, permissions, req);
            }
        } else {
            runnable.run();
        }
    }

    /**
     * onRequestResult回调
     *
     * @param isReq
     * @param grantResults
     * @param okRun
     * @param deniRun
     */
    public static void onRequestPermissionsResult(boolean isReq, int[] grantResults, Runnable okRun, Runnable deniRun) {
        if (isReq) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                okRun.run();
            } else {
                deniRun.run();
            }
        }
    }

}
