package com.coui.appcompat.searchview;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class e implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchBar f13487a;

    public /* synthetic */ e(COUISearchBar cOUISearchBar) {
        this.f13487a = cOUISearchBar;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUISearchBar.b(this.f13487a, cOUIDynamicAnimation, z2, f3, f4);
    }
}