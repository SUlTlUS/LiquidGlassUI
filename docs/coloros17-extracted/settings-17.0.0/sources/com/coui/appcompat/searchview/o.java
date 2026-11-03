package com.coui.appcompat.searchview;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class o implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchViewAnimate f13498a;

    public /* synthetic */ o(COUISearchViewAnimate cOUISearchViewAnimate) {
        this.f13498a = cOUISearchViewAnimate;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUISearchViewAnimate.b(this.f13498a, valueAnimator);
    }
}