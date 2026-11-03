package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import com.google.android.material.shape.MaterialShapeDrawable;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class b implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppBarLayout f13717a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MaterialShapeDrawable f13718b;

    public /* synthetic */ b(AppBarLayout appBarLayout, MaterialShapeDrawable materialShapeDrawable) {
        this.f13717a = appBarLayout;
        this.f13718b = materialShapeDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        AppBarLayout.a(this.f13717a, this.f13718b, valueAnimator);
    }
}