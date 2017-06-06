package com.yiyicai.tvtrendapp.android_hybriddemo.params;

import org.json.JSONObject;

/**
 * 数据拉取的参数类（实体类）
 * Created by vane on 16/6/9.
 */

public class HybridParamAjax {

    public ACTION tagname = ACTION.GET;
    public String url;
    public JSONObject param;// this param is json data
    public String callback;

    public enum ACTION {
        GET("get"), POST("post");

        public String mValue;

        ACTION(String value) {
            mValue = value;
        }

        public static ACTION findByAbbr(String value) {
            for (ACTION currEnum : ACTION.values()) {
                if (currEnum.mValue.equals(value)) {
                    return currEnum;
                }
            }
            return GET;
        }

    }

    @Override
    public String toString() {
        return "HybridParamAjax{" +
                "tagname=" + tagname +
                ", url='" + url + '\'' +
                ", param=" + param +
                ", callback='" + callback + '\'' +
                '}';
    }
}
