package com.dl7.simple.xviewpager.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dl7.simple.xviewpager.PhotoPagerAdapter;
import com.dl7.simple.xviewpager.R;
import com.dl7.xviewpager.DragSlopLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimateActivity extends AppCompatActivity {

    boolean isOpen = true;
    @BindView(R.id.vp_photo)
    ViewPager mVpPhoto;
    @BindView(R.id.iv_favorite)
    ImageView mIvFavorite;
    @BindView(R.id.iv_start)
    ImageView mIvStart;
    @BindView(R.id.iv_praise)
    ImageView mIvPraise;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.ds_layout)
    DragSlopLayout mDsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);
        ButterKnife.bind(this);

        mDsLayout = (DragSlopLayout) findViewById(R.id.ds_layout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_photo);
        List<Integer> imgList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            imgList.add(R.mipmap.img);
        }
        PhotoPagerAdapter mPagerAdapter = new PhotoPagerAdapter(this, imgList);
        mViewPager.setAdapter(mPagerAdapter);
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
        mDsLayout.interactWithViewPager(true);
        mDsLayout.setAnimatorMode(DragSlopLayout.SLIDE_IN_LEFT);
//        mDragSlopLayout.interactWithViewPager(true);
    }

    @OnClick({R.id.iv_favorite, R.id.iv_start, R.id.iv_praise, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_favorite:
                Toast.makeText(this, "喜欢", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_start:
                Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_praise:
                Toast.makeText(this, "点赞", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
