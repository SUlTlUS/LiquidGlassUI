package com.coui.appcompat.seekbar.core;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import kotlin.jvm.internal.Ref;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISeekBarAdvanced f13530a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Ref.IntRef f13531b;

    public /* synthetic */ c(COUISeekBarAdvanced cOUISeekBarAdvanced, Ref.IntRef intRef) {
        this.f13530a = cOUISeekBarAdvanced;
        this.f13531b = intRef;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUISeekBarAdvanced.e(this.f13530a, this.f13531b, cOUIDynamicAnimation, z2, f3, f4);
    }
}