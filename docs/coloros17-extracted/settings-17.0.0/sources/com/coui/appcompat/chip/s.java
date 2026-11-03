package com.coui.appcompat.chip;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class s implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipTabGroupFragmentMediator f13341a;

    public /* synthetic */ s(COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator) {
        this.f13341a = cOUIChipTabGroupFragmentMediator;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIChipTabGroupFragmentMediator.d(this.f13341a, cOUIDynamicAnimation, z2, f3, f4);
    }
}