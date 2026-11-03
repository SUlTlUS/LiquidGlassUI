package com.google.android.material.appbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class o implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIViewOffsetHelper f13740a;

    public /* synthetic */ o(COUIViewOffsetHelper cOUIViewOffsetHelper) {
        this.f13740a = cOUIViewOffsetHelper;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIViewOffsetHelper.a(this.f13740a, cOUIDynamicAnimation, f3, f4);
    }
}