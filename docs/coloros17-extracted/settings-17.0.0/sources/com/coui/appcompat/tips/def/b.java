package com.coui.appcompat.tips.def;

import android.animation.ValueAnimator;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ View[] f13601a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View[] f13602b;

    public /* synthetic */ b(View[] viewArr, View[] viewArr2) {
        this.f13601a = viewArr;
        this.f13602b = viewArr2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIDefaultTopTips.c(this.f13601a, this.f13602b, valueAnimator);
    }
}