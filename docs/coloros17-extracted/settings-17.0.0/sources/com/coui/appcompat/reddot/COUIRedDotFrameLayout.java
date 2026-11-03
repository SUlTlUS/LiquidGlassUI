package com.coui.appcompat.reddot;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.support.reddot.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIRedDotFrameLayout extends FrameLayout {
    public static final int CIRCLE_TYPE = 1;
    public static final int RECTANGLE_TYPE = 0;
    String TAG;
    private View mAnchorView;
    private int mAnchorViewShapeType;
    private int mAnchorViewSize;
    private int mCouiDotDiameter;
    private int mCouiDotViewHeight;
    private int mMarginTopAndLeftOfAnchorView;
    private int mMarginTopAndLeftOfReddot;
    private int mRedDotMode;
    private String mRedDotText;
    private COUIHintRedDot mRedDotView;
    private final Runnable mRequestLayoutRunnable;

    public COUIRedDotFrameLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIRedDotFrameLayout cOUIRedDotFrameLayout, COUIHintRedDot cOUIHintRedDot) {
        cOUIRedDotFrameLayout.addView(cOUIHintRedDot);
    }

    private void addRedDot() {
        if (this.mRedDotMode != 0) {
            COUIHintRedDot cOUIHintRedDot = new COUIHintRedDot(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            cOUIHintRedDot.setLayoutParams(layoutParams);
            layoutParams.gravity = 8388661;
            cOUIHintRedDot.setPointMode(this.mRedDotMode);
            int i3 = this.mRedDotMode;
            if (i3 == 2 || i3 == 5) {
                cOUIHintRedDot.setViewHeight(this.mCouiDotViewHeight);
                cOUIHintRedDot.setPointText(this.mRedDotText);
            } else {
                cOUIHintRedDot.setDotDiameter(this.mCouiDotDiameter);
            }
            post(new b(this, cOUIHintRedDot));
            refresh();
        }
    }

    public static /* synthetic */ void b(COUIRedDotFrameLayout cOUIRedDotFrameLayout) {
        cOUIRedDotFrameLayout.requestLayout();
    }

    private void init(AttributeSet attributeSet, int i3) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_medium_icon_size);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_large_icon_size);
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.COUIRedDotFrameLayout, i3, 0);
            this.mRedDotMode = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIRedDotFrameLayout_couiHintRedPointMode, 0);
            this.mRedDotText = typedArrayObtainStyledAttributes.getString(R.styleable.COUIRedDotFrameLayout_couiHintRedPointText);
            this.mAnchorViewShapeType = typedArrayObtainStyledAttributes.getInt(R.styleable.COUIRedDotFrameLayout_anchorViewShapeType, 0);
            this.mAnchorViewSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIRedDotFrameLayout_anchorViewDpSize, dimensionPixelSize);
            typedArrayObtainStyledAttributes.recycle();
        }
        int i4 = this.mRedDotMode;
        if (i4 == 0) {
            return;
        }
        int i5 = this.mAnchorViewSize;
        if (i5 < dimensionPixelSize) {
            if (i4 == 1 || i4 == 4) {
                this.mCouiDotDiameter = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_small_reddot_size);
            }
            if (this.mAnchorViewShapeType == 0) {
                int i6 = this.mRedDotMode;
                if (i6 == 2 || i6 == 5) {
                    this.mMarginTopAndLeftOfAnchorView = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_small_number_topend_margin_rectangle);
                } else {
                    this.mMarginTopAndLeftOfAnchorView = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_small_icon_topend_margin_rectangle);
                }
            } else {
                int i7 = this.mRedDotMode;
                if (i7 == 1 || i7 == 4) {
                    this.mMarginTopAndLeftOfReddot = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_small_icon_topend_margin_circle);
                }
            }
        } else if (i5 >= dimensionPixelSize2) {
            if (i4 == 2 || i4 == 5) {
                this.mCouiDotViewHeight = getResources().getDimensionPixelSize(R.dimen.coui_height_large);
            } else {
                this.mCouiDotDiameter = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_large_reddot_size);
            }
            if (this.mAnchorViewShapeType == 0) {
                int i8 = this.mRedDotMode;
                if (i8 == 2 || i8 == 5) {
                    this.mMarginTopAndLeftOfAnchorView = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_large_number_topend_margin_rectangle);
                } else {
                    this.mMarginTopAndLeftOfAnchorView = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_large_icon_topend_margin_rectangle);
                }
            } else {
                int i9 = this.mRedDotMode;
                if (i9 == 1 || i9 == 4) {
                    this.mMarginTopAndLeftOfReddot = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_large_icon_topend_margin_circle);
                }
            }
        } else {
            if (i4 == 1 || i4 == 4) {
                this.mCouiDotDiameter = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_medium_reddot_size);
            }
            if (this.mAnchorViewShapeType == 0) {
                int i10 = this.mRedDotMode;
                if (i10 == 2 || i10 == 5) {
                    this.mMarginTopAndLeftOfAnchorView = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_medium_number_topend_margin_rectangle);
                } else {
                    this.mMarginTopAndLeftOfAnchorView = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_medium_icon_topend_margin_rectangle);
                }
            } else {
                int i11 = this.mRedDotMode;
                if (i11 == 1 || i11 == 4) {
                    this.mMarginTopAndLeftOfReddot = getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_medium_icon_topend_margin_circle);
                }
            }
        }
        if (this.mRedDotMode == 4) {
            this.mCouiDotDiameter += getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_mode_stroke_extra_diameter);
        }
        if (this.mRedDotMode == 5) {
            this.mCouiDotViewHeight += getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_mode_stroke_extra_diameter);
        }
    }

    private boolean isRtlMode() {
        return getLayoutDirection() == 1;
    }

    private void refresh() {
        removeCallbacks(this.mRequestLayoutRunnable);
        post(this.mRequestLayoutRunnable);
    }

    private void setChildView() {
        if (this.mRedDotView == null || this.mAnchorView == null) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt instanceof COUIHintRedDot) {
                    this.mRedDotView = (COUIHintRedDot) childAt;
                } else {
                    this.mAnchorView = childAt;
                }
            }
        }
    }

    public COUIHintRedDot getRedDotView() {
        return this.mRedDotView;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        if (this.mRedDotMode == 0) {
            return;
        }
        View view = this.mAnchorView;
        if (view == null || this.mRedDotView == null) {
            if (view == null || this.mRedDotView != null) {
                return;
            }
            view.layout(0, 0, view.getMeasuredWidth(), this.mAnchorView.getMeasuredHeight());
            return;
        }
        if (isRtlMode()) {
            View view2 = this.mAnchorView;
            int i7 = this.mMarginTopAndLeftOfAnchorView;
            view2.layout(i7, i7, view2.getMeasuredWidth() + i7, this.mMarginTopAndLeftOfAnchorView + this.mAnchorView.getMeasuredHeight());
            COUIHintRedDot cOUIHintRedDot = this.mRedDotView;
            int i8 = this.mMarginTopAndLeftOfReddot;
            cOUIHintRedDot.layout(i8, i8, cOUIHintRedDot.getWidth() + i8, this.mMarginTopAndLeftOfReddot + this.mRedDotView.getHeight());
            return;
        }
        View view3 = this.mAnchorView;
        view3.layout(0, this.mMarginTopAndLeftOfAnchorView, view3.getMeasuredWidth(), this.mMarginTopAndLeftOfAnchorView + this.mAnchorView.getMeasuredHeight());
        COUIHintRedDot cOUIHintRedDot2 = this.mRedDotView;
        int width = getWidth() - this.mRedDotView.getWidth();
        int i9 = this.mMarginTopAndLeftOfReddot;
        int width2 = getWidth();
        int i10 = this.mMarginTopAndLeftOfReddot;
        cOUIHintRedDot2.layout(width - i9, i9, width2 - i10, i10 + this.mRedDotView.getHeight());
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (this.mRedDotMode == 0) {
            return;
        }
        setChildView();
        View view = this.mAnchorView;
        if (view != null && this.mRedDotView != null) {
            setMeasuredDimension(getMeasuredWidth() + this.mMarginTopAndLeftOfAnchorView, getMeasuredHeight() + this.mMarginTopAndLeftOfAnchorView);
        } else {
            if (view == null || this.mRedDotView != null) {
                return;
            }
            setMeasuredDimension(view.getWidth(), this.mAnchorView.getHeight());
        }
    }

    public void removeRedDot() {
        COUIHintRedDot cOUIHintRedDot = this.mRedDotView;
        if (cOUIHintRedDot != null) {
            removeView(cOUIHintRedDot);
            this.mRedDotView = null;
            refresh();
        }
    }

    public void showReddot(int i3, String str) {
        showReddot(i3, str, getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_medium_icon_size), 0);
    }

    public COUIRedDotFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIRedDotFrameLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.TAG = "COUIRedDotFrameLayout";
        this.mRedDotMode = 0;
        this.mAnchorViewShapeType = 0;
        this.mCouiDotViewHeight = getResources().getDimensionPixelSize(R.dimen.coui_height);
        this.mRequestLayoutRunnable = new a(this);
        init(attributeSet, i3);
        addRedDot();
    }

    public void showReddot(int i3, String str, int i4, int i5) {
        this.mAnchorViewShapeType = i5;
        this.mAnchorViewSize = i4;
        this.mRedDotMode = i3;
        this.mRedDotText = str;
        init(null, 0);
        addRedDot();
    }
}