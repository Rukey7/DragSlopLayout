package com.dl7.drag;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.dl7.drag.animate.AnimatorPresenter;
import com.dl7.drag.animate.CustomViewAnimator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by long on 2016/9/6.
 * 实现拖拽出界外的布局
 */
public class DragSlopLayout extends FrameLayout {

    public static final int MODE_DRAG = 1;
    public static final int MODE_FIX_ALWAYS = 2;
    public static final int MODE_ANIMATE = 3;

    // 展开
    static final int STATUS_EXPANDED = 101;
    // 收缩
    static final int STATUS_COLLAPSED = 102;
    // 退出
    static final int STATUS_EXIT = 103;
    // 滚动
    static final int STATUS_SCROLL = 104;

    // ViewDragHelper 的敏感度
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    // 判断快速滑动的速率
    private static final float FLING_VELOCITY = 2000;
    // 下坠动画时间
    private static final int FALL_BOUND_DURATION = 1000;

    // 模式
    private int mMode;
    // 固定高度
    private int mFixHeight;
    // 最大高度
    private int mMaxHeight;
    // 整个布局高度
    private int mHeight;
    // 拖拽模式的临界Top值
    private int mCriticalTop;
    // 拖拽模式的展开状态Top值
    private int mExpandedTop;
    // 拖拽模式的收缩状态Top值
    private int mCollapsedTop;
    // 是否处于拖拽状态
    private boolean mIsDrag = false;
    // 拖拽状态
    @DragStatus
    private int mDragStatus = STATUS_EXPANDED;
    // 是否有 post 显示动画 Runnable
    private boolean mHasShowRunnable = false;

    private Context mContext;
    // 布局的第1个子视图
    private View mMainView;
    // 可拖拽的视图，为布局的第2个子视图
    private View mDragView;
    // 拖拽帮助类
    private ViewDragHelper mDragHelper;
    // 下坠滚动辅助类
    private ScrollerCompat mFallBoundScroller;
    // 回升滚动辅助类
    private ScrollerCompat mComeBackScroller;
    // ViewPager 监听器
    private ViewPager.OnPageChangeListener mViewPagerListener;
    // 动画持有者
    private AnimatorPresenter mAnimPresenter;
    // 和 ViewPager 联动时的自动执行进入动画的延迟时间，注意和动画的启动延迟时间区分
    private int mAutoAnimateDelay = 1000;
    // 是否手动执行了退出动画，如果为真则不再执行自动进入动画
    private boolean mIsDoOutAnim = false;
    // 是否设置自定义动画，true 的话关闭 ViewPager 联动动画
    private boolean mIsCustomAnimator = false;
    // 关联的 ScrollView，实现垂直方向的平滑滚动
    private View mAttachScrollView;


    public DragSlopLayout(Context context) {
        this(context, null);
    }

    public DragSlopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DragSlopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs);
    }

    private void _init(Context context, AttributeSet attrs) {
        mContext = context;
        mDragHelper = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, callback);
        mFallBoundScroller = ScrollerCompat.create(context, new BounceInterpolator());
        mComeBackScroller = ScrollerCompat.create(context, new DecelerateInterpolator());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DragSlopLayout, 0, 0);
        mFixHeight = a.getDimensionPixelOffset(R.styleable.DragSlopLayout_fix_height, mFixHeight);
        mMaxHeight = a.getDimensionPixelOffset(R.styleable.DragSlopLayout_max_height, 0);
        mMode = a.getInt(R.styleable.DragSlopLayout_mode, MODE_DRAG);
        a.recycle();
        if (mMode == MODE_DRAG) {
            mDragStatus = STATUS_COLLAPSED;
        }
        mAnimPresenter = new AnimatorPresenter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount < 2) {
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mMaxHeight == 0) {
            // 未设置最大高度则为布局高度的 2/3
            mMaxHeight = getMeasuredHeight() * 2 / 3;
        }
        View childView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();
        // 限定视图的最大高度
        if (childHeight > mMaxHeight) {
            MeasureSpec.makeMeasureSpec(childWidth - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
            childView.measure(MeasureSpec.makeMeasureSpec(childWidth - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mMaxHeight - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        MarginLayoutParams lp;
        View childView = getChildAt(1);
        lp = (MarginLayoutParams) childView.getLayoutParams();
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();
        if (mMode != MODE_DRAG) {
            // 非拖拽模式固定高度为子视图高度
            mFixHeight = childHeight;
        } else if (mFixHeight > childHeight) {
            // 固定高度超过子视图高度则设置为固定模式
            mMode = MODE_FIX_ALWAYS;
            mFixHeight = childHeight;
        }
        int childTop;
        if (mDragStatus == STATUS_EXIT) {
            // 对于 ViewPager 换页后会回调 onLayout()，需要进行处理
            if (mMode == MODE_DRAG) {
                childTop = b;
            } else {
                childTop = b - mFixHeight;
                childView.setTranslationY(childHeight);
            }
        } else if (mDragStatus == STATUS_COLLAPSED) {
            childTop = b - mFixHeight;
        } else if (mDragStatus == STATUS_EXPANDED) {
            childTop = b - childHeight;
        } else {
            childTop = b - mFixHeight;
        }
        childView.layout(lp.leftMargin, childTop, lp.leftMargin + childWidth, childTop + childHeight);

//            mCriticalTop = mHeight - (mDragView.getHeight() - mFixHeight) / 2 - mFixHeight;
        mCriticalTop = b - childHeight / 2;
        mExpandedTop = b - childHeight;
        mCollapsedTop = b - mFixHeight;
    }

    /***********************************
     * ViewDragHelper
     ********************************************/

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        if (_isNeedIntercept(ev)) {
            isIntercept = true;
        } else
        if (mDragHelper.isViewUnder(mDragView, (int) ev.getX(), (int) ev.getY()) && mMode == MODE_DRAG) {
            _stopAllScroller();
        }
        if (mDragStatus == STATUS_EXIT) {
            getHandler().removeCallbacks(mShowRunnable);
        }
        return isIntercept;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAttachScrollView != null) {
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return mIsDrag;
    }


    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            requestDisallowInterceptTouchEvent(true);
            mIsDrag = child == mDragView;
            return mIsDrag;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (Math.abs(yvel) < FLING_VELOCITY) {
                if (mDragView.getTop() > mCriticalTop) {
                    if (mDragStatus == STATUS_EXPANDED) {
                        mDragHelper.smoothSlideViewTo(mDragView, 0, mCollapsedTop);
                    } else {
                        mFallBoundScroller.startScroll(0, mDragView.getTop(), 0, mCollapsedTop - mDragView.getTop(),
                                FALL_BOUND_DURATION);
                    }
                    ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
                } else {
                    mDragHelper.smoothSlideViewTo(mDragView, 0, mExpandedTop);
                    ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
                }
            } else if (yvel > 0) {
                if (!_flingScrollView(yvel)) {
                    mDragHelper.settleCapturedViewAt(0, mCollapsedTop);
                    ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
                }
            } else {
                if (!_flingScrollView(yvel)) {
                    mDragHelper.settleCapturedViewAt(0, mExpandedTop);
                    ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
                }
            }
            requestDisallowInterceptTouchEvent(false);
        }

        /**
         * 检测 ViewDrag 状态变化，eg：
         * STATE_IDLE       闲置、停止
         * STATE_DRAGGING   拖拽中
         * STATE_SETTLING   滚动状态(描述不是很确切)，比如拖拽放手后在onViewReleased()调用smoothSlideViewTo()控制View移动的时候
         * @param state
         */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                _switchStatus();
            }
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
            if (mAttachScrollView.getScrollY() > 0 || (mDragView.getTop() == mExpandedTop && dy < 0)) {
                mAttachScrollView.scrollBy(0, -dy);
                return mExpandedTop;
            }
            int newTop = Math.max(mExpandedTop, top);
            newTop = Math.min(mCollapsedTop, newTop);
            return newTop;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return child == mDragView ? child.getHeight() : 0;
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true) ||
                _continueSettling(mFallBoundScroller) || _continueSettling(mComeBackScroller)) {
            mDragStatus = STATUS_SCROLL;
            ViewCompat.postInvalidateOnAnimation(this);
        }
        super.computeScroll();
    }

    /**
     * 处理自定义滚动动画
     */
    private boolean _continueSettling(ScrollerCompat scroller) {
        boolean keepGoing = scroller.computeScrollOffset();
        if (!keepGoing) {
            return false;
        }
        final int x = scroller.getCurrX();
        final int y = scroller.getCurrY();
        final int dx = x - mDragView.getLeft();
        final int dy = y - mDragView.getTop();
        if (dx != 0) {
            ViewCompat.offsetLeftAndRight(mDragView, dx);
        }
        if (dy != 0) {
            ViewCompat.offsetTopAndBottom(mDragView, dy);
        }
        if (keepGoing && x == scroller.getFinalX() && y == scroller.getFinalY()) {
            // Close enough. The interpolator/scroller might think we're still moving
            // but the user sure doesn't.
            scroller.abortAnimation();
            keepGoing = false;
            ViewCompat.postInvalidateOnAnimation(this);
            _switchStatus();
        }
        return keepGoing;
    }

    /**
     * 停止所有滚动
     */
    private void _stopAllScroller() {
        if (!mFallBoundScroller.isFinished()) {
            mFallBoundScroller.abortAnimation();
        }
        if (!mComeBackScroller.isFinished()) {
            mComeBackScroller.abortAnimation();
        }
    }

    /*********************************** Inside ********************************************/

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_EXPANDED, STATUS_COLLAPSED, STATUS_EXIT, STATUS_SCROLL})
    @interface DragStatus {
    }

    /**
     * 切换状态
     */
    private void _switchStatus() {
        if (mDragView.getTop() == mExpandedTop) {
            mDragStatus = STATUS_EXPANDED;
        } else if (mDragView.getTop() == mCollapsedTop) {
            mDragStatus = STATUS_COLLAPSED;
        } else if (mDragView.getTop() == mHeight) {
            mDragStatus = STATUS_EXIT;
        } else {
            mDragStatus = STATUS_SCROLL;
        }
    }

    /**
     * 隐藏 DragView
     *
     * @param percent ViewPager 滑动百分比
     */
    private void _hideDragView(float percent) {
        if (!mHasShowRunnable && !mIsDoOutAnim) {
            float hidePercent = percent * 3;
            if (hidePercent > 1.0f) {
                hidePercent = 1.0f;
                mDragStatus = STATUS_EXIT;
            } else {
                mDragStatus = STATUS_SCROLL;
            }

            if (mMode == MODE_DRAG) {
                _stopAllScroller();
                final int y = (int) (mFixHeight * hidePercent + mCollapsedTop);
                final int dy = y - mDragView.getTop();
                if (dy != 0) {
                    ViewCompat.offsetTopAndBottom(mDragView, dy);
                }
            } else if (mMode == MODE_ANIMATE) {
                mAnimPresenter.handleAnimateFrame(mDragView, hidePercent);
            }
        }
    }

    /**
     * 显示 DragView
     *
     * @param delay 延迟时间
     */
    private void _showDragView(int delay) {
        mHasShowRunnable = true;
        postDelayed(mShowRunnable, delay);
    }

    /**
     * 动画显示 Runnable
     */
    private Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            mHasShowRunnable = false;
            if (mMode == MODE_DRAG) {
                mComeBackScroller.startScroll(0, mDragView.getTop(), 0, mCollapsedTop - mDragView.getTop(), 500);
                ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
            } else if (mMode == MODE_ANIMATE) {
                startInAnim();
                mDragStatus = STATUS_SCROLL;
            }
        }
    };

    /*********************************** ScrollView ********************************************/

    /**
     * 设置关联的 ScrollView 如果有的话，目前只支持 ScrollView 和 NestedScrollView 及其自视图
     * @param attachScrollView  ScrollView or NestedScrollView
     */
    public void setAttachScrollView(View attachScrollView) {
        if (!_isScrollView(attachScrollView)) {
            throw new IllegalArgumentException("The view must be ScrollView or NestedScrollView.");
        }
        mAttachScrollView = attachScrollView;
    }

    /**
     * 如果点击在关联的 ScrollView 区域则由父类 DragSlopLayout 中断事件接收，并全权控制处理 ScrollView
     * @param ev    点击事件
     * @return
     */
    private boolean _isNeedIntercept(MotionEvent ev) {
        if (mAttachScrollView == null) {
            return false;
        }
        int y = (int) ev.getY() - mDragView.getTop();
        if (mDragHelper.isViewUnder(mAttachScrollView, (int) ev.getX(), y) && mMode == MODE_DRAG) {
            return true;
        }
        return false;
    }

    /**
     * 滑动 ScrollView
     * @param yvel  滑动速度
     * @return
     */
    private boolean _flingScrollView(float yvel) {
        if (mAttachScrollView == null || mAttachScrollView.getScrollY() == 0) {
            return false;
        }
        if (mAttachScrollView instanceof ScrollView) {
            ((ScrollView) mAttachScrollView).fling((int) -yvel);
        } else if (mAttachScrollView instanceof NestedScrollView) {
            ((NestedScrollView) mAttachScrollView).fling((int) -yvel);
        }
        return true;
    }

    /**
     * 判断视图是否为 ScrollView or NestedScrollView 或它的子类
     *
     * @param view View
     * @return
     */
    private boolean _isScrollView(View view) {
        boolean isScrollView = false;
        if (view instanceof ScrollView || view instanceof NestedScrollView) {
            isScrollView = true;
        } else {
            ViewParent parent = view.getParent();
            while (parent != null) {
                if (parent instanceof ScrollView || parent instanceof NestedScrollView) {
                    isScrollView = true;
                    break;
                }
            }
        }
        return isScrollView;
    }

    /*********************************** ViewPager ********************************************/

    /**
     * 和 ViewPager 进行联动，注意第1个子视图必须为 ViewPager 或它的子类
     *
     * @param isInteract 是否联动
     */
    public void interactWithViewPager(boolean isInteract) {
        if (!_isViewPager(mMainView)) {
            throw new IllegalArgumentException("The first child view must be ViewPager.");
        }
        if (mViewPagerListener != null) {
            ((ViewPager) mMainView).removeOnPageChangeListener(mViewPagerListener);
        }
        if (!isInteract) {
            mViewPagerListener = null;
            return;
        }
        mViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {

            boolean isRightSlide = true;
            float mLastOffset = 0;
            int status = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (status != ViewPager.SCROLL_STATE_IDLE && mMode != MODE_FIX_ALWAYS && !mIsCustomAnimator) {
                    // 判断拖拽过界的方向
                    if (Math.abs(positionOffset - mLastOffset) > 0.8f &&
                            status == ViewPager.SCROLL_STATE_DRAGGING) {
                        if (positionOffset > 0.5f) {
                            isRightSlide = false;
                        } else {
                            isRightSlide = true;
                        }
                    }
                    float percent;
                    if (isRightSlide) {
                        percent = positionOffset;
                        if (positionOffset == 0 && status == ViewPager.SCROLL_STATE_SETTLING && mLastOffset > 0.5f) {
                            percent = 1.0f;
                        }
                    } else {
                        percent = 1 - positionOffset;
                        if (positionOffset == 0 && status == ViewPager.SCROLL_STATE_SETTLING && mLastOffset > 0.5f) {
                            percent = 0;
                        }
                    }
                    _hideDragView(percent);
                    mLastOffset = positionOffset;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && !mIsCustomAnimator) {
                    isRightSlide = true;
                    mLastOffset = 0;
                    // 如果手动调用退出动画则不做自动启动动画
                    if (mDragStatus == STATUS_EXIT && !mIsDoOutAnim) {
                        _showDragView(mAutoAnimateDelay);
                    }
                }
                status = state;
            }
        };
        ((ViewPager) mMainView).addOnPageChangeListener(mViewPagerListener);
    }

    public int getAutoAnimateDelay() {
        return mAutoAnimateDelay;
    }

    public void setAutoAnimateDelay(int autoAnimateDelay) {
        mAutoAnimateDelay = autoAnimateDelay;
    }

    /**
     * 判断视图是否为 ViewPager 或它的子类
     *
     * @param view View
     * @return
     */
    private boolean _isViewPager(View view) {
        boolean isViewPager = false;
        if (view instanceof ViewPager) {
            isViewPager = true;
        } else {
            ViewParent parent = view.getParent();
            while (parent != null) {
                if (parent instanceof ViewPager) {
                    isViewPager = true;
                    break;
                }
            }
        }
        return isViewPager;
    }

    /***********************************
     * Animation
     ********************************************/

    public static final int SLIDE_BOTTOM = 101;
    public static final int SLIDE_LEFT = 102;
    public static final int SLIDE_RIGHT = 103;
    public static final int FADE = 104;
    public static final int FLIP_X = 105;
    public static final int FLIP_Y = 106;
    public static final int ZOOM = 107;
    public static final int ZOOM_LEFT = 108;
    public static final int ZOOM_RIGHT = 109;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SLIDE_BOTTOM, SLIDE_LEFT, SLIDE_RIGHT, FADE, FLIP_X, FLIP_Y, ZOOM, ZOOM_LEFT, ZOOM_RIGHT})
    public @interface AnimatorMode {
    }

    public int getAnimatorMode() {
        return mAnimPresenter.getAnimatorMode();
    }

    public void setAnimatorMode(@AnimatorMode int animatorMode) {
        mIsCustomAnimator = false;
        mAnimPresenter.setAnimatorMode(animatorMode);
    }

    public int getStartDelay() {
        return mAnimPresenter.getStartDelay();
    }

    public void setStartDelay(int startDelay) {
        mAnimPresenter.setStartDelay(startDelay);
    }

    public int getDuration() {
        return mAnimPresenter.getDuration();
    }

    public void setDuration(int duration) {
        mAnimPresenter.setDuration(duration);
    }

    public Interpolator getInterpolator() {
        return mAnimPresenter.getInterpolator();
    }

    public void setInterpolator(Interpolator interpolator) {
        mAnimPresenter.setInterpolator(interpolator);
    }

    /**
     * 启动进入动画
     */
    public void startInAnim() {
        mIsDoOutAnim = false;
        mAnimPresenter.startInAnim(mDragView);
    }

    /**
     * 启动退出动画
     * 注意：调用了退出动画则默认关闭自动启动动画效果
     */
    public void startOutAnim() {
        mIsDoOutAnim = true;
        mAnimPresenter.startOutAnim(mDragView);
    }

    /**
     * 设置自定义动画
     * @param inAnimator    进入动画
     * @param outAnimator   退出动画
     */
    public void setCustomAnimator(CustomViewAnimator inAnimator, CustomViewAnimator outAnimator) {
        mIsCustomAnimator = true;
        mAnimPresenter.setCustomAnimator(inAnimator, outAnimator);
    }
}
