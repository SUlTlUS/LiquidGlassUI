package com.coui.appcompat.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.coui.appcompat.accessibilityutil.COUIAccessibilityUtil;
import com.coui.appcompat.material.bottomnavigation.BottomNavigationItemView;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.settings.romupdate.RomUpdateListParser;
import com.support.bottomnavigation.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUINavigationItemView extends BottomNavigationItemView {
    public static final int TIPS_CIRCLE = 1;
    public static final int TIPS_HIDE = 3;
    public static final int TIPS_OVAL = 2;
    private int bigOffsetHorizontal;
    private AccessibilityManager mAccessibilityManager;
    private COUIHintRedDot mCOUIHintRedDot;
    private int mDefRedDotOffset;
    private int mDefRedDotTextOffsetHorizontal;
    private int mDefRedDotTextOffsetVertical;
    private int mEnlargeIconSize;
    private int mEnlargeItemHeight;
    private FrameLayout mFlRoot;
    private ImageView mIcon;
    private FrameLayout mIconContainer;
    private int mIconMarginTop;
    private boolean mIsEnlarge;
    private boolean mIsEnlargeView;
    private boolean mIsHorizontalView;
    private boolean mIsShowPressShadow;
    private final RectF mItemRect;
    private TextView mLargeLabel;
    private int mLayoutType;
    private COUIMaskEffectDrawable mMaskEffectDrawable;
    private int mNavigationType;
    private int mNormalIconSize;
    private int mNormalItemHeight;
    private Rect mRedDotRect;
    private TextView mSmallLabel;
    private COUIStateEffectDrawable mStateEffectBackground;
    private int mTextSize;
    private int[] offset;
    private int smallOffsetHorizontal;

    public COUINavigationItemView(Context context) {
        super(context);
        this.mItemRect = new RectF();
        this.smallOffsetHorizontal = -2;
        this.bigOffsetHorizontal = 1;
        this.mEnlargeIconSize = getResources().getDimensionPixelSize(R.dimen.coui_navigation_enlarge_icon_size);
        this.mNormalIconSize = getResources().getDimensionPixelSize(R.dimen.coui_navigation_normal_icon_size);
        this.mEnlargeItemHeight = getResources().getDimensionPixelSize(R.dimen.coui_navigation_enlarge_item_height);
        this.mNormalItemHeight = getResources().getDimensionPixelSize(R.dimen.coui_navigation_normal_item_height);
        this.mIconMarginTop = getResources().getDimensionPixelSize(R.dimen.coui_navigation_enlarge_icon_margin_top);
        this.mDefRedDotTextOffsetVertical = getResources().getDimensionPixelSize(R.dimen.coui_navigation_red_dot_with_number_vertical_offset);
        this.mDefRedDotTextOffsetHorizontal = 0;
        this.mDefRedDotOffset = getResources().getDimensionPixelSize(R.dimen.coui_navigation_red_dot_offset);
        this.offset = new int[2];
        this.mIsEnlarge = false;
        this.mIsEnlargeView = false;
        this.mIsShowPressShadow = false;
        this.mIsHorizontalView = false;
        this.mAccessibilityManager = null;
        this.mSmallLabel = (TextView) findViewById(com.google.android.material.R.id.navigation_bar_item_small_label_view);
        this.mLargeLabel = (TextView) findViewById(com.google.android.material.R.id.navigation_bar_item_large_label_view);
        this.mIcon = (ImageView) findViewById(com.google.android.material.R.id.navigation_bar_item_icon_view);
        this.mIconContainer = (FrameLayout) findViewById(com.google.android.material.R.id.navigation_bar_item_icon_container);
        this.mFlRoot = (FrameLayout) findViewById(R.id.fl_root);
        this.mCOUIHintRedDot = (COUIHintRedDot) findViewById(R.id.red_dot);
        setTextSize(context.getResources().getDimensionPixelSize(R.dimen.coui_navigation_item_text_size));
        setClipChildren(false);
        setClipToPadding(false);
        setHapticFeedbackEnabled(false);
        this.mDefRedDotTextOffsetHorizontal = context.getResources().getDimensionPixelSize(R.dimen.coui_navigation_red_dot_with_number_horizontal_offset);
        configStateEffectBackground();
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG);
    }

    private void configStateEffectBackground() {
        setDefaultFocusHighlightEnabled(false);
        float dimension = getContext().getResources().getDimension(R.dimen.coui_navigation_item_background_radius);
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(getContext(), 1);
        this.mMaskEffectDrawable = cOUIMaskEffectDrawable;
        cOUIMaskEffectDrawable.setMaskRect(this.mItemRect, dimension, dimension);
        this.mMaskEffectDrawable.enableSelectedState(false);
        this.mMaskEffectDrawable.setMinProgressForTouchEnterAnimation(0.0f);
        COUIStateEffectDrawable cOUIStateEffectDrawable = new COUIStateEffectDrawable(new Drawable[]{getBackground() == null ? new ColorDrawable(0) : getBackground(), this.mMaskEffectDrawable});
        this.mStateEffectBackground = cOUIStateEffectDrawable;
        super.setBackground(cOUIStateEffectDrawable);
    }

    public static int dp2px(Context context, int i3) {
        return Double.valueOf(((double) (context.getResources().getDisplayMetrics().density * i3)) + 0.5d).intValue();
    }

    private void enlargeSelectView(boolean z2) {
        if (this.mIsEnlargeView) {
            setIconSize(z2 ? this.mEnlargeIconSize : this.mNormalIconSize);
            this.mSmallLabel.setVisibility(z2 ? 8 : 0);
            if (this.mIsHorizontalView) {
                return;
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mIconContainer.getLayoutParams();
            layoutParams.setMargins(0, z2 ? 0 : this.mIconMarginTop, 0, 0);
            this.mIconContainer.setLayoutParams(layoutParams);
        }
    }

    private void getOffset(int[] iArr) {
        if (this.mCOUIHintRedDot.getPointMode() == 1) {
            int i3 = this.mDefRedDotOffset;
            iArr[1] = i3;
            iArr[0] = i3;
            return;
        }
        iArr[1] = this.mDefRedDotTextOffsetVertical;
        iArr[0] = this.mDefRedDotTextOffsetHorizontal;
        if (this.mCOUIHintRedDot.getPointNumber() >= 100 && this.mCOUIHintRedDot.getPointNumber() < 1000) {
            iArr[0] = iArr[0] + UIUtil.dip2px(getContext(), this.bigOffsetHorizontal);
        } else {
            if (this.mCOUIHintRedDot.getPointNumber() <= 0 || this.mCOUIHintRedDot.getPointNumber() >= 10) {
                return;
            }
            iArr[0] = iArr[0] + UIUtil.dip2px(getContext(), this.smallOffsetHorizontal);
        }
    }

    private boolean isRtlMode() {
        return getLayoutDirection() == 1;
    }

    private void layoutRedDot() {
        if (this.mCOUIHintRedDot.getVisibility() == 8) {
            return;
        }
        if (this.mRedDotRect == null) {
            this.mRedDotRect = new Rect();
        }
        getOffset(this.offset);
        if (ViewCompat.getLayoutDirection(this) == 1) {
            this.mRedDotRect.set(this.mIconContainer.getLeft(), this.mIconContainer.getTop(), this.mIconContainer.getLeft() + this.mCOUIHintRedDot.getMeasuredWidth(), this.mIconContainer.getTop() + this.mCOUIHintRedDot.getMeasuredHeight());
            Rect rect = this.mRedDotRect;
            int[] iArr = this.offset;
            rect.offset(-iArr[0], -iArr[1]);
        } else {
            this.mRedDotRect.set(this.mIconContainer.getRight() - this.mCOUIHintRedDot.getMeasuredWidth(), this.mIconContainer.getTop(), this.mIconContainer.getRight(), this.mIconContainer.getTop() + this.mCOUIHintRedDot.getMeasuredHeight());
            Rect rect2 = this.mRedDotRect;
            int[] iArr2 = this.offset;
            rect2.offset(iArr2[0], -iArr2[1]);
        }
        COUIHintRedDot cOUIHintRedDot = this.mCOUIHintRedDot;
        Rect rect3 = this.mRedDotRect;
        cOUIHintRedDot.layout(rect3.left, rect3.top, rect3.right, rect3.bottom);
    }

    private void leftToRight() {
        int measuredWidth;
        int measuredWidth2;
        View childAt = this.mFlRoot.getChildAt(0);
        View childAt2 = this.mFlRoot.getChildAt(1);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_navigation_enlarge_icon_horizontal_margin);
        int measuredWidth3 = ((this.mFlRoot.getMeasuredWidth() - childAt.getMeasuredWidth()) - childAt2.getMeasuredWidth()) - dimensionPixelSize;
        int i3 = measuredWidth3 > 0 ? measuredWidth3 / 2 : 0;
        if (this.mIsEnlargeView) {
            measuredWidth2 = (this.mFlRoot.getMeasuredWidth() / 2) - (childAt.getMeasuredWidth() / 2);
            measuredWidth = (this.mFlRoot.getMeasuredWidth() / 2) + (childAt.getMeasuredWidth() / 2);
        } else {
            measuredWidth = i3 + childAt.getMeasuredWidth();
            measuredWidth2 = i3;
        }
        childAt.layout(measuredWidth2, (this.mFlRoot.getMeasuredHeight() / 2) - (childAt.getMeasuredHeight() / 2), measuredWidth, (this.mFlRoot.getMeasuredHeight() / 2) + (childAt.getMeasuredHeight() / 2));
        int right = childAt.getRight() + dimensionPixelSize;
        int measuredWidth4 = this.mFlRoot.getMeasuredWidth() - i3;
        int measuredHeight = (this.mFlRoot.getMeasuredHeight() / 2) - (childAt2.getMeasuredHeight() / 2);
        int measuredHeight2 = (this.mFlRoot.getMeasuredHeight() / 2) + (childAt2.getMeasuredHeight() / 2);
        childAt2.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth4 - right, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), 1073741824));
        childAt2.layout(right, measuredHeight, measuredWidth4, measuredHeight2);
        if (this.mIsEnlargeView) {
            childAt2.setVisibility(8);
        }
        this.mIsHorizontalView = true;
    }

    private void rightToLeft() {
        View childAt = this.mFlRoot.getChildAt(0);
        View childAt2 = this.mFlRoot.getChildAt(1);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_navigation_enlarge_icon_horizontal_margin);
        int measuredWidth = ((this.mFlRoot.getMeasuredWidth() - childAt.getMeasuredWidth()) - childAt2.getMeasuredWidth()) - dimensionPixelSize;
        int i3 = measuredWidth > 0 ? measuredWidth / 2 : 0;
        if (this.mIsEnlargeView) {
            childAt.layout((this.mFlRoot.getMeasuredWidth() / 2) - (childAt.getMeasuredWidth() / 2), (this.mFlRoot.getMeasuredHeight() / 2) - (childAt.getMeasuredHeight() / 2), (this.mFlRoot.getMeasuredWidth() / 2) + (childAt.getMeasuredWidth() / 2), (this.mFlRoot.getMeasuredHeight() / 2) + (childAt.getMeasuredHeight() / 2));
        } else {
            int measuredWidth2 = this.mFlRoot.getMeasuredWidth() - i3;
            childAt.layout(measuredWidth2 - childAt.getMeasuredWidth(), (this.mFlRoot.getMeasuredHeight() / 2) - (childAt.getMeasuredHeight() / 2), measuredWidth2, (this.mFlRoot.getMeasuredHeight() / 2) + (childAt.getMeasuredHeight() / 2));
        }
        int left = childAt.getLeft() - dimensionPixelSize;
        int measuredHeight = (this.mFlRoot.getMeasuredHeight() / 2) - (childAt2.getMeasuredHeight() / 2);
        int measuredHeight2 = (this.mFlRoot.getMeasuredHeight() / 2) + (childAt2.getMeasuredHeight() / 2);
        childAt2.measure(View.MeasureSpec.makeMeasureSpec(left - i3, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), 1073741824));
        childAt2.layout(i3, measuredHeight, left, measuredHeight2);
        if (this.mIsEnlargeView) {
            childAt2.setVisibility(8);
        }
        this.mIsHorizontalView = true;
    }

    public void childLayout(int i3) {
        this.mLayoutType = i3;
        requestLayout();
    }

    public void clearColorFilter() {
        this.mIcon.clearColorFilter();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.mIsShowPressShadow) {
            if (motionEvent.getActionMasked() == 0) {
                this.mStateEffectBackground.setTouched(true);
            }
            if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3) {
                this.mStateEffectBackground.setTouched(false);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public COUIHintRedDot getCOUIHintRedDot() {
        return this.mCOUIHintRedDot;
    }

    @Override // com.coui.appcompat.material.bottomnavigation.BottomNavigationItemView, com.coui.appcompat.material.navigation.NavigationBarItemView
    public int getItemDefaultMarginResId() {
        return R.dimen.coui_navigation_enlarge_default_margin;
    }

    @Override // com.coui.appcompat.material.bottomnavigation.BottomNavigationItemView, com.coui.appcompat.material.navigation.NavigationBarItemView
    public int getItemLayoutResId() {
        return R.layout.coui_navigation_item_layout;
    }

    @Override // com.coui.appcompat.material.navigation.NavigationBarItemView, androidx.appcompat.view.menu.MenuView.ItemView
    public void initialize(MenuItemImpl menuItemImpl, int i3) {
        super.initialize(menuItemImpl, i3);
        TooltipCompat.setTooltipText(this, "");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIsEnlarge) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mFlRoot.getLayoutParams();
            layoutParams.gravity = 80;
            if (this.mIsEnlargeView) {
                layoutParams.height = this.mEnlargeItemHeight;
                setIconSize(this.mNormalIconSize);
                setIconTintList(null);
            } else {
                layoutParams.height = this.mNormalItemHeight;
            }
            this.mFlRoot.setLayoutParams(layoutParams);
            enlargeSelectView(isSelected());
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mCOUIHintRedDot.setPointMode(0);
        this.mCOUIHintRedDot.setPointText("");
        this.mCOUIHintRedDot.setVisibility(4);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        boolean zOnHoverEvent = super.onHoverEvent(motionEvent);
        if (this.mAccessibilityManager.isTouchExplorationEnabled()) {
            return true;
        }
        return zOnHoverEvent;
    }

    @Override // com.coui.appcompat.material.navigation.NavigationBarItemView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mNavigationType == 0) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompatWrap = AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo);
            accessibilityNodeInfoCompatWrap.setCollectionItemInfo(null);
            accessibilityNodeInfoCompatWrap.setRoleDescription(null);
            accessibilityNodeInfo.setClassName(COUIAccessibilityUtil.BUTTON_CLASS_NAME);
            accessibilityNodeInfo.setSelected(false);
            accessibilityNodeInfoCompatWrap.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        boolean z3 = getContext().getResources().getBoolean(R.bool.is_big_layout);
        if (this.mLayoutType == 1 || !z3) {
            topToBottom();
        } else if (isRtlMode()) {
            rightToLeft();
        } else {
            leftToRight();
        }
        layoutRedDot();
    }

    @Override // com.coui.appcompat.material.navigation.NavigationBarItemView, android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.mItemRect.set(0.0f, 0.0f, i3, i4);
    }

    public void refresh() {
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable != null) {
            cOUIStateEffectDrawable.refresh(getContext());
        }
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable == null) {
            super.setBackground(drawable);
        } else if (drawable == null) {
            cOUIStateEffectDrawable.setViewBackground(new ColorDrawable(0));
        } else {
            cOUIStateEffectDrawable.setViewBackground(drawable);
        }
    }

    @Override // com.coui.appcompat.material.navigation.NavigationBarItemView, androidx.appcompat.view.menu.MenuView.ItemView
    public void setChecked(boolean z2) {
        enlargeSelectView(z2);
        setSelected(z2);
    }

    public void setEnlarge(boolean z2, boolean z3) {
        this.mIsEnlarge = z2;
        this.mIsEnlargeView = z3;
    }

    public void setIconTintForWhite() {
        if (this.mIsEnlargeView) {
            return;
        }
        this.mIcon.setColorFilter(-1);
    }

    public void setNavigationType(int i3) {
        if (this.mNavigationType != i3) {
            this.mNavigationType = i3;
        }
    }

    public void setShowPressShadow(boolean z2) {
        this.mIsShowPressShadow = z2;
    }

    public void setTextSize(int i3) {
        this.mTextSize = i3;
        this.mSmallLabel.setTextSize(0, i3);
        this.mLargeLabel.setTextSize(0, this.mTextSize);
        requestLayout();
    }

    public void topToBottom() {
        View childAt = this.mFlRoot.getChildAt(0);
        View childAt2 = this.mFlRoot.getChildAt(1);
        int dimensionPixelSize = (this.mIsEnlargeView && isSelected()) ? 0 : getResources().getDimensionPixelSize(R.dimen.coui_navigation_icon_margin_top);
        childAt.layout((this.mFlRoot.getMeasuredWidth() / 2) - (childAt.getMeasuredWidth() / 2), dimensionPixelSize, (this.mFlRoot.getMeasuredWidth() / 2) + (childAt.getMeasuredWidth() / 2), childAt.getMeasuredHeight() + dimensionPixelSize);
        int measuredWidth = (this.mFlRoot.getMeasuredWidth() / 2) - (childAt2.getMeasuredWidth() / 2);
        int measuredWidth2 = (this.mFlRoot.getMeasuredWidth() / 2) + (childAt2.getMeasuredWidth() / 2);
        int measuredHeight = this.mFlRoot.getMeasuredHeight() - childAt2.getMeasuredHeight();
        Resources resources = getResources();
        int i3 = R.dimen.coui_navigation_icon_margin_top;
        int dimensionPixelSize2 = measuredHeight - resources.getDimensionPixelSize(i3);
        Resources resources2 = getResources();
        int i4 = R.dimen.coui_navigation_text_margin_top;
        childAt2.layout(measuredWidth, dimensionPixelSize2 + resources2.getDimensionPixelSize(i4), measuredWidth2, (this.mFlRoot.getMeasuredHeight() - getResources().getDimensionPixelSize(i3)) + getResources().getDimensionPixelSize(i4));
        if (this.mIsEnlargeView) {
            childAt2.setVisibility(0);
        }
        this.mIsHorizontalView = false;
    }
}