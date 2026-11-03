package com.coui.appcompat.toolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class t implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarGroupTransitionController f13675a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUISpringAnimation f13676b;

    public /* synthetic */ t(ToolbarGroupTransitionController toolbarGroupTransitionController, COUISpringAnimation cOUISpringAnimation) {
        this.f13675a = toolbarGroupTransitionController;
        this.f13676b = cOUISpringAnimation;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        ToolbarGroupTransitionController.h(this.f13675a, this.f13676b, cOUIDynamicAnimation, z2, f3, f4);
    }
}