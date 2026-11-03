package com.coui.appcompat.panel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

/* JADX INFO: loaded from: classes7.dex */
public abstract class COUIAbsPanelAdjustResizeHelper {
    private boolean mCouiPanelEdgeToEdgeEnable;

    public void adjustResize(Context context, ViewGroup viewGroup, WindowInsets windowInsets, View view, boolean z2) {
    }

    public int getMarginBottomValue() {
        return -1;
    }

    public int getPaddingBottomOffset() {
        return -1;
    }

    public float getTranslateOffset() {
        return -1.0f;
    }

    public int getWindowType() {
        return -1;
    }

    public boolean isCouiPanelEdgeToEdgeEnable() {
        return this.mCouiPanelEdgeToEdgeEnable;
    }

    public void recoveryScrollingParentViewPaddingBottom(COUIPanelContentLayout cOUIPanelContentLayout) {
    }

    public boolean releaseData() {
        return false;
    }

    public void resetInnerStatus() {
    }

    public void setCouiPanelEdgeToEdgeEnable(boolean z2) {
        this.mCouiPanelEdgeToEdgeEnable = z2;
    }

    public void setIgnoreHideKeyboardAnim(boolean z2) {
    }

    public void setWindowType(int i3) {
    }
}