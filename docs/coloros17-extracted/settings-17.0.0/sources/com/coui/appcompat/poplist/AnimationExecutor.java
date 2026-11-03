package com.coui.appcompat.poplist;

import android.animation.Animator;
import android.view.View;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;

/* JADX INFO: loaded from: classes7.dex */
interface AnimationExecutor {
    boolean isAsynchronous();

    void runOnMainThread(View view, Runnable runnable);

    void setAlpha(View view, float f3);

    void setScaleX(View view, float f3);

    void setScaleY(View view, float f3);

    Animator startAnimation(COUISpringAnimation cOUISpringAnimation, View view, float f3);
}