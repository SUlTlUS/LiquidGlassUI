package com.coui.appcompat.seekbar.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"com/coui/appcompat/seekbar/animation/SectionAnimationHandler$snapToNearestSection$1$2", "Landroid/animation/AnimatorListenerAdapter;", "cancelled", "", "onAnimationCancel", "", "animation", "Landroid/animation/Animator;", "onAnimationEnd", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SectionAnimationHandler$snapToNearestSection$1$2 extends AnimatorListenerAdapter {
    final /* synthetic */ float $length;
    final /* synthetic */ float $targetRatio;
    private boolean cancelled;
    final /* synthetic */ SectionAnimationHandler this$0;

    public SectionAnimationHandler$snapToNearestSection$1$2(SectionAnimationHandler sectionAnimationHandler, float f3, float f4) {
        this.this$0 = sectionAnimationHandler;
        this.$targetRatio = f3;
        this.$length = f4;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animation) {
        Intrinsics.checkNotNullParameter(animation, "animation");
        this.cancelled = true;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animation) {
        Intrinsics.checkNotNullParameter(animation, "animation");
        if (this.cancelled) {
            return;
        }
        SectionAnimationHandler.access$getState$p(this.this$0).setOldProgress(SectionAnimationHandler.access$getState$p(this.this$0).getProgress());
        SectionAnimationHandler.access$getState$p(this.this$0).setScale(this.$targetRatio);
        SectionAnimationHandler.access$getState$p(this.this$0).setDrawProgressScale(this.$targetRatio);
        SectionAnimationHandler.access$getState$p(this.this$0).setSectionThumbX(this.$targetRatio * this.$length);
        SectionAnimationHandler.access$getCallback$p(this.this$0).requestInvalidate();
    }
}