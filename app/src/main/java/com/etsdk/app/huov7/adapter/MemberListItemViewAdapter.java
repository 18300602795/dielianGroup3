package com.etsdk.app.huov7.adapter;

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
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liang530.utils.GlideDisplay;
import com.liang530.views.imageview.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/5.
 */
public class MemberListItemViewAdapter
        extends XRecyclerView.Adapter<MemberListItemViewAdapter.ViewHolder> {
    private Context context;
    private List<MemberModel> memberModels;

    public MemberListItemViewAdapter(Context context, List<MemberModel> memberModels) {
        this.context = context;
        this.memberModels = memberModels;
    }

    public void upDate(List<MemberModel> memberModels) {
        this.memberModels.clear();
        this.memberModels.addAll(memberModels);
        notifyDataSetChanged();
    }

    public void addDate(List<MemberModel> memberModels) {
        this.memberModels.addAll(memberModels);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MemberModel memberModel = memberModels.get(position);
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

    @Override
    public int getItemCount() {
        return memberModels.size();
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