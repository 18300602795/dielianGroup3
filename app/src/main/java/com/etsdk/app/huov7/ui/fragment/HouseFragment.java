package com.etsdk.app.huov7.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.model.ArticleList;
import com.etsdk.app.huov7.provider.ArticleListItemViewProvider;
import com.etsdk.app.huov7.ui.PostedActivity;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * Created by Administrator on 2018\2\22 0022.
 */

public class HouseFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView fragment_recycle;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    Items items = new Items();
    BaseRefreshLayout baseRefreshLayout;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_travels);
        initDate();
    }

    private void initDate() {
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        fragment_recycle.setLayoutManager(new LinearLayoutManager(mContext));
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(ArticleBean.class, new ArticleListItemViewProvider(multiTypeAdapter));
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startActivity(new Intent(getActivity(), PostedActivity.class));
                break;
        }
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.postListApi);
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.postListApi), new HttpJsonCallBackDialog<ArticleList>() {
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
    protected void onResumeLazy() {
        super.onResumeLazy();
        if (baseRefreshLayout != null) {
            baseRefreshLayout.refresh();
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }
}
