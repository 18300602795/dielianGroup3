package com.etsdk.app.huov7.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.iLive.model.CurLiveInfo;
import com.etsdk.app.huov7.iLive.model.MySelfInfo;
import com.etsdk.app.huov7.iLive.utils.Constants;
import com.etsdk.app.huov7.iLive.views.LiveActivity;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.ui.AccountManageActivity;
import com.etsdk.app.huov7.ui.ArticleActivity;
import com.etsdk.app.huov7.ui.ChatRoomActivity;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.LoginActivity;
import com.etsdk.app.huov7.ui.MineGiftCouponListActivityNew;
import com.etsdk.app.huov7.ui.NearbyActivity;
import com.liang530.log.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\2\22 0022.
 */

public class HunterHeaderView extends RelativeLayout {
    View mRootView;
    private Context mContext;
    @BindView(R.id.game_ll)
    LinearLayout game_ll;
    AnimationDrawable animationDrawable;
    @BindView(R.id.tv_notice)
    TextSwitcher tv_notice;
    String[] mAdvertisements;
    final int HOME_AD_RESULT = 1;
    int mSwitcherCount = 0;
    GameBeanList gameBeanList;

    public void setGameBeanList(GameBeanList gameBeanList) {
        this.gameBeanList = gameBeanList;
        game_ll.removeAllViews();
        if (gameBeanList.getData().getList().size() <= 20) {
            for (int i = 0; i < gameBeanList.getData().getList().size(); i++) {
                GameItemView gameItemView = new GameItemView(mContext);
                gameItemView.setData(gameBeanList.getData().getList().get(i));
                game_ll.addView(gameItemView);
            }
//            game_ll.addView(new MoreItemView(mContext));
        } else {
            for (int i = 0; i < 20; i++) {
                GameItemView gameItemView = new GameItemView(mContext);
                gameItemView.setData(gameBeanList.getData().getList().get(i));
                game_ll.addView(gameItemView);
            }
//            game_ll.addView(new MoreItemView(mContext));
        }
    }

    @OnClick({R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5, R.id.item6, R.id.item7, R.id.item8})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item1:
                ChatRoomActivity.start(mContext);
                break;
            case R.id.item2:
                NearbyActivity.start(mContext);
                break;
            case R.id.item3:
                break;
            case R.id.item4:
                break;
            case R.id.item5:
                break;
            case R.id.item6:
                break;
            case R.id.item7:
                Intent intent = new Intent(mContext, LiveActivity.class);
                MySelfInfo.getInstance().setIdStatus(Constants.HOST);
                MySelfInfo.getInstance().setJoinRoomWay(true);
                L.i("333", "id：" + MySelfInfo.getInstance().getId());
                L.i("333", "roomNum：" + MySelfInfo.getInstance().getMyRoomNum());
                CurLiveInfo.setTitle("直播间");
                CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
                CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
                mContext.startActivity(intent);
//                startActivity(new Intent(getActivity(), DemoGuest.class));
                break;
            case R.id.item8:
                break;

        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 广告
                case HOME_AD_RESULT:
                    mSwitcherCount++;
                    String str = mAdvertisements[mSwitcherCount % mAdvertisements.length];
                    String[] strings = str.split(" ");
                    String name = strings[0];
                    int bstart = str.indexOf(name);
                    int bend = bstart + name.length();
                    SpannableStringBuilder style = new SpannableStringBuilder(str);
                    style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), bstart, bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    tv_notice.setText(style);
                    mHandler.sendEmptyMessageDelayed(HOME_AD_RESULT, 3000);
                    break;
            }

        }
    };

    public HunterHeaderView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public HunterHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public HunterHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.hunter_header, this);
        ButterKnife.bind(mRootView);
        tv_notice.setFactory(new ViewSwitcher.ViewFactory() {
            // 这里用来创建内部的视图，这里创建TextView，用来显示文字
            public View makeView() {
                TextView tv = new TextView(mContext);
                // 设置文字的显示单位以及文字的大小
                return tv;
            }
        });
        tv_notice.setInAnimation(mContext,
                R.anim.enter_bottom);
        tv_notice.setOutAnimation(mContext, R.anim.leave_top);
        mAdvertisements = new String[]{"aa 收到了游戏上分邀约", "bb 收到了游戏辅导邀约", "cc 收到了游戏上分邀约"};
        mHandler.sendEmptyMessage(HOME_AD_RESULT);
    }
}
