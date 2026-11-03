package com.coui.appcompat.seekbar.core;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISeekBarAdvanced f13529a;

    public /* synthetic */ b(COUISeekBarAdvanced cOUISeekBarAdvanced) {
        this.f13529a = cOUISeekBarAdvanced;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUISeekBarAdvanced.f(this.f13529a, cOUIDynamicAnimation, z2, f3, f4);
    }
}