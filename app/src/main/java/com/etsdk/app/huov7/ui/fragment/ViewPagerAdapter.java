package com.etsdk.app.huov7.ui.fragment;

/**
 * Created by Administrator on 2017/11/14.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.ui.CouponDetailActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.GiftDetailActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.util.ImgUtil;

import java.util.List;

/**
 * head1图片轮播适配器
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<AdImage> listbean;// 装载着轮播图的信息
    private Context context;

    public ViewPagerAdapter(List<AdImage> listbean, Context context) {
        this.listbean = listbean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listbean == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container,
                                  final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_img, null);
        ImageView img = (ImageView) view.findViewById(R.id.img_header);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                StringUtils.dip2px(container.getContext(), 300), StringUtils.dip2px(container.getContext(), 160));
//        img.setLayoutParams(layoutParams);
//        img.setScaleType(ImageView.ScaleType.FIT_XY);
        final AdImage listBean = listbean.get(position % listbean.size());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(listBean.getType())) {
                    WebViewActivity.start(v.getContext(), "", listBean.getUrl());
                } else if ("2".equals(listBean.getType())) {
                    GameDetailV2Activity.start(v.getContext(), listBean.getTarget() + "");
                } else if ("3".equals(listBean.getType())) {
                    GiftDetailActivity.start(v.getContext(), listBean.getTarget() + "");
                } else if ("4".equals(listBean.getType())) {
                    CouponDetailActivity.start(v.getContext(), listBean.getTarget() + "");
                }
            }
        });
        ImgUtil.setImg(context, listBean.getImage(), R.mipmap.gg, img);
        container.addView(view);
        return view;
    }


}
