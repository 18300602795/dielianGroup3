package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GuildAdapter;
import com.etsdk.app.huov7.adapter.GuildAdapter2;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.view.HunterHeaderView;
import com.game.sdk.log.T;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

import static com.etsdk.app.huov7.R.id.recyclerView;


/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildFragment extends AutoLazyFragment {
    @BindView(recyclerView)
    XRecyclerView fragment_recycle;
    private int isnew = 0;//	否	INT	是否新游 2 新游 1 普通 0 所有 20170113新增
    private int remd = 0;//	否	INT	是否新游 2 推荐 1 普通 0 所有 20170113新增
    private int server = 0;//	否	INT	是否新游 2 开服游戏 1 普通 0 所有 20170113新增
    private int test = 0;//	否	INT	是否新游 2 开测游戏 1 普通 0 所有 20170113新增
    private int hot = 0;
    private int category = 3;   //INT	是否单机 2 网游 1 GM 3 BT 4 折扣 5 精品 0 所有 20170113新增
    private String type;//分类id
    private GuildAdapter2 adapter;
    int currentPage = 1;
    HunterHeaderView headerView;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_xrecycle);
        setupUI();
    }

    private void setupUI() {
        fragment_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new GuildAdapter2(new ArrayList<GameBean>(), getActivity());
        headerView = new HunterHeaderView(getActivity());
        headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        headerAndFooterWrapper.addHeaderView(headerView);
        mLoadMoreWrapper = new LoadMoreWrapper(headerAndFooterWrapper);
        fragment_recycle.setAdapter(mLoadMoreWrapper);
        fragment_recycle.setLoadingListener(new XRecyclerView.LoadingListener() {
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

    public void getPageData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("hot", hot);
        httpParams.put("isnew", isnew);
        httpParams.put("remd", remd);
        httpParams.put("server", server);
        httpParams.put("test", test);
        httpParams.put("category", category);
        if (type != null) {
            httpParams.put("type", type);
        }
        httpParams.put("page", currentPage);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi), new HttpJsonCallBackDialog<GameBeanList>() {
            @Override
            public void onDataSuccess(GameBeanList data) {
                fragment_recycle.refreshComplete();
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    if (currentPage == 1) {
                        adapter.upDate(data.getData().getList());
                        headerView.setGameBeanList(data);
                    } else {
                        adapter.addDate(data.getData().getList());
                    }
                } else {
                    if (currentPage == 1) {
                        T.s(getActivity(), "");
                    } else {
                        fragment_recycle.setNoMore(true);
                    }
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                if (fragment_recycle != null)
                    fragment_recycle.refreshComplete();
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                if (fragment_recycle != null)
                    fragment_recycle.refreshComplete();
            }
        });
    }

//    @Override
//    public void getPageData(final int requestPageNo) {
//        if (requestPageNo != 1) {
//            Items resultItems = new Items();
//            getPageData(requestPageNo, resultItems);
//            return;
//        }
//        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.union);
//        //成功，失败，null数据
//        L.e("333", "url：" + AppApi.getUrl(AppApi.union));
//        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.union), new HttpJsonCallBackDialog<String>() {
//            @Override
//            public void onDataSuccess(String data) {
//                L.e("333", "data：" + data);
//            }
//
//            @Override
//            public void onJsonSuccess(int code, String msg, String data) {
//                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
//                try {
//                    JSONObject jsonObject = new JSONObject(data);
//                    int code2 = jsonObject.getInt("code");
//                    String data2 = jsonObject.getString("data");
//                    L.e("333", "data2：" + data2);
//                    GuildHeader header = JsonUtil.parse(data2, GuildHeader.class);
//                    L.e("333", "name：" + header.getGuild().get(0).getName());
//                    AileApplication.groupId = header.getGuild().get(0).getId();
//                    if (code2 == 200) {
//                        Items resultItems = new Items();
//                        resultItems.add(header);
//                        baseRefreshLayout.resultLoadData(items, resultItems, 1);
//                        getPageData(requestPageNo, resultItems);
//                    } else {
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Items resultItems = new Items();
//                    resultItems.add(new GuildHeader());
//                    baseRefreshLayout.resultLoadData(items, resultItems, 1);
//                    getPageData(requestPageNo, resultItems);
//                }
//
//            }
//
//            @Override
//            public void onFailure(int errorNo, String strMsg, String completionInfo) {
//                L.e("333", "code：" + errorNo + "msg：" + strMsg + "data：" + completionInfo);
//            }
//
//        });
//    }


    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }

}
