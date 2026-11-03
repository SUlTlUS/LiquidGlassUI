package com.coui.appcompat.progressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.content.res.ResourcesCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.coui.appcompat.animation.COUIInEaseInterpolator;
import com.coui.appcompat.animation.COUILinearInterpolator;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.coui.appcompat.animation.COUIOutEaseInterpolator;
import com.support.progressbar.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUICircularProgressDrawable extends Drawable {
    private static final int ACCURACY = 100;
    private static final boolean DEBUG = false;
    private static final float DEFAULT_ALPHA_START_FRACTION = 0.7f;
    private static final long DEFAULT_DELAY = 200;
    private static final long DEFAULT_ERROR_DURATION = 350;
    private static final long DEFAULT_ICON_PAUSE_DURATION = 300;
    private static final long DEFAULT_ICON_RESUME_DURATION = 200;
    private static final float DEFAULT_MIN_PROGRESS_VALUE = 1.0E-4f;
    private static final long DEFAULT_PROGRESS_PAUSE_DURATION = 200;
    private static final long DEFAULT_PROGRESS_RESUME_DURATION = 200;
    private static final int FULL_ALPHA = 255;
    private static final int FULL_DEGREE = 360;
    private static final int ORIGINAL_ANGLE = -90;
    private static final String TAG = "COUICircularDrawable";
    private int mActualProgress;
    private float mCenterX;
    private float mCenterY;
    private AnimatorSet mErrorAnimatorSet;
    private float mErrorIconCircleBias;
    private float mErrorIconCircleRadius;
    private int mErrorIconColor;
    private float mErrorIconRectBias;
    private float mErrorIconRectHeight;
    private float mErrorIconRectWidth;
    private View mHostView;
    private ValueAnimator mIconErrorAnimator;
    private float mIconErrorScale;
    private Paint mIconPaint;
    private ValueAnimator mIconPauseAnimator;
    private float mIconPauseScale;
    private ValueAnimator mIconRecoverAnimator;
    private ValueAnimator mIconResumeAnimator;
    private OnProgressChangedListener mOnProgressChangedListener;
    private OnProgressStateAnimatorListener mOnProgressStateAnimatorListener;
    private AnimatorSet mPauseAnimatorSet;
    private int mPauseIconColor;
    private float mPauseIconRectGap;
    private float mPauseIconRectHeight;
    private float mPauseIconRectRadius;
    private float mPauseIconRectWidth;
    private float mProgressBarOuterDiameter;
    private float mProgressBarStrokeWidth;
    private ValueAnimator mProgressEnlargeAnimator;
    private Paint mProgressPaint;
    private ValueAnimator mProgressPauseAnimator;
    private ProgressBarStyleProperty mProgressProperty;
    private ValueAnimator mProgressResumeAnimator;
    private ValueAnimator mProgressShrinkAnimator;
    private AnimatorSet mRecoverAnimatorSet;
    private AnimatorSet mResumeAnimatorSet;
    private float mShadowRadius;
    private float mShadowXBias;
    private float mShadowYBias;
    private SpringAnimation mSpringAnimation;
    private Paint mTrackPaint;
    private ProgressBarStyleProperty mTrackProperty;
    private float mVisualProgress;
    private static final Interpolator DEFAULT_LINEAR_INTERPOLATOR = new COUILinearInterpolator();
    private static final Interpolator DEFAULT_IN_EASE_INTERPOLATOR = new COUIInEaseInterpolator();
    private static final Interpolator DEFAULT_OUT_EASE_INTERPOLATOR = new COUIOutEaseInterpolator();
    private static final Interpolator DEFAULT_MOVE_EASE_INTERPOLATOR = new COUIMoveEaseInterpolator();
    private static final ArgbEvaluator ColorEvaluator = new ArgbEvaluator();
    private static final FloatPropertyCompat<COUICircularProgressDrawable> VISUAL_PROGRESS = new AnonymousClass1("visualProgress");
    private int mGlobalAlpha = 255;
    private int mProgressBarAlpha = 255;
    private int mMax = 100;
    private int mCurrentPauseIconAlpha = 0;
    private int mCurrentErrorIconAlpha = 0;
    private int mShadowColor = 0;
    private boolean mAnimating = false;

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUICircularProgressDrawable$1, reason: invalid class name */
    public class AnonymousClass1 extends FloatPropertyCompat<COUICircularProgressDrawable> {
        public AnonymousClass1(String str) {
            super(str);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ float getValue(COUICircularProgressDrawable cOUICircularProgressDrawable) {
            return getValue2(cOUICircularProgressDrawable);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public /* bridge */ /* synthetic */ void setValue(COUICircularProgressDrawable cOUICircularProgressDrawable, float f3) {
            setValue2(cOUICircularProgressDrawable, f3);
        }

        /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
        public float getValue2(COUICircularProgressDrawable cOUICircularProgressDrawable) {
            return cOUICircularProgressDrawable.getVisualProgress();
        }

        /* JADX INFO: renamed from: setValue, reason: avoid collision after fix types in other method */
        public void setValue2(COUICircularProgressDrawable cOUICircularProgressDrawable, float f3) {
            COUICircularProgressDrawable.access$000(cOUICircularProgressDrawable, f3);
            COUICircularProgressDrawable.access$100(cOUICircularProgressDrawable);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUICircularProgressDrawable$2, reason: invalid class name */
    public class AnonymousClass2 implements Animator.AnimatorListener {
        public AnonymousClass2() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, false);
            if (COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this) != null) {
                COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this).onPauseAnimationEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, true);
            if (COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this) != null) {
                COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this).onPauseAnimationStart();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUICircularProgressDrawable$3, reason: invalid class name */
    public class AnonymousClass3 implements Animator.AnimatorListener {
        public AnonymousClass3() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, false);
            if (COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this) != null) {
                COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this).onResumeAnimationEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, true);
            if (COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this) != null) {
                COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this).onResumeAnimationStart();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUICircularProgressDrawable$4, reason: invalid class name */
    public class AnonymousClass4 implements Animator.AnimatorListener {
        public AnonymousClass4() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, false);
            if (COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this) != null) {
                COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this).onErrorAnimationEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, true);
            if (COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this) != null) {
                COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this).onErrorAnimationStart();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.progressbar.COUICircularProgressDrawable$5, reason: invalid class name */
    public class AnonymousClass5 implements Animator.AnimatorListener {
        public AnonymousClass5() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, false);
            if (COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this) != null) {
                COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this).onRecoverAnimationEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUICircularProgressDrawable.access$202(COUICircularProgressDrawable.this, true);
            if (COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this) != null) {
                COUICircularProgressDrawable.access$300(COUICircularProgressDrawable.this).onRecoverAnimationStart();
            }
        }
    }

    public interface OnProgressChangedListener {
        default void onProgressChanged(int i3) {
        }

        default void onVisualProgressChanged(float f3) {
        }
    }

    public interface OnProgressStateAnimatorListener {
        default void onErrorAnimationEnd() {
        }

        default void onErrorAnimationStart() {
        }

        default void onPauseAnimationEnd() {
        }

        default void onPauseAnimationStart() {
        }

        default void onRecoverAnimationEnd() {
        }

        default void onRecoverAnimationStart() {
        }

        default void onResumeAnimationEnd() {
        }

        default void onResumeAnimationStart() {
        }
    }

    public static class ProgressBarStyleProperty {
        private int mCurrentBarColor;
        private float mProgressBarCurrentOuterDiameter;
        private float mProgressBarCurrentStrokeWidth;
        private int mProgressBarErrorColor;
        private float mProgressBarErrorOuterDiameter = Float.MIN_VALUE;
        private float mProgressBarErrorStrokeWidth = Float.MIN_VALUE;
        private float mProgressBarCenterX = 0.0f;
        private float mProgressBarCenterY = 0.0f;
        private float mProgressBarStrokeWidth = 0.0f;
        private float mProgressBarOuterDiameter = 0.0f;
        private int mProgressBarColor = 0;

        public int getCurrentBarColor() {
            return this.mCurrentBarColor;
        }

        public float getProgressBarCenterX() {
            return this.mProgressBarCenterX;
        }

        public float getProgressBarCenterY() {
            return this.mProgressBarCenterY;
        }

        public int getProgressBarColor() {
            return this.mProgressBarColor;
        }

        public float getProgressBarCurrentOuterDiameter() {
            return this.mProgressBarCurrentOuterDiameter;
        }

        public float getProgressBarCurrentStrokeWidth() {
            return this.mProgressBarCurrentStrokeWidth;
        }

        public int getProgressBarErrorColor() {
            return this.mProgressBarErrorColor;
        }

        public float getProgressBarErrorOuterDiameter() {
            return this.mProgressBarErrorStrokeWidth == Float.MIN_VALUE ? this.mProgressBarOuterDiameter : this.mProgressBarErrorOuterDiameter;
        }

        public float getProgressBarErrorStrokeWidth() {
            float f3 = this.mProgressBarErrorStrokeWidth;
            return f3 == Float.MIN_VALUE ? this.mProgressBarStrokeWidth : f3;
        }

        public float getProgressBarOuterDiameter() {
            return this.mProgressBarOuterDiameter;
        }

        public float getProgressBarStrokeWidth() {
            return this.mProgressBarStrokeWidth;
        }

        public void setCurrentBarColor(int i3) {
            this.mCurrentBarColor = i3;
        }

        public void setProgressBarCenterX(float f3) {
            this.mProgressBarCenterX = f3;
        }

        public void setProgressBarCenterY(float f3) {
            this.mProgressBarCenterY = f3;
        }

        public void setProgressBarColor(int i3) {
            this.mProgressBarColor = i3;
            this.mCurrentBarColor = i3;
        }

        public void setProgressBarCurrentOuterDiameter(float f3) {
            if (f3 < 0.0f) {
                Log.w(COUICircularProgressDrawable.TAG, "Progress bar outer diameter should be greater than 0 !");
            }
            this.mProgressBarCurrentOuterDiameter = Math.max(0.0f, f3);
        }

        public void setProgressBarCurrentStrokeWidth(float f3) {
            if (f3 < 0.0f) {
                Log.w(COUICircularProgressDrawable.TAG, "Progress bar stroke width should be greater than 0 !");
            }
            this.mProgressBarCurrentStrokeWidth = Math.max(0.0f, f3);
        }

        public void setProgressBarErrorColor(int i3) {
            this.mProgressBarErrorColor = i3;
        }

        public void setProgressBarErrorOuterDiameter(float f3) {
            if (f3 < 0.0f) {
                Log.w(COUICircularProgressDrawable.TAG, "Progress bar outer diameter should be greater than 0 !");
            }
            this.mProgressBarErrorOuterDiameter = Math.max(0.0f, f3);
        }

        public void setProgressBarErrorStrokeWidth(float f3) {
            if (f3 < 0.0f) {
                Log.w(COUICircularProgressDrawable.TAG, "Progress bar stroke width should be greater than 0 !");
            }
            this.mProgressBarErrorStrokeWidth = Math.max(0.0f, f3);
        }

        public void setProgressBarOuterDiameter(float f3) {
            if (f3 < 0.0f) {
                Log.w(COUICircularProgressDrawable.TAG, "Progress bar outer diameter should be greater than 0 !");
            }
            this.mProgressBarOuterDiameter = Math.max(0.0f, f3);
        }

        public void setProgressBarStrokeWidth(float f3) {
            if (f3 < 0.0f) {
                Log.w(COUICircularProgressDrawable.TAG, "Progress bar stroke width should be greater than 0 !");
            }
            this.mProgressBarStrokeWidth = Math.max(0.0f, f3);
        }
    }

    public COUICircularProgressDrawable(Context context) {
        initAttr(context);
        initPaint();
        initAnimator();
    }

    public static /* synthetic */ void a(COUICircularProgressDrawable cOUICircularProgressDrawable, ValueAnimator valueAnimator) {
        cOUICircularProgressDrawable.getClass();
        float animatedFraction = valueAnimator.getAnimatedFraction();
        cOUICircularProgressDrawable.mCurrentPauseIconAlpha = (int) (255.0f * animatedFraction);
        cOUICircularProgressDrawable.mIconPauseScale = (animatedFraction * 0.3f) + 0.7f;
        cOUICircularProgressDrawable.invalidateSelf();
    }

    public static /* synthetic */ void access$000(COUICircularProgressDrawable cOUICircularProgressDrawable, float f3) {
        cOUICircularProgressDrawable.setVisualProgress(f3);
    }

    public static /* synthetic */ void access$100(COUICircularProgressDrawable cOUICircularProgressDrawable) {
        cOUICircularProgressDrawable.notifyVisualProgressChanged();
    }

    public static /* synthetic */ boolean access$202(COUICircularProgressDrawable cOUICircularProgressDrawable, boolean z2) {
        cOUICircularProgressDrawable.mAnimating = z2;
        return z2;
    }

    public static /* synthetic */ OnProgressStateAnimatorListener access$300(COUICircularProgressDrawable cOUICircularProgressDrawable) {
        return cOUICircularProgressDrawable.mOnProgressStateAnimatorListener;
    }

    private float actual2VisualProgress(float f3) {
        int i3 = this.mMax;
        return (((int) ((f3 * 100.0f) / i3)) / 100.0f) * i3;
    }

    public static /* synthetic */ void b(COUICircularProgressDrawable cOUICircularProgressDrawable, ValueAnimator valueAnimator) {
        cOUICircularProgressDrawable.getClass();
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f3 = 1.0f - animatedFraction;
        float progressBarErrorOuterDiameter = cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorOuterDiameter() + ((cOUICircularProgressDrawable.mProgressProperty.getProgressBarOuterDiameter() - cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorOuterDiameter()) * f3);
        float progressBarErrorOuterDiameter2 = cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorOuterDiameter() + ((cOUICircularProgressDrawable.mTrackProperty.getProgressBarOuterDiameter() - cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorOuterDiameter()) * f3);
        float progressBarErrorStrokeWidth = cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorStrokeWidth() + ((cOUICircularProgressDrawable.mProgressProperty.getProgressBarStrokeWidth() - cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorStrokeWidth()) * f3);
        float progressBarErrorStrokeWidth2 = cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorStrokeWidth() + (f3 * (cOUICircularProgressDrawable.mTrackProperty.getProgressBarStrokeWidth() - cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorStrokeWidth()));
        ArgbEvaluator argbEvaluator = ColorEvaluator;
        int iIntValue = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(cOUICircularProgressDrawable.mTrackProperty.getProgressBarColor()), Integer.valueOf(cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorColor()))).intValue();
        int iIntValue2 = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(cOUICircularProgressDrawable.mProgressProperty.getProgressBarColor()), Integer.valueOf(cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorColor()))).intValue();
        cOUICircularProgressDrawable.mProgressProperty.setProgressBarCurrentOuterDiameter(progressBarErrorOuterDiameter);
        cOUICircularProgressDrawable.mProgressProperty.setProgressBarCurrentStrokeWidth(progressBarErrorStrokeWidth);
        cOUICircularProgressDrawable.mProgressProperty.setCurrentBarColor(iIntValue2);
        cOUICircularProgressDrawable.mTrackProperty.setProgressBarCurrentOuterDiameter(progressBarErrorOuterDiameter2);
        cOUICircularProgressDrawable.mTrackProperty.setProgressBarCurrentStrokeWidth(progressBarErrorStrokeWidth2);
        cOUICircularProgressDrawable.mTrackProperty.setCurrentBarColor(iIntValue);
        cOUICircularProgressDrawable.invalidateSelf();
    }

    public static /* synthetic */ void c(COUICircularProgressDrawable cOUICircularProgressDrawable, ValueAnimator valueAnimator) {
        cOUICircularProgressDrawable.getClass();
        float animatedFraction = 1.0f - valueAnimator.getAnimatedFraction();
        cOUICircularProgressDrawable.mIconErrorScale = (0.3f * animatedFraction) + 0.7f;
        cOUICircularProgressDrawable.mCurrentErrorIconAlpha = (int) (animatedFraction * 255.0f);
        cOUICircularProgressDrawable.invalidateSelf();
    }

    public static /* synthetic */ void d(COUICircularProgressDrawable cOUICircularProgressDrawable, ValueAnimator valueAnimator) {
        cOUICircularProgressDrawable.getClass();
        float animatedFraction = valueAnimator.getAnimatedFraction();
        cOUICircularProgressDrawable.mIconErrorScale = (0.3f * animatedFraction) + 0.7f;
        cOUICircularProgressDrawable.mCurrentErrorIconAlpha = (int) (animatedFraction * 255.0f);
        cOUICircularProgressDrawable.invalidateSelf();
    }

    private void drawErrorIcon(Canvas canvas) {
        int i3 = this.mCurrentErrorIconAlpha;
        if (i3 != 0) {
            canvas.saveLayerAlpha(0.0f, 0.0f, this.mCenterX * 2.0f, this.mCenterY * 2.0f, i3);
            float f3 = this.mIconErrorScale;
            canvas.scale(f3, f3, this.mCenterX, this.mCenterY);
            this.mIconPaint.setColor(this.mErrorIconColor);
            float f4 = this.mCenterX;
            float f5 = this.mErrorIconRectWidth;
            float f6 = this.mCenterY;
            float f7 = this.mErrorIconRectBias;
            canvas.drawRect(f4 - (f5 / 2.0f), f6 - f7, f4 + (f5 / 2.0f), (f6 - f7) + this.mErrorIconRectHeight, this.mIconPaint);
            canvas.drawCircle(this.mCenterX, this.mCenterY + this.mErrorIconCircleBias, this.mErrorIconCircleRadius, this.mIconPaint);
            canvas.restore();
        }
    }

    private void drawPauseIcon(Canvas canvas) {
        int i3 = this.mCurrentPauseIconAlpha;
        if (i3 != 0) {
            canvas.saveLayerAlpha(0.0f, 0.0f, this.mCenterX * 2.0f, this.mCenterY * 2.0f, i3);
            float f3 = this.mIconPauseScale;
            canvas.scale(f3, f3, this.mCenterX, this.mCenterY);
            this.mIconPaint.setColor(this.mPauseIconColor);
            float f4 = this.mCenterX;
            float f5 = f4 - this.mPauseIconRectWidth;
            float f6 = this.mPauseIconRectGap;
            float f7 = f5 - (f6 / 2.0f);
            float f8 = this.mCenterY;
            float f9 = this.mPauseIconRectHeight;
            float f10 = this.mPauseIconRectRadius;
            canvas.drawRoundRect(f7, f8 - (f9 / 2.0f), f4 - (f6 / 2.0f), f8 + (f9 / 2.0f), f10, f10, this.mIconPaint);
            float f11 = this.mCenterX;
            float f12 = this.mPauseIconRectGap;
            float f13 = this.mCenterY;
            float f14 = this.mPauseIconRectHeight;
            float f15 = this.mPauseIconRectRadius;
            canvas.drawRoundRect(f11 + (f12 / 2.0f), f13 - (f14 / 2.0f), f11 + this.mPauseIconRectWidth + (f12 / 2.0f), f13 + (f14 / 2.0f), f15, f15, this.mIconPaint);
            canvas.restore();
        }
    }

    private void drawProgress(Canvas canvas) {
        float progressBarCurrentOuterDiameter = (this.mTrackProperty.getProgressBarCurrentOuterDiameter() - this.mTrackProperty.getProgressBarCurrentStrokeWidth()) / 2.0f;
        float progressBarCurrentOuterDiameter2 = (this.mProgressProperty.getProgressBarCurrentOuterDiameter() - this.mProgressProperty.getProgressBarCurrentStrokeWidth()) / 2.0f;
        int i3 = this.mProgressBarAlpha;
        if (i3 != 255) {
            canvas.saveLayerAlpha(0.0f, 0.0f, this.mCenterX * 2.0f, this.mCenterY * 2.0f, i3);
        } else {
            canvas.save();
        }
        canvas.drawCircle(this.mCenterX, this.mCenterY, progressBarCurrentOuterDiameter, this.mTrackPaint);
        canvas.rotate(-90.0f, this.mCenterX, this.mCenterY);
        canvas.drawArc(this.mProgressProperty.getProgressBarCenterX() - progressBarCurrentOuterDiameter2, this.mProgressProperty.getProgressBarCenterY() - progressBarCurrentOuterDiameter2, this.mProgressProperty.getProgressBarCenterX() + progressBarCurrentOuterDiameter2, this.mProgressProperty.getProgressBarCenterY() + progressBarCurrentOuterDiameter2, 0.0f, Math.max(1.0E-4f, (this.mVisualProgress * 360.0f) / this.mMax), false, this.mProgressPaint);
        canvas.restore();
    }

    public static /* synthetic */ void e(COUICircularProgressDrawable cOUICircularProgressDrawable, DynamicAnimation dynamicAnimation, float f3, float f4) {
        cOUICircularProgressDrawable.invalidateSelf();
    }

    public static /* synthetic */ void f(COUICircularProgressDrawable cOUICircularProgressDrawable, ValueAnimator valueAnimator) {
        cOUICircularProgressDrawable.getClass();
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float progressBarErrorOuterDiameter = cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorOuterDiameter() + ((cOUICircularProgressDrawable.mProgressProperty.getProgressBarOuterDiameter() - cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorOuterDiameter()) * animatedFraction);
        float progressBarErrorOuterDiameter2 = cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorOuterDiameter() + ((cOUICircularProgressDrawable.mTrackProperty.getProgressBarOuterDiameter() - cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorOuterDiameter()) * animatedFraction);
        float progressBarErrorStrokeWidth = cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorStrokeWidth() + ((cOUICircularProgressDrawable.mProgressProperty.getProgressBarStrokeWidth() - cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorStrokeWidth()) * animatedFraction);
        float progressBarErrorStrokeWidth2 = cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorStrokeWidth() + ((cOUICircularProgressDrawable.mTrackProperty.getProgressBarStrokeWidth() - cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorStrokeWidth()) * animatedFraction);
        ArgbEvaluator argbEvaluator = ColorEvaluator;
        int iIntValue = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(cOUICircularProgressDrawable.mTrackProperty.getProgressBarErrorColor()), Integer.valueOf(cOUICircularProgressDrawable.mTrackProperty.getProgressBarColor()))).intValue();
        int iIntValue2 = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(cOUICircularProgressDrawable.mProgressProperty.getProgressBarErrorColor()), Integer.valueOf(cOUICircularProgressDrawable.mProgressProperty.getProgressBarColor()))).intValue();
        cOUICircularProgressDrawable.mProgressProperty.setProgressBarCurrentOuterDiameter(progressBarErrorOuterDiameter);
        cOUICircularProgressDrawable.mProgressProperty.setProgressBarCurrentStrokeWidth(progressBarErrorStrokeWidth);
        cOUICircularProgressDrawable.mProgressProperty.setCurrentBarColor(iIntValue2);
        cOUICircularProgressDrawable.mTrackProperty.setProgressBarCurrentOuterDiameter(progressBarErrorOuterDiameter2);
        cOUICircularProgressDrawable.mTrackProperty.setProgressBarCurrentStrokeWidth(progressBarErrorStrokeWidth2);
        cOUICircularProgressDrawable.mTrackProperty.setCurrentBarColor(iIntValue);
        cOUICircularProgressDrawable.invalidateSelf();
    }

    public static /* synthetic */ void g(COUICircularProgressDrawable cOUICircularProgressDrawable, ValueAnimator valueAnimator) {
        cOUICircularProgressDrawable.getClass();
        cOUICircularProgressDrawable.mCurrentPauseIconAlpha = (int) ((1.0f - valueAnimator.getAnimatedFraction()) * 255.0f);
        cOUICircularProgressDrawable.mIconPauseScale = 1.0f;
        cOUICircularProgressDrawable.invalidateSelf();
    }

    public static /* synthetic */ void h(COUICircularProgressDrawable cOUICircularProgressDrawable, ValueAnimator valueAnimator) {
        cOUICircularProgressDrawable.getClass();
        cOUICircularProgressDrawable.mProgressBarAlpha = (int) ((1.0f - valueAnimator.getAnimatedFraction()) * 255.0f);
        cOUICircularProgressDrawable.invalidateSelf();
    }

    public static /* synthetic */ void i(COUICircularProgressDrawable cOUICircularProgressDrawable, ValueAnimator valueAnimator) {
        cOUICircularProgressDrawable.getClass();
        cOUICircularProgressDrawable.mProgressBarAlpha = (int) (valueAnimator.getAnimatedFraction() * 255.0f);
        cOUICircularProgressDrawable.invalidateSelf();
    }

    private void initAnimator() {
        initSpring();
        initPauseAnimator();
        initResumeAnimator();
        initErrorAnimator();
        initRecoverAnimator();
    }

    private void initAttr(Context context) {
        this.mPauseIconRectWidth = context.getResources().getDimension(R.dimen.coui_circular_progress_pause_icon_rect_width);
        this.mPauseIconRectHeight = context.getResources().getDimension(R.dimen.coui_circular_progress_pause_icon_rect_height);
        this.mPauseIconRectRadius = context.getResources().getDimension(R.dimen.coui_circular_progress_pause_icon_rect_radius);
        this.mPauseIconRectGap = context.getResources().getDimension(R.dimen.coui_circular_progress_pause_icon_rect_gap);
        this.mErrorIconRectWidth = context.getResources().getDimension(R.dimen.coui_circular_progress_error_icon_rect_width);
        this.mErrorIconRectHeight = context.getResources().getDimension(R.dimen.coui_circular_progress_error_icon_rect_height);
        this.mErrorIconRectBias = context.getResources().getDimension(R.dimen.coui_circular_progress_error_icon_rect_bias);
        this.mErrorIconCircleRadius = context.getResources().getDimension(R.dimen.coui_circular_progress_error_icon_circle_radius);
        this.mErrorIconCircleBias = context.getResources().getDimension(R.dimen.coui_circular_progress_error_icon_circle_bias);
        this.mShadowRadius = context.getResources().getDimension(R.dimen.coui_circular_progress_shadow_radius);
        this.mShadowXBias = context.getResources().getDimension(R.dimen.coui_circular_progress_shadow_x_bias);
        this.mShadowYBias = context.getResources().getDimension(R.dimen.coui_circular_progress_shadow_y_bias);
        this.mShadowColor = ResourcesCompat.getColor(context.getResources(), R.color.coui_circular_progress_shadow_color, context.getTheme());
    }

    private void initErrorAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mProgressShrinkAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(DEFAULT_ERROR_DURATION);
        ValueAnimator valueAnimator = this.mProgressShrinkAnimator;
        Interpolator interpolator = DEFAULT_MOVE_EASE_INTERPOLATOR;
        valueAnimator.setInterpolator(interpolator);
        this.mProgressShrinkAnimator.addUpdateListener(new f(this));
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mIconErrorAnimator = valueAnimatorOfFloat2;
        valueAnimatorOfFloat2.setDuration(DEFAULT_ERROR_DURATION);
        this.mIconErrorAnimator.setInterpolator(interpolator);
        this.mIconErrorAnimator.addUpdateListener(new g(this));
        AnimatorSet animatorSet = new AnimatorSet();
        this.mErrorAnimatorSet = animatorSet;
        animatorSet.playTogether(this.mIconErrorAnimator, this.mProgressShrinkAnimator);
        this.mErrorAnimatorSet.addListener(new AnonymousClass4());
    }

    private void initPaint() {
        Paint paint = new Paint(1);
        this.mTrackPaint = paint;
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        this.mTrackProperty = new ProgressBarStyleProperty();
        Paint paint2 = new Paint(1);
        this.mProgressPaint = paint2;
        paint2.setStrokeCap(Paint.Cap.ROUND);
        this.mProgressPaint.setStyle(style);
        this.mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.mProgressProperty = new ProgressBarStyleProperty();
        Paint paint3 = new Paint(1);
        this.mIconPaint = paint3;
        paint3.setStyle(Paint.Style.FILL);
    }

    private void initPauseAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mProgressPauseAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(200L);
        this.mProgressPauseAnimator.setInterpolator(DEFAULT_LINEAR_INTERPOLATOR);
        this.mProgressPauseAnimator.addUpdateListener(new a(this));
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mIconPauseAnimator = valueAnimatorOfFloat2;
        valueAnimatorOfFloat2.setStartDelay(200L);
        this.mIconPauseAnimator.setDuration(300L);
        this.mIconPauseAnimator.setInterpolator(DEFAULT_IN_EASE_INTERPOLATOR);
        this.mIconPauseAnimator.addUpdateListener(new b(this));
        AnimatorSet animatorSet = new AnimatorSet();
        this.mPauseAnimatorSet = animatorSet;
        animatorSet.playTogether(this.mIconPauseAnimator, this.mProgressPauseAnimator);
        this.mPauseAnimatorSet.addListener(new AnonymousClass2());
    }

    private void initRecoverAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mProgressEnlargeAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(DEFAULT_ERROR_DURATION);
        ValueAnimator valueAnimator = this.mProgressEnlargeAnimator;
        Interpolator interpolator = DEFAULT_MOVE_EASE_INTERPOLATOR;
        valueAnimator.setInterpolator(interpolator);
        this.mProgressEnlargeAnimator.addUpdateListener(new d(this));
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mIconRecoverAnimator = valueAnimatorOfFloat2;
        valueAnimatorOfFloat2.setDuration(DEFAULT_ERROR_DURATION);
        this.mIconRecoverAnimator.setInterpolator(interpolator);
        this.mIconRecoverAnimator.addUpdateListener(new e(this));
        AnimatorSet animatorSet = new AnimatorSet();
        this.mRecoverAnimatorSet = animatorSet;
        animatorSet.playTogether(this.mIconRecoverAnimator, this.mProgressEnlargeAnimator);
        this.mRecoverAnimatorSet.addListener(new AnonymousClass5());
    }

    private void initResumeAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mProgressResumeAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setStartDelay(200L);
        this.mProgressResumeAnimator.setDuration(200L);
        this.mProgressResumeAnimator.setInterpolator(DEFAULT_OUT_EASE_INTERPOLATOR);
        this.mProgressResumeAnimator.addUpdateListener(new h(this));
        ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mIconResumeAnimator = valueAnimatorOfFloat2;
        valueAnimatorOfFloat2.setDuration(200L);
        this.mIconResumeAnimator.setInterpolator(DEFAULT_LINEAR_INTERPOLATOR);
        this.mIconResumeAnimator.addUpdateListener(new i(this));
        AnimatorSet animatorSet = new AnimatorSet();
        this.mResumeAnimatorSet = animatorSet;
        animatorSet.playTogether(this.mProgressResumeAnimator, this.mIconResumeAnimator);
        this.mResumeAnimatorSet.addListener(new AnonymousClass3());
    }

    private void initSpring() {
        SpringForce springForce = new SpringForce();
        springForce.setDampingRatio(1.0f);
        springForce.setStiffness(50.0f);
        SpringAnimation springAnimation = new SpringAnimation(this, VISUAL_PROGRESS);
        this.mSpringAnimation = springAnimation;
        springAnimation.setSpring(springForce);
        this.mSpringAnimation.addUpdateListener(new c(this));
    }

    private void notifyActualProgressChanged() {
        OnProgressChangedListener onProgressChangedListener = this.mOnProgressChangedListener;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onProgressChanged(this.mActualProgress);
        }
    }

    private void notifyVisualProgressChanged() {
        OnProgressChangedListener onProgressChangedListener = this.mOnProgressChangedListener;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onVisualProgressChanged(this.mVisualProgress);
        }
    }

    private void setVisualProgress(float f3) {
        this.mVisualProgress = f3;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mTrackPaint.setColor(this.mTrackProperty.getCurrentBarColor());
        this.mTrackPaint.setStrokeWidth(this.mTrackProperty.getProgressBarCurrentStrokeWidth());
        this.mProgressPaint.setColor(this.mProgressProperty.getCurrentBarColor());
        this.mProgressPaint.setStrokeWidth(this.mProgressProperty.getProgressBarCurrentStrokeWidth());
        canvas.saveLayerAlpha(0.0f, 0.0f, this.mCenterX * 2.0f, this.mCenterY * 2.0f, this.mGlobalAlpha);
        drawProgress(canvas);
        drawPauseIcon(canvas);
        drawErrorIcon(canvas);
        canvas.restore();
    }

    public void error() {
        this.mErrorAnimatorSet.start();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mGlobalAlpha;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public float getVisualProgress() {
        return this.mVisualProgress;
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        super.invalidateSelf();
        View view = this.mHostView;
        if (view != null) {
            view.invalidate();
        }
    }

    public boolean isAnimating() {
        return this.mAnimating;
    }

    public void pause() {
        this.mPauseAnimatorSet.start();
    }

    public void recover() {
        this.mRecoverAnimatorSet.start();
    }

    public void recycle() {
        this.mHostView = null;
    }

    public void resume() {
        this.mResumeAnimatorSet.start();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.mGlobalAlpha = i3;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        invalidateSelf();
    }

    public void setErrorIconColor(int i3) {
        this.mErrorIconColor = i3;
        this.mTrackProperty.setProgressBarErrorColor(i3);
        this.mProgressProperty.setProgressBarErrorColor(i3);
    }

    public void setHostView(View view) {
        this.mHostView = view;
    }

    public void setIsDrawShadow(boolean z2) {
        if (z2) {
            this.mTrackPaint.setShadowLayer(this.mShadowRadius, this.mShadowXBias, this.mShadowYBias, this.mShadowColor);
            this.mIconPaint.setShadowLayer(this.mShadowRadius, this.mShadowXBias, this.mShadowYBias, this.mShadowColor);
        } else {
            this.mTrackPaint.clearShadowLayer();
            this.mIconPaint.clearShadowLayer();
        }
    }

    public void setMax(int i3) {
        if (i3 < 0) {
            Log.w(TAG, "Max value should not lesser than 0!");
            i3 = 0;
        }
        if (i3 != this.mMax) {
            if (i3 < this.mActualProgress) {
                this.mActualProgress = i3;
                this.mVisualProgress = i3;
            }
            this.mMax = i3;
        }
        invalidateSelf();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.mOnProgressChangedListener = onProgressChangedListener;
    }

    public void setOnProgressStateAnimatorListener(OnProgressStateAnimatorListener onProgressStateAnimatorListener) {
        this.mOnProgressStateAnimatorListener = onProgressStateAnimatorListener;
    }

    public void setPauseIconColor(int i3) {
        this.mPauseIconColor = i3;
    }

    /* JADX WARN: Code duplicated, block: B:7:0x004d  */
    public void setProgress(int i3, boolean z2) {
        Log.d(TAG, "setProgress: " + i3 + "\nmActualProgress = " + this.mActualProgress + "\nmVisualProgress = " + this.mVisualProgress + "\nanimate = " + z2);
        this.mActualProgress = i3;
        float fActual2VisualProgress = actual2VisualProgress((float) i3);
        if (z2) {
            float f3 = this.mVisualProgress;
            if (f3 != fActual2VisualProgress) {
                this.mSpringAnimation.setStartValue(f3);
                this.mSpringAnimation.animateToFinalPosition(fActual2VisualProgress);
            } else {
                this.mVisualProgress = fActual2VisualProgress;
                notifyVisualProgressChanged();
                invalidateSelf();
            }
        } else {
            this.mVisualProgress = fActual2VisualProgress;
            notifyVisualProgressChanged();
            invalidateSelf();
        }
        notifyActualProgressChanged();
    }

    public void setProgressBarErrorColor(int i3) {
        this.mTrackProperty.setProgressBarErrorColor(i3);
        this.mProgressProperty.setProgressBarErrorColor(i3);
    }

    public void setProgressBarErrorSize(float f3, float f4) {
        this.mProgressProperty.setProgressBarErrorOuterDiameter(f3);
        this.mProgressProperty.setProgressBarErrorStrokeWidth(f4);
        this.mTrackProperty.setProgressBarErrorOuterDiameter(f3);
        this.mTrackProperty.setProgressBarErrorStrokeWidth(f4);
    }

    public void setProgressColor(int i3) {
        this.mProgressProperty.setProgressBarColor(i3);
        invalidateSelf();
    }

    public void setProperties(float f3, float f4, float f5, float f6) {
        this.mCenterX = f3;
        this.mCenterY = f4;
        this.mProgressBarOuterDiameter = f5;
        this.mProgressBarStrokeWidth = f6;
        this.mTrackProperty.setProgressBarCenterX(f3);
        this.mTrackProperty.setProgressBarCenterY(this.mCenterY);
        this.mTrackProperty.setProgressBarOuterDiameter(this.mProgressBarOuterDiameter);
        this.mTrackProperty.setProgressBarStrokeWidth(this.mProgressBarStrokeWidth);
        this.mTrackProperty.setProgressBarCurrentOuterDiameter(this.mProgressBarOuterDiameter);
        this.mTrackProperty.setProgressBarCurrentStrokeWidth(this.mProgressBarStrokeWidth);
        this.mProgressProperty.setProgressBarCenterX(this.mCenterX);
        this.mProgressProperty.setProgressBarCenterY(this.mCenterY);
        this.mProgressProperty.setProgressBarOuterDiameter(this.mProgressBarOuterDiameter);
        this.mProgressProperty.setProgressBarStrokeWidth(this.mProgressBarStrokeWidth);
        this.mProgressProperty.setProgressBarCurrentOuterDiameter(this.mProgressBarOuterDiameter);
        this.mProgressProperty.setProgressBarCurrentStrokeWidth(this.mProgressBarStrokeWidth);
        this.mTrackPaint.setStrokeWidth(this.mTrackProperty.getProgressBarStrokeWidth());
        this.mProgressPaint.setStrokeWidth(this.mProgressProperty.getProgressBarStrokeWidth());
    }

    public void setTrackColor(int i3) {
        this.mTrackProperty.setProgressBarColor(i3);
        invalidateSelf();
    }
}