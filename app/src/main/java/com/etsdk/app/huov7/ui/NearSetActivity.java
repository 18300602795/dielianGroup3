package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.NearbyAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.etsdk.app.huov7.R.id.del_iv;
import static com.etsdk.app.huov7.R.id.love_iv;
import static com.etsdk.app.huov7.R.id.nearby;


/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class NearSetActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_set);
        ButterKnife.bind(this);
        tvTitleName.setText("我");
    }


    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NearSetActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
