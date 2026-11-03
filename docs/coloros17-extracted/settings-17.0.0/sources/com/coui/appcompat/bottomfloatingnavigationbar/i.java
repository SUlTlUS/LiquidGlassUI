package com.coui.appcompat.bottomfloatingnavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingNavigationBarMenuView f13279a;

    public /* synthetic */ i(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView) {
        this.f13279a = cOUIBottomFloatingNavigationBarMenuView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIBottomFloatingNavigationBarMenuView.c(this.f13279a, cOUIDynamicAnimation, z2, f3, f4);
    }
}