package com.fengjw.tvhelper.recenttask.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;

import java.util.HashMap;
import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;

/**
 * Created by fengjw on 2017/9/28.
 */

public class AppAdapter extends BaseAdapter{

    private Context mContext;
    List<HashMap<String,Object>> appInfos;
    private LayoutInflater mInflater;
    private HashMap<String, Object> singleAppInfo;

    public AppAdapter(Context context, List<HashMap<String,Object>> appInfos){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        //final ViewHolder mHolder;
        singleAppInfo = appInfos.get(i);
        view = mInflater.inflate(R.layout.item_listview, null);
        ImageView image = (ImageView) view.findViewById(R.id.image_app);
        TextView name = (TextView) view.findViewById(R.id.name_app);
        TextView size = (TextView) view.findViewById(R.id.size_app);
        TextView cachesize = (TextView) view.findViewById(R.id.cachesize_app);
        Intent singleIntent = (Intent) singleAppInfo.get("tag");
        view.setTag(singleIntent);
        image.setImageDrawable((Drawable) singleAppInfo.get("icon"));
        name.setText(singleAppInfo.get("title").toString());
        size.setText(singleAppInfo.get("tag").toString());
        cachesize.setText(singleAppInfo.get("packageName").toString());

        Log.d(TGA, "OnClickListener!");

        //绑定点击事件，用来进行应用间的跳转
        view.setOnClickListener(new SingleAppClickListener());
        //view.setOnClickListener(this);
        return view;
    }



    //点击应用的图标启动应用程序
    class SingleAppClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = (Intent)v.getTag();
            Log.d("fengjw", "click");
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                try {
                    mContext.startActivity(intent);
                }
                catch (ActivityNotFoundException e) {
                    Log.d("fengjw", "Unable to launch recent task", e);
                }
            }else {
                Log.d("fengjw", "intent is null!");
            }
        }
    }

//    private class ViewHolder{
//        ImageView image;
//        TextView name;
//        TextView size;
//        TextView cachesize;
//        Button mButton;
//        LinearLayout mLayout;
//    }

}
