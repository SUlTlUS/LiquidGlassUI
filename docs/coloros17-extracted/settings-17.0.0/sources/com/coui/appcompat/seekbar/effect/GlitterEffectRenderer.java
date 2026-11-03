package com.coui.appcompat.seekbar.effect;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.seekbar.handler.IOrientationHandler;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\n\u0018\u0000 ,2\u00020\u0001:\u0001,B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0006H\u0002J \u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0006H\u0016J\b\u0010\u001f\u001a\u00020 H\u0016J\b\u0010!\u001a\u00020\u0016H\u0002J\b\u0010\"\u001a\u00020#H\u0016J\u0010\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u00020 H\u0016J\b\u0010&\u001a\u00020\u0016H\u0002J\b\u0010'\u001a\u00020\u0016H\u0002J\u0016\u0010(\u001a\u00020\u00162\u0006\u0010)\u001a\u00020\u00142\u0006\u0010*\u001a\u00020\u0006J\u0010\u0010+\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001dH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006-"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/GlitterEffectRenderer;", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "()V", "mCurGlitterEffectAlpha", "", "mCurGlitterEffectValue", "", "mGlitterEffectAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mGlitterPaint", "Landroid/graphics/Paint;", "mLastGradientRect", "Landroid/graphics/RectF;", "mMaxToMinLinearGradient", "Landroid/graphics/LinearGradient;", "mMinToMaxLinearGradient", "mParams", "Lcom/coui/appcompat/seekbar/effect/GlitterEffectParams;", "mProgressRect", "mView", "Landroid/view/View;", "cancelAnimation", "", "computeGlitterEffectAlpha", "x", "drawEffect", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "progressPosition", "getEffectParams", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "initGlitterEffectAnim", "isEnabled", "", "setEffectParams", "params", "setMaxToMinLinearGradient", "setMinToMaxLinearGradient", "startAnimation", "view", "startVelocity", "updateGradient", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class GlitterEffectRenderer implements IEffectRenderer {
    private static final int COLOR_CHANNEL_MAX = 255;
    private static final int FULL_ANGLE = 360;
    public static final float GLITTER_EFFECT_SPRING_RESPONSE = 0.6f;
    private static final double GLITTER_LN_BASE = 85.0d;
    private static final float HALF_DIVIDER_F = 2.0f;
    public static final float SPRING_BOUNCE = 0.0f;
    private int mCurGlitterEffectAlpha;
    private float mCurGlitterEffectValue;
    private COUISpringAnimation mGlitterEffectAnim;
    private LinearGradient mMaxToMinLinearGradient;
    private LinearGradient mMinToMaxLinearGradient;
    private View mView;
    private GlitterEffectParams mParams = new GlitterEffectParams();
    private Paint mGlitterPaint = new Paint(1);
    private final RectF mProgressRect = new RectF();
    private final RectF mLastGradientRect = new RectF();

    public static /* synthetic */ void a(GlitterEffectRenderer glitterEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initGlitterEffectAnim$lambda$0(glitterEffectRenderer, cOUIDynamicAnimation, f3, f4);
    }

    private final int computeGlitterEffectAlpha(float x2) {
        return MathKt.roundToInt(((double) 255) * (((double) 1) - Math.exp((-(Math.log(GLITTER_LN_BASE) / ((double) 360))) * ((double) x2))));
    }

    private final void initGlitterEffectAnim() {
        if (this.mGlitterEffectAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.6f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mGlitterEffectAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new g(this));
        }
    }

    private static final void initGlitterEffectAnim$lambda$0(GlitterEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.mCurGlitterEffectValue = f3;
        this$0.mCurGlitterEffectAlpha = this$0.computeGlitterEffectAlpha(f3);
        View view = this$0.mView;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0019  */
    private final void setMaxToMinLinearGradient() {
        if (this.mMaxToMinLinearGradient != null) {
            RectF rectF = this.mProgressRect;
            float f3 = rectF.left;
            RectF rectF2 = this.mLastGradientRect;
            if (f3 != rectF2.left || rectF.right != rectF2.right) {
                RectF rectF3 = this.mProgressRect;
                this.mMaxToMinLinearGradient = new LinearGradient(rectF3.left, 0.0f, rectF3.right, 0.0f, this.mParams.getMaxColor(), this.mParams.getMinColor(), Shader.TileMode.CLAMP);
                this.mLastGradientRect.set(this.mProgressRect);
            }
        } else {
            RectF rectF4 = this.mProgressRect;
            this.mMaxToMinLinearGradient = new LinearGradient(rectF4.left, 0.0f, rectF4.right, 0.0f, this.mParams.getMaxColor(), this.mParams.getMinColor(), Shader.TileMode.CLAMP);
            this.mLastGradientRect.set(this.mProgressRect);
        }
        this.mGlitterPaint.setShader(this.mMaxToMinLinearGradient);
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0019  */
    private final void setMinToMaxLinearGradient() {
        if (this.mMinToMaxLinearGradient != null) {
            RectF rectF = this.mProgressRect;
            float f3 = rectF.left;
            RectF rectF2 = this.mLastGradientRect;
            if (f3 != rectF2.left || rectF.right != rectF2.right) {
                RectF rectF3 = this.mProgressRect;
                this.mMinToMaxLinearGradient = new LinearGradient(rectF3.left, 0.0f, rectF3.right, 0.0f, this.mParams.getMinColor(), this.mParams.getMaxColor(), Shader.TileMode.CLAMP);
                this.mLastGradientRect.set(this.mProgressRect);
            }
        } else {
            RectF rectF4 = this.mProgressRect;
            this.mMinToMaxLinearGradient = new LinearGradient(rectF4.left, 0.0f, rectF4.right, 0.0f, this.mParams.getMinColor(), this.mParams.getMaxColor(), Shader.TileMode.CLAMP);
            this.mLastGradientRect.set(this.mProgressRect);
        }
        this.mGlitterPaint.setShader(this.mMinToMaxLinearGradient);
    }

    private final void updateGradient(SeekBarState state) {
        if (state.getIsStartFromMiddle() && state.getIsRtl() && state.getScale() >= 1.0f) {
            setMaxToMinLinearGradient();
            return;
        }
        if (state.getIsStartFromMiddle() && state.getIsRtl() && state.getScale() <= 0.0f) {
            setMinToMaxLinearGradient();
            return;
        }
        if (state.getIsStartFromMiddle() && !state.getIsRtl() && state.getScale() >= 1.0f) {
            setMinToMaxLinearGradient();
            return;
        }
        if (state.getIsStartFromMiddle() && !state.getIsRtl() && state.getScale() <= 0.0f) {
            setMaxToMinLinearGradient();
            return;
        }
        if (!state.getIsStartFromMiddle() && state.getIsRtl() && state.getScale() >= 1.0f) {
            setMaxToMinLinearGradient();
        } else {
            if (state.getIsStartFromMiddle() || state.getIsRtl() || state.getScale() < 1.0f) {
                return;
            }
            setMinToMaxLinearGradient();
        }
    }

    public final void cancelAnimation() {
        COUISpringAnimation cOUISpringAnimation = this.mGlitterEffectAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        this.mView = null;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void drawEffect(Canvas canvas, SeekBarState state, float progressPosition) {
        COUISeekBarCore view;
        IOrientationHandler mOrientationHandler;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if (!getMEnabled() || !state.getShowGlitterEffect() || (view = state.getView()) == null || (mOrientationHandler = view.getMOrientationHandler()) == null) {
            return;
        }
        mOrientationHandler.calculateProgressRect(this.mProgressRect, state);
        updateGradient(state);
        if (this.mGlitterPaint.getShader() == null) {
            return;
        }
        this.mGlitterPaint.setAlpha((int) (this.mCurGlitterEffectAlpha * this.mParams.getIntensity()));
        canvas.drawRoundRect(this.mProgressRect, state.getProgressHeight() / 2.0f, state.getProgressHeight() / 2.0f, this.mGlitterPaint);
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public DrawPhase getDrawPhase() {
        return IEffectRenderer.DefaultImpls.getDrawPhase(this);
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: getEffectParams */
    public EffectParams getMParams() {
        return this.mParams;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: isEnabled */
    public boolean getMEnabled() {
        return true;
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void setEffectParams(EffectParams params) {
        Intrinsics.checkNotNullParameter(params, "params");
        if (params instanceof GlitterEffectParams) {
            this.mParams = (GlitterEffectParams) params;
        }
    }

    public final void startAnimation(View view, float startVelocity) {
        Intrinsics.checkNotNullParameter(view, "view");
        initGlitterEffectAnim();
        this.mView = view;
        COUISpringAnimation cOUISpringAnimation = this.mGlitterEffectAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setStartValue(this.mCurGlitterEffectValue);
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mGlitterEffectAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.animateToFinalPosition(0.0f);
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mGlitterEffectAnim;
        if (cOUISpringAnimation3 != null) {
            cOUISpringAnimation3.setStartVelocity(Math.abs(startVelocity));
        }
    }
}