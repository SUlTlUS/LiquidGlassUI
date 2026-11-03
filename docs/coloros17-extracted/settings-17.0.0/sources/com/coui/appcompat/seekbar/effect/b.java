package com.coui.appcompat.seekbar.effect;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConicLightEffectRenderer f13538a;

    public /* synthetic */ b(ConicLightEffectRenderer conicLightEffectRenderer) {
        this.f13538a = conicLightEffectRenderer;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        ConicLightEffectRenderer.a(this.f13538a, cOUIDynamicAnimation, z2, f3, f4);
    }
}