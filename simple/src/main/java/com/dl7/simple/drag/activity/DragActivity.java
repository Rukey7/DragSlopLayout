package com.dl7.simple.drag.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dl7.drag.DragSlopLayout;
import com.dl7.simple.drag.PhotoPagerAdapter;
import com.dl7.simple.drag.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DragActivity extends AppCompatActivity {

    @BindView(R.id.vp_photo)
    ViewPager mVpPhoto;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.sv_view)
    ScrollView mSvView;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
//    @BindView(R.id.blurView)
//    BlurView blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupBlurView();

        final String[] titleList = new String[]{
                "汤姆·哈迪", "克里斯蒂安·贝尔", "马克·沃尔伯格", "威尔·史密斯", "丹泽尔·华盛顿",
        };
        final String[] descList = new String[]{
                getString(R.string.TomHardy),
                getString(R.string.ChristianBale),
                getString(R.string.MarkWahlberg),
                getString(R.string.WillSmith),
                getString(R.string.DenzelWashington),
        };
        List<Integer> imgList = new ArrayList<>();
        imgList.add(R.mipmap.img5);
        imgList.add(R.mipmap.img5);
        imgList.add(R.mipmap.img5);
        imgList.add(R.mipmap.img5);
        imgList.add(R.mipmap.img5);
        PhotoPagerAdapter mPagerAdapter = new PhotoPagerAdapter(this, imgList);
        mVpPhoto.setAdapter(mPagerAdapter);
        mDragLayout.setAttachScrollView(mSvView);
//        mDragLayout.interactWithViewPager(true);

        mTvCount.setText("" + imgList.size());
        mTvTitle.setText(titleList[0]);
        mTvIndex.setText("" + 1);
        mTvContent.setText(descList[0]);

        mVpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTvTitle.setText(titleList[position]);
                mTvIndex.setText("" + (position + 1));
                mTvContent.setText(descList[position]);

            }
        });

        mDragLayout.setDragListener(new DragSlopLayout.OnDragSlopListener() {
            @Override
            public void onDragScrolled(int visibleHeight, float percent) {

                Log.e("DragSlopLayout", visibleHeight + " - "+ percent);
            }
        });
    }

    private void setupBlurView() {
        final float radius = 16f;

        final View decorView = getWindow().getDecorView();
        //Activity's root View. Can also be root View of your layout
        final View rootView = decorView.findViewById(android.R.id.content);
        //set background, if your root layout doesn't have one
        final Drawable windowBackground = decorView.getBackground();

//        blurView.setupWith(rootView)
//                .windowBackground(windowBackground)
//                .blurAlgorithm(new RenderScriptBlur(this, true)) //Optional, enabled by default. User can have custom implementation
//                .blurRadius(radius);
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
