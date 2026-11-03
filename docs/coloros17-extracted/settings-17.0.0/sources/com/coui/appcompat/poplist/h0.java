package com.coui.appcompat.poplist;

import com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class h0 implements COUIComposeMotionAnimator.OnAnimationUpdateCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SmallScreenAnimationController f13434a;

    public /* synthetic */ h0(SmallScreenAnimationController smallScreenAnimationController) {
        this.f13434a = smallScreenAnimationController;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator.OnAnimationUpdateCallback
    public final void onAnimationUpdate(COUIIComposeMotionUnit.ContributeSink contributeSink) {
        SmallScreenAnimationController.d(this.f13434a, contributeSink);
    }
}