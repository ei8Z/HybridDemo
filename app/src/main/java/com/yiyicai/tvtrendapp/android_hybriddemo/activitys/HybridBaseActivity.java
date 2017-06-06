package com.yiyicai.tvtrendapp.android_hybriddemo.activitys;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyicai.tvtrendapp.android_hybriddemo.R;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.HybridAjaxService;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.HybridWebViewClient;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.OnDownloadListener;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.DownloadProgress;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.LogUtils;

/**
 * Created by vane on 16/6/5.
 */

public class HybridBaseActivity extends AppCompatActivity implements OnDownloadListener{
    final String TAG="HybridBaseActivity";
    protected WebView mWebView;
    private TextView textView;
    public ProgressBar progressBar;
    protected HybridWebViewClient mWebViewClient;
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.WRITE_SETTINGS"};
    private static final int REQUEST_CODE = 0x11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.hybrid_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_download);
        textView = (TextView) findViewById(R.id.textView);
        initConfig(mWebView);
        //如果版本大于等于23就需要开启运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED&&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
                HybridAjaxService.checkVersion(this);
            }else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE); // without sdk version check
            }
        }else {
            HybridAjaxService.checkVersion(this);
        }
        LogUtils.setShow(true);
    }

    /**
     * 需要设置webview的属性则重写此方法
     * @param webView
     */
    protected void initConfig(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUserAgentString(settings.getUserAgentString() + " hybrid_1.0.0 ");
        mWebViewClient = new HybridWebViewClient(webView);
        webView.setWebViewClient(mWebViewClient);
        //注解约束的方式
//        webView.addJavascriptInterface(new HybridJsInterface(), HybridConfig.JSInterface);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    protected void loadUrl(String url) {
        Log.i(TAG, "loadUrl: 加载网页" + url);
        if (TextUtils.isEmpty(url)) return;
        //设置host，用于过滤
        mWebViewClient.setmFilterHost(Uri.parse(url).getHost());
        mWebView.loadUrl(url);
    }

    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= 17) {
            return super.isDestroyed();
        } else {
            return isFinishing();
        }
    }

    /**
     * 运行时权限回调接口
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                HybridAjaxService.checkVersion(this);
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDownloadStart() {
        progressBar.setMax((int) (DownloadProgress.getInstance().getTotal()/1024));//设置进度
    }

    @Override
    public void onDownloadSuccess() {
        Toast.makeText(this, "增量更新包，更新操作执行完毕", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloading(int progress) {
        progressBar.setProgress(progress);
        textView.setText(progress+"%");
    }

    @Override
    public void onDownloadFailed() {

    }
}
