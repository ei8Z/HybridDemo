package com.yiyicai.tvtrendapp.android_hybriddemo.actions;

import android.webkit.WebView;

import com.yiyicai.tvtrendapp.android_hybriddemo.params.HybridParamAjax;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;


/**
 * Created by vane on 16/6/2.
 */

public class HybridActionAjaxGet extends HybridAction {

    @Override
    public void onAction(WebView webView, String params, String jsmethod) {
        HybridParamAjax hybridParam = mGson.fromJson(params, HybridParamAjax.class);
        hybridParam.callback = jsmethod;
        LogUtils.show("HybridActionAjaxGet_onAction:得到的值为：",hybridParam.toString() ,"i");
        EventBus.getDefault().post(hybridParam);
        //将结果回传给主线程
    }
}
