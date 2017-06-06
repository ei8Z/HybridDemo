package com.yiyicai.tvtrendapp.android_hybriddemo.actions;

import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * 为js提供调用的方法所在的超类，所有的被调用的方法所在的类都应该继承该类
 * Created by vane on 16/6/2.
 */

public abstract class HybridAction {
    public static Gson mGson;

    static {
        //创建Gson解析类
        mGson = new GsonBuilder()
                //自定义的类型适配器
//                .registerTypeAdapter(HybridParamAnimation.class, new HybridParamAnimation.TypeDeserializer())
//                .registerTypeAdapter(HybridParamType.class, new HybridParamType.TypeDeserializer())
                .create();
    }

    public abstract void onAction(WebView webView, String params, String jsmethod);

}
