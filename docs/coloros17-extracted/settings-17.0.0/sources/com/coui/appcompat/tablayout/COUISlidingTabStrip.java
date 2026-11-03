package com.coui.appcompat.tablayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.tablayout.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUISlidingTabStrip extends LinearLayout {
    private final Paint mBottomDividerPaint;
    private COUITabLayout mCOUITabLayout;
    private int mContentMinWidth;
    private int mHorizontalLargeMargin;
    private int mHorizontalMediumMargin;
    private int mHorizontalSmallMargin;
    private int mIndicatorAnimTime;
    private ValueAnimator mIndicatorAnimator;
    private int mIndicatorBackgroundHeight;
    private int mIndicatorBackgroundPaddingLeft;
    private int mIndicatorBackgroundPaddingRight;
    private final Paint mIndicatorBackgroundPaint;
    private int mIndicatorLeft;
    private int mIndicatorRight;
    private float mIndicatorWidthRatio;
    float mLastOffset;
    private int mLastPosition;
    float mLastSelectionOffset;
    private int mLayoutDirection;
    protected int mSelectedIndicatorHeight;
    private final Paint mSelectedIndicatorPaint;
    int mSelectedPosition;
    float mSelectionOffset;
    private int mTabMediumSpacing;
    private int mTabSmallSpacing;

    /* JADX INFO: renamed from: com.coui.appcompat.tablayout.COUISlidingTabStrip$1, reason: invalid class name */
    public class AnonymousClass1 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ int val$curStartColor;
        final /* synthetic */ COUITabView val$curTabView;
        final /* synthetic */ ArgbEvaluator val$evaluator;
        final /* synthetic */ int val$leftDiff;
        final /* synthetic */ int val$nextStartColor;
        final /* synthetic */ TextView val$nextView;
        final /* synthetic */ int val$startLeft;
        final /* synthetic */ int val$startRight;
        final /* synthetic */ int val$targetLeft;
        final /* synthetic */ int val$targetRight;
        final /* synthetic */ int val$widthDiff;

        public AnonymousClass1(TextView textView, ArgbEvaluator argbEvaluator, int i3, COUITabView cOUITabView, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            this.val$nextView = textView;
            this.val$evaluator = argbEvaluator;
            this.val$nextStartColor = i3;
            this.val$curTabView = cOUITabView;
            this.val$curStartColor = i4;
            this.val$startRight = i5;
            this.val$startLeft = i6;
            this.val$widthDiff = i7;
            this.val$leftDiff = i8;
            this.val$targetRight = i9;
            this.val$targetLeft = i10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int i3;
            int i4;
            float animatedFraction = valueAnimator.getAnimatedFraction();
            this.val$nextView.setTextColor(((Integer) this.val$evaluator.evaluate(animatedFraction, Integer.valueOf(this.val$nextStartColor), Integer.valueOf(COUISlidingTabStrip.access$000(COUISlidingTabStrip.this).mSelectedTextColor))).intValue());
            COUITabView cOUITabView = this.val$curTabView;
            if (cOUITabView != null && cOUITabView.getTextView() != null) {
                this.val$curTabView.getTextView().setTextColor(((Integer) this.val$evaluator.evaluate(animatedFraction, Integer.valueOf(this.val$curStartColor), Integer.valueOf(COUISlidingTabStrip.access$000(COUISlidingTabStrip.this).mNormalTextColor))).intValue());
            }
            COUISlidingTabStrip cOUISlidingTabStrip = COUISlidingTabStrip.this;
            if (cOUISlidingTabStrip.mLastOffset == 0.0f) {
                cOUISlidingTabStrip.mLastOffset = animatedFraction;
            }
            if (animatedFraction - cOUISlidingTabStrip.mLastOffset > 0.0f) {
                int i5 = this.val$startRight;
                int i6 = this.val$startLeft;
                i3 = (int) ((i5 - i6) + (this.val$widthDiff * animatedFraction));
                i4 = (int) (i6 + (this.val$leftDiff * animatedFraction));
            } else {
                int i7 = this.val$targetRight;
                int i8 = this.val$targetLeft;
                float f3 = 1.0f - animatedFraction;
                i3 = (int) ((i7 - i8) - (this.val$widthDiff * f3));
                i4 = (int) (i8 - (this.val$leftDiff * f3));
            }
            cOUISlidingTabStrip.setIndicatorPosition(i4, i3 + i4);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tablayout.COUISlidingTabStrip$2, reason: invalid class name */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$position;

        public AnonymousClass2(int i3) {
            this.val$position = i3;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISlidingTabStrip cOUISlidingTabStrip = COUISlidingTabStrip.this;
            cOUISlidingTabStrip.mSelectedPosition = this.val$position;
            cOUISlidingTabStrip.mSelectionOffset = 0.0f;
            cOUISlidingTabStrip.updateIndicatorPosition();
            COUISlidingTabStrip.access$000(COUISlidingTabStrip.this).resetTextColorAfterAnim();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tablayout.COUISlidingTabStrip$3, reason: invalid class name */
    public class AnonymousClass3 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ int val$startLeft;
        final /* synthetic */ int val$startRight;
        final /* synthetic */ int val$targetLeft;
        final /* synthetic */ int val$targetRight;

        public AnonymousClass3(int i3, int i4, int i5, int i6) {
            this.val$startLeft = i3;
            this.val$targetLeft = i4;
            this.val$startRight = i5;
            this.val$targetRight = i6;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float animatedFraction = valueAnimator.getAnimatedFraction();
            COUISlidingTabStrip.this.setIndicatorPosition(COUIAnimationUtils.lerp(this.val$startLeft, this.val$targetLeft, animatedFraction), COUIAnimationUtils.lerp(this.val$startRight, this.val$targetRight, animatedFraction));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.tablayout.COUISlidingTabStrip$4, reason: invalid class name */
    public class AnonymousClass4 extends AnimatorListenerAdapter {
        final /* synthetic */ COUITabView val$curTabView;
        final /* synthetic */ int val$position;
        final /* synthetic */ COUITabView val$tabView;

        public AnonymousClass4(int i3, COUITabView cOUITabView, COUITabView cOUITabView2) {
            this.val$position = i3;
            this.val$tabView = cOUITabView;
            this.val$curTabView = cOUITabView2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISlidingTabStrip cOUISlidingTabStrip = COUISlidingTabStrip.this;
            cOUISlidingTabStrip.mSelectedPosition = this.val$position;
            cOUISlidingTabStrip.mSelectionOffset = 0.0f;
            if (this.val$tabView.getTextView() != null) {
                this.val$tabView.getTextView().setTextColor(COUISlidingTabStrip.access$000(COUISlidingTabStrip.this).mSelectedTextColor);
            }
            if (this.val$curTabView.getTextView() != null) {
                this.val$curTabView.getTextView().setTextColor(COUISlidingTabStrip.access$000(COUISlidingTabStrip.this).mNormalTextColor);
            }
        }
    }

    public COUISlidingTabStrip(Context context, COUITabLayout cOUITabLayout) {
        super(context);
        this.mSelectedPosition = -1;
        this.mLayoutDirection = -1;
        this.mIndicatorLeft = -1;
        this.mIndicatorRight = -1;
        this.mLastPosition = 0;
        this.mIndicatorAnimTime = -1;
        this.mCOUITabLayout = cOUITabLayout;
        setWillNotDraw(false);
        this.mSelectedIndicatorPaint = new Paint();
        this.mBottomDividerPaint = new Paint();
        this.mIndicatorBackgroundPaint = new Paint();
        setGravity(17);
        this.mHorizontalLargeMargin = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_large_horizontal_margin);
        this.mHorizontalMediumMargin = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_medium_horizontal_margin);
        this.mHorizontalSmallMargin = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_small_horizontal_margin);
        this.mTabSmallSpacing = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_small_tab_spacing);
        this.mTabMediumSpacing = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_medium_tab_spacing);
        this.mContentMinWidth = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_content_min_width);
    }

    public static /* synthetic */ COUITabLayout access$000(COUISlidingTabStrip cOUISlidingTabStrip) {
        return cOUISlidingTabStrip.mCOUITabLayout;
    }

    private int getIndicatorLeft(int i3) {
        int width = ((this.mCOUITabLayout.getWidth() - this.mCOUITabLayout.getPaddingLeft()) - this.mCOUITabLayout.getPaddingRight()) - getWidth();
        return (!isLayoutRTL() || width <= 0) ? i3 : i3 + width;
    }

    private int getIndicatorRight(int i3) {
        int width = ((this.mCOUITabLayout.getWidth() - this.mCOUITabLayout.getPaddingLeft()) - this.mCOUITabLayout.getPaddingRight()) - getWidth();
        return (!isLayoutRTL() || width <= 0) ? i3 : i3 + width;
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private void measureChildWithRedDot(COUITabView cOUITabView, int i3, int i4) {
        if (cOUITabView.getTextView() != null) {
            cOUITabView.getTextView().getLayoutParams().width = -2;
        }
        if (cOUITabView.getTextView() == null || cOUITabView.getHintRedDot() == null || cOUITabView.getHintRedDot().getVisibility() == 8) {
            cOUITabView.measure(i3, i4);
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cOUITabView.getHintRedDot().getLayoutParams();
        layoutParams.gravity = 48;
        if (cOUITabView.getHintRedDot().getPointMode() == 0) {
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            cOUITabView.measure(i3, i4);
            return;
        }
        if (isLayoutRTL()) {
            layoutParams.rightMargin = this.mCOUITabLayout.mDotHorizontalOffset;
        } else {
            layoutParams.leftMargin = this.mCOUITabLayout.mDotHorizontalOffset;
        }
        if (cOUITabView.getHintRedDot().getPointMode() == 2) {
            layoutParams.topMargin = this.mCOUITabLayout.mDotVerticalOffsetFromNumberRed;
        } else {
            layoutParams.topMargin = this.mCOUITabLayout.mDotVerticalOffsetFromOnlyRed;
        }
        cOUITabView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), i4);
        if (cOUITabView.getMeasuredWidth() > this.mCOUITabLayout.mRequestedTabMaxWidth) {
            cOUITabView.getTextView().getLayoutParams().width = ((this.mCOUITabLayout.mRequestedTabMaxWidth - cOUITabView.getHintRedDot().getMeasuredWidth()) - layoutParams.getMarginStart()) + layoutParams.getMarginEnd();
            cOUITabView.measure(i3, i4);
        }
    }

    private void measureShortChild(int i3, int i4, int i5, int i6) {
        int childCount = getChildCount();
        int i7 = ((i3 - i4) - (i5 * childCount)) / 2;
        int i8 = i5 / 2;
        setLayoutPadding(i7, i7);
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            setMargin(childAt, i8, i8, childAt.getMeasuredWidth());
        }
    }

    private void measureSmallChild(int i3, int i4, int i5) {
        int iMax;
        int i6;
        int childCount = getChildCount();
        int i7 = this.mContentMinWidth;
        if (i3 >= i7) {
            iMax = Math.max((i7 - i4) / (childCount + 1), i5);
            i6 = ((i3 - this.mContentMinWidth) + iMax) / 2;
        } else {
            iMax = Math.max((i3 - i4) / (childCount + 1), i5);
            i6 = iMax / 2;
        }
        int i8 = iMax / 2;
        setLayoutPadding(i6, i6);
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            setMargin(childAt, i8, i8, childAt.getMeasuredWidth());
        }
    }

    private int parseMinDivider(int i3) {
        if (i3 != -1) {
            return i3;
        }
        int measuredWidth = ((COUITabLayout) getParent()).getMeasuredWidth();
        int measuredHeight = ((COUITabLayout) getParent()).getMeasuredHeight();
        return (COUIResponsiveUtils.isMediumScreen(getContext(), measuredWidth, measuredHeight) || COUIResponsiveUtils.isLargeScreen(getContext(), measuredWidth, measuredHeight)) ? this.mTabMediumSpacing : this.mTabSmallSpacing;
    }

    private int parseMinMargin(int i3) {
        if (i3 != -1) {
            return i3;
        }
        int measuredWidth = ((COUITabLayout) getParent()).getMeasuredWidth();
        if (COUIResponsiveUtils.isLargeScreen(getContext(), measuredWidth, UIUtil.getScreenHeightMetrics(getContext()))) {
            return this.mHorizontalLargeMargin;
        }
        return COUIResponsiveUtils.isMediumScreen(getContext(), measuredWidth, UIUtil.getScreenHeightMetrics(getContext())) ? this.mHorizontalMediumMargin : this.mHorizontalSmallMargin;
    }

    private void setLayoutPadding(int i3, int i4) {
        if (getParent() == null || !(getParent() instanceof COUITabLayout)) {
            return;
        }
        ((COUITabLayout) getParent()).setPaddingLeftAndRight(i3, i4);
    }

    private void setMargin(View view, int i3, int i4, int i5) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = i5 + i4 + i3;
        view.setPaddingRelative(i3, view.getPaddingTop(), i4, view.getPaddingBottom());
        view.measure(View.MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824), View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824));
    }

    public void animateIndicatorToPosition(int i3, int i4) {
        boolean z2;
        int i5;
        int i6;
        ValueAnimator valueAnimator = this.mIndicatorAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            z2 = false;
        } else if (i3 != this.mLastPosition) {
            this.mIndicatorAnimator.end();
            z2 = false;
        } else {
            this.mIndicatorAnimator.cancel();
            z2 = true;
        }
        boolean z3 = ViewCompat.getLayoutDirection(this) == 1;
        View childAt = getChildAt(i3);
        if (childAt == null) {
            updateIndicatorPosition();
            return;
        }
        COUITabView cOUITabView = (COUITabView) childAt;
        COUITabView cOUITabView2 = (COUITabView) getChildAt(this.mCOUITabLayout.getSelectedTabPosition());
        if (cOUITabView.getTextView() == null || cOUITabView.mCustomView != null) {
            int indicatorLeft = getIndicatorLeft(cOUITabView.getLeft() + cOUITabView.mCustomView.getLeft());
            int indicatorRight = getIndicatorRight(cOUITabView.getLeft() + cOUITabView.mCustomView.getRight());
            if (Math.abs(i3 - this.mSelectedPosition) <= 1) {
                int i7 = this.mIndicatorLeft;
                i6 = this.mIndicatorRight;
                i5 = i7;
            } else {
                int iDpToPx = dpToPx(24);
                i5 = (i3 >= this.mSelectedPosition ? !z3 : z3) ? indicatorLeft - iDpToPx : iDpToPx + indicatorRight;
                i6 = i5;
            }
            if (i5 != indicatorLeft || i6 != indicatorRight) {
                ValueAnimator valueAnimator2 = new ValueAnimator();
                this.mIndicatorAnimator = valueAnimator2;
                valueAnimator2.setInterpolator(COUIAnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                valueAnimator2.setDuration(i4);
                valueAnimator2.setFloatValues(0.0f, 1.0f);
                valueAnimator2.addUpdateListener(new AnonymousClass3(i5, indicatorLeft, i6, indicatorRight));
                valueAnimator2.addListener(new AnonymousClass4(i3, cOUITabView, cOUITabView2));
                valueAnimator2.start();
            }
        } else {
            TextView textView = cOUITabView.getTextView();
            int i8 = this.mIndicatorLeft;
            int i9 = this.mIndicatorRight;
            int indicatorPadding = this.mCOUITabLayout.getIndicatorPadding();
            int indicatorLeft2 = getIndicatorLeft((cOUITabView.getLeft() + textView.getLeft()) - indicatorPadding);
            int indicatorRight2 = getIndicatorRight(cOUITabView.getLeft() + textView.getRight() + indicatorPadding);
            int i10 = (indicatorRight2 - indicatorLeft2) - (i9 - i8);
            int i11 = indicatorLeft2 - i8;
            int indicatorAnimTime = this.mCOUITabLayout.getIndicatorAnimTime(i3, this.mSelectedPosition);
            int i12 = this.mIndicatorAnimTime;
            if (i12 != -1) {
                indicatorAnimTime = i12;
            }
            ValueAnimator valueAnimator3 = new ValueAnimator();
            this.mIndicatorAnimator = valueAnimator3;
            valueAnimator3.setDuration(indicatorAnimTime);
            valueAnimator3.setInterpolator(new COUIEaseInterpolator());
            valueAnimator3.setIntValues(0, 1);
            valueAnimator3.addUpdateListener(new AnonymousClass1(textView, new ArgbEvaluator(), z2 ? textView.getCurrentTextColor() : this.mCOUITabLayout.mNormalTextColor, cOUITabView2, z2 ? cOUITabView2.getTextView().getCurrentTextColor() : this.mCOUITabLayout.mSelectedTextColor, i9, i8, i10, i11, indicatorRight2, indicatorLeft2));
            valueAnimator3.addListener(new AnonymousClass2(i3));
            valueAnimator3.start();
        }
        this.mLastPosition = this.mCOUITabLayout.getSelectedTabPosition();
    }

    public boolean childrenNeedLayout() {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            if (getChildAt(i3).getWidth() <= 0) {
                return true;
            }
        }
        return false;
    }

    public int dpToPx(int i3) {
        return Math.round(getResources().getDisplayMetrics().density * i3);
    }

    public Paint getBottomDividerPaint() {
        return this.mBottomDividerPaint;
    }

    public int getIndicatorAnimTime() {
        return this.mIndicatorAnimTime;
    }

    public int getIndicatorBackgroundHeight() {
        return this.mIndicatorBackgroundHeight;
    }

    public int getIndicatorBackgroundPaddingLeft() {
        return this.mIndicatorBackgroundPaddingLeft;
    }

    public int getIndicatorBackgroundPaddingRight() {
        return this.mIndicatorBackgroundPaddingRight;
    }

    public Paint getIndicatorBackgroundPaint() {
        return this.mIndicatorBackgroundPaint;
    }

    public float getIndicatorPosition() {
        return this.mSelectedPosition + this.mSelectionOffset;
    }

    public float getIndicatorWidthRatio() {
        return this.mIndicatorWidthRatio;
    }

    public Paint getSelectedIndicatorPaint() {
        return this.mSelectedIndicatorPaint;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        if (this.mCOUITabLayout.isUpdateindicatorposition()) {
            updateIndicatorPosition();
        }
        if (this.mCOUITabLayout.mTabAlreadyMeasure) {
            return;
        }
        ValueAnimator valueAnimator = this.mIndicatorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mIndicatorAnimator.cancel();
            animateIndicatorToPosition(this.mSelectedPosition, Math.round((1.0f - this.mIndicatorAnimator.getAnimatedFraction()) * this.mIndicatorAnimator.getDuration()));
        }
        COUITabLayout cOUITabLayout = this.mCOUITabLayout;
        cOUITabLayout.mTabAlreadyMeasure = true;
        cOUITabLayout.setScrollPosition(this.mSelectedPosition, 0.0f, true, true);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        if (View.MeasureSpec.getMode(i3) == 0) {
            return;
        }
        int size = View.MeasureSpec.getSize(i3);
        int childCount = getChildCount();
        if (childCount == 0) {
            super.onMeasure(i3, i4);
            return;
        }
        int minMargin = parseMinMargin(this.mCOUITabLayout.getTabMinMargin());
        int minDivider = parseMinDivider(this.mCOUITabLayout.getTabMinDivider());
        if (this.mCOUITabLayout.getTabMode() == 1) {
            this.mIndicatorWidthRatio = this.mCOUITabLayout.getDefaultIndicatoRatio();
            int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mCOUITabLayout.mRequestedTabMaxWidth, Integer.MIN_VALUE);
            int measuredWidth = 0;
            for (int i5 = 0; i5 < childCount; i5++) {
                COUITabView cOUITabView = (COUITabView) getChildAt(i5);
                setMargin(cOUITabView, 0, 0);
                measureChildWithRedDot(cOUITabView, iMakeMeasureSpec, i4);
                measuredWidth += cOUITabView.getMeasuredWidth();
            }
            int i6 = (minMargin * 2) + measuredWidth + ((childCount - 1) * minDivider);
            if (i6 <= this.mContentMinWidth) {
                measureSmallChild(size, measuredWidth, minDivider);
            } else if (i6 <= size) {
                measureShortChild(size, measuredWidth, minDivider, minMargin);
            } else {
                int i7 = minDivider / 2;
                int i8 = minMargin - i7;
                setLayoutPadding(i8, i8);
                for (int i9 = 0; i9 < childCount; i9++) {
                    View childAt = getChildAt(i9);
                    setMargin(childAt, i7, i7, childAt.getMeasuredWidth());
                }
            }
        } else {
            int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(this.mCOUITabLayout.mRequestedTabMaxWidth, Integer.MIN_VALUE);
            int i10 = minDivider / 2;
            int i11 = minMargin - i10;
            setLayoutPadding(i11, i11);
            for (int i12 = 0; i12 < childCount; i12++) {
                View childAt2 = getChildAt(i12);
                setMargin(childAt2, 0, 0);
                measureChildWithRedDot((COUITabView) childAt2, iMakeMeasureSpec2, i4);
                setMargin(childAt2, i10, i10, childAt2.getMeasuredWidth());
            }
        }
        int measuredWidth2 = 0;
        for (int i13 = 0; i13 < childCount; i13++) {
            measuredWidth2 += getChildAt(i13).getMeasuredWidth();
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(measuredWidth2, 1073741824), i4);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onRtlPropertiesChanged(int i3) {
        super.onRtlPropertiesChanged(i3);
    }

    public void setBottomDividerColor(int i3) {
        this.mBottomDividerPaint.setColor(i3);
        ViewCompat.postInvalidateOnAnimation(this.mCOUITabLayout);
    }

    public void setIndicatorAnimTime(int i3) {
        this.mIndicatorAnimTime = i3;
    }

    public void setIndicatorBackgroundHeight(int i3) {
        this.mIndicatorBackgroundHeight = i3;
    }

    public void setIndicatorBackgroundPaddingLeft(int i3) {
        this.mIndicatorBackgroundPaddingLeft = i3;
    }

    public void setIndicatorBackgroundPaddingRight(int i3) {
        this.mIndicatorBackgroundPaddingRight = i3;
    }

    public void setIndicatorLeft(int i3) {
        this.mIndicatorLeft = i3;
    }

    public void setIndicatorPosition(int i3, int i4) {
        int i5 = (i3 + i4) / 2;
        int iMax = Math.max(i4 - i3, dpToPx(32)) / 2;
        int i6 = i5 - iMax;
        int i7 = i5 + iMax;
        if (i6 == this.mIndicatorLeft && i7 == this.mIndicatorRight) {
            return;
        }
        this.mIndicatorLeft = i6;
        this.mIndicatorRight = i7;
        ViewCompat.postInvalidateOnAnimation(this.mCOUITabLayout);
    }

    public void setIndicatorPositionFromTabPosition(int i3, float f3) {
        ValueAnimator valueAnimator = this.mIndicatorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mIndicatorAnimator.cancel();
        }
        this.mSelectedPosition = i3;
        this.mSelectionOffset = f3;
        updateIndicatorPosition();
    }

    public void setIndicatorRight(int i3) {
        this.mIndicatorRight = i3;
    }

    public void setIndicatorWidthRatio(float f3) {
        this.mIndicatorWidthRatio = f3;
    }

    public void setSelectedIndicatorColor(int i3) {
        this.mSelectedIndicatorPaint.setColor(i3);
        ViewCompat.postInvalidateOnAnimation(this.mCOUITabLayout);
    }

    public void setSelectedIndicatorHeight(int i3) {
        if (this.mSelectedIndicatorHeight != i3) {
            this.mSelectedIndicatorHeight = i3;
            ViewCompat.postInvalidateOnAnimation(this.mCOUITabLayout);
        }
    }

    /* JADX WARN: Code duplicated, block: B:71:0x01dd  */
    public void updateIndicatorPosition() {
        int right;
        int left;
        int right2;
        int i3;
        float f3;
        int left2;
        int right3;
        int i4;
        float f4;
        View childAt = getChildAt(this.mSelectedPosition);
        COUITabView cOUITabView = (COUITabView) getChildAt(this.mSelectedPosition);
        boolean z2 = false;
        boolean z3 = (cOUITabView == null || cOUITabView.getTextView() == null || cOUITabView.mCustomView != null) ? false : true;
        if (cOUITabView != null && cOUITabView.mCustomView != null) {
            z2 = true;
        }
        int left3 = -1;
        if (z3) {
            TextView textView = cOUITabView.getTextView();
            if (textView.getWidth() > 0) {
                int left4 = (cOUITabView.getLeft() + textView.getLeft()) - this.mCOUITabLayout.getIndicatorPadding();
                int left5 = cOUITabView.getLeft() + textView.getRight() + this.mCOUITabLayout.getIndicatorPadding();
                if (this.mSelectionOffset > 0.0f && this.mSelectedPosition < getChildCount() - 1) {
                    COUITabView cOUITabView2 = (COUITabView) getChildAt(this.mSelectedPosition + 1);
                    View textView2 = cOUITabView2.mCustomView;
                    if (textView2 == null) {
                        textView2 = cOUITabView2.getTextView();
                    }
                    if (textView2 != null) {
                        left2 = (cOUITabView2.getLeft() + textView2.getLeft()) - this.mCOUITabLayout.getIndicatorPadding();
                        right3 = cOUITabView2.getLeft() + textView2.getRight() + this.mCOUITabLayout.getIndicatorPadding();
                    } else {
                        left2 = cOUITabView2.getLeft();
                        right3 = cOUITabView2.getRight();
                    }
                    int i5 = right3 - left2;
                    int i6 = left5 - left4;
                    int i7 = i5 - i6;
                    int i8 = left2 - left4;
                    if (this.mLastSelectionOffset == 0.0f) {
                        this.mLastSelectionOffset = this.mSelectionOffset;
                    }
                    float f5 = this.mSelectionOffset;
                    if (f5 - this.mLastSelectionOffset > 0.0f) {
                        i4 = (int) (i6 + (i7 * f5));
                        f4 = left4 + (i8 * f5);
                    } else {
                        i4 = (int) (i5 - (i7 * (1.0f - f5)));
                        f4 = left2 - (i8 * (1.0f - f5));
                    }
                    left4 = (int) f4;
                    left5 = left4 + i4;
                    this.mLastSelectionOffset = f5;
                }
                left3 = getIndicatorLeft(left4);
                right = getIndicatorRight(left5);
            } else {
                right = -1;
            }
        } else if (z2) {
            View view = cOUITabView.mCustomView;
            if (view.getWidth() > 0) {
                int left6 = (cOUITabView.getLeft() + view.getLeft()) - this.mCOUITabLayout.getIndicatorPadding();
                int left7 = cOUITabView.getLeft() + view.getRight() + this.mCOUITabLayout.getIndicatorPadding();
                if (this.mSelectionOffset > 0.0f && this.mSelectedPosition < getChildCount() - 1) {
                    COUITabView cOUITabView3 = (COUITabView) getChildAt(this.mSelectedPosition + 1);
                    View textView3 = cOUITabView3.mCustomView;
                    if (textView3 == null) {
                        textView3 = cOUITabView3.getTextView();
                    }
                    if (textView3 != null) {
                        left = (cOUITabView3.getLeft() + textView3.getLeft()) - this.mCOUITabLayout.getIndicatorPadding();
                        right2 = cOUITabView3.getLeft() + textView3.getRight() + this.mCOUITabLayout.getIndicatorPadding();
                    } else {
                        left = cOUITabView3.getLeft();
                        right2 = cOUITabView3.getRight();
                    }
                    int i9 = right2 - left;
                    int i10 = left7 - left6;
                    int i11 = i9 - i10;
                    int i12 = left - left6;
                    if (this.mLastSelectionOffset == 0.0f) {
                        this.mLastSelectionOffset = this.mSelectionOffset;
                    }
                    float f6 = this.mSelectionOffset;
                    if (f6 - this.mLastSelectionOffset > 0.0f) {
                        i3 = (int) (i10 + (i11 * f6));
                        f3 = left6 + (i12 * f6);
                    } else {
                        i3 = (int) (i9 - (i11 * (1.0f - f6)));
                        f3 = left - (i12 * (1.0f - f6));
                    }
                    left6 = (int) f3;
                    left7 = left6 + i3;
                    this.mLastSelectionOffset = f6;
                }
                left3 = getIndicatorLeft(left6);
                right = getIndicatorRight(left7);
            } else {
                right = -1;
            }
        } else if (childAt == null || childAt.getWidth() <= 0) {
            right = -1;
        } else {
            left3 = childAt.getLeft();
            right = childAt.getRight();
            if (this.mSelectionOffset > 0.0f && this.mSelectedPosition < getChildCount() - 1) {
                View childAt2 = getChildAt(this.mSelectedPosition + 1);
                float left8 = this.mSelectionOffset * childAt2.getLeft();
                float f7 = this.mSelectionOffset;
                left3 = (int) (left8 + ((1.0f - f7) * left3));
                right = (int) ((f7 * childAt2.getRight()) + ((1.0f - this.mSelectionOffset) * right));
            }
        }
        setIndicatorPosition(left3, right);
    }

    public int getIndicatorLeft() {
        return this.mIndicatorLeft;
    }

    public int getIndicatorRight() {
        return this.mIndicatorRight;
    }

    private void setMargin(View view, int i3, int i4) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        ViewCompat.setPaddingRelative(view, 0, view.getPaddingTop(), 0, view.getPaddingBottom());
        layoutParams.setMarginStart(i3);
        layoutParams.setMarginEnd(i4);
    }
}