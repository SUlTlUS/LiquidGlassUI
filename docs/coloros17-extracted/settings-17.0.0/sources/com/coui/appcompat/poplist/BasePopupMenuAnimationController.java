package com.coui.appcompat.poplist;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.coui.appcompat.anim.ability.COUIPopupTouchMotionAnimator;
import com.coui.appcompat.anim.ability.MenuTransitionUnit;
import com.coui.appcompat.anim.ability.ParentInfluenceUnit;
import com.coui.appcompat.anim.ability.PopupMotionAnimateKitBridgeUnit;
import com.coui.appcompat.anim.ability.PopupMotionAnimateKitController;
import com.coui.appcompat.anim.ability.SubMenuTransitionUnit;
import com.coui.appcompat.anim.param.PopupGravityMotionParams;
import com.coui.appcompat.anim.param.PopupGravityMotionParamsExtKt;
import com.coui.appcompat.animation.dynamicanimation.COUIComposeMotionAnimator;
import com.coui.appcompat.animation.rt.RtViewWrites;
import com.coui.appcompat.animation.touchanimation.COUITouchMotionAnimator;
import com.coui.appcompat.animation.touchanimation.TouchMotionParamConfig;
import com.coui.appcompat.log.COUILog;

/* JADX INFO: loaded from: classes7.dex */
abstract class BasePopupMenuAnimationController {

    @Deprecated
    static final float DEFAULT_MAIN_MENU_ALPHA_SPRING_RESPONSE = 0.3f;
    static final float DEFAULT_MAIN_MENU_ENTER_ALPHA_END_VALUE = 1.0f;
    static final float DEFAULT_MAIN_MENU_ENTER_ALPHA_START_VALUE = 0.0f;
    static final float DEFAULT_MAIN_MENU_ENTER_SCALE_END_VALUE = 1.0f;
    static final float DEFAULT_MAIN_MENU_ENTER_SCALE_START_VALUE = 0.0f;

    @Deprecated
    static final float DEFAULT_MAIN_MENU_SCALE_SPRING_RESPONSE = 0.4f;
    static final float DEFAULT_MAIN_MENU_SPRING_BOUNCE = 0.2f;
    static final float DEFAULT_MAIN_MENU_SPRING_EXIT_ALPHA_RESPONSE = 0.25f;
    static final float DEFAULT_MAIN_MENU_SPRING_EXIT_BOUNCE = 0.0f;
    static final float DEFAULT_MAIN_MENU_SPRING_EXIT_SCALE_RESPONSE = 0.3f;
    static final float DEFAULT_MAIN_MENU_SPRING_RESPONSE = 0.35f;
    static final float DEFAULT_SPRING_FACTOR = 10000.0f;
    static final float DEFAULT_SUB_MENU_SPRING_BOUNCE = 0.0f;
    static final float DEFAULT_SUB_MENU_SPRING_RESPONSE = 0.35f;
    protected static final float SUB_MENU_BLUR_VISIBILITY_MIN_ALPHA = 0.1f;
    private static final String TAG = "PopupAnimController";
    PopupMenuDomain mDomain;
    View mMainMenuRoot;
    protected COUIPopupTouchMotionAnimator mMainMenuTouchAnimator;
    protected COUIComposeMotionAnimator.OnAnimationUpdateCallback mMainMenuUpdateCallback;
    protected MenuTransitionUnit mMenuTransitionUnit;
    int mOverrideMainMenuTargetHeight;
    int mOverrideMainMenuTargetWidth;
    protected ParentInfluenceUnit mParentInfluenceUnit;
    View mRootView;
    View mSubMenuRoot;
    protected COUIPopupTouchMotionAnimator mSubMenuTouchAnimator;
    protected SubMenuTransitionUnit mSubMenuTransitionUnit;
    protected COUIComposeMotionAnimator.OnAnimationUpdateCallback mSubMenuUpdateCallback;
    protected float mLastGlobalTranslateX = 0.0f;
    protected float mLastGlobalTranslateY = 0.0f;
    protected float mLastGlobalScaleX = 1.0f;
    protected float mLastGlobalScaleY = 1.0f;
    protected float mRootViewAlpha = 1.0f;
    private final float[] mMainMenuPopupPathOffset = new float[2];
    OnMenuStateChangedListener mMenuStateChangedListener = null;
    float mSubMenuTranslateEndX = 0.0f;
    float mSubMenuScaleStartX = 0.0f;
    float mSubMenuScaleStartY = 0.0f;

    public interface OnMenuStateChangedListener {
        void onMainMenuAnimationCanceled();

        void onMainMenuEntered();

        void onMainMenuExited();

        void onMainMenuStartToEnter();

        void onMainMenuStartToExit();

        void onSubMenuAnimationCanceled();

        void onSubMenuEntered();

        void onSubMenuExited();

        void onSubMenuStartToEnter();

        void onSubMenuStartToExit();
    }

    public static /* synthetic */ boolean a(BasePopupMenuAnimationController basePopupMenuAnimationController, RoundFrameLayout roundFrameLayout, COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, MotionEvent motionEvent) {
        if (roundFrameLayout == basePopupMenuAnimationController.mMainMenuRoot && basePopupMenuAnimationController.isSubMenuVisible()) {
            return false;
        }
        MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
        motionEventObtain.transform(roundFrameLayout.getMatrix());
        cOUIPopupTouchMotionAnimator.handleTouchEvent(motionEventObtain);
        motionEventObtain.recycle();
        return false;
    }

    private static boolean anchorUsableForPopupMotion(Rect rect) {
        if (rect == null) {
            return false;
        }
        if (rect.isEmpty()) {
            return rect.left == rect.right && rect.top == rect.bottom;
        }
        return true;
    }

    public static void applyMainMenuRootAlphaFromGlobal(View view, float f3, boolean z2) {
        if (view == null) {
            return;
        }
        if (!(view instanceof RoundFrameLayout) || !((RoundFrameLayout) view).getUseBackgroundBlur()) {
            f3 = 1.0f;
        }
        setPopupMenuAlpha(view, f3, z2);
    }

    public static void applyMainMenuScaleFromCollector(COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, View view, float f3, float f4, float f5, float f6, boolean z2) {
        if (view == null || cOUIPopupTouchMotionAnimator == null) {
            return;
        }
        PopupMotionAnimateKitBridgeUnit bridge = cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getBridge();
        if (!bridge.isViewRtDrivesScale()) {
            setPopupMenuScale(view, f3 * f5, f4 * f6, z2);
        } else {
            if (f5 == 1.0f && f6 == 1.0f) {
                return;
            }
            float currentScale = bridge.getCurrentScale();
            setPopupMenuScale(view, f5 * currentScale, currentScale * f6, false);
        }
    }

    public static void applyMainMenuTranslationFromCollector(COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, View view, float f3, float f4, float f5, boolean z2) {
        if (view == null || cOUIPopupTouchMotionAnimator == null) {
            return;
        }
        if (!cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getBridge().isViewRtDrivesTranslation()) {
            RtViewWrites.setTranslationXY(view, f3, f4 + f5, z2);
        } else if (f5 != 0.0f) {
            RtViewWrites.setTranslationY(view, view.getTranslationY() + f5, false);
        }
    }

    public static void applyPopupAlphaFromCollector(COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, View view, View view2, float f3, boolean z2) {
        if (cOUIPopupTouchMotionAnimator == null) {
            setPopupMenuAlpha(view, f3, z2);
            applyMainMenuRootAlphaFromGlobal(view2, f3, z2);
            return;
        }
        PopupMotionAnimateKitBridgeUnit bridge = cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getBridge();
        if (!bridge.isViewRtDrivesAlpha()) {
            setPopupMenuAlpha(view, f3, z2);
            applyMainMenuRootAlphaFromGlobal(view2, f3, z2);
        } else {
            float bridgedAlpha = bridge.getBridgedAlpha();
            setPopupMenuAlpha(view, bridgedAlpha, z2);
            applyMainMenuRootAlphaFromGlobal(view2, bridgedAlpha, z2);
        }
    }

    public static /* synthetic */ boolean b(BasePopupMenuAnimationController basePopupMenuAnimationController, RoundFrameLayout roundFrameLayout, COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, MotionEvent motionEvent) {
        if (roundFrameLayout == basePopupMenuAnimationController.mMainMenuRoot && basePopupMenuAnimationController.isSubMenuVisible()) {
            return false;
        }
        cOUIPopupTouchMotionAnimator.handleTouchEvent(motionEvent);
        return false;
    }

    private void configureMainMenuPopupPathGeometryOnDomain() {
        PopupMenuDomain popupMenuDomain = this.mDomain;
        if (popupMenuDomain != null && !popupMenuDomain.mMainMenu.isEmpty() && anchorUsableForPopupMotion(this.mDomain.mAnchor)) {
            this.mDomain.computeMainMenuPopupPathAnchorOffset(PopupGravityMotionParamsExtKt.get(PopupGravityMotionParams.INSTANCE, PopupMotionAnimateKitController.POPUP_MOTION_INSTANCE_KEY, null), this.mMainMenuPopupPathOffset);
        } else {
            float[] fArr = this.mMainMenuPopupPathOffset;
            fArr[0] = 0.0f;
            fArr[1] = 0.0f;
        }
    }

    public static float effectiveParentScaleX(COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, float f3) {
        if (cOUIPopupTouchMotionAnimator != null) {
            PopupMotionAnimateKitBridgeUnit bridge = cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getBridge();
            if (bridge.isViewRtDrivesScale()) {
                return bridge.getCurrentScale();
            }
        }
        return f3;
    }

    public static float effectiveParentScaleY(COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, float f3) {
        if (cOUIPopupTouchMotionAnimator != null) {
            PopupMotionAnimateKitBridgeUnit bridge = cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getBridge();
            if (bridge.isViewRtDrivesScale()) {
                return bridge.getCurrentScale();
            }
        }
        return f3;
    }

    public static float effectiveParentTranslateX(COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, float f3) {
        View rtMotionView;
        return (cOUIPopupTouchMotionAnimator == null || !cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getBridge().isViewRtDrivesTranslation() || (rtMotionView = cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getRtMotionView()) == null) ? f3 : rtMotionView.getTranslationX();
    }

    public static float effectiveParentTranslateY(COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator, float f3) {
        View rtMotionView;
        return (cOUIPopupTouchMotionAnimator == null || !cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getBridge().isViewRtDrivesTranslation() || (rtMotionView = cOUIPopupTouchMotionAnimator.getPopupMotionKitController().getRtMotionView()) == null) ? f3 : rtMotionView.getTranslationY();
    }

    private COUIPopupTouchMotionAnimator makeAnimator(RoundFrameLayout roundFrameLayout) {
        if (roundFrameLayout == null) {
            return null;
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = new COUIPopupTouchMotionAnimator(roundFrameLayout.getContext(), usePopupTouchRenderThreadWrites());
        cOUIPopupTouchMotionAnimator.setTouchMotionTarget(roundFrameLayout);
        cOUIPopupTouchMotionAnimator.setDeformationCollectorOnly(true).setRubberCollectorOnly(true).setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(false);
        cOUIPopupTouchMotionAnimator.config(new TouchMotionParamConfig().setAnimationParamsType(COUITouchMotionAnimator.AnimationParamsType.MAX));
        roundFrameLayout.addDispatchTouchEventListener(new a(this, roundFrameLayout, cOUIPopupTouchMotionAnimator));
        return cOUIPopupTouchMotionAnimator;
    }

    private COUIPopupTouchMotionAnimator makeSubMenuAnimator(RoundFrameLayout roundFrameLayout) {
        if (roundFrameLayout == null) {
            return null;
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = new COUIPopupTouchMotionAnimator(roundFrameLayout.getContext(), usePopupTouchRenderThreadWrites());
        cOUIPopupTouchMotionAnimator.setTouchMotionTarget(roundFrameLayout);
        cOUIPopupTouchMotionAnimator.setDeformationCollectorOnly(true).setRubberCollectorOnly(true).setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(false);
        cOUIPopupTouchMotionAnimator.setSkipPivotCorrection(true);
        roundFrameLayout.addDispatchTouchEventListener(new b(this, roundFrameLayout, cOUIPopupTouchMotionAnimator));
        return cOUIPopupTouchMotionAnimator;
    }

    private static boolean requiresSyncAlphaForBlurBackground(View view) {
        return (view instanceof RoundFrameLayout) && ((RoundFrameLayout) view).getUseBackgroundBlur();
    }

    public static void setPopupMenuAlpha(View view, float f3, boolean z2) {
        if (view == null) {
            return;
        }
        RtViewWrites.setAlpha(view, f3, z2 && !requiresSyncAlphaForBlurBackground(view));
    }

    public static void setPopupMenuScale(View view, float f3, float f4, boolean z2) {
        if (view == null) {
            return;
        }
        RtViewWrites.setScaleXY(view, f3, f4, z2);
    }

    private void syncRtTargetsToKitController() {
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator == null) {
            return;
        }
        PopupMotionAnimateKitController popupMotionKitController = cOUIPopupTouchMotionAnimator.getPopupMotionKitController();
        popupMotionKitController.setRtMotionView(this.mMainMenuRoot);
        popupMotionKitController.setRtAlphaView(this.mRootView);
    }

    private static boolean usePopupTouchRenderThreadWrites() {
        return PopupMenuRtAnimations.isRtCapabilityAvailable();
    }

    public void applySubMenuVisibilityForBlurAlpha(float f3, float f4) {
        View view = this.mSubMenuRoot;
        if (view == null) {
            return;
        }
        if (!(view instanceof RoundFrameLayout)) {
            if (view.getVisibility() == 0 || f3 <= f4) {
                return;
            }
            this.mSubMenuRoot.setVisibility(0);
            return;
        }
        if (((RoundFrameLayout) view).getUseBackgroundBlur()) {
            int i3 = f3 <= f4 ? 4 : 0;
            if (this.mSubMenuRoot.getVisibility() != i3) {
                this.mSubMenuRoot.setVisibility(i3);
                return;
            }
            return;
        }
        if (this.mSubMenuRoot.getVisibility() == 0 || f3 <= f4) {
            return;
        }
        this.mSubMenuRoot.setVisibility(0);
    }

    public final void cancelAndResetSubMenuUnits() {
        SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit != null) {
            subMenuTransitionUnit.cancel();
            this.mSubMenuTransitionUnit.reset(false);
        }
        ParentInfluenceUnit parentInfluenceUnit = this.mParentInfluenceUnit;
        if (parentInfluenceUnit != null) {
            parentInfluenceUnit.reset(false);
        }
    }

    public void clearSubMenuUnits() {
        SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit != null) {
            subMenuTransitionUnit.cancel();
            this.mSubMenuTransitionUnit = null;
        }
        ParentInfluenceUnit parentInfluenceUnit = this.mParentInfluenceUnit;
        if (parentInfluenceUnit != null) {
            parentInfluenceUnit.reset(false);
            this.mParentInfluenceUnit = null;
        }
        this.mSubMenuUpdateCallback = null;
    }

    public final void configureMainPopupMotionGeometry() {
        configureMainMenuPopupPathGeometryOnDomain();
        if (this.mMainMenuTouchAnimator == null) {
            return;
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        float[] fArr = this.mMainMenuPopupPathOffset;
        cOUIPopupTouchMotionAnimator.setPopupPathAnchorOffset(fArr[0], fArr[1]);
    }

    public void destroy() {
        stopAllAnimation();
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator != null) {
            cOUIPopupTouchMotionAnimator.addEndListener(null);
            this.mMainMenuTouchAnimator.dispose();
            this.mMainMenuTouchAnimator = null;
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator2 = this.mSubMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator2 != null) {
            cOUIPopupTouchMotionAnimator2.addEndListener(null);
            this.mSubMenuTouchAnimator.dispose();
            this.mSubMenuTouchAnimator = null;
        }
        this.mMainMenuUpdateCallback = null;
        this.mSubMenuUpdateCallback = null;
        this.mMenuTransitionUnit = null;
        this.mSubMenuTransitionUnit = null;
        this.mParentInfluenceUnit = null;
        this.mRootView = null;
        this.mMainMenuRoot = null;
        this.mSubMenuRoot = null;
        this.mMenuStateChangedListener = null;
    }

    public void detach() {
    }

    public abstract void ensureSubMenuCallback();

    public final void freezeSubMenuAndMenuTransitionUnits() {
        SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit != null) {
            subMenuTransitionUnit.cancel();
        }
        MenuTransitionUnit menuTransitionUnit = this.mMenuTransitionUnit;
        if (menuTransitionUnit != null) {
            menuTransitionUnit.cancel();
        }
    }

    public abstract boolean isSubMenuVisible();

    public void prepareMainMenuEnterBeforePopupAnim(boolean z2) {
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator;
        View childAt;
        if (this.mMainMenuRoot == null) {
            return;
        }
        if (!z2 || (cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator) == null) {
            resetMainMenuOpaqueForEnter();
            return;
        }
        boolean zIsUseRt = cOUIPopupTouchMotionAnimator.isUseRt();
        setPopupMenuAlpha(this.mRootView, 0.0f, zIsUseRt);
        setPopupMenuAlpha(this.mMainMenuRoot, 0.0f, zIsUseRt);
        setPopupMenuScale(this.mMainMenuRoot, 0.0f, 0.0f, zIsUseRt);
        View view = this.mMainMenuRoot;
        if (!(view instanceof ViewGroup) || (childAt = ((ViewGroup) view).getChildAt(0)) == null) {
            return;
        }
        setPopupMenuAlpha(childAt, 0.0f, zIsUseRt);
    }

    public void prepareSubMenuEnterBeforeAnim(boolean z2, float f3, float f4) {
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator;
        if (this.mSubMenuRoot == null) {
            return;
        }
        if (!z2 || (cOUIPopupTouchMotionAnimator = this.mSubMenuTouchAnimator) == null) {
            resetSubMenuOpaqueForEnter();
            return;
        }
        boolean zIsUseRt = cOUIPopupTouchMotionAnimator.isUseRt();
        setPopupMenuAlpha(this.mSubMenuRoot, f3, zIsUseRt);
        setPopupMenuScale(this.mSubMenuRoot, f4, f4, zIsUseRt);
        if (this.mSubMenuRoot.getVisibility() == 8) {
            this.mSubMenuRoot.setVisibility(4);
        }
    }

    public final void resetGlobalCaches() {
        this.mLastGlobalTranslateX = 0.0f;
        this.mLastGlobalTranslateY = 0.0f;
        this.mLastGlobalScaleX = 1.0f;
        this.mLastGlobalScaleY = 1.0f;
        this.mRootViewAlpha = 1.0f;
    }

    public void resetMainMenuOpaqueForEnter() {
        View childAt;
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        boolean z2 = cOUIPopupTouchMotionAnimator != null && cOUIPopupTouchMotionAnimator.isUseRt();
        setPopupMenuAlpha(this.mRootView, 1.0f, z2);
        setPopupMenuAlpha(this.mMainMenuRoot, 1.0f, z2);
        View view = this.mMainMenuRoot;
        if (!(view instanceof ViewGroup) || (childAt = ((ViewGroup) view).getChildAt(0)) == null) {
            return;
        }
        setPopupMenuAlpha(childAt, 1.0f, z2);
    }

    public void resetSubMenuOpaqueForEnter() {
        if (this.mSubMenuRoot == null) {
            return;
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mSubMenuTouchAnimator;
        boolean z2 = cOUIPopupTouchMotionAnimator != null && cOUIPopupTouchMotionAnimator.isUseRt();
        setPopupMenuAlpha(this.mSubMenuRoot, 1.0f, z2);
        setPopupMenuScale(this.mSubMenuRoot, 1.0f, 1.0f, z2);
    }

    public void setDomain(PopupMenuDomain popupMenuDomain) {
        this.mDomain = popupMenuDomain;
    }

    public void setMainMenuAnimTargetSize(int i3, int i4) {
        this.mOverrideMainMenuTargetWidth = Math.max(0, i3);
        this.mOverrideMainMenuTargetHeight = Math.max(0, i4);
    }

    public void setMainMenuSize(int i3, int i4) {
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator != null) {
            cOUIPopupTouchMotionAnimator.onSizeChange(i3, i4);
            return;
        }
        COUILog.w(TAG, "[PopupLifecycle] setMainMenuSize skipped, animator is null, size=" + i3 + "x" + i4);
    }

    public void setMainMenuView(RoundFrameLayout roundFrameLayout) {
        this.mMainMenuRoot = roundFrameLayout;
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator != null) {
            cOUIPopupTouchMotionAnimator.cancel();
            this.mMainMenuTouchAnimator.clearPopupKitAnimationResources();
            this.mMainMenuTouchAnimator.dispose();
        }
        this.mMainMenuTouchAnimator = makeAnimator(roundFrameLayout);
        syncRtTargetsToKitController();
        this.mMainMenuUpdateCallback = null;
        this.mMenuTransitionUnit = null;
    }

    public void setMenuRootView(View view) {
        this.mRootView = view;
        syncRtTargetsToKitController();
    }

    public void setOnSubMenuStateChangedListener(OnMenuStateChangedListener onMenuStateChangedListener) {
        this.mMenuStateChangedListener = onMenuStateChangedListener;
    }

    public void setSubMenuSize(int i3, int i4) {
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mSubMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator != null) {
            cOUIPopupTouchMotionAnimator.onSizeChange(i3, i4);
            return;
        }
        COUILog.w(TAG, "[PopupLifecycle] setSubMenuSize skipped, animator is null, size=" + i3 + "x" + i4);
    }

    public void setSubMenuView(RoundFrameLayout roundFrameLayout) {
        clearSubMenuUnits();
        this.mSubMenuRoot = roundFrameLayout;
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mSubMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator != null) {
            cOUIPopupTouchMotionAnimator.cancel();
            this.mSubMenuTouchAnimator.clearPopupKitAnimationResources();
            this.mSubMenuTouchAnimator.dispose();
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimatorMakeSubMenuAnimator = makeSubMenuAnimator(roundFrameLayout);
        this.mSubMenuTouchAnimator = cOUIPopupTouchMotionAnimatorMakeSubMenuAnimator;
        if (cOUIPopupTouchMotionAnimatorMakeSubMenuAnimator == null || roundFrameLayout == null) {
            return;
        }
        ParentInfluenceUnit parentInfluenceUnit = new ParentInfluenceUnit();
        this.mParentInfluenceUnit = parentInfluenceUnit;
        this.mSubMenuTouchAnimator.addMotionUnit(parentInfluenceUnit);
        SubMenuTransitionUnit subMenuTransitionUnit = new SubMenuTransitionUnit();
        this.mSubMenuTransitionUnit = subMenuTransitionUnit;
        this.mSubMenuTouchAnimator.addMotionUnit(subMenuTransitionUnit);
        ensureSubMenuCallback();
    }

    public void showMainMenuRootIfNeeded() {
        View view = this.mMainMenuRoot;
        if (view == null || view.getVisibility() != 8) {
            return;
        }
        this.mMainMenuRoot.setVisibility(0);
    }

    public void startMainMenuEnter(boolean z2) {
    }

    public void startMainMenuExit(boolean z2) {
    }

    public final void startSubMenuEnter() {
        startSubMenuEnter(true);
    }

    public final void startSubMenuExit() {
        startSubMenuExit(true);
    }

    public void stopAllAnimation() {
        SubMenuTransitionUnit subMenuTransitionUnit = this.mSubMenuTransitionUnit;
        if (subMenuTransitionUnit != null) {
            subMenuTransitionUnit.cancel();
            this.mSubMenuTransitionUnit.reset(false);
        }
        ParentInfluenceUnit parentInfluenceUnit = this.mParentInfluenceUnit;
        if (parentInfluenceUnit != null) {
            parentInfluenceUnit.reset(false);
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator != null) {
            cOUIPopupTouchMotionAnimator.addEndListener(null);
            this.mMainMenuTouchAnimator.cancel();
            this.mMainMenuTouchAnimator.clearPopupKitAnimationResources();
        }
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator2 = this.mSubMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator2 != null) {
            cOUIPopupTouchMotionAnimator2.addEndListener(null);
            this.mSubMenuTouchAnimator.cancel();
            this.mSubMenuTouchAnimator.clearPopupKitAnimationResources();
        }
        MenuTransitionUnit menuTransitionUnit = this.mMenuTransitionUnit;
        if (menuTransitionUnit != null) {
            menuTransitionUnit.cancel();
            this.mMenuTransitionUnit.reset(false);
        }
        resetGlobalCaches();
    }

    public void syncMainMenuAnimPivot() {
    }

    public final void syncMainMenuEnterPivotFromDomain() {
        PopupMenuDomain popupMenuDomain;
        configureMainMenuPopupPathGeometryOnDomain();
        View view = this.mMainMenuRoot;
        if (view == null || (popupMenuDomain = this.mDomain) == null) {
            return;
        }
        view.setPivotX(popupMenuDomain.getMainMenuEnterPivotX());
        this.mMainMenuRoot.setPivotY(this.mDomain.getMainMenuEnterPivotY());
    }

    public final void startMainMenuEnter() {
        startMainMenuEnter(true);
    }

    public final void startMainMenuExit() {
        startMainMenuExit(true);
    }

    public void startSubMenuEnter(boolean z2) {
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mMainMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator == null || cOUIPopupTouchMotionAnimator == null) {
            return;
        }
        this.mMainMenuTouchAnimator.resetTouchOnly(true);
    }

    public void startSubMenuExit(boolean z2) {
        COUIPopupTouchMotionAnimator cOUIPopupTouchMotionAnimator = this.mSubMenuTouchAnimator;
        if (cOUIPopupTouchMotionAnimator != null) {
            cOUIPopupTouchMotionAnimator.resetTouchOnly(z2);
        }
        View view = this.mMainMenuRoot;
        if (view == null || this.mSubMenuRoot == null) {
            return;
        }
        this.mSubMenuTranslateEndX = view.getTranslationX();
        this.mSubMenuScaleStartX = this.mSubMenuRoot.getScaleX();
        this.mSubMenuScaleStartY = this.mSubMenuRoot.getScaleY();
    }
}