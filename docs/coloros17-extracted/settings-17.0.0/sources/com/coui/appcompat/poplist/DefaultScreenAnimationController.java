package com.coui.appcompat.poplist;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.coui.appcompat.anim.ability.COUIPopupTouchMotionAnimator;
import com.coui.appcompat.anim.ability.MenuTransitionUnit;
import com.coui.appcompat.anim.ability.ParentInfluenceUnit;
import com.coui.appcompat.anim.ability.SubMenuTransitionUnit;
import com.coui.appcompat.animation.rt.RtViewWrites;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.OnAnimationEndListener;
import com.coui.appcompat.log.COUILog;

/* JADX INFO: loaded from: classes7.dex */
class DefaultScreenAnimationController extends BasePopupMenuAnimationController {
    private static final boolean COUI_DEBUG;
    private static final float DEFAULT_ALPHA = 1.0f;
    private static final float DEFAULT_MAIN_MENU_ALPHA_PERCENT = 0.7f;
    private static final float DEFAULT_MAIN_MENU_SCALE_PERCENT = 0.9f;
    private static final float DEFAULT_SCALE = 1.0f;
    private static final float MIN_ALPHA = 0.1f;
    private static final float PIVOT_CENTER_RATIO = 0.5f;
    private static final String TAG = "PopupMenuAnimCtrl-D";
    private boolean mSubMenuManuallyHidden = false;
    private boolean mSubMenuPivotCompensationEnabled = false;
    private float mSubMenuPivotDeltaX = 0.0f;
    private float mSubMenuPivotDeltaY = 0.0f;
    private float mMainMenuScalePercent = 0.9f;
    private float mMainMenuAlphaPercent = 0.7f;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.DefaultScreenAnimationController$1, reason: invalid class name */
    public class AnonymousClass1 implements OnAnimationEndListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
        public void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
            if (DefaultScreenAnimationController.f()) {
                Log.d(DefaultScreenAnimationController.TAG, "onAnimationEnd enter popupKit: canceled=" + z2);
            }
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = DefaultScreenAnimationController.this.mMenuStateChangedListener;
            if (onMenuStateChangedListener != null) {
                if (z2) {
                    onMenuStateChangedListener.onMainMenuAnimationCanceled();
                } else {
                    onMenuStateChangedListener.onMainMenuEntered();
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.DefaultScreenAnimationController$2, reason: invalid class name */
    public class AnonymousClass2 implements OnAnimationEndListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
        public void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
            if (DefaultScreenAnimationController.f()) {
                Log.d(DefaultScreenAnimationController.TAG, "onAnimationEnd exit popupKit: canceled=" + z2);
            }
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = DefaultScreenAnimationController.this.mMenuStateChangedListener;
            if (onMenuStateChangedListener != null) {
                if (z2) {
                    onMenuStateChangedListener.onMainMenuAnimationCanceled();
                } else {
                    onMenuStateChangedListener.onMainMenuExited();
                }
            }
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public static /* synthetic */ void c(DefaultScreenAnimationController defaultScreenAnimationController, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        View view = defaultScreenAnimationController.mSubMenuRoot;
        if (view == null || !view.isAttachedToWindow()) {
            return;
        }
        boolean zIsUseRt = defaultScreenAnimationController.mSubMenuTouchAnimator.isUseRt();
        float alpha = contributeSink.getAlpha();
        if (defaultScreenAnimationController.mSubMenuManuallyHidden) {
            return;
        }
        defaultScreenAnimationController.applySubMenuVisibilityForBlurAlpha(alpha, 0.1f);
        float translateX = contributeSink.getTranslateX();
        float translateY = contributeSink.getTranslateY();
        float scaleX = contributeSink.getScaleX();
        float scaleY = contributeSink.getScaleY();
        if (defaultScreenAnimationController.mSubMenuPivotCompensationEnabled) {
            translateX += defaultScreenAnimationController.mSubMenuPivotDeltaX * (1.0f - defaultScreenAnimationController.mLastGlobalScaleX);
            translateY += defaultScreenAnimationController.mSubMenuPivotDeltaY * (1.0f - defaultScreenAnimationController.mLastGlobalScaleY);
        }
        RtViewWrites.setTranslationXY(defaultScreenAnimationController.mSubMenuRoot, translateX, translateY, zIsUseRt);
        BasePopupMenuAnimationController.setPopupMenuScale(defaultScreenAnimationController.mSubMenuRoot, scaleX, scaleY, zIsUseRt);
        BasePopupMenuAnimationController.setPopupMenuAlpha(defaultScreenAnimationController.mSubMenuRoot, alpha, zIsUseRt);
    }

    private void configMainMenuAnimationPropertiesForSubMenuEnter() {
        this.mMainMenuScalePercent = 0.9f;
        this.mMainMenuAlphaPercent = 0.7f;
        View view = this.mMainMenuRoot;
        view.setPivotX(view.getWidth() * 0.5f);
        View view2 = this.mMainMenuRoot;
        view2.setPivotY(view2.getHeight() * 0.5f);
    }

    public static /* synthetic */ void d(DefaultScreenAnimationController defaultScreenAnimationController, COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
        if (z2) {
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = defaultScreenAnimationController.mMenuStateChangedListener;
            if (onMenuStateChangedListener != null) {
                onMenuStateChangedListener.onSubMenuAnimationCanceled();
                return;
            }
            return;
        }
        if (f3 != 0.0f) {
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener2 = defaultScreenAnimationController.mMenuStateChangedListener;
            if (onMenuStateChangedListener2 != null) {
                onMenuStateChangedListener2.onSubMenuEntered();
                return;
            }
            return;
        }
        defaultScreenAnimationController.resetMainMenuPivotToEnter();
        BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener3 = defaultScreenAnimationController.mMenuStateChangedListener;
        if (onMenuStateChangedListener3 != null) {
            onMenuStateChangedListener3.onSubMenuExited();
        }
    }

    public static /* synthetic */ void e(DefaultScreenAnimationController defaultScreenAnimationController, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        View view;
        View childAt;
        View view2 = defaultScreenAnimationController.mMainMenuRoot;
        if (view2 == null || !view2.isAttachedToWindow()) {
            return;
        }
        boolean zIsUseRt = defaultScreenAnimationController.mMainMenuTouchAnimator.isUseRt();
        float translateX = contributeSink.getTranslateX();
        float translateY = contributeSink.getTranslateY();
        float scaleX = contributeSink.getScaleX();
        float scaleY = contributeSink.getScaleY();
        float alpha = contributeSink.getAlpha();
        defaultScreenAnimationController.mLastGlobalTranslateX = translateX;
        defaultScreenAnimationController.mLastGlobalTranslateY = translateY;
        defaultScreenAnimationController.mLastGlobalScaleX = scaleX;
        defaultScreenAnimationController.mLastGlobalScaleY = scaleY;
        defaultScreenAnimationController.mRootViewAlpha = alpha;
        MenuTransitionUnit menuTransitionUnit = defaultScreenAnimationController.mMenuTransitionUnit;
        float currentScaleX = menuTransitionUnit != null ? menuTransitionUnit.getCurrentScaleX() : 1.0f;
        MenuTransitionUnit menuTransitionUnit2 = defaultScreenAnimationController.mMenuTransitionUnit;
        BasePopupMenuAnimationController.applyMainMenuScaleFromCollector(defaultScreenAnimationController.mMainMenuTouchAnimator, defaultScreenAnimationController.mMainMenuRoot, scaleX, scaleY, currentScaleX, menuTransitionUnit2 != null ? menuTransitionUnit2.getCurrentScaleY() : 1.0f, zIsUseRt);
        MenuTransitionUnit menuTransitionUnit3 = defaultScreenAnimationController.mMenuTransitionUnit;
        BasePopupMenuAnimationController.applyMainMenuTranslationFromCollector(defaultScreenAnimationController.mMainMenuTouchAnimator, defaultScreenAnimationController.mMainMenuRoot, translateX, translateY, menuTransitionUnit3 != null ? menuTransitionUnit3.getCurrentTranslationY() : 0.0f, zIsUseRt);
        View view3 = defaultScreenAnimationController.mMainMenuRoot;
        if ((view3 instanceof ViewGroup) && (childAt = ((ViewGroup) view3).getChildAt(0)) != null) {
            MenuTransitionUnit menuTransitionUnit4 = defaultScreenAnimationController.mMenuTransitionUnit;
            BasePopupMenuAnimationController.setPopupMenuAlpha(childAt, menuTransitionUnit4 != null ? menuTransitionUnit4.getCurrentContentAlpha() : 1.0f, zIsUseRt);
        }
        if (defaultScreenAnimationController.mParentInfluenceUnit != null && (view = defaultScreenAnimationController.mSubMenuRoot) != null && view.getVisibility() == 0) {
            defaultScreenAnimationController.mParentInfluenceUnit.updateFromParent(BasePopupMenuAnimationController.effectiveParentTranslateX(defaultScreenAnimationController.mMainMenuTouchAnimator, translateX), BasePopupMenuAnimationController.effectiveParentTranslateY(defaultScreenAnimationController.mMainMenuTouchAnimator, translateY), BasePopupMenuAnimationController.effectiveParentScaleX(defaultScreenAnimationController.mMainMenuTouchAnimator, scaleX), BasePopupMenuAnimationController.effectiveParentScaleY(defaultScreenAnimationController.mMainMenuTouchAnimator, scaleY), alpha);
        }
        BasePopupMenuAnimationController.applyPopupAlphaFromCollector(defaultScreenAnimationController.mMainMenuTouchAnimator, defaultScreenAnimationController.mRootView, defaultScreenAnimationController.mMainMenuRoot, alpha, zIsUseRt);
    }

    private void ensureAbility() {
        int i3;
        if (this.mMainMenuTouchAnimator != null) {
            int iWidth = this.mDomain.mMainMenu.width();
            int iHeight = this.mDomain.mMainMenu.height();
            boolean z2 = !this.mDomain.isMainMenuBelowAnchor();
            boolean z3 = this.mDomain.getMainMenuHorizontalAlignment() == 1;
            boolean z4 = this.mDomain.getMainMenuHorizontalAlignment() == 0;
            if (this.mMainMenuRoot != null) {
                int i4 = this.mOverrideMainMenuTargetWidth;
                if (i4 > 0 && (i3 = this.mOverrideMainMenuTargetHeight) > 0) {
                    iWidth = i4;
                    iHeight = i3;
                }
                if (COUI_DEBUG) {
                    Log.d(TAG, "ensureAbility: View layout - left=" + this.mMainMenuRoot.getLeft() + ", top=" + this.mMainMenuRoot.getTop() + ", translationY=" + this.mMainMenuRoot.getTranslationY() + ", visibility=" + this.mMainMenuRoot.getVisibility() + ", menuWidthPx=" + iWidth + ", menuHeightPx=" + iHeight + ", isAboveAnchor=" + z2);
                }
                this.mMainMenuTouchAnimator.setTargetSize(iWidth, iHeight);
                this.mMainMenuTouchAnimator.setIsAboveAnchor(z2);
                this.mMainMenuTouchAnimator.setIsCenter(z3);
                this.mMainMenuTouchAnimator.setIsLeft(z4);
                if (this.mMenuTransitionUnit == null) {
                    MenuTransitionUnit menuTransitionUnit = new MenuTransitionUnit();
                    this.mMenuTransitionUnit = menuTransitionUnit;
                    this.mMainMenuTouchAnimator.addMotionUnit(menuTransitionUnit);
                }
                if (this.mMainMenuUpdateCallback == null) {
                    z zVar = new z(this);
                    this.mMainMenuUpdateCallback = zVar;
                    this.mMainMenuTouchAnimator.addOnAnimationUpdateCallback(zVar);
                }
                configureMainPopupMotionGeometry();
            }
        }
    }

    public static /* bridge */ /* synthetic */ boolean f() {
        return COUI_DEBUG;
    }

    private void resetMainMenuPivotToEnter() {
        syncMainMenuEnterPivotFromDomain();
        syncPivotToAnimator();
    }

    private void syncPivotToAnimator() {
        View view;
        PopupMenuDomain popupMenuDomain;
        if (this.mMainMenuTouchAnimator == null || (view = this.mMainMenuRoot) == null) {
            return;
        }
        float width = view.getWidth();
        float height = this.mMainMenuRoot.getHeight();
        if (width <= 0.0f && (popupMenuDomain = this.mDomain) != null && !popupMenuDomain.mMainMenu.isEmpty()) {
            width = this.mDomain.mMainMenu.width();
            height = this.mDomain.mMainMenu.height();
        }
        if (width > 0.0f) {
            this.mMainMenuTouchAnimator.setPivotPx(this.mMainMenuRoot.getPivotX(), this.mMainMenuRoot.getPivotY(), width, height);
        }
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void detach() {
        View childAt;
        View view = this.mSubMenuRoot;
        if (view instanceof RoundFrameLayout) {
            view.setAlpha(1.0f);
            this.mSubMenuRoot.setScaleX(1.0f);
            this.mSubMenuRoot.setScaleY(1.0f);
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator == null || !cOUIPopupTouchMotionAnimator.isAnyAnimating()) {
            View view2 = this.mMainMenuRoot;
            if (view2 != null) {
                view2.setScaleX(1.0f);
                this.mMainMenuRoot.setScaleY(1.0f);
                this.mMainMenuRoot.setTranslationX(0.0f);
                this.mMainMenuRoot.setTranslationY(0.0f);
                View view3 = this.mMainMenuRoot;
                if ((view3 instanceof ViewGroup) && (childAt = ((ViewGroup) view3).getChildAt(0)) != null) {
                    childAt.setAlpha(1.0f);
                }
            }
            View view4 = this.mRootView;
            if (view4 != null) {
                view4.setScaleX(1.0f);
                this.mRootView.setScaleY(1.0f);
                this.mRootView.setAlpha(1.0f);
            }
        }
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void ensureSubMenuCallback() {
        if (this.mSubMenuUpdateCallback != null || this.mSubMenuTouchAnimator == null) {
            return;
        }
        x xVar = new x(this);
        this.mSubMenuUpdateCallback = xVar;
        this.mSubMenuTouchAnimator.addOnAnimationUpdateCallback(xVar);
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public boolean isSubMenuVisible() {
        View view = this.mSubMenuRoot;
        return view != null && view.getVisibility() == 0;
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void setMainMenuView(RoundFrameLayout roundFrameLayout) {
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator != null && this.mMainMenuRoot != roundFrameLayout) {
            cOUIPopupTouchMotionAnimator.cancel();
            this.mMainMenuTouchAnimator = null;
        }
        super.setMainMenuView(roundFrameLayout);
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void setSubMenuView(RoundFrameLayout roundFrameLayout) {
        this.mSubMenuManuallyHidden = false;
        this.mSubMenuPivotCompensationEnabled = false;
        super.setSubMenuView(roundFrameLayout);
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void startMainMenuEnter(boolean z2) {
        if (this.mMainMenuRoot == null) {
            Log.w(TAG, "No main menu root view! Set a main menu view before starting animation!");
            return;
        }
        this.mRootView.setTranslationY(0.0f);
        this.mRootView.setScaleX(1.0f);
        this.mRootView.setScaleY(1.0f);
        BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = this.mMenuStateChangedListener;
        if (onMenuStateChangedListener != null) {
            onMenuStateChangedListener.onMainMenuStartToEnter();
        }
        if (this.mMainMenuRoot.getVisibility() == 8) {
            this.mMainMenuRoot.setVisibility(0);
        }
        this.mMainMenuRoot.setAlpha(1.0f);
        syncMainMenuEnterPivotFromDomain();
        if (isSubMenuVisible()) {
            this.mSubMenuManuallyHidden = true;
            COUILog.d(TAG, "startMainMenuEnter: sub menu is visible, force hide!");
            cancelAndResetSubMenuUnits();
            View view = this.mSubMenuRoot;
            if (view != null) {
                view.setVisibility(8);
                this.mSubMenuRoot.setAlpha(1.0f);
                this.mSubMenuRoot.setScaleX(1.0f);
                this.mSubMenuRoot.setScaleY(1.0f);
                this.mSubMenuRoot.setTranslationX(0.0f);
                this.mSubMenuRoot.setTranslationY(0.0f);
            }
            this.mSubMenuPivotCompensationEnabled = false;
        }
        ensureAbility();
        syncPivotToAnimator();
        MenuTransitionUnit menuTransitionUnit = this.mMenuTransitionUnit;
        if (menuTransitionUnit != null) {
            menuTransitionUnit.cancel();
            this.mMenuTransitionUnit.reset(false);
        }
        prepareMainMenuEnterBeforePopupAnim(z2);
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator == null) {
            showMainMenuRootIfNeeded();
            return;
        }
        cOUIPopupTouchMotionAnimator.setPopupMotionEndListener(new AnonymousClass1());
        showMainMenuRootIfNeeded();
        this.mMainMenuTouchAnimator.startPopupAnim();
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void startMainMenuExit(boolean z2) {
        if (this.mMainMenuRoot == null) {
            Log.w(TAG, "No main menu root view! Set a main menu view before starting animation!");
            return;
        }
        if (isSubMenuVisible()) {
            SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
            boolean z3 = subMenuTransitionUnit != null && subMenuTransitionUnit.isRunning();
            freezeSubMenuAndMenuTransitionUnits();
            if (this.mSubMenuRoot != null) {
                float left = this.mMainMenuRoot.getLeft() + this.mMainMenuRoot.getTranslationX() + this.mMainMenuRoot.getPivotX();
                float top = this.mMainMenuRoot.getTop() + this.mMainMenuRoot.getTranslationY() + this.mMainMenuRoot.getPivotY();
                float left2 = (left - this.mSubMenuRoot.getLeft()) - this.mSubMenuRoot.getTranslationX();
                float top2 = (top - this.mSubMenuRoot.getTop()) - this.mSubMenuRoot.getTranslationY();
                if (z3) {
                    this.mSubMenuPivotDeltaX = left2 - this.mSubMenuRoot.getPivotX();
                    this.mSubMenuPivotDeltaY = top2 - this.mSubMenuRoot.getPivotY();
                    this.mSubMenuPivotCompensationEnabled = true;
                    if (COUI_DEBUG) {
                        Log.d(TAG, "startMainMenuExit: pivot compensation enabled, pivotDelta=(" + this.mSubMenuPivotDeltaX + "," + this.mSubMenuPivotDeltaY + "), subPivot=(" + this.mSubMenuRoot.getPivotX() + "," + this.mSubMenuRoot.getPivotY() + "), idealPivot=(" + left2 + "," + top2 + ")");
                    }
                } else {
                    this.mSubMenuRoot.setPivotX(left2);
                    this.mSubMenuRoot.setPivotY(top2);
                    this.mSubMenuPivotCompensationEnabled = false;
                }
                if (this.mSubMenuRoot.getVisibility() != 0) {
                    this.mSubMenuRoot.setVisibility(0);
                }
            }
        }
        BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = this.mMenuStateChangedListener;
        if (onMenuStateChangedListener != null) {
            onMenuStateChangedListener.onMainMenuStartToExit();
        }
        ensureAbility();
        syncPivotToAnimator();
        this.mMainMenuTouchAnimator.setPopupMotionEndListener(new AnonymousClass2());
        this.mMainMenuTouchAnimator.startDismissAnim();
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void startSubMenuEnter(boolean z2) {
        if (this.mMainMenuRoot != null && this.mSubMenuRoot != null) {
            configMainMenuAnimationPropertiesForSubMenuEnter();
            syncPivotToAnimator();
        }
        super.startSubMenuEnter(z2);
        if (this.mMainMenuRoot == null) {
            Log.e(TAG, "No main menu view! Add a main menu view before showing sub menu!");
            return;
        }
        if (this.mSubMenuRoot == null) {
            Log.w(TAG, "No sub menu root view! Set a sub menu view before starting animation!");
            return;
        }
        this.mSubMenuManuallyHidden = false;
        this.mSubMenuPivotCompensationEnabled = false;
        BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = this.mMenuStateChangedListener;
        if (onMenuStateChangedListener != null) {
            onMenuStateChangedListener.onSubMenuStartToEnter();
        }
        this.mSubMenuRoot.setPivotX(this.mDomain.getSubMenuEnterPivotX());
        this.mSubMenuRoot.setPivotY(this.mDomain.getSubMenuEnterPivotY());
        prepareSubMenuEnterBeforeAnim(z2, 0.0f, 0.0f);
        ParentInfluenceUnit parentInfluenceUnit = this.mParentInfluenceUnit;
        if (parentInfluenceUnit != null) {
            parentInfluenceUnit.updateFromParent(this.mLastGlobalTranslateX, this.mLastGlobalTranslateY, this.mLastGlobalScaleX, this.mLastGlobalScaleY, this.mRootViewAlpha);
        }
        SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit != null) {
            subMenuTransitionUnit.configForDefaultScreenEnter(0.0f, 0.0f);
            this.mSubMenuTransitionUnit.setProgressCallback(null);
            this.mSubMenuTransitionUnit.addEndListener(new y(this));
        }
        ensureAbility();
        MenuTransitionUnit menuTransitionUnit = this.mMenuTransitionUnit;
        if (menuTransitionUnit != null) {
            float f3 = this.mMainMenuScalePercent;
            menuTransitionUnit.animateToSubMenuEnterState(f3, f3, 0.0f, this.mMainMenuAlphaPercent, 0.0f, 0.35f);
        }
        SubMenuTransitionUnit subMenuTransitionUnit2 = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit2 != null) {
            subMenuTransitionUnit2.setStartValue(subMenuTransitionUnit2.getCurrentProgress());
            this.mSubMenuTransitionUnit.animateToEnterState(0.0f, 0.35f);
            if (z2 || !this.mSubMenuTransitionUnit.canSkipToEnd()) {
                return;
            }
            this.mSubMenuTransitionUnit.skipToEnd();
        }
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void startSubMenuExit(boolean z2) {
        super.startSubMenuExit(z2);
        if (this.mMainMenuRoot == null) {
            Log.e(TAG, "No main menu view! Add a main menu view before showing sub menu!");
            return;
        }
        if (this.mSubMenuRoot == null) {
            Log.w(TAG, "No sub menu root view! Set a sub menu view before starting animation!");
            return;
        }
        BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = this.mMenuStateChangedListener;
        if (onMenuStateChangedListener != null) {
            onMenuStateChangedListener.onSubMenuStartToExit();
        }
        MenuTransitionUnit menuTransitionUnit = this.mMenuTransitionUnit;
        if (menuTransitionUnit != null) {
            menuTransitionUnit.animateToSubMenuExitState(0.0f, 0.35f);
        }
        SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit != null) {
            subMenuTransitionUnit.setStartValue(subMenuTransitionUnit.getCurrentProgress());
            this.mSubMenuTransitionUnit.animateToExitState(0.0f, 0.35f);
            if (z2 || !this.mSubMenuTransitionUnit.canSkipToEnd()) {
                return;
            }
            this.mSubMenuTransitionUnit.skipToEnd();
        }
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void stopAllAnimation() {
        super.stopAllAnimation();
        this.mSubMenuManuallyHidden = false;
        this.mSubMenuPivotCompensationEnabled = false;
        View view = this.mMainMenuRoot;
        if (view != null) {
            view.setScaleX(1.0f);
            this.mMainMenuRoot.setScaleY(1.0f);
            this.mMainMenuRoot.setTranslationX(0.0f);
            this.mMainMenuRoot.setTranslationY(0.0f);
        }
        View view2 = this.mSubMenuRoot;
        if (view2 != null) {
            view2.setAlpha(1.0f);
            this.mSubMenuRoot.setScaleX(1.0f);
            this.mSubMenuRoot.setScaleY(1.0f);
            this.mSubMenuRoot.setTranslationX(0.0f);
            this.mSubMenuRoot.setTranslationY(0.0f);
        }
        View view3 = this.mRootView;
        if (view3 != null) {
            view3.setScaleX(1.0f);
            this.mRootView.setScaleY(1.0f);
            this.mRootView.setAlpha(1.0f);
        }
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void syncMainMenuAnimPivot() {
        View view;
        if (this.mMainMenuTouchAnimator == null || (view = this.mMainMenuRoot) == null || view.getWidth() <= 0) {
            return;
        }
        syncPivotToAnimator();
    }
}