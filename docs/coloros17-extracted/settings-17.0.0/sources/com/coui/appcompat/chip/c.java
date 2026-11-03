package com.coui.appcompat.chip;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChip.ChipGroupAnimatorImpl f13318a;

    public /* synthetic */ c(COUIChip.ChipGroupAnimatorImpl chipGroupAnimatorImpl) {
        this.f13318a = chipGroupAnimatorImpl;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIChip.ChipGroupAnimatorImpl.a(this.f13318a, cOUIDynamicAnimation, z2, f3, f4);
    }
}