package com.coui.appcompat.seekbar.animation;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ LoadingDragReturnHandler f13513a;

    public /* synthetic */ d(LoadingDragReturnHandler loadingDragReturnHandler) {
        this.f13513a = loadingDragReturnHandler;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        LoadingDragReturnHandler.b(this.f13513a, cOUIDynamicAnimation, z2, f3, f4);
    }
}