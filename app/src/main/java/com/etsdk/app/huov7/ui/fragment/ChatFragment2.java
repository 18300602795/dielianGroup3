package com.etsdk.app.huov7.ui.fragment;

import com.etsdk.app.huov7.chat.cache.UserCacheManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class ChatFragment2 extends EaseChatFragment {
    @Override
    protected void sendMessage(EMMessage message) {
        UserCacheManager.setMsgExt(message);
        super.sendMessage(message);
    }
}
