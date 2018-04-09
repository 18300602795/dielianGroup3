package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.dialog.CouponExchangeDialogUtil;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.kymjs.rxvolley.RxVolley;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/8.
 */

public class MyWalletActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;

    @BindView(R.id.integral_tv)
    TextView integral_tv;
    @BindView(R.id.ptb_tv)
    TextView ptb_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        setupUI();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.default_gray), 0);
    }

    private void setupUI() {
        tvTitleName.setText("我的钱包");
        getUserInfoData();
    }

    @OnClick({R.id.iv_titleLeft, R.id.item_ll1, R.id.item_ll2, R.id.item_ll3, R.id.item_ll4, R.id.item_ll5, R.id.item_ll6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.item_ll1:
                new CouponExchangeDialogUtil().showExchangeDialog(mContext, "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.item_ll2:
                UserSpendRecordActivity.start(mContext);
                break;
            case R.id.item_ll3:
                UserChargeRecordActivity.start(mContext);
                break;
            case R.id.item_ll4:
                new CouponExchangeDialogUtil().showExchangeDialog(mContext, "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.item_ll5:
                new CouponExchangeDialogUtil().showExchangeDialog(mContext, "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.item_ll6:
                new CouponExchangeDialogUtil().showExchangeDialog(mContext, "友情提示", "该功能正在开发中，敬请期待");
                break;
        }
    }

    private void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    updateUserInfoData(data);
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

    private void updateUserInfoData(UserInfoResultBean userInfoResultBean) {
        integral_tv.setText(userInfoResultBean.getMyintegral() + "");
        ptb_tv.setText(userInfoResultBean.getPtbcnt() + "");
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MyWalletActivity.class);
        context.startActivity(starter);
    }
}
