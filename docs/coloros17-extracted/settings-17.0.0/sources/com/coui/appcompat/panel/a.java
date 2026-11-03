package com.coui.appcompat.panel;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomSheetDialog f13389a;

    public /* synthetic */ a(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        this.f13389a = cOUIBottomSheetDialog;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIBottomSheetDialog.c(this.f13389a, cOUIDynamicAnimation, f3, f4);
    }
}