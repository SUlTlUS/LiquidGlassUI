package com.coui.appcompat.panel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.panel.R;

/* JADX INFO: loaded from: classes7.dex */
public class IgnoreWindowInsetsFrameLayout extends FrameLayout {
    private boolean mCouiPanelEdgeToEdgeEnable;
    private boolean mIsIgnoreWindowInsetsBottom;
    private boolean mIsIgnoreWindowInsetsLeft;
    private boolean mIsIgnoreWindowInsetsRight;
    private boolean mIsIgnoreWindowInsetsTop;
    private boolean mUsingIgnoreWindowInsetsBottom;
    private int mWindowInsetsBottomOffset;
    private int mWindowInsetsLeftOffset;
    private int mWindowInsetsRightOffset;
    private int mWindowInsetsTopOffset;

    public IgnoreWindowInsetsFrameLayout(Context context) {
        super(context);
        this.mIsIgnoreWindowInsetsLeft = true;
        this.mIsIgnoreWindowInsetsTop = true;
        this.mIsIgnoreWindowInsetsRight = true;
        this.mIsIgnoreWindowInsetsBottom = true;
        this.mUsingIgnoreWindowInsetsBottom = true;
    }

    private void initAttr(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.IgnoreWindowInsetsFrameLayout);
            this.mIsIgnoreWindowInsetsLeft = typedArrayObtainStyledAttributes.getBoolean(R.styleable.IgnoreWindowInsetsFrameLayout_ignoreWindowInsetsLeft, true);
            this.mIsIgnoreWindowInsetsTop = typedArrayObtainStyledAttributes.getBoolean(R.styleable.IgnoreWindowInsetsFrameLayout_ignoreWindowInsetsTop, true);
            this.mIsIgnoreWindowInsetsRight = typedArrayObtainStyledAttributes.getBoolean(R.styleable.IgnoreWindowInsetsFrameLayout_ignoreWindowInsetsRight, true);
            this.mIsIgnoreWindowInsetsBottom = typedArrayObtainStyledAttributes.getBoolean(R.styleable.IgnoreWindowInsetsFrameLayout_ignoreWindowInsetsBottom, true);
            typedArrayObtainStyledAttributes.recycle();
            syncIgnoreBottomInsetsForNavigationMode();
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int iMax;
        syncIgnoreBottomInsetsForNavigationMode();
        int i3 = windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        int iMax2 = this.mIsIgnoreWindowInsetsLeft ? 0 : Math.max(0, windowInsets.getSystemWindowInsetLeft() + this.mWindowInsetsLeftOffset);
        int iMax3 = this.mIsIgnoreWindowInsetsTop ? 0 : Math.max(0, windowInsets.getSystemWindowInsetTop() + this.mWindowInsetsTopOffset);
        int iMax4 = this.mIsIgnoreWindowInsetsRight ? 0 : Math.max(0, windowInsets.getSystemWindowInsetRight() + this.mWindowInsetsRightOffset);
        if (this.mUsingIgnoreWindowInsetsBottom) {
            iMax = 0;
        } else {
            if (this.mCouiPanelEdgeToEdgeEnable) {
                i3 = 0;
            }
            iMax = Math.max(0, i3 + this.mWindowInsetsBottomOffset);
        }
        setPadding(iMax2, iMax3, iMax4, iMax);
        this.mWindowInsetsLeftOffset = 0;
        this.mWindowInsetsTopOffset = 0;
        this.mWindowInsetsRightOffset = 0;
        this.mWindowInsetsBottomOffset = 0;
        return windowInsets.consumeSystemWindowInsets();
    }

    public void setCouiPanelEdgeToEdgeEnable(boolean z2) {
        this.mCouiPanelEdgeToEdgeEnable = z2;
    }

    public void setIgnoreWindowInsetsBottom(boolean z2) {
        this.mIsIgnoreWindowInsetsBottom = z2;
    }

    public void setIgnoreWindowInsetsLeft(boolean z2) {
        this.mIsIgnoreWindowInsetsLeft = z2;
    }

    public void setIgnoreWindowInsetsRight(boolean z2) {
        this.mIsIgnoreWindowInsetsRight = z2;
    }

    public void setIgnoreWindowInsetsTop(boolean z2) {
        this.mIsIgnoreWindowInsetsTop = z2;
    }

    public void setWindowInsetsBottomOffset(int i3) {
        this.mWindowInsetsBottomOffset = i3;
    }

    public void setWindowInsetsLeftOffset(int i3) {
        this.mWindowInsetsLeftOffset = i3;
    }

    public void setWindowInsetsRightOffset(int i3) {
        this.mWindowInsetsRightOffset = i3;
    }

    public void setWindowInsetsTopOffset(int i3) {
        this.mWindowInsetsTopOffset = i3;
    }

    public void syncIgnoreBottomInsetsForNavigationMode() {
        if (COUINavigationBarUtil.isGestureNavigation(getContext()) || UIUtil.isInFreeFormModeWindowMode(getContext())) {
            this.mUsingIgnoreWindowInsetsBottom = this.mIsIgnoreWindowInsetsBottom;
            return;
        }
        this.mUsingIgnoreWindowInsetsBottom = false;
        setFitsSystemWindows(false);
        setClipToPadding(true);
    }

    public IgnoreWindowInsetsFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsIgnoreWindowInsetsLeft = true;
        this.mIsIgnoreWindowInsetsTop = true;
        this.mIsIgnoreWindowInsetsRight = true;
        this.mIsIgnoreWindowInsetsBottom = true;
        this.mUsingIgnoreWindowInsetsBottom = true;
        initAttr(attributeSet);
    }

    public IgnoreWindowInsetsFrameLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mIsIgnoreWindowInsetsLeft = true;
        this.mIsIgnoreWindowInsetsTop = true;
        this.mIsIgnoreWindowInsetsRight = true;
        this.mIsIgnoreWindowInsetsBottom = true;
        this.mUsingIgnoreWindowInsetsBottom = true;
        initAttr(attributeSet);
    }
}