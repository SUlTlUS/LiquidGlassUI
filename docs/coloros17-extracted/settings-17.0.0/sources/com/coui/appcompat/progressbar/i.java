package com.coui.appcompat.progressbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICircularProgressDrawable f13465a;

    public /* synthetic */ i(COUICircularProgressDrawable cOUICircularProgressDrawable) {
        this.f13465a = cOUICircularProgressDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUICircularProgressDrawable.g(this.f13465a, valueAnimator);
    }
}