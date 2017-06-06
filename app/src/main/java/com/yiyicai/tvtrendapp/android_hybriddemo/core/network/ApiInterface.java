package com.yiyicai.tvtrendapp.android_hybriddemo.core.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by gqq on 2017/3/3.
 */
// 将请求的path、请求参数、响应的数据类型做一个整体的管理。
//    Api的接口的抽象化，具体每一个实现类都代表一个服务器的接口
public interface ApiInterface {

    @NonNull String getPath();

    @Nullable
    RequestParam getRequestParam();

    @NonNull Class<? extends ResponseEntity> getResponseEntity();
}
