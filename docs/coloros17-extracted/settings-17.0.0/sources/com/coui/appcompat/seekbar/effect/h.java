package com.coui.appcompat.seekbar.effect;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class h implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ LoadingEffectRenderer f13549a;

    public /* synthetic */ h(LoadingEffectRenderer loadingEffectRenderer) {
        this.f13549a = loadingEffectRenderer;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        LoadingEffectRenderer.d(this.f13549a, cOUIDynamicAnimation, f3, f4);
    }
}