package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.log.COUILog;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPopupMenuRootView extends FrameLayout {
    private static final float CANCEL_EVENT_CENTER_FACTOR = 0.5f;
    private static final boolean DEBUG_DRAW;
    private static final float DEBUG_OVERLAY_MENU_ALPHA = 0.2f;
    private static final long MAIN_AREA_DISMISS_DELAY_MS = 500;
    private static final String TAG = "COUIPopupMenuRootView";
    BasePopupMenuAnimationController mController;
    private final Paint mDebugPaint;
    private BasePopupMenuAnimationController mDefaultScreenController;
    private boolean mDelayedMainAreaDismissEnabled;
    PopupMenuDomain mDomain;
    private final Rect mDrawingRect;
    private boolean mHasSentDownToSubMenu;
    private long mMainAreaDismissDelayMs;
    private final Runnable mMainAreaDismissRunnable;
    private boolean mMainAreaDismissRunnablePosted;
    private float mMainAreaDownX;
    private float mMainAreaDownY;
    private int mMainAreaSlideTouchSlop;
    private boolean mMainAreaSlideTracking;
    int mMainMenuHeight;
    ViewGroup mMainMenuRootView;
    int mMainMenuWidth;
    private OnMenuStateChangedListener mMenuStateChangedListener;
    private boolean mNeedReDispatchDownForNextEvent;
    private final BasePopupMenuAnimationController.OnMenuStateChangedListener mOnSubMenuStateChangedListener;
    private Runnable mPendingDismissRunnable;
    private boolean mPendingMainMenuTouchEventRestore;
    private RegionBasedLongPressDetector mRegionBasedLongPressDetector;
    private Runnable mShowSubMenuAfterAnimationSkipped;
    private BasePopupMenuAnimationController mSmallScreenController;
    private boolean mSubMenuEntered;
    private View.OnClickListener mSubMenuHeaderClickListener;
    int mSubMenuHeight;
    ViewGroup mSubMenuRootView;
    private final float[] mSubMenuTransformCoords;
    int mSubMenuWidth;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUIPopupMenuRootView$1, reason: invalid class name */
    public class AnonymousClass1 implements BasePopupMenuAnimationController.OnMenuStateChangedListener {
        private final View.OnClickListener mCancelSubMenuEnterAndStartExit = new r(this);
        private final View.OnClickListener mCancelSubMenuExitAndStartEnter = new s(this);

        public AnonymousClass1() {
        }

        public static /* synthetic */ void a(AnonymousClass1 anonymousClass1, View view) {
            COUIPopupMenuRootView.this.hideSubMenu(true);
            COUIPopupMenuRootView.this.mMainMenuRootView.setOnClickListener(null);
        }

        public static /* synthetic */ void b(AnonymousClass1 anonymousClass1, View view) {
            COUIPopupMenuRootView.this.showSubMenu();
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onMainMenuAnimationCanceled() {
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onMainMenuAnimationCanceled();
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onMainMenuEntered() {
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onMainMenuEntered();
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onMainMenuExited() {
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onMainMenuExited();
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onMainMenuStartToEnter() {
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onMainMenuStartToEnter();
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onMainMenuStartToExit() {
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onMainMenuStartToExit();
            }
            ViewGroup viewGroup = COUIPopupMenuRootView.this.mMainMenuRootView;
            if (viewGroup instanceof RoundFrameLayout) {
                ((RoundFrameLayout) viewGroup).setSpotLightTouchEnabled(true);
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onSubMenuAnimationCanceled() {
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onSubMenuAnimationCanceled();
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onSubMenuEntered() {
            COUIPopupMenuRootView.j(COUIPopupMenuRootView.this, true);
            if (COUIPopupMenuRootView.d(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.d(COUIPopupMenuRootView.this).setSubMenuJustEntered(true);
            }
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onSubMenuEntered();
            }
            ViewGroup viewGroup = COUIPopupMenuRootView.this.mSubMenuRootView;
            if (viewGroup instanceof RoundFrameLayout) {
                ((RoundFrameLayout) viewGroup).clearOverrideOutline();
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onSubMenuExited() {
            COUIPopupMenuRootView cOUIPopupMenuRootView;
            ViewGroup viewGroup;
            if (COUIPopupMenuRootView.d(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.d(COUIPopupMenuRootView.this).setSubMenuJustEntered(false);
                COUIPopupMenuRootView.d(COUIPopupMenuRootView.this).clearLastTriggeredInSequence();
            }
            COUIPopupMenuRootView.g(COUIPopupMenuRootView.this, false);
            ViewGroup viewGroup2 = COUIPopupMenuRootView.this.mSubMenuRootView;
            if (viewGroup2 instanceof RoundFrameLayout) {
                ((RoundFrameLayout) viewGroup2).setAllowDispatchEvent(true);
            }
            ViewGroup viewGroup3 = COUIPopupMenuRootView.this.mMainMenuRootView;
            if (viewGroup3 instanceof RoundFrameLayout) {
                ((RoundFrameLayout) viewGroup3).setSpotLightTouchEnabled(true);
            }
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onSubMenuExited();
            }
            if (COUIPopupMenuRootView.c(COUIPopupMenuRootView.this) && (viewGroup = (cOUIPopupMenuRootView = COUIPopupMenuRootView.this).mMainMenuRootView) != null) {
                cOUIPopupMenuRootView.allowListViewDispatchTouchEvent(viewGroup, true);
                COUIPopupMenuRootView.h(COUIPopupMenuRootView.this, false);
            }
            COUIPopupMenuRootView cOUIPopupMenuRootView2 = COUIPopupMenuRootView.this;
            cOUIPopupMenuRootView2.allowListViewScroll(cOUIPopupMenuRootView2.mMainMenuRootView, true);
            COUIPopupMenuRootView.this.configSubMenuHeaderOnClick(null);
            COUIPopupMenuRootView.this.removeSubMenuView();
            if (COUIPopupMenuRootView.e(COUIPopupMenuRootView.this) != null) {
                Runnable runnableE = COUIPopupMenuRootView.e(COUIPopupMenuRootView.this);
                COUIPopupMenuRootView.i(COUIPopupMenuRootView.this, null);
                runnableE.run();
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onSubMenuStartToEnter() {
            COUIPopupMenuRootView.g(COUIPopupMenuRootView.this, false);
            COUIPopupMenuRootView.f(COUIPopupMenuRootView.this, false);
            COUIPopupMenuRootView.j(COUIPopupMenuRootView.this, false);
            ViewGroup viewGroup = COUIPopupMenuRootView.this.mSubMenuRootView;
            if (viewGroup instanceof RoundFrameLayout) {
                ((RoundFrameLayout) viewGroup).setAllowDispatchEvent(true);
            }
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onSubMenuStartToEnter();
            }
            ViewGroup viewGroup2 = COUIPopupMenuRootView.this.mMainMenuRootView;
            if (viewGroup2 != null) {
                if (viewGroup2 instanceof RoundFrameLayout) {
                    ((RoundFrameLayout) viewGroup2).clearSpotLightState();
                    ((RoundFrameLayout) COUIPopupMenuRootView.this.mMainMenuRootView).setSpotLightTouchEnabled(false);
                }
                COUIPopupMenuRootView cOUIPopupMenuRootView = COUIPopupMenuRootView.this;
                cOUIPopupMenuRootView.allowListViewDispatchTouchEvent(cOUIPopupMenuRootView.mMainMenuRootView, false);
                COUIPopupMenuRootView cOUIPopupMenuRootView2 = COUIPopupMenuRootView.this;
                cOUIPopupMenuRootView2.allowListViewScroll(cOUIPopupMenuRootView2.mMainMenuRootView, false);
                COUIPopupMenuRootView.k(COUIPopupMenuRootView.this);
                COUIPopupMenuRootView.this.mMainMenuRootView.setOnClickListener(this.mCancelSubMenuEnterAndStartExit);
                COUIPopupMenuRootView cOUIPopupMenuRootView3 = COUIPopupMenuRootView.this;
                cOUIPopupMenuRootView3.allowListViewScroll(cOUIPopupMenuRootView3.mSubMenuRootView, true);
                COUIPopupMenuRootView.this.configSubMenuHeaderOnClick(this.mCancelSubMenuEnterAndStartExit);
            }
        }

        @Override // com.coui.appcompat.poplist.BasePopupMenuAnimationController.OnMenuStateChangedListener
        public void onSubMenuStartToExit() {
            COUIPopupMenuRootView.g(COUIPopupMenuRootView.this, true);
            COUIPopupMenuRootView.f(COUIPopupMenuRootView.this, false);
            COUIPopupMenuRootView.j(COUIPopupMenuRootView.this, false);
            ViewGroup viewGroup = COUIPopupMenuRootView.this.mSubMenuRootView;
            if (viewGroup instanceof RoundFrameLayout) {
                ((RoundFrameLayout) viewGroup).setAllowDispatchEvent(false);
            }
            if (COUIPopupMenuRootView.b(COUIPopupMenuRootView.this) != null) {
                COUIPopupMenuRootView.b(COUIPopupMenuRootView.this).onSubMenuStartToExit();
            }
            ViewGroup viewGroup2 = COUIPopupMenuRootView.this.mMainMenuRootView;
            if (viewGroup2 != null) {
                viewGroup2.setFocusable(false);
                COUIPopupMenuRootView.this.mMainMenuRootView.setClickable(false);
                COUIPopupMenuRootView.this.mMainMenuRootView.setOnClickListener(null);
                if (!COUIPopupMenuRootView.c(COUIPopupMenuRootView.this)) {
                    COUIPopupMenuRootView cOUIPopupMenuRootView = COUIPopupMenuRootView.this;
                    cOUIPopupMenuRootView.allowListViewDispatchTouchEvent(cOUIPopupMenuRootView.mMainMenuRootView, true);
                }
                COUIPopupMenuRootView cOUIPopupMenuRootView2 = COUIPopupMenuRootView.this;
                cOUIPopupMenuRootView2.allowListViewScroll(cOUIPopupMenuRootView2.mSubMenuRootView, false);
                COUIPopupMenuRootView.this.configSubMenuHeaderOnClick(this.mCancelSubMenuExitAndStartEnter);
            }
            ViewGroup viewGroup3 = COUIPopupMenuRootView.this.mSubMenuRootView;
            if (viewGroup3 instanceof RoundFrameLayout) {
                ((RoundFrameLayout) viewGroup3).clearSpotLightState();
            }
        }
    }

    public interface OnMenuStateChangedListener {
        default void onMainMenuAnimationCanceled() {
        }

        default void onMainMenuEntered() {
        }

        default void onMainMenuExited() {
        }

        default void onMainMenuStartToEnter() {
        }

        default void onMainMenuStartToExit() {
        }

        default void onSubMenuAnimationCanceled() {
        }

        default void onSubMenuEntered() {
        }

        default void onSubMenuExited() {
        }

        default void onSubMenuStartToEnter() {
        }

        default void onSubMenuStartToExit() {
        }
    }

    public enum PopupMenuTouchDispatchResult {
        NOT_APPLICABLE,
        RETURN_TRUE,
        RETURN_FALSE;

        public static /* bridge */ /* synthetic */ PopupMenuTouchDispatchResult a(boolean z2) {
            return fromConsumed(z2);
        }

        private static PopupMenuTouchDispatchResult fromConsumed(boolean z2) {
            return z2 ? RETURN_TRUE : RETURN_FALSE;
        }
    }

    static {
        DEBUG_DRAW = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUIPopupMenuRootView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUIPopupMenuRootView cOUIPopupMenuRootView) {
        cOUIPopupMenuRootView.mMainAreaDismissRunnablePosted = false;
        if (cOUIPopupMenuRootView.mSubMenuEntered) {
            cOUIPopupMenuRootView.hideSubMenu(true);
        }
    }

    private void applyDebugMenuTranslucencyForInspect() {
        ViewGroup viewGroup = this.mMainMenuRootView;
        if (viewGroup != null) {
            viewGroup.setAlpha(0.2f);
        }
        ViewGroup viewGroup2 = this.mSubMenuRootView;
        if (viewGroup2 != null) {
            viewGroup2.setAlpha(0.2f);
        }
    }

    public static /* bridge */ /* synthetic */ OnMenuStateChangedListener b(COUIPopupMenuRootView cOUIPopupMenuRootView) {
        return cOUIPopupMenuRootView.mMenuStateChangedListener;
    }

    public static /* bridge */ /* synthetic */ boolean c(COUIPopupMenuRootView cOUIPopupMenuRootView) {
        return cOUIPopupMenuRootView.mPendingMainMenuTouchEventRestore;
    }

    private void cancelMainAreaDismissRunnable() {
        removeCallbacks(this.mMainAreaDismissRunnable);
        this.mMainAreaDismissRunnablePosted = false;
    }

    private void cancelSubMenuGestureOutsideBounds(MotionEvent motionEvent, float f3, float f4) {
        if (this.mHasSentDownToSubMenu) {
            float[] fArrTransformToSubMenuCoords = transformToSubMenuCoords(f3, f4);
            MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getEventTime(), 3, fArrTransformToSubMenuCoords[0], fArrTransformToSubMenuCoords[1], motionEvent.getMetaState());
            this.mSubMenuRootView.dispatchTouchEvent(motionEventObtain);
            motionEventObtain.recycle();
            this.mHasSentDownToSubMenu = false;
        }
    }

    private void clipCanvasOutRectCompat(Canvas canvas, Rect rect) {
        canvas.clipOutRect(rect);
    }

    public static /* bridge */ /* synthetic */ RegionBasedLongPressDetector d(COUIPopupMenuRootView cOUIPopupMenuRootView) {
        return cOUIPopupMenuRootView.mRegionBasedLongPressDetector;
    }

    private PopupMenuTouchDispatchResult dispatchAfterSubMenuExitIfNeeded(MotionEvent motionEvent) {
        if (!this.mNeedReDispatchDownForNextEvent) {
            return PopupMenuTouchDispatchResult.NOT_APPLICABLE;
        }
        this.mNeedReDispatchDownForNextEvent = false;
        MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
        motionEventObtain.setAction(3);
        super.dispatchTouchEvent(motionEventObtain);
        motionEventObtain.recycle();
        if (motionEvent.getActionMasked() == 0) {
            return PopupMenuTouchDispatchResult.a(super.dispatchTouchEvent(motionEvent));
        }
        MotionEvent motionEventObtain2 = MotionEvent.obtain(motionEvent);
        motionEventObtain2.setAction(0);
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEventObtain2);
        motionEventObtain2.recycle();
        return PopupMenuTouchDispatchResult.a(zDispatchTouchEvent);
    }

    private void drawDebugAnchorOutset(Canvas canvas) {
        this.mDebugPaint.setColor(Color.parseColor("#330000FF"));
        canvas.save();
        this.mDrawingRect.set(this.mDomain.mAnchor);
        clipCanvasOutRectCompat(canvas, this.mDrawingRect);
        this.mDomain.getAnchorRealRect(this.mDrawingRect);
        canvas.drawRect(this.mDrawingRect, this.mDebugPaint);
        canvas.restore();
    }

    private void drawDebugDomainRects(Canvas canvas) {
        this.mDebugPaint.setColor(Color.parseColor("#3300FF00"));
        this.mDrawingRect.set(this.mDomain.mAnchor);
        canvas.drawRect(this.mDrawingRect, this.mDebugPaint);
        this.mDebugPaint.setColor(Color.parseColor("#33FF00FF"));
        this.mDrawingRect.set(this.mDomain.mMainMenu);
        canvas.drawRect(this.mDrawingRect, this.mDebugPaint);
        this.mDebugPaint.setColor(Color.parseColor("#33FFFF00"));
        this.mDrawingRect.set(this.mDomain.mSubMenuAnchor);
        canvas.drawRect(this.mDrawingRect, this.mDebugPaint);
        this.mDebugPaint.setColor(Color.parseColor("#3300FFFF"));
        this.mDrawingRect.set(this.mDomain.mMainMenuRelocated);
        canvas.drawRect(this.mDrawingRect, this.mDebugPaint);
        this.mDebugPaint.setColor(Color.parseColor("#33000000"));
        this.mDrawingRect.set(this.mDomain.mSubMenu);
        canvas.drawRect(this.mDrawingRect, this.mDebugPaint);
    }

    private void drawDebugLayoutInspectOverlay(Canvas canvas) {
        if (this.mDomain == null) {
            return;
        }
        applyDebugMenuTranslucencyForInspect();
        drawDebugWindowBarrier(canvas);
        drawDebugAnchorOutset(canvas);
        drawDebugDomainRects(canvas);
    }

    private void drawDebugWindowBarrier(Canvas canvas) {
        this.mDebugPaint.setColor(Color.parseColor("#33FF0000"));
        canvas.save();
        this.mDomain.getAvailableRect(this.mDrawingRect);
        clipCanvasOutRectCompat(canvas, this.mDrawingRect);
        canvas.drawRect(this.mDomain.mWindow, this.mDebugPaint);
        canvas.restore();
    }

    public static /* bridge */ /* synthetic */ Runnable e(COUIPopupMenuRootView cOUIPopupMenuRootView) {
        return cOUIPopupMenuRootView.mShowSubMenuAfterAnimationSkipped;
    }

    public static /* bridge */ /* synthetic */ void f(COUIPopupMenuRootView cOUIPopupMenuRootView, boolean z2) {
        cOUIPopupMenuRootView.mHasSentDownToSubMenu = z2;
    }

    private PopupMenuTouchDispatchResult forwardTouchInsideSubMenuBounds(MotionEvent motionEvent, int i3, float f3, float f4) {
        if (!this.mHasSentDownToSubMenu) {
            float[] fArrTransformToSubMenuCoords = transformToSubMenuCoords(f3, f4);
            MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getEventTime(), 0, fArrTransformToSubMenuCoords[0], fArrTransformToSubMenuCoords[1], motionEvent.getMetaState());
            boolean zDispatchTouchEvent = this.mSubMenuRootView.dispatchTouchEvent(motionEventObtain);
            motionEventObtain.recycle();
            if (!zDispatchTouchEvent) {
                return PopupMenuTouchDispatchResult.NOT_APPLICABLE;
            }
            this.mHasSentDownToSubMenu = true;
        }
        MotionEvent motionEventTransformEventToSubMenu = transformEventToSubMenu(motionEvent);
        boolean zDispatchTouchEvent2 = this.mSubMenuRootView.dispatchTouchEvent(motionEventTransformEventToSubMenu);
        if (motionEventTransformEventToSubMenu != motionEvent) {
            motionEventTransformEventToSubMenu.recycle();
        }
        if (i3 == 1 || i3 == 3) {
            if (this.mHasSentDownToSubMenu && i3 == 3) {
                float[] fArrTransformToSubMenuCoords2 = transformToSubMenuCoords(motionEvent.getX(), motionEvent.getY());
                MotionEvent motionEventObtain2 = MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getEventTime(), 3, fArrTransformToSubMenuCoords2[0], fArrTransformToSubMenuCoords2[1], motionEvent.getMetaState());
                this.mSubMenuRootView.dispatchTouchEvent(motionEventObtain2);
                motionEventObtain2.recycle();
            }
            this.mHasSentDownToSubMenu = false;
        }
        return PopupMenuTouchDispatchResult.a(zDispatchTouchEvent2);
    }

    private PopupMenuTouchDispatchResult forwardTouchToSubMenuIfNeeded(MotionEvent motionEvent, int i3, boolean z2) {
        if (z2 || this.mNeedReDispatchDownForNextEvent || this.mSubMenuRootView == null || this.mDomain == null || !this.mSubMenuEntered) {
            return PopupMenuTouchDispatchResult.NOT_APPLICABLE;
        }
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        if (isPointInSubMenu(x2, y2)) {
            return forwardTouchInsideSubMenuBounds(motionEvent, i3, x2, y2);
        }
        cancelSubMenuGestureOutsideBounds(motionEvent, x2, y2);
        return PopupMenuTouchDispatchResult.NOT_APPLICABLE;
    }

    public static /* bridge */ /* synthetic */ void g(COUIPopupMenuRootView cOUIPopupMenuRootView, boolean z2) {
        cOUIPopupMenuRootView.mNeedReDispatchDownForNextEvent = z2;
    }

    public static /* bridge */ /* synthetic */ void h(COUIPopupMenuRootView cOUIPopupMenuRootView, boolean z2) {
        cOUIPopupMenuRootView.mPendingMainMenuTouchEventRestore = z2;
    }

    private void handleMainAreaDelayedDismissOnTouch(MotionEvent motionEvent, int i3) {
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        if (i3 == 0) {
            cancelMainAreaDismissRunnable();
            boolean zIsPointInMainMenuExclusiveForDelayedDismiss = isPointInMainMenuExclusiveForDelayedDismiss(x2, y2);
            this.mMainAreaSlideTracking = zIsPointInMainMenuExclusiveForDelayedDismiss;
            if (zIsPointInMainMenuExclusiveForDelayedDismiss) {
                this.mMainAreaDownX = x2;
                this.mMainAreaDownY = y2;
                return;
            }
            return;
        }
        if (i3 != 2) {
            if (i3 == 1 || i3 == 3) {
                cancelMainAreaDismissRunnable();
                this.mMainAreaSlideTracking = false;
                return;
            }
            return;
        }
        boolean zIsPointInMainMenuExclusiveForDelayedDismiss2 = isPointInMainMenuExclusiveForDelayedDismiss(x2, y2);
        if (this.mMainAreaSlideTracking) {
            if (!zIsPointInMainMenuExclusiveForDelayedDismiss2) {
                cancelMainAreaDismissRunnable();
                this.mMainAreaSlideTracking = false;
                return;
            } else {
                if (this.mMainAreaDismissRunnablePosted || !hasExceededMainAreaSlideTouchSlop(x2, y2)) {
                    return;
                }
                this.mMainAreaDismissRunnablePosted = true;
                postDelayed(this.mMainAreaDismissRunnable, this.mMainAreaDismissDelayMs);
                return;
            }
        }
        if (zIsPointInMainMenuExclusiveForDelayedDismiss2) {
            this.mMainAreaSlideTracking = true;
            this.mMainAreaDownX = x2;
            this.mMainAreaDownY = y2;
            if (this.mMainAreaDismissRunnablePosted) {
                return;
            }
            this.mMainAreaDismissRunnablePosted = true;
            postDelayed(this.mMainAreaDismissRunnable, this.mMainAreaDismissDelayMs);
        }
    }

    private boolean hasExceededMainAreaSlideTouchSlop(float f3, float f4) {
        float f5 = f3 - this.mMainAreaDownX;
        float f6 = f4 - this.mMainAreaDownY;
        float f7 = this.mMainAreaSlideTouchSlop;
        return (f5 * f5) + (f6 * f6) > f7 * f7;
    }

    public static /* bridge */ /* synthetic */ void i(COUIPopupMenuRootView cOUIPopupMenuRootView, Runnable runnable) {
        cOUIPopupMenuRootView.mShowSubMenuAfterAnimationSkipped = runnable;
    }

    private boolean isPointInMainMenu(float f3, float f4) {
        PopupMenuDomain popupMenuDomain;
        if (this.mMainMenuRootView != null && (popupMenuDomain = this.mDomain) != null) {
            Rect rect = popupMenuDomain.mMainMenu;
            if (f3 >= rect.left && f3 <= rect.right && f4 >= rect.top && f4 <= rect.bottom) {
                return true;
            }
        }
        return false;
    }

    private boolean isPointInMainMenuExclusiveForDelayedDismiss(float f3, float f4) {
        return isPointInMainMenu(f3, f4) && !isPointInSubMenu(f3, f4);
    }

    private boolean isPointInSubMenu(float f3, float f4) {
        PopupMenuDomain popupMenuDomain;
        if (this.mSubMenuRootView != null && (popupMenuDomain = this.mDomain) != null) {
            Rect rect = popupMenuDomain.mSubMenu;
            if (f3 >= rect.left && f3 <= rect.right && f4 >= rect.top && f4 <= rect.bottom) {
                return true;
            }
        }
        return false;
    }

    public static /* bridge */ /* synthetic */ void j(COUIPopupMenuRootView cOUIPopupMenuRootView, boolean z2) {
        cOUIPopupMenuRootView.mSubMenuEntered = z2;
    }

    public static /* bridge */ /* synthetic */ void k(COUIPopupMenuRootView cOUIPopupMenuRootView) {
        cOUIPopupMenuRootView.cancelMainAreaDismissRunnable();
    }

    private void restoreMainMenuTouchAfterLongPressOnUp(int i3) {
        ViewGroup viewGroup;
        if (this.mPendingMainMenuTouchEventRestore && i3 == 1 && (viewGroup = this.mMainMenuRootView) != null) {
            allowListViewDispatchTouchEvent(viewGroup, true);
            this.mPendingMainMenuTouchEventRestore = false;
        }
    }

    private MotionEvent transformEventToSubMenu(MotionEvent motionEvent) {
        PopupMenuDomain popupMenuDomain;
        MotionEvent motionEventObtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
        if (this.mSubMenuRootView != null && (popupMenuDomain = this.mDomain) != null) {
            Rect rect = popupMenuDomain.mSubMenu;
            motionEventObtainNoHistory.offsetLocation(-rect.left, -rect.top);
        }
        return motionEventObtainNoHistory;
    }

    private float[] transformToSubMenuCoords(float f3, float f4) {
        PopupMenuDomain popupMenuDomain;
        if (this.mSubMenuRootView == null || (popupMenuDomain = this.mDomain) == null) {
            float[] fArr = this.mSubMenuTransformCoords;
            fArr[0] = f3;
            fArr[1] = f4;
            return fArr;
        }
        float[] fArr2 = this.mSubMenuTransformCoords;
        Rect rect = popupMenuDomain.mSubMenu;
        fArr2[0] = f3 - rect.left;
        fArr2[1] = f4 - rect.top;
        return fArr2;
    }

    public void addMainMenuView(RoundFrameLayout roundFrameLayout) {
        ViewGroup viewGroup = this.mMainMenuRootView;
        if (viewGroup != null) {
            removeView(viewGroup);
        }
        if (this.mSubMenuRootView != null) {
            hideSubMenu(false);
        }
        this.mMainMenuRootView = roundFrameLayout;
        addView(roundFrameLayout, new ViewGroup.LayoutParams(-2, -2));
        allowListViewScroll(this.mMainMenuRootView, true);
        this.mController.setMainMenuView(roundFrameLayout);
        this.mController.setMenuRootView(this);
        this.mController.setOnSubMenuStateChangedListener(this.mOnSubMenuStateChangedListener);
        this.mController.setMainMenuSize(this.mMainMenuWidth, this.mMainMenuHeight);
    }

    public void addSubMenuView(RoundFrameLayout roundFrameLayout) {
        ViewGroup viewGroup = this.mSubMenuRootView;
        if (viewGroup != null) {
            removeView(viewGroup);
        }
        this.mSubMenuRootView = roundFrameLayout;
        roundFrameLayout.setTranslationZ(1.0f);
        addView(this.mSubMenuRootView, new ViewGroup.LayoutParams(-2, -2));
        allowListViewScroll(this.mSubMenuRootView, true);
        this.mController.setSubMenuView(roundFrameLayout);
        showSubMenu();
        this.mController.setSubMenuSize(this.mSubMenuWidth, this.mSubMenuHeight);
    }

    public void allowListViewDispatchTouchEvent(ViewGroup viewGroup, boolean z2) {
        if (viewGroup != null) {
            View childAt = viewGroup.getChildAt(0);
            if (childAt instanceof COUITouchListView) {
                ((COUITouchListView) childAt).allowDispatchEvent(z2);
            } else if (childAt instanceof COUITouchRecyclerView) {
                ((COUITouchRecyclerView) childAt).allowDispatchEvent(z2);
            }
        }
    }

    public void allowListViewScroll(ViewGroup viewGroup, boolean z2) {
        if (viewGroup != null) {
            View childAt = viewGroup.getChildAt(0);
            if (childAt instanceof COUITouchListView) {
                ((COUITouchListView) childAt).allowScroll(z2);
            } else if (childAt instanceof COUITouchRecyclerView) {
                ((COUITouchRecyclerView) childAt).allowScroll(z2);
            }
        }
    }

    public void configSubMenuHeaderOnClick(View.OnClickListener onClickListener) {
        this.mSubMenuHeaderClickListener = onClickListener;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (this.mDelayedMainAreaDismissEnabled && this.mSubMenuEntered && this.mSubMenuRootView != null) {
            handleMainAreaDelayedDismissOnTouch(motionEvent, actionMasked);
        }
        RegionBasedLongPressDetector regionBasedLongPressDetector = this.mRegionBasedLongPressDetector;
        if (regionBasedLongPressDetector != null) {
            regionBasedLongPressDetector.handleTouchEvent(motionEvent);
        }
        PopupMenuTouchDispatchResult popupMenuTouchDispatchResultDispatchAfterSubMenuExitIfNeeded = dispatchAfterSubMenuExitIfNeeded(motionEvent);
        PopupMenuTouchDispatchResult popupMenuTouchDispatchResult = PopupMenuTouchDispatchResult.NOT_APPLICABLE;
        if (popupMenuTouchDispatchResultDispatchAfterSubMenuExitIfNeeded != popupMenuTouchDispatchResult) {
            return popupMenuTouchDispatchResultDispatchAfterSubMenuExitIfNeeded == PopupMenuTouchDispatchResult.RETURN_TRUE;
        }
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        restoreMainMenuTouchAfterLongPressOnUp(actionMasked);
        PopupMenuTouchDispatchResult popupMenuTouchDispatchResultForwardTouchToSubMenuIfNeeded = forwardTouchToSubMenuIfNeeded(motionEvent, actionMasked, zDispatchTouchEvent);
        if (popupMenuTouchDispatchResultForwardTouchToSubMenuIfNeeded != popupMenuTouchDispatchResult) {
            return popupMenuTouchDispatchResultForwardTouchToSubMenuIfNeeded == PopupMenuTouchDispatchResult.RETURN_TRUE;
        }
        return zDispatchTouchEvent;
    }

    public void hideMainMenu(boolean z2) {
        BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
        if (basePopupMenuAnimationController == null) {
            return;
        }
        if (z2) {
            basePopupMenuAnimationController.startMainMenuExit();
        } else {
            basePopupMenuAnimationController.startMainMenuExit(false);
        }
    }

    public void hideSubMenu(boolean z2) {
        cancelMainAreaDismissRunnable();
        ViewGroup viewGroup = this.mSubMenuRootView;
        if (viewGroup != null) {
            if (z2 && this.mSubMenuEntered) {
                this.mPendingMainMenuTouchEventRestore = true;
            }
            if (!z2) {
                this.mController.startSubMenuExit(false);
                return;
            }
            View childAt = viewGroup.getChildAt(0);
            if (childAt instanceof COUITouchListView) {
                ((COUITouchListView) childAt).smoothScrollToPosition(0);
            }
            this.mController.startSubMenuExit();
        }
    }

    public void hideSubMenuFromLongPress() {
        if (this.mSubMenuRootView != null) {
            long jUptimeMillis = SystemClock.uptimeMillis();
            MotionEvent motionEventObtain = MotionEvent.obtain(jUptimeMillis, jUptimeMillis, 3, this.mSubMenuRootView.getWidth() * 0.5f, this.mSubMenuRootView.getHeight() * 0.5f, 0);
            this.mSubMenuRootView.dispatchTouchEvent(motionEventObtain);
            motionEventObtain.recycle();
            this.mHasSentDownToSubMenu = false;
        }
        hideSubMenu(true);
    }

    public boolean isSubMenuAttached() {
        ViewGroup viewGroup = this.mSubMenuRootView;
        return (viewGroup == null || viewGroup.getParent() == null) ? false : true;
    }

    public boolean isTouchPointInSubMenuRegion(float f3, float f4) {
        return isPointInSubMenu(f3, f4);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setVisibility(0);
        ViewGroup viewGroup = this.mMainMenuRootView;
        if (viewGroup == null || this.mDomain == null) {
            return;
        }
        viewGroup.setVisibility(8);
        this.mController.startMainMenuEnter();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelMainAreaDismissRunnable();
        removeDelayedDismiss();
        BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
        if (basePopupMenuAnimationController != null) {
            basePopupMenuAnimationController.stopAllAnimation();
        }
        this.mMainMenuRootView.setFocusable(false);
        this.mMainMenuRootView.setClickable(false);
        this.mMainMenuRootView.setOnClickListener(null);
        allowListViewDispatchTouchEvent(this.mMainMenuRootView, true);
        allowListViewScroll(this.mSubMenuRootView, false);
        configSubMenuHeaderOnClick(null);
        removeSubMenuView();
        this.mShowSubMenuAfterAnimationSkipped = null;
        BasePopupMenuAnimationController basePopupMenuAnimationController2 = this.mController;
        if (basePopupMenuAnimationController2 != null) {
            basePopupMenuAnimationController2.destroy();
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (DEBUG_DRAW) {
            drawDebugLayoutInspectOverlay(canvas);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        ViewGroup viewGroup = this.mMainMenuRootView;
        if (viewGroup != null) {
            Rect rect = this.mDomain.mMainMenu;
            viewGroup.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
        ViewGroup viewGroup2 = this.mSubMenuRootView;
        if (viewGroup2 != null) {
            Rect rect2 = this.mDomain.mSubMenu;
            viewGroup2.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        ViewGroup viewGroup = this.mMainMenuRootView;
        if (viewGroup != null) {
            viewGroup.measure(View.MeasureSpec.makeMeasureSpec(this.mMainMenuWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mMainMenuHeight, 1073741824));
            BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
            if (basePopupMenuAnimationController != null) {
                basePopupMenuAnimationController.setMainMenuSize(this.mMainMenuWidth, this.mMainMenuHeight);
            }
        }
        ViewGroup viewGroup2 = this.mSubMenuRootView;
        if (viewGroup2 != null) {
            viewGroup2.measure(View.MeasureSpec.makeMeasureSpec(this.mSubMenuWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mSubMenuHeight, 1073741824));
            BasePopupMenuAnimationController basePopupMenuAnimationController2 = this.mController;
            if (basePopupMenuAnimationController2 != null) {
                basePopupMenuAnimationController2.setSubMenuSize(this.mSubMenuWidth, this.mSubMenuHeight);
            }
        }
        setMeasuredDimension(View.MeasureSpec.getSize(i3), View.MeasureSpec.getSize(i4));
    }

    public void performSubMenuHeader(View view) {
        View.OnClickListener onClickListener = this.mSubMenuHeaderClickListener;
        if (onClickListener == null) {
            return;
        }
        if (view == null) {
            view = this.mSubMenuRootView;
        }
        if (view == null) {
            return;
        }
        onClickListener.onClick(view);
    }

    public void postDelayedDismiss(Runnable runnable, long j3) {
        removeDelayedDismiss();
        this.mPendingDismissRunnable = runnable;
        if (runnable != null) {
            postDelayed(runnable, j3);
        }
    }

    public void postSkipExitAnimationAndShowSubMenu(Runnable runnable) {
        this.mShowSubMenuAfterAnimationSkipped = runnable;
    }

    public void removeDelayedDismiss() {
        Runnable runnable = this.mPendingDismissRunnable;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.mPendingDismissRunnable = null;
        }
    }

    public void removeSubMenuView() {
        ViewGroup viewGroup = this.mSubMenuRootView;
        if (viewGroup != null) {
            removeView(viewGroup);
            this.mSubMenuRootView = null;
            this.mController.detach();
            this.mController.setSubMenuView(null);
            this.mNeedReDispatchDownForNextEvent = true;
            this.mHasSentDownToSubMenu = false;
            this.mSubMenuEntered = false;
            RegionBasedLongPressDetector regionBasedLongPressDetector = this.mRegionBasedLongPressDetector;
            if (regionBasedLongPressDetector != null) {
                regionBasedLongPressDetector.setSubMenuJustEntered(false);
            }
        }
    }

    public void setDomain(PopupMenuDomain popupMenuDomain) {
        this.mDomain = popupMenuDomain;
        if (COUIResponsiveUtils.isSmallScreen(getContext(), this.mDomain.mWindow.width())) {
            if (this.mSmallScreenController == null) {
                this.mSmallScreenController = new SmallScreenAnimationController(getContext());
            }
            this.mController = this.mSmallScreenController;
        } else {
            if (this.mDefaultScreenController == null) {
                this.mDefaultScreenController = new DefaultScreenAnimationController();
            }
            this.mController = this.mDefaultScreenController;
        }
        this.mController.setDomain(this.mDomain);
        invalidate();
    }

    public void setMainMenuSize(int i3, int i4) {
        this.mMainMenuWidth = i3;
        this.mMainMenuHeight = i4;
    }

    public void setOnSubMenuStateChangedListener(OnMenuStateChangedListener onMenuStateChangedListener) {
        this.mMenuStateChangedListener = onMenuStateChangedListener;
    }

    public void setRegionBasedLongPressDetector(RegionBasedLongPressDetector regionBasedLongPressDetector) {
        this.mRegionBasedLongPressDetector = regionBasedLongPressDetector;
    }

    public void setSubMenuSize(int i3, int i4) {
        this.mSubMenuWidth = i3;
        this.mSubMenuHeight = i4;
    }

    public void showMainMenu() {
        BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
        if (basePopupMenuAnimationController == null) {
            return;
        }
        basePopupMenuAnimationController.startMainMenuEnter();
        if (this.mSubMenuRootView != null) {
            hideSubMenu(true);
        }
    }

    public void showSubMenu() {
        this.mController.startSubMenuEnter();
    }

    public void stopAllAnimationsImmediately() {
        BasePopupMenuAnimationController basePopupMenuAnimationController = this.mController;
        if (basePopupMenuAnimationController != null) {
            basePopupMenuAnimationController.stopAllAnimation();
        }
    }

    public COUIPopupMenuRootView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPopupMenuRootView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public COUIPopupMenuRootView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mNeedReDispatchDownForNextEvent = false;
        this.mShowSubMenuAfterAnimationSkipped = null;
        this.mPendingDismissRunnable = null;
        this.mHasSentDownToSubMenu = false;
        this.mSubMenuEntered = false;
        this.mPendingMainMenuTouchEventRestore = false;
        this.mDelayedMainAreaDismissEnabled = true;
        this.mMainAreaDismissDelayMs = 500L;
        this.mMainAreaSlideTouchSlop = 0;
        this.mMainAreaSlideTracking = false;
        this.mMainAreaDownX = 0.0f;
        this.mMainAreaDownY = 0.0f;
        this.mMainAreaDismissRunnablePosted = false;
        this.mMainAreaDismissRunnable = new q(this);
        this.mSubMenuHeaderClickListener = null;
        this.mRegionBasedLongPressDetector = null;
        this.mOnSubMenuStateChangedListener = new AnonymousClass1();
        this.mMainMenuRootView = null;
        this.mSubMenuRootView = null;
        this.mMainMenuWidth = 0;
        this.mMainMenuHeight = 0;
        this.mSubMenuWidth = 0;
        this.mSubMenuHeight = 0;
        this.mDebugPaint = new Paint(1);
        this.mDrawingRect = new Rect();
        this.mSubMenuTransformCoords = new float[2];
        this.mMainAreaSlideTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if (DEBUG_DRAW) {
            setWillNotDraw(false);
        }
        setFocusable(false);
        setLayerType(2, null);
    }
}