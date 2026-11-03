package com.coui.appcompat.panel;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import java.util.Arrays;

/* JADX INFO: loaded from: classes7.dex */
public class COUIViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "COUIViewDragHelper";
    private static final Interpolator sInterpolator = new AnonymousClass1();
    private final Callback mCallback;
    private View mCapturedView;
    private final int mDefaultEdgeSize;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private OverScroller mScroller;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;
    private int mActivePointerId = -1;
    private final Runnable mSetIdleRunnable = new AnonymousClass2();

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIViewDragHelper$1, reason: invalid class name */
    public class AnonymousClass1 implements Interpolator {
        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f3) {
            float f4 = f3 - 1.0f;
            return (f4 * f4 * f4 * f4 * f4) + 1.0f;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIViewDragHelper$2, reason: invalid class name */
    public class AnonymousClass2 implements Runnable {
        public AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIViewDragHelper.this.setDragState(0);
        }
    }

    public static abstract class Callback {
        public int clampViewPositionHorizontal(View view, int i3, int i4) {
            return 0;
        }

        public int clampViewPositionVertical(View view, int i3, int i4) {
            return 0;
        }

        public int getOrderedChildIndex(int i3) {
            return i3;
        }

        public int getViewHorizontalDragRange(View view) {
            return 0;
        }

        public int getViewVerticalDragRange(View view) {
            return 0;
        }

        public void onEdgeDragStarted(int i3, int i4) {
        }

        public boolean onEdgeLock(int i3) {
            return false;
        }

        public void onEdgeTouched(int i3, int i4) {
        }

        public void onViewCaptured(View view, int i3) {
        }

        public void onViewDragStateChanged(int i3) {
        }

        public void onViewPositionChanged(View view, int i3, int i4, int i5, int i6) {
        }

        public void onViewReleased(View view, float f3, float f4) {
        }

        public abstract boolean tryCaptureView(View view, int i3);
    }

    private COUIViewDragHelper(Context context, ViewGroup viewGroup, Callback callback) {
        if (viewGroup == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback may not be null");
        }
        this.mParentView = viewGroup;
        this.mCallback = callback;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        int i3 = (int) ((context.getResources().getDisplayMetrics().density * 20.0f) + 0.5f);
        this.mDefaultEdgeSize = i3;
        this.mEdgeSize = i3;
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    private boolean checkNewEdgeDrag(float f3, float f4, int i3, int i4) {
        float fAbs = Math.abs(f3);
        float fAbs2 = Math.abs(f4);
        if ((this.mInitialEdgesTouched[i3] & i4) == i4 && (this.mTrackingEdges & i4) != 0 && (this.mEdgeDragsLocked[i3] & i4) != i4 && (this.mEdgeDragsInProgress[i3] & i4) != i4) {
            int i5 = this.mTouchSlop;
            if (fAbs > i5 || fAbs2 > i5) {
                if (fAbs < fAbs2 * 0.5f && this.mCallback.onEdgeLock(i4)) {
                    int[] iArr = this.mEdgeDragsLocked;
                    iArr[i3] = iArr[i3] | i4;
                    return false;
                }
                if ((this.mEdgeDragsInProgress[i3] & i4) == 0 && fAbs > this.mTouchSlop) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkTouchSlop(View view, float f3, float f4) {
        if (view == null) {
            return false;
        }
        boolean z2 = this.mCallback.getViewHorizontalDragRange(view) > 0;
        boolean z3 = this.mCallback.getViewVerticalDragRange(view) > 0;
        if (z2 && z3) {
            float f5 = (f3 * f3) + (f4 * f4);
            int i3 = this.mTouchSlop;
            return f5 > ((float) (i3 * i3));
        }
        if (z2) {
            return Math.abs(f3) > ((float) this.mTouchSlop);
        }
        return z3 && Math.abs(f4) > ((float) this.mTouchSlop);
    }

    private int clampMag(int i3, int i4, int i5) {
        int iAbs = Math.abs(i3);
        if (iAbs < i4) {
            return 0;
        }
        if (iAbs > i5) {
            return i3 > 0 ? i5 : -i5;
        }
        return i3;
    }

    private void clearMotionHistory() {
        float[] fArr = this.mInitialMotionX;
        if (fArr == null) {
            return;
        }
        Arrays.fill(fArr, 0.0f);
        Arrays.fill(this.mInitialMotionY, 0.0f);
        Arrays.fill(this.mLastMotionX, 0.0f);
        Arrays.fill(this.mLastMotionY, 0.0f);
        Arrays.fill(this.mInitialEdgesTouched, 0);
        Arrays.fill(this.mEdgeDragsInProgress, 0);
        Arrays.fill(this.mEdgeDragsLocked, 0);
        this.mPointersDown = 0;
    }

    private int computeAxisDuration(int i3, int i4, int i5) {
        int iAbs;
        if (i3 == 0) {
            return 0;
        }
        int width = this.mParentView.getWidth();
        float f3 = width / 2;
        float fDistanceInfluenceForSnapDuration = f3 + (distanceInfluenceForSnapDuration(Math.min(1.0f, Math.abs(i3) / width)) * f3);
        int iAbs2 = Math.abs(i4);
        if (iAbs2 > 0) {
            iAbs = Math.round(Math.abs(fDistanceInfluenceForSnapDuration / iAbs2) * 1000.0f) * 4;
        } else {
            iAbs = (int) (((Math.abs(i3) / (i5 == 0 ? 1 : Math.abs(i5))) + 1.0f) * 256.0f);
        }
        return Math.min(iAbs, 600);
    }

    private int computeSettleDuration(View view, int i3, int i4, int i5, int i6) {
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        int iClampMag = clampMag(i5, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int iClampMag2 = clampMag(i6, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int iAbs = Math.abs(i3);
        int iAbs2 = Math.abs(i4);
        int iAbs3 = Math.abs(iClampMag);
        int iAbs4 = Math.abs(iClampMag2);
        int i7 = iAbs3 + iAbs4;
        int i8 = iAbs + iAbs2;
        if (i7 <= 0) {
            if (i8 > 0) {
                f5 = i8;
                f3 = iAbs / f5;
                f6 = iAbs2;
            } else {
                f3 = 0.5f;
                f4 = 0.5f;
            }
            return (int) ((computeAxisDuration(i3, iClampMag, this.mCallback.getViewHorizontalDragRange(view)) * f3) + (computeAxisDuration(i4, iClampMag2, this.mCallback.getViewVerticalDragRange(view)) * f4));
        }
        if (iClampMag != 0) {
            f7 = iAbs3;
            f8 = i7;
        } else {
            f7 = iAbs;
            f8 = i8;
        }
        f3 = f7 / f8;
        if (iClampMag2 != 0) {
            f6 = iAbs4;
            f5 = i7;
        } else {
            f6 = iAbs2;
            f5 = i8;
        }
        f4 = f6 / f5;
        return (int) ((computeAxisDuration(i3, iClampMag, this.mCallback.getViewHorizontalDragRange(view)) * f3) + (computeAxisDuration(i4, iClampMag2, this.mCallback.getViewVerticalDragRange(view)) * f4));
    }

    public static COUIViewDragHelper create(ViewGroup viewGroup, Callback callback) {
        return new COUIViewDragHelper(viewGroup.getContext(), viewGroup, callback);
    }

    private void dispatchViewReleased(float f3, float f4) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, f3, f4);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            setDragState(0);
        }
    }

    private float distanceInfluenceForSnapDuration(float f3) {
        return (float) Math.sin((f3 - 0.5f) * 0.47123894f);
    }

    private void dragTo(int i3, int i4, int i5, int i6) {
        int left = this.mCapturedView.getLeft();
        int top = this.mCapturedView.getTop();
        if (i5 != 0) {
            i3 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, i3, i5);
            ViewCompat.offsetLeftAndRight(this.mCapturedView, i3 - left);
        }
        int i7 = i3;
        if (i6 != 0) {
            i4 = this.mCallback.clampViewPositionVertical(this.mCapturedView, i4, i6);
            ViewCompat.offsetTopAndBottom(this.mCapturedView, i4 - top);
        }
        int i8 = i4;
        if (i5 == 0 && i6 == 0) {
            return;
        }
        this.mCallback.onViewPositionChanged(this.mCapturedView, i7, i8, i7 - left, i8 - top);
    }

    private void ensureMotionHistorySizeForId(int i3) {
        float[] fArr = this.mInitialMotionX;
        if (fArr == null || fArr.length <= i3) {
            int i4 = i3 + 1;
            float[] fArr2 = new float[i4];
            float[] fArr3 = new float[i4];
            float[] fArr4 = new float[i4];
            float[] fArr5 = new float[i4];
            int[] iArr = new int[i4];
            int[] iArr2 = new int[i4];
            int[] iArr3 = new int[i4];
            if (fArr != null) {
                System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
                float[] fArr6 = this.mInitialMotionY;
                System.arraycopy(fArr6, 0, fArr3, 0, fArr6.length);
                float[] fArr7 = this.mLastMotionX;
                System.arraycopy(fArr7, 0, fArr4, 0, fArr7.length);
                float[] fArr8 = this.mLastMotionY;
                System.arraycopy(fArr8, 0, fArr5, 0, fArr8.length);
                int[] iArr4 = this.mInitialEdgesTouched;
                System.arraycopy(iArr4, 0, iArr, 0, iArr4.length);
                int[] iArr5 = this.mEdgeDragsInProgress;
                System.arraycopy(iArr5, 0, iArr2, 0, iArr5.length);
                int[] iArr6 = this.mEdgeDragsLocked;
                System.arraycopy(iArr6, 0, iArr3, 0, iArr6.length);
            }
            this.mInitialMotionX = fArr2;
            this.mInitialMotionY = fArr3;
            this.mLastMotionX = fArr4;
            this.mLastMotionY = fArr5;
            this.mInitialEdgesTouched = iArr;
            this.mEdgeDragsInProgress = iArr2;
            this.mEdgeDragsLocked = iArr3;
        }
    }

    private boolean forceSettleCapturedViewAt(int i3, int i4, int i5, int i6) {
        int left = i3 - this.mCapturedView.getLeft();
        int top = i4 - this.mCapturedView.getTop();
        if (left == 0 && top == 0) {
            setDragState(0);
            return false;
        }
        computeSettleDuration(this.mCapturedView, left, top, i5, i6);
        setDragState(2);
        return true;
    }

    private int getEdgesTouched(int i3, int i4) {
        int i5 = i3 < this.mParentView.getLeft() + this.mEdgeSize ? 1 : 0;
        if (i4 < this.mParentView.getTop() + this.mEdgeSize) {
            i5 |= 4;
        }
        if (i3 > this.mParentView.getRight() - this.mEdgeSize) {
            i5 |= 2;
        }
        return i4 > this.mParentView.getBottom() - this.mEdgeSize ? i5 | 8 : i5;
    }

    private boolean isValidPointerForActionMove(int i3) {
        if (isPointerDown(i3)) {
            return true;
        }
        Log.e(TAG, "Ignoring pointerId=" + i3 + " because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  COUIViewDragHelper did not receive all the events in the event stream.");
        return false;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        dispatchViewReleased(clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v16 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4, types: [int] */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.coui.appcompat.panel.COUIViewDragHelper$Callback] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void reportNewEdgeDrags(float f3, float f4, int i3) {
        int i4;
        boolean zCheckNewEdgeDrag = checkNewEdgeDrag(f3, f4, i3, 1);
        ?? r2 = zCheckNewEdgeDrag;
        if (checkNewEdgeDrag(f4, f3, i3, 4)) {
            r2 = (zCheckNewEdgeDrag ? 1 : 0) | 4;
        }
        ?? r3 = r2;
        if (checkNewEdgeDrag(f3, f4, i3, 2)) {
            r3 = (r2 == true ? 1 : 0) | 2;
        }
        ?? r4 = r3;
        if (checkNewEdgeDrag(f4, f3, i3, 8)) {
            i4 = (r3 == true ? 1 : 0) | 8;
        }
        if (r4 == 0) {
            r4 = i4;
            return;
        }
        r4 = i4;
        int[] iArr = this.mEdgeDragsInProgress;
        iArr[i3] = (iArr[i3] | r4) == true ? 1 : 0;
        this.mCallback.onEdgeDragStarted(r4, i3);
    }

    private void saveInitialMotion(float f3, float f4, int i3) {
        ensureMotionHistorySizeForId(i3);
        float[] fArr = this.mInitialMotionX;
        this.mLastMotionX[i3] = f3;
        fArr[i3] = f3;
        float[] fArr2 = this.mInitialMotionY;
        this.mLastMotionY[i3] = f4;
        fArr2[i3] = f4;
        this.mInitialEdgesTouched[i3] = getEdgesTouched((int) f3, (int) f4);
        this.mPointersDown |= 1 << i3;
    }

    private void saveLastMotion(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        for (int i3 = 0; i3 < pointerCount; i3++) {
            int pointerId = motionEvent.getPointerId(i3);
            if (isValidPointerForActionMove(pointerId)) {
                float x2 = motionEvent.getX(i3);
                float y2 = motionEvent.getY(i3);
                this.mLastMotionX[pointerId] = x2;
                this.mLastMotionY[pointerId] = y2;
            }
        }
    }

    public void abort() {
        cancel();
        if (this.mDragState == 2) {
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            int currX2 = this.mScroller.getCurrX();
            int currY2 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        setDragState(0);
    }

    public boolean canScroll(View view, boolean z2, int i3, int i4, int i5, int i6) {
        int i7;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                int i8 = i5 + scrollX;
                if (i8 >= childAt.getLeft() && i8 < childAt.getRight() && (i7 = i6 + scrollY) >= childAt.getTop() && i7 < childAt.getBottom() && canScroll(childAt, true, i3, i4, i8 - childAt.getLeft(), i7 - childAt.getTop())) {
                    return true;
                }
            }
        }
        if (z2) {
            return view.canScrollHorizontally(-i3) || view.canScrollVertically(-i4);
        }
        return false;
    }

    public void cancel() {
        this.mActivePointerId = -1;
        clearMotionHistory();
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void captureChildView(View view, int i3) {
        if (view.getParent() == this.mParentView) {
            this.mCapturedView = view;
            this.mActivePointerId = i3;
            this.mCallback.onViewCaptured(view, i3);
            setDragState(1);
            return;
        }
        throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the COUIViewDragHelper's tracked parent view (" + this.mParentView + ")");
    }

    public boolean continueSettling(boolean z2) {
        if (this.mDragState == 2) {
            boolean zComputeScrollOffset = this.mScroller.computeScrollOffset();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            int left = currX - this.mCapturedView.getLeft();
            int top = currY - this.mCapturedView.getTop();
            if (left != 0) {
                ViewCompat.offsetLeftAndRight(this.mCapturedView, left);
            }
            if (top != 0) {
                ViewCompat.offsetTopAndBottom(this.mCapturedView, top);
            }
            if (left != 0 || top != 0) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, currX, currY, left, top);
            }
            if (zComputeScrollOffset && currX == this.mScroller.getFinalX() && currY == this.mScroller.getFinalY()) {
                this.mScroller.abortAnimation();
                zComputeScrollOffset = false;
            }
            if (!zComputeScrollOffset) {
                if (z2) {
                    this.mParentView.post(this.mSetIdleRunnable);
                } else {
                    setDragState(0);
                }
            }
        }
        return this.mDragState == 2;
    }

    public View findTopChildUnder(int i3, int i4) {
        for (int childCount = this.mParentView.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(childCount));
            if (i3 >= childAt.getLeft() && i3 < childAt.getRight() && i4 >= childAt.getTop() && i4 < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    public void flingCapturedView(int i3, int i4, int i5, int i6) {
        if (!this.mReleaseInProgress) {
            throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
        }
        this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId), i3, i5, i4, i6);
        setDragState(2);
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getDefaultEdgeSize() {
        return this.mDefaultEdgeSize;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public boolean isCapturedViewUnder(int i3, int i4) {
        return isViewUnder(this.mCapturedView, i3, i4);
    }

    public boolean isEdgeTouched(int i3) {
        int length = this.mInitialEdgesTouched.length;
        for (int i4 = 0; i4 < length; i4++) {
            if (isEdgeTouched(i3, i4)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPointerDown(int i3) {
        return (this.mPointersDown & (1 << i3)) != 0;
    }

    public boolean isViewUnder(View view, int i3, int i4) {
        return view != null && i3 >= view.getLeft() && i3 < view.getRight() && i4 >= view.getTop() && i4 < view.getBottom();
    }

    public void processTouchEvent(MotionEvent motionEvent) {
        int i3;
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int i4 = 0;
        if (actionMasked == 0) {
            float x2 = motionEvent.getX();
            float y2 = motionEvent.getY();
            int pointerId = motionEvent.getPointerId(0);
            View viewFindTopChildUnder = findTopChildUnder((int) x2, (int) y2);
            saveInitialMotion(x2, y2, pointerId);
            tryCaptureViewForDrag(viewFindTopChildUnder, pointerId);
            int i5 = this.mInitialEdgesTouched[pointerId];
            int i6 = this.mTrackingEdges;
            if ((i5 & i6) != 0) {
                this.mCallback.onEdgeTouched(i5 & i6, pointerId);
                return;
            }
            return;
        }
        if (actionMasked == 1) {
            if (this.mDragState == 1) {
                releaseViewForPointerUp();
            }
            cancel();
            return;
        }
        if (actionMasked == 2) {
            if (this.mDragState == 1) {
                if (isValidPointerForActionMove(this.mActivePointerId)) {
                    int iFindPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    float x3 = motionEvent.getX(iFindPointerIndex);
                    float y3 = motionEvent.getY(iFindPointerIndex);
                    float[] fArr = this.mLastMotionX;
                    int i7 = this.mActivePointerId;
                    int i8 = (int) (x3 - fArr[i7]);
                    int i9 = (int) (y3 - this.mLastMotionY[i7]);
                    dragTo(this.mCapturedView.getLeft() + i8, this.mCapturedView.getTop() + i9, i8, i9);
                    saveLastMotion(motionEvent);
                    return;
                }
                return;
            }
            int pointerCount = motionEvent.getPointerCount();
            while (i4 < pointerCount) {
                int pointerId2 = motionEvent.getPointerId(i4);
                if (isValidPointerForActionMove(pointerId2)) {
                    float x4 = motionEvent.getX(i4);
                    float y4 = motionEvent.getY(i4);
                    float f3 = x4 - this.mInitialMotionX[pointerId2];
                    float f4 = y4 - this.mInitialMotionY[pointerId2];
                    reportNewEdgeDrags(f3, f4, pointerId2);
                    if (this.mDragState != 1) {
                        View viewFindTopChildUnder2 = findTopChildUnder((int) x4, (int) y4);
                        if (checkTouchSlop(viewFindTopChildUnder2, f3, f4) && tryCaptureViewForDrag(viewFindTopChildUnder2, pointerId2)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                i4++;
            }
            saveLastMotion(motionEvent);
            return;
        }
        if (actionMasked == 3) {
            if (this.mDragState == 1) {
                dispatchViewReleased(0.0f, 0.0f);
            }
            cancel();
            return;
        }
        if (actionMasked == 5) {
            int pointerId3 = motionEvent.getPointerId(actionIndex);
            float x5 = motionEvent.getX(actionIndex);
            float y5 = motionEvent.getY(actionIndex);
            saveInitialMotion(x5, y5, pointerId3);
            if (this.mDragState != 0) {
                if (isCapturedViewUnder((int) x5, (int) y5)) {
                    tryCaptureViewForDrag(this.mCapturedView, pointerId3);
                    return;
                }
                return;
            } else {
                tryCaptureViewForDrag(findTopChildUnder((int) x5, (int) y5), pointerId3);
                int i10 = this.mInitialEdgesTouched[pointerId3];
                int i11 = this.mTrackingEdges;
                if ((i10 & i11) != 0) {
                    this.mCallback.onEdgeTouched(i10 & i11, pointerId3);
                    return;
                }
                return;
            }
        }
        if (actionMasked != 6) {
            return;
        }
        int pointerId4 = motionEvent.getPointerId(actionIndex);
        if (this.mDragState == 1 && pointerId4 == this.mActivePointerId) {
            int pointerCount2 = motionEvent.getPointerCount();
            while (true) {
                if (i4 >= pointerCount2) {
                    i3 = -1;
                    break;
                }
                int pointerId5 = motionEvent.getPointerId(i4);
                if (pointerId5 != this.mActivePointerId) {
                    View viewFindTopChildUnder3 = findTopChildUnder((int) motionEvent.getX(i4), (int) motionEvent.getY(i4));
                    View view = this.mCapturedView;
                    if (viewFindTopChildUnder3 == view && tryCaptureViewForDrag(view, pointerId5)) {
                        i3 = this.mActivePointerId;
                        break;
                    }
                }
                i4++;
            }
            if (i3 == -1) {
                releaseViewForPointerUp();
            }
        }
        clearMotionHistory(pointerId4);
    }

    public void setCapturedView(View view) {
        this.mCapturedView = view;
    }

    public void setDragState(int i3) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState != i3) {
            this.mDragState = i3;
            this.mCallback.onViewDragStateChanged(i3);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
            }
        }
    }

    public void setEdgeSize(int i3) {
        this.mEdgeSize = i3;
    }

    public void setEdgeTrackingEnabled(int i3) {
        this.mTrackingEdges = i3;
    }

    public void setMinVelocity(float f3) {
        this.mMinVelocity = f3;
    }

    public boolean settleCapturedViewAt(int i3, int i4) {
        if (this.mReleaseInProgress) {
            return forceSettleCapturedViewAt(i3, i4, (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    /* JADX WARN: Code duplicated, block: B:54:0x00e9  */
    /* JADX WARN: Code duplicated, block: B:63:0x0101  */
    public boolean shouldInterceptTouchEvent(MotionEvent motionEvent) {
        View viewFindTopChildUnder;
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        if (actionMasked == 0) {
            float x2 = motionEvent.getX();
            float y2 = motionEvent.getY();
            int pointerId = motionEvent.getPointerId(0);
            saveInitialMotion(x2, y2, pointerId);
            View viewFindTopChildUnder2 = findTopChildUnder((int) x2, (int) y2);
            if (viewFindTopChildUnder2 == this.mCapturedView && this.mDragState == 2) {
                tryCaptureViewForDrag(viewFindTopChildUnder2, pointerId);
            }
            int i3 = this.mInitialEdgesTouched[pointerId];
            int i4 = this.mTrackingEdges;
            if ((i3 & i4) != 0) {
                this.mCallback.onEdgeTouched(i3 & i4, pointerId);
            }
        } else if (actionMasked == 1) {
            cancel();
        } else if (actionMasked != 2) {
            if (actionMasked == 3) {
                cancel();
            } else if (actionMasked == 5) {
                int pointerId2 = motionEvent.getPointerId(actionIndex);
                float x3 = motionEvent.getX(actionIndex);
                float y3 = motionEvent.getY(actionIndex);
                saveInitialMotion(x3, y3, pointerId2);
                int i5 = this.mDragState;
                if (i5 == 0) {
                    int i6 = this.mInitialEdgesTouched[pointerId2];
                    int i7 = this.mTrackingEdges;
                    if ((i6 & i7) != 0) {
                        this.mCallback.onEdgeTouched(i6 & i7, pointerId2);
                    }
                } else if (i5 == 2 && (viewFindTopChildUnder = findTopChildUnder((int) x3, (int) y3)) == this.mCapturedView) {
                    tryCaptureViewForDrag(viewFindTopChildUnder, pointerId2);
                }
            } else if (actionMasked == 6) {
                clearMotionHistory(motionEvent.getPointerId(actionIndex));
            }
        } else if (this.mInitialMotionX != null && this.mInitialMotionY != null) {
            int pointerCount = motionEvent.getPointerCount();
            for (int i8 = 0; i8 < pointerCount; i8++) {
                int pointerId3 = motionEvent.getPointerId(i8);
                if (isValidPointerForActionMove(pointerId3)) {
                    float x4 = motionEvent.getX(i8);
                    float y4 = motionEvent.getY(i8);
                    float f3 = x4 - this.mInitialMotionX[pointerId3];
                    float f4 = y4 - this.mInitialMotionY[pointerId3];
                    View viewFindTopChildUnder3 = findTopChildUnder((int) x4, (int) y4);
                    boolean z2 = viewFindTopChildUnder3 != null && checkTouchSlop(viewFindTopChildUnder3, f3, f4);
                    if (!z2) {
                        reportNewEdgeDrags(f3, f4, pointerId3);
                        if (this.mDragState != 1) {
                            break;
                        }
                    } else {
                        int left = viewFindTopChildUnder3.getLeft();
                        int i9 = (int) f3;
                        int iClampViewPositionHorizontal = this.mCallback.clampViewPositionHorizontal(viewFindTopChildUnder3, left + i9, i9);
                        int top = viewFindTopChildUnder3.getTop();
                        int i10 = (int) f4;
                        int iClampViewPositionVertical = this.mCallback.clampViewPositionVertical(viewFindTopChildUnder3, top + i10, i10);
                        int viewHorizontalDragRange = this.mCallback.getViewHorizontalDragRange(viewFindTopChildUnder3);
                        int viewVerticalDragRange = this.mCallback.getViewVerticalDragRange(viewFindTopChildUnder3);
                        if ((viewHorizontalDragRange == 0 || (viewHorizontalDragRange > 0 && iClampViewPositionHorizontal == left)) && (viewVerticalDragRange == 0 || (viewVerticalDragRange > 0 && iClampViewPositionVertical == top))) {
                            break;
                        }
                        reportNewEdgeDrags(f3, f4, pointerId3);
                        if (this.mDragState != 1 || (z2 && tryCaptureViewForDrag(viewFindTopChildUnder3, pointerId3))) {
                            break;
                        }
                    }
                }
            }
            saveLastMotion(motionEvent);
        }
        return this.mDragState == 1;
    }

    public boolean smoothSlideViewTo(View view, int i3, int i4) {
        this.mCapturedView = view;
        this.mActivePointerId = -1;
        boolean zForceSettleCapturedViewAt = forceSettleCapturedViewAt(i3, i4, 0, 0);
        if (!zForceSettleCapturedViewAt && this.mDragState == 0 && this.mCapturedView != null) {
            this.mCapturedView = null;
        }
        return zForceSettleCapturedViewAt;
    }

    public boolean tryCaptureViewForDrag(View view, int i3) {
        if (view == this.mCapturedView && this.mActivePointerId == i3) {
            return true;
        }
        if (view == null || !this.mCallback.tryCaptureView(view, i3)) {
            return false;
        }
        this.mActivePointerId = i3;
        captureChildView(view, i3);
        return true;
    }

    private float clampMag(float f3, float f4, float f5) {
        float fAbs = Math.abs(f3);
        if (fAbs < f4) {
            return 0.0f;
        }
        if (fAbs > f5) {
            return f3 > 0.0f ? f5 : -f5;
        }
        return f3;
    }

    public static COUIViewDragHelper create(ViewGroup viewGroup, float f3, Callback callback) {
        COUIViewDragHelper cOUIViewDragHelperCreate = create(viewGroup, callback);
        cOUIViewDragHelperCreate.mTouchSlop = (int) (cOUIViewDragHelperCreate.mTouchSlop * (1.0f / f3));
        return cOUIViewDragHelperCreate;
    }

    public boolean isEdgeTouched(int i3, int i4) {
        return isPointerDown(i4) && (this.mInitialEdgesTouched[i4] & i3) != 0;
    }

    public boolean checkTouchSlop(int i3) {
        int length = this.mInitialMotionX.length;
        for (int i4 = 0; i4 < length; i4++) {
            if (checkTouchSlop(i3, i4)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTouchSlop(int i3, int i4) {
        if (!isPointerDown(i4)) {
            return false;
        }
        boolean z2 = (i3 & 1) == 1;
        boolean z3 = (i3 & 2) == 2;
        float f3 = this.mLastMotionX[i4] - this.mInitialMotionX[i4];
        float f4 = this.mLastMotionY[i4] - this.mInitialMotionY[i4];
        if (z2 && z3) {
            float f5 = (f3 * f3) + (f4 * f4);
            int i5 = this.mTouchSlop;
            return f5 > ((float) (i5 * i5));
        }
        if (z2) {
            return Math.abs(f3) > ((float) this.mTouchSlop);
        }
        return z3 && Math.abs(f4) > ((float) this.mTouchSlop);
    }

    private void clearMotionHistory(int i3) {
        if (this.mInitialMotionX == null || !isPointerDown(i3)) {
            return;
        }
        this.mInitialMotionX[i3] = 0.0f;
        this.mInitialMotionY[i3] = 0.0f;
        this.mLastMotionX[i3] = 0.0f;
        this.mLastMotionY[i3] = 0.0f;
        this.mInitialEdgesTouched[i3] = 0;
        this.mEdgeDragsInProgress[i3] = 0;
        this.mEdgeDragsLocked[i3] = 0;
        this.mPointersDown = (~(1 << i3)) & this.mPointersDown;
    }
}