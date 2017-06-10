package com.yiyicai.tvtrendapp.android_hybriddemo.core;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yiyicai.tvtrendapp.android_hybriddemo.actions.HybridAction;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.LogUtils;

/**
 * Created by ei8Z on 2017/5/31.
 */

public class HybridWebViewClient extends WebViewClient{
    final String TAG = "HybridWebViewClient";
    private WebView webView;
    private String mFilterHost;
    public HybridWebViewClient(WebView webView) {
        this.webView = webView;
    }

    public void setmFilterHost(String mFilterHost) {
        this.mFilterHost = mFilterHost;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    /**
     * 内部做了版本判定，只有版本>=21才会执行,资源替换的方法
     */
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //如果版本大于等于21
            Log.i(TAG, "shouldInterceptRequest: 地址new：" + request.getUrl());
            if (interceptUrlHandle(view, request.getUrl())) {
                // TODO: 2017/6/9 如果是用于交互符合要求的请求
            } else {
                // TODO: 2017/6/9 不是用于交互符合要求的请求
            }
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    /**
     * 内部做了版本判定，只有版本<21才会执行,资源替换的方法
     */
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //如果版本小于21
            Log.i(TAG, "shouldInterceptRequest: 地址old：" + url);
            if (interceptUrlHandle(view, Uri.parse(url))) {
                // TODO: 2017/6/9 如果是用于交互符合要求的请求
            } else {
                // TODO: 2017/6/9 不是用于交互符合要求的请求
            }
        }
        return super.shouldInterceptRequest(view, url);
    }

    public boolean interceptUrlHandle(final WebView view, Uri parse){
        String scheme = parse.getScheme();
        LogUtils.show("HyBridWebViewClient_shouldOverrideUrlLoading","当前请求：" + parse+ "当前Scheme:" + parse.getScheme(),"i");
        if (HybridConfig.SCHEME.equals(scheme)||"medmedlinkerhybrid".equals(scheme)) {
            LogUtils.show("HyBridWebViewClient_shouldOverrideUrlLoading","Scheme条件满足" +scheme,"i");
            String host = parse.getHost();
            String param = parse.getQueryParameter(HybridConstant.GET_PARAM);
            String callback = parse.getQueryParameter(HybridConstant.GET_CALLBACK);
            LogUtils.show("HyBridWebViewClient_shouldOverrideUrlLoading","host值：" + host+"  param值：" + param+" callback值：" + callback,"i");
            //依据host获取对应方法，如果为空，需要返回 Erro：没有该接口
            if (null == HybridConfig.TagnameMapping.mapping(host)) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(view.getContext(), "没有该接口", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            try {
                hybridDispatcher(host, param, callback);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        refreshLayoutListener.successRefresh();

    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        refreshLayoutListener.failRefresh();
    }

    /**
     * 开始反射获，调用相应的执行方法
     * @param method
     * @param params
     * @param jsmethod
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void hybridDispatcher(String method, String params, String jsmethod) throws IllegalAccessException, InstantiationException {
        LogUtils.show("HyBridWebViewClient_hybridDispatcher","进入解析器：" + "本地方法："+method+ "参数："+params+"回调JS方法：" +jsmethod ,"i");
        Class type = HybridConfig.TagnameMapping.mapping(method);
        //通过反射创建类
        HybridAction action = (HybridAction) type.newInstance();
        //调用被子类复写后的方法
        action.onAction(webView, params, jsmethod);
    }

    /**
     * 用于判断文件type
     * @param url
     * @return
     */
    private String getMimeType(String url) {
        if (url.contains(".")) {
            int index = url.lastIndexOf(".");
            if (index > -1) {
                int paramIndex = url.indexOf("?");
                String type = url.substring(index + 1, paramIndex == -1 ? url.length() : paramIndex);
                Log.i(TAG, "getMimeType: type" + type);
                switch (type) {
                    case "js":
                        return "text/javascript";
                    case "css":
                        return "text/css";
                    case "html":
                        return "text/html";
                    case "png":
                        return "image/png";
                    case "jpg":
                        return "image/jpg";
                    case "gif":
                        return "image/gif";
                }
            }
        }
        return "text/plain";
    }
    public RefreshLayoutListener refreshLayoutListener;
    public void setRefreshLayoutListener(RefreshLayoutListener refreshLayoutListener){
        this.refreshLayoutListener = refreshLayoutListener;
    }
    public interface RefreshLayoutListener{
        public void failRefresh();
        public void successRefresh();
    }
}
