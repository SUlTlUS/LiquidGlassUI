package com.google.android.material.appbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class g implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICollapsingToolbarLayout f13727a;

    public /* synthetic */ g(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        this.f13727a = cOUICollapsingToolbarLayout;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUICollapsingToolbarLayout.f(this.f13727a, cOUIDynamicAnimation, z2, f3, f4);
    }
}