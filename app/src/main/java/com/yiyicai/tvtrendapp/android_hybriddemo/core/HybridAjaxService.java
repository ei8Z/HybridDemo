package com.yiyicai.tvtrendapp.android_hybriddemo.core;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.yiyicai.tvtrendapp.android_hybriddemo.actions.HybridAction;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.DownloadUICallback;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.NativeClient;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.OnDownloadListener;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.ResponseEntity;
import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.UICallback;
import com.yiyicai.tvtrendapp.android_hybriddemo.entity.DownloadEntity;
import com.yiyicai.tvtrendapp.android_hybriddemo.entity.HybridVersionEntity;
import com.yiyicai.tvtrendapp.android_hybriddemo.entity.api.ApiRequestData;
import com.yiyicai.tvtrendapp.android_hybriddemo.entity.api.ApiRequestVersion;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.DownloadProgress;
import com.yiyicai.tvtrendapp.android_hybriddemo.utils.FileUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * 拉取增量更新的部分？
 * Created by vane on 16/5/10.
 */
public class HybridAjaxService {
    public static OnDownloadListener onDownloadListener;//进度监听
    private static Context mContext;
    public static final String TAG = "HybridAjaxService";
    private static HashMap<String,ResponseBody> responseBodyMap;
    private static int updateCount;//当前所有需要更新的请求数量
    private static int updatecount_Connection;//当前已经连接完毕，等待数据下载的请求数量
    private static HashMap<String,DownloadEntity> downloadEntityMap;//保存所有已经连接的请求，以及相关数据
    private static class CompareVersion {
        HybridVersionEntity localVersion;
        HybridVersionEntity remoteVersion;

        public CompareVersion(HybridVersionEntity localVersion, HybridVersionEntity remoteVersion) {
            this.localVersion = localVersion;
            this.remoteVersion = remoteVersion;
        }
    }

    public static void checkVersion(final Context context) {
        Log.i("===", "checkVersion: 进入");
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>((Activity) context);
        onDownloadListener = (OnDownloadListener) activityWeakReference.get();
        mContext=context;
        NativeClient.getInstance().enqueue(new ApiRequestVersion(), new UICallback() {
            @Override
            public void onBusinessResponse(boolean isSucces, ResponseEntity responseEntity) {
                if (!isSucces) {
                    return;
                }
                if (responseEntity instanceof HybridVersionEntity) {
                    Log.i(TAG, "onBusinessResponse: 数据：" + responseEntity.toString());
                    HybridVersionEntity hybridVersionEntity = (HybridVersionEntity) responseEntity;
                    compareVersionFunction(hybridVersionEntity, context);
                }
            }
        }, "requestVersion");
    }

    /**
     * 与本地（如果有）保存的版本信息对比
     *
     * @param hybridVersionEntity
     * @param context
     */
    public static void compareVersionFunction(final HybridVersionEntity hybridVersionEntity, final Context context) {
        new AsyncTask<Void, Void, CompareVersion>() {
            @Override
            protected CompareVersion doInBackground(Void... params) {
                Log.i(TAG, "doInBackground: 版本信息：" + hybridVersionEntity);
                //2.对比本地保存是版本信息和服务器的版本信息是否一致
                HybridVersionEntity localVersion;//本地的版本信息
                HybridVersionEntity remoteVersion;//远程服务器上的版本信息
                //本地数据，参数一：文件路径，参数二：文件名
                File version = new File(FileUtil.getRootDir(context), HybridConfig.FILE_HYBRID_DATA_VERSION);
                if (!version.exists() || version.isDirectory() || TextUtils.isEmpty(FileUtil.readFile(version))) {
                    //若不存在
                    localVersion = null;
                    //3.本地保存版本信息，
                    Log.i(TAG, "doInBackground: 本地文件不存在,将文件写入本地");
                    File target = FileUtil.rebuildFile(FileUtil.getRootDir(context), HybridConfig.FILE_HYBRID_DATA_VERSION);
                    //将最新信息写入到本地文件
                    FileUtil.writeFile(target, HybridAction.mGson.toJson(hybridVersionEntity));
                    //读取本地文件
//                    String versionStr = FileUtil.readFile(version);
                    //得到远端版本，实际上多余的一步
//                    remoteVersion = new Gson().fromJson(versionStr, HybridVersionEntity.class);
                    remoteVersion = hybridVersionEntity;
                } else {
                    //拿到本地版本信息
                    localVersion = new Gson().fromJson(FileUtil.readFile(version), HybridVersionEntity.class);
                    //更新本地版本信息
                    Log.i(TAG, "doInBackground: 本地文件存在" + localVersion + "，更新");
                    File target = FileUtil.rebuildFile(FileUtil.getRootDir(context), HybridConfig.FILE_HYBRID_DATA_VERSION);
                    FileUtil.writeFile(target, HybridAction.mGson.toJson(hybridVersionEntity));
                    //拿到远端版本信息，这奇怪的写法
//                    String remoteVersionStr = FileUtil.readFile(target);
//                    remoteVersion = new Gson().fromJson(remoteVersionStr, HybridVersionEntity.class);
                    remoteVersion = hybridVersionEntity;
                }
                Log.i(TAG, "doInBackground: 执行完毕");
                return new CompareVersion(localVersion, remoteVersion);
            }

            @Override
            protected void onPostExecute(CompareVersion compareVersion) {
                Log.i(TAG, "onPostExecute: 开始比对版本信息");
                compareVersion(compareVersion.localVersion, compareVersion.remoteVersion);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 下载web业务包
     */
    private static void compareVersion(HybridVersionEntity localVersion, final HybridVersionEntity remoteVersion) {
        ArrayList<HybridVersionEntity.PackagesBean> localList;
        if(localVersion != null) {
             localList= (ArrayList<HybridVersionEntity.PackagesBean>) localVersion.getPackages();
        }else {
            localList=new ArrayList<>();
        }
        ArrayList<HybridVersionEntity.PackagesBean> remoteList = (ArrayList<HybridVersionEntity.PackagesBean>) remoteVersion.getPackages();
        for (int i = 0; i < remoteList.size(); i++) {
            HybridVersionEntity.PackagesBean remoteBean = remoteList.get(i);
            String name = remoteBean.getName().substring(remoteBean.getName().indexOf("_")+1);
            Log.i(TAG, "compareVersion: name:" + name);
            if (i > localList.size() - 1) {
                Log.i(TAG, "compareVersion: 更新包数量大于本地包数量，接下来所有多余的包直接更新");
                zipToSdcard(remoteBean.getZipurl(),name+".zip",name);
                updateCount++;
            } else {
                HybridVersionEntity.PackagesBean localBean = localList.get(i);
                Log.i(TAG, "compareVersion: 比对增量包：" + localBean.getName());
                //比对
                if (remoteBean.equals(localBean)) {
                    Log.i(TAG, "compareVersion: 本地包线上包对比并无差异");
                } else {
                    Log.i(TAG, "compareVersion: 本地包线上包对比失败，需要更新");
                    zipToSdcard(remoteBean.getZipurl(),name+".zip",name);
                    updateCount++;
                }
            }
            if (i == remoteList.size() - 1 && localList.size() - 1 > i) {
                //如果远程版本比对到最后，本地还有多余的包则全部删除
                Log.i(TAG, "compareVersion: 本地存在多余包，需要删除");
                File storageDirectory = new File(FileUtil.getRootDir(mContext), HybridConfig.FILE_HYBRID_DATA_PATH);
                for (int i1 = i + 1; i1 < localList.size(); i1++) {
                    HybridVersionEntity.PackagesBean deletePackageBean = localList.get(i1);
                    Log.i(TAG, "compareVersion: 删除本地多余的包，包名：" + deletePackageBean.getName());
                    File zip = new File(storageDirectory, deletePackageBean.getName()+".zip");//获取到zip包文件
                    File unZip = new File(storageDirectory, deletePackageBean.getName());//获取到对应zip解压后的解压目录文件夹
                    FileUtil.deleteFile(unZip);//删除解压缩后的文件
                    FileUtil.deleteFile(zip);//删除压缩包
                }
            }
        }
        if(updateCount==0){
            //说明没有需要更新的
            onDownloadListener.onDownloadFailed();
        }
    }

    /**
     * 完成下载和启动写入的方法
     * 当比对失败时需要执行该方法进行更新
     * @param url
     * @param zipFileName
     * @param zipFolderName
     */
    private static void zipToSdcard(final String url, final String zipFileName, final String zipFolderName) {
        final Uri uri = Uri.parse(url);
        NativeClient.getInstance().enqueue(new ApiRequestData(url), new DownloadUICallback() {
            @Override
            public void onBusinessInputstrea(boolean isSucces, Response response) {
                if (downloadEntityMap==null){
                    downloadEntityMap = new HashMap<String, DownloadEntity>();
                }
                //拿到输入流
                Log.i(TAG, "onBusinessInputstrea: 开始从服务器拉取增量包完毕");
                DownloadProgress progress = DownloadProgress.getInstance();
                progress.setTotal(progress.getTotal() + response.body().contentLength());//当前总下载量+新增的总下载量
                downloadEntityMap.put(url,new DownloadEntity(response,zipFileName,zipFolderName));
                updatecount_Connection++;
                //判断当前连接数量和连接数量，判断是否所有需要更新的包都已经连接成功可以开始同意下载，便利管理
                if (updatecount_Connection==updateCount) {
                    onDownloadListener.onDownloadStart();
                    for (DownloadEntity entity:downloadEntityMap.values()) {
                        unZipFile(entity.getResponseBody(), entity.getZipFileName(), entity.getZipFolderName());//统一开始数据请求
                    }
                    Log.i(TAG, "compareVersion: 所有文件更新，解压缩完毕");
                    onDownloadListener.onDownloadSuccess();
                }

            }
        }, "requestUpdataZip");
    }

    /**
     * 拿到更新的数据，开始更新压缩包，及解压缩到指定目录，
     * 通过删除相应的旧文件再替换上去完成
     * @param response
     * @param zipFileName
     * @param zipFolderName
     */
    private static void unZipFile(Response response, String zipFileName, String zipFolderName) {
        File storageDirectory = new File(FileUtil.getRootDir(mContext), HybridConfig.FILE_HYBRID_DATA_PATH);
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs();//创建解压目录
        }
        File zip = FileUtil.rebuildFile(storageDirectory, zipFileName);
        Log.i(TAG, "unZipFile: 开始写入本地文件：" + zip.getAbsolutePath());
        ResponseBody responseBody = response.body();
        FileUtil.writeFile(zip, responseBody, onDownloadListener);//写入到本地zip文件

        File unZip = new File(storageDirectory, zipFolderName);//获取到对应频道的解压目录文件夹
        if (unZip.exists()) {
            //文件存在则删除指定文件，进行更新
            Log.i(TAG, "unZipFile: 删除解压缩目录下的旧文件：" + unZip.getAbsolutePath());
            FileUtil.clearFolder(unZip);
        } else {
            //否则创建文件夹目录
            Log.i(TAG, "unZipFile: 解压缩的目录还未存在，开始创建目录");
            unZip.mkdirs();
        }
        FileUtil.unZip(zip.getAbsolutePath(), unZip.getAbsolutePath());//解压缩到指定目录
        Log.i(TAG, "unZipFile: 数据解压缩完毕");
    }
}
