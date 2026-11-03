package com.coui.appcompat.toolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class c0 implements COUIComposeMotionAnimator.OnAnimationUpdateCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarMaterialEffectDelegate f13627a;

    public /* synthetic */ c0(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate) {
        this.f13627a = toolbarMaterialEffectDelegate;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator.OnAnimationUpdateCallback
    public final void onAnimationUpdate(COUIIComposeMotionUnit.ContributeSink contributeSink) {
        ToolbarMaterialEffectDelegate.c(this.f13627a, contributeSink);
    }
}