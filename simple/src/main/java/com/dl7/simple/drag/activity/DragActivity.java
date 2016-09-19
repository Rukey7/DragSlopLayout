package com.dl7.simple.drag.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dl7.drag.DragSlopLayout;
import com.dl7.simple.drag.PhotoPagerAdapter;
import com.dl7.simple.drag.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DragActivity extends BaseActivity {

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

    boolean isOpen = true;
    private boolean mIsInteract = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        ButterKnife.bind(this);
        initToolBar(mToolBar, true, "");

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
        imgList.add(R.mipmap.img1);
        imgList.add(R.mipmap.img2);
        imgList.add(R.mipmap.img3);
        imgList.add(R.mipmap.img4);
        imgList.add(R.mipmap.img5);

        PhotoPagerAdapter mPagerAdapter = new PhotoPagerAdapter(this, imgList, false);
        mVpPhoto.setAdapter(mPagerAdapter);
        // 实现 ScrollView 的平滑滚动
        mDragLayout.setAttachScrollView(mSvView);
        // 和 ViewPager 联动
        mDragLayout.interactWithViewPager(mIsInteract);

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
        mPagerAdapter.setListener(new PhotoPagerAdapter.OnPhotoClickListener() {
            @Override
            public void onPhotoClick() {
                if (isOpen) {
                    mDragLayout.scrollOutScreen(500);
                } else {
                    mDragLayout.scrollInScreen(500);
                }
                isOpen = !isOpen;
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
}
