package com.coui.appcompat.seekbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIVerticalSeekBar f13535a;

    public /* synthetic */ d(COUIVerticalSeekBar cOUIVerticalSeekBar) {
        this.f13535a = cOUIVerticalSeekBar;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIVerticalSeekBar.c(this.f13535a, valueAnimator);
    }
}