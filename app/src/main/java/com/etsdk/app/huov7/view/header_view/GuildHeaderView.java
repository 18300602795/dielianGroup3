package com.etsdk.app.huov7.view.header_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GuildHeader;
import com.etsdk.app.huov7.ui.MemberListActivity;
import com.etsdk.app.huov7.ui.OtherInfoActivity;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.SdkConstant;
import com.liang530.views.imageview.CircleImageView;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildHeaderView extends LinearLayout {
    GuildHeader guildHeader;
    @BindView(R.id.iv_mineHead)
    RoundedImageView iv_mineHead;
    @BindView(R.id.member_icons)
    LinearLayout member_icons;
    @BindView(R.id.notice_tv)
    TextView notice_tv;
    @BindView(R.id.master_name)
    TextView master_name;
    Context context;

    public GuildHeaderView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.header_guild, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_mineHead})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mineHead:
                OtherInfoActivity.start(context, guildHeader.getPresident());
                break;
        }
    }

    public void setGuildHeader(final GuildHeader guildHeader) {
        if (guildHeader != null && guildHeader.getMembers() != null && guildHeader.getGuild() != null && guildHeader.getPresident() != null){
            this.guildHeader = guildHeader;
            member_icons.removeAllViews();
            master_name.setText(guildHeader.getPresident().getNickname());
            notice_tv.setText(guildHeader.getGuild().get(0).getAnnouncement());
            Glide.with(context).load(SdkConstant.BASE_URL + guildHeader.getPresident().getPortrait()).placeholder(R.drawable.bg_game).into(iv_mineHead);
            for (int i = 0; i < guildHeader.getMembers().size(); i++) {
                CircleImageView roundedImageView = new CircleImageView(context);
                final int finalI = i;
                roundedImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OtherInfoActivity.start(context, guildHeader.getMembers().get(finalI));
                    }
                });
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(StringUtils.dip2px(context, 30), StringUtils.dip2px(context, 30));
                layoutParams.setMargins(StringUtils.dip2px(context, 10), 0, StringUtils.dip2px(context, 10), 0);
                roundedImageView.setLayoutParams(layoutParams);
                Glide.with(context).load(SdkConstant.BASE_URL + guildHeader.getMembers().get(i).getPortrait()).placeholder(R.drawable.bg_game).into(roundedImageView);
                member_icons.addView(roundedImageView);
            }
            CircleImageView roundedImageView = new CircleImageView(context);
            roundedImageView.setImageResource(R.mipmap.member_more);
            roundedImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    MemberListActivity.start(context);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(StringUtils.dip2px(context, 30), StringUtils.dip2px(context, 30));
            layoutParams.setMargins(StringUtils.dip2px(context, 10), 0, StringUtils.dip2px(context, 10), 0);
            roundedImageView.setLayoutParams(layoutParams);
            member_icons.addView(roundedImageView);
        }
    }
}
