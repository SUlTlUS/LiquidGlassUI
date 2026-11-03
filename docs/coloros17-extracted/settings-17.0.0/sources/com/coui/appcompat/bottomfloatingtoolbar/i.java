package com.coui.appcompat.bottomfloatingtoolbar;

import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ItemViewAnimatorImpl f13289a;

    public /* synthetic */ i(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        this.f13289a = itemViewAnimatorImpl;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ItemViewAnimatorImpl.c(this.f13289a, valueAnimator);
    }
}