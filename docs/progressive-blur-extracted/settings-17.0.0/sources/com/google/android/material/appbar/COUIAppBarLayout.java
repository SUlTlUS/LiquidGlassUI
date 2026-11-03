package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.toolbar.AppBarBlurHelper;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class COUIAppBarLayout extends LinearLayout implements CoordinatorLayout.AttachedBehavior {
    private static final boolean DEBUG;
    private static final float DEFAULT_BOUNCE = 0.0f;
    private static final float DEFAULT_RESPONSE = 0.0f;
    private static final int DEF_STYLE_RES;
    private static final int INVALID_SCROLL_RANGE = -1;
    static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
    static final int PENDING_ACTION_COLLAPSED = 2;
    static final int PENDING_ACTION_EXPANDED = 1;
    static final int PENDING_ACTION_FORCE = 8;
    static final int PENDING_ACTION_NONE = 0;
    public static final int SCROLL_SOURCE_DRAG_HEADER = 1;
    public static final int SCROLL_SOURCE_NESTED_LIST = 0;
    private static final String TAG = "COUIAppBarLayout";
    private Behavior behavior;
    private int currentOffset;
    private int downPreScrollRange;
    private int downScrollRange;
    private ValueAnimator elevationOverlayAnimator;
    private boolean haveChildWithInterpolator;
    private WindowInsetsCompat lastInsets;
    private boolean liftOnScroll;
    private final List<LiftOnScrollListener> liftOnScrollListeners;
    private WeakReference<View> liftOnScrollTargetView;
    private int liftOnScrollTargetViewId;
    private boolean liftable;
    private boolean liftableOverride;
    private boolean lifted;
    private List<BaseOnOffsetChangedListener> listeners;
    private View mExpandableContent;
    private boolean mGradientBlurEnabled;
    private boolean mHasExpandableArea;
    private boolean mNormalBlurEnabled;
    private int pendingAction;
    private Drawable statusBarForeground;
    private int[] tmpStatesArray;
    private int totalScrollRange;

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUIAppBarLayout$1, reason: invalid class name */
    public class AnonymousClass1 implements OnApplyWindowInsetsListener {
        public AnonymousClass1() {
        }

        @Override // androidx.core.view.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return COUIAppBarLayout.this.onWindowInsetChanged(windowInsetsCompat);
        }
    }

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUIAppBarLayout$2, reason: invalid class name */
    /* JADX INFO: loaded from: classes8.dex */
    public class AnonymousClass2 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ MaterialShapeDrawable val$background;

        public AnonymousClass2(MaterialShapeDrawable materialShapeDrawable) {
            this.val$background = materialShapeDrawable;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.val$background.setElevation(fFloatValue);
            if (COUIAppBarLayout.access$000(COUIAppBarLayout.this) instanceof MaterialShapeDrawable) {
                ((MaterialShapeDrawable) COUIAppBarLayout.access$000(COUIAppBarLayout.this)).setElevation(fFloatValue);
            }
            Iterator it = COUIAppBarLayout.access$100(COUIAppBarLayout.this).iterator();
            while (it.hasNext()) {
                ((LiftOnScrollListener) it.next()).onUpdate(fFloatValue, this.val$background.getResolvedTintColor());
            }
        }
    }

    public static class BaseBehavior<T extends COUIAppBarLayout> extends COUIHeaderBehavior<T> {
        private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
        private WeakReference<View> lastNestedScrollingChildRef;
        private int lastStartedType;
        private boolean mNestedScrollInProgress;
        private WeakReference<View> mNestedScrollingTargetRef;
        private int mSpringDispatchDirection;
        private boolean mSpringDispatchListenerInstalled;
        private ValueAnimator offsetAnimator;
        private int offsetDelta;
        private BaseDragCallback onDragCallback;
        private SavedState savedState;

        /* JADX INFO: renamed from: com.google.android.material.appbar.COUIAppBarLayout$BaseBehavior$1, reason: invalid class name */
        /* JADX INFO: loaded from: classes8.dex */
        public class AnonymousClass1 implements ValueAnimator.AnimatorUpdateListener {
            final /* synthetic */ COUIAppBarLayout val$child;
            final /* synthetic */ CoordinatorLayout val$coordinatorLayout;

            public AnonymousClass1(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout) {
                this.val$coordinatorLayout = coordinatorLayout;
                this.val$child = cOUIAppBarLayout;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BaseBehavior.this.setHeaderTopBottomOffset(this.val$coordinatorLayout, this.val$child, ((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        }

        /* JADX INFO: renamed from: com.google.android.material.appbar.COUIAppBarLayout$BaseBehavior$2, reason: invalid class name */
        /* JADX INFO: loaded from: classes8.dex */
        public class AnonymousClass2 implements AccessibilityViewCommand {
            final /* synthetic */ COUIAppBarLayout val$appBarLayout;
            final /* synthetic */ CoordinatorLayout val$coordinatorLayout;
            final /* synthetic */ int val$dy;
            final /* synthetic */ View val$scrollingView;

            public AnonymousClass2(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3) {
                this.val$coordinatorLayout = coordinatorLayout;
                this.val$appBarLayout = cOUIAppBarLayout;
                this.val$scrollingView = view;
                this.val$dy = i3;
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
            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public boolean perform(View view, AccessibilityViewCommand.CommandArguments commandArguments) {
                BaseBehavior.this.onNestedPreScroll(this.val$coordinatorLayout, this.val$appBarLayout, this.val$scrollingView, 0, this.val$dy, new int[]{0, 0}, 1);
                return true;
            }
        }

        /* JADX INFO: renamed from: com.google.android.material.appbar.COUIAppBarLayout$BaseBehavior$3, reason: invalid class name */
        /* JADX INFO: loaded from: classes8.dex */
        public class AnonymousClass3 implements AccessibilityViewCommand {
            final /* synthetic */ COUIAppBarLayout val$appBarLayout;
            final /* synthetic */ boolean val$expand;

            public AnonymousClass3(COUIAppBarLayout cOUIAppBarLayout, boolean z2) {
                this.val$appBarLayout = cOUIAppBarLayout;
                this.val$expand = z2;
            }

            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public boolean perform(View view, AccessibilityViewCommand.CommandArguments commandArguments) {
                this.val$appBarLayout.setExpanded(this.val$expand);
                return true;
            }
        }

        /* JADX INFO: loaded from: classes8.dex */
        public static abstract class BaseDragCallback<T extends COUIAppBarLayout> {
            public abstract boolean canDrag(T t2);
        }

        public BaseBehavior() {
            this.mSpringDispatchDirection = 1;
        }

        public static /* synthetic */ void a(BaseBehavior baseBehavior, COUIAppBarLayout cOUIAppBarLayout, CoordinatorLayout coordinatorLayout, COUIViewOffsetHelper cOUIViewOffsetHelper, int i3) {
            baseBehavior.getClass();
            if (COUIAppBarLayout.access$200()) {
                COUILog.d(COUIAppBarLayout.TAG, "mSpringDispatchListenerInstalled currentOffset: " + i3);
            }
            for (int i4 = 0; i4 < cOUIAppBarLayout.getChildCount(); i4++) {
                LayoutParams layoutParams = (LayoutParams) cOUIAppBarLayout.getChildAt(i4).getLayoutParams();
                ChildScrollEffect scrollEffect = layoutParams.getScrollEffect();
                if (scrollEffect != null && (layoutParams.getScrollFlags() & 1) != 0) {
                    scrollEffect.onOffsetChanged(cOUIAppBarLayout, cOUIAppBarLayout.getChildAt(i4), i3);
                }
            }
            cOUIAppBarLayout.onOffsetChanged(i3);
            baseBehavior.updateAppBarLayoutDrawableState(coordinatorLayout, cOUIAppBarLayout, i3, baseBehavior.mSpringDispatchDirection, false);
        }

        public static /* synthetic */ int access$300(BaseBehavior baseBehavior) {
            return baseBehavior.offsetDelta;
        }

        private void addAccessibilityScrollActions(CoordinatorLayout coordinatorLayout, T t2, View view) {
            if (getTopBottomOffsetForScrollingSibling() != (-t2.getTotalScrollRange()) && view.canScrollVertically(1)) {
                addActionToExpand(coordinatorLayout, t2, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD, false);
            }
            if (getTopBottomOffsetForScrollingSibling() != 0) {
                if (!view.canScrollVertically(-1)) {
                    addActionToExpand(coordinatorLayout, t2, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD, true);
                    return;
                }
                int i3 = -t2.getDownNestedPreScrollRange();
                if (i3 != 0) {
                    ViewCompat.replaceAccessibilityAction(coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD, null, new AnonymousClass2(coordinatorLayout, t2, view, i3));
                }
            }
        }

        private void addActionToExpand(CoordinatorLayout coordinatorLayout, T t2, AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat, boolean z2) {
            ViewCompat.replaceAccessibilityAction(coordinatorLayout, accessibilityActionCompat, null, new AnonymousClass3(t2, z2));
        }

        private int calculateSnapOffset(int i3, int i4, int i5) {
            return i3 < (i4 + i5) / 2 ? i4 : i5;
        }

        private boolean canScrollChildren(CoordinatorLayout coordinatorLayout, T t2, View view) {
            return t2.hasScrollableChildren() && coordinatorLayout.getHeight() - view.getHeight() <= t2.getHeight();
        }

        private void cancelOffsetAnimation() {
            ValueAnimator valueAnimator = this.offsetAnimator;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                return;
            }
            this.offsetAnimator.cancel();
        }

        private static boolean checkFlag(int i3, int i4) {
            return (i3 & i4) == i4;
        }

        private void clearNestedScrollInProgress() {
            this.mNestedScrollInProgress = false;
            this.mNestedScrollingTargetRef = null;
        }

        private View findFirstScrollingChild(CoordinatorLayout coordinatorLayout) {
            int childCount = coordinatorLayout.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = coordinatorLayout.getChildAt(i3);
                if ((childAt instanceof NestedScrollingChild) || (childAt instanceof ListView) || (childAt instanceof ScrollView)) {
                    return childAt;
                }
            }
            return null;
        }

        private static View getAppBarChildOnOffset(COUIAppBarLayout cOUIAppBarLayout, int i3) {
            int iAbs = Math.abs(i3);
            int childCount = cOUIAppBarLayout.getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = cOUIAppBarLayout.getChildAt(i4);
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

        private void installSpringDispatchListener(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4) {
            COUIViewOffsetHelper cOUIViewOffsetHelper;
            this.mSpringDispatchDirection = i3 < i4 ? -1 : 1;
            if (this.mSpringDispatchListenerInstalled || (cOUIViewOffsetHelper = this.mViewOffsetHelper) == null) {
                return;
            }
            cOUIViewOffsetHelper.setOnAnimateUpdateListener(new c(this, t2, coordinatorLayout));
            this.mSpringDispatchListenerInstalled = true;
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
                            topInset -= ViewCompat.getMinimumHeight(childAt);
                        }
                    }
                    if (ViewCompat.getFitsSystemWindows(childAt)) {
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

        private boolean isNestedScrollInProgress() {
            if (!this.mNestedScrollInProgress) {
                return false;
            }
            WeakReference<View> weakReference = this.mNestedScrollingTargetRef;
            View view = weakReference != null ? weakReference.get() : null;
            if (view != null && view.isAttachedToWindow()) {
                return true;
            }
            clearNestedScrollInProgress();
            return false;
        }

        private View resolveScrollingView(T t2) {
            RecyclerView recyclerView;
            View view;
            WeakReference<View> weakReference = this.lastNestedScrollingChildRef;
            if (weakReference != null && (view = weakReference.get()) != null) {
                return view;
            }
            if ((t2 instanceof COUIDividerAppBarLayout) && (recyclerView = ((COUIDividerAppBarLayout) t2).mTargetView) != null) {
                return recyclerView;
            }
            ViewParent parent = t2.getParent();
            if (!(parent instanceof ViewGroup)) {
                return null;
            }
            ViewGroup viewGroup = (ViewGroup) parent;
            if (viewGroup instanceof CoordinatorLayout) {
                return findFirstScrollingChild((CoordinatorLayout) viewGroup);
            }
            return null;
        }

        private void setNestedScrollInProgress(View view) {
            this.mNestedScrollInProgress = true;
            this.mNestedScrollingTargetRef = new WeakReference<>(view);
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

        private void updateAccessibilityActions(CoordinatorLayout coordinatorLayout, T t2) {
            ViewCompat.removeAccessibilityAction(coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD.getId());
            ViewCompat.removeAccessibilityAction(coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD.getId());
            View viewFindFirstScrollingChild = findFirstScrollingChild(coordinatorLayout);
            if (viewFindFirstScrollingChild == null || t2.getTotalScrollRange() == 0 || !(((CoordinatorLayout.LayoutParams) viewFindFirstScrollingChild.getLayoutParams()).getBehavior() instanceof ScrollingViewBehavior)) {
                return;
            }
            addAccessibilityScrollActions(coordinatorLayout, t2, viewFindFirstScrollingChild);
        }

        private void updateAppBarLayoutDrawableState(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4, boolean z2) {
            View appBarChildOnOffset = getAppBarChildOnOffset(t2, i3);
            boolean zShouldLift = false;
            if (appBarChildOnOffset != null) {
                int scrollFlags = ((LayoutParams) appBarChildOnOffset.getLayoutParams()).getScrollFlags();
                if ((scrollFlags & 1) != 0) {
                    int minimumHeight = ViewCompat.getMinimumHeight(appBarChildOnOffset);
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
                t2.jumpDrawablesToCurrentState();
            }
        }

        public void animateOffsetTo(CoordinatorLayout coordinatorLayout, T t2, int i3, float f3) {
            int iAbs = Math.abs(getTopBottomOffsetForScrollingSibling() - i3);
            float fAbs = Math.abs(f3);
            animateOffsetWithDuration(coordinatorLayout, t2, i3, fAbs > 0.0f ? Math.round((iAbs / fAbs) * 1000.0f) * 3 : (int) (((iAbs / t2.getHeight()) + 1.0f) * 150.0f));
        }

        public void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4) {
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling();
            cancelOffsetAnimation();
            if (COUIAppBarLayout.access$200()) {
                COUILog.d(COUIAppBarLayout.TAG, "animateOffsetWithDuration offset: " + i3 + " (valueAnimator; duration=" + i4 + ") currentOffset:" + topBottomOffsetForScrollingSibling);
            }
            if (topBottomOffsetForScrollingSibling == i3) {
                return;
            }
            if (this.offsetAnimator == null) {
                ValueAnimator valueAnimator = new ValueAnimator();
                this.offsetAnimator = valueAnimator;
                valueAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
                this.offsetAnimator.addUpdateListener(new AnonymousClass1(coordinatorLayout, t2));
            }
            this.offsetAnimator.setDuration(Math.min(i4, 600));
            this.offsetAnimator.setIntValues(topBottomOffsetForScrollingSibling, i3);
            this.offsetAnimator.start();
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ boolean canDragView(View view) {
            return canDragView((COUIAppBarLayout) view);
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ int getMaxDragOffset(View view) {
            return getMaxDragOffset((COUIAppBarLayout) view);
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ int getScrollRangeForDragFling(View view) {
            return getScrollRangeForDragFling((COUIAppBarLayout) view);
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public int getTopBottomOffsetForScrollingSibling() {
            return getTopAndBottomOffset() + this.offsetDelta;
        }

        public boolean isOffsetAnimatorRunning() {
            ValueAnimator valueAnimator = this.offsetAnimator;
            return (valueAnimator != null && valueAnimator.isRunning()) || isAnimatingOffset();
        }

        public boolean isUseSpring() {
            return false;
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ int layoutRequestHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, View view, int i3) {
            return layoutRequestHeaderTopBottomOffset(coordinatorLayout, (COUIAppBarLayout) view, i3);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onDetachedFromLayoutParams() {
            clearNestedScrollInProgress();
            super.onDetachedFromLayoutParams();
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ void onFlingFinished(CoordinatorLayout coordinatorLayout, View view) {
            onFlingFinished(coordinatorLayout, (COUIAppBarLayout) view);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i3) {
            return onLayoutChild(coordinatorLayout, (COUIAppBarLayout) view, i3);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, int i5, int i6) {
            return onMeasureChild(coordinatorLayout, (COUIAppBarLayout) view, i3, i4, i5, i6);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3, int i4, int[] iArr, int i5) {
            onNestedPreScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, i3, i4, iArr, i5);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            onNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, i3, i4, i5, i6, i7, iArr);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, View view, Parcelable parcelable) {
            onRestoreInstanceState(coordinatorLayout, (COUIAppBarLayout) view, parcelable);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, View view) {
            return onSaveInstanceState(coordinatorLayout, (COUIAppBarLayout) view);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i3, int i4) {
            return onStartNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, view3, i3, i4);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i3) {
            onStopNestedScroll(coordinatorLayout, (COUIAppBarLayout) view, view2, i3);
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
                    savedState.firstVisibleChildAtMinimumHeight = bottom == ViewCompat.getMinimumHeight(childAt) + t2.getTopInset();
                    savedState.firstVisibleChildPercentageShown = bottom / childAt.getHeight();
                    return savedState;
                }
            }
            return null;
        }

        public void setDragCallback(BaseDragCallback baseDragCallback) {
            this.onDragCallback = baseDragCallback;
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior
        public /* bridge */ /* synthetic */ int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, int i5) {
            return setHeaderTopBottomOffset(coordinatorLayout, (COUIAppBarLayout) view, i3, i4, i5);
        }

        public final int setHeaderTopBottomOffsetInternal(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4, int i5, boolean z2, float f3, float f4, boolean z3, boolean z4) {
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling();
            if (COUIAppBarLayout.access$200()) {
                COUILog.d(COUIAppBarLayout.TAG, "setHeaderTopBottomOffset.start newOffset: " + i3 + " curOffset=" + topBottomOffsetForScrollingSibling + " minOffset=" + i4 + " maxOffset=" + i5 + " isSpring=" + z2);
            }
            int i6 = 0;
            if (i4 == 0 || topBottomOffsetForScrollingSibling < i4 || topBottomOffsetForScrollingSibling > i5) {
                this.offsetDelta = 0;
            } else {
                i3 = MathUtils.clamp(i3, i4, i5);
                if (topBottomOffsetForScrollingSibling != i3) {
                    int iInterpolateOffset = t2.hasChildWithInterpolator() ? interpolateOffset(t2, i3) : i3;
                    if (z2) {
                        installSpringDispatchListener(coordinatorLayout, t2, i3, topBottomOffsetForScrollingSibling);
                        animateTopAndBottomOffset(iInterpolateOffset, f3, f4, z3);
                        this.offsetDelta = i3 - iInterpolateOffset;
                        i6 = topBottomOffsetForScrollingSibling - i3;
                    } else {
                        boolean zLayoutRequestTopAndBottomOffset = z4 ? layoutRequestTopAndBottomOffset(iInterpolateOffset) : setTopAndBottomOffset(iInterpolateOffset);
                        int i7 = topBottomOffsetForScrollingSibling - i3;
                        this.offsetDelta = i3 - iInterpolateOffset;
                        if (zLayoutRequestTopAndBottomOffset) {
                            while (i6 < t2.getChildCount()) {
                                LayoutParams layoutParams = (LayoutParams) t2.getChildAt(i6).getLayoutParams();
                                ChildScrollEffect scrollEffect = layoutParams.getScrollEffect();
                                if (scrollEffect != null && (layoutParams.getScrollFlags() & 1) != 0) {
                                    scrollEffect.onOffsetChanged(t2, t2.getChildAt(i6), getTopAndBottomOffset());
                                }
                                i6++;
                            }
                        }
                        if (!zLayoutRequestTopAndBottomOffset && t2.hasChildWithInterpolator()) {
                            coordinatorLayout.dispatchDependentViewsChanged(t2);
                        }
                        t2.onOffsetChanged(getTopAndBottomOffset());
                        updateAppBarLayoutDrawableState(coordinatorLayout, t2, i3, i3 < topBottomOffsetForScrollingSibling ? -1 : 1, false);
                        i6 = i7;
                    }
                }
            }
            updateAccessibilityActions(coordinatorLayout, t2);
            if (COUIAppBarLayout.access$200()) {
                COUILog.d(COUIAppBarLayout.TAG, "setHeaderTopBottomOffset.end consumed: " + i6 + " offsetDelta=" + this.offsetDelta + " newOffset=" + i3);
            }
            return i6;
        }

        public void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, T t2) {
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
                    if (childIndexOnOffset == 0 && ViewCompat.getFitsSystemWindows(t2) && ViewCompat.getFitsSystemWindows(childAt)) {
                        topInset2 -= t2.getTopInset();
                    }
                    if (checkFlag(scrollFlags, 2)) {
                        minimumHeight += ViewCompat.getMinimumHeight(childAt);
                    } else if (checkFlag(scrollFlags, 5)) {
                        int minimumHeight2 = ViewCompat.getMinimumHeight(childAt) + minimumHeight;
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

        public boolean canDragView(T t2) {
            View viewResolveScrollingView;
            BaseDragCallback baseDragCallback = this.onDragCallback;
            if (baseDragCallback != null) {
                return baseDragCallback.canDrag(t2);
            }
            if (isNestedScrollInProgress()) {
                return false;
            }
            if ((!(t2 instanceof COUIDividerAppBarLayout) || ((COUIDividerAppBarLayout) t2).mTargetViewState == 0) && (viewResolveScrollingView = resolveScrollingView(t2)) != null && viewResolveScrollingView.isShown()) {
                return !viewResolveScrollingView.canScrollVertically(-1);
            }
            return false;
        }

        public int getMaxDragOffset(T t2) {
            return -t2.getDownNestedScrollRange();
        }

        public int getScrollRangeForDragFling(T t2) {
            return t2.getTotalScrollRange();
        }

        public int layoutRequestHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, T t2, int i3) {
            return setHeaderTopBottomOffset(coordinatorLayout, t2, i3, Integer.MIN_VALUE, Integer.MAX_VALUE, false, 0.0f, 0.0f, true);
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
                            layoutRequestHeaderTopBottomOffset(coordinatorLayout, (COUIAppBarLayout) t2, i4);
                        }
                    } else if ((pendingAction & 1) != 0) {
                        if (z2) {
                            animateOffsetTo(coordinatorLayout, t2, 0, 0.0f);
                        } else {
                            layoutRequestHeaderTopBottomOffset(coordinatorLayout, (COUIAppBarLayout) t2, 0);
                        }
                    }
                }
            } else if (savedState.fullyScrolled) {
                layoutRequestHeaderTopBottomOffset(coordinatorLayout, (COUIAppBarLayout) t2, -t2.getTotalScrollRange());
            } else if (savedState.fullyExpanded) {
                layoutRequestHeaderTopBottomOffset(coordinatorLayout, (COUIAppBarLayout) t2, 0);
            } else {
                View childAt = t2.getChildAt(savedState.firstVisibleChildIndex);
                layoutRequestHeaderTopBottomOffset(coordinatorLayout, (COUIAppBarLayout) t2, (-childAt.getBottom()) + (this.savedState.firstVisibleChildAtMinimumHeight ? ViewCompat.getMinimumHeight(childAt) + t2.getTopInset() : Math.round(childAt.getHeight() * this.savedState.firstVisibleChildPercentageShown)));
            }
            t2.resetPendingAction();
            this.savedState = null;
            layoutRequestTopAndBottomOffset(MathUtils.clamp(getTopAndBottomOffset(), -t2.getTotalScrollRange(), 0));
            updateAppBarLayoutDrawableState(coordinatorLayout, t2, getAnimateTopAndBottomOffset(), 0, true);
            t2.onOffsetChanged(getAnimateTopAndBottomOffset());
            updateAccessibilityActions(coordinatorLayout, t2);
            return zOnLayoutChild;
        }

        public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4, int i5, int i6) {
            if (((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.LayoutParams) t2.getLayoutParams())).height != -2) {
                return super.onMeasureChild(coordinatorLayout, t2, i3, i4, i5, i6);
            }
            coordinatorLayout.onMeasureChild(t2, i3, i4, View.MeasureSpec.makeMeasureSpec(0, 0), i6);
            return true;
        }

        /* JADX WARN: Code duplicated, block: B:9:0x0027  */
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
                    iArr[1] = scroll(coordinatorLayout, t3, i4, i7, i8, 0);
                } else {
                    t3 = t2;
                }
            }
            if (t3.isLiftOnScroll()) {
                t3.setLiftedState(t3.shouldLift(view));
            }
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, T t2, View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            if (i6 < 0) {
                iArr[1] = scroll(coordinatorLayout, t2, i6, -t2.getDownNestedScrollRange(), 0, 0);
            }
            if (i6 == 0) {
                updateAccessibilityActions(coordinatorLayout, t2);
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
            boolean z2 = (i3 & 2) != 0 && (t2.isLiftOnScroll() || canScrollChildren(coordinatorLayout, t2, view));
            if (z2) {
                cancelOffsetAnimation();
                setNestedScrollInProgress(view2);
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
            clearNestedScrollInProgress();
        }

        public int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4, int i5) {
            return setHeaderTopBottomOffset(coordinatorLayout, t2, i3, i4, i5, false, 0.0f, 0.0f, false);
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mSpringDispatchDirection = 1;
        }

        public final int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, T t2, int i3, int i4, int i5, boolean z2, float f3, float f4, boolean z3) {
            return setHeaderTopBottomOffsetInternal(coordinatorLayout, t2, i3, i4, i5, z2, f3, f4, false, z3);
        }

        public static class SavedState extends AbsSavedState {
            public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
            boolean firstVisibleChildAtMinimumHeight;
            int firstVisibleChildIndex;
            float firstVisibleChildPercentageShown;
            boolean fullyExpanded;
            boolean fullyScrolled;

            /* JADX INFO: renamed from: com.google.android.material.appbar.COUIAppBarLayout$BaseBehavior$SavedState$1, reason: invalid class name */
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

    public interface BaseOnOffsetChangedListener<T extends COUIAppBarLayout> {
        void onOffsetChanged(T t2, int i3);
    }

    public static class Behavior extends BaseBehavior<COUIAppBarLayout> {

        /* JADX INFO: loaded from: classes8.dex */
        public static abstract class DragCallback extends BaseBehavior.BaseDragCallback<COUIAppBarLayout> {
        }

        public Behavior() {
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean animateTopAndBottomOffset(int i3, float f3, float f4, boolean z2) {
            return super.animateTopAndBottomOffset(i3, f3, f4, z2);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ void cancelAnimateOffset() {
            super.cancelAnimateOffset();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getAnimateTopAndBottomOffset() {
            return super.getAnimateTopAndBottomOffset();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getLeftAndRightOffset() {
            return super.getLeftAndRightOffset();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isAnimatingOffset() {
            return super.isAnimatingOffset();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isHorizontalOffsetEnabled() {
            return super.isHorizontalOffsetEnabled();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isVerticalOffsetEnabled() {
            return super.isVerticalOffsetEnabled();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean layoutRequestTopAndBottomOffset(int i3) {
            return super.layoutRequestTopAndBottomOffset(i3);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onDetachedFromLayoutParams() {
            super.onDetachedFromLayoutParams();
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, int i3) {
            return super.onLayoutChild(coordinatorLayout, cOUIAppBarLayout, i3);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, int i3, int i4, int i5, int i6) {
            return super.onMeasureChild(coordinatorLayout, cOUIAppBarLayout, i3, i4, i5, i6);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void onNestedPreScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3, int i4, int[] iArr, int i5) {
            super.onNestedPreScroll(coordinatorLayout, cOUIAppBarLayout, view, i3, i4, iArr, i5);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void onNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
            super.onNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, i3, i4, i5, i6, i7, iArr);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, Parcelable parcelable) {
            super.onRestoreInstanceState(coordinatorLayout, cOUIAppBarLayout, parcelable);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout) {
            return super.onSaveInstanceState(coordinatorLayout, cOUIAppBarLayout);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, View view2, int i3, int i4) {
            return super.onStartNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, view2, i3, i4);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void onStopNestedScroll(CoordinatorLayout coordinatorLayout, COUIAppBarLayout cOUIAppBarLayout, View view, int i3) {
            super.onStopNestedScroll(coordinatorLayout, cOUIAppBarLayout, view, i3);
        }

        @Override // com.google.android.material.appbar.COUIHeaderBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            return super.onTouchEvent(coordinatorLayout, view, motionEvent);
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void setDragCallback(BaseBehavior.BaseDragCallback baseDragCallback) {
            super.setDragCallback(baseDragCallback);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setHorizontalOffsetEnabled(boolean z2) {
            super.setHorizontalOffsetEnabled(z2);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean setLeftAndRightOffset(int i3) {
            return super.setLeftAndRightOffset(i3);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setOnAnimateUpdateListener(COUIViewOffsetHelper.OnAnimateUpdateListener onAnimateUpdateListener) {
            super.setOnAnimateUpdateListener(onAnimateUpdateListener);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i3) {
            return super.setTopAndBottomOffset(i3);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setVerticalOffsetEnabled(boolean z2) {
            super.setVerticalOffsetEnabled(z2);
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
    public static abstract class ChildScrollEffect {
        public abstract void onOffsetChanged(COUIAppBarLayout cOUIAppBarLayout, View view, float f3);
    }

    /* JADX INFO: loaded from: classes8.dex */
    public static class CompressChildScrollEffect extends ChildScrollEffect {
        private static final float COMPRESS_DISTANCE_FACTOR = 0.3f;
        private final Rect relativeRect = new Rect();
        private final Rect ghostRect = new Rect();

        private static void updateRelativeRect(Rect rect, COUIAppBarLayout cOUIAppBarLayout, View view) {
            view.getDrawingRect(rect);
            cOUIAppBarLayout.offsetDescendantRectToMyCoords(view, rect);
            rect.offset(0, -cOUIAppBarLayout.getTopInset());
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.ChildScrollEffect
        public void onOffsetChanged(COUIAppBarLayout cOUIAppBarLayout, View view, float f3) {
            updateRelativeRect(this.relativeRect, cOUIAppBarLayout, view);
            float fAbs = this.relativeRect.top - Math.abs(f3);
            if (fAbs > 0.0f) {
                ViewCompat.setClipBounds(view, null);
                view.setTranslationY(0.0f);
                return;
            }
            float fClamp = 1.0f - MathUtils.clamp(Math.abs(fAbs / this.relativeRect.height()), 0.0f, 1.0f);
            float fHeight = (-fAbs) - ((this.relativeRect.height() * 0.3f) * (1.0f - (fClamp * fClamp)));
            view.setTranslationY(fHeight);
            view.getDrawingRect(this.ghostRect);
            this.ghostRect.offset(0, (int) (-fHeight));
            ViewCompat.setClipBounds(view, this.ghostRect);
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
    public interface LiftOnScrollListener {
        void onUpdate(float f3, int i3);
    }

    public interface OnOffsetChangedListener extends BaseOnOffsetChangedListener<COUIAppBarLayout> {
        @Override // com.google.android.material.appbar.COUIAppBarLayout.BaseOnOffsetChangedListener
        void onOffsetChanged(COUIAppBarLayout cOUIAppBarLayout, int i3);
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        DEF_STYLE_RES = R.style.Widget_Design_AppBarLayout;
    }

    public COUIAppBarLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ Drawable access$000(COUIAppBarLayout cOUIAppBarLayout) {
        return cOUIAppBarLayout.statusBarForeground;
    }

    public static /* synthetic */ List access$100(COUIAppBarLayout cOUIAppBarLayout) {
        return cOUIAppBarLayout.liftOnScrollListeners;
    }

    public static /* synthetic */ boolean access$200() {
        return DEBUG;
    }

    private void clearLiftOnScrollTargetView() {
        WeakReference<View> weakReference = this.liftOnScrollTargetView;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.liftOnScrollTargetView = null;
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
            if (childAt.getVisibility() != 8 && !ViewCompat.getFitsSystemWindows(childAt)) {
                return true;
            }
        }
        return false;
    }

    private void startLiftOnScrollElevationOverlayAnimation(MaterialShapeDrawable materialShapeDrawable, boolean z2) {
        float dimension = getResources().getDimension(R.dimen.design_appbar_elevation);
        float f3 = z2 ? 0.0f : dimension;
        if (!z2) {
            dimension = 0.0f;
        }
        ValueAnimator valueAnimator = this.elevationOverlayAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(f3, dimension);
        this.elevationOverlayAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(getResources().getInteger(R.integer.app_bar_elevation_anim_duration));
        this.elevationOverlayAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        this.elevationOverlayAnimator.addUpdateListener(new AnonymousClass2(materialShapeDrawable));
        this.elevationOverlayAnimator.start();
    }

    private void updateWillNotDraw() {
        setWillNotDraw(!shouldDrawStatusBarForeground());
    }

    public void addLiftOnScrollListener(LiftOnScrollListener liftOnScrollListener) {
        this.liftOnScrollListeners.add(liftOnScrollListener);
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

    public void clearExpandableContent() {
        View view = this.mExpandableContent;
        if (view != null && view.getParent() == this) {
            removeView(this.mExpandableContent);
        }
        this.mExpandableContent = null;
    }

    public void clearLiftOnScrollListener() {
        this.liftOnScrollListeners.clear();
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
    public CoordinatorLayout.Behavior<COUIAppBarLayout> getBehavior() {
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
                    minimumHeight = ViewCompat.getMinimumHeight(childAt);
                } else {
                    if ((i5 & 2) != 0) {
                        minimumHeight = measuredHeight - ViewCompat.getMinimumHeight(childAt);
                    } else {
                        iMin = i6 + measuredHeight;
                    }
                    if (childCount == 0 && ViewCompat.getFitsSystemWindows(childAt)) {
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
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight() + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
            int i5 = layoutParams.scrollFlags;
            if ((i5 & 1) == 0) {
                break;
            }
            minimumHeight += measuredHeight;
            if ((i5 & 2) != 0) {
                minimumHeight -= ViewCompat.getMinimumHeight(childAt);
                break;
            }
        }
        int iMax = Math.max(0, minimumHeight);
        this.downScrollRange = iMax;
        return iMax;
    }

    public View getExpandableContent() {
        return this.mExpandableContent;
    }

    public int getLiftOnScrollTargetViewId() {
        return this.liftOnScrollTargetViewId;
    }

    public final int getMinimumHeightForVisibleOverlappingContent() {
        int topInset = getTopInset();
        int minimumHeight = ViewCompat.getMinimumHeight(this);
        if (minimumHeight == 0) {
            int childCount = getChildCount();
            minimumHeight = childCount >= 1 ? ViewCompat.getMinimumHeight(getChildAt(childCount - 1)) : 0;
            if (minimumHeight == 0) {
                return getHeight() / 3;
            }
        }
        return (minimumHeight * 2) + topInset;
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
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight();
            int i5 = layoutParams.scrollFlags;
            if ((i5 & 1) == 0) {
                break;
            }
            minimumHeight += measuredHeight + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
            if (i4 == 0 && ViewCompat.getFitsSystemWindows(childAt)) {
                minimumHeight -= getTopInset();
            }
            if ((i5 & 2) != 0) {
                minimumHeight -= ViewCompat.getMinimumHeight(childAt);
                break;
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

    public boolean isGradientBlurEnabled() {
        return this.mGradientBlurEnabled;
    }

    public boolean isLiftOnScroll() {
        return this.liftOnScroll;
    }

    public boolean isLifted() {
        return this.lifted;
    }

    public boolean isNormalBackgroundBlurEnabled() {
        return this.mNormalBlurEnabled;
    }

    public void loadExpandableContent(int i3) {
        clearExpandableContent();
        View viewInflate = LayoutInflater.from(getContext()).inflate(i3, (ViewGroup) this, false);
        this.mExpandableContent = viewInflate;
        addView(viewInflate);
        this.mHasExpandableArea = true;
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
        if (ViewCompat.getFitsSystemWindows(this) && shouldOffsetFirstChild()) {
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
        if (mode != 1073741824 && ViewCompat.getFitsSystemWindows(this) && shouldOffsetFirstChild()) {
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
            ViewCompat.postInvalidateOnAnimation(this);
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
        WindowInsetsCompat windowInsetsCompat2 = ViewCompat.getFitsSystemWindows(this) ? windowInsetsCompat : null;
        if (!ObjectsCompat.equals(this.lastInsets, windowInsetsCompat2)) {
            this.lastInsets = windowInsetsCompat2;
            updateWillNotDraw();
            requestLayout();
        }
        return windowInsetsCompat;
    }

    public boolean removeLiftOnScrollListener(LiftOnScrollListener liftOnScrollListener) {
        return this.liftOnScrollListeners.remove(liftOnScrollListener);
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
    public void setElevation(float f3) {
        super.setElevation(f3);
        MaterialShapeUtils.setElevation(this, f3);
    }

    public void setExpandableContent(View view) {
        clearExpandableContent();
        this.mExpandableContent = view;
        if (view != null) {
            this.mHasExpandableArea = true;
            addView(view);
            requestLayout();
        }
    }

    public void setExpanded(boolean z2) {
        setExpanded(z2, ViewCompat.isLaidOut(this));
    }

    public void setGradientBlurEnabled(boolean z2) {
        if (this.mGradientBlurEnabled == z2) {
            return;
        }
        if (z2 && this.mNormalBlurEnabled) {
            this.mNormalBlurEnabled = false;
            AppBarBlurHelper.clearNormalBlurForAppBar(this);
        }
        this.mGradientBlurEnabled = z2;
        if (z2) {
            AppBarBlurHelper.setUpGradientBlurBackground(this);
        } else {
            if (z2) {
                return;
            }
            AppBarBlurHelper.clearGradientBlurForAppBar(this);
        }
    }

    public void setHasExpandableArea(boolean z2) {
        if (this.mHasExpandableArea != z2) {
            this.mHasExpandableArea = z2;
            if (!z2) {
                clearExpandableContent();
            }
            requestLayout();
        }
    }

    public void setLiftOnScroll(boolean z2) {
        this.liftOnScroll = z2;
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

    public void setNormalBackgroundBlurEnabled(boolean z2) {
        if (this.mNormalBlurEnabled == z2) {
            return;
        }
        if (z2 && this.mGradientBlurEnabled) {
            this.mGradientBlurEnabled = false;
            AppBarBlurHelper.clearGradientBlurForAppBar(this);
        }
        this.mNormalBlurEnabled = z2;
        if (z2) {
            AppBarBlurHelper.setUpNormalBlurBackground(this);
        } else {
            if (z2) {
                return;
            }
            AppBarBlurHelper.clearNormalBlurForAppBar(this);
        }
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i3) {
        if (i3 != 1) {
            throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
        }
        super.setOrientation(i3);
    }

    public void setStatusBarForeground(Drawable drawable) {
        Drawable drawable2 = this.statusBarForeground;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            Drawable drawableMutate = drawable != null ? drawable.mutate() : null;
            this.statusBarForeground = drawableMutate;
            if (drawableMutate != null) {
                if (drawableMutate.isStateful()) {
                    this.statusBarForeground.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.statusBarForeground, ViewCompat.getLayoutDirection(this));
                this.statusBarForeground.setVisible(getVisibility() == 0, false);
                this.statusBarForeground.setCallback(this);
            }
            updateWillNotDraw();
            ViewCompat.postInvalidateOnAnimation(this);
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

    public static class ScrollingViewBehavior extends COUIHeaderScrollingViewBehavior {
        private COUICoordinatorScrollingViewAnimator mCoordinatorLayoutAnimator;
        private final Rect tempRect3;

        /* JADX INFO: renamed from: com.google.android.material.appbar.COUIAppBarLayout$ScrollingViewBehavior$1, reason: invalid class name */
        /* JADX INFO: loaded from: classes8.dex */
        public class AnonymousClass1 implements COUICoordinatorScrollingViewAnimator.OffsetParamsProvider {
            final /* synthetic */ BaseBehavior val$ablBehavior;
            final /* synthetic */ View val$dependency;

            public AnonymousClass1(BaseBehavior baseBehavior, View view) {
                this.val$ablBehavior = baseBehavior;
                this.val$dependency = view;
            }

            @Override // com.google.android.material.appbar.COUICoordinatorScrollingViewAnimator.OffsetParamsProvider
            public int getOffsetDelta() {
                return BaseBehavior.access$300(this.val$ablBehavior);
            }

            @Override // com.google.android.material.appbar.COUICoordinatorScrollingViewAnimator.OffsetParamsProvider
            public int getOverlapPixels() {
                return ScrollingViewBehavior.this.getOverlapPixelsForOffset(this.val$dependency);
            }

            @Override // com.google.android.material.appbar.COUICoordinatorScrollingViewAnimator.OffsetParamsProvider
            public int getVerticalLayoutGap() {
                return ScrollingViewBehavior.this.getVerticalLayoutGap();
            }
        }

        public ScrollingViewBehavior() {
            this.tempRect3 = new Rect();
        }

        private void appBarOffsetChildAsNeeded(View view, View view2) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) view2.getLayoutParams()).getBehavior();
            if (behavior instanceof AppBarLayout.BaseBehavior) {
                AppBarLayout.BaseBehavior baseBehavior = (AppBarLayout.BaseBehavior) behavior;
                ViewCompat.offsetTopAndBottom(view, (((view2.getBottom() - view.getTop()) + (baseBehavior.getTopBottomOffsetForScrollingSibling() - baseBehavior.getTopAndBottomOffset())) + getVerticalLayoutGap()) - getOverlapPixelsForOffset(view2));
            }
        }

        private void couiAppBarOffsetChildAsNeeded(View view, View view2) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) view2.getLayoutParams()).getBehavior();
            if (behavior instanceof BaseBehavior) {
                BaseBehavior baseBehavior = (BaseBehavior) behavior;
                if (!baseBehavior.isUseSpring()) {
                    ViewCompat.offsetTopAndBottom(view, (((view2.getBottom() - view.getTop()) + BaseBehavior.access$300(baseBehavior)) + getVerticalLayoutGap()) - getOverlapPixelsForOffset(view2));
                    return;
                }
                COUICoordinatorScrollingViewAnimator cOUICoordinatorScrollingViewAnimator = this.mCoordinatorLayoutAnimator;
                if (cOUICoordinatorScrollingViewAnimator != null) {
                    if (view2 instanceof COUIAppBarLayout) {
                        cOUICoordinatorScrollingViewAnimator.updateChildLayoutTop(view2);
                    }
                    this.mCoordinatorLayoutAnimator.applyChildOffset();
                    return;
                }
                COUIViewOffsetHelper cOUIViewOffsetHelper = baseBehavior.mViewOffsetHelper;
                COUIViewOffsetHelper cOUIViewOffsetHelper2 = this.mViewOffsetHelper;
                if (cOUIViewOffsetHelper == null || cOUIViewOffsetHelper2 == null) {
                    ViewCompat.offsetTopAndBottom(view, (((view2.getBottom() - view.getTop()) + BaseBehavior.access$300(baseBehavior)) + getVerticalLayoutGap()) - getOverlapPixelsForOffset(view2));
                    return;
                }
                COUICoordinatorScrollingViewAnimator cOUICoordinatorScrollingViewAnimator2 = new COUICoordinatorScrollingViewAnimator(view2, view, cOUIViewOffsetHelper, cOUIViewOffsetHelper2, new AnonymousClass1(baseBehavior, view2));
                this.mCoordinatorLayoutAnimator = cOUICoordinatorScrollingViewAnimator2;
                cOUICoordinatorScrollingViewAnimator2.initialize();
            }
        }

        private View findAppBarDependency(CoordinatorLayout coordinatorLayout, View view) {
            List<View> dependencies = coordinatorLayout.getDependencies(view);
            int size = dependencies.size();
            for (int i3 = 0; i3 < size; i3++) {
                View view2 = dependencies.get(i3);
                if ((view2 instanceof COUIAppBarLayout) || (view2 instanceof AppBarLayout)) {
                    return view2;
                }
            }
            return null;
        }

        private static int getAppBarLayoutOffset(View view) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) view.getLayoutParams()).getBehavior();
            if (behavior instanceof BaseBehavior) {
                return ((BaseBehavior) behavior).getTopBottomOffsetForScrollingSibling();
            }
            if (behavior instanceof AppBarLayout.BaseBehavior) {
                return ((AppBarLayout.BaseBehavior) behavior).getTopBottomOffsetForScrollingSibling();
            }
            return 0;
        }

        private void offsetChildAsNeeded(View view, View view2) {
            if (COUIAppBarLayout.access$200()) {
                COUILog.d(COUIAppBarLayout.TAG, "offsetChildAsNeeded: " + view.getTop() + "child ScrollY = " + view.getScrollY() + " SimpleName: " + view.getClass().getSimpleName() + view);
            }
            if (view2 instanceof AppBarLayout) {
                appBarOffsetChildAsNeeded(view, view2);
            } else {
                couiAppBarOffsetChildAsNeeded(view, view2);
            }
        }

        private void updateLiftedStateIfNeeded(View view, View view2) {
            if (view2 instanceof COUIAppBarLayout) {
                COUIAppBarLayout cOUIAppBarLayout = (COUIAppBarLayout) view2;
                if (cOUIAppBarLayout.isLiftOnScroll()) {
                    cOUIAppBarLayout.setLiftedState(cOUIAppBarLayout.shouldLift(view));
                    return;
                }
                return;
            }
            if (view2 instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) view2;
                if (appBarLayout.isLiftOnScroll()) {
                    appBarLayout.setLiftedState(appBarLayout.shouldLift(view));
                }
            }
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean animateTopAndBottomOffset(int i3, float f3, float f4, boolean z2) {
            return super.animateTopAndBottomOffset(i3, f3, f4, z2);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ void cancelAnimateOffset() {
            super.cancelAnimateOffset();
        }

        @Override // com.google.android.material.appbar.COUIHeaderScrollingViewBehavior
        public View findFirstDependency(List<View> list) {
            int size = list.size();
            for (int i3 = 0; i3 < size; i3++) {
                View view = list.get(i3);
                if ((view instanceof COUIAppBarLayout) || (view instanceof AppBarLayout)) {
                    return view;
                }
            }
            return null;
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getAnimateTopAndBottomOffset() {
            return super.getAnimateTopAndBottomOffset();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getLeftAndRightOffset() {
            return super.getLeftAndRightOffset();
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x001d, code lost:
        
            if (r4 == 0) goto L23;
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x0023, code lost:
        
            return (r5 / r4) + 1.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x003e, code lost:
        
            if (r4 == 0) goto L23;
         */
        @Override // com.google.android.material.appbar.COUIHeaderScrollingViewBehavior
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public float getOverlapRatioForOffset(android.view.View r5) {
            /*
                r4 = this;
                boolean r4 = r5 instanceof com.google.android.material.appbar.COUIAppBarLayout
                r0 = 1065353216(0x3f800000, float:1.0)
                r1 = 0
                if (r4 == 0) goto L24
                com.google.android.material.appbar.COUIAppBarLayout r5 = (com.google.android.material.appbar.COUIAppBarLayout) r5
                int r4 = r5.getTotalScrollRange()
                int r2 = r5.getDownNestedPreScrollRange()
                int r5 = getAppBarLayoutOffset(r5)
                if (r2 == 0) goto L1c
                int r3 = r4 + r5
                if (r3 > r2) goto L1c
                return r1
            L1c:
                int r4 = r4 - r2
                if (r4 == 0) goto L41
            L1f:
                float r5 = (float) r5
                float r4 = (float) r4
                float r5 = r5 / r4
                float r5 = r5 + r0
                return r5
            L24:
                boolean r4 = r5 instanceof com.google.android.material.appbar.AppBarLayout
                if (r4 == 0) goto L41
                com.google.android.material.appbar.AppBarLayout r5 = (com.google.android.material.appbar.AppBarLayout) r5
                int r4 = r5.getTotalScrollRange()
                int r2 = r5.getDownNestedPreScrollRange()
                int r5 = getAppBarLayoutOffset(r5)
                if (r2 == 0) goto L3d
                int r3 = r4 + r5
                if (r3 > r2) goto L3d
                return r1
            L3d:
                int r4 = r4 - r2
                if (r4 == 0) goto L41
                goto L1f
            L41:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.COUIAppBarLayout.ScrollingViewBehavior.getOverlapRatioForOffset(android.view.View):float");
        }

        @Override // com.google.android.material.appbar.COUIHeaderScrollingViewBehavior
        public int getScrollRange(View view) {
            if (view instanceof COUIAppBarLayout) {
                return ((COUIAppBarLayout) view).getTotalScrollRange();
            }
            return view instanceof AppBarLayout ? ((AppBarLayout) view).getTotalScrollRange() : super.getScrollRange(view);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isAnimatingOffset() {
            return super.isAnimatingOffset();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isHorizontalOffsetEnabled() {
            return super.isHorizontalOffsetEnabled();
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean isVerticalOffsetEnabled() {
            return super.isVerticalOffsetEnabled();
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
            return (view2 instanceof COUIAppBarLayout) || (view2 instanceof AppBarLayout);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean layoutRequestTopAndBottomOffset(int i3) {
            return super.layoutRequestTopAndBottomOffset(i3);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
            offsetChildAsNeeded(view, view2);
            updateLiftedStateIfNeeded(view, view2);
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, View view, View view2) {
            if ((view2 instanceof COUIAppBarLayout) || (view2 instanceof AppBarLayout)) {
                ViewCompat.removeAccessibilityAction(coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD.getId());
                ViewCompat.removeAccessibilityAction(coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD.getId());
            }
        }

        /* JADX WARN: Code duplicated, block: B:31:0x009a  */
        /* JADX WARN: Code duplicated, block: B:33:0x00a7  */
        /* JADX WARN: Code duplicated, block: B:34:0x00ad  */
        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i3) {
            if (COUIAppBarLayout.access$200()) {
                COUILog.d(COUIAppBarLayout.TAG, "ScrollingViewBehavior.onLayoutChild child Bottom = " + view.getBottom() + " SimpleName = " + view.getClass().getSimpleName());
            }
            View viewFindAppBarDependency = findAppBarDependency(coordinatorLayout, view);
            if (viewFindAppBarDependency instanceof AppBarLayout) {
                return super.onLayoutChild(coordinatorLayout, view, i3);
            }
            layoutChild(coordinatorLayout, view, i3);
            if (this.mViewOffsetHelper == null) {
                this.mViewOffsetHelper = new COUIViewOffsetHelper(view);
            }
            CoordinatorLayout.Behavior behavior = viewFindAppBarDependency != null ? ((CoordinatorLayout.LayoutParams) viewFindAppBarDependency.getLayoutParams()).getBehavior() : null;
            if (behavior == null || !(behavior instanceof BaseBehavior)) {
                this.mViewOffsetHelper.onViewLayout();
                if (this.mViewOffsetHelper.isAnimatingOffset()) {
                    COUIViewOffsetHelper cOUIViewOffsetHelper = this.mViewOffsetHelper;
                    cOUIViewOffsetHelper.applyOffsets(cOUIViewOffsetHelper.getAnimateTopAndBottomOffset());
                } else {
                    this.mViewOffsetHelper.applyOffsets();
                }
            } else {
                BaseBehavior baseBehavior = (BaseBehavior) behavior;
                if (baseBehavior.isUseSpring()) {
                    COUIAppBarLayout cOUIAppBarLayout = (COUIAppBarLayout) viewFindAppBarDependency;
                    COUIViewOffsetHelper cOUIViewOffsetHelper2 = baseBehavior.mViewOffsetHelper;
                    this.mViewOffsetHelper.onViewLayout(cOUIAppBarLayout, cOUIViewOffsetHelper2 != null ? cOUIViewOffsetHelper2.getLayoutTop() : viewFindAppBarDependency.getTop());
                    if (this.mViewOffsetHelper.isAnimatingOffset()) {
                        COUIViewOffsetHelper cOUIViewOffsetHelper3 = this.mViewOffsetHelper;
                        cOUIViewOffsetHelper3.applyOffsets(cOUIViewOffsetHelper3.getAnimateTopAndBottomOffset());
                    } else {
                        COUICoordinatorScrollingViewAnimator cOUICoordinatorScrollingViewAnimator = this.mCoordinatorLayoutAnimator;
                        if (cOUICoordinatorScrollingViewAnimator != null) {
                            cOUICoordinatorScrollingViewAnimator.applyChildOffset();
                        } else {
                            offsetChildAsNeeded(view, viewFindAppBarDependency);
                        }
                    }
                } else {
                    this.mViewOffsetHelper.onViewLayout();
                    if (this.mViewOffsetHelper.isAnimatingOffset()) {
                        this.mViewOffsetHelper.applyOffsets();
                    } else {
                        COUIViewOffsetHelper cOUIViewOffsetHelper4 = this.mViewOffsetHelper;
                        cOUIViewOffsetHelper4.applyOffsets(cOUIViewOffsetHelper4.getAnimateTopAndBottomOffset());
                    }
                }
            }
            if (getTempTopBottomOffset() != 0) {
                this.mViewOffsetHelper.layoutRequestTopAndBottomOffset(getTempTopBottomOffset());
                setTempTopBottomOffset(0);
            }
            if (getTempLeftRightOffset() != 0) {
                this.mViewOffsetHelper.setLeftAndRightOffset(getTempLeftRightOffset());
                setTempLeftRightOffset(0);
            }
            if (!COUIAppBarLayout.access$200()) {
                return true;
            }
            COUILog.d(COUIAppBarLayout.TAG, "after ScrollingViewBehavior.onLayoutChild child Bottom = " + view.getBottom() + " SimpleName = " + view.getClass().getSimpleName());
            return true;
        }

        @Override // com.google.android.material.appbar.COUIHeaderScrollingViewBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i3, int i4, int i5, int i6) {
            return super.onMeasureChild(coordinatorLayout, view, i3, i4, i5, i6);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onRequestChildRectangleOnScreen(CoordinatorLayout coordinatorLayout, View view, Rect rect, boolean z2) {
            View viewFindFirstDependency = findFirstDependency(coordinatorLayout.getDependencies(view));
            if (viewFindFirstDependency != null) {
                if (shouldHeaderOverlapScrollingChild() && (viewFindFirstDependency instanceof COUIAppBarLayout)) {
                    this.tempRect3.set(rect);
                    this.tempRect3.offset(Math.round(view.getX()), Math.round(view.getY()));
                    this.tempRect1.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());
                    if (!this.tempRect1.contains(this.tempRect3)) {
                        ((COUIAppBarLayout) viewFindFirstDependency).setExpanded(false, !z2);
                        return true;
                    }
                } else {
                    rect.offset(view.getLeft(), view.getTop());
                    Rect rect2 = this.tempRect1;
                    rect2.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());
                    if (!rect2.contains(rect)) {
                        if (viewFindFirstDependency instanceof COUIAppBarLayout) {
                            ((COUIAppBarLayout) viewFindFirstDependency).setExpanded(false, !z2);
                        } else if (viewFindFirstDependency instanceof AppBarLayout) {
                            ((AppBarLayout) viewFindFirstDependency).setExpanded(false, !z2);
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setHorizontalOffsetEnabled(boolean z2) {
            super.setHorizontalOffsetEnabled(z2);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean setLeftAndRightOffset(int i3) {
            return super.setLeftAndRightOffset(i3);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setOnAnimateUpdateListener(COUIViewOffsetHelper.OnAnimateUpdateListener onAnimateUpdateListener) {
            super.setOnAnimateUpdateListener(onAnimateUpdateListener);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i3) {
            return super.setTopAndBottomOffset(i3);
        }

        @Override // com.google.android.material.appbar.COUIViewOffsetBehavior
        public /* bridge */ /* synthetic */ void setVerticalOffsetEnabled(boolean z2) {
            super.setVerticalOffsetEnabled(z2);
        }

        public ScrollingViewBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.tempRect3 = new Rect();
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ScrollingViewBehavior_Layout);
            setOverlayTop(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public COUIAppBarLayout(Context context, AttributeSet attributeSet) {
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
        if (!this.liftOnScroll || !(getBackground() instanceof MaterialShapeDrawable)) {
            return true;
        }
        startLiftOnScrollElevationOverlayAnimation((MaterialShapeDrawable) getBackground(), z2);
        return true;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public COUIAppBarLayout(Context context, AttributeSet attributeSet, int i3) {
        int i4 = DEF_STYLE_RES;
        super(MaterialThemeOverlay.wrap(context, attributeSet, i3, i4), attributeSet, i3);
        this.totalScrollRange = -1;
        this.downPreScrollRange = -1;
        this.downScrollRange = -1;
        this.pendingAction = 0;
        this.liftOnScrollListeners = new ArrayList();
        Context context2 = getContext();
        setOrientation(1);
        if (getOutlineProvider() == ViewOutlineProvider.BACKGROUND) {
            ViewUtilsLollipop.setBoundsViewOutlineProvider(this);
        }
        ViewUtilsLollipop.setStateListAnimatorFromAttrs(this, attributeSet, i3, i4);
        TypedArray typedArrayObtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R.styleable.AppBarLayout, i3, i4, new int[0]);
        ViewCompat.setBackground(this, typedArrayObtainStyledAttributes.getDrawable(R.styleable.AppBarLayout_android_background));
        if (getBackground() instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) getBackground();
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
            materialShapeDrawable.setFillColor(ColorStateList.valueOf(colorDrawable.getColor()));
            materialShapeDrawable.initializeElevationOverlay(context2);
            ViewCompat.setBackground(this, materialShapeDrawable);
        }
        int i5 = R.styleable.AppBarLayout_expanded;
        if (typedArrayObtainStyledAttributes.hasValue(i5)) {
            setExpanded(typedArrayObtainStyledAttributes.getBoolean(i5, false), false, false);
        }
        int i6 = R.styleable.AppBarLayout_elevation;
        if (typedArrayObtainStyledAttributes.hasValue(i6)) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, typedArrayObtainStyledAttributes.getDimensionPixelSize(i6, 0));
        }
        int i7 = R.styleable.AppBarLayout_android_keyboardNavigationCluster;
        if (typedArrayObtainStyledAttributes.hasValue(i7)) {
            setKeyboardNavigationCluster(typedArrayObtainStyledAttributes.getBoolean(i7, false));
        }
        int i8 = R.styleable.AppBarLayout_android_touchscreenBlocksFocus;
        if (typedArrayObtainStyledAttributes.hasValue(i8)) {
            setTouchscreenBlocksFocus(typedArrayObtainStyledAttributes.getBoolean(i8, false));
        }
        this.liftOnScroll = typedArrayObtainStyledAttributes.getBoolean(R.styleable.AppBarLayout_liftOnScroll, false);
        this.liftOnScrollTargetViewId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.AppBarLayout_liftOnScrollTargetViewId, -1);
        setStatusBarForeground(typedArrayObtainStyledAttributes.getDrawable(R.styleable.AppBarLayout_statusBarForeground));
        typedArrayObtainStyledAttributes.recycle();
        ViewCompat.setOnApplyWindowInsetsListener(this, new AnonymousClass1());
    }

    private void setExpanded(boolean z2, boolean z3, boolean z4) {
        if (DEBUG) {
            COUILog.d(TAG, "setExpanded : " + z2);
        }
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

    public static class LayoutParams extends LinearLayout.LayoutParams {
        static final int COLLAPSIBLE_FLAGS = 10;
        static final int FLAG_QUICK_RETURN = 5;
        static final int FLAG_SNAP = 17;
        private static final int SCROLL_EFFECT_COMPRESS = 1;
        private static final int SCROLL_EFFECT_NONE = 0;
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
        public @interface ScrollFlags {
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.scrollFlags = 1;
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AppBarLayout_Layout);
            this.scrollFlags = typedArrayObtainStyledAttributes.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
            setScrollEffect(createScrollEffectFromInt(typedArrayObtainStyledAttributes.getInt(R.styleable.AppBarLayout_Layout_layout_scrollEffect, 0)));
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
            this.scrollInterpolator = layoutParams.scrollInterpolator;
        }
    }
}