package com.yiyicai.tvtrendapp.android_hybriddemo.entity;


import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.ResponseEntity;

import java.util.List;

/**
 * 服务器返回的版本信息实体类
 * Created by vane on 16/7/13.
 */

public class HybridVersionEntity extends ResponseEntity {

    /**
     * loadimg : http://192.168.0.200/demoapp/load.png
     * packages : [{"name":"增量更新包","version":"0.0.1","flagint":20170526001,"zipurl":"http://192.168.0.200/demoapp/pages_001.zip","addtime":1496315344}]
     */

    private String loadimg;
    private List<PackagesBean> packages;

    public String getLoadimg() {
        return loadimg;
    }

    public void setLoadimg(String loadimg) {
        this.loadimg = loadimg;
    }

    public List<PackagesBean> getPackages() {
        return packages;
    }

    public void setPackages(List<PackagesBean> packages) {
        this.packages = packages;
    }

    public static class PackagesBean {
        /**
         * name : 增量更新包
         * version : 0.0.1
         * flagint : 20170526001
         * zipurl : http://192.168.0.200/demoapp/pages_001.zip
         * addtime : 1496315344
         */

        private String name;
        private String version;
        private long flagint;
        private String zipurl;
        private int addtime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public long getFlagint() {
            return flagint;
        }

        public void setFlagint(long flagint) {
            this.flagint = flagint;
        }

        public String getZipurl() {
            return zipurl;
        }

        public void setZipurl(String zipurl) {
            this.zipurl = zipurl;
        }

        public int getAddtime() {
            return addtime;
        }

        public void setAddtime(int addtime) {
            this.addtime = addtime;
        }

        @Override
        public boolean equals(Object obj) {
            PackagesBean packagesBean = (PackagesBean) obj;
            if (!this.name.equals(packagesBean.getName())){
                return false;
            }else if (!this.getVersion().equals(packagesBean.getVersion())){
                return false;
            }else if (this.getFlagint() != packagesBean.getFlagint()){
                return false;
            }else {
                return true;
            }
        }

        @Override
        public String toString() {
            return "PackagesBean{" +
                    "name='" + name + '\'' +
                    ", version='" + version + '\'' +
                    ", flagint=" + flagint +
                    ", zipurl='" + zipurl + '\'' +
                    ", addtime=" + addtime +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HybridVersionEntity{" +
                "loadimg='" + loadimg + '\'' +
                ", packages=" + packages +
                '}';
    }
}
