package com.fengjw.tvhelper.recenttask.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by fengjw on 2017/10/30.
 */

public class HookCallback {

    public static void dexInject(){
        Log.d("clear_task", "this is dex code,welcome to HookTool~");
        ActivityManager mActivityManager = null;
        Method mRemoveTask;

        try {
            Class<?> ActivityThread = Class.forName("android.app.ActivityThread");

//            Method[] methods = ActivityThread.getMethods();
//            for (Method method : methods) {
//                Log.e("clear_task", method.getName());
//            }

            Method method = ActivityThread.getMethod("currentActivityThread");
            Object currentActivityThread = method.invoke(ActivityThread);//获取currentActivityThread 对象

            Method method2 = currentActivityThread.getClass().getMethod("getApplication");
            Context CONTEXT_INSTANCE =(Context)method2.invoke(currentActivityThread);//获取 Context对象

            Log.e("clear_task","CONTEXT_INSTANCE:"+CONTEXT_INSTANCE);

            Class<?> activityManagerClass = Class.forName("android.app.ActivityManager");
            mActivityManager = (ActivityManager) CONTEXT_INSTANCE.getSystemService(Context.ACTIVITY_SERVICE);

            mRemoveTask = activityManagerClass.getMethod("removeTask", new Class[] { int.class, int.class });
            mRemoveTask.setAccessible(true);

            Log.e("clear_task","dexInject start");
            List<ActivityManager.RecentTaskInfo> recents = mActivityManager.getRecentTasks(Integer.MAX_VALUE, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
            // Start from 1, since we don't want to kill ourselves!
            for( int i=1; i < recents.size(); i++ ) {
                Log.e("clear_task","invoke start --------");
                mRemoveTask.invoke(mActivityManager, recents.get(i).persistentId, 0 );
                Log.e("clear_task","dexInject --------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
