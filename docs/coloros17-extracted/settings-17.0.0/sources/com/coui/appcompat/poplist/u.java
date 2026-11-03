package com.coui.appcompat.poplist;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class u implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUITouchRecyclerView f13448a;

    public /* synthetic */ u(COUITouchRecyclerView cOUITouchRecyclerView) {
        this.f13448a = cOUITouchRecyclerView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUITouchRecyclerView.c(this.f13448a, cOUIDynamicAnimation, f3, f4);
    }
}