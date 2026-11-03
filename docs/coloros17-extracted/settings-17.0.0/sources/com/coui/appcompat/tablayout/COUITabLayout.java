package com.coui.appcompat.tablayout;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pools;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.scrollview.COUIHorizontalScrollView;
import com.decouple.injector.adaptor.BaseConfig;
import com.support.tablayout.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes7.dex */
public class COUITabLayout extends COUIHorizontalScrollView {
    private static final int ANIMATION_DURATION = 300;
    protected static final int DEFAULT_GAP_TEXT_ICON = 8;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
    private static final float DEFAULT_MAXIMUM_WIDTH_RATIO = 0.7f;
    public static final int DEFAULT_MIN_INDICATOR = 32;
    private static final int FIFTY = 50;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_FILL = 0;
    private static final int HUNDRED_FIFTY = 150;
    public static final int INVALID_WIDTH = -1;
    private static final String MEDIUM_FONT = "sans-serif-medium";
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    public static final int MOTION_NON_ADJACENT_OFFSET = 24;
    public static final float ONE = 1.0f;
    private static final float POINT_FIVE = 0.5f;
    public static final int PRESS_RIPPLE_CORNER_RADIUS = 8;
    private static final String REGULAR_FONT = "sans-serif";
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final Pools.Pool<COUITab> TAB_POOL = new Pools.SynchronizedPool(16);
    private static final int THREE_HUNDRED = 300;
    public static final float ZERO = 0.0f;
    private AdapterChangeListener mAdapterChangeListener;
    private int mBottomDividerColor;
    private boolean mBottomDividerEnabled;
    private int mButtonMarginEnd;
    private ArrayList<PrivateButton> mButtons;
    private OnTabSelectedListener mCurrentVpSelectedListener;
    private float mDefaultIndicatorRatio;
    private float mDefaultTabTextSize;
    protected int mDotHorizontalOffset;
    protected int mDotVerticalOffsetFromNumberRed;
    protected int mDotVerticalOffsetFromOnlyRed;
    protected boolean mEnableVibrator;
    private ArgbEvaluator mEvaluator;
    private int mIndicatorPadding;
    private boolean mIsUpdateindicatorposition;
    private float mLastOffset;
    private int mLongTextViewHeight;
    private int mMode;
    protected boolean mNeedAdjust;
    protected int mNormalTextColor;
    protected Typeface mNormalTypeface;
    private int mOriginalRequestedTabMaxWidth;
    private int mOriginalRequestedTabMinWidth;
    private TabLayoutOnPageChangeListener mPageChangeListener;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mPagerAdapterObserver;
    protected int mRequestedTabMaxWidth;
    private int mRequestedTabMinWidth;
    private int mResizeHeight;
    private ValueAnimator mScrollAnimator;
    private int mSelectedIndicatorColor;
    private int mSelectedIndicatorDisableColor;
    private OnTabSelectedListener mSelectedListener;
    private final ArrayList<OnTabSelectedListener> mSelectedListeners;
    private int mSelectedPosition;
    protected COUITab mSelectedTab;
    protected int mSelectedTextColor;
    protected Typeface mSelectedTypeface;
    private boolean mSetupViewPagerImplicitly;
    private int mStyle;
    protected boolean mTabAlreadyMeasure;
    protected final int mTabBackgroundResId;

    @Deprecated
    private int mTabGravity;
    private int mTabMinDivider;
    private int mTabMinMargin;
    protected int mTabPaddingBottom;
    protected int mTabPaddingEnd;
    protected int mTabPaddingStart;
    protected int mTabPaddingTop;
    protected final COUISlidingTabStrip mTabStrip;
    private int mTabTextAppearance;
    protected ColorStateList mTabTextColors;
    private int mTabTextDisabledColor;
    private float mTabTextSize;
    private Typeface mTabTextTypeFace;
    private final Pools.Pool<COUITabView> mTabViewPool;
    private final ArrayList<COUITab> mTabs;
    private int mTextColorBlue;
    private int mTextColorGreen;
    private int mTextColorRed;
    private ViewPager mViewPager;

    /* JADX INFO: renamed from: com.coui.appcompat.tablayout.COUITabLayout$1, reason: invalid class name */
    public class AnonymousClass1 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass1() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUITabLayout.this.scrollTo(((Integer) valueAnimator.getAnimatedValue()).intValue(), 0);
        }
    }

    public class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        private boolean mAutoRefresh;

        public AdapterChangeListener() {
        }

        @Override // androidx.viewpager.widget.ViewPager.OnAdapterChangeListener
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            if (COUITabLayout.access$000(COUITabLayout.this) == viewPager) {
                COUITabLayout.this.setPagerAdapter(pagerAdapter2, this.mAutoRefresh);
            }
        }

        public void setAutoRefresh(boolean z2) {
            this.mAutoRefresh = z2;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public interface OnTabSelectedListener {
        void onTabReselected(COUITab cOUITab);

        void onTabSelected(COUITab cOUITab);

        void onTabUnselected(COUITab cOUITab);
    }

    public class PagerAdapterObserver extends DataSetObserver {
        public PagerAdapterObserver() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            COUITabLayout.this.populateFromPagerAdapter();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            COUITabLayout.this.populateFromPagerAdapter();
        }
    }

    public class PrivateButton {
        View.OnClickListener mButtonClicklistener;
        Drawable mButtonDrawable;

        public PrivateButton(Drawable drawable, View.OnClickListener onClickListener) {
            this.mButtonDrawable = drawable;
            this.mButtonClicklistener = onClickListener;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabGravity {
    }

    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int mPreviousScrollState;
        private int mScrollState;
        private final WeakReference<COUITabLayout> mTabLayoutRef;

        public TabLayoutOnPageChangeListener(COUITabLayout cOUITabLayout) {
            this.mTabLayoutRef = new WeakReference<>(cOUITabLayout);
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i3) {
            this.mPreviousScrollState = this.mScrollState;
            this.mScrollState = i3;
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i3, float f3, int i4) {
            COUITabLayout cOUITabLayout = this.mTabLayoutRef.get();
            if (cOUITabLayout != null) {
                int i5 = this.mScrollState;
                cOUITabLayout.setScrollPosition(i3, f3, i5 != 2 || this.mPreviousScrollState == 1, (i5 == 2 && this.mPreviousScrollState == 0) ? false : true);
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i3) {
            COUITabLayout cOUITabLayout = this.mTabLayoutRef.get();
            if (cOUITabLayout == null || cOUITabLayout.getSelectedTabPosition() == i3 || i3 >= cOUITabLayout.getTabCount()) {
                return;
            }
            int i4 = this.mScrollState;
            cOUITabLayout.selectTab(cOUITabLayout.getTabAt(i3), i4 == 0 || (i4 == 2 && this.mPreviousScrollState == 0));
        }

        public void reset() {
            this.mPreviousScrollState = 0;
            this.mScrollState = 0;
        }
    }

    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.OnTabSelectedListener
        public void onTabReselected(COUITab cOUITab) {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.OnTabSelectedListener
        public void onTabSelected(COUITab cOUITab) {
            this.mViewPager.setCurrentItem(cOUITab.getPosition());
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.OnTabSelectedListener
        public void onTabUnselected(COUITab cOUITab) {
        }
    }

    public COUITabLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ ViewPager access$000(COUITabLayout cOUITabLayout) {
        return cOUITabLayout.mViewPager;
    }

    private void addTabFromItemView(COUITabItem cOUITabItem) {
        COUITab cOUITabNewTab = newTab();
        CharSequence charSequence = cOUITabItem.mText;
        if (charSequence != null) {
            cOUITabNewTab.setText(charSequence);
        }
        Drawable drawable = cOUITabItem.mIcon;
        if (drawable != null) {
            cOUITabNewTab.setIcon(drawable);
        }
        int i3 = cOUITabItem.mCustomLayout;
        if (i3 != 0) {
            cOUITabNewTab.setCustomView(i3);
        }
        if (!TextUtils.isEmpty(cOUITabItem.getContentDescription())) {
            cOUITabNewTab.setContentDescription(cOUITabItem.getContentDescription());
        }
        addTab(cOUITabNewTab);
    }

    private void addTabView(COUITab cOUITab) {
        this.mTabStrip.addView(cOUITab.mView, cOUITab.getPosition(), createLayoutParamsForTabs());
    }

    private void addViewInternal(View view) {
        if (!(view instanceof COUITabItem)) {
            throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
        }
        addTabFromItemView((COUITabItem) view);
    }

    private void animateToTab(int i3) {
        if (i3 == -1) {
            return;
        }
        if (getWindowToken() == null || !ViewCompat.isLaidOut(this) || this.mTabStrip.childrenNeedLayout()) {
            setScrollPosition(i3, 0.0f, true);
            return;
        }
        int scrollX = getScrollX();
        int iCalculateScrollXForTab = calculateScrollXForTab(i3, 0.0f);
        if (scrollX != iCalculateScrollXForTab) {
            ensureScrollAnimator();
            this.mScrollAnimator.setIntValues(scrollX, iCalculateScrollXForTab);
            this.mScrollAnimator.start();
        }
        this.mTabStrip.animateIndicatorToPosition(i3, 300);
    }

    private void applyModeAndGravity() {
        updateTabViews(true);
    }

    private int calculateScrollXForTab(int i3, float f3) {
        int width;
        int width2 = 0;
        if (getWidth() == 0) {
            return 0;
        }
        View childAt = this.mTabStrip.getChildAt(i3);
        int i4 = i3 + 1;
        View childAt2 = i4 < this.mTabStrip.getChildCount() ? this.mTabStrip.getChildAt(i4) : null;
        if (childAt != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            width = childAt.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
        } else {
            width = 0;
        }
        if (childAt2 != null) {
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) childAt2.getLayoutParams();
            width2 = layoutParams2.rightMargin + childAt2.getWidth() + layoutParams2.leftMargin;
        }
        int width3 = (width / 2) - (getWidth() / 2);
        if (childAt != null) {
            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            width3 += ViewCompat.getLayoutDirection(this) == 0 ? (childAt.getLeft() - layoutParams3.leftMargin) + (getPaddingLeft() / 2) + (getPaddingRight() / 2) : ((childAt.getRight() + layoutParams3.rightMargin) - (getPaddingLeft() / 2)) - (getPaddingRight() / 2);
        }
        int i5 = (int) ((width + width2) * 0.5f * f3);
        return ViewCompat.getLayoutDirection(this) == 0 ? width3 + i5 : width3 - i5;
    }

    private void configureTab(COUITab cOUITab, int i3) {
        cOUITab.setPosition(i3);
        this.mTabs.add(i3, cOUITab);
        int size = this.mTabs.size();
        while (true) {
            i3++;
            if (i3 >= size) {
                return;
            } else {
                this.mTabs.get(i3).setPosition(i3);
            }
        }
    }

    private static ColorStateList createColorStateList(int i3, int i4, int i5) {
        return new ColorStateList(new int[][]{new int[]{16842913, 16842910}, new int[]{-16842913, -16842910}, HorizontalScrollView.EMPTY_STATE_SET}, new int[]{i5, i4, i3});
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        return new LinearLayout.LayoutParams(1, -1);
    }

    private COUITabView createTabView(COUITab cOUITab) {
        Pools.Pool<COUITabView> pool = this.mTabViewPool;
        COUITabView cOUITabViewAcquire = pool != null ? pool.acquire() : null;
        if (cOUITabViewAcquire == null) {
            cOUITabViewAcquire = new COUITabView(getContext(), this);
        }
        cOUITabViewAcquire.setTab(cOUITab);
        cOUITabViewAcquire.setFocusable(true);
        cOUITabViewAcquire.setMinimumWidth(getTabMinWidth());
        cOUITabViewAcquire.setEnabled(isEnabled());
        return cOUITabViewAcquire;
    }

    private void dispatchTabReselected(COUITab cOUITab) {
        for (int size = this.mSelectedListeners.size() - 1; size >= 0; size--) {
            this.mSelectedListeners.get(size).onTabReselected(cOUITab);
        }
    }

    private void dispatchTabSelected(COUITab cOUITab) {
        for (int size = this.mSelectedListeners.size() - 1; size >= 0; size--) {
            this.mSelectedListeners.get(size).onTabSelected(cOUITab);
        }
    }

    private void dispatchTabUnselected(COUITab cOUITab) {
        for (int size = this.mSelectedListeners.size() - 1; size >= 0; size--) {
            this.mSelectedListeners.get(size).onTabUnselected(cOUITab);
        }
    }

    private void drawButton(Canvas canvas) {
        int width;
        int scrollX;
        int width2;
        int width3;
        int scrollX2;
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_button_width);
        if (this.mButtons.size() == 1) {
            Drawable drawable = this.mButtons.get(0).mButtonDrawable;
            int dimensionPixelSize2 = this.mButtonMarginEnd;
            if (dimensionPixelSize2 == -1) {
                dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_button_default_horizontal_margin);
            }
            if (ViewCompat.getLayoutDirection(this) == 1) {
                width2 = getScrollX() + dimensionPixelSize2;
                width3 = dimensionPixelSize + dimensionPixelSize2;
                scrollX2 = getScrollX();
            } else {
                width2 = (getWidth() - (dimensionPixelSize + dimensionPixelSize2)) + getScrollX();
                width3 = getWidth() - dimensionPixelSize2;
                scrollX2 = getScrollX();
            }
            int i3 = width3 + scrollX2;
            int height = getHeight() / 2;
            Resources resources = getResources();
            int i4 = R.dimen.coui_tab_layout_button_default_vertical_margin;
            drawable.setBounds(width2, height - resources.getDimensionPixelSize(i4), i3, (getHeight() / 2) + getResources().getDimensionPixelSize(i4));
            drawable.draw(canvas);
            return;
        }
        if (this.mButtons.size() >= 2) {
            for (int i5 = 0; i5 < this.mButtons.size(); i5++) {
                int dimensionPixelSize3 = this.mButtonMarginEnd;
                if (dimensionPixelSize3 == -1) {
                    dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_multi_button_default_horizontal_margin);
                }
                if (ViewCompat.getLayoutDirection(this) == 1) {
                    scrollX = dimensionPixelSize3 + (getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_multi_button_default_padding) * i5);
                    width = getScrollX();
                } else {
                    width = getWidth() - ((dimensionPixelSize3 + dimensionPixelSize) + (getResources().getDimensionPixelSize(R.dimen.coui_tab_layout_multi_button_default_padding) * i5));
                    scrollX = getScrollX();
                }
                int i6 = scrollX + width;
                Drawable drawable2 = this.mButtons.get(i5).mButtonDrawable;
                int height2 = getHeight() / 2;
                Resources resources2 = getResources();
                int i7 = R.dimen.coui_tab_layout_button_default_vertical_margin;
                drawable2.setBounds(i6, height2 - resources2.getDimensionPixelSize(i7), i6 + dimensionPixelSize, (getHeight() / 2) + getResources().getDimensionPixelSize(i7));
                drawable2.draw(canvas);
            }
        }
    }

    private void ensureScrollAnimator() {
        if (this.mScrollAnimator == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.mScrollAnimator = valueAnimator;
            valueAnimator.setInterpolator(new COUIEaseInterpolator());
            this.mScrollAnimator.setDuration(300L);
            this.mScrollAnimator.addUpdateListener(new AnonymousClass1());
        }
    }

    private int getDefaultHeight() {
        int size = this.mTabs.size();
        for (int i3 = 0; i3 < size; i3++) {
            COUITab cOUITab = this.mTabs.get(i3);
            if (cOUITab != null && cOUITab.getIcon() != null && !TextUtils.isEmpty(cOUITab.getText())) {
                return 72;
            }
        }
        return 48;
    }

    private float getScrollPosition() {
        return this.mTabStrip.getIndicatorPosition();
    }

    private int getTabMinWidth() {
        return 0;
    }

    private int getTabScrollRange() {
        return Math.max(0, ((this.mTabStrip.getWidth() - getWidth()) - getPaddingLeft()) - getPaddingRight());
    }

    private void removeTabViewAt(int i3) {
        COUITabView cOUITabView = (COUITabView) this.mTabStrip.getChildAt(i3);
        this.mTabStrip.removeViewAt(i3);
        if (cOUITabView != null) {
            cOUITabView.reset();
            this.mTabViewPool.release(cOUITabView);
        }
        requestLayout();
    }

    private void setSelectedTabView(int i3) {
        int childCount = this.mTabStrip.getChildCount();
        if (i3 < childCount) {
            int i4 = 0;
            while (i4 < childCount) {
                this.mTabStrip.getChildAt(i4).setSelected(i4 == i3);
                i4++;
            }
        }
    }

    private void updateAllTabs() {
        int size = this.mTabs.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mTabs.get(i3).updateView();
        }
    }

    private void updateTextColor() {
        this.mNormalTextColor = this.mTabTextColors.getDefaultColor();
        int colorForState = this.mTabTextColors.getColorForState(new int[]{16842910, 16842913}, COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorPrimaryText, 0));
        this.mSelectedTextColor = colorForState;
        this.mTextColorRed = Math.abs(Color.red(colorForState) - Color.red(this.mNormalTextColor));
        this.mTextColorGreen = Math.abs(Color.green(this.mSelectedTextColor) - Color.green(this.mNormalTextColor));
        this.mTextColorBlue = Math.abs(Color.blue(this.mSelectedTextColor) - Color.blue(this.mNormalTextColor));
    }

    public void addButton(int i3, View.OnClickListener onClickListener) {
        addButton(ContextCompat.getDrawable(getContext(), i3), onClickListener);
    }

    public void addOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        if (this.mSelectedListeners.contains(onTabSelectedListener)) {
            return;
        }
        this.mSelectedListeners.add(onTabSelectedListener);
    }

    public void addTab(COUITab cOUITab) {
        addTab(cOUITab, this.mTabs.isEmpty());
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view) {
        addViewInternal(view);
    }

    @Deprecated
    public void changeTabTextFont(COUITabView cOUITabView, boolean z2) {
    }

    public void clearOnTabSelectedListeners() {
        this.mSelectedListeners.clear();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        Canvas canvas2;
        super.dispatchDraw(canvas);
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip != null) {
            if (cOUISlidingTabStrip.getIndicatorBackgroundPaint() != null) {
                canvas.drawRect(this.mTabStrip.getIndicatorBackgroundPaddingLeft() + getScrollX(), getHeight() - this.mTabStrip.getIndicatorBackgroundHeight(), (getWidth() + getScrollX()) - this.mTabStrip.getIndicatorBackgroundPaddingRight(), getHeight(), this.mTabStrip.getIndicatorBackgroundPaint());
                canvas2 = canvas;
            } else {
                canvas2 = canvas;
            }
            if (this.mTabStrip.getSelectedIndicatorPaint() != null) {
                canvas2.drawText(" ", 0.0f, 0.0f, this.mTabStrip.getSelectedIndicatorPaint());
                if (this.mTabStrip.getIndicatorRight() > this.mTabStrip.getIndicatorLeft()) {
                    int paddingLeft = getPaddingLeft() + this.mTabStrip.getIndicatorLeft();
                    int paddingLeft2 = getPaddingLeft() + this.mTabStrip.getIndicatorRight();
                    int scrollX = (getScrollX() + getPaddingLeft()) - this.mIndicatorPadding;
                    int scrollX2 = ((getScrollX() + getWidth()) - getPaddingRight()) + this.mIndicatorPadding;
                    if (paddingLeft2 > scrollX && paddingLeft < scrollX2) {
                        if (paddingLeft < scrollX) {
                            paddingLeft = scrollX;
                        }
                        if (paddingLeft2 > scrollX2) {
                            paddingLeft2 = scrollX2;
                        }
                        canvas2.drawRect(paddingLeft, getHeight() - this.mTabStrip.mSelectedIndicatorHeight, paddingLeft2, getHeight(), this.mTabStrip.getSelectedIndicatorPaint());
                    }
                }
                if (this.mBottomDividerEnabled) {
                    canvas2.drawRect(getLeft(), getHeight() - 1, getScrollX() + getWidth() + this.mIndicatorPadding, getHeight(), this.mTabStrip.getBottomDividerPaint());
                }
            }
        } else {
            canvas2 = canvas;
        }
        drawButton(canvas2);
    }

    public int dpToPx(int i3) {
        return Math.round(getResources().getDisplayMetrics().density * i3);
    }

    public boolean enableTab(int i3, boolean z2) {
        COUITabView cOUITabView;
        COUITab tabAt = getTabAt(i3);
        if (tabAt == null || (cOUITabView = tabAt.mView) == null) {
            return false;
        }
        cOUITabView.setEnabled(z2);
        return true;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateLayoutParams(attributeSet);
    }

    public float getDefaultIndicatoRatio() {
        return this.mDefaultIndicatorRatio;
    }

    public int getIndicatorAnimTime(int i3, int i4) {
        return Math.min(300, (Math.abs(i3 - i4) * 50) + 150);
    }

    public int getIndicatorBackgroundHeight() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return -1;
        }
        return cOUISlidingTabStrip.getIndicatorBackgroundHeight();
    }

    public int getIndicatorBackgroundPaddingLeft() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return -1;
        }
        return cOUISlidingTabStrip.getIndicatorBackgroundPaddingLeft();
    }

    public int getIndicatorBackgroundPaddingRight() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return -1;
        }
        return cOUISlidingTabStrip.getIndicatorBackgroundPaddingRight();
    }

    public int getIndicatorBackgroundPaintColor() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return -1;
        }
        return cOUISlidingTabStrip.getIndicatorBackgroundPaint().getColor();
    }

    public int getIndicatorPadding() {
        return this.mIndicatorPadding;
    }

    public float getIndicatorWidthRatio() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return -1.0f;
        }
        return cOUISlidingTabStrip.getIndicatorWidthRatio();
    }

    public int getRequestedTabMaxWidth() {
        return this.mRequestedTabMaxWidth;
    }

    public int getRequestedTabMinWidth() {
        return this.mRequestedTabMinWidth;
    }

    public int getSelectedIndicatorColor() {
        return this.mSelectedIndicatorColor;
    }

    public int getSelectedTabPosition() {
        COUITab cOUITab = this.mSelectedTab;
        if (cOUITab != null) {
            return cOUITab.getPosition();
        }
        return -1;
    }

    public COUITab getTabAt(int i3) {
        if (i3 < 0 || i3 >= getTabCount()) {
            return null;
        }
        return this.mTabs.get(i3);
    }

    public int getTabCount() {
        return this.mTabs.size();
    }

    public int getTabGravity() {
        return this.mTabGravity;
    }

    public int getTabMinDivider() {
        return this.mTabMinDivider;
    }

    public int getTabMinMargin() {
        return this.mTabMinMargin;
    }

    public int getTabMode() {
        return this.mMode;
    }

    public int getTabPaddingBottom() {
        return this.mTabPaddingBottom;
    }

    public int getTabPaddingEnd() {
        return this.mTabPaddingEnd;
    }

    public int getTabPaddingStart() {
        return this.mTabPaddingStart;
    }

    public int getTabPaddingTop() {
        return this.mTabPaddingTop;
    }

    public COUISlidingTabStrip getTabStrip() {
        return this.mTabStrip;
    }

    public ColorStateList getTabTextColors() {
        return this.mTabTextColors;
    }

    public float getTabTextSize() {
        return this.mTabTextSize;
    }

    @Deprecated
    public boolean isResizeText() {
        return false;
    }

    public boolean isUpdateindicatorposition() {
        return this.mIsUpdateindicatorposition;
    }

    public COUITab newTab() {
        COUITab cOUITabAcquire = TAB_POOL.acquire();
        if (cOUITabAcquire == null) {
            cOUITabAcquire = new COUITab();
        }
        cOUITabAcquire.mParent = this;
        cOUITabAcquire.mView = createTabView(cOUITabAcquire);
        return cOUITabAcquire;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mViewPager == null) {
            ViewParent parent = getParent();
            if (parent instanceof ViewPager) {
                setupWithViewPager((ViewPager) parent, true, true);
            }
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mTabAlreadyMeasure = false;
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mSetupViewPagerImplicitly) {
            setupWithViewPager(null);
            this.mSetupViewPagerImplicitly = false;
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getTabCount(), false, 0));
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.widget.HorizontalScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            for (int i3 = 0; i3 < this.mButtons.size(); i3++) {
                if (this.mButtons.get(i3).mButtonClicklistener != null && this.mButtons.get(i3).mButtonDrawable.getBounds().contains(((int) motionEvent.getX()) + getScrollX(), (int) motionEvent.getY())) {
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int i7;
        super.onLayout(z2, i3, i4, i5, i6);
        if (!this.mNeedAdjust || (i7 = this.mSelectedPosition) < 0 || i7 >= this.mTabStrip.getChildCount()) {
            return;
        }
        this.mNeedAdjust = false;
        scrollTo(calculateScrollXForTab(this.mSelectedPosition, 0.0f), 0);
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        int iDpToPx = dpToPx(getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
        int mode = View.MeasureSpec.getMode(i4);
        if (mode == Integer.MIN_VALUE) {
            i4 = View.MeasureSpec.makeMeasureSpec(Math.min(iDpToPx, View.MeasureSpec.getSize(i4)), 1073741824);
        } else if (mode == 0) {
            i4 = View.MeasureSpec.makeMeasureSpec(iDpToPx, 1073741824);
        }
        int size = View.MeasureSpec.getSize(i3);
        if (this.mOriginalRequestedTabMaxWidth == -1) {
            this.mRequestedTabMaxWidth = (int) (size * 0.7f);
        }
        if (View.MeasureSpec.getMode(i3) != 1073741824) {
            setMeasuredDimension(0, 0);
            return;
        }
        int i5 = this.mMode;
        if (i5 == 0) {
            getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE), i4);
        } else if (i5 == 1) {
            getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), i4);
        }
        setMeasuredDimension(size, getChildAt(0).getMeasuredHeight());
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            for (int i3 = 0; i3 < this.mButtons.size(); i3++) {
                if (this.mButtons.get(i3).mButtonClicklistener != null && this.mButtons.get(i3).mButtonDrawable.getBounds().contains(((int) motionEvent.getX()) + getScrollX(), (int) motionEvent.getY())) {
                    this.mButtons.get(i3).mButtonClicklistener.onClick(this);
                    return true;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void populateFromPagerAdapter() {
        int currentItem;
        removeAllTabs();
        PagerAdapter pagerAdapter = this.mPagerAdapter;
        if (pagerAdapter != null) {
            int count = pagerAdapter.getCount();
            PagerAdapter pagerAdapter2 = this.mPagerAdapter;
            if (pagerAdapter2 instanceof COUIFragmentStatePagerAdapter) {
                COUIFragmentStatePagerAdapter cOUIFragmentStatePagerAdapter = (COUIFragmentStatePagerAdapter) pagerAdapter2;
                for (int i3 = 0; i3 < count; i3++) {
                    if (cOUIFragmentStatePagerAdapter.getPageIcon(i3) > 0) {
                        addTab(newTab().setIcon(cOUIFragmentStatePagerAdapter.getPageIcon(i3)), false);
                    } else {
                        addTab(newTab().setText(cOUIFragmentStatePagerAdapter.getPageTitle(i3)), false);
                    }
                }
            } else {
                for (int i4 = 0; i4 < count; i4++) {
                    addTab(newTab().setText(this.mPagerAdapter.getPageTitle(i4)), false);
                }
            }
            ViewPager viewPager = this.mViewPager;
            if (viewPager == null || count <= 0 || (currentItem = viewPager.getCurrentItem()) == getSelectedTabPosition() || currentItem >= getTabCount()) {
                return;
            }
            selectTab(getTabAt(currentItem));
        }
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUITabLayout, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUITabLayout, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes != null) {
            int i3 = R.styleable.COUITabLayout_couiTabTextColor;
            if (typedArrayObtainStyledAttributes.hasValue(i3)) {
                this.mTabTextColors = typedArrayObtainStyledAttributes.getColorStateList(i3);
            }
            int i4 = R.styleable.COUITabLayout_couiTabIndicatorColor;
            if (typedArrayObtainStyledAttributes.hasValue(i4)) {
                setSelectedTabIndicatorColor(typedArrayObtainStyledAttributes.getColor(i4, 0));
            }
            updateTextColor();
            typedArrayObtainStyledAttributes.recycle();
        }
        for (COUITab cOUITab : this.mTabs) {
            if (cOUITab != null && cOUITab.getView() != null) {
                cOUITab.getView().refresh();
            }
        }
    }

    public void removeAllButtons(int i3) {
        this.mButtons.clear();
        setTabMode(i3);
        invalidate();
    }

    public void removeAllTabs() {
        for (int childCount = this.mTabStrip.getChildCount() - 1; childCount >= 0; childCount--) {
            removeTabViewAt(childCount);
        }
        Iterator<COUITab> it = this.mTabs.iterator();
        while (it.hasNext()) {
            COUITab next = it.next();
            it.remove();
            next.reset();
            TAB_POOL.release(next);
        }
        this.mSelectedTab = null;
        this.mTabAlreadyMeasure = false;
    }

    public void removeOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mSelectedListeners.remove(onTabSelectedListener);
    }

    public void removeTab(COUITab cOUITab) {
        if (cOUITab.mParent != this) {
            throw new IllegalArgumentException("COUITab does not belong to this TabLayout.");
        }
        removeTabAt(cOUITab.getPosition());
    }

    public void removeTabAt(int i3) {
        COUITab cOUITab = this.mSelectedTab;
        int position = cOUITab != null ? cOUITab.getPosition() : 0;
        removeTabViewAt(i3);
        COUITab cOUITabRemove = this.mTabs.remove(i3);
        if (cOUITabRemove != null) {
            cOUITabRemove.reset();
            TAB_POOL.release(cOUITabRemove);
        }
        int size = this.mTabs.size();
        for (int i4 = i3; i4 < size; i4++) {
            this.mTabs.get(i4).setPosition(i4);
        }
        if (position == i3) {
            selectTab(this.mTabs.isEmpty() ? null : this.mTabs.get(Math.max(0, i3 - 1)));
        }
    }

    public void resetTextColorAfterAnim() {
        int childCount = this.mTabStrip.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = this.mTabStrip.getChildAt(i3);
            if (childAt instanceof COUITabView) {
                ((COUITabView) childAt).getTextView().setTextColor(this.mTabTextColors);
            }
        }
    }

    public void selectTab(COUITab cOUITab) {
        selectTab(cOUITab, true);
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView
    public void setEnableVibrator(boolean z2) {
        this.mEnableVibrator = z2;
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.mTabStrip.setSelectedIndicatorColor(z2 ? this.mSelectedIndicatorColor : this.mSelectedIndicatorDisableColor);
        for (int i3 = 0; i3 < getTabCount(); i3++) {
            enableTab(i3, z2);
        }
    }

    public void setIndicatorAnimTime(int i3) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip != null) {
            cOUISlidingTabStrip.setIndicatorAnimTime(i3);
        }
    }

    public void setIndicatorBackgroundColor(int i3) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        cOUISlidingTabStrip.getIndicatorBackgroundPaint().setColor(i3);
    }

    public void setIndicatorBackgroundHeight(int i3) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        cOUISlidingTabStrip.setIndicatorBackgroundHeight(i3);
    }

    public void setIndicatorBackgroundPaddingLeft(int i3) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        cOUISlidingTabStrip.setIndicatorBackgroundPaddingLeft(i3);
    }

    public void setIndicatorBackgroundPaddingRight(int i3) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        cOUISlidingTabStrip.setIndicatorBackgroundPaddingRight(i3);
    }

    public void setIndicatorPadding(int i3) {
        this.mIndicatorPadding = i3;
        requestLayout();
    }

    public void setIndicatorWidthRatio(float f3) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.mTabStrip;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        this.mDefaultIndicatorRatio = f3;
        cOUISlidingTabStrip.setIndicatorWidthRatio(f3);
    }

    @Deprecated
    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        OnTabSelectedListener onTabSelectedListener2 = this.mSelectedListener;
        if (onTabSelectedListener2 != null) {
            removeOnTabSelectedListener(onTabSelectedListener2);
        }
        this.mSelectedListener = onTabSelectedListener;
        if (onTabSelectedListener != null) {
            addOnTabSelectedListener(onTabSelectedListener);
        }
    }

    public void setPaddingLeftAndRight(int i3, int i4) {
        ViewCompat.setPaddingRelative(this, i3, 0, i4, 0);
    }

    public void setPagerAdapter(PagerAdapter pagerAdapter, boolean z2) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter2 = this.mPagerAdapter;
        if (pagerAdapter2 != null && (dataSetObserver = this.mPagerAdapterObserver) != null) {
            pagerAdapter2.unregisterDataSetObserver(dataSetObserver);
        }
        this.mPagerAdapter = pagerAdapter;
        if (z2 && pagerAdapter != null) {
            if (this.mPagerAdapterObserver == null) {
                this.mPagerAdapterObserver = new PagerAdapterObserver();
            }
            pagerAdapter.registerDataSetObserver(this.mPagerAdapterObserver);
        }
        populateFromPagerAdapter();
    }

    public void setRequestedTabMaxWidth(int i3) {
        this.mRequestedTabMaxWidth = i3;
        this.mOriginalRequestedTabMaxWidth = i3;
    }

    public void setRequestedTabMinWidth(int i3) {
        this.mRequestedTabMinWidth = i3;
        this.mOriginalRequestedTabMinWidth = i3;
    }

    public void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        ensureScrollAnimator();
        this.mScrollAnimator.addListener(animatorListener);
    }

    public void setScrollPosition(int i3, float f3, boolean z2) {
        setScrollPosition(i3, f3, z2, true);
    }

    public void setSelectedTabIndicatorColor(int i3) {
        this.mTabStrip.setSelectedIndicatorColor(i3);
        this.mSelectedIndicatorColor = i3;
    }

    public void setSelectedTabIndicatorHeight(int i3) {
        this.mTabStrip.setSelectedIndicatorHeight(i3);
    }

    public void setTabGravity(int i3) {
    }

    public void setTabMinDivider(int i3) {
        this.mTabMinDivider = i3;
        requestLayout();
    }

    public void setTabMinMargin(int i3) {
        this.mTabMinMargin = i3;
        ViewCompat.setPaddingRelative(this, i3, 0, i3, 0);
        requestLayout();
    }

    public void setTabMode(int i3) {
        if (i3 != this.mMode) {
            this.mMode = i3;
            applyModeAndGravity();
        }
    }

    public void setTabPaddingBottom(int i3) {
        this.mTabPaddingBottom = i3;
        requestLayout();
    }

    public void setTabPaddingEnd(int i3) {
        this.mTabPaddingEnd = i3;
        requestLayout();
    }

    public void setTabPaddingStart(int i3) {
        this.mTabPaddingStart = i3;
        requestLayout();
    }

    public void setTabPaddingTop(int i3) {
        this.mTabPaddingTop = i3;
        requestLayout();
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.mTabTextColors != colorStateList) {
            this.mTabTextColors = colorStateList;
            updateTextColor();
            updateAllTabs();
        }
    }

    public void setTabTextSize(float f3) {
        if (this.mTabStrip != null) {
            this.mDefaultTabTextSize = f3;
            this.mTabTextSize = f3;
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter pagerAdapter) {
        setPagerAdapter(pagerAdapter, false);
    }

    public void setUpdateindicatorposition(boolean z2) {
        this.mIsUpdateindicatorposition = z2;
    }

    public void setupWithViewPager(ViewPager viewPager) {
        setupWithViewPager(viewPager, true);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    public void updateTabViews(boolean z2) {
        for (int i3 = 0; i3 < this.mTabStrip.getChildCount(); i3++) {
            COUITabView cOUITabView = (COUITabView) this.mTabStrip.getChildAt(i3);
            cOUITabView.setMinimumWidth(getTabMinWidth());
            if (cOUITabView.getTextView() != null) {
                ViewCompat.setPaddingRelative(cOUITabView.getTextView(), this.mTabPaddingStart, this.mTabPaddingTop, this.mTabPaddingEnd, this.mTabPaddingBottom);
            }
            if (z2) {
                cOUITabView.requestLayout();
            }
        }
    }

    public COUITabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiTabLayoutStyle);
    }

    public void addButton(Drawable drawable, View.OnClickListener onClickListener) {
        addButton(drawable, onClickListener, (Drawable) null, (View.OnClickListener) null);
    }

    public void addTab(COUITab cOUITab, int i3) {
        addTab(cOUITab, i3, this.mTabs.isEmpty());
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i3) {
        addViewInternal(view);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateDefaultLayoutParams();
    }

    public void selectTab(COUITab cOUITab, boolean z2) {
        COUITab cOUITab2 = this.mSelectedTab;
        if (cOUITab2 == cOUITab) {
            if (cOUITab2 != null) {
                dispatchTabReselected(cOUITab);
                return;
            }
            return;
        }
        int position = cOUITab != null ? cOUITab.getPosition() : -1;
        if (z2) {
            if ((cOUITab2 == null || cOUITab2.getPosition() == -1) && position != -1) {
                setScrollPosition(position, 0.0f, true);
            } else {
                animateToTab(position);
            }
            if (position != -1) {
                setSelectedTabView(position);
            }
            this.mSelectedPosition = position;
        } else if (isEnabled() && this.mEnableVibrator) {
            performHapticFeedback(302);
        }
        if (cOUITab2 != null) {
            dispatchTabUnselected(cOUITab2);
        }
        this.mSelectedTab = cOUITab;
        if (cOUITab != null) {
            dispatchTabSelected(cOUITab);
        }
    }

    public void setScrollPosition(int i3, float f3, boolean z2, boolean z3) {
        int iRound = Math.round(i3 + f3);
        if (iRound < 0 || iRound >= this.mTabStrip.getChildCount()) {
            return;
        }
        if (z3) {
            this.mTabStrip.setIndicatorPositionFromTabPosition(i3, f3);
        } else if (this.mTabStrip.mSelectedPosition != getSelectedTabPosition()) {
            this.mTabStrip.mSelectedPosition = getSelectedTabPosition();
            this.mTabStrip.updateIndicatorPosition();
        }
        ValueAnimator valueAnimator = this.mScrollAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mScrollAnimator.cancel();
        }
        scrollTo(calculateScrollXForTab(i3, f3), 0);
        if (z2) {
            setSelectedTabView(iRound, f3);
        }
    }

    public void setupWithViewPager(ViewPager viewPager, boolean z2) {
        setupWithViewPager(viewPager, z2, false);
    }

    public COUITabLayout(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.COUITabLayoutBaseStyle);
    }

    private void setupWithViewPager(ViewPager viewPager, boolean z2, boolean z3) {
        ViewPager viewPager2 = this.mViewPager;
        if (viewPager2 != null) {
            TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = this.mPageChangeListener;
            if (tabLayoutOnPageChangeListener != null) {
                viewPager2.removeOnPageChangeListener(tabLayoutOnPageChangeListener);
            }
            AdapterChangeListener adapterChangeListener = this.mAdapterChangeListener;
            if (adapterChangeListener != null) {
                this.mViewPager.removeOnAdapterChangeListener(adapterChangeListener);
            }
        }
        OnTabSelectedListener onTabSelectedListener = this.mCurrentVpSelectedListener;
        if (onTabSelectedListener != null) {
            removeOnTabSelectedListener(onTabSelectedListener);
            this.mCurrentVpSelectedListener = null;
        }
        if (viewPager != null) {
            this.mViewPager = viewPager;
            if (this.mPageChangeListener == null) {
                this.mPageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.mPageChangeListener.reset();
            viewPager.addOnPageChangeListener(this.mPageChangeListener);
            ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
            this.mCurrentVpSelectedListener = viewPagerOnTabSelectedListener;
            addOnTabSelectedListener(viewPagerOnTabSelectedListener);
            if (viewPager.getAdapter() != null) {
                setPagerAdapter(viewPager.getAdapter(), z2);
            }
            if (this.mAdapterChangeListener == null) {
                this.mAdapterChangeListener = new AdapterChangeListener();
            }
            this.mAdapterChangeListener.setAutoRefresh(z2);
            viewPager.addOnAdapterChangeListener(this.mAdapterChangeListener);
            setScrollPosition(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.mViewPager = null;
            setPagerAdapter(null, false);
        }
        this.mSetupViewPagerImplicitly = z3;
    }

    public void addButton(int i3, View.OnClickListener onClickListener, int i4, View.OnClickListener onClickListener2) {
        addButton(ContextCompat.getDrawable(getContext(), i3), onClickListener, ContextCompat.getDrawable(getContext(), i4), onClickListener2);
    }

    public void addTab(COUITab cOUITab, boolean z2) {
        addTab(cOUITab, this.mTabs.size(), z2);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        addViewInternal(view);
    }

    public COUITabLayout(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mTabs = new ArrayList<>();
        this.mSelectedListeners = new ArrayList<>();
        this.mTabViewPool = new Pools.SimplePool(12);
        this.mRequestedTabMaxWidth = -1;
        this.mSelectedPosition = 0;
        this.mLastOffset = 0.0f;
        this.mEvaluator = new ArgbEvaluator();
        this.mIsUpdateindicatorposition = false;
        this.mButtons = new ArrayList<>();
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.mStyle = styleAttribute;
            if (styleAttribute == 0) {
                this.mStyle = i3;
            }
        } else {
            this.mStyle = i3;
        }
        this.mSelectedTypeface = Typeface.create("sans-serif-medium", 0);
        this.mNormalTypeface = Typeface.create(REGULAR_FONT, 0);
        setHorizontalScrollBarEnabled(false);
        COUISlidingTabStrip cOUISlidingTabStrip = new COUISlidingTabStrip(context, this);
        this.mTabStrip = cOUISlidingTabStrip;
        super.addView(cOUISlidingTabStrip, 0, new FrameLayout.LayoutParams(-2, -1));
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUITabLayout, i3, i4);
        cOUISlidingTabStrip.setSelectedIndicatorHeight(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabIndicatorHeight, 0));
        int color = typedArrayObtainStyledAttributes.getColor(R.styleable.COUITabLayout_couiTabIndicatorColor, 0);
        this.mSelectedIndicatorColor = color;
        cOUISlidingTabStrip.setSelectedIndicatorColor(color);
        this.mBottomDividerColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUITabLayout_couiTabBottomDividerColor, 0);
        this.mBottomDividerEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUITabLayout_couiTabBottomDividerEnabled, false);
        cOUISlidingTabStrip.setBottomDividerColor(this.mBottomDividerColor);
        setIndicatorBackgroundHeight(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabIndicatorBackgroundHeight, 0));
        setIndicatorBackgroundColor(typedArrayObtainStyledAttributes.getColor(R.styleable.COUITabLayout_couiTabIndicatorBackgroundColor, 0));
        setIndicatorBackgroundPaddingLeft(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabIndicatorBackgroundPaddingLeft, 0));
        setIndicatorBackgroundPaddingRight(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabIndicatorBackgroundPaddingRight, 0));
        setIndicatorWidthRatio(typedArrayObtainStyledAttributes.getFloat(R.styleable.COUITabLayout_couiTabIndicatorWidthRatio, 0.0f));
        this.mResizeHeight = getResources().getDimensionPixelOffset(R.dimen.coui_tablayout_default_resize_height);
        this.mLongTextViewHeight = getResources().getDimensionPixelOffset(R.dimen.tablayout_long_text_view_height);
        this.mTabMinDivider = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUITabLayout_couiTabMinDivider, -1);
        this.mTabMinMargin = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUITabLayout_couiTabMinMargin, -1);
        this.mIndicatorPadding = getResources().getDimensionPixelOffset(R.dimen.coui_tablayout_indicator_padding);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabPadding, -1);
        this.mTabPaddingStart = dimensionPixelSize;
        this.mTabPaddingTop = dimensionPixelSize;
        this.mTabPaddingEnd = dimensionPixelSize;
        this.mTabPaddingBottom = dimensionPixelSize;
        this.mTabPaddingStart = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabPaddingStart, dimensionPixelSize);
        this.mTabPaddingTop = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabPaddingTop, this.mTabPaddingTop);
        this.mTabPaddingEnd = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabPaddingEnd, this.mTabPaddingEnd);
        this.mTabPaddingBottom = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabPaddingBottom, this.mTabPaddingBottom);
        this.mTabPaddingStart = Math.max(0, this.mTabPaddingStart);
        this.mTabPaddingTop = Math.max(0, this.mTabPaddingTop);
        this.mTabPaddingEnd = Math.max(0, this.mTabPaddingEnd);
        this.mTabPaddingBottom = Math.max(0, this.mTabPaddingBottom);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUITabLayout_couiTabTextAppearance, R.style.TextAppearance_Design_COUITab);
        this.mTabTextAppearance = resourceId;
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, androidx.appcompat.R.styleable.TextAppearance);
        try {
            float dimensionPixelSize2 = typedArrayObtainStyledAttributes2.getDimensionPixelSize(androidx.appcompat.R.styleable.TextAppearance_android_textSize, 0);
            this.mTabTextSize = dimensionPixelSize2;
            this.mDefaultTabTextSize = dimensionPixelSize2;
            this.mTabTextColors = typedArrayObtainStyledAttributes2.getColorStateList(androidx.appcompat.R.styleable.TextAppearance_android_textColor);
            typedArrayObtainStyledAttributes2.recycle();
            int i5 = R.styleable.COUITabLayout_couiTabTextColor;
            if (typedArrayObtainStyledAttributes.hasValue(i5)) {
                this.mTabTextColors = typedArrayObtainStyledAttributes.getColorStateList(i5);
            }
            this.mTabTextDisabledColor = COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorDisabledNeutral, 0);
            int i6 = R.styleable.COUITabLayout_couiTabSelectedTextColor;
            if (typedArrayObtainStyledAttributes.hasValue(i6)) {
                this.mTabTextColors = createColorStateList(this.mTabTextColors.getDefaultColor(), this.mTabTextDisabledColor, typedArrayObtainStyledAttributes.getColor(i6, 0));
            }
            this.mRequestedTabMinWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUITabLayout_couiTabMinWidth, -1);
            this.mTabBackgroundResId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.COUITabLayout_couiTabBackground, 0);
            this.mMode = typedArrayObtainStyledAttributes.getInt(R.styleable.COUITabLayout_couiTabMode, 1);
            this.mTabGravity = typedArrayObtainStyledAttributes.getInt(R.styleable.COUITabLayout_couiTabGravity, 0);
            this.mEnableVibrator = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUITabLayout_couiTabEnableVibrator, true);
            this.mSelectedIndicatorDisableColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUITabLayout_couiTabIndicatorDisableColor, getResources().getColor(R.color.couiTabIndicatorDisableColor));
            int i7 = R.styleable.COUITabLayout_couiTabTextSize;
            if (typedArrayObtainStyledAttributes.hasValue(i7)) {
                float dimension = typedArrayObtainStyledAttributes.getDimension(i7, 0.0f);
                this.mTabTextSize = dimension;
                this.mDefaultTabTextSize = dimension;
            }
            this.mOriginalRequestedTabMinWidth = this.mRequestedTabMinWidth;
            this.mOriginalRequestedTabMaxWidth = this.mRequestedTabMaxWidth;
            this.mButtonMarginEnd = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUITabLayout_couiTabButtonMarginEnd, -1);
            typedArrayObtainStyledAttributes.recycle();
            this.mDotHorizontalOffset = context.getResources().getDimensionPixelSize(com.support.reddot.R.dimen.coui_dot_horizontal_offset);
            this.mDotVerticalOffsetFromOnlyRed = context.getResources().getDimensionPixelSize(com.support.reddot.R.dimen.coui_dot_vertical_offset_only_red);
            this.mDotVerticalOffsetFromNumberRed = context.getResources().getDimensionPixelSize(com.support.reddot.R.dimen.coui_dot_vertical_offset_number_red);
            applyModeAndGravity();
            updateTextColor();
            setOverScrollMode(1);
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes2.recycle();
            throw th;
        }
    }

    private void setSelectedTabView(int i3, float f3) {
        COUITabView cOUITabView;
        float f4;
        if (Math.abs(f3 - this.mLastOffset) > 0.5f || f3 == 0.0f) {
            this.mSelectedPosition = i3;
        }
        this.mLastOffset = f3;
        if (i3 != this.mSelectedPosition && isEnabled()) {
            COUITabView cOUITabView2 = (COUITabView) this.mTabStrip.getChildAt(i3);
            if (f3 >= 0.5f) {
                cOUITabView = (COUITabView) this.mTabStrip.getChildAt(i3 - 1);
                f4 = f3 - 0.5f;
            } else {
                cOUITabView = (COUITabView) this.mTabStrip.getChildAt(i3 + 1);
                f4 = 0.5f - f3;
            }
            float f5 = f4 / 0.5f;
            if (cOUITabView.getTextView() != null) {
                cOUITabView.getTextView().setTextColor(((Integer) this.mEvaluator.evaluate(f5, Integer.valueOf(this.mSelectedTextColor), Integer.valueOf(this.mNormalTextColor))).intValue());
            }
            if (cOUITabView2.getTextView() != null) {
                cOUITabView2.getTextView().setTextColor(((Integer) this.mEvaluator.evaluate(f5, Integer.valueOf(this.mNormalTextColor), Integer.valueOf(this.mSelectedTextColor))).intValue());
            }
        }
        if (f3 != 0.0f || i3 >= getTabCount()) {
            return;
        }
        int i4 = 0;
        while (true) {
            boolean z2 = true;
            if (i4 < getTabCount()) {
                View childAt = this.mTabStrip.getChildAt(i4);
                COUITabView cOUITabView3 = (COUITabView) childAt;
                if (cOUITabView3.getTextView() != null) {
                    cOUITabView3.getTextView().setTextColor(this.mTabTextColors);
                }
                if (i4 != i3) {
                    z2 = false;
                }
                childAt.setSelected(z2);
                i4++;
            } else {
                this.mNeedAdjust = true;
                return;
            }
        }
    }

    public void addTab(COUITab cOUITab, int i3, boolean z2) {
        if (cOUITab.mParent == this) {
            configureTab(cOUITab, i3);
            addTabView(cOUITab);
            if (z2) {
                cOUITab.select();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("COUITab belongs to a different TabLayout.");
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i3, ViewGroup.LayoutParams layoutParams) {
        addViewInternal(view);
    }

    @Deprecated
    public void setTabTextSize(float f3, boolean z2) {
        setTabTextSize(f3);
    }

    public void setTabTextColors(int i3, int i4) {
        setTabTextColors(createColorStateList(i3, this.mTabTextDisabledColor, i4));
    }

    public void addButton(Drawable drawable, View.OnClickListener onClickListener, Drawable drawable2, View.OnClickListener onClickListener2) {
        this.mButtons.clear();
        this.mButtons.add(new PrivateButton(drawable, onClickListener));
        if (drawable2 != null) {
            this.mButtons.add(new PrivateButton(drawable2, onClickListener2));
        }
        setTabMode(0);
        invalidate();
    }
}