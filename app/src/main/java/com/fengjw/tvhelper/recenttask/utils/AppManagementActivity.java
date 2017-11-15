package com.fengjw.tvhelper.recenttask.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fengjw.tvhelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AppManagementActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AppManagementActivity";

    private ImageView mAppImage;
    private TextView m1AppTv;
    private TextView mNameApp;
    private TextView mVersionApp;
    private LinearLayout mAppLin;
    private TextView mOpenApp;
    private TextView mStopAppTv;
    private String packageName;
    private int position;
    private static final int STOP_RUN = 1;
    private static final int CONTINUE_RUN = 2;


    //
    private Intent mIntent;
    private List<HashMap<String, Object>> appInfos = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_app);
        packageName = getIntent().getStringExtra("packageName");
        position = getIntent().getIntExtra("position", 0);
        Log.d(TAG, "packageName : " + packageName);
        reloadButtons(this, appInfos, 16);
        initView();
        //init();
        bindView();
    }

    public static void reloadButtons(Activity activity, List<HashMap<String, Object>> appInfos, int appNumber) {
        int MAX_RECENT_TASKS = appNumber; // allow for some discards
        int repeatCount = appNumber;// 保证上面两个值相等,设定存放的程序个数

		/* 每次加载必须清空list中的内容 */
        appInfos.removeAll(appInfos);

        // 得到包管理器和activity管理器
        final Context context = activity.getApplication();
        final PackageManager pm = context.getPackageManager();
        final ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        // 从ActivityManager中取出用户最近launch过的 MAX_RECENT_TASKS + 1 个，以从早到晚的时间排序，
        // 注意这个 0x0002,它的值在launcher中是用ActivityManager.RECENT_IGNORE_UNAVAILABLE
        // 但是这是一个隐藏域，因此我把它的值直接拷贝到这里
        final List<ActivityManager.RecentTaskInfo> recentTasks = am
                .getRecentTasks(MAX_RECENT_TASKS + 1, 0x0002);

//        final List<ActivityManager.RunningTaskInfo> recentTaskInfos = am.getRunningTasks(20);
//        Log.d("fengjw", "size : " + recentTaskInfos.size());
//        for (int i = 0; i < recentTaskInfos.size(); i ++){
//            Log.d("fengjw", recentTaskInfos.get(i).baseActivity.toString());
//        }

        // 这个activity的信息是我们的launcher
        ActivityInfo homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(
                Intent.CATEGORY_HOME).resolveActivityInfo(pm, 0);
        int numTasks = recentTasks.size();
        for (int i = 1; i < numTasks && (i < MAX_RECENT_TASKS); i++) {
            HashMap<String, Object> singleAppInfo = new HashMap<String, Object>();// 当个启动过的应用程序的信息
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            Intent intent = new Intent(info.baseIntent);
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }
            /**
             * 如果找到是launcher，直接continue，后面的appInfos.add操作就不会发生了
             */
            if (homeInfo != null) {
                if (homeInfo.packageName.equals(intent.getComponent()
                        .getPackageName())
                        && homeInfo.name.equals(intent.getComponent()
                        .getClassName())) {
                    MAX_RECENT_TASKS = MAX_RECENT_TASKS + 1;
                    continue;
                }
            }
            // 设置intent的启动方式为 创建新task()【并不一定会创建】
            intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            // 获取指定应用程序activity的信息(按我的理解是：某一个应用程序的最后一个在前台出现过的activity。)
            final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                final String title = activityInfo.loadLabel(pm).toString();
                Drawable icon = activityInfo.loadIcon(pm);

                //&& info.id != -1
                if (title != null && title.length() > 0 && icon != null ) {
                    singleAppInfo.put("title", title);
                    singleAppInfo.put("icon", icon);
                    singleAppInfo.put("tag", intent);
                    singleAppInfo.put("packageName", activityInfo.packageName);
                    singleAppInfo.put("id", info.persistentId);
                    appInfos.add(singleAppInfo);
                }
            }
        }
        MAX_RECENT_TASKS = repeatCount;
    }


    private void bindView(){
        mAppImage.setImageDrawable((Drawable) appInfos.get(position).get("icon"));
        mNameApp.setText(appInfos.get(position).get("title").toString());
        mVersionApp.setText(appInfos.get(position).get("id").toString());
    }

    private void initView() {
        mAppImage = (ImageView) findViewById(R.id.image_app);
        m1AppTv = (TextView) findViewById(R.id.tv_1_app);
        mNameApp = (TextView) findViewById(R.id.tv_name_app);
        mVersionApp = (TextView) findViewById(R.id.tv_version_app);
        mAppLin = (LinearLayout) findViewById(R.id.lin_app);
        mOpenApp = (TextView) findViewById(R.id.tv_open_app);
        mOpenApp.setOnClickListener(this);
        mStopAppTv = (TextView) findViewById(R.id.tv_stop_app);
        mStopAppTv.setOnClickListener(this);

        //bindView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_open_app:
                //toast("open");
                startApp(packageName);
                break;
            case R.id.tv_stop_app:
//                toast("已停止运行" + mAppInfo.getName());
//                if (mForceStopManager.canForceStop()){
////                        onForceStopOk();
////                        //Log.d(TGA, "getItemId : " + getItemId(position));
////                        //Log.d(TGA, "getItemViewType : " + getItemViewType(position));
////                        //removeData(position);
////                        Log.d(TGA, "delete");
////                    //Intent intent = new Intent(this, StopRunActivity.class);
////                    //intent.putExtra("position", position);
////                    this.setResult(STOP_RUN);
//                    finish();
//                    }else {
//                        Log.d(TGA, "no delete");
//                    }

                this.setResult(STOP_RUN);
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                Log.d(TAG, "id : " + (int)appInfos.get(position).get("id"));
                am.removeTask((int)appInfos.get(position).get("id"));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            try {
                this.setResult(CONTINUE_RUN);
                finish();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    //
    public void startApp(String appPackageName){
        try {

            for (int i = 0; i < appInfos.size(); i ++){
                if (appPackageName.equals(appInfos.get(i).get("packageName"))){
                    mIntent = (Intent) appInfos.get(i).get("tag");
                }
            }

            Log.d(TAG, "click");
            if (mIntent != null) {
                mIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                try {
                    startActivity(mIntent);
                    finish();
                }
                catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Unable to launch recent task", e);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG, "intent is null!");
            }
//            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
//            startActivity(intent);
//            finish();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, R.string.app_management_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void showLog(String str){
        Log.d(TAG, str);
    }

}
