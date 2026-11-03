package com.coui.appcompat.poplist;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class v implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUITouchRecyclerView f13449a;

    public /* synthetic */ v(COUITouchRecyclerView cOUITouchRecyclerView) {
        this.f13449a = cOUITouchRecyclerView;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUITouchRecyclerView.d(this.f13449a, cOUIDynamicAnimation, z2, f3, f4);
    }
}