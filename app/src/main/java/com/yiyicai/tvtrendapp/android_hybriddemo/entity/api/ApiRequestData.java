package com.yiyicai.tvtrendapp.android_hybriddemo.entity.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.ApiInterface;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.RequestParam;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.ResponseEntity;

/**
 * Created by ei8Z on 2017/6/3.
 */

public class ApiRequestData implements ApiInterface{
    private String url = "";
    public ApiRequestData(String url) {
        this.url = url;
    }

    @NonNull
    @Override
    public String getPath() {
        return url;
    }

    @Nullable
    @Override
    public RequestParam getRequestParam() {
        return null;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return null;
    }
}
