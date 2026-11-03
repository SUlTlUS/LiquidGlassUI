package com.coui.appcompat.progressbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICircularProgressDrawable f13460a;

    public /* synthetic */ d(COUICircularProgressDrawable cOUICircularProgressDrawable) {
        this.f13460a = cOUICircularProgressDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUICircularProgressDrawable.f(this.f13460a, valueAnimator);
    }
}