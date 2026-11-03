package com.google.android.material.appbar;

import android.widget.TextView;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class f implements COUIDynamicAnimation.OnAnimationUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ TextView f13725a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f13726b;

    public /* synthetic */ f(TextView textView, int i3) {
        this.f13725a = textView;
        this.f13726b = i3;
    }

    @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        COUICollapsingToolbarLayout.a(this.f13725a, this.f13726b, cOUIDynamicAnimation, f3, f4);
    }
}