package com.dl7.drag.animate;

import android.animation.Animator;
import android.view.View;

/**
 * Created by long on 2016/9/12.
 * 自定义动画
 */
public abstract class CustomViewAnimator extends BaseViewAnimator {

    @Override
    protected void prepare(View target) {
        mAnimatorSet.playTogether(doAnimator());
    }

    protected abstract Animator doAnimator();
}
