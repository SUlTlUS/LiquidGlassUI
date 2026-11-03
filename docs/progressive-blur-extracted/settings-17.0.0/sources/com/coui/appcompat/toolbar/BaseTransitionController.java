package com.coui.appcompat.toolbar;

import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.view.View;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes7.dex */
public abstract class BaseTransitionController {
    private static final float BLUR_QUANTIZE_STEP = 0.5f;
    protected static final float MIN_VISIBLE_CHANGE_SCALE = 0.001f;
    protected static final float VIEW_PROP_RESTING_STATE = 0.99f;
    protected final List<COUISpringAnimation> mRunningAnimations = new ArrayList();
    protected final Set<View> mAnimatedViews = new HashSet();
    protected final Map<View, Float> mCurrentBlur = new HashMap();
    protected final Map<View, Float> mAppliedBlurRadius = new HashMap();
    protected final boolean mBlurEnabled = COUIMaterialStateManager.INSTANCE.isBlurEffectEnable();

    private void applyBlurApi31(View view, float f3) {
        float fRound = f3 <= 0.5f ? 0.0f : Math.round(f3 / 0.5f) * 0.5f;
        Float f4 = this.mAppliedBlurRadius.get(view);
        if (f4 == null || Float.compare(f4.floatValue(), fRound) != 0) {
            this.mAppliedBlurRadius.put(view, Float.valueOf(fRound));
            if (fRound <= 0.0f) {
                view.setRenderEffect(null);
            } else {
                view.setRenderEffect(RenderEffect.createBlurEffect(fRound, fRound, Shader.TileMode.CLAMP));
            }
        }
    }

    private void cancelSpringAnimationsInList() {
        for (int size = this.mRunningAnimations.size() - 1; size >= 0; size--) {
            COUISpringAnimation cOUISpringAnimation = this.mRunningAnimations.get(size);
            if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
                cOUISpringAnimation.cancel();
            }
        }
    }

    private static void clearBlurApi31(View view) {
        view.setRenderEffect(null);
    }

    public static void clearBlurEffect(View view) {
        clearBlurApi31(view);
    }

    public void applyBlur(View view, float f3) {
        if (this.mBlurEnabled) {
            applyBlurApi31(view, f3);
        }
    }

    public void cancelAll() {
        cancelSpringAnimationsInList();
        this.mRunningAnimations.clear();
    }

    public abstract boolean isAtRestingState(View view);

    public abstract void resetView(View view);
}