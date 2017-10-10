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
package com.fengjw.tvhelper.update.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.update.DownloadAllActivity;
import com.fengjw.tvhelper.update.utils.ApkModel;
import com.fengjw.tvhelper.update.utils.ApkUtils;
import com.fengjw.tvhelper.update.utils.LogDownloadListener;
import com.fengjw.tvhelper.update.utils.NumberProgressBar;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/6/5
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private static final String TGA = "DownloadAdapter";

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;

    private List<DownloadTask> values;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type;
    private DownloadAllActivity mDownloadAllActivity;

    public DownloadAdapter(Context context) {
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public DownloadAdapter(){
        mDownloadAllActivity = new DownloadAllActivity();
        this.context = mDownloadAllActivity;
        //notifyDataSetChanged();
    }

    public void updateData(int type) {
        //这里是将数据库的数据恢复
        this.type = type;
        if (type == TYPE_ALL) values = OkDownload.restore(DownloadManager.getInstance().getAll());
        if (type == TYPE_FINISH) values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        if (type == TYPE_ING) values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        notifyDataSetChanged();//如果适配器的内容发生改变，强制调用getview刷新
        Log.d("DownloadAllActivity", "刷新UI了吗");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_download_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadTask task = values.get(position);
        String tag = createTag(task);
        task.register(new ListDownloadListener(tag, holder))//
                .register(new LogDownloadListener());
        holder.setTag(tag);
        holder.setTask(task);
        holder.bind();
        //holder.itemView.setFocusable(true);
        holder.refresh(task.progress);// 更新进度条
    }

    public void unRegister() {
        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        for (DownloadTask task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    private String createTag(DownloadTask task) {
        return type + "_" + task.progress.tag;
    }

    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.priority)
        TextView priority;
        @Bind(R.id.downloadSize)
        TextView downloadSize;
        @Bind(R.id.tvProgress)
        TextView tvProgress;
        @Bind(R.id.netSpeed)
        TextView netSpeed;
        @Bind(R.id.pbProgress)
        NumberProgressBar pbProgress;
        @Bind(R.id.start)
        Button download;
        @Bind((R.id.restart))
        Button redownload;
        private DownloadTask task;
        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            //
            //itemView.setFocusable(true);


            ButterKnife.bind(this, itemView);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void bind() {
            Progress progress = task.progress;
            ApkModel apk = (ApkModel) progress.extra1;
            if (apk != null) {
                //Glide.with(context).load(apk.iconUrl).error(R.mipmap.ic_launcher).into(icon);
                icon.setImageDrawable(apk.getIcon());
                name.setText(apk.name);
                priority.setText(apk.getDescription());
            } else {
                name.setText(progress.fileName);
            }
        }

        public void refresh(Progress progress) {
            String currentSize = Formatter.formatFileSize(context, (int)progress.currentSize);
            String totalSize = Formatter.formatFileSize(context, (int)progress.totalSize);
            downloadSize.setText(currentSize + "/" + totalSize);
            //priority.setText();
            switch (progress.status) {
                case Progress.NONE:
                    netSpeed.setText("停止");
                    download.setText("安装");
                    download.setFocusable(false);
                    redownload.setText("下载");
                    break;
                case Progress.PAUSE:
                    netSpeed.setText("暂停中");
                    download.setText("继续");
                    download.setFocusable(true);
                    redownload.setText("重新下载");
                    break;
                case Progress.ERROR:
                    netSpeed.setText("下载出错");
                    download.setText("出错");
                    redownload.setText("重新下载");
                    //download.removeCallbacks(task);
                    break;
                case Progress.WAITING:
                    netSpeed.setText("等待中");
                    download.setText("等待");
                    download.setFocusable(true);
                    redownload.setText("重新下载");
                    break;
                case Progress.FINISH:
                    netSpeed.setText("下载完成");
                    download.setText("安装");
                    download.setFocusable(true);
                    redownload.setText("重新下载");
                    //redownload.setFocusable(false);
                    break;
                case Progress.LOADING:
                    String speed = Formatter.formatFileSize(context, progress.speed);
                    netSpeed.setText(String.format("%s/s", speed));
                    download.setText("暂停");
                    download.setFocusable(true);
                    redownload.setText("重新下载");
                    break;
            }
            tvProgress.setText(numberFormat.format(progress.fraction));
            pbProgress.setMax(10000);
            pbProgress.setProgress((int) (progress.fraction * 10000));
        }

        @OnClick(R.id.start)
        public void start() {
            Progress progress = task.progress;
            switch (progress.status) {
                case Progress.PAUSE:
                case Progress.NONE:
                case Progress.ERROR:
                    task.start();
                    //task.restart();
                    break;
                case Progress.LOADING:
                    task.pause();
                    break;
                case Progress.FINISH:
                        ApkUtils.install(context.getApplicationContext(), new File(progress.filePath));
                        Log.d(TGA, "apkUrl : " + progress.filePath);
                    break;
            }
            refresh(progress);
        }

        @OnClick(R.id.remove)
        public void remove() {
            task.remove(true);
            updateData(type);
        }

        @OnClick(R.id.restart)
        public void restart() {
            task.restart();
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }

    private class ListDownloadListener extends DownloadListener {

        private ViewHolder holder;

        ListDownloadListener(Object tag, ViewHolder holder) {
            super(tag);
            this.holder = holder;
            //holder.itemView.setFocusable(true);
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, final Progress progress) { //下载完成，这里弹提示框
            //Toast.makeText(context, "下载完成:" + progress.filePath, Toast.LENGTH_SHORT).show();
            updateData(type);
            Log.d("NetworkGetService", "onFinish 方法内部");
            try {
                Log.d(TGA, "type : " + progress.priority);
                if (progress.priority == 2) {
                    ApkUtils.install(context.getApplicationContext(), new File(progress.filePath));
                    Log.d(TGA, "执行到这里了？");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.setDataAndType(Uri.fromFile(new File(progress.filePath)),
//                        "application/vnd.android.package-archive");
//                Log.d(TGA, progress.filePath);
//                //context.startActivity(intent);
//                context.getApplicationContext().startActivity(intent);
        }

        @Override
        public void onRemove(Progress progress) {

        }
    }

}

