package com.coui.appcompat.chip;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class l implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipTabDrawable.LayerAlphaAnimation f13327a;

    public /* synthetic */ l(COUIChipTabDrawable.LayerAlphaAnimation layerAlphaAnimation) {
        this.f13327a = layerAlphaAnimation;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIChipTabDrawable.LayerAlphaAnimation.a(this.f13327a, cOUIDynamicAnimation, f3, f4);
    }
}