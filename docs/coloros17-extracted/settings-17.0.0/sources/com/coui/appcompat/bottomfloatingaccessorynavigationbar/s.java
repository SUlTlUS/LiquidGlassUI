package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class s implements COUIComposeMotionAnimator.OnAnimationUpdateCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomFloatingAccessoryView f13269a;

    public /* synthetic */ s(COUIBottomFloatingAccessoryView cOUIBottomFloatingAccessoryView) {
        this.f13269a = cOUIBottomFloatingAccessoryView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator.OnAnimationUpdateCallback
    public final void onAnimationUpdate(COUIIComposeMotionUnit.ContributeSink contributeSink) {
        COUIBottomFloatingAccessoryView.a(this.f13269a, contributeSink);
    }
}