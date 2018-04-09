package com.etsdk.app.huov7.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ReplyAdapter2;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.Comment;
import com.etsdk.app.huov7.view.header_view.ReplyHeaderView;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\6 0006.
 */

public class ReplyActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tv_titleName;
    @BindView(R.id.comment_et)
    EditText comment_et;
    @BindView(R.id.item_recycle)
    RecyclerView item_recycle;
    ReplyAdapter2 adapter2;
    ReplyHeaderView headerView;
    HeaderAndFooterWrapper headerAndFooterWrapper;
    LoadMoreWrapper mLoadMoreWrapper;
    Comment comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ButterKnife.bind(this);
        initDate();
    }


    private void initDate() {
        comment = (Comment) getIntent().getSerializableExtra("cont");
        tv_titleName.setText(comment.getUname());
        adapter2 = new ReplyAdapter2(mContext, comment.getReply());
        item_recycle.setLayoutManager(new LinearLayoutManager(mContext));
        item_recycle.setAdapter(adapter2);
        headerView = new ReplyHeaderView(mContext);
        headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter2);
        headerAndFooterWrapper.addHeaderView(headerView);
        headerView.setData(comment);
        mLoadMoreWrapper = new LoadMoreWrapper(headerAndFooterWrapper);
        item_recycle.setAdapter(mLoadMoreWrapper);

    }

    @OnClick({R.id.iv_titleLeft, R.id.send_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.send_tv:
                sendComment();
                break;
        }
    }

    private void sendComment() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.addReplyApi);
        httpParams.put("article_id", comment.getArticle_id());
        httpParams.put("contents", comment_et.getText().toString());
        httpParams.put("id", comment.getId());
        httpParams.put("uid", comment.getUid());
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.addReplyApi), new HttpJsonCallBackDialog<String>() {
            @Override
            public void onDataSuccess(String data) {
                L.e("333", "data：" + data);
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int code2 = jsonObject.getInt("code");
                    if (code2 == 201) {
                        LoginActivity.start(mContext);
                        return;
                    }
                    if (code2 == 200) {
                        finish();
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
}
