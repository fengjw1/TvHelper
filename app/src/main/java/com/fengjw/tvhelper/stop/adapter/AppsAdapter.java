package com.fengjw.tvhelper.stop.adapter;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.stop.utils.ForceStopManager;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;

import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;

/**
 * Created by fengjw on 2017/10/11.
 */

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {


    private Context mContext;
    private List<StopAppInfo> mList;
    private final LayoutInflater mLayoutInflater;

    //初始化的部分
    private ForceStopManager mForceStopManager;
    private ApplicationsState mApplicationsState;
    private StopAppInfo mAppInfo;
    private Application app;
    private String mPackageName;


    public AppsAdapter(Context context, List<StopAppInfo> list, Application app) {
        super();
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
        this.app = app;
        mApplicationsState = ApplicationsState.getInstance(app);
        Log.d(TGA, "AppsAdapter!");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_listview_new, parent, false);
        Log.d(TGA, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TGA, "positon = " + position);
        mAppInfo = mList.get(position);
        holder.image.setImageDrawable(mAppInfo.getIconResource());
        holder.name.setText(mAppInfo.getName());
        holder.size.setText(mAppInfo.getSize());
        holder.cachesize.setText(mAppInfo.getCacheSize());

        //holder.btn_stoprun.setText(mAppInfo.getName());
        //holder.btn_stoprun.setFocusable(true);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mAppInfo = mList.get(position);//这里是获取当前要清除的Apk信息
                    //因为在onBindViewHolder中会加载很多的apk信息，但是每次删除一个
                    Toast.makeText(mContext, "click name = " + mAppInfo.getName(),
                            Toast.LENGTH_SHORT).show();
                    mForceStopManager = new ForceStopManager(mContext, mAppInfo);
                    if (mForceStopManager.canForceStop()){
                        onForceStopOk();
                        //Log.d(TGA, "getItemId : " + getItemId(position));
                        //Log.d(TGA, "getItemViewType : " + getItemViewType(position));
                        removeData(position);
                        Log.d(TGA, "delete");
                    }else {
                        Log.d(TGA, "no delete");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Log.d(TGA, "onBindViewHolder");
    }

//    private void init(){
//        mPackageName = mAppInfo.getPackageName();
//        Log.d(TGA, "PackageName = " + mPackageName);
//        //final int userId = UserHandle.myUserId();
//        //Log.d(TGA, "userId : " + userId);
//        //final ApplicationsState.AppEntry entry = mApplicationsState.getEntry(mPackageName, userId);
//        Log.d(TGA, "mApplicationsState :" + mApplicationsState);
//        //mAppInfo = new StopAppInfo(mContext, entry);
//        mForceStopManager = new ForceStopManager(mContext, mAppInfo);
//        Log.d(TGA, "appInfo: name =" + mAppInfo.getName()
//                + " CacheSize =" + mAppInfo.getCacheSize()
//                + " DateSize =" + mAppInfo.getDataSize()
//                + " Size =" + mAppInfo.getSize()
//                + " Version =" + mAppInfo.getVersion());
//    }

    private void removeData(int position){
        mList.remove(position);
        //notifyDataSetChanged();
        //notifyItemRemoved(position);
        notifyItemRangeRemoved(position, mList.size() - position);
    }

    @Override
    public int getItemCount() {
        Log.d(TGA, "mList.size() = " + mList.size());
        return mList.size();
    }


    private void onForceStopOk() {
        mForceStopManager.forceStop(mApplicationsState);
        Log.d(TGA, "onForceStopOK");
        //onBackPressed();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private TextView size;
        private TextView cachesize;
        private Button btn_stoprun;
        private LinearLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_app);
            name = (TextView) itemView.findViewById(R.id.name_app);
            size = (TextView) itemView.findViewById(R.id.size_app);
            cachesize = (TextView) itemView.findViewById(R.id.cachesize_app);
            btn_stoprun = (Button) itemView.findViewById(R.id.btn_stoprun);
            mLayout = (LinearLayout) itemView.findViewById(R.id.linear_app);
            Log.d(TGA, "ViewHolder!");
        }


    }

}
