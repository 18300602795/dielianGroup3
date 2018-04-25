package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.util.ImgUtil;
import com.liang530.views.convenientbanner.holder.Holder;

public class NetImageHolderView implements Holder<AdImage> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, AdImage banner) {
            ImgUtil.setImg(context, banner.getImage(), R.mipmap.gg, imageView);
            //TODO 得到bitmap可用于颜色取样
//            Glide.with(context).load(banner.getImage()).asBitmap().error(R.mipmap.gg)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            imageView.setImageBitmap(resource);
//                        }
//                    });
        }
    }