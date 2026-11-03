package com.coui.appcompat.seekbar.effect;

import com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class l implements COUIComposeMotionAnimator.OnAnimationUpdateCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ TouchMotionEffectRenderer f13553a;

    public /* synthetic */ l(TouchMotionEffectRenderer touchMotionEffectRenderer) {
        this.f13553a = touchMotionEffectRenderer;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator.OnAnimationUpdateCallback
    public final void onAnimationUpdate(COUIIComposeMotionUnit.ContributeSink contributeSink) {
        TouchMotionEffectRenderer.a(this.f13553a, contributeSink);
    }
}