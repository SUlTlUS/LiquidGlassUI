package com.coui.appcompat.seekbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIVerticalSeekBar f13526a;

    public /* synthetic */ b(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        this.f13526a = cOUIVerticalSeekBar;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIVerticalSeekBar.a(this.f13526a, valueAnimator);
    }
}