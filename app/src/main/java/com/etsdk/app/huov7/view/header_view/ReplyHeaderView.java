package com.etsdk.app.huov7.view.header_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.Comment;
import com.etsdk.app.huov7.util.TimeUtils;
import com.game.sdk.SdkConstant;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;


/**
 * Created by Administrator on 2018\2\22 0022.
 */

public class ReplyHeaderView extends RelativeLayout {
    View mRootView;
    Context mContext;
    TextView check_tv;
    TextView name_tv;
    TextView item_con;
    TextView time_tv;
    RoundedImageView head_img;

    public ReplyHeaderView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public ReplyHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public ReplyHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.reply_header, this);
        check_tv = mRootView.findViewById(R.id.check_tv);
        item_con = mRootView.findViewById(R.id.item_con);
        name_tv = mRootView.findViewById(R.id.name_tv);
        time_tv = mRootView.findViewById(R.id.time_tv);
        head_img = mRootView.findViewById(R.id.head_img);
        check_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
    }

    public void setData(Comment comment) {
        Glide.with(mContext).load(SdkConstant.BASE_URL + comment.getPortrait()).placeholder(R.mipmap.ic_launcher).into(head_img);
        name_tv.setText(comment.getUname());
        item_con.setText(comment.getContent());
        time_tv.setText(TimeUtils.getTime(Long.valueOf(comment.getTime())));

    }

}
