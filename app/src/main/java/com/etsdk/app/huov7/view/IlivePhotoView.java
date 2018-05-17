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

import static com.etsdk.app.huov7.R.id.tv_game_title;

/**
 * Created by Administrator on 2018\3\29 0029.
 */

public class IlivePhotoView extends LinearLayout {
    @BindView(R.id.iv_game_img)
    RoundedImageView iv_game_img;
    private Context mContext;

    public IlivePhotoView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.item_photo, this);
        ButterKnife.bind(this);
    }

    public void setData(String url) {
        ImgUtil.setImg(getContext(), url, R.mipmap.icon_load, iv_game_img);
    }

//    @OnClick({R.id.item_game})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.item_game:
//                if (gameBean == null) {
//                    return;
//                }
//                GameDetailV2Activity.start(getContext(), gameBean.getGameid());
//                break;
//        }
//    }

}
