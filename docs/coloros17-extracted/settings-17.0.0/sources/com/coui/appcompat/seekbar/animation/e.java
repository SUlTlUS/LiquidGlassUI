package com.coui.appcompat.seekbar.animation;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SectionAnimationHandler f13514a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ float f13515b;

    public /* synthetic */ e(SectionAnimationHandler sectionAnimationHandler, float f3) {
        this.f13514a = sectionAnimationHandler;
        this.f13515b = f3;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SectionAnimationHandler.c(this.f13514a, this.f13515b, valueAnimator);
    }
}