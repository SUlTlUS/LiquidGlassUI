package com.coui.appcompat.poplist;

import android.graphics.Rect;
import android.view.View;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.UIUtil;

/* JADX INFO: loaded from: classes7.dex */
final class MenuRotationHelper {
    private static final float FULL_DEG = 360.0f;
    private static final float HALF = 0.5f;
    private static final float HALF_DEG = 180.0f;
    private static final float MOD_90_EPS_DEG = 0.05f;
    private static final float QUARTER_DEG = 90.0f;
    private static final float QUARTER_EPS_DEG = 1.0f;
    private static final String TAG = "MenuRotationHelper";
    private static final float ZERO_EPS_DEG = 0.05f;
    private static final float[] LOCAL_AXIS_BOUNDS = new float[4];
    private static final int[] CLAMP_DX_DY = new int[2];

    public enum PhysicalHAlign {
        LEFT,
        CENTER,
        RIGHT
    }

    public static final class RotationLayoutOut {
        float mPivotX;
        float mPivotY;
        final Rect mScreenBounds = new Rect();
        int mWrapperLeft;
        int mWrapperTop;
    }

    public enum RotationQuarter {
        NONE,
        DEG_90,
        DEG_270;

        public static RotationQuarter tryParseQuarter(float f3) {
            if (Float.isFinite(f3) && MenuRotationHelper.isMultipleOf90Mod360(f3)) {
                float fSmallestEquivalentDeg = MenuRotationHelper.smallestEquivalentDeg(f3);
                if (Math.abs(fSmallestEquivalentDeg) <= 0.05f) {
                    return NONE;
                }
                if (Math.abs(Math.abs(fSmallestEquivalentDeg) - 90.0f) < 1.0f) {
                    return fSmallestEquivalentDeg > 0.0f ? DEG_90 : DEG_270;
                }
            }
            return null;
        }

        public boolean allowsExpandableSubMenu() {
            return this == NONE;
        }

        public boolean isNonZeroRotation() {
            return this != NONE;
        }

        public boolean isOrthogonalForLocate() {
            return this == DEG_90 || this == DEG_270;
        }

        public int toDisplayDegrees() {
            int iOrdinal = ordinal();
            if (iOrdinal == 1) {
                return 90;
            }
            if (iOrdinal != 2) {
                return 0;
            }
            return UIUtil.CONSTANT_INT_TWO_HUNDRED_SEVENTY;
        }
    }

    private MenuRotationHelper() {
    }

    public static float clampPivot(float f3, int i3) {
        if (i3 <= 0) {
            return 0.0f;
        }
        return Math.max(0.0f, Math.min(i3, f3));
    }

    public static RotationQuarter classifyDegrees(float f3) {
        RotationQuarter rotationQuarterTryParseQuarter = RotationQuarter.tryParseQuarter(f3);
        if (rotationQuarterTryParseQuarter != null) {
            return rotationQuarterTryParseQuarter;
        }
        COUILog.w(TAG, "classifyDegrees: unsupported angle " + f3 + "; treating as NONE.");
        return RotationQuarter.NONE;
    }

    public static void computeClampDeltaToFitRectInRect(Rect rect, Rect rect2, int[] iArr) {
        int i3;
        int i4;
        int i5;
        int i6;
        if (rect.width() > rect2.width()) {
            i3 = rect2.left - rect.left;
        } else {
            int i7 = rect.left;
            int i8 = rect2.left;
            i3 = (i7 >= i8 && (i7 = rect.right) <= (i8 = rect2.right)) ? 0 : i8 - i7;
        }
        if (rect.height() <= rect2.height()) {
            int i9 = rect.top;
            int i10 = rect2.top;
            if (i9 < i10) {
                i6 = i10 - i9;
            } else {
                i4 = rect.bottom;
                i5 = rect2.bottom;
                if (i4 <= i5) {
                    i6 = 0;
                }
            }
            iArr[0] = i3;
            iArr[1] = i6;
        }
        i5 = rect2.top;
        i4 = rect.top;
        i6 = i5 - i4;
        iArr[0] = i3;
        iArr[1] = i6;
    }

    public static int computeExplicitSideHorizontalClearancePx(Rect rect, Rect rect2, int i3) {
        if (i3 == 3) {
            return rect.left - rect2.left;
        }
        if (i3 == 4) {
            return rect2.right - rect.right;
        }
        COUILog.w(TAG, "computeExplicitSideHorizontalClearancePx: unexpected popupSide=" + i3);
        return 0;
    }

    public static int computeForcedMainMenuEnterPivotX(int i3, int i4, boolean z2) {
        if (i3 == 1) {
            if (!z2) {
                return 0;
            }
        } else {
            if (i3 != 2) {
                COUILog.w(TAG, "computeForcedMainMenuEnterPivotX: unexpected forcedEdge=" + i3);
                return 0;
            }
            if (z2) {
                return 0;
            }
        }
        return i4;
    }

    public static void computeRotationLayout(Rect rect, int i3, int i4, float f3, Rect rect2, RotationLayoutOut rotationLayoutOut) {
        float[] fArr = LOCAL_AXIS_BOUNDS;
        localAxisBounds(i3, i4, f3, fArr);
        computeRotationLayoutFromWrapper(Math.round(rect.left - fArr[0]), Math.round(rect.top - fArr[2]), i3, i4, f3, rect2, rotationLayoutOut);
    }

    public static void computeRotationLayoutFromWrapper(int i3, int i4, int i5, int i6, float f3, Rect rect, RotationLayoutOut rotationLayoutOut) {
        rotationLayoutOut.mPivotX = i5 > 0 ? clampPivot(i5 * 0.5f, i5) : 0.0f;
        rotationLayoutOut.mPivotY = i6 > 0 ? clampPivot(i6 * 0.5f, i6) : 0.0f;
        screenAxisBounds(i3, i4, i5, i6, f3, rotationLayoutOut.mScreenBounds);
        Rect rect2 = rotationLayoutOut.mScreenBounds;
        int[] iArr = CLAMP_DX_DY;
        computeClampDeltaToFitRectInRect(rect2, rect, iArr);
        rotationLayoutOut.mScreenBounds.offset(iArr[0], iArr[1]);
        rotationLayoutOut.mWrapperLeft = iArr[0] + i3;
        rotationLayoutOut.mWrapperTop = i4 + iArr[1];
    }

    public static int computeSideHorizontalClearancePx(Rect rect, Rect rect2, RotationQuarter rotationQuarter) {
        if (!rotationQuarter.isOrthogonalForLocate()) {
            COUILog.w(TAG, "computeSideHorizontalClearancePx: expected DEG_90/DEG_270, got " + rotationQuarter);
            rotationQuarter = RotationQuarter.DEG_90;
        }
        return rotationQuarter == RotationQuarter.DEG_90 ? rect.left - rect2.left : rect2.right - rect.right;
    }

    public static void explicitSideDisplayBounds(Rect rect, int i3, int i4, int i5, int i6, int i7, int i8, Rect rect2) {
        int iCenterY;
        if (i3 != 3 && i3 != 4) {
            COUILog.w(TAG, "explicitSideDisplayBounds: unexpected popupSide=" + i3 + "; using LEFT.");
            i3 = 3;
        }
        if (i4 != 0 && i4 != 1 && i4 != 2) {
            COUILog.w(TAG, "explicitSideDisplayBounds: unsupported crossAxisAlignment=" + i4);
            i4 = 0;
        }
        int i9 = i3 == 3 ? rect.left - i5 : rect.right;
        if (i4 == 1) {
            iCenterY = rect.top;
        } else {
            iCenterY = i4 == 2 ? rect.bottom - i6 : rect.centerY() - (i6 / 2);
        }
        rect2.set(i9, iCenterY, i5 + i9, i6 + iCenterY);
        rect2.offset(i7, i8);
    }

    public static int forcedMenuLeft(Rect rect, int i3, int i4, boolean z2) {
        int iCenterX;
        if (i4 == 1) {
            if (!z2) {
                return rect.left;
            }
            iCenterX = rect.right;
        } else if (i4 != 2) {
            COUILog.w(TAG, "forcedMenuLeft: expected ALIGN_START/END, got " + i4);
            iCenterX = rect.centerX();
            i3 /= 2;
        } else {
            if (z2) {
                return rect.left;
            }
            iCenterX = rect.right;
        }
        return iCenterX - i3;
    }

    public static PhysicalHAlign inferMainMenuPhysicalHAlign(PopupMenuDomain popupMenuDomain) {
        if (!popupMenuDomain.mMainMenu.isEmpty() && !popupMenuDomain.mMainMenuCenterAlign) {
            int i3 = popupMenuDomain.mForcedHorizontalEdge;
            if (i3 == 1 || i3 == 2) {
                View view = popupMenuDomain.mAnchorView;
                return physicalHAlignForForcedEdge(popupMenuDomain.mForcedHorizontalEdge, view != null && view.getLayoutDirection() == 1);
            }
            int iCenterX = popupMenuDomain.mAnchor.centerX();
            int iCenterX2 = popupMenuDomain.mMainMenu.centerX();
            Rect rect = popupMenuDomain.mWindow;
            int i4 = rect.left;
            Rect rect2 = popupMenuDomain.mWindowBarriers;
            int i5 = i4 + rect2.left;
            int i6 = rect.right - rect2.right;
            Rect rect3 = popupMenuDomain.mMainMenu;
            if (rect3.left == i5) {
                return PhysicalHAlign.LEFT;
            }
            if (rect3.right == i6) {
                return PhysicalHAlign.RIGHT;
            }
            if (iCenterX2 == iCenterX) {
                return PhysicalHAlign.CENTER;
            }
            return iCenterX2 < iCenterX ? PhysicalHAlign.LEFT : PhysicalHAlign.RIGHT;
        }
        return PhysicalHAlign.CENTER;
    }

    public static boolean isMultipleOf90Mod360(float f3) {
        float f4 = (((f3 % 360.0f) + 360.0f) % 360.0f) % 90.0f;
        return f4 < 0.05f || f4 > 89.95f;
    }

    public static void localAxisBounds(float f3, float f4, RotationQuarter rotationQuarter, float[] fArr) {
        int iOrdinal = rotationQuarter.ordinal();
        if (iOrdinal == 0) {
            fArr[0] = 0.0f;
            fArr[1] = f3;
            fArr[2] = 0.0f;
            fArr[3] = f4;
            return;
        }
        if (iOrdinal == 1 || iOrdinal == 2) {
            fArr[0] = (f3 - f4) * 0.5f;
            float f5 = (f3 + f4) * 0.5f;
            fArr[1] = f5;
            fArr[2] = (f4 - f3) * 0.5f;
            fArr[3] = f5;
            return;
        }
        COUILog.w(TAG, "localAxisBounds: unexpected kind=" + rotationQuarter);
        fArr[0] = 0.0f;
        fArr[1] = f3;
        fArr[2] = 0.0f;
        fArr[3] = f4;
    }

    public static PhysicalHAlign physicalHAlignForCrossAxis(int i3, boolean z2) {
        if (i3 == 1) {
            return z2 ? PhysicalHAlign.RIGHT : PhysicalHAlign.LEFT;
        }
        if (i3 == 2) {
            return z2 ? PhysicalHAlign.LEFT : PhysicalHAlign.RIGHT;
        }
        return PhysicalHAlign.CENTER;
    }

    public static PhysicalHAlign physicalHAlignForForcedEdge(int i3, boolean z2) {
        if (i3 == 1) {
            return z2 ? PhysicalHAlign.RIGHT : PhysicalHAlign.LEFT;
        }
        if (i3 == 2) {
            return z2 ? PhysicalHAlign.LEFT : PhysicalHAlign.RIGHT;
        }
        COUILog.w(TAG, "physicalHAlignForForcedEdge: unexpected forcedEdge=" + i3);
        return PhysicalHAlign.LEFT;
    }

    public static int rotatedLocateHeight(RotationQuarter rotationQuarter, int i3, int i4) {
        return rotationQuarter.isOrthogonalForLocate() ? i3 : i4;
    }

    public static int rotatedLocateWidth(RotationQuarter rotationQuarter, int i3, int i4) {
        return rotationQuarter.isOrthogonalForLocate() ? i4 : i3;
    }

    public static void screenAxisBounds(int i3, int i4, int i5, int i6, float f3, Rect rect) {
        float[] fArr = LOCAL_AXIS_BOUNDS;
        localAxisBounds(i5, i6, f3, fArr);
        float f4 = i3;
        float f5 = i4;
        rect.set(Math.round(fArr[0] + f4), Math.round(fArr[2] + f5), Math.round(f4 + fArr[1]), Math.round(f5 + fArr[3]));
    }

    public static float smallestEquivalentDeg(float f3) {
        if (!Float.isFinite(f3)) {
            return 0.0f;
        }
        float f4 = ((f3 % 360.0f) + 360.0f) % 360.0f;
        return f4 > 180.0f ? f4 - 360.0f : f4;
    }

    public static void localAxisBounds(float f3, float f4, float f5, float[] fArr) {
        if (f3 > 0.0f && f4 > 0.0f && Float.isFinite(f3) && Float.isFinite(f4)) {
            localAxisBounds(f3, f4, classifyDegrees(f5), fArr);
            return;
        }
        fArr[0] = 0.0f;
        fArr[1] = Math.max(0.0f, f3);
        fArr[2] = 0.0f;
        fArr[3] = Math.max(0.0f, f4);
    }
}