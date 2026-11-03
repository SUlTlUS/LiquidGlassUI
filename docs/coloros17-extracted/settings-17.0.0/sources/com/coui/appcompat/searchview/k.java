package com.coui.appcompat.searchview;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class k implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchBar f13494a;

    public /* synthetic */ k(COUISearchBar cOUISearchBar) {
        this.f13494a = cOUISearchBar;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUISearchBar.c(this.f13494a, cOUIDynamicAnimation, f3, f4);
    }
}