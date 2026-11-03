package com.coui.appcompat.bottomfloatingedittoolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingEditToolbar f13270a;

    public /* synthetic */ a(COUIBottomFloatingEditToolbar cOUIBottomFloatingEditToolbar) {
        this.f13270a = cOUIBottomFloatingEditToolbar;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIBottomFloatingEditToolbar.a(this.f13270a, cOUIDynamicAnimation, z2, f3, f4);
    }
}