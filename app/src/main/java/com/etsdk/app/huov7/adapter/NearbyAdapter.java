package com.etsdk.app.huov7.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/11.
 */

public class NearbyAdapter extends BaseAdapter {
    private List<String> names;
    private Context context;

    public NearbyAdapter(List<String> names, Context context) {
        this.names = names;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NearViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nearby, parent, false);
            holder = new NearViewHolder();
            holder.cardview = (CardView) convertView.findViewById(R.id.cardview);
            holder.near_name = (TextView) convertView.findViewById(R.id.near_name);
            holder.iv_love = (ImageView) convertView.findViewById(R.id.iv_love);
            holder.iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
            convertView.setTag(holder);
        } else {
            holder = (NearViewHolder) convertView.getTag();
        }
        holder.near_name.setText(names.get(position % names.size()));
        return convertView;
    }


    class NearViewHolder {
        CardView cardview;
        TextView near_name;
        ImageView iv_love;
        ImageView iv_del;
    }
}
