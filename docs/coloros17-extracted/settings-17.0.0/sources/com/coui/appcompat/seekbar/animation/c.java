package com.coui.appcompat.seekbar.animation;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ LoadingDragReturnHandler f13512a;

    public /* synthetic */ c(LoadingDragReturnHandler loadingDragReturnHandler) {
        this.f13512a = loadingDragReturnHandler;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        LoadingDragReturnHandler.a(this.f13512a, cOUIDynamicAnimation, f3, f4);
    }
}