package com.etsdk.app.huov7.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.view.View;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.iLive.presenters.MessageEvent;
import com.etsdk.app.huov7.model.InstallApkRecord;
import com.etsdk.app.huov7.model.MessageObservable;
import com.game.sdk.log.L;
import com.liang530.application.BaseApplication;
import com.liulishuo.filedownloader.FileDownloader;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by liu hong liang on 2016/12/1.
 */

public class AileApplication extends BaseApplication {
    private Map<String, InstallApkRecord> installingApkList = new HashMap<>();
    public static String agent;
    boolean f = false;
    public static String imei;
    public static String groupId;
    public static boolean isLogin = false;
    public static String faceUrl = "";

    @Override
    public void onCreate() {
        super.onCreate();
//        MobSDK.init(getApplicationContext(), "23eef8ceee721", "0d1fab9808cfd3c136ea9326678f685d");
        MultiTypeInstaller.start();
        L.init(BuildConfig.LOG_DEBUG);
        com.liang530.log.L.init(BuildConfig.LOG_DEBUG);
        //设置同时最大下载数量
        FileDownloader.init(getApplicationContext());
        FileDownloader.getImpl().setMaxNetworkThreadCount(8);
        imei = getIMEI(this);
        L.i("333", "imei：" + imei);

        if (MsfSdkUtils.isMainProcess(this)) {
            L.i("333", "消息监听");
            ILiveSDK.getInstance().setCaptureMode(ILiveConstants.CAPTURE_MODE_SURFACEVIEW);
            ILiveLog.setLogLevel(ILiveLog.TILVBLogLevel.DEBUG);
            ILiveSDK.getInstance().initSdk(this, 1400028096, 11851);
            // 初始化直播模块
            ILVLiveConfig liveConfig = new ILVLiveConfig();
            liveConfig.setLiveMsgListener(MessageEvent.getInstance());
            ILVLiveManager.getInstance().init(liveConfig);
//            ILiveSDK.getInstance().initSdk(this, 1400083397, 25111);
//            ILVLiveManager.getInstance().init(new ILVLiveConfig()
//                    .setLiveMsgListener(MessageObservable.getInstance()));

            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                    }
                }
            });
        }
    }


    @Override
    public Class getLoginClass() {
        return null;
    }

    public Map<String, InstallApkRecord> getInstallingApkList() {
        return installingApkList;
    }

    /**
     * 获取手机IMEI号
     * <p>
     * 需要动态权限: android.permission.READ_PHONE_STATE
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        return imei;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
