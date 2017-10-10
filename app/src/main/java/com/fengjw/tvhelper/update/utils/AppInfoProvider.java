package com.fengjw.tvhelper.update.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fengjw on 2017/8/8.
 */

public class AppInfoProvider {
    private PackageManager packageManager;
    public AppInfoProvider(Context context){
        packageManager = context.getPackageManager();
    }

    /*
    获取所有应用信息，并保存到list中。
     */
    public List<AppInfo> getAllApps(){
        List<AppInfo> list = new ArrayList<>();
        AppInfo myAppInfo;

        //
        List<PackageInfo> packageInfos = packageManager
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo info : packageInfos){
            myAppInfo = new AppInfo();

            //
            String packageName = info.packageName;

            ApplicationInfo appInfo = info.applicationInfo;

            Drawable icon = appInfo.loadIcon(packageManager);

            String appName = appInfo.loadLabel(packageManager).toString();

            String verName = info.versionName;

            int verCode = info.versionCode;

            myAppInfo.setApp_name(appName);
            myAppInfo.setPkg_name(packageName);
            myAppInfo.setVer_name(verName);
            myAppInfo.setVerCode(verCode);
            myAppInfo.setIcon(icon);
            list.add(myAppInfo);
        }
        return list;
    }

}
