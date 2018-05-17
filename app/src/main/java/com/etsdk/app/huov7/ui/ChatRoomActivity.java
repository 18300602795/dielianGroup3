package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.RoomAdapter;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.model.RoomModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class ChatRoomActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.room_recycle)
    XRecyclerView room_recycle;
    private RoomAdapter adapter;
    List<RoomModel> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        tvTitleName.setText("聊天室");
        models = new ArrayList<>();
        addList();
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        room_recycle.setLayoutManager(manager);
        adapter = new RoomAdapter(models);
        room_recycle.setAdapter(adapter);
    }

    private void addList() {
        for (int i = 0; i < 10; i++) {
            RoomModel room = new RoomModel();
            room.setFaceUrl(AileApplication.faceUrl);
            room.setCreateName("aaa");
            room.setNum(7);
            room.setCreateId("yan104");
            room.setRoomTitle("互动游戏交友");
            room.setRoomId(52639);
            models.add(room);
        }
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
        Intent starter = new Intent(context, ChatRoomActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
