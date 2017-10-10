package com.fengjw.tvhelper.stop.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

import com.android.settingslib.applications.ApplicationsState;

/**
 * Created by fengjw on 2017/10/10.
 */

public class StopAppInfo {
    private final Object mLock = new Object();
    private final Context mContext;
    private ApplicationsState.AppEntry mEntry;

    public StopAppInfo(Context context, ApplicationsState.AppEntry entry) {
        mContext = context;
        mEntry = entry;
    }

    public void setEntry(ApplicationsState.AppEntry entry) {
        synchronized (mLock) {
            mEntry = entry;
        }
    }

    public String getName() {
        synchronized (mLock) {
            mEntry.ensureLabel(mContext);
            return mEntry.label;
        }
    }

    public String getSize() {
        synchronized (mLock) {
            return mEntry.sizeStr;
        }
    }

    public Drawable getIconResource() {
        synchronized (mLock) {
            return mEntry.icon;
        }
//        synchronized (mLock){
//            return mEntry.info.icon;
//        }//原来的写法
    }

    public String getPackageName() {
        synchronized (mLock) {
            return mEntry.info.packageName;
        }
    }

    public ApplicationInfo getApplicationInfo() {
        synchronized (mLock) {
            return mEntry.info;
        }
    }

    public boolean isStopped() {
        synchronized (mLock) {
            return (mEntry.info.flags & ApplicationInfo.FLAG_STOPPED) != 0;
        }
    }

    public boolean isInstalled() {
        synchronized (mLock) {
            return (mEntry.info.flags & ApplicationInfo.FLAG_INSTALLED) != 0;
        }
    }

    public boolean isUpdatedSystemApp() {
        synchronized (mLock) {
            return (mEntry.info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
        }
    }

    public boolean isEnabled() {
        synchronized (mLock) {
            return mEntry.info.enabled;
        }
    }

    public boolean isSystemApp() {
        synchronized (mLock) {
            return (mEntry.info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        }
    }

    public String getCacheSize() {
        synchronized (mLock) {
            return Formatter.formatFileSize(mContext, mEntry.cacheSize + mEntry.externalCacheSize);
        }
    }

    public String getDataSize() {
        synchronized (mLock) {
            return Formatter.formatFileSize(mContext, mEntry.dataSize + mEntry.externalDataSize);
        }
    }

    public String getSpaceManagerActivityName() {
        synchronized (mLock) {
            return mEntry.info.manageSpaceActivityName;
        }
    }

    public int getUid() {
        synchronized (mLock) {
            return mEntry.info.uid;
        }
    }

    public String getVersion() {
        synchronized (mLock) {
            return mEntry.getVersion(mContext);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
