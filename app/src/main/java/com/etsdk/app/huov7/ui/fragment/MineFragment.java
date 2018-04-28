package com.etsdk.app.huov7.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.MineAdapter;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.chat.utils.PushUtil;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.StartupResultBean;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.AccountManageActivity;
import com.etsdk.app.huov7.ui.ArticleActivity;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.LoginActivity;
import com.etsdk.app.huov7.ui.MineGiftCouponListActivityNew;
import com.etsdk.app.huov7.update.UpdateVersionDialog;
import com.etsdk.app.huov7.update.UpdateVersionService;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.utils.GlideDisplay;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.sdk.Constant;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

import static android.R.attr.data;
import static android.R.attr.theme;
import static com.etsdk.app.huov7.base.AileApplication.groupId;
import static com.tencent.qalsdk.service.QalService.context;


/**
 * Created by Administrator on 2018\3\16 0016.
 */

public class MineFragment extends AutoLazyFragment {
    @BindView(R.id.info_ll)
    LinearLayout info_ll;
    @BindView(R.id.off_line)
    TextView off_line;
    @BindView(R.id.iv_mineHead)
    RoundedImageView iv_mineHead;
    @BindView(R.id.nick_tv)
    TextView nick_tv;
    @BindView(R.id.sign_tv)
    TextView sign_tv;
    @BindView(R.id.version_tv)
    TextView version_tv;
    private UserInfoResultBean resultBean;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.item_mine);
        setupUI();
    }

    private void setupUI() {
        getUserInfoData();
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                resultBean = data;
                if (data != null) {
                    info_ll.setVisibility(View.VISIBLE);
                    off_line.setVisibility(View.GONE);
                    nick_tv.setText(data.getNickname());
                    GlideDisplay.display(iv_mineHead, data.getPortrait(), R.drawable.bg_game);
                    if (!AileApplication.isLogin) {
                        login(data, false);
                    }
                } else {
                    info_ll.setVisibility(View.GONE);
                    off_line.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi2), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @OnClick({R.id.account_ll, R.id.video_ll, R.id.gift_ll, R.id.game_ll, R.id.post_ll, R.id.update_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_ll:
                //个人中心
                if (resultBean != null) {
                    AccountManageActivity.start(getContext());
                } else {
                    LoginActivity.start(getContext());
                }
                break;
            case R.id.video_ll:

                break;
            case R.id.gift_ll:
                MineGiftCouponListActivityNew.start(getContext(), MineGiftCouponListActivityNew.TYPE_GIFT, "礼包");
                break;
            case R.id.game_ll:
                DownloadManagerActivity.start(getContext());
                break;
            case R.id.post_ll:
                ArticleActivity.start(getContext());
                break;
            case R.id.update_ll:
                handleUpdate();
                break;

        }
    }

    private void register(final UserInfoResultBean data) {
        final TLSService tlsService = TLSService.getInstance();
        int result = tlsService.TLSStrAccReg("fx" + data.getUsername(), "00112233", new TLSStrAccRegListener() {
            @Override
            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
                login(data, true);
                L.i("333", "注册成功");
            }

            @Override
            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
                L.i("333", "注册失败1");
            }

            @Override
            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
                L.i("333", "注册失败2");
            }
        });
        L.i("333", "帐号不合法： " + result);
        if (result == TLSErrInfo.INPUT_INVALID) {
            L.i("333", "帐号不合法");
        }
    }

    private void login(final UserInfoResultBean data, final boolean isRegister) {
        final TLSService tlsService = TLSService.getInstance();
        tlsService.TLSPwdLogin("fx" + data.getUsername(), "00112233", new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
                loginIM(tlsUserInfo, isRegister, data);
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
                L.i("333", "登录失败1");
            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
                L.i("333", "登录失败2：" + tlsErrInfo.ErrCode + "msg：" + tlsErrInfo.Msg);
            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                L.i("333", "登录失败3：" + tlsErrInfo.ErrCode + "msg：" + tlsErrInfo.Msg);
                if (tlsErrInfo.ErrCode == 229) {
                    register(data);
                }
            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                L.i("333", "登录失败4：" + tlsErrInfo.ErrCode + "msg：" + tlsErrInfo.Msg);
            }
        });
    }

    private void setPhoto(final String faceUrl) {
        TIMFriendshipManager.getInstance().setFaceUrl(faceUrl, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                L.e("333", "setFaceUrl failed: " + code + " desc" + desc);
            }

            @Override
            public void onSuccess() {
                AileApplication.faceUrl = faceUrl;
                L.e("333", "setFaceUrl succ");
            }
        });
    }

    /**
     * 处理版本更新信息
     */
    private void handleUpdate() {
        final boolean showCancel;
        final StartupResultBean.UpdateInfo updateInfo = EventBus.getDefault().getStickyEvent(StartupResultBean.UpdateInfo.class);
        if (updateInfo != null) {//有更新
            if ("1".equals(updateInfo.getUp_status())) {//强制更新
                showCancel = false;
            } else if ("2".equals(updateInfo.getUp_status())) {//选择更新
                showCancel = true;
            } else {
                return;
            }
            if (TextUtils.isEmpty(updateInfo.getUrl()) ||
                    (!updateInfo.getUrl().startsWith("http") && !updateInfo.getUrl().startsWith("https"))) {
                return;//url不可用
            }
            new UpdateVersionDialog().showDialog(getContext(), showCancel, updateInfo.getContent(), new UpdateVersionDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    Intent intent = new Intent(getContext(), UpdateVersionService.class);
                    intent.putExtra("url", updateInfo.getUrl());
                    getContext().startService(intent);
                    T.s(getContext(), "开始下载,请在下载完成后确认安装！");
                    if (!showCancel) {//是强更则关闭界面
                        ((Activity) getContext()).finish();
                    }
                }

                @Override
                public void cancel() {
                }
            });
        } else {
            T.s(getContext(), "当前已为最新版本");
        }
    }

    private void setNick(String nick) {
        TIMFriendshipManager.getInstance().setNickName(nick, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                L.e("333", "setNickName failed: " + code + " desc");
            }

            @Override
            public void onSuccess() {
                L.e("333", "setNickName succ");
            }
        });
    }

    private void loginIM(final TLSUserInfo tlsUserInfo, final boolean isRegister, final UserInfoResultBean data) {
        TLSHelper helper = TLSHelper.getInstance();
        String usersig = helper.getUserSig(tlsUserInfo.identifier);
        L.i("333", "登录成功：usersig：" + usersig);
        TLSService.getInstance().setLastErrno(0);
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constant.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constant.SDK_APPID));
        user.setIdentifier(tlsUserInfo.identifier);
        //发起登录请求
        TIMManager.getInstance().login(
                Constant.SDK_APPID,
                user,
                TLSService.getInstance().getUserSig(tlsUserInfo.identifier),                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        AileApplication.isLogin = false;
                    }

                    @Override
                    public void onSuccess() {
                        //初始化程序后台后消息推送
                        PushUtil.getInstance();
                        //初始化消息监听
                        MessageEvent.getInstance();
                        AileApplication.isLogin = true;
                        if (isRegister) {
                            setNick(data.getNickname());
                            setPhoto(data.getPortrait());
                        } else {
                            AileApplication.faceUrl = data.getPortrait();
                        }
                        applyGroup();
                        L.i("333", "登录成功：" + tlsUserInfo.identifier);

                    }
                });
    }

    private void applyGroup() {
        TIMGroupManager.getInstance().applyJoinGroup("@TGS#2ZN3CKFFU", "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                L.i("333", "加群失败：" + i + "msg：" + s);
            }

            @Override
            public void onSuccess() {
                L.i("333", "加群成功：");
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
