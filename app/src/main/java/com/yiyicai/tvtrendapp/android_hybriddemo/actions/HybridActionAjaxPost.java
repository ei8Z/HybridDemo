package com.yiyicai.tvtrendapp.android_hybriddemo.actions;

import android.webkit.WebView;

import com.yiyicai.tvtrendapp.android_hybriddemo.params.HybridParamAjax;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.LogUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by vane on 16/6/2.
 */

public class HybridActionAjaxPost extends HybridAction {

    @Override
    public void onAction(final WebView webView, String params, final String jsmethod) {
        LogUtils.show("HybridActionAjaxPost_onAction:得到的值为：",params ,"i");
        HybridParamAjax hybridParam = mGson.fromJson(params, HybridParamAjax.class);
        hybridParam.tagname = HybridParamAjax.ACTION.POST;
        hybridParam.callback = jsmethod;
        EventBus.getDefault().post(hybridParam);
        //将结果回传给主线程
    }
}
