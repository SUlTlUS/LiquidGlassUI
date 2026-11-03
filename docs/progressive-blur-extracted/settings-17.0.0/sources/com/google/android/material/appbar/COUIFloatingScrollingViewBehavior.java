package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.log.COUILog;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class COUIFloatingScrollingViewBehavior extends COUIAppBarLayout.ScrollingViewBehavior {
    private static final boolean COUI_DEBUG;
    private static final String TAG = "COUIFloatingBehavior";
    private int mAppliedHeaderHeight;
    private View mBehaviorChild;
    private boolean mHeaderOffsetEnabled;
    private int mLastAppliedPaddingTop;
    private View.OnAttachStateChangeListener mPendingDetachCleanupListener;
    private boolean mPendingScrollRestore;
    private View.OnLayoutChangeListener mScrollingViewReadyListener;
    private int mTargetScrollOffset;
    private boolean mTransparentBgEnabled;
    private boolean mTransparentBgPending;

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUIFloatingScrollingViewBehavior$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes8.dex */
    public class AnonymousClass1 implements View.OnAttachStateChangeListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            COUIFloatingScrollingViewBehavior.access$000(COUIFloatingScrollingViewBehavior.this, view);
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUIFloatingScrollingViewBehavior() {
        this.mAppliedHeaderHeight = 0;
        this.mTransparentBgEnabled = false;
        this.mTransparentBgPending = false;
        this.mLastAppliedPaddingTop = -1;
        this.mPendingScrollRestore = false;
        this.mTargetScrollOffset = 0;
        this.mHeaderOffsetEnabled = true;
    }

    public static /* synthetic */ void a(int i3, RecyclerView recyclerView) {
        int iComputeVerticalScrollOffset = i3 - recyclerView.computeVerticalScrollOffset();
        if (COUI_DEBUG) {
            COUILog.d(TAG, "restorePendingScrollOffset: target=" + i3 + ", scrollBy dy=" + iComputeVerticalScrollOffset);
        }
        if (iComputeVerticalScrollOffset != 0) {
            recyclerView.scrollBy(0, iComputeVerticalScrollOffset);
        }
    }

    public static /* synthetic */ void access$000(COUIFloatingScrollingViewBehavior cOUIFloatingScrollingViewBehavior, View view) {
        cOUIFloatingScrollingViewBehavior.removeScrollingViewReadyListener(view);
    }

    private boolean applyHeaderOffsetToContent(View view, int i3) {
        View viewFindScrollingView = findScrollingView(view);
        if (viewFindScrollingView != null) {
            view = viewFindScrollingView;
        } else {
            if (!(view instanceof ViewGroup) || ((ViewGroup) view).getChildCount() == 0) {
                if (!COUI_DEBUG) {
                    return false;
                }
                COUILog.d(TAG, "applyHeaderOffsetToContent: defer until scrolling view or children");
                return false;
            }
            if (COUI_DEBUG) {
                COUILog.d(TAG, "applyHeaderOffsetToContent: padding behavior child (non-scrollable subtree)");
            }
        }
        if (view instanceof WebView) {
            return true;
        }
        if (this.mLastAppliedPaddingTop >= 0 && view.getPaddingTop() == this.mLastAppliedPaddingTop && this.mAppliedHeaderHeight == i3) {
            if (COUI_DEBUG) {
                COUILog.d(TAG, "applyHeaderOffsetToContent: padding already correct, skip");
            }
            return true;
        }
        if (COUI_DEBUG) {
            COUILog.d(TAG, "applyHeaderOffsetToContent: headerHeight=" + i3);
        }
        applyPaddingHeaderOffset(view, i3);
        return true;
    }

    private void applyPaddingHeaderOffset(View view, int i3) {
        int i4;
        int paddingTop = view.getPaddingTop();
        int i5 = this.mLastAppliedPaddingTop;
        int i6 = (i5 < 0 || paddingTop != i5) ? paddingTop : i5 - this.mAppliedHeaderHeight;
        int i7 = i6 + i3;
        int i8 = i7 - paddingTop;
        boolean z2 = i5 >= 0 && (i4 = this.mAppliedHeaderHeight) > 0 && i3 != i4;
        boolean z3 = z2 && i8 != 0 && (view instanceof RecyclerView) && ((RecyclerView) view).getChildCount() > 0;
        if (z3) {
            this.mTargetScrollOffset = ((RecyclerView) view).computeVerticalScrollOffset();
        }
        if (COUI_DEBUG) {
            COUILog.d(TAG, "applyPaddingHeaderOffset: basePadding=" + i6 + ", headerHeight=" + i3 + ", currentPaddingTop=" + paddingTop + ", paddingDelta=" + i8 + ", mTargetScrollOffset=" + this.mTargetScrollOffset + ", restoreScroll=" + z3 + ", headerHeightChanged=" + z2 + ", mLastAppliedPaddingTop=" + this.mLastAppliedPaddingTop + ", mAppliedHeaderHeight=" + this.mAppliedHeaderHeight + ", newPaddingTop=" + i7);
        }
        view.setPadding(view.getPaddingLeft(), i7, view.getPaddingRight(), view.getPaddingBottom());
        this.mLastAppliedPaddingTop = i7;
        if (view instanceof ViewGroup) {
            ((ViewGroup) view).setClipToPadding(false);
        }
        if (z3) {
            this.mPendingScrollRestore = true;
        }
    }

    public static /* synthetic */ void b(COUIFloatingScrollingViewBehavior cOUIFloatingScrollingViewBehavior, CoordinatorLayout coordinatorLayout, View view, View view2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        int measuredHeight;
        cOUIFloatingScrollingViewBehavior.getClass();
        if (COUI_DEBUG) {
            COUILog.d(TAG, "watchForScrollingView: child layout changed, retry offset");
        }
        COUIAppBarLayout cOUIAppBarLayoutFindAppBarDependency = findAppBarDependency(coordinatorLayout, view);
        if (cOUIAppBarLayoutFindAppBarDependency == null || (measuredHeight = cOUIAppBarLayoutFindAppBarDependency.getMeasuredHeight()) <= 0) {
            return;
        }
        cOUIFloatingScrollingViewBehavior.syncHeaderOffsetWithAppBar(coordinatorLayout, view, measuredHeight);
    }

    private static COUIAppBarLayout findAppBarDependency(CoordinatorLayout coordinatorLayout, View view) {
        List<View> dependencies = coordinatorLayout.getDependencies(view);
        int size = dependencies.size();
        for (int i3 = 0; i3 < size; i3++) {
            View view2 = dependencies.get(i3);
            if (view2 instanceof COUIAppBarLayout) {
                return (COUIAppBarLayout) view2;
            }
        }
        return null;
    }

    private void removeScrollingViewReadyListener(View view) {
        View.OnLayoutChangeListener onLayoutChangeListener = this.mScrollingViewReadyListener;
        if (onLayoutChangeListener != null) {
            view.removeOnLayoutChangeListener(onLayoutChangeListener);
            this.mScrollingViewReadyListener = null;
        }
        View.OnAttachStateChangeListener onAttachStateChangeListener = this.mPendingDetachCleanupListener;
        if (onAttachStateChangeListener != null) {
            view.removeOnAttachStateChangeListener(onAttachStateChangeListener);
            this.mPendingDetachCleanupListener = null;
        }
        this.mBehaviorChild = null;
    }

    private void restorePendingScrollOffset(View view) {
        View viewFindScrollingView = findScrollingView(view);
        if (viewFindScrollingView instanceof RecyclerView) {
            this.mPendingScrollRestore = false;
            RecyclerView recyclerView = (RecyclerView) viewFindScrollingView;
            m mVar = new m(this.mTargetScrollOffset, recyclerView);
            if (recyclerView.isComputingLayout()) {
                recyclerView.post(mVar);
            } else {
                mVar.run();
            }
        }
    }

    private void setAppBarLayoutTransparent(COUIAppBarLayout cOUIAppBarLayout) {
        cOUIAppBarLayout.setBackgroundColor(0);
        cOUIAppBarLayout.setTargetElevation(0.0f);
    }

    private void syncHeaderOffsetWithAppBar(CoordinatorLayout coordinatorLayout, View view, int i3) {
        if (i3 <= 0) {
            return;
        }
        if (!this.mHeaderOffsetEnabled) {
            this.mAppliedHeaderHeight = i3;
            removeScrollingViewReadyListener(view);
        } else if (!applyHeaderOffsetToContent(view, i3)) {
            watchForScrollingView(coordinatorLayout, view);
        } else {
            this.mAppliedHeaderHeight = i3;
            removeScrollingViewReadyListener(view);
        }
    }

    private void watchForScrollingView(CoordinatorLayout coordinatorLayout, View view) {
        this.mBehaviorChild = view;
        removeScrollingViewReadyListener(view);
        if (COUI_DEBUG) {
            COUILog.d(TAG, "watchForScrollingView: watching child layout changes for scrolling view");
        }
        l lVar = new l(this, coordinatorLayout, view);
        this.mScrollingViewReadyListener = lVar;
        view.addOnLayoutChangeListener(lVar);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1();
        this.mPendingDetachCleanupListener = anonymousClass1;
        view.addOnAttachStateChangeListener(anonymousClass1);
    }

    public View findScrollingView(View view) {
        if (isScrollableView(view)) {
            return view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View viewFindScrollingView = findScrollingView(viewGroup.getChildAt(i3));
            if (viewFindScrollingView != null) {
                return viewFindScrollingView;
            }
        }
        return null;
    }

    public boolean isScrollableView(View view) {
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        if ((view instanceof RecyclerView) || (view instanceof NestedScrollView) || (view instanceof ScrollView) || (view instanceof HorizontalScrollView) || (view instanceof ListView) || (view instanceof GridView) || (view instanceof WebView) || view.canScrollVertically(1)) {
            return true;
        }
        return view.canScrollVertically(-1);
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout.ScrollingViewBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        boolean zOnDependentViewChanged = super.onDependentViewChanged(coordinatorLayout, view, view2);
        if (view2 instanceof COUIAppBarLayout) {
            COUIAppBarLayout cOUIAppBarLayout = (COUIAppBarLayout) view2;
            int measuredHeight = cOUIAppBarLayout.getMeasuredHeight();
            if (this.mTransparentBgEnabled && this.mTransparentBgPending) {
                if (COUI_DEBUG) {
                    COUILog.d(TAG, "onDependentViewChanged: apply transparent AppBar bg (pending cleared)");
                }
                setAppBarLayoutTransparent(cOUIAppBarLayout);
                this.mTransparentBgPending = false;
            }
            if (measuredHeight > 0 && measuredHeight != this.mAppliedHeaderHeight) {
                if (COUI_DEBUG) {
                    COUILog.d(TAG, "onDependentViewChanged: headerHeight change " + this.mAppliedHeaderHeight + " -> " + measuredHeight);
                }
                syncHeaderOffsetWithAppBar(coordinatorLayout, view, measuredHeight);
            }
            if (COUI_DEBUG) {
                View viewFindScrollingView = findScrollingView(view);
                if (viewFindScrollingView instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) viewFindScrollingView;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDependentViewChanged: paddingTop=");
                    sb.append(recyclerView.getPaddingTop());
                    sb.append(" scrollOffset=");
                    sb.append(recyclerView.computeVerticalScrollOffset());
                    sb.append(" child[0].top=");
                    sb.append(recyclerView.getChildCount() > 0 ? recyclerView.getChildAt(0).getTop() : -1);
                    sb.append(" translationY=");
                    sb.append(view.getTranslationY());
                    COUILog.d(TAG, sb.toString());
                }
            }
        }
        return zOnDependentViewChanged;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onDetachedFromLayoutParams() {
        View view = this.mBehaviorChild;
        if (view != null) {
            removeScrollingViewReadyListener(view);
        }
        this.mBehaviorChild = null;
        super.onDetachedFromLayoutParams();
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout.ScrollingViewBehavior, com.google.android.material.appbar.COUIViewOffsetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i3) {
        int measuredHeight;
        COUIAppBarLayout cOUIAppBarLayoutFindAppBarDependency = findAppBarDependency(coordinatorLayout, view);
        if (cOUIAppBarLayoutFindAppBarDependency != null && (measuredHeight = cOUIAppBarLayoutFindAppBarDependency.getMeasuredHeight()) > 0) {
            syncHeaderOffsetWithAppBar(coordinatorLayout, view, measuredHeight);
        }
        boolean zOnLayoutChild = super.onLayoutChild(coordinatorLayout, view, i3);
        if (this.mPendingScrollRestore) {
            restorePendingScrollOffset(view);
        }
        return zOnLayoutChild;
    }

    @Override // com.google.android.material.appbar.COUIAppBarLayout.ScrollingViewBehavior, com.google.android.material.appbar.COUIHeaderScrollingViewBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, int i5, int i6) {
        COUIAppBarLayout cOUIAppBarLayoutFindAppBarDependency;
        int measuredHeight;
        if (this.mHeaderOffsetEnabled && (cOUIAppBarLayoutFindAppBarDependency = findAppBarDependency(coordinatorLayout, view)) != null && (measuredHeight = cOUIAppBarLayoutFindAppBarDependency.getMeasuredHeight()) > 0 && ((this.mLastAppliedPaddingTop < 0 || measuredHeight != this.mAppliedHeaderHeight) && applyHeaderOffsetToContent(view, measuredHeight))) {
            this.mAppliedHeaderHeight = measuredHeight;
        }
        return super.onMeasureChild(coordinatorLayout, view, i3, i4, i5, i6);
    }

    public void setHeaderOffsetEnabled(boolean z2) {
        this.mHeaderOffsetEnabled = z2;
    }

    public void setTransparentBg(boolean z2) {
        setTransparentBg(z2, null);
    }

    @Override // com.google.android.material.appbar.COUIHeaderScrollingViewBehavior
    public boolean shouldHeaderOverlapScrollingChild() {
        return this.mHeaderOffsetEnabled;
    }

    public void setTransparentBg(boolean z2, COUIAppBarLayout cOUIAppBarLayout) {
        this.mTransparentBgEnabled = z2;
        this.mTransparentBgPending = z2;
        if (!z2 || cOUIAppBarLayout == null || cOUIAppBarLayout.getMeasuredHeight() <= 0) {
            return;
        }
        setAppBarLayoutTransparent(cOUIAppBarLayout);
        this.mTransparentBgPending = false;
    }

    public COUIFloatingScrollingViewBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAppliedHeaderHeight = 0;
        this.mTransparentBgEnabled = false;
        this.mTransparentBgPending = false;
        this.mLastAppliedPaddingTop = -1;
        this.mPendingScrollRestore = false;
        this.mTargetScrollOffset = 0;
        this.mHeaderOffsetEnabled = true;
    }
}