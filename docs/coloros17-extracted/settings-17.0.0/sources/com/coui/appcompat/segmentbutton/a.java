package com.coui.appcompat.segmentbutton;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISegmentButtonLayout f13554a;

    public /* synthetic */ a(COUISegmentButtonLayout cOUISegmentButtonLayout) {
        this.f13554a = cOUISegmentButtonLayout;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUISegmentButtonLayout.b(this.f13554a, cOUIDynamicAnimation, f3, f4);
    }
}