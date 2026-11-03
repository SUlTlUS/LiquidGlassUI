package com.coui.appcompat.tips.def;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTips f13611a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ boolean f13612b;

    public /* synthetic */ f(COUIDefaultTopTips cOUIDefaultTopTips, boolean z2) {
        this.f13611a = cOUIDefaultTopTips;
        this.f13612b = z2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIDefaultTopTips.a(this.f13611a, this.f13612b, valueAnimator);
    }
}