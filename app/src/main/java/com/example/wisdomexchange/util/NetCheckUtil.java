package com.example.wisdomexchange.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Author: PengLiang
 * @Time: 2020/11/21
 * @Description: 检查网络状态工具类
 */
public class NetCheckUtil {

    public static boolean checkNet(Context context) {
        // 判断是否具有可以用于通信渠道
        boolean mobileConnection = isMobileConnection(context);
        boolean wifiConnection = isWIFIConnection(context);
        if (!mobileConnection && !wifiConnection) {
            // 没有网络
            return false;
        }
        return true;
    }


    /**
     * 判断手机接入点（APN）是否处于可以使用的状态
     *
     * @param context
     */
    public static boolean isMobileConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前wifi是否是处于可以使用状态
     *
     * @param context
     */
    public static boolean isWIFIConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
