package com.coui.appcompat.seekbar.animation;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class k implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ThumbStateAnimationHandler f13525a;

    public /* synthetic */ k(ThumbStateAnimationHandler thumbStateAnimationHandler) {
        this.f13525a = thumbStateAnimationHandler;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ThumbStateAnimationHandler.a(this.f13525a, cOUIDynamicAnimation, f3, f4);
    }
}