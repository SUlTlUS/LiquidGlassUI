package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import com.coui.appcompat.log.COUILog;

/* JADX INFO: loaded from: classes7.dex */
class COUIRotatingPopupMenuRootView extends COUIPopupMenuRootView {
    private static final String TAG = "COUIRotatingPopupMenuRootView";
    private int mMainMenuChildMeasureHeight;
    private int mMainMenuChildMeasureWidth;
    private final Rect mMainMenuLayoutBounds;
    private float mMainRotationDegrees;
    private boolean mMainRotationPending;

    public COUIRotatingPopupMenuRootView(Context context) {
        super(context);
        this.mMainRotationDegrees = 0.0f;
        this.mMainMenuLayoutBounds = new Rect();
    }

    private void applyMainMenuRotationImpl(RoundFrameLayout roundFrameLayout, float f3) {
        PopupMenuDomain popupMenuDomain;
        int width = roundFrameLayout.getWidth();
        int height = roundFrameLayout.getHeight();
        if (width <= 0 || height <= 0) {
            COUILog.w(TAG, "Main menu wrapper size still 0; menu content rotation not applied");
            return;
        }
        if (MenuRotationHelper.classifyDegrees(f3).isOrthogonalForLocate() && (popupMenuDomain = this.mDomain) != null && popupMenuDomain.mMainMenuRotationWrapperActive) {
            roundFrameLayout.setPivotX(width / 2.0f);
            roundFrameLayout.setPivotY(height / 2.0f);
            roundFrameLayout.setRotation(f3);
        }
        syncMainMenuAnimPivot();
    }

    private void applyPendingMenuRotations() {
        if (this.mMainRotationPending) {
            ViewGroup viewGroup = this.mMainMenuRootView;
            if (viewGroup instanceof RoundFrameLayout) {
                applyMainMenuRotationImpl((RoundFrameLayout) viewGroup, this.mMainRotationDegrees);
            }
        }
    }

    public static void computeMainMenuWrapperLayoutRect(PopupMenuDomain popupMenuDomain, int i3, int i4, Rect rect) {
        if (popupMenuDomain == null || rect == null || popupMenuDomain.mMainMenu.isEmpty()) {
            if (rect != null) {
                rect.setEmpty();
                return;
            }
            return;
        }
        if (popupMenuDomain.mMainMenuRotationWrapperActive && i3 > 0 && i4 > 0) {
            int i5 = popupMenuDomain.mMainMenuRotationWrapperLeft;
            int i6 = popupMenuDomain.mMainMenuRotationWrapperTop;
            rect.set(i5, i6, i3 + i5, i4 + i6);
        } else {
            if (i3 <= 0 || i4 <= 0) {
                rect.set(popupMenuDomain.mMainMenu);
                return;
            }
            Rect rect2 = popupMenuDomain.mMainMenu;
            int iWidth = rect2.left + ((rect2.width() - i3) / 2);
            Rect rect3 = popupMenuDomain.mMainMenu;
            int iHeight = rect3.top + ((rect3.height() - i4) / 2);
            rect.set(iWidth, iHeight, i3 + iWidth, i4 + iHeight);
        }
    }

    public void clearMainMenuChildMeasureSize() {
        this.mMainMenuChildMeasureWidth = 0;
        this.mMainMenuChildMeasureHeight = 0;
    }

    public void clearMenuRotation(RoundFrameLayout roundFrameLayout) {
        if (roundFrameLayout == null) {
            return;
        }
        roundFrameLayout.setRotation(0.0f);
        roundFrameLayout.setTranslationX(0.0f);
        roundFrameLayout.setTranslationY(0.0f);
        ViewGroup viewGroup = this.mMainMenuRootView;
        if (roundFrameLayout == viewGroup) {
            this.mMainRotationPending = false;
            this.mMainRotationDegrees = 0.0f;
        }
        PopupMenuDomain popupMenuDomain = this.mDomain;
        if (popupMenuDomain == null || roundFrameLayout != viewGroup) {
            return;
        }
        popupMenuDomain.clearMainMenuRotationLayout();
    }

    @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        clearMainMenuChildMeasureSize();
        BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
        if (basePopupMenuAnimationController != null) {
            basePopupMenuAnimationController.setMainMenuAnimTargetSize(0, 0);
        }
        super.onDetachedFromWindow();
    }

    @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        PopupMenuDomain popupMenuDomain;
        PopupMenuDomain popupMenuDomain2;
        if (this.mMainMenuRootView != null && (popupMenuDomain2 = this.mDomain) != null) {
            computeMainMenuWrapperLayoutRect(popupMenuDomain2, this.mMainMenuChildMeasureWidth, this.mMainMenuChildMeasureHeight, this.mMainMenuLayoutBounds);
            ViewGroup viewGroup = this.mMainMenuRootView;
            Rect rect = this.mMainMenuLayoutBounds;
            viewGroup.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
        ViewGroup viewGroup2 = this.mSubMenuRootView;
        if (viewGroup2 != null && (popupMenuDomain = this.mDomain) != null) {
            Rect rect2 = popupMenuDomain.mSubMenu;
            viewGroup2.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
        }
        applyPendingMenuRotations();
    }

    @Override // com.coui.appcompat.poplist.COUIPopupMenuRootView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        ViewGroup viewGroup = this.mMainMenuRootView;
        if (viewGroup != null) {
            int i5 = this.mMainMenuChildMeasureWidth;
            if (i5 <= 0) {
                i5 = this.mMainMenuWidth;
            }
            int i6 = this.mMainMenuChildMeasureHeight;
            if (i6 <= 0) {
                i6 = this.mMainMenuHeight;
            }
            viewGroup.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(i6, 1073741824));
            BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
            if (basePopupMenuAnimationController != null) {
                basePopupMenuAnimationController.setMainMenuSize(i5, i6);
            }
        }
        ViewGroup viewGroup2 = this.mSubMenuRootView;
        if (viewGroup2 != null) {
            viewGroup2.measure(View.MeasureSpec.makeMeasureSpec(this.mSubMenuWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mSubMenuHeight, 1073741824));
            BasePopupMenuAnimationController basePopupMenuAnimationController2 = this.mController;
            if (basePopupMenuAnimationController2 != null) {
                basePopupMenuAnimationController2.setSubMenuSize(this.mSubMenuWidth, this.mSubMenuHeight);
            }
        }
        setMeasuredDimension(View.MeasureSpec.getSize(i3), View.MeasureSpec.getSize(i4));
    }

    public void setMainMenuAnimTargetSize(int i3, int i4) {
        BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
        if (basePopupMenuAnimationController != null) {
            basePopupMenuAnimationController.setMainMenuAnimTargetSize(i3, i4);
        }
    }

    public void setMainMenuChildMeasureSize(int i3, int i4) {
        this.mMainMenuChildMeasureWidth = Math.max(0, i3);
        this.mMainMenuChildMeasureHeight = Math.max(0, i4);
    }

    public void setMainRotation(float f3) {
        if (this.mMainMenuRootView instanceof RoundFrameLayout) {
            this.mMainRotationPending = true;
            this.mMainRotationDegrees = f3;
        }
    }

    public void syncMainMenuAnimPivot() {
        BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
        if (basePopupMenuAnimationController != null) {
            basePopupMenuAnimationController.syncMainMenuAnimPivot();
        }
    }
}