package com.coui.appcompat.searchview;

import android.animation.ValueAnimator;
import android.graphics.Insets;
import android.view.WindowInsetsAnimationController;
import android.view.animation.Interpolator;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class q implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ WindowInsetsAnimationController f13501a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ValueAnimator f13502b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ CustomWindowInsetsAnimationControlListener f13503c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ Interpolator f13504d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ Insets f13505e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final /* synthetic */ Insets f13506f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final /* synthetic */ Interpolator f13507g;

    public /* synthetic */ q(WindowInsetsAnimationController windowInsetsAnimationController, ValueAnimator valueAnimator, CustomWindowInsetsAnimationControlListener customWindowInsetsAnimationControlListener, Interpolator interpolator, Insets insets, Insets insets2, Interpolator interpolator2) {
        this.f13501a = windowInsetsAnimationController;
        this.f13502b = valueAnimator;
        this.f13503c = customWindowInsetsAnimationControlListener;
        this.f13504d = interpolator;
        this.f13505e = insets;
        this.f13506f = insets2;
        this.f13507g = interpolator2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        CustomWindowInsetsAnimationControlListener.b(this.f13501a, this.f13502b, this.f13503c, this.f13504d, this.f13505e, this.f13506f, this.f13507g, valueAnimator);
    }
}