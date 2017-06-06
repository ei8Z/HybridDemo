package com.yiyicai.tvtrendapp.android_hybriddemo.core.network;

import android.util.Log;

import com.google.gson.Gson;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.HybridConfig;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 单例化
 * Created by ei8Z on 2017/2/23.
 */
public class NativeClient {

    final String TAG = "NativeClient";
    private static NativeClient shopClient;
    private OkHttpClient okHttpClient;
    private Gson mGson;

    public static synchronized NativeClient getInstance(){
        if (shopClient==null){
            shopClient=new NativeClient();
        }
        return shopClient;
    }

    private NativeClient(){
        mGson = new Gson();
        //日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //构造器构建okhttpclient
        okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    //请求版本信息
    public Call requestVersion(){
        Request request =new Request.Builder()
                .get()
                .url(HybridConfig.VERSION_HOST)
                .build();
        return okHttpClient.newCall(request);
    }


    // 同步：直接拿到response里面的实体类数据
    public <T extends ResponseEntity>T excute(ApiInterface apiInterface) throws IOException{
        Response response = newApiCall(apiInterface,null).execute();
        Class<T> clazz = (Class<T>) apiInterface.getResponseEntity();
        return getResponseEntity(response,clazz);
    };
    // 异步回调：最后要创建UICallBack

    public Call enqueue(ApiInterface apiInterface,UICallback uiCallback,String tag){
        // 构建call模型
        Call call = newApiCall(apiInterface,tag);
        // 告诉uicallback里面的数据要转换的类型

        uiCallback.setResponseType(apiInterface.getResponseEntity());
        // 为了规范，我们在方法里面直接执行异步方法，就需要一个UiCallback，所以通过参数传递
        Log.i(TAG, "enqueue: 开始获取");
        call.enqueue(uiCallback);
        return call;
    }

    // 根据响应Response，将响应体转换成响应的实体类
    public  <T extends ResponseEntity>T getResponseEntity(Response response, Class<T> clazz) throws IOException {
        // 没有成功
        if (!response.isSuccessful()){
            throw new IOException("Response code is"+response.code());

        }
        // 成功，转换成相应的实体类了
        return mGson.fromJson(response.body().string(),clazz);
    }
    // 根据参数构建请求
    private Call newApiCall(ApiInterface apiInterface,String tag){
        Request.Builder builder = new Request.Builder();
        builder.url(apiInterface.getPath());

        //如果有请求体的话
        if (apiInterface.getRequestParam()!=null){
            String json = mGson.toJson(apiInterface.getRequestParam());
            RequestBody requestBody = new FormBody.Builder()
                    .add("json",json)
                    .build();
            builder.post(requestBody);
        }
        builder.tag(tag);
        Request request = builder.build();
        return  okHttpClient.newCall(request);
    }

    /** 通过给请求设置Tag，然后取消的时候根据判断Tag来取消:tag，构建请求的时候给请求设置的。
     * 1. 给请求设置tag
     * 2. 取消的方法中根据tag来取消
     */
    public void CancelByTag(String tag){
        // 1. 在调度器中等待执行的---> 2. 调度器中正在执行的
        for (Call call:okHttpClient.dispatcher().queuedCalls()){
            if (call.request().tag().equals(tag)){
                call.cancel();
            }
        }
        for (Call call:okHttpClient.dispatcher().runningCalls()){
            if (call.request().tag().equals(tag)){
                call.cancel();
            }
        }

    }
}
