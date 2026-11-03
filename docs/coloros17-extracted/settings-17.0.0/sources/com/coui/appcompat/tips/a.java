package com.coui.appcompat.tips;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIMarqueeTextView f13595a;

    public /* synthetic */ a(COUIMarqueeTextView cOUIMarqueeTextView) {
        this.f13595a = cOUIMarqueeTextView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIMarqueeTextView.a(this.f13595a, valueAnimator);
    }
}