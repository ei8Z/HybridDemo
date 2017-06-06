package com.yiyicai.tvtrendapp.android_hybriddemo.entity;

import okhttp3.Response;

/**
 * Created by ei8Z on 2017/6/6.
 */

public class DownloadEntity {
    private Response response;
    private String zipFileName;
    private String zipFolderName;

    public DownloadEntity( ) {}

    public DownloadEntity(Response responseBody, String zipFileName, String zipFolderName) {
        this.response = responseBody;
        this.zipFileName = zipFileName;
        this.zipFolderName = zipFolderName;
    }

    public Response getResponseBody() {
        return response;
    }

    public void setResponseBody(Response response) {
        this.response = response;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }

    public String getZipFolderName() {
        return zipFolderName;
    }

    public void setZipFolderName(String zipFolderName) {
        this.zipFolderName = zipFolderName;
    }

    @Override
    public String toString() {
        return "DownloadEntity{" +
                "responseBody=" + response +
                ", zipFileName='" + zipFileName + '\'' +
                ", zipFolderName='" + zipFolderName + '\'' +
                '}';
    }
}
