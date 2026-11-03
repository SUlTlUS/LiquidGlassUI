package com.coui.appcompat.bottomfloatingtoolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class h implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ItemViewAnimatorImpl f13288a;

    public /* synthetic */ h(ItemViewAnimatorImpl itemViewAnimatorImpl) {
        this.f13288a = itemViewAnimatorImpl;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        ItemViewAnimatorImpl.a(this.f13288a, cOUIDynamicAnimation, z2, f3, f4);
    }
}