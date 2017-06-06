package com.yiyicai.tvtrendapp.android_hybriddemo.core.network;

/**
 * Created by ei8Z on 2017/6/6.
 */

public interface OnDownloadListener {

        /**
         * 开始下载
         */
        void onDownloadStart();
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
}
