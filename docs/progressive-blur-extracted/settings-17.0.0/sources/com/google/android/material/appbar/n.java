package com.google.android.material.appbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class n implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIViewOffsetHelper f13739a;

    public /* synthetic */ n(COUIViewOffsetHelper cOUIViewOffsetHelper) {
        this.f13739a = cOUIViewOffsetHelper;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIViewOffsetHelper.b(this.f13739a, cOUIDynamicAnimation, z2, f3, f4);
    }
}