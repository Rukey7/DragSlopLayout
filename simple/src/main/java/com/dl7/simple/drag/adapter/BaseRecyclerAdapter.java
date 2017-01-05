package com.dl7.simple.drag.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 95 on 2016/4/21.
 * 适配器基类
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<T> mDatas;


    public BaseRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mDatas = new ArrayList<>();
    }

    public BaseRecyclerAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public BaseRecyclerAdapter(Context context, T[] datas) {
        this.mContext = context;
        this.mDatas = new ArrayList<T>();
        Collections.addAll(mDatas, datas);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 更新数据，替换原有数据
     * @param items
     */
    public void updateItems(List<T> items) {
        mDatas = items;
        notifyDataSetChanged();
    }

    /**
     * 插入一条数据
     * @param item 数据
     */
    public void addItem(T item) {
        mDatas.add(0, item);
        notifyItemInserted(0);
    }

    /**
     * 插入一条数据
     * @param item 数据
     * @param position 插入位置
     */
    public void addItem(T item, int position) {
        position = Math.min(position, mDatas.size());
        mDatas.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 在列表尾添加一串数据
     * @param items
     */
    public void addItems(List<T> items) {
        mDatas.addAll(items);
    }

    /**
     * 移除一条数据
     * @param position 位置
     */
    public void removeItem(int position) {
        if (position > mDatas.size() - 1) {
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 移除一条数据
     * @param item 数据
     */
    public void removeItem(T item) {
        int pos = 0;
        for (T info : mDatas) {
            if (item.hashCode() == info.hashCode()) {
                removeItem(pos);
                break;
            }
            pos++;
        }
    }

    /**
     * 清除所有数据
     */
    public void cleanItems() {
        mDatas.clear();
        notifyDataSetChanged();
    }
}
