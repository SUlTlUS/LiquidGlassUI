package com.coui.appcompat.seekbar.animation;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISeekBarCore f13521a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f13522b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ boolean f13523c;

    public /* synthetic */ i(COUISeekBarCore cOUISeekBarCore, int i3, boolean z2) {
        this.f13521a = cOUISeekBarCore;
        this.f13522b = i3;
        this.f13523c = z2;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        StandardAnimationHandler.a(this.f13521a, this.f13522b, this.f13523c, cOUIDynamicAnimation, z2, f3, f4);
    }
}