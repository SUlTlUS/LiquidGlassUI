package com.coui.appcompat.seekbar.effect;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ LoadingEffectRenderer f13550a;

    public /* synthetic */ i(LoadingEffectRenderer loadingEffectRenderer) {
        this.f13550a = loadingEffectRenderer;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        LoadingEffectRenderer.a(this.f13550a, cOUIDynamicAnimation, z2, f3, f4);
    }
}