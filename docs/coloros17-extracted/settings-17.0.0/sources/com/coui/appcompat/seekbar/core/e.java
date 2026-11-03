package com.coui.appcompat.seekbar.core;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISeekBarCore f13533a;

    public /* synthetic */ e(COUISeekBarCore cOUISeekBarCore) {
        this.f13533a = cOUISeekBarCore;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUISeekBarCore.a(this.f13533a, cOUIDynamicAnimation, z2, f3, f4);
    }
}