package com.yiyicai.tvtrendapp.android_hybriddemo.core;


import com.yiyicai.tvtrendapp.android_hybriddemo.actions.HybridActionAjaxGet;
import com.yiyicai.tvtrendapp.android_hybriddemo.actions.HybridActionAjaxPost;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.LogUtils;

import java.util.HashMap;

/**
 * Created by vane on 16/6/2.
 */

public class HybridConfig {

    public static final String SCHEME = "hybrid";
    public static final String ACTIONPRE = "medlinker.hybrid.";//配置是intent-filter中的action的前缀
//    public static final String VERSION_HOST = "http://yexiaochai.github.io/Hybrid/webapp/hybrid_ver.json";
    public static final String VERSION_HOST = "http://192.168.0.200:9090/demoapp/config";//版本更新对比地址
    public static final String FILE_HYBRID_DATA_VERSION = "hybrid_data_version";
    public static final String FILE_HYBRID_DATA_PATH = "hybrid_webapp";
    public static final String JSInterface = "HybridJSInterface";

    public static class TagnameMapping {
        //将所有方法调用的的action类放入map集合中
        //初次调用加载static中的内容
        private static HashMap<String, Class> mMap;
        static {
            mMap = new HashMap<>();
            //本地提供的所有方法的class对象map集合
//            mMap.put("forward", HybridActionForward.class);
//            mMap.put("showheader", HybridActionShowHeader.class);
//            mMap.put("updateheader", HybridActionUpdateHeader.class);
//            mMap.put("back", HybridActionBack.class);
//            mMap.put("showloading", HybridActionShowLoading.class);
            mMap.put("get", HybridActionAjaxGet.class);
            mMap.put("post", HybridActionAjaxPost.class);
        }
        public static Class mapping(String method) {
            LogUtils.show("HybridConfig_TagnameMapping_mapping","方法名key：" + method,"i");
            return mMap.get(method);
        }
    }

    public static class IconMapping {
        private static HashMap<String, Integer> mMap;

        static {
            mMap = new HashMap<>();
//            mMap.put("back", R.drawable.ic_back);//头部返回按钮图标
        }

        public static int mapping(String icon) {
            LogUtils.show("HybridConfig_IconMapping_mapping","图标key：" + icon,"i");
            boolean has = mMap.containsKey(icon);
            if (!has) return -1;
            return mMap.get(icon);
        }
    }
}
