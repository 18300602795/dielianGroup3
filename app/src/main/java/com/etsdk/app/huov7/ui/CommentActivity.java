package com.etsdk.app.huov7.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.model.ArticleBeans;
import com.etsdk.app.huov7.model.Comment;
import com.etsdk.app.huov7.provider.ArticleHeaderViewProvider;
import com.etsdk.app.huov7.provider.CommentListItemViewProvider;
import com.etsdk.app.huov7.util.JsonUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2018\3\5 0005.
 */

public class CommentActivity extends ImmerseActivity implements AdvRefreshListener {
    @BindView(R.id.tv_titleName)
    TextView tv_titleName;
    @BindView(R.id.iv_title_down)
    ImageView iv_title_down;
    @BindView(R.id.comment_et)
    EditText comment_et;
    @BindView(R.id.comment_tv)
    TextView comment_tv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    Items items = new Items();
    BaseRefreshLayout baseRefreshLayout;
    private MultiTypeAdapter multiTypeAdapter;
    ArticleBean argumentBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initDate();
    }


    private void initDate() {
        tv_titleName.setText("评论");
        argumentBean = (ArticleBean) getIntent().getSerializableExtra("argument");
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.applyGlobalMultiTypePool();
        multiTypeAdapter.register(ArticleBeans.class, new ArticleHeaderViewProvider(baseRefreshLayout));
        multiTypeAdapter.register(Comment.class, new CommentListItemViewProvider());
        multiTypeAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
        iv_title_down.setVisibility(View.VISIBLE);
        iv_title_down.setImageResource(R.mipmap.share);
        comment_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    comment_tv.setVisibility(View.GONE);
                } else {
                    comment_tv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.detailsListApi);
        httpParams.put("article_id", argumentBean.getArticle_id());
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.detailsListApi), new HttpJsonCallBackDialog<String>() {
            @Override
            public void onDataSuccess(String data) {
                L.e("333", "data：" + data);
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String data2 = jsonObject.getString("data");
                    L.e("333", "data2：" + data2);
                    ArticleBeans articleBeans = JsonUtil.parse(data2, ArticleBeans.class);
                    if (articleBeans != null && articleBeans.getCom() != null) {
                        int maxPage = (int) Math.ceil(articleBeans.getCom().size() / 20.);
                        Items resultItems = new Items();
                        resultItems.add(articleBeans);
                        resultItems.addAll(articleBeans.getCom());
                        baseRefreshLayout.resultLoadData(items, resultItems, maxPage);
                    } else if (articleBeans != null && articleBeans.getCom() == null) {
                        Items resultItems = new Items();
                        resultItems.add(articleBeans);
                        baseRefreshLayout.resultLoadData(items, resultItems, 1);
                    } else {
                        baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    L.e("333", "e：" + e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                L.e("333", "errorNo：" + errorNo + "strMsg：" + strMsg + "completionInfo：" + completionInfo);
                baseRefreshLayout.resultLoadData(items, null, null);
            }
        });
    }

    private void sendComment() {
        if (StringUtils.isEmpty(comment_et.getText().toString())) {
            T.s(mContext, "请输入评论内容");
            return;
        }
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.addCommentsApi);
        httpParams.put("article_id", argumentBean.getArticle_id());
        httpParams.put("contents", comment_et.getText().toString());
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.addCommentsApi), new HttpJsonCallBackDialog<String>() {
            @Override
            public void onDataSuccess(String data) {
                L.e("333", "data：" + data);
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                comment_et.setText("");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int code2 = jsonObject.getInt("code");
                    if (code2 == 201) {
                        LoginActivity.start(mContext);
                        return;
                    }
                    if (code2 == 200) {
                        baseRefreshLayout.refresh();
                    } else {
                        T.s(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                L.e("333", "errorNo：" + errorNo + "strMsg：" + strMsg + "completionInfo：" + completionInfo);
            }
        });
    }


    @OnClick({R.id.iv_titleLeft, R.id.iv_title_down, R.id.send_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_title_down:
                break;
            case R.id.send_tv:
                sendComment();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (baseRefreshLayout != null)
            baseRefreshLayout.refresh();
    }
}
