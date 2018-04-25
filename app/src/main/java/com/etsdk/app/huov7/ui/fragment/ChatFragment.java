package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.chat.modle.Conversation;
import com.etsdk.app.huov7.chat.modle.CustomMessage;
import com.etsdk.app.huov7.chat.modle.MessageFactory;
import com.etsdk.app.huov7.chat.modle.NomalConversation;
import com.etsdk.app.huov7.chat.ui.ChatActivity;
import com.etsdk.app.huov7.chat.utils.PushUtil;
import com.etsdk.app.huov7.chat.utils.TimeUtil;
import com.etsdk.app.huov7.ui.ChatListActivity;
import com.etsdk.app.huov7.ui.LoginActivityV1;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.log.L;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendFutureItem;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.data;
import static com.etsdk.app.huov7.R.id.refresh;


/**
 * Created by Administrator on 2018\2\22 0022.
 */

public class ChatFragment extends AutoLazyFragment implements ConversationView {

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
    private ConversationPresenter presenter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        initDate();
    }

    public void initDate() {
//        if (StringUtils.isEmpty(AileApplication.groupId)) {
//            login_ll.setVisibility(View.VISIBLE);
//            group_ll.setVisibility(View.GONE);
//            chat_ll.setVisibility(View.GONE);
//        } else {
        presenter = new ConversationPresenter(this);
        presenter.getConversation();
        login_ll.setVisibility(View.GONE);
        group_ll.setVisibility(View.VISIBLE);
        chat_ll.setVisibility(View.VISIBLE);
//        }
    }

    @OnClick({R.id.group_ll, R.id.chat_ll, R.id.login_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_ll:
                group_tip.setVisibility(View.GONE);
                if (AileApplication.isLogin) {
                    ChatActivity.navToChat(getActivity(), "@TGS#2ZN3CKFFU", TIMConversationType.Group);
                } else {
                    LoginActivityV1.start(getActivity());
                }
                break;
            case R.id.chat_ll:
                single_tip.setVisibility(View.GONE);
                has_img.setVisibility(View.GONE);
                chat_count_tv.setVisibility(View.GONE);
                if (AileApplication.isLogin) {
                    ChatListActivity.start(getActivity());
                } else {
                    LoginActivityV1.start(getActivity());
                }
                break;
            case R.id.login_ll:
                LoginActivityV1.start(getActivity());
                break;
        }
    }


    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(List<TIMConversation> conversationList) {

    }

    @Override
    public void updateMessage(final TIMMessage message) {
        L.i("333", "收到新消息");
        if (message == null) {
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.Group) {
            L.i("333", "group：" + message.getConversation().getPeer());
            if (message.getConversation().getPeer().equals("@TGS#2ZN3CKFFU")) {
                List<String> users = new ArrayList<>();
                users.add(message.getSender());
                TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        if (message.isRead()) {
                            if (group_tip != null)
                                group_tip.setVisibility(View.GONE);
                        }
                        NomalConversation conversation = new NomalConversation(message.getConversation());
                        conversation.setLastMessage(MessageFactory.getMessage(message));
                        String text = StringUtils.isEmpty(timUserProfiles.get(0).getNickName()) ? message.getSender() : timUserProfiles.get(0).getNickName();
                        if (text.equals("@TIM#SYSTEM")) {
                            text = "系统消息";
                        }
                        if (count_tv != null)
                            count_tv.setText(text + "：" + conversation.getLastMessageSummary());
                        if (time_tv != null)
                            time_tv.setText(TimeUtil.getChatTimeStr(conversation.getLastMessageTime()));
                    }
                });
            }
            if (!message.isRead()) {
                if (message.getConversation().getType() == TIMConversationType.C2C) {
                    L.i("333", "c2c：" + message.getSender());
                    if (single_tip != null)
                        single_tip.setVisibility(View.VISIBLE);
                }
                if (message.getConversation().getType() == TIMConversationType.Group) {
                    L.i("333", "group：" + message.getConversation().getPeer());
                    if (group_tip != null)
                        group_tip.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    @Override
    public void updateFriendshipMessage() {

    }

    @Override
    public void removeConversation(String identify) {

    }

    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {

    }

    @Override
    public void refresh() {

    }
}
