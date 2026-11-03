package com.coui.appcompat.segmentbutton;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISegmentButtonLayout f13555a;

    public /* synthetic */ b(COUISegmentButtonLayout cOUISegmentButtonLayout) {
        this.f13555a = cOUISegmentButtonLayout;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUISegmentButtonLayout.d(this.f13555a, cOUIDynamicAnimation, f3, f4);
    }
}