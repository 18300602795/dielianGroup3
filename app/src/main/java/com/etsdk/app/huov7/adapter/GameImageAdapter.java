package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.util.ImgUtil;
import com.game.sdk.log.L;
import com.liang530.photopicker.ShowPicVPActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by liu hong liang on 2016/10/11.
 */

public class GameImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private GameBean data;

    public GameImageAdapter(GameBean data) {
        if (data == null){
            this.data = new GameBean();
        }else {
            this.data = data;
        }

    }

    public void setData(GameBean data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_image, parent, false);
            return new ViewHolder(inflate);
        } else {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_vedio, parent, false);
            return new VideoHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        GlideDisplay.display(holder.ivGameImg,data.get(position),R.mipmap.portrait_load);
        if (holder instanceof ViewHolder) {
            ImgUtil.setImg(((ViewHolder) holder).ivGameImg.getContext(), data.getImage().get(position), R.mipmap.gg, ((ViewHolder) holder).ivGameImg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPicVPActivity.start(v.getContext(), data.getImage(), position, false);
                }
            });
        } else if (holder instanceof VideoHolder) {
            ImgUtil.setImg(((VideoHolder) holder).ivGameImg.getContext(), data.getImage().get(position), R.mipmap.gg, ((VideoHolder) holder).ivGameImg);
            String url = "http://tui.idielian.com";
            if (TextUtils.isEmpty(data.getApp_video())) {
                ((VideoHolder) holder).player_iv.setVisibility(View.GONE);
            } else {
                url = url + data.getApp_video();
            }
            final String videoUrl = url;
            L.i("333", "ur：" + videoUrl);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShowVideoActivity.start(v.getContext());
                    if (!TextUtils.isEmpty(data.getApp_video())) {
                        JCVideoPlayerStandard.startFullscreen(v.getContext(), JCVideoPlayerStandard.class, videoUrl, "");
                    } else {
                        ShowPicVPActivity.start(v.getContext(), data.getImage(), position, false);
                    }
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return data.getImage() == null ? 0 : data.getImage().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_img)
        ImageView ivGameImg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class VideoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_img)
        ImageView ivGameImg;
        @BindView(R.id.player_iv)
        ImageView player_iv;

        public VideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
