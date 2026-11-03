package com.coui.appcompat.poplist;

import android.animation.Animator;
import android.os.Looper;
import android.view.View;
import com.coui.appcompat.animation.dynamicanimation.COUIRtAnimationImpl;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.oplus.animation.OplusAsyncAnimatorUtils;
import com.oplus.view.OplusRenderNodeAnimator;

/* JADX INFO: loaded from: classes7.dex */
class RenderThreadAnimationExecutor implements AnimationExecutor {
    static final RenderThreadAnimationExecutor INSTANCE = new RenderThreadAnimationExecutor();

    private RenderThreadAnimationExecutor() {
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public boolean isAsynchronous() {
        return true;
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public void runOnMainThread(View view, Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            view.post(runnable);
        } else {
            runnable.run();
        }
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public void setAlpha(View view, float f3) {
        OplusAsyncAnimatorUtils.setAlpha(view, f3);
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public void setScaleX(View view, float f3) {
        OplusAsyncAnimatorUtils.setScaleX(view, f3);
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public void setScaleY(View view, float f3) {
        OplusAsyncAnimatorUtils.setScaleY(view, f3);
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public Animator startAnimation(COUISpringAnimation cOUISpringAnimation, View view, float f3) {
        Animator animatorCreateRtAnimator = OplusRenderNodeAnimator.createRtAnimator(new COUIRtAnimationImpl(cOUISpringAnimation), view);
        OplusRenderNodeAnimator.animateToFinalPosition(animatorCreateRtAnimator, f3);
        return animatorCreateRtAnimator;
    }
}