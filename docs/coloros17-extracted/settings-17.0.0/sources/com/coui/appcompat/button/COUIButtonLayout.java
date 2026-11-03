package com.coui.appcompat.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.support.button.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIButtonLayout extends LinearLayout {
    private boolean isForceSmallScreenWidth;
    private boolean isLimitHeight;
    private int mHorizontalLayoutPadding;
    private int mLimitMaxWidth;
    private int mMaxHeight;
    private int mMaxWidth;
    private OnButtonLayoutVisibilityChangedListener mOnButtonLayoutVisibilityChangedListener;
    private int mOrientation;
    private int mVerticalLayoutPadding;

    public interface OnButtonLayoutVisibilityChangedListener {
        void onButtonLayoutVisibilityChanged(int i3);
    }

    public COUIButtonLayout(Context context) {
        super(context);
        this.isLimitHeight = false;
        this.isForceSmallScreenWidth = false;
    }

    private void initResource() {
        this.mOrientation = getOrientation();
        this.mHorizontalLayoutPadding = getResources().getDimensionPixelSize(R.dimen.coui_horizontal_btn_margin);
        this.mVerticalLayoutPadding = getResources().getDimensionPixelSize(R.dimen.coui_horizontal_single_btn_margin);
    }

    private void setPaddingHorizontal(int i3) {
        if (i3 == 0) {
            i3 = getOrientation() == 0 ? this.mHorizontalLayoutPadding : this.mVerticalLayoutPadding;
        }
        setPaddingRelative(i3, getPaddingTop(), i3, getPaddingBottom());
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public boolean isForceSmallScreenWidth() {
        return this.isForceSmallScreenWidth;
    }

    public boolean isLimitHeight() {
        return this.isLimitHeight;
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int mode = View.MeasureSpec.getMode(i3);
        int i5 = this.mLimitMaxWidth;
        if (i5 <= 0 || !(mode == Integer.MIN_VALUE || mode == 1073741824)) {
            this.mMaxWidth = View.MeasureSpec.getSize(i3);
        } else {
            int iMin = Math.min(i5, View.MeasureSpec.getSize(i3));
            this.mMaxWidth = iMin;
            i3 = View.MeasureSpec.makeMeasureSpec(iMin, 1073741824);
        }
        this.mMaxHeight = View.MeasureSpec.getSize(i4);
        super.onMeasure(i3, i4);
    }

    @Override // android.view.View
    public void onVisibilityChanged(View view, int i3) {
        OnButtonLayoutVisibilityChangedListener onButtonLayoutVisibilityChangedListener;
        super.onVisibilityChanged(view, i3);
        if (view != this || (onButtonLayoutVisibilityChangedListener = this.mOnButtonLayoutVisibilityChangedListener) == null) {
            return;
        }
        onButtonLayoutVisibilityChangedListener.onButtonLayoutVisibilityChanged(i3);
    }

    public boolean setForceSmallScreenWidth(boolean z2) {
        this.isForceSmallScreenWidth = z2;
        return z2;
    }

    public void setHorizontalLayoutPadding(int i3) {
        this.mHorizontalLayoutPadding = i3;
        if (getOrientation() == 0) {
            setPaddingHorizontal(this.mHorizontalLayoutPadding);
        }
    }

    public void setLimitHeight(boolean z2) {
        this.isLimitHeight = z2;
    }

    public void setOnButtonLayoutVisibilityChangedListener(OnButtonLayoutVisibilityChangedListener onButtonLayoutVisibilityChangedListener) {
        this.mOnButtonLayoutVisibilityChangedListener = onButtonLayoutVisibilityChangedListener;
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i3) {
        super.setOrientation(i3);
        if (this.mOrientation != i3) {
            setPaddingHorizontal(0);
            this.mOrientation = i3;
        }
    }

    public void setVerticalLayoutPadding(int i3) {
        this.mVerticalLayoutPadding = i3;
        if (getOrientation() == 1) {
            setPaddingHorizontal(this.mVerticalLayoutPadding);
        }
    }

    public COUIButtonLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isLimitHeight = false;
        this.isForceSmallScreenWidth = false;
        initResource();
        if (getContext() != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, com.support.appcompat.R.styleable.COUIButtonLayout);
            this.mHorizontalLayoutPadding = (int) typedArrayObtainStyledAttributes.getDimension(com.support.appcompat.R.styleable.COUIButtonLayout_horizontalLayoutPadding, this.mHorizontalLayoutPadding);
            this.mVerticalLayoutPadding = (int) typedArrayObtainStyledAttributes.getDimension(com.support.appcompat.R.styleable.COUIButtonLayout_verticalLayoutPadding, this.mVerticalLayoutPadding);
            this.mLimitMaxWidth = (int) typedArrayObtainStyledAttributes.getDimension(com.support.appcompat.R.styleable.COUIButtonLayout_couiLimitMaxWidth, this.mLimitMaxWidth);
            typedArrayObtainStyledAttributes.recycle();
        }
        setPaddingHorizontal(0);
    }
}