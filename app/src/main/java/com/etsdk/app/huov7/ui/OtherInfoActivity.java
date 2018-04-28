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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ArticleListAdapter;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.chat.ui.ChatActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.model.ArticleList;
import com.etsdk.app.huov7.model.BackBean;
import com.etsdk.app.huov7.model.MemberList;
import com.etsdk.app.huov7.model.MemberModel;
import com.etsdk.app.huov7.model.OtherBean;
import com.etsdk.app.huov7.provider.ArticleListItemViewProvider;
import com.etsdk.app.huov7.util.ImgUtil;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.SdkConstant;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.views.imageview.CircleImageView;
import com.tencent.TIMConversationType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static android.R.attr.data;

public class OtherInfoActivity extends ImmerseActivity {
    @BindView(R.id.group_icon)
    CircleImageView group_icon;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.sign_tv)
    TextView sign_tv;
    @BindView(R.id.nodate_ll)
    LinearLayout nodate_ll;
    @BindView(R.id.nodate_tv)
    TextView nodate_tv;

    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    String userName;
    int currentPage = 1;
    MemberModel memberModel;
    ArticleListAdapter articleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        userName = getIntent().getStringExtra("name");
        tvTitleName.setText("Ta的信息");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        articleListAdapter = new ArticleListAdapter(mContext, new ArrayList<ArticleBean>());
        recyclerView.setAdapter(articleListAdapter);
        getModel();
    }

    public void getModel() {
        final OtherBean otherBean = new OtherBean();
        otherBean.setUsername(userName);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(otherBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<MemberModel>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(MemberModel data) {
                if (data != null) {
                    nodate_ll.setVisibility(View.GONE);
                    memberModel = data;
                    name_tv.setText(memberModel.getNickname());
//                    SdkConstant.BASE_URL +
                    ImgUtil.setImg(mContext, memberModel.getPortrait(), R.drawable.bg_game, group_icon);
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
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                AileApplication.groupId = "";
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.memsdetail), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @OnClick({R.id.iv_titleLeft, R.id.chat_tv, R.id.nodate_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.nodate_ll:
                getModel();
                break;
            case R.id.chat_tv:
                if (memberModel != null)
                    ChatActivity.navToChat(mContext, "fx" + memberModel.getUsername(), TIMConversationType.C2C);
                break;
        }
    }

    public void getPageData() {
        nodate_ll.setVisibility(View.GONE);
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.myListApi);
        httpParams.put("page", currentPage);
        httpParams.put("mem_id", memberModel.getMem_id());
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
                        nodate_tv.setText("Ta还没有发布过内容");
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
                    nodate_tv.setText("Ta还没有发布过内容");
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


    public static void start(Context context, String userName) {
        Intent starter = new Intent(context, OtherInfoActivity.class);
        starter.putExtra("name", userName);
        context.startActivity(starter);
    }
}
