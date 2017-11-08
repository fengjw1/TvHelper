package com.fengjw.tvhelper.stop;

import android.app.Application;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.stop.adapter.AppsAdapter;
import com.fengjw.tvhelper.stop.utils.AppsInfo;
import com.fengjw.tvhelper.stop.utils.DomXml;
import com.fengjw.tvhelper.stop.utils.Filter;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;
import java.util.ArrayList;
import java.util.List;


public class StopRunActivity extends AppCompatActivity {

    private RecyclerView mView;
    private AppsAdapter mAdapter;
    private AppsInfo mAppsInfo;
    private List<ApplicationsState.AppEntry> mList;
    private List<StopAppInfo> mAppInfoList;
    public final static String TGA = "MainActivity";
    private StaggeredGridLayoutManager mLayoutManager;
    //private GridLayoutManager mLayoutManager;
    private int columNum = 4;
    private Application mApplication;
    private DomXml mXml;
    private List<Filter> mFilters;
    private UsageStatsManager mManager;
    private int resultPosition;
    private Intent intent;
    private static final int STOP_RUN = 1;
    private static final int CONTINUE_RUN = 2;
    private static final int REFRESH_UI = 3;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_UI:
                    Log.d("fengjw", "handle");
                    //init();
                    mAppInfoList.remove(resultPosition);
                    mAdapter.notifyItemRemoved(resultPosition);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_run);
        try {
            init();
            mApplication = getApplication();
            mAdapter = new AppsAdapter(this, mAppInfoList, mApplication);
            mView = (RecyclerView) findViewById(R.id.apps_recyclerview);
            //mView.requestFocus();
            //LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            //mLayoutManager = new GridLayoutManager(this, columNum);
            mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            //mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mView.setLayoutManager(mLayoutManager);
            //添加分割线
            //mView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            //mView.addItemDecoration(new DividerGridItemDecoration(this));
            //mView.setHasFixedSize(true);
            //border.attachTo(mView);
            mView.setFocusable(false);
            mView.setAdapter(mAdapter);
//            DefaultItemAnimator animator = new DefaultItemAnimator();
//            animator.setAddDuration(1000);
//            animator.setRemoveDuration(1000);
//            mView.setItemAnimator(animator);
            mView.scrollToPosition(0);

            mAdapter.setOnItemClickListener(new AppsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    intent = new Intent(StopRunActivity.this, AppManagementActivity.class);
                    intent.putExtra("name", mAppInfoList.get(position).getPackageName());
                    //intent.putExtra("position", position);
                    //startActivity(intent);
                    resultPosition = position;
                    startActivityForResult(intent, 1);
                    Toast.makeText(StopRunActivity.this, mAppInfoList.get(position).getPackageName(), Toast.LENGTH_SHORT).show();
                    //finish();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                break;
            default:
                break;
        }

//        switch (resultCode){
//            case 1:
//                break;
//            default:
//                break;
//        }

        switch (resultCode){
            case STOP_RUN:
                Log.d("fengjw", "STOP_RUN");
                mHandler.sendEmptyMessage(REFRESH_UI);
                break;
            case CONTINUE_RUN:
                Log.d(TGA, "CONTINUE_RUN");
                break;
            default:
                break;
        }

    }

    private void init(){
        try {
            mXml = new DomXml(this);
            mFilters = mXml.XMLResolve();
            Log.d(TGA, "xml size : " + mFilters.size());
            for (int i = 0; i < mFilters.size(); i ++){
                Log.d(TGA, mFilters.get(i).getName());
            }
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

            //xml 过滤
            for (int i = 0; i < mAppInfoList.size(); i ++) {
                for (int j = 0; j < mFilters.size(); j ++) {
                    if (mFilters.get(j).getName().equals(mAppInfoList.get(i).getPackageName())){
                        mAppInfoList.remove(i);
                        Log.d(TGA, "remove pkgName : " + mAppInfoList.get(i).getPackageName());
                    }
//                    if (mFilters[j].getName().equals(info.getPackageName())){
//                        mAppInfoList.remove(info);
//                        Log.d(TGA, "remove : " + info.getPackageName());
//                    }else {
//                        Log.d(TGA, "filter name = " + filter.getName() + "  "
//                                + "info.getPackageName = " + info.getPackageName());
//                    }
                }
            }

//            for (StopAppInfo appInfo : mAppInfoList){
//                Log.d(TGA, "appInfo: name =" + appInfo.getName()
//                        + "pkgname = " + appInfo.getPackageName()
//                        + " CacheSize =" + appInfo.getCacheSize()
//                        + " DateSize =" + appInfo.getDataSize()
//                        + " Size =" + appInfo.getSize()
//                        + " Version =" + appInfo.getVersion());
//            }

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
    protected void onRestart() {
        super.onRestart();
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
