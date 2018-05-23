package com.etsdk.app.huov7.iLive.views;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.iLive.adapter.ChatMsgListAdapter;
import com.etsdk.app.huov7.iLive.adapter.LinkAdapter;
import com.etsdk.app.huov7.iLive.model.ChatEntity;
import com.etsdk.app.huov7.iLive.model.CurLiveInfo;
import com.etsdk.app.huov7.iLive.model.LiveInfoJson;
import com.etsdk.app.huov7.iLive.model.MemberID;
import com.etsdk.app.huov7.iLive.model.MemberInfo;
import com.etsdk.app.huov7.iLive.model.MySelfInfo;
import com.etsdk.app.huov7.iLive.model.RoomInfoJson;
import com.etsdk.app.huov7.iLive.presenters.GetLinkSignHelper;
import com.etsdk.app.huov7.iLive.presenters.LiveHelper;
import com.etsdk.app.huov7.iLive.presenters.LiveListViewHelper;
import com.etsdk.app.huov7.iLive.presenters.UserServerHelper;
import com.etsdk.app.huov7.iLive.presenters.viewinface.GetLinkSigView;
import com.etsdk.app.huov7.iLive.presenters.viewinface.LiveListView;
import com.etsdk.app.huov7.iLive.presenters.viewinface.LiveView;
import com.etsdk.app.huov7.iLive.presenters.viewinface.ProfileView;
import com.etsdk.app.huov7.iLive.utils.Constants;
import com.etsdk.app.huov7.iLive.utils.GlideCircleTransform;
import com.etsdk.app.huov7.iLive.utils.LogConstants;
import com.etsdk.app.huov7.iLive.utils.UIUtils;
import com.etsdk.app.huov7.iLive.views.customviews.HeartLayout;
import com.etsdk.app.huov7.iLive.views.customviews.InputTextMsgDialog;
import com.etsdk.app.huov7.iLive.views.customviews.RadioGroupDialog;
import com.etsdk.app.huov7.ui.dialog.IliveMemberInfoDialog;
import com.etsdk.app.huov7.ui.dialog.VoiceDialog;
import com.etsdk.app.huov7.util.ImgUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.app.huov7.view.GiftView;
import com.etsdk.app.huov7.view.IlivePhotoView;
import com.game.sdk.log.L;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.av.TIMAvManager;
import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRecordOption;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.tools.quality.ILiveQualityData;
import com.tencent.ilivesdk.tools.quality.LiveInfo;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.liteav.beauty.TXCVideoPreprocessor;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVText;
import com.tencent.qcloud.sdk.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.etsdk.app.huov7.base.AileApplication.faceUrl;


/**
 * Live直播类
 */
public class LiveActivity extends BaseActivity implements LiveView, View.OnClickListener, ProfileView, LiveListView, GetLinkSigView {
    private static final String TAG = LiveActivity.class.getSimpleName();
    private static final int GETPROFILE_JOIN = 0x200;

    private LiveHelper mLiveHelper;
    private LiveListViewHelper mLiveListHelper;
    private GetLinkSignHelper mLinkHelper;

    private ArrayList<ChatEntity> mArrayListChatEntity;
    private ChatMsgListAdapter mChatMsgListAdapter;
    private static final int MINFRESHINTERVAL = 500;
    private static final int UPDAT_WALL_TIME_TIMER_TASK = 1;
    private static final int TIMEOUT_INVITE = 2;
    private boolean mBoolRefreshLock = false;
    private boolean mBoolNeedRefresh = false;
    private final Timer mTimer = new Timer();
    private ArrayList<ChatEntity> mTmpChatList = new ArrayList<ChatEntity>();//缓冲队列
    private TimerTask mTimerTask = null;
    private static final int REFRESH_LISTVIEW = 5;
    private HeartLayout mHeartLayout;
    private HeartBeatTask mHeartBeatTask;//心跳
    private ImageView mHeadIcon;
    private TextView mHostNameTv;
    private LinearLayout mHostLayout, mHostLeaveLayout;
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private long mSecond = 0;
    private String formatTime;
    private Timer mHearBeatTimer, mVideoTimer;
    private VideoTimerTask mVideoTimerTask;//计时器
    private TextView mVideoTime;
    private ObjectAnimator mObjAnim;
    private ImageView mRecordBall;
    private TextView roomId;
    private LinearLayout join_tip;
    private TextView join_tv;
    private LinearLayout photo_ll;
    private RoundedImageView iv_game_img;
    private int thumbUp = 0;
    private long admireTime = 0;
    private int watchCount = 0;
    private boolean bCleanMode = false;
    private boolean bInAvRoom = false, bSlideUp = false, bDelayQuit = false;
    private boolean bReadyToChange = false;
    private boolean bHLSPush = false;
    private boolean bVideoMember = false;       // 是否上麦观众
    private String backGroundId;

    private TextView tvMembers;
    private TextView tvAdmires;
    private AVRootView mRootView;

    private Dialog mDetailDialog;

    private TXCVideoPreprocessor mTxcFilter;//美颜处理器
    private Map<String, IlivePhotoView> photoViewMap;
    private Map<String, MemberInfo> memberInfoMap;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_live);
        checkPermission();
        photoViewMap = new HashMap<>();
        memberInfoMap = new HashMap<>();
        mLiveHelper = new LiveHelper(this, this);
        mLiveListHelper = new LiveListViewHelper(this);
        mLinkHelper = new GetLinkSignHelper(this);
        initView();
        backGroundId = CurLiveInfo.getHostID();
        //进入房间流程
        L.i("333", "开始进入房间");
        mLiveHelper.startEnterRoom();
    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDAT_WALL_TIME_TIMER_TASK:
                    updateWallTime();
                    break;
                case REFRESH_LISTVIEW:
                    doRefreshListView();
                    break;
                case TIMEOUT_INVITE:
                    String id = "" + msg.obj;
                    mLiveHelper.sendGroupCmd(Constants.AVIMCMD_MULTI_HOST_CANCELINVITE, id);
                    break;
            }
            return false;
        }
    });

    /**
     * 时间格式化
     */
    private void updateWallTime() {
        String hs, ms, ss;

        long h, m, s;
        h = mSecond / 3600;
        m = (mSecond % 3600) / 60;
        s = (mSecond % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        if (hs.equals("00")) {
            formatTime = ms + ":" + ss;
        } else {
            formatTime = hs + ":" + ms + ":" + ss;
        }

        if (Constants.HOST == MySelfInfo.getInstance().getIdStatus() && null != mVideoTime) {
            L.i(TAG, " refresh time ");
            mVideoTime.setText(formatTime);
        }
    }

    /**
     * 初始化UI
     */
    private TextView BtnBack, BtnMic, BtnNormal, mBeautyConfirm, memberMic;
    private ListView mListViewMsgItems;
    private LinearLayout mHostCtrView, mNomalMemberCtrView, mBeautySettings;
    private FrameLayout mFullControllerUi;
    private SeekBar mBeautyBar, mWhiteBar;
    private int mBeautyRate, mWhiteRate;
    private TextView pushBtn, recordBtn;

    private void showHeadIcon(ImageView view, String avatar) {
        if (TextUtils.isEmpty(avatar)) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
            Bitmap cirBitMap = UIUtils.createCircleImage(bitmap, 0);
            view.setImageBitmap(cirBitMap);
        } else {
            L.d(TAG, "load icon: " + avatar);
            RequestManager req = Glide.with(this);
            req.load(avatar).transform(new GlideCircleTransform(this)).into(view);
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        iv_game_img = (RoundedImageView) findViewById(R.id.iv_game_img);
        join_tip = (LinearLayout) findViewById(R.id.join_tip);
        join_tv = (TextView) findViewById(R.id.join_tv);
        photo_ll = (LinearLayout) findViewById(R.id.photo_ll);
        mHostCtrView = (LinearLayout) findViewById(R.id.host_bottom_layout);
        mNomalMemberCtrView = (LinearLayout) findViewById(R.id.member_bottom_layout);
        mHostLeaveLayout = (LinearLayout) findViewById(R.id.ll_host_leave);

        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        mVideoTime = (TextView) findViewById(R.id.broadcasting_time);
        mHeadIcon = (ImageView) findViewById(R.id.head_icon);
        mHostNameTv = (TextView) findViewById(R.id.host_name);
        tvMembers = (TextView) findViewById(R.id.member_counts);
        tvAdmires = (TextView) findViewById(R.id.heart_counts);

        // 通用对话框初始化
        initVoiceTypeDialog();
        initRoleDialog();

        // 通用按钮初始化

        mBeautySettings = (LinearLayout) findViewById(R.id.qav_beauty_setting);
        mBeautyConfirm = (TextView) findViewById(R.id.qav_beauty_setting_finish);
        mBeautyConfirm.setOnClickListener(this);
        mBeautyBar = (SeekBar) (findViewById(R.id.qav_beauty_progress));
        mBeautyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                L.d("SeekBar", "onStopTrackingTouch");
                Toast.makeText(LiveActivity.this, "beauty " + mBeautyRate + "%", Toast.LENGTH_SHORT).show();//美颜度
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                L.d("SeekBar", "onStartTrackingTouch");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Log.i(TAG, "onProgressChanged " + progress);
                mBeautyRate = progress;
                if (MySelfInfo.getInstance().getBeautyType() == 1) {
                    mTxcFilter.setBeautyLevel(progress * 7 / 100);
                } else {//美颜
                    ILiveRoomManager.getInstance().enableBeauty(getBeautyProgress(progress));
                }
            }
        });
        mWhiteBar = (SeekBar) (findViewById(R.id.qav_white_progress));
        mWhiteBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                L.d("SeekBar", "onStopTrackingTouch");
                Toast.makeText(LiveActivity.this, "white " + mWhiteRate + "%", Toast.LENGTH_SHORT).show();//美白度
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                L.d("SeekBar", "onStartTrackingTouch");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Log.i(TAG, "onProgressChanged " + progress);
                mWhiteRate = progress;
                if (MySelfInfo.getInstance().getBeautyType() == 1) {
                    mTxcFilter.setWhitenessLevel(progress * 9 / 100);
                } else {//美白
                    ILiveRoomManager.getInstance().enableWhite(getBeautyProgress(progress));
                }
            }
        });

        roomId = (TextView) findViewById(R.id.room_id);

        //for 测试用
        TextView paramVideo = (TextView) findViewById(R.id.param_video);
        paramVideo.setOnClickListener(this);
        tvTipsMsg = (TextView) findViewById(R.id.qav_tips_msg);
        tvTipsMsg.setTextColor(Color.BLACK);
        paramTimer.schedule(task, 1000, 1000);

        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            // 初始化主播控件
            mHostCtrView.setVisibility(View.VISIBLE);
            mNomalMemberCtrView.setVisibility(View.GONE);
            mRecordBall = (ImageView) findViewById(R.id.record_ball);
            BtnMic = (TextView) findViewById(R.id.host_mic_btn);

            findViewById(R.id.host_message_input).setOnClickListener(this);
            findViewById(R.id.host_menu_more).setOnClickListener(this);
            BtnMic.setOnClickListener(this);

            tvAdmires.setVisibility(View.VISIBLE);

            initBackDialog();
            initDetailDailog();
            startRecordAnimation();
            showHeadIcon(mHeadIcon, MySelfInfo.getInstance().getAvatar());
        } else {
            // 初始化观众控件
            memberMic = (TextView) findViewById(R.id.member_mic_btn);
            mHostCtrView.setVisibility(View.GONE);
            changeCtrlView(bVideoMember);


            findViewById(R.id.record_tip).setVisibility(View.GONE);
            mHostNameTv.setVisibility(View.VISIBLE);
            findViewById(R.id.member_message_input).setOnClickListener(this);
            findViewById(R.id.member_send_good).setOnClickListener(this);
            findViewById(R.id.member_menu_more).setOnClickListener(this);
            findViewById(R.id.member_mic_btn).setOnClickListener(this);

            List<String> ids = new ArrayList<>();
            ids.add(CurLiveInfo.getHostID());
            showHeadIcon(mHeadIcon, CurLiveInfo.getHostAvator());
            mHostNameTv.setText(UIUtils.getLimitString(CurLiveInfo.getHostID(), 10));

            mHostLayout = (LinearLayout) findViewById(R.id.head_up_layout);
            mHostLayout.setOnClickListener(this);
        }
        BtnNormal = (TextView) findViewById(R.id.normal_btn);
        BtnNormal.setOnClickListener(this);
        mFullControllerUi = (FrameLayout) findViewById(R.id.controll_ui);

        initRecordDialog();

        BtnBack = (TextView) findViewById(R.id.btn_back);
        BtnBack.setOnClickListener(this);

        mListViewMsgItems = (ListView) findViewById(R.id.im_msg_listview);
        mArrayListChatEntity = new ArrayList<ChatEntity>();
        mChatMsgListAdapter = new ChatMsgListAdapter(this, mListViewMsgItems, mArrayListChatEntity);
        mListViewMsgItems.setAdapter(mChatMsgListAdapter);
        mChatMsgListAdapter.setItemListener(new ChatMsgListAdapter.ItemListener() {
            @Override
            public void applyVoice(String id) {
                mLiveHelper.sendC2CCmd(Constants.INVITE_VOICE, "邀请上麦", id);
            }
        });
        tvMembers.setText("" + CurLiveInfo.getMembers());
        tvAdmires.setText("" + CurLiveInfo.getAdmires());

        //TODO 获取渲染层
        mRootView = (AVRootView) findViewById(R.id.av_root_view);
        //TODO 设置渲染层
        ILVLiveManager.getInstance().setAvVideoView(mRootView);

        mRootView.setLocalFullScreen(false);
        mRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        mRootView.setSubMarginY(getResources().getDimensionPixelSize(R.dimen.small_area_margin_top));
        mRootView.setSubMarginX(getResources().getDimensionPixelSize(R.dimen.small_area_marginright));
        mRootView.setSubPadding(getResources().getDimensionPixelSize(R.dimen.small_area_marginbetween));
        mRootView.setSubWidth(getResources().getDimensionPixelSize(R.dimen.small_area_width));
        mRootView.setSubHeight(getResources().getDimensionPixelSize(R.dimen.small_area_height));
        mRootView.setSubCreatedListener(new AVRootView.onSubViewCreatedListener() {
            @Override
            public void onSubViewCreated() {
                for (int i = 1; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
                    final int index = i;
                    AVVideoView avVideoView = mRootView.getViewByIndex(index);
                    avVideoView.setRotate(false);
                    avVideoView.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            mRootView.swapVideoView(0, index);
                            backGroundId = mRootView.getViewByIndex(0).getIdentifier();
                            return super.onSingleTapConfirmed(e);
                        }
                    });
                }


                mRootView.getViewByIndex(0).setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        if (MySelfInfo.getInstance().getIdStatus() != Constants.HOST) {
                            if (e1.getY() - e2.getY() > 20 && Math.abs(velocityY) > 10) {
                                bSlideUp = true;
                            } else if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 10) {
                                bSlideUp = false;
                            }
                            switchRoom();
                        }
                        return false;
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        ILiveRoomManager.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ILiveRoomManager.getInstance().onPause();
    }

    /**
     * 直播心跳
     */
    private class HeartBeatTask extends TimerTask {
        @Override
        public void run() {
            String host = CurLiveInfo.getHostID();
            L.i(TAG, "HeartBeatTask " + host);
            if (!TextUtils.isEmpty(MySelfInfo.getInstance().getId()) && MySelfInfo.getInstance().getId().equals(CurLiveInfo.getHostID()))
                UserServerHelper.getInstance().heartBeater(1);
            else
                UserServerHelper.getInstance().heartBeater(MySelfInfo.getInstance().getIdStatus());
            mLiveHelper.pullMemberList();
        }
    }

    /**
     * 记时器
     */
    private class VideoTimerTask extends TimerTask {
        public void run() {
            L.i(TAG, "timeTask ");
            ++mSecond;
            if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST)
                mHandler.sendEmptyMessage(UPDAT_WALL_TIME_TIMER_TASK);
        }
    }

    @Override
    protected void onDestroy() {
        watchCount = 0;
        super.onDestroy();
        mLiveHelper.quitLiveRoom();

        if (null != mHearBeatTimer) {
            mHearBeatTimer.cancel();
            mHearBeatTimer = null;
        }
        if (null != mVideoTimer) {
            mVideoTimer.cancel();
            mVideoTimer = null;
        }
        if (null != paramTimer) {
            paramTimer.cancel();
            paramTimer = null;
        }
        thumbUp = 0;
        CurLiveInfo.setMembers(0);
        CurLiveInfo.setAdmires(0);
        CurLiveInfo.setCurrentRequestCount(0);
        mLiveHelper.onDestory();
    }


    /**
     * 点击Back键
     */
    @Override
    public void onBackPressed() {
        if (bInAvRoom) {
            bDelayQuit = false;
            quiteLiveByPurpose();
        } else {
            clearOldData();
            finish();
        }
    }

    @Override
    public void forceQuitRoom(String strMessage) {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        ILiveRoomManager.getInstance().onPause();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(strMessage)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                callExitRoom();
            }
        });
        alertDialog.show();
    }

    /**
     * 主动退出直播
     */
    private void quiteLiveByPurpose() {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            if (backDialog.isShowing() == false)
                backDialog.show();
        } else {
            callExitRoom();
        }
    }

    private void callExitRoom() {
        mLiveHelper.startExitRoom();
    }


    private Dialog backDialog;

    private void initBackDialog() {
        backDialog = new Dialog(this, R.style.dialog);
        backDialog.setContentView(R.layout.dialog_end_live);
        TextView tvSure = (TextView) backDialog.findViewById(R.id.btn_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callExitRoom();
                // 取消跨房连麦
                ILVLiveManager.getInstance().unlinkRoom(null);
                backDialog.dismiss();
            }
        });
        TextView tvCancel = (TextView) backDialog.findViewById(R.id.btn_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDialog.cancel();
            }
        });
    }

    // 变声对话框
    private RadioGroupDialog voiceTypeDialog;
    private int curVoice = 0;

    private void initVoiceTypeDialog() {
        final String[] roles = new String[]{"原声", "萝莉", "大叔", "空灵", "幼稚园", "重机器",
                "擎天柱", "困兽", "土掉渣/歪果仁/方言", "金属机器人", "死肥仔"};
        voiceTypeDialog = new RadioGroupDialog(this, roles);
        voiceTypeDialog.setTitle(R.string.str_dt_voice);
        voiceTypeDialog.setSelected(curVoice);
        voiceTypeDialog.setOnItemClickListener(new RadioGroupDialog.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                L.d(TAG, "initRoleDialog->onClick item:" + position);
                curRole = position;
                ILiveSDK.getInstance().getAvAudioCtrl().setVoiceType(curRole);
            }
        });
    }

    // 角色对话框
    private RadioGroupDialog roleDialog;
    private int curRole = 0;

    private void initRoleDialog() {
        final String[] roles = new String[]{"高清(960*540,25fps)", "标清(640*368,20fps)", "流畅(640*368,15fps)"};
        final String[] values = new String[]{Constants.HD_ROLE, Constants.SD_ROLE, Constants.LD_ROLE};
        final String[] guestValues = new String[]{Constants.HD_GUEST_ROLE, Constants.SD_GUEST_ROLE, Constants.LD_GUEST_ROLE};
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            if (CurLiveInfo.getCurRole().equals(Constants.SD_ROLE)) {
                curRole = 1;
            } else if (CurLiveInfo.getCurRole().equals(Constants.LD_ROLE)) {
                curRole = 2;
            }
        }
        roleDialog = new RadioGroupDialog(this, roles);
        roleDialog.setTitle(R.string.str_dt_change_role);
        roleDialog.setSelected(curRole);
        roleDialog.setOnItemClickListener(new RadioGroupDialog.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                L.d(TAG, "initRoleDialog->onClick item:" + position);
                curRole = position;
                if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
                    mLiveHelper.changeRole(values[curRole]);
                } else {
                    mLiveHelper.changeRole(guestValues[curRole]);
                }
            }
        });
    }

    /**
     * 完成进出房间流程
     */
    @Override
    public void enterRoomComplete(int id_status, boolean isSucc) {
        mBeautyBar.setProgress(72);
        mWhiteBar.setProgress(33);

        mRootView.getViewByIndex(0).setVisibility(GLView.VISIBLE);

        //mRootView.getViewByIndex(0).setRotate(true);
//        mRootView.getViewByIndex(0).setDiffDirectionRenderMode(AVVideoView.ILiveRenderMode.BLACK_TO_FILL);
        bInAvRoom = true;
        bDelayQuit = true;
        bReadyToChange = true;
        roomId.setText("" + CurLiveInfo.getRoomNum());
        if (isSucc == true) {
            //主播心跳
            mHearBeatTimer = new Timer(true);
            mHeartBeatTask = new HeartBeatTask();
            mHearBeatTimer.schedule(mHeartBeatTask, 100, 5 * 1000); //5秒重复上报心跳 拉取房间列表

            //直播时间
            mVideoTimer = new Timer(true);
            mVideoTimerTask = new VideoTimerTask();
            mVideoTimer.schedule(mVideoTimerTask, 1000, 1000);

            //IM初始化
            if (id_status == Constants.HOST) {//主播方式加入房间成功
                L.i("333", "主播");
                mHostNameTv.setText(MySelfInfo.getInstance().getId());

                //注册一个音频回调为变声用
                ILiveSDK.getInstance().getAvAudioCtrl().registAudioDataCallbackWithByteBuffer(AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_VOICEDISPOSE, new AVAudioCtrl.RegistAudioDataCompleteCallbackWithByteBuffer() {
                    @Override
                    public int onComplete(AVAudioCtrl.AudioFrameWithByteBuffer audioFrameWithByteBuffer, int i) {
                        return 0;
                    }
                });

                //开启摄像头渲染画面
                L.i(TAG, "createlive enterRoomComplete isSucc" + isSucc);
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putBoolean("living", true);
                editor.apply();
                IlivePhotoView photoView = new IlivePhotoView(this);
                photoView.setData(MySelfInfo.getInstance().getAvatar());
                final MemberInfo memberInfo;
                if (memberInfoMap.containsKey(MySelfInfo.getInstance().getId())) {
                    memberInfo = memberInfoMap.get(MySelfInfo.getInstance().getId());
                } else {
                    memberInfo = new MemberInfo();
                    memberInfo.setIsOnVideoChat(false);
                    memberInfo.setVoice(false);
                    memberInfoMap.put(MySelfInfo.getInstance().getId(), memberInfo);
                }
                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IliveMemberInfoDialog memberInfoDialog = new IliveMemberInfoDialog(LiveActivity.this, MySelfInfo.getInstance().getId(), MySelfInfo.getInstance().getAvatar(), MySelfInfo.getInstance().getNickName());
                        memberInfoDialog.show();
                        memberInfoDialog.hideInvite();
                        memberInfoDialog.setListener(new IliveMemberInfoDialog.ClickListener() {
                            @Override
                            public void report() {

                            }

                            @Override
                            public void invite() {


                            }

                            @Override
                            public void friend() {

                            }

                            @Override
                            public void gift() {
                                bottomwindow(findViewById(R.id.member_menu_more));
                            }

                            @Override
                            public void kick() {

                            }

                            @Override
                            public void close() {
                            }
                        });
                    }
                });
                photo_ll.addView(photoView);
            } else {
                L.i("333", "观众");
                IlivePhotoView photoView = new IlivePhotoView(this);
                photoView.setData(MySelfInfo.getInstance().getAvatar());
                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IliveMemberInfoDialog memberInfoDialog = new IliveMemberInfoDialog(LiveActivity.this, MySelfInfo.getInstance().getId(), MySelfInfo.getInstance().getAvatar(), MySelfInfo.getInstance().getNickName());
                        memberInfoDialog.show();
                        if (mLiveHelper.isVoice()) {
                            memberInfoDialog.setVoice("下麦");
                        } else {
                            memberInfoDialog.hideInvite();
                        }
                        memberInfoDialog.setListener(new IliveMemberInfoDialog.ClickListener() {
                            @Override
                            public void report() {

                            }

                            @Override
                            public void invite() {
                                mLiveHelper.downMemberVideo();
                            }

                            @Override
                            public void friend() {

                            }

                            @Override
                            public void gift() {
                                bottomwindow(findViewById(R.id.member_menu_more));
                            }

                            @Override
                            public void kick() {

                            }

                            @Override
                            public void close() {
                            }
                        });
                    }
                });
                photo_ll.addView(photoView);
                // 更新控制栏
                changeCtrlView(false);
                //发消息通知上线
                mLiveHelper.sendGroupCmd(Constants.AVIMCMD_ENTERLIVE, "");
            }
        }
    }


    @Override
    public void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                UserServerHelper.getInstance().reportMe(MySelfInfo.getInstance().getIdStatus(), 1);//通知server 我下线了
            }
        }.start();

        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            if ((getBaseContext() != null) && (null != mDetailDialog) && (mDetailDialog.isShowing() == false)) {
                L.d(TAG, LogConstants.ACTION_HOST_QUIT_ROOM + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "quite room callback"
                        + LogConstants.DIV + LogConstants.STATUS.SUCCEED + LogConstants.DIV + "id status " + id_status);
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putBoolean("living", false);
                editor.apply();
                mDetailTime.setText(formatTime);
                mDetailAdmires.setText("" + CurLiveInfo.getAdmires());
                mDetailWatchCount.setText("" + watchCount);
                mDetailDialog.show();
            }
        } else {
            clearOldData();
            finish();
        }

        if (null != mTxcFilter && MySelfInfo.getInstance().getBeautyType() == 1) {
            L.d(TAG, "FILTER->destory");
            mTxcFilter.release();
            mTxcFilter = null;
        }
        //发送
        bInAvRoom = false;
    }


    private TextView mDetailTime, mDetailAdmires, mDetailWatchCount;

    private void initDetailDailog() {
        mDetailDialog = new Dialog(this, R.style.dialog);
        mDetailDialog.setContentView(R.layout.dialog_live_detail);
        mDetailTime = (TextView) mDetailDialog.findViewById(R.id.tv_time);
        mDetailAdmires = (TextView) mDetailDialog.findViewById(R.id.tv_admires);
        mDetailWatchCount = (TextView) mDetailDialog.findViewById(R.id.tv_members);

        mDetailDialog.setCancelable(false);

        TextView tvCancel = (TextView) mDetailDialog.findViewById(R.id.btn_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailDialog.dismiss();
                finish();
            }
        });
//        mDetailDialog.show();
    }

    /**
     * 成员状态变更
     */
    @Override
    public void memberJoin(final String id, final TIMUserProfile profile) {
        L.d("333", LogConstants.ACTION_VIEWER_ENTER_ROOM + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "on member join" +
                LogConstants.DIV + "join room " + id);
        watchCount++;
        refreshTextListView(id, TextUtils.isEmpty(profile.getNickName()) ? id : profile.getNickName(), "加入房间", profile.getFaceUrl(), Constants.MEMBER_ENTER);
        IlivePhotoView photoView = new IlivePhotoView(this);
        photoView.setData(profile.getFaceUrl());
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MemberInfo memberInfo;
                if (memberInfoMap.containsKey(id)) {
                    memberInfo = memberInfoMap.get(id);
                } else {
                    memberInfo = new MemberInfo();
                    memberInfo.setIsOnVideoChat(false);
                    memberInfo.setVoice(false);
                    memberInfoMap.put(id, memberInfo);
                }

                L.i("333", "点击连麦");
                final IliveMemberInfoDialog memberInfoDialog = new IliveMemberInfoDialog(LiveActivity.this, profile.getIdentifier(), profile.getFaceUrl(), profile.getNickName());
                memberInfoDialog.show();
                if (memberInfo.isOnVideoChat()) {
                    L.i("333", "下麦");
                    if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST || profile.getIdentifier() == MySelfInfo.getInstance().getId()) {
                        memberInfoDialog.setVoice("下麦");
                    } else {
                        memberInfoDialog.hideInvite();
                    }
                } else {
                    L.i("333", "邀请上麦");
                    if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
                        memberInfoDialog.setVoice("邀请上麦");
                    } else {
                        memberInfoDialog.hideInvite();
                    }
                }
                if (MySelfInfo.getInstance().getIdStatus() != Constants.HOST) {
                    memberInfoDialog.hideInvite();
                }
                memberInfoDialog.setListener(new IliveMemberInfoDialog.ClickListener() {
                    @Override
                    public void report() {

                    }

                    @Override
                    public void invite() {
                        if (memberInfo.isOnVideoChat()) {
                            if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
                                mLiveHelper.sendC2CCmd(Constants.DOWN_VOICE, "", profile.getIdentifier());
                            } else {
                                mLiveHelper.downMemberVideo();
                            }
                        } else {
                            mLiveHelper.sendC2CCmd(Constants.INVITE_VOICE, "邀请上麦", profile.getIdentifier());
                        }

                    }

                    @Override
                    public void friend() {

                    }

                    @Override
                    public void gift() {
                        bottomwindow(findViewById(R.id.member_menu_more));
                    }

                    @Override
                    public void kick() {

                    }

                    @Override
                    public void close() {
                    }
                });
            }
        });
        photo_ll.addView(photoView);
        photoViewMap.put(id, photoView);
        join_tip.setVisibility(View.VISIBLE);
        ImgUtil.setImg(LiveActivity.this, profile.getFaceUrl(), R.mipmap.icon_load, iv_game_img);
        join_tv.setText(TextUtils.isEmpty(profile.getNickName()) ? id : profile.getNickName());
        TranslateAnimation animation = new TranslateAnimation(-StringUtils.dip2px(LiveActivity.this, 200), StringUtils.dip2px(LiveActivity.this, 15), 0, 0);
        animation.setDuration(300);
        join_tip.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(200);
                        join_tip.setAnimation(alphaAnimation);
                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                join_tip.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }, 800);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void memberLeave(String id) {
        IlivePhotoView photoView = photoViewMap.get(id);
        photo_ll.removeView(photoView);
    }

    @Override
    public void hostLeave(String id, String name) {
        refreshTextListView(id, "host", "leave for a while", "", Constants.HOST_LEAVE);
    }

    @Override
    public void hostBack(String id, String name) {
        refreshTextListView(id, TextUtils.isEmpty(name) ? id : name, "is back", "", Constants.HOST_BACK);
    }

    @Override
    public void refreshMember(ArrayList<MemberID> memlist) {
        if (memlist != null && tvMembers != null)
            tvMembers.setText("" + memlist.size());
    }

    @Override
    public void linkRoomReq(final String id, String name) {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.live_btn_link);
            builder.setMessage("[" + id + "]" + getString(R.string.str_link_req_tips));
            builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mLiveHelper.refuseLink(id);
                }
            });
            builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mLiveHelper.acceptLink(id);
                }
            });
            builder.show();
        } else {
            mLiveHelper.refuseLink(id);
        }
    }


    @Override
    public void inviteVoice(String id) {
        VoiceDialog voiceDialog = new VoiceDialog();
        voiceDialog.show(LiveActivity.this, new VoiceDialog.Listener() {
            @Override
            public void ok() {
                mLiveHelper.upMemberVideo();
                mLiveHelper.toggleMic();
            }

            @Override
            public void cancel() {

            }
        });

    }

    @Override
    public void linkRoomAccept(final String id, final String strRoomId) {
        L.d(TAG, "linkRoomAccept->id:" + id + ", room:" + strRoomId);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.live_btn_link);
        builder.setMessage("[" + id + "]" + getString(R.string.str_link_start_tips));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mLinkHelper.getLinkSign(id, strRoomId);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public void onGetSignRsp(String id, String roomid, String sign) {
        L.d(TAG, "onGetSignRsp->id:" + id + ", room:" + roomid + ", sign:" + sign);
        mLiveHelper.linkRoom(id, roomid, sign);
    }

    /**
     * 红点动画
     */
    private void startRecordAnimation() {
        mObjAnim = ObjectAnimator.ofFloat(mRecordBall, "alpha", 1f, 0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();
    }

    private float getBeautyProgress(int progress) {
        L.d("shixu", "progress: " + progress);
        return (9.0f * progress / 100.0f);
    }

    @Override
    public void refreshText(String identifier, String text, String name, String faceUrl, int type) {
        if (text != null) {
            MemberInfo memberInfo = memberInfoMap.get(identifier);
            if (memberInfo == null) {
                memberInfo = new MemberInfo();
                memberInfoMap.put(identifier, memberInfo);
            }
            if (type == Constants.UP_VOICE_NOTICE) {
                type = Constants.TEXT_TYPE;
                L.i("333", "上麦了：" + identifier);
                memberInfo.setIsOnVideoChat(true);
            }
            if (type == Constants.DOWN_VOICE_NOTICE) {
                type = Constants.TEXT_TYPE;
                L.i("333", "下麦了：" + identifier);
                memberInfo.setIsOnVideoChat(false);
            }
            L.i("333", "显示信息");
            refreshTextListView(identifier, name, text, faceUrl, type);
        }
    }

    @Override
    public void refreshThumbUp() {
        CurLiveInfo.setAdmires(CurLiveInfo.getAdmires() + 1);
        if (!bCleanMode) {      // 纯净模式下不播放飘星动画
            mHeartLayout.addFavor();
        }
        tvAdmires.setText("" + CurLiveInfo.getAdmires());
    }


    private void showReportDialog() {
        final Dialog reportDialog = new Dialog(this, R.style.report_dlg);
        reportDialog.setContentView(R.layout.dialog_live_report);

        TextView tvReportDirty = (TextView) reportDialog.findViewById(R.id.btn_dirty);
        TextView tvReportFalse = (TextView) reportDialog.findViewById(R.id.btn_false);
        TextView tvReportVirus = (TextView) reportDialog.findViewById(R.id.btn_virus);
        TextView tvReportIllegal = (TextView) reportDialog.findViewById(R.id.btn_illegal);
        TextView tvReportYellow = (TextView) reportDialog.findViewById(R.id.btn_yellow);
        TextView tvReportCancel = (TextView) reportDialog.findViewById(R.id.btn_cancel);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    default:
                        reportDialog.cancel();
                        break;
                }
            }
        };

        tvReportDirty.setOnClickListener(listener);
        tvReportFalse.setOnClickListener(listener);
        tvReportVirus.setOnClickListener(listener);
        tvReportIllegal.setOnClickListener(listener);
        tvReportYellow.setOnClickListener(listener);
        tvReportCancel.setOnClickListener(listener);

        reportDialog.setCanceledOnTouchOutside(true);
        reportDialog.show();
    }

    private boolean checkInterval() {
        if (0 == admireTime) {
            admireTime = System.currentTimeMillis();
            return true;
        }
        long newTime = System.currentTimeMillis();
        if (newTime >= admireTime + 1000) {
            admireTime = newTime;
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.host_message_input:
            case R.id.member_message_input:
                inputMsgDialog();
                break;
            case R.id.member_send_good:
                mHeartLayout.addFavor();
                if (checkInterval()) {
                    mLiveHelper.sendGroupCmd(Constants.AVIMCMD_PRAISE, "");
                    CurLiveInfo.setAdmires(CurLiveInfo.getAdmires() + 1);
                    tvAdmires.setText("" + CurLiveInfo.getAdmires());
                }
                break;
            case R.id.host_menu_more:
                bottomwindow(findViewById(R.id.host_menu_more));
                break;
            case R.id.member_menu_more:
                bottomwindow(findViewById(R.id.member_menu_more));
                break;
            case R.id.host_mic_btn:
                if (mLiveHelper.isMicOn()) {
                    BtnMic.setBackgroundResource(R.drawable.icon_mic_close);
                } else {
                    BtnMic.setBackgroundResource(R.drawable.icon_mic_open);
                }
                mLiveHelper.toggleMic();
                break;
            case R.id.member_mic_btn:
                if (mLiveHelper.isVoice()) {
                    if (mLiveHelper.isMicOn()) {
                        memberMic.setBackgroundResource(R.drawable.icon_mic_close);
                    } else {
                        memberMic.setBackgroundResource(R.drawable.icon_mic_open);
                    }
                    mLiveHelper.toggleMic();
                } else {
                    mLiveHelper.sendGroupCmd(Constants.APPLY_VOICE, "申请上麦");
                }
                break;
            case R.id.normal_btn:
                bCleanMode = false;
                mFullControllerUi.setVisibility(View.VISIBLE);
                BtnNormal.setVisibility(View.GONE);
                break;
            case R.id.head_up_layout:
                break;
            case R.id.qav_beauty_setting_finish:
                mBeautySettings.setVisibility(View.GONE);
                mFullControllerUi.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_back:
                quiteLiveByPurpose();
                break;
            case R.id.param_video:
                showTips = !showTips;
                break;
        }
    }

    private void bottomwindow(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        GiftView giftView = new GiftView(LiveActivity.this);
        giftView.setListener(new GiftView.GridListener() {
            @Override
            public void onclick(int res) {
                L.i("333", "发礼物：" + res);
                mLiveHelper.sendGroupCmd(Constants.GIFT_TYPE, "" + res);
            }
        });
        popupWindow = new PopupWindow(giftView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        //添加按键事件监听
    }

    private String getParams(String src, String title, String key) {
        int pos = src.indexOf(key);
        if (-1 != pos) {
            pos += key.length() + 2;
            int endPos = src.indexOf(",", pos);
            return title + ": " + src.substring(pos, endPos) + "\n";
        }
        return "";
    }

    // 补充房间信息
    private String expandTips(String tips) {

        // 获取是否开启硬件编解码
        if (null != ILiveSDK.getInstance().getAVContext().getRoom()) {
            String videoTips = ILiveSDK.getInstance().getAVContext().getRoom().getQualityParam();
            tips += getParams(videoTips, "大画面硬编解", "qos_big_hw");
        }

        if (null != ILiveSDK.getInstance().getAvVideoCtrl()) {
            // 输出采集支持分辨率
            Camera camera = (Camera) ILiveSDK.getInstance().getAvVideoCtrl().getCamera();
            if (null != camera) {
                tips += "摄像头支持分辨率: \n";
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
                for (Camera.Size size : supportedPreviewSizes) {
                    tips += "\t" + size.width + "*" + size.height + "\n";
                }
            }
        }
        return tips;
    }

    //for 测试获取测试参数
    private boolean showTips = false;
    private TextView tvTipsMsg;
    Timer paramTimer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (showTips) {
                        if (tvTipsMsg != null && ILiveSDK.getInstance().getAVContext() != null &&
                                ILiveSDK.getInstance().getAVContext().getRoom() != null) {
                            //String tips =getQualityTips();
                            String tips = "";
                            ILiveQualityData qData = ILiveRoomManager.getInstance().getQualityData();
                            if (null != qData) {
                                tips += "FPS:\t" + qData.getUpFPS() + "\n";
                                tips += "Send:\t" + qData.getSendKbps() + "Kbps\t";
                                tips += "Recv:\t" + qData.getRecvKbps() + "Kbps\n";
                                tips += "SendLossRate:\t" + qData.getSendLossRate() + "%\t";
                                tips += "RecvLossRate:\t" + qData.getRecvLossRate() + "%\n";
                                tips += "AppCPURate:\t" + qData.getAppCPURate() + "%\t";
                                tips += "SysCPURate:\t" + qData.getSysCPURate() + "%\n";
                                Map<String, LiveInfo> userMaps = qData.getLives();
                                for (Map.Entry<String, LiveInfo> entry : userMaps.entrySet()) {
                                    tips += "\t" + entry.getKey() + "-" + entry.getValue().getWidth() + "*" + entry.getValue().getHeight() + "\n";
                                }
                            }

                            //tips = expandTips(tips);
                            tips += '\n';
                            tips += getQualityTips(ILiveSDK.getInstance().getAVContext().getRoom().getQualityTips());
                            tvTipsMsg.getBackground().setAlpha(125);
                            tvTipsMsg.setText(tips);
                            tvTipsMsg.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvTipsMsg.setText("");
                        tvTipsMsg.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    };


    @Override
    public void changeCtrlView(boolean videoMember) {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            // 主播不存在切换
            return;
        }
        bVideoMember = videoMember;
        if (bVideoMember) {
            mNomalMemberCtrView.setVisibility(View.GONE);
        } else {
            mNomalMemberCtrView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 发消息弹出框
     */
    private void inputMsgDialog() {
        InputTextMsgDialog inputMsgDialog = new InputTextMsgDialog(this, R.style.inputdialog, this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = inputMsgDialog.getWindow().getAttributes();

        lp.width = (int) (display.getWidth()); //设置宽度
        inputMsgDialog.getWindow().setAttributes(lp);
        inputMsgDialog.setCancelable(true);
        inputMsgDialog.show();
    }

    /**
     * 消息刷新显示
     *
     * @param name    发送者
     * @param context 内容
     * @param type    类型 （上线线消息和 聊天消息）
     */
    public void refreshTextListView(String identifier, String name, String context, String faceUrl, int type) {
        ChatEntity entity = new ChatEntity();
        entity.setIdentifier(identifier);
        entity.setSenderName(name);
        entity.setContext(context);
        entity.setFaceUrl(faceUrl);
        entity.setType(type);
        //mArrayListChatEntity.add(entity);
        notifyRefreshListView(entity);
        //mChatMsgListAdapter.notifyDataSetChanged();

        mListViewMsgItems.setVisibility(View.VISIBLE);
        L.d(TAG, "refreshTextListView height " + mListViewMsgItems.getHeight());

        if (mListViewMsgItems.getCount() > 1) {
            if (true)
                mListViewMsgItems.setSelection(0);
            else
                mListViewMsgItems.setSelection(mListViewMsgItems.getCount() - 1);
        }
    }


    /**
     * 通知刷新消息ListView
     */
    private void notifyRefreshListView(ChatEntity entity) {
        mBoolNeedRefresh = true;
        mTmpChatList.add(entity);
        if (mBoolRefreshLock) {
            return;
        } else {
            doRefreshListView();
        }
    }


    /**
     * 刷新ListView并重置状态
     */
    private void doRefreshListView() {
        if (mBoolNeedRefresh) {
            mBoolRefreshLock = true;
            mBoolNeedRefresh = false;
            mArrayListChatEntity.addAll(mTmpChatList);
            mTmpChatList.clear();
            mChatMsgListAdapter.notifyDataSetChanged();

            if (null != mTimerTask) {
                mTimerTask.cancel();
            }
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    L.v(TAG, "doRefreshListView->task enter with need:" + mBoolNeedRefresh);
                    mHandler.sendEmptyMessage(REFRESH_LISTVIEW);
                }
            };
            //mTimer.cancel();
            mTimer.schedule(mTimerTask, MINFRESHINTERVAL);
        } else {
            mBoolRefreshLock = false;
        }
    }

    @Override
    public void updateProfileInfo(TIMUserProfile profile) {

    }

    @Override
    public void updateUserInfo(int requestCode, List<TIMUserProfile> profiles) {
        if (null != profiles) {
            switch (requestCode) {
                case GETPROFILE_JOIN:
                    for (TIMUserProfile user : profiles) {
                        tvMembers.setText("" + CurLiveInfo.getMembers());
                        L.w(TAG, "get nick name:" + user.getNickName());
                        L.w(TAG, "get remark name:" + user.getRemark());
                        L.w(TAG, "get avatar:" + user.getFaceUrl());
                        if (!TextUtils.isEmpty(user.getNickName())) {
                            refreshTextListView(user.getIdentifier(), user.getNickName(), "join live", user.getFaceUrl(), Constants.MEMBER_ENTER);
                        } else {
                            refreshTextListView(user.getIdentifier(), user.getIdentifier(), "join live", user.getFaceUrl(), Constants.MEMBER_ENTER);
                        }
                    }
                    break;
            }

        }
    }


    private Dialog recordDialog;
    private String filename = "";
    private boolean mRecord = false;
    private EditText filenameEditText;

    private void initRecordDialog() {
        recordDialog = new Dialog(this, R.style.dialog);
        recordDialog.setContentView(R.layout.record_layout);

        filenameEditText = (EditText) recordDialog.findViewById(R.id.record_filename);

        if (filename.length() > 0) {
            filenameEditText.setText(filename);
        }
        filenameEditText.setText("" + CurLiveInfo.getRoomNum());

        Button videoRecord = (Button) recordDialog.findViewById(R.id.btn_record_video);
        videoRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ILiveRecordOption option = new ILiveRecordOption();
                filename = filenameEditText.getText().toString();
                option.fileName("sxb_" + ILiveLoginManager.getInstance().getMyUserId() + "_" + filename);

                option.classId(123);
                option.recordType(TIMAvManager.RecordType.VIDEO);
                mLiveHelper.notifyNewRecordInfo(filename);
                recordDialog.dismiss();
            }
        });
        Button audioRecord = (Button) recordDialog.findViewById(R.id.btn_record_audio);
        audioRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ILiveRecordOption option = new ILiveRecordOption();
                filename = filenameEditText.getText().toString();
                option.fileName("sxb_" + ILiveLoginManager.getInstance().getMyUserId() + "_" + filename);

                option.classId(123);
                option.recordType(TIMAvManager.RecordType.AUDIO);
                recordDialog.dismiss();
                recordDialog.dismiss();
            }
        });
        Window dialogWindow = recordDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        recordDialog.setCanceledOnTouchOutside(false);
    }


    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WAKE_LOCK);
            if ((checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        }
    }

    // 清除老房间数据
    private void clearOldData() {
        mArrayListChatEntity.clear();
        mBoolNeedRefresh = true;
        if (mBoolRefreshLock) {
            return;
        } else {
            doRefreshListView();
        }
        mRootView.clearUserView();
    }


    @Override
    public void showRoomList(UserServerHelper.RequestBackInfo reqinfo, ArrayList<RoomInfoJson> livelist) {
        if (reqinfo.getErrorCode() != 0) {
            Toast.makeText(this, "error" + reqinfo.getErrorCode() + " info " + reqinfo.getErrorInfo(), Toast.LENGTH_SHORT).show();
            return;
        }

        if (null == livelist || 0 == livelist.size()) {
            L.d(TAG, "showRoomList->there room list is empty");
            return;
        }

        int index = 0, oldPos = 0;
        for (; index < livelist.size(); index++) {
            if (livelist.get(index).getInfo().getRoomnum() == CurLiveInfo.getRoomNum()) {
                oldPos = index;
                index++;
                break;
            }
        }
        if (bSlideUp) {
            index -= 2;
        }
        RoomInfoJson info = livelist.get((index + livelist.size()) % livelist.size());

        if (null != info) {
            MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
            MySelfInfo.getInstance().setJoinRoomWay(false);
            CurLiveInfo.setHostID(info.getHostId());
            CurLiveInfo.setHostName("");
            CurLiveInfo.setHostAvator("");
            CurLiveInfo.setRoomNum(info.getInfo().getRoomnum());
            CurLiveInfo.setMembers(info.getInfo().getMemsize()); // 添加自己
            CurLiveInfo.setAdmires(info.getInfo().getThumbup());

            backGroundId = CurLiveInfo.getHostID();

            showHeadIcon(mHeadIcon, CurLiveInfo.getHostAvator());
            if (!TextUtils.isEmpty(CurLiveInfo.getHostName())) {
                mHostNameTv.setText(UIUtils.getLimitString(CurLiveInfo.getHostName(), 10));
            } else {
                mHostNameTv.setText(UIUtils.getLimitString(CurLiveInfo.getHostID(), 10));
            }
            tvMembers.setText("" + CurLiveInfo.getMembers());
            tvAdmires.setText("" + CurLiveInfo.getAdmires());

            clearOldData();
            //进入房间流程
            mLiveHelper.switchRoom();
        } else {
            bReadyToChange = true;
        }
    }

    private void switchRoom() {
        if (bReadyToChange) {
            mLiveListHelper.getPageData();
        }
    }

    private static String getValue(String src, String param, String sep) {
        int idx = src.indexOf(param);
        if (-1 != idx) {
            idx += param.length() + 1;
            if (-1 != sep.indexOf(src.charAt(idx))) {
                idx++;
            }
            for (int i = idx; i < src.length(); i++) {
                if (-1 != sep.indexOf(src.charAt(i))) {
                    return src.substring(idx, i).trim();
                }
            }
        }

        return "";
    }

    public String getQualityTips(String qualityTips) {
        String strTips = "";
        String sep = "[](),\n";

        strTips += "AVSDK版本号: " + getValue(qualityTips, "sdk_version", sep) + "\n";
        strTips += "房间号: " + getValue(qualityTips, "RoomID", sep) + "\n";
        strTips += "角色: " + getValue(qualityTips, "ControlRole", sep) + "\n";
        strTips += "权限: " + getValue(qualityTips, "Authority", sep) + "\n";
        String tmpStr = getValue(qualityTips, "视频采集", "\n");
        if (!TextUtils.isEmpty(tmpStr))
            strTips += "采集信息: " + getValue(qualityTips, "视频采集", "\n") + "\n";
        strTips += "麦克风: " + getValue(qualityTips, "Mic", sep) + "\n";
        strTips += "扬声器: " + getValue(qualityTips, "Spk", sep) + "\n";

        return strTips;
    }

    private void showLinkDialog() {
        final Dialog dialog = new Dialog(this, R.style.common_dlg);
        dialog.setContentView(R.layout.dialog_link);

        ListView listView = (ListView) dialog.findViewById(R.id.lv_linklist);
        LinkAdapter adapter = new LinkAdapter(this);
        adapter.setOnItemClickListenr(new LinkAdapter.OnItemClick() {
            @Override
            public void onClick(RoomInfoJson info) {
                List<String> curLinkedList = ILVLiveManager.getInstance().getCurrentLinkedUserArray();
                if (null != curLinkedList && curLinkedList.size() >= 3) {
                    Toast.makeText(LiveActivity.this, getString(R.string.str_tips_link_limit), Toast.LENGTH_SHORT).show();
                } else {
                    mLiveHelper.sendLinkReq(info.getHostId());
                }
                dialog.dismiss();
            }
        });
        listView.setAdapter(adapter);

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setTitle(R.string.live_link_title);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

}
