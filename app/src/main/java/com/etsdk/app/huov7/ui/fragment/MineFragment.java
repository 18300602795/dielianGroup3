package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.MineAdapter;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;
import com.kymjs.rxvolley.RxVolley;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\3\16 0016.
 */

public class MineFragment extends AutoLazyFragment {
    @BindView(R.id.fragment_recycle)
    RecyclerView fragment_recycle;
    MineAdapter adapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_recycle2);
        setupUI();
    }

    private void setupUI() {
        adapter = new MineAdapter(getActivity());
        fragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragment_recycle.setAdapter(adapter);
        getUserInfoData();
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (adapter != null) {
                    adapter.setData(data);
                    if (!StringUtils.isEmpty(data.getReg()) && data.getReg().equals("1"))
                        login(data.getUsername(), "123456");
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                AileApplication.groupId = "";
                if (adapter != null)
                    adapter.setData(null);
                if (CODE_SESSION_ERROR.equals(code)) {
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi2), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void login(String userName, String password) {
        EMClient.getInstance().login(userName, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("333", "登录聊天服务器成功！");
                try {
                    EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(10, null);//需异步处理
                    List<EMGroupInfo> groupsList = result.getData();
                    String cursor = result.getCursor();
                    Log.i("333", "id：" + groupsList.get(0).getGroupId());
                    AileApplication.groupId = groupsList.get(0).getGroupId();
                    EMClient.getInstance().groupManager().joinGroup(groupsList.get(0).getGroupId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("333", "登录聊天服务器失败！" + "code " + code + " message " + message);
            }
        });
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        updateData();
    }

    /**
     * 更新数据
     */
    public void updateData() {
        getUserInfoData();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
    }
}
