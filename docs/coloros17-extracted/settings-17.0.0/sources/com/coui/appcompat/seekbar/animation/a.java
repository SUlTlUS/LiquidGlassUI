package com.coui.appcompat.seekbar.animation;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ClickAnimationHandler f13510a;

    public /* synthetic */ a(ClickAnimationHandler clickAnimationHandler) {
        this.f13510a = clickAnimationHandler;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        ClickAnimationHandler.a(this.f13510a, cOUIDynamicAnimation, f3, f4);
    }
}