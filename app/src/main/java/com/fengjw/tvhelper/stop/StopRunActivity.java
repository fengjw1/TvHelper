package com.fengjw.tvhelper.stop;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.stop.adapter.AppsAdapter;
import com.fengjw.tvhelper.stop.utils.AppsInfo;
import com.fengjw.tvhelper.stop.utils.DividerGridItemDecoration;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;

import org.evilbinary.tv.widget.BorderView;

import java.util.ArrayList;
import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;

public class StopRunActivity extends AppCompatActivity {

    private RecyclerView mView;
    private AppsAdapter mAdapter;
    private AppsInfo mAppsInfo;
    private List<ApplicationsState.AppEntry> mList;
    private List<StopAppInfo> mAppInfoList;
    public final static String TGA = "MainActivity";
    //private StaggeredGridLayoutManager mLayoutManager;
    private GridLayoutManager mLayoutManager;
    private int columNum = 4;
    private Application mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_run);
        try {
            init();

            BorderView border = new BorderView(this);
            border.setBackgroundResource(R.drawable.border_highlight);

            mApplication = getApplication();
            mAdapter = new AppsAdapter(this, mAppInfoList, mApplication);
            mView = (RecyclerView) findViewById(R.id.apps_recyclerview);
            //mView.requestFocus();
            //LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mLayoutManager = new GridLayoutManager(this, columNum);
            //mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            //mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mView.setLayoutManager(mLayoutManager);
            //添加分割线
            //mView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            //mView.addItemDecoration(new DividerGridItemDecoration(this));
            //mView.setHasFixedSize(true);
            border.attachTo(mView);
            mView.setFocusable(false);


            DefaultItemAnimator animator = new DefaultItemAnimator();
            animator.setAddDuration(1000);
            animator.setRemoveDuration(1000);
            mView.setItemAnimator(animator);

            mView.setAdapter(mAdapter);
            mView.scrollToPosition(0);
            
        }catch (Exception e){
            e.printStackTrace();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
