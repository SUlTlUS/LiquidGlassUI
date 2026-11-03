package com.google.android.material.appbar;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.motion.MotionUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AppBarLayout extends LinearLayout implements CoordinatorLayout.AttachedBehavior {
    private static final int DEF_STYLE_RES = R.style.Widget_Design_AppBarLayout;
    private static final int INVALID_SCROLL_RANGE = -1;
    static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
    static final int PENDING_ACTION_COLLAPSED = 2;
    static final int PENDING_ACTION_EXPANDED = 1;
    static final int PENDING_ACTION_FORCE = 8;
    static final int PENDING_ACTION_NONE = 0;
    private final float appBarElevation;
    private int backgroundOriginalColor;
    private Behavior behavior;
    private int currentOffset;
    private int downPreScrollRange;
    private int downScrollRange;
    private boolean haveChildWithInterpolator;
    private WindowInsetsCompat lastInsets;
    private boolean liftOnScroll;
    private ColorStateList liftOnScrollColor;
    private ValueAnimator liftOnScrollColorAnimator;
    private final long liftOnScrollColorDuration;
    private final TimeInterpolator liftOnScrollColorInterpolator;
    private ValueAnimator.AnimatorUpdateListener liftOnScrollColorUpdateListener;
    private final List<LiftOnScrollListener> liftOnScrollListeners;
    private WeakReference<View> liftOnScrollTargetView;
    private int liftOnScrollTargetViewId;
    private final LinkedHashSet<LiftOnScrollProgressListener> liftProgressListeners;
    private boolean liftable;
    private boolean liftableOverride;
    private boolean lifted;
    private List<BaseOnOffsetChangedListener> listeners;
    private int pendingAction;
    private Drawable statusBarForeground;
    private Integer statusBarForegroundOriginalColor;
    private int[] tmpStatesArray;
    private int totalScrollRange;

    /* JADX INFO: renamed from: com.google.android.material.appbar.AppBarLayout$1, reason: invalid class name */
    public class AnonymousClass1 implements OnApplyWindowInsetsListener {
        public AnonymousClass1() {
        }

        @Override // androidx.core.view.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return AppBarLayout.this.onWindowInsetChanged(windowInsetsCompat);
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
    public static class BaseBehavior<T extends AppBarLayout> extends HeaderBehavior<T> {
        private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
        private WeakReference<View> lastNestedScrollingChildRef;
        private int lastStartedType;
        private ValueAnimator offsetAnimator;
        private int offsetDelta;
        private BaseDragCallback onDragCallback;
        private SavedState savedState;

        /* JADX INFO: renamed from: com.google.android.material.appbar.AppBarLayout$BaseBehavior$1, reason: invalid class name */
        public class AnonymousClass1 implements ValueAnimator.AnimatorUpdateListener {
            final /* synthetic */ AppBarLayout val$child;
            final /* synthetic */ CoordinatorLayout val$coordinatorLayout;

            public AnonymousClass1(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
                this.val$coordinatorLayout = coordinatorLayout;
                this.val$child = appBarLayout;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BaseBehavior.this.setHeaderTopBottomOffset(this.val$coordinatorLayout, this.val$child, ((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        }

        /* JADX INFO: renamed from: com.google.android.material.appbar.AppBarLayout$BaseBehavior$2, reason: invalid class name */
        public class AnonymousClass2 extends AccessibilityDelegateCompat {
            final /* synthetic */ AppBarLayout val$appBarLayout;
            final /* synthetic */ CoordinatorLayout val$coordinatorLayout;

            public AnonymousClass2(AppBarLayout appBarLayout, CoordinatorLayout coordinatorLayout) {
                this.val$appBarLayout = appBarLayout;
                this.val$coordinatorLayout = coordinatorLayout;
            }

            @Override // androidx.core.view.AccessibilityDelegateCompat
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                View viewAccess$000;
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setClassName(ScrollView.class.getName());
                if (this.val$appBarLayout.getTotalScrollRange() == 0 || (viewAccess$000 = BaseBehavior.access$000(BaseBehavior.this, this.val$coordinatorLayout)) == null || !BaseBehavior.access$100(BaseBehavior.this, this.val$appBarLayout)) {
                    return;
                }
                if (BaseBehavior.this.getTopBottomOffsetForScrollingSibling() != (-this.val$appBarLayout.getTotalScrollRange())) {
                    accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD);
                    accessibilityNodeInfoCompat.setScrollable(true);
                }
                if (BaseBehavior.this.getTopBottomOffsetForScrollingSibling() != 0) {
                    if (!viewAccess$000.canScrollVertically(-1)) {
                        accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD);
                        accessibilityNodeInfoCompat.setScrollable(true);
                    } else if ((-this.val$appBarLayout.getDownNestedPreScrollRange()) != 0) {
                        accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD);
                        accessibilityNodeInfoCompat.setScrollable(true);
                    }
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            @Override // androidx.core.view.AccessibilityDelegateCompat
            public boolean performAccessibilityAction(View view, int i3, Bundle bundle) {
                if (i3 == 4096) {
                    this.val$appBarLayout.setExpanded(false);
                    return true;
                }
                if (i3 != 8192) {
                    return super.performAccessibilityAction(view, i3, bundle);
                }
                if (BaseBehavior.this.getTopBottomOffsetForScrollingSibling() != 0) {
                    View viewAccess$000 = BaseBehavior.access$000(BaseBehavior.this, this.val$coordinatorLayout);
                    if (!viewAccess$000.canScrollVertically(-1)) {
                        this.val$appBarLayout.setExpanded(true);
                        return true;
                    }
                    int i4 = -this.val$appBarLayout.getDownNestedPreScrollRange();
                    if (i4 != 0) {
                        BaseBehavior.this.onNestedPreScroll(this.val$coordinatorLayout, this.val$appBarLayout, viewAccess$000, 0, i4, new int[]{0, 0}, 1);
                        return true;
                    }
                }
                return false;
            }
        }

        public static abstract class BaseDragCallback<T extends AppBarLayout> {
            public abstract boolean canDrag(T t2);
        }

        public BaseBehavior() {
        }

        public static /* synthetic */ View access$000(BaseBehavior baseBehavior, CoordinatorLayout coordinatorLayout) {
            return baseBehavior.getChildWithScrollingBehavior(coordinatorLayout);
        }

        public static /* synthetic */ boolean access$100(BaseBehavior baseBehavior, AppBarLayout appBarLayout) {
            return baseBehavior.childrenHaveScrollFlags(appBarLayout);
        }

        public static /* synthetic */ int access$200(BaseBehavior baseBehavior) {
            return baseBehavior.offsetDelta;
        }

        private void addAccessibilityDelegateIfNeeded(CoordinatorLayout coordinatorLayout, T t2) {
            if (ViewCompat.hasAccessibilityDelegate(coordinatorLayout)) {
                return;
            }
            ViewCompat.setAccessibilityDelegate(coordinatorLayout, new AnonymousClass2(t2, coordinatorLayout));
        }

        private void animateOffsetTo(CoordinatorLayout coordinatorLayout, T t2, int i3, float f3) {
            int iAbs = Math.abs(getTopBottomOffsetForScrollingSibling() - i3);
            float fAbs = Math.abs(f3);
            animateOffsetWithDuration(coordinatorLayout, t2, i3, fAbs > 0.0f ? Math.round((iAbs / fAbs) * 1000.0f) * 3 : (int) (((iAbs / t2.getHeight()) + 1.0f) * 150.0f));
        }

        private void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4) {
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling();
            if (topBottomOffsetForScrollingSibling == i3) {
                ValueAnimator valueAnimator = this.offsetAnimator;
                if (valueAnimator == null || !valueAnimator.isRunning()) {
                    return;
                }
                this.offsetAnimator.cancel();
                return;
            }
            ValueAnimator valueAnimator2 = this.offsetAnimator;
            if (valueAnimator2 == null) {
                ValueAnimator valueAnimator3 = new ValueAnimator();
                this.offsetAnimator = valueAnimator3;
                valueAnimator3.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
                this.offsetAnimator.addUpdateListener(new AnonymousClass1(coordinatorLayout, t2));
            } else {
                valueAnimator2.cancel();
            }
            this.offsetAnimator.setDuration(Math.min(i4, 600));
            this.offsetAnimator.setIntValues(topBottomOffsetForScrollingSibling, i3);
            this.offsetAnimator.start();
        }

        private int calculateSnapOffset(int i3, int i4, int i5) {
            return i3 < (i4 + i5) / 2 ? i4 : i5;
        }

        private boolean canScrollChildren(CoordinatorLayout coordinatorLayout, T t2, View view) {
            return t2.hasScrollableChildren() && coordinatorLayout.getHeight() - view.getHeight() <= t2.getHeight();
        }

        private static boolean checkFlag(int i3, int i4) {
            return (i3 & i4) == i4;
        }

        private boolean childrenHaveScrollFlags(AppBarLayout appBarLayout) {
            int childCount = appBarLayout.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                if (((LayoutParams) appBarLayout.getChildAt(i3).getLayoutParams()).scrollFlags != 0) {
                    return true;
                }
            }
            return false;
        }

        private View findFirstScrollingChild(CoordinatorLayout coordinatorLayout) {
            int childCount = coordinatorLayout.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = coordinatorLayout.getChildAt(i3);
                if ((childAt instanceof NestedScrollingChild) || (childAt instanceof AbsListView) || (childAt instanceof ScrollView)) {
                    return childAt;
                }
            }
            return null;
        }

        private static View getAppBarChildOnOffset(AppBarLayout appBarLayout, int i3) {
            int iAbs = Math.abs(i3);
            int childCount = appBarLayout.getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = appBarLayout.getChildAt(i4);
                if (iAbs >= childAt.getTop() && iAbs <= childAt.getBottom()) {
                    return childAt;
                }
            }
            return null;
        }

        private int getChildIndexOnOffset(T t2, int i3) {
            int childCount = t2.getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = t2.getChildAt(i4);
                int top = childAt.getTop();
                int bottom = childAt.getBottom();
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (checkFlag(layoutParams.getScrollFlags(), 32)) {
                    top -= ((LinearLayout.LayoutParams) layoutParams).topMargin;
                    bottom += ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                }
                int i5 = -i3;
                if (top <= i5 && bottom >= i5) {
                    return i4;
                }
            }
            return -1;
        }

        private View getChildWithScrollingBehavior(CoordinatorLayout coordinatorLayout) {
            int childCount = coordinatorLayout.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = coordinatorLayout.getChildAt(i3);
                if (((CoordinatorLayout.LayoutParams) childAt.getLayoutParams()).getBehavior() instanceof ScrollingViewBehavior) {
                    return childAt;
                }
            }
            return null;
        }

        private int interpolateOffset(T t2, int i3) {
            int iAbs = Math.abs(i3);
            int childCount = t2.getChildCount();
            int topInset = 0;
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = t2.getChildAt(i4);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                Interpolator scrollInterpolator = layoutParams.getScrollInterpolator();
                if (iAbs >= childAt.getTop() && iAbs <= childAt.getBottom()) {
                    if (scrollInterpolator == null) {
                        break;
                    }
                    int scrollFlags = layoutParams.getScrollFlags();
                    if ((scrollFlags & 1) != 0) {
                        topInset = childAt.getHeight() + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                        if ((scrollFlags & 2) != 0) {
                            topInset -= childAt.getMinimumHeight();
                        }
                    }
                    if (childAt.getFitsSystemWindows()) {
                        topInset -= t2.getTopInset();
                    }
                    if (topInset <= 0) {
                        break;
                    }
                    float f3 = topInset;
                    return Integer.signum(i3) * (childAt.getTop() + Math.round(f3 * scrollInterpolator.getInterpolation((iAbs - childAt.getTop()) / f3)));
                }
            }
            return i3;
        }

        private boolean shouldJumpElevationState(CoordinatorLayout coordinatorLayout, T t2) {
            List<View> dependents = coordinatorLayout.getDependents(t2);
            int size = dependents.size();
            for (int i3 = 0; i3 < size; i3++) {
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) dependents.get(i3).getLayoutParams()).getBehavior();
                if (behavior instanceof ScrollingViewBehavior) {
                    return ((ScrollingViewBehavior) behavior).getOverlayTop() != 0;
                }
            }
            return false;
        }

        private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, T t2) {
            int topInset = t2.getTopInset() + t2.getPaddingTop();
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling() - topInset;
            int childIndexOnOffset = getChildIndexOnOffset(t2, topBottomOffsetForScrollingSibling);
            if (childIndexOnOffset >= 0) {
                View childAt = t2.getChildAt(childIndexOnOffset);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int scrollFlags = layoutParams.getScrollFlags();
                if ((scrollFlags & 17) == 17) {
                    int topInset2 = -childAt.getTop();
                    int minimumHeight = -childAt.getBottom();
                    if (childIndexOnOffset == 0 && t2.getFitsSystemWindows() && childAt.getFitsSystemWindows()) {
                        topInset2 -= t2.getTopInset();
                    }
                    if (checkFlag(scrollFlags, 2)) {
                        minimumHeight += childAt.getMinimumHeight();
                    } else if (checkFlag(scrollFlags, 5)) {
                        int minimumHeight2 = childAt.getMinimumHeight() + minimumHeight;
                        if (topBottomOffsetForScrollingSibling < minimumHeight2) {
                            topInset2 = minimumHeight2;
                        } else {
                            minimumHeight = minimumHeight2;
                        }
                    }
                    if (checkFlag(scrollFlags, 32)) {
                        topInset2 += ((LinearLayout.LayoutParams) layoutParams).topMargin;
                        minimumHeight -= ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                    }
                    animateOffsetTo(coordinatorLayout, t2, MathUtils.clamp(calculateSnapOffset(topBottomOffsetForScrollingSibling, minimumHeight, topInset2) + topInset, -t2.getTotalScrollRange(), 0), 0.0f);
                }
            }
        }

        private void updateAppBarLayoutDrawableState(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4, boolean z2) {
            View appBarChildOnOffset = getAppBarChildOnOffset(t2, i3);
            boolean zShouldLift = false;
            if (appBarChildOnOffset != null) {
                int scrollFlags = ((LayoutParams) appBarChildOnOffset.getLayoutParams()).getScrollFlags();
                if ((scrollFlags & 1) != 0) {
                    int minimumHeight = appBarChildOnOffset.getMinimumHeight();
                    if (i4 <= 0 || (scrollFlags & 12) == 0 ? !((scrollFlags & 2) == 0 || (-i3) < (appBarChildOnOffset.getBottom() - minimumHeight) - t2.getTopInset()) : (-i3) >= (appBarChildOnOffset.getBottom() - minimumHeight) - t2.getTopInset()) {
                        zShouldLift = true;
                    }
                }
            }
            if (t2.isLiftOnScroll()) {
                zShouldLift = t2.shouldLift(findFirstScrollingChild(coordinatorLayout));
            }
            boolean liftedState = t2.setLiftedState(zShouldLift);
            if (z2 || (liftedState && shouldJumpElevationState(coordinatorLayout, t2))) {
                if (t2.getBackground() != null) {
                    t2.getBackground().jumpToCurrentState();
                }
                if (t2.getForeground() != null) {
                    t2.getForeground().jumpToCurrentState();
                }
                if (t2.getStateListAnimator() != null) {
                    t2.getStateListAnimator().jumpToCurrentState();
                }
            }
        }

        @Override // com.google.android.material.appbar.HeaderBehavior
        public /* bridge */ /* synthetic */ boolean canDragView(View view) {
            return canDragView((AppBarLayout) view);
        }

        @Override // com.google.android.material.appbar.HeaderBehavior
        public /* bridge */ /* synthetic */ int getMaxDragOffset(View view) {
            return getMaxDragOffset((AppBarLayout) view);
        }

        @Override // com.google.android.material.appbar.HeaderBehavior
        public /* bridge */ /* synthetic */ int getScrollRangeForDragFling(View view) {
            return getScrollRangeForDragFling((AppBarLayout) view);
        }

        @Override // com.google.android.material.appbar.HeaderBehavior
        public int getTopBottomOffsetForScrollingSibling() {
            return getTopAndBottomOffset() + this.offsetDelta;
        }

        public boolean isOffsetAnimatorRunning() {
            ValueAnimator valueAnimator = this.offsetAnimator;
            return valueAnimator != null && valueAnimator.isRunning();
        }

        @Override // com.google.android.material.appbar.HeaderBehavior
        public /* bridge */ /* synthetic */ void onFlingFinished(CoordinatorLayout coordinatorLayout, View view) {
            onFlingFinished(coordinatorLayout, (AppBarLayout) view);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i3) {
            return onLayoutChild(coordinatorLayout, (AppBarLayout) view, i3);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, int i5, int i6) {
            return onMeasureChild(coordinatorLayout, (AppBarLayout) view, i3, i4, i5, i6);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3, int i4, int[] iArr, int i5) {
            onNestedPreScroll(coordinatorLayout, (AppBarLayout) view, view2, i3, i4, iArr, i5);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            onNestedScroll(coordinatorLayout, (AppBarLayout) view, view2, i3, i4, i5, i6, i7, iArr);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, View view, Parcelable parcelable) {
            onRestoreInstanceState(coordinatorLayout, (AppBarLayout) view, parcelable);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, View view) {
            return onSaveInstanceState(coordinatorLayout, (AppBarLayout) view);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i3, int i4) {
            return onStartNestedScroll(coordinatorLayout, (AppBarLayout) view, view2, view3, i3, i4);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3) {
            onStopNestedScroll(coordinatorLayout, (AppBarLayout) view, view2, i3);
        }

        public void restoreScrollState(SavedState savedState, boolean z2) {
            if (this.savedState == null || z2) {
                this.savedState = savedState;
            }
        }

        public SavedState saveScrollState(Parcelable parcelable, T t2) {
            int topAndBottomOffset = getTopAndBottomOffset();
            int childCount = t2.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = t2.getChildAt(i3);
                int bottom = childAt.getBottom() + topAndBottomOffset;
                if (childAt.getTop() + topAndBottomOffset <= 0 && bottom >= 0) {
                    if (parcelable == null) {
                        parcelable = AbsSavedState.EMPTY_STATE;
                    }
                    SavedState savedState = new SavedState(parcelable);
                    boolean z2 = topAndBottomOffset == 0;
                    savedState.fullyExpanded = z2;
                    savedState.fullyScrolled = !z2 && (-topAndBottomOffset) >= t2.getTotalScrollRange();
                    savedState.firstVisibleChildIndex = i3;
                    savedState.firstVisibleChildAtMinimumHeight = bottom == childAt.getMinimumHeight() + t2.getTopInset();
                    savedState.firstVisibleChildPercentageShown = bottom / childAt.getHeight();
                    return savedState;
                }
            }
            return null;
        }

        public void setDragCallback(BaseDragCallback baseDragCallback) {
            this.onDragCallback = baseDragCallback;
        }

        @Override // com.google.android.material.appbar.HeaderBehavior
        public /* bridge */ /* synthetic */ int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, int i5) {
            return setHeaderTopBottomOffset(coordinatorLayout, (AppBarLayout) view, i3, i4, i5);
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public boolean canDragView(T t2) {
            BaseDragCallback baseDragCallback = this.onDragCallback;
            if (baseDragCallback != null) {
                return baseDragCallback.canDrag(t2);
            }
            WeakReference<View> weakReference = this.lastNestedScrollingChildRef;
            if (weakReference == null) {
                return true;
            }
            View view = weakReference.get();
            return (view == null || !view.isShown() || view.canScrollVertically(-1)) ? false : true;
        }

        public int getMaxDragOffset(T t2) {
            return (-t2.getDownNestedScrollRange()) + t2.getTopInset();
        }

        public int getScrollRangeForDragFling(T t2) {
            return t2.getTotalScrollRange();
        }

        public void onFlingFinished(CoordinatorLayout coordinatorLayout, T t2) {
            snapToChildIfNeeded(coordinatorLayout, t2);
            if (t2.isLiftOnScroll()) {
                t2.setLiftedState(t2.shouldLift(findFirstScrollingChild(coordinatorLayout)));
            }
        }

        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, T t2, int i3) {
            boolean zOnLayoutChild = super.onLayoutChild(coordinatorLayout, t2, i3);
            int pendingAction = t2.getPendingAction();
            SavedState savedState = this.savedState;
            if (savedState == null || (pendingAction & 8) != 0) {
                if (pendingAction != 0) {
                    boolean z2 = (pendingAction & 4) != 0;
                    if ((pendingAction & 2) != 0) {
                        int i4 = -t2.getUpNestedPreScrollRange();
                        if (z2) {
                            animateOffsetTo(coordinatorLayout, t2, i4, 0.0f);
                        } else {
                            setHeaderTopBottomOffset(coordinatorLayout, t2, i4);
                        }
                    } else if ((pendingAction & 1) != 0) {
                        if (z2) {
                            animateOffsetTo(coordinatorLayout, t2, 0, 0.0f);
                        } else {
                            setHeaderTopBottomOffset(coordinatorLayout, t2, 0);
                        }
                    }
                }
            } else if (savedState.fullyScrolled) {
                setHeaderTopBottomOffset(coordinatorLayout, t2, -t2.getTotalScrollRange());
            } else if (savedState.fullyExpanded) {
                setHeaderTopBottomOffset(coordinatorLayout, t2, 0);
            } else {
                View childAt = t2.getChildAt(savedState.firstVisibleChildIndex);
                setHeaderTopBottomOffset(coordinatorLayout, t2, (-childAt.getBottom()) + (this.savedState.firstVisibleChildAtMinimumHeight ? childAt.getMinimumHeight() + t2.getTopInset() : Math.round(childAt.getHeight() * this.savedState.firstVisibleChildPercentageShown)));
            }
            t2.resetPendingAction();
            this.savedState = null;
            setTopAndBottomOffset(MathUtils.clamp(getTopAndBottomOffset(), -t2.getTotalScrollRange(), 0));
            updateAppBarLayoutDrawableState(coordinatorLayout, t2, getTopAndBottomOffset(), 0, true);
            t2.onOffsetChanged(getTopAndBottomOffset());
            addAccessibilityDelegateIfNeeded(coordinatorLayout, t2);
            return zOnLayoutChild;
        }

        public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4, int i5, int i6) {
            if (((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.LayoutParams) t2.getLayoutParams())).height != -2) {
                return super.onMeasureChild(coordinatorLayout, t2, i3, i4, i5, i6);
            }
            coordinatorLayout.onMeasureChild(t2, i3, i4, View.MeasureSpec.makeMeasureSpec(0, 0), i6);
            return true;
        }

        /* JADX WARN: Code duplicated, block: B:9:0x0026  */
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, T t2, View view, int i3, int i4, int[] iArr, int i5) {
            T t3;
            int i6;
            int downNestedPreScrollRange;
            if (i4 == 0) {
                t3 = t2;
            } else {
                if (i4 < 0) {
                    i6 = -t2.getTotalScrollRange();
                    downNestedPreScrollRange = t2.getDownNestedPreScrollRange() + i6;
                } else {
                    i6 = -t2.getUpNestedPreScrollRange();
                    downNestedPreScrollRange = 0;
                }
                int i7 = i6;
                int i8 = downNestedPreScrollRange;
                if (i7 != i8) {
                    t3 = t2;
                    iArr[1] = scroll(coordinatorLayout, t3, i4, i7, i8);
                } else {
                    t3 = t2;
                }
            }
            if (t3.isLiftOnScroll()) {
                t3.setLiftedState(t3.shouldLift(view));
            }
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, T t2, View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            BaseBehavior<T> baseBehavior;
            CoordinatorLayout coordinatorLayout2;
            T t3;
            int i8;
            if (i6 < 0) {
                baseBehavior = this;
                coordinatorLayout2 = coordinatorLayout;
                t3 = t2;
                i8 = i6;
                iArr[1] = baseBehavior.scroll(coordinatorLayout2, t3, i8, -t2.getDownNestedScrollRange(), 0);
            } else {
                baseBehavior = this;
                coordinatorLayout2 = coordinatorLayout;
                t3 = t2;
                i8 = i6;
            }
            if (i8 == 0) {
                baseBehavior.addAccessibilityDelegateIfNeeded(coordinatorLayout2, t3);
            }
        }

        public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, T t2, Parcelable parcelable) {
            if (parcelable instanceof SavedState) {
                restoreScrollState((SavedState) parcelable, true);
                super.onRestoreInstanceState(coordinatorLayout, t2, this.savedState.getSuperState());
            } else {
                super.onRestoreInstanceState(coordinatorLayout, t2, parcelable);
                this.savedState = null;
            }
        }

        public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, T t2) {
            Parcelable parcelableOnSaveInstanceState = super.onSaveInstanceState(coordinatorLayout, t2);
            SavedState savedStateSaveScrollState = saveScrollState(parcelableOnSaveInstanceState, t2);
            return savedStateSaveScrollState == null ? parcelableOnSaveInstanceState : savedStateSaveScrollState;
        }

        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, T t2, View view, View view2, int i3, int i4) {
            ValueAnimator valueAnimator;
            boolean z2 = (i3 & 2) != 0 && (t2.isLiftOnScroll() || t2.isLifted() || canScrollChildren(coordinatorLayout, t2, view));
            if (z2 && (valueAnimator = this.offsetAnimator) != null) {
                valueAnimator.cancel();
            }
            this.lastNestedScrollingChildRef = null;
            this.lastStartedType = i4;
            return z2;
        }

        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, T t2, View view, int i3) {
            if (this.lastStartedType == 0 || i3 == 1) {
                snapToChildIfNeeded(coordinatorLayout, t2);
                if (t2.isLiftOnScroll()) {
                    t2.setLiftedState(t2.shouldLift(view));
                }
            }
            this.lastNestedScrollingChildRef = new WeakReference<>(view);
        }

        public int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4, int i5) {
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling();
            int i6 = 0;
            if (i4 == 0 || topBottomOffsetForScrollingSibling < i4 || topBottomOffsetForScrollingSibling > i5) {
                this.offsetDelta = 0;
            } else {
                int iClamp = MathUtils.clamp(i3, i4, i5);
                if (topBottomOffsetForScrollingSibling != iClamp) {
                    int iInterpolateOffset = t2.hasChildWithInterpolator() ? interpolateOffset(t2, iClamp) : iClamp;
                    boolean topAndBottomOffset = setTopAndBottomOffset(iInterpolateOffset);
                    int i7 = topBottomOffsetForScrollingSibling - iClamp;
                    this.offsetDelta = iClamp - iInterpolateOffset;
                    if (topAndBottomOffset) {
                        while (i6 < t2.getChildCount()) {
                            LayoutParams layoutParams = (LayoutParams) t2.getChildAt(i6).getLayoutParams();
                            ChildScrollEffect scrollEffect = layoutParams.getScrollEffect();
                            if (scrollEffect != null && (layoutParams.getScrollFlags() & 1) != 0) {
                                scrollEffect.onOffsetChanged(t2, t2.getChildAt(i6), getTopAndBottomOffset());
                            }
                            i6++;
                        }
                    }
                    if (!topAndBottomOffset && t2.hasChildWithInterpolator()) {
                        coordinatorLayout.dispatchDependentViewsChanged(t2);
                    }
                    t2.onOffsetChanged(getTopAndBottomOffset());
                    updateAppBarLayoutDrawableState(coordinatorLayout, t2, iClamp, iClamp < topBottomOffsetForScrollingSibling ? -1 : 1, false);
                    i6 = i7;
                }
            }
            addAccessibilityDelegateIfNeeded(coordinatorLayout, t2);
            return i6;
        }

        public static class SavedState extends AbsSavedState {
            public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
            boolean firstVisibleChildAtMinimumHeight;
            int firstVisibleChildIndex;
            float firstVisibleChildPercentageShown;
            boolean fullyExpanded;
            boolean fullyScrolled;

            /* JADX INFO: renamed from: com.google.android.material.appbar.AppBarLayout$BaseBehavior$SavedState$1, reason: invalid class name */
            public class AnonymousClass1 implements Parcelable.ClassLoaderCreator<SavedState> {
                @Override // android.os.Parcelable.Creator
                public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
                    return createFromParcel(parcel);
                }

                @Override // android.os.Parcelable.Creator
                public /* bridge */ /* synthetic */ Object[] newArray(int i3) {
                    return newArray(i3);
                }

                @Override // android.os.Parcelable.ClassLoaderCreator
                public /* bridge */ /* synthetic */ SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return createFromParcel(parcel, classLoader);
                }

                @Override // android.os.Parcelable.Creator
                public SavedState[] newArray(int i3) {
                    return new SavedState[i3];
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.ClassLoaderCreator
                public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }

                @Override // android.os.Parcelable.Creator
                public SavedState createFromParcel(Parcel parcel) {
                    return new SavedState(parcel, null);
                }
            }

            public SavedState(Parcel parcel, ClassLoader classLoader) {
                super(parcel, classLoader);
                this.fullyScrolled = parcel.readByte() != 0;
                this.fullyExpanded = parcel.readByte() != 0;
                this.firstVisibleChildIndex = parcel.readInt();
                this.firstVisibleChildPercentageShown = parcel.readFloat();
                this.firstVisibleChildAtMinimumHeight = parcel.readByte() != 0;
            }

            @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i3) {
                super.writeToParcel(parcel, i3);
                parcel.writeByte(this.fullyScrolled ? (byte) 1 : (byte) 0);
                parcel.writeByte(this.fullyExpanded ? (byte) 1 : (byte) 0);
                parcel.writeInt(this.firstVisibleChildIndex);
                parcel.writeFloat(this.firstVisibleChildPercentageShown);
                parcel.writeByte(this.firstVisibleChildAtMinimumHeight ? (byte) 1 : (byte) 0);
            }

            public SavedState(Parcelable parcelable) {
                super(parcelable);
            }
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
    public interface BaseOnOffsetChangedListener<T extends AppBarLayout> {
        void onOffsetChanged(T t2, int i3);
    }

    /* JADX INFO: loaded from: classes8.dex */
    public static class Behavior extends BaseBehavior<AppBarLayout> {

        public static abstract class DragCallback extends BaseBehavior.BaseDragCallback<AppBarLayout> {
        }

        public Behavior() {
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getLeftAndRightOffset() {
            return super.getLeftAndRightOffset();
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isHorizontalOffsetEnabled() {
            return super.isHorizontalOffsetEnabled();
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isVerticalOffsetEnabled() {
            return super.isVerticalOffsetEnabled();
        }

        @Override // com.google.android.material.appbar.HeaderBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i3) {
            return super.onLayoutChild(coordinatorLayout, appBarLayout, i3);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i3, int i4, int i5, int i6) {
            return super.onMeasureChild(coordinatorLayout, appBarLayout, i3, i4, i5, i6);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i3, int i4, int[] iArr, int i5) {
            super.onNestedPreScroll(coordinatorLayout, appBarLayout, view, i3, i4, iArr, i5);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            super.onNestedScroll(coordinatorLayout, appBarLayout, view, i3, i4, i5, i6, i7, iArr);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, Parcelable parcelable) {
            super.onRestoreInstanceState(coordinatorLayout, appBarLayout, parcelable);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
            return super.onSaveInstanceState(coordinatorLayout, appBarLayout);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i3, int i4) {
            return super.onStartNestedScroll(coordinatorLayout, appBarLayout, view, view2, i3, i4);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i3) {
            super.onStopNestedScroll(coordinatorLayout, appBarLayout, view, i3);
        }

        @Override // com.google.android.material.appbar.HeaderBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            return super.onTouchEvent(coordinatorLayout, view, motionEvent);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void setDragCallback(BaseBehavior.BaseDragCallback baseDragCallback) {
            super.setDragCallback(baseDragCallback);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setHorizontalOffsetEnabled(boolean z2) {
            super.setHorizontalOffsetEnabled(z2);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean setLeftAndRightOffset(int i3) {
            return super.setLeftAndRightOffset(i3);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i3) {
            return super.setTopAndBottomOffset(i3);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setVerticalOffsetEnabled(boolean z2) {
            super.setVerticalOffsetEnabled(z2);
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }

    public static abstract class ChildScrollEffect {
        public abstract void onOffsetChanged(AppBarLayout appBarLayout, View view, float f3);
    }

    /* JADX INFO: loaded from: classes8.dex */
    public static class CompressChildScrollEffect extends ChildScrollEffect {
        private static final float COMPRESS_DISTANCE_FACTOR = 0.3f;
        private final Rect relativeRect = new Rect();
        private final Rect ghostRect = new Rect();

        private static void updateRelativeRect(Rect rect, AppBarLayout appBarLayout, View view) {
            view.getDrawingRect(rect);
            appBarLayout.offsetDescendantRectToMyCoords(view, rect);
            rect.offset(0, -appBarLayout.getTopInset());
        }

        @Override // com.google.android.material.appbar.AppBarLayout.ChildScrollEffect
        public void onOffsetChanged(AppBarLayout appBarLayout, View view, float f3) {
            updateRelativeRect(this.relativeRect, appBarLayout, view);
            float fAbs = this.relativeRect.top - Math.abs(f3);
            if (fAbs > 0.0f) {
                view.setClipBounds(null);
                view.setTranslationY(0.0f);
                view.setAlpha(1.0f);
                return;
            }
            float fClamp = 1.0f - MathUtils.clamp(Math.abs(fAbs / this.relativeRect.height()), 0.0f, 1.0f);
            float fHeight = (-fAbs) - ((this.relativeRect.height() * 0.3f) * (1.0f - (fClamp * fClamp)));
            view.setTranslationY(fHeight);
            view.getDrawingRect(this.ghostRect);
            this.ghostRect.offset(0, (int) (-fHeight));
            if (fHeight >= this.ghostRect.height()) {
                view.setAlpha(0.0f);
            } else {
                view.setAlpha(1.0f);
            }
            view.setClipBounds(this.ghostRect);
        }
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        static final int COLLAPSIBLE_FLAGS = 10;
        static final int FLAG_QUICK_RETURN = 5;
        static final int FLAG_SNAP = 17;
        public static final int SCROLL_EFFECT_COMPRESS = 1;
        public static final int SCROLL_EFFECT_NONE = 0;
        public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
        public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
        public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
        public static final int SCROLL_FLAG_NO_SCROLL = 0;
        public static final int SCROLL_FLAG_SCROLL = 1;
        public static final int SCROLL_FLAG_SNAP = 16;
        public static final int SCROLL_FLAG_SNAP_MARGINS = 32;
        private ChildScrollEffect scrollEffect;
        int scrollFlags;
        Interpolator scrollInterpolator;

        /* JADX INFO: loaded from: classes8.dex */
        @Retention(RetentionPolicy.SOURCE)
        public @interface ScrollEffect {
        }

        /* JADX INFO: loaded from: classes8.dex */
        @Retention(RetentionPolicy.SOURCE)
        public @interface ScrollFlags {
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.scrollFlags = 1;
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AppBarLayout_Layout);
            this.scrollFlags = typedArrayObtainStyledAttributes.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
            setScrollEffect(typedArrayObtainStyledAttributes.getInt(R.styleable.AppBarLayout_Layout_layout_scrollEffect, 0));
            int i3 = R.styleable.AppBarLayout_Layout_layout_scrollInterpolator;
            if (typedArrayObtainStyledAttributes.hasValue(i3)) {
                this.scrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(context, typedArrayObtainStyledAttributes.getResourceId(i3, 0));
            }
            typedArrayObtainStyledAttributes.recycle();
        }

        private ChildScrollEffect createScrollEffectFromInt(int i3) {
            if (i3 != 1) {
                return null;
            }
            return new CompressChildScrollEffect();
        }

        public ChildScrollEffect getScrollEffect() {
            return this.scrollEffect;
        }

        public int getScrollFlags() {
            return this.scrollFlags;
        }

        public Interpolator getScrollInterpolator() {
            return this.scrollInterpolator;
        }

        public boolean isCollapsible() {
            int i3 = this.scrollFlags;
            return (i3 & 1) == 1 && (i3 & 10) != 0;
        }

        public void setScrollEffect(ChildScrollEffect childScrollEffect) {
            this.scrollEffect = childScrollEffect;
        }

        public void setScrollFlags(int i3) {
            this.scrollFlags = i3;
        }

        public void setScrollInterpolator(Interpolator interpolator) {
            this.scrollInterpolator = interpolator;
        }

        public void setScrollEffect(int i3) {
            this.scrollEffect = createScrollEffectFromInt(i3);
        }

        public LayoutParams(int i3, int i4) {
            super(i3, i4);
            this.scrollFlags = 1;
        }

        public LayoutParams(int i3, int i4, float f3) {
            super(i3, i4, f3);
            this.scrollFlags = 1;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.scrollFlags = 1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.scrollFlags = 1;
        }

        public LayoutParams(LinearLayout.LayoutParams layoutParams) {
            super(layoutParams);
            this.scrollFlags = 1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((LinearLayout.LayoutParams) layoutParams);
            this.scrollFlags = 1;
            this.scrollFlags = layoutParams.scrollFlags;
            this.scrollEffect = layoutParams.scrollEffect;
            this.scrollInterpolator = layoutParams.scrollInterpolator;
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
    @Deprecated
    public interface LiftOnScrollListener {
        void onUpdate(float f3, int i3);
    }

    /* JADX INFO: loaded from: classes8.dex */
    public static abstract class LiftOnScrollProgressListener {
        public abstract void onUpdate(float f3, int i3, float f4);
    }

    /* JADX INFO: loaded from: classes8.dex */
    public interface OnOffsetChangedListener extends BaseOnOffsetChangedListener<AppBarLayout> {
        @Override // com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
        void onOffsetChanged(AppBarLayout appBarLayout, int i3);
    }

    /* JADX INFO: loaded from: classes8.dex */
    public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
        public ScrollingViewBehavior() {
        }

        private static int getAppBarLayoutOffset(AppBarLayout appBarLayout) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
            if (behavior instanceof BaseBehavior) {
                return ((BaseBehavior) behavior).getTopBottomOffsetForScrollingSibling();
            }
            return 0;
        }

        private void offsetChildAsNeeded(View view, View view2) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) view2.getLayoutParams()).getBehavior();
            if (behavior instanceof BaseBehavior) {
                ViewCompat.offsetTopAndBottom(view, (((view2.getBottom() - view.getTop()) + BaseBehavior.access$200((BaseBehavior) behavior)) + getVerticalLayoutGap()) - getOverlapPixelsForOffset(view2));
            }
        }

        private void updateLiftedStateIfNeeded(View view, View view2) {
            if (view2 instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) view2;
                if (appBarLayout.isLiftOnScroll()) {
                    appBarLayout.setLiftedState(appBarLayout.shouldLift(view));
                }
            }
        }

        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior
        public /* bridge */ /* synthetic */ View findFirstDependency(List list) {
            return findFirstDependency((List<View>) list);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getLeftAndRightOffset() {
            return super.getLeftAndRightOffset();
        }

        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior
        public float getOverlapRatioForOffset(View view) {
            int i3;
            if (view instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) view;
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                int downNestedPreScrollRange = appBarLayout.getDownNestedPreScrollRange();
                int appBarLayoutOffset = getAppBarLayoutOffset(appBarLayout);
                if ((downNestedPreScrollRange == 0 || totalScrollRange + appBarLayoutOffset > downNestedPreScrollRange) && (i3 = totalScrollRange - downNestedPreScrollRange) != 0) {
                    return (appBarLayoutOffset / i3) + 1.0f;
                }
            }
            return 0.0f;
        }

        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior
        public int getScrollRange(View view) {
            return view instanceof AppBarLayout ? ((AppBarLayout) view).getTotalScrollRange() : super.getScrollRange(view);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isHorizontalOffsetEnabled() {
            return super.isHorizontalOffsetEnabled();
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isVerticalOffsetEnabled() {
            return super.isVerticalOffsetEnabled();
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
            return view2 instanceof AppBarLayout;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
            offsetChildAsNeeded(view, view2);
            updateLiftedStateIfNeeded(view, view2);
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, View view, View view2) {
            if (view2 instanceof AppBarLayout) {
                ViewCompat.setAccessibilityDelegate(coordinatorLayout, null);
            }
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i3) {
            return super.onLayoutChild(coordinatorLayout, view, i3);
        }

        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, int i5, int i6) {
            return super.onMeasureChild(coordinatorLayout, view, i3, i4, i5, i6);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onRequestChildRectangleOnScreen(CoordinatorLayout coordinatorLayout, View view, Rect rect, boolean z2) {
            AppBarLayout appBarLayoutFindFirstDependency = findFirstDependency(coordinatorLayout.getDependencies(view));
            if (appBarLayoutFindFirstDependency != null) {
                Rect rect2 = new Rect(rect);
                rect2.offset(view.getLeft(), view.getTop());
                Rect rect3 = this.tempRect1;
                rect3.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());
                if (!rect3.contains(rect2)) {
                    appBarLayoutFindFirstDependency.setExpanded(false, !z2);
                    return true;
                }
            }
            return false;
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setHorizontalOffsetEnabled(boolean z2) {
            super.setHorizontalOffsetEnabled(z2);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean setLeftAndRightOffset(int i3) {
            return super.setLeftAndRightOffset(i3);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i3) {
            return super.setTopAndBottomOffset(i3);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setVerticalOffsetEnabled(boolean z2) {
            super.setVerticalOffsetEnabled(z2);
        }

        public ScrollingViewBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ScrollingViewBehavior_Layout);
            setOverlayTop(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
            typedArrayObtainStyledAttributes.recycle();
        }

        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior
        public AppBarLayout findFirstDependency(List<View> list) {
            int size = list.size();
            for (int i3 = 0; i3 < size; i3++) {
                View view = list.get(i3);
                if (view instanceof AppBarLayout) {
                    return (AppBarLayout) view;
                }
            }
            return null;
        }
    }

    public AppBarLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(AppBarLayout appBarLayout, MaterialShapeDrawable materialShapeDrawable, ValueAnimator valueAnimator) {
        appBarLayout.getClass();
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        materialShapeDrawable.setElevation(fFloatValue);
        Drawable drawable = appBarLayout.statusBarForeground;
        if (drawable instanceof MaterialShapeDrawable) {
            ((MaterialShapeDrawable) drawable).setElevation(fFloatValue);
        }
        Iterator<LiftOnScrollListener> it = appBarLayout.liftOnScrollListeners.iterator();
        while (it.hasNext()) {
            it.next().onUpdate(fFloatValue, materialShapeDrawable.getResolvedTintColor());
        }
        Iterator<LiftOnScrollProgressListener> it2 = appBarLayout.liftProgressListeners.iterator();
        while (it2.hasNext()) {
            it2.next().onUpdate(fFloatValue, materialShapeDrawable.getResolvedTintColor(), fFloatValue / appBarLayout.appBarElevation);
        }
    }

    public static /* synthetic */ void b(AppBarLayout appBarLayout, ColorStateList colorStateList, MaterialShapeDrawable materialShapeDrawable, Integer num, ValueAnimator valueAnimator) {
        Integer num2;
        appBarLayout.getClass();
        float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        int iLayer = MaterialColors.layer(appBarLayout.backgroundOriginalColor, colorStateList.getDefaultColor(), fFloatValue);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(iLayer));
        if (appBarLayout.statusBarForeground != null && (num2 = appBarLayout.statusBarForegroundOriginalColor) != null && num2.equals(num)) {
            appBarLayout.statusBarForeground.setTint(iLayer);
        }
        if (!appBarLayout.liftOnScrollListeners.isEmpty()) {
            for (LiftOnScrollListener liftOnScrollListener : appBarLayout.liftOnScrollListeners) {
                if (materialShapeDrawable.getFillColor() != null) {
                    liftOnScrollListener.onUpdate(0.0f, iLayer);
                }
            }
        }
        if (appBarLayout.liftProgressListeners.isEmpty()) {
            return;
        }
        Iterator<LiftOnScrollProgressListener> it = appBarLayout.liftProgressListeners.iterator();
        while (it.hasNext()) {
            it.next().onUpdate(0.0f, iLayer, fFloatValue);
        }
    }

    private void clearLiftOnScrollTargetView() {
        WeakReference<View> weakReference = this.liftOnScrollTargetView;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.liftOnScrollTargetView = null;
    }

    private Integer extractStatusBarForegroundColor() {
        Drawable drawable = this.statusBarForeground;
        if (drawable instanceof MaterialShapeDrawable) {
            return Integer.valueOf(((MaterialShapeDrawable) drawable).getResolvedTintColor());
        }
        ColorStateList colorStateListOrNull = DrawableUtils.getColorStateListOrNull(drawable);
        if (colorStateListOrNull != null) {
            return Integer.valueOf(colorStateListOrNull.getDefaultColor());
        }
        return null;
    }

    private View findLiftOnScrollTargetView(View view) {
        int i3;
        if (this.liftOnScrollTargetView == null && (i3 = this.liftOnScrollTargetViewId) != -1) {
            View viewFindViewById = view != null ? view.findViewById(i3) : null;
            if (viewFindViewById == null && (getParent() instanceof ViewGroup)) {
                viewFindViewById = ((ViewGroup) getParent()).findViewById(this.liftOnScrollTargetViewId);
            }
            if (viewFindViewById != null) {
                this.liftOnScrollTargetView = new WeakReference<>(viewFindViewById);
            }
        }
        WeakReference<View> weakReference = this.liftOnScrollTargetView;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    private boolean hasCollapsibleChild() {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            if (((LayoutParams) getChildAt(i3).getLayoutParams()).isCollapsible()) {
                return true;
            }
        }
        return false;
    }

    private void initializeLiftOnScrollWithColor(MaterialShapeDrawable materialShapeDrawable, ColorStateList colorStateList) {
        this.liftOnScrollColorUpdateListener = new a(this, colorStateList, materialShapeDrawable, MaterialColors.getColorOrNull(getContext(), R.attr.colorSurface));
    }

    private void initializeLiftOnScrollWithElevation(Context context, MaterialShapeDrawable materialShapeDrawable) {
        materialShapeDrawable.initializeElevationOverlay(context);
        this.liftOnScrollColorUpdateListener = new b(this, materialShapeDrawable);
    }

    private void invalidateScrollRanges() {
        Behavior behavior = this.behavior;
        BaseBehavior.SavedState savedStateSaveScrollState = (behavior == null || this.totalScrollRange == -1 || this.pendingAction != 0) ? null : behavior.saveScrollState(AbsSavedState.EMPTY_STATE, this);
        this.totalScrollRange = -1;
        this.downPreScrollRange = -1;
        this.downScrollRange = -1;
        if (savedStateSaveScrollState != null) {
            this.behavior.restoreScrollState(savedStateSaveScrollState, false);
        }
    }

    private boolean isLiftOnScrollCompatibleBackground() {
        return getBackground() instanceof MaterialShapeDrawable;
    }

    private MaterialShapeDrawable maybeConvertToMaterialShapeDrawable(Drawable drawable) {
        if (drawable instanceof MaterialShapeDrawable) {
            return (MaterialShapeDrawable) drawable;
        }
        ColorStateList colorStateListOrNull = DrawableUtils.getColorStateListOrNull(drawable);
        if (colorStateListOrNull == null) {
            return null;
        }
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        materialShapeDrawable.setFillColor(colorStateListOrNull);
        return materialShapeDrawable;
    }

    private Drawable maybeCreateLiftOnScrollBackground(Context context, Drawable drawable) {
        MaterialShapeDrawable materialShapeDrawableMaybeConvertToMaterialShapeDrawable = maybeConvertToMaterialShapeDrawable(drawable);
        if (materialShapeDrawableMaybeConvertToMaterialShapeDrawable == null || materialShapeDrawableMaybeConvertToMaterialShapeDrawable.getFillColor() == null) {
            return drawable;
        }
        this.backgroundOriginalColor = materialShapeDrawableMaybeConvertToMaterialShapeDrawable.getFillColor().getDefaultColor();
        ColorStateList colorStateList = this.liftOnScrollColor;
        if (colorStateList != null) {
            initializeLiftOnScrollWithColor(materialShapeDrawableMaybeConvertToMaterialShapeDrawable, colorStateList);
            return materialShapeDrawableMaybeConvertToMaterialShapeDrawable;
        }
        initializeLiftOnScrollWithElevation(context, materialShapeDrawableMaybeConvertToMaterialShapeDrawable);
        return materialShapeDrawableMaybeConvertToMaterialShapeDrawable;
    }

    private boolean setLiftableState(boolean z2) {
        if (this.liftable == z2) {
            return false;
        }
        this.liftable = z2;
        refreshDrawableState();
        return true;
    }

    private boolean shouldDrawStatusBarForeground() {
        return this.statusBarForeground != null && getTopInset() > 0;
    }

    private boolean shouldOffsetFirstChild() {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            if (childAt.getVisibility() != 8 && !childAt.getFitsSystemWindows()) {
                return true;
            }
        }
        return false;
    }

    private void startLiftOnScrollColorAnimation(float f3, float f4) {
        ValueAnimator valueAnimator = this.liftOnScrollColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(f3, f4);
        this.liftOnScrollColorAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(this.liftOnScrollColorDuration);
        this.liftOnScrollColorAnimator.setInterpolator(this.liftOnScrollColorInterpolator);
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener = this.liftOnScrollColorUpdateListener;
        if (animatorUpdateListener != null) {
            this.liftOnScrollColorAnimator.addUpdateListener(animatorUpdateListener);
        }
        this.liftOnScrollColorAnimator.start();
    }

    private void updateWillNotDraw() {
        setWillNotDraw(!shouldDrawStatusBarForeground());
    }

    @Deprecated
    public void addLiftOnScrollListener(LiftOnScrollListener liftOnScrollListener) {
        this.liftOnScrollListeners.add(liftOnScrollListener);
    }

    public void addLiftOnScrollProgressListener(LiftOnScrollProgressListener liftOnScrollProgressListener) {
        this.liftProgressListeners.add(liftOnScrollProgressListener);
    }

    public void addOnOffsetChangedListener(BaseOnOffsetChangedListener baseOnOffsetChangedListener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList();
        }
        if (baseOnOffsetChangedListener == null || this.listeners.contains(baseOnOffsetChangedListener)) {
            return;
        }
        this.listeners.add(baseOnOffsetChangedListener);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Deprecated
    public void clearLiftOnScrollListener() {
        this.liftOnScrollListeners.clear();
    }

    public void clearLiftOnScrollProgressListener() {
        this.liftProgressListeners.clear();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (shouldDrawStatusBarForeground()) {
            int iSave = canvas.save();
            canvas.translate(0.0f, -this.currentOffset);
            this.statusBarForeground.draw(canvas);
            canvas.restoreToCount(iSave);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.statusBarForeground;
        if (drawable != null && drawable.isStateful() && drawable.setState(drawableState)) {
            invalidateDrawable(drawable);
        }
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return generateDefaultLayoutParams();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateLayoutParams(attributeSet);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    public CoordinatorLayout.Behavior<AppBarLayout> getBehavior() {
        Behavior behavior = new Behavior();
        this.behavior = behavior;
        return behavior;
    }

    public int getDownNestedPreScrollRange() {
        int iMin;
        int minimumHeight;
        int i3 = this.downPreScrollRange;
        if (i3 != -1) {
            return i3;
        }
        int i4 = 0;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredHeight = childAt.getMeasuredHeight();
                int i5 = layoutParams.scrollFlags;
                if ((i5 & 5) != 5) {
                    if (i4 > 0) {
                        break;
                    }
                } else {
                    int i6 = ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                    if ((i5 & 8) != 0) {
                        minimumHeight = childAt.getMinimumHeight();
                    } else {
                        if ((i5 & 2) != 0) {
                            minimumHeight = measuredHeight - childAt.getMinimumHeight();
                        } else {
                            iMin = i6 + measuredHeight;
                        }
                        if (childCount == 0 && childAt.getFitsSystemWindows()) {
                            iMin = Math.min(iMin, measuredHeight - getTopInset());
                        }
                        i4 += iMin;
                    }
                    iMin = i6 + minimumHeight;
                    if (childCount == 0) {
                        iMin = Math.min(iMin, measuredHeight - getTopInset());
                    }
                    i4 += iMin;
                }
            }
        }
        int iMax = Math.max(0, i4);
        this.downPreScrollRange = iMax;
        return iMax;
    }

    public int getDownNestedScrollRange() {
        int i3 = this.downScrollRange;
        if (i3 != -1) {
            return i3;
        }
        int childCount = getChildCount();
        int minimumHeight = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredHeight = childAt.getMeasuredHeight() + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                int i5 = layoutParams.scrollFlags;
                if ((i5 & 1) == 0) {
                    break;
                }
                minimumHeight += measuredHeight;
                if ((i5 & 2) != 0) {
                    minimumHeight -= childAt.getMinimumHeight();
                    break;
                }
            }
        }
        int iMax = Math.max(0, minimumHeight);
        this.downScrollRange = iMax;
        return iMax;
    }

    public int getLiftOnScrollTargetViewId() {
        return this.liftOnScrollTargetViewId;
    }

    public MaterialShapeDrawable getMaterialShapeBackground() {
        Drawable background = getBackground();
        if (background instanceof MaterialShapeDrawable) {
            return (MaterialShapeDrawable) background;
        }
        return null;
    }

    public final int getMinimumHeightForVisibleOverlappingContent() {
        int topInset = getTopInset();
        int minimumHeight = getMinimumHeight();
        if (minimumHeight != 0) {
            int i3 = (minimumHeight * 2) + topInset;
            return i3 < getHeight() ? i3 : minimumHeight + topInset;
        }
        int childCount = getChildCount();
        int minimumHeight2 = childCount >= 1 ? getChildAt(childCount - 1).getMinimumHeight() : 0;
        if (minimumHeight2 == 0) {
            return getHeight() / 3;
        }
        int i4 = (minimumHeight2 * 2) + topInset;
        return i4 < getHeight() ? i4 : minimumHeight2 + topInset;
    }

    public int getPendingAction() {
        return this.pendingAction;
    }

    public Drawable getStatusBarForeground() {
        return this.statusBarForeground;
    }

    @Deprecated
    public float getTargetElevation() {
        return 0.0f;
    }

    public final int getTopInset() {
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        if (windowInsetsCompat != null) {
            return windowInsetsCompat.getSystemWindowInsetTop();
        }
        return 0;
    }

    public final int getTotalScrollRange() {
        int i3 = this.totalScrollRange;
        if (i3 != -1) {
            return i3;
        }
        int childCount = getChildCount();
        int minimumHeight = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredHeight = childAt.getMeasuredHeight();
                int i5 = layoutParams.scrollFlags;
                if ((i5 & 1) == 0) {
                    break;
                }
                minimumHeight += measuredHeight + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                if (i4 == 0 && childAt.getFitsSystemWindows()) {
                    minimumHeight -= getTopInset();
                }
                if ((i5 & 2) != 0) {
                    minimumHeight -= childAt.getMinimumHeight();
                    break;
                }
            }
        }
        int iMax = Math.max(0, minimumHeight);
        this.totalScrollRange = iMax;
        return iMax;
    }

    public int getUpNestedPreScrollRange() {
        return getTotalScrollRange();
    }

    public boolean hasChildWithInterpolator() {
        return this.haveChildWithInterpolator;
    }

    public boolean hasScrollableChildren() {
        return getTotalScrollRange() != 0;
    }

    public boolean isLiftOnScroll() {
        return this.liftOnScroll;
    }

    public boolean isLifted() {
        return this.lifted;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    public int[] onCreateDrawableState(int i3) {
        if (this.tmpStatesArray == null) {
            this.tmpStatesArray = new int[4];
        }
        int[] iArr = this.tmpStatesArray;
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i3 + iArr.length);
        boolean z2 = this.liftable;
        int i4 = R.attr.state_liftable;
        if (!z2) {
            i4 = -i4;
        }
        iArr[0] = i4;
        iArr[1] = (z2 && this.lifted) ? R.attr.state_lifted : -R.attr.state_lifted;
        int i5 = R.attr.state_collapsible;
        if (!z2) {
            i5 = -i5;
        }
        iArr[2] = i5;
        iArr[3] = (z2 && this.lifted) ? R.attr.state_collapsed : -R.attr.state_collapsed;
        return View.mergeDrawableStates(iArrOnCreateDrawableState, iArr);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearLiftOnScrollTargetView();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        boolean z3 = true;
        if (getFitsSystemWindows() && shouldOffsetFirstChild()) {
            int topInset = getTopInset();
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                ViewCompat.offsetTopAndBottom(getChildAt(childCount), topInset);
            }
        }
        invalidateScrollRanges();
        this.haveChildWithInterpolator = false;
        int childCount2 = getChildCount();
        for (int i7 = 0; i7 < childCount2; i7++) {
            if (((LayoutParams) getChildAt(i7).getLayoutParams()).getScrollInterpolator() != null) {
                this.haveChildWithInterpolator = true;
                break;
            }
        }
        Drawable drawable = this.statusBarForeground;
        if (drawable != null) {
            drawable.setBounds(0, 0, getWidth(), getTopInset());
        }
        if (this.liftableOverride) {
            return;
        }
        if (!this.liftOnScroll && !hasCollapsibleChild()) {
            z3 = false;
        }
        setLiftableState(z3);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        int mode = View.MeasureSpec.getMode(i4);
        if (mode != 1073741824 && getFitsSystemWindows() && shouldOffsetFirstChild()) {
            int measuredHeight = getMeasuredHeight();
            if (mode == Integer.MIN_VALUE) {
                measuredHeight = MathUtils.clamp(getMeasuredHeight() + getTopInset(), 0, View.MeasureSpec.getSize(i4));
            } else if (mode == 0) {
                measuredHeight += getTopInset();
            }
            setMeasuredDimension(getMeasuredWidth(), measuredHeight);
        }
        invalidateScrollRanges();
    }

    public void onOffsetChanged(int i3) {
        this.currentOffset = i3;
        if (!willNotDraw()) {
            postInvalidateOnAnimation();
        }
        List<BaseOnOffsetChangedListener> list = this.listeners;
        if (list != null) {
            int size = list.size();
            for (int i4 = 0; i4 < size; i4++) {
                BaseOnOffsetChangedListener baseOnOffsetChangedListener = this.listeners.get(i4);
                if (baseOnOffsetChangedListener != null) {
                    baseOnOffsetChangedListener.onOffsetChanged(this, i3);
                }
            }
        }
    }

    public WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat windowInsetsCompat) {
        WindowInsetsCompat windowInsetsCompat2 = getFitsSystemWindows() ? windowInsetsCompat : null;
        if (!ObjectsCompat.equals(this.lastInsets, windowInsetsCompat2)) {
            this.lastInsets = windowInsetsCompat2;
            updateWillNotDraw();
            requestLayout();
        }
        return windowInsetsCompat;
    }

    @Deprecated
    public boolean removeLiftOnScrollListener(LiftOnScrollListener liftOnScrollListener) {
        return this.liftOnScrollListeners.remove(liftOnScrollListener);
    }

    public boolean removeLiftOnScrollProgressListener(LiftOnScrollProgressListener liftOnScrollProgressListener) {
        return this.liftProgressListeners.remove(liftOnScrollProgressListener);
    }

    public void removeOnOffsetChangedListener(BaseOnOffsetChangedListener baseOnOffsetChangedListener) {
        List<BaseOnOffsetChangedListener> list = this.listeners;
        if (list == null || baseOnOffsetChangedListener == null) {
            return;
        }
        list.remove(baseOnOffsetChangedListener);
    }

    public void resetPendingAction() {
        this.pendingAction = 0;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        super.setBackground(maybeCreateLiftOnScrollBackground(getContext(), drawable));
    }

    @Override // android.view.View
    public void setElevation(float f3) {
        super.setElevation(f3);
        MaterialShapeUtils.setElevation(this, f3);
    }

    public void setExpanded(boolean z2) {
        setExpanded(z2, isLaidOut());
    }

    public void setLiftOnScroll(boolean z2) {
        this.liftOnScroll = z2;
    }

    public void setLiftOnScrollColor(ColorStateList colorStateList) {
        if (this.liftOnScrollColor != colorStateList) {
            this.liftOnScrollColor = colorStateList;
            setBackground(getBackground());
        }
    }

    public void setLiftOnScrollTargetView(View view) {
        this.liftOnScrollTargetViewId = -1;
        if (view == null) {
            clearLiftOnScrollTargetView();
        } else {
            this.liftOnScrollTargetView = new WeakReference<>(view);
        }
    }

    public void setLiftOnScrollTargetViewId(int i3) {
        this.liftOnScrollTargetViewId = i3;
        clearLiftOnScrollTargetView();
    }

    public boolean setLiftable(boolean z2) {
        this.liftableOverride = true;
        return setLiftableState(z2);
    }

    public void setLiftableOverrideEnabled(boolean z2) {
        this.liftableOverride = z2;
    }

    public boolean setLifted(boolean z2) {
        return setLiftedState(z2, true);
    }

    public boolean setLiftedState(boolean z2) {
        return setLiftedState(z2, !this.liftableOverride);
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i3) {
        if (i3 != 1) {
            throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
        }
        super.setOrientation(i3);
    }

    public void setPendingAction(int i3) {
        this.pendingAction = i3;
    }

    public void setStatusBarForeground(Drawable drawable) {
        Drawable drawable2 = this.statusBarForeground;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            this.statusBarForeground = drawable != null ? drawable.mutate() : null;
            this.statusBarForegroundOriginalColor = extractStatusBarForegroundColor();
            Drawable drawable3 = this.statusBarForeground;
            if (drawable3 != null) {
                if (drawable3.isStateful()) {
                    this.statusBarForeground.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.statusBarForeground, getLayoutDirection());
                this.statusBarForeground.setVisible(getVisibility() == 0, false);
                this.statusBarForeground.setCallback(this);
            }
            updateWillNotDraw();
            postInvalidateOnAnimation();
        }
    }

    public void setStatusBarForegroundColor(int i3) {
        setStatusBarForeground(new ColorDrawable(i3));
    }

    public void setStatusBarForegroundResource(int i3) {
        setStatusBarForeground(AppCompatResources.getDrawable(getContext(), i3));
    }

    @Deprecated
    public void setTargetElevation(float f3) {
        ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, f3);
    }

    @Override // android.view.View
    public void setVisibility(int i3) {
        super.setVisibility(i3);
        boolean z2 = i3 == 0;
        Drawable drawable = this.statusBarForeground;
        if (drawable != null) {
            drawable.setVisible(z2, false);
        }
    }

    public boolean shouldLift(View view) {
        View viewFindLiftOnScrollTargetView = findLiftOnScrollTargetView(view);
        if (viewFindLiftOnScrollTargetView != null) {
            view = viewFindLiftOnScrollTargetView;
        }
        if (view != null) {
            return view.canScrollVertically(-1) || view.getScrollY() > 0;
        }
        return false;
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.statusBarForeground;
    }

    public AppBarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.appBarLayoutStyle);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return generateDefaultLayoutParams();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return generateLayoutParams(layoutParams);
    }

    public void setExpanded(boolean z2, boolean z3) {
        setExpanded(z2, z3, true);
    }

    public boolean setLiftedState(boolean z2, boolean z3) {
        if (!z3 || this.lifted == z2) {
            return false;
        }
        this.lifted = z2;
        refreshDrawableState();
        if (!isLiftOnScrollCompatibleBackground()) {
            return true;
        }
        if (this.liftOnScrollColor != null) {
            startLiftOnScrollColorAnimation(z2 ? 0.0f : 1.0f, z2 ? 1.0f : 0.0f);
            return true;
        }
        if (!this.liftOnScroll) {
            return true;
        }
        startLiftOnScrollColorAnimation(z2 ? 0.0f : this.appBarElevation, z2 ? this.appBarElevation : 0.0f);
        return true;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public AppBarLayout(Context context, AttributeSet attributeSet, int i3) {
        int i4 = DEF_STYLE_RES;
        super(MaterialThemeOverlay.wrap(context, attributeSet, i3, i4), attributeSet, i3);
        this.totalScrollRange = -1;
        this.downPreScrollRange = -1;
        this.downScrollRange = -1;
        this.pendingAction = 0;
        this.liftOnScrollListeners = new ArrayList();
        this.liftProgressListeners = new LinkedHashSet<>();
        Context context2 = getContext();
        setOrientation(1);
        if (getOutlineProvider() == ViewOutlineProvider.BACKGROUND) {
            ViewUtilsLollipop.setBoundsViewOutlineProvider(this);
        }
        ViewUtilsLollipop.setStateListAnimatorFromAttrs(this, attributeSet, i3, i4);
        TypedArray typedArrayObtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R.styleable.AppBarLayout, i3, i4, new int[0]);
        this.liftOnScrollColor = MaterialResources.getColorStateList(context2, typedArrayObtainStyledAttributes, R.styleable.AppBarLayout_liftOnScrollColor);
        this.liftOnScrollColorDuration = MotionUtils.resolveThemeDuration(context2, R.attr.motionDurationMedium2, getResources().getInteger(R.integer.app_bar_elevation_anim_duration));
        this.liftOnScrollColorInterpolator = MotionUtils.resolveThemeInterpolator(context2, R.attr.motionEasingStandardInterpolator, AnimationUtils.LINEAR_INTERPOLATOR);
        int i5 = R.styleable.AppBarLayout_expanded;
        if (typedArrayObtainStyledAttributes.hasValue(i5)) {
            setExpanded(typedArrayObtainStyledAttributes.getBoolean(i5, false), false, false);
        }
        int i6 = R.styleable.AppBarLayout_elevation;
        if (typedArrayObtainStyledAttributes.hasValue(i6)) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, typedArrayObtainStyledAttributes.getDimensionPixelSize(i6, 0));
        }
        setBackground(typedArrayObtainStyledAttributes.getDrawable(R.styleable.AppBarLayout_android_background));
        int i7 = R.styleable.AppBarLayout_android_keyboardNavigationCluster;
        if (typedArrayObtainStyledAttributes.hasValue(i7)) {
            setKeyboardNavigationCluster(typedArrayObtainStyledAttributes.getBoolean(i7, false));
        }
        int i8 = R.styleable.AppBarLayout_android_touchscreenBlocksFocus;
        if (typedArrayObtainStyledAttributes.hasValue(i8)) {
            setTouchscreenBlocksFocus(typedArrayObtainStyledAttributes.getBoolean(i8, false));
        }
        this.appBarElevation = getResources().getDimension(R.dimen.design_appbar_elevation);
        this.liftOnScroll = typedArrayObtainStyledAttributes.getBoolean(R.styleable.AppBarLayout_liftOnScroll, false);
        this.liftOnScrollTargetViewId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.AppBarLayout_liftOnScrollTargetViewId, -1);
        setStatusBarForeground(typedArrayObtainStyledAttributes.getDrawable(R.styleable.AppBarLayout_statusBarForeground));
        typedArrayObtainStyledAttributes.recycle();
        ViewCompat.setOnApplyWindowInsetsListener(this, new AnonymousClass1());
    }

    private void setExpanded(boolean z2, boolean z3, boolean z4) {
        this.pendingAction = (z2 ? 1 : 2) | (z3 ? 4 : 0) | (z4 ? 8 : 0);
        requestLayout();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ LinearLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateLayoutParams(attributeSet);
    }

    public void removeOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        removeOnOffsetChangedListener((BaseOnOffsetChangedListener) onOffsetChangedListener);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return generateLayoutParams(layoutParams);
    }

    public void addOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        addOnOffsetChangedListener((BaseOnOffsetChangedListener) onOffsetChangedListener);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            return new LayoutParams((LinearLayout.LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }
}