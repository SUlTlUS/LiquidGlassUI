package com.coui.appcompat.seekbar.animation;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.heytap.cloud.sdk.utils.Constants;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\f\u0018\u0000 $2\u00020\u0001:\u0002#$B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\bJ\b\u0010\u001c\u001a\u00020\u0019H\u0002J\u0016\u0010\u001d\u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\b2\u0006\u0010\u001f\u001a\u00020\bJ\u000e\u0010 \u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\bJ \u0010!\u001a\u00020\u00192\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\nH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u001a\u0010\f\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\u0014R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/ThumbStateAnimationHandler;", "", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "callback", "Lcom/coui/appcompat/seekbar/animation/ThumbStateAnimationHandler$Callback;", "(Lcom/coui/appcompat/seekbar/core/SeekBarState;Lcom/coui/appcompat/seekbar/animation/ThumbStateAnimationHandler$Callback;)V", "fromRadius", "", "isRunning", "", "()Z", "lastEnlargeFraction", "getLastEnlargeFraction", "()F", "setLastEnlargeFraction", "(F)V", "pendingShrink", "getPendingShrink", "setPendingShrink", "(Z)V", "springAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "toRadius", Constants.ResultMessage.RESULT_CANCEL, "", "checkPendingShrink", "fraction", "initAnim", "startAppear", "from", TypedValues.TransitionType.S_TO, "startDisappear", "startTransition", "appear", "Callback", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ThumbStateAnimationHandler {
    private static final float APPEAR_BOUNCE = 0.3f;
    private static final float APPEAR_RESPONSE = 0.5f;
    private static final float DISAPPEAR_BOUNCE = 0.0f;
    private static final float DISAPPEAR_RESPONSE = 0.4f;
    private static final float FINAL_POSITION = 10000.0f;
    private final Callback callback;
    private float fromRadius;
    private float lastEnlargeFraction;
    private boolean pendingShrink;
    private COUISpringAnimation springAnim;
    private final SeekBarState state;
    private float toRadius;

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\b\u0010\u0005\u001a\u00020\u0003H&¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/ThumbStateAnimationHandler$Callback;", "", "onPendingShrinkTriggered", "", "requestInvalidate", "stopExistingThumbScaleAnimation", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Callback {
        void onPendingShrinkTriggered();

        void requestInvalidate();

        void stopExistingThumbScaleAnimation();
    }

    public ThumbStateAnimationHandler(SeekBarState state, Callback callback) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.state = state;
        this.callback = callback;
    }

    public static /* synthetic */ void a(ThumbStateAnimationHandler thumbStateAnimationHandler, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initAnim$lambda$0(thumbStateAnimationHandler, cOUIDynamicAnimation, f3, f4);
    }

    private final void initAnim() {
        if (this.springAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.3f);
        cOUISpringForce.setResponse(0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.springAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new k(this));
        }
    }

    private static final void initAnim$lambda$0(ThumbStateAnimationHandler this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        SeekBarState seekBarState = this$0.state;
        float f5 = this$0.fromRadius;
        seekBarState.setCurThumbRadius(RangesKt.coerceAtLeast(f5 + ((this$0.toRadius - f5) * (f3 / 10000.0f)), 0.0f));
        this$0.callback.requestInvalidate();
    }

    private final void startTransition(float fromRadius, float toRadius, boolean appear) {
        this.callback.stopExistingThumbScaleAnimation();
        initAnim();
        COUISpringAnimation cOUISpringAnimation = this.springAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        if (cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        this.fromRadius = fromRadius;
        this.toRadius = toRadius;
        COUISpringForce spring = cOUISpringAnimation.getSpring();
        if (spring != null) {
            spring.setBounce(appear ? 0.3f : 0.0f);
            spring.setResponse(appear ? 0.5f : 0.4f);
        }
        this.state.setCurThumbRadius(fromRadius);
        cOUISpringAnimation.setStartValue(0.0f);
        cOUISpringAnimation.animateToFinalPosition(10000.0f);
    }

    public final void cancel() {
        COUISpringAnimation cOUISpringAnimation = this.springAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancel();
    }

    public final void checkPendingShrink(float fraction) {
        if (this.pendingShrink && fraction < this.lastEnlargeFraction) {
            this.pendingShrink = false;
            this.callback.onPendingShrinkTriggered();
        }
        this.lastEnlargeFraction = fraction;
    }

    public final float getLastEnlargeFraction() {
        return this.lastEnlargeFraction;
    }

    public final boolean getPendingShrink() {
        return this.pendingShrink;
    }

    public final boolean isRunning() {
        COUISpringAnimation cOUISpringAnimation = this.springAnim;
        return cOUISpringAnimation != null && cOUISpringAnimation.isRunning();
    }

    public final void setLastEnlargeFraction(float f3) {
        this.lastEnlargeFraction = f3;
    }

    public final void setPendingShrink(boolean z2) {
        this.pendingShrink = z2;
    }

    public final void startAppear(float from, float to) {
        startTransition(from, to, true);
    }

    public final void startDisappear(float from) {
        startTransition(from, 0.0f, false);
    }
}