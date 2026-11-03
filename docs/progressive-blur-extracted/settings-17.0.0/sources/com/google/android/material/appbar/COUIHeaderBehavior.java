package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes.dex */
abstract class COUIHeaderBehavior<V extends View> extends COUIViewOffsetBehavior<V> {
    private static final int INVALID_POINTER = -1;
    private int activePointerId;
    private Runnable flingRunnable;
    private boolean isBeingDragged;
    private int lastMotionY;
    OverScroller scroller;
    private int touchSlop;
    private VelocityTracker velocityTracker;

    /* JADX INFO: loaded from: classes8.dex */
    public class FlingRunnable implements Runnable {
        private final V layout;
        private final CoordinatorLayout parent;

        public FlingRunnable(CoordinatorLayout coordinatorLayout, V v2) {
            this.parent = coordinatorLayout;
            this.layout = v2;
        }

        @Override // java.lang.Runnable
        public void run() {
            OverScroller overScroller;
            if (this.layout == null || (overScroller = COUIHeaderBehavior.this.scroller) == null) {
                return;
            }
            if (!overScroller.computeScrollOffset()) {
                COUIHeaderBehavior.this.onFlingFinished(this.parent, this.layout);
                return;
            }
            COUIHeaderBehavior cOUIHeaderBehavior = COUIHeaderBehavior.this;
            cOUIHeaderBehavior.setHeaderTopBottomOffset(this.parent, this.layout, cOUIHeaderBehavior.scroller.getCurrY());
            ViewCompat.postOnAnimation(this.layout, this);
        }
    }

    public COUIHeaderBehavior() {
        this.activePointerId = -1;
        this.touchSlop = -1;
    }

    private void ensureVelocityTracker() {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
    }

    public boolean canDragView(V v2) {
        return false;
    }

    public boolean fling(CoordinatorLayout coordinatorLayout, V v2, int i3, int i4, float f3) {
        Runnable runnable = this.flingRunnable;
        if (runnable != null) {
            v2.removeCallbacks(runnable);
            this.flingRunnable = null;
        }
        if (this.scroller == null) {
            this.scroller = new OverScroller(v2.getContext());
        }
        this.scroller.fling(0, getTopAndBottomOffset(), 0, Math.round(f3), 0, 0, i3, i4);
        if (!this.scroller.computeScrollOffset()) {
            onFlingFinished(coordinatorLayout, v2);
            return false;
        }
        FlingRunnable flingRunnable = new FlingRunnable(coordinatorLayout, v2);
        this.flingRunnable = flingRunnable;
        ViewCompat.postOnAnimation(v2, flingRunnable);
        return true;
    }

    public int getMaxDragOffset(V v2) {
        return -v2.getHeight();
    }

    public int getScrollRangeForDragFling(V v2) {
        return v2.getHeight();
    }

    public int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }

    public int layoutRequestHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v2, int i3) {
        return setHeaderTopBottomOffset(coordinatorLayout, v2, i3, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public void onFlingFinished(CoordinatorLayout coordinatorLayout, V v2) {
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v2, MotionEvent motionEvent) {
        int iFindPointerIndex;
        if (this.touchSlop < 0) {
            this.touchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getActionMasked() == 2 && this.isBeingDragged) {
            int i3 = this.activePointerId;
            if (i3 == -1 || (iFindPointerIndex = motionEvent.findPointerIndex(i3)) == -1) {
                return false;
            }
            int y2 = (int) motionEvent.getY(iFindPointerIndex);
            if (Math.abs(y2 - this.lastMotionY) > this.touchSlop) {
                this.lastMotionY = y2;
                return true;
            }
        }
        if (motionEvent.getActionMasked() == 0) {
            this.activePointerId = -1;
            int x2 = (int) motionEvent.getX();
            int y3 = (int) motionEvent.getY();
            boolean z2 = canDragView(v2) && coordinatorLayout.isPointInChildBounds(v2, x2, y3);
            this.isBeingDragged = z2;
            if (z2) {
                this.lastMotionY = y3;
                this.activePointerId = motionEvent.getPointerId(0);
                ensureVelocityTracker();
                OverScroller overScroller = this.scroller;
                if (overScroller != null && !overScroller.isFinished()) {
                    this.scroller.abortAnimation();
                    return true;
                }
            }
        }
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.addMovement(motionEvent);
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:30:0x0080  */
    /* JADX WARN: Code duplicated, block: B:33:0x008a  */
    /* JADX WARN: Code duplicated, block: B:36:0x0091 A[ADDED_TO_REGION] */
    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v2, MotionEvent motionEvent) {
        boolean z2;
        VelocityTracker velocityTracker;
        VelocityTracker velocityTracker2;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1) {
            VelocityTracker velocityTracker3 = this.velocityTracker;
            if (velocityTracker3 != null) {
                velocityTracker3.addMovement(motionEvent);
                this.velocityTracker.computeCurrentVelocity(1000);
                fling(coordinatorLayout, v2, -getScrollRangeForDragFling(v2), 0, this.velocityTracker.getYVelocity(this.activePointerId));
                z2 = true;
            }
            this.isBeingDragged = false;
            this.activePointerId = -1;
            velocityTracker = this.velocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.velocityTracker = null;
            }
            velocityTracker2 = this.velocityTracker;
            if (velocityTracker2 != null) {
                velocityTracker2.addMovement(motionEvent);
            }
            if (this.isBeingDragged) {
            }
        }
        if (actionMasked != 2) {
            if (actionMasked != 3) {
                if (actionMasked == 6) {
                    int i3 = motionEvent.getActionIndex() == 0 ? 1 : 0;
                    this.activePointerId = motionEvent.getPointerId(i3);
                    this.lastMotionY = (int) (motionEvent.getY(i3) + 0.5f);
                }
            }
        } else if (this.isBeingDragged) {
            int iFindPointerIndex = motionEvent.findPointerIndex(this.activePointerId);
            if (iFindPointerIndex == -1) {
                return false;
            }
            int y2 = (int) motionEvent.getY(iFindPointerIndex);
            int i4 = this.lastMotionY - y2;
            this.lastMotionY = y2;
            scroll(coordinatorLayout, v2, i4, getMaxDragOffset(v2), 0, 1);
        }
        z2 = false;
        velocityTracker2 = this.velocityTracker;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(motionEvent);
        }
        return !this.isBeingDragged || z2;
        z2 = false;
        this.isBeingDragged = false;
        this.activePointerId = -1;
        velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.velocityTracker = null;
        }
        velocityTracker2 = this.velocityTracker;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(motionEvent);
        }
        if (this.isBeingDragged) {
        }
    }

    public int scroll(CoordinatorLayout coordinatorLayout, V v2, int i3, int i4, int i5, int i6) {
        return setHeaderTopBottomOffset(coordinatorLayout, v2, getTopBottomOffsetForScrollingSibling() - i3, i4, i5);
    }

    public int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v2, int i3) {
        return setHeaderTopBottomOffset(coordinatorLayout, v2, i3, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v2, int i3, int i4, int i5) {
        int iClamp;
        int topAndBottomOffset = getTopAndBottomOffset();
        if (i4 == 0 || topAndBottomOffset < i4 || topAndBottomOffset > i5 || topAndBottomOffset == (iClamp = MathUtils.clamp(i3, i4, i5))) {
            return 0;
        }
        setTopAndBottomOffset(iClamp);
        return topAndBottomOffset - iClamp;
    }

    public COUIHeaderBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.activePointerId = -1;
        this.touchSlop = -1;
    }
}