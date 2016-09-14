package com.dl7.simple.drag.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dl7.drag.DragSlopLayout;
import com.dl7.simple.drag.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DragClickActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_one)
    Button mBtnOne;
    @BindView(R.id.btn_two)
    Button mBtnTwo;
    @BindView(R.id.btn_three)
    Button mBtnThree;
    @BindView(R.id.ll_view)
    LinearLayout mLlView;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_click);
        ButterKnife.bind(this);

        mDragLayout.setEnableBlur(true);
    }

    @OnClick({R.id.btn_one, R.id.btn_two, R.id.btn_three})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_one:
                Log.e("DragClickActivity", "btn_one");
                Toast.makeText(this, "One", Toast.LENGTH_SHORT).show();
                mIvPhoto.setImageResource(R.mipmap.pic1);
                mDragLayout.updateBlurView();
                break;
            case R.id.btn_two:
                Toast.makeText(this, "Two", Toast.LENGTH_SHORT).show();
                mIvPhoto.setImageResource(R.mipmap.pic2);
                mDragLayout.updateBlurView();
                break;
            case R.id.btn_three:
                Toast.makeText(this, "Three", Toast.LENGTH_SHORT).show();
                mIvPhoto.setImageResource(R.mipmap.pic3);
                mDragLayout.updateBlurView();
                break;
        }
    }

    @OnClick(R.id.ll_view)
    public void onClick() {
        Log.e("DragClickActivity", "onClick");
    }
}
