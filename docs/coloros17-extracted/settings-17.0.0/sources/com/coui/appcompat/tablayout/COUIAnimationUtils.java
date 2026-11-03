package com.coui.appcompat.tablayout;

import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.coui.appcompat.animation.COUILinearInterpolator;

/* JADX INFO: loaded from: classes7.dex */
public class COUIAnimationUtils {
    private static final int SCALE_STANDARD = 100;
    public static final Interpolator LINEAR_INTERPOLATOR = new COUILinearInterpolator();
    public static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
    public static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR = new FastOutLinearInInterpolator();
    public static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();
    public static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    public static int getScaleStandard(Context context) {
        return (int) (context.getResources().getDisplayMetrics().density * 100.0f);
    }

    public static float lerp(float f3, float f4, float f5) {
        return f3 + (f5 * (f4 - f3));
    }

    public static int lerp(int i3, int i4, float f3) {
        return i3 + Math.round(f3 * (i4 - i3));
    }
}