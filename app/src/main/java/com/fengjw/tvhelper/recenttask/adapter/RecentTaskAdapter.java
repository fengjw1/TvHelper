package com.fengjw.tvhelper.recenttask.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fengjw.tvhelper.R;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fengjw on 2017/10/27.
 */

public class RecentTaskAdapter extends RecyclerView.Adapter<RecentTaskAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<String, Object> singleAppInfo;
    private OnItemClickListener mOnItemClickListener = null;
    private List<HashMap<String,Object>> mAppInfos;
    private static final String TAG = "RecentTaskAdapter";
    public RecentTaskAdapter(Context context, List<HashMap<String,Object>> appInfos){
        super();
        mContext = context;
        mAppInfos = appInfos;
        mInflater = LayoutInflater.from(context);
    }

    public static interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_listview_new_2, parent, false);
        Log.d(TAG, "onCreateViewHolder");
//        Intent singleIntent = (Intent) singleAppInfo.get("tag");
//        view.setTag(singleIntent);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    public void updateData(int position){
        mAppInfos.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "positon = " + position);
        singleAppInfo = mAppInfos.get(position);
        holder.image.setImageDrawable((Drawable) singleAppInfo.get("icon"));
        holder.name.setText(singleAppInfo.get("title").toString());

        holder.itemView.setFocusable(true);
        if (position == 0) {
            Log.d(TAG, "requestFocus");
            holder.itemView.requestFocus();
        }
        Log.d(TAG, "out !");
        //itemview animation
        holder.itemView.setOnFocusChangeListener(new HomeFocusListener());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mAppInfos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name;
        private LinearLayout mLinearLayout;
//        private TextView size;
//        private TextView cachesize;
//        private Button btn_stoprun;
//        private LinearLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_app);
            name = (TextView) itemView.findViewById(R.id.name_app);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.lin_app);
//            size = (TextView) itemView.findViewById(R.id.size_app);
//            cachesize = (TextView) itemView.findViewById(R.id.cachesize_app);
//            btn_stoprun = (Button) itemView.findViewById(R.id.btn_stoprun);
//            mLayout = (LinearLayout) itemView.findViewById(R.id.linear_app);
            Log.d(TAG, "ViewHolder!");
        }
    }


    class HomeFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                //zoomOutWindow(view);
                ViewCompat.animate(view)
                        .setDuration(200)
                        .scaleX(1.3f)
                        .scaleY(1.3f)
                        .start();
            }else {
                //zoomInWindow(view);
                ViewCompat.animate(view)
                        .setDuration(200)
                        .scaleX(1f)
                        .scaleY(1f)
                        .start();
            }
        }
    }

//    private void zoomOutWindow(View view) {
//        AnimationSet animationSet = new AnimationSet(true);
//        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(500);
//        animation.setFillAfter(true);
//        animationSet.addAnimation(animation);
//        animationSet.setFillAfter(true);
//        view.clearAnimation();
//        view.startAnimation(animationSet);
//    }
//
//    private void zoomInWindow(View view) {
//        AnimationSet animationSet = new AnimationSet(true);
//        ScaleAnimation animation = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(500);
//        animation.setFillAfter(true);
//        animationSet.addAnimation(animation);
//        animationSet.setFillAfter(true);
//        view.startAnimation(animationSet);
//    }

}
