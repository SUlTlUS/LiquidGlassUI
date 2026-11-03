package com.coui.appcompat.searchview;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class l implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchBar f13495a;

    public /* synthetic */ l(COUISearchBar cOUISearchBar) {
        this.f13495a = cOUISearchBar;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUISearchBar.i(this.f13495a, cOUIDynamicAnimation, f3, f4);
    }
}