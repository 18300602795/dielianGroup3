package com.etsdk.app.huov7.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GiftImgAdapter;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.log.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class GiftView extends LinearLayout {
    private List<GridView> gridList = new ArrayList<>();
    List<Integer> gifts1 = new ArrayList<>();
    List<Integer> gifts2 = new ArrayList<>();
    List<Integer> gifts3 = new ArrayList<>();
    GridListener listener;
    LinearLayout gift_ll;
    View view;
    ViewPager viewPager;
    LinearLayout ll_dot;
    Context context;
    View rootView;

    public void setListener(GridListener listener) {
        this.listener = listener;
    }

    public GiftView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.window_gift, this);
        gift_ll = (LinearLayout) rootView.findViewById(R.id.gift_ll);
        view = LayoutInflater.from(context).inflate(R.layout.view_gift, null);
        viewPager = (ViewPager) view.findViewById(R.id.pager_gift);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        prepareEmoticon();
    }

    /**
     * 初始表情布局下底部圆点
     *
     * @param list
     */
    private void gotoInitData(List<GridView> list) {
        ll_dot.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            if (i == 0) {
                imageView.setImageResource(R.drawable.shape_dot_select);

            } else {
                imageView.setImageResource(R.drawable.shape_dot_nomal);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(StringUtils.dip2px(getContext(), 8),
                    StringUtils.dip2px(getContext(), 8));
            layoutParams.setMargins(20, 0, 0, 0);
            ll_dot.addView(imageView, layoutParams);
        }
        if (ll_dot.getChildCount() <= 1) {
            ll_dot.setVisibility(View.GONE);
        } else {
            ll_dot.setVisibility(VISIBLE);
        }
        viewPager.setOffscreenPageLimit(6);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ll_dot.getChildCount(); i++) {
                    if (i != position) {
                        ((ImageView) ll_dot.getChildAt(i)).setImageResource(R.drawable.shape_dot_nomal);
                    }
                }
                ((ImageView) ll_dot.getChildAt(position)).setImageResource(R.drawable.shape_dot_select);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void prepareEmoticon() {
        if (gift_ll == null) return;
        gridList.clear();
        addGrid();
        viewPager.setAdapter(new GiftAdapter(gridList));
        gotoInitData(gridList);
        gift_ll.addView(view);
    }

    private void addGrid() {
        getGift1();
        getGift2();
        getGift3();
        GridView gridView1 = new GridView(getContext());
        gridView1.setNumColumns(4);
        GiftImgAdapter adapter1 = new GiftImgAdapter(gifts1, context);
        gridView1.setAdapter(adapter1);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onclick(gifts1.get(position));
                }
            }
        });
        gridList.add(gridView1);

        GridView gridView2 = new GridView(getContext());
        gridView2.setNumColumns(4);
        GiftImgAdapter adapter2 = new GiftImgAdapter(gifts2, context);
        gridView2.setAdapter(adapter2);
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onclick(gifts2.get(position));
                }
            }
        });
        gridList.add(gridView2);

        GridView gridView3 = new GridView(getContext());
        gridView3.setNumColumns(4);
        GiftImgAdapter adapter3 = new GiftImgAdapter(gifts3, context);
        gridView3.setAdapter(adapter3);
        gridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onclick(gifts3.get(position));
                }
            }
        });
        gridList.add(gridView3);
    }

    private void getGift1(){
        gifts1.add(R.drawable.molive_icon_charm_lv_20);
        gifts1.add(R.drawable.molive_icon_charm_lv_21);
        gifts1.add(R.drawable.molive_icon_charm_lv_22);
        gifts1.add(R.drawable.molive_icon_charm_lv_23);
        gifts1.add(R.drawable.molive_icon_charm_lv_24);
        gifts1.add(R.drawable.molive_icon_charm_lv_25);
        gifts1.add(R.drawable.molive_icon_charm_lv_26);
        gifts1.add(R.drawable.molive_icon_charm_lv_27);
    }

    private void getGift2(){
        gifts2.add(R.drawable.ml_w_lv_s01);
        gifts2.add(R.drawable.ml_w_lv_s02);
        gifts2.add(R.drawable.ml_w_lv_s03);
        gifts2.add(R.drawable.ml_w_lv_s04);
        gifts2.add(R.drawable.ml_w_lv_s05);
        gifts2.add(R.drawable.ml_w_lv_s06);
        gifts2.add(R.drawable.ml_w_lv_s07);
        gifts2.add(R.drawable.ml_w_lv_s08);
    }

    private void getGift3(){
        gifts3.add(R.drawable.molive_icon_wealth_lv_30);
        gifts3.add(R.drawable.molive_icon_wealth_lv_31);
        gifts3.add(R.drawable.molive_icon_wealth_lv_32);
        gifts3.add(R.drawable.molive_icon_wealth_lv_33);
        gifts3.add(R.drawable.molive_icon_wealth_lv_34);
        gifts3.add(R.drawable.molive_icon_wealth_lv_35);
        gifts3.add(R.drawable.molive_icon_wealth_lv_36);
        gifts3.add(R.drawable.molive_icon_wealth_lv_37);
    }

    /**
     * 表情适配器
     */
    public class GiftAdapter extends PagerAdapter {
        private List<GridView> list;

        public GiftAdapter(List<GridView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((GridView) object);
        }

        @Override
        public GridView instantiateItem(ViewGroup container, int position) {
            GridView GridView = list.get(position);
            container.addView(GridView);
            return GridView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public interface GridListener {
        void onclick(int res);
    }
}
