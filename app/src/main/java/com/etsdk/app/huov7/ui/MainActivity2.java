package com.etsdk.app.huov7.ui;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GroupHomeAdapter;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.chat.modle.FriendshipInfo;
import com.etsdk.app.huov7.chat.modle.GroupInfo;
import com.etsdk.app.huov7.chat.modle.MessageFactory;
import com.etsdk.app.huov7.chat.modle.NomalConversation;
import com.etsdk.app.huov7.chat.modle.UserInfo;
import com.etsdk.app.huov7.chat.utils.PushUtil;
import com.etsdk.app.huov7.chat.utils.TimeUtil;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.iLive.model.MySelfInfo;
import com.etsdk.app.huov7.model.StartupResultBean;
import com.etsdk.app.huov7.model.StatusObservable;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.fragment.ChatFragment;
import com.etsdk.app.huov7.ui.fragment.HomeFragment2;
import com.etsdk.app.huov7.ui.fragment.HouseFragment;
import com.etsdk.app.huov7.ui.fragment.MineFragment;
import com.etsdk.app.huov7.ui.fragment.NewsListFragment;
import com.etsdk.app.huov7.update.UpdateVersionDialog;
import com.etsdk.app.huov7.update.UpdateVersionService;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMUser;
import com.tencent.TIMUserProfile;
import com.tencent.TIMUserStatusListener;
import com.tencent.TIMValueCallBack;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.sdk.Constant;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
import com.tencent.qcloud.ui.NotifyDialog;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.data;
import static com.etsdk.app.huov7.R.id.count_tv;
import static com.etsdk.app.huov7.R.id.single_tip;
import static com.etsdk.app.huov7.R.id.time_tv;

/**
 * Created by Administrator on 2018\3\14 0014.
 */

public class MainActivity2 extends ImmerseActivity {
    //    @BindView(R.id.sticky)
//    StickyNavLayout sticky;
    @BindView(R.id.pager_view)
    ViewPager mViewPager;
    @BindView(R.id.group_ll)
    LinearLayout group_ll;
    @BindView(R.id.group_tv)
    TextView group_tv;
    @BindView(R.id.group_iv)
    ImageView group_iv;
    @BindView(R.id.event_ll)
    LinearLayout event_ll;
    @BindView(R.id.event_tv)
    TextView event_tv;
    @BindView(R.id.event_iv)
    ImageView event_iv;
    @BindView(R.id.chat_ll)
    LinearLayout chat_ll;
    @BindView(R.id.chat_tv)
    TextView chat_tv;
    @BindView(R.id.chat_iv)
    ImageView chat_iv;
    @BindView(R.id.house_ll)
    LinearLayout house_ll;
    @BindView(R.id.house_tv)
    TextView house_tv;
    @BindView(R.id.house_iv)
    ImageView house_iv;
    @BindView(R.id.mine_ll)
    LinearLayout mine_ll;
    @BindView(R.id.mine_tv)
    TextView mine_tv;
    @BindView(R.id.mine_iv)
    ImageView mine_iv;
    @BindView(R.id.group_tip)
    TextView group_tip;
    @BindView(R.id.house_tip)
    TextView house_tip;
    @BindView(R.id.chat_tip)
    TextView chat_tip;
    @BindView(R.id.event_tip)
    TextView event_tip;
    @BindView(R.id.mine_tip)
    TextView mine_tip;
    List<Fragment> fragmentList = new ArrayList<>();
    private GroupHomeAdapter mAdapter;
    private List<TextView> textViews;
    private ConversationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            L.i("333", "透明状态栏");
            setTranslucentStatus(true);
        }
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        initDate();
        getUserInfoData();
        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d("333", "receive force offline message");
//                Intent intent = new Intent(MainActivity2.this, DialogActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                new NotifyDialog().show(getString(R.string.tls_expire), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
            }
        });
        presenter = new ConversationPresenter(new ConversationView() {
            @Override
            public void initView(List<TIMConversation> conversationList) {

            }

            @Override
            public void updateMessage(TIMMessage message) {
                if (message == null) {
                    return;
                }
//                if (message.getConversation().getType() == TIMConversationType.System){
//                    return;
//                }
                if (!message.isRead()) {
                    chat_tip.setVisibility(View.VISIBLE);
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
        });
        presenter.getConversation();
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bg_blue), 255);
    }


    /**
     * 设置状态栏透明
     *
     * @param on
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
            new UpdateVersionDialog().showDialog(mContext, showCancel, updateInfo.getContent(), new UpdateVersionDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    Intent intent = new Intent(mContext, UpdateVersionService.class);
                    intent.putExtra("url", updateInfo.getUrl());
                    mContext.startService(intent);
                    T.s(mContext, "开始下载,请在下载完成后确认安装！");
                    if (!showCancel) {//是强更则关闭界面
                        finish();
                    }
                }

                @Override
                public void cancel() {
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragmentList != null && fragmentList.size() >= 5) {
            if (fragmentList.get(4) != null) {
                ((MineFragment) fragmentList.get(4)).updateData();
            }
        }
    }


    private void initDate() {
        handleUpdate();
        textViews = new ArrayList<>();
        textViews.add(group_tv);
        textViews.add(house_tv);
        textViews.add(chat_tv);
        textViews.add(event_tv);
        textViews.add(mine_tv);
        fragmentList.add(new HomeFragment2());
        fragmentList.add(new HouseFragment());
        fragmentList.add(new ChatFragment());
        fragmentList.add(NewsListFragment.newInstance("2", null));
        fragmentList.add(new MineFragment());
        mAdapter = new GroupHomeAdapter(getSupportFragmentManager(), fragmentList);
        clear();
        show(0);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clear();
                show(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.group_ll, R.id.event_ll, R.id.chat_ll, R.id.house_ll, R.id.mine_ll})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.search_iv:
//                SearchActivity.start(mContext);
//                break;
//            case iv_title_down:
//                DownloadManagerActivity.start(mContext);
//                break;
            case R.id.group_ll:
                mViewPager.setCurrentItem(0);
                clear();
                show(0);
                break;
            case R.id.event_ll:
                mViewPager.setCurrentItem(3);
                clear();
                show(3);
                break;
            case R.id.chat_ll:
                mViewPager.setCurrentItem(2);
                clear();
                show(2);
                break;
            case R.id.house_ll:
                mViewPager.setCurrentItem(1);
                clear();
                show(1);
                break;
            case R.id.mine_ll:
                mViewPager.setCurrentItem(4);
                clear();
                show(4);
                break;
            default:
                break;
        }
    }

    private void clear() {
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(getResources().getColor(R.color.black));
        }
        group_iv.setImageResource(R.mipmap.tab_icon_tj_us);
        house_iv.setImageResource(R.mipmap.tab_icon_game_us);
        chat_iv.setImageResource(R.mipmap.zixun_us);
        event_iv.setImageResource(R.mipmap.tab_icon_fuli_us);
        mine_iv.setImageResource(R.mipmap.tab_icon_my_us);
    }

    private void show(int position) {
        chat_tip.setVisibility(View.GONE);
        textViews.get(position).setTextColor(getResources().getColor(R.color.text_green));
        switch (position) {
            case 0:
                group_iv.setImageResource(R.mipmap.tab_icon_tj_s);
                break;
            case 1:
                house_iv.setImageResource(R.mipmap.tab_icon_game_s);
                break;
            case 2:
                chat_iv.setImageResource(R.mipmap.zixun_s);
                ((ChatFragment) fragmentList.get(2)).initDate();
                break;
            case 3:
                event_iv.setImageResource(R.mipmap.tab_icon_fuli_s);
                break;
            case 4:
                mine_iv.setImageResource(R.mipmap.tab_icon_my_s);
                break;
        }
    }

    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    //重写onBackPressed()方法,继承自退出的方法
    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    loginIM(data);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                AileApplication.groupId = "";
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi2), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void loginIM(final UserInfoResultBean data) {
//        String id = TLSService.getInstance().getLastUserIdentifier();
//        //发起登录请求
//        TIMUser user = new TIMUser();
//        user.setAccountType(String.valueOf(Constant.ACCOUNT_TYPE));
//        user.setAppIdAt3rd(String.valueOf(Constant.SDK_APPID));
//        user.setIdentifier(id);
//
//        TIMManager.getInstance().login(
//                Constant.SDK_APPID,
//                user,
//                TLSService.getInstance().getUserSig(id),                    //用户帐号签名，由私钥加密获得，具体请参考文档
//                new TIMCallBack() {
//                    @Override
//                    public void onError(int i, String s) {
//                        AileApplication.isLogin = false;
//                        L.i("333", "登录失败：" + s);
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        //初始化程序后台后消息推送
//                        PushUtil.getInstance();
//                        //初始化消息监听
//                        MessageEvent.getInstance();
//                        AileApplication.faceUrl = data.getPortrait();
//                        AileApplication.isLogin = true;
//                        L.i("333", "登录成功：");
//                        applyGroup();
//                    }
//                });
        ILiveLoginManager.getInstance().tlsLoginAll("fx" + data.getUsername(), "00112233", new ILiveCallBack() {
            @Override
            public void onSuccess(Object obj) {
                PushUtil.getInstance();
                //初始化消息监听
                MessageEvent.getInstance();
                AileApplication.faceUrl = data.getPortrait();
                AileApplication.isLogin = true;
                L.i("333", "登录成功：");
                MySelfInfo.getInstance().setId(data.getUsername());
                MySelfInfo.getInstance().setNickName(data.getNickname());
                MySelfInfo.getInstance().setAvatar(data.getPortrait());
                MySelfInfo.getInstance().setMyRoomNum(52639);
                afterLogin();
                applyGroup();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                AileApplication.isLogin = false;
                L.i("333", "Login failed:" + module + "|" + errCode + "|" + errMsg);
            }
        });
    }


    // 登录成功
    private void afterLogin() {
        ILiveLoginManager.getInstance().setUserStatusListener(StatusObservable.getInstance());
        com.etsdk.app.huov7.model.UserInfo.getInstance().writeToCache(getApplicationContext());
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

    public void logout() {
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
    }

}
