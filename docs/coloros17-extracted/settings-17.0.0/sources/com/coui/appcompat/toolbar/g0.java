package com.coui.appcompat.toolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class g0 implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarTextTransitionController f13638a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUISpringAnimation f13639b;

    public /* synthetic */ g0(ToolbarTextTransitionController toolbarTextTransitionController, COUISpringAnimation cOUISpringAnimation) {
        this.f13638a = toolbarTextTransitionController;
        this.f13639b = cOUISpringAnimation;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        ToolbarTextTransitionController.d(this.f13638a, this.f13639b, cOUIDynamicAnimation, z2, f3, f4);
    }
}