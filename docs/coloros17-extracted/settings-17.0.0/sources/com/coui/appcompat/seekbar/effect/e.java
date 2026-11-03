package com.coui.appcompat.seekbar.effect;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeformationEffectRenderer f13541a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ float f13542b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ float f13543c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ float f13544d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ float f13545e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final /* synthetic */ float f13546f;

    public /* synthetic */ e(DeformationEffectRenderer deformationEffectRenderer, float f3, float f4, float f5, float f6, float f7) {
        this.f13541a = deformationEffectRenderer;
        this.f13542b = f3;
        this.f13543c = f4;
        this.f13544d = f5;
        this.f13545e = f6;
        this.f13546f = f7;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        DeformationEffectRenderer.a(this.f13541a, this.f13542b, this.f13543c, this.f13544d, this.f13545e, this.f13546f, valueAnimator);
    }
}