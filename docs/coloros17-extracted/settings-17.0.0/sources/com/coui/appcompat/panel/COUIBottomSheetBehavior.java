package com.coui.appcompat.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
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
import android.view.animation.PathInterpolator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import com.coui.appcompat.animation.COUIOutEaseInterpolator;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.view.MaterialResource;
import com.google.android.material.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.oplus.physicsengine.engine.AnimationListener;
import com.oplus.physicsengine.engine.AnimationUpdateListener;
import com.oplus.physicsengine.engine.BaseBehavior;
import com.oplus.physicsengine.engine.DragBehavior;
import com.oplus.physicsengine.engine.FloatValueHolder;
import com.oplus.physicsengine.engine.PhysicalAnimator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> implements AnimationListener, AnimationUpdateListener {
    private static final int BOTTOM_DISPLAY_VIEW_HEIGHT_LONG_TO_SHORT = 1;
    private static final int BOTTOM_DISPLAY_VIEW_HEIGHT_SHORT_TO_LONG = 2;
    private static final int CENTER_DISPLAY_VIEW_HEIGHT_LONG_TO_SHORT = 3;
    private static final int CENTER_DISPLAY_VIEW_HEIGHT_SHORT_TO_LONG = 4;
    private static final int CORNER_ANIMATION_DURATION = 500;
    private static boolean DEBUG = false;
    private static final int DEFAULT_DISPLAY = 0;
    private static final float DEFAULT_PHYSICS_DAMPING_RATIO = 0.6f;
    private static final float DEFAULT_PHYSICS_FREQUENCY = 16.0f;
    private static final float DEFAULT_TRANSLATE_HIDING_ANIMATOR_DURATION = 333.0f;
    private static final int DEF_STYLE_RES;
    private static final int DRAG_TO_HIDDEN_SPEED_THRESHOLD = 5000;
    private static final float HIDE_FRICTION = 0.1f;
    private static final float HIDE_THRESHOLD = 0.5f;
    public static final int PEEK_HEIGHT_AUTO = -1;
    private static final float PHYSICS_UNSET = Float.MIN_VALUE;
    private static final String PROPERTY_OFFSET_TOP_AND_BOTTOM = "offsetTopAndBottom";
    private static final int PULL_UP_DY_THRESHOLD = -100;
    private static final float PULL_UP_FRICTION = 0.5f;
    private static final int PULL_UP_SPEED_THRESHOLD = 10000;
    public static final int SAVE_ALL = -1;
    public static final int SAVE_FIT_TO_CONTENTS = 2;
    public static final int SAVE_HIDEABLE = 4;
    public static final int SAVE_NONE = 0;
    public static final int SAVE_PEEK_HEIGHT = 1;
    public static final int SAVE_SKIP_COLLAPSED = 8;
    private static final float SETTLE_ANIM_SPRING_BOUNCE = 0.0f;
    private static final float SETTLE_ANIM_SPRING_RESPONSE = 0.4f;
    private static final int SHAKE_HAND_MOVING_BASE_DOWN_VELOCITY = 100;
    private static final int SHAKE_HAND_MOVING_BASE_UP_VELOCITY = -100;
    private static final int SHAKE_HAND_MOVING_DIRECTION_DEFAULT = 0;
    private static final int SHAKE_HAND_MOVING_DIRECTION_DOWN = 2;
    private static final int SHAKE_HAND_MOVING_DIRECTION_UP = 1;
    private static final float SHAKE_HAND_MOVING_FACTOR = 0.4f;
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
    private float alphaRadio;
    private final ArrayList<COUIBottomSheetCallback> callbacks;
    int collapsedOffset;
    private final COUIViewDragHelper.Callback dragCallback;
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
    private Rect mBarRect;
    COUIPanelDragListener mCOUIPanelDragListener;
    private boolean mCanHideKeyboard;
    private Context mContext;
    private int mCurTop;
    private int mCurrentHeightChangeAnimToken;
    private int mDialogMaxHeight;
    private DragBehavior mDragBehavior;
    private View mDragChild;
    private float mDragCurrentValue;
    private float mDragDampingRatio;
    private float mDragFrequency;
    private FloatValueHolder mDragValueHolder;
    private boolean mGlobalDrag;
    boolean mHalfExpandOffsetUseParentRootViewHeight;
    private boolean mIsHandlePanel;
    private boolean mIsIgnoreExpandedOffsetChange;
    private boolean mIsInTinyScreen;
    private boolean mIsNestedScrollingCheckEnabled;
    private int mLastMeasureHeight;
    private int mLastOffsetInFling;
    private int mLastOrientation;
    private boolean mLayoutAtMaxHeight;
    private int mLayoutBottom;
    private Rect mLayoutRect;
    private OnNestedScrollingChild mOnNestedScrollingChild;
    private COUISpringAnimation mPanelHeightChangeAnim;
    private OnPanelHeightChangeAnimListener mPanelHeightChangeAnimListener;
    private COUISpringForce mPanelHeightSpringForce;
    private int mPanelPaddingBottom;
    private Rect mParentRect;
    private PhysicalAnimator mPhysicalAnimator;
    private boolean mPhysicsEnable;
    int mPressDownState;
    private COUIPanelPullUpListener mPullUpListener;
    private PullUpToDismissPanelListener mPullUpToDismissPanelListener;
    private int mRunningHeightChangeAnimToken;
    private int mSettleTargetState;
    private int mShakeHandMovingDirection;
    private boolean mStartHeightChangeAnim;
    private int mStartTopValue;
    private int mTargetState;
    private int mViewHeightType;
    private int mWantTop;
    private float mYVelocity;
    private MaterialShapeDrawable materialShapeDrawable;
    private float maximumVelocity;
    private boolean nestedScrolled;
    WeakReference<View> nestedScrollingChildRef;
    int parentHeight;
    int parentMarginTop;
    int parentRootViewHeight;
    int parentWidth;
    private int peekHeight;
    private boolean peekHeightAuto;
    private int peekHeightMin;
    private int saveFlags;
    private ShapeAppearanceModel shapeAppearanceModelDefault;
    private boolean shapeThemingEnabled;
    private boolean skipCollapsed;
    int state;
    boolean touchingScrollingChild;
    private boolean updateImportantForAccessibilityOnSiblings;
    private VelocityTracker velocityTracker;
    COUIViewDragHelper viewDragHelper;
    WeakReference<V> viewRef;

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$1, reason: invalid class name */
    public class AnonymousClass1 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            if (COUIBottomSheetBehavior.access$000(COUIBottomSheetBehavior.this) == 0) {
                COUIBottomSheetBehavior.access$100(COUIBottomSheetBehavior.this, f3);
            } else {
                COUIBottomSheetBehavior.access$200(COUIBottomSheetBehavior.this, cOUIDynamicAnimation, f3, f4);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$2, reason: invalid class name */
    public class AnonymousClass2 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            if (COUIBottomSheetBehavior.access$000(COUIBottomSheetBehavior.this) == 0) {
                COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
                cOUIBottomSheetBehavior.setStateInternal(COUIBottomSheetBehavior.access$300(cOUIBottomSheetBehavior));
                COUIBottomSheetBehavior.this.viewDragHelper.setDragState(0);
                return;
            }
            if (COUIBottomSheetBehavior.access$400(COUIBottomSheetBehavior.this) != null) {
                COUIBottomSheetBehavior.access$400(COUIBottomSheetBehavior.this).onAnimationEnd(cOUIDynamicAnimation, z2, f4);
            }
            COUIBottomSheetBehavior.access$002(COUIBottomSheetBehavior.this, 0);
            if (COUIBottomSheetBehavior.access$500(COUIBottomSheetBehavior.this) == COUIBottomSheetBehavior.access$600(COUIBottomSheetBehavior.this)) {
                COUIBottomSheetBehavior.access$702(COUIBottomSheetBehavior.this, false);
            }
            WeakReference<V> weakReference = COUIBottomSheetBehavior.this.viewRef;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            COUIBottomSheetBehavior.access$800(COUIBottomSheetBehavior.this, 0);
            COUIBottomSheetBehavior.this.viewRef.get().requestLayout();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$3, reason: invalid class name */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ View val$child;
        final /* synthetic */ int val$finalState;

        public AnonymousClass3(View view, int i3) {
            this.val$child = view;
            this.val$finalState = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIBottomSheetBehavior.this.settleToState(this.val$child, this.val$finalState);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$4, reason: invalid class name */
    public class AnonymousClass4 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass4() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUIBottomSheetBehavior.access$900(COUIBottomSheetBehavior.this) != null) {
                COUIBottomSheetBehavior.access$900(COUIBottomSheetBehavior.this).setInterpolation(fFloatValue);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$5, reason: invalid class name */
    public class AnonymousClass5 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ View val$child;

        public AnonymousClass5(View view) {
            this.val$child = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int iFloatValue = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.val$child.offsetTopAndBottom(iFloatValue - COUIBottomSheetBehavior.access$1000(COUIBottomSheetBehavior.this));
            COUIBottomSheetBehavior.this.dispatchOnSlide(this.val$child.getTop());
            COUIBottomSheetBehavior.access$1002(COUIBottomSheetBehavior.this, iFloatValue);
            if (COUIBottomSheetBehavior.access$1100(COUIBottomSheetBehavior.this) != null) {
                COUIBottomSheetBehavior.access$1200(COUIBottomSheetBehavior.this, this.val$child);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$6, reason: invalid class name */
    public class AnonymousClass6 extends AnimatorListenerAdapter {
        public AnonymousClass6() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUIBottomSheetBehavior.this.setStateInternal(5);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$7, reason: invalid class name */
    public class AnonymousClass7 extends COUIViewDragHelper.Callback {
        public AnonymousClass7() {
        }

        private boolean releasedLow(View view) {
            int top = view.getTop();
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
            return top > (cOUIBottomSheetBehavior.parentHeight + cOUIBottomSheetBehavior.getExpandedOffset()) / 2;
        }

        @Override // com.coui.appcompat.panel.COUIViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view, int i3, int i4) {
            return view.getLeft();
        }

        @Override // com.coui.appcompat.panel.COUIViewDragHelper.Callback
        public int clampViewPositionVertical(View view, int i3, int i4) {
            if (COUIBottomSheetBehavior.access$1100(COUIBottomSheetBehavior.this) != null) {
                COUIBottomSheetBehavior.access$1100(COUIBottomSheetBehavior.this).onDraggingPanel();
            }
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
            int iOnDragging = 0;
            if (cOUIBottomSheetBehavior.state == 1) {
                if (cOUIBottomSheetBehavior.isPanelHeightChangeAnimRunning()) {
                    COUIBottomSheetBehavior.access$2200(COUIBottomSheetBehavior.this).cancel();
                }
                if (view.getTop() <= COUIBottomSheetBehavior.this.getExpandedOffset()) {
                    if (COUIBottomSheetBehavior.access$1400(COUIBottomSheetBehavior.this) && COUIBottomSheetBehavior.access$1500(COUIBottomSheetBehavior.this).isDragging()) {
                        COUIBottomSheetBehavior.access$1500(COUIBottomSheetBehavior.this).endDrag(0.0f);
                        COUIBottomSheetBehavior.access$1602(COUIBottomSheetBehavior.this, null);
                    }
                    if (COUIBottomSheetBehavior.access$1100(COUIBottomSheetBehavior.this) != null && COUIBottomSheetBehavior.this.getExpandedOffset() > 0) {
                        COUIBottomSheetBehavior.access$1702(COUIBottomSheetBehavior.this, true);
                        if (i4 < 0) {
                            i4 = Math.max(i4, (view.getMeasuredHeight() - COUIBottomSheetBehavior.access$1900(COUIBottomSheetBehavior.this)) - COUIBottomSheetBehavior.access$2300(COUIBottomSheetBehavior.this));
                        }
                        if (i4 != 0) {
                            iOnDragging = COUIBottomSheetBehavior.access$1100(COUIBottomSheetBehavior.this).onDragging(i4, COUIBottomSheetBehavior.this.getExpandedOffset());
                        }
                    }
                } else {
                    int top = view.getTop();
                    if (COUIBottomSheetBehavior.access$1400(COUIBottomSheetBehavior.this)) {
                        COUIBottomSheetBehavior.access$2400(COUIBottomSheetBehavior.this, view, top + i4);
                    } else if (COUIBottomSheetBehavior.access$2500(COUIBottomSheetBehavior.this) > 10000.0f) {
                        i3 = ((int) ((i4 * 0.5f) + 0.5f)) + top;
                    }
                }
            }
            COUIBottomSheetBehavior.access$1200(COUIBottomSheetBehavior.this, view);
            int expandedOffset = COUIBottomSheetBehavior.this.getExpandedOffset() - iOnDragging;
            COUIBottomSheetBehavior cOUIBottomSheetBehavior2 = COUIBottomSheetBehavior.this;
            return MathUtils.clamp(i3, expandedOffset, cOUIBottomSheetBehavior2.hideable ? cOUIBottomSheetBehavior2.parentHeight : cOUIBottomSheetBehavior2.collapsedOffset);
        }

        @Override // com.coui.appcompat.panel.COUIViewDragHelper.Callback
        public int getViewVerticalDragRange(View view) {
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
            return cOUIBottomSheetBehavior.hideable ? cOUIBottomSheetBehavior.parentHeight : cOUIBottomSheetBehavior.collapsedOffset;
        }

        @Override // com.coui.appcompat.panel.COUIViewDragHelper.Callback
        public void onViewDragStateChanged(int i3) {
            if (i3 == 1 && COUIBottomSheetBehavior.access$1300(COUIBottomSheetBehavior.this)) {
                COUIBottomSheetBehavior.this.setStateInternal(1);
            }
        }

        @Override // com.coui.appcompat.panel.COUIViewDragHelper.Callback
        public void onViewPositionChanged(View view, int i3, int i4, int i5, int i6) {
            COUIBottomSheetBehavior.this.dispatchOnSlide(i4);
        }

        @Override // com.coui.appcompat.panel.COUIViewDragHelper.Callback
        public void onViewReleased(View view, float f3, float f4) {
            int i3;
            if (COUIBottomSheetBehavior.access$1400(COUIBottomSheetBehavior.this) && COUIBottomSheetBehavior.access$1500(COUIBottomSheetBehavior.this).isDragging()) {
                COUIBottomSheetBehavior.access$1500(COUIBottomSheetBehavior.this).endDrag(0.0f);
                COUIBottomSheetBehavior.access$1602(COUIBottomSheetBehavior.this, null);
            }
            boolean z2 = false;
            COUIBottomSheetBehavior.access$1702(COUIBottomSheetBehavior.this, false);
            if (COUIBottomSheetBehavior.access$1100(COUIBottomSheetBehavior.this) != null) {
                COUIBottomSheetBehavior.access$1100(COUIBottomSheetBehavior.this).onReleasedDrag();
                float ratio = view instanceof COUIPanelPercentFrameLayout ? ((COUIPanelPercentFrameLayout) view).getRatio() : 1.0f;
                COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
                if (((int) (((cOUIBottomSheetBehavior.parentHeight - COUIBottomSheetBehavior.access$1800(cOUIBottomSheetBehavior, view)) / ratio) - ((view.getHeight() - COUIBottomSheetBehavior.access$1900(COUIBottomSheetBehavior.this)) / ratio))) <= COUIBottomSheetBehavior.this.getExpandedOffset() && view.getTop() < COUIBottomSheetBehavior.this.getExpandedOffset()) {
                    COUIBottomSheetBehavior.access$1100(COUIBottomSheetBehavior.this).onReleased(COUIBottomSheetBehavior.this.getExpandedOffset());
                    return;
                }
            }
            int i4 = 6;
            if (f4 < 0.0f) {
                if (COUIBottomSheetBehavior.access$2000(COUIBottomSheetBehavior.this)) {
                    i3 = COUIBottomSheetBehavior.this.fitToContentsOffset;
                } else {
                    int top = view.getTop();
                    COUIBottomSheetBehavior cOUIBottomSheetBehavior2 = COUIBottomSheetBehavior.this;
                    int i5 = cOUIBottomSheetBehavior2.halfExpandedOffset;
                    if (top > i5) {
                        i3 = i5;
                    } else {
                        i3 = cOUIBottomSheetBehavior2.expandedOffset;
                    }
                }
                i4 = 3;
            } else {
                COUIBottomSheetBehavior cOUIBottomSheetBehavior3 = COUIBottomSheetBehavior.this;
                if (cOUIBottomSheetBehavior3.hideable && cOUIBottomSheetBehavior3.shouldHide(view, f4)) {
                    COUIPanelDragListener cOUIPanelDragListener = COUIBottomSheetBehavior.this.mCOUIPanelDragListener;
                    if (cOUIPanelDragListener != null && cOUIPanelDragListener.onDragWhileEditing()) {
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior4 = COUIBottomSheetBehavior.this;
                        int i6 = cOUIBottomSheetBehavior4.fitToContentsOffset;
                        COUIBottomSheetBehavior.access$2102(cOUIBottomSheetBehavior4, false);
                        i3 = i6;
                    } else if ((Math.abs(f3) < Math.abs(f4) && f4 > 500.0f) || releasedLow(view)) {
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior5 = COUIBottomSheetBehavior.this;
                        int i7 = cOUIBottomSheetBehavior5.parentRootViewHeight;
                        COUIBottomSheetBehavior.access$2102(cOUIBottomSheetBehavior5, true);
                        i3 = i7;
                        i4 = 5;
                    } else if (COUIBottomSheetBehavior.access$2000(COUIBottomSheetBehavior.this)) {
                        i3 = COUIBottomSheetBehavior.this.fitToContentsOffset;
                    } else if (Math.abs(view.getTop() - COUIBottomSheetBehavior.this.expandedOffset) < Math.abs(view.getTop() - COUIBottomSheetBehavior.this.halfExpandedOffset)) {
                        i3 = COUIBottomSheetBehavior.this.expandedOffset;
                    } else {
                        i3 = COUIBottomSheetBehavior.this.halfExpandedOffset;
                    }
                    i4 = 3;
                } else if (f4 == 0.0f || Math.abs(f3) > Math.abs(f4)) {
                    int top2 = view.getTop();
                    if (!COUIBottomSheetBehavior.access$2000(COUIBottomSheetBehavior.this)) {
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior6 = COUIBottomSheetBehavior.this;
                        int i8 = cOUIBottomSheetBehavior6.halfExpandedOffset;
                        if (top2 < i8) {
                            if (top2 < Math.abs(top2 - cOUIBottomSheetBehavior6.collapsedOffset)) {
                                i3 = COUIBottomSheetBehavior.this.expandedOffset;
                                i4 = 3;
                            } else {
                                i3 = COUIBottomSheetBehavior.this.halfExpandedOffset;
                            }
                        } else if (Math.abs(top2 - i8) < Math.abs(top2 - COUIBottomSheetBehavior.this.collapsedOffset)) {
                            i3 = COUIBottomSheetBehavior.this.halfExpandedOffset;
                        } else {
                            i3 = COUIBottomSheetBehavior.this.collapsedOffset;
                            i4 = 4;
                        }
                    } else if (Math.abs(top2 - COUIBottomSheetBehavior.this.fitToContentsOffset) < Math.abs(top2 - COUIBottomSheetBehavior.this.collapsedOffset)) {
                        i3 = COUIBottomSheetBehavior.this.fitToContentsOffset;
                        i4 = 3;
                    } else {
                        i3 = COUIBottomSheetBehavior.this.collapsedOffset;
                        i4 = 4;
                    }
                } else {
                    if (COUIBottomSheetBehavior.access$2000(COUIBottomSheetBehavior.this)) {
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior7 = COUIBottomSheetBehavior.this;
                        COUIPanelDragListener cOUIPanelDragListener2 = cOUIBottomSheetBehavior7.mCOUIPanelDragListener;
                        if (cOUIPanelDragListener2 == null) {
                            i3 = cOUIBottomSheetBehavior7.collapsedOffset;
                        } else if (cOUIPanelDragListener2.onDragWhileEditing()) {
                            i3 = COUIBottomSheetBehavior.this.fitToContentsOffset;
                            i4 = 3;
                        } else {
                            i3 = COUIBottomSheetBehavior.this.parentRootViewHeight;
                            i4 = 5;
                        }
                    } else {
                        int top3 = view.getTop();
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior8 = COUIBottomSheetBehavior.this;
                        int i9 = cOUIBottomSheetBehavior8.halfExpandedOffset;
                        if (top3 > i9 && top3 < cOUIBottomSheetBehavior8.collapsedOffset) {
                            z2 = true;
                        }
                        if (cOUIBottomSheetBehavior8.mPressDownState == 6 && z2) {
                            i3 = cOUIBottomSheetBehavior8.collapsedOffset;
                        } else if (Math.abs(top3 - i9) < Math.abs(top3 - COUIBottomSheetBehavior.this.collapsedOffset)) {
                            i3 = COUIBottomSheetBehavior.this.halfExpandedOffset;
                        } else {
                            i3 = COUIBottomSheetBehavior.this.collapsedOffset;
                        }
                    }
                    i4 = 4;
                }
            }
            COUIBottomSheetBehavior.this.startSettlingAnimation(view, i4, i3, true);
        }

        @Override // com.coui.appcompat.panel.COUIViewDragHelper.Callback
        public boolean tryCaptureView(View view, int i3) {
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
            int i4 = cOUIBottomSheetBehavior.state;
            if (i4 == 1 || cOUIBottomSheetBehavior.touchingScrollingChild) {
                return false;
            }
            if (i4 == 3 && cOUIBottomSheetBehavior.activePointerId == i3) {
                WeakReference<View> weakReference = cOUIBottomSheetBehavior.nestedScrollingChildRef;
                View view2 = weakReference != null ? weakReference.get() : null;
                if (view2 != null && view2.canScrollVertically(-1)) {
                    return false;
                }
            }
            WeakReference<V> weakReference2 = COUIBottomSheetBehavior.this.viewRef;
            return weakReference2 != null && weakReference2.get() == view;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$8, reason: invalid class name */
    public class AnonymousClass8 implements AccessibilityViewCommand {
        final /* synthetic */ int val$state;

        public AnonymousClass8(int i3) {
            this.val$state = i3;
        }

        @Override // androidx.core.view.accessibility.AccessibilityViewCommand
        public boolean perform(View view, AccessibilityViewCommand.CommandArguments commandArguments) {
            COUIBottomSheetBehavior.this.setPanelState(this.val$state);
            return true;
        }
    }

    public static abstract class COUIBottomSheetCallback {
        public abstract void onSlide(View view, float f3);

        public abstract void onStateChanged(View view, int i3);
    }

    public interface OnNestedScrollingChild {
        View getNestedScrollingChild();
    }

    public interface OnPanelHeightChangeAnimListener {
        default void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3) {
        }

        default void onAnimationStart() {
        }

        default void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        }
    }

    public interface PullUpToDismissPanelListener {
        void onPullUpDismiss();
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

        /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetBehavior$SavedState$1, reason: invalid class name */
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

        public SavedState(Parcelable parcelable, COUIBottomSheetBehavior<?> cOUIBottomSheetBehavior) {
            super(parcelable);
            this.state = cOUIBottomSheetBehavior.state;
            this.peekHeight = COUIBottomSheetBehavior.access$2600(cOUIBottomSheetBehavior);
            this.fitToContents = COUIBottomSheetBehavior.access$2000((COUIBottomSheetBehavior) cOUIBottomSheetBehavior);
            this.hideable = cOUIBottomSheetBehavior.hideable;
            this.skipCollapsed = COUIBottomSheetBehavior.access$2700(cOUIBottomSheetBehavior);
        }

        @Deprecated
        public SavedState(Parcelable parcelable, int i3) {
            super(parcelable);
            this.state = i3;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        DEF_STYLE_RES = R.style.Widget_Design_BottomSheet_Modal;
    }

    public COUIBottomSheetBehavior(Context context, AttributeSet attributeSet) {
        int i3;
        super(context, attributeSet);
        this.saveFlags = 0;
        this.fitToContents = true;
        this.updateImportantForAccessibilityOnSiblings = false;
        this.halfExpandedRatio = 0.5f;
        this.elevation = -1.0f;
        this.draggable = true;
        this.state = 4;
        this.mPressDownState = 4;
        this.mYVelocity = 0.0f;
        this.mLayoutAtMaxHeight = true;
        this.mLastOrientation = -1;
        this.mShakeHandMovingDirection = 0;
        this.mViewHeightType = 0;
        this.mParentRect = new Rect();
        this.mLayoutRect = new Rect();
        this.mHalfExpandOffsetUseParentRootViewHeight = true;
        this.callbacks = new ArrayList<>();
        this.mLastOffsetInFling = 0;
        this.alphaRadio = 0.0f;
        this.mDragFrequency = 16.0f;
        this.mDragDampingRatio = 0.6f;
        this.mPhysicsEnable = false;
        this.mDragChild = null;
        this.mIsInTinyScreen = false;
        this.mIsHandlePanel = false;
        this.mBarRect = new Rect();
        this.mGlobalDrag = true;
        this.mIsNestedScrollingCheckEnabled = false;
        this.mCurrentHeightChangeAnimToken = 0;
        this.mRunningHeightChangeAnimToken = -1;
        this.mTargetState = 0;
        this.dragCallback = new AnonymousClass7();
        this.mContext = context;
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
        if (typedValuePeekValue == null || (i3 = typedValuePeekValue.data) != -1) {
            setPanelPeekHeight(typedArrayObtainStyledAttributes.getDimensionPixelSize(i5, -1));
        } else {
            setPanelPeekHeight(i3);
        }
        setHideable(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        setGestureInsetBottomIgnored(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_gestureInsetBottomIgnored, false));
        setFitToContents(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        setPanelSkipCollapsed(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        setDraggable(typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_draggable, true));
        setSaveFlags(typedArrayObtainStyledAttributes.getInt(R.styleable.BottomSheetBehavior_Layout_behavior_saveFlags, -1));
        setHalfExpandedRatio(typedArrayObtainStyledAttributes.getFloat(R.styleable.BottomSheetBehavior_Layout_behavior_halfExpandedRatio, 0.5f));
        int i6 = R.styleable.BottomSheetBehavior_Layout_behavior_expandedOffset;
        TypedValue typedValuePeekValue2 = typedArrayObtainStyledAttributes.peekValue(i6);
        if (typedValuePeekValue2 == null || typedValuePeekValue2.type != 16) {
            setExpandedOffset(typedArrayObtainStyledAttributes.getDimensionPixelOffset(i6, 0));
        } else {
            setExpandedOffset(typedValuePeekValue2.data);
        }
        typedArrayObtainStyledAttributes.recycle();
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.mCanHideKeyboard = false;
    }

    public static /* synthetic */ int access$000(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mViewHeightType;
    }

    public static /* synthetic */ int access$002(COUIBottomSheetBehavior cOUIBottomSheetBehavior, int i3) {
        cOUIBottomSheetBehavior.mViewHeightType = i3;
        return i3;
    }

    public static /* synthetic */ void access$100(COUIBottomSheetBehavior cOUIBottomSheetBehavior, float f3) {
        cOUIBottomSheetBehavior.panelHeightVerticalMoving(f3);
    }

    public static /* synthetic */ int access$1000(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mLastOffsetInFling;
    }

    public static /* synthetic */ int access$1002(COUIBottomSheetBehavior cOUIBottomSheetBehavior, int i3) {
        cOUIBottomSheetBehavior.mLastOffsetInFling = i3;
        return i3;
    }

    public static /* synthetic */ COUIPanelPullUpListener access$1100(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mPullUpListener;
    }

    public static /* synthetic */ void access$1200(COUIBottomSheetBehavior cOUIBottomSheetBehavior, View view) {
        cOUIBottomSheetBehavior.calculatePanelOutsideAlpha(view);
    }

    public static /* synthetic */ boolean access$1300(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.draggable;
    }

    public static /* synthetic */ boolean access$1400(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mPhysicsEnable;
    }

    public static /* synthetic */ DragBehavior access$1500(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mDragBehavior;
    }

    public static /* synthetic */ View access$1602(COUIBottomSheetBehavior cOUIBottomSheetBehavior, View view) {
        cOUIBottomSheetBehavior.mDragChild = view;
        return view;
    }

    public static /* synthetic */ boolean access$1702(COUIBottomSheetBehavior cOUIBottomSheetBehavior, boolean z2) {
        cOUIBottomSheetBehavior.mIsIgnoreExpandedOffsetChange = z2;
        return z2;
    }

    public static /* synthetic */ int access$1800(COUIBottomSheetBehavior cOUIBottomSheetBehavior, View view) {
        return cOUIBottomSheetBehavior.getMarginBottom(view);
    }

    public static /* synthetic */ int access$1900(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mPanelPaddingBottom;
    }

    public static /* synthetic */ void access$200(COUIBottomSheetBehavior cOUIBottomSheetBehavior, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUIBottomSheetBehavior.panelHeightAdaptive(cOUIDynamicAnimation, f3, f4);
    }

    public static /* synthetic */ boolean access$2000(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.fitToContents;
    }

    public static /* synthetic */ boolean access$2102(COUIBottomSheetBehavior cOUIBottomSheetBehavior, boolean z2) {
        cOUIBottomSheetBehavior.mCanHideKeyboard = z2;
        return z2;
    }

    public static /* synthetic */ COUISpringAnimation access$2200(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mPanelHeightChangeAnim;
    }

    public static /* synthetic */ int access$2300(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mDialogMaxHeight;
    }

    public static /* synthetic */ void access$2400(COUIBottomSheetBehavior cOUIBottomSheetBehavior, View view, float f3) {
        cOUIBottomSheetBehavior.dragToNewTop(view, f3);
    }

    public static /* synthetic */ float access$2500(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.getYVelocity();
    }

    public static /* synthetic */ int access$2600(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.peekHeight;
    }

    public static /* synthetic */ boolean access$2700(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.skipCollapsed;
    }

    public static /* synthetic */ int access$300(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mSettleTargetState;
    }

    public static /* synthetic */ OnPanelHeightChangeAnimListener access$400(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mPanelHeightChangeAnimListener;
    }

    public static /* synthetic */ int access$500(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mRunningHeightChangeAnimToken;
    }

    public static /* synthetic */ int access$600(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.mCurrentHeightChangeAnimToken;
    }

    public static /* synthetic */ boolean access$702(COUIBottomSheetBehavior cOUIBottomSheetBehavior, boolean z2) {
        cOUIBottomSheetBehavior.mStartHeightChangeAnim = z2;
        return z2;
    }

    public static /* synthetic */ void access$800(COUIBottomSheetBehavior cOUIBottomSheetBehavior, int i3) {
        cOUIBottomSheetBehavior.setOutlineBottomOffset(i3);
    }

    public static /* synthetic */ MaterialShapeDrawable access$900(COUIBottomSheetBehavior cOUIBottomSheetBehavior) {
        return cOUIBottomSheetBehavior.materialShapeDrawable;
    }

    private void addAccessibilityActionForState(V v2, AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat, int i3) {
        ViewCompat.replaceAccessibilityAction(v2, accessibilityActionCompat, null, new AnonymousClass8(i3));
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
        if (DEBUG) {
            Log.d(TAG, "calculateHalfExpandedOffset: halfExpandedRatio=" + this.halfExpandedRatio + " halfExpandedOffset=" + this.halfExpandedOffset);
        }
        if (this.mHalfExpandOffsetUseParentRootViewHeight && this.mIsHandlePanel && this.halfExpandedRatio == 0.5f) {
            this.halfExpandedOffset = (this.parentRootViewHeight / 2) - this.parentMarginTop;
            if (DEBUG) {
                Log.d(TAG, "calculateHalfExpandedOffset: modified halfExpandedOffset=" + this.halfExpandedOffset);
            }
        }
        if (this.mIsHandlePanel) {
            this.halfExpandedOffset = Math.max(this.halfExpandedOffset, getExpandedOffset());
        }
    }

    private void calculatePanelOutsideAlpha(View view) {
        float top = 1.0f - ((view.getTop() - getExpandedOffset()) / this.parentHeight);
        this.alphaRadio = top;
        COUIPanelPullUpListener cOUIPanelPullUpListener = this.mPullUpListener;
        if (cOUIPanelPullUpListener != null) {
            cOUIPanelPullUpListener.onOffsetChanged(top);
        }
    }

    private int calculatePeekHeight() {
        return this.peekHeightAuto ? Math.max(this.peekHeightMin, this.parentHeight - ((this.parentWidth * 9) / 16)) : this.peekHeight;
    }

    private void checkOrientationChange() {
        int i3 = this.mContext.getResources().getConfiguration().orientation;
        int i4 = this.mLastOrientation;
        if (i4 != -1 && i4 != i3 && this.mStartHeightChangeAnim) {
            this.mStartHeightChangeAnim = false;
        }
        this.mLastOrientation = i3;
    }

    private void createMaterialShapeDrawable(Context context, AttributeSet attributeSet, boolean z2) {
        createMaterialShapeDrawable(context, attributeSet, z2, null);
    }

    private void createPanelHeightChangeAnim() {
        androidx.dynamicanimation.animation.FloatValueHolder floatValueHolder = new androidx.dynamicanimation.animation.FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        this.mPanelHeightSpringForce = cOUISpringForce;
        cOUISpringForce.setBounce(0.0f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(this.mPanelHeightSpringForce);
        this.mPanelHeightChangeAnim = spring;
        spring.addUpdateListener(new AnonymousClass1());
        this.mPanelHeightChangeAnim.addEndListener(new AnonymousClass2());
    }

    private void createShapeValueAnimator() {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.interpolatorAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(500L);
        this.interpolatorAnimator.addUpdateListener(new AnonymousClass4());
    }

    private void dragToNewTop(View view, float f3) {
        if (this.mDragBehavior.isDragging()) {
            this.mDragBehavior.onDragging(f3);
            return;
        }
        this.mDragChild = view;
        float top = view.getTop();
        this.mDragValueHolder.setStartValue(top);
        this.mDragBehavior.beginDrag(top, top);
        this.mDragCurrentValue = top;
    }

    public static <V extends View> COUIBottomSheetBehavior<V> from(V v2) {
        ViewGroup.LayoutParams layoutParams = v2.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (behavior instanceof COUIBottomSheetBehavior) {
            return (COUIBottomSheetBehavior) behavior;
        }
        throw new IllegalArgumentException("The view is not associated with COUIBottomSheetBehavior");
    }

    private Rect getLayoutRect(CoordinatorLayout coordinatorLayout, View view, int i3) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        this.mParentRect.set(coordinatorLayout.getPaddingLeft() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, coordinatorLayout.getPaddingTop() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, (coordinatorLayout.getWidth() - coordinatorLayout.getPaddingRight()) - ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, (coordinatorLayout.getHeight() - coordinatorLayout.getPaddingBottom()) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
        WindowInsetsCompat lastWindowInsets = coordinatorLayout.getLastWindowInsets();
        if (lastWindowInsets != null && ViewCompat.getFitsSystemWindows(coordinatorLayout) && !ViewCompat.getFitsSystemWindows(view)) {
            this.mParentRect.left += lastWindowInsets.getSystemWindowInsetLeft();
            this.mParentRect.top += lastWindowInsets.getSystemWindowInsetTop();
            this.mParentRect.right -= lastWindowInsets.getSystemWindowInsetRight();
            this.mParentRect.bottom -= lastWindowInsets.getSystemWindowInsetBottom();
        }
        GravityCompat.apply(resolveGravity(layoutParams.gravity), view.getMeasuredWidth(), view.getMeasuredHeight(), this.mParentRect, this.mLayoutRect, i3);
        return this.mLayoutRect;
    }

    private int getMarginBottom(View view) {
        if (view == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return 0;
    }

    private int getTargetTopForState(int i3) {
        if (i3 == 3) {
            return getExpandedOffset();
        }
        if (i3 == 4) {
            return this.collapsedOffset;
        }
        if (i3 == 5) {
            return this.parentRootViewHeight;
        }
        if (i3 != 6) {
            return -1;
        }
        return this.halfExpandedOffset;
    }

    private View getVisiblePanelContentLayout(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return null;
        }
        for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if ((childAt instanceof COUIPanelContentLayout) && childAt.getVisibility() == 0) {
                return childAt;
            }
        }
        return null;
    }

    private int getWantTop(V v2, int i3) {
        float ratio;
        boolean hasAnchor;
        if (v2 instanceof COUIPanelPercentFrameLayout) {
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = (COUIPanelPercentFrameLayout) v2;
            ratio = cOUIPanelPercentFrameLayout.getRatio();
            hasAnchor = cOUIPanelPercentFrameLayout.getHasAnchor();
        } else {
            ratio = 1.0f;
            hasAnchor = false;
        }
        if (!this.mIsIgnoreExpandedOffsetChange) {
            int marginBottom = getMarginBottom(v2);
            if (hasAnchor) {
                this.fitToContentsOffset = 0;
            } else {
                this.fitToContentsOffset = (int) Math.max(0.0f, ((this.parentHeight - marginBottom) / ratio) - ((i3 - this.mPanelPaddingBottom) / ratio));
            }
            if (this.mIsHandlePanel) {
                this.expandedOffset = this.fitToContentsOffset;
            }
        }
        return getExpandedOffset();
    }

    private float getYVelocity() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
        return this.velocityTracker.getYVelocity(this.activePointerId);
    }

    private boolean ifInTopOfMultiWindowMode() {
        Activity activityContextToActivity = UIUtil.contextToActivity(this.mContext);
        if (activityContextToActivity != null) {
            View decorView = activityContextToActivity.getWindow().getDecorView();
            int[] iArr = new int[2];
            decorView.getLocationInWindow(iArr);
            int i3 = iArr[1];
            decorView.getLocationOnScreen(iArr);
            int i4 = iArr[1];
            if (activityContextToActivity.isInMultiWindowMode() && i4 == i3) {
                return true;
            }
        }
        return false;
    }

    private boolean isClickedOnBar(View view, int i3, int i4) {
        View viewFindViewById;
        if (!(view instanceof COUIPanelPercentFrameLayout) || (viewFindViewById = view.findViewById(com.support.panel.R.id.panel_drag_bar)) == null) {
            return false;
        }
        viewFindViewById.getHitRect(this.mBarRect);
        return this.mBarRect.contains(i3, i4);
    }

    private boolean isImeVisible(View view) {
        try {
            WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(view);
            return rootWindowInsets != null && rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime());
        } catch (Exception e3) {
            Log.e(TAG, "isImeVisible exception: " + e3.getMessage());
            return false;
        }
    }

    private boolean isPanelCenterDisplay() {
        return (COUIPanelMultiWindowUtils.isSmallScreen(this.mContext, null) || this.mIsHandlePanel) ? false : true;
    }

    private void panelHeightAdaptive(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        int i3 = this.mCurTop;
        int i4 = this.mWantTop;
        float fAbs = Math.abs((f3 - this.mCurTop) / (i3 - i4 == 0 ? 1 : Math.abs(i3 - i4)));
        OnPanelHeightChangeAnimListener onPanelHeightChangeAnimListener = this.mPanelHeightChangeAnimListener;
        if (onPanelHeightChangeAnimListener != null) {
            onPanelHeightChangeAnimListener.onAnimationUpdate(cOUIDynamicAnimation, fAbs, f4);
        }
        int i5 = (int) f3;
        int top = i5 - this.viewRef.get().getTop();
        if (top != 0) {
            ViewCompat.offsetTopAndBottom(this.viewRef.get(), top);
            if (isPanelCenterDisplay()) {
                int i6 = this.mViewHeightType;
                if (i6 == 3) {
                    setOutlineBottomOffset(Math.abs(getExpandedOffset() - i5) * (-2));
                    this.viewRef.get().invalidateOutline();
                } else if (i6 == 4) {
                    setOutlineBottomOffset(Math.abs(i5 - this.mWantTop) * (-2));
                    this.viewRef.get().invalidateOutline();
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:27:0x003a A[PHI: r0
  0x003a: PHI (r0v13 int) = (r0v12 int), (r0v15 int) binds: [B:33:0x004b, B:26:0x0038] A[DONT_GENERATE, DONT_INLINE]] */
    private void panelHeightVerticalMoving(float f3) {
        float expandedOffset;
        int expandedOffset2;
        int i3;
        COUIViewDragHelper cOUIViewDragHelper = this.viewDragHelper;
        if (cOUIViewDragHelper == null || cOUIViewDragHelper.getCapturedView() == null) {
            return;
        }
        int i4 = (int) f3;
        int i5 = this.mSettleTargetState;
        if (i5 != 3) {
            if (i5 != 4) {
                if (i5 == 6) {
                    int i6 = this.mShakeHandMovingDirection;
                    if (i6 == 1 && i4 < (expandedOffset2 = this.halfExpandedOffset) && this.mStartTopValue > expandedOffset2) {
                        expandedOffset = expandedOffset2;
                        i4 = (int) (expandedOffset - ((expandedOffset2 - i4) * 0.4f));
                    } else if (i6 == 2 && i4 > (i3 = this.halfExpandedOffset) && this.mStartTopValue < i3) {
                        i4 = (int) (i3 + ((i4 - i3) * 0.4f));
                    }
                }
            } else if (this.mShakeHandMovingDirection == 2 && i4 > (i3 = this.collapsedOffset) && this.mStartTopValue < i3) {
                i4 = (int) (i3 + ((i4 - i3) * 0.4f));
            }
        } else if (this.mShakeHandMovingDirection == 1 && i4 < getExpandedOffset() && this.mStartTopValue > getExpandedOffset()) {
            expandedOffset = getExpandedOffset();
            expandedOffset2 = getExpandedOffset();
            i4 = (int) (expandedOffset - ((expandedOffset2 - i4) * 0.4f));
        }
        int i7 = i4;
        calculatePanelOutsideAlpha(this.viewDragHelper.getCapturedView());
        int top = i7 - this.viewDragHelper.getCapturedView().getTop();
        if (top != 0) {
            ViewCompat.offsetTopAndBottom(this.viewDragHelper.getCapturedView(), top);
            this.dragCallback.onViewPositionChanged(this.viewDragHelper.getCapturedView(), 0, i7, 0, top);
        }
    }

    private void reset() {
        this.activePointerId = -1;
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.velocityTracker = null;
        }
    }

    private int resolveGravity(int i3) {
        if ((i3 & 7) == 0) {
            i3 |= 8388611;
        }
        return (i3 & 112) == 0 ? i3 | 48 : i3;
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

    private void setFragmentPanelViewBottom(View view) {
        View viewFindViewById = view.findViewById(com.support.panel.R.id.bottom_sheet_dialog);
        if (viewFindViewById == null) {
            return;
        }
        viewFindViewById.setBottom(view.getHeight() - view.getPaddingBottom());
        View viewFindViewById2 = viewFindViewById.findViewById(com.support.panel.R.id.first_panel_container);
        if (viewFindViewById2 == null || !(viewFindViewById2 instanceof ViewGroup)) {
            return;
        }
        viewFindViewById2.setBottom(viewFindViewById.getHeight());
        View visiblePanelContentLayout = getVisiblePanelContentLayout((ViewGroup) viewFindViewById2);
        if (visiblePanelContentLayout != null) {
            visiblePanelContentLayout.setBottom(viewFindViewById2.getHeight());
        }
    }

    private void setNormalPanelViewBottom(View view, View view2) {
        view2.setBottom(view.getHeight() - view.getPaddingBottom());
    }

    private void setOutlineBottomOffset(int i3) {
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference == null || !(weakReference.get() instanceof COUIPanelPercentFrameLayout)) {
            return;
        }
        ((COUIPanelPercentFrameLayout) this.viewRef.get()).setOutlineBottomOffset(i3);
    }

    private void setShakeHandMovingDirection(float f3) {
        if (f3 > 100.0f) {
            this.mShakeHandMovingDirection = 2;
        } else if (f3 < -100.0f) {
            this.mShakeHandMovingDirection = 1;
        } else {
            this.mShakeHandMovingDirection = 0;
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
            v2.post(new AnonymousClass3(v2, i3));
        } else {
            settleToState(v2, i3);
        }
    }

    private void startHeightChangeAnimation(int i3, int i4) {
        OnPanelHeightChangeAnimListener onPanelHeightChangeAnimListener = this.mPanelHeightChangeAnimListener;
        if (onPanelHeightChangeAnimListener != null) {
            onPanelHeightChangeAnimListener.onAnimationStart();
        }
        this.mRunningHeightChangeAnimToken = this.mCurrentHeightChangeAnimToken;
        this.mPanelHeightChangeAnim.setStartValue(i3);
        this.mPanelHeightChangeAnim.animateToFinalPosition(i4);
    }

    private void startPanelTranslateAnimation(View view, int i3, int i4, float f3, PathInterpolator pathInterpolator) {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(i3, i4);
        valueAnimatorOfFloat.setDuration((long) f3);
        valueAnimatorOfFloat.setInterpolator(pathInterpolator);
        valueAnimatorOfFloat.addUpdateListener(new AnonymousClass5(view));
        valueAnimatorOfFloat.addListener(new AnonymousClass6());
        this.mLastOffsetInFling = view.getTop();
        view.offsetTopAndBottom(view.getTop());
        valueAnimatorOfFloat.start();
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

    public void applyPhysics(float f3, float f4) {
        if (f3 == Float.MIN_VALUE || f4 == Float.MIN_VALUE) {
            this.mPhysicsEnable = false;
            return;
        }
        this.mPhysicsEnable = true;
        this.mDragFrequency = f3;
        this.mDragDampingRatio = f4;
        this.mPhysicalAnimator = PhysicalAnimator.create(this.mContext);
        this.mDragValueHolder = new FloatValueHolder(0.0f);
        DragBehavior dragBehavior = (DragBehavior) ((DragBehavior) new DragBehavior().withProperty(this.mDragValueHolder)).setSpringProperty(this.mDragFrequency, this.mDragDampingRatio).applyTo(null);
        this.mDragBehavior = dragBehavior;
        this.mPhysicalAnimator.addBehavior(dragBehavior);
        this.mPhysicalAnimator.addAnimationListener(this.mDragBehavior, this);
        this.mPhysicalAnimator.addAnimationUpdateListener(this.mDragBehavior, this);
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

    public void forceSetPanelState(int i3) {
        WeakReference<V> weakReference;
        int targetTopForState;
        if (isPanelHeightChangeAnimRunning() && this.mPanelHeightChangeAnim != null && (targetTopForState = getTargetTopForState(i3)) != -1) {
            this.mSettleTargetState = i3;
            this.mPanelHeightChangeAnim.animateToFinalPosition(targetTopForState);
            this.mTargetState = i3;
            updateDrawableForTargetState(i3);
            return;
        }
        if (this.state == 2 && (weakReference = this.viewRef) != null && weakReference.get() != null) {
            int top = this.viewRef.get().getTop();
            if (top <= getExpandedOffset()) {
                this.state = 3;
            } else if (top >= this.collapsedOffset) {
                this.state = 4;
            } else if (Math.abs(top - this.halfExpandedOffset) < Math.abs(top - getExpandedOffset())) {
                this.state = 6;
            } else {
                this.state = 3;
            }
        }
        setPanelState(i3);
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

    public int getTargetState() {
        return (this.state == 2 || isPanelHeightChangeAnimRunning()) ? this.mSettleTargetState : this.state;
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

    public boolean isPanelHeightChangeAnimRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mPanelHeightChangeAnim;
        if (cOUISpringAnimation != null) {
            return cOUISpringAnimation.isRunning();
        }
        return false;
    }

    @Override // com.oplus.physicsengine.engine.AnimationListener
    public void onAnimationCancel(BaseBehavior baseBehavior) {
    }

    @Override // com.oplus.physicsengine.engine.AnimationListener
    public void onAnimationEnd(BaseBehavior baseBehavior) {
    }

    @Override // com.oplus.physicsengine.engine.AnimationListener
    public void onAnimationStart(BaseBehavior baseBehavior) {
    }

    @Override // com.oplus.physicsengine.engine.AnimationUpdateListener
    public void onAnimationUpdate(BaseBehavior baseBehavior) {
        if (baseBehavior.getAnimatedValue() != null) {
            this.mDragCurrentValue = ((Float) baseBehavior.getAnimatedValue()).floatValue();
        }
        View view = this.mDragChild;
        if (view != null) {
            ViewCompat.offsetTopAndBottom(this.mDragChild, -((int) (view.getTop() - this.mDragCurrentValue)));
            dispatchOnSlide(this.mDragChild.getTop());
        }
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
        COUIViewDragHelper cOUIViewDragHelper;
        if (!v2.isShown() || !this.draggable) {
            this.ignoreEvents = true;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            COUISpringAnimation cOUISpringAnimation = this.mPanelHeightChangeAnim;
            this.mPressDownState = (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) ? getState() : this.mSettleTargetState;
            reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (actionMasked == 0) {
            this.initialX = (int) motionEvent.getX();
            int y2 = (int) motionEvent.getY();
            this.initialY = y2;
            if (!this.mGlobalDrag && !isClickedOnBar(v2, this.initialX, y2)) {
                this.ignoreEvents = true;
                return false;
            }
            this.ignoreEvents = false;
            if (this.state != 2) {
                OnNestedScrollingChild onNestedScrollingChild = this.mOnNestedScrollingChild;
                if (onNestedScrollingChild != null && (this.nestedScrollingChildRef == null || onNestedScrollingChild.getNestedScrollingChild() != this.nestedScrollingChildRef.get())) {
                    this.nestedScrollingChildRef = new WeakReference<>(this.mOnNestedScrollingChild.getNestedScrollingChild());
                }
                WeakReference<View> weakReference = this.nestedScrollingChildRef;
                View view = weakReference != null ? weakReference.get() : null;
                if (view != null && coordinatorLayout.isPointInChildBounds(view, this.initialX, this.initialY)) {
                    this.activePointerId = motionEvent.getPointerId(UIUtil.getAdjustmentPointerIndex(motionEvent, motionEvent.getActionIndex()));
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
        if (!this.ignoreEvents && (cOUIViewDragHelper = this.viewDragHelper) != null && cOUIViewDragHelper.shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        WeakReference<View> weakReference2 = this.nestedScrollingChildRef;
        View view2 = weakReference2 != null ? weakReference2.get() : null;
        if (view2 != null) {
            return (actionMasked != 2 || this.ignoreEvents || this.state == 1 || coordinatorLayout.isPointInChildBounds(view2, this.initialX, this.initialY) || this.viewDragHelper == null || Math.abs(((float) this.initialY) - motionEvent.getY()) <= ((float) this.viewDragHelper.getTouchSlop())) ? false : true;
        }
        return (actionMasked != 2 || this.ignoreEvents || this.state == 1 || this.viewDragHelper == null || Math.abs(((float) this.initialY) - motionEvent.getY()) <= ((float) this.viewDragHelper.getTouchSlop())) ? false : true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v2, int i3) {
        boolean hasAnchor;
        MaterialShapeDrawable materialShapeDrawable;
        COUISpringAnimation cOUISpringAnimation;
        if (this.mTargetState == 5 && (cOUISpringAnimation = this.mPanelHeightChangeAnim) != null && cOUISpringAnimation.isRunning()) {
            Log.w(TAG, "onLayoutChild，mTargetState == STATE_HIDDEN return!");
            return true;
        }
        if (ViewCompat.getFitsSystemWindows(coordinatorLayout) && !ViewCompat.getFitsSystemWindows(v2)) {
            v2.setFitsSystemWindows(true);
        }
        float ratio = 1.0f;
        if (this.viewRef == null) {
            this.peekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            this.mDialogMaxHeight = this.mContext.getResources().getDimensionPixelOffset(com.support.panel.R.dimen.coui_panel_max_height);
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
            this.viewDragHelper = COUIViewDragHelper.create(coordinatorLayout, this.dragCallback);
        }
        int top = v2.getTop();
        int i4 = this.mViewHeightType;
        if (i4 == 1 || i4 == 3) {
            getLayoutRect(coordinatorLayout, v2, i3);
            Rect rect = this.mLayoutRect;
            v2.layout(rect.left, rect.top, rect.right, this.mLayoutBottom);
            View viewFindViewById = v2.findViewById(com.support.panel.R.id.coui_panel_content_layout);
            if (viewFindViewById != null) {
                if (viewFindViewById.getParent() == v2) {
                    setNormalPanelViewBottom(v2, viewFindViewById);
                } else {
                    setFragmentPanelViewBottom(v2);
                }
            }
        } else {
            coordinatorLayout.onLayoutChild(v2, i3);
        }
        this.parentWidth = coordinatorLayout.getWidth();
        this.parentHeight = coordinatorLayout.getHeight();
        if (ifInTopOfMultiWindowMode() && isImeVisible(v2)) {
            this.parentRootViewHeight = Math.max(this.parentRootViewHeight, UIUtil.getScreenHeightMetrics(this.mContext));
        } else {
            this.parentRootViewHeight = coordinatorLayout.getRootView().getHeight();
        }
        this.parentMarginTop = COUIViewMarginUtil.getMargin(coordinatorLayout, 1);
        if (DEBUG) {
            Log.d(TAG, "onLayoutChild: parentHeight=" + this.parentHeight + " parentRootViewHeight=" + this.parentRootViewHeight + " marginTop=" + this.parentMarginTop);
        }
        if (v2 instanceof COUIPanelPercentFrameLayout) {
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = (COUIPanelPercentFrameLayout) v2;
            ratio = cOUIPanelPercentFrameLayout.getRatio();
            hasAnchor = cOUIPanelPercentFrameLayout.getHasAnchor();
        } else {
            hasAnchor = false;
        }
        if (!this.mIsIgnoreExpandedOffsetChange) {
            int marginBottom = getMarginBottom(v2);
            if (hasAnchor) {
                this.fitToContentsOffset = 0;
            } else {
                this.fitToContentsOffset = (int) Math.max(0.0f, ((this.parentHeight - marginBottom) / ratio) - ((v2.getHeight() - this.mPanelPaddingBottom) / ratio));
            }
            if (this.mIsHandlePanel) {
                this.expandedOffset = this.fitToContentsOffset;
            }
        }
        if (DEBUG) {
            Log.d(TAG, "updateFollowHandPanelLocation fitToContentsOffset:" + this.fitToContentsOffset + " expandOffset=" + this.expandedOffset + " mIsHandlePanel=" + this.mIsHandlePanel);
        }
        this.mIsIgnoreExpandedOffsetChange = false;
        calculateHalfExpandedOffset();
        calculateCollapsedOffset();
        int i5 = this.state;
        if (i5 == 3) {
            int i6 = this.mViewHeightType;
            if (i6 == 1) {
                ViewCompat.offsetTopAndBottom(v2, isPanelHeightChangeAnimRunning() ? this.mCurTop : getExpandedOffset());
            } else if (i6 == 2) {
                ViewCompat.offsetTopAndBottom(v2, this.mCurTop);
            } else if (i6 == 3) {
                ViewCompat.offsetTopAndBottom(v2, isPanelHeightChangeAnimRunning() ? this.mCurTop : getExpandedOffset());
                if (isPanelHeightChangeAnimRunning()) {
                    setOutlineBottomOffset(Math.abs(getExpandedOffset() - this.mCurTop) * (-2));
                    v2.invalidateOutline();
                }
            } else if (i6 != 4) {
                ViewCompat.offsetTopAndBottom(v2, getExpandedOffset());
            } else {
                ViewCompat.offsetTopAndBottom(v2, this.mCurTop);
                setOutlineBottomOffset(Math.abs(this.mWantTop - this.mCurTop) * (-2));
                v2.invalidateOutline();
            }
        } else if (i5 == 6) {
            ViewCompat.offsetTopAndBottom(v2, this.halfExpandedOffset);
        } else if (this.hideable && i5 == 5) {
            ViewCompat.offsetTopAndBottom(v2, this.parentHeight);
        } else if (i5 == 4) {
            ViewCompat.offsetTopAndBottom(v2, this.collapsedOffset);
        } else if (i5 == 1 || i5 == 2) {
            ViewCompat.offsetTopAndBottom(v2, top - v2.getTop());
        }
        if (DEBUG) {
            Log.e(TAG, "behavior parentHeight: " + this.parentHeight + " marginBottom: " + getMarginBottom(v2) + "\n mDesignBottomSheetFrameLayout.getRatio()" + ratio + " fitToContentsOffset: " + this.fitToContentsOffset + " H: " + v2.getMeasuredHeight() + "\n Y: " + v2.getY() + " getExpandedOffset" + getExpandedOffset());
        }
        this.nestedScrollingChildRef = new WeakReference<>(findScrollingChild(v2));
        return true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, V v2, int i3, int i4, int i5, int i6) {
        checkOrientationChange();
        boolean zOnMeasureChild = super.onMeasureChild(coordinatorLayout, v2, i3, i4, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i5) + v2.getPaddingBottom(), View.MeasureSpec.getMode(i5)), i6);
        int measuredHeight = v2.getMeasuredHeight();
        this.mCurTop = v2.getTop();
        if (this.mStartHeightChangeAnim && getState() == 3 && !this.mLayoutAtMaxHeight && measuredHeight != this.mLastMeasureHeight) {
            if (this.mPanelHeightChangeAnim == null) {
                createPanelHeightChangeAnim();
            }
            this.mPanelHeightSpringForce.setResponse(0.4f);
            if (!isPanelHeightChangeAnimRunning()) {
                setOutlineBottomOffset(0);
            }
            this.mWantTop = getWantTop(v2, measuredHeight);
            int i7 = this.mLastMeasureHeight;
            if (measuredHeight < i7) {
                this.mLayoutBottom = i7;
                if (isPanelCenterDisplay()) {
                    this.mViewHeightType = 3;
                } else {
                    this.mViewHeightType = 1;
                }
                startHeightChangeAnimation(this.mCurTop, this.mWantTop);
            } else if (measuredHeight > i7) {
                if (isPanelCenterDisplay()) {
                    this.mViewHeightType = 4;
                } else {
                    this.mViewHeightType = 2;
                }
                startHeightChangeAnimation(this.mCurTop, this.mWantTop);
            }
        }
        this.mLastMeasureHeight = measuredHeight;
        return zOnMeasureChild;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v2, View view, float f3, float f4) {
        WeakReference<View> weakReference;
        this.mYVelocity = -f4;
        return !this.mIsNestedScrollingCheckEnabled && (weakReference = this.nestedScrollingChildRef) != null && view == weakReference.get() && (this.state != 3 || super.onNestedPreFling(coordinatorLayout, v2, view, f3, f4));
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v2, View view, int i3, int i4, int[] iArr, int i5) {
        if (i5 == 1 || isPanelHeightChangeAnimRunning() || this.mIsNestedScrollingCheckEnabled) {
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
                iArr[1] = top - getExpandedOffset();
                calculatePanelOutsideAlpha(v2);
                if (this.mPhysicsEnable) {
                    dragToNewTop(v2, getExpandedOffset());
                } else {
                    ViewCompat.offsetTopAndBottom(v2, -iArr[1]);
                }
                setStateInternal(3);
            } else {
                if (!this.draggable) {
                    return;
                }
                calculatePanelOutsideAlpha(v2);
                iArr[1] = i4;
                if (this.mPhysicsEnable) {
                    dragToNewTop(v2, i6);
                } else {
                    ViewCompat.offsetTopAndBottom(v2, -i4);
                }
                setStateInternal(1);
            }
        } else if (i4 < 0 && !view.canScrollVertically(-1)) {
            if (i6 > this.collapsedOffset && !this.hideable) {
                calculatePanelOutsideAlpha(v2);
                int i7 = this.collapsedOffset;
                int i8 = top - i7;
                iArr[1] = i8;
                if (this.mPhysicsEnable) {
                    dragToNewTop(v2, i7);
                } else {
                    ViewCompat.offsetTopAndBottom(v2, -i8);
                }
                setStateInternal(4);
            } else {
                if (!this.draggable) {
                    return;
                }
                iArr[1] = i4;
                if (i4 < -100) {
                    i4 = (int) (i4 * 0.5f);
                }
                calculatePanelOutsideAlpha(v2);
                if (this.mPhysicsEnable) {
                    dragToNewTop(v2, i6);
                } else {
                    ViewCompat.offsetTopAndBottom(v2, -i4);
                }
                setStateInternal(1);
            }
        }
        if (!this.mPhysicsEnable) {
            dispatchOnSlide(v2.getTop());
        }
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
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v2), (COUIBottomSheetBehavior<?>) this);
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
        if (this.mPhysicsEnable && this.mDragBehavior.isDragging()) {
            this.mDragBehavior.endDrag(0.0f);
            this.mDragChild = null;
        }
        int i5 = 3;
        if (v2.getTop() == getExpandedOffset()) {
            setStateInternal(3);
            return;
        }
        WeakReference<View> weakReference = this.nestedScrollingChildRef;
        if (weakReference != null && view == weakReference.get() && this.nestedScrolled) {
            if (this.lastNestedScrollDy <= 0) {
                if (this.hideable && shouldHide(v2, getYVelocity())) {
                    COUIPanelDragListener cOUIPanelDragListener = this.mCOUIPanelDragListener;
                    if (cOUIPanelDragListener == null || !cOUIPanelDragListener.onDragWhileEditing()) {
                        i4 = this.parentRootViewHeight;
                        this.mCanHideKeyboard = true;
                        i5 = 5;
                    } else {
                        i4 = this.fitToContentsOffset;
                        this.mCanHideKeyboard = false;
                    }
                } else if (this.lastNestedScrollDy == 0) {
                    int top = v2.getTop();
                    if (!this.fitToContents) {
                        int i6 = this.halfExpandedOffset;
                        if (top < i6) {
                            if (top < Math.abs(top - this.collapsedOffset)) {
                                i4 = this.expandedOffset;
                            } else {
                                i4 = this.halfExpandedOffset;
                            }
                        } else if (Math.abs(top - i6) < Math.abs(top - this.collapsedOffset)) {
                            i4 = this.halfExpandedOffset;
                        } else {
                            i4 = this.collapsedOffset;
                            i5 = 4;
                        }
                        i5 = 6;
                    } else if (Math.abs(top - this.fitToContentsOffset) < Math.abs(top - this.collapsedOffset)) {
                        i4 = this.fitToContentsOffset;
                    } else {
                        i4 = this.collapsedOffset;
                        i5 = 4;
                    }
                } else {
                    if (this.fitToContents) {
                        COUIPanelDragListener cOUIPanelDragListener2 = this.mCOUIPanelDragListener;
                        if (cOUIPanelDragListener2 == null) {
                            i4 = this.collapsedOffset;
                        } else if (cOUIPanelDragListener2.onDragWhileEditing()) {
                            i4 = this.fitToContentsOffset;
                        } else {
                            i4 = this.parentRootViewHeight;
                            i5 = 5;
                        }
                    } else {
                        int top2 = v2.getTop();
                        int i7 = this.halfExpandedOffset;
                        boolean z2 = top2 > i7 && top2 < this.collapsedOffset;
                        if (!(this.mPressDownState == 6 && z2) && Math.abs(top2 - i7) < Math.abs(top2 - this.collapsedOffset)) {
                            i4 = this.halfExpandedOffset;
                            i5 = 6;
                        } else {
                            i4 = this.collapsedOffset;
                        }
                    }
                    i5 = 4;
                }
            } else if (this.fitToContents) {
                i4 = this.fitToContentsOffset;
            } else {
                int top3 = v2.getTop();
                int i8 = this.halfExpandedOffset;
                if (top3 > i8) {
                    i5 = 6;
                    i4 = i8;
                } else {
                    i4 = this.expandedOffset;
                }
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
        COUIViewDragHelper cOUIViewDragHelper = this.viewDragHelper;
        if (cOUIViewDragHelper != null) {
            try {
                cOUIViewDragHelper.processTouchEvent(motionEvent);
            } catch (Exception e3) {
                e3.printStackTrace();
                return true;
            }
        }
        if (actionMasked == 0) {
            reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        this.mYVelocity = getYVelocity();
        if (actionMasked == 2 && !this.ignoreEvents && this.viewDragHelper != null && Math.abs(this.initialY - motionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
            this.viewDragHelper.captureChildView(v2, motionEvent.getPointerId(UIUtil.getAdjustmentPointerIndex(motionEvent, motionEvent.getActionIndex())));
        }
        return !this.ignoreEvents;
    }

    public void removeBottomSheetCallback(COUIBottomSheetCallback cOUIBottomSheetCallback) {
        this.callbacks.remove(cOUIBottomSheetCallback);
    }

    @Deprecated
    public void setBottomSheetCallback(COUIBottomSheetCallback cOUIBottomSheetCallback) {
        if (DEBUG) {
            Log.w(TAG, "BottomSheetBehavior now supports multiple callbacks. `setBottomSheetCallback()` removes all existing callbacks, including ones set internally by library authors, which may result in unintended behavior. This may change in the future. Please use `addBottomSheetCallback()` and `removeBottomSheetCallback()` instead to set your own callbacks.");
        }
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

    public void setGlobalDrag(boolean z2) {
        this.mGlobalDrag = z2;
    }

    public void setHalfExpandOffsetUseParentRootViewHeight(boolean z2) {
        this.mHalfExpandOffsetUseParentRootViewHeight = z2;
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

    public void setHeightChangeAnim(boolean z2) {
        WeakReference<V> weakReference;
        this.mStartHeightChangeAnim = z2;
        if (z2) {
            if (this.mCurrentHeightChangeAnimToken == Integer.MAX_VALUE) {
                this.mCurrentHeightChangeAnimToken = 0;
                this.mRunningHeightChangeAnimToken = -1;
            }
            this.mCurrentHeightChangeAnimToken++;
        }
        if (z2 && isPanelCenterDisplay() && (weakReference = this.viewRef) != null && (weakReference.get() instanceof COUIPanelPercentFrameLayout)) {
            ((COUIPanelPercentFrameLayout) this.viewRef.get()).updateClipToOutline(true);
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setHideable(boolean z2) {
        if (this.hideable != z2) {
            this.hideable = z2;
            if (!z2 && this.state == 5) {
                setPanelState(4);
            }
            updateAccessibilityActions();
        }
    }

    public void setIsHandlePanel(boolean z2) {
        this.mIsHandlePanel = z2;
    }

    public void setIsInTinyScreen(boolean z2) {
        this.mIsInTinyScreen = z2;
    }

    public void setIsNestedScrollingCheckEnabled(boolean z2) {
        this.mIsNestedScrollingCheckEnabled = z2;
    }

    public void setLayoutAtMaxHeight(boolean z2) {
        this.mLayoutAtMaxHeight = z2;
    }

    public void setOnNestedScrollingChild(OnNestedScrollingChild onNestedScrollingChild) {
        this.mOnNestedScrollingChild = onNestedScrollingChild;
    }

    public void setOnPanelHeightChangeAnimListener(OnPanelHeightChangeAnimListener onPanelHeightChangeAnimListener) {
        this.mPanelHeightChangeAnimListener = onPanelHeightChangeAnimListener;
    }

    public void setPanelDragListener(COUIPanelDragListener cOUIPanelDragListener) {
        this.mCOUIPanelDragListener = cOUIPanelDragListener;
    }

    public void setPanelPaddingBottom(int i3) {
        this.mPanelPaddingBottom = i3;
    }

    public void setPanelPeekHeight(int i3) {
        setPanelPeekHeight(i3, false);
    }

    public void setPanelSkipCollapsed(boolean z2) {
        this.skipCollapsed = z2;
    }

    public void setPanelState(int i3) {
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

    public void setPullUpListener(COUIPanelPullUpListener cOUIPanelPullUpListener) {
        this.mPullUpListener = cOUIPanelPullUpListener;
    }

    public void setPullUpToDismissPanelListener(PullUpToDismissPanelListener pullUpToDismissPanelListener) {
        this.mPullUpToDismissPanelListener = pullUpToDismissPanelListener;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void setSaveFlags(int i3) {
        this.saveFlags = i3;
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
            expandedOffset = this.parentRootViewHeight;
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

    public void startSettleRunnable(View view, int i3, int i4) {
        if (this.mPanelHeightChangeAnim == null) {
            createPanelHeightChangeAnim();
        }
        if (i3 == 5) {
            float top = this.parentHeight - view.getTop();
            float f3 = this.parentHeight;
            float f4 = 0.0f;
            if (top > 0.0f && f3 != 0.0f) {
                f4 = top / f3;
            }
            this.mPanelHeightSpringForce.setResponse(0.18f + (0.19f * f4));
        } else {
            this.mPanelHeightSpringForce.setResponse(0.4f);
        }
        if (this.mPanelHeightChangeAnim.isRunning()) {
            this.mSettleTargetState = i3;
            return;
        }
        this.mSettleTargetState = i3;
        COUIViewDragHelper cOUIViewDragHelper = this.viewDragHelper;
        if (cOUIViewDragHelper == null || cOUIViewDragHelper.getCapturedView() == null || this.viewDragHelper.getViewDragState() != 2) {
            setStateInternal(this.mSettleTargetState);
            return;
        }
        setShakeHandMovingDirection(this.mYVelocity);
        int top2 = this.viewDragHelper.getCapturedView().getTop();
        this.mStartTopValue = top2;
        this.mPanelHeightChangeAnim.setStartValue(top2);
        this.mPanelHeightChangeAnim.setStartVelocity(this.mYVelocity);
        this.mPanelHeightChangeAnim.animateToFinalPosition(i4);
    }

    public void startSettlingAnimation(View view, int i3, int i4, boolean z2) {
        COUIBottomSheetBehavior<V> cOUIBottomSheetBehavior;
        if ((z2 && getState() == 1) ? this.viewDragHelper.settleCapturedViewAt(view.getLeft(), i4) : this.viewDragHelper.smoothSlideViewTo(view, view.getLeft(), i4)) {
            setStateInternal(2);
            updateDrawableForTargetState(i3);
            getYVelocity();
            if (!this.mIsInTinyScreen) {
                cOUIBottomSheetBehavior = this;
                if (i3 == 5 && cOUIBottomSheetBehavior.isImeVisible(view) && UIUtil.isInFreeFormModeWindowMode(cOUIBottomSheetBehavior.mContext)) {
                    i4 += UIUtil.getScreenHeightMetrics(cOUIBottomSheetBehavior.mContext);
                }
                cOUIBottomSheetBehavior.startSettleRunnable(view, i3, i4);
            } else if (i3 == 5) {
                cOUIBottomSheetBehavior = this;
                cOUIBottomSheetBehavior.startPanelTranslateAnimation(view, 0, this.mContext.getResources().getDimensionPixelOffset(com.support.panel.R.dimen.coui_panel_max_height_tiny_screen), DEFAULT_TRANSLATE_HIDING_ANIMATOR_DURATION, new COUIOutEaseInterpolator());
            } else {
                cOUIBottomSheetBehavior = this;
                cOUIBottomSheetBehavior.startSettleRunnable(view, i3, i4);
            }
            cOUIBottomSheetBehavior.mTargetState = i3;
        } else {
            cOUIBottomSheetBehavior = this;
            cOUIBottomSheetBehavior.setStateInternal(i3);
        }
        PullUpToDismissPanelListener pullUpToDismissPanelListener = cOUIBottomSheetBehavior.mPullUpToDismissPanelListener;
        if (pullUpToDismissPanelListener == null || i3 != 5) {
            return;
        }
        pullUpToDismissPanelListener.onPullUpDismiss();
    }

    public void stopSettlingAnimationIfRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mPanelHeightChangeAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mPanelHeightChangeAnim.cancel();
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
}