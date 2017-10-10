package com.fengjw.tvhelper.update.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.IPackageManager;
import android.content.pm.VerificationParams;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.fengjw.tvhelper.update.PushDialogActivity;
import com.fengjw.tvhelper.update.utils.ApkModel;
import com.fengjw.tvhelper.update.utils.AppInfo;
import com.fengjw.tvhelper.update.utils.AppInfoProvider;
import com.fengjw.tvhelper.update.utils.HeadBean;
import com.fengjw.tvhelper.update.utils.HttpUtil;
import com.fengjw.tvhelper.update.utils.INDEX;
import com.fengjw.tvhelper.update.utils.LogDownloadListener;
import com.fengjw.tvhelper.update.utils.getUrl;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class NetworkGetService extends Service {

    private List<ApkModel> apks; //类型是ApkModel
    private List<ApkModel> apkShows;
    private static final int GET_ALL_APP_FINISH = 1;
    private static final String TGA = "NetworkGetService";
    private final int INSTALL_REPLACE_EXISTING = 2;
    private final int GET_ALL_APP_TOAST_FINFISH = 3;
    private static int Index = 0;
    private INDEX mINDEX;
    public NetworkGetService() {
    }

    private Handler handler = new Handler(){
        private int i = 1;
        @Override
        public void handleMessage(Message msg) {
            //apkPaths = new ArrayList<>();
            switch (msg.what){
                case GET_ALL_APP_FINISH:
                    Log.d(TGA, "这里是handler");
                    for (ApkModel apk : apks) { //循环体，操作所有下载按钮
                        //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
                        GetRequest<File> request = OkGo.<File>get(apk.url);
                        //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                        Log.d(TGA, "task!");
                        DownloadTask task = OkDownload.request(apk.url, request)//
                                .priority(apk.priority)//
                                .extra1(apk)//
                                .save()//
                                .register(new LogDownloadListener());//
                        task.start();
                        task.register(new ListDownloadListener(task, apk.url));
                        Log.d(TGA, "apk 循环次数 " + i++);
                    }
                    Log.d(TGA, "OKDownload work!");
                    //Toast.makeText(NetworkGetService.this, "OKDownload work!", Toast.LENGTH_SHORT).show();
                    break;
                case GET_ALL_APP_TOAST_FINFISH:
                    OkDownload.getInstance().removeAll(true);
                    for (ApkModel apk : apkShows) { //循环体，操作所有下载按钮
                        //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
                        GetRequest<File> request = OkGo.<File>get(apk.url);
                        //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                        Log.d(TGA, "task!");
                        DownloadTask task = OkDownload.request("package:" + apk.pkgName, request)//
                                .priority(apk.priority)//
                                .extra1(apk)//
                                .save()//
                                .register(new LogDownloadListener());
                        Log.d(TGA, "apk 循环次数 " + i++);
                    }
                    Log.d(TGA, "OKDownload work!");
//                    if (mINDEX.getIndex() == 2){
//                        Log.d(TGA, "index jump!");
//                        Intent intent = new Intent(getApplicationContext(), DownloadAllActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
                    if (mINDEX.getIndex() == 2) {
                        Log.d(TGA, "index jump!");
                        Intent intent = new Intent(getApplicationContext(), PushDialogActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TGA, "onCreate");
        //Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();//这里只启动一次
        OkDownload.getInstance().setFolder(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).getPath());
        String path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getPath();
        Log.d(TGA, path);
        OkDownload.getInstance().getThreadPool().setCorePoolSize(3);
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TGA, "onStartCommand");
        //Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        //init();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TGA, "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void init(){
        Log.d(TGA, "init() is work!");
        //从数据库中回复数据
        List<Progress> progressList = DownloadManager.getInstance().getAll();
        OkDownload.restore(progressList);
        mINDEX = new INDEX();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TGA, "Thread now!");
                sendRequestWithOKHttp();
            }
        }).start();

        Log.d(TGA, "here!");


    }

    private void sendRequestWithOKHttp(){
        Log.d(TGA, "sendRequestWithOKHttp() now!");
        String url = "http://192.168.1.14:8800/index.php/apkapi?model=TV918&product=ktc&sdanum=SDA123456789";
        //正式使用下面的url
        String urlTest = getUrl.getRemoteUri();
        Log.d(TGA, "urlTest : " + urlTest);
        apks = new ArrayList<>();
        apkShows = new ArrayList<>();
        Log.d(TGA, "sendRequestWithOKHttp");
        HttpUtil.sendOKHttpResquest(url, new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TGA, "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String responseData = response.body().string();
                Log.d(TGA, "onResponse");
                //Log.d(TGA, responseData);
                parseNewJSONWithJSONObject(responseData);
            }
        });
    }

    private void parseNewJSONWithJSONObject(String responseData){
        Log.d(TGA, "parseNewJSONWithJSONObject() now!");
        try {
            Gson gson = new Gson();
            HeadBean bean = gson.fromJson(responseData, HeadBean.class);
            List<HeadBean.ApklistBean> appList = bean.getApklist();

//            for (HeadBean.ApklistBean app : appList){ //foreach()
//                Log.d(TGA, "appName : " + app.getApp_name());
//                Log.d(TGA, "fileName : " + app.getFile_name());
//                Log.d(TGA, "verName : " + app.getVer_name());
//                Log.d(TGA, "verCode : " + app.getVer_code());
//                Log.d(TGA, "url : " + app.getApk_url());
//                Log.d(TGA, "MD5 : " + app.getMD5());
//                Log.d(TGA, "packageName : " + app.getPkg_name());
//                Log.d(TGA, "type : " + app.getUpdate_type());
//                Log.d(TGA, "Introduction : " + app.getIntroduction());
//                Log.d(TGA, "-----------------------------------------");
//            }

            //本地信息
            AppInfoProvider appInfoProvider = new AppInfoProvider(this);
            List<AppInfo> appInfoList = appInfoProvider.getAllApps();
//        for (AppInfo appInfo : appInfoList){
//            Log.d(TGA, "appName : " + appInfo.getApp_name());
//            Log.d(TGA, "fileName : " + appInfo.getFile_name());
//            Log.d(TGA, "verName : " + appInfo.getVer_name());
//            Log.d(TGA, "verCode : " + appInfo.getVerCode());
//            Log.d(TGA, "url : " + appInfo.getUrl());
//            Log.d(TGA, "MD5 : " + appInfo.getMD5());
//            Log.d(TGA, "packageName : " + appInfo.getPkg_name());
//            Log.d(TGA, "-----------------------------------------");
//        }
//
            Log.d(TGA, "-----------------------------------------");
            Log.d(TGA, "-----------------------------------------");
            Log.d(TGA, "-----------------------------------------");
            for (HeadBean.ApklistBean app : appList) {
                for (AppInfo appInfo : appInfoList) {
                    //http
                    String httpAppPkgName = app.getPkg_name();
                    int httpAppverCode = app.getVer_code();
                    int httpType = app.getUpdate_type();
                    //info
                    String AppInfoPkgName = appInfo.getPkg_name();
                    int AppInfoverCode = appInfo.getVerCode();


                    String name = app.getApp_name();
                    String pkgName = app.getPkg_name();
                    String url = app.getApk_url();
                    String iconUrl = app.getPic_url();
                    String description = app.getIntroduction();
                    Drawable icon = appInfo.getIcon();
                    int type = app.getUpdate_type();
                    String verName = app.getVer_name() + app.getVer_code();

                    //判断是否更新
                    if (httpAppPkgName.equals(AppInfoPkgName)) {
                        if (httpAppverCode > AppInfoverCode) {
                            if (httpType == 1) {

                                Log.d(TGA, "name = " + name);
                                Log.d(TGA, "url = " + url);
                                Log.d(TGA, "verCode = " + app.getVer_code());
                                Log.d(TGA, "type = " + app.getUpdate_type());
                                ApkModel apkModel = new ApkModel();
                                apkModel.name = name;
                                apkModel.pkgName = pkgName;
                                apkModel.url = url;
                                apkModel.verName = verName;
                                apkModel.icon = icon;
                                apkModel.priority = type;
                                apkModel.description = description;
                                Log.d(TGA, "apkModel.url = " + apkModel.url);
                                apks.add(apkModel);
                            } else if (httpType == 2) {
                                mINDEX.setIndex(2);
                                ApkModel apkModel = new ApkModel();
                                apkModel.name = name;
                                apkModel.pkgName = pkgName;
                                apkModel.url = url;
                                apkModel.verName = verName;
                                apkModel.icon = icon;
                                apkModel.priority = type;
                                apkModel.description = description;
                                //Log.d(TGA, "apkModel.url = " + apkModel.url);
                                Log.d(TGA, "mINDEX : " + mINDEX.getIndex());
                                apkShows.add(apkModel);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //initData();

        if (apks.size() == 0){
            handler.sendEmptyMessage(GET_ALL_APP_TOAST_FINFISH);
        }else {
            handler.sendEmptyMessage(GET_ALL_APP_FINISH);
            Log.d(TGA, "静默安装发送！");
        }
        Log.d(TGA, "initData after!");
    }

    private void initData() {
        Log.d(TGA, "initData now!");
        apks = new ArrayList<>();
        ApkModel apk1 = new ApkModel();
        apk1.name = "爱奇艺";
        apk1.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c10c4c0155c9adf1282af008ed329378d54112ac";
        apk1.url = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk";
        apks.add(apk1);
        ApkModel apk2 = new ApkModel();
        apk2.name = "微信";
        apk2.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/00814b5dad9b54cc804466369c8cb18f23e23823f";
        apk2.url = "http://116.117.158.129/f2.market.xiaomi.com/download/AppStore/04275951df2d94fee0a8210a3b51ae624cc34483a/com.tencent.mm.apk";
        apks.add(apk2);
        ApkModel apk3 = new ApkModel();
        apk3.name = "新浪微博";
        apk3.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/01db44d7f809430661da4fff4d42e703007430f38";
        apk3.url = "http://60.28.125.129/f1.market.xiaomi.com/download/AppStore/0ff41344f280f40c83a1bbf7f14279fb6542ebd2a/com.sina.weibo.apk";
        //apks.add(apk3);
        ApkModel apk4 = new ApkModel();
        apk4.name = "QQ";
        apk4.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/072725ca573700292b92e636ec126f51ba4429a50";
        apk4.url = "http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0ff0604fd770f481927d1edfad35675a3568ba656/com.tencent.mobileqq.apk";
        //apks.add(apk4);
    }

    public void installPackage(String apkPath)
    {
        PackageInstallObserver2 installObserver2 = new PackageInstallObserver2();
        try {
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManager.getDeclaredMethod("getService", String.class);
            getService.setAccessible(true);
            IBinder packAgeBinder = (IBinder) getService.invoke(null, "package");
            IPackageManager iPm = IPackageManager.Stub.asInterface(packAgeBinder);
            VerificationParams verificationParams=new VerificationParams();
            try {
                Log.i(TGA, "1");
                iPm.installPackage(apkPath, installObserver2,INSTALL_REPLACE_EXISTING,
                        new File(apkPath).getPath(),verificationParams,null);
                Log.i(TGA, "2");

            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.d(TGA, "安装失败1");
        }
    }

    public class PackageInstallObserver2 extends IPackageInstallObserver2.Stub {

        @Override
        public void packageInstalled(String packageName, int returnCode) throws RemoteException {
            if (returnCode == 1) //返回1表示安装成功，否则安装失败
            {
                //Toast.makeText(NetworkGetService.this, "安装成功！", Toast.LENGTH_SHORT).show();
                Log.e(TGA, "packageName=" + packageName + ",returnCode=" + returnCode);
            } else {
                //Toast.makeText(NetworkGetService.this, "安装失败！", Toast.LENGTH_SHORT).show();
                Log.d(TGA, "安装失败！");
            }
        }
    }


    private class ListDownloadListener extends DownloadListener{

        private DownloadTask mDownloadTask;

        public ListDownloadListener(DownloadTask task, Object tag) {
            super(tag);
            this.mDownloadTask = task;
            Log.d(TGA, "ListDownloadListener");
            //Toast.makeText(NetworkGetService.this, "ListDownloadListener", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart(Progress progress) {

        }

        @Override
        public void onProgress(Progress progress) {

        }

        @Override
        public void onError(Progress progress) {
            mDownloadTask.remove(true);
            stopSelf();
        }

        @Override
        public void onFinish(File file, Progress progress) {
//            Toast.makeText(NetworkGetService.this, "下载完成:" + progress.filePath,
//                    Toast.LENGTH_SHORT).show();
            Log.d(TGA, progress.filePath);
            //ApkUtils.install(getApplicationContext(), new File(progress.filePath));
            //mDownloadTask.remove(true);
            installPackage(progress.filePath);
            //mDownloadTask.remove();
            Log.d(TGA, "从installPackage退出了！");
            //stopSelf();

        }

        @Override
        public void onRemove(Progress progress) {

        }
    }



}
