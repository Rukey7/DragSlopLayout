package com.dl7.simple.drag.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dl7.simple.drag.R;

import java.util.List;

/**
 * Created by long on 2017/1/6.
 */
public class ThumbAdapter extends BaseRecyclerAdapter<Integer> {

    public ThumbAdapter(Context context) {
        super(context);
    }

    public ThumbAdapter(Context context, List<Integer> datas) {
        super(context, datas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_thumb, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).ivPhoto.setImageResource(mDatas.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivPhoto;

        public ViewHolder(View rootView) {
            super(rootView);
            this.ivPhoto = (ImageView) rootView.findViewById(R.id.iv_photo);
        }

    }
}
