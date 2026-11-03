package com.coui.appcompat.seekbar.animation;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class g implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SectionAnimationHandler f13518a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ float f13519b;

    public /* synthetic */ g(SectionAnimationHandler sectionAnimationHandler, float f3) {
        this.f13518a = sectionAnimationHandler;
        this.f13519b = f3;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SectionAnimationHandler.a(this.f13518a, this.f13519b, valueAnimator);
    }
}