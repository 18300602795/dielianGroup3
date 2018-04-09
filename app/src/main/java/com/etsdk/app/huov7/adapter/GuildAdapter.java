package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.view.NewListGameItem2;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildAdapter extends XRecyclerView.Adapter<GuildAdapter.GuildViewHolder> {

    private List<GameBean> gameBeens;
    private Context mContext;

    public GuildAdapter(List<GameBean> gameBeens, Context mContext) {
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
        return new GuildViewHolder(new NewListGameItem2(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(GuildViewHolder holder, int position) {
        holder.listGameItem.setGameBean(gameBeens.get(position));
    }

    @Override
    public int getItemCount() {
        return gameBeens.size();
    }


    class GuildViewHolder extends RecyclerView.ViewHolder {
        NewListGameItem2 listGameItem;

        GuildViewHolder(NewListGameItem2 itemView) {
            super(itemView);
            listGameItem = itemView;
        }
    }

}
