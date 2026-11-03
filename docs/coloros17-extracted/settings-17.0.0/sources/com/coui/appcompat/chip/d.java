package com.coui.appcompat.chip;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipDrawable.CloseIconAnimation f13319a;

    public /* synthetic */ d(COUIChipDrawable.CloseIconAnimation closeIconAnimation) {
        this.f13319a = closeIconAnimation;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIChipDrawable.CloseIconAnimation.a(this.f13319a, cOUIDynamicAnimation, z2, f3, f4);
    }
}