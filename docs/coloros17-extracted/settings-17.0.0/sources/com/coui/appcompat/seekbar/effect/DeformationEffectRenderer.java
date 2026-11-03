package com.coui.appcompat.seekbar.effect;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.animation.DecelerateInterpolator;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.seekbar.listener.DeformedValueBean;
import com.coui.appcompat.seekbar.listener.OnDeformedListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 >2\u00020\u0001:\u0001>B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\u000e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\bJ\u0006\u0010\u001d\u001a\u00020\u001bJ\u0006\u0010\u001e\u001a\u00020\u001bJ\u0006\u0010\u001f\u001a\u00020\u001bJ\u0018\u0010 \u001a\u00020\b2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\bH\u0002J \u0010$\u001a\u00020\u001b2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u00152\u0006\u0010(\u001a\u00020\bH\u0016J\b\u0010)\u001a\u00020*H\u0016J\b\u0010+\u001a\u0004\u0018\u00010\u0010J\b\u0010,\u001a\u00020\u001bH\u0002J\u0006\u0010-\u001a\u00020\u001bJ\b\u0010.\u001a\u00020\u0019H\u0016J\u000e\u0010/\u001a\u00020\u001b2\u0006\u00100\u001a\u000201J\u0006\u00102\u001a\u00020\u001bJ\u0010\u00103\u001a\u00020\u001b2\b\u00104\u001a\u0004\u0018\u00010\u0010J\u0010\u00105\u001a\u00020\u001b2\b\u00106\u001a\u0004\u0018\u000107J\u0010\u00108\u001a\u00020\u001b2\u0006\u00109\u001a\u00020*H\u0016J\u000e\u0010:\u001a\u00020\u001b2\u0006\u0010'\u001a\u00020\u0015J\u0006\u0010;\u001a\u00020\u001bJ\b\u0010<\u001a\u00020\u001bH\u0002J\b\u0010=\u001a\u00020\u0019H\u0002R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\f\u0010\nR\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0016\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\n¨\u0006?"}, d2 = {"Lcom/coui/appcompat/seekbar/effect/DeformationEffectRenderer;", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "()V", "deformationParams", "Lcom/coui/appcompat/seekbar/effect/DeformationEffectParams;", "getDeformationParams", "()Lcom/coui/appcompat/seekbar/effect/DeformationEffectParams;", "heightBottomDeformedValue", "", "getHeightBottomDeformedValue", "()F", "heightTopDeformedValue", "getHeightTopDeformedValue", "mDeformationAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mOnDeformedListener", "Lcom/coui/appcompat/seekbar/listener/OnDeformedListener;", "mParams", "mReleaseAnimator", "Landroid/animation/ValueAnimator;", "mState", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "widthDeformedValue", "getWidthDeformedValue", "bottomDeformedChange", "", "calculateFlingDeformationValue", "", "scale", "calculateTouchDeformationValue", "cancelAll", "cancelDeformationRelease", "computeValue", "x", "", "maxValue", "drawEffect", "canvas", "Landroid/graphics/Canvas;", "state", "progressPosition", "getEffectParams", "Lcom/coui/appcompat/seekbar/effect/EffectParams;", "getOnDeformedListener", "heightDeformedChanged", "initDeformationAnim", "isEnabled", "notifyScaleChanged", "progress", "", "resetDeformationValue", "setDeformedListener", "listener", "setDeformedParams", "bean", "Lcom/coui/appcompat/seekbar/listener/DeformedValueBean;", "setEffectParams", "params", "setState", "startDeformationRelease", "syncDeformationValuesToState", "topDeformedChange", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DeformationEffectRenderer implements IEffectRenderer {
    private static final double DEFORMATION_EXPONENTIAL_FACTOR = 11.5d;
    public static final int RELEASE_ANIM_DURATION = 200;
    private static final String TAG = "DeformationEffectRenderer";
    private COUISpringAnimation mDeformationAnim;
    private OnDeformedListener mOnDeformedListener;
    private DeformationEffectParams mParams;
    private ValueAnimator mReleaseAnimator;
    private SeekBarState mState;

    public DeformationEffectRenderer() {
        DeformationEffectParams deformationEffectParams = new DeformationEffectParams();
        deformationEffectParams.setEnabled(true);
        this.mParams = deformationEffectParams;
    }

    public static /* synthetic */ void a(DeformationEffectRenderer deformationEffectRenderer, float f3, float f4, float f5, float f6, float f7, ValueAnimator valueAnimator) {
        startDeformationRelease$lambda$3$lambda$2(deformationEffectRenderer, f3, f4, f5, f6, f7, valueAnimator);
    }

    public static /* synthetic */ void b(DeformationEffectRenderer deformationEffectRenderer, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initDeformationAnim$lambda$1(deformationEffectRenderer, cOUIDynamicAnimation, f3, f4);
    }

    private final boolean bottomDeformedChange() {
        boolean z2 = this.mParams.getCurBottomDeformationValue() == getHeightBottomDeformedValue();
        boolean z3 = !z2;
        if (!z2) {
            this.mParams.setCurBottomDeformationValue(getHeightBottomDeformedValue());
        }
        return z3;
    }

    private final float computeValue(double x2, float maxValue) {
        return (float) (((double) maxValue) * (((double) 1) - Math.exp(x2 * (-11.5d))));
    }

    private final void heightDeformedChanged() {
        OnDeformedListener onDeformedListener = this.mOnDeformedListener;
        if (onDeformedListener == null || this.mState == null) {
            return;
        }
        if (topDeformedChange() || bottomDeformedChange()) {
            onDeformedListener.onHeightDeformedChanged(this.mParams.getCurTopDeformationValue(), this.mParams.getCurBottomDeformationValue());
        }
    }

    private static final void initDeformationAnim$lambda$1(DeformationEffectRenderer this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        SeekBarState seekBarState = this$0.mState;
        if (seekBarState == null) {
            return;
        }
        float f5 = f3 / 100000;
        if (seekBarState.getScale() > 1.0f) {
            DeformationEffectParams deformationEffectParams = this$0.mParams;
            double d3 = f5;
            deformationEffectParams.setHeightBottomDeformedUpValue(this$0.computeValue(d3, deformationEffectParams.getMaxMovingDistance()));
            DeformationEffectParams deformationEffectParams2 = this$0.mParams;
            deformationEffectParams2.setHeightTopDeformedUpValue(this$0.computeValue(d3, deformationEffectParams2.getMaxMovingDistance() + this$0.mParams.getMaxHeightDeformedValue()));
            DeformationEffectParams deformationEffectParams3 = this$0.mParams;
            deformationEffectParams3.setWidthDeformedValue(this$0.computeValue(d3, deformationEffectParams3.getMaxWidthDeformedValue()));
            this$0.syncDeformationValuesToState();
            this$0.heightDeformedChanged();
            COUISeekBarCore view = seekBarState.getView();
            if (view != null) {
                view.invalidate();
                return;
            }
            return;
        }
        if (seekBarState.getScale() < 0.0f) {
            DeformationEffectParams deformationEffectParams4 = this$0.mParams;
            double d4 = f5;
            deformationEffectParams4.setHeightTopDeformedDownValue(this$0.computeValue(d4, deformationEffectParams4.getMaxMovingDistance()));
            DeformationEffectParams deformationEffectParams5 = this$0.mParams;
            deformationEffectParams5.setHeightBottomDeformedDownValue(this$0.computeValue(d4, deformationEffectParams5.getMaxMovingDistance() + this$0.mParams.getMaxHeightDeformedValue()));
            DeformationEffectParams deformationEffectParams6 = this$0.mParams;
            deformationEffectParams6.setWidthDeformedValue(this$0.computeValue(d4, deformationEffectParams6.getMaxWidthDeformedValue()));
            this$0.syncDeformationValuesToState();
            this$0.heightDeformedChanged();
            COUISeekBarCore view2 = seekBarState.getView();
            if (view2 != null) {
                view2.invalidate();
            }
        }
    }

    private static final void startDeformationRelease$lambda$3$lambda$2(DeformationEffectRenderer this$0, float f3, float f4, float f5, float f6, float f7, ValueAnimator animation) {
        COUISeekBarCore view;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(animation, "animation");
        Object animatedValue = animation.getAnimatedValue();
        Intrinsics.checkNotNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float fFloatValue = ((Float) animatedValue).floatValue();
        this$0.mParams.setHeightTopDeformedUpValue(f3 * fFloatValue);
        this$0.mParams.setHeightBottomDeformedDownValue(f4 * fFloatValue);
        this$0.mParams.setWidthDeformedValue(f5 * fFloatValue);
        this$0.mParams.setHeightBottomDeformedUpValue(f6 * fFloatValue);
        this$0.mParams.setHeightTopDeformedDownValue(f7 * fFloatValue);
        this$0.syncDeformationValuesToState();
        this$0.heightDeformedChanged();
        SeekBarState seekBarState = this$0.mState;
        if (seekBarState == null || (view = seekBarState.getView()) == null) {
            return;
        }
        view.invalidate();
    }

    private final void syncDeformationValuesToState() {
        SeekBarState seekBarState = this.mState;
        if (seekBarState == null) {
            return;
        }
        seekBarState.setHeightTopDeformedUpValue(this.mParams.getHeightTopDeformedUpValue());
        seekBarState.setHeightBottomDeformedUpValue(this.mParams.getHeightBottomDeformedUpValue());
        seekBarState.setHeightTopDeformedDownValue(this.mParams.getHeightTopDeformedDownValue());
        seekBarState.setHeightBottomDeformedDownValue(this.mParams.getHeightBottomDeformedDownValue());
        seekBarState.setWidthDeformedValue(this.mParams.getWidthDeformedValue());
    }

    private final boolean topDeformedChange() {
        boolean z2 = this.mParams.getCurTopDeformationValue() == getHeightTopDeformedValue();
        boolean z3 = !z2;
        if (!z2) {
            this.mParams.setCurTopDeformationValue(getHeightTopDeformedValue());
        }
        return z3;
    }

    public final void calculateFlingDeformationValue(float scale) {
        COUISpringAnimation cOUISpringAnimation = this.mDeformationAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        if (scale > 1.0f) {
            cOUISpringAnimation.animateToFinalPosition((scale - 1) * 100000);
        } else if (scale < 0.0f) {
            cOUISpringAnimation.animateToFinalPosition(Math.abs(scale) * 100000);
        } else {
            resetDeformationValue();
        }
    }

    public final void calculateTouchDeformationValue() {
        SeekBarState seekBarState;
        COUISpringAnimation cOUISpringAnimation = this.mDeformationAnim;
        if (cOUISpringAnimation == null || (seekBarState = this.mState) == null) {
            return;
        }
        if (seekBarState.getScale() > 1.0f) {
            cOUISpringAnimation.animateToFinalPosition(((seekBarState.getScale() - 1) / 5) * 100000);
        } else if (seekBarState.getScale() < 0.0f) {
            cOUISpringAnimation.animateToFinalPosition((Math.abs(seekBarState.getScale()) / 5) * 100000);
        }
    }

    public final void cancelAll() {
        COUISpringAnimation cOUISpringAnimation = this.mDeformationAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        ValueAnimator valueAnimator = this.mReleaseAnimator;
        if (valueAnimator != null) {
            if (!valueAnimator.isRunning()) {
                valueAnimator = null;
            }
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }
        this.mReleaseAnimator = null;
    }

    public final void cancelDeformationRelease() {
        ValueAnimator valueAnimator = this.mReleaseAnimator;
        if (valueAnimator != null) {
            if (!valueAnimator.isRunning()) {
                valueAnimator = null;
            }
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void drawEffect(Canvas canvas, SeekBarState state, float progressPosition) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
    }

    /* JADX INFO: renamed from: getDeformationParams, reason: from getter */
    public final DeformationEffectParams getMParams() {
        return this.mParams;
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

    public final float getHeightBottomDeformedValue() {
        float heightBottomDeformedUpValue;
        float heightBottomDeformedDownValue;
        SeekBarState seekBarState = this.mState;
        if (seekBarState == null || !seekBarState.getIsRtl()) {
            heightBottomDeformedUpValue = this.mParams.getHeightBottomDeformedUpValue();
            heightBottomDeformedDownValue = this.mParams.getHeightBottomDeformedDownValue();
        } else {
            heightBottomDeformedUpValue = this.mParams.getHeightBottomDeformedDownValue();
            heightBottomDeformedDownValue = this.mParams.getHeightBottomDeformedUpValue();
        }
        return heightBottomDeformedUpValue - heightBottomDeformedDownValue;
    }

    public final float getHeightTopDeformedValue() {
        float heightTopDeformedUpValue;
        float heightTopDeformedDownValue;
        SeekBarState seekBarState = this.mState;
        if (seekBarState == null || !seekBarState.getIsRtl()) {
            heightTopDeformedUpValue = this.mParams.getHeightTopDeformedUpValue();
            heightTopDeformedDownValue = this.mParams.getHeightTopDeformedDownValue();
        } else {
            heightTopDeformedUpValue = this.mParams.getHeightTopDeformedDownValue();
            heightTopDeformedDownValue = this.mParams.getHeightTopDeformedUpValue();
        }
        return heightTopDeformedUpValue - heightTopDeformedDownValue;
    }

    /* JADX INFO: renamed from: getOnDeformedListener, reason: from getter */
    public final OnDeformedListener getMOnDeformedListener() {
        return this.mOnDeformedListener;
    }

    public final float getWidthDeformedValue() {
        return this.mParams.getWidthDeformedValue();
    }

    public final void initDeformationAnim() {
        if (this.mDeformationAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.1f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mDeformationAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new f(this));
        }
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    /* JADX INFO: renamed from: isEnabled */
    public boolean getMEnabled() {
        SeekBarState seekBarState;
        return this.mParams.getEnabled() && (seekBarState = this.mState) != null && seekBarState.getIsSupportDeformation();
    }

    public final void notifyScaleChanged(int progress) {
        SeekBarState seekBarState;
        OnDeformedListener onDeformedListener = this.mOnDeformedListener;
        if (onDeformedListener == null || (seekBarState = this.mState) == null) {
            return;
        }
        onDeformedListener.onScaleChanged(this.mParams.toBean(progress, seekBarState.getScale(), seekBarState.getDrawProgressScale()));
    }

    public final void resetDeformationValue() {
        this.mParams.resetDeformationValues();
        syncDeformationValuesToState();
        COUISpringAnimation cOUISpringAnimation = this.mDeformationAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancel();
    }

    public final void setDeformedListener(OnDeformedListener listener) {
        this.mOnDeformedListener = listener;
    }

    public final void setDeformedParams(DeformedValueBean bean) {
        SeekBarState seekBarState;
        if (bean == null || (seekBarState = this.mState) == null) {
            return;
        }
        seekBarState.setScale(bean.getScale());
        seekBarState.setDrawProgressScale(bean.getDrawProgressScale());
        this.mParams.setFromBean(bean);
        COUISeekBarCore view = seekBarState.getView();
        if (view != null) {
            view.invalidate();
        }
    }

    @Override // com.coui.appcompat.seekbar.effect.IEffectRenderer
    public void setEffectParams(EffectParams params) {
        Intrinsics.checkNotNullParameter(params, "params");
        if (params instanceof DeformationEffectParams) {
            this.mParams = (DeformationEffectParams) params;
        }
    }

    public final void setState(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.mState = state;
    }

    public final void startDeformationRelease() {
        if (this.mState == null) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = this.mDeformationAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        cancelDeformationRelease();
        float heightTopDeformedUpValue = this.mParams.getHeightTopDeformedUpValue();
        float heightBottomDeformedDownValue = this.mParams.getHeightBottomDeformedDownValue();
        float widthDeformedValue = this.mParams.getWidthDeformedValue();
        float heightBottomDeformedUpValue = this.mParams.getHeightBottomDeformedUpValue();
        float heightTopDeformedDownValue = this.mParams.getHeightTopDeformedDownValue();
        if (heightTopDeformedUpValue == 0.0f && heightBottomDeformedDownValue == 0.0f && widthDeformedValue == 0.0f && heightBottomDeformedUpValue == 0.0f && heightTopDeformedDownValue == 0.0f) {
            return;
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        valueAnimatorOfFloat.setDuration(200L);
        valueAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        valueAnimatorOfFloat.addUpdateListener(new e(this, heightTopDeformedUpValue, heightBottomDeformedDownValue, widthDeformedValue, heightBottomDeformedUpValue, heightTopDeformedDownValue));
        valueAnimatorOfFloat.addListener(new DeformationEffectRenderer$startDeformationRelease$1$2(this));
        valueAnimatorOfFloat.start();
        this.mReleaseAnimator = valueAnimatorOfFloat;
    }
}