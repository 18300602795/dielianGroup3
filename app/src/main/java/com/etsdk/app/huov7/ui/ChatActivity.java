package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.fragment.ChatFragment2;
import com.hyphenate.easeui.EaseConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class ChatActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.iv_title_down)
    ImageView iv_title_down;
    int type;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);
        iv_title_down.setVisibility(View.VISIBLE);
        //new出EaseChatFragment或其子类的实例
        ChatFragment2 chatFragment = new ChatFragment2();
        chatFragment.hideTitleBar();
        String title = getIntent().getStringExtra("title");
        type = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, 1);
        username = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        //传入参数
        Bundle args = new Bundle();
        if (type == 1) {
            iv_title_down.setImageResource(R.drawable.chat_detail);
        } else {
            iv_title_down.setImageResource(R.drawable.group_chat_detail);
        }
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, type);
        args.putString(EaseConstant.EXTRA_USER_ID, username);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.conversion_frame, chatFragment).commit();
        tvTitleName.setText(title);
    }

    @OnClick({R.id.iv_titleLeft, R.id.iv_title_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_title_down:
                if (type == 1) {
//                    OtherInfoActivity.start(mContext, username);
                } else {
                    MemberListActivity.start(mContext);
                }
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ChatActivity.class);
        context.startActivity(starter);
    }

}
