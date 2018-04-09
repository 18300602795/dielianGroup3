package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.view.NewListGameItem2;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class GameItemViewProvider2
        extends ItemViewProvider<GameBean, GameItemViewProvider2.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new NewListGameItem2(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GameBean gameBean) {
        holder.listGameItem.setGameBean(gameBean);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        NewListGameItem2 listGameItem;

        ViewHolder(NewListGameItem2 itemView) {
            super(itemView);
            listGameItem = itemView;
        }
    }
}