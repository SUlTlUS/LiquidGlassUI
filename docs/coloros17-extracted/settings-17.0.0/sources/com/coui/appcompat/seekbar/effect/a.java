package com.coui.appcompat.seekbar.effect;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConicLightEffectRenderer f13537a;

    public /* synthetic */ a(ConicLightEffectRenderer conicLightEffectRenderer) {
        this.f13537a = conicLightEffectRenderer;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ConicLightEffectRenderer.c(this.f13537a, cOUIDynamicAnimation, f3, f4);
    }
}