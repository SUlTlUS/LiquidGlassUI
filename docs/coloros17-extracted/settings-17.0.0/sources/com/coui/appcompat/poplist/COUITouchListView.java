package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.AccessibilityUtils.COUIAccessibilityUtil;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.list.COUIForegroundListView;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.scrollbar.COUIScrollBar;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.support.poplist.R;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
@Deprecated
public class COUITouchListView extends COUIForegroundListView implements COUIScrollBar.COUIScrollable {
    public static final int ACTION_IS_FROM_TOUCH_LISTVIEW = -1;
    private static final int CAN_SCROLL_DOWN = 1;
    private static final int CAN_SCROLL_UP = -1;
    private static final boolean COUI_DEBUG;
    private static final String TAG = "COUITouchListView";
    private boolean mAllowDispatchEvent;
    private boolean mAllowScroll;
    private COUIScrollBar mCOUIScrollBar;
    private Rect mChildRectTemp;
    private DividerAnimationController mDividerAnimationController;
    private View mDownView;
    private int mDownY;
    private boolean mInTalkbackMode;
    private boolean mIsDynamicSelection;
    private boolean mIsNeedVibrate;
    private List<Integer> mItemHeightMap;
    private int mLastMotion;
    private int mLastTouchTarget;
    private Rect mParentRectTemp;
    private int mScrollY;
    private int mScrollbarVerticalPadding;
    private int mTotalHeight;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUITouchListView$1, reason: invalid class name */
    public class AnonymousClass1 implements AbsListView.OnScrollListener {
        public AnonymousClass1() {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i3, int i4, int i5) {
            if (COUITouchListView.c(COUITouchListView.this) != null) {
                int iIntValue = ((Integer) COUITouchListView.c(COUITouchListView.this).get(i3)).intValue();
                View childAt = absListView.getChildAt(0);
                if (childAt != null) {
                    iIntValue = (iIntValue - childAt.getHeight()) - childAt.getTop();
                }
                COUITouchListView.e(COUITouchListView.this, iIntValue);
            }
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int i3) {
            DividerAnimationController.d(COUITouchListView.a(COUITouchListView.this), i3);
        }
    }

    public class DividerAnimationController {
        private static final float DIVIDER_ANIMATION_RATIO = 0.0f;
        private static final float DIVIDER_ANIMATION_RESPONSE = 0.25f;
        private static final float DIVIDER_DEFAULT_ALPHA = 1.0f;
        private static final int MOVE_FLAG_DOWN = 2;
        private static final int MOVE_FLAG_OUT = 0;
        private static final int MOVE_FLAG_UP = -2;
        private Map<View, COUISpringAnimation> mDividerAnimations;
        private boolean mForceStopDividerAnimation;
        private boolean mNoNeedDoDividerAnimation;
        private int mScrollState;

        /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUITouchListView$DividerAnimationController$1, reason: invalid class name */
        public class AnonymousClass1 implements COUIDynamicAnimation.OnAnimationUpdateListener {
            final /* synthetic */ View val$dividerView;

            public AnonymousClass1(View view) {
                this.val$dividerView = view;
            }

            @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
            public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
                this.val$dividerView.setAlpha(f3);
            }
        }

        public /* synthetic */ DividerAnimationController(COUITouchListView cOUITouchListView, t tVar) {
            this();
        }

        public static /* bridge */ /* synthetic */ void a(DividerAnimationController dividerAnimationController, boolean z2) {
            dividerAnimationController.mForceStopDividerAnimation = z2;
        }

        public static /* bridge */ /* synthetic */ void b(DividerAnimationController dividerAnimationController, boolean z2, int i3) {
            dividerAnimationController.dividerAnim(z2, i3);
        }

        public static /* bridge */ /* synthetic */ void c(DividerAnimationController dividerAnimationController) {
            dividerAnimationController.dividerAnimOut();
        }

        public static /* bridge */ /* synthetic */ void d(DividerAnimationController dividerAnimationController, int i3) {
            dividerAnimationController.onScrollStateChanged(i3);
        }

        private void dividerAnim(boolean z2, int i3) {
            if (COUITouchListView.d(COUITouchListView.this) == -1) {
                return;
            }
            COUITouchListView cOUITouchListView = COUITouchListView.this;
            if (COUITouchListView.f(COUITouchListView.this, cOUITouchListView.getChildAt(COUITouchListView.d(cOUITouchListView) - COUITouchListView.this.getFirstVisiblePosition()))) {
                float f3 = z2 ? 1.0f : 0.0f;
                if (COUITouchListView.d(COUITouchListView.this) > 0 && (!z2 || i3 != -2)) {
                    startDividerAnimation((COUITouchListView.d(COUITouchListView.this) - 1) - COUITouchListView.this.getFirstVisiblePosition(), f3);
                }
                if (COUITouchListView.d(COUITouchListView.this) < COUITouchListView.this.getCount() - 1) {
                    if (z2 && i3 == 2) {
                        return;
                    }
                    startDividerAnimation((COUITouchListView.d(COUITouchListView.this) + 1) - COUITouchListView.this.getFirstVisiblePosition(), f3);
                }
            }
        }

        private void dividerAnimOut() {
            dividerAnim(false, 0);
        }

        public static /* bridge */ /* synthetic */ void e(DividerAnimationController dividerAnimationController) {
            dividerAnimationController.release();
        }

        private COUISpringAnimation getOrCreateDividerAnimation(View view) {
            COUISpringAnimation cOUISpringAnimation = this.mDividerAnimations.get(view);
            if (cOUISpringAnimation != null) {
                return cOUISpringAnimation;
            }
            COUISpringAnimation cOUISpringAnimation2 = new COUISpringAnimation(new FloatValueHolder());
            COUISpringForce cOUISpringForce = new COUISpringForce();
            cOUISpringForce.setBounce(0.0f);
            cOUISpringForce.setResponse(0.25f);
            cOUISpringAnimation2.setSpring(cOUISpringForce);
            cOUISpringAnimation2.setMinimumVisibleChange(0.002f);
            cOUISpringAnimation2.addUpdateListener(new AnonymousClass1(view));
            cOUISpringAnimation2.setStartValue(1.0f);
            this.mDividerAnimations.put(view, cOUISpringAnimation2);
            return cOUISpringAnimation2;
        }

        private void onScrollStateChanged(int i3) {
            if (this.mScrollState != i3) {
                if ((i3 == 1 || i3 == 2) && !COUITouchListView.b(COUITouchListView.this)) {
                    resetDividerAnimation();
                    this.mNoNeedDoDividerAnimation = true;
                } else {
                    this.mNoNeedDoDividerAnimation = false;
                }
                this.mScrollState = i3;
            }
        }

        private void release() {
            Iterator<Map.Entry<View, COUISpringAnimation>> it = this.mDividerAnimations.entrySet().iterator();
            while (it.hasNext()) {
                it.next().getValue().cancel();
                it.remove();
            }
        }

        private void resetDividerAnimation() {
            for (Map.Entry<View, COUISpringAnimation> entry : this.mDividerAnimations.entrySet()) {
                COUISpringAnimation value = entry.getValue();
                if (value.isRunning()) {
                    value.cancelComplete();
                    entry.getKey().setAlpha(1.0f);
                }
            }
        }

        private void startDividerAnimation(int i3, float f3) {
            View childAt = COUITouchListView.this.getChildAt(i3);
            if (childAt == null || this.mNoNeedDoDividerAnimation || this.mForceStopDividerAnimation || childAt.getAlpha() == f3) {
                return;
            }
            int firstVisiblePosition = COUITouchListView.this.getFirstVisiblePosition() + i3;
            if (firstVisiblePosition >= 0 && firstVisiblePosition < COUITouchListView.this.getAdapter().getCount()) {
                if (COUITouchListView.this.getAdapter().getItemViewType(firstVisiblePosition) == 2) {
                    return;
                }
                getOrCreateDividerAnimation(childAt).animateToFinalPosition(f3);
                return;
            }
            Log.e(COUITouchListView.TAG, "startDividerAnimation adapterPosition in error range！,getAdapter().getCount():" + COUITouchListView.this.getAdapter().getCount() + ",position:" + i3 + ",getFirstVisiblePosition():" + COUITouchListView.this.getFirstVisiblePosition());
        }

        private DividerAnimationController() {
            this.mScrollState = 0;
            this.mDividerAnimations = new HashMap();
            this.mNoNeedDoDividerAnimation = false;
            this.mForceStopDividerAnimation = false;
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUITouchListView(Context context) {
        this(context, null);
    }

    public static /* bridge */ /* synthetic */ DividerAnimationController a(COUITouchListView cOUITouchListView) {
        return cOUITouchListView.mDividerAnimationController;
    }

    public static /* bridge */ /* synthetic */ boolean b(COUITouchListView cOUITouchListView) {
        return cOUITouchListView.mIsDynamicSelection;
    }

    public static /* bridge */ /* synthetic */ List c(COUITouchListView cOUITouchListView) {
        return cOUITouchListView.mItemHeightMap;
    }

    private boolean canSelect(View view) {
        return view != null && (view.getBackground() instanceof COUIMaskEffectDrawable) && view.isEnabled();
    }

    private boolean cancelLastItemSelect(MotionEvent motionEvent, int i3) {
        View childAt = getChildAt(this.mLastTouchTarget - getFirstVisiblePosition());
        if (canSelect(childAt)) {
            dispatchTargetEvent(childAt, motionEvent, 3);
            ((COUIMaskEffectDrawable) childAt.getBackground()).setTouchSelectExited();
            DividerAnimationController.b(this.mDividerAnimationController, true, i3);
        }
        this.mLastTouchTarget = -1;
        return true;
    }

    private void createCOUIScrollDelegate() {
        this.mCOUIScrollBar = new COUIScrollBar.Builder(this).marginTop(this.mScrollbarVerticalPadding).marginBottom(this.mScrollbarVerticalPadding).build();
    }

    public static /* bridge */ /* synthetic */ int d(COUITouchListView cOUITouchListView) {
        return cOUITouchListView.mLastTouchTarget;
    }

    private void dispatchTargetEvent(View view, MotionEvent motionEvent, int i3) {
        this.mChildRectTemp = new Rect();
        this.mParentRectTemp = new Rect();
        getChildVisibleRect(view, this.mChildRectTemp, null);
        getLocalVisibleRect(this.mParentRectTemp);
        Rect rect = this.mChildRectTemp;
        int i4 = rect.left;
        Rect rect2 = this.mParentRectTemp;
        int i5 = i4 - rect2.left;
        int i6 = rect.top - rect2.top;
        int x2 = (int) motionEvent.getX();
        int y2 = (int) motionEvent.getY();
        MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
        motionEventObtain.setSource(-1);
        motionEventObtain.setLocation(x2 - i5, y2 - i6);
        motionEventObtain.setAction(i3);
        view.dispatchTouchEvent(motionEventObtain);
    }

    public static /* bridge */ /* synthetic */ void e(COUITouchListView cOUITouchListView, int i3) {
        cOUITouchListView.mScrollY = i3;
    }

    public static /* bridge */ /* synthetic */ boolean f(COUITouchListView cOUITouchListView, View view) {
        return cOUITouchListView.canSelect(view);
    }

    private void performHapticFeedback() {
        if (this.mIsNeedVibrate) {
            performHapticFeedback(302);
        }
    }

    public void allowDispatchEvent(boolean z2) {
        this.mAllowDispatchEvent = z2;
    }

    public void allowScroll(boolean z2) {
        this.mAllowScroll = z2;
    }

    @Override // android.view.View
    public boolean awakenScrollBars() {
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        return cOUIScrollBar != null ? cOUIScrollBar.awakenScrollBars(2000L) : super.awakenScrollBars();
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.dispatchDrawOver(canvas);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (this.mAllowDispatchEvent) {
            return super.dispatchHoverEvent(motionEvent);
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        View childAt;
        if (!this.mAllowDispatchEvent) {
            return false;
        }
        if (!this.mAllowScroll && motionEvent.getActionMasked() == 2) {
            return true;
        }
        if (canScrollVertically(1) || canScrollVertically(-1)) {
            this.mIsDynamicSelection = false;
        } else {
            this.mIsDynamicSelection = true;
        }
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        int x2 = (int) motionEvent.getX();
        int y2 = (int) motionEvent.getY();
        if (COUI_DEBUG) {
            Log.d(TAG, "dispatchTouchEvent actionMasked:" + MotionEvent.actionToString(actionMasked) + ",actionIndex:" + actionIndex + ",getPointerCount:" + motionEvent.getPointerCount());
        }
        if (actionMasked == 0) {
            this.mDownY = y2;
            this.mInTalkbackMode = COUIAccessibilityUtil.isTalkbackEnabled(getContext());
            int iPointToPosition = pointToPosition(x2, y2);
            this.mLastTouchTarget = iPointToPosition;
            View childAt2 = getChildAt(iPointToPosition - getFirstVisiblePosition());
            this.mDownView = childAt2;
            if (canSelect(childAt2)) {
                DividerAnimationController.c(this.mDividerAnimationController);
                ((COUIMaskEffectDrawable) this.mDownView.getBackground()).setTouchEntered();
            }
        } else if (actionMasked == 1) {
            int i3 = this.mLastTouchTarget;
            if ((i3 != -1 && !this.mInTalkbackMode) || this.mLastMotion == 0) {
                View childAt3 = getChildAt(i3 - getFirstVisiblePosition());
                if (childAt3 != null) {
                    COUILog.d(TAG, "target = " + childAt3 + " lastTouchTarget = " + this.mLastTouchTarget + " item id at position = " + this.mLastTouchTarget);
                    int i4 = this.mLastTouchTarget;
                    performItemClick(childAt3, i4, getItemIdAtPosition(i4));
                    dispatchTargetEvent(childAt3, motionEvent, 1);
                }
                DividerAnimationController.b(this.mDividerAnimationController, true, 0);
                this.mLastTouchTarget = -1;
                this.mLastMotion = actionMasked;
                return false;
            }
            DividerAnimationController.b(this.mDividerAnimationController, true, 0);
            this.mLastTouchTarget = -1;
        } else if (actionMasked == 2) {
            if (this.mLastTouchTarget != -1 && !this.mIsDynamicSelection && Math.abs(y2 - this.mDownY) > ViewConfiguration.get(getContext()).getScaledTouchSlop() && canSelect(this.mDownView)) {
                ((COUIMaskEffectDrawable) this.mDownView.getBackground()).setTouchExited();
                DividerAnimationController.b(this.mDividerAnimationController, true, 0);
                this.mLastTouchTarget = -1;
            }
            int iPointToPosition2 = pointToPosition(x2, y2);
            if (iPointToPosition2 == -1 || motionEvent.getPointerCount() > 1 || this.mInTalkbackMode) {
                this.mLastMotion = actionMasked;
                return cancelLastItemSelect(motionEvent, 0);
            }
            if (iPointToPosition2 != this.mLastTouchTarget && DefaultAdapter.isDataIndex(iPointToPosition2) && this.mIsDynamicSelection && (childAt = getChildAt(iPointToPosition2 - getFirstVisiblePosition())) != null) {
                cancelLastItemSelect(motionEvent, canSelect(childAt) ? iPointToPosition2 - this.mLastTouchTarget : 0);
                if (canSelect(childAt)) {
                    dispatchTargetEvent(childAt, motionEvent, 0);
                    ((COUIMaskEffectDrawable) childAt.getBackground()).setTouchSelectEntered();
                    performHapticFeedback();
                    this.mLastTouchTarget = iPointToPosition2;
                    DividerAnimationController.c(this.mDividerAnimationController);
                }
            }
        } else if (actionMasked == 3) {
            cancelLastItemSelect(motionEvent, 0);
        } else if (actionMasked == 5) {
            this.mLastMotion = actionMasked;
            return cancelLastItemSelect(motionEvent, 0);
        }
        this.mLastMotion = actionMasked;
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // com.coui.appcompat.scrollbar.COUIScrollBar.COUIScrollable
    public COUIScrollBar getCOUIScrollDelegate() {
        return this.mCOUIScrollBar;
    }

    @Override // com.coui.appcompat.scrollbar.COUIScrollBar.COUIScrollable
    public View getCOUIScrollableView() {
        return this;
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.onAttachedToWindow();
        } else {
            createCOUIScrollDelegate();
        }
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.release();
            this.mCOUIScrollBar = null;
        }
        DividerAnimationController.e(this.mDividerAnimationController);
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null && cOUIScrollBar.onInterceptTouchEvent(motionEvent)) {
            return true;
        }
        if (this.mIsDynamicSelection) {
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar == null || !cOUIScrollBar.onTouchEvent(motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.view.View
    public void onVisibilityChanged(View view, int i3) {
        super.onVisibilityChanged(view, i3);
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.onVisibilityChanged(view, i3);
        }
    }

    @Override // android.view.View
    public void onWindowVisibilityChanged(int i3) {
        super.onWindowVisibilityChanged(i3);
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.onWindowVisibilityChanged(i3);
        }
    }

    public void setForceStopDividerAnimation(boolean z2) {
        DividerAnimationController.a(this.mDividerAnimationController, z2);
    }

    public void setIsNeedVibrate(boolean z2) {
        this.mIsNeedVibrate = z2;
    }

    public void setItemHeightMap(List<Integer> list, int i3) {
        this.mItemHeightMap = list;
        this.mTotalHeight = i3;
    }

    @Override // com.coui.appcompat.scrollbar.COUIScrollBar.COUIScrollable
    public void setNewCOUIScrollDelegate(COUIScrollBar cOUIScrollBar) {
        if (cOUIScrollBar == null) {
            throw new IllegalArgumentException("setNewFastScrollDelegate must NOT be NULL.");
        }
        this.mCOUIScrollBar = cOUIScrollBar;
        cOUIScrollBar.onAttachedToWindow();
    }

    @Override // com.coui.appcompat.scrollbar.COUIScrollBar.COUIScrollable
    public int superComputeVerticalScrollExtent() {
        return getHeight();
    }

    @Override // com.coui.appcompat.scrollbar.COUIScrollBar.COUIScrollable
    public int superComputeVerticalScrollOffset() {
        return this.mScrollY;
    }

    @Override // com.coui.appcompat.scrollbar.COUIScrollBar.COUIScrollable
    public int superComputeVerticalScrollRange() {
        return this.mTotalHeight;
    }

    @Override // com.coui.appcompat.scrollbar.COUIScrollBar.COUIScrollable
    public void superOnTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
    }

    public COUITouchListView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUITouchListView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUITouchListView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mAllowDispatchEvent = true;
        this.mAllowScroll = true;
        this.mIsNeedVibrate = true;
        this.mIsDynamicSelection = true;
        this.mTotalHeight = 0;
        this.mScrollY = 0;
        this.mLastMotion = -1;
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_fade_edge_length));
        this.mScrollbarVerticalPadding = context.getResources().getDimensionPixelOffset(R.dimen.coui_popup_list_window_scrollbar_vertical_padding);
        this.mDividerAnimationController = new DividerAnimationController(this, null);
        setOnScrollListener(new AnonymousClass1());
        createCOUIScrollDelegate();
        setDefaultFocusHighlightEnabled(false);
    }
}