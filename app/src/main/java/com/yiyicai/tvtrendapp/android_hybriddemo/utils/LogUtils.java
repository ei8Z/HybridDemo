package com.yiyicai.tvtrendapp.android_hybriddemo.utils;

import android.util.Log;

/**
 * Created by ei8Z on 2017/5/19.
 */

public class LogUtils {
    private static boolean isShow = false;

    public static void setShow(boolean show) {
        isShow = show;
    }

    public static void show(String key, String discribe, String type) {
        if (!isShow) {
            return;
        }
        key = "MyTest" + key;
        switch (type) {
            case "e":
                show_E(key, discribe);
                break;
            default:
                show_I(key, discribe);
                break;
        }
    }

    private static void show_I(String key, String discribe) {
        Log.i(key, discribe);
    }

    private static void show_E(String key, String discribe) {
        Log.e(key, discribe);
    }
}
