package com.etsdk.app.huov7.chat.modle;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.chat.adapter.ChatAdapter;
import com.etsdk.app.huov7.chat.utils.TimeUtil;
import com.etsdk.app.huov7.util.ImgUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.SdkConstant;
import com.game.sdk.log.L;
import com.liang530.utils.GlideDisplay;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageStatus;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息数据基类
 */
public abstract class Message {

    protected final String TAG = "Message";

    TIMMessage message;

    private boolean hasTime;

    /**
     * 消息描述信息
     */
    private String desc;


    public TIMMessage getMessage() {
        return message;
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    public abstract void showMessage(ChatAdapter.ViewHolder viewHolder, Context context);

    /**
     * 获取显示气泡
     *
     * @param viewHolder 界面样式
     */
    public RelativeLayout getBubbleView(final ChatAdapter.ViewHolder viewHolder) {
        viewHolder.systemMessage.setVisibility(hasTime ? View.VISIBLE : View.GONE);
        viewHolder.systemMessage.setText(TimeUtil.getChatTimeStr(message.timestamp()));
        showDesc(viewHolder);
        String faceUrl = "";
        for (int i = 0; i < message.getElementCount(); ++i) {
            if (message.getElement(i).getType() == TIMElemType.Custom) {
                TIMCustomElem elem = (TIMCustomElem) message.getElement(i);
                if (elem.getDesc().equals("头像")) {
                    faceUrl = new String(elem.getData());
                }
            }
        }
        if (message.isSelf()) {
            viewHolder.leftPanel.setVisibility(View.GONE);
            viewHolder.rightPanel.setVisibility(View.VISIBLE);
            ImgUtil.setImg(viewHolder.context, AileApplication.faceUrl, R.drawable.bg_game, viewHolder.rightAvatar);
            return viewHolder.rightMessage;
        } else {
            viewHolder.leftPanel.setVisibility(View.VISIBLE);
            viewHolder.rightPanel.setVisibility(View.GONE);
            //群聊显示名称，群名片>个人昵称>identify
            if (message.getConversation().getType() == TIMConversationType.Group) {
                viewHolder.sender.setVisibility(View.VISIBLE);
                String name = "";
                viewHolder.leftAvatar.setImageResource(R.drawable.bg_game);
                if (message.getSenderGroupMemberProfile() != null) {
                    name = message.getSenderGroupMemberProfile().getNameCard();
                }
                if (name.equals("") && message.getSenderProfile() != null) {
                    name = message.getSenderProfile().getNickName();
                    ImgUtil.setImg(viewHolder.context, faceUrl, R.drawable.bg_game, viewHolder.leftAvatar);
                }
                if (name.equals("")) {
                    name = message.getSender();
                }
                viewHolder.sender.setText(name);
            } else {
                ImgUtil.setImg(viewHolder.context, faceUrl, R.drawable.bg_game, viewHolder.leftAvatar);
                viewHolder.sender.setVisibility(View.GONE);
            }
            return viewHolder.leftMessage;
        }
    }

    /**
     * 显示消息状态
     *
     * @param viewHolder 界面样式
     */
    public void showStatus(ChatAdapter.ViewHolder viewHolder) {
        switch (message.status()) {
            case Sending:
                viewHolder.error.setVisibility(View.GONE);
                viewHolder.sending.setVisibility(View.VISIBLE);
                break;
            case SendSucc:
                viewHolder.error.setVisibility(View.GONE);
                viewHolder.sending.setVisibility(View.GONE);
                break;
            case SendFail:
                viewHolder.error.setVisibility(View.VISIBLE);
                viewHolder.sending.setVisibility(View.GONE);
                viewHolder.leftPanel.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 判断是否是自己发的
     */
    public boolean isSelf() {
        return message.isSelf();
    }

    /**
     * 获取消息摘要
     */
    public abstract String getSummary();

    /**
     * 保存消息或消息文件
     */
    public abstract void save();


    /**
     * 删除消息
     */
    public void remove() {
        if (message != null) {
            message.remove();
        }
    }


    /**
     * 是否需要显示时间获取
     */
    public boolean getHasTime() {
        return hasTime;
    }


    /**
     * 是否需要显示时间设置
     *
     * @param message 上一条消息
     */
    public void setHasTime(TIMMessage message) {
        if (message == null) {
            hasTime = true;
            return;
        }
        hasTime = this.message.timestamp() - message.timestamp() > 300;
    }


    /**
     * 消息是否发送失败
     */
    public boolean isSendFail() {
        return message.status() == TIMMessageStatus.SendFail;
    }

    /**
     * 清除气泡原有数据
     */
    protected void clearView(ChatAdapter.ViewHolder viewHolder) {
        getBubbleView(viewHolder).removeAllViews();
        getBubbleView(viewHolder).setOnClickListener(null);
    }

    /**
     * 获取发送者
     */
    public String getSender() {
        if (message.getSender() == null) return "";
        return message.getSender();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    private void showDesc(ChatAdapter.ViewHolder viewHolder) {

        if (desc == null || desc.equals("")) {
            viewHolder.rightDesc.setVisibility(View.GONE);
        } else {
            viewHolder.rightDesc.setVisibility(View.VISIBLE);
            viewHolder.rightDesc.setText(desc);
        }
    }
}
