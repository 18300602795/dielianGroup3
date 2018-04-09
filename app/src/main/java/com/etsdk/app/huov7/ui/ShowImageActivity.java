package com.etsdk.app.huov7.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ShowImgAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.fragment.ShowImgFragment;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.liang530.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018\3\1 0001.
 */

public class ShowImageActivity extends ImmerseActivity {
    @BindView(R.id.show_pager)
    ViewPager show_pager;
    @BindView(R.id.num_tv)
    TextView num_tv;
    ShowImgAdapter imgAdapter;
    List<BaseFragment> fragments;
    ArrayList<String> imgs;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_img);
        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        imgs = getIntent().getStringArrayListExtra("imgs");
        Log.i("333", "imgs： " + imgs);
//        num_tv = findViewById(R.id.num_tv);
        if (imgs.size() == 1) {
            num_tv.setVisibility(View.GONE);
        }
        String num = "1/" + imgs.size();
        num_tv.setText(num);
//        show_pager = findViewById(R.id.show_pager);
        for (int i = 0; i < imgs.size(); i++) {
            ShowImgFragment imgFragment = new ShowImgFragment();
            fragments.add(imgFragment);
            imgFragment.showImg(imgs.get(i));
        }
        imgAdapter = new ShowImgAdapter(getSupportFragmentManager(), fragments);
        show_pager.setAdapter(imgAdapter);
        show_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String num = (position + 1) + "/" + imgs.size();
                num_tv.setText(num);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
