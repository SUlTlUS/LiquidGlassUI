package com.coui.appcompat.seekbar.animation;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\f\u001a\u00020\u000bH\u0002J\u0010\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J(\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0003H\u0016J\u0010\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u0015H\u0016J\b\u0010\u0019\u001a\u00020\u000bH\u0016J\b\u0010\u001a\u001a\u00020\u000bH\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/StandardAnimationHandler;", "Lcom/coui/appcompat/seekbar/animation/IAnimationHandler;", "mSeekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "(Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;)V", "mLastEndClickListener", "Lcom/coui/appcompat/animation/dynamicanimation/COUIDynamicAnimation$OnAnimationEndListener;", "mProgressSpringAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mThumbScaleSpringAnim", "initSpringAnimation", "", "initThumbScaleSpringAnim", "onProgressAnimationUpdate", "value", "", "startProgressAnimation", "from", "", TypedValues.TransitionType.S_TO, "fromUser", "", "seekBar", "startThumbScaleAnimation", "enlarge", "stopAllAnimations", "stopThumbScaleAnimation", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class StandardAnimationHandler implements IAnimationHandler {
    public static final float CLICK_SPRING_RESPONSE = 0.3f;
    public static final float SPRING_BOUNCE = 0.0f;
    public static final float THUMB_SCALE_BOUNCE_ENLARGE = 0.3f;
    public static final float THUMB_SCALE_BOUNCE_SHRINK = 0.0f;
    public static final float THUMB_SCALE_SPRING_RESPONSE = 0.5f;
    private COUIDynamicAnimation.OnAnimationEndListener mLastEndClickListener;
    private COUISpringAnimation mProgressSpringAnim;
    private final COUISeekBarCore mSeekBar;
    private COUISpringAnimation mThumbScaleSpringAnim;

    public StandardAnimationHandler(COUISeekBarCore mSeekBar) {
        Intrinsics.checkNotNullParameter(mSeekBar, "mSeekBar");
        this.mSeekBar = mSeekBar;
    }

    public static /* synthetic */ void a(COUISeekBarCore cOUISeekBarCore, int i3, boolean z2, COUIDynamicAnimation cOUIDynamicAnimation, boolean z3, float f3, float f4) {
        startProgressAnimation$lambda$1(cOUISeekBarCore, i3, z2, cOUIDynamicAnimation, z3, f3, f4);
    }

    public static /* synthetic */ void b(StandardAnimationHandler standardAnimationHandler, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initSpringAnimation$lambda$0(standardAnimationHandler, cOUIDynamicAnimation, f3, f4);
    }

    public static /* synthetic */ void c(StandardAnimationHandler standardAnimationHandler, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initThumbScaleSpringAnim$lambda$2(standardAnimationHandler, cOUIDynamicAnimation, f3, f4);
    }

    private final void initSpringAnimation() {
        if (this.mProgressSpringAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.3f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mProgressSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new j(this));
        }
    }

    private static final void initSpringAnimation$lambda$0(StandardAnimationHandler this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onProgressAnimationUpdate(f3);
    }

    private final void initThumbScaleSpringAnim() {
        if (this.mThumbScaleSpringAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.3f);
        cOUISpringForce.setResponse(0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mThumbScaleSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new h(this));
        }
    }

    private static final void initThumbScaleSpringAnim$lambda$2(StandardAnimationHandler this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        SeekBarState state = this$0.mSeekBar.getState();
        if (state != null) {
            state.setCurThumbRadius(f3);
            this$0.mSeekBar.invalidate();
        }
    }

    private final void onProgressAnimationUpdate(float value) {
        int iCoerceIn;
        SeekBarState state = this.mSeekBar.getState();
        float seekBarLength = this.mSeekBar.getSeekBarLength();
        int max = state.getMax() - state.getMin();
        if (seekBarLength <= 0.0f || max <= 0) {
            return;
        }
        float f3 = seekBarLength / max;
        if (f3 > 0.0f) {
            if (this.mSeekBar.isVertical()) {
                iCoerceIn = RangesKt.coerceIn(MathKt.roundToInt(state.getMax() - (value / f3)), state.getMin(), state.getMax());
                state.setProgress(iCoerceIn);
                state.setScale(state.progressToScale(iCoerceIn));
            } else {
                int iCoerceIn2 = RangesKt.coerceIn((int) (value / f3), state.getMin(), state.getMax());
                state.setProgress(iCoerceIn2);
                state.setScale(seekBarLength > 0.0f ? (value - (state.getMin() * f3)) / seekBarLength : 0.0f);
                iCoerceIn = iCoerceIn2;
            }
            state.setDrawProgressScale(state.getScale());
            this.mSeekBar.syncProgressToParent(iCoerceIn);
            this.mSeekBar.invalidate();
        }
    }

    private static final void startProgressAnimation$lambda$1(COUISeekBarCore finalSeekBar, int i3, boolean z2, COUIDynamicAnimation cOUIDynamicAnimation, boolean z3, float f3, float f4) {
        Intrinsics.checkNotNullParameter(finalSeekBar, "$finalSeekBar");
        if (z3) {
            return;
        }
        finalSeekBar.onProgressAnimationEnd(i3, z2);
    }

    @Override // com.coui.appcompat.seekbar.animation.IAnimationHandler
    public void startProgressAnimation(int from, int to) {
        startProgressAnimation(from, to, false, this.mSeekBar);
    }

    @Override // com.coui.appcompat.seekbar.animation.IAnimationHandler
    public void startThumbScaleAnimation(boolean enlarge) {
        COUISpringForce spring;
        SeekBarState state = this.mSeekBar.getState();
        initThumbScaleSpringAnim();
        COUISpringAnimation cOUISpringAnimation = this.mThumbScaleSpringAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        if (cOUISpringAnimation != null && (spring = cOUISpringAnimation.getSpring()) != null) {
            spring.setBounce(enlarge ? 0.3f : 0.0f);
            spring.setResponse(0.5f);
        }
        float curThumbRadius = state.getCurThumbRadius();
        float thumbMaxRadius = enlarge ? state.getThumbMaxRadius() : state.getThumbRadius();
        COUISpringAnimation cOUISpringAnimation2 = this.mThumbScaleSpringAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.setStartValue(curThumbRadius);
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mThumbScaleSpringAnim;
        if (cOUISpringAnimation3 != null) {
            cOUISpringAnimation3.animateToFinalPosition(thumbMaxRadius);
        }
    }

    @Override // com.coui.appcompat.seekbar.animation.IAnimationHandler
    public void stopAllAnimations() {
        COUISpringAnimation cOUISpringAnimation = this.mProgressSpringAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        stopThumbScaleAnimation();
    }

    @Override // com.coui.appcompat.seekbar.animation.IAnimationHandler
    public void stopThumbScaleAnimation() {
        COUISpringAnimation cOUISpringAnimation = this.mThumbScaleSpringAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancel();
    }

    @Override // com.coui.appcompat.seekbar.animation.IAnimationHandler
    public void startProgressAnimation(int from, int to, boolean fromUser, COUISeekBarCore seekBar) {
        float scale;
        float max;
        COUISpringAnimation cOUISpringAnimation;
        Intrinsics.checkNotNullParameter(seekBar, "seekBar");
        SeekBarState state = seekBar.getState();
        float seekBarLength = seekBar.getSeekBarLength();
        int max2 = state.getMax() - state.getMin();
        if (seekBarLength > 0.0f && max2 > 0) {
            float f3 = max2;
            float f4 = seekBarLength / f3;
            initSpringAnimation();
            COUISpringAnimation cOUISpringAnimation2 = this.mProgressSpringAnim;
            if (cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning()) {
                cOUISpringAnimation2.cancel();
            }
            i iVar = new i(seekBar, to, fromUser);
            COUIDynamicAnimation.OnAnimationEndListener onAnimationEndListener = this.mLastEndClickListener;
            if (onAnimationEndListener != null && (cOUISpringAnimation = this.mProgressSpringAnim) != null && cOUISpringAnimation != null) {
                cOUISpringAnimation.removeEndListener(onAnimationEndListener);
            }
            COUISpringAnimation cOUISpringAnimation3 = this.mProgressSpringAnim;
            if (cOUISpringAnimation3 != null) {
                cOUISpringAnimation3.addEndListener(iVar);
            }
            this.mLastEndClickListener = iVar;
            if (seekBar.isVertical()) {
                scale = (state.getMax() - ((state.getScale() * f3) + state.getMin())) * f4;
                max = state.getMax() - to;
            } else {
                scale = ((state.getScale() * f3) + state.getMin()) * f4;
                max = to;
            }
            float f5 = max * f4;
            seekBar.startTrackingTouch(fromUser, false);
            COUISpringAnimation cOUISpringAnimation4 = this.mProgressSpringAnim;
            if (cOUISpringAnimation4 != null) {
                cOUISpringAnimation4.setStartValue(scale);
            }
            COUISpringAnimation cOUISpringAnimation5 = this.mProgressSpringAnim;
            if (cOUISpringAnimation5 != null) {
                cOUISpringAnimation5.animateToFinalPosition(f5);
                return;
            }
            return;
        }
        seekBar.superSetProgress(to);
        seekBar.onProgressAnimationEnd(to, fromUser);
    }
}