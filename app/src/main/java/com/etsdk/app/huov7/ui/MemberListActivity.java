package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.MemberListItemViewAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.MemberList;
import com.etsdk.app.huov7.model.MemberModel;
import com.game.sdk.log.T;
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

public class MemberListActivity extends ImmerseActivity {

    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    int currentPage = 1;
    MemberListItemViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("成员列表");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MemberListItemViewAdapter(mContext, new ArrayList<MemberModel>());
        recyclerView.setAdapter(adapter);
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


    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    public void getPageData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.memslist);
        httpParams.put("page", currentPage);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        L.e("333", "url：" + AppApi.getUrl(AppApi.memslist));
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.memslist), new HttpJsonCallBackDialog<MemberList>() {
            @Override
            public void onDataSuccess(MemberList data) {
                recyclerView.refreshComplete();
                L.e("333", "data：" + data.getData().size());
                if (data != null && data.getData() != null) {
                    if (currentPage == 1) {
                        adapter.upDate(data.getData());
                    } else {
                        adapter.addDate(data.getData());
                    }
                } else {
                    if (currentPage == 1) {
                        T.s(mContext, "");
                    } else {
                        recyclerView.setNoMore(true);
                    }
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                recyclerView.refreshComplete();
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                L.e("333", "code：" + errorNo + "msg：" + strMsg + "data：" + completionInfo);
                recyclerView.refreshComplete();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, MemberListActivity.class);
        context.startActivity(starter);
    }
}
