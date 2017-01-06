package com.dl7.simple.drag.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dl7.drag.DragSlopLayout;
import com.dl7.simple.drag.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DragBlurActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.ll_favorite)
    LinearLayout mLlFavorite;
    @BindView(R.id.ll_next)
    LinearLayout mLlNext;
    @BindView(R.id.ll_download)
    LinearLayout mLlDownload;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    private final int[] mImgRes = new int[] {R.mipmap.pic2, R.mipmap.pic4, R.mipmap.pic5};
    private int mIndex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_blur);
        ButterKnife.bind(this);
        initToolBar(mToolBar, true, "");
//        mDragLayout.setEnableBlur(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.blur_local:
//                mDragLayout.setBlurFull(false);
                return true;
            case R.id.blur_full:
//                mDragLayout.setBlurFull(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ll_favorite, R.id.ll_next, R.id.ll_download, R.id.ll_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_favorite:
                Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_next:
                mIvPhoto.setImageResource(mImgRes[mIndex++ % mImgRes.length]);
//                mDragLayout.updateBlurView();
                break;
            case R.id.ll_download:
                Toast.makeText(this, "下载", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
