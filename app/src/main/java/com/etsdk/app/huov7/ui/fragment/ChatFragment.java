package com.etsdk.app.huov7.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.ui.ChatActivity;
import com.etsdk.app.huov7.ui.ChatListActivity;
import com.etsdk.app.huov7.ui.LoginActivityV1;
import com.etsdk.app.huov7.ui.MainActivity2;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.app.huov7.util.TimeUtils;
import com.game.sdk.log.L;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\2\22 0022.
 */

public class ChatFragment extends AutoLazyFragment {

    @BindView(R.id.login_ll)
    LinearLayout login_ll;
    @BindView(R.id.group_ll)
    LinearLayout group_ll;
    @BindView(R.id.chat_ll)
    LinearLayout chat_ll;
    @BindView(R.id.head_img)
    RoundedImageView head_img;
    @BindView(R.id.chat_img)
    RoundedImageView chat_img;
    @BindView(R.id.name_tv)
    public TextView name_tv;
    @BindView(R.id.time_tv)
    public TextView time_tv;
    @BindView(R.id.count_tv)
    public TextView count_tv;
    @BindView(R.id.chat_name_tv)
    TextView chat_name_tv;
    @BindView(R.id.chat_time_tv)
    TextView chat_time_tv;
    @BindView(R.id.chat_count_tv)
    public TextView chat_count_tv;
    @BindView(R.id.has_img)
    public ImageView has_img;
    @BindView(R.id.group_tip)
    public TextView group_tip;
    @BindView(R.id.single_tip)
    public TextView single_tip;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        initDate();
    }

    public void initDate() {
        if (StringUtils.isEmpty(AileApplication.groupId)) {
            login_ll.setVisibility(View.VISIBLE);
            group_ll.setVisibility(View.GONE);
            chat_ll.setVisibility(View.GONE);
        } else {
            login_ll.setVisibility(View.GONE);
            group_ll.setVisibility(View.VISIBLE);
            chat_ll.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.group_ll, R.id.chat_ll, R.id.login_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_ll:
//                group_tip.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, AileApplication.groupId).putExtra(EaseConstant.EXTRA_CHAT_TYPE, 2).putExtra("title", "蝶恋工会"));
                break;
            case R.id.chat_ll:
                single_tip.setVisibility(View.GONE);
                has_img.setVisibility(View.GONE);
                chat_count_tv.setVisibility(View.GONE);
                ChatListActivity.start(getActivity());
                break;
            case R.id.login_ll:
                LoginActivityV1.start(getActivity());
                break;
        }
    }

    public void setChat() {
        if (!StringUtils.isEmpty(AileApplication.groupId)) {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(AileApplication.groupId);
            if (conversation != null) {
                EMMessage message = conversation.getLastMessage();
                String count = "";
                switch (message.getType()) {
                    case TXT:
                        count = ((EMTextMessageBody) message.getBody()).getMessage();
                        break;
                    case IMAGE:
                        count = "图片消息";
                        break;
                    case VIDEO:
                        count = "视频消息";
                        break;
                    case LOCATION:
                        count = "位置消息";
                        break;
                    case VOICE:
                        count = "语音消息";
                        break;
                    case FILE:
                        count = "文件消息";
                        break;
                    case CMD:
                        count = "透传消息消息";
                        break;
                }
                count_tv.setText(message.getFrom() + "：" + count);
                L.i("333", "message：" + message.getFrom() + "：" + count);
                time_tv.setText(TimeUtils.getTime(message.getMsgTime()));
                L.i("333", "time：" + message.getMsgTime());
//                if (!message.isAcked()) {
//                    group_tip.setVisibility(View.VISIBLE);
//                }
            }
        }
        for (int i = 0; i < ((MainActivity2) getActivity()).emmessages.size(); i++) {
            if (((MainActivity2) getActivity()).emmessages.get(i).getChatType() == EMMessage.ChatType.Chat) {
                if (!((MainActivity2) getActivity()).emmessages.get(i).isAcked()) {
                    single_tip.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        initDate();
        setChat();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }
}
