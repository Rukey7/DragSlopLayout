package com.dl7.drag;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Bitmap工具类主要包括获取Bitmap和对Bitmap的操作
 */
final public class BitmapUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private BitmapUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap 源Bitmap
     * @param w 宽
     * @param h 高
     * @return 目标Bitmap
     */
    public static Bitmap zoom(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

}