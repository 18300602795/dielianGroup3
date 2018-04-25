package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.ui.CommentActivity;
import com.etsdk.app.huov7.ui.LoginActivity;
import com.etsdk.app.huov7.ui.ShowImageActivity;
import com.etsdk.app.huov7.util.ImgUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.app.huov7.util.TimeUtils;
import com.game.sdk.SdkConstant;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
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

/**
 * Created by liu hong liang on 2017/1/5.
 */
public class ArticleListAdapter
        extends XRecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ArticleBean> articleBeens;
    private String[] imgs;

    public ArticleListAdapter(Context context, ArrayList<ArticleBean> articleBeens) {
        this.context = context;
        this.articleBeens = articleBeens;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_forum, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int errorImage = R.mipmap.ic_launcher;
        final ArticleBean articleBean = articleBeens.get(position);
        if (!StringUtils.isEmpty(articleBean.getImage_url())) {
            String pictures = articleBean.getImage_url();
            imgs = pictures.split(",");
            if (imgs.length == 1) {
                holder.img_ll.setVisibility(View.VISIBLE);
                holder.img1.setVisibility(View.VISIBLE);
                holder.img2.setVisibility(View.GONE);
                holder.img3.setVisibility(View.GONE);
                holder.img4.setVisibility(View.VISIBLE);
                ImgUtil.setImg(holder.context, imgs[0], errorImage, holder.img1);
            } else if (imgs.length == 2) {
                holder.img_ll.setVisibility(View.VISIBLE);
                holder.img1.setVisibility(View.VISIBLE);
                holder.img2.setVisibility(View.VISIBLE);
                holder.img3.setVisibility(View.GONE);
                holder.img4.setVisibility(View.GONE);
                ImgUtil.setImg(holder.context, imgs[0], errorImage, holder.img1);
                ImgUtil.setImg(holder.context, imgs[1], errorImage, holder.img2);
            } else if (imgs.length >= 3) {
                holder.img_ll.setVisibility(View.VISIBLE);
                holder.img1.setVisibility(View.VISIBLE);
                holder.img2.setVisibility(View.VISIBLE);
                holder.img3.setVisibility(View.VISIBLE);
                holder.img4.setVisibility(View.GONE);
                ImgUtil.setImg(holder.context, imgs[0], errorImage, holder.img1);
                ImgUtil.setImg(holder.context, imgs[1], errorImage, holder.img2);
                ImgUtil.setImg(holder.context, imgs[2], errorImage, holder.img3);
            }
        } else {
            holder.img_ll.setVisibility(View.GONE);
        }
        holder.title_tv.setText(articleBean.getTitle());
        holder.read_tv.setText("0");
        holder.time_tv.setText(TimeUtils.getTime(Long.valueOf(articleBean.getPublish_time())));
        holder.name_tv.setText(articleBean.getNickname());
        holder.count_tv.setText(articleBean.getContents());
        holder.like_num.setText(articleBean.getLike_number());
        ImgUtil.setImg(holder.context, SdkConstant.BASE_URL + articleBean.getPortrait(), R.mipmap.ic_launcher, holder.head_img);
        if (articleBean.getP_status() != null && articleBean.getP_status().equals("1")) {
            holder.like_img.setImageResource(R.mipmap.praise_1215);
        } else {
            holder.like_img.setImageResource(R.mipmap.unpraise_1215);
        }

        holder.item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, CommentActivity.class);
                intent.putExtra("argument", articleBean);
                holder.context.startActivity(intent);
            }
        });
        holder.img_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = new ArrayList<>();
                for (String img : articleBean.getImage_url().split(",")) {
                    list.add(img);
                }
                Intent intent = new Intent(holder.context, ShowImageActivity.class);
                intent.putExtra("imgs", list);
                holder.context.startActivity(intent);
            }
        });

        holder.comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, CommentActivity.class);
                intent.putExtra("argument", articleBean);
                holder.context.startActivity(intent);
            }
        });

        holder.like_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.addPraiseApi);
                httpParams.put("article_id", articleBean.getArticle_id());
                //成功，失败，null数据
                NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.addPraiseApi), new HttpJsonCallBackDialog<String>() {
                    @Override
                    public void onDataSuccess(String data) {
                        L.e("333", "data：" + data);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onJsonSuccess(int code, String msg, String data) {
                        L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int code2 = jsonObject.getInt("code");
                            if (code2 == 201) {
                                LoginActivity.start(holder.context);
                                return;
                            }
                            if (code2 == 200) {
                                if (articleBean.getP_status() != null && articleBean.getP_status().equals("1")) {
                                    articleBean.setP_status("2");
                                    articleBean.setLike_number(String.valueOf(Integer.valueOf(articleBean.getLike_number()) - 1));
                                } else {
                                    articleBean.setP_status("1");
                                    articleBean.setLike_number(String.valueOf(Integer.valueOf(articleBean.getLike_number()) + 1));
                                }
                            } else {
                                T.s(holder.context, msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg, String completionInfo) {
                        L.e("333", "errorNo：" + errorNo + "strMsg：" + strMsg + "completionInfo：" + completionInfo);
                    }
                });
            }
        });

        holder.share_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleBeens.size();
    }

    public void upDate(List<ArticleBean> articleBeen) {
        this.articleBeens.clear();
        this.articleBeens.addAll(articleBeen);
        notifyDataSetChanged();
    }

    public void addDate(List<ArticleBean> articleBeen) {
        this.articleBeens.addAll(articleBeen);
        notifyDataSetChanged();
    }


    static class ViewHolder extends XRecyclerView.ViewHolder {
        @BindView(R.id.item_ll)
        LinearLayout item_ll;
        @BindView(R.id.head_img)
        RoundedImageView head_img;
        @BindView(R.id.name_tv)
        TextView name_tv;
        @BindView(R.id.read_tv)
        TextView read_tv;
        @BindView(R.id.time_tv)
        TextView time_tv;
        @BindView(R.id.title_tv)
        TextView title_tv;
        @BindView(R.id.count_tv)
        TextView count_tv;
        @BindView(R.id.like_num)
        TextView like_num;
        @BindView(R.id.like_img)
        ImageView like_img;
        @BindView(R.id.img_ll)
        LinearLayout img_ll;
        @BindView(R.id.img1)
        ImageView img1;
        @BindView(R.id.img2)
        ImageView img2;
        @BindView(R.id.img3)
        ImageView img3;
        @BindView(R.id.img4)
        ImageView img4;
        @BindView(R.id.comment_ll)
        LinearLayout comment_ll;
        @BindView(R.id.like_ll)
        LinearLayout like_ll;
        @BindView(R.id.share_ll)
        LinearLayout share_ll;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}