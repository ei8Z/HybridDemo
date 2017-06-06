package com.yiyicai.tvtrendapp.android_hybriddemo;

import android.os.Bundle;
import android.util.Log;

import com.yiyicai.tvtrendapp.android_hybriddemo.activitys.HybridWebViewActivity;

import java.io.File;

public class MainActivity extends HybridWebViewActivity {
    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: tab.js文件存在吗：" + new File("/storage/sdcard/Android/data/com.yiyicai.tvtrendapp.android_hybriddemo/files/hybrid_webapp/appkj/views/index.html").exists());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUrl("http://192.168.0.106:8020/appkj/views/index.html");
    }
}
//当前文件：ggdetail.html解压缩完毕，解压路径：/storage/sdcard/Android/data/com.yiyicai.tvtrendapp.android_hybriddemo/files/hybrid_webapp/page/views/ggdetail.html
//查询地址：/storage/sdcard/Android/data/com.yiyicai.tvtrendapp.android_hybriddemo/files/hybrid_webapp/appkj/views/index.html
//当前文件：main1.js解压缩完毕，解压路径：/storage/sdcard/Android/data/com.yiyicai.tvtrendapp.android_hybriddemo/files/hybrid_webapp/static/static/js/main1.js
//查询地址：/storage/sdcard/Android/data/com.yiyicai.tvtrendapp.android_hybriddemo/files/hybrid_webapp/appkj/static/js/main.js