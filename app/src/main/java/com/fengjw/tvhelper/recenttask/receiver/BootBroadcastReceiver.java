package com.fengjw.tvhelper.recenttask.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by fengjw on 2017/11/15.
 */

public class BootBroadcastReceiver extends BroadcastReceiver{

    private Context mContext;
    private static final int MAX_RECENT_TASKS = 16;
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        clearRecentList(mContext);
    }

    private void clearRecentList(Context context){
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RecentTaskInfo> recentTasks = am
                .getRecentTasks(MAX_RECENT_TASKS + 1, 0x0002);
        for (int i = 0; i < recentTasks.size(); i ++){
            am.removeTask(recentTasks.get(i).persistentId);
        }
        Log.d(TAG, "recentTasks size : " + recentTasks.size());
    }

}
