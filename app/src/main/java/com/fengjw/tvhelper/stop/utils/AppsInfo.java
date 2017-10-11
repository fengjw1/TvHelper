package com.fengjw.tvhelper.stop.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.android.settingslib.applications.ApplicationsState;

import java.util.ArrayList;
import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;

/**
 * Created by fengjw on 2017/9/27.
 */

public class AppsInfo {

    private final ApplicationsState.Session mSessionRunning;
    private final ApplicationsState.AppFilter mFilterRunning;
    private final ApplicationsState mApplicationsState;
    private final Activity mContext;

    public AppsInfo(Activity context){
        Log.d(TGA, "AppsInfo");
        mContext = context;
        mFilterRunning = FILTER_RUNNING;
        mApplicationsState = ApplicationsState.getInstance(context.getApplication());
        mSessionRunning = mApplicationsState.newSession(new RowUpdateCallbacks() {
            @Override
            protected void doRebuild() {
                Log.d(TGA, "doRebuild");
            }

            @Override
            public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
                Log.d(TGA, "onRebuildComplete");
            }
        });
        Log.d(TGA, "mSessionRunning");
        rebuildRunning();
        Log.d(TGA, "rebuildRunning");
    }


    public void init(){
        mSessionRunning.resume();
        Log.d(TGA, "init()");
    }

    public List<ApplicationsState.AppEntry> rebuildRunning() { //这里是获取相关的app列表
        List<ApplicationsState.AppEntry> apps =
                mSessionRunning.rebuild(mFilterRunning, ApplicationsState.ALPHA_COMPARATOR);
        Log.d(TGA, "rebuildRunning()");
        return apps;
    }

    private abstract class RowUpdateCallbacks implements ApplicationsState.Callbacks {

        protected abstract void doRebuild();

        @Override
        public void onRunningStateChanged(boolean running) {
            doRebuild();
        }

        @Override
        public void onPackageListChanged() {
            doRebuild();
        }

        @Override
        public void onPackageIconChanged() {
            doRebuild();
        }

        @Override
        public void onPackageSizeChanged(String packageName) {
            doRebuild();
        }

        @Override
        public void onAllSizesComputed() {
            doRebuild();
        }

        @Override
        public void onLauncherInfoChanged() {
            doRebuild();
        }

        @Override
        public void onLoadEntriesCompleted() {
            doRebuild();
        }
    }

    private static final ApplicationsState.AppFilter FILTER_RUNNING =
            new ApplicationsState.AppFilter() {

                @Override
                public void init() {}

                @Override
                public boolean filterApp(ApplicationsState.AppEntry info) {
                    Log.d(TGA, "RowUpdateCallbacks");
                    return (info.info.flags & ApplicationInfo.FLAG_STOPPED) == 0;
                }
            };

}
