package com.coui.appcompat.seekbar.effect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, d2 = {"com/coui/appcompat/seekbar/effect/DeformationEffectRenderer$startDeformationRelease$1$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationCancel", "", "animation", "Landroid/animation/Animator;", "onAnimationEnd", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DeformationEffectRenderer$startDeformationRelease$1$2 extends AnimatorListenerAdapter {
    final /* synthetic */ DeformationEffectRenderer this$0;

    public DeformationEffectRenderer$startDeformationRelease$1$2(DeformationEffectRenderer deformationEffectRenderer) {
        this.this$0 = deformationEffectRenderer;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animation) {
        Intrinsics.checkNotNullParameter(animation, "animation");
        this.this$0.resetDeformationValue();
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animation) {
        Intrinsics.checkNotNullParameter(animation, "animation");
        this.this$0.resetDeformationValue();
    }
}