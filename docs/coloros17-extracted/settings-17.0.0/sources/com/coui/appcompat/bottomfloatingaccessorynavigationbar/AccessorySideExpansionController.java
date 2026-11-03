package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.View;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.IExpansionViewTouchMotionAnimHost;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.bottomfloatingnavigationbar.BottomFloatingOverlapHelper;

/* JADX INFO: loaded from: classes7.dex */
final class AccessorySideExpansionController {
    private static final float DEFAULT_SPRING_ANIMATION_FACTOR = 10000.0f;
    private static final float EXPANSION_VIEW_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float EXPANSION_VIEW_MIN_SCALE = 0.5f;
    private static final float HIGH_END_ALPHA_SPRING_RESPONSE = 0.35f;
    private static final float HIGH_END_SCALE_SPRING_BOUNCE = 0.35f;
    private static final float HIGH_END_SCALE_SPRING_RESPONSE = 0.55f;
    private static final float HIGH_END_TRANSLATION_X_SPRING_BOUNCE = 0.35f;
    private static final float HIGH_END_TRANSLATION_X_SPRING_RESPONSE = 0.55f;
    private static final float LOW_END_ALPHA_SPRING_RESPONSE = 0.2f;
    private static final float LOW_END_SCALE_SPRING_BOUNCE = 0.3f;
    private static final float LOW_END_SCALE_SPRING_RESPONSE = 0.5f;
    private static final float LOW_END_TRANSLATION_X_SPRING_BOUNCE = 0.3f;
    private static final float LOW_END_TRANSLATION_X_SPRING_RESPONSE = 0.5f;
    private static final float TRANSLATION_X_MIN_VISIBLE_CHANGE = 0.001f;
    private final View mAccessoryView;
    private COUISpringAnimation mAlphaAnim;
    private final AccessoryAlphaDelegate mAlphaDelegate;
    private final Host mHost;
    private boolean mIsActive;
    private final boolean mIsStartSide;
    private Runnable mOverlapAlphaGeometryCallback;
    private COUISpringAnimation mScaleAnim;
    private COUISpringAnimation mTranslationXAnim;
    private float mTranslationXAnimTarget;
    private int mVisibleLayoutLeft;
    private final Rect mFakeBounds = new Rect();
    private final int[] mTmpLocationOnScreen = new int[2];
    private int mLastFakeBoundsLeft = Integer.MIN_VALUE;

    public interface AccessoryAlphaDelegate {
        float getAccessoryViewAlpha(View view);

        void setAccessoryViewAlpha(View view, float f3);
    }

    public interface Host {
        void clearSideMeltBallParams(boolean z2);

        int getAccessorySize();

        RectF getMenuDrawingBounds();

        boolean isSupportMeltBall();
    }

    public AccessorySideExpansionController(boolean z2, Host host, AccessoryAlphaDelegate accessoryAlphaDelegate, View view) {
        this.mIsStartSide = z2;
        this.mHost = host;
        this.mAlphaDelegate = accessoryAlphaDelegate;
        this.mAccessoryView = view;
    }

    public static /* synthetic */ void a(AccessorySideExpansionController accessorySideExpansionController, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        accessorySideExpansionController.checkAllAlphaScaleAnimationsEnded();
    }

    private void applyTranslationToTargetLeft(int i3, boolean z2) {
        float f3 = i3 - this.mVisibleLayoutLeft;
        if (this.mAccessoryView.getVisibility() == 8) {
            z2 = false;
        }
        if (!z2) {
            cancelTranslationAnimIfRunning();
            this.mAccessoryView.setTranslationX(f3);
            this.mLastFakeBoundsLeft = i3;
            onGeometryChangedForAlpha();
            return;
        }
        initTranslationAnim();
        float translationX = this.mAccessoryView.getTranslationX();
        if (this.mIsActive && this.mLastFakeBoundsLeft == Integer.MIN_VALUE) {
            translationX = this.mFakeBounds.left - this.mVisibleLayoutLeft;
            this.mAccessoryView.setTranslationX(translationX);
            onGeometryChangedForAlpha();
        }
        this.mLastFakeBoundsLeft = i3;
        if (this.mTranslationXAnim == null) {
            this.mAccessoryView.setTranslationX(f3);
            onGeometryChangedForAlpha();
            return;
        }
        if (!this.mIsActive && z2) {
            setExpansionViewTouchMotionAnimEnabled(false);
        }
        this.mTranslationXAnim.setStartValue(translationX);
        this.mTranslationXAnimTarget = f3;
        this.mTranslationXAnim.animateToFinalPosition(f3);
    }

    private boolean areAlphaScaleAnimationsIdle() {
        COUISpringAnimation cOUISpringAnimation = this.mAlphaAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            return false;
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mScaleAnim;
        return cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning();
    }

    private void attachOverlapAlphaPolicy() {
        COUISpringAnimation cOUISpringAnimation = this.mAlphaAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mAlphaAnim.cancel();
        }
        this.mOverlapAlphaGeometryCallback = new c(this);
    }

    private void attachSpringAlphaPolicy(boolean z2) {
        this.mOverlapAlphaGeometryCallback = null;
        initAlphaAnim();
        COUISpringAnimation cOUISpringAnimation = this.mAlphaAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setStartValue(this.mAlphaDelegate.getAccessoryViewAlpha(this.mAccessoryView) * 10000.0f);
            this.mAlphaAnim.animateToFinalPosition(z2 ? 10000.0f : 0.0f);
        }
    }

    public static /* synthetic */ void b(AccessorySideExpansionController accessorySideExpansionController, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        accessorySideExpansionController.checkAllAlphaScaleAnimationsEnded();
    }

    public static /* synthetic */ void c(AccessorySideExpansionController accessorySideExpansionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        accessorySideExpansionController.setScaleSnapped(accessorySideExpansionController.mAccessoryView, f3 / 10000.0f);
        accessorySideExpansionController.onGeometryChangedForAlpha();
    }

    private void checkAllAlphaScaleAnimationsEnded() {
        if (!areAlphaScaleAnimationsIdle() || this.mIsActive) {
            return;
        }
        this.mAccessoryView.setEnabled(false);
    }

    private float computeOverlapRatioWithMenu() {
        return BottomFloatingOverlapHelper.computeScaledViewOverlapRatio(this.mAccessoryView, this.mAccessoryView.getParent() instanceof View ? (View) this.mAccessoryView.getParent() : null, this.mHost.getMenuDrawingBounds(), this.mTmpLocationOnScreen);
    }

    public static /* synthetic */ void d(AccessorySideExpansionController accessorySideExpansionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        accessorySideExpansionController.mAlphaDelegate.setAccessoryViewAlpha(accessorySideExpansionController.mAccessoryView, f3 / 10000.0f);
    }

    private void doAlphaScaleAnim(boolean z2) {
        initScaleAnim();
        COUISpringAnimation cOUISpringAnimation = this.mScaleAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setStartValue(this.mAccessoryView.getScaleX() * 10000.0f);
            this.mScaleAnim.animateToFinalPosition(z2 ? 10000.0f : 5000.0f);
        }
        if (willRunTranslationAnim(z2)) {
            attachOverlapAlphaPolicy();
        } else {
            attachSpringAlphaPolicy(z2);
        }
        onGeometryChangedForAlpha();
    }

    public static /* synthetic */ void e(AccessorySideExpansionController accessorySideExpansionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        accessorySideExpansionController.mAccessoryView.setTranslationX(f3);
        accessorySideExpansionController.onGeometryChangedForAlpha();
        accessorySideExpansionController.finishCollapseTranslationIfFullyOverlapped();
    }

    public static /* synthetic */ void f(AccessorySideExpansionController accessorySideExpansionController, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        if (accessorySideExpansionController.mIsActive) {
            accessorySideExpansionController.setExpansionViewTouchMotionAnimEnabled(true);
        } else if (accessorySideExpansionController.shouldClearMeltBall()) {
            accessorySideExpansionController.mHost.clearSideMeltBallParams(accessorySideExpansionController.mIsStartSide);
        }
    }

    private void finishCollapseTranslationIfFullyOverlapped() {
        COUISpringAnimation cOUISpringAnimation;
        if (this.mIsActive || (cOUISpringAnimation = this.mTranslationXAnim) == null || !cOUISpringAnimation.isRunning() || computeOverlapRatioWithMenu() < 1.0f) {
            return;
        }
        float f3 = this.mTranslationXAnimTarget;
        this.mTranslationXAnim.cancel();
        this.mAccessoryView.setTranslationX(f3);
        onGeometryChangedForAlpha();
    }

    public static /* synthetic */ void g(AccessorySideExpansionController accessorySideExpansionController) {
        accessorySideExpansionController.updateAccessoryViewAlphaByOverlap();
    }

    private void initAlphaAnim() {
        if (this.mAlphaAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(this.mHost.isSupportMeltBall() ? 0.35f : 0.2f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mAlphaAnim = spring;
        spring.addUpdateListener(new a(this));
        this.mAlphaAnim.addEndListener(new b(this));
    }

    private void initScaleAnim() {
        if (this.mScaleAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(this.mHost.isSupportMeltBall() ? 0.35f : 0.3f);
        cOUISpringForce.setResponse(this.mHost.isSupportMeltBall() ? 0.55f : 0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mScaleAnim = spring;
        spring.addUpdateListener(new f(this));
        this.mScaleAnim.addEndListener(new g(this));
    }

    private void initTranslationAnim() {
        if (this.mTranslationXAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(this.mHost.isSupportMeltBall() ? 0.35f : 0.3f);
        cOUISpringForce.setResponse(this.mHost.isSupportMeltBall() ? 0.55f : 0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mTranslationXAnim = spring;
        spring.setMinimumVisibleChange(0.001f);
        this.mTranslationXAnim.addUpdateListener(new d(this));
        this.mTranslationXAnim.addEndListener(new e(this));
    }

    private void prepareForShowAnim() {
        COUISpringAnimation cOUISpringAnimation = this.mAlphaAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            COUISpringAnimation cOUISpringAnimation2 = this.mScaleAnim;
            if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
                this.mAccessoryView.setScaleX(0.5f);
                this.mAccessoryView.setScaleY(0.5f);
                this.mAlphaDelegate.setAccessoryViewAlpha(this.mAccessoryView, 0.0f);
            }
        }
    }

    private void setExpansionViewTouchMotionAnimEnabled(boolean z2) {
        KeyEvent.Callback callback = this.mAccessoryView;
        if (callback instanceof IExpansionViewTouchMotionAnimHost) {
            ((IExpansionViewTouchMotionAnimHost) callback).setExpansionViewTouchMotionAnimEnabled(z2);
        }
    }

    private void setScaleSnapped(View view, float f3) {
        float fRound;
        int width = view.getWidth();
        int height = view.getHeight();
        if (width > 0) {
            float f4 = width;
            fRound = Math.round(f4 * f3) / f4;
        } else {
            fRound = f3;
        }
        view.setScaleX(fRound);
        if (height > 0) {
            float f5 = height;
            f3 = Math.round(f3 * f5) / f5;
        }
        view.setScaleY(f3);
    }

    private boolean shouldClearMeltBall() {
        return this.mHost.isSupportMeltBall() && this.mAccessoryView.getVisibility() == 0;
    }

    private void updateAccessoryViewAlphaByOverlap() {
        this.mAlphaDelegate.setAccessoryViewAlpha(this.mAccessoryView, BottomFloatingOverlapHelper.alphaFromOverlapRatio(computeOverlapRatioWithMenu(), 50.0f));
    }

    private boolean willRunTranslationAnim(boolean z2) {
        if (this.mHost.isSupportMeltBall() && this.mAccessoryView.getVisibility() != 8) {
            return z2 || !this.mFakeBounds.isEmpty();
        }
        return false;
    }

    public void applyAlphaScaleState(boolean z2, boolean z3) {
        if (z3) {
            if (z2) {
                prepareForShowAnim();
            }
            doAlphaScaleAnim(z2);
            return;
        }
        clearAlphaPolicy();
        cancelAlphaScaleAnimIfRunning();
        if (z2) {
            this.mAccessoryView.setScaleX(1.0f);
            this.mAccessoryView.setScaleY(1.0f);
            this.mAlphaDelegate.setAccessoryViewAlpha(this.mAccessoryView, 1.0f);
        } else {
            this.mAccessoryView.setScaleX(0.5f);
            this.mAccessoryView.setScaleY(0.5f);
            this.mAlphaDelegate.setAccessoryViewAlpha(this.mAccessoryView, 0.0f);
            this.mAccessoryView.setVisibility(4);
        }
    }

    public void cancelAlphaScaleAnimIfRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mAlphaAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mAlphaAnim.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mScaleAnim;
        if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
            return;
        }
        this.mScaleAnim.cancel();
    }

    public void cancelTranslationAnimIfRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mTranslationXAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mTranslationXAnim.cancel();
    }

    public void clearAlphaPolicy() {
        this.mOverlapAlphaGeometryCallback = null;
    }

    public void computeInactiveFakeBounds(int i3, int i4, int i5, int i6, boolean z2) {
        int accessorySize = this.mHost.getAccessorySize();
        if (this.mIsStartSide) {
            z2 = !z2;
        }
        if (!z2) {
            i3 = (i3 + i4) - accessorySize;
        }
        this.mFakeBounds.set(i3, i5, accessorySize + i3, i6);
    }

    public boolean isActive() {
        return this.mIsActive;
    }

    public void layoutChildAndApplyTranslation(int i3, int i4, boolean z2) {
        int accessorySize = this.mHost.getAccessorySize();
        View view = this.mAccessoryView;
        int i5 = this.mVisibleLayoutLeft;
        view.layout(i5, i3, accessorySize + i5, i4);
        if (!this.mHost.isSupportMeltBall()) {
            cancelTranslationAnimIfRunning();
            this.mAccessoryView.setTranslationX(0.0f);
            onGeometryChangedForAlpha();
            return;
        }
        boolean z3 = this.mIsActive;
        int i6 = z3 ? this.mVisibleLayoutLeft : this.mFakeBounds.left;
        if (z3 || !this.mFakeBounds.isEmpty()) {
            applyTranslationToTargetLeft(i6, z2);
        } else {
            this.mAccessoryView.setTranslationX(0.0f);
            onGeometryChangedForAlpha();
        }
    }

    public void onGeometryChangedForAlpha() {
        Runnable runnable = this.mOverlapAlphaGeometryCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void setActive(boolean z2) {
        if (z2 == this.mIsActive) {
            return;
        }
        this.mIsActive = z2;
        if (z2) {
            this.mAccessoryView.setVisibility(0);
            this.mAccessoryView.setEnabled(true);
        }
    }

    public void setVisibleLayoutLeft(int i3) {
        this.mVisibleLayoutLeft = i3;
    }
}