package com.yiyicai.tvtrendapp.android_hybriddemo.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 文件下载进度
 * Created by ei8Z on 2017/6/6.
 */

public class DownloadProgress {
    private long current = 0;//当前下载量
    private long total = 0;//总下载量
    private Lock lock = new ReentrantLock();// 锁对象
    private static DownloadProgress downloadProgress;
    private DownloadProgress(){}
    public synchronized static DownloadProgress getInstance(){
        if (downloadProgress==null){
            downloadProgress = new DownloadProgress();
        }
        return downloadProgress;
    }
    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        lock.lock();
        this.current = current;
        lock.unlock();
    }

    public long getTotal() {
        return total;
    }

    public void  setTotal(long total) {
        lock.lock();
        this.total = total;
        lock.unlock();
    }

    @Override
    public String toString() {
        return "DownloadProgress{" +
                "current=" + current +
                ", total=" + total +
                '}';
    }
}
