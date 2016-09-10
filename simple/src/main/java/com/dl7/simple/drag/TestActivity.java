package com.dl7.simple.drag;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dl7.drag.DragSlopLayout;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    int mFixHeight;
    View mDragView;
    DragSlopLayout mDragSlopLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mFixHeight = getResources().getDimensionPixelOffset(R.dimen.layout_fix_height);
        mDragView = findViewById(R.id.fl_view);
        mDragSlopLayout = (DragSlopLayout) findViewById(R.id.ds_layout);
        Log.e("TestActivity", ""+mFixHeight);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_photo);
        List<Integer> imgList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            imgList.add(R.mipmap.img);
        }
        PhotoPagerAdapter mPagerAdapter = new PhotoPagerAdapter(this, imgList);
        mViewPager.setAdapter(mPagerAdapter);
        mDragSlopLayout.interactWithViewPager(true);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////                Log.i("TestActivity", "position " + position);
////                Log.d("TestActivity", "positionOffset " + positionOffset);
////                Log.i("TestActivity", "positionOffsetPixels " + positionOffsetPixels);
//                float percent = (float) Math.abs(positionOffset - 0.5) * 2;
//                int y = (int) (mFixHeight * (percent - 1));
//                Log.w("TestActivity", ""+percent);
//                Log.w("TestActivity", ""+mDragView.getTranslationY());
//                Log.e("TestActivity", ""+y);
//                mDragView.setTranslationY(-y);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.e("TestActivity", "" + position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.w("TestActivity", "" + state);
//            }
//        });
    }
}
