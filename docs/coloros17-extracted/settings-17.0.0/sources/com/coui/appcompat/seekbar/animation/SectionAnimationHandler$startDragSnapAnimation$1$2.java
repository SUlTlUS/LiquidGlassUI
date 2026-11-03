package com.coui.appcompat.seekbar.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"com/coui/appcompat/seekbar/animation/SectionAnimationHandler$startDragSnapAnimation$1$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SectionAnimationHandler$startDragSnapAnimation$1$2 extends AnimatorListenerAdapter {
    final /* synthetic */ float $length;
    final /* synthetic */ SectionAnimationHandler this$0;

    public SectionAnimationHandler$startDragSnapAnimation$1$2(SectionAnimationHandler sectionAnimationHandler, float f3) {
        this.this$0 = sectionAnimationHandler;
        this.$length = f3;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animation) {
        Intrinsics.checkNotNullParameter(animation, "animation");
        if (this.this$0.getIsFastMoving()) {
            this.this$0.setFastMoving(false);
            SectionAnimationHandler.access$handleFastMoveSnap(this.this$0, this.$length);
        }
    }
}