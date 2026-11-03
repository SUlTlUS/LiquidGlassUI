package com.coui.appcompat.bottomfloatingnavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class h implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingNavigationBar f13278a;

    public /* synthetic */ h(COUIBottomFloatingNavigationBar cOUIBottomFloatingNavigationBar) {
        this.f13278a = cOUIBottomFloatingNavigationBar;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIBottomFloatingNavigationBar.e(this.f13278a, cOUIDynamicAnimation, z2, f3, f4);
    }
}