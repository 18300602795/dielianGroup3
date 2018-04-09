package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.GuildHeader;
import com.etsdk.app.huov7.view.header_view.GuildHeaderView;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class GuildHeaderViewProvider
        extends ItemViewProvider<GuildHeader, GuildHeaderViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new GuildHeaderView(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GuildHeader guildHeader) {
        holder.headerView.setGuildHeader(guildHeader);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        GuildHeaderView headerView;
        ViewHolder(GuildHeaderView itemView) {
            super(itemView);
            headerView = itemView;
        }
    }
}