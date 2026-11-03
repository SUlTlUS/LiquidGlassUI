package com.coui.appcompat.panel;

import android.animation.ValueAnimator;
import com.oplus.dynamicframerate.AnimationVelocityCalculator;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIBottomSheetDialog f13390a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AnimationVelocityCalculator f13391b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ ValueAnimator f13392c;

    public /* synthetic */ b(COUIBottomSheetDialog cOUIBottomSheetDialog, AnimationVelocityCalculator animationVelocityCalculator, ValueAnimator valueAnimator) {
        this.f13390a = cOUIBottomSheetDialog;
        this.f13391b = animationVelocityCalculator;
        this.f13392c = valueAnimator;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIBottomSheetDialog.b(this.f13390a, this.f13391b, this.f13392c, valueAnimator);
    }
}