package com.coui.appcompat.searchview;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class n implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchViewAnimate f13497a;

    public /* synthetic */ n(COUISearchViewAnimate cOUISearchViewAnimate) {
        this.f13497a = cOUISearchViewAnimate;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUISearchViewAnimate.a(this.f13497a, valueAnimator);
    }
}