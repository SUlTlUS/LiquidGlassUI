package com.coui.appcompat.poplist;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

/* JADX INFO: loaded from: classes7.dex */
public class RegionBasedLongPressDetector {
    private static final int INVALID_POSITION_DATA_INDEX = -1;
    private static final int LOCATION_COMPONENT_COUNT = 2;
    private static final int STATE_DETECTING = 1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_TRIGGERED = 2;
    private boolean mAnchorSlopExceeded;
    private float mAnchorSlopOriginX;
    private float mAnchorSlopOriginY;
    private CancelPredicate mCancelPredicate;
    private OnDetectionStateListener mDetectionStateListener;
    private boolean mLastTriggeredIsSubMenuAnchor;
    private ViewParent mLastTriggeredParent;
    private OnLongPressListener mListener;
    private Runnable mPendingLongPress;
    private final TargetResolver mResolver;
    private final View mRootView;
    private boolean mTargetIsSubMenuAnchor;
    private int mTargetPositionDataIndex;
    private View mTargetView;
    private final int mTouchSlop;
    private final Rect mTargetRect = new Rect();
    private final int[] mRootLocation = new int[2];
    private final int[] mTargetLocation = new int[2];
    private long mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
    private boolean mEnabled = true;
    private int mState = 0;
    private boolean mSubMenuJustEnteredNoMoveYet = false;
    private int mLastTriggeredPositionDataIndex = -1;

    @FunctionalInterface
    public interface CancelPredicate {
        boolean shouldCancel();
    }

    public interface OnDetectionStateListener {
        void onDetectionEnded();

        void onDetectionStarted(View view);
    }

    public interface OnLongPressListener {
        void onLongPress(View view, int i3, boolean z2);
    }

    public static final class Target {
        public final boolean hasSubMenu;
        public final boolean isSubMenuAnchor;
        public final int positionDataIndex;
        public final View view;

        public Target(View view, int i3, boolean z2) {
            this(view, i3, z2, false);
        }

        public Target(View view, int i3, boolean z2, boolean z3) {
            this.view = view;
            this.positionDataIndex = i3;
            this.hasSubMenu = z2;
            this.isSubMenuAnchor = z3;
        }
    }

    public interface TargetResolver {
        Target resolve(View view, float f3, float f4);
    }

    public RegionBasedLongPressDetector(View view, TargetResolver targetResolver) {
        if (view == null || targetResolver == null) {
            throw new IllegalArgumentException("rootView and resolver must be non-null");
        }
        this.mRootView = view;
        this.mResolver = targetResolver;
        this.mTouchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
    }

    public static /* synthetic */ void a(RegionBasedLongPressDetector regionBasedLongPressDetector, View view, int i3, boolean z2) {
        regionBasedLongPressDetector.mPendingLongPress = null;
        if (regionBasedLongPressDetector.mState == 1 && regionBasedLongPressDetector.mTargetView == view) {
            CancelPredicate cancelPredicate = regionBasedLongPressDetector.mCancelPredicate;
            if (cancelPredicate == null || !cancelPredicate.shouldCancel()) {
                regionBasedLongPressDetector.mState = 2;
                regionBasedLongPressDetector.mLastTriggeredParent = view != null ? view.getParent() : null;
                regionBasedLongPressDetector.mLastTriggeredPositionDataIndex = i3;
                regionBasedLongPressDetector.mLastTriggeredIsSubMenuAnchor = z2;
                if (view == null || !view.isAttachedToWindow()) {
                    View view2 = regionBasedLongPressDetector.mRootView;
                    if (view2 != null && view2.isAttachedToWindow()) {
                        regionBasedLongPressDetector.mRootView.performHapticFeedback(0);
                    }
                } else {
                    view.performHapticFeedback(0);
                }
                OnLongPressListener onLongPressListener = regionBasedLongPressDetector.mListener;
                if (onLongPressListener != null && view != null) {
                    onLongPressListener.onLongPress(view, i3, z2);
                }
                OnDetectionStateListener onDetectionStateListener = regionBasedLongPressDetector.mDetectionStateListener;
                if (onDetectionStateListener != null) {
                    onDetectionStateListener.onDetectionEnded();
                }
                regionBasedLongPressDetector.mState = 0;
                regionBasedLongPressDetector.mTargetView = null;
            }
        }
    }

    private boolean anchorRequiresTouchSlop() {
        return (this.mLastTriggeredParent == null || this.mLastTriggeredIsSubMenuAnchor) ? false : true;
    }

    private void cancelScheduledLongPress() {
        Runnable runnable = this.mPendingLongPress;
        if (runnable != null) {
            this.mRootView.removeCallbacks(runnable);
            this.mPendingLongPress = null;
        }
    }

    private boolean hasExceededAnchorTouchSlop(float f3, float f4) {
        float f5 = f3 - this.mAnchorSlopOriginX;
        float f6 = f4 - this.mAnchorSlopOriginY;
        float f7 = (f5 * f5) + (f6 * f6);
        int i3 = this.mTouchSlop;
        return f7 > ((float) (i3 * i3));
    }

    private boolean isPointInTargetView(float f3, float f4) {
        if (this.mTargetView == null) {
            return false;
        }
        this.mRootView.getLocationOnScreen(this.mRootLocation);
        this.mTargetView.getLocationOnScreen(this.mTargetLocation);
        int[] iArr = this.mRootLocation;
        float f5 = f3 + iArr[0];
        float f6 = f4 + iArr[1];
        Rect rect = this.mTargetRect;
        int[] iArr2 = this.mTargetLocation;
        int i3 = iArr2[0];
        rect.set(i3, iArr2[1], this.mTargetView.getWidth() + i3, this.mTargetLocation[1] + this.mTargetView.getHeight());
        return this.mTargetRect.contains((int) f5, (int) f6);
    }

    private void resetAnchorSlopState(float f3, float f4) {
        this.mAnchorSlopOriginX = f3;
        this.mAnchorSlopOriginY = f4;
        this.mAnchorSlopExceeded = false;
    }

    private void scheduleLongPress() {
        if (this.mTargetIsSubMenuAnchor && anchorRequiresTouchSlop() && !this.mAnchorSlopExceeded) {
            return;
        }
        cancelScheduledLongPress();
        d0 d0Var = new d0(this, this.mTargetView, this.mTargetPositionDataIndex, this.mTargetIsSubMenuAnchor);
        this.mPendingLongPress = d0Var;
        this.mRootView.postDelayed(d0Var, this.mLongPressTimeout);
    }

    private void startDetection(float f3, float f4) {
        View view;
        View view2;
        stopDetection();
        Target targetResolve = this.mResolver.resolve(this.mRootView, f3, f4);
        if (targetResolve == null || (view = targetResolve.view) == null) {
            return;
        }
        if (targetResolve.hasSubMenu || targetResolve.isSubMenuAnchor) {
            this.mTargetView = view;
            this.mTargetPositionDataIndex = targetResolve.positionDataIndex;
            this.mTargetIsSubMenuAnchor = targetResolve.isSubMenuAnchor;
            CancelPredicate cancelPredicate = this.mCancelPredicate;
            if (cancelPredicate == null || !cancelPredicate.shouldCancel()) {
                if (!(this.mLastTriggeredParent != null && (view2 = targetResolve.view) != null && view2.getParent() == this.mLastTriggeredParent && targetResolve.positionDataIndex == this.mLastTriggeredPositionDataIndex && targetResolve.isSubMenuAnchor == this.mLastTriggeredIsSubMenuAnchor) && isPointInTargetView(f3, f4)) {
                    this.mState = 1;
                    OnDetectionStateListener onDetectionStateListener = this.mDetectionStateListener;
                    if (onDetectionStateListener != null) {
                        onDetectionStateListener.onDetectionStarted(this.mTargetView);
                    }
                    if (!this.mTargetIsSubMenuAnchor) {
                        scheduleLongPress();
                    } else if (anchorRequiresTouchSlop()) {
                        resetAnchorSlopState(f3, f4);
                    } else {
                        this.mAnchorSlopExceeded = true;
                        scheduleLongPress();
                    }
                }
            }
        }
    }

    private void stopDetection() {
        this.mAnchorSlopExceeded = false;
        OnDetectionStateListener onDetectionStateListener = this.mDetectionStateListener;
        if (onDetectionStateListener != null) {
            onDetectionStateListener.onDetectionEnded();
        }
        cancelScheduledLongPress();
        this.mState = 0;
        this.mTargetView = null;
        this.mTargetIsSubMenuAnchor = false;
    }

    private void tryScheduleAnchorLongPress(float f3, float f4) {
        if (!this.mAnchorSlopExceeded && hasExceededAnchorTouchSlop(f3, f4)) {
            this.mAnchorSlopExceeded = true;
            if (this.mSubMenuJustEnteredNoMoveYet) {
                this.mSubMenuJustEnteredNoMoveYet = false;
            }
        }
        if (this.mAnchorSlopExceeded && this.mPendingLongPress == null) {
            scheduleLongPress();
        }
    }

    private void updatePosition(float f3, float f4) {
        int i3 = this.mState;
        if (i3 == 2) {
            return;
        }
        if (i3 == 0) {
            startDetection(f3, f4);
            return;
        }
        if (this.mTargetView == null) {
            return;
        }
        CancelPredicate cancelPredicate = this.mCancelPredicate;
        if (cancelPredicate != null && cancelPredicate.shouldCancel()) {
            stopDetection();
            return;
        }
        if (isPointInTargetView(f3, f4)) {
            int i4 = this.mState;
            if (i4 == 1 && this.mTargetIsSubMenuAnchor) {
                tryScheduleAnchorLongPress(f3, f4);
                return;
            } else {
                if (i4 == 1 && this.mPendingLongPress == null) {
                    scheduleLongPress();
                    return;
                }
                return;
            }
        }
        this.mSubMenuJustEnteredNoMoveYet = false;
        this.mAnchorSlopExceeded = false;
        cancelScheduledLongPress();
        this.mState = 0;
        this.mTargetView = null;
        OnDetectionStateListener onDetectionStateListener = this.mDetectionStateListener;
        if (onDetectionStateListener != null) {
            onDetectionStateListener.onDetectionEnded();
        }
    }

    public void clearLastTriggeredInSequence() {
        this.mLastTriggeredParent = null;
        this.mLastTriggeredPositionDataIndex = -1;
        this.mLastTriggeredIsSubMenuAnchor = false;
    }

    public long getLongPressTimeout() {
        return this.mLongPressTimeout;
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0022  */
    public boolean handleTouchEvent(MotionEvent motionEvent) {
        if (!this.mEnabled) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        if (actionMasked == 0) {
            startDetection(x2, y2);
        } else if (actionMasked == 1) {
            this.mLastTriggeredParent = null;
            this.mLastTriggeredPositionDataIndex = -1;
            this.mSubMenuJustEnteredNoMoveYet = false;
            stopDetection();
        } else if (actionMasked == 2) {
            updatePosition(x2, y2);
        } else if (actionMasked == 3) {
            this.mLastTriggeredParent = null;
            this.mLastTriggeredPositionDataIndex = -1;
            this.mSubMenuJustEnteredNoMoveYet = false;
            stopDetection();
        }
        return false;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setCancelPredicate(CancelPredicate cancelPredicate) {
        this.mCancelPredicate = cancelPredicate;
    }

    public void setEnabled(boolean z2) {
        if (!z2) {
            stopDetection();
        }
        this.mEnabled = z2;
    }

    public void setLongPressTimeout(long j3) {
        if (j3 <= 0) {
            j3 = ViewConfiguration.getLongPressTimeout();
        }
        this.mLongPressTimeout = j3;
    }

    public void setOnDetectionStateListener(OnDetectionStateListener onDetectionStateListener) {
        this.mDetectionStateListener = onDetectionStateListener;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        this.mListener = onLongPressListener;
    }

    public void setSubMenuJustEntered(boolean z2) {
        if (z2 && this.mLastTriggeredParent == null) {
            return;
        }
        this.mSubMenuJustEnteredNoMoveYet = z2;
    }
}