package com.example.wisdomexchange.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdomexchange.R;

/**
 * @Author: PengLiang
 * @Time: 2020/11/23
 * @Description: 自己封装的Toast
 */
public class ToastUtil {

    public static Toast mToast;
    private static TextView msgTv;

    public static void show(Context context, String msg) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_item, null);
        if (mToast == null) {
            msgTv = view.findViewById(R.id.tv_toast);
            msgTv.setText(msg);
            mToast = new Toast(context);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(view);
        } else {
            msgTv.setText(msg);
        }
        mToast.show();
    }

    // 取消Toast（用于退出当前界面）
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
