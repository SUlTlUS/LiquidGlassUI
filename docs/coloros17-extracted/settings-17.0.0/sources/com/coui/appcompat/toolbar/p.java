package com.coui.appcompat.toolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class p implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarGroupTransitionController f13667a;

    public /* synthetic */ p(ToolbarGroupTransitionController toolbarGroupTransitionController) {
        this.f13667a = toolbarGroupTransitionController;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ToolbarGroupTransitionController.b(this.f13667a, cOUIDynamicAnimation, f3, f4);
    }
}