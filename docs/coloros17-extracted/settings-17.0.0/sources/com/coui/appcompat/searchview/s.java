package com.coui.appcompat.searchview;

import android.view.animation.Interpolator;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class s implements Interpolator {
    @Override // android.animation.TimeInterpolator
    public final float getInterpolation(float f3) {
        return CustomWindowInsetsAnimationControlListener.a(f3);
    }
}