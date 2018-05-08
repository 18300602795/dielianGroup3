package com.etsdk.app.huov7.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.util.ImgUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\3\29 0029.
 */

public class GirlItemView extends LinearLayout {
    @BindView(R.id.item_game)
    LinearLayout item_game;
    @BindView(R.id.iv_game_img)
    RoundedImageView iv_game_img;
    @BindView(R.id.tv_game_title)
    TextView tv_game_title;
    private Context mContext;

    public GirlItemView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.item_girl, this);
        ButterKnife.bind(this);
    }

    public void setData(int gameBean) {
        iv_game_img.setImageResource(gameBean);
    }

    @OnClick({R.id.item_game})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_game:
                break;
        }
    }

}
