package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.HomePage1Data;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.SearchActivity;
import com.etsdk.app.huov7.util.StringUtils;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\2\28 0028.
 */

public class HomeFragment2 extends AutoLazyFragment {
    @BindView(R.id.fragment_pager)
    ViewPager mViewPager;
    List<Fragment> fragments;
    FragmentPagerAdapter mAdapter;
    @BindView(R.id.ll_dots)
    LinearLayout ll_dots;
    @BindView(R.id.hunter_pager)
    ViewPager hunter_pager;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    private ViewPagerAdapter pagerAdapter;
    private List<AdImage> imgs;
    private List<View> mDots = new ArrayList<>();// 存放圆点视图的集合

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_home2);
        initDate();
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int height = Math.abs(verticalOffset);
                if (height > StringUtils.dip2px(mContext, 100)) {
                    height = StringUtils.dip2px(mContext, 100);
                }
                float alpha = Float.valueOf(height) / Float.valueOf(StringUtils.dip2px(mContext, 100));
                title_tv.setAlpha(alpha);
            }
        });
    }

    private void initDate() {
        fragments = new ArrayList<>();
        fragments.add(new GuildFragment());
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        imgs = new ArrayList<>();
        hunter_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = mDots.size();
                for (int i = 0; i < count; i++) {
                    if (position % imgs.size() == i) {
                        mDots.get(i).setBackgroundResource(
                                R.mipmap.dot_focus);
                    } else {
                        mDots.get(i).setBackgroundResource(
                                R.mipmap.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                abc.removeCallbacksAndMessages(null);
                abc.sendEmptyMessageDelayed(0, 4000);
            }
        });
        getPageData();
    }

    public void getPageData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.hompageApi);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.hompageApi), new HttpJsonCallBackDialog<HomePage1Data>() {
            @Override
            public void onDataSuccess(HomePage1Data data) {
                if (data != null && data.getData() != null) {
                    imgs = data.getData().getHometopper().getList();
                    showBanner();
                    startScrollView();
                } else {

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

    @OnClick({R.id.title_search, R.id.title_menu, R.id.title_bar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_search:
                SearchActivity.start(mContext);
                break;
            case R.id.title_menu:
                DownloadManagerActivity.start(mContext);
                break;
        }
    }


    Handler abc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = hunter_pager.getCurrentItem();
            hunter_pager.setCurrentItem(currentItem + 1);
        }
    };


    /**
     * 开始轮播图片
     */
    private void startScrollView() {
        if (pagerAdapter == null) {
            pagerAdapter = new ViewPagerAdapter(imgs, getContext());
            hunter_pager.setAdapter(pagerAdapter);
            hunter_pager.setCurrentItem(10000 * imgs.size());
        } else {
            pagerAdapter.notifyDataSetChanged();
        }
        // 实现轮播效果
        abc.sendEmptyMessageDelayed(0, 4000);
    }

    /**
     * 显示广告条
     */
    private void showBanner() {
        // 创建ViewPager对应的点
        ll_dots.removeAllViews();
        mDots.clear();
        for (int i = 0; i < imgs.size(); i++) {
            View dot = new View(getActivity());
            int size = StringUtils.dip2px(getActivity(), 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    size, size);
            params.leftMargin = size;
            if (i == 0) {
                dot.setBackgroundResource(R.mipmap.dot_focus);// 默认选择第1个点
            } else {
                dot.setBackgroundResource(R.mipmap.dot_normal);
            }
            dot.setLayoutParams(params);
            ll_dots.addView(dot);
            mDots.add(dot);
        }
    }


    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        abc.removeCallbacksAndMessages(null);
    }
}
