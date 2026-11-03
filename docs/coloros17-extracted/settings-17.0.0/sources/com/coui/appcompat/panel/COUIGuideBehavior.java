package com.coui.appcompat.panel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import com.coui.appcompat.view.MaterialResource;
import com.google.android.material.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public class COUIGuideBehavior<V extends View> extends BottomSheetBehavior<V> {
    private static final int CORNER_ANIMATION_DURATION = 500;
    private static final int DEF_STYLE_RES = R.style.Widget_Design_BottomSheet_Modal;
    private static final float HIDE_FRICTION = 0.1f;
    private static final float HIDE_THRESHOLD = 0.5f;
    public static final int PEEK_HEIGHT_AUTO = -1;
    public static final int SAVE_ALL = -1;
    public static final int SAVE_FIT_TO_CONTENTS = 2;
    public static final int SAVE_HIDEABLE = 4;
    public static final int SAVE_NONE = 0;
    public static final int SAVE_PEEK_HEIGHT = 1;
    public static final int SAVE_SKIP_COLLAPSED = 8;
    private static final int SIGNIFICANT_VEL_THRESHOLD = 500;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_HALF_EXPANDED = 6;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "BottomSheetBehavior";
    private static final int VERTICAL_SLIDING_PARAMETER_THRESHOLD = 2;
    int activePointerId;
    private final ArrayList<COUIBottomSheetCallback> callbacks;
    int collapsedOffset;
    private final ViewDragHelper.Callback dragCallback;
    private boolean draggable;
    float elevation;
    int expandedOffset;
    private boolean fitToContents;
    int fitToContentsOffset;
    private boolean gestureInsetBottomIgnored;
    int halfExpandedOffset;
    float halfExpandedRatio;
    boolean hideable;
    private boolean ignoreEvents;
    private Map<View, Integer> importantForAccessibilityMap;
    private int initialX;
    private int initialY;
    private ValueAnimator interpolatorAnimator;
    private boolean isShapeExpanded;
    private int lastNestedScrollDy;
    COUIPanelDragListener mCOUIPanelDragListener;
    private boolean mCanHideKeyboard;
    private boolean mIsIgnoreExpandedOffsetChange;
    private COUIPanelPullUpListener mPullUpListener;
    private MaterialShapeDrawable materialShapeDrawable;
    private float maximumVelocity;
    private boolean nestedScrolled;
    WeakReference<View> nestedScrollingChildRef;
    int parentHeight;
    int parentWidth;
    private int peekHeight;
    private boolean peekHeightAuto;
    private int peekHeightMin;
    private int saveFlags;
    private COUIGuideBehavior<V>.SettleRunnable settleRunnable;
    private ShapeAppearanceModel shapeAppearanceModelDefault;
    private boolean shapeThemingEnabled;
    private boolean skipCollapsed;
    int state;
    boolean touchingScrollingChild;
    private boolean updateImportantForAccessibilityOnSiblings;
    private VelocityTracker velocityTracker;
    ViewDragHelper viewDragHelper;
    WeakReference<V> viewRef;

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIGuideBehavior$1, reason: invalid class name */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ View val$child;
        final /* synthetic */ int val$finalState;

        public AnonymousClass1(View view, int i3) {
            this.val$child = view;
            this.val$finalState = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIGuideBehavior.this.settleToState(this.val$child, this.val$finalState);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIGuideBehavior$2, reason: invalid class name */
    public class AnonymousClass2 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass2() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUIGuideBehavior.access$000(COUIGuideBehavior.this) != null) {
                COUIGuideBehavior.access$000(COUIGuideBehavior.this).setInterpolation(fFloatValue);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIGuideBehavior$3, reason: invalid class name */
    public class AnonymousClass3 extends ViewDragHelper.Callback {
        public AnonymousClass3() {
        }

        private boolean releasedLow(View view) {
            int top = view.getTop();
            COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
            return top > (cOUIGuideBehavior.parentHeight + cOUIGuideBehavior.getExpandedOffset()) / 2;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view, int i3, int i4) {
            return view.getLeft();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionVertical(View view, int i3, int i4) {
            int iOnDragging;
            int i5;
            if (COUIGuideBehavior.access$300(COUIGuideBehavior.this) == null || ((i5 = COUIGuideBehavior.this.state) != 3 && (i5 != 1 || view.getTop() > COUIGuideBehavior.this.getExpandedOffset()))) {
                iOnDragging = 0;
            } else {
                COUIGuideBehavior.access$602(COUIGuideBehavior.this, true);
                iOnDragging = COUIGuideBehavior.access$300(COUIGuideBehavior.this).onDragging(i4, COUIGuideBehavior.this.getExpandedOffset());
            }
            int expandedOffset = COUIGuideBehavior.this.getExpandedOffset() - iOnDragging;
            COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
            return MathUtils.clamp(i3, expandedOffset, cOUIGuideBehavior.hideable ? cOUIGuideBehavior.parentHeight : cOUIGuideBehavior.collapsedOffset);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int getViewVerticalDragRange(View view) {
            COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
            return cOUIGuideBehavior.hideable ? cOUIGuideBehavior.parentHeight : cOUIGuideBehavior.collapsedOffset;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewDragStateChanged(int i3) {
            if (i3 == 1 && COUIGuideBehavior.access$200(COUIGuideBehavior.this)) {
                COUIGuideBehavior.this.setStateInternal(1);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View view, int i3, int i4, int i5, int i6) {
            COUIGuideBehavior.this.dispatchOnSlide(i4);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewReleased(View view, float f3, float f4) {
            int i3;
            if (COUIGuideBehavior.access$300(COUIGuideBehavior.this) != null && COUIGuideBehavior.this.parentHeight - view.getHeight() < COUIGuideBehavior.this.getExpandedOffset() && view.getTop() < COUIGuideBehavior.this.getExpandedOffset()) {
                COUIGuideBehavior.access$300(COUIGuideBehavior.this).onReleased(COUIGuideBehavior.this.getExpandedOffset());
                return;
            }
            int i4 = 6;
            if (f4 < 0.0f) {
                if (COUIGuideBehavior.access$400(COUIGuideBehavior.this)) {
                    i3 = COUIGuideBehavior.this.fitToContentsOffset;
                } else {
                    int top = view.getTop();
                    COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
                    int i5 = cOUIGuideBehavior.halfExpandedOffset;
                    if (top > i5) {
                        i3 = i5;
                    } else {
                        i3 = cOUIGuideBehavior.expandedOffset;
                    }
                }
                i4 = 3;
            } else {
                COUIGuideBehavior cOUIGuideBehavior2 = COUIGuideBehavior.this;
                if (cOUIGuideBehavior2.hideable && cOUIGuideBehavior2.shouldHide(view, f4)) {
                    COUIPanelDragListener cOUIPanelDragListener = COUIGuideBehavior.this.mCOUIPanelDragListener;
                    if (cOUIPanelDragListener != null && cOUIPanelDragListener.onDragWhileEditing()) {
                        COUIGuideBehavior cOUIGuideBehavior3 = COUIGuideBehavior.this;
                        int i6 = cOUIGuideBehavior3.fitToContentsOffset;
                        COUIGuideBehavior.access$502(cOUIGuideBehavior3, false);
                        i3 = i6;
                    } else if ((Math.abs(f3) < Math.abs(f4) && f4 > 500.0f) || releasedLow(view)) {
                        COUIGuideBehavior cOUIGuideBehavior4 = COUIGuideBehavior.this;
                        int i7 = cOUIGuideBehavior4.parentHeight;
                        COUIGuideBehavior.access$502(cOUIGuideBehavior4, true);
                        i4 = 5;
                        i3 = i7;
                    } else if (COUIGuideBehavior.access$400(COUIGuideBehavior.this)) {
                        i3 = COUIGuideBehavior.this.fitToContentsOffset;
                    } else if (Math.abs(view.getTop() - COUIGuideBehavior.this.expandedOffset) < Math.abs(view.getTop() - COUIGuideBehavior.this.halfExpandedOffset)) {
                        i3 = COUIGuideBehavior.this.expandedOffset;
                    } else {
                        i3 = COUIGuideBehavior.this.halfExpandedOffset;
                    }
                    i4 = 3;
                } else if (f4 == 0.0f || Math.abs(f3) > Math.abs(f4)) {
                    int top2 = view.getTop();
                    if (!COUIGuideBehavior.access$400(COUIGuideBehavior.this)) {
                        COUIGuideBehavior cOUIGuideBehavior5 = COUIGuideBehavior.this;
                        int i8 = cOUIGuideBehavior5.halfExpandedOffset;
                        if (top2 < i8) {
                            if (top2 < Math.abs(top2 - cOUIGuideBehavior5.collapsedOffset)) {
                                i3 = COUIGuideBehavior.this.expandedOffset;
                                i4 = 3;
                            } else {
                                i3 = COUIGuideBehavior.this.halfExpandedOffset;
                            }
                        } else if (Math.abs(top2 - i8) < Math.abs(top2 - COUIGuideBehavior.this.collapsedOffset)) {
                            i3 = COUIGuideBehavior.this.halfExpandedOffset;
                        } else {
                            i3 = COUIGuideBehavior.this.collapsedOffset;
                            i4 = 4;
                        }
                    } else if (Math.abs(top2 - COUIGuideBehavior.this.fitToContentsOffset) < Math.abs(top2 - COUIGuideBehavior.this.collapsedOffset)) {
                        i3 = COUIGuideBehavior.this.fitToContentsOffset;
                        i4 = 3;
                    } else {
                        i3 = COUIGuideBehavior.this.collapsedOffset;
                        i4 = 4;
                    }
                } else {
                    if (COUIGuideBehavior.access$400(COUIGuideBehavior.this)) {
                        i3 = COUIGuideBehavior.this.collapsedOffset;
                    } else {
                        int top3 = view.getTop();
                        if (Math.abs(top3 - COUIGuideBehavior.this.halfExpandedOffset) < Math.abs(top3 - COUIGuideBehavior.this.collapsedOffset)) {
                            i3 = COUIGuideBehavior.this.halfExpandedOffset;
                        } else {
                            i3 = COUIGuideBehavior.this.collapsedOffset;
                        }
                    }
                    i4 = 4;
                }
            }
            COUIGuideBehavior.this.startSettlingAnimation(view, i4, i3, true);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View view, int i3) {
            COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
            int i4 = cOUIGuideBehavior.state;
            if (i4 == 1 || cOUIGuideBehavior.touchingScrollingChild) {
                return false;
            }
            if (i4 == 3 && cOUIGuideBehavior.activePointerId == i3) {
                WeakReference<View> weakReference = cOUIGuideBehavior.nestedScrollingChildRef;
                View view2 = weakReference != null ? weakReference.get() : null;
                if (view2 != null && view2.canScrollVertically(-1)) {
                    return false;
                }
            }
            WeakReference<V> weakReference2 = COUIGuideBehavior.this.viewRef;
            return weakReference2 != null && weakReference2.get() == view;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIGuideBehavior$4, reason: invalid class name */
    public class AnonymousClass4 implements AccessibilityViewCommand {
        final /* synthetic */ int val$state;

        public AnonymousClass4(int i3) {
            this.val$state = i3;
        }

        @Override // androidx.core.view.accessibility.AccessibilityViewCommand
        public boolean perform(View view, AccessibilityViewCommand.CommandArguments commandArguments) {
            COUIGuideBehavior.this.setState(this.val$state);
            return true;
        }
    }

    public static abstract class COUIBottomSheetCallback {
        public abstract void onSlide(View view, float f3);

        public abstract void onStateChanged(View view, int i3);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SaveFlags {
    }

    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        boolean fitToContents;
        boolean hideable;
        int peekHeight;
        boolean skipCollapsed;
        final int state;

        /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIGuideBehavior$SavedState$1, reason: invalid class name */
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
                return new SavedState(parcel, (ClassLoader) null);
            }
        }

        public SavedState(Parcel parcel) {
            this(parcel, (ClassLoader) null);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeInt(this.state);
            parcel.writeInt(this.peekHeight);
            parcel.writeInt(this.fitToContents ? 1 : 0);
            parcel.writeInt(this.hideable ? 1 : 0);
            parcel.writeInt(this.skipCollapsed ? 1 : 0);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.state = parcel.readInt();
            this.peekHeight = parcel.readInt();
            this.fitToContents = parcel.readInt() == 1;
            this.hideable = parcel.readInt() == 1;
            this.skipCollapsed = parcel.readInt() == 1;
        }

        public SavedState(Parcelable parcelable, COUIGuideBehavior<?> cOUIGuideBehavior) {
            super(parcelable);
            this.state = cOUIGuideBehavior.state;
            this.peekHeight = COUIGuideBehavior.access$700((COUIGuideBehavior) cOUIGuideBehavior);
            this.fitToContents = COUIGuideBehavior.access$400((COUIGuideBehavior) cOUIGuideBehavior);
            this.hideable = cOUIGuideBehavior.hideable;
            this.skipCollapsed = COUIGuideBehavior.access$800((COUIGuideBehavior) cOUIGuideBehavior);
        }

        @Deprecated
        public SavedState(Parcelable parcelable, int i3) {
            super(parcelable);
            this.state = i3;
        }
    }

    public class SettleRunnable implements Runnable {
        private boolean isPosted;
        int targetState;
        private final View view;

        public SettleRunnable(View view, int i3) {
            this.view = view;
            this.targetState = i3;
        }

        public static /* synthetic */ boolean access$100(SettleRunnable settleRunnable) {
            return settleRunnable.isPosted;
        }

        public static /* synthetic */ boolean access$102(SettleRunnable settleRunnable, boolean z2) {
            settleRunnable.isPosted = z2;
            return z2;
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewDragHelper viewDragHelper = COUIGuideBehavior.this.viewDragHelper;
            if (viewDragHelper == null || !viewDragHelper.continueSettling(true)) {
                COUIGuideBehavior.this.setStateInternal(this.targetState);
            } else {
                ViewCompat.postOnAnimation(this.view, this);
            }
            this.isPosted = false;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public COUIGuideBehavior() {
        this.saveFlags = 0;
        this.fitToContents = true;
        this.updateImportantForAccessibilityOnSiblings = false;
        this.settleRunnable = null;
        this.halfExpandedRatio = 0.5f;
        this.elevation = -1.0f;
        this.draggable = true;
        this.state = 4;
        this.callbacks = new ArrayList<>();
        this.dragCallback = new AnonymousClass3();
    }

    public static /* synthetic */ MaterialShapeDrawable access$000(COUIGuideBehavior cOUIGuideBehavior) {
        return cOUIGuideBehavior.materialShapeDrawable;
    }

    public static /* synthetic */ boolean access$200(COUIGuideBehavior cOUIGuideBehavior) {
        return cOUIGuideBehavior.draggable;
    }

    public static /* synthetic */ COUIPanelPullUpListener access$300(COUIGuideBehavior cOUIGuideBehavior) {
        return cOUIGuideBehavior.mPullUpListener;
    }

    public static /* synthetic */ boolean access$400(COUIGuideBehavior cOUIGuideBehavior) {
        return cOUIGuideBehavior.fitToContents;
    }

    public static /* synthetic */ boolean access$502(COUIGuideBehavior cOUIGuideBehavior, boolean z2) {
        cOUIGuideBehavior.mCanHideKeyboard = z2;
        return z2;
    }

    public static /* synthetic */ boolean access$602(COUIGuideBehavior cOUIGuideBehavior, boolean z2) {
        cOUIGuideBehavior.mIsIgnoreExpandedOffsetChange = z2;
        return z2;
    }

    public static /* synthetic */ int access$700(COUIGuideBehavior cOUIGuideBehavior) {
        return cOUIGuideBehavior.peekHeight;
    }

    public static /* synthetic */ boolean access$800(COUIGuideBehavior cOUIGuideBehavior) {
        return cOUIGuideBehavior.skipCollapsed;
    }

    private void addAccessibilityActionForState(V v2, AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat, int i3) {
        ViewCompat.replaceAccessibilityAction(v2, accessibilityActionCompat, null, new AnonymousClass4(i3));
    }

    private void calculateCollapsedOffset() {
        int iCalculatePeekHeight = calculatePeekHeight();
        if (this.fitToContents) {
            this.collapsedOffset = Math.max(this.parentHeight - iCalculatePeekHeight, this.fitToContentsOffset);
        } else {
            this.collapsedOffset = this.parentHeight - iCalculatePeekHeight;
        }
    }

    private void calculateHalfExpandedOffset() {
        this.halfExpandedOffset = (int) (this.parentHeight * (1.0f - this.halfExpandedRatio));
    }

    private int calculatePeekHeight() {
        return this.peekHeightAuto ? Math.max(this.peekHeightMin, this.parentHeight - ((this.parentWidth * 9) / 16)) : this.peekHeight;
    }

    private void createMaterialShapeDrawable(Context context, AttributeSet attributeSet, boolean z2) {
        createMaterialShapeDrawable(context, attributeSet, z2, null);
    }

    private void createShapeValueAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.interpolatorAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(500L);
        this.interpolatorAnimator.addUpdateListener(new AnonymousClass2());
    }

    public static <V extends View> COUIGuideBehavior<V> from(V v2) {
        ViewGroup.LayoutParams layoutParams = v2.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (behavior instanceof BottomSheetBehavior) {
            return (COUIGuideBehavior) behavior;
        }
        throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
    }

    private float getYVelocity() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
        return this.velocityTracker.getYVelocity(this.activePointerId);
    }

    private void reset() {
        this.activePointerId = -1;
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.velocityTracker = null;
        }
    }

    private void restoreOptionalState(SavedState savedState) {
        int i3 = this.saveFlags;
        if (i3 == 0) {
            return;
        }
        if (i3 == -1 || (i3 & 1) == 1) {
            this.peekHeight = savedState.peekHeight;
        }
        if (i3 == -1 || (i3 & 2) == 2) {
            this.fitToContents = savedState.fitToContents;
        }
        if (i3 == -1 || (i3 & 4) == 4) {
            this.hideable = savedState.hideable;
        }
        if (i3 == -1 || (i3 & 8) == 8) {
            this.skipCollapsed = savedState.skipCollapsed;
        }
    }

    private void setPanelPeekHeight(int i3, boolean z2) {
        V v2;
        if (i3 == -1) {
            if (this.peekHeightAuto) {
                return;
            } else {
                this.peekHeightAuto = true;
            }
        } else {
            if (!this.peekHeightAuto && this.peekHeight == i3) {
                return;
            }
            this.peekHeightAuto = false;
            this.peekHeight = Math.max(0, i3);
        }
        if (this.viewRef != null) {
            calculateCollapsedOffset();
            if (this.state != 4 || (v2 = this.viewRef.get()) == null) {
                return;
            }
            if (z2) {
                settleToStatePendingLayout(this.state);
            } else {
                v2.requestLayout();
            }
        }
    }

    private void setSystemGestureInsets(CoordinatorLayout coordinatorLayout) {
        WindowInsets rootWindowInsets;
        if (isGestureInsetBottomIgnored() || (rootWindowInsets = coordinatorLayout.getRootWindowInsets()) == null) {
            return;
        }
        this.peekHeight += rootWindowInsets.getSystemGestureInsets().bottom;
    }

    private void settleToStatePendingLayout(int i3) {
        V v2 = this.viewRef.get();
        if (v2 == null) {
            return;
        }
        ViewParent parent = v2.getParent();
        if (parent != null && parent.isLayoutRequested() && ViewCompat.isAttachedToWindow(v2)) {
            v2.post(new AnonymousClass1(v2, i3));
        } else {
            settleToState(v2, i3);
        }
    }

    private void updateAccessibilityActions() {
        V v2;
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference == null || (v2 = weakReference.get()) == null) {
            return;
        }
        ViewCompat.removeAccessibilityAction(v2, 524288);
        ViewCompat.removeAccessibilityAction(v2, 262144);
        ViewCompat.removeAccessibilityAction(v2, 1048576);
        if (this.hideable && this.state != 5) {
            addAccessibilityActionForState(v2, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, 5);
        }
        int i3 = this.state;
        if (i3 == 3) {
            addAccessibilityActionForState(v2, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, this.fitToContents ? 4 : 6);
            return;
        }
        if (i3 == 4) {
            addAccessibilityActionForState(v2, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, this.fitToContents ? 3 : 6);
        } else {
            if (i3 != 6) {
                return;
            }
            addAccessibilityActionForState(v2, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, 4);
            addAccessibilityActionForState(v2, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, 3);
        }
    }

    private void updateDrawableForTargetState(int i3) {
        ValueAnimator valueAnimator;
        if (i3 == 2) {
            return;
        }
        boolean z2 = i3 == 3;
        if (this.isShapeExpanded != z2) {
            this.isShapeExpanded = z2;
            if (this.materialShapeDrawable == null || (valueAnimator = this.interpolatorAnimator) == null) {
                return;
            }
            if (valueAnimator.isRunning()) {
                this.interpolatorAnimator.reverse();
                return;
            }
            float f3 = z2 ? 0.0f : 1.0f;
            this.interpolatorAnimator.setFloatValues(1.0f - f3, f3);
            this.interpolatorAnimator.start();
        }
    }

    private void updateImportantForAccessibility(boolean z2) {
        Map<View, Integer> map;
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference == null) {
            return;
        }
        ViewParent parent = weakReference.get().getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (z2) {
                if (this.importantForAccessibilityMap != null) {
                    return;
                } else {
                    this.importantForAccessibilityMap = new HashMap(childCount);
                }
            }
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = coordinatorLayout.getChildAt(i3);
                if (childAt != this.viewRef.get()) {
                    if (z2) {
                        this.importantForAccessibilityMap.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                        if (this.updateImportantForAccessibilityOnSiblings) {
                            ViewCompat.setImportantForAccessibility(childAt, 4);
                        }
                    } else if (this.updateImportantForAccessibilityOnSiblings && (map = this.importantForAccessibilityMap) != null && map.containsKey(childAt)) {
                        ViewCompat.setImportantForAccessibility(childAt, this.importantForAccessibilityMap.get(childAt).intValue());
                    }
                }
            }
            if (z2) {
                return;
            }
            this.importantForAccessibilityMap = null;
        }
    }

    public void addBottomSheetCallback(COUIBottomSheetCallback cOUIBottomSheetCallback) {
        if (this.callbacks.contains(cOUIBottomSheetCallback)) {
            return;
        }
        this.callbacks.add(cOUIBottomSheetCallback);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void disableShapeAnimations() {
        this.interpolatorAnimator = null;
    }

    public void dispatchOnSlide(int i3) {
        float f3;
        float expandedOffset;
        V v2 = this.viewRef.get();
        if (v2 == null || this.callbacks.isEmpty()) {
            return;
        }
        int i4 = this.collapsedOffset;
        if (i3 > i4 || i4 == getExpandedOffset()) {
            int i5 = this.collapsedOffset;
            f3 = i5 - i3;
            expandedOffset = this.parentHeight - i5;
        } else {
            int i6 = this.collapsedOffset;
            f3 = i6 - i3;
            expandedOffset = i6 - getExpandedOffset();
        }
        float f4 = f3 / expandedOffset;
        for (int i7 = 0; i7 < this.callbacks.size(); i7++) {
            this.callbacks.get(i7).onSlide(v2, f4);
        }
    }

    public View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view) && view.getVisibility() == 0) {
            return view;
        }
        if (!(view instanceof ViewGroup) || view.getVisibility() != 0) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View viewFindScrollingChild = findScrollingChild(viewGroup.getChildAt(i3));
            if (viewFindScrollingChild != null) {
                return viewFindScrollingChild;
            }
        }
        return null;
    }

    public COUIPanelDragListener getCOUIPanelDragListener() {
        return this.mCOUIPanelDragListener;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public int getExpandedOffset() {
        return this.fitToContents ? this.fitToContentsOffset : this.expandedOffset;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public float getHalfExpandedRatio() {
        return this.halfExpandedRatio;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public int getPeekHeight() {
        if (this.peekHeightAuto) {
            return -1;
        }
        return this.peekHeight;
    }

    public int getPeekHeightMin() {
        return this.peekHeightMin;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public int getSaveFlags() {
        return this.saveFlags;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public boolean getSkipCollapsed() {
        return this.skipCollapsed;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public int getState() {
        return this.state;
    }

    public boolean isCanHideKeyboard() {
        return this.mCanHideKeyboard;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public boolean isDraggable() {
        return this.draggable;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public boolean isFitToContents() {
        return this.fitToContents;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public boolean isGestureInsetBottomIgnored() {
        return this.gestureInsetBottomIgnored;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public boolean isHideable() {
        return this.hideable;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams layoutParams) {
        super.onAttachedToLayoutParams(layoutParams);
        this.viewRef = null;
        this.viewDragHelper = null;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        this.viewRef = null;
        this.viewDragHelper = null;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v2, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper;
        if (!v2.isShown() || !this.draggable) {
            this.ignoreEvents = true;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (actionMasked == 0) {
            this.initialX = (int) motionEvent.getX();
            this.initialY = (int) motionEvent.getY();
            if (this.state != 2) {
                WeakReference<View> weakReference = this.nestedScrollingChildRef;
                View view = weakReference != null ? weakReference.get() : null;
                if (view != null && coordinatorLayout.isPointInChildBounds(view, this.initialX, this.initialY)) {
                    this.activePointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                    this.touchingScrollingChild = true;
                }
            }
            this.ignoreEvents = this.activePointerId == -1 && !coordinatorLayout.isPointInChildBounds(v2, this.initialX, this.initialY);
        } else if (actionMasked == 1) {
            COUIPanelPullUpListener cOUIPanelPullUpListener = this.mPullUpListener;
            if (cOUIPanelPullUpListener != null) {
                cOUIPanelPullUpListener.onCancel();
            }
        } else if (actionMasked == 3) {
            this.touchingScrollingChild = false;
            this.activePointerId = -1;
            if (this.ignoreEvents) {
                this.ignoreEvents = false;
                return false;
            }
        }
        if (!this.ignoreEvents && (viewDragHelper = this.viewDragHelper) != null && viewDragHelper.shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        if (Math.abs(this.initialY - motionEvent.getY()) > Math.abs(this.initialX - motionEvent.getX()) * 2.0f && this.viewDragHelper != null && Math.abs(this.initialY - motionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
            return true;
        }
        WeakReference<View> weakReference2 = this.nestedScrollingChildRef;
        View view2 = weakReference2 != null ? weakReference2.get() : null;
        return (actionMasked != 2 || view2 == null || this.ignoreEvents || this.state == 1 || coordinatorLayout.isPointInChildBounds(view2, (int) motionEvent.getX(), (int) motionEvent.getY()) || this.viewDragHelper == null || Math.abs(((float) this.initialY) - motionEvent.getY()) <= ((float) this.viewDragHelper.getTouchSlop())) ? false : true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v2, int i3) {
        MaterialShapeDrawable materialShapeDrawable;
        if (ViewCompat.getFitsSystemWindows(coordinatorLayout) && !ViewCompat.getFitsSystemWindows(v2)) {
            v2.setFitsSystemWindows(true);
        }
        if (this.viewRef == null) {
            this.peekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            setSystemGestureInsets(coordinatorLayout);
            this.viewRef = new WeakReference<>(v2);
            if (this.shapeThemingEnabled && (materialShapeDrawable = this.materialShapeDrawable) != null) {
                ViewCompat.setBackground(v2, materialShapeDrawable);
            }
            MaterialShapeDrawable materialShapeDrawable2 = this.materialShapeDrawable;
            if (materialShapeDrawable2 != null) {
                float elevation = this.elevation;
                if (elevation == -1.0f) {
                    elevation = ViewCompat.getElevation(v2);
                }
                materialShapeDrawable2.setElevation(elevation);
                boolean z2 = this.state == 3;
                this.isShapeExpanded = z2;
                this.materialShapeDrawable.setInterpolation(z2 ? 0.0f : 1.0f);
            }
            updateAccessibilityActions();
            if (ViewCompat.getImportantForAccessibility(v2) == 0) {
                ViewCompat.setImportantForAccessibility(v2, 1);
            }
        }
        if (this.viewDragHelper == null) {
            this.viewDragHelper = ViewDragHelper.create(coordinatorLayout, this.dragCallback);
        }
        int top = v2.getTop();
        coordinatorLayout.onLayoutChild(v2, i3);
        this.parentWidth = coordinatorLayout.getWidth();
        int height = coordinatorLayout.getHeight();
        this.parentHeight = height;
        if (!this.mIsIgnoreExpandedOffsetChange) {
            this.fitToContentsOffset = Math.max(0, height - v2.getHeight());
        }
        this.mIsIgnoreExpandedOffsetChange = false;
        calculateHalfExpandedOffset();
        calculateCollapsedOffset();
        int i4 = this.state;
        if (i4 == 3) {
            ViewCompat.offsetTopAndBottom(v2, getExpandedOffset());
        } else if (i4 == 6) {
            ViewCompat.offsetTopAndBottom(v2, this.halfExpandedOffset);
        } else if (this.hideable && i4 == 5) {
            ViewCompat.offsetTopAndBottom(v2, this.parentHeight);
        } else if (i4 == 4) {
            ViewCompat.offsetTopAndBottom(v2, this.collapsedOffset);
        } else if (i4 == 1 || i4 == 2) {
            ViewCompat.offsetTopAndBottom(v2, top - v2.getTop());
        }
        this.nestedScrollingChildRef = new WeakReference<>(findScrollingChild(v2));
        return true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v2, View view, float f3, float f4) {
        WeakReference<View> weakReference = this.nestedScrollingChildRef;
        return weakReference != null && view == weakReference.get() && (this.state != 3 || super.onNestedPreFling(coordinatorLayout, v2, view, f3, f4));
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v2, View view, int i3, int i4, int[] iArr, int i5) {
        if (i5 == 1) {
            return;
        }
        WeakReference<View> weakReference = this.nestedScrollingChildRef;
        if (view != (weakReference != null ? weakReference.get() : null)) {
            return;
        }
        int top = v2.getTop();
        int i6 = top - i4;
        if (i4 > 0) {
            if (i6 < getExpandedOffset()) {
                int expandedOffset = top - getExpandedOffset();
                iArr[1] = expandedOffset;
                ViewCompat.offsetTopAndBottom(v2, -expandedOffset);
                setStateInternal(3);
            } else {
                if (!this.draggable) {
                    return;
                }
                iArr[1] = i4;
                ViewCompat.offsetTopAndBottom(v2, -i4);
                setStateInternal(1);
            }
        } else if (i4 < 0 && !view.canScrollVertically(-1)) {
            int i7 = this.collapsedOffset;
            if (i6 > i7 && !this.hideable) {
                int i8 = top - i7;
                iArr[1] = i8;
                ViewCompat.offsetTopAndBottom(v2, -i8);
                setStateInternal(4);
            } else {
                if (!this.draggable) {
                    return;
                }
                iArr[1] = i4;
                ViewCompat.offsetTopAndBottom(v2, -i4);
                setStateInternal(1);
            }
        }
        dispatchOnSlide(v2.getTop());
        this.lastNestedScrollDy = i4;
        this.nestedScrolled = true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v2, View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v2, Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v2, savedState.getSuperState());
        restoreOptionalState(savedState);
        int i3 = savedState.state;
        if (i3 == 1 || i3 == 2) {
            this.state = 4;
        } else {
            this.state = i3;
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v2) {
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v2), (COUIGuideBehavior<?>) this);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v2, View view, View view2, int i3, int i4) {
        this.lastNestedScrollDy = 0;
        this.nestedScrolled = false;
        return (i3 & 2) != 0;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v2, View view, int i3) {
        int i4;
        int i5 = 3;
        if (v2.getTop() == getExpandedOffset()) {
            setStateInternal(3);
            return;
        }
        WeakReference<View> weakReference = this.nestedScrollingChildRef;
        if (weakReference != null && view == weakReference.get() && this.nestedScrolled) {
            if (this.lastNestedScrollDy > 0) {
                if (this.fitToContents) {
                    i4 = this.fitToContentsOffset;
                } else {
                    int top = v2.getTop();
                    int i6 = this.halfExpandedOffset;
                    if (top > i6) {
                        i5 = 6;
                        i4 = i6;
                    } else {
                        i4 = this.expandedOffset;
                    }
                }
            } else if (this.hideable && shouldHide(v2, getYVelocity())) {
                COUIPanelDragListener cOUIPanelDragListener = this.mCOUIPanelDragListener;
                if (cOUIPanelDragListener == null || !cOUIPanelDragListener.onDragWhileEditing()) {
                    i4 = this.parentHeight;
                    this.mCanHideKeyboard = true;
                    i5 = 5;
                } else {
                    i4 = this.fitToContentsOffset;
                    this.mCanHideKeyboard = false;
                }
            } else if (this.lastNestedScrollDy == 0) {
                int top2 = v2.getTop();
                if (!this.fitToContents) {
                    int i7 = this.halfExpandedOffset;
                    if (top2 < i7) {
                        if (top2 < Math.abs(top2 - this.collapsedOffset)) {
                            i4 = this.expandedOffset;
                        } else {
                            i4 = this.halfExpandedOffset;
                        }
                    } else if (Math.abs(top2 - i7) < Math.abs(top2 - this.collapsedOffset)) {
                        i4 = this.halfExpandedOffset;
                    } else {
                        i4 = this.collapsedOffset;
                        i5 = 4;
                    }
                    i5 = 6;
                } else if (Math.abs(top2 - this.fitToContentsOffset) < Math.abs(top2 - this.collapsedOffset)) {
                    i4 = this.fitToContentsOffset;
                } else {
                    i4 = this.collapsedOffset;
                    i5 = 4;
                }
            } else {
                if (this.fitToContents) {
                    i4 = this.collapsedOffset;
                } else {
                    int top3 = v2.getTop();
                    if (Math.abs(top3 - this.halfExpandedOffset) < Math.abs(top3 - this.collapsedOffset)) {
                        i4 = this.halfExpandedOffset;
                        i5 = 6;
                    } else {
                        i4 = this.collapsedOffset;
                    }
                }
                i5 = 4;
            }
            startSettlingAnimation(v2, i5, i4, false);
            this.nestedScrolled = false;
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v2, MotionEvent motionEvent) {
        if (!v2.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (this.state == 1 && actionMasked == 0) {
            return true;
        }
        ViewDragHelper viewDragHelper = this.viewDragHelper;
        if (viewDragHelper != null) {
            viewDragHelper.processTouchEvent(motionEvent);
        }
        if (actionMasked == 0) {
            reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (actionMasked == 2 && !this.ignoreEvents && this.viewDragHelper != null && Math.abs(this.initialY - motionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
            this.viewDragHelper.captureChildView(v2, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return !this.ignoreEvents;
    }

    public void removeBottomSheetCallback(COUIBottomSheetCallback cOUIBottomSheetCallback) {
        this.callbacks.remove(cOUIBottomSheetCallback);
    }

    @Deprecated
    public void setBottomSheetCallback(COUIBottomSheetCallback cOUIBottomSheetCallback) {
        Log.w(TAG, "BottomSheetBehavior now supports multiple callbacks. `setBottomSheetCallback()` removes all existing callbacks, including ones set internally by library authors, which may result in unintended behavior. This may change in the future. Please use `addBottomSheetCallback()` and `removeBottomSheetCallback()` instead to set your own callbacks.");
        this.callbacks.clear();
        if (cOUIBottomSheetCallback != null) {
            this.callbacks.add(cOUIBottomSheetCallback);
        }
    }

    public void setCanHideKeyboard(boolean z2) {
        this.mCanHideKeyboard = z2;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setDraggable(boolean z2) {
        this.draggable = z2;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setExpandedOffset(int i3) {
        if (i3 < 0) {
            throw new IllegalArgumentException("offset must be greater than or equal to 0");
        }
        this.expandedOffset = i3;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setFitToContents(boolean z2) {
        if (this.fitToContents == z2) {
            return;
        }
        this.fitToContents = z2;
        if (this.viewRef != null) {
            calculateCollapsedOffset();
        }
        setStateInternal((this.fitToContents && this.state == 6) ? 3 : this.state);
        updateAccessibilityActions();
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setGestureInsetBottomIgnored(boolean z2) {
        this.gestureInsetBottomIgnored = z2;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setHalfExpandedRatio(float f3) {
        if (f3 <= 0.0f || f3 >= 1.0f) {
            throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
        }
        this.halfExpandedRatio = f3;
        if (this.viewRef != null) {
            calculateHalfExpandedOffset();
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setHideable(boolean z2) {
        if (this.hideable != z2) {
            this.hideable = z2;
            if (!z2 && this.state == 5) {
                setState(4);
            }
            updateAccessibilityActions();
        }
    }

    public void setPanelDragListener(COUIPanelDragListener cOUIPanelDragListener) {
        this.mCOUIPanelDragListener = cOUIPanelDragListener;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setPeekHeight(int i3) {
        setPanelPeekHeight(i3, false);
    }

    public void setPullUpListener(COUIPanelPullUpListener cOUIPanelPullUpListener) {
        this.mPullUpListener = cOUIPanelPullUpListener;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setSaveFlags(int i3) {
        this.saveFlags = i3;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setSkipCollapsed(boolean z2) {
        this.skipCollapsed = z2;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setState(int i3) {
        if (i3 == this.state) {
            return;
        }
        if (this.viewRef != null) {
            settleToStatePendingLayout(i3);
            return;
        }
        if (i3 == 4 || i3 == 3 || i3 == 6 || (this.hideable && i3 == 5)) {
            this.state = i3;
        }
    }

    public void setStateInternal(int i3) {
        V v2;
        if (this.state == i3) {
            return;
        }
        this.state = i3;
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference == null || (v2 = weakReference.get()) == null) {
            return;
        }
        if (i3 == 3) {
            updateImportantForAccessibility(true);
        } else if (i3 == 6 || i3 == 5 || i3 == 4) {
            updateImportantForAccessibility(false);
        }
        updateDrawableForTargetState(i3);
        for (int i4 = 0; i4 < this.callbacks.size(); i4++) {
            this.callbacks.get(i4).onStateChanged(v2, i3);
        }
        updateAccessibilityActions();
    }

    public void setStateWithoutAnim(int i3, boolean z2) {
        V v2;
        if (this.state == i3) {
            return;
        }
        this.state = i3;
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference == null || !z2 || (v2 = weakReference.get()) == null) {
            return;
        }
        for (int i4 = 0; i4 < this.callbacks.size(); i4++) {
            this.callbacks.get(i4).onStateChanged(v2, i3);
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setUpdateImportantForAccessibilityOnSiblings(boolean z2) {
        this.updateImportantForAccessibilityOnSiblings = z2;
    }

    public void settleToState(View view, int i3) {
        int expandedOffset;
        int i4;
        if (i3 == 4) {
            expandedOffset = this.collapsedOffset;
        } else if (i3 == 6) {
            expandedOffset = this.halfExpandedOffset;
            if (this.fitToContents && expandedOffset <= (i4 = this.fitToContentsOffset)) {
                i3 = 3;
                expandedOffset = i4;
            }
        } else if (i3 == 3) {
            expandedOffset = getExpandedOffset();
        } else {
            if (!this.hideable || i3 != 5) {
                throw new IllegalArgumentException("Illegal state argument: " + i3);
            }
            expandedOffset = this.parentHeight;
        }
        startSettlingAnimation(view, i3, expandedOffset, false);
    }

    public boolean shouldHide(View view, float f3) {
        if (this.skipCollapsed) {
            return true;
        }
        if (view.getTop() < this.collapsedOffset) {
            return false;
        }
        return Math.abs((((float) view.getTop()) + (f3 * 0.1f)) - ((float) this.collapsedOffset)) / ((float) calculatePeekHeight()) > 0.5f;
    }

    public void startSettlingAnimation(View view, int i3, int i4, boolean z2) {
        if (!(z2 ? this.viewDragHelper.settleCapturedViewAt(view.getLeft(), i4) : this.viewDragHelper.smoothSlideViewTo(view, view.getLeft(), i4))) {
            setStateInternal(i3);
            return;
        }
        setStateInternal(2);
        updateDrawableForTargetState(i3);
        if (this.settleRunnable == null) {
            this.settleRunnable = new SettleRunnable(view, i3);
        }
        if (SettleRunnable.access$100(this.settleRunnable)) {
            this.settleRunnable.targetState = i3;
            return;
        }
        COUIGuideBehavior<V>.SettleRunnable settleRunnable = this.settleRunnable;
        settleRunnable.targetState = i3;
        ViewCompat.postOnAnimation(view, settleRunnable);
        SettleRunnable.access$102(this.settleRunnable, true);
    }

    private void createMaterialShapeDrawable(Context context, AttributeSet attributeSet, boolean z2, ColorStateList colorStateList) {
        if (this.shapeThemingEnabled) {
            this.shapeAppearanceModelDefault = ShapeAppearanceModel.builder(context, attributeSet, R.attr.bottomSheetStyle, DEF_STYLE_RES).build();
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.shapeAppearanceModelDefault);
            this.materialShapeDrawable = materialShapeDrawable;
            materialShapeDrawable.initializeElevationOverlay(context);
            if (z2 && colorStateList != null) {
                this.materialShapeDrawable.setFillColor(colorStateList);
                return;
            }
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.colorBackground, typedValue, true);
            this.materialShapeDrawable.setTint(typedValue.data);
        }
    }

    public COUIGuideBehavior(Context context, AttributeSet attributeSet) {
        int i3;
        super(context, attributeSet);
        this.saveFlags = 0;
        this.fitToContents = true;
        this.updateImportantForAccessibilityOnSiblings = false;
        this.settleRunnable = null;
        this.halfExpandedRatio = 0.5f;
        this.elevation = -1.0f;
        this.draggable = true;
        this.state = 4;
        this.callbacks = new ArrayList<>();
        this.dragCallback = new AnonymousClass3();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.BottomSheetBehavior_Layout);
        this.shapeThemingEnabled = typedArrayObtainStyledAttributes.hasValue(R.styleable.BottomSheetBehavior_Layout_shapeAppearance);
        int i4 = R.styleable.BottomSheetBehavior_Layout_backgroundTint;
        boolean zHasValue = typedArrayObtainStyledAttributes.hasValue(i4);
        if (zHasValue) {
            createMaterialShapeDrawable(context, attributeSet, zHasValue, MaterialResource.getColorStateList(context, typedArrayObtainStyledAttributes, i4));
        } else {
            createMaterialShapeDrawable(context, attributeSet, zHasValue);
        }
        createShapeValueAnimator();
        this.elevation = typedArrayObtainStyledAttributes.getDimension(R.styleable.BottomSheetBehavior_Layout_android_elevation, -1.0f);
        int i5 = R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight;
        TypedValue typedValuePeekValue = typedArrayObtainStyledAttributes.peekValue(i5);
        if (typedValuePeekValue != null && (i3 = typedValuePeekValue.data) == -1) {
            setPeekHeight(i3);
        } else {
            setPeekHeight(typedArrayObtainStyledAttributes.getDimensionPixelSize(i5, -1));
        }
        setHideable(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        setGestureInsetBottomIgnored(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_gestureInsetBottomIgnored, false));
        setFitToContents(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        setSkipCollapsed(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        setDraggable(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_draggable, true));
        setSaveFlags(typedArrayObtainStyledAttributes.getInt(R.styleable.BottomSheetBehavior_Layout_behavior_saveFlags, 0));
        setHalfExpandedRatio(typedArrayObtainStyledAttributes.getFloat(R.styleable.BottomSheetBehavior_Layout_behavior_halfExpandedRatio, 0.5f));
        int i6 = R.styleable.BottomSheetBehavior_Layout_behavior_expandedOffset;
        TypedValue typedValuePeekValue2 = typedArrayObtainStyledAttributes.peekValue(i6);
        if (typedValuePeekValue2 != null && typedValuePeekValue2.type == 16) {
            setExpandedOffset(typedValuePeekValue2.data);
        } else {
            setExpandedOffset(typedArrayObtainStyledAttributes.getDimensionPixelOffset(i6, 0));
        }
        typedArrayObtainStyledAttributes.recycle();
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.mCanHideKeyboard = false;
    }
}