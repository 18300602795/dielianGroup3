package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.Comment;
import com.etsdk.app.huov7.ui.ReplyActivity;
import com.etsdk.app.huov7.util.ImgUtil;
import com.etsdk.app.huov7.util.TimeUtils;
import com.game.sdk.SdkConstant;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/5.
 */
public class CommentListItemViewAdapter
        extends XRecyclerView.Adapter<CommentListItemViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Comment> comments;

    public CommentListItemViewAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(root);
    }

    public void upDate(List<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    public void addDate(List<Comment> comments) {
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comment comment = comments.get(position);
        holder.name_tv.setText(comment.getUname());
        holder.item_con.setText(comment.getContent());
        holder.time_tv.setText(TimeUtils.getTime(Long.valueOf(comment.getTime())));
        ImgUtil.setImg(holder.context, SdkConstant.BASE_URL + comment.getPortrait(), R.mipmap.ic_launcher, holder.head_img);
        holder.adapter2 = new ReplyAdapter2(holder.context, comment.getReply());
        holder.item_recycle.setLayoutManager(new LinearLayoutManager(holder.context));
        holder.item_recycle.setAdapter(holder.adapter2);
        holder.item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, ReplyActivity.class);
                intent.putExtra("cont", comment);
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_ll)
        LinearLayout item_ll;
        @BindView(R.id.head_img)
        RoundedImageView head_img;
        @BindView(R.id.name_tv)
        TextView name_tv;
        @BindView(R.id.read_tv)
        TextView read_tv;
        @BindView(R.id.item_con)
        TextView item_con;
        @BindView(R.id.time_tv)
        TextView time_tv;
        @BindView(R.id.item_recycle)
        RecyclerView item_recycle;
        ReplyAdapter2 adapter2;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}