package com.coui.appcompat.progressbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICircularProgressDrawable f13458a;

    public /* synthetic */ b(COUICircularProgressDrawable cOUICircularProgressDrawable) {
        this.f13458a = cOUICircularProgressDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUICircularProgressDrawable.a(this.f13458a, valueAnimator);
    }
}