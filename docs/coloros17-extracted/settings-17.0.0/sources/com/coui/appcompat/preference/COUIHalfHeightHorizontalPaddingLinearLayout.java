package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIHalfHeightHorizontalPaddingLinearLayout extends LinearLayout {
    private static final int SPECIAL_COUNT = 2;
    private View mEndView;
    private int mFixPaddingEnd;
    private View mStartView;

    public COUIHalfHeightHorizontalPaddingLinearLayout(Context context) {
        super(context);
        this.mFixPaddingEnd = 0;
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (getChildCount() == 2) {
            this.mStartView = getChildAt(0);
            this.mEndView = getChildAt(1);
            if (this.mStartView.getMeasuredHeight() < this.mEndView.getMeasuredHeight()) {
                setPadding(getPaddingStart(), 0, getPaddingEnd(), 0);
            }
            int measuredHeight = getMeasuredHeight() / 2;
            if (measuredHeight >= this.mFixPaddingEnd) {
                return;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.support_preference_category_layout_title_margin_end_large);
            if (measuredHeight != getPaddingStart() || measuredHeight != getPaddingEnd() || layoutParams.getMarginEnd() == dimensionPixelSize || layoutParams.getMarginEnd() == 0) {
                setPadding(measuredHeight, getPaddingTop(), measuredHeight, getPaddingBottom());
                if (measuredHeight < this.mFixPaddingEnd) {
                    layoutParams.setMarginEnd((layoutParams.getMarginEnd() + this.mFixPaddingEnd) - measuredHeight);
                    setLayoutParams(layoutParams);
                }
                super.onMeasure(i3, i4);
            }
        }
    }

    public COUIHalfHeightHorizontalPaddingLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFixPaddingEnd = 0;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIHalfHeightHorizontalPaddingLinearLayout);
        this.mFixPaddingEnd = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHalfHeightHorizontalPaddingLinearLayout_fixPaddingEnd, this.mFixPaddingEnd);
        typedArrayObtainStyledAttributes.recycle();
    }
}