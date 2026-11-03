package com.coui.appcompat.bottomfloatingtoolbar;

import com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements COUIComposeMotionAnimator.OnAnimationUpdateCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingToolbarMenuView f13283a;

    public /* synthetic */ e(COUIBottomFloatingToolbarMenuView cOUIBottomFloatingToolbarMenuView) {
        this.f13283a = cOUIBottomFloatingToolbarMenuView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator.OnAnimationUpdateCallback
    public final void onAnimationUpdate(COUIIComposeMotionUnit.ContributeSink contributeSink) {
        COUIBottomFloatingToolbarMenuView.a(this.f13283a, contributeSink);
    }
}