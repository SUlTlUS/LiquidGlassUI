package com.coui.appcompat.panel;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.coui.appcompat.animation.COUIEaseInterpolator;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPanelPressHelper {
    private static final String BG_ALPHA = "bgAlpha";
    private static final PathInterpolator COUI_EASE_INTERPOLATOR = new COUIEaseInterpolator();
    private static final int LOAD_BG_DURATION = 200;
    private Float bgAlpha = Float.valueOf(0.0f);
    private ValueAnimator pressAnim;
    private ValueAnimator releaseAnim;

    public static /* synthetic */ void a(COUIPanelPressHelper cOUIPanelPressHelper, View view, ValueAnimator valueAnimator) {
        cOUIPanelPressHelper.getClass();
        Float f3 = (Float) valueAnimator.getAnimatedValue(BG_ALPHA);
        cOUIPanelPressHelper.bgAlpha = f3;
        view.setAlpha(f3.floatValue());
    }

    public static /* synthetic */ void b(COUIPanelPressHelper cOUIPanelPressHelper, View view, ValueAnimator valueAnimator) {
        cOUIPanelPressHelper.getClass();
        Float f3 = (Float) valueAnimator.getAnimatedValue(BG_ALPHA);
        cOUIPanelPressHelper.bgAlpha = f3;
        view.setAlpha(f3.floatValue());
    }

    private void cancelAnim(ValueAnimator valueAnimator) {
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        valueAnimator.cancel();
    }

    public void endAnim(View view) {
        cancelAnim(this.pressAnim);
        ValueAnimator valueAnimatorOfPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat(BG_ALPHA, this.bgAlpha.floatValue(), 0.0f));
        this.releaseAnim = valueAnimatorOfPropertyValuesHolder;
        valueAnimatorOfPropertyValuesHolder.setInterpolator(COUI_EASE_INTERPOLATOR);
        this.releaseAnim.setDuration(200L);
        this.releaseAnim.addUpdateListener(new f(this, view));
        this.releaseAnim.start();
    }

    public void startAnim(View view) {
        cancelAnim(this.releaseAnim);
        ValueAnimator valueAnimatorOfPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat(BG_ALPHA, 0.0f, 1.0f));
        this.pressAnim = valueAnimatorOfPropertyValuesHolder;
        valueAnimatorOfPropertyValuesHolder.setInterpolator(COUI_EASE_INTERPOLATOR);
        this.pressAnim.setDuration(200L);
        this.pressAnim.addUpdateListener(new e(this, view));
        this.pressAnim.start();
    }
}