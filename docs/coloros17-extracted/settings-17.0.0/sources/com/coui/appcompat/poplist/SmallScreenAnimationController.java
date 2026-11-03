package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.coui.appcompat.anim.ability.COUIPopupTouchMotionAnimator;
import com.coui.appcompat.anim.ability.MenuTransitionUnit;
import com.coui.appcompat.anim.ability.ParentInfluenceUnit;
import com.coui.appcompat.anim.ability.SubMenuTransitionUnit;
import com.coui.appcompat.animation.rt.RtViewWrites;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.OnAnimationEndListener;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.poplist.R;

/* JADX INFO: loaded from: classes7.dex */
class SmallScreenAnimationController extends BasePopupMenuAnimationController {
    private static final float ALPHA_OPAQUE = 1.0f;
    private static final float ANIMATION_END_VALUE_AT_START = 0.0f;
    private static final boolean COUI_DEBUG;
    private static final float DEFAULT_MAIN_MENU_ALPHA_PERCENT = 0.3f;
    private static final float DEFAULT_TRANSLATE = 0.0f;
    private static final int FIRST_CHILD_INDEX = 0;
    private static final int LIST_VIEW_ALPHA_FIRST_CHILD_INDEX = 1;
    private static final float MAIN_MENU_PIVOT_X_LEFT_ALIGN = 0.0f;
    private static final float MAIN_MENU_PIVOT_Y_TOP = 0.0f;
    private static final float MAIN_MENU_TARGET_TRANSLATION_Y_NONE = 0.0f;
    private static final int MAIN_MENU_WIDTH_HALF_DIVISOR = 2;
    private static final float MENU_TRANSITION_Y_NONE = 0.0f;
    private static final int OVERRIDE_OUTLINE_LEFT = 0;
    private static final float SCALE_IDENTITY = 1.0f;
    private static final int SUBMENU_CLIP_TOP_END = 0;
    private static final float SUBMENU_ENTER_ALPHA_END = 1.0f;
    private static final float SUBMENU_ENTER_ALPHA_START = 1.0f;
    private static final float SUBMENU_OUTLINE_PROGRESS_FULL = 1.0f;
    private static final int SUBMENU_TRANSLATE_Y_END = 0;
    private static final String TAG = "PopupMenuAnimCtrl-S";
    private static final float TRANSLATION_FLOAT_ZERO = 0.0f;
    private static final int TRANSLATION_INT_ZERO = 0;
    private float mMainMenuAlphaPercent;
    private float mMainMenuScalePercentX;
    private float mMainMenuScalePercentY;
    private final int mMenuInnerPaddingVertical;
    private final int mMinGapBetweenMainAndSubMenu;
    private int mSubMenuClippedBottomEnd;
    private int mSubMenuClippedBottomStart;
    private int mSubMenuClippedTopEnd;
    private int mSubMenuClippedTopStart;
    private int mSubMenuTranslateEndY;
    private int mSubMenuTranslateStartY;
    private final Rect mDynamicSubmenuAnchorBounds = new Rect();
    private float mMainMenuSubTranslation = 0.0f;
    private float mLastRealProgress = 0.0f;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.SmallScreenAnimationController$1, reason: invalid class name */
    public class AnonymousClass1 implements OnAnimationEndListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
        public void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
            if (SmallScreenAnimationController.g()) {
                Log.d(SmallScreenAnimationController.TAG, "onAnimationEnd enter popupKit: canceled=" + z2);
            }
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = SmallScreenAnimationController.this.mMenuStateChangedListener;
            if (onMenuStateChangedListener != null) {
                if (z2) {
                    onMenuStateChangedListener.onMainMenuAnimationCanceled();
                } else {
                    onMenuStateChangedListener.onMainMenuEntered();
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.SmallScreenAnimationController$2, reason: invalid class name */
    public class AnonymousClass2 implements OnAnimationEndListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
        public void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
            if (SmallScreenAnimationController.g()) {
                Log.d(SmallScreenAnimationController.TAG, "onAnimationEnd exit popupKit: canceled=" + z2);
            }
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = SmallScreenAnimationController.this.mMenuStateChangedListener;
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

    public SmallScreenAnimationController(Context context) {
        this.mMinGapBetweenMainAndSubMenu = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_min_gap_to_top);
        this.mMenuInnerPaddingVertical = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_padding_vertical);
    }

    public static /* synthetic */ void c(SmallScreenAnimationController smallScreenAnimationController, COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
        if (z2) {
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = smallScreenAnimationController.mMenuStateChangedListener;
            if (onMenuStateChangedListener != null) {
                onMenuStateChangedListener.onSubMenuAnimationCanceled();
                return;
            }
            return;
        }
        if (f3 != 0.0f) {
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener2 = smallScreenAnimationController.mMenuStateChangedListener;
            if (onMenuStateChangedListener2 != null) {
                onMenuStateChangedListener2.onSubMenuEntered();
                return;
            }
            return;
        }
        smallScreenAnimationController.mMainMenuSubTranslation = 0.0f;
        smallScreenAnimationController.resetMainMenuPivotToEnter();
        BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener3 = smallScreenAnimationController.mMenuStateChangedListener;
        if (onMenuStateChangedListener3 != null) {
            onMenuStateChangedListener3.onSubMenuExited();
        }
    }

    private float clampMainMenuTranslationY(float f3) {
        PopupMenuDomain popupMenuDomain = this.mDomain;
        return Math.max((popupMenuDomain.mWindow.top + popupMenuDomain.mWindowBarriers.top) - popupMenuDomain.mMainMenu.top, f3);
    }

    private float clampSubMenuTranslationY(float f3) {
        if (this.mDomain.mSubMenu.isEmpty()) {
            return f3;
        }
        PopupMenuDomain popupMenuDomain = this.mDomain;
        return Math.max((popupMenuDomain.mWindow.top + popupMenuDomain.mWindowBarriers.top) - popupMenuDomain.mSubMenu.top, f3);
    }

    private float computeMainMenuTargetTranslationY() {
        int i3;
        float f3 = 0.0f;
        if (this.mDomain.mSubMenu.isEmpty()) {
            return 0.0f;
        }
        PopupMenuDomain popupMenuDomain = this.mDomain;
        int i4 = popupMenuDomain.mMainMenu.top;
        int i5 = this.mMinGapBetweenMainAndSubMenu;
        int i6 = i4 + i5;
        int i7 = popupMenuDomain.mSubMenu.top;
        if (i6 <= i7) {
            int i8 = i4 + i5;
            int i9 = this.mSubMenuTranslateEndY;
            if (i8 > i7 + i9) {
                i3 = i7 + i9;
                i4 += i5;
            }
            return clampMainMenuTranslationY(f3);
        }
        i3 = i7 - i5;
        f3 = i3 - i4;
        return clampMainMenuTranslationY(f3);
    }

    private void configMainMenuAnimationPropertiesForSubMenuEnter() {
        this.mMainMenuAlphaPercent = 0.3f;
        float fWidth = this.mDomain.mMainMenuRelocated.width() / this.mDomain.mMainMenu.width();
        this.mMainMenuScalePercentX = fWidth;
        this.mMainMenuScalePercentY = fWidth;
        PopupMenuDomain popupMenuDomain = this.mDomain;
        Rect rect = popupMenuDomain.mMainMenu;
        int i3 = rect.left;
        Rect rect2 = popupMenuDomain.mMainMenuRelocated;
        if (i3 == rect2.left) {
            this.mMainMenuRoot.setPivotX(0.0f);
        } else if (rect.right == rect2.right) {
            View view = this.mMainMenuRoot;
            view.setPivotX(view.getWidth());
        } else {
            View view2 = this.mMainMenuRoot;
            view2.setPivotX(view2.getWidth() / 2.0f);
        }
        this.mMainMenuRoot.setPivotY(0.0f);
    }

    private void configSubMenuAnimationPropertiesForSubMenuEnter() {
        BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = this.mMenuStateChangedListener;
        if (onMenuStateChangedListener != null) {
            onMenuStateChangedListener.onSubMenuStartToEnter();
        }
        this.mSubMenuClippedTopStart = this.mMenuInnerPaddingVertical;
        this.mSubMenuClippedTopEnd = 0;
        int iHeight = this.mDomain.mSubMenuAnchor.height();
        PopupMenuDomain popupMenuDomain = this.mDomain;
        this.mSubMenuClippedBottomStart = Math.min(iHeight, popupMenuDomain.mMainMenu.bottom - popupMenuDomain.mSubMenuAnchor.top);
        this.mSubMenuClippedBottomEnd = this.mDomain.mSubMenu.height();
        View view = this.mSubMenuRoot;
        if (view instanceof RoundFrameLayout) {
            ((RoundFrameLayout) view).setOverrideOutline(0, this.mSubMenuClippedTopStart, this.mDomain.mSubMenu.width(), this.mSubMenuClippedTopStart + this.mSubMenuClippedBottomStart, 1.0f);
        }
    }

    public static /* synthetic */ void d(SmallScreenAnimationController smallScreenAnimationController, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        View view;
        View childAt;
        View view2 = smallScreenAnimationController.mMainMenuRoot;
        if (view2 == null || !view2.isAttachedToWindow()) {
            return;
        }
        boolean zIsUseRt = smallScreenAnimationController.mMainMenuTouchAnimator.isUseRt();
        float translateX = contributeSink.getTranslateX();
        float translateY = contributeSink.getTranslateY();
        float scaleX = contributeSink.getScaleX();
        float scaleY = contributeSink.getScaleY();
        float alpha = contributeSink.getAlpha();
        smallScreenAnimationController.mLastGlobalTranslateX = translateX;
        smallScreenAnimationController.mLastGlobalTranslateY = translateY;
        smallScreenAnimationController.mLastGlobalScaleX = scaleX;
        smallScreenAnimationController.mLastGlobalScaleY = scaleY;
        smallScreenAnimationController.mRootViewAlpha = alpha;
        MenuTransitionUnit menuTransitionUnit = smallScreenAnimationController.mMenuTransitionUnit;
        float currentScaleX = menuTransitionUnit != null ? menuTransitionUnit.getCurrentScaleX() : 1.0f;
        MenuTransitionUnit menuTransitionUnit2 = smallScreenAnimationController.mMenuTransitionUnit;
        BasePopupMenuAnimationController.applyMainMenuScaleFromCollector(smallScreenAnimationController.mMainMenuTouchAnimator, smallScreenAnimationController.mMainMenuRoot, scaleX, scaleY, currentScaleX, menuTransitionUnit2 != null ? menuTransitionUnit2.getCurrentScaleY() : 1.0f, zIsUseRt);
        MenuTransitionUnit menuTransitionUnit3 = smallScreenAnimationController.mMenuTransitionUnit;
        BasePopupMenuAnimationController.applyMainMenuTranslationFromCollector(smallScreenAnimationController.mMainMenuTouchAnimator, smallScreenAnimationController.mMainMenuRoot, translateX, translateY, menuTransitionUnit3 != null ? menuTransitionUnit3.getCurrentTranslationY() : 0.0f, zIsUseRt);
        View view3 = smallScreenAnimationController.mMainMenuRoot;
        if ((view3 instanceof ViewGroup) && (childAt = ((ViewGroup) view3).getChildAt(0)) != null) {
            MenuTransitionUnit menuTransitionUnit4 = smallScreenAnimationController.mMenuTransitionUnit;
            BasePopupMenuAnimationController.setPopupMenuAlpha(childAt, menuTransitionUnit4 != null ? menuTransitionUnit4.getCurrentContentAlpha() : 1.0f, zIsUseRt);
        }
        if (smallScreenAnimationController.mParentInfluenceUnit != null && (view = smallScreenAnimationController.mSubMenuRoot) != null && view.getVisibility() == 0) {
            smallScreenAnimationController.mParentInfluenceUnit.updateFromParent(BasePopupMenuAnimationController.effectiveParentTranslateX(smallScreenAnimationController.mMainMenuTouchAnimator, translateX), BasePopupMenuAnimationController.effectiveParentTranslateY(smallScreenAnimationController.mMainMenuTouchAnimator, translateY), BasePopupMenuAnimationController.effectiveParentScaleX(smallScreenAnimationController.mMainMenuTouchAnimator, scaleX), BasePopupMenuAnimationController.effectiveParentScaleY(smallScreenAnimationController.mMainMenuTouchAnimator, scaleY), alpha);
        }
        BasePopupMenuAnimationController.applyPopupAlphaFromCollector(smallScreenAnimationController.mMainMenuTouchAnimator, smallScreenAnimationController.mRootView, smallScreenAnimationController.mMainMenuRoot, alpha, zIsUseRt);
    }

    public static /* synthetic */ void e(SmallScreenAnimationController smallScreenAnimationController, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        View view = smallScreenAnimationController.mSubMenuRoot;
        if (view == null || !view.isAttachedToWindow()) {
            return;
        }
        boolean zIsUseRt = smallScreenAnimationController.mSubMenuTouchAnimator.isUseRt();
        float alpha = contributeSink.getAlpha();
        smallScreenAnimationController.applySubMenuVisibilityForBlurAlpha(alpha, 0.1f);
        float fClampSubMenuTranslationY = smallScreenAnimationController.clampSubMenuTranslationY(contributeSink.getTranslateY());
        float scaleX = contributeSink.getScaleX();
        float scaleY = contributeSink.getScaleY();
        RtViewWrites.setTranslationXY(smallScreenAnimationController.mSubMenuRoot, contributeSink.getTranslateX(), fClampSubMenuTranslationY, zIsUseRt);
        BasePopupMenuAnimationController.setPopupMenuScale(smallScreenAnimationController.mSubMenuRoot, scaleX, scaleY, zIsUseRt);
        BasePopupMenuAnimationController.setPopupMenuAlpha(smallScreenAnimationController.mSubMenuRoot, alpha, zIsUseRt);
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
                int i5 = (int) (iHeight - this.mMainMenuSubTranslation);
                if (COUI_DEBUG) {
                    Log.d(TAG, "ensureAbility: View layout - left=" + this.mMainMenuRoot.getLeft() + ", top=" + this.mMainMenuRoot.getTop() + ", translationY=" + this.mMainMenuRoot.getTranslationY() + ", visibility=" + this.mMainMenuRoot.getVisibility() + ", menuHeightPx=" + iHeight + ", isAboveAnchor=" + z2);
                }
                this.mMainMenuTouchAnimator.setTargetSize(iWidth, i5);
                this.mMainMenuTouchAnimator.setIsAboveAnchor(z2);
                this.mMainMenuTouchAnimator.setIsCenter(z3);
                this.mMainMenuTouchAnimator.setIsLeft(z4);
                if (this.mMenuTransitionUnit == null) {
                    MenuTransitionUnit menuTransitionUnit = new MenuTransitionUnit();
                    this.mMenuTransitionUnit = menuTransitionUnit;
                    this.mMainMenuTouchAnimator.addMotionUnit(menuTransitionUnit);
                }
                if (this.mMainMenuUpdateCallback == null) {
                    h0 h0Var = new h0(this);
                    this.mMainMenuUpdateCallback = h0Var;
                    this.mMainMenuTouchAnimator.addOnAnimationUpdateCallback(h0Var);
                }
                configureMainPopupMotionGeometry();
            }
        }
    }

    public static /* synthetic */ void f(SmallScreenAnimationController smallScreenAnimationController, float f3) {
        smallScreenAnimationController.onSubMenuProgressUpdate(f3);
    }

    public static /* bridge */ /* synthetic */ boolean g() {
        return COUI_DEBUG;
    }

    private float getLastRealProgress() {
        return this.mLastRealProgress;
    }

    private void onSubMenuProgressUpdate(float f3) {
        View view;
        if (!(this.mSubMenuRoot instanceof RoundFrameLayout)) {
            return;
        }
        if (f3 < getLastRealProgress() && (view = this.mDomain.mAnchorView) != null) {
            int i3 = this.mSubMenuTranslateStartY;
            view.getGlobalVisibleRect(this.mDynamicSubmenuAnchorBounds);
            View view2 = this.mMainMenuRoot;
            if (view2 != null) {
                this.mDynamicSubmenuAnchorBounds.offset(-((int) view2.getTranslationX()), -((int) this.mMainMenuRoot.getTranslationY()));
            }
            int i4 = (this.mDynamicSubmenuAnchorBounds.top - this.mDomain.mSubMenu.top) - this.mMenuInnerPaddingVertical;
            this.mSubMenuTranslateStartY = i4;
            if (i3 != i4) {
                if (COUI_DEBUG) {
                    Log.d(TAG, "onSubMenuProgressUpdate: dynamic anchor recalc, old=" + i3 + ", new=" + this.mSubMenuTranslateStartY + ", dynamicAnchorTop=" + this.mDynamicSubmenuAnchorBounds.top + ", subMenuTop=" + this.mDomain.mSubMenu.top);
                }
                SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
                if (subMenuTransitionUnit != null) {
                    subMenuTransitionUnit.updateTranslateYTargets(this.mSubMenuTranslateStartY, this.mSubMenuTranslateEndY);
                }
            }
        }
        this.mLastRealProgress = f3;
        if (this.mSubMenuRoot.getVisibility() != 0) {
            this.mSubMenuRoot.setVisibility(0);
        }
        int convertedFraction = (int) UIUtil.getConvertedFraction(this.mSubMenuClippedTopStart, this.mSubMenuClippedTopEnd, f3);
        ((RoundFrameLayout) this.mSubMenuRoot).setOverrideOutline(0, convertedFraction, this.mDomain.mSubMenu.width(), convertedFraction + ((int) UIUtil.getConvertedFraction(this.mSubMenuClippedBottomStart, this.mSubMenuClippedBottomEnd, f3)), f3);
        View childAt = ((RoundFrameLayout) this.mSubMenuRoot).getChildAt(0);
        if (!(childAt instanceof ListView)) {
            return;
        }
        int i5 = 1;
        while (true) {
            ListView listView = (ListView) childAt;
            if (i5 > listView.getChildCount()) {
                return;
            }
            View childAt2 = listView.getChildAt(i5);
            if (childAt2 != null && childAt2.getAlpha() != 1.0f) {
                childAt2.setAlpha(1.0f);
            }
            i5++;
        }
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
        View view = this.mSubMenuRoot;
        if (view instanceof RoundFrameLayout) {
            view.setTranslationY(0.0f);
            this.mSubMenuRoot.setTranslationX(0.0f);
            this.mSubMenuRoot.setScaleX(1.0f);
            this.mSubMenuRoot.setScaleY(1.0f);
            ((RoundFrameLayout) this.mSubMenuRoot).clearOverrideOutline();
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator == null || !cOUIPopupTouchMotionAnimator.isAnyAnimating()) {
            View view2 = this.mMainMenuRoot;
            if (view2 != null) {
                view2.setScaleX(1.0f);
                this.mMainMenuRoot.setScaleY(1.0f);
                this.mMainMenuRoot.setTranslationX(0.0f);
                this.mMainMenuRoot.setTranslationY(0.0f);
            }
            View view3 = this.mRootView;
            if (view3 != null) {
                view3.setScaleX(1.0f);
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
        i0 i0Var = new i0(this);
        this.mSubMenuUpdateCallback = i0Var;
        this.mSubMenuTouchAnimator.addOnAnimationUpdateCallback(i0Var);
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
        this.mMainMenuSubTranslation = 0.0f;
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void setSubMenuView(RoundFrameLayout roundFrameLayout) {
        PopupMenuDomain popupMenuDomain = this.mDomain;
        this.mSubMenuTranslateStartY = (popupMenuDomain.mSubMenuAnchor.top - popupMenuDomain.mSubMenu.top) - this.mMenuInnerPaddingVertical;
        this.mSubMenuTranslateEndY = 0;
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
            COUILog.d(TAG, "startMainMenuEnter: sub menu is visible, force hide!");
            cancelAndResetSubMenuUnits();
            View view = this.mSubMenuRoot;
            if (view instanceof RoundFrameLayout) {
                view.setVisibility(8);
                this.mSubMenuRoot.setTranslationY(0.0f);
                this.mSubMenuRoot.setTranslationX(0.0f);
                this.mSubMenuRoot.setScaleX(1.0f);
                this.mSubMenuRoot.setScaleY(1.0f);
                ((RoundFrameLayout) this.mSubMenuRoot).clearOverrideOutline();
            }
            this.mMainMenuRoot.setTranslationY(0.0f);
            this.mMainMenuRoot.setScaleX(1.0f);
        }
        this.mMainMenuSubTranslation = 0.0f;
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
            Log.w(TAG, "startMainMenuEnter: mMainMenuTouchAnimator is null, skip popup anim");
            showMainMenuRootIfNeeded();
        } else {
            cOUIPopupTouchMotionAnimator.setPopupMotionEndListener(new AnonymousClass1());
            this.mMainMenuTouchAnimator.startPopupAnim();
            showMainMenuRootIfNeeded();
        }
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void startMainMenuExit(boolean z2) {
        if (this.mMainMenuRoot == null) {
            Log.w(TAG, "No main menu root view! Set a main menu view before starting animation!");
            return;
        }
        if (isSubMenuVisible()) {
            freezeSubMenuAndMenuTransitionUnits();
            if (this.mSubMenuRoot != null) {
                float left = this.mMainMenuRoot.getLeft() + this.mMainMenuRoot.getTranslationX() + this.mMainMenuRoot.getPivotX();
                float top = this.mMainMenuRoot.getTop() + this.mMainMenuRoot.getTranslationY() + this.mMainMenuRoot.getPivotY();
                float left2 = (left - this.mSubMenuRoot.getLeft()) - this.mSubMenuRoot.getTranslationX();
                float top2 = (top - this.mSubMenuRoot.getTop()) - this.mSubMenuRoot.getTranslationY();
                this.mSubMenuRoot.setPivotX(left2);
                this.mSubMenuRoot.setPivotY(top2);
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
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator == null) {
            Log.w(TAG, "startMainMenuExit: mMainMenuTouchAnimator is null, skip dismiss anim");
        } else {
            cOUIPopupTouchMotionAnimator.setPopupMotionEndListener(new AnonymousClass2());
            this.mMainMenuTouchAnimator.startDismissAnim();
        }
    }

    @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController
    public void startSubMenuEnter(boolean z2) {
        super.startSubMenuEnter(z2);
        if (this.mMainMenuRoot == null) {
            Log.e(TAG, "No main menu view! Add a main menu view before showing sub menu!");
            return;
        }
        if (this.mSubMenuRoot == null) {
            Log.w(TAG, "No sub menu root view! Set a sub menu view before starting animation!");
            return;
        }
        configMainMenuAnimationPropertiesForSubMenuEnter();
        syncPivotToAnimator();
        configSubMenuAnimationPropertiesForSubMenuEnter();
        prepareSubMenuEnterBeforeAnim(z2, 1.0f, 1.0f);
        ParentInfluenceUnit parentInfluenceUnit = this.mParentInfluenceUnit;
        if (parentInfluenceUnit != null) {
            parentInfluenceUnit.updateFromParent(this.mLastGlobalTranslateX, this.mLastGlobalTranslateY, this.mLastGlobalScaleX, this.mLastGlobalScaleY, this.mRootViewAlpha);
        }
        if (this.mSubMenuTransitionUnit != null) {
            MenuTransitionUnit menuTransitionUnit = this.mMenuTransitionUnit;
            float currentTranslationY = menuTransitionUnit != null ? menuTransitionUnit.getCurrentTranslationY() : 0.0f;
            float f3 = this.mSubMenuTranslateStartY + currentTranslationY;
            this.mSubMenuTransitionUnit.configForSmallScreenEnter(f3, this.mSubMenuTranslateEndY, 1.0f, 1.0f);
            if (COUI_DEBUG) {
                Log.d(TAG, "startSubMenuEnter: config SubMenuTransitionUnit, rawStartY=" + this.mSubMenuTranslateStartY + ", menuTranslateY=" + currentTranslationY + ", adjustedStartY=" + f3 + ", translateEndY=" + this.mSubMenuTranslateEndY + ", subMenuView.translationY=" + this.mSubMenuRoot.getTranslationY() + ", subMenuView.translationX=" + this.mSubMenuRoot.getTranslationX() + ", subMenuView.scaleX=" + this.mSubMenuRoot.getScaleX() + ", subMenuView.scaleY=" + this.mSubMenuRoot.getScaleY() + ", lastGlobalTY=" + this.mLastGlobalTranslateY + ", lastGlobalTX=" + this.mLastGlobalTranslateX);
            }
            this.mSubMenuTransitionUnit.setProgressCallback(new f0(this));
            this.mSubMenuTransitionUnit.addEndListener(new g0(this));
        }
        MenuTransitionUnit menuTransitionUnit2 = this.mMenuTransitionUnit;
        float fClampSubMenuTranslationY = clampSubMenuTranslationY(this.mLastGlobalTranslateY + this.mSubMenuTranslateStartY + (menuTransitionUnit2 != null ? menuTransitionUnit2.getCurrentTranslationY() : 0.0f));
        this.mSubMenuRoot.setTranslationX(this.mLastGlobalTranslateX);
        this.mSubMenuRoot.setTranslationY(fClampSubMenuTranslationY);
        this.mSubMenuRoot.setScaleX(this.mLastGlobalScaleX);
        this.mSubMenuRoot.setScaleY(this.mLastGlobalScaleY);
        ensureAbility();
        if (this.mMenuTransitionUnit != null) {
            float fComputeMainMenuTargetTranslationY = computeMainMenuTargetTranslationY();
            this.mMainMenuSubTranslation = fComputeMainMenuTargetTranslationY;
            this.mMenuTransitionUnit.animateToSubMenuEnterState(this.mMainMenuScalePercentX, this.mMainMenuScalePercentY, fComputeMainMenuTargetTranslationY, this.mMainMenuAlphaPercent, 0.0f, 0.35f);
        }
        SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit != null) {
            subMenuTransitionUnit.setStartValue(subMenuTransitionUnit.getCurrentProgress());
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
        View view = this.mSubMenuRoot;
        if (view == null) {
            Log.w(TAG, "No sub menu root view! Set a sub menu view before starting animation!");
            return;
        }
        if (view.getVisibility() != 0) {
            this.mMainMenuSubTranslation = 0.0f;
            SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
            if (subMenuTransitionUnit != null) {
                subMenuTransitionUnit.reset(false);
            }
            resetMainMenuPivotToEnter();
            BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener = this.mMenuStateChangedListener;
            if (onMenuStateChangedListener != null) {
                onMenuStateChangedListener.onSubMenuStartToExit();
                this.mMenuStateChangedListener.onSubMenuExited();
                return;
            }
            return;
        }
        PopupMenuDomain popupMenuDomain = this.mDomain;
        this.mSubMenuTranslateStartY = (popupMenuDomain.mSubMenuAnchor.top - popupMenuDomain.mSubMenu.top) - this.mMenuInnerPaddingVertical;
        if (COUI_DEBUG) {
            Log.d(TAG, "startSubMenuExit: recalc mSubMenuTranslateStartY, anchorTop=" + this.mDomain.mSubMenuAnchor.top + ", subMenuTop=" + this.mDomain.mSubMenu.top + ", subMenuView.translationY=" + this.mSubMenuRoot.getTranslationY() + ", subMenuView.translationX=" + this.mSubMenuRoot.getTranslationX() + ", subMenuView.scaleX=" + this.mSubMenuRoot.getScaleX() + ", subMenuView.scaleY=" + this.mSubMenuRoot.getScaleY() + ", subMenuView.visibility=" + this.mSubMenuRoot.getVisibility() + ", mainMenu.translationY=" + this.mMainMenuRoot.getTranslationY() + ", mainMenu.scaleX=" + this.mMainMenuRoot.getScaleX() + ", lastGlobalTY=" + this.mLastGlobalTranslateY);
        }
        BasePopupMenuAnimationController.OnMenuStateChangedListener onMenuStateChangedListener2 = this.mMenuStateChangedListener;
        if (onMenuStateChangedListener2 != null) {
            onMenuStateChangedListener2.onSubMenuStartToExit();
        }
        MenuTransitionUnit menuTransitionUnit = this.mMenuTransitionUnit;
        if (menuTransitionUnit != null) {
            menuTransitionUnit.animateToSubMenuExitState(0.0f, 0.35f);
        }
        SubMenuTransitionUnit subMenuTransitionUnit2 = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit2 != null) {
            subMenuTransitionUnit2.updateTranslateYTargets(this.mSubMenuTranslateStartY, this.mSubMenuTranslateEndY);
            SubMenuTransitionUnit subMenuTransitionUnit3 = this.mSubMenuTransitionUnit;
            subMenuTransitionUnit3.setStartValue(subMenuTransitionUnit3.getCurrentProgress());
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
        this.mMainMenuSubTranslation = 0.0f;
        View view = this.mMainMenuRoot;
        if (view != null) {
            view.setScaleX(1.0f);
            this.mMainMenuRoot.setScaleY(1.0f);
            this.mMainMenuRoot.setTranslationX(0.0f);
            this.mMainMenuRoot.setTranslationY(0.0f);
        }
        View view2 = this.mSubMenuRoot;
        if (view2 != null) {
            view2.setScaleX(1.0f);
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