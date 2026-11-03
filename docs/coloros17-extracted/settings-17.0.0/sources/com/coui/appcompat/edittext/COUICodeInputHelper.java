package com.coui.appcompat.edittext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;

/* JADX INFO: loaded from: classes7.dex */
public class COUICodeInputHelper {
    private static final PathInterpolator ANIMATOR_INTERPOLATOR = new COUIMoveEaseInterpolator();
    public static final long INBOX_APPEAR_ANIMATOR_DURATION = 100;
    public static final long INBOX_DELAY_ANIMATOR_DURATION = 33;
    public static final long NUMBER_APPEAR_ANIMATOR_DURATION = 100;
    public static final long NUMBER_DELAY_ANIMATOR_DURATION = 33;
    public static final float NUMBER_SCALE_START = 0.6f;
    private View codeItemView;
    private float endNumberAlpha;
    private boolean isInboxAnimatorRuning;
    private boolean isNumberAnimatorRuning;
    private String mAnimatorNumber;
    private boolean mCurrentInboxAppear;
    private boolean mCurrentNumberAppear;
    private ValueAnimator mInboxAlphaAnimator;
    private ValueAnimator mNumberScaleAnimator;
    private float startNumberAlpha;
    private float startNumberScale = 0.6f;
    private float endNumberScale = 1.0f;
    private float mCurrentNumberAlpha = 1.0f;
    private float mCurrentNumberScale = 1.0f;
    private float mCurrentInboxAlpha = 1.0f;

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICodeInputHelper$1, reason: invalid class name */
    public class AnonymousClass1 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass1() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUICodeInputHelper.access$002(COUICodeInputHelper.this, ((Float) valueAnimator.getAnimatedValue("alphaHolder")).floatValue());
            COUICodeInputHelper.access$102(COUICodeInputHelper.this, ((Float) valueAnimator.getAnimatedValue("scaleHolder")).floatValue());
            COUICodeInputHelper.access$200(COUICodeInputHelper.this).invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICodeInputHelper$2, reason: invalid class name */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        public AnonymousClass2() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            COUICodeInputHelper.access$302(COUICodeInputHelper.this, false);
            COUICodeInputHelper.access$200(COUICodeInputHelper.this).invalidate();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICodeInputHelper.access$302(COUICodeInputHelper.this, false);
            COUICodeInputHelper.access$200(COUICodeInputHelper.this).invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICodeInputHelper$3, reason: invalid class name */
    public class AnonymousClass3 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass3() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUICodeInputHelper.access$402(COUICodeInputHelper.this, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            COUICodeInputHelper.access$200(COUICodeInputHelper.this).invalidate();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.edittext.COUICodeInputHelper$4, reason: invalid class name */
    public class AnonymousClass4 extends AnimatorListenerAdapter {
        public AnonymousClass4() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            COUICodeInputHelper.access$502(COUICodeInputHelper.this, false);
            COUICodeInputHelper.access$200(COUICodeInputHelper.this).invalidate();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICodeInputHelper.access$502(COUICodeInputHelper.this, false);
            COUICodeInputHelper.access$200(COUICodeInputHelper.this).invalidate();
        }
    }

    public COUICodeInputHelper(View view) {
        this.codeItemView = view;
    }

    public static /* synthetic */ float access$002(COUICodeInputHelper cOUICodeInputHelper, float f3) {
        cOUICodeInputHelper.mCurrentNumberAlpha = f3;
        return f3;
    }

    public static /* synthetic */ float access$102(COUICodeInputHelper cOUICodeInputHelper, float f3) {
        cOUICodeInputHelper.mCurrentNumberScale = f3;
        return f3;
    }

    public static /* synthetic */ View access$200(COUICodeInputHelper cOUICodeInputHelper) {
        return cOUICodeInputHelper.codeItemView;
    }

    public static /* synthetic */ boolean access$302(COUICodeInputHelper cOUICodeInputHelper, boolean z2) {
        cOUICodeInputHelper.isNumberAnimatorRuning = z2;
        return z2;
    }

    public static /* synthetic */ float access$402(COUICodeInputHelper cOUICodeInputHelper, float f3) {
        cOUICodeInputHelper.mCurrentInboxAlpha = f3;
        return f3;
    }

    public static /* synthetic */ boolean access$502(COUICodeInputHelper cOUICodeInputHelper, boolean z2) {
        cOUICodeInputHelper.isInboxAnimatorRuning = z2;
        return z2;
    }

    private void cancelInboxAlphaAnimator() {
        ValueAnimator valueAnimator = this.mInboxAlphaAnimator;
        if (valueAnimator == null || !this.isInboxAnimatorRuning) {
            return;
        }
        valueAnimator.cancel();
    }

    private void cancelNumberScaleAnimator() {
        ValueAnimator valueAnimator = this.mNumberScaleAnimator;
        if (valueAnimator == null || !this.isNumberAnimatorRuning) {
            return;
        }
        valueAnimator.cancel();
    }

    private void executeInboxAnimator(boolean z2) {
        if (z2) {
            float f3 = this.mCurrentInboxAlpha;
            if (f3 <= 0.0f || f3 >= 1.0f) {
                this.startNumberAlpha = 0.0f;
            } else {
                this.startNumberAlpha = f3;
            }
            this.endNumberAlpha = 1.0f;
        } else {
            float f4 = this.mCurrentInboxAlpha;
            if (f4 <= 0.0f || f4 >= 1.0f) {
                this.startNumberAlpha = 1.0f;
            } else {
                this.startNumberAlpha = f4;
            }
            this.endNumberAlpha = 0.0f;
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.startNumberAlpha, this.endNumberAlpha);
        this.mInboxAlphaAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(100L);
        this.mInboxAlphaAnimator.setStartDelay(z2 ? 33L : 0L);
        this.mInboxAlphaAnimator.setInterpolator(ANIMATOR_INTERPOLATOR);
        this.mInboxAlphaAnimator.addUpdateListener(new AnonymousClass3());
        this.mInboxAlphaAnimator.addListener(new AnonymousClass4());
        this.mInboxAlphaAnimator.start();
        this.isInboxAnimatorRuning = true;
        this.mCurrentInboxAlpha = this.startNumberAlpha;
    }

    private void executeNumberAnimator(boolean z2) {
        if (z2) {
            float f3 = this.mCurrentNumberAlpha;
            if (f3 <= 0.0f || f3 >= 1.0f) {
                this.startNumberAlpha = 0.0f;
            } else {
                this.startNumberAlpha = f3;
            }
            this.endNumberAlpha = 1.0f;
            this.startNumberScale = 0.6f;
        } else {
            float f4 = this.mCurrentNumberAlpha;
            if (f4 <= 0.0f || f4 >= 1.0f) {
                this.startNumberAlpha = 1.0f;
            } else {
                this.startNumberAlpha = f4;
            }
            this.endNumberAlpha = 0.0f;
            this.startNumberScale = 1.0f;
        }
        this.endNumberScale = 1.0f;
        ValueAnimator valueAnimatorOfPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat("scaleHolder", this.startNumberScale, 1.0f), PropertyValuesHolder.ofFloat("alphaHolder", this.startNumberAlpha, this.endNumberAlpha));
        this.mNumberScaleAnimator = valueAnimatorOfPropertyValuesHolder;
        valueAnimatorOfPropertyValuesHolder.setDuration(100L);
        this.mNumberScaleAnimator.setStartDelay(z2 ? 0L : 33L);
        this.mNumberScaleAnimator.setInterpolator(ANIMATOR_INTERPOLATOR);
        this.mNumberScaleAnimator.addUpdateListener(new AnonymousClass1());
        this.mNumberScaleAnimator.addListener(new AnonymousClass2());
        this.mNumberScaleAnimator.start();
        this.isNumberAnimatorRuning = true;
        this.mCurrentNumberAlpha = this.startNumberAlpha;
        this.mCurrentNumberScale = this.startNumberScale;
    }

    private void setCurrentInboxAppear(boolean z2) {
        this.mCurrentInboxAppear = z2;
    }

    private void setCurrentNumberAppear(boolean z2) {
        this.mCurrentNumberAppear = z2;
    }

    public String getAnimatorNumber() {
        return this.mAnimatorNumber;
    }

    public float getCurrentInboxAlpha() {
        return this.mCurrentInboxAlpha;
    }

    public float getCurrentNumberAlpha() {
        return this.mCurrentNumberAlpha;
    }

    public float getCurrentNumberScale() {
        return this.mCurrentNumberScale;
    }

    public ValueAnimator getInboxAnimator() {
        return this.mInboxAlphaAnimator;
    }

    public ValueAnimator getNumberAnimator() {
        return this.mNumberScaleAnimator;
    }

    public boolean isCurrentNumberAppear() {
        return this.mCurrentNumberAppear;
    }

    public boolean isInboxAnimatorRuning() {
        return this.isInboxAnimatorRuning;
    }

    public boolean isNumberAnimatorRuning() {
        return this.isNumberAnimatorRuning;
    }

    public void startInboxAnimator(boolean z2) {
        setCurrentInboxAppear(z2);
        if (this.isInboxAnimatorRuning) {
            cancelInboxAlphaAnimator();
        }
        executeInboxAnimator(z2);
    }

    public void startNumberAnimator(boolean z2, String str) {
        this.mAnimatorNumber = str;
        setCurrentNumberAppear(z2);
        if (this.isNumberAnimatorRuning) {
            cancelNumberScaleAnimator();
        }
        executeNumberAnimator(z2);
    }
}