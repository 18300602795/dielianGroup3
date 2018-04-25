package com.etsdk.app.huov7.chat.modle;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.chat.adapter.ChatAdapter;
import com.game.sdk.log.L;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupTipsElem;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 群tips消息
 */
public class GroupTipMessage extends Message {


    public GroupTipMessage(TIMMessage message) {
        this.message = message;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (name != null)
                name.setText((String) msg.obj);
        }
    };
    TextView name;

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.leftPanel.setVisibility(View.GONE);
        viewHolder.rightPanel.setVisibility(View.GONE);
        viewHolder.systemMessage.setVisibility(View.VISIBLE);
//        viewHolder.systemMessage.setText(getSummary());
        name = viewHolder.systemMessage;
        getSummary();
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        final TIMGroupTipsElem e = (TIMGroupTipsElem) message.getElement(0);
        List<String> users = new ArrayList<>();
        Iterator<Map.Entry<String, TIMGroupMemberInfo>> iterator = e.getChangedGroupMemberInfo().entrySet().iterator();
        switch (e.getTipsType()) {
            case CancelAdmin:
            case SetAdmin:
                getInfo(users, 0);
                break;
            case Join:
                users.clear();
                while (iterator.hasNext()) {
                    Map.Entry<String, TIMGroupMemberInfo> item = iterator.next();
                    users.add(item.getValue().getUser());
                }
                getInfo(users, 1);
                break;
            case Kick:
                users.clear();
                users.add(e.getUserList().get(0));
                getInfo(users, 2);
                break;
            case ModifyMemberInfo:
                users.clear();
                getInfo(users, 3);
                while (iterator.hasNext()) {
                    Map.Entry<String, TIMGroupMemberInfo> item = iterator.next();
                    users.add(item.getValue().getUser());
                }
                break;
            case Quit:
                users.clear();
                users.add(e.getOpUser());
                getInfo(users, 4);
                break;
            case ModifyGroupInfo:
                getInfo(users, 5);
                break;
        }

        return "";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    private String getName(TIMGroupMemberInfo info) {
        if (info.getNameCard().equals("")) {
            return info.getUser();
        }
        return info.getNameCard();
    }

    private void getInfo(List<String> users, final int type) {
        final android.os.Message message = new android.os.Message();
        if (type == 0) {
            message.obj = AileApplication.getInstance().getString(R.string.summary_group_admin_change);
            handler.sendMessage(message);
            return;
        }
        if (type == 5) {
            message.obj = AileApplication.getInstance().getString(R.string.summary_group_info_change);
            handler.sendMessage(message);
            return;
        }
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                String names = "";
                for (TIMUserProfile userProfile : timUserProfiles) {
                    String name = userProfile.getNickName();
                    if (name.equals("")) {
                        name = userProfile.getIdentifier();
                    }
                    names += name + " ";
                }
                L.i("333", "name：" + names);
                if (type == 4) {
                    names += AileApplication.getInstance().getString(R.string.summary_group_mem_quit);
                }
                if (type == 3) {
                    names += AileApplication.getInstance().getString(R.string.summary_group_mem_modify);
                }
                if (type == 2) {
                    names += AileApplication.getInstance().getString(R.string.summary_group_mem_kick);
                }
                if (type == 1) {
                    names += AileApplication.getInstance().getString(R.string.summary_group_mem_add);
                }
                message.obj = names;
                handler.sendMessage(message);
            }
        });
    }

}
