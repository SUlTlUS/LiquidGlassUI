package com.coui.appcompat.panel;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPanelAdjustResizeHelperAfterR f13393a;

    public /* synthetic */ c(COUIPanelAdjustResizeHelperAfterR cOUIPanelAdjustResizeHelperAfterR) {
        this.f13393a = cOUIPanelAdjustResizeHelperAfterR;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIPanelAdjustResizeHelperAfterR.a(this.f13393a, cOUIDynamicAnimation, f3, f4);
    }
}