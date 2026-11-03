package com.coui.appcompat.bottomfloatingnavigationbar;

import android.graphics.RectF;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final class BottomFloatingOverlapHelper {
    public static final float ALPHA_FADE_START_OVERLAP_PERCENT = 50.0f;

    private BottomFloatingOverlapHelper() {
    }

    public static float alphaFromOverlapRatio(float f3, float f4) {
        float fMax = Math.max(0.0f, Math.min(1.0f, f4 / 100.0f));
        float f5 = 1.0f - fMax;
        if (f5 <= 0.0f || f3 <= fMax) {
            return 1.0f;
        }
        return 1.0f - ((f3 - fMax) / f5);
    }

    public static float computeScaledViewOverlapRatio(View view, View view2, RectF rectF, int[] iArr) {
        if (view != null && view2 != null && rectF != null && !rectF.isEmpty() && iArr.length >= 2) {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width > 0 && height > 0) {
                view.getLocationOnScreen(iArr);
                float scaleX = view.getScaleX();
                float scaleY = view.getScaleY();
                float f3 = iArr[0];
                float f4 = iArr[1];
                float f5 = width * scaleX;
                float f6 = height * scaleY;
                view2.getLocationOnScreen(iArr);
                int i3 = iArr[0];
                float f7 = i3 + rectF.left;
                int i4 = iArr[1];
                float fMax = Math.max(0.0f, Math.min(f3 + f5, i3 + rectF.right) - Math.max(f3, f7)) * Math.max(0.0f, Math.min(f4 + f6, i4 + rectF.bottom) - Math.max(f4, i4 + rectF.top));
                float f8 = f5 * f6;
                if (f8 > 0.0f) {
                    return Math.min(1.0f, fMax / f8);
                }
            }
        }
        return 0.0f;
    }
}