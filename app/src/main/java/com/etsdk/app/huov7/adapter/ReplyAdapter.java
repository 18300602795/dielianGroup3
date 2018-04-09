package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.Comment;
import com.etsdk.app.huov7.ui.ReplyActivity;
import com.etsdk.app.huov7.util.TimeUtils;
import com.game.sdk.SdkConstant;

import java.util.List;

/**
 * Created by Administrator on 2018\3\5 0005.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {
    private Context context;
    private ReplyAdapter2 adapter2;
    private List<Comment> comments;


    public ReplyAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public void addReply(List<Comment> comments) {
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyViewHolder holder, final int position) {
        holder.name_tv.setText(comments.get(position).getUname());
        holder.item_con.setText(comments.get(position).getContent());
        holder.tower_tv.setText("第" + String.valueOf(position + 2) + "楼");
        holder.time_tv.setText(TimeUtils.getTime(Long.valueOf(comments.get(position).getTime())));
        Glide.with(context).load(SdkConstant.BASE_URL + comments.get(position).getPortrait()).placeholder(R.mipmap.ic_launcher).into(holder.head_img);
        adapter2 = new ReplyAdapter2(context,comments.get(position).getReply());
        holder.item_recycle.setLayoutManager(new LinearLayoutManager(context));
        holder.item_recycle.setAdapter(adapter2);
        holder.item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("cont", comments.get(position));
                intent.putExtra("title", "第" + String.valueOf(position + 2) + "楼");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (comments == null)
            return 0;
        return comments.size();
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_ll;
        ImageView head_img;
        TextView name_tv, read_tv, item_con, tower_tv, time_tv;
        RecyclerView item_recycle;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            item_ll = itemView.findViewById(R.id.item_ll);
            head_img = itemView.findViewById(R.id.head_img);
            name_tv = itemView.findViewById(R.id.name_tv);
            read_tv = itemView.findViewById(R.id.read_tv);
            item_con = itemView.findViewById(R.id.item_con);
            time_tv = itemView.findViewById(R.id.time_tv);
            item_recycle = itemView.findViewById(R.id.item_recycle);
        }
    }

}
