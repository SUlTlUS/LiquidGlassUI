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
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\t\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\tJ\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u000bJ\u000e\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u0011R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/coui/appcompat/seekbar/animation/FlexibleFollowHandHandler;", "", "()V", "isRunning", "", "()Z", "mFlexibleFollowHandAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "mState", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "mView", "Landroid/view/View;", "cancelFlexibleFollowHandAnim", "", "initFlexibleFollowHandAnim", "setFlexibleFollowHandValue", "scale", "", "setStartValue", "setState", "state", "setView", "view", "updateFlexibleFollowHand", "targetScale", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class FlexibleFollowHandHandler {
    public static final int FLEXIBLE_FOLLOW_HAND_SCALE_FACTOR = 1000;
    public static final float FLEXIBLE_FOLLOW_HAND_SPRING_RESPONSE = 0.15f;
    public static final float SPRING_BOUNCE = 0.0f;
    private static final String TAG = "FlexibleFollowHandHandler";
    private COUISpringAnimation mFlexibleFollowHandAnim;
    private SeekBarState mState;
    private View mView;

    public static /* synthetic */ void a(FlexibleFollowHandHandler flexibleFollowHandHandler, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initFlexibleFollowHandAnim$lambda$0(flexibleFollowHandHandler, cOUIDynamicAnimation, f3, f4);
    }

    private static final void initFlexibleFollowHandAnim$lambda$0(FlexibleFollowHandHandler this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        SeekBarState seekBarState = this$0.mState;
        if (seekBarState != null) {
            seekBarState.setDrawProgressScale(f3 / 1000);
            View view = this$0.mView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public final void cancelFlexibleFollowHandAnim() {
        COUISpringAnimation cOUISpringAnimation = this.mFlexibleFollowHandAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancel();
    }

    public final void initFlexibleFollowHandAnim() {
        if (this.mFlexibleFollowHandAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.15f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mFlexibleFollowHandAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new b(this));
        }
    }

    public final boolean isRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mFlexibleFollowHandAnim;
        return cOUISpringAnimation != null && cOUISpringAnimation.isRunning();
    }

    public final void setFlexibleFollowHandValue(float scale) {
        SeekBarState seekBarState = this.mState;
        if (seekBarState != null) {
            seekBarState.setDrawProgressScale(scale);
            View view = this.mView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public final void setStartValue(float scale) {
        COUISpringAnimation cOUISpringAnimation = this.mFlexibleFollowHandAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setStartValue(scale * 1000);
        }
    }

    public final void setState(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.mState = state;
    }

    public final void setView(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        this.mView = view;
    }

    public final void updateFlexibleFollowHand(float targetScale) {
        COUISpringAnimation cOUISpringAnimation = this.mFlexibleFollowHandAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.animateToFinalPosition(targetScale * 1000);
        }
    }
}