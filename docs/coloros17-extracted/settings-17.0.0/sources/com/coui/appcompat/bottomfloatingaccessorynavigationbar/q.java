package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class q implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryView f13267a;

    public /* synthetic */ q(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView) {
        this.f13267a = cOUIBottomFloatingAccessoryView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIBottomFloatingAccessoryView.c(this.f13267a, cOUIDynamicAnimation, f3, f4);
    }
}