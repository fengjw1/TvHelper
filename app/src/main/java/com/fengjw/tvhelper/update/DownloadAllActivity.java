/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fengjw.tvhelper.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.update.adapter.DownloadAdapter;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import butterknife.Bind;
import butterknife.OnClick;

public class DownloadAllActivity extends BaseActivity implements XExecutor.OnAllTaskEndListener {

//    @Bind(R.id.toolbar)
//    Toolbar toolbar;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    private final static String TGA = "DownloadAllActivity";

    private DownloadAdapter adapter;
    private OkDownload okDownload;


    public final static int INSTALL_APK = 1;
    private static Context mContext;
    public String packageName;
    public DownloadTask mTask;
    private InstallAPKReceiver mReceiver = null;


    public Handler InstallHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INSTALL_APK:
                    try {
                        if (mReceiver != null){
                            unregisterReceiver(mReceiver);
                            mReceiver = null;
                        }
                        okDownload = OkDownload.getInstance();
                        mTask = okDownload.getTask(packageName);
                        Log.d(TGA, "mTask!");
                        mTask.remove(true);
                        Log.d(TGA, "mTask remove!");
                        adapter.updateData(0);
                        adapter.notifyDataSetChanged();
                        Log.d(TGA, "mAdapter!");
                    }catch(Exception e){
                        Log.d(TGA, "INSTALL_APK Execption!");
                        e.printStackTrace();
                    }finally {
                        if (mReceiver != null){
                            unregisterReceiver(mReceiver);
                            mReceiver = null;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_all);
       //initToolBar(toolbar, true, "所有任务");
        mContext = DownloadAllActivity.this;
        okDownload = OkDownload.getInstance();
        //okDownload.removeAll(true);
        adapter = new DownloadAdapter(this);
        adapter.updateData(DownloadAdapter.TYPE_ALL);
        //recyclerView.setFocusableInTouchMode(true);
        //recyclerView.requestFocus();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        okDownload.addOnAllTaskEndListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.notifyDataSetChanged();
        //注册动态广播
        mReceiver = new InstallAPKReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addDataScheme("package");
        this.registerReceiver(mReceiver, filter);
        Log.d(TGA, "onStart Receiver register!");
        adapter.notifyDataSetChanged();
    }

    //back keyboard
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            Intent intent = new Intent(DownloadAllActivity.this,DownloadListActivity.class);
//            startActivity(intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAllTaskEnd() {
        //showToast("所有下载任务已结束");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null){
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        okDownload.removeOnAllTaskEndListener(this);
        adapter.unRegister();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.removeAll)
    public void removeAll(View view) {
        //okDownload.removeAll();
        okDownload.removeAll(true);
        adapter.updateData(DownloadAdapter.TYPE_ALL);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.pauseAll)
    public void pauseAll(View view) {
        okDownload.pauseAll();
    }

    @OnClick(R.id.startAll)
    public void startAll(View view) {
        okDownload.startAll();
    }


    public class InstallAPKReceiver extends BroadcastReceiver{

        private OkDownload mOkDownload;

        public InstallAPKReceiver(){
            //this.mOkDownload = okDownload;
            mOkDownload = OkDownload.getInstance();
            Log.d(TGA, "InstallAPKReceiver()");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
                    Log.d(TGA, "Name : " + intent.getDataString());
                    if (mOkDownload.hasTask(intent.getDataString())){
                        packageName = intent.getDataString();
                        Log.d(TGA, "packageName = " + packageName);
                        InstallHandler.sendEmptyMessage(INSTALL_APK);
                        Log.d(TGA, "Handler!");
                    }else {
                        if (mReceiver != null)
                        unregisterReceiver(mReceiver);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
