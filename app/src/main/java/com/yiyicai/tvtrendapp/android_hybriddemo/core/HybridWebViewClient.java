package com.yiyicai.tvtrendapp.android_hybriddemo.core;

import android.net.Uri;
import android.util.Log;
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

//    /**
//     * 调用本地文件
//     * @param view
//     * @param url
//     * @return
//     */
//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        //url解析，获取到地址
//        Uri uri = Uri.parse(url);//转为Uri地址
//        Log.i(TAG, "shouldInterceptRequest: 文件请求地址：" + uri+"   mFilterHost："+mFilterHost+"uriHost：" + uri.getHost()+" path："+uri.getPath());
//        File storageDirectory = new File(FileUtil.getRootDir(view.getContext()), HybridConfig.FILE_HYBRID_DATA_PATH);
//        File unZip=null;
//        if (mFilterHost.equals(uri.getHost())){
//            //host相同，且文件存在
//            WebResourceResponse webResourceResponse = null;
//            try {
//                //调用到本地资源
//                String mimeType = getMimeType(url);
//                if(mimeType.equals("text/html")){
////                    return webResourceResponse;
//                    url = url.replace("appkj","page");
//                }else {
//                    url = url.replace("appkj","static");
//                }
//                uri = Uri.parse(url);
//                 unZip= new File(storageDirectory, uri.getPath());//获取到对应频道的解压目录文件夹
//
//                webResourceResponse = new WebResourceResponse(mimeType, "UTF-8", new FileInputStream(unZip));
//                Log.i(TAG, "shouldInterceptRequest: 文件已存在：" + unZip.getAbsolutePath() + "返回的类：" + webResourceResponse);
//                return webResourceResponse;w
//            } catch (IOException e) {
//                Log.e(TAG, "shouldInterceptRequest:文件未找到"+unZip.getAbsolutePath());
//                e.printStackTrace();
//            }
//        }
//        //否则直接回调父类方法
//        return super.shouldInterceptRequest(view, url);
//    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, String url) {
        Uri parse = Uri.parse(url);
        String scheme = parse.getScheme();
        LogUtils.show("HyBridWebViewClient_shouldOverrideUrlLoading","当前请求：" + url+ "当前Scheme:" + parse.getScheme(),"i");
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
                return super.shouldOverrideUrlLoading(view, url);
            }
            try {
                hybridDispatcher(host, param, callback);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return false;
        }
        view.loadUrl(url);
        return false;
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
}
