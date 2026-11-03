package com.coui.appcompat.seekbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIVerticalSeekBar f13536a;

    public /* synthetic */ e(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        this.f13536a = cOUIVerticalSeekBar;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIVerticalSeekBar.d(this.f13536a, valueAnimator);
    }
}