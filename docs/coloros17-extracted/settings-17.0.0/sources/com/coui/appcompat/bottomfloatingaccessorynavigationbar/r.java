package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class r implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryView f13268a;

    public /* synthetic */ r(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView) {
        this.f13268a = cOUIBottomFloatingAccessoryView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIBottomFloatingAccessoryView.f(this.f13268a, cOUIDynamicAnimation, z2, f3, f4);
    }
}