package com.coui.appcompat.seekbar.animation;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class j implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ StandardAnimationHandler f13524a;

    public /* synthetic */ j(StandardAnimationHandler standardAnimationHandler) {
        this.f13524a = standardAnimationHandler;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        StandardAnimationHandler.b(this.f13524a, cOUIDynamicAnimation, f3, f4);
    }
}