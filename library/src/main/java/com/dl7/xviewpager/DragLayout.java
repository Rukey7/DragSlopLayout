package com.dl7.xviewpager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by long on 2016/9/6.
 */
public class DragLayout extends FrameLayout {

    // 展开
    public static final int STATUS_EXPANDED = 101;
    // 收缩
    public static final int STATUS_COLLAPSED = 102;
    // 退出
    public static final int STATUS_EXIT = 103;

    private static final int BLURRY_DEFAULT_COLOR = Color.argb(44, 0, 255, 255);

    private static final float TOUCH_SLOP_SENSITIVITY = 1.f;
    private static final float FLING_VELOCITY = 2000;
    private int mFixHeight = 200;
    private int mHeight;
    private Context mContext;
    private ViewDragHelper mDragHelper;
    private View mMainView;
    private View mDragView;
    private boolean mIsDrag = false;


    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs);
    }

    private void _init(Context context, AttributeSet attrs) {
        mContext = context;
        mDragHelper = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            // DragLayout 只能且必须包含两个子视图，第一个为主视图，另一个为可拖拽的视图
            throw new IllegalArgumentException("DragLayout must contains two sub-views.");
        }
        mMainView = getChildAt(0);
        mDragView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        if (childCount != 2) {
            // DragLayout 只能且必须包含两个子视图，第一个为主视图，另一个为可拖拽的视图
            throw new IllegalArgumentException("DragLayout must contains two sub-views.");
        }

        MarginLayoutParams lp;
        View mainView = getChildAt(0);
        lp = (MarginLayoutParams) mainView.getLayoutParams();
        int width = mainView.getMeasuredWidth();
        int height = mainView.getMeasuredHeight();
        mainView.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + width, lp.topMargin + height);

        View childView = getChildAt(1);
        lp = (MarginLayoutParams) childView.getLayoutParams();
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();
        int childTop = b - mFixHeight;
        childView.layout(lp.leftMargin, childTop, lp.leftMargin + childWidth, childTop + childHeight);
    }

    /**********************************
     * ViewDragHelper使用的基本模板
     ********************************************/

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        Rect rect = new Rect(mDragView.getLeft(), mDragView.getTop(), mDragView.getRight(), mDragView.getBottom());
        if (rect.contains((int)ev.getX(), (int)ev.getY())) {
            isIntercept = true;
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return mIsDrag;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        private int criticalTop;
        private int expandedTop;
        private int collapsedTop;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            requestDisallowInterceptTouchEvent(true);
            mIsDrag = child == mDragView;
            criticalTop = mHeight - (mDragView.getHeight() - mFixHeight) / 2 - mFixHeight;
            expandedTop = mHeight - mDragView.getHeight();
            collapsedTop = mHeight - mFixHeight;
            return mIsDrag;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (Math.abs(yvel) < FLING_VELOCITY) {
                if (mDragView.getTop() > criticalTop) {
                    mDragHelper.smoothSlideViewTo(mDragView, 0, collapsedTop);
                    ViewCompat.postInvalidateOnAnimation(DragLayout.this);
                } else {
                    mDragHelper.smoothSlideViewTo(mDragView, 0, expandedTop);
                    ViewCompat.postInvalidateOnAnimation(DragLayout.this);
                }
            } else if (yvel > 0) {
                mDragHelper.smoothSlideViewTo(mDragView, 0, collapsedTop);
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            } else {
                mDragHelper.smoothSlideViewTo(mDragView, 0, expandedTop);
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            }
            requestDisallowInterceptTouchEvent(false);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int newTop = Math.max(expandedTop, top);
            newTop = Math.min(collapsedTop, newTop);
            return newTop;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        super.computeScroll();
    }
}
