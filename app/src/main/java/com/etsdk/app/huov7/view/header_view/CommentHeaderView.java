package com.etsdk.app.huov7.view.header_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ImageAdapter;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.model.Dianzan;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.app.huov7.util.TimeUtils;
import com.game.sdk.SdkConstant;
import com.liang530.log.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\2\22 0022.
 */

public class CommentHeaderView extends RelativeLayout {
    View mRootView;
    private Context mContext;
    private ImageAdapter adapter;
    private RecyclerView img_recycle;
    private TextView like_num;
    private ArticleBean argumentBean;
    private String[] imgs;
    private TextView name_tv, read_tv, tower_tv, time_tv, title_tv, cont_tv;
    private LinearLayout like_ll;
    private LinearLayout praise_bg;
    private ImageView head_img, praise_img;
    private List<String> names;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initDate();
                    break;
                case 2:
//                    ToastUtils.showShort(mContext, (String) msg.obj);
                    break;
            }
        }
    };


    public CommentHeaderView(Context context, ArticleBean argumentBean) {
        super(context);
        this.mContext = context;
        this.argumentBean = argumentBean;
        initView();
        initDate();
    }

    public CommentHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        names = new ArrayList<>();
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.comment_header, this);
        name_tv = mRootView.findViewById(R.id.name_tv);
        read_tv = mRootView.findViewById(R.id.read_tv);
        time_tv = mRootView.findViewById(R.id.time_tv);
        title_tv = mRootView.findViewById(R.id.title_tv);
        cont_tv = mRootView.findViewById(R.id.cont_tv);
        praise_bg = mRootView.findViewById(R.id.praise_bg);
        head_img = mRootView.findViewById(R.id.head_img);
        praise_img = mRootView.findViewById(R.id.praise_img);
        like_ll = mRootView.findViewById(R.id.like_ll);

        img_recycle = mRootView.findViewById(R.id.img_recycle);
        like_num = mRootView.findViewById(R.id.like_num);
        img_recycle.setLayoutManager(new LinearLayoutManager(mContext));
        if (!StringUtils.isEmpty(argumentBean.getImage_url())) {
            String pictures = argumentBean.getImage_url();
            L.i("333", "pictures：" + pictures);
            imgs = pictures.split(",");
        }
        adapter = new ImageAdapter(mContext, imgs);
        img_recycle.setAdapter(adapter);
    }

    private void initDate() {
        praise_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                praise();
            }
        });
        name_tv.setText(argumentBean.getNickname());
        time_tv.setText(TimeUtils.getTime(Long.valueOf(argumentBean.getPublish_time())));
        title_tv.setText(argumentBean.getLike_number());
        cont_tv.setText(argumentBean.getContents());
        Glide.with(mContext).load(SdkConstant.BASE_URL + argumentBean.getPortrait()).placeholder(R.mipmap.ic_launcher).into(head_img);
        if (argumentBean.getP_status() != null && argumentBean.getP_status().equals("1")) {
            praise_img.setImageResource(R.mipmap.praise_1215);
            praise_bg.setBackgroundResource(R.drawable.like_bg_select);
        } else {
            praise_img.setImageResource(R.mipmap.unpraise_1215);
            praise_bg.setBackgroundResource(R.drawable.like_bg_normal);
        }
//        LogUtils.i("点赞数：" + names.size());
        setLikes2();
    }

    private void praise() {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("argument_id", argumentBean.getId());
//        map.put("user_id", MyApplication.infoBean.getId());
//        LogUtils.i("praise：" + StringUtils.getCompUrlFromParams(UrlUtils.LIKE, map));
//        OkHttpUtil.postdata(UrlUtils.LIKE, map, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtils.e("e：" + e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String res = response.body().string().trim();
//                LogUtils.e("获取用户详情：" + res);
//                try {
//                    parseJson(res);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private void parseJson(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        Message message = new Message();
        int code = jsonObject.getInt("code");
        if (code == 200) {
            message.what = 1;
            if (argumentBean.getP_status() != null && argumentBean.getP_status().equals("1")) {
//                LogUtils.i("取消点赞");
                argumentBean.setP_status("2");
                argumentBean.setLike_number(String.valueOf(Integer.valueOf(argumentBean.getLike_number()) + 1));
//                names.remove(MyApplication.infoBean.getNickname());
            } else {
//                LogUtils.i("点赞");
                argumentBean.setP_status("1");
//                names.add(MyApplication.infoBean.getNickname());
            }
            handler.sendMessage(message);
        } else {
            String msg = jsonObject.getString("msg");
            message.what = 2;
            message.obj = msg;
            handler.sendMessage(message);
        }
    }

    public void setLikes(List<Dianzan> dianzens) {
        this.names.clear();
        if (dianzens.size() != 0) {
            like_ll.setVisibility(VISIBLE);
            String num = "";
            for (int i = 0; i < dianzens.size(); i++) {
                this.names.add(dianzens.get(i).getNickname());
                if (i == dianzens.size() - 1) {
                    num += dianzens.get(i).getNickname();
                } else {
                    num += dianzens.get(i).getNickname() + ",";
                }
            }
            like_num.setText(num + "等" + dianzens.size() + "人觉得很赞");
        } else {
            like_ll.setVisibility(GONE);
        }

    }

    public void setLikes2() {
//        LogUtils.i("zan：" + names);
        if (names.size() != 0) {
            like_ll.setVisibility(VISIBLE);
            String num = "";
            for (int i = 0; i < names.size(); i++) {
                if (i == names.size() - 1) {
                    num += names.get(i);
                } else {
                    num += names.get(i) + ",";
                }
            }
            like_num.setText(num + "等" + names.size() + "人觉得很赞");
        } else {
            like_ll.setVisibility(GONE);
        }

    }


}
