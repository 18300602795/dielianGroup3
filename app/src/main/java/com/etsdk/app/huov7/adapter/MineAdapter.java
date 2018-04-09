package com.etsdk.app.huov7.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.StartupResultBean;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.AccountManageActivity;
import com.etsdk.app.huov7.ui.ArticleActivity;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.LoginActivity;
import com.etsdk.app.huov7.ui.MineGiftCouponListActivityNew;
import com.etsdk.app.huov7.update.UpdateVersionDialog;
import com.etsdk.app.huov7.update.UpdateVersionService;
import com.liang530.log.T;
import com.liang530.utils.GlideDisplay;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018\3\16 0016.
 */

public class MineAdapter extends RecyclerView.Adapter<MineAdapter.MineViewHolder> {
    private Context context;
    private UserInfoResultBean data;

    public MineAdapter(Context context) {
        this.context = context;
    }

    public void setData(UserInfoResultBean data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public MineAdapter.MineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mine, parent, false);
        return new MineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MineAdapter.MineViewHolder holder, int position) {
        if (data != null) {
            holder.info_ll.setVisibility(View.VISIBLE);
            holder.off_line.setVisibility(View.GONE);
            holder.nick_tv.setText(data.getNickname());
            GlideDisplay.display(holder.iv_mineHead, data.getPortrait(), R.mipmap.ic_launcher);
        } else {
            holder.info_ll.setVisibility(View.GONE);
            holder.off_line.setVisibility(View.VISIBLE);
        }
        holder.account_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //个人中心
                if (data != null) {
                    AccountManageActivity.start(context);
                } else {
                    LoginActivity.start(context);
                }
            }
        });
        holder.video_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.gift_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MineGiftCouponListActivityNew.start(context, MineGiftCouponListActivityNew.TYPE_GIFT, "礼包");
            }
        });

        holder.game_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //我的游戏
                DownloadManagerActivity.start(context);
            }
        });

        holder.post_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleActivity.start(context);
            }
        });

        holder.update_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleUpdate();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * 处理版本更新信息
     */
    private void handleUpdate() {
        final boolean showCancel;
        final StartupResultBean.UpdateInfo updateInfo = EventBus.getDefault().getStickyEvent(StartupResultBean.UpdateInfo.class);
        if (updateInfo != null) {//有更新
            if ("1".equals(updateInfo.getUp_status())) {//强制更新
                showCancel = false;
            } else if ("2".equals(updateInfo.getUp_status())) {//选择更新
                showCancel = true;
            } else {
                return;
            }
            if (TextUtils.isEmpty(updateInfo.getUrl()) ||
                    (!updateInfo.getUrl().startsWith("http") && !updateInfo.getUrl().startsWith("https"))) {
                return;//url不可用
            }
            new UpdateVersionDialog().showDialog(context, showCancel, updateInfo.getContent(), new UpdateVersionDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    Intent intent = new Intent(context, UpdateVersionService.class);
                    intent.putExtra("url", updateInfo.getUrl());
                    context.startService(intent);
                    T.s(context, "开始下载,请在下载完成后确认安装！");
                    if (!showCancel) {//是强更则关闭界面
                        ((Activity) context).finish();
                    }
                }

                @Override
                public void cancel() {
                }
            });
        } else {
            T.s(context, "当前已为最新版本");
        }
    }

    class MineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.account_ll)
        LinearLayout account_ll;
        @BindView(R.id.info_ll)
        LinearLayout info_ll;
        @BindView(R.id.off_line)
        TextView off_line;
        @BindView(R.id.iv_mineHead)
        RoundedImageView iv_mineHead;
        @BindView(R.id.nick_tv)
        TextView nick_tv;
        @BindView(R.id.sign_tv)
        TextView sign_tv;
        @BindView(R.id.video_ll)
        LinearLayout video_ll;
        @BindView(R.id.gift_ll)
        LinearLayout gift_ll;
        @BindView(R.id.game_ll)
        LinearLayout game_ll;
        @BindView(R.id.post_ll)
        LinearLayout post_ll;
        @BindView(R.id.update_ll)
        LinearLayout update_ll;
        @BindView(R.id.version_tv)
        TextView version_tv;

        public MineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
