package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.game.sdk.log.L;

import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class GiftImgAdapter extends BaseAdapter {
    List<Integer> integers;
    Context context;

    public GiftImgAdapter(List<Integer> integers, Context context) {
        this.integers = integers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return integers.size();
    }

    @Override
    public Object getItem(int position) {
        return integers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GiftHolder holder;
        if (convertView == null) {
            holder = new GiftHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gift, null);
            holder.gift_img = (ImageView) convertView.findViewById(R.id.gift_img);
            holder.gift_des = (TextView) convertView.findViewById(R.id.gift_des);
            holder.gift_price = (TextView) convertView.findViewById(R.id.gift_price);
            convertView.setTag(holder);
        } else {
            holder = (GiftHolder) convertView.getTag();
        }
        holder.gift_img.setImageResource(integers.get(position));
        return convertView;
    }

    class GiftHolder {
        ImageView gift_img;
        TextView gift_des;
        TextView gift_price;
    }

}
