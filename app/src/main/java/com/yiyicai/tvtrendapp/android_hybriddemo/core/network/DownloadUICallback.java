package com.yiyicai.tvtrendapp.android_hybriddemo.core.network;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ei8Z on 2017/6/2.
 */

public abstract class DownloadUICallback extends UICallback{

    @Override
    public void onResponseInUI(Call call, Response response) throws IOException {

        if (response.isSuccessful()){
            // 判断类为null
            if (response==null){
                throw new RuntimeException("Fatal Api Error In DownloadUICallback InputStream Is Null");
            }
            // 判断是不是真正的拿到数据了
            if (response!=null){
                // 成功，数据也有
                onBusinessInputstrea(true,response);
            }else {
                onBusinessInputstrea(false,response);
            }
        }
    }

    @Override
    public void onBusinessResponse(boolean isSucces, ResponseEntity responseEntity) {
        // TODO: 2017/6/2 由于下载callback只需要输入流，固该方法无用，先实现，避免实例化的时候出现，干扰逻辑
    }

    public abstract void onBusinessInputstrea(boolean isSucces, Response response) ;
}
