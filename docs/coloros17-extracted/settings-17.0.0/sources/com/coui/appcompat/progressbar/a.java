package com.coui.appcompat.progressbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICircularProgressDrawable f13457a;

    public /* synthetic */ a(COUICircularProgressDrawable cOUICircularProgressDrawable) {
        this.f13457a = cOUICircularProgressDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUICircularProgressDrawable.h(this.f13457a, valueAnimator);
    }
}