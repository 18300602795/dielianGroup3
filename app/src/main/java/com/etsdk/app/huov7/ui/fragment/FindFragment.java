package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.HomePage1Data;
import com.etsdk.app.huov7.model.TjAdTop;
import com.etsdk.app.huov7.provider.NetImageHolderView;
import com.etsdk.app.huov7.shop.ui.MainShopActivity;
import com.etsdk.app.huov7.ui.CouponDetailActivity;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.EarnActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.GiftDetailActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.NewsListActivity;
import com.etsdk.app.huov7.ui.SearchActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.ui.dialog.CouponExchangeDialogUtil;
import com.game.sdk.log.L;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.views.convenientbanner.ConvenientBanner;
import com.liang530.views.convenientbanner.holder.CBViewHolderCreator;
import com.liang530.views.convenientbanner.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/7.
 */

public class FindFragment extends AutoLazyFragment {
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.main_gameSearch)
    TextView mainGameSearch;
    @BindView(R.id.iv_tj_downManager)
    ImageView ivTjDownManager;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_find);
        setupUI();
    }

    private void setupUI() {
        L.i("333", "轮播图");
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.hompageApi);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.hompageApi), new HttpJsonCallBackDialog<HomePage1Data>() {
            @Override
            public void onDataSuccess(HomePage1Data data) {
                if (data != null && data.getData() != null) {
                    updateHomeData(data);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
            }
        });
    }


    private void updateHomeData(HomePage1Data data) {
        final TjAdTop tjAdTop = new TjAdTop(data.getData().getHometopper().getList());
        convenientBanner.setPages(new CBViewHolderCreator<NetImageHolderView>() {
            @Override
            public NetImageHolderView createHolder() {
                return new NetImageHolderView();
            }
        }, tjAdTop.getBannerList())
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        AdImage adImage = tjAdTop.getBannerList().get(position);
                        if ("1".equals(adImage.getType())) {
                            WebViewActivity.start(getActivity(), "", adImage.getUrl());
                        } else if ("2".equals(adImage.getType())) {
                            GameDetailV2Activity.start(getActivity(), adImage.getTarget() + "");
                        } else if ("3".equals(adImage.getType())) {
                            GiftDetailActivity.start(getActivity(), adImage.getTarget() + "");
                        } else if ("4".equals(adImage.getType())) {
                            CouponDetailActivity.start(getActivity(), adImage.getTarget() + "");
                        }
                    }
                });
        if (!convenientBanner.isTurning()) {
            if (tjAdTop.getBannerList().size() > 1) {
                convenientBanner.startTurning(3000);
            } else {
                convenientBanner.stopTurning();
            }
        }
    }

    @OnClick({R.id.main_gameSearch, R.id.iv_tj_downManager, R.id.rl_goto_msg, R.id.shop_rl, R.id.earn_rl, R.id.deal_rl, R.id.apply_rl, R.id.news_rl, R.id.strategy_rl, R.id.event_rl, R.id.evaluation_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_gameSearch:
                SearchActivity.start(mContext);
                break;
            case R.id.iv_tj_downManager:
                DownloadManagerActivity.start(mContext);
                break;
            case R.id.rl_goto_msg:
                MessageActivity.start(mContext);
                break;
            case R.id.shop_rl:
                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.earn_rl:
                EarnActivity.start(mContext);
                break;
            case R.id.apply_rl:
                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.deal_rl:
                MainShopActivity.start(mContext);
                break;
            case R.id.news_rl:
                NewsListActivity.start(mContext, "1", "");
                break;
            case R.id.strategy_rl:
                NewsListActivity.start(mContext, "3", "");
                break;
            case R.id.event_rl:
                NewsListActivity.start(mContext, "2", "");
                break;
            case R.id.evaluation_rl:
                NewsListActivity.start(mContext, "5", "");
                break;
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyViewLazy();
    }

}
