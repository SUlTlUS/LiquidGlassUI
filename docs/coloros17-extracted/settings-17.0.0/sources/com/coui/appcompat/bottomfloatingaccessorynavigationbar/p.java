package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class p implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryView f13266a;

    public /* synthetic */ p(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView) {
        this.f13266a = cOUIBottomFloatingAccessoryView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIBottomFloatingAccessoryView.g(this.f13266a, cOUIDynamicAnimation, f3, f4);
    }
}