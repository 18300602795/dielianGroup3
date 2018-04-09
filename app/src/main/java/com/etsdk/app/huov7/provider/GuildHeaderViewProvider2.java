package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.view.HunterHeaderView;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class GuildHeaderViewProvider2
        extends ItemViewProvider<GameBeanList, GuildHeaderViewProvider2.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new HunterHeaderView(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GameBeanList gameBeanList) {
        holder.headerView.setGameBeanList(gameBeanList);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        HunterHeaderView headerView;
        ViewHolder(HunterHeaderView itemView) {
            super(itemView);
            headerView = itemView;
        }
    }
}