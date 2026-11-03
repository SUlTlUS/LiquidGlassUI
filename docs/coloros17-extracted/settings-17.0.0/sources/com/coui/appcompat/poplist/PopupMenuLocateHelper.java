package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.component.responsiveui.ResponsiveUIModel;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.coui.component.responsiveui.window.WindowTotalSizeClass;
import com.support.poplist.R;
import java.util.Arrays;

/* JADX INFO: loaded from: classes7.dex */
class PopupMenuLocateHelper {
    private static final boolean COUI_DEBUG;
    private static final Rect DEFAULT_ANCHOR_OUTSETS;
    protected static final Rect EMPTY_OUTSETS;
    protected static final float MAIN_MENU_SHRINK_RATIO = 0.9f;
    private static final String TAG = "PopupMenuLocateHelper";
    protected final Rect mAnchorBounds;
    protected final int[] mAnchorLocationInWindow;
    protected final int[] mAnchorOffset;
    final Rect mApplicationWindow;
    protected final Rect mAvailableBounds;
    protected boolean mCenterAlign;
    protected final Rect mContentVisibleBounds;
    protected PopupMenuConfigRule mContextAnchorConfigRule;
    protected PopupMenuConfigRule mDefaultAnchorConfigRule;
    protected DisplayCutout mDisplayCutout;
    protected final PopupMenuDomain mDomain;
    protected final PopupMenuRuleExecutor mExecutor;
    protected int mGlobalOffsetX;
    protected int mGlobalOffsetY;
    protected int mHorizontalOverlapBetweenMainAndSubMenu;
    protected boolean mIgnoreBarrier;
    protected boolean mIgnoreLeftCutoutBarrier;
    protected boolean mIgnoreRightCutoutBarrier;
    protected boolean mIsRtl;
    protected boolean mLocateFromAboveAnchorToBelow;
    protected PopupMenuControlRule mMainMenuCenterLocateXRule;
    protected PopupMenuControlRule mMainMenuCenterLocateYRule;
    protected PopupMenuControlRule mMainMenuGlobalOffsetRule;
    protected int mMainMenuHeight;
    protected PopupMenuControlRule mMainMenuLocateXRule;
    protected PopupMenuControlRule mMainMenuLocateYRule;
    protected PopupMenuControlRule mMainMenuRelocateRule;
    protected int mMainMenuShrinkWidth;
    protected View mMainMenuView;
    protected int mMainMenuWidth;
    protected int mMinGapBetweenMainAndSubMenu;
    protected int mNavigationBarMargin;
    protected final int[] mOffset;
    protected ResponsiveUIModel mResponsiveUIModel;
    protected int mStatusBarMargin;
    protected int mSubMenuHeight;
    protected PopupMenuControlRule mSubMenuLocateRule;
    protected int mSubMenuWidth;
    protected final Rect mSubmenuAnchorBounds;
    protected PopupMenuConfigRule mSubmenuAnchorConfigRule;
    protected final Rect mTempContentVisibleBounds;
    private int[] mTempLocation;
    protected boolean mUseWindowBarrier;
    protected int mVerticalOverlapBetweenMainAndSubMenu;
    protected PopupMenuConfigRule mWindowBottomBarrierRule;
    protected PopupMenuConfigRule mWindowConfigRule;
    protected PopupMenuConfigRule mWindowCutoutBarrierRule;
    protected PopupMenuConfigRule mWindowLeftBarrierRule;
    protected PopupMenuConfigRule mWindowRightBarrierRule;
    protected PopupMenuConfigRule mWindowTopBarrierRule;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$1, reason: invalid class name */
    public class AnonymousClass1 extends DefaultPopupMenuConfigRule {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return -1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            return PopupMenuLocateHelper.this.mApplicationWindow;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 0;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$10, reason: invalid class name */
    public class AnonymousClass10 implements PopupMenuControlRule {
        public AnonymousClass10() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            int iCenterX = popupMenuDomain.mAnchor.centerX();
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            int i3 = popupMenuLocateHelper.mMainMenuWidth;
            int iMin = iCenterX - (i3 / 2);
            Rect rect = popupMenuLocateHelper.mAvailableBounds;
            int i4 = rect.right;
            int i5 = rect.left;
            if (i4 - i5 >= i3) {
                int iMax = Math.max(iMin, i5);
                PopupMenuLocateHelper popupMenuLocateHelper2 = PopupMenuLocateHelper.this;
                iMin = Math.min(iMax, popupMenuLocateHelper2.mAvailableBounds.right - popupMenuLocateHelper2.mMainMenuWidth);
            }
            Rect rect2 = popupMenuDomain.mMainMenu;
            rect2.set(iMin, rect2.top, PopupMenuLocateHelper.this.mMainMenuWidth + iMin, rect2.bottom);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$11, reason: invalid class name */
    public class AnonymousClass11 implements PopupMenuControlRule {
        public AnonymousClass11() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            int iCenterY = popupMenuDomain.mAnchor.centerY();
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            int i3 = popupMenuLocateHelper.mMainMenuHeight;
            int iMin = iCenterY - (i3 / 2);
            Rect rect = popupMenuLocateHelper.mAvailableBounds;
            int i4 = rect.bottom;
            int i5 = rect.top;
            if (i4 - i5 >= i3) {
                int iMax = Math.max(iMin, i5);
                PopupMenuLocateHelper popupMenuLocateHelper2 = PopupMenuLocateHelper.this;
                iMin = Math.min(iMax, popupMenuLocateHelper2.mAvailableBounds.bottom - popupMenuLocateHelper2.mMainMenuHeight);
            }
            Rect rect2 = popupMenuDomain.mMainMenu;
            rect2.set(rect2.left, iMin, rect2.right, PopupMenuLocateHelper.this.mMainMenuHeight + iMin);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$12, reason: invalid class name */
    public class AnonymousClass12 implements PopupMenuControlRule {
        public AnonymousClass12() {
        }

        private int locateX() {
            int iCenterX = PopupMenuLocateHelper.this.mDomain.mAnchor.centerX();
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            int i3 = popupMenuLocateHelper.mMainMenuWidth;
            int iCenterX2 = iCenterX - (i3 / 2);
            Rect rect = popupMenuLocateHelper.mAvailableBounds;
            int i4 = rect.left;
            if (iCenterX2 < i4) {
                iCenterX2 = i4;
            }
            int i5 = iCenterX2 + i3;
            int i6 = rect.right;
            if (i5 > i6) {
                iCenterX2 = i6 - i3;
            }
            if (iCenterX2 < i4) {
                iCenterX2 = rect.centerX() - (PopupMenuLocateHelper.this.mMainMenuWidth / 2);
            }
            if (PopupMenuLocateHelper.b()) {
                Log.d(PopupMenuLocateHelper.TAG, "mMainMenuLocateXRule mAnchor [left " + PopupMenuLocateHelper.this.mDomain.mAnchor.left + " top " + PopupMenuLocateHelper.this.mDomain.mAnchor.top + " right " + PopupMenuLocateHelper.this.mDomain.mAnchor.right + " bottom " + PopupMenuLocateHelper.this.mDomain.mAnchor.bottom + "] mMainMenuWidth " + PopupMenuLocateHelper.this.mMainMenuWidth + " mAvailableBounds [left " + PopupMenuLocateHelper.this.mAvailableBounds.left + " top " + PopupMenuLocateHelper.this.mAvailableBounds.top + " right " + PopupMenuLocateHelper.this.mAvailableBounds.right + " bottom " + PopupMenuLocateHelper.this.mAvailableBounds.bottom + "] result x = " + iCenterX2);
            }
            return iCenterX2;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            int iLocateX = locateX();
            Rect rect = popupMenuDomain.mMainMenu;
            rect.set(iLocateX, rect.top, PopupMenuLocateHelper.this.mMainMenuWidth + iLocateX, rect.bottom);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$13, reason: invalid class name */
    public class AnonymousClass13 implements PopupMenuControlRule {

        /* JADX INFO: renamed from: y, reason: collision with root package name */
        int f13402y = 0;

        public AnonymousClass13() {
        }

        private void locateY(Rect rect) {
            int iMax = Math.max(rect.bottom, PopupMenuLocateHelper.this.mAvailableBounds.top);
            int iMin = Math.min(rect.top, PopupMenuLocateHelper.this.mAvailableBounds.bottom);
            if (PopupMenuLocateHelper.this.mLocateFromAboveAnchorToBelow) {
                if (!tryLocateAboveAnchor(iMin)) {
                    tryLocateBelowAnchor(iMax);
                }
            } else if (!tryLocateBelowAnchor(iMax)) {
                tryLocateAboveAnchor(iMin);
            }
            if (PopupMenuLocateHelper.b()) {
                Log.d(PopupMenuLocateHelper.TAG, "mMainMenuLocateYRule anchorBounds [left " + rect.left + " top " + rect.top + " right " + rect.right + " bottom " + rect.bottom + "] mMainMenuHeight " + PopupMenuLocateHelper.this.mMainMenuHeight + " mAvailableBounds [left " + PopupMenuLocateHelper.this.mAvailableBounds.left + " top " + PopupMenuLocateHelper.this.mAvailableBounds.top + " right " + PopupMenuLocateHelper.this.mAvailableBounds.right + " bottom " + PopupMenuLocateHelper.this.mAvailableBounds.bottom + "] result y = " + this.f13402y);
            }
        }

        private boolean tryLocateAboveAnchor(int i3) {
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            int i4 = i3 - popupMenuLocateHelper.mAvailableBounds.top;
            int i5 = popupMenuLocateHelper.mMainMenuHeight;
            if (i4 < i5) {
                return false;
            }
            this.f13402y = i3 - i5;
            return true;
        }

        private boolean tryLocateBelowAnchor(int i3) {
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            if (popupMenuLocateHelper.mAvailableBounds.bottom - i3 < popupMenuLocateHelper.mMainMenuHeight) {
                return false;
            }
            this.f13402y = i3;
            return true;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            Rect rect = new Rect();
            popupMenuDomain.getAnchorRealRect(rect);
            this.f13402y = PopupMenuLocateHelper.this.mAvailableBounds.top;
            locateY(rect);
            Rect rect2 = popupMenuDomain.mMainMenu;
            int i3 = rect2.left;
            int i4 = this.f13402y;
            rect2.set(i3, i4, rect2.right, PopupMenuLocateHelper.this.mMainMenuHeight + i4);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$14, reason: invalid class name */
    public class AnonymousClass14 implements PopupMenuControlRule {
        public AnonymousClass14() {
        }

        private int getOffsetX(PopupMenuDomain popupMenuDomain) {
            int iWidth = popupMenuDomain.mMainMenu.width() - popupMenuDomain.mMainMenuRelocated.width();
            int iCenterX = popupMenuDomain.mAnchor.centerX();
            int iCenterX2 = popupMenuDomain.mMainMenu.centerX();
            if (iCenterX < iCenterX2 - 1) {
                return 0;
            }
            return iCenterX > iCenterX2 + 1 ? iWidth : iWidth / 2;
        }

        private int getOffsetY(PopupMenuDomain popupMenuDomain) {
            int i3 = popupMenuDomain.mMainMenu.top;
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            int i4 = popupMenuLocateHelper.mMinGapBetweenMainAndSubMenu;
            int i5 = popupMenuLocateHelper.mSubMenuHeight;
            int i6 = i3 + i4 + i5;
            int i7 = popupMenuLocateHelper.mAvailableBounds.bottom;
            if (i6 < i7) {
                return 0;
            }
            return ((i7 - i5) - i4) - i3;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            if (!PopupMenuLocateHelper.this.isCurrentContainerSmallScreen()) {
                int iWidth = (int) (popupMenuDomain.mMainMenu.width() * 0.9f);
                int iHeight = (int) (popupMenuDomain.mMainMenu.height() * 0.9f);
                int iCenterX = popupMenuDomain.mMainMenu.centerX() - (iWidth / 2);
                int iCenterY = popupMenuDomain.mMainMenu.centerY() - (iHeight / 2);
                popupMenuDomain.mMainMenuRelocated.set(iCenterX, iCenterY, iWidth + iCenterX, iHeight + iCenterY);
                return;
            }
            int iWidth2 = (int) (popupMenuDomain.mMainMenu.width() * 0.9f);
            int iHeight2 = (int) (popupMenuDomain.mMainMenu.height() * 0.9f);
            Rect rect = popupMenuDomain.mMainMenuRelocated;
            Rect rect2 = popupMenuDomain.mMainMenu;
            int i3 = rect2.left;
            int i4 = rect2.top;
            rect.set(i3, i4, iWidth2 + i3, iHeight2 + i4);
            popupMenuDomain.mMainMenuRelocated.offset(getOffsetX(popupMenuDomain), getOffsetY(popupMenuDomain));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$15, reason: invalid class name */
    public class AnonymousClass15 implements PopupMenuControlRule {
        public AnonymousClass15() {
        }

        private int getOffsetX(PopupMenuDomain popupMenuDomain) {
            if (PopupMenuLocateHelper.this.isCurrentContainerSmallScreen()) {
                return popupMenuDomain.mMainMenu.left;
            }
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            if (popupMenuLocateHelper.mIsRtl) {
                Rect rect = popupMenuDomain.mMainMenuRelocated;
                int i3 = rect.right;
                int i4 = popupMenuLocateHelper.mHorizontalOverlapBetweenMainAndSubMenu;
                int i5 = popupMenuLocateHelper.mSubMenuWidth;
                return (i3 - i4) + i5 < popupMenuLocateHelper.mAvailableBounds.right ? i3 - i4 : (rect.left + i4) - i5;
            }
            Rect rect2 = popupMenuDomain.mMainMenuRelocated;
            int i6 = rect2.left;
            int i7 = popupMenuLocateHelper.mHorizontalOverlapBetweenMainAndSubMenu;
            int i8 = popupMenuLocateHelper.mSubMenuWidth;
            return (i6 + i7) - i8 > popupMenuLocateHelper.mAvailableBounds.left ? (i6 + i7) - i8 : rect2.right - i7;
        }

        private int getOffsetY(PopupMenuDomain popupMenuDomain) {
            if (!PopupMenuLocateHelper.this.isCurrentContainerSmallScreen()) {
                PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
                int i3 = popupMenuLocateHelper.mSubmenuAnchorBounds.top;
                int i4 = popupMenuLocateHelper.mSubMenuHeight;
                int i5 = i3 + i4;
                int i6 = popupMenuLocateHelper.mAvailableBounds.bottom;
                return i5 < i6 ? i3 : i6 - i4;
            }
            int subMenuAnchorTopAfterMainMenuRelocated = getSubMenuAnchorTopAfterMainMenuRelocated(popupMenuDomain);
            PopupMenuLocateHelper popupMenuLocateHelper2 = PopupMenuLocateHelper.this;
            int i7 = popupMenuLocateHelper2.mVerticalOverlapBetweenMainAndSubMenu;
            int i8 = popupMenuLocateHelper2.mSubMenuHeight;
            int i9 = (subMenuAnchorTopAfterMainMenuRelocated - i7) + i8;
            int i10 = popupMenuLocateHelper2.mAvailableBounds.bottom;
            return i9 < i10 ? subMenuAnchorTopAfterMainMenuRelocated - i7 : i10 - i8;
        }

        private int getSubMenuAnchorTopAfterMainMenuRelocated(PopupMenuDomain popupMenuDomain) {
            int i3 = PopupMenuLocateHelper.this.mSubmenuAnchorBounds.top;
            Rect rect = popupMenuDomain.mMainMenu;
            return (int) (popupMenuDomain.mMainMenuRelocated.top + ((rect.height() > 0 ? popupMenuDomain.mMainMenuRelocated.height() / popupMenuDomain.mMainMenu.height() : 1.0f) * (i3 - rect.top)));
        }

        @Override // com.coui.appcompat.poplist.PopupMenuControlRule
        public void operation(PopupMenuDomain popupMenuDomain) {
            Rect rect = popupMenuDomain.mSubMenu;
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            rect.set(0, 0, popupMenuLocateHelper.mSubMenuWidth, popupMenuLocateHelper.mSubMenuHeight);
            popupMenuDomain.mSubMenu.offset(getOffsetX(popupMenuDomain), getOffsetY(popupMenuDomain));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$2, reason: invalid class name */
    public class AnonymousClass2 extends DefaultPopupMenuConfigRule {
        private final Rect mDisplayFrame = new Rect();

        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return 0;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            this.mDisplayFrame.set(0, 0, Math.max(PopupMenuLocateHelper.this.mResponsiveUIModel.margin(), PopupMenuLocateHelper.this.mContentVisibleBounds.left), Math.abs(PopupMenuLocateHelper.this.mApplicationWindow.height()));
            return this.mDisplayFrame;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 2;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$3, reason: invalid class name */
    public class AnonymousClass3 extends DefaultPopupMenuConfigRule {
        private final Rect mDisplayFrame = new Rect();

        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return 2;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            int iMargin = PopupMenuLocateHelper.this.mResponsiveUIModel.margin();
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            int iMax = Math.max(iMargin, popupMenuLocateHelper.mApplicationWindow.right - popupMenuLocateHelper.mContentVisibleBounds.right);
            Rect rect = this.mDisplayFrame;
            Rect rect2 = PopupMenuLocateHelper.this.mApplicationWindow;
            int i3 = rect2.right;
            rect.set(i3 - iMax, 0, i3, Math.abs(rect2.height()));
            return this.mDisplayFrame;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 2;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$4, reason: invalid class name */
    public class AnonymousClass4 extends DefaultPopupMenuConfigRule {
        private final Rect mDisplayFrame = new Rect();

        public AnonymousClass4() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return 1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            Rect rect = this.mDisplayFrame;
            int iAbs = Math.abs(PopupMenuLocateHelper.this.mApplicationWindow.width());
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            rect.set(0, 0, iAbs, popupMenuLocateHelper.mContentVisibleBounds.top + popupMenuLocateHelper.mStatusBarMargin);
            return this.mDisplayFrame;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 2;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$5, reason: invalid class name */
    public class AnonymousClass5 extends DefaultPopupMenuConfigRule {
        private final Rect mDisplayFrame = new Rect();

        public AnonymousClass5() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            DisplayCutout displayCutout = PopupMenuLocateHelper.this.mDisplayCutout;
            if (displayCutout == null) {
                return -1;
            }
            if (!displayCutout.getBoundingRectTop().isEmpty()) {
                return 1;
            }
            if (!PopupMenuLocateHelper.this.mDisplayCutout.getBoundingRectBottom().isEmpty()) {
                return 3;
            }
            if (PopupMenuLocateHelper.this.mDisplayCutout.getBoundingRectLeft().isEmpty()) {
                return (PopupMenuLocateHelper.this.mDisplayCutout.getBoundingRectRight().isEmpty() || PopupMenuLocateHelper.this.mIgnoreRightCutoutBarrier) ? -1 : 2;
            }
            return PopupMenuLocateHelper.this.mIgnoreLeftCutoutBarrier ? -1 : 0;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            this.mDisplayFrame.setEmpty();
            DisplayCutout displayCutout = PopupMenuLocateHelper.this.mDisplayCutout;
            if (displayCutout == null) {
                return this.mDisplayFrame;
            }
            if (!displayCutout.getBoundingRectTop().isEmpty()) {
                Rect rect = this.mDisplayFrame;
                int iWidth = PopupMenuLocateHelper.this.mApplicationWindow.width();
                PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
                rect.set(0, 0, iWidth, Math.max(popupMenuLocateHelper.mContentVisibleBounds.top, popupMenuLocateHelper.mDisplayCutout.getBoundingRectTop().bottom));
            } else if (!PopupMenuLocateHelper.this.mDisplayCutout.getBoundingRectBottom().isEmpty()) {
                this.mDisplayFrame.set(0, PopupMenuLocateHelper.this.mDisplayCutout.getBoundingRectBottom().top, Math.abs(PopupMenuLocateHelper.this.mApplicationWindow.width()), PopupMenuLocateHelper.this.mApplicationWindow.bottom);
            } else if (!PopupMenuLocateHelper.this.mDisplayCutout.getBoundingRectLeft().isEmpty()) {
                PopupMenuLocateHelper popupMenuLocateHelper2 = PopupMenuLocateHelper.this;
                if (popupMenuLocateHelper2.mIgnoreLeftCutoutBarrier) {
                    return this.mDisplayFrame;
                }
                this.mDisplayFrame.set(0, 0, popupMenuLocateHelper2.mDisplayCutout.getBoundingRectLeft().right, Math.abs(PopupMenuLocateHelper.this.mApplicationWindow.height()));
            } else if (!PopupMenuLocateHelper.this.mDisplayCutout.getBoundingRectRight().isEmpty()) {
                PopupMenuLocateHelper popupMenuLocateHelper3 = PopupMenuLocateHelper.this;
                if (popupMenuLocateHelper3.mIgnoreRightCutoutBarrier) {
                    return this.mDisplayFrame;
                }
                Rect rect2 = this.mDisplayFrame;
                int i3 = popupMenuLocateHelper3.mDisplayCutout.getBoundingRectRight().left;
                Rect rect3 = PopupMenuLocateHelper.this.mApplicationWindow;
                rect2.set(i3, 0, rect3.right, Math.abs(rect3.height()));
            }
            return this.mDisplayFrame;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 2;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$6, reason: invalid class name */
    public class AnonymousClass6 extends DefaultPopupMenuConfigRule {
        private final Rect mDisplayFrame = new Rect();
        private final Rect mDisplayFrameOutsets;

        public AnonymousClass6() {
            this.mDisplayFrameOutsets = new Rect(0, PopupMenuLocateHelper.this.mNavigationBarMargin, 0, 0);
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return 3;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            PopupMenuLocateHelper popupMenuLocateHelper = PopupMenuLocateHelper.this;
            Rect rect = popupMenuLocateHelper.mApplicationWindow;
            int i3 = rect.bottom;
            this.mDisplayFrame.set(0, i3 - (i3 - popupMenuLocateHelper.mContentVisibleBounds.bottom), Math.abs(rect.width()), PopupMenuLocateHelper.this.mApplicationWindow.bottom);
            return this.mDisplayFrame;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return this.mDisplayFrameOutsets;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 2;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$7, reason: invalid class name */
    public class AnonymousClass7 extends DefaultPopupMenuConfigRule {
        public AnonymousClass7() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return -1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            return PopupMenuLocateHelper.this.mAnchorBounds;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 1;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$8, reason: invalid class name */
    public class AnonymousClass8 extends DefaultPopupMenuConfigRule {
        public AnonymousClass8() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return -1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            return PopupMenuLocateHelper.this.mAnchorBounds;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.c();
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 1;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuLocateHelper$9, reason: invalid class name */
    public class AnonymousClass9 extends DefaultPopupMenuConfigRule {
        public AnonymousClass9() {
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return -1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            return PopupMenuLocateHelper.this.mSubmenuAnchorBounds;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuLocateHelper.DefaultPopupMenuConfigRule, com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 3;
        }
    }

    public static class DefaultPopupMenuConfigRule implements PopupMenuConfigRule {
        private boolean mEnabled = true;

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getBarrierDirection() {
            return -1;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getDisplayFrame() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public Rect getOutsets() {
            return PopupMenuLocateHelper.EMPTY_OUTSETS;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public boolean getPopupMenuRuleEnabled() {
            return this.mEnabled;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public int getType() {
            return 2;
        }

        @Override // com.coui.appcompat.poplist.PopupMenuConfigRule
        public void setPopupMenuRuleEnabled(boolean z2) {
            this.mEnabled = z2;
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        EMPTY_OUTSETS = new Rect();
        DEFAULT_ANCHOR_OUTSETS = new Rect();
    }

    public PopupMenuLocateHelper(Context context) {
        this(context, false);
    }

    public static /* synthetic */ void a(PopupMenuLocateHelper popupMenuLocateHelper, PopupMenuDomain popupMenuDomain) {
        int i3 = popupMenuLocateHelper.mGlobalOffsetX;
        popupMenuDomain.mGlobalOffsetX = i3;
        popupMenuDomain.mGlobalOffsetY = popupMenuLocateHelper.mGlobalOffsetY;
        int iMin = Math.min(Math.max(popupMenuLocateHelper.mAvailableBounds.left, popupMenuDomain.mMainMenu.left + i3), popupMenuLocateHelper.mAvailableBounds.right - popupMenuDomain.mMainMenu.width());
        int iMin2 = Math.min(Math.max(popupMenuLocateHelper.mAvailableBounds.top, popupMenuDomain.mMainMenu.top + popupMenuLocateHelper.mGlobalOffsetY), popupMenuLocateHelper.mAvailableBounds.bottom - popupMenuDomain.mMainMenu.height());
        Rect rect = popupMenuDomain.mMainMenu;
        rect.set(iMin, iMin2, rect.width() + iMin, popupMenuDomain.mMainMenu.height() + iMin2);
    }

    public static /* bridge */ /* synthetic */ boolean b() {
        return COUI_DEBUG;
    }

    public static /* bridge */ /* synthetic */ Rect c() {
        return DEFAULT_ANCHOR_OUTSETS;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void executeConfigRules(View view, int i3, int i4) {
        this.mDomain.reset();
        this.mExecutor.execute((PopupMenuRule) this.mWindowConfigRule, this.mDomain);
        if (!this.mIgnoreBarrier && this.mUseWindowBarrier) {
            this.mExecutor.execute((PopupMenuRule) this.mWindowLeftBarrierRule, this.mDomain).execute(this.mWindowTopBarrierRule, this.mDomain).execute(this.mWindowRightBarrierRule, this.mDomain).execute(this.mWindowBottomBarrierRule, this.mDomain).execute(this.mWindowCutoutBarrierRule, this.mDomain);
        }
        if (view instanceof PopupMenuConfigRule) {
            PopupMenuConfigRule popupMenuConfigRule = (PopupMenuConfigRule) view;
            if (popupMenuConfigRule.getType() == 1) {
                this.mExecutor.execute((PopupMenuRule) popupMenuConfigRule, this.mDomain);
                return;
            }
        }
        if (i3 == Integer.MIN_VALUE || i4 == Integer.MIN_VALUE) {
            this.mExecutor.execute((PopupMenuRule) this.mDefaultAnchorConfigRule, this.mDomain);
        } else {
            this.mExecutor.execute((PopupMenuRule) this.mContextAnchorConfigRule, this.mDomain);
        }
    }

    private void executeShowSubMenu() {
        this.mExecutor.execute((PopupMenuRule) this.mSubmenuAnchorConfigRule, this.mDomain).execute(this.mMainMenuRelocateRule, this.mDomain).execute(this.mSubMenuLocateRule, this.mDomain);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void findAllBarrierRulesAndExecute(View view) {
        if (view.getVisibility() != 0) {
            return;
        }
        if (view instanceof PopupMenuConfigRule) {
            PopupMenuConfigRule popupMenuConfigRule = (PopupMenuConfigRule) view;
            if (popupMenuConfigRule.getType() == 2) {
                this.mExecutor.execute((PopupMenuRule) popupMenuConfigRule, this.mDomain);
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                findAllBarrierRulesAndExecute(viewGroup.getChildAt(i3));
            }
        }
    }

    private void getGlobalVisibleRectWithoutTransformation(View view, Rect rect) {
        view.getGlobalVisibleRect(rect);
        view.getLocationInWindow(this.mAnchorLocationInWindow);
        int[] iArr = this.mAnchorLocationInWindow;
        rect.offset(iArr[0] - rect.left, iArr[1] - rect.top);
        int iWidth = rect.left;
        int iHeight = rect.top;
        if (view.getWidth() != 0 && view.getScaleX() != 0.0f) {
            float pivotX = view.getPivotX() / view.getWidth();
            iWidth = (int) ((rect.left + (rect.width() * pivotX)) - ((rect.width() * pivotX) / view.getScaleX()));
        }
        if (view.getHeight() != 0 && view.getScaleY() != 0.0f) {
            float pivotY = view.getPivotY() / view.getHeight();
            iHeight = (int) ((rect.top + (rect.height() * pivotY)) - ((rect.height() * pivotY) / view.getScaleY()));
        }
        rect.set(iWidth, iHeight, view.getWidth() + iWidth, view.getHeight() + iHeight);
        if (COUI_DEBUG) {
            Log.d(TAG, "bounds with scale transform = " + rect + ",mAnchorLocationInWindow:" + Arrays.toString(this.mAnchorLocationInWindow) + " origin width = " + view.getWidth() + " origin height = " + view.getHeight() + " offset x = " + iWidth + " offset y = " + iHeight + " bounds = " + rect);
        }
    }

    private int getWindowLeft(View view) {
        UIUtil.getLocationOnScreenForSplit(view, this.mTempLocation);
        int[] iArr = this.mTempLocation;
        int i3 = iArr[0];
        view.getLocationInWindow(iArr);
        return i3 - this.mTempLocation[0];
    }

    private void setSubmenuAnchor(View view) {
        this.mDomain.mAnchorView = view;
        if (this.mMainMenuView != null) {
            int left = 0;
            View view2 = view;
            int top = 0;
            while (view2 != null && view2 != this.mMainMenuView) {
                left += view2.getLeft();
                top += view2.getTop();
                if (!(view2.getParent() instanceof View)) {
                    break;
                }
                view2 = (View) view2.getParent();
                left -= view2.getScrollX();
                top -= view2.getScrollY();
            }
            if (view2 == this.mMainMenuView) {
                Rect rect = this.mSubmenuAnchorBounds;
                Rect rect2 = this.mDomain.mMainMenu;
                int i3 = rect2.left;
                rect.set(i3 + left, rect2.top + top, i3 + left + view.getWidth(), this.mDomain.mMainMenu.top + top + view.getHeight());
                if (COUI_DEBUG) {
                    Rect rect3 = new Rect();
                    view.getGlobalVisibleRect(rect3);
                    Log.d(TAG, "setSubmenuAnchor: pure layout bounds=" + this.mSubmenuAnchorBounds + ", transformed bounds=" + rect3 + ", relativeLeft=" + left + ", relativeTop=" + top + ", mainMenu=" + this.mDomain.mMainMenu + ", mainMenuTY=" + this.mMainMenuView.getTranslationY() + ", mainMenuSY=" + this.mMainMenuView.getScaleY());
                    return;
                }
                return;
            }
        }
        view.getGlobalVisibleRect(this.mSubmenuAnchorBounds);
    }

    private void setupContextAnchorConfigRule() {
        this.mContextAnchorConfigRule = new AnonymousClass7();
    }

    private void setupMainMenuCenterLocateXRule() {
        this.mMainMenuCenterLocateXRule = new AnonymousClass10();
    }

    private void setupMainMenuGlobalOffsetRule() {
        this.mMainMenuGlobalOffsetRule = new c0(this);
    }

    private void setupMainMenuRelocateRule() {
        this.mMainMenuRelocateRule = new AnonymousClass14();
    }

    private void setupSubMenuLocateRule() {
        this.mSubMenuLocateRule = new AnonymousClass15();
    }

    private void setupSubmenuAnchorConfigRule() {
        this.mSubmenuAnchorConfigRule = new AnonymousClass9();
    }

    private void setupWindowBottomBarrierRule() {
        this.mWindowBottomBarrierRule = new AnonymousClass6();
    }

    private void setupWindowConfigRule() {
        this.mWindowConfigRule = new AnonymousClass1();
    }

    private void setupWindowCutoutBarrierRule() {
        this.mWindowCutoutBarrierRule = new AnonymousClass5();
    }

    private void setupWindowLeftBarrierRule() {
        this.mWindowLeftBarrierRule = new AnonymousClass2();
    }

    private void setupWindowRightBarrierRule() {
        this.mWindowRightBarrierRule = new AnonymousClass3();
    }

    private void setupWindowTopBarrierRule() {
        this.mWindowTopBarrierRule = new AnonymousClass4();
    }

    public boolean checkIfLimitedWindowOrAnchorResized(View view, int i3, int i4, View view2) {
        boolean z2 = true;
        if (view == null) {
            COUILog.e(TAG, "Anchor is null!");
            return true;
        }
        if (view2 == null) {
            view2 = view.getRootView();
        }
        view2.getWindowVisibleDisplayFrame(this.mTempContentVisibleBounds);
        if (this.mTempContentVisibleBounds.width() == this.mContentVisibleBounds.width() && this.mTempContentVisibleBounds.height() == this.mContentVisibleBounds.height()) {
            z2 = false;
        } else {
            COUILog.w(TAG, "Visible bounds changed!");
        }
        COUILog.d(TAG, " old content visible bounds = " + this.mContentVisibleBounds + " new content visible bounds = " + this.mTempContentVisibleBounds);
        this.mContentVisibleBounds.set(this.mTempContentVisibleBounds);
        return z2;
    }

    public void clearMainMenuMeasureCaps() {
    }

    public final void completeAnchorSetup(View view) {
        Rect rect = this.mContentVisibleBounds;
        int[] iArr = this.mOffset;
        rect.offset(-iArr[0], -iArr[1]);
        Rect rect2 = this.mContentVisibleBounds;
        rect2.bottom = Math.min(rect2.bottom, this.mApplicationWindow.bottom);
        ResponsiveUIModel responsiveUIModel = this.mResponsiveUIModel;
        if (responsiveUIModel == null) {
            ResponsiveUIModel responsiveUIModel2 = new ResponsiveUIModel(view.getContext(), Math.abs(this.mApplicationWindow.width()), Math.abs(this.mApplicationWindow.height()));
            this.mResponsiveUIModel = responsiveUIModel2;
            responsiveUIModel2.chooseMargin(MarginType.MARGIN_SMALL);
        } else {
            responsiveUIModel.rebuild(Math.abs(this.mApplicationWindow.width()), Math.abs(this.mApplicationWindow.height()));
        }
        if (view.getRootView().isAttachedToWindow()) {
            return;
        }
        Log.d(TAG, "Detected an unattached anchor, could be a dummy anchor");
        this.mIgnoreBarrier = true;
    }

    public void executeShowMainMenu() {
        if (this.mCenterAlign) {
            this.mExecutor.execute((PopupMenuRule) this.mMainMenuCenterLocateXRule, this.mDomain).execute(this.mMainMenuCenterLocateYRule, this.mDomain);
        } else {
            this.mExecutor.execute((PopupMenuRule) this.mMainMenuLocateXRule, this.mDomain).execute(this.mMainMenuLocateYRule, this.mDomain);
        }
        this.mExecutor.execute((PopupMenuRule) this.mMainMenuGlobalOffsetRule, this.mDomain);
    }

    public PopupMenuDomain getDomain() {
        return this.mDomain;
    }

    public int getMainMenuHeight() {
        return this.mMainMenuHeight;
    }

    public int getMainMenuWidth() {
        return this.mMainMenuWidth;
    }

    public int getMaxMainMenuHeight() {
        return this.mDomain.getAvailableRectHeight();
    }

    public int getMaxSubMenuHeight() {
        return isCurrentContainerSmallScreen() ? this.mDomain.getAvailableRectHeight() : this.mDomain.getAvailableRectHeight() - this.mMinGapBetweenMainAndSubMenu;
    }

    public int getSubMenuHeight() {
        return this.mSubMenuHeight;
    }

    public int getSubMenuWidth() {
        return this.mSubMenuWidth;
    }

    public boolean isCurrentContainerSmallScreen() {
        ResponsiveUIModel responsiveUIModel = this.mResponsiveUIModel;
        return responsiveUIModel != null && responsiveUIModel.windowSizeClass().getWindowTotalSizeClass() == WindowTotalSizeClass.Compact;
    }

    public void prepareShowMainMenu(int i3, int i4, boolean z2, int i5, int i6) {
        this.mLocateFromAboveAnchorToBelow = z2;
        this.mGlobalOffsetX = i5;
        this.mGlobalOffsetY = i6;
        this.mDomain.getAvailableRect(this.mAvailableBounds);
        this.mMainMenuWidth = Math.min(i3, Math.abs(this.mAvailableBounds.width()));
        this.mMainMenuHeight = Math.min(i4, Math.abs(this.mAvailableBounds.height()));
        executeShowMainMenu();
        this.mDomain.dump();
        this.mExecutor.endConfigRulesRecord();
    }

    public void prepareShowSubMenu(View view, View view2, int i3, int i4, boolean z2) {
        this.mMainMenuView = view;
        this.mIsRtl = z2;
        boolean zIsCurrentContainerSmallScreen = isCurrentContainerSmallScreen();
        setSubmenuAnchor(view2);
        this.mSubMenuWidth = Math.min(i3, Math.abs(this.mAvailableBounds.width()));
        this.mSubMenuHeight = Math.min(i4, Math.abs(this.mAvailableBounds.height()) - (zIsCurrentContainerSmallScreen ? this.mMinGapBetweenMainAndSubMenu : 0));
        executeShowSubMenu();
        this.mDomain.dump();
    }

    public void prepareWindowAndAnchor(View view, int i3, int i4, View view2) {
        View rootView = view2 != null ? view2 : view.getRootView();
        rootView.getLocationOnScreen(this.mOffset);
        rootView.getGlobalVisibleRect(this.mApplicationWindow);
        rootView.getWindowVisibleDisplayFrame(this.mContentVisibleBounds);
        boolean z2 = COUI_DEBUG;
        if (z2) {
            Log.d(TAG, "limited window = " + rootView + " anchor = " + view + " window location = (" + this.mOffset[0] + ", " + this.mOffset[1] + ") anchor location = (" + this.mAnchorOffset[0] + ", " + this.mAnchorOffset[1] + ") final offset = (" + i3 + ", " + i4 + ") use window barrier = " + this.mUseWindowBarrier + " center align = " + this.mCenterAlign + " mApplicationWindow [left " + this.mApplicationWindow.left + " top " + this.mApplicationWindow.top + " right " + this.mApplicationWindow.right + " bottom " + this.mApplicationWindow.bottom + "]");
        }
        setAnchor(view, i3, i4, view2);
        if (view.getRootWindowInsets() != null) {
            this.mDisplayCutout = view.getRootWindowInsets().getDisplayCutout();
        }
        boolean z3 = getWindowLeft(view) > 0;
        this.mIgnoreLeftCutoutBarrier = z3;
        this.mIgnoreRightCutoutBarrier = !z3 && UIUtil.getScreenWidthRealSize(view.getContext()) > UIUtil.getScreenWidthMetrics(view.getContext());
        if (z2) {
            Log.d(TAG, "ignore left cutout mIgnoreLeftCutoutBarrier = " + this.mIgnoreLeftCutoutBarrier + " mIgnoreRightCutoutBarrier = " + this.mIgnoreRightCutoutBarrier + " getWindowLeft() = " + getWindowLeft(view) + " displayCutout = " + this.mDisplayCutout);
        }
        this.mExecutor.beginConfigRulesRecord();
        executeConfigRules(view, i3, i4);
        findAllBarrierRulesAndExecute(view.getRootView());
    }

    public void setAnchor(View view, int i3, int i4, View view2) {
        getGlobalVisibleRectWithoutTransformation(view, this.mAnchorBounds);
        if (i3 != Integer.MIN_VALUE && i4 != Integer.MIN_VALUE) {
            Rect rect = this.mAnchorBounds;
            int i5 = rect.left;
            int i6 = rect.top;
            rect.set(i5 + i3, i6 + i4, i5 + i3, i6 + i4);
        }
        completeAnchorSetup(view);
    }

    public void setCenterAlign(boolean z2) {
        this.mCenterAlign = z2;
        this.mDomain.mMainMenuCenterAlign = z2;
    }

    public void setSubMenuAnchorIsFirstItem(boolean z2) {
        this.mDomain.mSubMenuAnchorIsFirstItem = z2;
    }

    public void setupDefaultAnchorConfigRule() {
        this.mDefaultAnchorConfigRule = new AnonymousClass8();
    }

    public void setupMainMenuCenterLocateYRule() {
        this.mMainMenuCenterLocateYRule = new AnonymousClass11();
    }

    public void setupMainMenuLocateXRule() {
        this.mMainMenuLocateXRule = new AnonymousClass12();
    }

    public void setupMainMenuLocateYRule() {
        this.mMainMenuLocateYRule = new AnonymousClass13();
    }

    public final void setupRules() {
        setupWindowConfigRule();
        setupWindowLeftBarrierRule();
        setupWindowRightBarrierRule();
        setupWindowTopBarrierRule();
        setupWindowBottomBarrierRule();
        setupWindowCutoutBarrierRule();
        setupContextAnchorConfigRule();
        setupDefaultAnchorConfigRule();
        setupSubmenuAnchorConfigRule();
        setupMainMenuLocateXRule();
        setupMainMenuLocateYRule();
        setupMainMenuGlobalOffsetRule();
        setupMainMenuRelocateRule();
        setupSubMenuLocateRule();
        setupMainMenuCenterLocateXRule();
        setupMainMenuCenterLocateYRule();
    }

    public void useWindowBarrier(boolean z2) {
        this.mUseWindowBarrier = z2;
    }

    public PopupMenuLocateHelper(Context context, boolean z2) {
        this.mTempLocation = new int[2];
        this.mApplicationWindow = new Rect();
        this.mContentVisibleBounds = new Rect();
        this.mAnchorBounds = new Rect();
        this.mAvailableBounds = new Rect();
        this.mSubmenuAnchorBounds = new Rect();
        this.mTempContentVisibleBounds = new Rect();
        this.mOffset = new int[2];
        this.mAnchorOffset = new int[2];
        this.mAnchorLocationInWindow = new int[2];
        this.mStatusBarMargin = 0;
        this.mNavigationBarMargin = 0;
        this.mMinGapBetweenMainAndSubMenu = 0;
        this.mHorizontalOverlapBetweenMainAndSubMenu = 0;
        this.mVerticalOverlapBetweenMainAndSubMenu = 0;
        this.mMainMenuShrinkWidth = 0;
        this.mGlobalOffsetX = 0;
        this.mGlobalOffsetY = 0;
        this.mLocateFromAboveAnchorToBelow = false;
        this.mIsRtl = false;
        this.mUseWindowBarrier = true;
        this.mCenterAlign = false;
        this.mStatusBarMargin = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_top_status_bar_margin);
        this.mNavigationBarMargin = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_bottom_navigation_bar_margin);
        this.mMinGapBetweenMainAndSubMenu = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_min_gap_to_top);
        this.mMainMenuShrinkWidth = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_main_menu_shrink_width);
        this.mHorizontalOverlapBetweenMainAndSubMenu = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_horizontal_overlap_between_main_and_sub_menu);
        this.mVerticalOverlapBetweenMainAndSubMenu = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_vertical_overlap_between_main_and_sub_menu);
        this.mDomain = new PopupMenuDomain();
        this.mExecutor = new PopupMenuRuleExecutor();
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_default_vertical_gap_to_anchor);
        DEFAULT_ANCHOR_OUTSETS.set(0, dimensionPixelOffset, 0, dimensionPixelOffset);
        if (z2) {
            return;
        }
        setupRules();
    }
}