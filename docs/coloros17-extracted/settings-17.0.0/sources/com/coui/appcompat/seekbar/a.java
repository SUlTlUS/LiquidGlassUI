package com.coui.appcompat.seekbar;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIIntentSeekBar f13509a;

    public /* synthetic */ a(COUIIntentSeekBar cOUIIntentSeekBar) {
        this.f13509a = cOUIIntentSeekBar;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUIIntentSeekBar.g(this.f13509a, cOUIDynamicAnimation, f3, f4);
    }
}