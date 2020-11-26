package com.example.wisdomexchange.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {

    // 创建一个Activity的列表
    public static List<Activity> activities = new ArrayList<>();

    // 添加activity到activities中去
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    // 将activity从activities中移除
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    // 销毁所有的Activity,也就是将activities中的activity都移除
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
