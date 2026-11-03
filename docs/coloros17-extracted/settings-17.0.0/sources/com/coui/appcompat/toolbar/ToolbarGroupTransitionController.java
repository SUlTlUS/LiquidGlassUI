package com.coui.appcompat.toolbar;

import android.view.View;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.iola.SpringData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes7.dex */
public class ToolbarGroupTransitionController extends BaseTransitionController {
    private Runnable mAlphaChannelDone;
    private COUISpringAnimation mAlphaEnterAnim;
    private COUISpringAnimation mAlphaExitAnim;
    private Runnable mBlurChannelDone;
    private COUISpringAnimation mBlurEnterAnim;
    private COUISpringAnimation mBlurExitAnim;
    private View mChannelPrimary;
    private ToolbarGroupTransitionParams mParams;
    private Runnable mScaleChannelDone;
    private COUISpringAnimation mScaleEnterAnim;
    private COUISpringAnimation mScaleExitAnim;
    private final FloatValueHolder mBlurEnterFloatHolder = new FloatValueHolder();
    private final FloatValueHolder mBlurExitFloatHolder = new FloatValueHolder();
    private final ArrayList<View> mChannelChildren = new ArrayList<>();

    public static final class TransitionState {
        float mEndAlpha;
        float mEndBlur;
        float mEndScale;
        float mStartAlpha;
        float mStartBlur;
        float mStartScale;

        private TransitionState() {
        }

        public /* synthetic */ TransitionState(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public static /* synthetic */ void a(ToolbarGroupTransitionController toolbarGroupTransitionController, COUISpringAnimation cOUISpringAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Runnable runnable;
        toolbarGroupTransitionController.mRunningAnimations.remove(cOUISpringAnimation);
        if (z2 || (runnable = toolbarGroupTransitionController.mAlphaChannelDone) == null) {
            return;
        }
        runnable.run();
    }

    private void applyInitialAlphaScale(List<View> list, float f3, float f4) {
        for (View view : list) {
            view.setAlpha(f3);
            view.setScaleX(f4);
            view.setScaleY(f4);
            this.mAnimatedViews.add(view);
        }
    }

    private void applyInitialBlur(List<View> list, float f3) {
        for (View view : list) {
            applyBlur(view, f3);
            this.mCurrentBlur.put(view, Float.valueOf(f3));
        }
    }

    public static /* synthetic */ void b(ToolbarGroupTransitionController toolbarGroupTransitionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        for (int i3 = 0; i3 < toolbarGroupTransitionController.mChannelChildren.size(); i3++) {
            View view = toolbarGroupTransitionController.mChannelChildren.get(i3);
            if (view != null) {
                toolbarGroupTransitionController.mCurrentBlur.put(view, Float.valueOf(f3));
                toolbarGroupTransitionController.applyBlur(view, f3);
            }
        }
    }

    public static /* synthetic */ void c(ToolbarGroupTransitionController toolbarGroupTransitionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        View view = toolbarGroupTransitionController.mChannelPrimary;
        for (int i3 = 0; i3 < toolbarGroupTransitionController.mChannelChildren.size(); i3++) {
            View view2 = toolbarGroupTransitionController.mChannelChildren.get(i3);
            if (view2 != null && view2 != view) {
                view2.setAlpha(f3);
            }
        }
    }

    private void cancelAllGroupSprings() {
        cancelAnimIfRunning(this.mAlphaEnterAnim);
        cancelAnimIfRunning(this.mAlphaExitAnim);
        cancelAnimIfRunning(this.mScaleEnterAnim);
        cancelAnimIfRunning(this.mScaleExitAnim);
        cancelAnimIfRunning(this.mBlurEnterAnim);
        cancelAnimIfRunning(this.mBlurExitAnim);
    }

    private static void cancelAnimIfRunning(COUISpringAnimation cOUISpringAnimation) {
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancel();
    }

    private TransitionState computeTransitionState(List<View> list, ToolbarGroupTransitionParams toolbarGroupTransitionParams, boolean z2, Set<View> set) {
        TransitionState transitionState = new TransitionState(null);
        transitionState.mEndAlpha = z2 ? 1.0f : 0.0f;
        transitionState.mEndScale = z2 ? 1.0f : toolbarGroupTransitionParams.getButtonExitScaleTo();
        transitionState.mEndBlur = z2 ? 0.0f : toolbarGroupTransitionParams.getButtonExitMaxBlurRadius();
        boolean z3 = false;
        View view = list.get(0);
        if (set.contains(view) && !isAtRestingState(view)) {
            z3 = true;
        }
        if (z2) {
            if (z3) {
                transitionState.mStartAlpha = view.getAlpha();
                transitionState.mStartScale = view.getScaleX();
            } else {
                transitionState.mStartAlpha = 0.0f;
                transitionState.mStartScale = toolbarGroupTransitionParams.getButtonEnterScaleFrom();
            }
        } else if (z3) {
            transitionState.mStartAlpha = view.getAlpha();
            transitionState.mStartScale = view.getScaleX();
        } else {
            transitionState.mStartAlpha = 1.0f;
            transitionState.mStartScale = 1.0f;
        }
        Float f3 = this.mCurrentBlur.get(view);
        if (z2) {
            transitionState.mStartBlur = f3 != null ? f3.floatValue() : toolbarGroupTransitionParams.getButtonEnterMaxBlurRadius();
            return transitionState;
        }
        if (z3) {
            transitionState.mStartBlur = f3 != null ? f3.floatValue() : 0.0f;
            return transitionState;
        }
        transitionState.mStartBlur = 0.0f;
        return transitionState;
    }

    private Runnable createCompletionRunnable(List<View> list, Runnable runnable, int i3) {
        return new q(this, new int[]{i3}, list, runnable);
    }

    public static /* synthetic */ void d(ToolbarGroupTransitionController toolbarGroupTransitionController, COUISpringAnimation cOUISpringAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Runnable runnable;
        toolbarGroupTransitionController.mRunningAnimations.remove(cOUISpringAnimation);
        if (z2 || (runnable = toolbarGroupTransitionController.mBlurChannelDone) == null) {
            return;
        }
        runnable.run();
    }

    public static /* synthetic */ void e(ToolbarGroupTransitionController toolbarGroupTransitionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        View view = toolbarGroupTransitionController.mChannelPrimary;
        for (int i3 = 0; i3 < toolbarGroupTransitionController.mChannelChildren.size(); i3++) {
            View view2 = toolbarGroupTransitionController.mChannelChildren.get(i3);
            if (view2 != null) {
                if (view2 == view) {
                    view2.setScaleY(f3);
                } else {
                    view2.setScaleX(f3);
                    view2.setScaleY(f3);
                }
            }
        }
    }

    private void ensureAlphaEnterAnim(View view) {
        if (this.mAlphaEnterAnim != null) {
            return;
        }
        SpringData buttonEnterAlphaSpring = params().getButtonEnterAlphaSpring();
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(view, COUIDynamicAnimation.ALPHA, 1.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce(1.0f);
        cOUISpringForce.setBounce(buttonEnterAlphaSpring.getBounce()).setResponse(buttonEnterAlphaSpring.getResponse());
        cOUISpringAnimation.setSpring(cOUISpringForce);
        cOUISpringAnimation.setMinimumVisibleChange(0.00390625f);
        cOUISpringAnimation.addUpdateListener(new u(this));
        cOUISpringAnimation.addEndListener(new v(this, cOUISpringAnimation));
        this.mAlphaEnterAnim = cOUISpringAnimation;
    }

    private void ensureAlphaExitAnim(View view) {
        if (this.mAlphaExitAnim != null) {
            return;
        }
        ToolbarGroupTransitionParams toolbarGroupTransitionParamsParams = params();
        SpringData buttonExitAlphaSpring = toolbarGroupTransitionParamsParams.getButtonExitAlphaSpring();
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(view, COUIDynamicAnimation.ALPHA, 1.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce(0.0f);
        cOUISpringForce.setBounce(buttonExitAlphaSpring.getBounce()).setResponse(buttonExitAlphaSpring.getResponse());
        cOUISpringAnimation.setSpring(cOUISpringForce);
        cOUISpringAnimation.setMinimumVisibleChange(toolbarGroupTransitionParamsParams.getButtonGroupExitAlphaMinimumVisibleChange());
        cOUISpringAnimation.addUpdateListener(new y(this));
        cOUISpringAnimation.addEndListener(new z(this, cOUISpringAnimation));
        this.mAlphaExitAnim = cOUISpringAnimation;
    }

    private void ensureBlurEnterAnim() {
        if (this.mBlurEnterAnim != null) {
            return;
        }
        SpringData buttonEnterBlurSpring = params().getButtonEnterBlurSpring();
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.mBlurEnterFloatHolder);
        COUISpringForce cOUISpringForce = new COUISpringForce(0.0f);
        cOUISpringForce.setBounce(buttonEnterBlurSpring.getBounce()).setResponse(buttonEnterBlurSpring.getResponse());
        cOUISpringAnimation.setSpring(cOUISpringForce);
        cOUISpringAnimation.setMinimumVisibleChange(1.0f);
        cOUISpringAnimation.addUpdateListener(new a0(this));
        cOUISpringAnimation.addEndListener(new b0(this, cOUISpringAnimation));
        this.mBlurEnterAnim = cOUISpringAnimation;
    }

    private void ensureBlurExitAnim() {
        if (this.mBlurExitAnim != null) {
            return;
        }
        ToolbarGroupTransitionParams toolbarGroupTransitionParamsParams = params();
        SpringData buttonExitBlurSpring = toolbarGroupTransitionParamsParams.getButtonExitBlurSpring();
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.mBlurExitFloatHolder);
        COUISpringForce cOUISpringForce = new COUISpringForce(0.0f);
        cOUISpringForce.setBounce(buttonExitBlurSpring.getBounce()).setResponse(buttonExitBlurSpring.getResponse());
        cOUISpringAnimation.setSpring(cOUISpringForce);
        cOUISpringAnimation.setMinimumVisibleChange(toolbarGroupTransitionParamsParams.getButtonGroupExitBlurMinimumVisibleChange());
        cOUISpringAnimation.addUpdateListener(new p(this));
        cOUISpringAnimation.addEndListener(new t(this, cOUISpringAnimation));
        this.mBlurExitAnim = cOUISpringAnimation;
    }

    private void ensurePrimaryForChannels(View view) {
        View view2 = this.mChannelPrimary;
        if (view2 != null && view2 != view) {
            cancelAllGroupSprings();
            resetAllAnim();
        }
        this.mChannelPrimary = view;
    }

    private void ensureScaleEnterAnim(View view) {
        if (this.mScaleEnterAnim != null) {
            return;
        }
        SpringData buttonEnterScaleSpring = params().getButtonEnterScaleSpring();
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(view, COUIDynamicAnimation.SCALE_X, 1.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce(1.0f);
        cOUISpringForce.setBounce(buttonEnterScaleSpring.getBounce()).setResponse(buttonEnterScaleSpring.getResponse());
        cOUISpringAnimation.setSpring(cOUISpringForce);
        cOUISpringAnimation.setMinimumVisibleChange(0.001f);
        cOUISpringAnimation.addUpdateListener(new w(this));
        cOUISpringAnimation.addEndListener(new x(this, cOUISpringAnimation));
        this.mScaleEnterAnim = cOUISpringAnimation;
    }

    private void ensureScaleExitAnim(View view) {
        if (this.mScaleExitAnim != null) {
            return;
        }
        ToolbarGroupTransitionParams toolbarGroupTransitionParamsParams = params();
        SpringData buttonExitScaleSpring = toolbarGroupTransitionParamsParams.getButtonExitScaleSpring();
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(view, COUIDynamicAnimation.SCALE_X, 1.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce(1.0f);
        cOUISpringForce.setBounce(buttonExitScaleSpring.getBounce()).setResponse(buttonExitScaleSpring.getResponse());
        cOUISpringAnimation.setSpring(cOUISpringForce);
        cOUISpringAnimation.setMinimumVisibleChange(toolbarGroupTransitionParamsParams.getButtonGroupExitScaleMinimumVisibleChange());
        cOUISpringAnimation.addUpdateListener(new r(this));
        cOUISpringAnimation.addEndListener(new s(this, cOUISpringAnimation));
        this.mScaleExitAnim = cOUISpringAnimation;
    }

    public static /* synthetic */ void f(ToolbarGroupTransitionController toolbarGroupTransitionController, int[] iArr, List list, Runnable runnable) {
        toolbarGroupTransitionController.getClass();
        int i3 = iArr[0] - 1;
        iArr[0] = i3;
        if (i3 <= 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                View view = (View) it.next();
                toolbarGroupTransitionController.mAnimatedViews.remove(view);
                toolbarGroupTransitionController.mCurrentBlur.remove(view);
                toolbarGroupTransitionController.mAppliedBlurRadius.remove(view);
            }
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public static /* synthetic */ void g(ToolbarGroupTransitionController toolbarGroupTransitionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        for (int i3 = 0; i3 < toolbarGroupTransitionController.mChannelChildren.size(); i3++) {
            View view = toolbarGroupTransitionController.mChannelChildren.get(i3);
            if (view != null) {
                toolbarGroupTransitionController.mCurrentBlur.put(view, Float.valueOf(f3));
                toolbarGroupTransitionController.applyBlur(view, f3);
            }
        }
    }

    public static /* synthetic */ void h(ToolbarGroupTransitionController toolbarGroupTransitionController, COUISpringAnimation cOUISpringAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Runnable runnable;
        toolbarGroupTransitionController.mRunningAnimations.remove(cOUISpringAnimation);
        if (z2 || (runnable = toolbarGroupTransitionController.mBlurChannelDone) == null) {
            return;
        }
        runnable.run();
    }

    public static /* synthetic */ void i(ToolbarGroupTransitionController toolbarGroupTransitionController, COUISpringAnimation cOUISpringAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Runnable runnable;
        toolbarGroupTransitionController.mRunningAnimations.remove(cOUISpringAnimation);
        if (z2 || (runnable = toolbarGroupTransitionController.mScaleChannelDone) == null) {
            return;
        }
        runnable.run();
    }

    public static /* synthetic */ void j(ToolbarGroupTransitionController toolbarGroupTransitionController, COUISpringAnimation cOUISpringAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Runnable runnable;
        toolbarGroupTransitionController.mRunningAnimations.remove(cOUISpringAnimation);
        if (z2 || (runnable = toolbarGroupTransitionController.mScaleChannelDone) == null) {
            return;
        }
        runnable.run();
    }

    public static /* synthetic */ void k(ToolbarGroupTransitionController toolbarGroupTransitionController, COUISpringAnimation cOUISpringAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Runnable runnable;
        toolbarGroupTransitionController.mRunningAnimations.remove(cOUISpringAnimation);
        if (z2 || (runnable = toolbarGroupTransitionController.mAlphaChannelDone) == null) {
            return;
        }
        runnable.run();
    }

    public static /* synthetic */ void l(ToolbarGroupTransitionController toolbarGroupTransitionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        View view = toolbarGroupTransitionController.mChannelPrimary;
        for (int i3 = 0; i3 < toolbarGroupTransitionController.mChannelChildren.size(); i3++) {
            View view2 = toolbarGroupTransitionController.mChannelChildren.get(i3);
            if (view2 != null) {
                if (view2 == view) {
                    view2.setScaleY(f3);
                } else {
                    view2.setScaleX(f3);
                    view2.setScaleY(f3);
                }
            }
        }
    }

    public static /* synthetic */ void m(ToolbarGroupTransitionController toolbarGroupTransitionController, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        View view = toolbarGroupTransitionController.mChannelPrimary;
        for (int i3 = 0; i3 < toolbarGroupTransitionController.mChannelChildren.size(); i3++) {
            View view2 = toolbarGroupTransitionController.mChannelChildren.get(i3);
            if (view2 != null && view2 != view) {
                view2.setAlpha(f3);
            }
        }
    }

    private ToolbarGroupTransitionParams params() {
        if (this.mParams == null) {
            this.mParams = ToolbarGroupTransitionParamsExtKt.get(ToolbarGroupTransitionParams.INSTANCE);
        }
        return this.mParams;
    }

    private void resetAllAnim() {
        this.mAlphaEnterAnim = null;
        this.mAlphaExitAnim = null;
        this.mScaleEnterAnim = null;
        this.mScaleExitAnim = null;
        this.mBlurEnterAnim = null;
        this.mBlurExitAnim = null;
    }

    private void resetStaleAnimatedViews(Set<View> set, Set<View> set2) {
        for (View view : set) {
            if (view != null && !set2.contains(view)) {
                resetView(view);
                this.mCurrentBlur.remove(view);
                this.mAppliedBlurRadius.remove(view);
            }
        }
    }

    private boolean runIfEmptyAndComplete(List<View> list, Runnable runnable) {
        if (!list.isEmpty()) {
            return false;
        }
        this.mCurrentBlur.clear();
        this.mAppliedBlurRadius.clear();
        if (runnable == null) {
            return true;
        }
        runnable.run();
        return true;
    }

    private void startAlphaSpring(List<View> list, boolean z2, float f3, Runnable runnable) {
        View view = list.get(0);
        if (z2) {
            ensureAlphaEnterAnim(view);
        } else {
            ensureAlphaExitAnim(view);
        }
        COUISpringAnimation cOUISpringAnimation = z2 ? this.mAlphaEnterAnim : this.mAlphaExitAnim;
        this.mAlphaChannelDone = runnable;
        if (!this.mRunningAnimations.contains(cOUISpringAnimation)) {
            this.mRunningAnimations.add(cOUISpringAnimation);
        }
        cOUISpringAnimation.animateToFinalPosition(f3);
    }

    private void startBlurSpring(List<View> list, boolean z2, float f3, float f4, Runnable runnable) {
        if (z2) {
            ensureBlurEnterAnim();
        } else {
            ensureBlurExitAnim();
        }
        COUISpringAnimation cOUISpringAnimation = z2 ? this.mBlurEnterAnim : this.mBlurExitAnim;
        FloatValueHolder floatValueHolder = z2 ? this.mBlurEnterFloatHolder : this.mBlurExitFloatHolder;
        this.mBlurChannelDone = runnable;
        if (!cOUISpringAnimation.isRunning()) {
            floatValueHolder.setValue(f3);
        }
        if (!this.mRunningAnimations.contains(cOUISpringAnimation)) {
            this.mRunningAnimations.add(cOUISpringAnimation);
        }
        cOUISpringAnimation.animateToFinalPosition(f4);
    }

    private void startScaleSpring(List<View> list, boolean z2, float f3, Runnable runnable) {
        View view = list.get(0);
        if (z2) {
            ensureScaleEnterAnim(view);
        } else {
            ensureScaleExitAnim(view);
        }
        COUISpringAnimation cOUISpringAnimation = z2 ? this.mScaleEnterAnim : this.mScaleExitAnim;
        this.mScaleChannelDone = runnable;
        if (!this.mRunningAnimations.contains(cOUISpringAnimation)) {
            this.mRunningAnimations.add(cOUISpringAnimation);
        }
        cOUISpringAnimation.animateToFinalPosition(f3);
    }

    @Override // com.coui.appcompat.toolbar.BaseTransitionController
    public boolean isAtRestingState(View view) {
        return view.getAlpha() >= 0.99f && view.getScaleX() >= 0.99f && view.getScaleY() >= 0.99f;
    }

    public void release() {
        cancelAll();
        this.mAnimatedViews.clear();
        this.mCurrentBlur.clear();
        this.mAppliedBlurRadius.clear();
        this.mChannelPrimary = null;
        this.mChannelChildren.clear();
        resetAllAnim();
        this.mAlphaChannelDone = null;
        this.mScaleChannelDone = null;
        this.mBlurChannelDone = null;
        this.mParams = null;
    }

    @Override // com.coui.appcompat.toolbar.BaseTransitionController
    public void resetView(View view) {
        view.setAlpha(1.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        BaseTransitionController.clearBlurEffect(view);
    }

    public void startTransition(List<View> list, boolean z2, Runnable runnable) {
        HashSet hashSet = new HashSet(this.mAnimatedViews);
        this.mAnimatedViews.clear();
        resetStaleAnimatedViews(hashSet, new HashSet(list));
        if (runIfEmptyAndComplete(list, runnable)) {
            return;
        }
        ToolbarGroupTransitionParams toolbarGroupTransitionParamsParams = params();
        ensurePrimaryForChannels(list.get(0));
        cancelAllGroupSprings();
        TransitionState transitionStateComputeTransitionState = computeTransitionState(list, toolbarGroupTransitionParamsParams, z2, hashSet);
        applyInitialAlphaScale(list, transitionStateComputeTransitionState.mStartAlpha, transitionStateComputeTransitionState.mStartScale);
        applyInitialBlur(list, transitionStateComputeTransitionState.mStartBlur);
        this.mChannelChildren.clear();
        this.mChannelChildren.addAll(list);
        Runnable runnableCreateCompletionRunnable = createCompletionRunnable(list, runnable, this.mBlurEnabled ? 3 : 2);
        startAlphaSpring(list, z2, transitionStateComputeTransitionState.mEndAlpha, runnableCreateCompletionRunnable);
        startScaleSpring(list, z2, transitionStateComputeTransitionState.mEndScale, runnableCreateCompletionRunnable);
        if (this.mBlurEnabled) {
            startBlurSpring(list, z2, transitionStateComputeTransitionState.mStartBlur, transitionStateComputeTransitionState.mEndBlur, runnableCreateCompletionRunnable);
        }
    }
}