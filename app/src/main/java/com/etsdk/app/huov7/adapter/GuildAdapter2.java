package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.view.HunterView;
import com.etsdk.app.huov7.view.NewListGameItem2;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildAdapter2 extends XRecyclerView.Adapter<GuildAdapter2.GuildViewHolder> {

    private List<GameBean> gameBeens;
    private Context mContext;

    public GuildAdapter2(List<GameBean> gameBeens, Context mContext) {
        this.gameBeens = gameBeens;
        this.mContext = mContext;
    }

    public void upDate(List<GameBean> gameBeens) {
        this.gameBeens.clear();
        this.gameBeens.addAll(gameBeens);
        notifyDataSetChanged();
    }

    public void addDate(List<GameBean> gameBeens) {
        this.gameBeens.addAll(gameBeens);
        notifyDataSetChanged();
    }


    @Override
    public GuildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GuildViewHolder(new HunterView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(GuildViewHolder holder, int position) {
//        holder.listGameItem.setGameBean(gameBeens.get(position));
        holder.listGameItem.setGameBeanList();
    }

    @Override
    public int getItemCount() {
        return 5;
    }


    class GuildViewHolder extends RecyclerView.ViewHolder {
        HunterView listGameItem;

        GuildViewHolder(HunterView itemView) {
            super(itemView);
            listGameItem = itemView;
        }
    }

}
