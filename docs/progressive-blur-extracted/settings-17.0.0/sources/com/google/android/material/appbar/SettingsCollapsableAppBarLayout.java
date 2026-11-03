package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.android.settings.R;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.google.android.material.appbar.olivelink.COUICollapsableAppBarLayoutParams;
import com.google.android.material.appbar.olivelink.COUICollapsableAppBarLayoutParamsExtKt;
import com.oplus.settings.utils.LogUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes8.dex */
public class SettingsCollapsableAppBarLayout extends COUICollapsableAppBarLayout {
    public static final int DEFAULT_SCROLL_FLAG = 19;
    public static final float FLOAT_0 = 0.0f;
    public static final float FLOAT_1 = 1.0f;
    public static final int MODE_COLLAPSABLE = 0;
    public static final int MODE_FIXED_COLLAPSED = 1;
    public static final int MODE_FIXED_EXPANDED = 2;
    private static final String MODE_STATE_KEY = "MODE_STATE_KEY";
    private static final int NUM_17 = 17;
    private static final String OFFSET_STATE_KEY = "OFFSET_STATE_KEY";
    private static final String SUPER_STATE_KEY = "SUPER_STATE_KEY";
    private static final String TAG = "SettingsCollapsableAppBarLayout";
    private static final String TITLE_FRACTION_STATE_KEY = "TITLE_FRACTION_STATE_KEY";
    private boolean mAutoExpand;
    private int mCollapsingToolbarHeight;
    private int mEndPaddingBottom;
    private int mMode;
    private boolean mNeedUpdateModeAfterOffsetChanged;
    private int mOffset;
    private int mStartPaddingBottom;
    private boolean mSubtitleHideEnable;
    private int mSubtitleViewHeight;
    private boolean mUpdateOffset;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public static class ScrollBehavior extends COUIDividerAppBarLayout.DividerBehavior {
        private static final float SNAP_APP_BAR_OFFSET_SPAN_EPSILON = 1.0E-4f;
        private float mAttachBounce;
        private float mAttachResponse;
        private float mFollowBounce;
        private float mFollowHeaderBounce;
        private float mFollowHeaderResponse;
        private float mFollowListResponseDown;
        private float mFollowListResponseUp;
        private float mFollowResponse;
        private int mLastStartedType;
        private boolean mShouldSnapToBottom;
        private boolean mUseSpring = true;

        public ScrollBehavior() {
            COUICollapsableAppBarLayoutParams cOUICollapsableAppBarLayoutParams = COUICollapsableAppBarLayoutParamsExtKt.get(COUICollapsableAppBarLayoutParams.INSTANCE);
            this.mAttachResponse = cOUICollapsableAppBarLayoutParams.getAttachResponse();
            this.mAttachBounce = cOUICollapsableAppBarLayoutParams.getAttachBounce();
            this.mFollowResponse = cOUICollapsableAppBarLayoutParams.getFollowResponse();
            this.mFollowListResponseUp = cOUICollapsableAppBarLayoutParams.getFollowListResponseUp();
            this.mFollowListResponseDown = cOUICollapsableAppBarLayoutParams.getFollowListResponseDown();
            this.mFollowBounce = cOUICollapsableAppBarLayoutParams.getFollowBounce();
            this.mFollowHeaderResponse = cOUICollapsableAppBarLayoutParams.getFollowHeaderResponse();
            this.mFollowHeaderBounce = cOUICollapsableAppBarLayoutParams.getFollowHeaderBounce();
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, int i3, int i4) {
            if (!this.mUseSpring) {
                super.animateOffsetWithDuration(coordinatorLayout, cOUIAppBarLayout, i3, i4);
            } else {
                if (getTopBottomOffsetForScrollingSibling() == i3) {
                    return;
                }
                setHeaderTopBottomOffsetInternal(coordinatorLayout, cOUIAppBarLayout, i3, Integer.MIN_VALUE, Integer.MAX_VALUE, true, this.mAttachBounce, this.mAttachResponse, true, false);
            }
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public boolean canDragView(COUIAppBarLayout cOUIAppBarLayout) {
            return false;
        }

        public boolean contentInScreen(View view) {
            return (view.canScrollVertically(1) || view.canScrollVertically(-1)) ? false : true;
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ boolean fling(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, float f3) {
            return fling(coordinatorLayout, (COUIAppBarLayout) view, i3, i4, f3);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, com.google.android.material.appbar.COUIHeaderBehavior
        public int getTopBottomOffsetForScrollingSibling() {
            if (!this.mShouldSnapToBottom) {
                return super.getTopBottomOffsetForScrollingSibling();
            }
            this.mShouldSnapToBottom = false;
            return 0;
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public boolean isUseSpring() {
            return this.mUseSpring;
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3, int i4, int[] iArr, int i5) {
            onNestedPreScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, i3, i4, iArr, i5);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            onNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, i3, i4, i5, i6, i7, iArr);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i3, int i4) {
            return onStartNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, view3, i3, i4);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3) {
            onStopNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, i3);
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ int scroll(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, int i5, int i6) {
            return scroll(coordinatorLayout, (COUIAppBarLayout) view, i3, i4, i5, i6);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ boolean canDragView(View view) {
            return canDragView((COUIAppBarLayout) view);
        }

        public boolean fling(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, int i3, int i4, float f3) {
            if (!this.mUseSpring) {
                return super.fling(coordinatorLayout, cOUIAppBarLayout, i3, i4, f3);
            }
            snapToChildIfNeeded(coordinatorLayout, cOUIAppBarLayout);
            return false;
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3, int i4, int[] iArr, int i5) {
            if ((view instanceof COUIRecyclerView) && view.getScrollY() < 0) {
                i4 = 0;
            }
            super.onNestedPreScroll(coordinatorLayout, cOUIAppBarLayout, view, i3, i4, iArr, i5);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            super.onNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, i3, i4, i5, ((cOUIAppBarLayout instanceof SettingsCollapsableAppBarLayout) && SettingsCollapsableAppBarLayout.b((SettingsCollapsableAppBarLayout) cOUIAppBarLayout) == 1) ? 0 : i6, i7, iArr);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, View view2, int i3, int i4) {
            this.mLastStartedType = i4;
            return super.onStartNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, view2, i3, i4);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3) {
            if (cOUIAppBarLayout.getChildCount() > 0) {
                boolean z2 = false;
                if (cOUIAppBarLayout.getChildAt(0) instanceof SettingsCollapsingToolbarLayout) {
                    boolean z3 = (((COUIAppBarLayout.LayoutParams) cOUIAppBarLayout.getChildAt(0).getLayoutParams()).getScrollFlags() & 17) == 17 && contentInScreen(view) && ((SettingsCollapsableAppBarLayout) cOUIAppBarLayout).mCollapsable;
                    this.mShouldSnapToBottom = z3;
                    if (z3 && SettingsCollapsableAppBarLayout.a((SettingsCollapsableAppBarLayout) cOUIAppBarLayout)) {
                        z2 = true;
                    }
                    this.mShouldSnapToBottom = z2;
                    if (SettingsCollapsableAppBarLayout.b((SettingsCollapsableAppBarLayout) cOUIAppBarLayout) == 1) {
                        LogUtils.d(SettingsCollapsableAppBarLayout.TAG, "current mode fixed collapse return");
                        return;
                    }
                }
            }
            super.onStopNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, i3);
        }

        public int scroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, int i3, int i4, int i5, int i6) {
            float f3;
            float f4;
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling() - i3;
            if (i6 == 1) {
                f3 = this.mFollowHeaderBounce;
                f4 = this.mFollowHeaderResponse;
            } else {
                f3 = this.mFollowBounce;
                f4 = i3 > 0 ? this.mFollowListResponseUp : this.mFollowListResponseDown;
            }
            return setHeaderTopBottomOffset(coordinatorLayout, cOUIAppBarLayout, topBottomOffsetForScrollingSibling, i4, i5, this.mUseSpring, f3, f4, false);
        }
    }

    public SettingsCollapsableAppBarLayout(Context context) {
        this(context, null);
    }

    public static /* bridge */ /* synthetic */ boolean a(SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout) {
        return settingsCollapsableAppBarLayout.mAutoExpand;
    }

    private void adjustPaddingBottom(int i3, boolean z2) {
        float totalScrollRange = getTotalScrollRange();
        if (z2 || !(totalScrollRange == 0.0f || this.mStartPaddingBottom == this.mEndPaddingBottom)) {
            float fAbs = Math.abs(i3) / totalScrollRange;
            int i4 = this.mStartPaddingBottom;
            translateDivider(-(i4 + ((int) ((this.mEndPaddingBottom - i4) * fAbs))));
        }
    }

    private void adjustSubtitleIfNeed(int i3) {
        View viewFindSubtitleContentView = findSubtitleContentView();
        int dimensionPixelOffset = 0;
        boolean z2 = viewFindSubtitleContentView != null && viewFindSubtitleContentView.getVisibility() == 0;
        if (findCollapsingToolbarLayout() == null || !z2) {
            return;
        }
        float fAbs = Math.abs(i3) / getTotalScrollRange();
        float f3 = this.mSubtitleHideEnable ? 1.0f - fAbs : 1.0f;
        ViewGroup.LayoutParams layoutParams = viewFindSubtitleContentView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            if (COUIResponsiveUtils.isSmallScreen(getContext(), getMeasuredWidth())) {
                dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_left_compat);
            } else if (COUIResponsiveUtils.isMediumScreen(getContext(), getMeasuredWidth(), UIUtil.getScreenHeightMetrics(getContext()))) {
                dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_left_medium);
            } else if (COUIResponsiveUtils.isLargeScreen(getContext(), getMeasuredWidth(), UIUtil.getScreenHeightMetrics(getContext()))) {
                dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.toolbar_normal_padding_left_expanded);
            }
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            if (marginLayoutParams.getMarginStart() != dimensionPixelOffset) {
                marginLayoutParams.setMarginStart(dimensionPixelOffset);
                viewFindSubtitleContentView.setLayoutParams(layoutParams);
            }
        }
        if (Float.isNaN(fAbs)) {
            return;
        }
        viewFindSubtitleContentView.setTranslationY((this.mEndPaddingBottom - this.mStartPaddingBottom) * (1.0f - fAbs));
        viewFindSubtitleContentView.setAlpha(f3);
    }

    public static /* bridge */ /* synthetic */ int b(SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout) {
        return settingsCollapsableAppBarLayout.mMode;
    }

    private View findCollapsingToolbarLayout() {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof SettingsCollapsingToolbarLayout) {
                return childAt;
            }
        }
        return null;
    }

    private float getExpansionFraction() {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof SettingsCollapsingToolbarLayout) {
            return ((SettingsCollapsingToolbarLayout) viewFindCollapsingToolbarLayout).mCollapsingTextHelper.getExpansionFraction();
        }
        return 0.0f;
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, com.support.toolbar.R.styleable.COUICollapsableAppBarLayout);
        this.mMode = typedArrayObtainStyledAttributes.getInt(1, 0);
        this.mSubtitleHideEnable = typedArrayObtainStyledAttributes.getBoolean(3, true);
        this.mStartPaddingBottom = typedArrayObtainStyledAttributes.getDimensionPixelOffset(2, getContext().getResources().getDimensionPixelOffset(R.dimen.coui_appbar_start_padding_bottom));
        this.mEndPaddingBottom = typedArrayObtainStyledAttributes.getDimensionPixelOffset(0, getContext().getResources().getDimensionPixelOffset(R.dimen.coui_appbar_end_padding_bottom));
        if (this.mMode == 0) {
            this.mCollapsable = true;
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    private boolean isExpanded() {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof SettingsCollapsingToolbarLayout) {
            return ((SettingsCollapsingToolbarLayout) viewFindCollapsingToolbarLayout).isExpanded();
        }
        return false;
    }

    private void setScrollFlags(int i3) {
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            if (layoutParams instanceof COUIAppBarLayout.LayoutParams) {
                ((COUIAppBarLayout.LayoutParams) layoutParams).setScrollFlags(i3);
                childAt.setLayoutParams(layoutParams);
            } else if (layoutParams instanceof AppBarLayout.LayoutParams) {
                ((AppBarLayout.LayoutParams) layoutParams).setScrollFlags(i3);
                childAt.setLayoutParams(layoutParams);
            }
        }
    }

    private void updateIconViewLocationIfNeed(float f3) {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof SettingsCollapsingToolbarLayout) {
            SettingsCollapsingToolbarLayout settingsCollapsingToolbarLayout = (SettingsCollapsingToolbarLayout) viewFindCollapsingToolbarLayout;
            settingsCollapsingToolbarLayout.mCollapsingTextHelper.setExpansionFraction(f3);
            settingsCollapsingToolbarLayout.updateIconViewLocationIfNeed();
        }
    }

    private boolean updateOffset() {
        return this.mUpdateOffset;
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout
    public void enableAutoExpand(boolean z2) {
        this.mAutoExpand = z2;
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout
    public View findSubtitleContentView() {
        return findViewById(R.id.coui_appbar_subtitle_content);
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIDividerAppBarLayout, com.google.android.material.appbar.COUIAppBarLayout, androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    public CoordinatorLayout.Behavior<COUIAppBarLayout> getBehavior() {
        return new ScrollBehavior();
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIDividerAppBarLayout
    public int getDividerScrollRange() {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout != null) {
            int i3 = this.mMode;
            if (i3 == 0) {
                return getTotalScrollRange();
            }
            if (i3 == 1) {
                return viewFindCollapsingToolbarLayout.getMinimumHeight();
            }
        }
        return getMeasuredHeight();
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout
    public int getMode() {
        return this.mMode;
    }

    public int getOffset() {
        return this.mOffset;
    }

    public float getOffsetFraction() {
        return Math.abs(this.mOffset) / getTotalScrollRange();
    }

    public boolean isCollapsed() {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof SettingsCollapsingToolbarLayout) {
            return ((SettingsCollapsingToolbarLayout) viewFindCollapsingToolbarLayout).isCollapsed();
        }
        return false;
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout
    public boolean isSubtitleHideEnable() {
        return this.mSubtitleHideEnable;
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIDividerAppBarLayout, com.google.android.material.appbar.COUIAppBarLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setMode(this.mMode);
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        post(new p(this));
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        if (findSubtitleContentView() != null) {
            findSubtitleContentView().setTranslationY(-this.mStartPaddingBottom);
        }
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIAppBarLayout, android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        View viewFindSubtitleContentView = findSubtitleContentView();
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        this.mUpdateOffset = this.mSubtitleHideEnable && isCollapsed();
        boolean z2 = viewFindSubtitleContentView != null && viewFindSubtitleContentView.getVisibility() == 0;
        if (viewFindCollapsingToolbarLayout != null) {
            ViewGroup.LayoutParams layoutParams = viewFindCollapsingToolbarLayout.getLayoutParams();
            if (this.mCollapsingToolbarHeight == 0) {
                this.mCollapsingToolbarHeight = layoutParams.height;
            }
            int i5 = this.mCollapsingToolbarHeight;
            if (i5 != 0) {
                if (z2) {
                    viewFindSubtitleContentView.measure(i3, i4);
                    ViewGroup.LayoutParams layoutParams2 = viewFindSubtitleContentView.getLayoutParams();
                    int i6 = this.mSubtitleViewHeight;
                    boolean z3 = (i6 == 0 || i6 == viewFindSubtitleContentView.getMeasuredHeight()) ? false : true;
                    this.mUpdateOffset = this.mUpdateOffset || (z3 && isCollapsed());
                    if (layoutParams.height == this.mCollapsingToolbarHeight || z3) {
                        if (layoutParams2 instanceof ViewGroup.MarginLayoutParams) {
                            int measuredHeight = viewFindSubtitleContentView.getMeasuredHeight();
                            this.mSubtitleViewHeight = measuredHeight;
                            ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin = -measuredHeight;
                            viewFindSubtitleContentView.setLayoutParams(layoutParams2);
                        }
                        layoutParams.height = this.mCollapsingToolbarHeight + this.mSubtitleViewHeight;
                        viewFindCollapsingToolbarLayout.setLayoutParams(layoutParams);
                    }
                } else {
                    layoutParams.height = i5;
                    viewFindCollapsingToolbarLayout.setLayoutParams(layoutParams);
                }
            }
        }
        this.mUpdateOffset &= !(getPendingAction() != 0);
        super.onMeasure(i3, i4);
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIAppBarLayout
    public void onOffsetChanged(int i3) {
        super.onOffsetChanged(i3);
        if (i3 != this.mOffset) {
            if (this.mMode == 0) {
                this.mScrollDyByOffset = Math.max(0, -i3);
            }
            this.mOffset = i3;
            onDividerChanged();
            adjustPaddingBottom(i3, false);
            adjustSubtitleIfNeed(i3);
            View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
            if (viewFindCollapsingToolbarLayout instanceof SettingsCollapsingToolbarLayout) {
                ((SettingsCollapsingToolbarLayout) viewFindCollapsingToolbarLayout).updateIconViewLocationIfNeed();
                viewFindCollapsingToolbarLayout.invalidate();
            }
            if (this.mNeedUpdateModeAfterOffsetChanged) {
                int i4 = this.mMode;
                if (i4 == 0) {
                    this.mNeedUpdateModeAfterOffsetChanged = false;
                } else if (i4 == 1 && this.mOffset == (-getDividerScrollRange())) {
                    updateIconViewLocationIfNeed(1.0f);
                    this.mNeedUpdateModeAfterOffsetChanged = false;
                } else if (this.mMode == 2 && this.mOffset == 0) {
                    setScrollFlags(0);
                    updateIconViewLocationIfNeed(0.0f);
                    this.mNeedUpdateModeAfterOffsetChanged = false;
                }
            }
            if (this.mTargetViewState == 2 && this.mOffset == (-getDividerScrollRange()) && !this.mTargetView.canScrollVertically(1) && !this.mTargetView.canScrollVertically(-1) && this.mMode == 0) {
                setExpanded(true);
            }
        }
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIDividerAppBarLayout, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mOffset = bundle.getInt(OFFSET_STATE_KEY);
            this.mMode = bundle.getInt(MODE_STATE_KEY);
            updateIconViewLocationIfNeed(bundle.getFloat(TITLE_FRACTION_STATE_KEY, 0.0f));
            parcelable = bundle.getParcelable(SUPER_STATE_KEY);
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIDividerAppBarLayout, android.view.View
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState());
        bundle.putInt(OFFSET_STATE_KEY, this.mOffset);
        bundle.putInt(MODE_STATE_KEY, this.mMode);
        bundle.putFloat(TITLE_FRACTION_STATE_KEY, getExpansionFraction());
        return bundle;
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIDividerAppBarLayout
    public boolean refreshAppBar(View view) {
        boolean z2;
        boolean zRefreshAppBar = super.refreshAppBar(view);
        if (!view.canScrollVertically(1) && !view.canScrollVertically(-1) && this.mMode == 0 && isCollapsed() && this.mTargetViewState == 2) {
            setExpanded(true);
            z2 = true;
        } else {
            z2 = false;
        }
        return zRefreshAppBar || z2;
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout
    @Deprecated
    public void refreshExpand(View view) {
        if (view.canScrollVertically(1) || view.canScrollVertically(-1) || this.mMode != 0 || !isCollapsed()) {
            return;
        }
        setExpanded(true);
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout, com.google.android.material.appbar.COUIDividerAppBarLayout
    public void reset() {
        super.reset();
        this.mCollapsable = true;
        setMode(0);
        setExpanded(true);
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout
    public void setMode(int i3) {
        int i4 = this.mMode;
        if (i4 != i3) {
            if (i3 == 0) {
                this.mCollapsable = true;
                if (i4 == 1 && this.mScrollDyByScroll > 0) {
                    this.mScrollDyByOffset -= this.mOffset;
                    onDividerChanged();
                }
                if (this.mMode == 2 && this.mScrollDyByScroll > 0) {
                    this.mScrollDyByOffset -= this.mOffset;
                    setExpanded(false);
                }
            } else if (i3 == 1) {
                this.mCollapsable = false;
                setExpanded(false);
                this.mScrollDyByOffset = 0;
                if (isCollapsed()) {
                    updateIconViewLocationIfNeed(1.0f);
                } else {
                    this.mNeedUpdateModeAfterOffsetChanged = true;
                }
                onDividerChanged();
            } else if (i3 != 2) {
                LogUtils.d(TAG, "setMode " + i3);
            } else {
                this.mCollapsable = false;
                setExpanded(true);
                this.mScrollDyByOffset = 0;
                if (isExpanded()) {
                    setScrollFlags(0);
                    updateIconViewLocationIfNeed(0.0f);
                } else {
                    this.mNeedUpdateModeAfterOffsetChanged = true;
                }
                onDividerChanged();
            }
            this.mMode = i3;
        }
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout
    public void setSubtitleHideEnable(boolean z2) {
        if (this.mSubtitleHideEnable != z2) {
            this.mSubtitleHideEnable = z2;
            updateSubtitle();
        }
    }

    @Override // com.google.android.material.appbar.COUICollapsableAppBarLayout
    public void updateSubtitle() {
        adjustSubtitleIfNeed(this.mOffset);
    }

    public SettingsCollapsableAppBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMode = 0;
        this.mCollapsingToolbarHeight = 0;
        this.mSubtitleViewHeight = 0;
        this.mAutoExpand = true;
        this.mUpdateOffset = false;
        this.mNeedUpdateModeAfterOffsetChanged = false;
        init(attributeSet);
    }

    public SettingsCollapsableAppBarLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mMode = 0;
        this.mCollapsingToolbarHeight = 0;
        this.mSubtitleViewHeight = 0;
        this.mAutoExpand = true;
        this.mUpdateOffset = false;
        this.mNeedUpdateModeAfterOffsetChanged = false;
        init(attributeSet);
    }
}