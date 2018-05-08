package com.etsdk.app.huov7.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.service.HuoSdkService;
import com.game.sdk.log.L;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.liang530.application.BaseApplication;
import com.liang530.log.SP;
import com.liang530.utils.BaseAppUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupManager;
import com.tencent.TIMLogLevel;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
import com.xiaomi.mipush.sdk.MiPushClient;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;

public class StartActivity extends ImmerseActivity {

    @BindView(R.id.iv_start_img)
    ImageView ivStartImg;
    @BindView(R.id.activity_start)
    RelativeLayout activityStart;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        init();
        clearNotification();
        setupUI();
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }

    private void init() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(), loglvl);
        //初始化TLS
        TlsBusiness.init(getApplicationContext());
        //设置刷新监听
        RefreshEvent.getInstance();
//        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
    }



    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#5DC9F7"));
    }

    private void setupUI() {
        startTime = System.currentTimeMillis();
        Intent intent = new Intent(this, HuoSdkService.class);
        startService(intent);
        if (isFirstRun(mContext)) {
            GuideActivity.start(mContext);
            finish();
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartActivity.this.finish();
                if (isFirstRun(mContext)) {
                    GuideActivity.start(mContext);
                } else {
                    MainActivity.start(mContext, 0);
                }
                finish();
            }
        }, 3000);
    }

    public static boolean isFirstRun(Context context) {
        boolean isFirstRun = false;
        if (SP.getSp() == null) {
            SP.init(BaseApplication.getInstance());
        }

        int versionCode = SP.getInt("versionCode", 0);
        int newAppVersion = BaseAppUtil.getAppVersionCode();
        if (versionCode != newAppVersion) {
            SP.putInt("versionCode", newAppVersion).commit();
            isFirstRun = true;
        }
        return isFirstRun;
    }

    /**
     * 清楚所有通知栏通知
     */
    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        MiPushClient.clearNotification(getApplicationContext());
    }
}
