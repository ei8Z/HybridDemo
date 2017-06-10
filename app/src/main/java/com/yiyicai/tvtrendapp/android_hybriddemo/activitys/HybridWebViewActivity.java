package com.yiyicai.tvtrendapp.android_hybriddemo.activitys;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;

import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.DownloadUICallback;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.NativeClient;
import com.yiyicai.tvtrendapp.android_hybriddemo.entity.api.ApiRequestData;
import com.yiyicai.tvtrendapp.android_hybriddemo.params.HybridParamAjax;
import com.yiyicai.tvtrendapp.android_hybriddemo.params.HybridParamCallback;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Response;

/**
 * Created by vane on 16/6/3.
 */

public class HybridWebViewActivity extends HybridBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("vane", "HybridWebViewActivity onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView!=null){
            mWebView.pauseTimers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView!=null){
            mWebView.resumeTimers();
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("vane", "webview cangoback= " + mWebView.canGoBack());

        if ( mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.e("vane", "HybridWebViewActivity onCreate");
    }

    /**
     * ajax请求
     * @param msg
     */
    @Subscribe
    public void onEventMainThread(final HybridParamAjax msg) {
        LogUtils.show("HybridWebViewActivity_onEventMainThread","HybridParamAjax_ajax请求" + msg,"i");
        NativeClient.getInstance().enqueue(new ApiRequestData(msg.url) {
        }, new DownloadUICallback() {
            @Override
            public void onBusinessInputstrea(boolean isSucces, Response response) {
                if (TextUtils.isEmpty(msg.callback)) return;//如果callback无有效值则结束
                HybridParamCallback hybridParamCallback = new HybridParamCallback();
                hybridParamCallback.callback = msg.callback;
                try {
                    hybridParamCallback.data = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LogUtils.show("HybridWebViewActivity_onResponse","数据拿取成功：" + hybridParamCallback.data,"i");
                handleHybridCallback(hybridParamCallback);
            }
        },"");
    }

    /**
     * 将获取的数据回调给js
     * @param param
     */
    private void handleHybridCallback(final HybridParamCallback param) {
        LogUtils.show("HybridWebViewActivity_handleHybridCallback——回传的参数为：","HybridParamCallback" + param,"i");
        if (isDestroyed()) return;
        if (!TextUtils.isEmpty(param.callback)) {
            //这里将本地请求到的值转为json数据
            String script = "Hybrid.callback(" + new H5RequestEntity(param.callback, param.data).toString() + ")";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //如果版本大于4.4，Api_19则使用以下方法返回给H5页面
                mWebView.evaluateJavascript(script, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        LogUtils.show("HybridWebViewActivity_handleHybridCallback_onReceiveValue","使用evaluateJavascript调用js方法","i");
                        if (!"true".equals(value) && "back".equals(param.tagname))
                            onBackPressed();
                    }
                });
            } else {
                //以传统的方式返回给H5页面
                mWebView.loadUrl("javascript:Hybrid.callback(" + new H5RequestEntity(param.callback, param.data).toString() + ")");
            }

        } else if ("back".equals(param.tagname)) {
            onBackPressed();
        }
    }

    public static final class H5RequestEntity {

        public H5RequestEntity(String callback, String data) {
            this.data = data;
            this.callback = callback;
        }

        public String callback;
        public String data;

        @Override
        public String toString() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("callback", callback);
                jsonObject.put("data", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }
}
