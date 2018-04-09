package com.etsdk.app.huov7.adapter;

/**
 * Created by Administrator on 2017/11/14.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.liang530.fragment.BaseFragment;

import java.util.List;

public class ShowImgAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public ShowImgAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }


    public ShowImgAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
