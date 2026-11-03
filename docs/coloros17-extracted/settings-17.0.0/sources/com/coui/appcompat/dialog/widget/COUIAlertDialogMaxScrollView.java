package com.coui.appcompat.dialog.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIAlertDialogMaxScrollView extends ScrollView {
    private int mLandscapeMaxHeight;
    private int mMaxHeight;
    private int mMaxWidth;
    private OnSizeChangeListener mOnSizeChangeListener;

    public interface OnSizeChangeListener {
        void onSizeChange(int i3, int i4, int i5, int i6);
    }

    public COUIAlertDialogMaxScrollView(Context context) {
        super(context);
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        boolean z2;
        super.onMeasure(i3, i4);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int i5 = this.mMaxWidth;
        boolean z3 = true;
        if (i5 == 0 || measuredWidth <= i5) {
            z2 = false;
        } else {
            i3 = View.MeasureSpec.makeMeasureSpec(i5, 1073741824);
            z2 = true;
        }
        int i6 = this.mMaxHeight;
        if (measuredHeight > i6) {
            i4 = View.MeasureSpec.makeMeasureSpec(i6, 1073741824);
        } else {
            z3 = z2;
        }
        if (z3) {
            super.onMeasure(i3, i4);
        }
    }

    @Override // android.widget.ScrollView, android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        OnSizeChangeListener onSizeChangeListener = this.mOnSizeChangeListener;
        if (onSizeChangeListener != null) {
            onSizeChangeListener.onSizeChange(i3, i4, i5, i6);
        }
    }

    public void setMaxHeight(int i3) {
        this.mMaxHeight = i3;
    }

    public void setMaxWidth(int i3) {
        this.mMaxWidth = i3;
    }

    public void setOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        this.mOnSizeChangeListener = onSizeChangeListener;
    }

    public COUIAlertDialogMaxScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIAlertDialogMaxLinearLayout);
        this.mMaxWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIAlertDialogMaxLinearLayout_maxWidth, 0);
        this.mMaxHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIAlertDialogMaxLinearLayout_maxHeight, 0);
        typedArrayObtainStyledAttributes.recycle();
    }
}