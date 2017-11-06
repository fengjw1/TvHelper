package com.fengjw.tvhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.recenttask.RecentTaskActivity;
import com.fengjw.tvhelper.stop.StopRunActivity;
import com.fengjw.tvhelper.stop.utils.AppsInfo;
import com.fengjw.tvhelper.stop.utils.DomXml;
import com.fengjw.tvhelper.stop.utils.Filter;
import com.fengjw.tvhelper.stop.utils.ForceStopManager;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;
import com.fengjw.tvhelper.update.DownloadAllActivity;

import org.evilbinary.tv.widget.BorderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;


public class MainActivity extends Activity implements View.OnClickListener {

    private RelativeLayout mLayout;
    private ForceStopManager mForceStopManager;
    private ApplicationsState mApplicationsState;
    private StopAppInfo mInfo;

    /*
    这里是预加载一次
     */
    private AppsInfo mAppsInfo;
    private List<StopAppInfo> mAppInfoList;
    private List<ApplicationsState.AppEntry> mList;

    private String fileName = Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DOWNLOADS).getPath() + "/filter.txt";
    private DomXml mXml;
    private String str_write;
    private List<Filter> mFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

//        try {
//            /*
//            如果这里不执行一次，那么后面的activity无法直接获取完整的列表，具体不清楚原因。
//             */
//            init();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        BorderView border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);
        mLayout  = (RelativeLayout) findViewById(R.id.main);
        border.attachTo(mLayout);

        for (int i = 0; i < mLayout.getChildCount(); i ++){
            mLayout.getChildAt(i).setOnClickListener(this);
        }

    }

//    private void cinFile(){
//        Log.d(TGA, "str_write : " + str_write);
//        try { //放置到文件中
//            mXml.writeSDFile(fileName, str_write);
//        }catch (IOException e){
//            e.printStackTrace();
//            Log.d(TGA, e.getMessage());
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.d(TGA, e.getMessage());
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        try {
//            /*
//            如果这里不执行一次，那么后面的activity无法直接获取完整的列表，具体不清楚原因。
//             */
//            init();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        try {
//            /*
//            如果这里不执行一次，那么后面的activity无法直接获取完整的列表，具体不清楚原因。
//             */
//            init();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    private void init(){
//        try {
//            mXml = new DomXml(this);
//            mFilters = mXml.XMLResolve();
//            mApplicationsState = ApplicationsState.getInstance(getApplication());
//
//            mAppsInfo = new AppsInfo(this);
//            mAppInfoList = new ArrayList<>();
//            Log.d(TGA, "Enter mAppInfo");
//            mAppsInfo.init();
//            mList = mAppsInfo.rebuildRunning();
//
//            Log.d(TGA, "mList Size is " + mList.size());
//            for (ApplicationsState.AppEntry appEntry : mList){
//                try {
//                    StopAppInfo appInfo = new StopAppInfo(this, appEntry);
//                    mAppInfoList.add(appInfo);
//                }catch (Exception e){
//                    Log.d(TGA, e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//            for (StopAppInfo appInfo : mAppInfoList){
//                str_write += " " + appInfo.getName() + " : " + appInfo.getPackageName() + "\n";
////                Log.d(TGA, "appInfo: name =" + appInfo.getName()
////                        + " CacheSize =" + appInfo.getCacheSize()
////                        + " DateSize =" + appInfo.getDataSize()
////                        + " Size =" + appInfo.getSize()
////                        + " Version =" + appInfo.getVersion());
//            }
//
//            //xml 过滤
//            for (int i = 0; i < mAppInfoList.size(); i ++) {
//                for (int j = 0; j < mFilters.size(); j ++) {
//                    if (mFilters.get(j).getName().equals(mAppInfoList.get(i).getPackageName())){
//                        mAppInfoList.remove(i);
//                        Log.d(TGA, "remove pkgName : " + mAppInfoList.get(i).getPackageName());
//                    }
////                    if (mFilters[j].getName().equals(info.getPackageName())){
////                        mAppInfoList.remove(info);
////                        Log.d(TGA, "remove : " + info.getPackageName());
////                    }else {
////                        Log.d(TGA, "filter name = " + filter.getName() + "  "
////                                + "info.getPackageName = " + info.getPackageName());
////                    }
//                }
//            }
//
//            cinFile();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TGA, "onKeyDown");
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            try {
//                if (mAppInfoList.size() > 0) {
//                    mInfo = mAppInfoList.get(mAppInfoList.size() - 1);
//                }else {
//                    mInfo = mAppInfoList.get(0);
//                }
//                Log.d(TGA, "mAppInfoList.size() : " + mAppInfoList.size());
//                Log.d(TGA, "mInfo.getName : " + mInfo.getPackageName());
//                if (mInfo.getPackageName().equals("com.fengjw.tvhelper")){
//                    mForceStopManager = new ForceStopManager(this, mInfo);
//                    if (mForceStopManager.canForceStop()) {
//                        onForceStopOk();
//                    }
//                }else {
//                    Log.d(TGA, "自身没有加载出来");
//                }
//                Log.d(TGA, "back!");
//                Toast.makeText(this, "back!", Toast.LENGTH_SHORT).show();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            //return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    private void onForceStopOk() {
//        mForceStopManager.forceStop(mApplicationsState);
//        Log.d(TGA, "onForceStopOK");
//        //onBackPressed();
//    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (view == mLayout.getChildAt(0)){
            intent.setClass(this, DownloadAllActivity.class);
            startActivity(intent);
        }else if (view == mLayout.getChildAt(1)){
            intent.setClass(this, RecentTaskActivity.class);
            startActivity(intent);
        }else if (view == mLayout.getChildAt(2)){
            String pkgName = "com.ktc.filemanager";
            startApp(pkgName);
        }
//        else if (view == mLayout.getChildAt(3)){
//            intent.setClass(this, StopRunActivity.class);
//            startActivity(intent);
//        }
    }

    public void startApp(String appPackageName){
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
            //finish();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "文件管理未安装！", Toast.LENGTH_SHORT).show();
        }
    }

}
