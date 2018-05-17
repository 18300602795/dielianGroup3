package com.etsdk.app.huov7.iLive.presenters.viewinface;

import com.etsdk.app.huov7.iLive.model.RoomInfoJson;
import com.etsdk.app.huov7.iLive.presenters.UserServerHelper;
import com.tencent.qcloud.presentation.viewfeatures.MvpView;

import java.util.ArrayList;


/**
 *  列表页面回调
 */
public interface LiveListView extends MvpView {


    void showRoomList(UserServerHelper.RequestBackInfo result, ArrayList<RoomInfoJson> roomlist);
}
