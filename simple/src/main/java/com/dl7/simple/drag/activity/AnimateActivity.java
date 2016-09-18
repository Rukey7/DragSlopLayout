package com.dl7.simple.drag.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dl7.drag.DragSlopLayout;
import com.dl7.drag.animate.CustomViewAnimator;
import com.dl7.simple.drag.PhotoPagerAdapter;
import com.dl7.simple.drag.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimateActivity extends BaseActivity {

    @BindView(R.id.vp_photo)
    ViewPager mVpPhoto;
    @BindView(R.id.ll_view)
    LinearLayout mLlView;
    @BindView(R.id.iv_favorite)
    ImageView mIvFavorite;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;
    @BindView(R.id.iv_praise)
    ImageView mIvPraise;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.ds_layout)
    DragSlopLayout mDsLayout;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    boolean isOpen = true;
    private boolean mIsInteract = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);
        ButterKnife.bind(this);
        initToolBar(mToolBar, true, "");

        mDsLayout = (DragSlopLayout) findViewById(R.id.ds_layout);
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
                if (isOpen) {
                    mDsLayout.startOutAnim();
                } else {
                    mDsLayout.startInAnim();
                }
                isOpen = !isOpen;
            }
        });
        mDsLayout.interactWithViewPager(mIsInteract);
    }

    @OnClick({R.id.iv_favorite, R.id.iv_download, R.id.iv_praise, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_favorite:
                Toast.makeText(this, "喜欢", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_download:
                Toast.makeText(this, "下载", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_praise:
                Toast.makeText(this, "点赞", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_animate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.slide_bottom:
                mDsLayout.setAnimatorMode(DragSlopLayout.SLIDE_BOTTOM);
                return true;
            case R.id.slide_left:
                mDsLayout.setAnimatorMode(DragSlopLayout.SLIDE_LEFT);
                return true;
            case R.id.slide_right:
                mDsLayout.setAnimatorMode(DragSlopLayout.SLIDE_RIGHT);
                return true;
            case R.id.slide_fade:
                mDsLayout.setAnimatorMode(DragSlopLayout.FADE);
                return true;
            case R.id.slide_flip_x:
                mDsLayout.setAnimatorMode(DragSlopLayout.FLIP_X);
                return true;
            case R.id.slide_flip_y:
                mDsLayout.setAnimatorMode(DragSlopLayout.FLIP_Y);
                return true;
            case R.id.slide_zoom:
                mDsLayout.setAnimatorMode(DragSlopLayout.ZOOM);
                return true;
            case R.id.slide_zoom_left:
                mDsLayout.setAnimatorMode(DragSlopLayout.ZOOM_LEFT);
                return true;
            case R.id.slide_zoom_right:
                mDsLayout.setAnimatorMode(DragSlopLayout.ZOOM_RIGHT);
                return true;
            case R.id.slide_custom_one:
                _handleCustomOne();
                return true;
            case R.id.slide_custom_two:
                _handleCustomTwo();
                return true;

            case R.id.item_interact:
                mIsInteract = !mIsInteract;
                item.setChecked(mIsInteract);
                mDsLayout.interactWithViewPager(mIsInteract);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 自定义动画 1
     */
    private void _handleCustomOne() {
        mDsLayout.setCustomAnimator(new CustomViewAnimator() {
            @Override
            protected Animator doAnimator() {
                ObjectAnimator one = ObjectAnimator.ofFloat(mIvFavorite, "translationX", mDsLayout.getWidth(), -100, 50, 0);
                ObjectAnimator two = ObjectAnimator.ofFloat(mIvDownload, "translationX", mDsLayout.getWidth(), -100, 50, 0);
                two.setStartDelay(100);
                ObjectAnimator three = ObjectAnimator.ofFloat(mIvPraise, "translationX", mDsLayout.getWidth(), -100, 50, 0);
                three.setStartDelay(200);
                ObjectAnimator four = ObjectAnimator.ofFloat(mIvShare, "translationX", mDsLayout.getWidth(), -100, 50, 0);
                four.setStartDelay(300);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(700);
                animatorSet.playTogether(one, two, three, four);
                return animatorSet;
            }
        }, new CustomViewAnimator() {
            @Override
            protected Animator doAnimator() {
                ObjectAnimator one = ObjectAnimator.ofFloat(mIvFavorite, "translationX", 0, mDsLayout.getWidth());
                one.setStartDelay(300);
                ObjectAnimator two = ObjectAnimator.ofFloat(mIvDownload, "translationX", 0, mDsLayout.getWidth());
                two.setStartDelay(200);
                ObjectAnimator three = ObjectAnimator.ofFloat(mIvPraise, "translationX", 0, mDsLayout.getWidth());
                three.setStartDelay(100);
                ObjectAnimator four = ObjectAnimator.ofFloat(mIvShare, "translationX", 0, mDsLayout.getWidth());
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(700);
                animatorSet.playTogether(four, three, two, one);
                return animatorSet;
            }
        });
    }

    /**
     * 自定义动画 2
     */
    private void _handleCustomTwo() {
        mDsLayout.setCustomAnimator(new CustomViewAnimator() {
            @Override
            protected Animator doAnimator() {
                ObjectAnimator one = ObjectAnimator.ofFloat(mIvFavorite, "translationY", mLlView.getHeight(), 0);
                ObjectAnimator two = ObjectAnimator.ofFloat(mIvDownload, "translationY", mLlView.getHeight(), 0);
                two.setStartDelay(100);
                ObjectAnimator three = ObjectAnimator.ofFloat(mIvPraise, "translationY", mLlView.getHeight(), 0);
                three.setStartDelay(200);
                ObjectAnimator four = ObjectAnimator.ofFloat(mIvShare, "translationY", mLlView.getHeight(), 0);
                four.setStartDelay(300);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(300);
                animatorSet.playTogether(one, two, three, four);
                return animatorSet;
            }
        }, new CustomViewAnimator() {
            @Override
            protected Animator doAnimator() {
                ObjectAnimator one = ObjectAnimator.ofFloat(mIvFavorite, "translationY", 0, mLlView.getHeight());
                ObjectAnimator two = ObjectAnimator.ofFloat(mIvDownload, "translationY", 0, mLlView.getHeight());
                two.setStartDelay(100);
                ObjectAnimator three = ObjectAnimator.ofFloat(mIvPraise, "translationY", 0, mLlView.getHeight());
                three.setStartDelay(200);
                ObjectAnimator four = ObjectAnimator.ofFloat(mIvShare, "translationY", 0, mLlView.getHeight());
                four.setStartDelay(300);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(300);
                animatorSet.playTogether(one, two, three, four);
                return animatorSet;
            }
        });
    }
}
