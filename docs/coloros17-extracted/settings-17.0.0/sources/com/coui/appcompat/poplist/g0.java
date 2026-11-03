package com.coui.appcompat.poplist;

import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.OnAnimationEndListener;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class g0 implements OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SmallScreenAnimationController f13432a;

    public /* synthetic */ g0(SmallScreenAnimationController smallScreenAnimationController) {
        this.f13432a = smallScreenAnimationController;
    }

    @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
        SmallScreenAnimationController.c(this.f13432a, cOUIIComposeMotionUnit, z2, f3, f4);
    }
}