package com.coui.appcompat.toolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class r implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarGroupTransitionController f13672a;

    public /* synthetic */ r(ToolbarGroupTransitionController toolbarGroupTransitionController) {
        this.f13672a = toolbarGroupTransitionController;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ToolbarGroupTransitionController.e(this.f13672a, cOUIDynamicAnimation, f3, f4);
    }
}