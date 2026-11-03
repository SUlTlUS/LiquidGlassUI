package com.coui.appcompat.bottomfloatingnavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class j implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingNavigationBarMenuView f13280a;

    public /* synthetic */ j(COUIBottomFloatingNavigationBarMenuView cOUIBottomFloatingNavigationBarMenuView) {
        this.f13280a = cOUIBottomFloatingNavigationBarMenuView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIBottomFloatingNavigationBarMenuView.b(this.f13280a, cOUIDynamicAnimation, z2, f3, f4);
    }
}