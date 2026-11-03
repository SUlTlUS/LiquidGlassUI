package com.coui.appcompat.poplist;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import com.coui.appcompat.anim.param.PopupGravityMotionParams;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.Domain;

/* JADX INFO: loaded from: classes7.dex */
class PopupMenuDomain extends Domain {
    private static final boolean COUI_DEBUG;
    static final int FORCED_HORIZONTAL_END = 2;
    static final int FORCED_HORIZONTAL_NONE = 0;
    static final int FORCED_HORIZONTAL_START = 1;
    static final int HORIZONTAL_ALIGN_CENTER = 1;
    static final int HORIZONTAL_ALIGN_LEFT = 0;
    static final int HORIZONTAL_ALIGN_RIGHT = 2;
    static final int MAIN_MENU_POPUP_SIDE_ABOVE = 1;
    static final int MAIN_MENU_POPUP_SIDE_AUTO = 0;
    static final int MAIN_MENU_POPUP_SIDE_BELOW = 2;
    static final int MAIN_MENU_POPUP_SIDE_LEFT = 3;
    static final int MAIN_MENU_POPUP_SIDE_RIGHT = 4;
    private static final float PATH_ANCHOR_EDGE_CLAMP_EPS = 0.5f;
    static final int PATH_ANCHOR_MODE_AUTO = 1;
    static final int PATH_ANCHOR_MODE_CENTER = 0;
    private static final int PATH_ANCHOR_MODE_OVERRIDE_NONE = -1;
    static final int PATH_ANCHOR_SLOT_CENTER = 1;
    static final int PATH_ANCHOR_SLOT_END = 2;
    static final int PATH_ANCHOR_SLOT_OVERRIDE_NONE = -1;
    static final int PATH_ANCHOR_SLOT_START = 0;
    private static final String TAG = "PopupMenuDomain";
    View mAnchorView;
    boolean mMainMenuRotationWrapperActive;
    int mMainMenuRotationWrapperLeft;
    int mMainMenuRotationWrapperTop;
    Rect mWindow = new Rect();
    Rect mAnchor = new Rect();
    Rect mMainMenu = new Rect();
    Rect mMainMenuRelocated = new Rect();
    Rect mSubMenu = new Rect();
    Rect mPopup = new Rect();
    Rect mSubMenuAnchor = new Rect();
    Rect mAnchorOutsets = new Rect();
    Rect mWindowBarriers = new Rect();
    private final Rect mAvailableClipBounds = new Rect();
    int mGlobalOffsetX = 0;
    int mGlobalOffsetY = 0;
    boolean mSubMenuAnchorIsFirstItem = false;
    boolean mMainMenuCenterAlign = false;
    int mMainMenuPopupSide = 0;
    int mPathAnchorMode = 0;
    int mPathAnchorSlotOverride = -1;
    int mResolvedPathAnchorSlot = 1;
    float mLastPathAnchorX = 0.0f;
    float mLastPathAnchorY = 0.0f;
    private int mPathAnchorModeOverride = -1;
    private float mPathAnchorAutoPaddingOverride = Float.NaN;
    int mForcedHorizontalEdge = 0;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuDomain$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$coui$appcompat$poplist$MenuRotationHelper$PhysicalHAlign;

        static {
            int[] iArr = new int[MenuRotationHelper.PhysicalHAlign.values().length];
            $SwitchMap$com$coui$appcompat$poplist$MenuRotationHelper$PhysicalHAlign = iArr;
            try {
                iArr[MenuRotationHelper.PhysicalHAlign.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$coui$appcompat$poplist$MenuRotationHelper$PhysicalHAlign[MenuRotationHelper.PhysicalHAlign.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$coui$appcompat$poplist$MenuRotationHelper$PhysicalHAlign[MenuRotationHelper.PhysicalHAlign.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    private float effectiveAutoPadding(int i3, int i4, float f3, int i5) {
        if (i3 != 1 || i4 == 1 || f3 <= 0.0f) {
            return 0.0f;
        }
        float fMin = (((i5 == 3 || i5 == 4) ? Math.min(this.mAnchor.height(), this.mMainMenu.height()) : Math.min(this.mAnchor.width(), this.mMainMenu.width())) * 0.5f) - 0.5f;
        if (fMin <= 0.0f) {
            return 0.0f;
        }
        return Math.min(f3, fMin);
    }

    private int inferPathAnchorSlotFromGeometry(int i3) {
        if (i3 == 3 || i3 == 4) {
            if (this.mMainMenu.centerY() < this.mAnchor.centerY()) {
                return 0;
            }
            return this.mMainMenu.centerY() > this.mAnchor.centerY() ? 2 : 1;
        }
        int mainMenuHorizontalAlignment = getMainMenuHorizontalAlignment();
        if (mainMenuHorizontalAlignment != 0) {
            return mainMenuHorizontalAlignment != 2 ? 1 : 2;
        }
        return 0;
    }

    private boolean isLayoutRtl() {
        View view = this.mAnchorView;
        return view != null && view.getLayoutDirection() == 1;
    }

    private static int normalizePathAnchorSlot(int i3) {
        if (i3 == 0 || i3 == 1 || i3 == 2) {
            return i3;
        }
        return 1;
    }

    public static int resolvePathAnchorMode(float f3) {
        return f3 >= 1.0f ? 1 : 0;
    }

    private int resolvePathAnchorModeWithOverride(float f3) {
        int i3 = this.mPathAnchorModeOverride;
        return i3 == -1 ? resolvePathAnchorMode(f3) : i3;
    }

    public static int resolvePathAnchorSlotOverride(float f3) {
        int iRound = Math.round(f3);
        if (iRound == 0 || iRound == 1 || iRound == 2) {
            return iRound;
        }
        return -1;
    }

    private float resolveTangentCoordOnHorizontalEdge(int i3, float f3, boolean z2) {
        Rect rect = this.mAnchor;
        float f4 = z2 ? rect.right : rect.left;
        float f5 = z2 ? this.mAnchor.left : this.mAnchor.right;
        return Math.max(Math.min(f4, f5), Math.min(Math.max(f4, f5), tangentCoordForSlot(i3, f4, this.mAnchor.centerX(), f5, f3)));
    }

    private float resolveTangentCoordOnVerticalEdge(int i3, float f3, boolean z2) {
        Rect rect = this.mAnchor;
        float f4 = rect.top;
        float f5 = rect.bottom;
        return Math.max(Math.min(f4, f5), Math.min(Math.max(f4, f5), tangentCoordForSlot(i3, f4, rect.centerY(), f5, f3)));
    }

    private static float tangentCoordForSlot(int i3, float f3, float f4, float f5, float f6) {
        if (i3 != 0) {
            return i3 != 2 ? f4 : f5 - f6;
        }
        return f3 + f6;
    }

    private boolean useAutoCornerPivotX() {
        return (this.mPathAnchorMode != 1 || this.mResolvedPathAnchorSlot == 1 || this.mMainMenu.isEmpty()) ? false : true;
    }

    private boolean useAutoCornerPivotY() {
        int resolvedMainMenuPopupSide = getResolvedMainMenuPopupSide();
        if (this.mPathAnchorMode != 1 || this.mResolvedPathAnchorSlot == 1 || this.mMainMenu.isEmpty()) {
            return false;
        }
        return resolvedMainMenuPopupSide == 3 || resolvedMainMenuPopupSide == 4;
    }

    public void clearMainMenuPlacementState() {
        this.mForcedHorizontalEdge = 0;
        this.mMainMenuPopupSide = 0;
        clearMainMenuRotationLayout();
    }

    public void clearMainMenuRotationLayout() {
        this.mMainMenuRotationWrapperActive = false;
        this.mMainMenuRotationWrapperLeft = 0;
        this.mMainMenuRotationWrapperTop = 0;
    }

    public boolean computeMainMenuClippedToAvailable(Rect rect) {
        if (this.mMainMenu.isEmpty()) {
            return false;
        }
        rect.set(this.mMainMenu);
        getAvailableRect(this.mAvailableClipBounds);
        return rect.intersect(this.mAvailableClipBounds);
    }

    public void computeMainMenuPopupPathAnchorOffset(float f3, float[] fArr) {
        computeMainMenuPopupPathAnchorOffset(f3, 0, 0.0f, -1, fArr);
    }

    public void dump() {
        Log.d(TAG, "mWindow = " + this.mWindow + " mAnchor = " + this.mAnchor + " mAnchorOutsets = " + this.mAnchorOutsets + " mWindowBarriers = " + this.mWindowBarriers + " mMainMenu = " + this.mMainMenu + " mMainMenuRelocated = " + this.mMainMenuRelocated + " mSubMenu = " + this.mSubMenu + " mSubMenuAnchor = " + this.mSubMenuAnchor + " mGlobalOffsetX = " + this.mGlobalOffsetX + " mGlobalOffsetY = " + this.mGlobalOffsetY);
    }

    public void getAnchorRealRect(Rect rect) {
        Rect rect2 = this.mAnchor;
        int i3 = rect2.left;
        Rect rect3 = this.mAnchorOutsets;
        rect.set(i3 - rect3.left, rect2.top - rect3.top, rect2.right + rect3.right, rect2.bottom + rect3.bottom);
    }

    public void getAvailableRect(Rect rect) {
        Rect rect2 = this.mWindow;
        int i3 = rect2.left;
        Rect rect3 = this.mWindowBarriers;
        rect.set(i3 + rect3.left, rect2.top + rect3.top, rect2.right - rect3.right, rect2.bottom - rect3.bottom);
        if (COUI_DEBUG) {
            Log.d(TAG, "PopupMenuDomain getAvailableRect mWindow.left " + this.mWindow.left + " mWindowBarriers.left " + this.mWindowBarriers.left + " mWindow.top " + this.mWindow.top + " mWindowBarriers.top " + this.mWindowBarriers.top + " mWindow.right " + this.mWindow.right + " mWindowBarriers.right " + this.mWindowBarriers.right + " mWindow.bottom " + this.mWindow.bottom + " mWindowBarriers.bottom " + this.mWindowBarriers.bottom);
        }
    }

    public int getAvailableRectHeight() {
        Rect rect = this.mWindow;
        int i3 = rect.bottom;
        Rect rect2 = this.mWindowBarriers;
        return (i3 - rect2.bottom) - (rect.top + rect2.top);
    }

    public int getAvailableRectWidth() {
        Rect rect = this.mWindow;
        int i3 = rect.right;
        Rect rect2 = this.mWindowBarriers;
        return (i3 - rect2.right) - (rect.left + rect2.left);
    }

    public int getMainMenuEnterPivotX() {
        if (this.mMainMenuCenterAlign) {
            return this.mMainMenu.centerX() - this.mMainMenu.left;
        }
        int i3 = this.mForcedHorizontalEdge;
        if (i3 == 1 || i3 == 2) {
            return MenuRotationHelper.computeForcedMainMenuEnterPivotX(this.mForcedHorizontalEdge, this.mMainMenu.width(), isLayoutRtl());
        }
        return useAutoCornerPivotX() ? Math.round(Math.min(Math.max(this.mLastPathAnchorX - this.mMainMenu.left, 0.0f), this.mMainMenu.width())) : Math.min(Math.max(this.mAnchor.centerX() - this.mMainMenu.left, 0), this.mMainMenu.width());
    }

    public int getMainMenuEnterPivotY() {
        if (this.mMainMenuCenterAlign) {
            return this.mMainMenu.centerY() - this.mMainMenu.top;
        }
        if (useAutoCornerPivotY()) {
            return Math.round(Math.min(Math.max(this.mLastPathAnchorY - this.mMainMenu.top, 0.0f), this.mMainMenu.height()));
        }
        if (this.mMainMenu.centerY() > this.mAnchor.centerY()) {
            return 0;
        }
        return this.mMainMenu.height();
    }

    public int getMainMenuHorizontalAlignment() {
        int i3 = AnonymousClass1.$SwitchMap$com$coui$appcompat$poplist$MenuRotationHelper$PhysicalHAlign[MenuRotationHelper.inferMainMenuPhysicalHAlign(this).ordinal()];
        if (i3 != 1) {
            return i3 != 2 ? 1 : 2;
        }
        return 0;
    }

    public int getResolvedMainMenuPopupSide() {
        int i3 = this.mMainMenuPopupSide;
        if (i3 != 0) {
            return i3;
        }
        return isMainMenuBelowAnchor() ? 2 : 1;
    }

    public int getSubMenuEnterPivotX() {
        Rect rect = this.mSubMenu;
        if (rect.left > this.mMainMenu.left) {
            return 0;
        }
        return rect.width();
    }

    public int getSubMenuEnterPivotY() {
        return this.mSubMenuAnchor.centerY() - this.mSubMenu.top;
    }

    public boolean isMainMenuBelowAnchor() {
        return !this.mMainMenu.isEmpty() && this.mMainMenu.centerY() > this.mAnchor.centerY();
    }

    public void reset() {
        this.mWindow.setEmpty();
        this.mAnchor.setEmpty();
        this.mMainMenu.setEmpty();
        this.mSubMenu.setEmpty();
        this.mPopup.setEmpty();
        this.mAnchorOutsets.setEmpty();
        this.mWindowBarriers.setEmpty();
        this.mMainMenuRelocated.setEmpty();
        this.mSubMenuAnchor.setEmpty();
        clearMainMenuPlacementState();
    }

    public float resolvePathAnchorAutoPadding(float f3) {
        return Float.isNaN(this.mPathAnchorAutoPaddingOverride) ? f3 : this.mPathAnchorAutoPaddingOverride;
    }

    public int resolvePathAnchorSlot(int i3, int i4, int i5) {
        if (i4 != -1) {
            return normalizePathAnchorSlot(i4);
        }
        if (i3 == 0) {
            return 1;
        }
        return inferPathAnchorSlotFromGeometry(i5);
    }

    public void setMainMenuPopupSide(int i3) {
        if (i3 == 0 || i3 == 1 || i3 == 2 || i3 == 3 || i3 == 4) {
            this.mMainMenuPopupSide = i3;
            return;
        }
        COUILog.w(TAG, "setMainMenuPopupSide: unsupported side " + i3 + "; ignored");
    }

    public void setMainMenuRotationLayout(int i3, int i4) {
        this.mMainMenuRotationWrapperActive = true;
        this.mMainMenuRotationWrapperLeft = i3;
        this.mMainMenuRotationWrapperTop = i4;
    }

    public void setPathAnchorAutoModeEnabled(boolean z2) {
        this.mPathAnchorModeOverride = z2 ? 1 : 0;
    }

    public void setPathAnchorAutoPadding(float f3) {
        float fMax = 0.0f;
        if (!Float.isNaN(f3) && !Float.isInfinite(f3)) {
            fMax = Math.max(0.0f, f3);
        }
        this.mPathAnchorAutoPaddingOverride = fMax;
    }

    public void computeMainMenuPopupPathAnchorOffset(PopupGravityMotionParams popupGravityMotionParams, float[] fArr) {
        if (popupGravityMotionParams == null) {
            computeMainMenuPopupPathAnchorOffset(0.0f, resolvePathAnchorModeWithOverride(0.0f), resolvePathAnchorAutoPadding(0.0f), -1, fArr);
        } else {
            computeMainMenuPopupPathAnchorOffset(popupGravityMotionParams.getPathAnchorEdgeOffsetY(), resolvePathAnchorModeWithOverride(popupGravityMotionParams.getPathAnchorMode()), resolvePathAnchorAutoPadding(popupGravityMotionParams.getPathAnchorAutoPadding()), resolvePathAnchorSlotOverride(popupGravityMotionParams.getPathAnchorSlotOverride()), fArr);
        }
    }

    public void computeMainMenuPopupPathAnchorOffset(float f3, int i3, float f4, int i4, float[] fArr) {
        float fResolveTangentCoordOnHorizontalEdge;
        float fResolveTangentCoordOnVerticalEdge;
        this.mPathAnchorMode = i3;
        this.mPathAnchorSlotOverride = i4;
        int resolvedMainMenuPopupSide = getResolvedMainMenuPopupSide();
        int iResolvePathAnchorSlot = resolvePathAnchorSlot(i3, i4, resolvedMainMenuPopupSide);
        this.mResolvedPathAnchorSlot = iResolvePathAnchorSlot;
        float fEffectiveAutoPadding = effectiveAutoPadding(i3, iResolvePathAnchorSlot, f4, resolvedMainMenuPopupSide);
        boolean zIsLayoutRtl = isLayoutRtl();
        if (resolvedMainMenuPopupSide == 3) {
            fResolveTangentCoordOnHorizontalEdge = this.mAnchor.left - f3;
            fResolveTangentCoordOnVerticalEdge = resolveTangentCoordOnVerticalEdge(iResolvePathAnchorSlot, fEffectiveAutoPadding, zIsLayoutRtl);
        } else if (resolvedMainMenuPopupSide == 4) {
            fResolveTangentCoordOnHorizontalEdge = this.mAnchor.right + f3;
            fResolveTangentCoordOnVerticalEdge = resolveTangentCoordOnVerticalEdge(iResolvePathAnchorSlot, fEffectiveAutoPadding, zIsLayoutRtl);
        } else if (resolvedMainMenuPopupSide == 1) {
            fResolveTangentCoordOnHorizontalEdge = resolveTangentCoordOnHorizontalEdge(iResolvePathAnchorSlot, fEffectiveAutoPadding, zIsLayoutRtl);
            fResolveTangentCoordOnVerticalEdge = this.mAnchor.top - f3;
        } else {
            fResolveTangentCoordOnHorizontalEdge = resolveTangentCoordOnHorizontalEdge(iResolvePathAnchorSlot, fEffectiveAutoPadding, zIsLayoutRtl);
            fResolveTangentCoordOnVerticalEdge = f3 + this.mAnchor.bottom;
        }
        this.mLastPathAnchorX = fResolveTangentCoordOnHorizontalEdge;
        this.mLastPathAnchorY = fResolveTangentCoordOnVerticalEdge;
        fArr[0] = fResolveTangentCoordOnHorizontalEdge - this.mMainMenu.centerX();
        fArr[1] = fResolveTangentCoordOnVerticalEdge - this.mMainMenu.centerY();
    }
}