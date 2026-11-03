package com.coui.appcompat.bottomfloatingtoolbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.accessibilityutil.COUIAccessibilityUtil;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.bottomfloatingnavigationbar.COUIFadeMaskImageView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.coui.appcompat.state.COUIMaskRippleDrawable;
import com.coui.appcompat.textview.COUITextView;
import com.google.android.material.navigation.NavigationBarView;
import com.support.bottomnavigation.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomFloatingToolbarItemView extends FrameLayout {
    private static final float DEFAULT_SPRING_ANIMATION_FACTOR = 10000.0f;
    private static final int DEFAULT_TITLE_TEXT_SIZE_SP = 16;
    private static final int EDIT_TOOLBAR_HORIZONTAL_TITLE_TEXT_SIZE_DP = 14;
    private static final int ELLIPSIZE_WIDTH_EPSILON_PX = 2;
    private static final int NAVI_TITLE_TEXT_SIZE_DP = 10;
    private static final float NEW_TITLE_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float NEW_TITLE_ALPHA_SPRING_RESPONSE = 0.3f;
    private static final float OLD_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float OLD_ALPHA_SPRING_RESPONSE = 0.2f;
    private static final int RED_DOT_DOUBLE_DIGIT_LIMIT = 100;
    private static final int RED_DOT_SINGLE_DIGIT_LIMIT = 10;
    public static final int STYLE_EDIT_TOOLBAR_HORIZONTAL = 4;
    public static final int STYLE_EDIT_TOOLBAR_VERTICAL = 3;
    public static final int STYLE_NAVI_LARGE_ICON = 2;
    public static final int STYLE_NAVI_VERTICAL = 1;
    public static final int STYLE_TOOLBAR_ICON_OR_TEXT = 0;
    private final int mBackgroundHorizontalPadding;
    private final RectF mBackgroundMaskRect;
    private COUIHintRedDot mCOUIHintRedDot;
    private ColorStateList mCustomTitleTextColor;
    private final int mDefRedDotOffset;
    private final int mFloatingToolbarItemHeight;
    private final int mFloatingToolbarMaskHorizontalOutset;
    private final int mFloatingToolbarSingleMaskInsetHorizontal;
    private int mGroupId;
    private COUIFadeMaskImageView mIcon;
    private int mIconResId;
    private final int mIconTitleGapHorizontal;
    private final int mIconTitleGapVertical;
    private final int mIconTopMargin;
    private boolean mIsSingleVisibleItemInMenu;
    private boolean mIsWidthAnimating;
    private int mItemHeight;
    private int mItemStyle;
    private final int mLargeIconOverflow;
    private final int mLargeIconSize;
    private final COUIMaskRippleDrawable mMaskRippleDrawable;
    private int mMaxTitleWidth;
    private int mMinTitleWidth;
    private final int mNaviIconHeight;
    private final String mRedDotDescription;
    private int mRedDotNumber;
    private Rect mRedDotRect;
    private final int mRedDotRightOverflow1Digit;
    private final int mRedDotRightOverflow2Digit;
    private final int mRedDotRightOverflow3Digit;
    private final int mRedDotTopOverflow;
    private int mRedDotType;
    private TextView mShortTitle;
    private COUISpringAnimation mShortTitleAlphaAnim;
    private boolean mShowRedDot;
    private TextView mTitle;
    private COUISpringAnimation mTitleAlphaAnim;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarItemView$1, reason: invalid class name */
    public class AnonymousClass1 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            COUIBottomFloatingToolbarItemView.access$000(COUIBottomFloatingToolbarItemView.this).setAlpha(f3 / 10000.0f);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarItemView$2, reason: invalid class name */
    public class AnonymousClass2 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUIBottomFloatingToolbarItemView.access$100(COUIBottomFloatingToolbarItemView.this);
            COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = COUIBottomFloatingToolbarItemView.this;
            COUIBottomFloatingToolbarItemView.access$200(cOUIBottomFloatingToolbarItemView, COUIBottomFloatingToolbarItemView.access$000(cOUIBottomFloatingToolbarItemView), false);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarItemView$3, reason: invalid class name */
    public class AnonymousClass3 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            float f5 = f3 / 10000.0f;
            if (COUIBottomFloatingToolbarItemView.access$300(COUIBottomFloatingToolbarItemView.this) != null) {
                COUIBottomFloatingToolbarItemView.access$300(COUIBottomFloatingToolbarItemView.this).setAlpha(f5);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarItemView$4, reason: invalid class name */
    public class AnonymousClass4 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass4() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUIBottomFloatingToolbarItemView.access$100(COUIBottomFloatingToolbarItemView.this);
            COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView = COUIBottomFloatingToolbarItemView.this;
            COUIBottomFloatingToolbarItemView.access$200(cOUIBottomFloatingToolbarItemView, COUIBottomFloatingToolbarItemView.access$300(cOUIBottomFloatingToolbarItemView), false);
        }
    }

    public COUIBottomFloatingToolbarItemView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ TextView access$000(COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView) {
        return cOUIBottomFloatingToolbarItemView.mTitle;
    }

    public static /* synthetic */ void access$100(COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView) {
        cOUIBottomFloatingToolbarItemView.checkAndEndWidthAnimation();
    }

    public static /* synthetic */ void access$200(COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView, TextView textView, boolean z2) {
        cOUIBottomFloatingToolbarItemView.setTextViewPreventsShake(textView, z2);
    }

    public static /* synthetic */ TextView access$300(COUIBottomFloatingToolbarItemView cOUIBottomFloatingToolbarItemView) {
        return cOUIBottomFloatingToolbarItemView.mShortTitle;
    }

    private void applyBackgroundMaskRect(int i3, float f3) {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        int i4 = -i3;
        int i5 = width + i3;
        this.mBackgroundMaskRect.set(i4, 0.0f, i5, height);
        syncBackgroundDrawableBounds(i4, 0, i5, height);
        this.mMaskRippleDrawable.setMaskRect(this.mBackgroundMaskRect, f3, f3);
    }

    private void applyDefaultViewStyle() {
        this.mTitle.setTextSize(2, 16.0f);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_text_padding_horizontal);
        this.mTitle.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        this.mMaskRippleDrawable.setDrawableEnabled(true);
    }

    private void applyEditToolbarHorizontalStyle() {
        this.mItemHeight = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_edit_toolbar_item_height_horizontal);
        this.mTitle.setVisibility(0);
        this.mIcon.setVisibility(0);
        TextViewCompat.setTextAppearance(this.mTitle, com.support.appcompat.R.style.couiTextButtonM);
        this.mTitle.setTextSize(0, dp2px(14.0f));
        this.mTitle.setGravity(NavigationBarView.ITEM_GRAVITY_START_CENTER);
        this.mTitle.setMinWidth(0);
        this.mTitle.setPadding(0, 0, 0, 0);
        this.mMaskRippleDrawable.setDrawableEnabled(true);
        this.mTitle.setMaxLines(2);
        this.mTitle.setEllipsize(TextUtils.TruncateAt.END);
        setClipChildren(false);
        setClipToPadding(false);
    }

    private void applyEditToolbarVerticalStyle() {
        this.mItemHeight = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_height);
        this.mTitle.setVisibility(0);
        this.mIcon.setVisibility(0);
        this.mTitle.setTextSize(0, dp2px(10.0f));
        this.mTitle.setGravity(17);
        this.mTitle.setMinWidth(0);
        this.mTitle.setPadding(0, 0, 0, 0);
        this.mMaskRippleDrawable.setDrawableEnabled(true);
        this.mTitle.setMaxLines(1);
        setClipChildren(false);
        setClipToPadding(false);
    }

    private void applyNaviLargeIconStyle() {
        this.mTitle.setVisibility(0);
        this.mIcon.setVisibility(4);
        this.mTitle.setTextSize(0, dp2px(10.0f));
        this.mTitle.setGravity(17);
        this.mTitle.setMinWidth(0);
        this.mTitle.setPadding(0, 0, 0, 0);
        this.mMaskRippleDrawable.setDrawableEnabled(false);
        setClipChildren(false);
        setClipToPadding(false);
    }

    private void applyNaviVerticalStyle() {
        this.mTitle.setVisibility(0);
        this.mIcon.setVisibility(0);
        this.mTitle.setTextSize(0, dp2px(10.0f));
        this.mTitle.setGravity(17);
        this.mTitle.setMinWidth(0);
        this.mTitle.setPadding(0, 0, 0, 0);
        this.mMaskRippleDrawable.setDrawableEnabled(true);
        if (this.mShortTitle == null) {
            initShortTitle(getContext());
        }
        setClipChildren(false);
        setClipToPadding(false);
    }

    private void checkAndEndWidthAnimation() {
        if (this.mItemStyle != 1) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = this.mTitleAlphaAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            COUISpringAnimation cOUISpringAnimation2 = this.mShortTitleAlphaAnim;
            if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
                this.mIsWidthAnimating = false;
                setItemTextPreventsShake(false);
            }
        }
    }

    private float dp2px(float f3) {
        return TypedValue.applyDimension(1, f3, getContext().getResources().getDisplayMetrics());
    }

    private int getRedDotRightOverflow() {
        int i3 = this.mRedDotNumber;
        if (i3 < 10) {
            return this.mRedDotRightOverflow1Digit;
        }
        return i3 < 100 ? this.mRedDotRightOverflow2Digit : this.mRedDotRightOverflow3Digit;
    }

    private boolean hasIcon() {
        return this.mIcon.getDrawable() != null;
    }

    private void initIcon(Context context) {
        this.mIcon = new COUIFadeMaskImageView(context);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_icon_width);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, this.mNaviIconHeight);
        layoutParams.gravity = 17;
        this.mIcon.setLayoutParams(layoutParams);
        this.mIcon.setAdjustViewBounds(true);
        this.mIcon.setMaxWidth(dimensionPixelSize);
        this.mIcon.setMaxHeight(this.mNaviIconHeight);
        addView(this.mIcon);
    }

    private void initRedDot(Context context) {
        this.mCOUIHintRedDot = new COUIHintRedDot(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 53;
        this.mCOUIHintRedDot.setLayoutParams(layoutParams);
        this.mCOUIHintRedDot.setVisibility(8);
        this.mCOUIHintRedDot.setDotDiameter(getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_red_dot_radius));
        this.mCOUIHintRedDot.setPointMode(1);
        addView(this.mCOUIHintRedDot);
    }

    private void initShortTitle(Context context) {
        this.mShortTitle = new COUITextView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        this.mShortTitle.setLayoutParams(layoutParams);
        this.mShortTitle.setGravity(17);
        this.mShortTitle.setEllipsize(TextUtils.TruncateAt.END);
        this.mShortTitle.setTypeface(Typeface.create("sans-serif-medium", 0));
        this.mShortTitle.setMaxLines(1);
        this.mShortTitle.setTextAlignment(4);
        this.mShortTitle.setTextSize(0, dp2px(10.0f));
        this.mShortTitle.setPadding(0, 0, 0, 0);
        this.mShortTitle.setAlpha(0.0f);
        this.mShortTitle.setVisibility(0);
        addView(this.mShortTitle);
    }

    private void initShortTitleAlphaAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.3f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mShortTitleAlphaAnim = spring;
        spring.addUpdateListener(new AnonymousClass3());
        this.mShortTitleAlphaAnim.addEndListener(new AnonymousClass4());
    }

    private void initTitle(Context context) {
        this.mTitle = new COUITextView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, this.mFloatingToolbarItemHeight);
        layoutParams.gravity = 17;
        this.mTitle.setLayoutParams(layoutParams);
        this.mTitle.setGravity(17);
        this.mTitle.setEllipsize(TextUtils.TruncateAt.END);
        this.mTitle.setTypeface(Typeface.create("sans-serif-medium", 0));
        this.mTitle.setMaxWidth(getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_text_max_width));
        this.mTitle.setMaxHeight(this.mFloatingToolbarItemHeight);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_text_padding_horizontal);
        this.mTitle.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        this.mTitle.setMaxLines(1);
        this.mTitle.setMinWidth(getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_text_min_width));
        this.mTitle.setTextAlignment(4);
        this.mTitle.setTextColor(resolveToolbarDefaultTitleColorStateList());
        this.mTitle.setTextSize(2, 16.0f);
        this.mTitle.setVisibility(8);
        addView(this.mTitle);
    }

    private void initTitleAlphaAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.2f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mTitleAlphaAnim = spring;
        spring.addUpdateListener(new AnonymousClass1());
        this.mTitleAlphaAnim.addEndListener(new AnonymousClass2());
    }

    private void initView(Context context) {
        initIcon(context);
        initTitle(context);
        initRedDot(context);
        this.mRedDotRect = new Rect();
        initTitleAlphaAnim();
        initShortTitleAlphaAnim();
    }

    private boolean isEditToolbarItemStyle() {
        int i3 = this.mItemStyle;
        return i3 == 3 || i3 == 4;
    }

    private boolean isToolbarItemStyle() {
        return this.mItemStyle == 0 || isEditToolbarItemStyle();
    }

    private void layoutEditToolbarHorizontal(int i3, int i4, int i5, int i6) {
        int i7;
        int i8 = i5 - i3;
        int i9 = i6 - i4;
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int measuredWidth = this.mIcon.getMeasuredWidth();
        int measuredHeight = this.mIcon.getMeasuredHeight();
        int measuredWidth2 = this.mTitle.getMeasuredWidth();
        int measuredHeight2 = this.mTitle.getMeasuredHeight();
        int i10 = this.mIconTitleGapHorizontal;
        int i11 = (i8 - ((measuredWidth + i10) + measuredWidth2)) / 2;
        int i12 = (i9 - measuredHeight) / 2;
        int i13 = (i9 - measuredHeight2) / 2;
        if (z2) {
            i7 = i11;
            i11 = i11 + measuredWidth2 + i10;
        } else {
            i7 = i11 + measuredWidth + i10;
        }
        this.mIcon.layout(i11, i12, i11 + measuredWidth, measuredHeight + i12);
        this.mTitle.layout(i7, i13, measuredWidth2 + i7, measuredHeight2 + i13);
        layoutRedDotRelativeToIcon(i11, i12, measuredWidth, z2);
    }

    private void layoutEditToolbarVertical(int i3, int i4, int i5, int i6) {
        int i7 = i5 - i3;
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int measuredWidth = this.mIcon.getMeasuredWidth();
        int measuredHeight = this.mIcon.getMeasuredHeight();
        int i8 = this.mIconTopMargin;
        int i9 = (i7 - measuredWidth) / 2;
        int i10 = measuredHeight + i8;
        this.mIcon.layout(i9, i8, i9 + measuredWidth, i10);
        int measuredWidth2 = this.mTitle.getMeasuredWidth();
        int measuredHeight2 = this.mTitle.getMeasuredHeight();
        int i11 = i10 + this.mIconTitleGapVertical;
        this.mTitle.layout(0, i11, measuredWidth2, measuredHeight2 + i11);
        layoutRedDotRelativeToIcon(i9, i8, measuredWidth, z2);
    }

    private void layoutNaviLargeIcon(int i3, int i4, int i5, int i6) {
        int i7 = i5 - i3;
        int measuredWidth = this.mIcon.getMeasuredWidth();
        int measuredHeight = this.mIcon.getMeasuredHeight();
        int i8 = -this.mLargeIconOverflow;
        int i9 = (i7 - measuredWidth) / 2;
        int i10 = measuredWidth + i9;
        this.mIcon.layout(i9, i8, i10, measuredHeight + i8);
        int measuredWidth2 = this.mTitle.getMeasuredWidth();
        int measuredHeight2 = this.mTitle.getMeasuredHeight();
        int i11 = this.mIconTopMargin + this.mNaviIconHeight + this.mIconTitleGapVertical;
        int i12 = (i7 - measuredWidth2) / 2;
        this.mTitle.layout(i12, i11, measuredWidth2 + i12, measuredHeight2 + i11);
        if (this.mShowRedDot) {
            int measuredWidth3 = this.mCOUIHintRedDot.getMeasuredWidth();
            int measuredHeight3 = this.mCOUIHintRedDot.getMeasuredHeight();
            int iMax = Math.max(0, i8);
            this.mCOUIHintRedDot.layout(i10 - measuredWidth3, iMax, i10, measuredHeight3 + iMax);
        }
    }

    private void layoutNaviVertical(int i3, int i4, int i5, int i6) {
        int i7 = i5 - i3;
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int measuredWidth = this.mIcon.getMeasuredWidth();
        int measuredHeight = this.mIcon.getMeasuredHeight();
        int measuredWidth2 = this.mTitle.getMeasuredWidth();
        int measuredHeight2 = this.mTitle.getMeasuredHeight();
        int i8 = this.mIconTopMargin;
        int i9 = (i7 - measuredWidth) / 2;
        int i10 = measuredHeight + i8;
        this.mIcon.layout(i9, i8, i9 + measuredWidth, i10);
        int i11 = i10 + this.mIconTitleGapVertical;
        int i12 = (i7 - measuredWidth2) / 2;
        this.mTitle.layout(i12, i11, measuredWidth2 + i12, measuredHeight2 + i11);
        TextView textView = this.mShortTitle;
        if (textView != null && textView.getVisibility() == 0) {
            int measuredWidth3 = this.mShortTitle.getMeasuredWidth();
            int i13 = (i7 - measuredWidth3) / 2;
            this.mShortTitle.layout(i13, i11, measuredWidth3 + i13, this.mShortTitle.getMeasuredHeight() + i11);
        }
        layoutRedDotRelativeToIcon(i9, i8, measuredWidth, z2);
    }

    private void layoutRedDot() {
        this.mCOUIHintRedDot.setVisibility(this.mShowRedDot ? 0 : 8);
        if (this.mShowRedDot) {
            if (ViewCompat.getLayoutDirection(this) == 1) {
                this.mRedDotRect.set((-this.mCOUIHintRedDot.getMeasuredWidth()) / 2, (-this.mCOUIHintRedDot.getMeasuredHeight()) / 2, this.mCOUIHintRedDot.getMeasuredWidth() / 2, this.mCOUIHintRedDot.getMeasuredHeight() / 2);
                Rect rect = this.mRedDotRect;
                int i3 = this.mDefRedDotOffset;
                rect.offset(i3, i3);
            } else {
                this.mRedDotRect.set(getWidth() - (this.mCOUIHintRedDot.getMeasuredWidth() / 2), (-this.mCOUIHintRedDot.getMeasuredHeight()) / 2, getWidth() + (this.mCOUIHintRedDot.getMeasuredWidth() / 2), this.mCOUIHintRedDot.getMeasuredHeight() / 2);
                Rect rect2 = this.mRedDotRect;
                int i4 = this.mDefRedDotOffset;
                rect2.offset(-i4, i4);
            }
            COUIHintRedDot cOUIHintRedDot = this.mCOUIHintRedDot;
            Rect rect3 = this.mRedDotRect;
            cOUIHintRedDot.layout(rect3.left, rect3.top, rect3.right, rect3.bottom);
        }
    }

    private void layoutRedDotRelativeToIcon(int i3, int i4, int i5, boolean z2) {
        if (this.mShowRedDot) {
            int measuredWidth = this.mCOUIHintRedDot.getMeasuredWidth();
            int measuredHeight = this.mCOUIHintRedDot.getMeasuredHeight();
            if (this.mRedDotType != 2) {
                if (z2) {
                    this.mCOUIHintRedDot.layout(i3, i4, measuredWidth + i3, measuredHeight + i4);
                    return;
                } else {
                    int i6 = i3 + i5;
                    this.mCOUIHintRedDot.layout(i6 - measuredWidth, i4, i6, measuredHeight + i4);
                    return;
                }
            }
            int i7 = i4 - this.mRedDotTopOverflow;
            int redDotRightOverflow = getRedDotRightOverflow();
            if (z2) {
                int i8 = i3 - redDotRightOverflow;
                this.mCOUIHintRedDot.layout(i8, i7, measuredWidth + i8, measuredHeight + i7);
            } else {
                int i9 = i3 + i5 + redDotRightOverflow;
                this.mCOUIHintRedDot.layout(i9 - measuredWidth, i7, i9, measuredHeight + i7);
            }
        }
    }

    private void loadIconFromResourceId(int i3) {
        Drawable drawable;
        if (i3 == 0 || (drawable = ResourcesCompat.getDrawable(getResources(), i3, getContext().getTheme())) == null) {
            return;
        }
        this.mIcon.setImageDrawable(drawable.mutate());
        this.mIcon.jumpDrawablesToCurrentState();
        this.mIcon.invalidate();
    }

    private void measureTitleOrIcon() {
        this.mMaskRippleDrawable.setCustomRippleMask();
        if (hasIcon()) {
            this.mTitle.setVisibility(8);
            this.mIcon.setVisibility(0);
        } else {
            this.mIcon.setVisibility(8);
            this.mTitle.setVisibility(0);
        }
    }

    private void measureTitleWithEllipsisFix(TextView textView, int i3, int i4) {
        if (textView == null || textView.getVisibility() == 8) {
            return;
        }
        textView.setGravity(17);
        textView.setTextAlignment(4);
        textView.measure(i3, i4);
        Layout layout = textView.getLayout();
        if (layout == null || layout.getLineCount() <= 0 || layout.getEllipsisCount(0) <= 0) {
            return;
        }
        textView.setGravity(NavigationBarView.ITEM_GRAVITY_START_CENTER);
        textView.setTextAlignment(1);
        textView.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, View.MeasureSpec.getSize(i3) - 2), 1073741824), i4);
    }

    private void rebindIconDrawableToCurrentTheme() {
        loadIconFromResourceId(this.mIconResId);
    }

    private ColorStateList resolveBottomNavDefaultTitleColorStateList() {
        Context context = getContext();
        int attrColor = COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelTertiary, 0);
        int attrColor2 = COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelPrimary, 0);
        int attrColor3 = COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelPrimaryVariant, 0);
        return new ColorStateList(new int[][]{new int[]{-16842910}, new int[]{16842913}, new int[]{android.R.attr.state_checked}, new int[]{16842919}, new int[0]}, new int[]{attrColor, attrColor2, attrColor2, attrColor3, attrColor3});
    }

    private ColorStateList resolveToolbarDefaultTitleColorStateList() {
        Context context = getContext();
        int attrColor = COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelPrimary, 0);
        return new ColorStateList(new int[][]{new int[]{-16842910}, new int[0]}, new int[]{COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorLabelTertiary, 0), attrColor});
    }

    private void setTextViewPreventsShake(TextView textView, boolean z2) {
        if (textView != null) {
            textView.setLayerType(z2 ? 2 : 0, null);
            textView.getPaint().setLinearText(z2);
            textView.getPaint().setSubpixelText(z2);
        }
    }

    private boolean shouldStripPressedForContainerTouch() {
        if (this.mItemStyle == 1 || isEditToolbarItemStyle()) {
            return true;
        }
        if (this.mItemStyle == 0) {
            return BottomFloatingToolbarUtil.isOs17OrAbove();
        }
        return false;
    }

    private void syncBackgroundDrawableBounds(int i3, int i4, int i5, int i6) {
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = this.mMaskRippleDrawable;
        if (cOUIMaskRippleDrawable == null) {
            return;
        }
        cOUIMaskRippleDrawable.setBounds(i3, i4, i5, i6);
    }

    private void updateBackgroundMaskRect() {
        if (this.mMaskRippleDrawable == null) {
            return;
        }
        int i3 = this.mItemStyle;
        if (i3 == 0) {
            applyBackgroundMaskRect(this.mIsSingleVisibleItemInMenu ? -this.mFloatingToolbarSingleMaskInsetHorizontal : this.mFloatingToolbarMaskHorizontalOutset, this.mFloatingToolbarItemHeight / 2.0f);
        } else if (i3 == 1 || isEditToolbarItemStyle()) {
            applyBackgroundMaskRect(this.mBackgroundHorizontalPadding, this.mItemHeight / 2.0f);
        }
    }

    private void updateIconMaskState() {
        if (this.mIcon != null) {
            this.mIcon.setShowMask(isSelected());
        }
    }

    private void updateTextColor() {
        int i3 = this.mItemStyle;
        if (i3 == 1) {
            ColorStateList colorStateListResolveBottomNavDefaultTitleColorStateList = this.mCustomTitleTextColor;
            if (colorStateListResolveBottomNavDefaultTitleColorStateList == null) {
                colorStateListResolveBottomNavDefaultTitleColorStateList = resolveBottomNavDefaultTitleColorStateList();
            }
            if (colorStateListResolveBottomNavDefaultTitleColorStateList != null) {
                this.mTitle.setTextColor(colorStateListResolveBottomNavDefaultTitleColorStateList);
            }
            if (this.mShortTitle == null) {
                initShortTitle(getContext());
            }
            if (colorStateListResolveBottomNavDefaultTitleColorStateList != null) {
                this.mShortTitle.setTextColor(colorStateListResolveBottomNavDefaultTitleColorStateList);
                return;
            }
            return;
        }
        if (i3 == 2) {
            ColorStateList colorStateListResolveBottomNavDefaultTitleColorStateList2 = this.mCustomTitleTextColor;
            if (colorStateListResolveBottomNavDefaultTitleColorStateList2 == null) {
                colorStateListResolveBottomNavDefaultTitleColorStateList2 = resolveBottomNavDefaultTitleColorStateList();
            }
            if (colorStateListResolveBottomNavDefaultTitleColorStateList2 != null) {
                this.mTitle.setTextColor(colorStateListResolveBottomNavDefaultTitleColorStateList2);
                return;
            }
            return;
        }
        if (isToolbarItemStyle()) {
            ColorStateList colorStateListResolveToolbarDefaultTitleColorStateList = this.mCustomTitleTextColor;
            if (colorStateListResolveToolbarDefaultTitleColorStateList == null) {
                colorStateListResolveToolbarDefaultTitleColorStateList = resolveToolbarDefaultTitleColorStateList();
            }
            if (colorStateListResolveToolbarDefaultTitleColorStateList != null) {
                this.mTitle.setTextColor(colorStateListResolveToolbarDefaultTitleColorStateList);
            }
        }
    }

    private void updateViewStyle() {
        int i3 = this.mItemStyle;
        if (i3 == 1) {
            applyNaviVerticalStyle();
        } else if (i3 == 2) {
            applyNaviLargeIconStyle();
        } else if (i3 == 3) {
            applyEditToolbarVerticalStyle();
        } else if (i3 == 4) {
            applyEditToolbarHorizontalStyle();
        } else {
            applyDefaultViewStyle();
        }
        updateTextColor();
    }

    public void apply(COUIBottomFloatingToolbarItem cOUIBottomFloatingToolbarItem) {
        setId(cOUIBottomFloatingToolbarItem.getId());
        setIcon(cOUIBottomFloatingToolbarItem.getIcon());
        setTitle(cOUIBottomFloatingToolbarItem.getTitle());
        setGroupId(cOUIBottomFloatingToolbarItem.getGroupId());
        setContentDescription(cOUIBottomFloatingToolbarItem.getDescription());
        setShowRedDot(cOUIBottomFloatingToolbarItem.isShowRedDot());
        setEnabled(cOUIBottomFloatingToolbarItem.isEnabled());
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public Drawable getIcon() {
        return this.mIcon.getDrawable();
    }

    public Drawable getIconDrawable() {
        COUIFadeMaskImageView cOUIFadeMaskImageView = this.mIcon;
        if (cOUIFadeMaskImageView != null) {
            return cOUIFadeMaskImageView.getDrawable();
        }
        return null;
    }

    public int getItemStyle() {
        return this.mItemStyle;
    }

    public boolean getShowRedDot() {
        return this.mShowRedDot;
    }

    public CharSequence getTitle() {
        return this.mTitle.getText();
    }

    @Override // android.view.ViewGroup, android.view.View
    public int[] onCreateDrawableState(int i3) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i3);
        if (!shouldStripPressedForContainerTouch()) {
            return iArrOnCreateDrawableState;
        }
        int i4 = 0;
        for (int i5 : iArrOnCreateDrawableState) {
            if (i5 == 16842919) {
                i4++;
            }
        }
        if (i4 == 0) {
            return iArrOnCreateDrawableState;
        }
        int[] iArr = new int[iArrOnCreateDrawableState.length - i4];
        int i6 = 0;
        for (int i7 : iArrOnCreateDrawableState) {
            if (i7 != 16842919) {
                iArr[i6] = i7;
                i6++;
            }
        }
        return iArr;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        String str;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mItemStyle == 0 && (getContentDescription() == null || getContentDescription().length() == 0)) {
            StringBuilder sb = new StringBuilder();
            sb.append((Object) this.mTitle.getText());
            if (this.mShowRedDot) {
                str = "," + this.mRedDotDescription;
            } else {
                str = "";
            }
            sb.append(str);
            accessibilityNodeInfo.setContentDescription(sb.toString());
        }
        accessibilityNodeInfo.setClassName(COUIAccessibilityUtil.BUTTON_CLASS_NAME);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int i7 = this.mItemStyle;
        if (i7 == 1) {
            layoutNaviVertical(i3, i4, i5, i6);
        } else if (i7 == 2) {
            layoutNaviLargeIcon(i3, i4, i5, i6);
        } else if (i7 == 3) {
            layoutEditToolbarVertical(i3, i4, i5, i6);
        } else if (i7 == 4) {
            layoutEditToolbarHorizontal(i3, i4, i5, i6);
        } else {
            super.onLayout(z2, i3, i4, i5, i6);
            layoutRedDot();
        }
        updateBackgroundMaskRect();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int i5;
        int i6 = this.mItemStyle;
        if (i6 == 1) {
            int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mItemHeight, 1073741824);
            measureChild(this.mIcon, i3, iMakeMeasureSpec);
            int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
            if (this.mIsWidthAnimating) {
                measureTitleWithEllipsisFix(this.mTitle, View.MeasureSpec.makeMeasureSpec(this.mMaxTitleWidth, 1073741824), iMakeMeasureSpec2);
                if (this.mShortTitle != null && (i5 = this.mMinTitleWidth) > 0) {
                    measureTitleWithEllipsisFix(this.mShortTitle, View.MeasureSpec.makeMeasureSpec(i5, 1073741824), iMakeMeasureSpec2);
                }
            } else {
                int iMakeMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), 1073741824);
                measureTitleWithEllipsisFix(this.mTitle, iMakeMeasureSpec3, iMakeMeasureSpec2);
                TextView textView = this.mShortTitle;
                if (textView != null) {
                    measureTitleWithEllipsisFix(textView, iMakeMeasureSpec3, iMakeMeasureSpec2);
                }
            }
            measureChild(this.mCOUIHintRedDot, i3, iMakeMeasureSpec);
            setMeasuredDimension(View.MeasureSpec.getSize(i3), this.mItemHeight);
            return;
        }
        if (i6 == 3) {
            int iMakeMeasureSpec4 = View.MeasureSpec.makeMeasureSpec(this.mItemHeight, 1073741824);
            measureChild(this.mIcon, i3, iMakeMeasureSpec4);
            measureTitleWithEllipsisFix(this.mTitle, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
            measureChild(this.mCOUIHintRedDot, i3, iMakeMeasureSpec4);
            setMeasuredDimension(View.MeasureSpec.getSize(i3), this.mItemHeight);
            return;
        }
        if (i6 == 4) {
            int iMakeMeasureSpec5 = View.MeasureSpec.makeMeasureSpec(this.mItemHeight, 1073741824);
            measureChild(this.mIcon, i3, iMakeMeasureSpec5);
            this.mTitle.measure(View.MeasureSpec.makeMeasureSpec((View.MeasureSpec.getSize(i3) - this.mIcon.getMeasuredWidth()) - this.mIconTitleGapHorizontal, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
            measureChild(this.mCOUIHintRedDot, i3, iMakeMeasureSpec5);
            setMeasuredDimension(View.MeasureSpec.getSize(i3), this.mItemHeight);
            return;
        }
        if (i6 != 2) {
            measureTitleOrIcon();
            super.onMeasure(i3, i4);
            if (this.mTitle.getVisibility() == 0) {
                measureTitleWithEllipsisFix(this.mTitle, View.MeasureSpec.makeMeasureSpec(this.mTitle.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.mTitle.getMeasuredHeight(), 1073741824));
                return;
            }
            return;
        }
        int iMakeMeasureSpec6 = View.MeasureSpec.makeMeasureSpec(this.mItemHeight, 1073741824);
        int iMakeMeasureSpec7 = View.MeasureSpec.makeMeasureSpec(this.mLargeIconSize, 1073741824);
        this.mIcon.measure(iMakeMeasureSpec7, iMakeMeasureSpec7);
        measureTitleWithEllipsisFix(this.mTitle, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        measureChild(this.mCOUIHintRedDot, i3, iMakeMeasureSpec6);
        setMeasuredDimension(View.MeasureSpec.getSize(i3), this.mItemHeight);
    }

    public void refreshThemeAppearance() {
        this.mMaskRippleDrawable.refresh(getContext());
        updateTextColor();
        rebindIconDrawableToCurrentTheme();
        invalidate();
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.mIcon.setEnabled(z2);
        this.mTitle.setEnabled(z2);
        if (z2) {
            return;
        }
        setShowRedDot(false);
    }

    public void setGroupId(int i3) {
        this.mGroupId = i3;
    }

    public void setIcon(Drawable drawable) {
        this.mIconResId = 0;
        this.mIcon.setImageDrawable(drawable);
    }

    public void setIconResourceId(int i3) {
        this.mIconResId = i3;
        loadIconFromResourceId(i3);
    }

    public void setItemStyle(int i3) {
        if (this.mItemStyle != i3) {
            this.mItemStyle = i3;
            updateViewStyle();
            requestLayout();
        }
    }

    public void setItemTextPreventsShake(boolean z2) {
        setTextViewPreventsShake(this.mTitle, z2);
        setTextViewPreventsShake(this.mShortTitle, z2);
    }

    @Override // android.view.View
    public void setPressed(boolean z2) {
        super.setPressed(z2);
        updateBackgroundMaskRect();
    }

    public void setRedDot(int i3, int i4) {
        this.mRedDotType = i3;
        this.mRedDotNumber = i4;
        if (i3 == 1) {
            this.mShowRedDot = true;
            this.mCOUIHintRedDot.setPointMode(1);
            this.mCOUIHintRedDot.setVisibility(0);
        } else if (i3 != 2) {
            this.mShowRedDot = false;
            this.mCOUIHintRedDot.setVisibility(8);
        } else {
            this.mShowRedDot = true;
            this.mCOUIHintRedDot.setPointMode(2);
            this.mCOUIHintRedDot.setPointNumber(i4);
            this.mCOUIHintRedDot.setVisibility(0);
        }
        requestLayout();
    }

    @Override // android.view.View
    public void setSelected(boolean z2) {
        super.setSelected(z2);
        int i3 = this.mItemStyle;
        if (i3 == 1 || i3 == 2) {
            COUIFadeMaskImageView cOUIFadeMaskImageView = this.mIcon;
            if (cOUIFadeMaskImageView != null) {
                cOUIFadeMaskImageView.setSelected(z2);
            }
            TextView textView = this.mTitle;
            if (textView != null) {
                textView.setSelected(z2);
            }
            updateIconMaskState();
        }
    }

    public void setShowRedDot(boolean z2) {
        this.mShowRedDot = z2;
        this.mCOUIHintRedDot.setVisibility(z2 ? 0 : 8);
    }

    public void setSingleVisibleItemInMenu(boolean z2) {
        if (this.mIsSingleVisibleItemInMenu == z2) {
            return;
        }
        this.mIsSingleVisibleItemInMenu = z2;
        if (this.mItemStyle == 0 && isPressed()) {
            updateBackgroundMaskRect();
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle.setText(charSequence);
        TextView textView = this.mShortTitle;
        if (textView != null) {
            textView.setText(charSequence);
        }
        if (this.mItemStyle != 0) {
            if (getContentDescription() == null || getContentDescription().length() == 0) {
                setContentDescription(charSequence);
            }
        }
    }

    public void setTitleTextColor(ColorStateList colorStateList) {
        this.mCustomTitleTextColor = colorStateList;
        updateTextColor();
    }

    public void startWidthAnimation(int i3, int i4, boolean z2) {
        if (this.mItemStyle != 1 || this.mShortTitle == null) {
            return;
        }
        this.mIsWidthAnimating = true;
        this.mMaxTitleWidth = i3;
        this.mMinTitleWidth = i4;
        float alpha = this.mTitle.getAlpha() * 10000.0f;
        float f3 = z2 ? 0.0f : 10000.0f;
        float alpha2 = this.mShortTitle.getAlpha() * 10000.0f;
        float f4 = z2 ? 10000.0f : 0.0f;
        if (z2) {
            this.mTitleAlphaAnim.getSpring().setResponse(0.2f);
            this.mShortTitleAlphaAnim.getSpring().setResponse(0.3f);
        } else {
            this.mTitleAlphaAnim.getSpring().setResponse(0.3f);
            this.mShortTitleAlphaAnim.getSpring().setResponse(0.2f);
        }
        this.mTitleAlphaAnim.setStartValue(alpha);
        this.mTitleAlphaAnim.animateToFinalPosition(f3);
        this.mShortTitleAlphaAnim.setStartValue(alpha2);
        this.mShortTitleAlphaAnim.animateToFinalPosition(f4);
        setItemTextPreventsShake(true);
        requestLayout();
    }

    public COUIBottomFloatingToolbarItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiFloatingToolbarStyle);
    }

    public COUIBottomFloatingToolbarItemView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIBottomFloatingToolbarItemView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mDefRedDotOffset = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_red_dot_offset);
        this.mRedDotDescription = getResources().getString(com.support.reddot.R.string.red_dot_description);
        this.mBackgroundMaskRect = new RectF();
        this.mShowRedDot = false;
        this.mItemStyle = 0;
        this.mMaxTitleWidth = 0;
        this.mMinTitleWidth = 0;
        this.mIsWidthAnimating = false;
        this.mRedDotType = 0;
        this.mRedDotNumber = 0;
        this.mItemHeight = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_height);
        this.mIconTopMargin = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_icon_top_margin);
        this.mNaviIconHeight = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_icon_height);
        this.mIconTitleGapVertical = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_icon_title_gap_vertical);
        this.mIconTitleGapHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_icon_title_gap_horizontal);
        this.mLargeIconSize = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_large_icon_size);
        this.mLargeIconOverflow = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_large_icon_overflow);
        this.mRedDotTopOverflow = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_red_dot_top_overflow);
        this.mRedDotRightOverflow1Digit = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_red_dot_right_overflow_1_digit);
        this.mRedDotRightOverflow2Digit = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_red_dot_right_overflow_2_digit);
        this.mRedDotRightOverflow3Digit = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_red_dot_right_overflow_3_digit);
        this.mBackgroundHorizontalPadding = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_bg_horizontal_padding);
        this.mFloatingToolbarMaskHorizontalOutset = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_view_space);
        this.mFloatingToolbarSingleMaskInsetHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_single_mask_inset_horizontal);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_height);
        this.mFloatingToolbarItemHeight = dimensionPixelSize;
        initView(context);
        setClickable(true);
        setFocusable(true);
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(context);
        this.mMaskRippleDrawable = cOUIMaskRippleDrawable;
        cOUIMaskRippleDrawable.setCustomRippleMask();
        cOUIMaskRippleDrawable.setDrawableEnabled(true);
        setBackground(cOUIMaskRippleDrawable);
        setLayoutDirection(3);
        setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.coui_floating_toolbar_item_width));
        setMinimumHeight(dimensionPixelSize);
    }
}