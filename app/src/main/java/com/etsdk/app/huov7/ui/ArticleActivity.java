package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ArticleListAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.model.ArticleList;
import com.etsdk.app.huov7.provider.ArticleListItemViewProvider;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class ArticleActivity extends ImmerseActivity {
    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.nodate_ll)
    LinearLayout nodate_ll;
    @BindView(R.id.nodate_tv)
    TextView nodate_tv;
    int currentPage = 1;
    ArticleListAdapter articleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("我的帖子");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        articleListAdapter = new ArticleListAdapter(mContext, new ArrayList<ArticleBean>());
        recyclerView.setAdapter(articleListAdapter);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                getPageData();
            }

            @Override
            public void onLoadMore() {
                currentPage += 1;
                getPageData();
            }
        });
        getPageData();
    }


    @OnClick({R.id.iv_titleLeft, R.id.nodate_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.nodate_ll:
                getPageData();
                break;
        }
    }

    public void getPageData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.myListApi);
        httpParams.put("page", currentPage);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        L.e("333", "url：" + AppApi.getUrl(AppApi.myListApi));
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.myListApi), new HttpJsonCallBackDialog<ArticleList>() {
            @Override
            public void onDataSuccess(ArticleList data) {
                L.e("333", "data：" + data.getData().size());
                recyclerView.refreshComplete();
                if (data != null && data.getData() != null && data.getData() != null) {
                    if (currentPage == 1) {
                        articleListAdapter.upDate(data.getData());
                    } else {
                        articleListAdapter.addDate(data.getData());
                    }

                } else {
                    if (currentPage == 1) {
                        nodate_ll.setVisibility(View.VISIBLE);
                        nodate_tv.setText("还没有发布过内容");
                    } else {
                        recyclerView.setNoMore(true);
                    }
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                recyclerView.refreshComplete();
                if (currentPage != 1) {
                    recyclerView.setNoMore(true);
                } else {
                    nodate_ll.setVisibility(View.VISIBLE);
                    nodate_tv.setText("还没有发布过内容");
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                L.e("333", "errorNo：" + errorNo + "strMsg：" + strMsg + "completionInfo：" + completionInfo);
                recyclerView.refreshComplete();
                if (currentPage != 1) {
                    recyclerView.setNoMore(true);
                } else {
                    nodate_ll.setVisibility(View.VISIBLE);
                    nodate_tv.setText("连接失败，请检查网络");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, ArticleActivity.class);
        context.startActivity(starter);
    }
}
