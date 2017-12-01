package com.fengjw.tvhelper.recenttask.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fengjw on 2017/11/15.
 */

public class BootBroadcastReceiver extends BroadcastReceiver{

    private static final int MAX_RECENT_TASKS = 16;
    private final String TAG = getClass().getSimpleName();
    private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) {
            Log.d("fengjw", "ACTION_BOOT");
            clearRecentList(context);
        }
    }

    private void clearRecentList(Context context){
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RecentTaskInfo> recentTasks = am
                .getRecentTasks(MAX_RECENT_TASKS + 1, 0x0002);
        final PackageManager pm = context.getPackageManager();

        int numTasks = recentTasks.size();
        for (int i = 1; i < numTasks && (i < MAX_RECENT_TASKS); i++) {
            HashMap<String, Object> singleAppInfo = new HashMap<String, Object>();// 当个启动过的应用程序的信息
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            Intent intent = new Intent(info.baseIntent);
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }
            intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            // 获取指定应用程序activity的信息(按我的理解是：某一个应用程序的最后一个在前台出现过的activity。)
            final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                if (activityInfo.packageName.equals("com.ktc.launcher") ||
                        activityInfo.packageName.equals("com.fengjw.apkupdatetool") ||
                        activityInfo.packageName.equals("com.fengjw.tvhelper") ||
                        activityInfo.packageName.equals("com.mstar.tv.tvplayer.ui")){
                    Log.d("fengjw", "no remove : " + activityInfo.packageName);
                }else {
                    am.removeTask(recentTasks.get(i).persistentId);
                    Log.d("fengjw", "remove : " + activityInfo.packageName);
                }

            }

        }



    }

}
