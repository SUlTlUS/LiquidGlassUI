package com.coui.appcompat.seekbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIVerticalSeekBar f13527a;

    public /* synthetic */ c(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        this.f13527a = cOUIVerticalSeekBar;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIVerticalSeekBar.b(this.f13527a, valueAnimator);
    }
}