package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.NearbyAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class NearbyActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.nearby)
    SwipeDeck nearby;
    @BindView(R.id.get_iv)
    ImageView get_iv;
    @BindView(R.id.del_iv)
    ImageView del_iv;
    @BindView(R.id.love_iv)
    ImageView love_iv;
    @BindView(R.id.col_iv)
    ImageView col_iv;
    private NearbyAdapter adapter;
    private List<String> names;
    private boolean isReturn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);
        names = new ArrayList<>();
        addNames();
        tvTitleName.setText("点点");
        adapter = new NearbyAdapter(names, mContext);
        nearby.setAdapter(adapter);
        nearby.setLeftImage(R.id.iv_del);
        nearby.setRightImage(R.id.iv_love);
        nearby.setDelView(del_iv);
        nearby.setLoveView(love_iv);

        nearby.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long stableId) {
                get_iv.setImageResource(R.drawable.ic_undo_dislike_enable);
                isReturn = true;
            }

            @Override
            public void cardSwipedRight(long stableId) {
                get_iv.setImageResource(R.drawable.ic_undo_dislike_disable);
                isReturn = false;
            }

            @Override
            public void returnSwipedLeft(long itemId) {
                get_iv.setImageResource(R.drawable.ic_undo_dislike_disable);
                isReturn = false;
            }

            @Override
            public boolean isDragEnabled(long itemId) {
                return true;
            }
        });
    }

    private void addNames() {
        for (int i = 0; i < 15; i++) {
            names.add("aaa");
            names.add("bbb");
            names.add("ccc");
        }
    }


    @OnClick({R.id.iv_titleLeft, R.id.matching_iv, R.id.mine_iv, R.id.get_iv, R.id.del_iv, R.id.love_iv, R.id.col_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.matching_iv:
                break;
            case R.id.mine_iv:
                NearSetActivity.start(mContext);
                break;
            case R.id.get_iv:
                if (isReturn) {
                    nearby.unSwipeCard(300);
                } else {
                    Toast.makeText(mContext, "只能回退上一个点“X”的人", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.del_iv:
                nearby.swipeTopCardLeft(300);
                break;
            case R.id.love_iv:
                nearby.swipeTopCardRight(300);
                break;
            case R.id.col_iv:
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NearbyActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
