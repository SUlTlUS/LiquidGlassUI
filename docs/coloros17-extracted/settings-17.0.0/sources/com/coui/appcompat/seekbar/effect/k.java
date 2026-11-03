package com.coui.appcompat.seekbar.effect;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class k implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ LoadingEffectRenderer f13552a;

    public /* synthetic */ k(LoadingEffectRenderer loadingEffectRenderer) {
        this.f13552a = loadingEffectRenderer;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        LoadingEffectRenderer.b(this.f13552a, cOUIDynamicAnimation, z2, f3, f4);
    }
}