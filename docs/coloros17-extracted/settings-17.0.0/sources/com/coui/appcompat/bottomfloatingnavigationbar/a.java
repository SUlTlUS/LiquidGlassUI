package com.coui.appcompat.bottomfloatingnavigationbar;

import com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIComposeMotionAnimator.OnAnimationUpdateCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BaseBottomFloatingMenuView f13271a;

    public /* synthetic */ a(BaseBottomFloatingMenuView baseBottomFloatingMenuView) {
        this.f13271a = baseBottomFloatingMenuView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator.OnAnimationUpdateCallback
    public final void onAnimationUpdate(COUIIComposeMotionUnit.ContributeSink contributeSink) {
        BaseBottomFloatingMenuView.a(this.f13271a, contributeSink);
    }
}