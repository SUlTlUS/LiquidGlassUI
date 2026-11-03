package com.coui.appcompat.chip;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipGroup f13320a;

    public /* synthetic */ e(COUIChipGroup cOUIChipGroup) {
        this.f13320a = cOUIChipGroup;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIChipGroup.c(this.f13320a, cOUIDynamicAnimation, z2, f3, f4);
    }
}