package com.yiyicai.tvtrendapp.android_hybriddemo.entity.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yiyicai.tvtrendapp.android_hybriddemo.core.HybridConfig;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.ApiInterface;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.RequestParam;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.ResponseEntity;
import com.yiyicai.tvtrendapp.android_hybriddemo.entity.HybridVersionEntity;

/**
 * Created by ei8Z on 2017/6/2.
 */

public class ApiRequestVersion implements ApiInterface {
    @NonNull
    @Override
    public String getPath() {
        return HybridConfig.VERSION_HOST;
    }

    @Nullable
    @Override
    public RequestParam getRequestParam() {
        return null;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return HybridVersionEntity.class;
    }
}
