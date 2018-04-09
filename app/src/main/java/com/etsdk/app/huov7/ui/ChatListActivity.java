package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.game.sdk.log.L;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class ChatListActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    EaseConversationListFragment conversationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);
        conversationListFragment = new EaseConversationListFragment();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                int type = 1;
                String title = conversation.conversationId();
                switch (conversation.getType()) {
                    case Chat:
                        type = 1;
                        break;
                    case GroupChat:
                        type = 2;
                        EMGroup group = EMClient.getInstance().groupManager().getGroup(title);
                        title = group.getGroupName();
                        break;
                    case ChatRoom:
                        type = 3;
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(title);
                        title = room.getName();
                        break;
                }
                startActivity(new Intent(ChatListActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()).putExtra(EaseConstant.EXTRA_CHAT_TYPE, type).putExtra("title", title));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.conversion_frame, conversationListFragment).commit();
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

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            L.i("333", "收到消息");
            conversationListFragment.refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            L.i("333", "收到透传消息");
            conversationListFragment.refresh();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            L.i("333", "收到已读回执");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            L.i("333", "收到已送达回执");
            conversationListFragment.refresh();
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
            L.i("333", "消息被撤回");
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            L.i("333", "消息状态变动");
        }
    };


    public static void start(Context context) {
        Intent starter = new Intent(context, ChatListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
