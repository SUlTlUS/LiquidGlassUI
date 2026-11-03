package com.coui.appcompat.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Insets;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0012\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u0003H\u0002R\u0014\u0010\t\u001a\u00020\u00078BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lcom/coui/appcompat/searchview/CustomWindowInsetsAnimationControlListener;", "Landroid/view/WindowInsetsAnimationControlListener;", "mShow", "", "mDuration", "", "mInsetsInterpolator", "Landroid/view/animation/Interpolator;", "(ZILandroid/view/animation/Interpolator;)V", "alphaInterpolator", "getAlphaInterpolator", "()Landroid/view/animation/Interpolator;", "mAnimator", "Landroid/animation/Animator;", "onCancelled", "", "controller", "Landroid/view/WindowInsetsAnimationController;", "onFinished", "onReady", "types", "runTransition", "Landroid/animation/ValueAnimator;", "show", "Companion", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CustomWindowInsetsAnimationControlListener implements WindowInsetsAnimationControlListener {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f);
    private static final TypeEvaluator<Insets> INSETS_EVALUATOR = new r();
    private Animator mAnimator;
    private final int mDuration;
    private final Interpolator mInsetsInterpolator;
    private final boolean mShow;

    @Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001f\u0010\u0005\u001a\u0010\u0012\f\u0012\n \b*\u0004\u0018\u00010\u00070\u00070\u0006¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"Lcom/coui/appcompat/searchview/CustomWindowInsetsAnimationControlListener$Companion;", "", "()V", "FAST_OUT_LINEAR_IN_INTERPOLATOR", "Landroid/view/animation/Interpolator;", "INSETS_EVALUATOR", "Landroid/animation/TypeEvaluator;", "Landroid/graphics/Insets;", "kotlin.jvm.PlatformType", "getINSETS_EVALUATOR", "()Landroid/animation/TypeEvaluator;", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final TypeEvaluator<Insets> getINSETS_EVALUATOR() {
            return CustomWindowInsetsAnimationControlListener.access$getINSETS_EVALUATOR$cp();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.CustomWindowInsetsAnimationControlListener$runTransition$2, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"com/coui/appcompat/searchview/CustomWindowInsetsAnimationControlListener$runTransition$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass2 extends AnimatorListenerAdapter {
        final /* synthetic */ WindowInsetsAnimationController $controller;
        final /* synthetic */ boolean $show;

        public AnonymousClass2(WindowInsetsAnimationController windowInsetsAnimationController, boolean z2) {
            this.$controller = windowInsetsAnimationController;
            this.$show = z2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            Intrinsics.checkNotNullParameter(animation, "animation");
            if (this.$controller.isCancelled()) {
                return;
            }
            this.$controller.finish(this.$show);
        }
    }

    public CustomWindowInsetsAnimationControlListener(boolean z2, int i3, Interpolator mInsetsInterpolator) {
        Intrinsics.checkNotNullParameter(mInsetsInterpolator, "mInsetsInterpolator");
        this.mShow = z2;
        this.mDuration = i3;
        this.mInsetsInterpolator = mInsetsInterpolator;
    }

    private static final Insets INSETS_EVALUATOR$lambda$2(float f3, Insets startValue, Insets endValue) {
        Intrinsics.checkNotNullParameter(startValue, "startValue");
        Intrinsics.checkNotNullParameter(endValue, "endValue");
        int i3 = startValue.left;
        int i4 = (int) (i3 + ((endValue.left - i3) * f3));
        int i5 = startValue.top;
        int i6 = (int) (i5 + ((endValue.top - i5) * f3));
        int i7 = startValue.right;
        int i8 = (int) (i7 + ((endValue.right - i7) * f3));
        int i9 = startValue.bottom;
        return Insets.of(i4, i6, i8, (int) (i9 + (f3 * (endValue.bottom - i9))));
    }

    private static final float _get_alphaInterpolator_$lambda$1(float f3) {
        return Math.min(1.0f, 2 * f3);
    }

    public static /* synthetic */ float a(float f3) {
        return _get_alphaInterpolator_$lambda$1(f3);
    }

    public static final /* synthetic */ TypeEvaluator access$getINSETS_EVALUATOR$cp() {
        return INSETS_EVALUATOR;
    }

    public static /* synthetic */ void b(WindowInsetsAnimationController windowInsetsAnimationController, ValueAnimator valueAnimator, CustomWindowInsetsAnimationControlListener customWindowInsetsAnimationControlListener, Interpolator interpolator, Insets insets, Insets insets2, Interpolator interpolator2, ValueAnimator valueAnimator2) {
        runTransition$lambda$0(windowInsetsAnimationController, valueAnimator, customWindowInsetsAnimationControlListener, interpolator, insets, insets2, interpolator2, valueAnimator2);
    }

    public static /* synthetic */ Insets c(float f3, Insets insets, Insets insets2) {
        return INSETS_EVALUATOR$lambda$2(f3, insets, insets2);
    }

    private final Interpolator getAlphaInterpolator() {
        return this.mShow ? new s() : FAST_OUT_LINEAR_IN_INTERPOLATOR;
    }

    private final ValueAnimator runTransition(WindowInsetsAnimationController controller, boolean show) {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimatorOfFloat.setDuration(this.mDuration);
        valueAnimatorOfFloat.setInterpolator(new LinearInterpolator());
        Interpolator interpolator = this.mInsetsInterpolator;
        Interpolator alphaInterpolator = getAlphaInterpolator();
        Insets hiddenStateInsets = show ? controller.getHiddenStateInsets() : controller.getShownStateInsets();
        Intrinsics.checkNotNull(hiddenStateInsets);
        Insets shownStateInsets = show ? controller.getShownStateInsets() : controller.getHiddenStateInsets();
        Intrinsics.checkNotNull(shownStateInsets);
        valueAnimatorOfFloat.addUpdateListener(new q(controller, valueAnimatorOfFloat, this, interpolator, hiddenStateInsets, shownStateInsets, alphaInterpolator));
        valueAnimatorOfFloat.addListener(new AnonymousClass2(controller, show));
        valueAnimatorOfFloat.start();
        Intrinsics.checkNotNull(valueAnimatorOfFloat);
        return valueAnimatorOfFloat;
    }

    private static final void runTransition$lambda$0(WindowInsetsAnimationController controller, ValueAnimator valueAnimator, CustomWindowInsetsAnimationControlListener this$0, Interpolator insetsInterpolator, Insets start, Insets end, Interpolator alphaInterpolator, ValueAnimator animation) {
        Intrinsics.checkNotNullParameter(controller, "$controller");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(insetsInterpolator, "$insetsInterpolator");
        Intrinsics.checkNotNullParameter(start, "$start");
        Intrinsics.checkNotNullParameter(end, "$end");
        Intrinsics.checkNotNullParameter(alphaInterpolator, "$alphaInterpolator");
        Intrinsics.checkNotNullParameter(animation, "animation");
        if (!controller.isReady()) {
            valueAnimator.cancel();
            return;
        }
        float animatedFraction = animation.getAnimatedFraction();
        controller.setInsetsAndAlpha(INSETS_EVALUATOR.evaluate(insetsInterpolator.getInterpolation(animatedFraction), start, end), alphaInterpolator.getInterpolation(this$0.mShow ? animatedFraction : 1 - animatedFraction), animatedFraction);
    }

    @Override // android.view.WindowInsetsAnimationControlListener
    public void onCancelled(WindowInsetsAnimationController controller) {
        Animator animator = this.mAnimator;
        if (animator != null) {
            Intrinsics.checkNotNull(animator);
            animator.cancel();
        }
    }

    @Override // android.view.WindowInsetsAnimationControlListener
    public void onFinished(WindowInsetsAnimationController controller) {
        Intrinsics.checkNotNullParameter(controller, "controller");
    }

    @Override // android.view.WindowInsetsAnimationControlListener
    public void onReady(WindowInsetsAnimationController controller, int types) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        this.mAnimator = runTransition(controller, this.mShow);
    }
}