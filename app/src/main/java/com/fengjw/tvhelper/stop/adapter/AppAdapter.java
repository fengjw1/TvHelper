package com.fengjw.tvhelper.stop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;

import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;

/**
 * Created by fengjw on 2017/9/28.
 */

public class AppAdapter extends BaseAdapter{

    private Context mContext;
    private List<StopAppInfo> mList;
    private LayoutInflater mInflater;

    public AppAdapter(Context context, List<StopAppInfo> list){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
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
        final ViewHolder mHolder;
        StopAppInfo appInfo = mList.get(i);
        if (view == null){
            view = mInflater.inflate(R.layout.item_listview, null);
            mHolder = new ViewHolder();
            mHolder.image = (ImageView) view.findViewById(R.id.image_app);
            mHolder.name = (TextView) view.findViewById(R.id.name_app);
            mHolder.size = (TextView) view.findViewById(R.id.size_app);
            mHolder.cachesize = (TextView) view.findViewById(R.id.cachesize_app);
            mHolder.mLayout = (LinearLayout) view.findViewById(R.id.linear_app);
            view.setTag(mHolder);
        }else {
            mHolder = (ViewHolder) view.getTag();
        }

        mHolder.image.setImageDrawable(appInfo.getIconResource());
        mHolder.name.setText(appInfo.getName());
        mHolder.size.setText(appInfo.getSize());
        mHolder.cachesize.setText(appInfo.getCacheSize());

        Log.d(TGA, "OnClickListener!");
        return view;
    }



    private class ViewHolder{
        ImageView image;
        TextView name;
        TextView size;
        TextView cachesize;
        LinearLayout mLayout;
    }

}
