package com.dl7.simple.xviewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_photo);

        List<Integer> imgList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            imgList.add(R.mipmap.img);
        }
        PhotoPagerAdapter mPagerAdapter = new PhotoPagerAdapter(this, imgList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("TestActivity", "position " + position);
                Log.d("TestActivity", "positionOffset " + positionOffset);
                Log.i("TestActivity", "positionOffsetPixels " + positionOffsetPixels);
                float percent = (float) Math.abs(positionOffset - 0.5);

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("TestActivity", "" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.w("TestActivity", "" + state);
            }
        });
    }
}
