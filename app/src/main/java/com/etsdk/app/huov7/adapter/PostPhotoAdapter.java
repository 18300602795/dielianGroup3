package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.ui.ShowImageActivity;
import com.etsdk.app.huov7.util.PhotoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\3\1 0001.
 */

public class PostPhotoAdapter extends RecyclerView.Adapter {
    private List<String> imgs;
    private Context context;
    private PostPhotoCallback photoCallback;

    public void setPhotoCallback(PostPhotoCallback photoCallback) {
        this.photoCallback = photoCallback;
    }

    public PostPhotoAdapter(List<String> imgs, Context context) {
        this.imgs = imgs;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 2) {
            view = LayoutInflater.from(context).inflate(R.layout.item_no_photo, parent, false);
            return new NoPhotoViewHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_add_photo, parent, false);
            return new AddPhotoViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_show_photo, parent, false);
            return new ShowPhotoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AddPhotoViewHolder) {
            ((AddPhotoViewHolder) holder).add_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (photoCallback != null)
                        photoCallback.add();
                }
            });
        }

        if (holder instanceof ShowPhotoViewHolder) {
            ((ShowPhotoViewHolder) holder).photo_iv.setImageBitmap(PhotoUtils.getimage(imgs.get(position), 60, 60));
            ((ShowPhotoViewHolder) holder).close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (photoCallback != null)
                        photoCallback.close(position);
                }
            });

            ((ShowPhotoViewHolder) holder).photo_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(imgs.get(position));
                    intent.putExtra("imgs", strings);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (imgs == null || imgs.size() == 0) {
            if (position == 0) {
                return 1;
            } else {
                return 2;
            }
        }
        if (imgs.size() < 9) {
            if (position == imgs.size()) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (imgs == null || imgs.size() == 0) {
            return 2;
        } else if (imgs.size() < 9) {
            return imgs.size() + 1;
        } else {
            return 9;
        }
    }

    class ShowPhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photo_iv;
        ImageView close_iv;

        public ShowPhotoViewHolder(View itemView) {
            super(itemView);
            photo_iv = itemView.findViewById(R.id.photo_iv);
            close_iv = itemView.findViewById(R.id.close_iv);
        }
    }

    class AddPhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView add_iv;

        public AddPhotoViewHolder(View itemView) {
            super(itemView);
            add_iv = itemView.findViewById(R.id.add_iv);
        }
    }

    class NoPhotoViewHolder extends RecyclerView.ViewHolder {
        public NoPhotoViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface PostPhotoCallback {
        void close(int position);

        void add();

        void show(int position);
    }
}
