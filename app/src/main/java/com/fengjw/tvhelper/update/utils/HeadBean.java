package com.fengjw.tvhelper.update.utils;

import java.util.List;

/**
 * Created by fengjw on 2017/8/17.
 */

public class HeadBean {
    /**
     * success : true
     * code : 1000
     * msg : success
     * describe : KTC应用升级
     * apklist : [{"app_name":"KTCLauncher","file_name":"KTCLauncher.apk","pkg_name":"com.android.mslauncher","ver_name":"2.0.0","ver_code":20,"apk_url":"http://ktcapkupdate.oss-cn-hangzhou.aliyuncs.com/6a838/launcher/PhilipsLauncher.apk","pic_url":"xxx","introduction":"主页升级测试","MD5":"0A7ACFCB23652A2075C2943275ADBBA8","update_type":1},{"app_name":"MTvPlayer","file_name":"MTvPlayer.apk","pkg_name":"com.mstar.tv.tvplayer.ui","ver_name":"3.0.0","ver_code":30,"apk_url":"http://ktcapkupdate.oss-cn-hangzhou.aliyuncs.com/6a838/launcher/PhilipsLauncher.apk","pic_url":"xxx","introduction":"TV升级测试","MD5":"0A7ACFCB23652A2075C2943275ADBBA8","update_type":2},{"app_name":"MLocalMM2","file_name":"MLocalMM2.apk","pkg_name":"com.jrm.localmm","ver_name":"3.0.0","ver_code":30,"apk_url":"http://ktcapkupdate.oss-cn-hangzhou.aliyuncs.com/6a838/launcher/PhilipsLauncher.apk","pic_url":"xxx","introduction":"修正某些错误","MD5":"0A7ACFCB23652A2075C2943275ADBBA8","update_type":1}]
     */

    private boolean success;
    private int code;
    private String msg;
    private String describe;
    private List<ApklistBean> apklist;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<ApklistBean> getApklist() {
        return apklist;
    }

    public void setApklist(List<ApklistBean> apklist) {
        this.apklist = apklist;
    }

    public static class ApklistBean {
        /**
         * app_name : KTCLauncher
         * file_name : KTCLauncher.apk
         * pkg_name : com.android.mslauncher
         * ver_name : 2.0.0
         * ver_code : 20
         * apk_url : http://ktcapkupdate.oss-cn-hangzhou.aliyuncs.com/6a838/launcher/PhilipsLauncher.apk
         * pic_url : xxx
         * introduction : 主页升级测试
         * MD5 : 0A7ACFCB23652A2075C2943275ADBBA8
         * update_type : 1
         */

        private String app_name;
        private String file_name;
        private String pkg_name;
        private String ver_name;
        private int ver_code;
        private String apk_url;
        private String pic_url;
        private String introduction;
        private String MD5;
        private int update_type;

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getPkg_name() {
            return pkg_name;
        }

        public void setPkg_name(String pkg_name) {
            this.pkg_name = pkg_name;
        }

        public String getVer_name() {
            return ver_name;
        }

        public void setVer_name(String ver_name) {
            this.ver_name = ver_name;
        }

        public int getVer_code() {
            return ver_code;
        }

        public void setVer_code(int ver_code) {
            this.ver_code = ver_code;
        }

        public String getApk_url() {
            return apk_url;
        }

        public void setApk_url(String apk_url) {
            this.apk_url = apk_url;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getMD5() {
            return MD5;
        }

        public void setMD5(String MD5) {
            this.MD5 = MD5;
        }

        public int getUpdate_type() {
            return update_type;
        }

        public void setUpdate_type(int update_type) {
            this.update_type = update_type;
        }
    }
}
