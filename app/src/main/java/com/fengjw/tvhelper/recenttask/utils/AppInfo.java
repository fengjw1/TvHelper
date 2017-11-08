package com.fengjw.tvhelper.recenttask.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by fengjw on 2017/8/8.
 */

public class AppInfo {
    private String app_name;
    private String file_name;
    private String pkg_name;
    private String ver_name;
    private int ver_code;
    private String url;
    private String MD5;
    private String description;
    private int type;

    private Drawable icon;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

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

    public int getVerCode() {
        return ver_code;
    }

    public void setVerCode(int verCode) {
        this.ver_code = verCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
