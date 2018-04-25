package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.MemberModel;
import com.etsdk.app.huov7.ui.OtherInfoActivity;
import com.etsdk.app.huov7.util.ImgUtil;
import com.game.sdk.SdkConstant;
import com.liang530.views.imageview.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/5.
 */
public class MemberListItemViewProvider
        extends ItemViewProvider<MemberModel, MemberListItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_member, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final MemberModel memberModel) {
        holder.master_name.setText(memberModel.getNickname());
        holder.post_tv.setVisibility(View.VISIBLE);
        ImgUtil.setImg(holder.context, SdkConstant.BASE_URL + memberModel.getPortrait(), R.drawable.bg_game, holder.iv_mineHead);
        switch (memberModel.getPosition()) {
            case "1":
                holder.post_tv.setText("会长");
                break;
            case "2":
                holder.post_tv.setText("副会长");
                break;
            case "3":
                holder.post_tv.setText("管理员");
                break;
            case "4":
                holder.post_tv.setVisibility(View.GONE);
                break;
        }
        holder.item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtherInfoActivity.start(holder.context, memberModel.getUsername());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_ll)
        LinearLayout item_ll;
        @BindView(R.id.iv_mineHead)
        CircleImageView iv_mineHead;
        @BindView(R.id.master_name)
        TextView master_name;
        @BindView(R.id.post_tv)
        TextView post_tv;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}