package com.coui.appcompat.seekbar.animation;

import android.view.View;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u0000 (2\u00020\u0001:\u0002()B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u000eJ\u0006\u0010\u001c\u001a\u00020\u001aJ\u0010\u0010\u001d\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\fJ\u000e\u0010\u001e\u001a\u00020\u001a2\u0006\u0010\u001f\u001a\u00020\u0010J\u000e\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\u0012J\u000e\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u0014J\u000e\u0010$\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020&J\u0006\u0010'\u001a\u00020\u001aR\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018¨\u0006*"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/ClickAnimationHandler;", "", "()V", "clickAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "getClickAnim", "()Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "isRunning", "", "()Z", "mClickAnim", "mClickAnimUpdateListener", "Lcom/coui/appcompat/seekbar/animation/ClickAnimationHandler$OnClickAnimUpdateListener;", "mLastEndClickListener", "Lcom/coui/appcompat/animation/dynamicanimation/COUIDynamicAnimation$OnAnimationEndListener;", "mState", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "mView", "Landroid/view/View;", "pixPerProgress", "", "getPixPerProgress", "()F", "setPixPerProgress", "(F)V", "addEndListener", "", "listener", "initClickAnim", "setOnClickAnimUpdateListener", "setState", "state", "setView", "view", "startClickAnim", "targetPosition", "startClickAnimToProgress", "targetProgress", "", "stopClikAnim", "Companion", "OnClickAnimUpdateListener", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ClickAnimationHandler {
    public static final float CLICK_SPRING_RESPONSE = 0.3f;
    public static final float SPRING_BOUNCE = 0.0f;
    private static final String TAG = "ClickAnimationHandler";
    private COUISpringAnimation mClickAnim;
    private OnClickAnimUpdateListener mClickAnimUpdateListener;
    private COUIDynamicAnimation.OnAnimationEndListener mLastEndClickListener;
    private SeekBarState mState;
    private View mView;
    private float pixPerProgress;

    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/ClickAnimationHandler$OnClickAnimUpdateListener;", "", "onClickAnimUpdate", "", "value", "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnClickAnimUpdateListener {
        void onClickAnimUpdate(float value);
    }

    public static /* synthetic */ void a(ClickAnimationHandler clickAnimationHandler, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initClickAnim$lambda$0(clickAnimationHandler, cOUIDynamicAnimation, f3, f4);
    }

    private static final void initClickAnim$lambda$0(ClickAnimationHandler this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        OnClickAnimUpdateListener onClickAnimUpdateListener = this$0.mClickAnimUpdateListener;
        if (onClickAnimUpdateListener != null) {
            onClickAnimUpdateListener.onClickAnimUpdate(f3);
        }
    }

    public final void addEndListener(COUIDynamicAnimation.OnAnimationEndListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        COUISpringAnimation cOUISpringAnimation = this.mClickAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        COUIDynamicAnimation.OnAnimationEndListener onAnimationEndListener = this.mLastEndClickListener;
        if (onAnimationEndListener != null) {
            cOUISpringAnimation.removeEndListener(onAnimationEndListener);
        }
        cOUISpringAnimation.addEndListener(listener);
        this.mLastEndClickListener = listener;
    }

    /* JADX INFO: renamed from: getClickAnim, reason: from getter */
    public final COUISpringAnimation getMClickAnim() {
        return this.mClickAnim;
    }

    public final float getPixPerProgress() {
        return this.pixPerProgress;
    }

    public final void initClickAnim() {
        if (this.mClickAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.3f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mClickAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new a(this));
        }
    }

    public final boolean isRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mClickAnim;
        return cOUISpringAnimation != null && cOUISpringAnimation.isRunning();
    }

    public final void setOnClickAnimUpdateListener(OnClickAnimUpdateListener listener) {
        this.mClickAnimUpdateListener = listener;
    }

    public final void setPixPerProgress(float f3) {
        this.pixPerProgress = f3;
    }

    public final void setState(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.mState = state;
    }

    public final void setView(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        this.mView = view;
    }

    public final void startClickAnim(float targetPosition) {
        COUISpringAnimation cOUISpringAnimation = this.mClickAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        cOUISpringAnimation.animateToFinalPosition(targetPosition);
    }

    public final void startClickAnimToProgress(int targetProgress) {
        COUISpringAnimation cOUISpringAnimation = this.mClickAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        float f3 = this.pixPerProgress;
        if (f3 <= 0.0f) {
            return;
        }
        cOUISpringAnimation.animateToFinalPosition(targetProgress * f3);
    }

    public final void stopClikAnim() {
        COUISpringAnimation cOUISpringAnimation = this.mClickAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancelComplete();
    }
}