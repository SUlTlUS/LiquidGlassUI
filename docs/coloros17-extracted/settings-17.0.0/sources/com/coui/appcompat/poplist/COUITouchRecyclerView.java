package com.coui.appcompat.poplist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.AccessibilityUtils.COUIAccessibilityUtil;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundRect.COUIShapePath;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.scrollbar.COUIScrollBar;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusPathAdapter;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUITouchRecyclerView extends COUIRecyclerView {
    private static final boolean COUI_DEBUG;
    private static final float HALF_HEIGHT = 2.0f;
    private static final int[] MASK_PRESSED;
    private static final float MASK_SPRING_BOUNCE = 0.0f;
    private static final float MASK_SPRING_RESPONSE_MIXED = 0.2f;
    private static final float MASK_SPRING_RESPONSE_VERTICAL_ONLY = 0.15f;
    private static final float SMOOTH_WEIGHT_FULL_ROUND = 1.0f;
    private static final String TAG = "COUITouchRecyclerView";
    private boolean mAllowDispatchEvent;
    private boolean mAllowScroll;
    private boolean mClearMaskOnDown;
    private View mDownView;
    private boolean mDownViewIsDirectTouch;
    private int mDownY;
    private final int mHorizontalIconOnlyMaskHeight;
    private boolean mInTalkbackMode;
    private boolean mIsNeedVibrate;
    private int mLastMotion;
    private int mLastSlideSelectionPosition;
    private final int mMaskCornerRadius;
    private final int mMaskCornerRadiusHorizontalIconOnly;
    private final int mMaskCornerRadiusHorizontalWithText;
    private final float[] mMaskCornerRadiusScratch;
    private final int mMaskHorizontalMargin;
    private final float mMaskSmoothWeightNormal;
    private int mPendingPrepareAdapterPosition;
    private final int mScrollbarVerticalPadding;
    private final Rect mSharedMaskAnimEnd;
    private final Rect mSharedMaskAnimStart;
    private final Rect mSharedMaskBounds;
    private ViewTreeObserver.OnPreDrawListener mSharedMaskBoundsPreDrawListener;
    private Runnable mSharedMaskBoundsReadyAction;
    private float mSharedMaskCurrentCornerRadius;
    private float mSharedMaskCurrentWeight;
    private COUIMaskEffectDrawable mSharedMaskDrawable;
    private float mSharedMaskEndCornerRadius;
    private float mSharedMaskEndWeight;
    private boolean mSharedMaskIsExiting;
    private OplusPathAdapter mSharedMaskOplusPathAdapter;
    private final Path mSharedMaskPath;
    private final RectF mSharedMaskRectF;
    private COUISpringAnimation mSharedMaskSpringAnim;
    private COUISpringForce mSharedMaskSpringForce;
    private float mSharedMaskStartCornerRadius;
    private float mSharedMaskStartWeight;
    private int mSharedMaskTargetPosition;
    private View mSharedMaskTargetView;

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUITouchRecyclerView$1, reason: invalid class name */
    public class AnonymousClass1 implements ViewTreeObserver.OnGlobalLayoutListener {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            COUITouchRecyclerView.l(COUITouchRecyclerView.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.COUITouchRecyclerView$2, reason: invalid class name */
    public class AnonymousClass2 implements ViewTreeObserver.OnPreDrawListener {
        public AnonymousClass2() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            if (!COUITouchRecyclerView.this.isAttachedToWindow()) {
                COUITouchRecyclerView.j(COUITouchRecyclerView.this);
                return true;
            }
            if (COUITouchRecyclerView.e(COUITouchRecyclerView.this) == -1) {
                COUITouchRecyclerView.k(COUITouchRecyclerView.this);
                return true;
            }
            COUITouchRecyclerView cOUITouchRecyclerView = COUITouchRecyclerView.this;
            if (!COUITouchRecyclerView.i(cOUITouchRecyclerView, COUITouchRecyclerView.e(cOUITouchRecyclerView))) {
                return true;
            }
            COUITouchRecyclerView.k(COUITouchRecyclerView.this);
            COUITouchRecyclerView.g(COUITouchRecyclerView.this, -1);
            Runnable runnableF = COUITouchRecyclerView.f(COUITouchRecyclerView.this);
            COUITouchRecyclerView.h(COUITouchRecyclerView.this, null);
            COUITouchRecyclerView.this.invalidate();
            if (runnableF != null) {
                runnableF.run();
            }
            return true;
        }
    }

    static {
        COUI_DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
        MASK_PRESSED = new int[]{16842919, 16842910};
    }

    public COUITouchRecyclerView(Context context) {
        this(context, null);
    }

    private void applyPopupVerticalScrollBarDelegate() {
        COUIScrollBar cOUIScrollDelegate = getCOUIScrollDelegate();
        setNewCOUIScrollDelegate(new COUIScrollBar.Builder(this).marginTop(this.mScrollbarVerticalPadding).marginBottom(this.mScrollbarVerticalPadding).build());
        if (cOUIScrollDelegate != null) {
            cOUIScrollDelegate.release();
        }
    }

    private boolean applySharedMaskBoundsAt(int i3) {
        RecyclerView.Adapter adapter = getAdapter();
        int itemCount = adapter != null ? adapter.getItemCount() : 0;
        for (int iMax = Math.max(0, i3); iMax < itemCount; iMax++) {
            RecyclerView.ViewHolder viewHolderFindViewHolderForAdapterPosition = findViewHolderForAdapterPosition(iMax);
            if (viewHolderFindViewHolderForAdapterPosition != null) {
                View view = viewHolderFindViewHolderForAdapterPosition.itemView;
                if (canSelect(view, iMax) && computeSharedMaskEndForItem(view, iMax, this.mSharedMaskAnimEnd, this.mMaskCornerRadiusScratch)) {
                    this.mSharedMaskIsExiting = false;
                    this.mSharedMaskTargetView = view;
                    this.mSharedMaskTargetPosition = iMax;
                    syncMaskRect(this.mSharedMaskAnimEnd, this.mMaskCornerRadiusScratch[0]);
                    return true;
                }
            }
        }
        return false;
    }

    private void attachSharedMaskToView(View view, int i3) {
        if (view == null) {
            return;
        }
        boolean z2 = this.mSharedMaskIsExiting;
        this.mSharedMaskIsExiting = false;
        int i4 = this.mSharedMaskTargetPosition;
        this.mSharedMaskTargetView = view;
        this.mSharedMaskTargetPosition = i3;
        if (computeSharedMaskEndForItem(view, i3, this.mSharedMaskAnimEnd, this.mMaskCornerRadiusScratch)) {
            float f3 = this.mMaskCornerRadiusScratch[0];
            if (z2) {
                this.mSharedMaskBounds.setEmpty();
            }
            boolean z3 = i3 == i4;
            boolean zEquals = this.mSharedMaskBounds.equals(this.mSharedMaskAnimEnd);
            if (this.mSharedMaskBounds.isEmpty()) {
                syncMaskRect(this.mSharedMaskAnimEnd, f3);
                maskPressed();
                this.mSharedMaskDrawable.setTouchEntered();
            } else if (!z3 || !zEquals) {
                startSharedMaskAnim(this.mSharedMaskBounds, this.mSharedMaskAnimEnd, f3);
            } else {
                maskPressed();
                this.mSharedMaskDrawable.setTouchEntered();
            }
        }
    }

    public static /* synthetic */ void c(COUITouchRecyclerView cOUITouchRecyclerView, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Rect rect = cOUITouchRecyclerView.mSharedMaskAnimStart;
        float f5 = (rect.left + rect.right) / 2.0f;
        float f6 = (rect.top + rect.bottom) / 2.0f;
        Rect rect2 = cOUITouchRecyclerView.mSharedMaskAnimEnd;
        float fSqrt = (float) Math.sqrt(Math.pow(((rect2.left + rect2.right) / 2.0f) - f5, 2.0d) + Math.pow(((rect2.top + rect2.bottom) / 2.0f) - f6, 2.0d));
        float fMax = Math.max(0.0f, Math.min(1.0f, fSqrt > 0.0f ? f3 / fSqrt : 1.0f));
        Rect rect3 = new Rect(cOUITouchRecyclerView.mSharedMaskBounds);
        cOUITouchRecyclerView.lerpRect(cOUITouchRecyclerView.mSharedMaskAnimStart, cOUITouchRecyclerView.mSharedMaskAnimEnd, fMax, cOUITouchRecyclerView.mSharedMaskBounds);
        float f7 = cOUITouchRecyclerView.mSharedMaskStartCornerRadius;
        cOUITouchRecyclerView.mSharedMaskCurrentCornerRadius = f7 + ((cOUITouchRecyclerView.mSharedMaskEndCornerRadius - f7) * fMax);
        float f8 = cOUITouchRecyclerView.mSharedMaskStartWeight;
        cOUITouchRecyclerView.mSharedMaskCurrentWeight = f8 + ((cOUITouchRecyclerView.mSharedMaskEndWeight - f8) * fMax);
        cOUITouchRecyclerView.updateSharedMaskPath();
        cOUITouchRecyclerView.mSharedMaskDrawable.setBounds(cOUITouchRecyclerView.mSharedMaskBounds);
        if (!cOUITouchRecyclerView.mSharedMaskIsExiting) {
            cOUITouchRecyclerView.maskPressed();
        }
        rect3.union(cOUITouchRecyclerView.mSharedMaskBounds);
        cOUITouchRecyclerView.invalidate(rect3);
    }

    private Rect calculateVerticalMaskBounds(Rect rect, int i3) {
        int i4 = rect.left;
        int i5 = this.mMaskHorizontalMargin;
        return new Rect(i4 + i5, rect.top, rect.right - i5, rect.bottom);
    }

    private boolean canSelect(View view, int i3) {
        return currentViewEnable(view) && isSelectableMenuItem(view, i3);
    }

    private void clearHandoffMask() {
        this.mSharedMaskBounds.setEmpty();
        this.mSharedMaskTargetView = null;
        this.mSharedMaskTargetPosition = -1;
        updateSharedMaskPath();
    }

    private void clearPendingSharedMaskLayoutAndPress() {
        unregisterSharedMaskBoundsPreDrawListener();
        this.mPendingPrepareAdapterPosition = -1;
        this.mSharedMaskBoundsReadyAction = null;
    }

    private boolean computeSharedMaskEndForItem(View view, int i3, Rect rect, float[] fArr) {
        int i4;
        float fHeight;
        PopupListItem item;
        this.mSharedMaskAnimStart.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        if (this.mSharedMaskAnimStart.isEmpty()) {
            return false;
        }
        RecyclerView.Adapter adapter = getAdapter();
        boolean z2 = adapter instanceof MixedPopupListAdapter;
        boolean z3 = z2 && ((MixedPopupListAdapter) adapter).getItemViewType(i3) == 0;
        boolean z4 = z3 && z2 && (item = ((MixedPopupListAdapter) adapter).getItem(i3)) != null && TextUtils.isEmpty(item.getTitle());
        if (!z3) {
            rect.set(calculateVerticalMaskBounds(this.mSharedMaskAnimStart, i3));
        } else if (z4) {
            Rect rect2 = this.mSharedMaskAnimStart;
            int iMin = Math.min(rect2.bottom, rect2.top + this.mHorizontalIconOnlyMaskHeight);
            Rect rect3 = this.mSharedMaskAnimStart;
            rect.set(rect3.left, rect3.top, rect3.right, iMin);
        } else {
            rect.set(this.mSharedMaskAnimStart);
        }
        if (!z3) {
            if (isSingleLineVerticalItem(view)) {
                fHeight = rect.height() / 2.0f;
            } else {
                i4 = this.mMaskCornerRadius;
            }
            fArr[0] = fHeight;
            return true;
        }
        i4 = z4 ? this.mMaskCornerRadiusHorizontalIconOnly : this.mMaskCornerRadiusHorizontalWithText;
        fHeight = i4;
        fArr[0] = fHeight;
        return true;
    }

    private boolean currentViewEnable(View view) {
        return view != null && view.isEnabled();
    }

    public static /* synthetic */ void d(COUITouchRecyclerView cOUITouchRecyclerView, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        if (z2) {
            if (!cOUITouchRecyclerView.mSharedMaskBounds.isEmpty()) {
                cOUITouchRecyclerView.mSharedMaskDrawable.setBounds(cOUITouchRecyclerView.mSharedMaskBounds);
            }
            if (COUI_DEBUG) {
                Log.d(TAG, "startSharedMaskAnim: animation canceled, keeping current bounds: " + cOUITouchRecyclerView.mSharedMaskBounds);
                return;
            }
            return;
        }
        cOUITouchRecyclerView.mSharedMaskBounds.set(cOUITouchRecyclerView.mSharedMaskAnimEnd);
        cOUITouchRecyclerView.mSharedMaskCurrentCornerRadius = cOUITouchRecyclerView.mSharedMaskEndCornerRadius;
        cOUITouchRecyclerView.mSharedMaskCurrentWeight = cOUITouchRecyclerView.mSharedMaskEndWeight;
        cOUITouchRecyclerView.updateSharedMaskPath();
        cOUITouchRecyclerView.mSharedMaskDrawable.setBounds(cOUITouchRecyclerView.mSharedMaskBounds);
        if (!cOUITouchRecyclerView.mSharedMaskIsExiting) {
            cOUITouchRecyclerView.maskPressed();
        }
        cOUITouchRecyclerView.invalidate(cOUITouchRecyclerView.mSharedMaskBounds);
    }

    private void drawSharedMask(Canvas canvas) {
        if (this.mSharedMaskDrawable != null) {
            if (!this.mSharedMaskBounds.isEmpty() || this.mSharedMaskIsExiting) {
                this.mSharedMaskDrawable.setBounds(this.mSharedMaskBounds);
                this.mSharedMaskDrawable.draw(canvas);
            }
        }
    }

    public static /* bridge */ /* synthetic */ int e(COUITouchRecyclerView cOUITouchRecyclerView) {
        return cOUITouchRecyclerView.mPendingPrepareAdapterPosition;
    }

    private void ensureSharedMaskSpringAnimation() {
        if (this.mSharedMaskSpringAnim == null) {
            COUISpringForce cOUISpringForce = new COUISpringForce();
            this.mSharedMaskSpringForce = cOUISpringForce;
            cOUISpringForce.setBounce(0.0f);
            this.mSharedMaskSpringForce.setResponse(getMaskSpringResponse());
            COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(new FloatValueHolder(0.0f));
            this.mSharedMaskSpringAnim = cOUISpringAnimation;
            cOUISpringAnimation.setSpring(this.mSharedMaskSpringForce);
            this.mSharedMaskSpringAnim.addUpdateListener(new u(this));
            this.mSharedMaskSpringAnim.addEndListener(new v(this));
        }
    }

    public static /* bridge */ /* synthetic */ Runnable f(COUITouchRecyclerView cOUITouchRecyclerView) {
        return cOUITouchRecyclerView.mSharedMaskBoundsReadyAction;
    }

    public static /* bridge */ /* synthetic */ void g(COUITouchRecyclerView cOUITouchRecyclerView, int i3) {
        cOUITouchRecyclerView.mPendingPrepareAdapterPosition = i3;
    }

    private float getMaskSpringResponse() {
        return hasHorizontalItems() ? 0.2f : 0.15f;
    }

    private float getTargetSharedMaskSmoothWeight() {
        if (isSingleLineVerticalItem(this.mSharedMaskTargetView)) {
            return 1.0f;
        }
        return this.mMaskSmoothWeightNormal;
    }

    public static /* bridge */ /* synthetic */ void h(COUITouchRecyclerView cOUITouchRecyclerView, Runnable runnable) {
        cOUITouchRecyclerView.mSharedMaskBoundsReadyAction = runnable;
    }

    private boolean hasHorizontalItems() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter instanceof MixedPopupListAdapter) {
            MixedPopupListAdapter mixedPopupListAdapter = (MixedPopupListAdapter) adapter;
            int itemCount = mixedPopupListAdapter.getItemCount();
            for (int i3 = 0; i3 < itemCount; i3++) {
                if (mixedPopupListAdapter.getItemViewType(i3) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static /* bridge */ /* synthetic */ boolean i(COUITouchRecyclerView cOUITouchRecyclerView, int i3) {
        return cOUITouchRecyclerView.applySharedMaskBoundsAt(i3);
    }

    private void initSharedMaskDrawable(Context context) {
        int color;
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(context, 0);
        this.mSharedMaskDrawable = cOUIMaskEffectDrawable;
        cOUIMaskEffectDrawable.enableFocusedState(false);
        this.mSharedMaskDrawable.setIsRoundStyle(false);
        if (COUIMaterialStateManager.INSTANCE.checkThemeEnable(context)) {
            color = ContextCompat.getColor(context, COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? R.color.coui_color_hover_dark : R.color.coui_color_hover_light);
        } else {
            color = ContextCompat.getColor(context, R.color.coui_color_hover);
        }
        this.mSharedMaskDrawable.setPressEndMaskColor(color);
        this.mSharedMaskDrawable.setCallback(this);
    }

    private boolean isSelectableMenuItem(View view, int i3) {
        PopupListItem item;
        if (view == null) {
            return false;
        }
        RecyclerView.Adapter adapter = getAdapter();
        if (!(adapter instanceof MixedPopupListAdapter)) {
            return true;
        }
        MixedPopupListAdapter mixedPopupListAdapter = (MixedPopupListAdapter) adapter;
        int itemViewType = mixedPopupListAdapter.getItemViewType(i3);
        return (itemViewType == 4 || itemViewType == 3 || ((item = mixedPopupListAdapter.getItem(i3)) != null && item.getItemType() == 3)) ? false : true;
    }

    private boolean isSingleLineVerticalItem(View view) {
        TextView textView;
        if (view == null || (textView = (TextView) view.findViewById(com.support.poplist.R.id.popup_list_window_item_title)) == null || textView.getLineCount() != 1) {
            return false;
        }
        TextView textView2 = (TextView) view.findViewById(com.support.poplist.R.id.popup_list_window_item_description);
        return textView2 == null || textView2.getVisibility() != 0 || TextUtils.isEmpty(textView2.getText());
    }

    public static /* bridge */ /* synthetic */ void j(COUITouchRecyclerView cOUITouchRecyclerView) {
        cOUITouchRecyclerView.clearPendingSharedMaskLayoutAndPress();
    }

    public static /* bridge */ /* synthetic */ void k(COUITouchRecyclerView cOUITouchRecyclerView) {
        cOUITouchRecyclerView.unregisterSharedMaskBoundsPreDrawListener();
    }

    public static /* bridge */ /* synthetic */ void l(COUITouchRecyclerView cOUITouchRecyclerView) {
        cOUITouchRecyclerView.updateOverScrollMode();
    }

    private void lerpRect(Rect rect, Rect rect2, float f3, Rect rect3) {
        int i3 = rect.left;
        int iRound = Math.round(i3 + ((rect2.left - i3) * f3));
        int i4 = rect.top;
        int iRound2 = Math.round(i4 + ((rect2.top - i4) * f3));
        int i5 = rect.right;
        int iRound3 = Math.round(i5 + ((rect2.right - i5) * f3));
        int i6 = rect.bottom;
        rect3.set(iRound, iRound2, iRound3, Math.round(i6 + ((rect2.bottom - i6) * f3)));
    }

    private void maskPressed() {
        this.mSharedMaskDrawable.setState(MASK_PRESSED);
    }

    private void performHapticFeedback() {
        if (this.mIsNeedVibrate) {
            performHapticFeedback(302);
        }
    }

    private void rebuildSharedMaskSmoothPath(RectF rectF, float f3, float f4) {
        this.mSharedMaskPath.reset();
        if (rectF.isEmpty()) {
            return;
        }
        if (!RoundCornerUtil.isPathSupportSingleCorner() || !RoundCornerUtil.isSmoothRoundRectOn() || !UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            this.mSharedMaskPath.addRoundRect(rectF, f3, f3, Path.Direction.CCW);
            return;
        }
        int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
        if (smoothStyleType != 1) {
            if (smoothStyleType == 0) {
                COUIShapePath.getRoundRectPath(this.mSharedMaskPath, rectF, f3);
                return;
            } else {
                this.mSharedMaskPath.addRoundRect(rectF, f3, f3, Path.Direction.CCW);
                return;
            }
        }
        if (this.mSharedMaskOplusPathAdapter == null) {
            this.mSharedMaskOplusPathAdapter = new OplusPathAdapter(this.mSharedMaskPath, 1);
        }
        if (COUIVersionUtil.getOSVersionCode() > 37) {
            this.mSharedMaskOplusPathAdapter.addSmoothRoundRect(rectF, f3, f3, f4, Path.Direction.CCW);
        } else {
            this.mSharedMaskOplusPathAdapter.addSmoothRoundRect(rectF, f3, f3, Path.Direction.CCW);
        }
    }

    private boolean shouldAttachSharedMask(View view, int i3) {
        PopupListItem item;
        RecyclerView.Adapter adapter = getAdapter();
        if (!(adapter instanceof MixedPopupListAdapter)) {
            return true;
        }
        MixedPopupListAdapter mixedPopupListAdapter = (MixedPopupListAdapter) adapter;
        return mixedPopupListAdapter.getItemViewType(i3) != 5 || (item = mixedPopupListAdapter.getItem(i3)) == null || item.isSharedPressedStateEnabled();
    }

    private void startSharedMaskAnim(Rect rect, Rect rect2, float f3) {
        if (rect == null || rect2 == null) {
            if (COUI_DEBUG) {
                Log.w(TAG, "startSharedMaskAnim: start or end is null");
                return;
            }
            return;
        }
        if (rect.equals(rect2)) {
            syncMaskRect(rect2, f3);
            return;
        }
        this.mSharedMaskAnimStart.set(rect);
        this.mSharedMaskAnimEnd.set(rect2);
        this.mSharedMaskStartCornerRadius = this.mSharedMaskCurrentCornerRadius;
        this.mSharedMaskStartWeight = this.mSharedMaskCurrentWeight;
        this.mSharedMaskEndCornerRadius = f3;
        this.mSharedMaskEndWeight = getTargetSharedMaskSmoothWeight();
        maskPressed();
        if (!this.mSharedMaskIsExiting) {
            this.mSharedMaskDrawable.setTouchEntered();
        }
        ensureSharedMaskSpringAnimation();
        if (this.mSharedMaskSpringForce != null) {
            this.mSharedMaskSpringForce.setResponse(getMaskSpringResponse());
        }
        float fSqrt = (float) Math.sqrt(Math.pow(((rect2.left + rect2.right) / 2.0f) - ((rect.left + rect.right) / 2.0f), 2.0d) + Math.pow(((rect2.top + rect2.bottom) / 2.0f) - ((rect.top + rect.bottom) / 2.0f), 2.0d));
        if (COUI_DEBUG) {
            Log.d(TAG, "startSharedMaskAnim: start=" + rect + ", end=" + rect2 + ", distance=" + fSqrt + ", startCornerRadius=" + this.mSharedMaskStartCornerRadius + ", targetCornerRadius=" + f3);
        }
        if (fSqrt <= 0.0f) {
            syncMaskRect(rect2, f3);
        } else {
            this.mSharedMaskSpringAnim.setStartValue(0.0f);
            this.mSharedMaskSpringAnim.animateToFinalPosition(fSqrt);
        }
    }

    private void syncMaskRect(Rect rect, float f3) {
        this.mSharedMaskBounds.set(rect);
        this.mSharedMaskCurrentCornerRadius = f3;
        this.mSharedMaskCurrentWeight = getTargetSharedMaskSmoothWeight();
        updateSharedMaskPath();
        this.mSharedMaskDrawable.setBounds(this.mSharedMaskBounds);
    }

    private void unregisterSharedMaskBoundsPreDrawListener() {
        if (this.mSharedMaskBoundsPreDrawListener != null) {
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this.mSharedMaskBoundsPreDrawListener);
            }
            this.mSharedMaskBoundsPreDrawListener = null;
        }
    }

    private void updateOverScrollMode() {
        boolean z2 = true;
        if (!canScrollVertically(1) && !canScrollVertically(-1)) {
            z2 = false;
        }
        setOverScrollEnable(z2);
    }

    private void updateSharedMaskPath() {
        if (this.mSharedMaskBounds.isEmpty()) {
            this.mSharedMaskPath.reset();
            this.mSharedMaskDrawable.setMaskPath(null);
        } else {
            this.mSharedMaskRectF.set(this.mSharedMaskBounds);
            rebuildSharedMaskSmoothPath(this.mSharedMaskRectF, this.mSharedMaskCurrentCornerRadius, this.mSharedMaskCurrentWeight);
            this.mSharedMaskDrawable.setMaskPath(this.mSharedMaskPath);
        }
    }

    public void allowDispatchEvent(boolean z2) {
        this.mAllowDispatchEvent = z2;
    }

    public void allowScroll(boolean z2) {
        this.mAllowScroll = z2;
    }

    public void beginSharedMaskPressHandoffThenAnimate(float f3, float f4, boolean z2) {
        if (this.mSharedMaskDrawable == null) {
            return;
        }
        maskPressed();
        this.mSharedMaskDrawable.animatePressFromProgress(f3, f4, z2);
        this.mClearMaskOnDown = true;
    }

    public void cancelLastItemSelect(MotionEvent motionEvent) {
        int childAdapterPosition;
        View view = this.mDownView;
        if (view != null && (childAdapterPosition = getChildAdapterPosition(view)) != -1 && canSelect(this.mDownView, childAdapterPosition)) {
            MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
            motionEventObtain.setAction(3);
            this.mDownView.dispatchTouchEvent(motionEventObtain);
            motionEventObtain.recycle();
        }
        this.mDownView = null;
    }

    public void clearSharedMask(boolean z2) {
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = this.mSharedMaskDrawable;
        if (cOUIMaskEffectDrawable == null) {
            return;
        }
        boolean zIsStateLocked = cOUIMaskEffectDrawable.isStateLocked(1);
        boolean zIsStateLocked2 = this.mSharedMaskDrawable.isStateLocked(16843623);
        if (zIsStateLocked || zIsStateLocked2) {
            if (COUI_DEBUG) {
                Log.d(TAG, "clearSharedMask: State is locked (touchEntered=" + zIsStateLocked + ", hovered=" + zIsStateLocked2 + "), skip clearing");
                return;
            }
            return;
        }
        clearPendingSharedMaskLayoutAndPress();
        this.mClearMaskOnDown = false;
        COUISpringAnimation cOUISpringAnimation = this.mSharedMaskSpringAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning() && !this.mSharedMaskIsExiting && !z2) {
            this.mSharedMaskSpringAnim.cancel();
            if (!this.mSharedMaskBounds.isEmpty()) {
                this.mSharedMaskDrawable.setBounds(this.mSharedMaskBounds);
            }
        }
        if (z2) {
            this.mSharedMaskIsExiting = true;
            this.mSharedMaskDrawable.setTouchExited();
            this.mSharedMaskTargetView = null;
            this.mSharedMaskTargetPosition = -1;
        } else {
            this.mSharedMaskIsExiting = false;
            this.mSharedMaskDrawable.setState(new int[0]);
            this.mSharedMaskDrawable.setTouchExited();
            this.mSharedMaskTargetView = null;
            this.mSharedMaskTargetPosition = -1;
            this.mSharedMaskBounds.setEmpty();
        }
        invalidate();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawSharedMask(canvas);
    }

    /* JADX WARN: Code duplicated, block: B:67:0x010c  */
    @Override // androidx.recyclerview.widget.COUIRecyclerView, android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int childAdapterPosition;
        boolean z2;
        int i3;
        View view;
        int itemViewType;
        if (!this.mAllowDispatchEvent) {
            return false;
        }
        if (!this.mAllowScroll && motionEvent.getActionMasked() == 2) {
            return true;
        }
        boolean z3 = (canScrollVertically(1) || canScrollVertically(-1)) ? false : true;
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        int x2 = (int) motionEvent.getX();
        int y2 = (int) motionEvent.getY();
        boolean z4 = COUI_DEBUG;
        if (z4) {
            Log.d(TAG, "dispatchTouchEvent actionMasked:" + MotionEvent.actionToString(actionMasked) + ",actionIndex:" + actionIndex + ",getPointerCount:" + motionEvent.getPointerCount());
        }
        if (actionMasked == 0) {
            this.mDownY = y2;
            this.mInTalkbackMode = COUIAccessibilityUtil.isTalkbackEnabled(getContext());
            if (getScrollY() == 0 && getScrollX() == 0) {
                View viewFindChildViewUnder = findChildViewUnder(x2, y2);
                if (viewFindChildViewUnder != null && (childAdapterPosition = getChildAdapterPosition(viewFindChildViewUnder)) != -1 && isSelectableMenuItem(viewFindChildViewUnder, childAdapterPosition)) {
                    this.mDownView = viewFindChildViewUnder;
                    this.mDownViewIsDirectTouch = true;
                    this.mLastSlideSelectionPosition = childAdapterPosition;
                    if (currentViewEnable(viewFindChildViewUnder)) {
                        if (this.mClearMaskOnDown) {
                            clearHandoffMask();
                            this.mClearMaskOnDown = false;
                        }
                        if (shouldAttachSharedMask(viewFindChildViewUnder, childAdapterPosition)) {
                            attachSharedMaskToView(viewFindChildViewUnder, childAdapterPosition);
                        }
                    }
                }
            } else {
                clearSharedMask(true);
                this.mDownView = null;
            }
        } else if (actionMasked == 1) {
            boolean z5 = (getScrollY() == 0 && getScrollX() == 0) ? false : true;
            View view2 = this.mDownView;
            if (((view2 != null && !this.mInTalkbackMode) || this.mLastMotion == 0) && !z5) {
                boolean z6 = this.mDownViewIsDirectTouch;
                super.dispatchTouchEvent(motionEvent);
                if (view2 != null) {
                    int childAdapterPosition2 = getChildAdapterPosition(view2);
                    if (z4) {
                        Log.d(TAG, "ACTION_UP: position=" + childAdapterPosition2);
                    }
                    if (childAdapterPosition2 != -1) {
                        view2.cancelLongPress();
                        if ((!z6 && view2.isClickable()) || (z6 && !view2.isEnabled())) {
                            view2.performClick();
                        }
                    }
                }
                clearSharedMask(true);
                this.mDownView = null;
                this.mDownViewIsDirectTouch = false;
                this.mLastSlideSelectionPosition = -1;
                this.mLastMotion = actionMasked;
                return true;
            }
            clearSharedMask(true);
            this.mDownView = null;
            this.mLastSlideSelectionPosition = -1;
        } else if (actionMasked != 2) {
            if (actionMasked == 3) {
                clearSharedMask(true);
                cancelLastItemSelect(motionEvent);
                this.mDownView = null;
                this.mLastSlideSelectionPosition = -1;
            } else if (actionMasked == 5) {
                this.mLastMotion = actionMasked;
                clearSharedMask(true);
                cancelLastItemSelect(motionEvent);
                this.mLastSlideSelectionPosition = -1;
            }
        } else if (getScrollY() == 0 && getScrollX() == 0) {
            int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            if (this.mDownView == null || z3 || Math.abs(y2 - this.mDownY) <= scaledTouchSlop) {
                View viewFindChildViewUnder2 = findChildViewUnder(x2, y2);
                int childAdapterPosition3 = viewFindChildViewUnder2 != null ? getChildAdapterPosition(viewFindChildViewUnder2) : -1;
                RecyclerView.Adapter adapter = getAdapter();
                boolean z7 = childAdapterPosition3 != -1 && (adapter instanceof MixedPopupListAdapter) && ((itemViewType = adapter.getItemViewType(childAdapterPosition3)) == 4 || itemViewType == 3);
                if (childAdapterPosition3 == -1 && (i3 = this.mSharedMaskTargetPosition) != -1 && (adapter instanceof MixedPopupListAdapter) && adapter.getItemViewType(i3) == 0 && (view = this.mSharedMaskTargetView) != null) {
                    int top = view.getTop();
                    int bottom = this.mSharedMaskTargetView.getBottom();
                    if (y2 < top || y2 > bottom) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                } else {
                    z2 = false;
                }
                if (!z7) {
                    if ((childAdapterPosition3 != -1 || z2) && motionEvent.getPointerCount() <= 1 && !this.mInTalkbackMode) {
                        boolean z8 = childAdapterPosition3 != this.mLastSlideSelectionPosition;
                        if (childAdapterPosition3 != -1 && z3 && z8 && viewFindChildViewUnder2 != null && isSelectableMenuItem(viewFindChildViewUnder2, childAdapterPosition3)) {
                            cancelLastItemSelect(motionEvent);
                            if (currentViewEnable(viewFindChildViewUnder2) && shouldAttachSharedMask(viewFindChildViewUnder2, childAdapterPosition3)) {
                                attachSharedMaskToView(viewFindChildViewUnder2, childAdapterPosition3);
                                performHapticFeedback();
                                this.mDownViewIsDirectTouch = false;
                                this.mDownView = viewFindChildViewUnder2;
                            } else if (currentViewEnable(viewFindChildViewUnder2)) {
                                clearSharedMask(true);
                                this.mDownViewIsDirectTouch = false;
                                this.mDownView = viewFindChildViewUnder2;
                            } else {
                                clearSharedMask(true);
                            }
                            this.mLastSlideSelectionPosition = childAdapterPosition3;
                        }
                    } else {
                        this.mLastMotion = actionMasked;
                        COUISpringAnimation cOUISpringAnimation = this.mSharedMaskSpringAnim;
                        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning() || this.mSharedMaskIsExiting) {
                            clearSharedMask(true);
                        } else {
                            this.mSharedMaskIsExiting = true;
                            this.mSharedMaskDrawable.setTouchExited();
                            this.mSharedMaskTargetView = null;
                            this.mSharedMaskTargetPosition = -1;
                        }
                        cancelLastItemSelect(motionEvent);
                        this.mLastSlideSelectionPosition = -1;
                    }
                }
            } else {
                clearSharedMask(true);
                cancelLastItemSelect(motionEvent);
                this.mDownView = null;
                this.mLastSlideSelectionPosition = -1;
            }
        } else {
            clearSharedMask(true);
            cancelLastItemSelect(motionEvent);
            this.mDownView = null;
            this.mLastSlideSelectionPosition = -1;
        }
        this.mLastMotion = actionMasked;
        return super.dispatchTouchEvent(motionEvent);
    }

    public COUIMaskEffectDrawable getSharedMaskDrawable() {
        return this.mSharedMaskDrawable;
    }

    public float getSharedMaskPressProgress() {
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = this.mSharedMaskDrawable;
        if (cOUIMaskEffectDrawable == null) {
            return 0.0f;
        }
        return cOUIMaskEffectDrawable.getPressProgressFraction();
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (drawable != this.mSharedMaskDrawable || this.mSharedMaskBounds.isEmpty()) {
            return;
        }
        invalidate(this.mSharedMaskBounds);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        applyPopupVerticalScrollBarDelegate();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearPendingSharedMaskLayoutAndPress();
        this.mClearMaskOnDown = false;
        COUISpringAnimation cOUISpringAnimation = this.mSharedMaskSpringAnim;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mSharedMaskSpringAnim.cancel();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0 || (getScrollY() == 0 && getScrollX() == 0)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    public void resetSharedMaskLockedStates() {
        COUIMaskEffectDrawable cOUIMaskEffectDrawable = this.mSharedMaskDrawable;
        if (cOUIMaskEffectDrawable == null) {
            return;
        }
        cOUIMaskEffectDrawable.setHoverStateLocked(false, false, false);
        this.mSharedMaskDrawable.setTouchEnterStateLocked(false, false, false);
    }

    public void scheduleSharedMaskBoundsReady(int i3, Runnable runnable) {
        if (this.mSharedMaskDrawable == null || i3 == -1) {
            if (runnable != null) {
                runnable.run();
            }
        } else {
            this.mPendingPrepareAdapterPosition = i3;
            this.mSharedMaskBoundsReadyAction = runnable;
            if (this.mSharedMaskBoundsPreDrawListener != null) {
                return;
            }
            this.mSharedMaskBoundsPreDrawListener = new AnonymousClass2();
            getViewTreeObserver().addOnPreDrawListener(this.mSharedMaskBoundsPreDrawListener);
        }
    }

    public void setIsNeedVibrate(boolean z2) {
        this.mIsNeedVibrate = z2;
    }

    public COUITouchRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUITouchRecyclerView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mMaskCornerRadiusScratch = new float[1];
        this.mSharedMaskBounds = new Rect();
        this.mSharedMaskRectF = new RectF();
        this.mSharedMaskPath = new Path();
        this.mSharedMaskAnimStart = new Rect();
        this.mSharedMaskAnimEnd = new Rect();
        this.mAllowDispatchEvent = true;
        this.mAllowScroll = true;
        this.mIsNeedVibrate = true;
        this.mSharedMaskIsExiting = false;
        this.mLastMotion = -1;
        this.mSharedMaskTargetPosition = -1;
        this.mLastSlideSelectionPosition = -1;
        this.mPendingPrepareAdapterPosition = -1;
        float f3 = COUIContextUtil.getFloat(context, com.support.poplist.R.dimen.coui_popup_list_window_shared_mask_smooth_round_corner_weight);
        this.mMaskSmoothWeightNormal = f3;
        this.mSharedMaskCurrentWeight = f3;
        this.mMaskHorizontalMargin = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_mask_margin_horizontal);
        this.mScrollbarVerticalPadding = context.getResources().getDimensionPixelOffset(com.support.poplist.R.dimen.coui_popup_list_window_scrollbar_vertical_padding_new);
        if (COUIVersionUtil.getOSVersionCode() > 37 && UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            this.mMaskCornerRadius = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_vertical_mask_radius_16_1);
        } else {
            this.mMaskCornerRadius = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_vertical_mask_radius);
        }
        if (COUIVersionUtil.getOSVersionCode() > 37 && UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            this.mMaskCornerRadiusHorizontalIconOnly = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_mask_radius_icon_only_16_1);
        } else {
            this.mMaskCornerRadiusHorizontalIconOnly = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_mask_radius_icon_only);
        }
        if (COUIVersionUtil.getOSVersionCode() > 37 && UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            this.mMaskCornerRadiusHorizontalWithText = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_mask_radius_with_text_16_1);
        } else {
            this.mMaskCornerRadiusHorizontalWithText = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_mask_radius_with_text);
        }
        this.mHorizontalIconOnlyMaskHeight = context.getResources().getDimensionPixelSize(com.support.poplist.R.dimen.coui_popup_list_window_horizontal_item_icon_only_height);
        initSharedMaskDrawable(context);
        setDefaultFocusHighlightEnabled(false);
        getViewTreeObserver().addOnGlobalLayoutListener(new AnonymousClass1());
    }
}