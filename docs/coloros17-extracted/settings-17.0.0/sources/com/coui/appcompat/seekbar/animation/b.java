package com.coui.appcompat.seekbar.animation;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ FlexibleFollowHandHandler f13511a;

    public /* synthetic */ b(FlexibleFollowHandHandler flexibleFollowHandHandler) {
        this.f13511a = flexibleFollowHandHandler;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        FlexibleFollowHandHandler.a(this.f13511a, cOUIDynamicAnimation, f3, f4);
    }
}