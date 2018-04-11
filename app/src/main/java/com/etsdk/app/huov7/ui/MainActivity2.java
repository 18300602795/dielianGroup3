package com.etsdk.app.huov7.ui;

import android.content.Intent;
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
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.StartupResultBean;
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
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    public List<EMMessage> emmessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            L.i("333", "透明状态栏");
            setTranslucentStatus(true);
        }
        emmessages = new ArrayList<>();
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
        initDate();
//        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        getUserInfoData();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
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
            if (fragmentList.get(4) != null) {
                (fragmentList.get(2)).onResume();
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
//        sticky.setStickyCallBack(new StickyNavLayout.StickyCallBack() {
//            @Override
//            public void move(int dy) {
//                if (dy > (StringUtils.dip2px(mContext, 100))) {
//                    dy = (StringUtils.dip2px(mContext, 100));
//                }
//                float scale;
//                if (dy != 0) {
//                    scale = (float) ((StringUtils.dip2px(mContext, 100)) - dy / 4) / (float) (StringUtils.dip2px(mContext, 100));
//                } else {
//                    scale = 1;
//                }
//                WindowManager wm = (WindowManager) mContext
//                        .getSystemService(Context.WINDOW_SERVICE);
//                int width = wm.getDefaultDisplay().getWidth();
//                float icon_width = ((float) dy / StringUtils.dip2px(mContext, 100)) * (width / 2 - StringUtils.dip2px(mContext, 40));
//                ObjectAnimator.ofFloat(group_icon, "scaleX", scale).setDuration(0).start();
//                ObjectAnimator.ofFloat(group_icon, "scaleY", scale).setDuration(0).start();
//                ObjectAnimator.ofFloat(group_icon, "translationY", dy / 4 * 3).setDuration(0).start();
//                ObjectAnimator.ofFloat(group_icon, "translationX", -icon_width).setDuration(0).start();
//                float name_width = ((float) dy / StringUtils.dip2px(mContext, 100)) * (width / 2 - StringUtils.dip2px(mContext, 110));
//                ObjectAnimator.ofFloat(group_name, "translationY", dy / 8).setDuration(0).start();
//                ObjectAnimator.ofFloat(group_name, "translationX", -name_width).setDuration(0).start();
//                float introduce_width = ((float) dy / StringUtils.dip2px(mContext, 100)) * (width / 2 - StringUtils.dip2px(mContext, 160));
//                ObjectAnimator.ofFloat(group_introduce, "translationY", dy / 8).setDuration(0).start();
//                ObjectAnimator.ofFloat(group_introduce, "translationX", -introduce_width).setDuration(0).start();
//            }
//        });
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
                ((ChatFragment) fragmentList.get(2)).setChat();
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
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().logout(true);
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(mContext)) {
                            //连接不到聊天服务器
                        } else {
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            L.i("333", "收到消息：");
            emmessages.clear();
            emmessages = messages;
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            L.i("333", "收到透传消息");
            emmessages.clear();
            emmessages = messages;
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            L.i("333", "收到已读回执");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
            //收到已送达回执
            L.i("333", "收到已送达回执");
            emmessages.clear();
            emmessages = messages;
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
            L.i("333", "消息被撤回");
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            L.i("333", "消息状态变动");
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                int count = getUnreadMsgCountTotal();
                if (count > 0) {
                    chat_tip.setVisibility(View.VISIBLE);
                }
            }
        });
    }




    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMessageCount();
    }


    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    if (!StringUtils.isEmpty(data.getReg()) && data.getReg().equals("2")) {
                        login(data.getUsername(), "123456");
                    }
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

}
