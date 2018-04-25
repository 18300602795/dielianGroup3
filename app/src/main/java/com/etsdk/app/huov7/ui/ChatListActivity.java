package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.chat.ui.ConversationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class ChatListActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    ConversationFragment conversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);
        conversationFragment = new ConversationFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.conversion_frame, conversationFragment).commit();
        tvTitleName.setText("聊天列表");
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
        Intent starter = new Intent(context, ChatListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
