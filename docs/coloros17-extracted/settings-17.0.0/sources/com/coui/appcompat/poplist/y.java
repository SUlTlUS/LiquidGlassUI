package com.coui.appcompat.poplist;

import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.OnAnimationEndListener;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class y implements OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DefaultScreenAnimationController f13451a;

    public /* synthetic */ y(DefaultScreenAnimationController defaultScreenAnimationController) {
        this.f13451a = defaultScreenAnimationController;
    }

    @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
        DefaultScreenAnimationController.d(this.f13451a, cOUIIComposeMotionUnit, z2, f3, f4);
    }
}