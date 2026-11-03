package com.coui.appcompat.progressbar;

import androidx.dynamicanimation.animation.DynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements DynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICircularProgressDrawable f13459a;

    public /* synthetic */ c(COUICircularProgressDrawable cOUICircularProgressDrawable) {
        this.f13459a = cOUICircularProgressDrawable;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f3, float f4) {
        COUICircularProgressDrawable.e(this.f13459a, dynamicAnimation, f3, f4);
    }
}