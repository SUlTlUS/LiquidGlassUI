package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.support.preference.R;

/* JADX INFO: loaded from: classes.dex */
public class COUICustomLinearLayoutForPreference extends LinearLayout {
    private String TAG;
    private boolean mAHavePriority;
    private boolean mBStickToC;
    private int mDefaultMarginEnd;
    private int mMessageLayoutMarginEndInRight;
    private int mReddotMarginEndInRightHasAssignment;
    private int mReddotMarginEndInRightNoAssignment;
    private boolean mSetMessageLayoutMarginEnd;
    private int mViewLowPriorityMinWidth;
    private View viewA;
    private View viewB;
    private View viewC;

    public COUICustomLinearLayoutForPreference(Context context) {
        this(context, null);
    }

    private void customMeasure(int i3, int i4) {
        COUICustomLinearLayoutForPreference cOUICustomLinearLayoutForPreference;
        int iMax;
        int realWidthWithMargin;
        int marginWidth;
        int iMin;
        int iMin2;
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec((View.MeasureSpec.getSize(i3) - getPaddingLeft()) - getPaddingRight(), View.MeasureSpec.getMode(i3));
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec((View.MeasureSpec.getSize(i4) - getPaddingTop()) - getPaddingBottom(), View.MeasureSpec.getMode(i4));
        if (this.viewA.getVisibility() != 8) {
            cOUICustomLinearLayoutForPreference = this;
            cOUICustomLinearLayoutForPreference.measureChildWithMargins(this.viewA, iMakeMeasureSpec, 0, iMakeMeasureSpec2, 0);
            iMax = Math.max(cOUICustomLinearLayoutForPreference.viewA.getMeasuredHeight(), 0);
        } else {
            cOUICustomLinearLayoutForPreference = this;
            cOUICustomLinearLayoutForPreference.measureChild(cOUICustomLinearLayoutForPreference.viewA, View.MeasureSpec.makeMeasureSpec(0, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 1073741824));
            iMax = 0;
        }
        if (cOUICustomLinearLayoutForPreference.viewB.getVisibility() != 8) {
            cOUICustomLinearLayoutForPreference.measureChildWithMargins(cOUICustomLinearLayoutForPreference.viewB, iMakeMeasureSpec, 0, iMakeMeasureSpec2, 0);
            iMax = Math.max(cOUICustomLinearLayoutForPreference.viewB.getMeasuredHeight(), iMax);
        } else {
            cOUICustomLinearLayoutForPreference.measureChild(cOUICustomLinearLayoutForPreference.viewB, View.MeasureSpec.makeMeasureSpec(0, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 1073741824));
        }
        if (cOUICustomLinearLayoutForPreference.viewC.getVisibility() != 8) {
            cOUICustomLinearLayoutForPreference.measureChildWithMargins(cOUICustomLinearLayoutForPreference.viewC, iMakeMeasureSpec, 0, iMakeMeasureSpec2, 0);
            iMax = Math.max(cOUICustomLinearLayoutForPreference.viewC.getMeasuredHeight(), iMax);
        } else {
            cOUICustomLinearLayoutForPreference.measureChild(cOUICustomLinearLayoutForPreference.viewC, View.MeasureSpec.makeMeasureSpec(0, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 1073741824));
        }
        int paddingLeft = cOUICustomLinearLayoutForPreference.getPaddingLeft();
        int measuredWidth = cOUICustomLinearLayoutForPreference.getMeasuredWidth() - cOUICustomLinearLayoutForPreference.getPaddingRight();
        int i5 = measuredWidth - paddingLeft;
        if (cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewA) + cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewB) + cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewC) > i5) {
            if (cOUICustomLinearLayoutForPreference.mAHavePriority) {
                realWidthWithMargin = cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewC);
                marginWidth = cOUICustomLinearLayoutForPreference.getMarginWidth(cOUICustomLinearLayoutForPreference.viewC);
            } else {
                realWidthWithMargin = cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewA);
                marginWidth = cOUICustomLinearLayoutForPreference.getMarginWidth(cOUICustomLinearLayoutForPreference.viewA);
            }
            int i6 = realWidthWithMargin - marginWidth;
            int i7 = cOUICustomLinearLayoutForPreference.mViewLowPriorityMinWidth;
            if (i6 >= i7) {
                i6 = i7;
            }
            if (cOUICustomLinearLayoutForPreference.mAHavePriority) {
                iMin2 = Math.min(cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewA), (i5 - (i6 + cOUICustomLinearLayoutForPreference.getMarginWidth(cOUICustomLinearLayoutForPreference.viewC))) - cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewB));
                iMin = measuredWidth - Math.max(measuredWidth - cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewC), (paddingLeft + iMin2) + cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewB));
            } else {
                iMin = Math.min(cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewC), (i5 - (i6 + cOUICustomLinearLayoutForPreference.getMarginWidth(cOUICustomLinearLayoutForPreference.viewA))) - cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewB));
                iMin2 = Math.min(cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewA), (i5 - iMin) - cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewB));
            }
            int realWidthWithMargin2 = cOUICustomLinearLayoutForPreference.getRealWidthWithMargin(cOUICustomLinearLayoutForPreference.viewB);
            if (cOUICustomLinearLayoutForPreference.viewA.getVisibility() != 8) {
                View view = cOUICustomLinearLayoutForPreference.viewA;
                cOUICustomLinearLayoutForPreference.setChildSize(view, iMin2 - cOUICustomLinearLayoutForPreference.getMarginWidth(view));
                iMax = Math.max(cOUICustomLinearLayoutForPreference.viewA.getMeasuredHeight(), iMax);
            }
            if (cOUICustomLinearLayoutForPreference.viewB.getVisibility() != 8) {
                View view2 = cOUICustomLinearLayoutForPreference.viewB;
                cOUICustomLinearLayoutForPreference.setChildSize(view2, realWidthWithMargin2 - cOUICustomLinearLayoutForPreference.getMarginWidth(view2));
                iMax = Math.max(cOUICustomLinearLayoutForPreference.viewB.getMeasuredHeight(), iMax);
            }
            if (cOUICustomLinearLayoutForPreference.viewC.getVisibility() != 8) {
                View view3 = cOUICustomLinearLayoutForPreference.viewC;
                cOUICustomLinearLayoutForPreference.setChildSize(view3, iMin - cOUICustomLinearLayoutForPreference.getMarginWidth(view3));
                iMax = Math.max(cOUICustomLinearLayoutForPreference.viewC.getMeasuredHeight(), iMax);
            }
            cOUICustomLinearLayoutForPreference.setMeasuredDimension(View.MeasureSpec.getSize(i3), View.MeasureSpec.makeMeasureSpec(iMax + cOUICustomLinearLayoutForPreference.getPaddingTop() + cOUICustomLinearLayoutForPreference.getPaddingBottom(), View.MeasureSpec.getMode(i4)));
        }
    }

    private int getMarginHeight(View view) {
        if (view.getVisibility() != 8) {
            return ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).bottomMargin;
        }
        return 0;
    }

    private int getMarginLeft(View view) {
        if (view.getVisibility() != 8) {
            return ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).leftMargin;
        }
        return 0;
    }

    private int getMarginTop(View view) {
        if (view.getVisibility() != 8) {
            return ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin;
        }
        return 0;
    }

    private int getMarginWidth(View view) {
        if (view.getVisibility() != 8) {
            return ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).leftMargin + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).rightMargin;
        }
        return 0;
    }

    private int getRealHeight(View view) {
        if (view.getVisibility() != 8) {
            return view.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).bottomMargin;
        }
        return 0;
    }

    private int getRealWidthWithMargin(View view) {
        if (view.getVisibility() != 8) {
            return view.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).leftMargin + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).rightMargin;
        }
        return 0;
    }

    private void init(Context context, AttributeSet attributeSet, int i3) {
        setOrientation(0);
        this.mReddotMarginEndInRightNoAssignment = getContext().getResources().getDimensionPixelSize(R.dimen.support_preference_reddot_margin_end_in_right_noassignment);
        this.mReddotMarginEndInRightHasAssignment = getContext().getResources().getDimensionPixelSize(R.dimen.support_preference_reddot_margin_end_in_right_hasassignment);
        this.mMessageLayoutMarginEndInRight = getContext().getResources().getDimensionPixelSize(R.dimen.support_preference_title_margin_end_in_right);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUICustomLinearLayoutForPreference, i3, 0);
        this.mDefaultMarginEnd = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUICustomLinearLayoutForPreference_couiMessageLayoutMarginEnd, 0);
        this.mBStickToC = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICustomLinearLayoutForPreference_couiBStickToC, this.mBStickToC);
        this.mAHavePriority = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICustomLinearLayoutForPreference_couiAHavePriority, this.mAHavePriority);
        this.mSetMessageLayoutMarginEnd = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICustomLinearLayoutForPreference_couiMarginEndOfA, this.mSetMessageLayoutMarginEnd);
        typedArrayObtainStyledAttributes.recycle();
        this.mViewLowPriorityMinWidth = context.getResources().getDimensionPixelSize(R.dimen.assignment_in_right_low_priority_min_width);
    }

    private boolean isRtlMode() {
        return getLayoutDirection() == 1;
    }

    private void setChildSize(View view, int i3) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0041  */
    private boolean setReddotAndMessageLayoutMarginEnd() {
        boolean z2;
        View view;
        View view2;
        View view3 = this.viewB;
        if (view3 == null || view3.getVisibility() != 0) {
            z2 = false;
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.viewB.getLayoutParams();
            View view4 = this.viewC;
            if (view4 == null || view4.getVisibility() != 0) {
                int marginEnd = layoutParams.getMarginEnd();
                int i3 = this.mReddotMarginEndInRightNoAssignment;
                if (marginEnd != i3) {
                    layoutParams.setMarginEnd(i3);
                    this.viewB.setLayoutParams(layoutParams);
                    z2 = true;
                } else {
                    z2 = false;
                }
            } else {
                int marginEnd2 = layoutParams.getMarginEnd();
                int i4 = this.mReddotMarginEndInRightHasAssignment;
                if (marginEnd2 != i4) {
                    layoutParams.setMarginEnd(i4);
                    this.viewB.setLayoutParams(layoutParams);
                    z2 = true;
                } else {
                    z2 = false;
                }
            }
        }
        if (this.mSetMessageLayoutMarginEnd && (view = this.viewA) != null && view.getVisibility() == 0) {
            View view5 = this.viewB;
            if ((view5 == null || view5.getVisibility() != 0) && ((view2 = this.viewC) == null || view2.getVisibility() != 0)) {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.viewA.getLayoutParams();
                int marginEnd3 = layoutParams2.getMarginEnd();
                int i5 = this.mDefaultMarginEnd;
                if (marginEnd3 != i5) {
                    layoutParams2.setMarginEnd(i5);
                    this.viewA.setLayoutParams(layoutParams2);
                    return true;
                }
            } else {
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.viewA.getLayoutParams();
                int marginEnd4 = layoutParams3.getMarginEnd();
                int i6 = this.mMessageLayoutMarginEndInRight;
                if (marginEnd4 != i6) {
                    layoutParams3.setMarginEnd(i6);
                    this.viewA.setLayoutParams(layoutParams3);
                    return true;
                }
            }
        }
        return z2;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int realWidthWithMargin;
        int realWidthWithMargin2;
        int paddingLeft = getPaddingLeft();
        int measuredWidth = getMeasuredWidth() - getPaddingRight();
        int paddingTop = getPaddingTop();
        int measuredHeight = (getMeasuredHeight() - getPaddingBottom()) - paddingTop;
        int realHeight = ((measuredHeight - getRealHeight(this.viewA)) / 2) + paddingTop;
        int realHeight2 = ((measuredHeight - getRealHeight(this.viewC)) / 2) + paddingTop;
        int realHeight3 = paddingTop + ((measuredHeight - getRealHeight(this.viewB)) / 2);
        if (isRtlMode()) {
            int realWidthWithMargin3 = measuredWidth - getRealWidthWithMargin(this.viewA);
            realWidthWithMargin = this.mBStickToC ? getRealWidthWithMargin(this.viewC) + paddingLeft : realWidthWithMargin3 - getRealWidthWithMargin(this.viewB);
            realWidthWithMargin2 = paddingLeft;
            paddingLeft = realWidthWithMargin3;
        } else {
            realWidthWithMargin2 = measuredWidth - getRealWidthWithMargin(this.viewC);
            realWidthWithMargin = this.mBStickToC ? realWidthWithMargin2 - getRealWidthWithMargin(this.viewB) : getRealWidthWithMargin(this.viewA) + paddingLeft;
        }
        View view = this.viewA;
        view.layout(getMarginLeft(view) + paddingLeft, getMarginTop(this.viewA) + realHeight, ((paddingLeft + getMarginLeft(this.viewA)) + getRealWidthWithMargin(this.viewA)) - getMarginWidth(this.viewA), ((realHeight + getMarginTop(this.viewA)) + getRealHeight(this.viewA)) - getMarginHeight(this.viewA));
        View view2 = this.viewC;
        view2.layout(getMarginLeft(view2) + realWidthWithMargin2, getMarginTop(this.viewC) + realHeight2, ((realWidthWithMargin2 + getMarginLeft(this.viewC)) + getRealWidthWithMargin(this.viewC)) - getMarginWidth(this.viewC), ((realHeight2 + getMarginTop(this.viewC)) + getRealHeight(this.viewC)) - getMarginHeight(this.viewC));
        View view3 = this.viewB;
        view3.layout(getMarginLeft(view3) + realWidthWithMargin, getMarginTop(this.viewB) + realHeight3, ((realWidthWithMargin + getMarginLeft(this.viewB)) + getRealWidthWithMargin(this.viewB)) - getMarginWidth(this.viewB), ((realHeight3 + getMarginTop(this.viewB)) + getRealHeight(this.viewB)) - getMarginHeight(this.viewB));
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        this.viewA = getChildAt(0);
        this.viewB = getChildAt(1);
        this.viewC = getChildAt(2);
        if (setReddotAndMessageLayoutMarginEnd()) {
            super.onMeasure(i3, i4);
        }
        customMeasure(i3, i4);
    }

    public COUICustomLinearLayoutForPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICustomLinearLayoutForPreference(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.TAG = "COUICustomLinearLayout";
        this.mBStickToC = true;
        this.mAHavePriority = true;
        this.mSetMessageLayoutMarginEnd = true;
        init(context, attributeSet, i3);
    }
}