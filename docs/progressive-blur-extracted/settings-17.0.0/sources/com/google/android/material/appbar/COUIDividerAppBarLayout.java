package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ScrollView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.support.toolbar.R;

/* JADX INFO: loaded from: classes.dex */
public class COUIDividerAppBarLayout extends COUIAppBarLayout {
    private static boolean DEBUG = false;
    private static final String DIVIDER_FRACTION_STATE_KEY = "DIVIDER_FRACTION_STATE_KEY";
    private static final float MAX_FRACTION = 1.0f;
    private static final String OFFSET_DY_SCROLL_STATE_KEY = "OFFSET_DY_SCROLL_STATE_KEY";
    private static final String OVERSCROLL_DY_SCROLL_STATE_KEY = "OVERSCROLL_DY_SCROLL_STATE_KEY";
    private static final String SCROLL_DY_SCROLL_STATE_KEY = "SCROLL_DY_SCROLL_STATE_KEY";
    private static final String SUPER_STATE_KEY = "SUPER_STATE_KEY";
    private static final String TAG = "COUIDividerAppBarLayout";
    private boolean mBlurDividerSuppressed;
    protected boolean mCollapsable;
    private float mDividerEndAlpha;
    private int mDividerEndMarginHorizontal;
    private float mDividerFraction;
    private float mDividerStartAlpha;
    private int mDividerStartMarginHorizontal;
    private boolean mDividerTargetExplicitlyBound;
    private View mDividerTargetView;
    private View mDividerView;
    private boolean mHasDivider;
    private OnDividerProgressChangedListener mOnDividerProgressChangedListener;
    private View.OnLayoutChangeListener mOnLayoutChangeListener;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;
    private RecyclerView.OnScrollListener mOnScrollListener;
    protected int mScrollDyByOffset;
    protected int mScrollDyByOverScroll;
    protected int mScrollDyByScroll;
    protected RecyclerView mTargetView;
    protected int mTargetViewState;

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUIDividerAppBarLayout$1, reason: invalid class name */
    public class AnonymousClass1 extends RecyclerView.OnScrollListener {
        public AnonymousClass1() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i3) {
            super.onScrollStateChanged(recyclerView, i3);
            COUIDividerAppBarLayout.this.mTargetViewState = i3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
            super.onScrolled(recyclerView, i3, i4);
            COUIDividerAppBarLayout cOUIDividerAppBarLayout = COUIDividerAppBarLayout.this;
            cOUIDividerAppBarLayout.mScrollDyByScroll = COUIDividerAppBarLayout.access$000(cOUIDividerAppBarLayout, recyclerView);
            COUIDividerAppBarLayout.this.onDividerChanged();
        }
    }

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUIDividerAppBarLayout$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnLayoutChangeListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            if (COUIDividerAppBarLayout.this.refreshAppBar(view)) {
                COUIDividerAppBarLayout.this.onDividerChanged();
            }
        }
    }

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUIDividerAppBarLayout$3, reason: invalid class name */
    /* JADX INFO: loaded from: classes8.dex */
    public class AnonymousClass3 implements ViewTreeObserver.OnScrollChangedListener {
        public AnonymousClass3() {
        }

        @Override // android.view.ViewTreeObserver.OnScrollChangedListener
        public void onScrollChanged() {
            if (COUIDividerAppBarLayout.access$100(COUIDividerAppBarLayout.this) == null) {
                return;
            }
            COUIDividerAppBarLayout cOUIDividerAppBarLayout = COUIDividerAppBarLayout.this;
            if (cOUIDividerAppBarLayout.refreshAppBar(COUIDividerAppBarLayout.access$100(cOUIDividerAppBarLayout))) {
                COUIDividerAppBarLayout.this.onDividerChanged();
            }
        }
    }

    public static class DividerAppBarOnScrollChangedListener implements ViewTreeObserver.OnScrollChangedListener {
        private COUIAppBarLayout mAbl;
        private View mTarget;

        private DividerAppBarOnScrollChangedListener() {
        }

        @Override // android.view.ViewTreeObserver.OnScrollChangedListener
        public void onScrollChanged() {
            View view = this.mTarget;
            if (view instanceof COUIRecyclerView) {
                int scrollY = view.getScrollY();
                ((COUIDividerAppBarLayout) this.mAbl).mScrollDyByOverScroll = Math.max(0, scrollY);
                ((COUIDividerAppBarLayout) this.mAbl).onDividerChanged();
                if (((COUIDividerAppBarLayout) this.mAbl).mScrollDyByOverScroll == 0) {
                    this.mTarget.getViewTreeObserver().removeOnScrollChangedListener(this);
                }
            }
        }

        public void setAbl(COUIAppBarLayout cOUIAppBarLayout) {
            this.mAbl = cOUIAppBarLayout;
        }

        public void setTarget(View view) {
            this.mTarget = view;
        }

        public /* synthetic */ DividerAppBarOnScrollChangedListener(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public static class DividerBehavior extends COUIAppBarLayout.Behavior {
        private DividerAppBarOnScrollChangedListener mScrollChangedListener;
        private boolean mStopAfterFling = false;

        public boolean canScrollUp(View view) {
            return view.canScrollVertically(1);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View view, View view2, float f3, float f4) {
            return onNestedPreFling(coordinatorLayout, (COUIAppBarLayout) view, view2, f3, f4);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            onNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, i3, i4, i5, i6, i7, iArr);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i3, int i4) {
            return onStartNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, view3, i3, i4);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3) {
            onStopNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, i3);
        }

        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, float f3, float f4) {
            this.mStopAfterFling = true;
            return super.onNestedPreFling(coordinatorLayout, cOUIAppBarLayout, view, f3, f4);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            if ((cOUIAppBarLayout instanceof COUIDividerAppBarLayout) && (view instanceof COUIRecyclerView)) {
                COUIDividerAppBarLayout cOUIDividerAppBarLayout = (COUIDividerAppBarLayout) cOUIAppBarLayout;
                int i8 = cOUIDividerAppBarLayout.mScrollDyByOverScroll;
                int iMax = Math.max(0, view.getScrollY());
                cOUIDividerAppBarLayout.mScrollDyByOverScroll = iMax;
                if (i8 != iMax) {
                    cOUIDividerAppBarLayout.onDividerChanged();
                }
            }
            super.onNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, i3, i4, i5, i6, i7, iArr);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, View view2, int i3, int i4) {
            return super.onStartNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, view2, i3, i4) || ((cOUIAppBarLayout instanceof COUIDividerAppBarLayout) && COUIDividerAppBarLayout.access$200((COUIDividerAppBarLayout) cOUIAppBarLayout));
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.Behavior, com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3) {
            if (view instanceof COUIRecyclerView) {
                if (this.mScrollChangedListener == null) {
                    this.mScrollChangedListener = new DividerAppBarOnScrollChangedListener(null);
                }
                this.mScrollChangedListener.setTarget(view);
                this.mScrollChangedListener.setAbl(cOUIAppBarLayout);
                view.getViewTreeObserver().addOnScrollChangedListener(this.mScrollChangedListener);
            }
            if (!this.mStopAfterFling) {
                super.onStopNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, i3);
            }
            this.mStopAfterFling = false;
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
    public interface OnDividerProgressChangedListener {
        void onDividerProgressChanged(float f3);
    }

    public COUIDividerAppBarLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ int access$000(COUIDividerAppBarLayout cOUIDividerAppBarLayout, RecyclerView recyclerView) {
        return cOUIDividerAppBarLayout.resolveRecyclerScrollDyForDivider(recyclerView);
    }

    public static /* synthetic */ View access$100(COUIDividerAppBarLayout cOUIDividerAppBarLayout) {
        return cOUIDividerAppBarLayout.mDividerTargetView;
    }

    public static /* synthetic */ boolean access$200(COUIDividerAppBarLayout cOUIDividerAppBarLayout) {
        return cOUIDividerAppBarLayout.isDividerAnimEnable();
    }

    private void bindListener() {
        View view = this.mDividerTargetView;
        if (view != null) {
            bindTargetViewListener(view);
            this.mScrollDyByScroll = resolveDividerScrollDy(this.mDividerTargetView);
            onDividerChanged();
        } else if (DEBUG) {
            COUILog.d(TAG, "Can not find divider target view");
        }
    }

    private void bindTargetViewListener(View view) {
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).addOnScrollListener(this.mOnScrollListener);
        } else {
            view.getViewTreeObserver().addOnScrollChangedListener(this.mOnScrollChangedListener);
        }
        view.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
    }

    private void findDividerTargetView() {
        if (!this.mDividerTargetExplicitlyBound || this.mDividerTargetView == null) {
            ViewGroup viewGroup = (ViewGroup) getParent();
            this.mDividerTargetView = null;
            this.mTargetView = null;
            for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                View childAt = viewGroup.getChildAt(i3);
                if (isDividerTargetView(childAt)) {
                    this.mDividerTargetView = childAt;
                    if (childAt instanceof RecyclerView) {
                        this.mTargetView = (RecyclerView) childAt;
                        return;
                    }
                    return;
                }
            }
        }
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.COUIDividerAppBarLayout);
        this.mHasDivider = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUIDividerAppBarLayout_hasDivider, true);
        this.mDividerStartAlpha = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIDividerAppBarLayout_dividerStartAlpha, 0.0f);
        this.mDividerEndAlpha = typedArrayObtainStyledAttributes.getFloat(R.styleable.COUIDividerAppBarLayout_dividerEndAlpha, this.mHasDivider ? 1.0f : 0.0f);
        this.mDividerStartMarginHorizontal = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIDividerAppBarLayout_dividerStartMarginHorizontal, getContext().getResources().getDimensionPixelOffset(R.dimen.coui_appbar_divider_expanded_margin_horizontal));
        this.mDividerEndMarginHorizontal = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.COUIDividerAppBarLayout_dividerEndMarginHorizontal, getContext().getResources().getDimensionPixelOffset(R.dimen.coui_appbar_divider_collapsed_margin_horizontal));
        typedArrayObtainStyledAttributes.recycle();
        this.mDividerEndAlpha = Math.max(0.0f, Math.min(this.mDividerEndAlpha, 1.0f));
        this.mDividerStartAlpha = Math.max(0.0f, Math.min(this.mDividerStartAlpha, 1.0f));
        setChildrenDrawingOrderEnabled(true);
        this.mOnScrollListener = new AnonymousClass1();
        this.mOnLayoutChangeListener = new AnonymousClass2();
        this.mOnScrollChangedListener = new AnonymousClass3();
    }

    private boolean isDividerAnimEnable() {
        return this.mDividerView != null && isDividerShown() && ((this.mDividerStartAlpha > this.mDividerEndAlpha ? 1 : (this.mDividerStartAlpha == this.mDividerEndAlpha ? 0 : -1)) != 0 || this.mDividerStartMarginHorizontal != this.mDividerEndMarginHorizontal);
    }

    private boolean isDividerShown() {
        return this.mHasDivider && !this.mBlurDividerSuppressed;
    }

    private boolean isDividerTargetView(View view) {
        return (view instanceof RecyclerView) || (view instanceof NestedScrollView) || (view instanceof ScrollView) || (view instanceof WebView);
    }

    private int resolveDividerScrollDy(View view) {
        if (view instanceof RecyclerView) {
            return resolveRecyclerScrollDyForDivider((RecyclerView) view);
        }
        if ((view instanceof NestedScrollView) || (view instanceof ScrollView) || (view instanceof WebView)) {
            return Math.min(Math.max(0, view.getScrollY()), getDividerScrollRange());
        }
        return 0;
    }

    private int resolveRecyclerScrollDyForDivider(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null || recyclerView.getChildCount() == 0) {
            return 0;
        }
        View childAt = recyclerView.getChildAt(0);
        int position = layoutManager.getPosition(childAt);
        int dividerScrollRange = getDividerScrollRange();
        return position > 0 ? dividerScrollRange : Math.min(Math.max(0, recyclerView.getPaddingTop() - childAt.getTop()), dividerScrollRange);
    }

    private void setDividerHorizontalMargin(int i3) {
        View view = this.mDividerView;
        if (view == null || !view.isAttachedToWindow() || this.mDividerView.getMeasuredWidth() <= 0) {
            return;
        }
        View view2 = this.mDividerView;
        view2.setPivotX(view2.getMeasuredWidth() / 2.0f);
        this.mDividerView.setScaleX((getMeasuredWidth() - (i3 * 2)) / this.mDividerView.getMeasuredWidth());
    }

    private void unbindTargetViewListener(View view) {
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).removeOnScrollListener(this.mOnScrollListener);
        } else {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnScrollChangedListener(this.mOnScrollChangedListener);
            }
        }
        view.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
    }

    private void updateDividerVisibility() {
        if (this.mDividerView != null) {
            this.mDividerView.setVisibility(isDividerShown() ? 0 : 8);
        }
    }

    public void bindRecyclerView(RecyclerView recyclerView) {
        View view = this.mDividerTargetView;
        if (view != null) {
            unbindTargetViewListener(view);
        }
        this.mDividerTargetExplicitlyBound = recyclerView != null;
        this.mTargetView = recyclerView;
        this.mDividerTargetView = recyclerView;
        if (recyclerView == null || !isAttachedToWindow()) {
            return;
        }
        bindTargetViewListener(this.mDividerTargetView);
        this.mScrollDyByScroll = resolveDividerScrollDy(this.mDividerTargetView);
        onDividerChanged();
    }

    public int computeDividerTotalScrollDy() {
        return Math.max(0, this.mScrollDyByScroll) + this.mScrollDyByOffset + this.mScrollDyByOverScroll;
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout, androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    public CoordinatorLayout.Behavior<COUIAppBarLayout> getBehavior() {
        return new DividerBehavior();
    }

    @Override // android.view.ViewGroup
    public int getChildDrawingOrder(int i3, int i4) {
        int iIndexOfChild;
        View view = this.mDividerView;
        if (view != null && (iIndexOfChild = indexOfChild(view)) >= 0) {
            if (i4 == 0) {
                return iIndexOfChild;
            }
            return i4 <= iIndexOfChild ? i4 - 1 : i4;
        }
        return super.getChildDrawingOrder(i3, i4);
    }

    public float getDividerEndAlpha() {
        return this.mDividerEndAlpha;
    }

    public int getDividerEndMarginHorizontal() {
        return this.mDividerEndMarginHorizontal;
    }

    public int getDividerScrollRange() {
        return getMeasuredHeight();
    }

    public float getDividerStartAlpha() {
        return this.mDividerStartAlpha;
    }

    public int getDividerStartMarginHorizontal() {
        return this.mDividerStartMarginHorizontal;
    }

    public boolean hasDivider() {
        return this.mHasDivider;
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mDividerView == null) {
            View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.coui_appbar_divider_layout, (ViewGroup) this, false);
            this.mDividerView = viewInflate;
            addView(viewInflate, getChildCount());
            this.mDividerView.setAlpha(this.mDividerStartAlpha);
        }
        this.mDividerView.setBackgroundColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorDivider));
        refreshDivider();
        updateDividerVisibility();
        COUIDarkModeUtil.setForceDarkAllow(this.mDividerView, false);
        findDividerTargetView();
        bindListener();
        setTouchscreenBlocksFocus(false);
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        View view = this.mDividerTargetView;
        if (view != null) {
            unbindTargetViewListener(view);
        } else if (DEBUG) {
            COUILog.d(TAG, "Can not find divider target view");
        }
    }

    public void onDividerChanged() {
        if (DEBUG) {
            COUILog.d(TAG, "onDividerChanged: mScrollDyByScroll = " + this.mScrollDyByScroll + " mScrollDyByOffset = " + this.mScrollDyByOffset + " mScrollDyByOverScroll = " + this.mScrollDyByOverScroll);
        }
        int iComputeDividerTotalScrollDy = computeDividerTotalScrollDy();
        if (iComputeDividerTotalScrollDy < 0 || !isDividerAnimEnable()) {
            return;
        }
        float f3 = this.mDividerFraction;
        float dividerScrollRange = getDividerScrollRange();
        if (dividerScrollRange == 0.0f) {
            this.mDividerFraction = dividerScrollRange;
        } else {
            this.mDividerFraction = Math.min(iComputeDividerTotalScrollDy / dividerScrollRange, 1.0f);
        }
        float f4 = this.mDividerStartAlpha;
        float f5 = this.mDividerEndAlpha - f4;
        float f6 = this.mDividerFraction;
        float f7 = f4 + (f5 * f6);
        int i3 = this.mDividerStartMarginHorizontal;
        int i4 = i3 + ((int) ((this.mDividerEndMarginHorizontal - i3) * f6));
        View view = this.mDividerView;
        if (view != null) {
            view.setAlpha(f7);
            setDividerHorizontalMargin(i4);
        }
        OnDividerProgressChangedListener onDividerProgressChangedListener = this.mOnDividerProgressChangedListener;
        if (onDividerProgressChangedListener != null) {
            float f8 = this.mDividerFraction;
            if (f3 != f8) {
                onDividerProgressChangedListener.onDividerProgressChanged(f8);
            }
        }
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mScrollDyByOffset = bundle.getInt(OFFSET_DY_SCROLL_STATE_KEY);
            this.mScrollDyByScroll = bundle.getInt(SCROLL_DY_SCROLL_STATE_KEY);
            this.mScrollDyByOverScroll = bundle.getInt(OVERSCROLL_DY_SCROLL_STATE_KEY);
            this.mDividerFraction = bundle.getFloat(DIVIDER_FRACTION_STATE_KEY);
            parcelable = bundle.getParcelable(SUPER_STATE_KEY);
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState());
        bundle.putInt(OFFSET_DY_SCROLL_STATE_KEY, this.mScrollDyByOffset);
        bundle.putInt(SCROLL_DY_SCROLL_STATE_KEY, this.mScrollDyByScroll);
        bundle.putInt(OVERSCROLL_DY_SCROLL_STATE_KEY, this.mScrollDyByOverScroll);
        bundle.putFloat(DIVIDER_FRACTION_STATE_KEY, this.mDividerFraction);
        return bundle;
    }

    public boolean refreshAppBar(View view) {
        int iResolveDividerScrollDy;
        if (!isDividerTargetView(view) || (iResolveDividerScrollDy = resolveDividerScrollDy(view)) == this.mScrollDyByScroll) {
            return false;
        }
        this.mScrollDyByScroll = iResolveDividerScrollDy;
        return true;
    }

    public void refreshDivider() {
        float f3 = this.mDividerStartAlpha;
        float f4 = this.mDividerEndAlpha - f3;
        float f5 = this.mDividerFraction;
        float f6 = f3 + (f4 * f5);
        int i3 = this.mDividerStartMarginHorizontal;
        int i4 = i3 + ((int) ((this.mDividerEndMarginHorizontal - i3) * f5));
        View view = this.mDividerView;
        if (view != null) {
            view.setBackgroundColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorDivider));
            this.mDividerView.setAlpha(f6);
        }
        onDividerChanged();
        setDividerHorizontalMargin(i4);
        OnDividerProgressChangedListener onDividerProgressChangedListener = this.mOnDividerProgressChangedListener;
        if (onDividerProgressChangedListener != null) {
            onDividerProgressChangedListener.onDividerProgressChanged(this.mDividerFraction);
        }
    }

    public void reset() {
        this.mScrollDyByScroll = 0;
        this.mScrollDyByOffset = 0;
        this.mScrollDyByOverScroll = 0;
    }

    public void setBlurDividerSuppressed(boolean z2) {
        if (this.mBlurDividerSuppressed == z2) {
            return;
        }
        this.mBlurDividerSuppressed = z2;
        updateDividerVisibility();
    }

    public void setDebug(boolean z2) {
        DEBUG = z2;
    }

    public void setDividerEndAlpha(float f3) {
        this.mDividerEndAlpha = f3;
    }

    public void setDividerEndMarginHorizontal(int i3) {
        this.mDividerEndMarginHorizontal = i3;
    }

    public void setDividerStartAlpha(float f3) {
        this.mDividerStartAlpha = f3;
    }

    public void setDividerStartMarginHorizontal(int i3) {
        this.mDividerStartMarginHorizontal = i3;
    }

    public void setHasDivider(boolean z2) {
        this.mHasDivider = z2;
        updateDividerVisibility();
    }

    public void setOnDividerProgressChangedListener(OnDividerProgressChangedListener onDividerProgressChangedListener) {
        this.mOnDividerProgressChangedListener = onDividerProgressChangedListener;
    }

    public void translateDivider(int i3) {
        View view = this.mDividerView;
        if (view == null || !view.isAttachedToWindow()) {
            return;
        }
        this.mDividerView.setTranslationY(i3);
    }

    public COUIDividerAppBarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIDividerAppBarLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mCollapsable = false;
        this.mTargetViewState = 0;
        this.mTargetView = null;
        this.mDividerTargetView = null;
        this.mDividerTargetExplicitlyBound = false;
        this.mDividerFraction = 0.0f;
        this.mBlurDividerSuppressed = false;
        this.mOnDividerProgressChangedListener = null;
        this.mOnScrollListener = null;
        this.mOnScrollChangedListener = null;
        this.mOnLayoutChangeListener = null;
        init(attributeSet);
    }
}