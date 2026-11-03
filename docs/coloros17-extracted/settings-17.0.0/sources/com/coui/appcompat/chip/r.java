package com.coui.appcompat.chip;

import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import kotlin.jvm.functions.Function0;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class r implements COUIDynamicAnimation.OnAnimationEndListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Function0 f13339a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUIChipTabGroupFragmentMediator f13340b;

    public /* synthetic */ r(Function0 function0, COUIChipTabGroupFragmentMediator cOUIChipTabGroupFragmentMediator) {
        this.f13339a = function0;
        this.f13340b = cOUIChipTabGroupFragmentMediator;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        COUIChipTabGroupFragmentMediator.c(this.f13339a, this.f13340b, cOUIDynamicAnimation, z2, f3, f4);
    }
}