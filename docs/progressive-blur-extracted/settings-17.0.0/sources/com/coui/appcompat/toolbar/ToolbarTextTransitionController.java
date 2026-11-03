package com.coui.appcompat.toolbar;

import android.view.View;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.iola.SpringData;
import java.util.HashSet;

/* JADX INFO: loaded from: classes7.dex */
public class ToolbarTextTransitionController extends BaseTransitionController {
    private COUISpringAnimation mEnterTextAlphaAnim;
    private Runnable mEnterTextAlphaChannelDone;
    private View mEnterTextAlphaDriver;
    private COUISpringAnimation mExitTextAlphaAnim;
    private Runnable mExitTextAlphaChannelDone;
    private View mExitTextAlphaDriver;
    private ToolbarGroupTransitionParams mParams;

    public static /* synthetic */ void a(int[] iArr, Runnable runnable) {
        int i3 = iArr[0] - 1;
        iArr[0] = i3;
        if (i3 > 0 || runnable == null) {
            return;
        }
        runnable.run();
    }

    private void animateFade(View view, boolean z2, boolean z3, Runnable runnable) {
        this.mAnimatedViews.add(view);
        float f3 = z2 ? 1.0f : 0.0f;
        view.setVisibility(0);
        if (!z3) {
            view.setAlpha(z2 ? 0.0f : 1.0f);
        }
        i0 i0Var = new i0(this, view, f3, z2, runnable);
        COUISpringAnimation cOUISpringAnimationObtainTextAlphaSpring = obtainTextAlphaSpring(view, z2);
        if (z2) {
            this.mEnterTextAlphaChannelDone = i0Var;
        } else {
            this.mExitTextAlphaChannelDone = i0Var;
        }
        if (!this.mRunningAnimations.contains(cOUISpringAnimationObtainTextAlphaSpring)) {
            this.mRunningAnimations.add(cOUISpringAnimationObtainTextAlphaSpring);
        }
        cOUISpringAnimationObtainTextAlphaSpring.animateToFinalPosition(f3);
    }

    public static /* synthetic */ void b(ToolbarTextTransitionController toolbarTextTransitionController, View view, float f3, boolean z2, Runnable runnable) {
        toolbarTextTransitionController.getClass();
        view.setAlpha(f3);
        if (!z2) {
            view.setVisibility(8);
        }
        toolbarTextTransitionController.mAnimatedViews.remove(view);
        if (runnable != null) {
            runnable.run();
        }
    }

    public static /* synthetic */ void c(ToolbarTextTransitionController toolbarTextTransitionController, COUISpringAnimation cOUISpringAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Runnable runnable;
        toolbarTextTransitionController.mRunningAnimations.remove(cOUISpringAnimation);
        if (z2 || (runnable = toolbarTextTransitionController.mExitTextAlphaChannelDone) == null) {
            return;
        }
        runnable.run();
    }

    private void configureTextAlphaSpring(COUISpringAnimation cOUISpringAnimation, boolean z2) {
        ToolbarGroupTransitionParams toolbarGroupTransitionParamsParams = params();
        SpringData textEnterAlphaSpring = z2 ? toolbarGroupTransitionParamsParams.getTextEnterAlphaSpring() : toolbarGroupTransitionParamsParams.getTextExitAlphaSpring();
        COUISpringForce cOUISpringForce = new COUISpringForce(z2 ? 1.0f : 0.0f);
        cOUISpringForce.setBounce(textEnterAlphaSpring.getBounce()).setResponse(textEnterAlphaSpring.getResponse());
        cOUISpringAnimation.setSpring(cOUISpringForce);
        cOUISpringAnimation.setMinimumVisibleChange(z2 ? 0.00390625f : toolbarGroupTransitionParamsParams.getTextExitAlphaMinimumVisibleChange());
    }

    public static /* synthetic */ void d(ToolbarTextTransitionController toolbarTextTransitionController, COUISpringAnimation cOUISpringAnimation, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Runnable runnable;
        toolbarTextTransitionController.mRunningAnimations.remove(cOUISpringAnimation);
        if (z2 || (runnable = toolbarTextTransitionController.mEnterTextAlphaChannelDone) == null) {
            return;
        }
        runnable.run();
    }

    private COUISpringAnimation obtainTextAlphaSpring(View view, boolean z2) {
        COUISpringAnimation cOUISpringAnimation = z2 ? this.mEnterTextAlphaAnim : this.mExitTextAlphaAnim;
        View view2 = z2 ? this.mEnterTextAlphaDriver : this.mExitTextAlphaDriver;
        if (cOUISpringAnimation != null && view2 == view) {
            return cOUISpringAnimation;
        }
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = new COUISpringAnimation(view, COUIDynamicAnimation.ALPHA, 1.0f);
        configureTextAlphaSpring(cOUISpringAnimation2, z2);
        if (z2) {
            this.mEnterTextAlphaAnim = cOUISpringAnimation2;
            this.mEnterTextAlphaDriver = view;
            cOUISpringAnimation2.addEndListener(new g0(this, cOUISpringAnimation2));
            return cOUISpringAnimation2;
        }
        this.mExitTextAlphaAnim = cOUISpringAnimation2;
        this.mExitTextAlphaDriver = view;
        cOUISpringAnimation2.addEndListener(new h0(this, cOUISpringAnimation2));
        return cOUISpringAnimation2;
    }

    private ToolbarGroupTransitionParams params() {
        if (this.mParams == null) {
            this.mParams = ToolbarGroupTransitionParamsExtKt.get(ToolbarGroupTransitionParams.INSTANCE);
        }
        return this.mParams;
    }

    @Override // com.coui.appcompat.toolbar.BaseTransitionController
    public boolean isAtRestingState(View view) {
        return view.getAlpha() >= 0.99f;
    }

    public void release() {
        cancelAll();
        this.mAnimatedViews.clear();
        this.mExitTextAlphaAnim = null;
        this.mEnterTextAlphaAnim = null;
        this.mExitTextAlphaDriver = null;
        this.mEnterTextAlphaDriver = null;
        this.mExitTextAlphaChannelDone = null;
        this.mEnterTextAlphaChannelDone = null;
        this.mParams = null;
    }

    @Override // com.coui.appcompat.toolbar.BaseTransitionController
    public void resetView(View view) {
        view.setAlpha(1.0f);
    }

    public void startCrossFade(View view, View view2, Runnable runnable) {
        HashSet<View> hashSet = new HashSet(this.mAnimatedViews);
        this.mAnimatedViews.clear();
        HashSet hashSet2 = new HashSet();
        if (view != null) {
            hashSet2.add(view);
        }
        if (view2 != null) {
            hashSet2.add(view2);
        }
        for (View view3 : hashSet) {
            if (view3 != null && !hashSet2.contains(view3)) {
                view3.setAlpha(1.0f);
            }
        }
        boolean z2 = false;
        int i3 = (view != null ? 1 : 0) + (view2 != null ? 1 : 0);
        if (i3 == 0) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        f0 f0Var = new f0(new int[]{i3}, runnable);
        if (view != null) {
            animateFade(view, false, hashSet.contains(view) && !isAtRestingState(view), f0Var);
        }
        if (view2 != null) {
            if (hashSet.contains(view2) && !isAtRestingState(view2)) {
                z2 = true;
            }
            animateFade(view2, true, z2, f0Var);
        }
    }
}