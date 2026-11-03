package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class m implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryView f13263a;

    public /* synthetic */ m(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView) {
        this.f13263a = cOUIBottomFloatingAccessoryView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIBottomFloatingAccessoryView.e(this.f13263a, cOUIDynamicAnimation, f3, f4);
    }
}