package com.coui.appcompat.bottomfloatingnavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingNavigationBar f13275a;

    public /* synthetic */ e(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        this.f13275a = cOUIBottomFloatingNavigationBar;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIBottomFloatingNavigationBar.d(this.f13275a, cOUIDynamicAnimation, z2, f3, f4);
    }
}