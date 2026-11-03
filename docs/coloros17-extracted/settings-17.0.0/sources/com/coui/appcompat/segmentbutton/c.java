package com.coui.appcompat.segmentbutton;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISegmentButtonLayout f13556a;

    public /* synthetic */ c(COUISegmentButtonLayout cOUISegmentButtonLayout) {
        this.f13556a = cOUISegmentButtonLayout;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUISegmentButtonLayout.c(this.f13556a, cOUIDynamicAnimation, f3, f4);
    }
}