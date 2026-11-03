package com.coui.appcompat.bottomfloatingtoolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class l implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MenuViewAnimatorImpl f13290a;

    public /* synthetic */ l(MenuViewAnimatorImpl menuViewAnimatorImpl) {
        this.f13290a = menuViewAnimatorImpl;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        MenuViewAnimatorImpl.a(this.f13290a, cOUIDynamicAnimation, z2, f3, f4);
    }
}