package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.UIUtil;
import com.google.android.material.appbar.olivelink.COUICollapsableAppBarLayoutParams;
import com.google.android.material.appbar.olivelink.COUICollapsableAppBarLayoutParamsExtKt;
import com.support.toolbar.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public class COUICollapsableAppBarLayout extends COUIDividerAppBarLayout {
    private static boolean DEBUG = false;
    public static final int DEFAULT_SCROLL_FLAG = 19;
    public static final int MODE_COLLAPSABLE = 0;
    public static final int MODE_FIXED_COLLAPSED = 1;
    public static final int MODE_FIXED_EXPANDED = 2;
    private static final String MODE_STATE_KEY = "MODE_STATE_KEY";
    private static final String OFFSET_STATE_KEY = "OFFSET_STATE_KEY";
    private static final String SUPER_STATE_KEY = "SUPER_STATE_KEY";
    private static final String TAG = "COUICollAppBarLayout";
    private static final String TITLE_FRACTION_STATE_KEY = "TITLE_FRACTION_STATE_KEY";
    private static final String TOTAL_SCROLL_RANGE = "TOTAL_SCROLL_RANGE";
    private boolean mAutoExpand;
    private int mCollapsingToolbarHeight;
    private int mEndPaddingBottom;
    private final RecyclerView.OnScrollListener mFixedModeDividerScrollListener;
    private RecyclerView mFixedModeScrollTarget;
    private int mMode;
    private boolean mNeedUpdateModeAfterOffsetChanged;
    private int mOffset;
    private int mStartPaddingBottom;
    private boolean mSubtitleHideEnable;
    private Integer mSubtitleOriginalLpWidth;
    private int mSubtitleViewHeight;
    private boolean mUpdateOffset;

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUICollapsableAppBarLayout$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes8.dex */
    public class AnonymousClass1 extends RecyclerView.OnScrollListener {
        public AnonymousClass1() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
            if (COUICollapsableAppBarLayout.access$000(COUICollapsableAppBarLayout.this)) {
                COUICollapsableAppBarLayout.this.onDividerChanged();
            }
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
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
                return;
            }
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling();
            if (COUICollapsableAppBarLayout.access$400()) {
                COUILog.d(COUICollapsableAppBarLayout.TAG, "animateOffsetWithDuration offset: " + i3 + " (spring; duration=" + i4 + " ignored) currentOffset=" + topBottomOffsetForScrollingSibling);
            }
            if (topBottomOffsetForScrollingSibling == i3) {
                return;
            }
            setHeaderTopBottomOffsetInternal(coordinatorLayout, cOUIAppBarLayout, i3, Integer.MIN_VALUE, Integer.MAX_VALUE, true, this.mAttachBounce, this.mAttachResponse, true, false);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ boolean canDragView(View view) {
            return canDragView((COUIAppBarLayout) view);
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

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public boolean canDragView(COUIAppBarLayout cOUIAppBarLayout) {
            if (!(cOUIAppBarLayout instanceof COUICollapsableAppBarLayout)) {
                return false;
            }
            COUICollapsableAppBarLayout cOUICollapsableAppBarLayout = (COUICollapsableAppBarLayout) cOUIAppBarLayout;
            if (cOUICollapsableAppBarLayout.getMode() == 0 && !COUICollapsableAppBarLayout.access$100(cOUICollapsableAppBarLayout)) {
                return super.canDragView(cOUIAppBarLayout);
            }
            return false;
        }

        public boolean fling(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, int i3, int i4, float f3) {
            if (COUICollapsableAppBarLayout.access$400()) {
                COUILog.d(COUICollapsableAppBarLayout.TAG, "COUIAppBarLayout.fling : ");
            }
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
            super.onNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, i3, i4, i5, ((cOUIAppBarLayout instanceof COUICollapsableAppBarLayout) && COUICollapsableAppBarLayout.access$200((COUICollapsableAppBarLayout) cOUIAppBarLayout) == 1) ? 0 : i6, i7, iArr);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, View view2, int i3, int i4) {
            this.mLastStartedType = i4;
            return super.onStartNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, view2, i3, i4);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3) {
            if (cOUIAppBarLayout.getChildCount() > 0) {
                if (cOUIAppBarLayout.getChildAt(0) instanceof COUICollapsingToolbarLayout) {
                    int scrollFlags = ((COUIAppBarLayout.LayoutParams) cOUIAppBarLayout.getChildAt(0).getLayoutParams()).getScrollFlags() & 17;
                    boolean z2 = scrollFlags == 17 && contentInScreen(view) && ((COUICollapsableAppBarLayout) cOUIAppBarLayout).mCollapsable;
                    this.mShouldSnapToBottom = z2;
                    this.mShouldSnapToBottom = z2 && COUICollapsableAppBarLayout.access$300((COUICollapsableAppBarLayout) cOUIAppBarLayout);
                    if (COUICollapsableAppBarLayout.access$400()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("((mLastStartedType == ViewCompat.TYPE_TOUCH) || (type == ViewCompat.TYPE_NON_TOUCH)) = ");
                        sb.append(this.mLastStartedType == 0 || i3 == 1);
                        sb.append("\n((snapFlag & LayoutParams.FLAG_SNAP) == LayoutParams.FLAG_SNAP) = ");
                        sb.append(scrollFlags == 17);
                        sb.append("\n(contentInScreen(target)) = ");
                        sb.append(contentInScreen(view));
                        sb.append("\n(((COUICollapsableAppBarLayout) abl).mCollapsable = ");
                        sb.append(((COUICollapsableAppBarLayout) cOUIAppBarLayout).mCollapsable);
                        sb.append("\nmShouldSnapToBottom = ");
                        sb.append(this.mShouldSnapToBottom);
                        COUILog.d(COUICollapsableAppBarLayout.TAG, sb.toString());
                    }
                }
            }
            super.onStopNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, i3);
        }

        public int scroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, int i3, int i4, int i5, int i6) {
            float f3;
            float f4;
            if (COUICollapsableAppBarLayout.access$400()) {
                COUILog.d(COUICollapsableAppBarLayout.TAG, "COUICollapsableAppBarLayout.scroll dy : " + i3);
            }
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling() - i3;
            if (i6 == 1) {
                f4 = this.mFollowHeaderBounce;
                f3 = this.mFollowHeaderResponse;
            } else {
                float f5 = this.mFollowBounce;
                f3 = i3 > 0 ? this.mFollowListResponseUp : this.mFollowListResponseDown;
                f4 = f5;
            }
            return setHeaderTopBottomOffset(coordinatorLayout, cOUIAppBarLayout, topBottomOffsetForScrollingSibling, i4, i5, this.mUseSpring, f4, f3, false);
        }
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUICollapsableAppBarLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ boolean access$000(COUICollapsableAppBarLayout cOUICollapsableAppBarLayout) {
        return cOUICollapsableAppBarLayout.usesFixedModeListScrollDivider();
    }

    public static /* synthetic */ boolean access$100(COUICollapsableAppBarLayout cOUICollapsableAppBarLayout) {
        return cOUICollapsableAppBarLayout.isCollapsed();
    }

    public static /* synthetic */ int access$200(COUICollapsableAppBarLayout cOUICollapsableAppBarLayout) {
        return cOUICollapsableAppBarLayout.mMode;
    }

    public static /* synthetic */ boolean access$300(COUICollapsableAppBarLayout cOUICollapsableAppBarLayout) {
        return cOUICollapsableAppBarLayout.mAutoExpand;
    }

    public static /* synthetic */ boolean access$400() {
        return DEBUG;
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
        boolean z3 = findCollapsingToolbarLayout() != null;
        int totalScrollRange = getTotalScrollRange();
        if (z3 && z2 && totalScrollRange != 0) {
            float fAbs = Math.abs(i3) / totalScrollRange;
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
            if (DEBUG) {
                COUILog.d(TAG, "adjustSubtitleIfNeed fraction: " + fAbs + " alpha: " + f3);
            }
            if (!Float.isNaN(fAbs)) {
                viewFindSubtitleContentView.setTranslationY((this.mEndPaddingBottom - this.mStartPaddingBottom) * (1.0f - fAbs));
                viewFindSubtitleContentView.setAlpha(f3);
            }
            applySubtitleMaxWidthToMatchToolbarTitle();
        }
    }

    private void adjustSubtitleWithAlpha(int i3, int i4) {
        View viewFindSubtitleContentView = findSubtitleContentView();
        boolean z2 = viewFindSubtitleContentView != null && viewFindSubtitleContentView.getVisibility() == 0;
        if (findCollapsingToolbarLayout() == null || !z2 || i4 == 0) {
            return;
        }
        float fAbs = Math.abs(i3) / i4;
        float f3 = this.mSubtitleHideEnable ? 1.0f - fAbs : 1.0f;
        if (DEBUG) {
            COUILog.d(TAG, "adjustSubtitleWithAlpha fraction: " + fAbs + " alpha: " + f3);
        }
        if (Float.isNaN(fAbs)) {
            return;
        }
        viewFindSubtitleContentView.setTranslationY((this.mEndPaddingBottom - this.mStartPaddingBottom) * (1.0f - fAbs));
        viewFindSubtitleContentView.setAlpha(f3);
    }

    private void applySubtitleMaxWidthToMatchToolbarTitle() {
        View viewFindSubtitleContentView = findSubtitleContentView();
        if (viewFindSubtitleContentView == null) {
            restoreSubtitleLpWidth(null);
            return;
        }
        if (viewFindSubtitleContentView.getVisibility() == 8) {
            restoreSubtitleLpWidth(viewFindSubtitleContentView);
            return;
        }
        int[] iArr = new int[2];
        if (!resolveTitleBoundsInAppBar(iArr)) {
            restoreSubtitleLpWidth(viewFindSubtitleContentView);
            return;
        }
        int left = iArr[0];
        int right = iArr[1];
        if (ViewCompat.getLayoutDirection(this) == 1) {
            right = viewFindSubtitleContentView.getRight();
        } else {
            left = viewFindSubtitleContentView.getLeft();
        }
        int iMax = Math.max(0, right - left);
        ViewGroup.LayoutParams layoutParams = viewFindSubtitleContentView.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        if (iMax == 0) {
            restoreSubtitleLpWidth(viewFindSubtitleContentView);
            return;
        }
        if (this.mSubtitleOriginalLpWidth == null) {
            this.mSubtitleOriginalLpWidth = Integer.valueOf(layoutParams.width);
        }
        if (layoutParams.width != iMax) {
            layoutParams.width = iMax;
            viewFindSubtitleContentView.setLayoutParams(layoutParams);
        }
    }

    private int computeFixedModeListScrollOffset(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null || recyclerView.getChildCount() == 0) {
            return 0;
        }
        View childAt = recyclerView.getChildAt(0);
        int position = layoutManager.getPosition(childAt);
        int dividerScrollRange = getDividerScrollRange();
        return position > 0 ? dividerScrollRange : Math.min(Math.max(0, recyclerView.getPaddingTop() - childAt.getTop()), dividerScrollRange);
    }

    private View findCollapsingToolbarLayout() {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUICollapsingToolbarLayout) {
                return childAt;
            }
        }
        return null;
    }

    private float getExpansionFraction() {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            return ((COUICollapsingToolbarLayout) viewFindCollapsingToolbarLayout).mCollapsingTextHelper.getExpansionFraction();
        }
        return 0.0f;
    }

    private boolean getUpdateOffset() {
        return this.mUpdateOffset;
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.COUICollapsableAppBarLayout);
        this.mMode = typedArrayObtainStyledAttributes.getInt(R.styleable.COUICollapsableAppBarLayout_mode, 0);
        this.mSubtitleHideEnable = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUICollapsableAppBarLayout_subtitleHideEnable, true);
        this.mStartPaddingBottom = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUICollapsableAppBarLayout_startPaddingBottom, getContext().getResources().getDimensionPixelOffset(R.dimen.coui_appbar_start_padding_bottom));
        this.mEndPaddingBottom = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUICollapsableAppBarLayout_endPaddingBottom, getContext().getResources().getDimensionPixelOffset(R.dimen.coui_appbar_end_padding_bottom));
        if (this.mMode == 0) {
            this.mCollapsable = true;
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    private boolean isCollapsed() {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            return ((COUICollapsingToolbarLayout) viewFindCollapsingToolbarLayout).isCollapsed();
        }
        return false;
    }

    private boolean isExpanded() {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            return ((COUICollapsingToolbarLayout) viewFindCollapsingToolbarLayout).isExpanded();
        }
        return false;
    }

    private boolean resolveTitleBoundsInAppBar(int[] iArr) {
        int left;
        int left2;
        if (iArr.length < 2) {
            return false;
        }
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (!(viewFindCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout)) {
            return false;
        }
        COUICollapsingToolbarLayout cOUICollapsingToolbarLayout = (COUICollapsingToolbarLayout) viewFindCollapsingToolbarLayout;
        if (!cOUICollapsingToolbarLayout.isTitleEnabled()) {
            return false;
        }
        RectF rectF = cOUICollapsingToolbarLayout.getCollapsingTextHelper().currentBounds;
        if (rectF.right <= rectF.left || (left2 = cOUICollapsingToolbarLayout.getLeft() + Math.round(rectF.right)) <= (left = cOUICollapsingToolbarLayout.getLeft() + Math.round(rectF.left))) {
            return false;
        }
        iArr[0] = left;
        iArr[1] = left2;
        return true;
    }

    private void restoreSubtitleLpWidth(View view) {
        ViewGroup.LayoutParams layoutParams;
        if (this.mSubtitleOriginalLpWidth != null && view != null && (layoutParams = view.getLayoutParams()) != null && layoutParams.width != this.mSubtitleOriginalLpWidth.intValue()) {
            layoutParams.width = this.mSubtitleOriginalLpWidth.intValue();
            view.setLayoutParams(layoutParams);
        }
        this.mSubtitleOriginalLpWidth = null;
    }

    private void setScrollFlags(int i3) {
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            COUIAppBarLayout.LayoutParams layoutParams = (COUIAppBarLayout.LayoutParams) childAt.getLayoutParams();
            layoutParams.setScrollFlags(i3);
            childAt.setLayoutParams(layoutParams);
        }
    }

    private void setUpdateOffset(boolean z2) {
        this.mUpdateOffset = z2;
    }

    private void unbindFixedModeDividerScrollListener() {
        RecyclerView recyclerView = this.mFixedModeScrollTarget;
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(this.mFixedModeDividerScrollListener);
            this.mFixedModeScrollTarget = null;
        }
    }

    private void updateIconViewLocationIfNeed(float f3) {
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            COUICollapsingToolbarLayout cOUICollapsingToolbarLayout = (COUICollapsingToolbarLayout) viewFindCollapsingToolbarLayout;
            cOUICollapsingToolbarLayout.mCollapsingTextHelper.setExpansionFraction(f3);
            cOUICollapsingToolbarLayout.updateIconViewLocationIfNeed();
        }
    }

    private boolean usesFixedModeListScrollDivider() {
        int i3 = this.mMode;
        return i3 == 1 || i3 == 2;
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
    public void bindRecyclerView(RecyclerView recyclerView) {
        unbindFixedModeDividerScrollListener();
        super.bindRecyclerView(recyclerView);
        if (recyclerView != null) {
            this.mFixedModeScrollTarget = recyclerView;
            recyclerView.addOnScrollListener(this.mFixedModeDividerScrollListener);
            onDividerChanged();
        }
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
    public int computeDividerTotalScrollDy() {
        if (!usesFixedModeListScrollDivider()) {
            return super.computeDividerTotalScrollDy();
        }
        RecyclerView recyclerView = this.mTargetView;
        if (recyclerView != null) {
            return Math.min(computeFixedModeListScrollOffset(recyclerView), getDividerScrollRange());
        }
        return 0;
    }

    public void enableAutoExpand(boolean z2) {
        this.mAutoExpand = z2;
    }

    public View findSubtitleContentView() {
        return findViewById(R.id.coui_appbar_subtitle_content);
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, com.google.android.material.appbar.COUIAppBarLayout, androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    public CoordinatorLayout.Behavior<COUIAppBarLayout> getBehavior() {
        return new ScrollBehavior();
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
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
            if (i3 == 2) {
                return viewFindCollapsingToolbarLayout.getMinimumHeight();
            }
        }
        return getMeasuredHeight();
    }

    public int getEndPaddingBottom() {
        return this.mEndPaddingBottom;
    }

    public int getMode() {
        return this.mMode;
    }

    public int getStartPaddingBottom() {
        return this.mStartPaddingBottom;
    }

    public boolean isSubtitleHideEnable() {
        return this.mSubtitleHideEnable;
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, com.google.android.material.appbar.COUIAppBarLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setMode(this.mMode);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        post(new d(this));
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, com.google.android.material.appbar.COUIAppBarLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        unbindFixedModeDividerScrollListener();
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        if (findSubtitleContentView() != null) {
            findSubtitleContentView().setTranslationY(-this.mStartPaddingBottom);
        }
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout, android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        applySubtitleMaxWidthToMatchToolbarTitle();
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout, android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        View viewFindSubtitleContentView = findSubtitleContentView();
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        boolean z2 = false;
        setUpdateOffset(this.mSubtitleHideEnable && isCollapsed());
        boolean z3 = viewFindSubtitleContentView != null && viewFindSubtitleContentView.getVisibility() == 0;
        if (viewFindCollapsingToolbarLayout != null) {
            ViewGroup.LayoutParams layoutParams = viewFindCollapsingToolbarLayout.getLayoutParams();
            if (this.mCollapsingToolbarHeight == 0) {
                this.mCollapsingToolbarHeight = layoutParams.height;
            }
            int i5 = this.mCollapsingToolbarHeight;
            if (i5 != 0) {
                if (z3) {
                    viewFindSubtitleContentView.measure(i3, i4);
                    ViewGroup.LayoutParams layoutParams2 = viewFindSubtitleContentView.getLayoutParams();
                    int i6 = this.mSubtitleViewHeight;
                    boolean z4 = (i6 == 0 || i6 == viewFindSubtitleContentView.getMeasuredHeight()) ? false : true;
                    if (this.mUpdateOffset || (z4 && isCollapsed())) {
                        z2 = true;
                    }
                    setUpdateOffset(z2);
                    if (layoutParams.height == this.mCollapsingToolbarHeight || z4) {
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
        super.onMeasure(i3, i4);
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout
    public void onOffsetChanged(int i3) {
        super.onOffsetChanged(i3);
        if (i3 == this.mOffset) {
            return;
        }
        if (this.mMode == 0) {
            this.mScrollDyByOffset = Math.max(0, -i3);
        }
        this.mOffset = i3;
        onDividerChanged();
        adjustPaddingBottom(i3, false);
        adjustSubtitleWithAlpha(i3, getTotalScrollRange());
        View viewFindCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (viewFindCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            ((COUICollapsingToolbarLayout) viewFindCollapsingToolbarLayout).updateIconViewLocationIfNeed();
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

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mOffset = bundle.getInt(OFFSET_STATE_KEY);
            this.mMode = bundle.getInt(MODE_STATE_KEY);
            updateIconViewLocationIfNeed(bundle.getFloat(TITLE_FRACTION_STATE_KEY, 0.0f));
            adjustSubtitleWithAlpha(this.mOffset, bundle.getInt(TOTAL_SCROLL_RANGE));
            parcelable = bundle.getParcelable(SUPER_STATE_KEY);
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, android.view.View
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState());
        bundle.putInt(OFFSET_STATE_KEY, this.mOffset);
        bundle.putInt(MODE_STATE_KEY, this.mMode);
        bundle.putFloat(TITLE_FRACTION_STATE_KEY, getExpansionFraction());
        bundle.putInt(TOTAL_SCROLL_RANGE, getTotalScrollRange());
        return bundle;
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
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

    @Deprecated
    public void refreshExpand(View view) {
        if (view.canScrollVertically(1) || view.canScrollVertically(-1) || this.mMode != 0 || !isCollapsed()) {
            return;
        }
        setExpanded(true);
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
    public void reset() {
        super.reset();
        this.mCollapsable = true;
        setMode(0);
        setExpanded(true);
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
    public void setDebug(boolean z2) {
        super.setDebug(z2);
        DEBUG = z2;
    }

    public void setEndPaddingBottom(int i3) {
        this.mEndPaddingBottom = i3;
        adjustPaddingBottom(this.mOffset, true);
    }

    public void setMode(int i3) {
        if (this.mMode == i3) {
            return;
        }
        if (i3 == 0) {
            this.mCollapsable = true;
            setScrollFlags(19);
            if (this.mMode == 1) {
                if (this.mScrollDyByScroll > 0) {
                    this.mScrollDyByOffset -= this.mOffset;
                    onDividerChanged();
                } else {
                    setExpanded(true);
                }
            }
            if (this.mMode == 2 && this.mScrollDyByScroll > 0) {
                this.mScrollDyByOffset -= this.mOffset;
                setExpanded(false);
            }
        } else if (i3 == 1) {
            this.mCollapsable = false;
            setExpanded(false);
            setScrollFlags(19);
            this.mScrollDyByOffset = 0;
            this.mScrollDyByOverScroll = 0;
            if (isCollapsed()) {
                updateIconViewLocationIfNeed(1.0f);
            } else {
                this.mNeedUpdateModeAfterOffsetChanged = true;
            }
            onDividerChanged();
        } else if (i3 == 2) {
            this.mCollapsable = false;
            setExpanded(true);
            this.mScrollDyByOffset = 0;
            this.mScrollDyByOverScroll = 0;
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

    public void setStartPaddingBottom(int i3) {
        this.mStartPaddingBottom = i3;
        adjustPaddingBottom(this.mOffset, true);
    }

    public void setSubtitleHideEnable(boolean z2) {
        if (this.mSubtitleHideEnable != z2) {
            this.mSubtitleHideEnable = z2;
            updateSubtitle();
        }
    }

    public void updateSubtitle() {
        adjustSubtitleIfNeed(this.mOffset);
    }

    public COUICollapsableAppBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMode = 0;
        this.mCollapsingToolbarHeight = 0;
        this.mSubtitleViewHeight = 0;
        this.mAutoExpand = true;
        this.mUpdateOffset = false;
        this.mNeedUpdateModeAfterOffsetChanged = false;
        this.mFixedModeDividerScrollListener = new AnonymousClass1();
        init(attributeSet);
    }

    public COUICollapsableAppBarLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mMode = 0;
        this.mCollapsingToolbarHeight = 0;
        this.mSubtitleViewHeight = 0;
        this.mAutoExpand = true;
        this.mUpdateOffset = false;
        this.mNeedUpdateModeAfterOffsetChanged = false;
        this.mFixedModeDividerScrollListener = new AnonymousClass1();
        init(attributeSet);
    }
}