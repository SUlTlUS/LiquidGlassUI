package com.coui.appcompat.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import com.support.toolbar.R;

/* JADX INFO: loaded from: classes7.dex */
public class AppBarBlurOverlayView extends View {
    private static final float EXTRA_HEIGHT_DP_FIXED = 8.0f;
    private static final float EXTRA_HEIGHT_DP_SCROLL_DRIVEN = 24.0f;
    private int mExtraHeightPx;
    private boolean mScrollDrivenAlphaEnabled;

    public AppBarBlurOverlayView(Context context) {
        this(context, null);
    }

    private void applyInitialAppearance() {
        setAlpha(this.mScrollDrivenAlphaEnabled ? 0.0f : 1.0f);
        if (!this.mScrollDrivenAlphaEnabled || getVisibility() == 4) {
            return;
        }
        setVisibility(4);
    }

    private int computeExtraHeightPx(Context context, boolean z2) {
        return Math.round(TypedValue.applyDimension(1, z2 ? 24.0f : 8.0f, context.getResources().getDisplayMetrics()));
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public int getExtraHeightPx() {
        return this.mExtraHeightPx;
    }

    public boolean isScrollDrivenAlphaEnabled() {
        return this.mScrollDrivenAlphaEnabled;
    }

    public void setExtraHeightPx(int i3) {
        int iMax = Math.max(0, i3);
        if (this.mExtraHeightPx != iMax) {
            this.mExtraHeightPx = iMax;
            requestLayout();
        }
    }

    public AppBarBlurOverlayView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AppBarBlurOverlayView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mScrollDrivenAlphaEnabled = false;
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AppBarBlurOverlayView, i3, 0);
            this.mScrollDrivenAlphaEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.AppBarBlurOverlayView_scrollDrivenAlphaEnabled, false);
            typedArrayObtainStyledAttributes.recycle();
        }
        this.mExtraHeightPx = computeExtraHeightPx(context, this.mScrollDrivenAlphaEnabled);
        setClickable(false);
        setFocusable(false);
        setFocusableInTouchMode(false);
        setImportantForAccessibility(2);
        applyInitialAppearance();
    }
}