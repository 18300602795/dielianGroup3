package com.etsdk.app.huov7.shop.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.view.ShopSelectTabView;
import com.liang530.views.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商城
 * 2017/6/8.
 */

public class MainShopActivity extends ImmerseActivity {
    @BindView(R.id.vp_main_shop)
    SViewPager vpMainShop;
    @BindView(R.id.select_tab)
    ShopSelectTabView selectTab;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    private MyPagerAdapter mAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop);
        ButterKnife.bind(this);
        setupUI();
    }


    private void setupUI() {
//        mainGameSearch.setText("输入想搜索的活动");
        tvTitleName.setText("账号交易");
        fragmentList.add(ShopListFragment.newInstance(ShopListFragment.TYPE_ALL));
        fragmentList.add(SellFragment.newInstance(SellFragment.MODE_PUBLISH, null, 0, null, null, null, null, null, 0, null, null, null));
        fragmentList.add(ShopListFragment.newInstance(ShopListFragment.TYPE_MY_BOUGHT));
        fragmentList.add(ShopListFragment.newInstance(ShopListFragment.TYPE_MY_SELL));
        fragmentList.add(ShopListFragment.newInstance(ShopListFragment.TYPE_MY_FAVOR));

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vpMainShop.setCanScroll(true);
        vpMainShop.setAdapter(mAdapter);
        vpMainShop.setOffscreenPageLimit(2);
        vpMainShop.setCanScroll(false);

        selectTab.setViewPager(vpMainShop);
    }


    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;

        }
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, MainShopActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
