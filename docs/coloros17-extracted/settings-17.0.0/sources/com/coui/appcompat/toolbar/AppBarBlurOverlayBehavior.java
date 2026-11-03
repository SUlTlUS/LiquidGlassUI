package com.coui.appcompat.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ScrollView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.COUIAppBarLayout;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public class AppBarBlurOverlayBehavior extends CoordinatorLayout.Behavior<AppBarBlurOverlayView> {
    private static final float ALPHA_SCROLL_THRESHOLD_DP = 30.0f;
    private int mAccumulatedContentScrollPx;
    private float mAlphaScrollThresholdPx;
    private COUIAppBarLayout mAppBarLayout;
    private int mAppBarVerticalOffset;
    private int mContentScrollBaselinePx;
    private final ViewTreeObserver.OnScrollChangedListener mContentScrollChangedListener;
    private ViewTreeObserver mContentScrollObserver;
    private CoordinatorLayout mCoordinatorLayout;
    private final COUIAppBarLayout.OnOffsetChangedListener mOffsetChangedListener;
    private boolean mOffsetListenerAdded;
    private AppBarBlurOverlayView mOverlayView;
    private ViewTreeObserver.OnGlobalLayoutListener mPendingScrollTargetListener;
    private final RecyclerView.OnScrollListener mRecyclerScrollListener;
    private boolean mScrollListenerAttached;
    private View mScrollTarget;

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.AppBarBlurOverlayBehavior$1, reason: invalid class name */
    public class AnonymousClass1 extends RecyclerView.OnScrollListener {
        public AnonymousClass1() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
            AppBarBlurOverlayBehavior.access$000(AppBarBlurOverlayBehavior.this, recyclerView, i4);
        }
    }

    public AppBarBlurOverlayBehavior(Context context) {
        this.mContentScrollBaselinePx = -1;
        this.mOffsetChangedListener = new a(this);
        this.mRecyclerScrollListener = new AnonymousClass1();
        this.mContentScrollChangedListener = new b(this);
        initThresholdPx(context);
    }

    public static /* synthetic */ void a(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior, COUIAppBarLayout cOUIAppBarLayout, int i3) {
        appBarBlurOverlayBehavior.mAppBarVerticalOffset = i3;
        AppBarBlurOverlayView appBarBlurOverlayView = appBarBlurOverlayBehavior.mOverlayView;
        if (appBarBlurOverlayView != null) {
            appBarBlurOverlayBehavior.updateOverlayAlpha(appBarBlurOverlayView, cOUIAppBarLayout);
        }
    }

    public static /* synthetic */ void access$000(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior, RecyclerView recyclerView, int i3) {
        appBarBlurOverlayBehavior.applyRecyclerViewScrollDelta(recyclerView, i3);
    }

    private void applyContentScrollPx(int i3) {
        COUIAppBarLayout cOUIAppBarLayout;
        if (this.mAccumulatedContentScrollPx == i3) {
            return;
        }
        this.mAccumulatedContentScrollPx = i3;
        AppBarBlurOverlayView appBarBlurOverlayView = this.mOverlayView;
        if (appBarBlurOverlayView == null || (cOUIAppBarLayout = this.mAppBarLayout) == null) {
            return;
        }
        updateOverlayAlpha(appBarBlurOverlayView, cOUIAppBarLayout);
    }

    private boolean applyOverlayBlurProperties(AppBarBlurOverlayView appBarBlurOverlayView, float f3) {
        boolean z2;
        if (appBarBlurOverlayView.getAlpha() != f3) {
            appBarBlurOverlayView.setAlpha(f3);
            z2 = true;
        } else {
            z2 = false;
        }
        if (appBarBlurOverlayView.isScrollDrivenAlphaEnabled()) {
            int i3 = f3 == 0.0f ? 4 : 0;
            if (appBarBlurOverlayView.getVisibility() != i3) {
                appBarBlurOverlayView.setVisibility(i3);
                return true;
            }
        }
        return z2;
    }

    private void applyRecyclerViewScrollDelta(RecyclerView recyclerView, int i3) {
        if (recyclerView.getScrollY() != 0) {
            refreshOverlayAlphaFromScroll();
            return;
        }
        if (i3 == 0) {
            refreshOverlayAlphaFromScroll();
        } else if (i3 >= 0 || this.mAccumulatedContentScrollPx != 0) {
            applyContentScrollPx(Math.max(0, this.mAccumulatedContentScrollPx + i3));
        }
    }

    private void attachContentScrollChangeListener(View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnScrollChangedListener(this.mContentScrollChangedListener);
            this.mContentScrollObserver = viewTreeObserver;
        }
    }

    private void attachContentScrollTracking(View view) {
        View viewResolveScrollableTarget = resolveScrollableTarget(view);
        if (isScrollableView(viewResolveScrollableTarget)) {
            if (this.mScrollTarget == viewResolveScrollableTarget && this.mScrollListenerAttached) {
                return;
            }
            detachContentScrollTracking();
            this.mScrollTarget = viewResolveScrollableTarget;
            if (viewResolveScrollableTarget instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) viewResolveScrollableTarget;
                recyclerView.addOnScrollListener(this.mRecyclerScrollListener);
                attachContentScrollChangeListener(recyclerView);
                applyRecyclerViewScrollDelta(recyclerView, 0);
            } else {
                attachContentScrollChangeListener(viewResolveScrollableTarget);
                syncContentScrollFromTarget(viewResolveScrollableTarget);
            }
            this.mScrollListenerAttached = true;
        }
    }

    public static /* synthetic */ void b(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior) {
        View view = appBarBlurOverlayBehavior.mScrollTarget;
        if (view instanceof RecyclerView) {
            appBarBlurOverlayBehavior.applyRecyclerViewScrollDelta((RecyclerView) view, 0);
        } else {
            if (view == null || !appBarBlurOverlayBehavior.isScrollableView(view)) {
                return;
            }
            appBarBlurOverlayBehavior.syncContentScrollFromTarget(appBarBlurOverlayBehavior.mScrollTarget);
        }
    }

    private void bindAppBar(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView, COUIAppBarLayout cOUIAppBarLayout) {
        this.mOverlayView = appBarBlurOverlayView;
        this.mCoordinatorLayout = coordinatorLayout;
        if (this.mAppBarLayout == cOUIAppBarLayout && this.mOffsetListenerAdded) {
            return;
        }
        removeOffsetListener();
        this.mAppBarLayout = cOUIAppBarLayout;
        this.mAppBarVerticalOffset = 0;
        cOUIAppBarLayout.addOnOffsetChangedListener(this.mOffsetChangedListener);
        this.mOffsetListenerAdded = true;
    }

    public static /* synthetic */ void c(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior, CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView) {
        COUIAppBarLayout cOUIAppBarLayoutFindAppBar = appBarBlurOverlayBehavior.findAppBar(coordinatorLayout, appBarBlurOverlayView);
        if (cOUIAppBarLayoutFindAppBar == null) {
            return;
        }
        appBarBlurOverlayBehavior.removePendingScrollTargetListener();
        appBarBlurOverlayBehavior.bindAppBar(coordinatorLayout, appBarBlurOverlayView, cOUIAppBarLayoutFindAppBar);
        appBarBlurOverlayBehavior.updateOverlayBounds(appBarBlurOverlayView, cOUIAppBarLayoutFindAppBar);
        appBarBlurOverlayBehavior.updateOverlayAlpha(appBarBlurOverlayView, cOUIAppBarLayoutFindAppBar);
        appBarBlurOverlayBehavior.scheduleDiscoverScrollTarget(coordinatorLayout, appBarBlurOverlayView);
    }

    private int computeContentScrollOffsetPx(View view) {
        if (view instanceof NestedScrollView) {
            return ((NestedScrollView) view).computeVerticalScrollOffset();
        }
        if (view instanceof ScrollView) {
            return ((ScrollView) view).getScrollY();
        }
        if (view instanceof WebView) {
            return view.getScrollY();
        }
        return 0;
    }

    private float computeOverlayAlpha(AppBarBlurOverlayView appBarBlurOverlayView, COUIAppBarLayout cOUIAppBarLayout) {
        int iResolveScrollDistancePx;
        if (!appBarBlurOverlayView.isScrollDrivenAlphaEnabled()) {
            return appBarBlurOverlayView.getAlpha();
        }
        if (isVerticallyScrollable(cOUIAppBarLayout) && (iResolveScrollDistancePx = resolveScrollDistancePx(cOUIAppBarLayout)) > 0) {
            return Math.min(1.0f, iResolveScrollDistancePx / this.mAlphaScrollThresholdPx);
        }
        return 0.0f;
    }

    public static /* synthetic */ void d(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior, CoordinatorLayout coordinatorLayout) {
        appBarBlurOverlayBehavior.discoverScrollTarget(coordinatorLayout);
        View view = appBarBlurOverlayBehavior.mScrollTarget;
        if (view != null) {
            appBarBlurOverlayBehavior.syncContentScrollFromTarget(view);
        }
    }

    private void detachContentScrollTracking() {
        View view;
        if (this.mScrollListenerAttached && (view = this.mScrollTarget) != null) {
            if (view instanceof RecyclerView) {
                ((RecyclerView) view).removeOnScrollListener(this.mRecyclerScrollListener);
            }
            ViewTreeObserver viewTreeObserver = this.mContentScrollObserver;
            if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
                this.mContentScrollObserver.removeOnScrollChangedListener(this.mContentScrollChangedListener);
            }
        }
        this.mContentScrollObserver = null;
        this.mScrollListenerAttached = false;
        this.mScrollTarget = null;
        this.mAccumulatedContentScrollPx = 0;
        this.mContentScrollBaselinePx = -1;
    }

    private void discoverScrollTarget(CoordinatorLayout coordinatorLayout) {
        View viewFindScrollingDescendant;
        View view = this.mScrollTarget;
        if (view != null && !isScrollTargetUsable(view)) {
            detachContentScrollTracking();
        }
        int childCount = coordinatorLayout.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = coordinatorLayout.getChildAt(i3);
            if (!(childAt instanceof COUIAppBarLayout) && !(childAt instanceof AppBarBlurOverlayView) && isScrollTargetUsable(childAt) && (viewFindScrollingDescendant = findScrollingDescendant(childAt)) != null) {
                attachContentScrollTracking(viewFindScrollingDescendant);
                return;
            }
        }
    }

    private boolean ensureInitialized(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView) {
        this.mCoordinatorLayout = coordinatorLayout;
        this.mOverlayView = appBarBlurOverlayView;
        COUIAppBarLayout cOUIAppBarLayoutFindAppBar = findAppBar(coordinatorLayout, appBarBlurOverlayView);
        if (cOUIAppBarLayoutFindAppBar == null) {
            scheduleDiscoverAppBar(coordinatorLayout, appBarBlurOverlayView);
            return false;
        }
        bindAppBar(coordinatorLayout, appBarBlurOverlayView, cOUIAppBarLayoutFindAppBar);
        boolean zUpdateOverlayAlpha = updateOverlayAlpha(appBarBlurOverlayView, cOUIAppBarLayoutFindAppBar) | updateOverlayBounds(appBarBlurOverlayView, cOUIAppBarLayoutFindAppBar);
        View view = this.mScrollTarget;
        if (view != null && isScrollTargetUsable(view)) {
            return zUpdateOverlayAlpha;
        }
        scheduleDiscoverScrollTarget(coordinatorLayout, appBarBlurOverlayView);
        return zUpdateOverlayAlpha;
    }

    private COUIAppBarLayout findAppBar(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView) {
        List<View> dependencies = coordinatorLayout.getDependencies(appBarBlurOverlayView);
        int size = dependencies.size();
        for (int i3 = 0; i3 < size; i3++) {
            View view = dependencies.get(i3);
            if (view instanceof COUIAppBarLayout) {
                return (COUIAppBarLayout) view;
            }
        }
        return findAppBarDescendant(coordinatorLayout);
    }

    private COUIAppBarLayout findAppBarDescendant(View view) {
        if (view instanceof COUIAppBarLayout) {
            return (COUIAppBarLayout) view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            COUIAppBarLayout cOUIAppBarLayoutFindAppBarDescendant = findAppBarDescendant(viewGroup.getChildAt(i3));
            if (cOUIAppBarLayoutFindAppBarDescendant != null) {
                return cOUIAppBarLayoutFindAppBarDescendant;
            }
        }
        return null;
    }

    private View findScrollingDescendant(View view) {
        if (!isScrollTargetUsable(view)) {
            return null;
        }
        if (isScrollableView(view)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View viewFindScrollingDescendant = findScrollingDescendant(viewGroup.getChildAt(i3));
                if (viewFindScrollingDescendant != null) {
                    return viewFindScrollingDescendant;
                }
            }
        }
        return null;
    }

    private int getEffectiveRecyclerScrollPx(RecyclerView recyclerView) {
        int scrollY = recyclerView.getScrollY();
        return scrollY != 0 ? Math.max(0, this.mAccumulatedContentScrollPx + scrollY) : this.mAccumulatedContentScrollPx;
    }

    private void initThresholdPx(Context context) {
        this.mAlphaScrollThresholdPx = TypedValue.applyDimension(1, 30.0f, context.getResources().getDisplayMetrics());
    }

    private boolean isContentScrollInteractionCapable(View view) {
        if (view.canScrollVertically(1) || view.canScrollVertically(-1)) {
            return true;
        }
        if (view instanceof RecyclerView) {
            return isRecyclerOverscrollCapable((RecyclerView) view);
        }
        return false;
    }

    private static boolean isRecyclerOverscrollCapable(RecyclerView recyclerView) {
        return recyclerView.getOverScrollMode() != 2;
    }

    private boolean isScrollTargetUsable(View view) {
        return view.getVisibility() == 0 && view.isAttachedToWindow();
    }

    private boolean isScrollableView(View view) {
        return (view instanceof RecyclerView) || (view instanceof NestedScrollView) || (view instanceof ScrollView) || (view instanceof WebView);
    }

    private boolean isVerticallyScrollable(COUIAppBarLayout cOUIAppBarLayout) {
        if (cOUIAppBarLayout.getTotalScrollRange() > 0) {
            return true;
        }
        View view = this.mScrollTarget;
        if (view != null) {
            return isContentScrollInteractionCapable(view);
        }
        CoordinatorLayout coordinatorLayout = this.mCoordinatorLayout;
        if (coordinatorLayout == null) {
            return false;
        }
        discoverScrollTarget(coordinatorLayout);
        View view2 = this.mScrollTarget;
        if (view2 != null) {
            return isContentScrollInteractionCapable(view2);
        }
        return false;
    }

    private void refreshOverlayAlphaFromScroll() {
        COUIAppBarLayout cOUIAppBarLayout;
        AppBarBlurOverlayView appBarBlurOverlayView = this.mOverlayView;
        if (appBarBlurOverlayView == null || (cOUIAppBarLayout = this.mAppBarLayout) == null) {
            return;
        }
        updateOverlayAlpha(appBarBlurOverlayView, cOUIAppBarLayout);
    }

    private void removeOffsetListener() {
        COUIAppBarLayout cOUIAppBarLayout = this.mAppBarLayout;
        if (cOUIAppBarLayout != null && this.mOffsetListenerAdded) {
            cOUIAppBarLayout.removeOnOffsetChangedListener(this.mOffsetChangedListener);
        }
        this.mOffsetListenerAdded = false;
        this.mAppBarVerticalOffset = 0;
    }

    private void removePendingScrollTargetListener() {
        AppBarBlurOverlayView appBarBlurOverlayView;
        if (this.mPendingScrollTargetListener == null || (appBarBlurOverlayView = this.mOverlayView) == null) {
            this.mPendingScrollTargetListener = null;
            return;
        }
        ViewTreeObserver viewTreeObserver = appBarBlurOverlayView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnGlobalLayoutListener(this.mPendingScrollTargetListener);
        }
        this.mPendingScrollTargetListener = null;
    }

    private int resolveContentScrollDistancePx() {
        View view = this.mScrollTarget;
        if (view instanceof RecyclerView) {
            return getEffectiveRecyclerScrollPx((RecyclerView) view);
        }
        return (view == null || (view instanceof RecyclerView)) ? this.mAccumulatedContentScrollPx : resolveNonRecyclerContentScrollPx(view);
    }

    private int resolveNonRecyclerContentScrollPx(View view) {
        int iComputeContentScrollOffsetPx = computeContentScrollOffsetPx(view);
        int i3 = this.mContentScrollBaselinePx;
        return i3 >= 0 ? Math.max(0, iComputeContentScrollOffsetPx - i3) : iComputeContentScrollOffsetPx;
    }

    private int resolveScrollDistancePx(COUIAppBarLayout cOUIAppBarLayout) {
        if (cOUIAppBarLayout.getTotalScrollRange() > 0) {
            int iAbs = Math.abs(this.mAppBarVerticalOffset);
            return iAbs > 0 ? iAbs : resolveContentScrollDistancePx();
        }
        if (this.mScrollTarget != null) {
            return resolveContentScrollDistancePx();
        }
        return 0;
    }

    private View resolveScrollableTarget(View view) {
        View viewFindScrollingDescendant;
        return (isScrollableView(view) || (viewFindScrollingDescendant = findScrollingDescendant(view)) == null) ? view : viewFindScrollingDescendant;
    }

    private void scheduleDiscoverAppBar(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView) {
        removePendingScrollTargetListener();
        this.mPendingScrollTargetListener = new c(this, coordinatorLayout, appBarBlurOverlayView);
        appBarBlurOverlayView.getViewTreeObserver().addOnGlobalLayoutListener(this.mPendingScrollTargetListener);
    }

    private void scheduleDiscoverScrollTarget(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView) {
        COUIAppBarLayout cOUIAppBarLayout = this.mAppBarLayout;
        if (cOUIAppBarLayout == null || cOUIAppBarLayout.getTotalScrollRange() <= 0 || Math.abs(this.mAppBarVerticalOffset) <= 0) {
            discoverScrollTarget(coordinatorLayout);
            View view = this.mScrollTarget;
            if (view != null) {
                syncContentScrollFromTarget(view);
            } else {
                appBarBlurOverlayView.post(new d(this, coordinatorLayout));
            }
        }
    }

    private void syncContentScrollFromTarget(View view) {
        if (isScrollableView(view)) {
            if (view instanceof RecyclerView) {
                applyRecyclerViewScrollDelta((RecyclerView) view, 0);
                return;
            }
            int iComputeContentScrollOffsetPx = computeContentScrollOffsetPx(view);
            if (!view.canScrollVertically(-1)) {
                this.mContentScrollBaselinePx = iComputeContentScrollOffsetPx;
            }
            int i3 = this.mContentScrollBaselinePx;
            if (i3 >= 0) {
                iComputeContentScrollOffsetPx = Math.max(0, iComputeContentScrollOffsetPx - i3);
            }
            applyContentScrollPx(iComputeContentScrollOffsetPx);
        }
    }

    private boolean updateOverlayAlpha(AppBarBlurOverlayView appBarBlurOverlayView, COUIAppBarLayout cOUIAppBarLayout) {
        return applyOverlayBlurProperties(appBarBlurOverlayView, computeOverlayAlpha(appBarBlurOverlayView, cOUIAppBarLayout));
    }

    private boolean updateOverlayBounds(AppBarBlurOverlayView appBarBlurOverlayView, View view) {
        int top = view.getTop();
        int bottom = view.getBottom() + appBarBlurOverlayView.getExtraHeightPx();
        if (appBarBlurOverlayView.getLeft() == view.getLeft() && appBarBlurOverlayView.getRight() == view.getRight() && appBarBlurOverlayView.getTop() == top && appBarBlurOverlayView.getBottom() == bottom) {
            return false;
        }
        appBarBlurOverlayView.layout(view.getLeft(), top, view.getRight(), bottom);
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public /* bridge */ /* synthetic */ boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
        return layoutDependsOn(coordinatorLayout, (AppBarBlurOverlayView) view, view2);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public /* bridge */ /* synthetic */ boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        return onDependentViewChanged(coordinatorLayout, (AppBarBlurOverlayView) view, view2);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onDetachedFromLayoutParams() {
        removeOffsetListener();
        detachContentScrollTracking();
        removePendingScrollTargetListener();
        this.mOverlayView = null;
        this.mAppBarLayout = null;
        this.mCoordinatorLayout = null;
        super.onDetachedFromLayoutParams();
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i3) {
        return onLayoutChild(coordinatorLayout, (AppBarBlurOverlayView) view, i3);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public /* bridge */ /* synthetic */ boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i3, int i4) {
        return onStartNestedScroll(coordinatorLayout, (AppBarBlurOverlayView) view, view2, view3, i3, i4);
    }

    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView, View view) {
        return view instanceof COUIAppBarLayout;
    }

    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView, View view) {
        if (view instanceof COUIAppBarLayout) {
            return ensureInitialized(coordinatorLayout, appBarBlurOverlayView);
        }
        return false;
    }

    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView, int i3) {
        ensureInitialized(coordinatorLayout, appBarBlurOverlayView);
        return this.mAppBarLayout != null;
    }

    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView, View view, View view2, int i3, int i4) {
        if ((i3 & 2) == 0) {
            return false;
        }
        this.mOverlayView = appBarBlurOverlayView;
        this.mCoordinatorLayout = coordinatorLayout;
        return false;
    }

    public AppBarBlurOverlayBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContentScrollBaselinePx = -1;
        this.mOffsetChangedListener = new a(this);
        this.mRecyclerScrollListener = new AnonymousClass1();
        this.mContentScrollChangedListener = new b(this);
        initThresholdPx(context);
    }
}