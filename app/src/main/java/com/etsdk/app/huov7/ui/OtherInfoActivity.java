package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.model.ArticleList;
import com.etsdk.app.huov7.provider.ArticleListItemViewProvider;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.SdkConstant;
import com.hyphenate.easeui.model.user.MemberModel;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.views.imageview.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class OtherInfoActivity extends ImmerseActivity implements AdvRefreshListener {
    @BindView(R.id.group_icon)
    CircleImageView group_icon;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.sign_tv)
    TextView sign_tv;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items = new Items();
    private MemberModel memberModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        memberModel = (MemberModel) getIntent().getSerializableExtra("member");
        tvTitleName.setText("Ta的信息");
        Log.i("333", "name：" + memberModel.getUsername());
        name_tv.setText(memberModel.getNickname());
        Glide.with(mContext).load(SdkConstant.BASE_URL + memberModel.getPortrait()).placeholder(R.drawable.bg_game).into(group_icon);
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(ArticleBean.class, new ArticleListItemViewProvider(multiTypeAdapter));
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }


    @OnClick({R.id.iv_titleLeft, R.id.chat_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.chat_tv:
                break;
        }
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.myListApi);
        httpParams.put("page", requestPageNo);
        httpParams.put("mem_id", memberModel.getMem_id());
        httpParams.put("offset", 20);
        //成功，失败，null数据
        L.e("333", "url：" + AppApi.getUrl(AppApi.myListApi));
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.myListApi), new HttpJsonCallBackDialog<ArticleList>() {
            @Override
            public void onDataSuccess(ArticleList data) {
                L.e("333", "data：" + data.getData().size());
                if (data != null && data.getData() != null) {
                    Items resultItems = new Items();
                    resultItems.addAll(data.getData());
                    baseRefreshLayout.resultLoadData(items,resultItems, null);
                } else {
                    baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                baseRefreshLayout.resultLoadData(items, null, null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                L.e("333", "errorNo：" + errorNo + "strMsg：" + strMsg + "completionInfo：" + completionInfo);
                baseRefreshLayout.resultLoadData(items, null, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public static void start(Context context, MemberModel memberModel) {
        Intent starter = new Intent(context, OtherInfoActivity.class);
        starter.putExtra("member", memberModel);
        context.startActivity(starter);
    }
}
