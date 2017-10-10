package com.fengjw.tvhelper.stop.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.UserHandle;
import android.util.Log;

import com.android.settingslib.applications.ApplicationsState;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;

/**
 * Created by fengjw on 2017/10/9.
 */

public class ForceStopManager {
    private final Context mContext;
    private final StopAppInfo mAppInfo;
    private boolean mShowForceStop;

    public ForceStopManager(Context context, StopAppInfo appInfo) {
        mContext = context;
        mAppInfo = appInfo;
        mShowForceStop = false;
    }

    public boolean canForceStop() {
        checkForceStop();
        return mShowForceStop;
    }

    public void forceStop(ApplicationsState state) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        am.forceStopPackage(mAppInfo.getPackageName());
        final int userId = UserHandle.getUserId(mAppInfo.getUid());
        state.invalidatePackage(mAppInfo.getPackageName(), userId);
        ApplicationsState.AppEntry newEnt = state.getEntry(mAppInfo.getPackageName(), userId);
        if (newEnt != null) {
            mAppInfo.setEntry(newEnt);
            Log.d(TGA, "newEnt != null");
        }
        Log.d(TGA, "forceStop()");
    }

    private void checkForceStop() {
        DevicePolicyManager dpm = (DevicePolicyManager) mContext.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        if (dpm.packageHasActiveAdmins(mAppInfo.getPackageName())) {
            // User can't force stop device admin.
            mShowForceStop = false;
        } else if (!mAppInfo.isStopped()) {
            // If the app isn't explicitly stopped, then always show the
            // force stop action.
            mShowForceStop = true;
        } else {
            Intent intent = new Intent(Intent.ACTION_QUERY_PACKAGE_RESTART,
                    Uri.fromParts("package", mAppInfo.getPackageName(), null));
            intent.putExtra(Intent.EXTRA_PACKAGES, new String[] {
                    mAppInfo.getPackageName() });
            intent.putExtra(Intent.EXTRA_UID, mAppInfo.getUid());
            intent.putExtra(Intent.EXTRA_USER_HANDLE, UserHandle.getUserId(mAppInfo.getUid()));
            mContext.sendOrderedBroadcast(intent, null, new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mShowForceStop = (getResultCode() != Activity.RESULT_CANCELED);
                }
            }, null, Activity.RESULT_CANCELED, null, null);
        }
    }
}
