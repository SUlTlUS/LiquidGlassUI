package com.coui.appcompat.dialog;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIGuideDialogButtonLayout f13360a;

    public /* synthetic */ f(COUIGuideDialogButtonLayout cOUIGuideDialogButtonLayout) {
        this.f13360a = cOUIGuideDialogButtonLayout;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIGuideDialogButtonLayout.c(this.f13360a, cOUIDynamicAnimation, f3, f4);
    }
}