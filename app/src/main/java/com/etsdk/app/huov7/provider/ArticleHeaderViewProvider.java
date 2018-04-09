package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ImageAdapter;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBeans;
import com.etsdk.app.huov7.model.Dianzan;
import com.etsdk.app.huov7.ui.LoginActivityV1;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.app.huov7.util.TimeUtils;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.game.sdk.SdkConstant;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/5.
 */
public class ArticleHeaderViewProvider
        extends ItemViewProvider<ArticleBeans, ArticleHeaderViewProvider.ViewHolder> {
    BaseRefreshLayout baseRefreshLayout;
    Context context;
    private String[] imgs;

    public ArticleHeaderViewProvider(BaseRefreshLayout baseRefreshLayout) {
        this.baseRefreshLayout = baseRefreshLayout;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.comment_header, parent, false);
        context = parent.getContext();
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final ArticleBeans articleBeans) {
        holder.img_recycle.setLayoutManager(new LinearLayoutManager(holder.context));
        if (!StringUtils.isEmpty(articleBeans.getImage_url())) {
            String pictures = articleBeans.getImage_url();
            L.i("333", "pictures：" + pictures);
            imgs = pictures.split(",");
            if (holder.adapter == null){
                holder.adapter = new ImageAdapter(holder.context, imgs);
                holder.img_recycle.setAdapter(holder.adapter);
            }
        }
        holder.praise_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.addPraiseApi);
                httpParams.put("article_id", articleBeans.getArticle_id());
                //成功，失败，null数据
                NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.addPraiseApi), new HttpJsonCallBackDialog<String>() {
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
                                LoginActivityV1.start(context);
                                return;
                            }
                            if (code2 == 200) {
                                baseRefreshLayout.refresh();
                            } else {
                                T.s(holder.context, msg);
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
        });
        holder.name_tv.setText(articleBeans.getNickname());
        holder.time_tv.setText(TimeUtils.getTime(Long.valueOf(articleBeans.getPublish_time())));
        holder.title_tv.setText(articleBeans.getTitle());
        holder.cont_tv.setText(articleBeans.getContents());
        Glide.with(holder.context).load(SdkConstant.BASE_URL + articleBeans.getPortrait()).placeholder(R.mipmap.ic_launcher).into(holder.head_img);
        if (articleBeans.getP_status() != null && articleBeans.getP_status().equals("1")) {
            holder.praise_img.setImageResource(R.mipmap.praise_1215);
            holder.praise_bg.setBackgroundResource(R.drawable.like_bg_select);
        } else {
            holder.praise_img.setImageResource(R.mipmap.unpraise_1215);
            holder.praise_bg.setBackgroundResource(R.drawable.like_bg_normal);
        }
        List<Dianzan> dianzens = articleBeans.getPraise();
        if (dianzens.size() != 0) {
            ArrayList<String> names = new ArrayList<>();
            holder.like_ll.setVisibility(View.VISIBLE);
            String num = "";
            for (int i = 0; i < dianzens.size(); i++) {
                names.add(dianzens.get(i).getNickname());
                if (i == dianzens.size() - 1) {
                    num += dianzens.get(i).getNickname();
                } else {
                    num += dianzens.get(i).getNickname() + ",";
                }
            }
            holder.like_num.setText(num + "等" + dianzens.size() + "人觉得很赞");
        } else {
            holder.like_ll.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_recycle)
        RecyclerView img_recycle;
        @BindView(R.id.like_num)
        TextView like_num;
        @BindView(R.id.name_tv)
        TextView name_tv;
        @BindView(R.id.read_tv)
        TextView read_tv;
        @BindView(R.id.time_tv)
        TextView time_tv;
        @BindView(R.id.title_tv)
        TextView title_tv;
        @BindView(R.id.cont_tv)
        TextView cont_tv;
        @BindView(R.id.like_ll)
        LinearLayout like_ll;
        @BindView(R.id.praise_bg)
        LinearLayout praise_bg;
        @BindView(R.id.praise_img)
        ImageView praise_img;
        @BindView(R.id.head_img)
        RoundedImageView head_img;
        Context context;
        ImageAdapter adapter;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}