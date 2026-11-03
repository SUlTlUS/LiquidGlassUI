package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import com.coui.appcompat.log.COUILog;
import com.support.poplist.R;
import java.util.Arrays;

/* JADX INFO: loaded from: classes7.dex */
class RotatingPopupMenuLocateHelper extends PopupMenuLocateHelper {
    private static final boolean COUI_DEBUG;
    private static final String TAG = "RotatingPopupLocate";
    private final float[] mAnchorMapCoords;
    private final Rect mAnchorRealBounds;
    private final Matrix mAnchorToGlobalMatrix;
    private final int[] mClampDxDy;
    private final Rect mDefaultAnchorGapOutsets;
    private final Rect mDesiredDisplayBounds;
    private final Matrix mGlobalToWindowMatrix;
    private boolean mHasMainMenuMeasureCaps;
    private int mMainMenuMeasureMaxHeight;
    private int mMainMenuMeasureMaxWidth;
    private int mMainMenuRotationContentHeight;
    private int mMainMenuRotationContentWidth;
    private MainMenuPlacementSpec mPlacementSpec;
    private final MenuRotationHelper.RotationLayoutOut mRotationLayoutOut;
    private final Matrix mWindowToGlobalMatrix;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.RotatingPopupMenuLocateHelper$1, reason: invalid class name */
    public class AnonymousClass1 extends PopupMenuLocateHelper.DefaultPopupMenuConfigRule {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return -1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            return RotatingPopupMenuLocateHelper.this.mAnchorBounds;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return RotatingPopupMenuLocateHelper.e(RotatingPopupMenuLocateHelper.this);
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 1;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.RotatingPopupMenuLocateHelper$2, reason: invalid class name */
    public class AnonymousClass2 implements PopupMenuControlRule {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            if (RotatingPopupMenuLocateHelper.shouldApplyCenterAlignY(RotatingPopupMenuLocateHelper.f(RotatingPopupMenuLocateHelper.this))) {
                int iCenterY = popupMenuDomain.mAnchor.centerY();
                RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper = RotatingPopupMenuLocateHelper.this;
                int i3 = rotatingPopupMenuLocateHelper.mMainMenuHeight;
                int iMin = iCenterY - (i3 / 2);
                Rect rect = rotatingPopupMenuLocateHelper.mAvailableBounds;
                int i4 = rect.bottom;
                int i5 = rect.top;
                if (i4 - i5 >= i3) {
                    int iMax = Math.max(iMin, i5);
                    RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper2 = RotatingPopupMenuLocateHelper.this;
                    iMin = Math.min(iMax, rotatingPopupMenuLocateHelper2.mAvailableBounds.bottom - rotatingPopupMenuLocateHelper2.mMainMenuHeight);
                }
                Rect rect2 = popupMenuDomain.mMainMenu;
                rect2.set(rect2.left, iMin, rect2.right, RotatingPopupMenuLocateHelper.this.mMainMenuHeight + iMin);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.RotatingPopupMenuLocateHelper$3, reason: invalid class name */
    public class AnonymousClass3 implements PopupMenuControlRule {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            int iG = RotatingPopupMenuLocateHelper.g(RotatingPopupMenuLocateHelper.this);
            Rect rect = popupMenuDomain.mMainMenu;
            rect.set(iG, rect.top, RotatingPopupMenuLocateHelper.this.mMainMenuWidth + iG, rect.bottom);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.RotatingPopupMenuLocateHelper$4, reason: invalid class name */
    public class AnonymousClass4 implements PopupMenuControlRule {
        int mPosY = 0;

        public AnonymousClass4() {
        }

        private void locateMainMenuY(Rect rect) {
            int iMax = Math.max(rect.bottom, RotatingPopupMenuLocateHelper.this.mAvailableBounds.top);
            int iMin = Math.min(rect.top, RotatingPopupMenuLocateHelper.this.mAvailableBounds.bottom);
            int side = RotatingPopupMenuLocateHelper.f(RotatingPopupMenuLocateHelper.this).getSide();
            if (side == 2) {
                this.mPosY = iMax;
                RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper = RotatingPopupMenuLocateHelper.this;
                Rect rect2 = rotatingPopupMenuLocateHelper.mAvailableBounds;
                this.mPosY = Math.max(rect2.top, Math.min(iMax, rect2.bottom - rotatingPopupMenuLocateHelper.mMainMenuHeight));
            } else if (side == 1) {
                RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper2 = RotatingPopupMenuLocateHelper.this;
                int i3 = rotatingPopupMenuLocateHelper2.mMainMenuHeight;
                int i4 = iMin - i3;
                this.mPosY = i4;
                Rect rect3 = rotatingPopupMenuLocateHelper2.mAvailableBounds;
                this.mPosY = Math.max(rect3.top, Math.min(i4, rect3.bottom - i3));
            } else if (RotatingPopupMenuLocateHelper.this.mLocateFromAboveAnchorToBelow) {
                if (!tryLocateAboveAnchor(iMin)) {
                    tryLocateBelowAnchor(iMax);
                }
            } else if (!tryLocateBelowAnchor(iMax)) {
                tryLocateAboveAnchor(iMin);
            }
            if (RotatingPopupMenuLocateHelper.b()) {
                Log.d(RotatingPopupMenuLocateHelper.TAG, "mMainMenuLocateYRule anchorBounds [left " + rect.left + " top " + rect.top + " right " + rect.right + " bottom " + rect.bottom + "] mMainMenuHeight " + RotatingPopupMenuLocateHelper.this.mMainMenuHeight + " mAvailableBounds [left " + RotatingPopupMenuLocateHelper.this.mAvailableBounds.left + " top " + RotatingPopupMenuLocateHelper.this.mAvailableBounds.top + " right " + RotatingPopupMenuLocateHelper.this.mAvailableBounds.right + " bottom " + RotatingPopupMenuLocateHelper.this.mAvailableBounds.bottom + "] result y = " + this.mPosY);
            }
        }

        private boolean tryLocateAboveAnchor(int i3) {
            RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper = RotatingPopupMenuLocateHelper.this;
            int i4 = i3 - rotatingPopupMenuLocateHelper.mAvailableBounds.top;
            int i5 = rotatingPopupMenuLocateHelper.mMainMenuHeight;
            if (i4 < i5) {
                return false;
            }
            this.mPosY = i3 - i5;
            return true;
        }

        private boolean tryLocateBelowAnchor(int i3) {
            RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper = RotatingPopupMenuLocateHelper.this;
            if (rotatingPopupMenuLocateHelper.mAvailableBounds.bottom - i3 < rotatingPopupMenuLocateHelper.mMainMenuHeight) {
                return false;
            }
            this.mPosY = i3;
            return true;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            popupMenuDomain.getAnchorRealRect(RotatingPopupMenuLocateHelper.d(RotatingPopupMenuLocateHelper.this));
            RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper = RotatingPopupMenuLocateHelper.this;
            this.mPosY = rotatingPopupMenuLocateHelper.mAvailableBounds.top;
            locateMainMenuY(RotatingPopupMenuLocateHelper.d(rotatingPopupMenuLocateHelper));
            Rect rect = popupMenuDomain.mMainMenu;
            int i3 = rect.left;
            int i4 = this.mPosY;
            rect.set(i3, i4, rect.right, RotatingPopupMenuLocateHelper.this.mMainMenuHeight + i4);
        }
    }

    public static final class LocateMainMenuAnchorXResult {
        final int mForcedHorizontalEdge;
        final int mPosX;

        public LocateMainMenuAnchorXResult(int i3, int i4) {
            this.mPosX = i3;
            this.mForcedHorizontalEdge = i4;
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public RotatingPopupMenuLocateHelper(Context context) {
        super(context, true);
        this.mAnchorToGlobalMatrix = new Matrix();
        this.mWindowToGlobalMatrix = new Matrix();
        this.mGlobalToWindowMatrix = new Matrix();
        this.mAnchorMapCoords = new float[8];
        this.mAnchorRealBounds = new Rect();
        this.mDesiredDisplayBounds = new Rect();
        this.mClampDxDy = new int[2];
        this.mRotationLayoutOut = new MenuRotationHelper.RotationLayoutOut();
        this.mPlacementSpec = MainMenuPlacementSpec.DEFAULT;
        this.mMainMenuMeasureMaxWidth = Integer.MAX_VALUE;
        this.mMainMenuMeasureMaxHeight = Integer.MAX_VALUE;
        Rect rect = new Rect();
        this.mDefaultAnchorGapOutsets = rect;
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_default_vertical_gap_to_anchor);
        rect.set(0, dimensionPixelOffset, 0, dimensionPixelOffset);
        setupRules();
    }

    private void applyMainMenuRotationLayout(MenuRotationHelper.RotationLayoutOut rotationLayoutOut) {
        this.mDomain.mMainMenu.set(rotationLayoutOut.mScreenBounds);
        this.mMainMenuWidth = rotationLayoutOut.mScreenBounds.width();
        this.mMainMenuHeight = rotationLayoutOut.mScreenBounds.height();
        this.mDomain.setMainMenuRotationLayout(rotationLayoutOut.mWrapperLeft, rotationLayoutOut.mWrapperTop);
    }

    public static /* bridge */ /* synthetic */ boolean b() {
        return COUI_DEBUG;
    }

    private void clearMainMenuRotationContentSize() {
        this.mMainMenuRotationContentWidth = 0;
        this.mMainMenuRotationContentHeight = 0;
    }

    public static LocateMainMenuAnchorXResult computeLocateMainMenuAnchorX(Rect rect, MainMenuPlacementSpec mainMenuPlacementSpec, boolean z2, int i3, Rect rect2, boolean z3) {
        int i4 = 0;
        if (z2) {
            return new LocateMainMenuAnchorXResult(locateMainMenuXDefault(rect, i3, rect2), 0);
        }
        int alignment = mainMenuPlacementSpec.getAlignment();
        if (alignment == 0 || mainMenuPlacementSpec.isSideForced()) {
            return new LocateMainMenuAnchorXResult(locateMainMenuXDefault(rect, i3, rect2), 0);
        }
        int iForcedMenuLeft = MenuRotationHelper.forcedMenuLeft(rect, i3, alignment, z3);
        int i5 = alignment != 1 ? 2 : 1;
        int i6 = rect2.left;
        if (iForcedMenuLeft < i6) {
            iForcedMenuLeft = i6;
        }
        int i7 = iForcedMenuLeft + i3;
        int i8 = rect2.right;
        if (i7 > i8) {
            iForcedMenuLeft = i8 - i3;
        }
        if (iForcedMenuLeft < i6) {
            iForcedMenuLeft = rect2.centerX() - (i3 / 2);
        } else {
            i4 = i5;
        }
        return new LocateMainMenuAnchorXResult(iForcedMenuLeft, i4);
    }

    public static int computeLocateMainMenuY(Rect rect, MainMenuPlacementSpec mainMenuPlacementSpec, boolean z2, int i3, Rect rect2) {
        int iMax = Math.max(rect.bottom, rect2.top);
        int iMin = Math.min(rect.top, rect2.bottom);
        int i4 = rect2.top;
        int side = mainMenuPlacementSpec.getSide();
        if (side == 2) {
            return Math.max(rect2.top, Math.min(iMax, rect2.bottom - i3));
        }
        if (side == 1) {
            return Math.max(rect2.top, Math.min(iMin - i3, rect2.bottom - i3));
        }
        if (z2) {
            int[] iArr = {i4};
            if (!tryLocateAboveAnchor(iMin, i3, rect2, iArr)) {
                tryLocateBelowAnchor(iMax, i3, rect2, iArr);
            }
            return iArr[0];
        }
        int[] iArr2 = {i4};
        if (!tryLocateBelowAnchor(iMax, i3, rect2, iArr2)) {
            tryLocateAboveAnchor(iMin, i3, rect2, iArr2);
        }
        return iArr2[0];
    }

    public static /* bridge */ /* synthetic */ Rect d(RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper) {
        return rotatingPopupMenuLocateHelper.mAnchorRealBounds;
    }

    public static /* bridge */ /* synthetic */ Rect e(RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper) {
        return rotatingPopupMenuLocateHelper.mDefaultAnchorGapOutsets;
    }

    public static /* bridge */ /* synthetic */ MainMenuPlacementSpec f(RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper) {
        return rotatingPopupMenuLocateHelper.mPlacementSpec;
    }

    public static /* bridge */ /* synthetic */ int g(RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper) {
        return rotatingPopupMenuLocateHelper.locateMainMenuAnchorX();
    }

    private static boolean isExplicitHorizontalSide(int i3) {
        return i3 == 3 || i3 == 4;
    }

    private int locateMainMenuAnchorX() {
        View view = this.mDomain.mAnchorView;
        boolean z2 = view != null && view.getLayoutDirection() == 1;
        this.mDomain.mForcedHorizontalEdge = 0;
        if (this.mCenterAlign) {
            return locateMainMenuXDefault();
        }
        int alignment = this.mPlacementSpec.getAlignment();
        if (alignment == 0 || this.mPlacementSpec.isSideForced()) {
            return locateMainMenuXDefault();
        }
        int iForcedMenuLeft = MenuRotationHelper.forcedMenuLeft(this.mDomain.mAnchor, this.mMainMenuWidth, alignment, z2);
        if (alignment == 1) {
            this.mDomain.mForcedHorizontalEdge = 1;
        } else {
            this.mDomain.mForcedHorizontalEdge = 2;
        }
        Rect rect = this.mAvailableBounds;
        int i3 = rect.left;
        if (iForcedMenuLeft < i3) {
            iForcedMenuLeft = i3;
        }
        int i4 = this.mMainMenuWidth;
        int i5 = iForcedMenuLeft + i4;
        int i6 = rect.right;
        if (i5 > i6) {
            iForcedMenuLeft = i6 - i4;
        }
        if (iForcedMenuLeft < i3) {
            iForcedMenuLeft = rect.centerX() - (this.mMainMenuWidth / 2);
            this.mDomain.mForcedHorizontalEdge = 0;
        }
        if (COUI_DEBUG) {
            Log.d(TAG, "locateMainMenuAnchorX forced mAnchor=" + this.mDomain.mAnchor + " mMainMenuWidth=" + this.mMainMenuWidth + " mAvailableBounds=" + this.mAvailableBounds + " result x=" + iForcedMenuLeft);
        }
        return iForcedMenuLeft;
    }

    private int locateMainMenuXDefault() {
        return locateMainMenuXDefault(this.mDomain.mAnchor, this.mMainMenuWidth, this.mAvailableBounds);
    }

    private void mapAnchorLocalPointToWindowBounds(View view, View view2, int i3, int i4, Rect rect) {
        float[] fArr = this.mAnchorMapCoords;
        fArr[0] = i3;
        fArr[1] = i4;
        mapAnchorLocalPointsToWindow(view, view2, fArr, 1);
        int iRound = Math.round(this.mAnchorMapCoords[0]);
        int iRound2 = Math.round(this.mAnchorMapCoords[1]);
        rect.set(iRound, iRound2, iRound, iRound2);
    }

    private void mapAnchorLocalPointsToWindow(View view, View view2, float[] fArr, int i3) {
        this.mAnchorToGlobalMatrix.reset();
        view.transformMatrixToGlobal(this.mAnchorToGlobalMatrix);
        this.mAnchorToGlobalMatrix.mapPoints(fArr, 0, fArr, 0, i3);
        this.mWindowToGlobalMatrix.reset();
        view2.transformMatrixToGlobal(this.mWindowToGlobalMatrix);
        this.mGlobalToWindowMatrix.reset();
        if (this.mWindowToGlobalMatrix.invert(this.mGlobalToWindowMatrix)) {
            this.mGlobalToWindowMatrix.mapPoints(fArr, 0, fArr, 0, i3);
        } else {
            Log.w(TAG, "Failed to invert limited window matrix; use global anchor points.");
        }
    }

    public static int resolvePopupSide(MainMenuPlacementSpec mainMenuPlacementSpec, boolean z2) {
        if (!mainMenuPlacementSpec.isSideForced()) {
            return 0;
        }
        if (mainMenuPlacementSpec.getSide() == 3) {
            return z2 ? 4 : 3;
        }
        return z2 ? 3 : 4;
    }

    private void setAnchorBoundsInWindow(View view, View view2, Rect rect) {
        int width = view.getWidth();
        int height = view.getHeight();
        float f3 = width / 2.0f;
        float f4 = height / 2.0f;
        float[] fArr = this.mAnchorMapCoords;
        fArr[0] = f3;
        fArr[1] = f4;
        mapAnchorLocalPointsToWindow(view, view2, fArr, 1);
        int iRound = Math.round(this.mAnchorMapCoords[0] - f3);
        int iRound2 = Math.round(this.mAnchorMapCoords[1] - f4);
        rect.set(iRound, iRound2, iRound + width, iRound2 + height);
        if (COUI_DEBUG) {
            view.getLocationInWindow(this.mAnchorLocationInWindow);
            Log.d(TAG, "center-mapped layout-sized anchor bounds = " + rect + ", mAnchorLocationInWindow:" + Arrays.toString(this.mAnchorLocationInWindow) + " layout=" + width + "x" + height + " rotation=" + view.getRotation() + " scale=(" + view.getScaleX() + "," + view.getScaleY() + ") pivot=(" + view.getPivotX() + "," + view.getPivotY() + ")");
        }
    }

    private void setMainMenuMeasureCaps(int i3, int i4) {
        this.mMainMenuMeasureMaxWidth = Math.max(0, i3);
        this.mMainMenuMeasureMaxHeight = Math.max(0, i4);
        this.mHasMainMenuMeasureCaps = true;
    }

    public static boolean shouldApplyCenterAlignY(MainMenuPlacementSpec mainMenuPlacementSpec) {
        return (mainMenuPlacementSpec.isVerticalForced() || mainMenuPlacementSpec.isSideForced()) ? false : true;
    }

    private static boolean tryLocateAboveAnchor(int i3, int i4, Rect rect, int[] iArr) {
        if (i3 - rect.top < i4) {
            return false;
        }
        iArr[0] = i3 - i4;
        return true;
    }

    private static boolean tryLocateBelowAnchor(int i3, int i4, Rect rect, int[] iArr) {
        if (rect.bottom - i3 < i4) {
            return false;
        }
        iArr[0] = i3;
        return true;
    }

    public void applyMainMenuLayoutRect(Rect rect) {
        this.mDomain.mMainMenu.set(rect);
        this.mMainMenuWidth = rect.width();
        this.mMainMenuHeight = rect.height();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void clearMainMenuMeasureCaps() {
        this.mHasMainMenuMeasureCaps = false;
        this.mMainMenuMeasureMaxWidth = Integer.MAX_VALUE;
        this.mMainMenuMeasureMaxHeight = Integer.MAX_VALUE;
    }

    public void computeMainMenuMeasureCaps(int i3, View view, MainMenuPlacementSpec mainMenuPlacementSpec, boolean z2) {
        int iMax;
        int iMin;
        this.mDomain.getAvailableRect(this.mAvailableBounds);
        int iMax2 = Math.max(0, this.mDomain.getAvailableRectWidth());
        int iMax3 = Math.max(0, this.mDomain.getAvailableRectHeight());
        int iMin2 = Math.min(iMax2, Math.max(0, i3));
        this.mDomain.getAnchorRealRect(this.mAnchorRealBounds);
        int iResolvePopupSide = resolvePopupSide(mainMenuPlacementSpec, view);
        if (isExplicitHorizontalSide(iResolvePopupSide)) {
            int iMax4 = Math.max(0, MenuRotationHelper.computeExplicitSideHorizontalClearancePx(this.mAnchorRealBounds, this.mAvailableBounds, iResolvePopupSide));
            if (z2) {
                setMainMenuMeasureCaps(Math.min(iMax3, iMax3), Math.min(iMax4, iMax2));
                return;
            } else {
                setMainMenuMeasureCaps(Math.min(iMin2, iMax4), iMax3);
                return;
            }
        }
        int side = mainMenuPlacementSpec.getSide();
        if (side != 1) {
            iMax = side != 2 ? iMax3 : Math.max(0, this.mAvailableBounds.bottom - this.mAnchorRealBounds.bottom);
        } else {
            iMax = Math.max(0, this.mAnchorRealBounds.top - this.mAvailableBounds.top);
        }
        boolean z3 = view.getLayoutDirection() == 1;
        int alignment = mainMenuPlacementSpec.getAlignment();
        MenuRotationHelper.PhysicalHAlign physicalHAlignPhysicalHAlignForCrossAxis = MenuRotationHelper.physicalHAlignForCrossAxis(alignment, z3);
        if (alignment == 1 || alignment == 2) {
            int iMax5 = physicalHAlignPhysicalHAlignForCrossAxis == MenuRotationHelper.PhysicalHAlign.LEFT ? Math.max(0, this.mAvailableBounds.right - this.mAnchorRealBounds.left) : Math.max(0, this.mAnchorRealBounds.right - this.mAvailableBounds.left);
            iMin2 = Math.min(iMin2, iMax5);
            iMin = Math.min(iMax2, iMax5);
        } else {
            iMin = iMax2;
        }
        if (z2) {
            setMainMenuMeasureCaps(Math.min(iMax, iMax3), Math.min(iMin, iMax2));
        } else {
            setMainMenuMeasureCaps(iMin2, iMax);
        }
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void executeShowMainMenu() {
        this.mExecutor.execute((PopupMenuRule) this.mMainMenuLocateXRule, this.mDomain).execute(this.mMainMenuLocateYRule, this.mDomain);
        if (this.mCenterAlign) {
            this.mExecutor.execute((PopupMenuRule) this.mMainMenuCenterLocateXRule, this.mDomain).execute(this.mMainMenuCenterLocateYRule, this.mDomain);
        }
        this.mExecutor.execute((PopupMenuRule) this.mMainMenuGlobalOffsetRule, this.mDomain);
    }

    public int getMainMenuMeasureMaxWidth(int i3) {
        return !this.mHasMainMenuMeasureCaps ? i3 : Math.min(i3, this.mMainMenuMeasureMaxWidth);
    }

    public int getMainMenuRotationContentHeight() {
        return this.mMainMenuRotationContentHeight;
    }

    public int getMainMenuRotationContentWidth() {
        return this.mMainMenuRotationContentWidth;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public int getMaxMainMenuHeight() {
        return !this.mHasMainMenuMeasureCaps ? this.mDomain.getAvailableRectHeight() : this.mMainMenuMeasureMaxHeight;
    }

    public void prepareOrthogonalMainMenu(int i3, int i4, float f3, boolean z2, int i5, int i6) {
        clearMainMenuRotationContentSize();
        this.mDomain.clearMainMenuPlacementState();
        if (this.mPlacementSpec.getSide() == 2) {
            z2 = false;
        } else if (this.mPlacementSpec.getSide() == 1) {
            z2 = true;
        }
        this.mLocateFromAboveAnchorToBelow = z2;
        this.mGlobalOffsetX = i5;
        this.mGlobalOffsetY = i6;
        this.mDomain.getAvailableRect(this.mAvailableBounds);
        MenuRotationHelper.RotationQuarter rotationQuarterClassifyDegrees = MenuRotationHelper.classifyDegrees(f3);
        this.mMainMenuWidth = Math.min(MenuRotationHelper.rotatedLocateWidth(rotationQuarterClassifyDegrees, i3, i4), Math.abs(this.mAvailableBounds.width()));
        this.mMainMenuHeight = Math.min(MenuRotationHelper.rotatedLocateHeight(rotationQuarterClassifyDegrees, i3, i4), Math.abs(this.mAvailableBounds.height()));
        executeShowMainMenu();
        this.mDesiredDisplayBounds.set(this.mDomain.mMainMenu);
        MenuRotationHelper.computeRotationLayout(this.mDesiredDisplayBounds, i3, i4, f3, this.mAvailableBounds, this.mRotationLayoutOut);
        applyMainMenuRotationLayout(this.mRotationLayoutOut);
        this.mDomain.dump();
        this.mExecutor.endConfigRulesRecord();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void prepareShowMainMenu(int i3, int i4, boolean z2, int i5, int i6) {
        clearMainMenuRotationContentSize();
        this.mDomain.clearMainMenuPlacementState();
        if (this.mPlacementSpec.getSide() == 2) {
            z2 = false;
        } else if (this.mPlacementSpec.getSide() == 1) {
            z2 = true;
        }
        super.prepareShowMainMenu(i3, i4, z2, i5, i6);
    }

    public void prepareShowMainMenuExplicitSide(int i3, int i4, float f3, int i5, int i6) {
        clearMainMenuRotationContentSize();
        this.mDomain.clearMainMenuPlacementState();
        int iResolvePopupSide = resolvePopupSide(this.mPlacementSpec, this.mDomain.mAnchorView);
        if (!isExplicitHorizontalSide(iResolvePopupSide)) {
            prepareShowMainMenu(i3, i4, false, i5, i6);
            return;
        }
        this.mDomain.setMainMenuPopupSide(iResolvePopupSide);
        this.mGlobalOffsetX = i5;
        this.mGlobalOffsetY = i6;
        this.mDomain.getAvailableRect(this.mAvailableBounds);
        MenuRotationHelper.RotationQuarter rotationQuarterClassifyDegrees = MenuRotationHelper.classifyDegrees(f3);
        int iRotatedLocateWidth = MenuRotationHelper.rotatedLocateWidth(rotationQuarterClassifyDegrees, i3, i4);
        int iRotatedLocateHeight = MenuRotationHelper.rotatedLocateHeight(rotationQuarterClassifyDegrees, i3, i4);
        this.mDomain.getAnchorRealRect(this.mAnchorRealBounds);
        MenuRotationHelper.explicitSideDisplayBounds(this.mAnchorRealBounds, iResolvePopupSide, this.mPlacementSpec.getAlignment(), iRotatedLocateWidth, iRotatedLocateHeight, i5, i6, this.mDesiredDisplayBounds);
        if (rotationQuarterClassifyDegrees.isOrthogonalForLocate()) {
            MenuRotationHelper.computeRotationLayout(this.mDesiredDisplayBounds, i3, i4, f3, this.mAvailableBounds, this.mRotationLayoutOut);
            this.mMainMenuRotationContentWidth = i3;
            this.mMainMenuRotationContentHeight = i4;
            applyMainMenuRotationLayout(this.mRotationLayoutOut);
        } else {
            MenuRotationHelper.computeClampDeltaToFitRectInRect(this.mDesiredDisplayBounds, this.mAvailableBounds, this.mClampDxDy);
            Rect rect = this.mDesiredDisplayBounds;
            int[] iArr = this.mClampDxDy;
            rect.offset(iArr[0], iArr[1]);
            applyMainMenuLayoutRect(this.mDesiredDisplayBounds);
        }
        PopupMenuDomain popupMenuDomain = this.mDomain;
        popupMenuDomain.mGlobalOffsetX = i5;
        popupMenuDomain.mGlobalOffsetY = i6;
        popupMenuDomain.dump();
        this.mExecutor.endConfigRulesRecord();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void prepareWindowAndAnchor(View view, int i3, int i4, View view2) {
        super.prepareWindowAndAnchor(view, i3, i4, view2);
        this.mDomain.mAnchorView = view;
    }

    public MainMenuPlacementSpec resolveFittingSpec(MainMenuPlacementSpec mainMenuPlacementSpec, View view, int i3, MenuRotationHelper.RotationQuarter rotationQuarter, int i4, int i5) {
        if (!mainMenuPlacementSpec.isSideForced()) {
            if ((mainMenuPlacementSpec.getAlignment() == 1 || mainMenuPlacementSpec.getAlignment() == 2) && i3 > 0) {
                this.mDomain.getAvailableRect(this.mAvailableBounds);
                int iAbs = Math.abs(this.mAvailableBounds.width());
                this.mDomain.getAnchorRealRect(this.mAnchorRealBounds);
                int iMax = MenuRotationHelper.physicalHAlignForCrossAxis(mainMenuPlacementSpec.getAlignment(), view.getLayoutDirection() == 1) == MenuRotationHelper.PhysicalHAlign.LEFT ? Math.max(0, this.mAvailableBounds.right - this.mAnchorRealBounds.left) : Math.max(0, this.mAnchorRealBounds.right - this.mAvailableBounds.left);
                if (rotationQuarter.isOrthogonalForLocate()) {
                    if (i4 > 0 && i5 > 0) {
                        i3 = MenuRotationHelper.rotatedLocateWidth(rotationQuarter, Math.max(i3, i4), i5);
                        if (i3 <= iAbs) {
                            return MainMenuPlacementSpec.ofSideAndAlignment(mainMenuPlacementSpec.getSide(), 0);
                        }
                    }
                } else if (i3 <= iAbs || i3 > iMax) {
                    return MainMenuPlacementSpec.ofSideAndAlignment(mainMenuPlacementSpec.getSide(), 0);
                }
            }
        }
        return mainMenuPlacementSpec;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void setAnchor(View view, int i3, int i4, View view2) {
        RotatingPopupMenuLocateHelper rotatingPopupMenuLocateHelper;
        View view3;
        if (view2 == null) {
            view2 = view.getRootView();
        }
        View view4 = view2;
        setAnchorBoundsInWindow(view, view4, this.mAnchorBounds);
        if (i3 == Integer.MIN_VALUE || i4 == Integer.MIN_VALUE) {
            rotatingPopupMenuLocateHelper = this;
            view3 = view;
        } else {
            rotatingPopupMenuLocateHelper = this;
            view3 = view;
            rotatingPopupMenuLocateHelper.mapAnchorLocalPointToWindowBounds(view3, view4, i3, i4, this.mAnchorBounds);
        }
        rotatingPopupMenuLocateHelper.completeAnchorSetup(view3);
    }

    public void setPlacementSpec(MainMenuPlacementSpec mainMenuPlacementSpec) {
        this.mPlacementSpec = mainMenuPlacementSpec;
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void setupDefaultAnchorConfigRule() {
        this.mDefaultAnchorConfigRule = new AnonymousClass1();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void setupMainMenuCenterLocateYRule() {
        this.mMainMenuCenterLocateYRule = new AnonymousClass2();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void setupMainMenuLocateXRule() {
        this.mMainMenuLocateXRule = new AnonymousClass3();
    }

    @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper
    public void setupMainMenuLocateYRule() {
        this.mMainMenuLocateYRule = new AnonymousClass4();
    }

    private static int locateMainMenuXDefault(Rect rect, int i3, Rect rect2) {
        int i4 = i3 / 2;
        int iCenterX = rect.centerX() - i4;
        int i5 = rect2.left;
        if (iCenterX < i5) {
            iCenterX = i5;
        }
        int i6 = iCenterX + i3;
        int i7 = rect2.right;
        if (i6 > i7) {
            iCenterX = i7 - i3;
        }
        if (iCenterX < i5) {
            iCenterX = rect2.centerX() - i4;
        }
        if (COUI_DEBUG) {
            Log.d(TAG, "mMainMenuLocateXRule mAnchor [left " + rect.left + " top " + rect.top + " right " + rect.right + " bottom " + rect.bottom + "] mMainMenuWidth " + i3 + " mAvailableBounds [left " + rect2.left + " top " + rect2.top + " right " + rect2.right + " bottom " + rect2.bottom + "] result x = " + iCenterX);
        }
        return iCenterX;
    }

    /* JADX WARN: Code duplicated, block: B:6:0x000a  */
    public static int resolvePopupSide(MainMenuPlacementSpec mainMenuPlacementSpec, View view) {
        boolean z2;
        if (view != null) {
            z2 = view.getLayoutDirection() == 1;
        }
        return resolvePopupSide(mainMenuPlacementSpec, z2);
    }
}