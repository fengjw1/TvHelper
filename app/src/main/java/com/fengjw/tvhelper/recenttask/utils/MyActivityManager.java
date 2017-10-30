package com.fengjw.tvhelper.recenttask.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by fengjw on 2017/10/30.
 */

public class MyActivityManager {
    private ActivityManager mActivityManager = null;
    private Method mRemoveTask;

    public MyActivityManager(Context context) {
        try {
            Class<?> activityManagerClass = Class.forName("android.app.ActivityManager");
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            mRemoveTask = activityManagerClass.getMethod("removeTask", new Class[] { int.class, int.class });
            mRemoveTask.setAccessible(true);

        }
        catch ( ClassNotFoundException e ) {
            Log.i("MyActivityManager", "No Such Class Found Exception", e);
        }
        catch ( Exception e ) {
            Log.i("MyActivityManager", "General Exception occurred", e);
        }
    }

    /**
     * If set, the process of the root activity of the task will be killed
     * as part of removing the task.
     */
    public static final int REMOVE_TASK_KILL_PROCESS = 0x0001;

    /**
     * Completely remove the given task.
     *
     * @param taskId Identifier of the task to be removed.
     * @param flags Additional operational flags.  May be 0 or
     * {@link #REMOVE_TASK_KILL_PROCESS}.
     * @return Returns true if the given task was found and removed.
     */
    public boolean removeTask(int taskId, int flags) {
        try {
            return (Boolean) mRemoveTask.invoke(mActivityManager, Integer.valueOf(taskId), Integer.valueOf(flags) );
        } catch (Exception ex) {
            Log.i("MyActivityManager", "Task removal failed", ex);
        }
        return false;
    }

    public void clearRecentTasks() {
        List<ActivityManager.RecentTaskInfo> recents = mActivityManager.getRecentTasks(1000, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
        // Start from 1, since we don't want to kill ourselves!
        for( int i=1; i < recents.size(); i++ ) {
            removeTask( recents.get(i).persistentId, 0);
        }
    }
}
