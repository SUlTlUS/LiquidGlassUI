package com.coui.appcompat.tips.def;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIDefaultTopTips f13606a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ float f13607b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ float f13608c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ float f13609d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ float f13610e;

    public /* synthetic */ e(COUIDefaultTopTips cOUIDefaultTopTips, float f3, float f4, float f5, float f6) {
        this.f13606a = cOUIDefaultTopTips;
        this.f13607b = f3;
        this.f13608c = f4;
        this.f13609d = f5;
        this.f13610e = f6;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIDefaultTopTips.b(this.f13606a, this.f13607b, this.f13608c, this.f13609d, this.f13610e, valueAnimator);
    }
}