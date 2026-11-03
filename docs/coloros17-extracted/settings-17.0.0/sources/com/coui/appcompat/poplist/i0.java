package com.coui.appcompat.poplist;

import com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i0 implements COUIComposeMotionAnimator.OnAnimationUpdateCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SmallScreenAnimationController f13436a;

    public /* synthetic */ i0(SmallScreenAnimationController smallScreenAnimationController) {
        this.f13436a = smallScreenAnimationController;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator.OnAnimationUpdateCallback
    public final void onAnimationUpdate(COUIIComposeMotionUnit.ContributeSink contributeSink) {
        SmallScreenAnimationController.e(this.f13436a, contributeSink);
    }
}