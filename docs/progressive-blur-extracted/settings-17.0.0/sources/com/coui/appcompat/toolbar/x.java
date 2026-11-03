package com.coui.appcompat.toolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class x implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarGroupTransitionController f13681a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUISpringAnimation f13682b;

    public /* synthetic */ x(ToolbarGroupTransitionController toolbarGroupTransitionController, COUISpringAnimation cOUISpringAnimation) {
        this.f13681a = toolbarGroupTransitionController;
        this.f13682b = cOUISpringAnimation;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        ToolbarGroupTransitionController.j(this.f13681a, this.f13682b, cOUIDynamicAnimation, z2, f3, f4);
    }
}