package com.etsdk.app.huov7.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.ui.MainGameActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\29 0029.
 */

public class MoreItemView extends LinearLayout {
    private Context mContext;

    public MoreItemView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.item_more, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.item_game})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_game:
                MainGameActivity.start(mContext);
                break;
        }
    }

}
