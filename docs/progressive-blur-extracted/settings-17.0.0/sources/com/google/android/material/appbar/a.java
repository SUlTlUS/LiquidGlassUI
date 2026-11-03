package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import com.google.android.material.shape.MaterialShapeDrawable;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class a implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppBarLayout f13713a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ColorStateList f13714b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MaterialShapeDrawable f13715c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ Integer f13716d;

    public /* synthetic */ a(AppBarLayout appBarLayout, ColorStateList colorStateList, MaterialShapeDrawable materialShapeDrawable, Integer num) {
        this.f13713a = appBarLayout;
        this.f13714b = colorStateList;
        this.f13715c = materialShapeDrawable;
        this.f13716d = num;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        AppBarLayout.b(this.f13713a, this.f13714b, this.f13715c, this.f13716d, valueAnimator);
    }
}