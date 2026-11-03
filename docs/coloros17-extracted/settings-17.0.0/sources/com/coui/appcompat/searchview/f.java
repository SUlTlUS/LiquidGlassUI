package com.coui.appcompat.searchview;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class f implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchBar f13488a;

    public /* synthetic */ f(COUISearchBar cOUISearchBar) {
        this.f13488a = cOUISearchBar;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUISearchBar.g(this.f13488a, cOUIDynamicAnimation, f3, f4);
    }
}