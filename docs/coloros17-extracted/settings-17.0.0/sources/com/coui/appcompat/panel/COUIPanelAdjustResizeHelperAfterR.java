package com.coui.appcompat.panel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.support.panel.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPanelAdjustResizeHelperAfterR extends COUIAbsPanelAdjustResizeHelper {
    private static final float DEFAULT_SPRING_BOUNCE = 0.0f;
    private static final float DEFAULT_SPRING_RESPONSE = 0.3f;
    private static final String TAG = "AdjustResizeAfterR";
    private int mAnimMaxMargin;
    private int mAnimStartBottomMargin;
    private int mAnimStartOffset;
    private int mAnimStartPaddingBottom;
    private int mAnimTargetBottomMargin;
    private int mAnimTargetOffset;
    private int mAnimTargetPaddingBottom;
    private View mLastBottomDesignSheetOrPanelContentLayout;
    private View mLastCouiPanelContentLayout;
    private COUISpringAnimation mOffsetSpringAnimation;
    private FloatValueHolder mOffsetValueHolder;
    private int mWindowType = 2;
    private int mCurrentKeyboardHeight = 0;
    private int mBottomMarginOfDesignBottomSheet = 0;

    public static /* synthetic */ void a(COUIPanelAdjustResizeHelperAfterR cOUIPanelAdjustResizeHelperAfterR, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        View view = cOUIPanelAdjustResizeHelperAfterR.mLastCouiPanelContentLayout;
        View view2 = cOUIPanelAdjustResizeHelperAfterR.mLastBottomDesignSheetOrPanelContentLayout;
        if (view2 == null || view == null) {
            return;
        }
        int i3 = cOUIPanelAdjustResizeHelperAfterR.mAnimTargetOffset;
        int i4 = cOUIPanelAdjustResizeHelperAfterR.mAnimStartOffset;
        float fMax = i3 != i4 ? Math.max(0.0f, Math.min(1.0f, (f3 - i4) / (i3 - i4))) : 1.0f;
        int i5 = cOUIPanelAdjustResizeHelperAfterR.mAnimStartBottomMargin;
        int iMax = Math.max(0, Math.min(i5 + ((int) ((cOUIPanelAdjustResizeHelperAfterR.mAnimTargetBottomMargin - i5) * fMax)), cOUIPanelAdjustResizeHelperAfterR.mAnimMaxMargin));
        int i6 = cOUIPanelAdjustResizeHelperAfterR.mAnimStartPaddingBottom;
        view.setPadding(0, 0, 0, Math.max(0, i6 + ((int) ((cOUIPanelAdjustResizeHelperAfterR.mAnimTargetPaddingBottom - i6) * fMax))));
        ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = iMax;
            view2.setLayoutParams(layoutParams);
        }
    }

    private void doMarginBottomAnim(int i3, int i4, int i5) {
        this.mAnimMaxMargin = i3;
        this.mAnimStartOffset = i4;
        this.mAnimTargetOffset = i5;
        int paddingBottom = this.mLastCouiPanelContentLayout.getPaddingBottom();
        int iMax = Math.max(0, i4 - paddingBottom);
        int iMin = Math.min(i5, i3);
        int iMax2 = Math.max(i5 - i3, 0);
        this.mAnimStartBottomMargin = iMax;
        this.mAnimStartPaddingBottom = paddingBottom;
        this.mAnimTargetBottomMargin = iMin;
        this.mAnimTargetPaddingBottom = iMax2;
        FloatValueHolder floatValueHolder = this.mOffsetValueHolder;
        if (floatValueHolder == null) {
            this.mOffsetValueHolder = new FloatValueHolder(i4);
        } else {
            floatValueHolder.setValue(i4);
        }
        COUISpringAnimation cOUISpringAnimation = this.mOffsetSpringAnimation;
        if (cOUISpringAnimation == null) {
            COUISpringAnimation startValue = new COUISpringAnimation(this.mOffsetValueHolder).setSpring(new COUISpringForce(i5).setBounce(0.0f).setResponse(0.3f)).setStartValue(i4);
            this.mOffsetSpringAnimation = startValue;
            startValue.addUpdateListener(new c(this));
        } else {
            cOUISpringAnimation.setStartValue(i4);
            this.mOffsetSpringAnimation.getSpring().setFinalPosition(i5);
            this.mAnimStartOffset = i4;
            this.mAnimTargetOffset = i5;
        }
        if (this.mOffsetSpringAnimation.isRunning()) {
            return;
        }
        this.mOffsetSpringAnimation.start();
    }

    private int getCurrentBottomMargin(View view) {
        if (view == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return 0;
    }

    private int getCurrentBottomOffset(View view, View view2) {
        FloatValueHolder floatValueHolder;
        COUISpringAnimation cOUISpringAnimation = this.mOffsetSpringAnimation;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning() && (floatValueHolder = this.mOffsetValueHolder) != null) {
            return (int) floatValueHolder.getValue();
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        return (layoutParams instanceof ViewGroup.MarginLayoutParams ? ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin : 0) + view2.getPaddingBottom();
    }

    private void resetPaddingAndMargin() {
        View view = this.mLastBottomDesignSheetOrPanelContentLayout;
        if (view != null) {
            if (this.mAnimTargetPaddingBottom == 0 && this.mAnimTargetBottomMargin == 0) {
                return;
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = this.mBottomMarginOfDesignBottomSheet;
                this.mLastBottomDesignSheetOrPanelContentLayout.setLayoutParams(layoutParams);
            }
            View view2 = this.mLastCouiPanelContentLayout;
            if (view2 != null) {
                view2.setPadding(0, 0, 0, 0);
            }
        }
    }

    private void setMarginBottomTo(View view, int i3, WindowInsets windowInsets, View view2) {
        int currentBottomMargin;
        COUISpringAnimation cOUISpringAnimation;
        int i4;
        if (view != null) {
            View viewFindViewById = view2.findViewById(R.id.coui_panel_content_layout);
            if (viewFindViewById == null) {
                Log.e(TAG, "couiPanelContentLayout is null");
                return;
            }
            int measuredHeight = view2.getMeasuredHeight();
            int measuredHeight2 = view.getMeasuredHeight() - viewFindViewById.getPaddingBottom();
            if (i3 > measuredHeight * 0.9f) {
                Log.e(TAG, "KeyboardHeight > availableHeight * 0.9f, so not elevated");
                return;
            }
            Context context = view.getContext();
            View viewFindViewById2 = view2.findViewById(com.support.appcompat.R.id.design_bottom_sheet);
            this.mBottomMarginOfDesignBottomSheet = COUIPanelMultiWindowUtils.getPanelMarginBottom(context, context.getResources().getConfiguration(), windowInsets, viewFindViewById2 instanceof COUIPanelPercentFrameLayout ? ((COUIPanelPercentFrameLayout) viewFindViewById2).isIsHandlePanel() : false, isCouiPanelEdgeToEdgeEnable());
            if (i3 > 0) {
                currentBottomMargin = (measuredHeight <= 0 || measuredHeight2 <= 0 || (i4 = measuredHeight2 + i3) <= measuredHeight) ? i3 : i3 - (i4 - measuredHeight);
            } else {
                currentBottomMargin = getCurrentBottomMargin(view);
            }
            int iMax = Math.max(currentBottomMargin, this.mBottomMarginOfDesignBottomSheet);
            if (i3 == 0) {
                i3 = this.mBottomMarginOfDesignBottomSheet;
            }
            int currentBottomOffset = getCurrentBottomOffset(view, viewFindViewById);
            if (currentBottomOffset == i3 && this.mLastBottomDesignSheetOrPanelContentLayout == view && this.mLastCouiPanelContentLayout == viewFindViewById && ((cOUISpringAnimation = this.mOffsetSpringAnimation) == null || !cOUISpringAnimation.isRunning())) {
                Log.w(TAG, "currentBottomOffset == targetBottomOffset, skip animation");
                return;
            }
            this.mLastBottomDesignSheetOrPanelContentLayout = view;
            this.mLastCouiPanelContentLayout = viewFindViewById;
            COUISpringAnimation cOUISpringAnimation2 = this.mOffsetSpringAnimation;
            if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
                doMarginBottomAnim(iMax, currentBottomOffset, i3);
                return;
            }
            FloatValueHolder floatValueHolder = this.mOffsetValueHolder;
            int value = (int) (floatValueHolder != null ? floatValueHolder.getValue() : currentBottomOffset);
            this.mAnimMaxMargin = iMax;
            this.mAnimStartOffset = value;
            this.mAnimTargetOffset = i3;
            this.mAnimStartBottomMargin = getCurrentBottomMargin(view);
            this.mAnimStartPaddingBottom = viewFindViewById.getPaddingBottom();
            this.mAnimTargetBottomMargin = Math.min(i3, iMax);
            this.mAnimTargetPaddingBottom = Math.max(i3 - iMax, 0);
            FloatValueHolder floatValueHolder2 = this.mOffsetValueHolder;
            if (floatValueHolder2 != null) {
                floatValueHolder2.setValue(value);
            }
            this.mOffsetSpringAnimation.animateToFinalPosition(i3);
        }
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void adjustResize(Context context, ViewGroup viewGroup, WindowInsets windowInsets, View view, boolean z2) {
        if (z2) {
            int iMax = Math.max(0, windowInsets.getInsets(WindowInsets.Type.ime()).bottom - windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom);
            if (iMax != this.mCurrentKeyboardHeight) {
                this.mCurrentKeyboardHeight = iMax;
                setMarginBottomTo(viewGroup, iMax, windowInsets, view);
            } else {
                Log.w(TAG, "keyboardHeight is the same size, keyboardHeight :" + iMax);
            }
        }
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public int getMarginBottomValue() {
        return -1;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public int getPaddingBottomOffset() {
        return -1;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public float getTranslateOffset() {
        return -1.0f;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public int getWindowType() {
        return this.mWindowType;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void recoveryScrollingParentViewPaddingBottom(COUIPanelContentLayout cOUIPanelContentLayout) {
        if (cOUIPanelContentLayout != null) {
            cOUIPanelContentLayout.setPadding(0, 0, 0, 0);
        }
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public boolean releaseData() {
        resetInnerStatus();
        COUISpringAnimation cOUISpringAnimation = this.mOffsetSpringAnimation;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mOffsetSpringAnimation.cancel();
            this.mOffsetSpringAnimation = null;
        }
        this.mOffsetValueHolder = null;
        resetPaddingAndMargin();
        this.mCurrentKeyboardHeight = 0;
        this.mBottomMarginOfDesignBottomSheet = 0;
        this.mLastBottomDesignSheetOrPanelContentLayout = null;
        this.mLastCouiPanelContentLayout = null;
        return true;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void resetInnerStatus() {
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void setIgnoreHideKeyboardAnim(boolean z2) {
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void setWindowType(int i3) {
        this.mWindowType = i3;
    }
}