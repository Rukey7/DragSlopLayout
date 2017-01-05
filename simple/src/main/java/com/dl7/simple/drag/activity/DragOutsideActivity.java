package com.dl7.simple.drag.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dl7.drag.DragSlopLayout;
import com.dl7.simple.drag.R;
import com.dl7.simple.drag.adapter.PhotoPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DragOutsideActivity extends BaseActivity {


    @BindView(R.id.vp_photo)
    ViewPager mVpPhoto;
    @BindView(R.id.rv_relate_list)
    RecyclerView mRvRelateList;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.iv_favorite)
    ImageView mIvFavorite;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;
    @BindView(R.id.iv_praise)
    ImageView mIvPraise;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.bottom_bar)
    LinearLayout mBottomBar;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;

    private boolean mIsInteract = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_outside);
        ButterKnife.bind(this);
        initToolBar(mToolBar, true, "Drag Outside Mode");

        List<Integer> imgList = new ArrayList<>();
        imgList.add(R.mipmap.img1);
        imgList.add(R.mipmap.img2);
        imgList.add(R.mipmap.img3);
        imgList.add(R.mipmap.img4);
        imgList.add(R.mipmap.img5);

        PhotoPagerAdapter mPagerAdapter = new PhotoPagerAdapter(this, imgList, false);
        mVpPhoto.setAdapter(mPagerAdapter);
        // 和 ViewPager 联动
        mDragLayout.interactWithViewPager(mIsInteract);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_interact:
                mIsInteract = !mIsInteract;
                item.setChecked(mIsInteract);
                mDragLayout.interactWithViewPager(mIsInteract);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_favorite, R.id.iv_download, R.id.iv_praise, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_favorite:
                break;
            case R.id.iv_download:
                break;
            case R.id.iv_praise:
                break;
            case R.id.iv_share:
                break;
        }
    }
}
