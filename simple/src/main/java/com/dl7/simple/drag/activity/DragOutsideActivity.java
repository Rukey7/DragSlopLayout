package com.dl7.simple.drag.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
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
import com.dl7.simple.drag.adapter.ThumbAdapter;
import com.dl7.simple.drag.utils.AnimateHelper;
import com.dl7.simple.drag.utils.RecyclerViewHelper;

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
    private ThumbAdapter mAdapter;
    private boolean mIsHideToolbar = false; // 是否隐藏 Toolbar
    private Animator mToolBarAnimator;
    private Animator mBottomBarAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_outside);
        ButterKnife.bind(this);
        initToolBar(mToolBar, true, "Drag Outside Mode");
        // 设置 ViewPager
        List<Integer> imgList = new ArrayList<>();
        imgList.add(R.mipmap.pic1);
        imgList.add(R.mipmap.pic2);
        imgList.add(R.mipmap.pic3);
        imgList.add(R.mipmap.pic4);
        imgList.add(R.mipmap.pic5);
        PhotoPagerAdapter mPagerAdapter = new PhotoPagerAdapter(this, imgList, true);
        mVpPhoto.setAdapter(mPagerAdapter);
        mPagerAdapter.setListener(new PhotoPagerAdapter.OnPhotoClickListener() {
            @Override
            public void onPhotoClick() {
                mIsHideToolbar = !mIsHideToolbar;
                if (mIsHideToolbar) {
                    AnimateHelper.stopAnimator(mToolBarAnimator);
                    mToolBarAnimator = AnimateHelper.doMoveVertical(mToolBar, (int) mToolBar.getTranslationY(),
                            -mToolBar.getBottom(), 300);
                    if (mBottomBar.getTranslationY() != mBottomBar.getHeight()) {
                        AnimateHelper.stopAnimator(mBottomBarAnimator);
                        mBottomBarAnimator = AnimateHelper.doMoveVertical(mBottomBar, (int) mBottomBar.getTranslationY(),
                                mBottomBar.getHeight(), 300);
                    }
                } else {
                    AnimateHelper.stopAnimator(mToolBarAnimator);
                    mToolBarAnimator = AnimateHelper.doMoveVertical(mToolBar, (int) mToolBar.getTranslationY(),
                            0, 300);
                    ViewCompat.animate(mToolBar).translationY(0).setDuration(300).start();
                    if (mBottomBar.getTranslationY() != 0) {
                        AnimateHelper.stopAnimator(mBottomBarAnimator);
                        mBottomBarAnimator = AnimateHelper.doMoveVertical(mBottomBar, (int) mBottomBar.getTranslationY(),
                                0, 300);
                    }
                }
            }
        });
        // 设置 RecyclerView
        List<Integer> thumbList = new ArrayList<>();
        thumbList.add(R.mipmap.pic1);
        thumbList.add(R.mipmap.pic2);
        thumbList.add(R.mipmap.pic3);
        thumbList.add(R.mipmap.pic4);
        thumbList.add(R.mipmap.pic5);
        thumbList.add(R.mipmap.pic1);
        thumbList.add(R.mipmap.pic2);
        thumbList.add(R.mipmap.pic3);
        thumbList.add(R.mipmap.pic4);
        thumbList.add(R.mipmap.pic5);
        thumbList.add(R.mipmap.pic1);
        thumbList.add(R.mipmap.pic2);
        thumbList.add(R.mipmap.pic3);
        thumbList.add(R.mipmap.pic4);
        thumbList.add(R.mipmap.pic5);
        mAdapter = new ThumbAdapter(this, thumbList);
        RecyclerViewHelper.initRecyclerViewH(this, mRvRelateList, mAdapter);
        // 和 ViewPager 联动
        mDragLayout.interactWithViewPager(mIsInteract);
        mDragLayout.setDragPositionListener(new DragSlopLayout.OnDragPositionListener() {
            @Override
            public void onDragPosition(int visibleHeight, float percent, boolean isUp) {
                if (AnimateHelper.isRunning(mBottomBarAnimator) || mIsHideToolbar) {
                    return;
                }
                if (isUp && mBottomBar.getTranslationY() != mBottomBar.getHeight()) {
                    mBottomBarAnimator = AnimateHelper.doMoveVertical(mBottomBar, (int) mBottomBar.getTranslationY(),
                            mBottomBar.getHeight(), 300);
                } else if (!isUp && mBottomBar.getTranslationY() != 0) {
                    mBottomBarAnimator = AnimateHelper.doMoveVertical(mBottomBar, (int) mBottomBar.getTranslationY(),
                            0, 300);
                }
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnimateHelper.deleteAnimator(mToolBarAnimator);
        AnimateHelper.deleteAnimator(mBottomBarAnimator);
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
