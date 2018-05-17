package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.iLive.model.CurLiveInfo;
import com.etsdk.app.huov7.iLive.model.MySelfInfo;
import com.etsdk.app.huov7.iLive.utils.Constants;
import com.etsdk.app.huov7.iLive.views.LiveActivity;
import com.etsdk.app.huov7.model.RoomModel;
import com.etsdk.app.huov7.util.ImgUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liang530.log.L;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/11.
 */

public class RoomAdapter extends XRecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<RoomModel> roomModels;
    private List<Integer> colors;

    public RoomAdapter(List<RoomModel> roomModels) {
        this.roomModels = roomModels;
        colors = new ArrayList<>();
        addColors();
    }

    private void addColors() {
        colors.add(R.drawable.room_colors1);
        colors.add(R.drawable.room_colors2);
        colors.add(R.drawable.room_colors3);
        colors.add(R.drawable.room_colors4);
        colors.add(R.drawable.room_colors5);
    }

    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RoomAdapter.RoomViewHolder holder, final int position) {
        holder.room_bg.setBackgroundResource(colors.get(position % colors.size()));
        holder.create_tv.setText(roomModels.get(position).getCreateName());
        holder.room_title.setText(roomModels.get(position).getRoomTitle());
        holder.num_tv.setText(String.valueOf(roomModels.get(position).getNum()));
        ImgUtil.setImg(holder.context, roomModels.get(position).getFaceUrl(), R.mipmap.icon_load, holder.iv_game_img);
        holder.room_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, LiveActivity.class);
                MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
                MySelfInfo.getInstance().setJoinRoomWay(false);
                L.i("333", "id：" + MySelfInfo.getInstance().getId());
                L.i("333", "roomNum：" + MySelfInfo.getInstance().getMyRoomNum());
                CurLiveInfo.setTitle("直播间");
                CurLiveInfo.setHostID(roomModels.get(position).getCreateId());
                CurLiveInfo.setRoomNum(roomModels.get(position).getRoomId());
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return roomModels.size();
    }


    class RoomViewHolder extends XRecyclerView.ViewHolder {
        @BindView(R.id.room_bg)
        RelativeLayout room_bg;
        @BindView(R.id.room_title)
        TextView room_title;
        @BindView(R.id.iv_game_img)
        RoundedImageView iv_game_img;
        @BindView(R.id.create_tv)
        TextView create_tv;
        @BindView(R.id.num_tv)
        TextView num_tv;
        Context context;

        public RoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}
