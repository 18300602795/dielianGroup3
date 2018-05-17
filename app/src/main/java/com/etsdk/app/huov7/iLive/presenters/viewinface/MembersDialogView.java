package com.etsdk.app.huov7.iLive.presenters.viewinface;

import com.etsdk.app.huov7.iLive.model.MemberInfo;
import com.tencent.qcloud.presentation.viewfeatures.MvpView;

import java.util.ArrayList;


/**
 * 成员列表回调
 */
public interface MembersDialogView extends MvpView {

    void showMembersList(ArrayList<MemberInfo> data);

}
