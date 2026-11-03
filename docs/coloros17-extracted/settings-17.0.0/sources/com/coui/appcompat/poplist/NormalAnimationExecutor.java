package com.coui.appcompat.poplist;

import android.animation.Animator;
import android.view.View;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;

/* JADX INFO: loaded from: classes7.dex */
class NormalAnimationExecutor implements AnimationExecutor {
    static final NormalAnimationExecutor INSTANCE = new NormalAnimationExecutor();

    private NormalAnimationExecutor() {
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public boolean isAsynchronous() {
        return false;
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public void runOnMainThread(View view, Runnable runnable) {
        runnable.run();
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public void setAlpha(View view, float f3) {
        view.setAlpha(f3);
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public void setScaleX(View view, float f3) {
        view.setScaleX(f3);
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public void setScaleY(View view, float f3) {
        view.setScaleY(f3);
    }

    @Override // com.coui.appcompat.poplist.AnimationExecutor
    public Animator startAnimation(COUISpringAnimation cOUISpringAnimation, View view, float f3) {
        cOUISpringAnimation.animateToFinalPosition(f3);
        return null;
    }
}