package com.coui.appcompat.seekbar.core;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISeekBarCore f13532a;

    public /* synthetic */ d(COUISeekBarCore cOUISeekBarCore) {
        this.f13532a = cOUISeekBarCore;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUISeekBarCore.c(this.f13532a, cOUIDynamicAnimation, f3, f4);
    }
}