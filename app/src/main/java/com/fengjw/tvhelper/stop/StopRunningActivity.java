package com.fengjw.tvhelper.stop;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.stop.adapter.AppAdapter;
import com.fengjw.tvhelper.stop.utils.AppsInfo;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;

import java.util.ArrayList;
import java.util.List;

public class StopRunningActivity extends AppCompatActivity {

    public final static String TGA = "MainActivity";
    private AppsInfo mAppsInfo;
    private List<ApplicationsState.AppEntry> mList;
    private List<StopAppInfo> mAppInfoList;
    private AppAdapter mAdapter;
    private ListView mView;
    private final int REFRASH_VIEW = 1;

//    Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case REFRASH_VIEW:
//                    mAdapter.notifyDataSetChanged();
//                    mAdapter.notify();
//                    Log.d(TGA, "Handler");
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_running);
        mView = (ListView) findViewById(R.id.mlist);
        mAdapter = new AppAdapter(this, mAppInfoList);
        mView.setAdapter(mAdapter);
        mView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StopRunningActivity.this, DetailActivity.class);
                StopAppInfo appInfo = mAppInfoList.get(i);
                //intent.putExtra("packagename", appInfo.getPackageName());
                intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
                Log.d(TGA, "packagename : " + appInfo.getPackageName());
                startActivity(intent);
                //finish();
            }
        });

        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    private void init(){
        try {
            mAppsInfo = new AppsInfo(this);
            mAppInfoList = new ArrayList<>();
            Log.d(TGA, "Enter mAppInfo");
            mAppsInfo.init();
            mList = mAppsInfo.rebuildRunning();
            Log.d(TGA, "mList Size is " + mList.size());
            for (ApplicationsState.AppEntry appEntry : mList){
                try {
                    StopAppInfo appInfo = new StopAppInfo(this, appEntry);
                    mAppInfoList.add(appInfo);
                }catch (Exception e){
                    Log.d(TGA, e.getMessage());
                    e.printStackTrace();
                }
            }
            for (StopAppInfo appInfo : mAppInfoList){
                Log.d(TGA, "appInfo: name =" + appInfo.getName()
                        + " CacheSize =" + appInfo.getCacheSize()
                        + " DateSize =" + appInfo.getDataSize()
                        + " Size =" + appInfo.getSize()
                        + " Version =" + appInfo.getVersion());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
