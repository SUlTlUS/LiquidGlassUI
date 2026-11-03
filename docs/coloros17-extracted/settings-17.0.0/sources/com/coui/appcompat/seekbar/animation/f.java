package com.coui.appcompat.seekbar.animation;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SectionAnimationHandler f13516a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ float f13517b;

    public /* synthetic */ f(SectionAnimationHandler sectionAnimationHandler, float f3) {
        this.f13516a = sectionAnimationHandler;
        this.f13517b = f3;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SectionAnimationHandler.b(this.f13516a, this.f13517b, valueAnimator);
    }
}