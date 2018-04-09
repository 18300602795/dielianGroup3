package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.ReplyBean;

import java.util.List;

/**
 * Created by Administrator on 2018\3\6 0006.
 */

public class ReplyAdapter2 extends RecyclerView.Adapter<ReplyAdapter2.ItemReplyHolderView> {
    private Context context;
    private List<ReplyBean> replyBeens;

    public ReplyAdapter2(Context context, List<ReplyBean> replyBeens) {
        this.context = context;
        this.replyBeens = replyBeens;
    }

    @Override
    public ItemReplyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reply2, parent, false);
        return new ItemReplyHolderView(view);
    }

    @Override
    public void onBindViewHolder(ItemReplyHolderView holder, int position) {
        String msg = replyBeens.get(position).getForm_uname() + "回复" + replyBeens.get(position).getTo_uname() + "：" + replyBeens.get(position).getContent();
        holder.con_tv.setText(msg);
    }

    @Override
    public int getItemCount() {
        if (replyBeens == null)
            return 0;
        return replyBeens.size();
    }

    class ItemReplyHolderView extends RecyclerView.ViewHolder {
        TextView con_tv;

        public ItemReplyHolderView(View itemView) {
            super(itemView);
            con_tv = itemView.findViewById(R.id.con_tv);
        }
    }

}
