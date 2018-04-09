package com.etsdk.app.huov7.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\3\1 0001.
 */

public class ShowImgFragment extends AutoLazyFragment {
    @BindView(R.id.show_img)
    ImageView show_img;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    String img;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    show_img.setImageBitmap((Bitmap) msg.obj);
                    if (isVisible) {
                        progressBar.setVisibility(View.GONE);
                        show_img.setImageBitmap((Bitmap) msg.obj);
                    } else {
                        showImg((Bitmap) msg.obj);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.item_show_img);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(img).placeholder(R.mipmap.ic_launcher).into(show_img);
        show_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }


    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
        } else {
            isVisible = false;
        }
    }

    public void showImg(Bitmap bitmap) {
        Message message = new Message();
        message.obj = bitmap;
        message.what = 1;
        handler.sendMessage(message);
    }


    public void showImg(final String img) {
        this.img = img;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = PhotoUtils.getimage(img, 1920, 1080);
//                Message message = new Message();
//                message.obj = bitmap;
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        }).start();
    }

}
