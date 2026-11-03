package com.coui.appcompat.tips.def;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTips f13596a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f13597b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f13598c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ int f13599d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ int f13600e;

    public /* synthetic */ a(COUIDefaultTopTips cOUIDefaultTopTips, int i3, int i4, int i5, int i6) {
        this.f13596a = cOUIDefaultTopTips;
        this.f13597b = i3;
        this.f13598c = i4;
        this.f13599d = i5;
        this.f13600e = i6;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIDefaultTopTips.f(this.f13596a, this.f13597b, this.f13598c, this.f13599d, this.f13600e, valueAnimator);
    }
}