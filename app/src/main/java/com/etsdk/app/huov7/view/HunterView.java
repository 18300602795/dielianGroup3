package com.etsdk.app.huov7.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.etsdk.app.huov7.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018\2\22 0022.
 */

public class HunterView extends RelativeLayout {
    View mRootView;
    private Context mContext;
    @BindView(R.id.game_ll)
    LinearLayout game_ll;


    public void setGameBeanList() {
        game_ll.removeAllViews();
        GirlItemView gameItemView1 = new GirlItemView(mContext);
        gameItemView1.setData(R.drawable.girl1);
        game_ll.addView(gameItemView1);

        GirlItemView gameItemView2 = new GirlItemView(mContext);
        gameItemView2.setData(R.drawable.girl2);
        game_ll.addView(gameItemView2);

        GirlItemView gameItemView3 = new GirlItemView(mContext);
        gameItemView3.setData(R.drawable.girl3);
        game_ll.addView(gameItemView3);

        GirlItemView gameItemView4 = new GirlItemView(mContext);
        gameItemView4.setData(R.drawable.girl4);
        game_ll.addView(gameItemView4);

        GirlItemView gameItemView5 = new GirlItemView(mContext);
        gameItemView5.setData(R.drawable.girl5);
        game_ll.addView(gameItemView5);

        GirlItemView gameItemView6 = new GirlItemView(mContext);
        gameItemView6.setData(R.drawable.girl6);
        game_ll.addView(gameItemView6);

        GirlItemView gameItemView7 = new GirlItemView(mContext);
        gameItemView7.setData(R.drawable.girl7);
        game_ll.addView(gameItemView7);

        GirlItemView gameItemView8 = new GirlItemView(mContext);
        gameItemView8.setData(R.drawable.girl8);
        game_ll.addView(gameItemView8);

        GirlItemView gameItemView9 = new GirlItemView(mContext);
        gameItemView9.setData(R.drawable.girl9);
        game_ll.addView(gameItemView9);

    }


    public HunterView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public HunterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public HunterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.view_hunter, this);
        ButterKnife.bind(mRootView);

    }
}
