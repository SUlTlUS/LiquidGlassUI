package com.coui.appcompat.seekbar.core;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISeekBarAdvanced f13528a;

    public /* synthetic */ a(COUISeekBarAdvanced cOUISeekBarAdvanced) {
        this.f13528a = cOUISeekBarAdvanced;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUISeekBarAdvanced.d(this.f13528a, cOUIDynamicAnimation, f3, f4);
    }
}