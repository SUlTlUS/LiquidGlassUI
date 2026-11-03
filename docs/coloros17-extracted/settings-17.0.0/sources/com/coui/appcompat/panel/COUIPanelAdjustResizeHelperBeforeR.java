package com.coui.appcompat.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ScrollView;
import androidx.core.view.ScrollingView;
import com.coui.appcompat.animation.COUIInEaseInterpolator;
import com.coui.appcompat.buttonBar.COUIButtonBarLayout;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.panel.R;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes7.dex */
public class COUIPanelAdjustResizeHelperBeforeR extends COUIAbsPanelAdjustResizeHelper {
    private static final float DISMISS_HEIGHT_ANIM_DURATION_COEFFICIENT = 50.0f;
    private static final float DISMISS_HEIGHT_ANIM_DURATION_INITIAL_VALUE = 200.0f;
    private static final int IME_ADJUST = 1;
    private static final int IME_HIDE = 2;
    private static final int IME_SHOW = 0;
    private static final float SHOW_HEIGHT_ANIM_DURATION_COEFFICIENT = 120.0f;
    private static final float SHOW_HEIGHT_ANIM_DURATION_INITIAL_VALUE = 300.0f;
    private ValueAnimator mBottomButtonBarAnim;
    private int mMarginBottomValue;
    private ValueAnimator mPaddingBottomAnim;
    private WeakReference<View> mPaddingBottomAnimView;
    private int mPaddingBottomOffset;
    private float mTranslateOffset;
    private static final Interpolator SHOW_HEIGHT_ANIM_INTERPOLATOR = new COUIInEaseInterpolator();
    private static final Interpolator DISMISS_HEIGHT_ANIM_INTERPOLATOR = new LinearInterpolator();
    private int mWindowType = 2;
    private int mAdjustResizeType = 2;
    private int mAdjustKeyboardStartHeight = 0;
    private int mAdjustKeyboardHeight = 0;
    private int mAdjustKeyboardOffset = 0;
    private int mFocusViewRawY = 0;
    private boolean mIsIgnoreHideKeyboardAnim = true;
    private boolean mIsKeyboardShow = false;
    private boolean mIsFocusViewDisplayInVerticalScrolledView = false;
    private View mFocusVerticalScrolledView = null;

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelAdjustResizeHelperBeforeR$1, reason: invalid class name */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$endValue;
        final /* synthetic */ int val$paddingLeft;
        final /* synthetic */ int val$paddingRight;
        final /* synthetic */ int val$paddingTop;
        final /* synthetic */ View val$view;

        public AnonymousClass1(View view, int i3, int i4, int i5, int i6) {
            this.val$view = view;
            this.val$paddingLeft = i3;
            this.val$paddingTop = i4;
            this.val$paddingRight = i5;
            this.val$endValue = i6;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$view.setPadding(this.val$paddingLeft, this.val$paddingTop, this.val$paddingRight, this.val$endValue);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelAdjustResizeHelperBeforeR$2, reason: invalid class name */
    public class AnonymousClass2 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ int val$paddingLeft;
        final /* synthetic */ int val$paddingRight;
        final /* synthetic */ int val$paddingTop;
        final /* synthetic */ View val$view;

        public AnonymousClass2(View view, int i3, int i4, int i5) {
            this.val$view = view;
            this.val$paddingLeft = i3;
            this.val$paddingTop = i4;
            this.val$paddingRight = i5;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (this.val$view.isAttachedToWindow()) {
                this.val$view.setPadding(this.val$paddingLeft, this.val$paddingTop, this.val$paddingRight, ((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelAdjustResizeHelperBeforeR$3, reason: invalid class name */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ float val$endValue;
        final /* synthetic */ COUIPanelContentLayout val$view;

        public AnonymousClass3(COUIPanelContentLayout cOUIPanelContentLayout, float f3) {
            this.val$view = cOUIPanelContentLayout;
            this.val$endValue = f3;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$view.getBtnBarLayout().setTranslationY(this.val$endValue);
            this.val$view.getDivider().setTranslationY(this.val$endValue);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelAdjustResizeHelperBeforeR$4, reason: invalid class name */
    public class AnonymousClass4 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ COUIPanelContentLayout val$view;

        public AnonymousClass4(COUIPanelContentLayout cOUIPanelContentLayout) {
            this.val$view = cOUIPanelContentLayout;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (this.val$view.isAttachedToWindow()) {
                float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                this.val$view.getBtnBarLayout().setTranslationY(fFloatValue);
                this.val$view.getDivider().setTranslationY(fFloatValue);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelAdjustResizeHelperBeforeR$5, reason: invalid class name */
    public class AnonymousClass5 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$endValue;
        final /* synthetic */ View val$view;

        public AnonymousClass5(View view, int i3) {
            this.val$view = view;
            this.val$endValue = i3;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIViewMarginUtil.setMargin(this.val$view, this.val$endValue, 3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIPanelAdjustResizeHelperBeforeR$6, reason: invalid class name */
    public class AnonymousClass6 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ View val$view;

        public AnonymousClass6(View view) {
            this.val$view = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (this.val$view.isAttachedToWindow()) {
                COUIViewMarginUtil.setMargin(this.val$view, ((Integer) valueAnimator.getAnimatedValue()).intValue(), 3);
            }
        }
    }

    private void adjustResizeBeforeR(ViewGroup viewGroup, boolean z2, int i3) {
        updateAdjustKeyboardType(z2);
        updateAdjustKeyboardData(viewGroup, i3);
        updateAdjustKeyboardOffset(viewGroup, Boolean.valueOf(z2));
        doAdjustKeyboardAnim(viewGroup, z2);
        this.mIsIgnoreHideKeyboardAnim = false;
    }

    private void doAdjustKeyboardAnim(ViewGroup viewGroup, boolean z2) {
        if (viewGroup == null || this.mPaddingBottomAnimView == null) {
            return;
        }
        if (!(viewGroup instanceof COUIPanelContentLayout)) {
            int screenHeightRealSize = UIUtil.getScreenHeightRealSize(viewGroup.getContext());
            doMarginBottomAnim(viewGroup, this.mMarginBottomValue, (long) (z2 ? Math.abs((this.mAdjustKeyboardOffset * SHOW_HEIGHT_ANIM_DURATION_COEFFICIENT) / screenHeightRealSize) + 300.0f : Math.abs((this.mAdjustKeyboardOffset * 50.0f) / screenHeightRealSize) + 200.0f));
            return;
        }
        COUIPanelContentLayout cOUIPanelContentLayout = (COUIPanelContentLayout) viewGroup;
        int maxHeight = cOUIPanelContentLayout.getMaxHeight();
        long jAbs = (long) (z2 ? Math.abs((this.mAdjustKeyboardOffset * SHOW_HEIGHT_ANIM_DURATION_COEFFICIENT) / maxHeight) + 300.0f : Math.abs((this.mAdjustKeyboardOffset * 50.0f) / maxHeight) + 200.0f);
        doPaddingBottomAnim(this.mPaddingBottomAnimView.get(), this.mPaddingBottomOffset, jAbs);
        doBottomButtonTranslateAnim(cOUIPanelContentLayout, this.mTranslateOffset, jAbs);
    }

    private void doBottomButtonTranslateAnim(COUIPanelContentLayout cOUIPanelContentLayout, float f3, long j3) {
        if (f3 == 0.0f || cOUIPanelContentLayout == null || cOUIPanelContentLayout.getBtnBarLayout() == null) {
            return;
        }
        float translationY = cOUIPanelContentLayout.getBtnBarLayout().getTranslationY();
        float fMin = Math.min(0.0f, f3 + translationY);
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(translationY, fMin);
        this.mBottomButtonBarAnim = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(j3);
        if (translationY < fMin) {
            this.mBottomButtonBarAnim.setInterpolator(SHOW_HEIGHT_ANIM_INTERPOLATOR);
        } else {
            this.mBottomButtonBarAnim.setInterpolator(DISMISS_HEIGHT_ANIM_INTERPOLATOR);
        }
        this.mBottomButtonBarAnim.addListener(new AnonymousClass3(cOUIPanelContentLayout, fMin));
        this.mBottomButtonBarAnim.addUpdateListener(new AnonymousClass4(cOUIPanelContentLayout));
        this.mBottomButtonBarAnim.start();
    }

    private void doMarginBottomAnim(View view, int i3, long j3) {
        if (i3 == 0 || view == null) {
            return;
        }
        int iMax = Math.max(0, COUIViewMarginUtil.getMargin(view, 3));
        int iMax2 = Math.max(0, i3 + iMax);
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(iMax, iMax2);
        valueAnimatorOfInt.setDuration(j3);
        if (iMax < iMax2) {
            valueAnimatorOfInt.setInterpolator(SHOW_HEIGHT_ANIM_INTERPOLATOR);
        } else {
            valueAnimatorOfInt.setInterpolator(DISMISS_HEIGHT_ANIM_INTERPOLATOR);
        }
        valueAnimatorOfInt.addListener(new AnonymousClass5(view, iMax2));
        valueAnimatorOfInt.addUpdateListener(new AnonymousClass6(view));
        valueAnimatorOfInt.start();
    }

    private void doPaddingBottomAnim(View view, int i3, long j3) {
        if (i3 == 0 || view == null) {
            return;
        }
        int paddingLeft = view.getPaddingLeft();
        int paddingRight = view.getPaddingRight();
        int paddingTop = view.getPaddingTop();
        int iMax = Math.max(0, view.getPaddingBottom());
        int iMax2 = Math.max(0, i3 + iMax);
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(iMax, iMax2);
        this.mPaddingBottomAnim = valueAnimatorOfInt;
        valueAnimatorOfInt.setDuration(j3);
        if (iMax < iMax2) {
            this.mPaddingBottomAnim.setInterpolator(SHOW_HEIGHT_ANIM_INTERPOLATOR);
        } else {
            this.mPaddingBottomAnim.setInterpolator(DISMISS_HEIGHT_ANIM_INTERPOLATOR);
        }
        this.mPaddingBottomAnim.addListener(new AnonymousClass1(view, paddingLeft, paddingTop, paddingRight, iMax2));
        this.mPaddingBottomAnim.addUpdateListener(new AnonymousClass2(view, paddingLeft, paddingTop, paddingRight));
        this.mPaddingBottomAnim.start();
    }

    private void findFocusView(ViewGroup viewGroup) {
        View viewFindFocus;
        if (viewGroup == null || (viewFindFocus = viewGroup.findFocus()) == null) {
            return;
        }
        this.mFocusViewRawY = 0;
        this.mIsFocusViewDisplayInVerticalScrolledView = false;
        this.mFocusVerticalScrolledView = null;
        if (isScrollable(viewFindFocus)) {
            this.mIsFocusViewDisplayInVerticalScrolledView = true;
            this.mFocusVerticalScrolledView = viewFindFocus;
        }
        this.mFocusViewRawY = getMeasureHeight(viewFindFocus) + viewFindFocus.getTop() + COUIViewMarginUtil.getMargin(viewFindFocus, 3);
        for (View view = (View) viewFindFocus.getParent(); view != null && view != viewGroup.getParent(); view = (View) view.getParent()) {
            if (isScrollable(view)) {
                this.mIsFocusViewDisplayInVerticalScrolledView = true;
                this.mFocusVerticalScrolledView = view;
            }
            this.mFocusViewRawY += view.getTop();
        }
    }

    private int getKeyboardHeightBeforeR(int i3, int i4) {
        return this.mWindowType == 2038 ? i3 : i3 - i4;
    }

    private int getMeasureHeight(View view) {
        if (view == null || view.getVisibility() == 8) {
            return 0;
        }
        int measuredHeight = view.getMeasuredHeight();
        if (measuredHeight != 0) {
            return measuredHeight;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        return view.getMeasuredHeight();
    }

    private boolean isScrollable(View view) {
        return (view instanceof ScrollView) || (view instanceof AbsListView) || (view instanceof ScrollingView);
    }

    private boolean updateAdjustKeyboardData(ViewGroup viewGroup, int i3) {
        if (viewGroup == null) {
            return false;
        }
        releaseData();
        if (viewGroup instanceof COUIPanelContentLayout) {
            COUIPanelContentLayout cOUIPanelContentLayout = (COUIPanelContentLayout) viewGroup;
            viewGroup.measure(View.MeasureSpec.makeMeasureSpec(viewGroup.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(cOUIPanelContentLayout.getMaxHeight(), cOUIPanelContentLayout.getLayoutAtMaxHeight() ? 1073741824 : Integer.MIN_VALUE));
            findFocusView(viewGroup);
        }
        int measuredHeight = viewGroup.getMeasuredHeight();
        this.mAdjustKeyboardStartHeight = measuredHeight;
        int i4 = this.mAdjustResizeType;
        if (i4 == 0) {
            this.mAdjustKeyboardHeight = i3;
            this.mAdjustKeyboardOffset = i3;
        } else if (i4 == 1) {
            this.mAdjustKeyboardStartHeight = measuredHeight - i3;
            this.mAdjustKeyboardOffset = i3 - this.mAdjustKeyboardHeight;
            this.mAdjustKeyboardHeight = i3;
        } else if (i4 == 2 && !this.mIsIgnoreHideKeyboardAnim) {
            this.mAdjustKeyboardHeight = i3;
            this.mAdjustKeyboardOffset = i3;
        }
        return true;
    }

    private void updateAdjustKeyboardOffset(ViewGroup viewGroup, Boolean bool) {
        this.mPaddingBottomAnimView = null;
        this.mPaddingBottomOffset = 0;
        this.mTranslateOffset = 0.0f;
        this.mMarginBottomValue = 0;
        if (viewGroup == null || this.mAdjustKeyboardOffset == 0) {
            return;
        }
        if (viewGroup instanceof COUIPanelContentLayout) {
            updateOffsetInConstraintLayout((COUIPanelContentLayout) viewGroup, bool);
        } else {
            updateOffsetInNormalLayout(viewGroup, bool);
        }
    }

    private void updateAdjustKeyboardType(boolean z2) {
        this.mAdjustResizeType = 2;
        boolean z3 = this.mIsKeyboardShow;
        if (!z3 && z2) {
            this.mAdjustResizeType = 0;
        } else if (z3 && z2) {
            this.mAdjustResizeType = 1;
        }
        this.mIsKeyboardShow = z2;
    }

    private void updateOffsetInConstraintLayout(COUIPanelContentLayout cOUIPanelContentLayout, Boolean bool) {
        int i3 = this.mAdjustResizeType == 2 ? -1 : 1;
        int maxHeight = cOUIPanelContentLayout.getMaxHeight();
        int i4 = this.mAdjustKeyboardOffset * i3;
        float translationY = cOUIPanelContentLayout.getBtnBarLayout() != null ? cOUIPanelContentLayout.getBtnBarLayout().getTranslationY() : 0.0f;
        this.mPaddingBottomAnimView = new WeakReference<>(cOUIPanelContentLayout);
        if ((this.mIsFocusViewDisplayInVerticalScrolledView && maxHeight != 0) || (!COUIPanelMultiWindowUtils.isPortrait(cOUIPanelContentLayout.getContext()) && translationY == 0.0f)) {
            View view = this.mFocusVerticalScrolledView;
            if (view != null) {
                View view2 = (View) view.getParent();
                if (view2 != null) {
                    this.mPaddingBottomAnimView = new WeakReference<>(view2);
                }
                this.mTranslateOffset = -i4;
            }
            this.mPaddingBottomOffset = i4;
            return;
        }
        int i5 = this.mAdjustKeyboardStartHeight - this.mFocusViewRawY;
        int paddingBottom = cOUIPanelContentLayout.getPaddingBottom();
        int height = cOUIPanelContentLayout.getBtnBarLayout() != null ? cOUIPanelContentLayout.getBtnBarLayout().getHeight() : 0;
        int height2 = cOUIPanelContentLayout.getDivider() != null ? cOUIPanelContentLayout.getDivider().getHeight() : 0;
        int i6 = this.mAdjustResizeType;
        if (i6 == 1) {
            i5 += this.mAdjustKeyboardHeight;
        } else if (i6 == 2) {
            i5 -= this.mAdjustKeyboardHeight;
        }
        int i7 = this.mAdjustKeyboardHeight;
        if (i5 >= i7 + height + height2 && paddingBottom == 0) {
            this.mTranslateOffset = -i4;
            return;
        }
        int i8 = ((i7 + height) + height2) - i5;
        int i9 = i3 * i8;
        this.mPaddingBottomOffset = Math.max(-paddingBottom, i9);
        if (this.mAdjustResizeType != 1) {
            this.mTranslateOffset = bool.booleanValue() ? -(i4 - i8) : -translationY;
            return;
        }
        int iMax = Math.max(0, paddingBottom + i9);
        int i10 = this.mAdjustKeyboardHeight;
        this.mTranslateOffset = (-Math.min(i10, Math.max(-i10, i10 - iMax))) - translationY;
    }

    private void updateOffsetInNormalLayout(ViewGroup viewGroup, Boolean bool) {
        int i3 = (this.mAdjustResizeType == 2 ? -1 : 1) * this.mAdjustKeyboardOffset;
        this.mPaddingBottomAnimView = new WeakReference<>(viewGroup);
        this.mMarginBottomValue = i3;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void adjustResize(Context context, ViewGroup viewGroup, WindowInsets windowInsets, View view, boolean z2) {
        if (viewGroup == null || !z2) {
            return;
        }
        int keyboardHeightBeforeR = getKeyboardHeightBeforeR(windowInsets.getSystemWindowInsetBottom(), (!COUINavigationBarUtil.isNavigationBarShow(context) || context.getResources().getBoolean(R.bool.is_ignore_nav_height_in_panel_ime_adjust)) ? 0 : COUINavigationBarUtil.getNavigationBarHeight(context));
        if (keyboardHeightBeforeR > 0) {
            adjustResizeBeforeR(viewGroup, true, keyboardHeightBeforeR);
            return;
        }
        if (this.mAdjustResizeType != 2) {
            adjustResizeBeforeR(viewGroup, false, this.mAdjustKeyboardHeight);
        }
        View viewFindViewById = view.findViewById(com.support.appcompat.R.id.design_bottom_sheet);
        int panelMarginBottom = COUIPanelMultiWindowUtils.getPanelMarginBottom(viewGroup.getContext(), viewGroup.getContext().getResources().getConfiguration(), windowInsets, viewFindViewById instanceof COUIPanelPercentFrameLayout ? ((COUIPanelPercentFrameLayout) viewFindViewById).isIsHandlePanel() : false, isCouiPanelEdgeToEdgeEnable());
        ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = panelMarginBottom;
            viewGroup.setLayoutParams(layoutParams);
        }
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public int getMarginBottomValue() {
        return this.mMarginBottomValue;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public int getPaddingBottomOffset() {
        return this.mPaddingBottomOffset;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public float getTranslateOffset() {
        return this.mTranslateOffset;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public int getWindowType() {
        return this.mWindowType;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void recoveryScrollingParentViewPaddingBottom(COUIPanelContentLayout cOUIPanelContentLayout) {
        if (cOUIPanelContentLayout != null) {
            COUIButtonBarLayout btnBarLayout = cOUIPanelContentLayout.getBtnBarLayout();
            View divider = cOUIPanelContentLayout.getDivider();
            if (btnBarLayout != null) {
                btnBarLayout.setTranslationY(0.0f);
            }
            if (divider != null) {
                divider.setTranslationY(0.0f);
            }
            cOUIPanelContentLayout.setPadding(0, 0, 0, 0);
        }
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public boolean releaseData() {
        ValueAnimator valueAnimator = this.mPaddingBottomAnim;
        boolean z2 = false;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.mPaddingBottomAnim.cancel();
                z2 = true;
            }
            this.mPaddingBottomAnim = null;
        }
        ValueAnimator valueAnimator2 = this.mBottomButtonBarAnim;
        if (valueAnimator2 != null) {
            if (valueAnimator2.isRunning()) {
                this.mBottomButtonBarAnim.cancel();
            }
            this.mBottomButtonBarAnim = null;
        }
        return z2;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void resetInnerStatus() {
        this.mAdjustKeyboardHeight = 0;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void setIgnoreHideKeyboardAnim(boolean z2) {
        this.mIsIgnoreHideKeyboardAnim = z2;
    }

    @Override // com.coui.appcompat.panel.COUIAbsPanelAdjustResizeHelper
    public void setWindowType(int i3) {
        this.mWindowType = i3;
    }
}