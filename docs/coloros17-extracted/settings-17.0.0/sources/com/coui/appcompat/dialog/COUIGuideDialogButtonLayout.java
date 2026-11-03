package com.coui.appcompat.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.button.COUIButton;
import com.support.appcompat.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes7.dex */
class COUIGuideDialogButtonLayout extends ViewGroup {
    static final int MULTIPLE_BUTTONS = 0;
    private static final Interpolator PAGE_SCROLL_INTEPOLATOR = new COUIEaseInterpolator();
    private static final float SECONDARY_BUTTON_ALPHA_END_PROGRESS = 0.6f;
    static final int SINGLE_BUTTON = 1;
    private static final float SPRING_MINIMUM_VISIBLE_CHANGE = 0.001f;
    private static final float SPRING_RESPONSE = 0.15f;
    private static final String TEXT_EMPTY = "";
    private static final float TEXT_FADE_END_PROGRESS = 0.85f;
    private static final float TEXT_FADE_RANGE = 0.35f;
    private static final float TEXT_FADE_START_PROGRESS = 0.5f;
    private static final float WIDTH_CHANGE_START_PROGRESS = 0.2f;
    private COUIGuideLayoutContentView.OnButtonClickListener mButtonClickListener;
    private int mButtonHeight;
    private final int mButtonSpacing;
    private int mButtonWidth;
    private float mCurrOffset;
    private int mCurrPage;
    private boolean mCustomDrawingOrderEnabled;
    private boolean mHasInitialized;
    private boolean mMultipleType;
    private COUITwoTextButton mNextButton;
    private COUIGuideLayoutContentView.OnButtonClickListener mNextButtonClickListener;
    private int mPagerCount;
    private float mPreOffset;
    private float mRawOffset;
    private int mScrollState;
    private COUIButton mSkipButton;
    private int mStartButtonWidth;
    private int mStartDraggingPage;
    private int mTouchedButtonIndex;
    private COUISpringAnimation mWidthSpringAnimation;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ButtonLayoutType {
    }

    public COUIGuideDialogButtonLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIGuideDialogButtonLayout cOUIGuideDialogButtonLayout, View view) {
        if (!cOUIGuideDialogButtonLayout.mMultipleType) {
            cOUIGuideDialogButtonLayout.dispatchOnStartClick();
        }
        int i3 = cOUIGuideDialogButtonLayout.mScrollState;
        if (i3 == 1) {
            return;
        }
        if (i3 == 2) {
            if (cOUIGuideDialogButtonLayout.mNextButton.getStartTextAlpha() > cOUIGuideDialogButtonLayout.mNextButton.getNextTextAlpha()) {
                cOUIGuideDialogButtonLayout.dispatchOnStartClick();
                return;
            } else {
                cOUIGuideDialogButtonLayout.dispatchOnNextClick();
                cOUIGuideDialogButtonLayout.mStartDraggingPage = cOUIGuideDialogButtonLayout.mPagerCount - 2;
                return;
            }
        }
        if (cOUIGuideDialogButtonLayout.mNextButton.getNextTextAlpha() < cOUIGuideDialogButtonLayout.mNextButton.getStartTextAlpha()) {
            cOUIGuideDialogButtonLayout.dispatchOnStartClick();
        } else {
            cOUIGuideDialogButtonLayout.dispatchOnNextClick();
            cOUIGuideDialogButtonLayout.mStartDraggingPage = cOUIGuideDialogButtonLayout.mPagerCount - 2;
        }
    }

    private void animateToPosition(float f3) {
        this.mWidthSpringAnimation.animateToFinalPosition(f3);
    }

    public static /* synthetic */ void b(COUIGuideDialogButtonLayout cOUIGuideDialogButtonLayout, View view) {
        if (cOUIGuideDialogButtonLayout.mSkipButton.getAlpha() == 1.0f) {
            cOUIGuideDialogButtonLayout.dispatchOnSkipClick();
        }
    }

    public static /* synthetic */ void c(COUIGuideDialogButtonLayout cOUIGuideDialogButtonLayout, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUIGuideDialogButtonLayout.widthChange(f3);
        cOUIGuideDialogButtonLayout.mRawOffset = f3;
    }

    private void dispatchOnNextClick() {
        COUIGuideLayoutContentView.OnButtonClickListener onButtonClickListener = this.mButtonClickListener;
        if (onButtonClickListener != null) {
            onButtonClickListener.onNextClick();
            this.mNextButtonClickListener.onNextClick();
        }
    }

    private void dispatchOnSkipClick() {
        COUIGuideLayoutContentView.OnButtonClickListener onButtonClickListener = this.mButtonClickListener;
        if (onButtonClickListener != null) {
            onButtonClickListener.onSkipClick();
        }
    }

    private void dispatchOnStartClick() {
        COUIGuideLayoutContentView.OnButtonClickListener onButtonClickListener = this.mButtonClickListener;
        if (onButtonClickListener != null) {
            onButtonClickListener.onStartClick();
        }
    }

    private int findTouchedButtonIndex(float f3, float f4) {
        if (isPointInView(this.mSkipButton, f3, f4)) {
            return indexOfChild(this.mSkipButton);
        }
        if (isPointInView(this.mNextButton, f3, f4)) {
            return indexOfChild(this.mNextButton);
        }
        return -1;
    }

    private void initAnimation() {
        this.mWidthSpringAnimation = new COUISpringAnimation(new FloatValueHolder());
        COUISpringForce cOUISpringForce = new COUISpringForce(0.0f);
        cOUISpringForce.setResponse(0.15f);
        cOUISpringForce.setBounce(0.0f);
        this.mWidthSpringAnimation.setSpring(cOUISpringForce);
        this.mWidthSpringAnimation.setMinValue(0.0f);
        this.mWidthSpringAnimation.setMaxValue(1.0f);
        this.mWidthSpringAnimation.setMinimumVisibleChange(0.001f);
        this.mWidthSpringAnimation.addUpdateListener(new f(this));
    }

    private void initButtons() {
        COUIButton cOUIButton = new COUIButton(getContext(), null, R.attr.couiButtonColorfulWhiteStyle);
        this.mSkipButton = cOUIButton;
        cOUIButton.setButtonType(COUIButton.ButtonType.SECONDARY);
        this.mSkipButton.setIsDrawableMaterialEffect(true);
        this.mSkipButton.setOnClickListener(new g(this));
        COUITwoTextButton cOUITwoTextButton = new COUITwoTextButton(getContext(), null, R.attr.couiButtonColorfulLargeStyle);
        this.mNextButton = cOUITwoTextButton;
        cOUITwoTextButton.setButtonType(COUIButton.ButtonType.PRIMARY);
        this.mNextButton.setIsDrawableMaterialEffect(true);
        this.mNextButton.setOnClickListener(new h(this));
        if (!this.mMultipleType) {
            this.mSkipButton.setVisibility(8);
            addView(this.mNextButton);
        } else {
            this.mSkipButton.setVisibility(0);
            addView(this.mSkipButton);
            addView(this.mNextButton);
        }
    }

    private boolean isPointInView(View view, float f3, float f4) {
        return view != null && f3 >= ((float) view.getLeft()) && f3 <= ((float) view.getRight()) && f4 >= ((float) view.getTop()) && f4 <= ((float) view.getBottom());
    }

    private void layoutButtonTransition(COUITwoTextButton cOUITwoTextButton, boolean z2) {
        int paddingLeft;
        int i3;
        if (z2) {
            paddingLeft = getPaddingLeft();
            i3 = this.mButtonWidth / 2;
        } else {
            int paddingLeft2 = getPaddingLeft();
            int i4 = this.mButtonWidth;
            paddingLeft = paddingLeft2 + i4 + this.mButtonSpacing;
            i3 = i4 / 2;
        }
        int i5 = paddingLeft + i3;
        float width = i5 + (this.mRawOffset * ((getWidth() / 2) - i5));
        float f3 = cOUITwoTextButton.getLayoutParams().width / 2.0f;
        int paddingTop = getPaddingTop();
        cOUITwoTextButton.layout((int) (width - f3), paddingTop, (int) (width + f3), this.mButtonHeight + paddingTop);
    }

    private void textAlphaAnimate(float f3) {
        float fMax = f3 > 0.5f ? Math.max(0.0f, Math.min(((0.85f - f3) * 1.0f) / 0.35f, 1.0f)) : 1.0f;
        float fMax2 = Math.max(0.0f, Math.min((f3 - 0.5f) / 0.5f, 1.0f));
        int i3 = this.mStartDraggingPage;
        int i4 = this.mPagerCount;
        if (i3 == i4 - 1 && f3 < 1.0f) {
            this.mNextButton.setNextTextAlpha(fMax2);
            this.mNextButton.setStartTextAlpha(fMax);
        } else if (i3 == i4 - 2 && f3 > 0.0f) {
            this.mNextButton.setNextTextAlpha(fMax);
            this.mNextButton.setStartTextAlpha(fMax2);
        }
        if (this.mNextButton.getStartTextAlpha() == 1.0f) {
            this.mNextButton.setNextTextAlpha(0.0f);
        } else if (this.mNextButton.getNextTextAlpha() == 1.0f) {
            this.mNextButton.setStartTextAlpha(0.0f);
        } else {
            this.mNextButton.setText("");
        }
        this.mNextButton.invalidate();
    }

    private void widthChange(float f3) {
        float fMax = Math.max(0.0f, Math.min(1.0f, (f3 - 0.2f) / 0.8f));
        ViewGroup.LayoutParams layoutParams = this.mNextButton.getLayoutParams();
        int i3 = this.mButtonWidth;
        layoutParams.width = (int) (i3 + ((i3 + this.mButtonSpacing) * fMax));
        float fMax2 = Math.max(0.0f, Math.min((0.6f - f3) / 0.6f, 1.0f));
        this.mSkipButton.setAlpha(fMax2);
        if (fMax2 == 0.0f) {
            this.mSkipButton.setVisibility(8);
        } else {
            this.mSkipButton.setVisibility(0);
        }
        requestLayout();
    }

    public void addButtonClickListener(COUIGuideLayoutContentView.OnButtonClickListener onButtonClickListener) {
        this.mButtonClickListener = onButtonClickListener;
    }

    @Override // android.view.ViewGroup
    public int getChildDrawingOrder(int i3, int i4) {
        if (!this.mCustomDrawingOrderEnabled) {
            return super.getChildDrawingOrder(i3, i4);
        }
        int i5 = this.mTouchedButtonIndex;
        if (i5 < 0 || i5 >= i3) {
            return super.getChildDrawingOrder(i3, i4);
        }
        if (i4 == i3 - 1) {
            return i5;
        }
        return i4 >= i5 ? i4 + 1 : i4;
    }

    public COUITwoTextButton getNextButton() {
        return this.mNextButton;
    }

    public COUIButton getSkipButton() {
        return this.mSkipButton;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            int iFindTouchedButtonIndex = findTouchedButtonIndex(motionEvent.getX(), motionEvent.getY());
            this.mTouchedButtonIndex = iFindTouchedButtonIndex;
            if (iFindTouchedButtonIndex >= 0) {
                this.mCustomDrawingOrderEnabled = true;
                invalidate();
            }
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int i7 = i5 - i3;
        int i8 = i6 - i4;
        boolean z3 = ViewCompat.getLayoutDirection(this) == 1;
        int paddingTop = (i8 - getPaddingTop()) - getPaddingBottom();
        this.mButtonWidth = (((i7 - getPaddingLeft()) - getPaddingRight()) - this.mButtonSpacing) / 2;
        this.mButtonHeight = paddingTop;
        if (!this.mMultipleType) {
            if (this.mNextButton.getVisibility() == 0) {
                int width = getWidth() - getPaddingRight();
                int paddingLeft = getPaddingLeft();
                int paddingTop2 = getPaddingTop();
                this.mNextButton.layout(paddingLeft, paddingTop2, width, this.mButtonHeight + paddingTop2);
                return;
            }
            return;
        }
        if (!z3) {
            if (this.mSkipButton.getVisibility() == 0) {
                int paddingLeft2 = getPaddingLeft();
                int paddingTop3 = getPaddingTop();
                this.mSkipButton.layout(paddingLeft2, paddingTop3, this.mButtonWidth + paddingLeft2, this.mButtonHeight + paddingTop3);
            }
            if (this.mNextButton.getVisibility() == 0) {
                layoutButtonTransition(this.mNextButton, false);
                return;
            }
            return;
        }
        if (this.mSkipButton.getVisibility() == 0) {
            int width2 = getWidth() - getPaddingRight();
            int i9 = width2 - this.mButtonWidth;
            int paddingTop4 = getPaddingTop();
            this.mSkipButton.layout(i9, paddingTop4, width2, this.mButtonHeight + paddingTop4);
        }
        if (this.mNextButton.getVisibility() == 0) {
            layoutButtonTransition(this.mNextButton, true);
        }
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int size = View.MeasureSpec.getSize(i3);
        this.mButtonWidth = (((size - getPaddingLeft()) - getPaddingRight()) - this.mButtonSpacing) / 2;
        this.mStartButtonWidth = (size - getPaddingLeft()) - getPaddingRight();
        if (!this.mHasInitialized) {
            this.mHasInitialized = true;
            if (this.mSkipButton.getVisibility() != 8) {
                this.mSkipButton.getLayoutParams().width = this.mButtonWidth;
            }
            if (this.mNextButton.getVisibility() != 8) {
                this.mNextButton.getLayoutParams().width = this.mButtonWidth;
            }
        }
        measureChildren(i3, i4);
        setMeasuredDimension(size, Math.max(this.mSkipButton.getMeasuredHeight(), this.mNextButton.getMeasuredHeight()) + getPaddingTop() + getPaddingBottom());
    }

    public void onPageScrollStateChanged(int i3) {
        this.mScrollState = i3;
        if (i3 == 1 || i3 == 0) {
            this.mStartDraggingPage = this.mCurrPage;
        }
    }

    public void onPageScrolled(int i3, float f3, int i4) {
        int i5 = this.mPagerCount;
        if (i5 <= 1) {
            return;
        }
        this.mPreOffset = this.mCurrOffset;
        float f4 = (i3 + f3) - (i5 - 2);
        this.mCurrOffset = f4;
        float fMax = Math.max(0.0f, Math.min(f4, 1.0f));
        this.mCurrOffset = fMax;
        float interpolation = PAGE_SCROLL_INTEPOLATOR.getInterpolation(fMax);
        this.mCurrOffset = interpolation;
        if (this.mPreOffset == interpolation) {
            return;
        }
        animateToPosition(interpolation);
        int i6 = this.mStartDraggingPage;
        int i7 = this.mPagerCount;
        if (i6 == i7 - 1) {
            float f5 = this.mCurrOffset;
            if (f5 < 1.0f) {
                textAlphaAnimate(1.0f - f5);
                return;
            }
        }
        if (i6 == i7 - 2) {
            float f6 = this.mCurrOffset;
            if (f6 > 0.0f) {
                textAlphaAnimate(f6);
            }
        }
    }

    public void onPageSelected(int i3) {
        this.mCurrPage = i3;
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        if (i3 != i5) {
            if (this.mRawOffset == 0.0f && this.mNextButton.getLayoutParams().width != this.mButtonWidth) {
                this.mNextButton.getLayoutParams().width = this.mButtonWidth;
            }
            if (this.mRawOffset == 1.0f && this.mNextButton.getStartTextAlpha() == 1.0f && this.mNextButton.getLayoutParams().width != this.mStartButtonWidth) {
                this.mNextButton.getLayoutParams().width = this.mStartButtonWidth;
            }
        }
    }

    public void removeButtonClickListener(COUIGuideLayoutContentView.OnButtonClickListener onButtonClickListener) {
        if (this.mButtonClickListener == onButtonClickListener) {
            this.mButtonClickListener = null;
        }
    }

    public void setBlurState(boolean z2) {
        COUIButton.MaterialEffectState materialEffectState = z2 ? COUIButton.MaterialEffectState.FORCE_ENABLE : COUIButton.MaterialEffectState.FORCE_DISABLE;
        this.mSkipButton.setUseMaterialEffectAttr(materialEffectState, COUIButton.ButtonType.SECONDARY);
        this.mNextButton.setUseMaterialEffectAttr(materialEffectState, COUIButton.ButtonType.PRIMARY);
    }

    public void setButtonLayoutType(int i3) {
        this.mMultipleType = i3 == 0;
    }

    public void setNextButtonClickListener(COUIGuideLayoutContentView.OnButtonClickListener onButtonClickListener) {
        this.mNextButtonClickListener = onButtonClickListener;
    }

    public void setPagerCount(int i3) {
        this.mPagerCount = i3;
    }

    public COUIGuideDialogButtonLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIGuideDialogButtonLayout(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIGuideDialogButtonLayout(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mButtonClickListener = null;
        this.mHasInitialized = false;
        this.mMultipleType = true;
        this.mScrollState = 0;
        this.mCustomDrawingOrderEnabled = false;
        this.mTouchedButtonIndex = -1;
        this.mButtonSpacing = getResources().getDimensionPixelSize(com.support.dialog.R.dimen.coui_dialog_guide_button_layout_space);
        initAnimation();
        initButtons();
        setChildrenDrawingOrderEnabled(true);
    }
}