package com.dl7.simple.drag.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by long on 2017/1/5.
 */

public final class AnimateHelper {

    private AnimateHelper() {
        throw new AssertionError();
    }


    /**
     * 垂直偏移动画
     * @param view
     * @param startY
     * @param endY
     * @param duration
     * @return
     */
    public static Animator doMoveVertical(View view, int startY, int endY, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", startY, endY).setDuration(duration);
        animator.start();
        return animator;
    }


    /**
     * 动画是否在运行
     * @param animator
     */
    public static boolean isRunning(Animator animator) {
        return animator != null && animator.isRunning();
    }

    /**
     * 启动动画
     * @param animator
     */
    public static void startAnimator(Animator animator) {
        if (animator != null && !animator.isRunning()) {
            animator.start();
        }
    }

    /**
     * 停止动画
     * @param animator
     */
    public static void stopAnimator(Animator animator) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    /**
     * 删除动画
     * @param animator
     */
    public static void deleteAnimator(Animator animator) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        animator = null;
    }
}
