package com.dl7.simple.drag.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dl7.drag.DragSlopLayout;
import com.dl7.simple.drag.PhotoPagerAdapter;
import com.dl7.simple.drag.R;

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
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);
        ButterKnife.bind(this);
        mToolBar.setTitle("美女");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDsLayout = (DragSlopLayout) findViewById(R.id.ds_layout);
        List<Integer> imgList = new ArrayList<>();
        imgList.add(R.mipmap.pic1);
        imgList.add(R.mipmap.pic2);
        imgList.add(R.mipmap.pic3);
        imgList.add(R.mipmap.pic4);
        imgList.add(R.mipmap.pic5);
        PhotoPagerAdapter mPagerAdapter = new PhotoPagerAdapter(this, imgList);
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
        mDsLayout.interactWithViewPager(true);
        mDsLayout.setAnimatorMode(DragSlopLayout.FLIP_Y);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
