package com.etsdk.app.huov7.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.chat.DemoHelper;
import com.etsdk.app.huov7.model.InstallApkRecord;
import com.game.sdk.log.L;
import com.liang530.application.BaseApplication;
import com.liulishuo.filedownloader.FileDownloader;
import com.mob.MobSDK;

import java.util.HashMap;
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

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(getApplicationContext(), "23eef8ceee721", "0d1fab9808cfd3c136ea9326678f685d");
        MultiTypeInstaller.start();
        L.init(BuildConfig.LOG_DEBUG);
        com.liang530.log.L.init(BuildConfig.LOG_DEBUG);
        //设置同时最大下载数量
        FileDownloader.init(getApplicationContext());
        FileDownloader.getImpl().setMaxNetworkThreadCount(8);
        imei = getIMEI(this);
        L.i("333", "imei：" + imei);
//
        DemoHelper.getInstance().init(getApplicationContext());
//        EMOptions options = new EMOptions();
//// 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
//        options.setAutoTransferMessageAttachments(true);
//// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
//        options.setAutoDownloadThumbnail(true);
//        int pid = android.os.Process.myPid();
//        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

//        if (processAppName == null || !processAppName.equalsIgnoreCase(getApplicationContext().getPackageName())) {
//
//            // 则此application::onCreate 是被service 调用的，直接返回
//            return;
//        }
////初始化
//        EMClient.getInstance().init(getApplicationContext(), options);
////在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
//        EaseUI.getInstance().init(getApplicationContext(), options);
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
