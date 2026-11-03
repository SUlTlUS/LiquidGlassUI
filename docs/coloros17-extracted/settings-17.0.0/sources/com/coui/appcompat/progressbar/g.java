package com.coui.appcompat.progressbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class g implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICircularProgressDrawable f13463a;

    public /* synthetic */ g(COUICircularProgressDrawable cOUICircularProgressDrawable) {
        this.f13463a = cOUICircularProgressDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUICircularProgressDrawable.d(this.f13463a, valueAnimator);
    }
}