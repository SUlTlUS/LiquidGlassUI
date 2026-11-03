package com.coui.appcompat.dialog.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.core.widget.TextViewCompat;
import com.coui.appcompat.buttonBar.COUIButtonBarLayout;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.dialog.COUIAlertDialogOverlayHelper;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.statement.COUIMaxHeightScrollView;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusOutline;
import com.oplus.graphics.OplusOutlineAdapter;
import com.support.dialog.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIAlertDialogMaxLinearLayout extends LinearLayout {
    private static final int NO_ID = -1;
    private static final float TWO_FLOAT = 2.0f;
    private static final int TWO_INT = 2;
    private static final int UNDEFINED_ATTR = -1;
    private final String TAG;
    private int mAdjustDialogPaddingBottom;
    private boolean mAiPaymentTextStyleEnabled;
    private boolean mBlurBackgroundWindow;
    private COUIButtonBarLayout mButtonPanel;
    private ViewGroup.LayoutParams mButtonPanelOriginalParams;
    private COUIMaxHeightNestedScrollView mButtonPanelScrollView;
    private ViewGroup mContentPanelLayout;
    private int mCouiBottomAlertDialogButtonbarMargintop;
    private int mCustomDialogPaddingBottom;
    private int mCustomMarginExtra;
    private FrameLayout mCustomPanelLayout;
    private FrameLayout mCustomView;
    private int mCustomViewHeight;
    private int mCustomViewMarginStart;
    private int mCustomViewWidth;
    private int mDialogContentPanelLayoutMinHeight;
    private int mDialogCustomViewMinHeight;
    private int mDialogLayoutMarginBottom;
    private int mDialogLayoutMarginBottomWithNavigationBar;
    private int mDialogLayoutMarginHorizontal;
    private int mDialogLayoutMarginTopFreeForm;
    private int mDialogLayoutMarginVerticalTotal;
    private COUIAlertDialogMessageView mDialogMessage;
    private View mDialogOverlay;
    private COUIDialogTitle mDialogTitle;
    private boolean mForceSplitAfterConfigurationChange;
    private boolean mHasLoading;
    private boolean mHasMessageMerge;
    private boolean mIsMergedToTitle;
    private boolean mIsSupportSmoothRoundCorner;
    private Boolean mIsThemeBitmapBackground;
    private boolean mIsTiny;
    private int mLastSetMarginBottomMaxHeightDelta;
    private LinearLayout mLinearLayoutTitle;
    private ViewGroup mListPanel;
    private int mListPanelMarginStart;
    private int mMaxHeight;
    private int mMaxWidth;
    private int mMergeAndSplitCriticalHeight;
    private int mMessagePaddingBottomWhenDialogTallDialog;
    private int mMessagePaddingEnd;
    private int mMessagePaddingStart;
    private int mMessagePaddingTopWhenDialogIsTallDialog;
    private boolean mNeedResetButtomBarTopMargin;
    private boolean mNeedSetMarginTop;
    private int mNeedSetPaddingLayoutId;
    private boolean mNeedsRemeasureAfterSplit;
    private int mOriginalScrollViewTitleMaxHeight;
    private int mOriginalScrollViewTitleMinHeight;
    private int mOriginalTitleTemplateMarginBottom;
    private int mOriginalTitleTemplateMarginEnd;
    private int mOriginalTitleTemplateMarginStart;
    private int mOriginalTitleTemplateMarginTop;
    private int mOriginalTopPanelMarginBottom;
    private COUIAlertDialogOverlayHelper mOverlayHelper;
    private int mRadius;
    private View mRootView;
    private Drawable mSavedInsetInnerDrawable;
    private int mScrollPaddingBottomMessageHasTitleInAssignment;
    private int mScrollPaddingBottomMessageTallDialog;
    private COUIMaxHeightNestedScrollView mScrollViewMessage;
    private COUIMaxHeightScrollView mScrollViewTitle;
    private int mScrollViewTitleOriginalMaxHeight;
    private View mSpacingViewForCustomView;
    private View mSpacingViewForListPanel;
    private View mSpacingViewForMessage;
    private boolean mSupportDynamicMarginTop;
    private final Rect mTempRect;
    private View mTitleTemplate;
    private View mTopPanelLayout;
    private boolean mUseBackgroundBlurFeature;

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.widget.COUIAlertDialogMaxLinearLayout$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            COUIAlertDialogMaxLinearLayout.access$000(COUIAlertDialogMaxLinearLayout.this, outline);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.widget.COUIAlertDialogMaxLinearLayout$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnApplyWindowInsetsListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout = COUIAlertDialogMaxLinearLayout.this;
            COUIAlertDialogMaxLinearLayout.access$200(cOUIAlertDialogMaxLinearLayout, COUIAlertDialogMaxLinearLayout.access$100(cOUIAlertDialogMaxLinearLayout));
            return windowInsets;
        }
    }

    public interface OnSizeChangeListener {
        void onSizeChange(int i3, int i4, int i5, int i6);
    }

    public COUIAlertDialogMaxLinearLayout(Context context) {
        super(context);
        this.TAG = "DialogMaxLinearLayout";
        this.mTempRect = new Rect();
        this.mNeedSetPaddingLayoutId = -1;
        this.mHasMessageMerge = true;
        this.mCustomMarginExtra = 5;
        this.mScrollViewTitleOriginalMaxHeight = -1;
        this.mIsMergedToTitle = false;
        this.mOriginalScrollViewTitleMaxHeight = -1;
        this.mOriginalScrollViewTitleMinHeight = -1;
        this.mOriginalTitleTemplateMarginTop = -1;
        this.mOriginalTitleTemplateMarginBottom = -1;
        this.mOriginalTitleTemplateMarginStart = -1;
        this.mOriginalTitleTemplateMarginEnd = -1;
        this.mOriginalTopPanelMarginBottom = -1;
        this.mBlurBackgroundWindow = false;
        this.mIsSupportSmoothRoundCorner = false;
        this.mHasLoading = false;
        this.mIsTiny = false;
        this.mSupportDynamicMarginTop = false;
    }

    public static /* synthetic */ void access$000(COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout, Outline outline) {
        cOUIAlertDialogMaxLinearLayout.setOutLineProviderInternal(outline);
    }

    public static /* synthetic */ boolean access$100(COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout) {
        return cOUIAlertDialogMaxLinearLayout.mSupportDynamicMarginTop;
    }

    public static /* synthetic */ void access$200(COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout, boolean z2) {
        cOUIAlertDialogMaxLinearLayout.updatePadding(z2);
    }

    private void adjustMaxHeightOfScrollViewTitle(boolean z2, boolean z3, boolean z4, int i3, boolean z5) {
        COUIMaxHeightScrollView cOUIMaxHeightScrollView = this.mScrollViewTitle;
        if (cOUIMaxHeightScrollView == null) {
            return;
        }
        if (z3 || z2 || z4 || z5) {
            if (this.mScrollViewTitleOriginalMaxHeight == -1) {
                this.mScrollViewTitleOriginalMaxHeight = cOUIMaxHeightScrollView.getMaxHeight();
            }
            int i4 = this.mMaxHeight;
            if (i4 > 0) {
                i3 = Math.min(i4, i3 + this.mDialogLayoutMarginVerticalTotal) - this.mDialogLayoutMarginVerticalTotal;
            }
            COUIButtonBarLayout cOUIButtonBarLayout = this.mButtonPanel;
            int measuredHeight = i3 - ((cOUIButtonBarLayout == null || z5) ? 0 : cOUIButtonBarLayout.getMeasuredHeight());
            if (this.mScrollViewTitle.getMaxHeight() == measuredHeight || measuredHeight <= 0) {
                return;
            }
            this.mScrollViewTitle.setMaxHeight(measuredHeight);
        }
    }

    private void applyAiPaymentTextStyle() {
        if (this.mAiPaymentTextStyleEnabled) {
            if (this.mDialogTitle == null) {
                this.mDialogTitle = (COUIDialogTitle) findViewById(R.id.alertTitle);
            }
            if (this.mDialogMessage == null) {
                this.mDialogMessage = (COUIAlertDialogMessageView) findViewById(android.R.id.message);
            }
            COUIDialogTitle cOUIDialogTitle = this.mDialogTitle;
            if (cOUIDialogTitle != null) {
                TextViewCompat.setTextAppearance(cOUIDialogTitle, this.mIsTiny ? R.style.COUIDialogTextAppearance_Title_Tiny_AiPayment : R.style.COUIDialogTextAppearance_Title_AiPayment);
            }
            COUIAlertDialogMessageView cOUIAlertDialogMessageView = this.mDialogMessage;
            if (cOUIAlertDialogMessageView != null) {
                cOUIAlertDialogMessageView.setTextColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorAiPaymentDialogMessage));
            }
        }
    }

    private void applyTransparentInsetInnerBackground() {
        if (getBackground() instanceof InsetDrawable) {
            InsetDrawable insetDrawable = (InsetDrawable) getBackground();
            if (this.mSavedInsetInnerDrawable == null) {
                this.mSavedInsetInnerDrawable = insetDrawable.getDrawable();
            }
            insetDrawable.getPadding(this.mTempRect);
            ColorDrawable colorDrawable = new ColorDrawable(0);
            Rect rect = this.mTempRect;
            setBackground(new InsetDrawable((Drawable) colorDrawable, rect.left, rect.top, rect.right, rect.bottom));
        }
    }

    private int calculateMergeAndSplitCriticalHeight(boolean z2, boolean z3, boolean z4, boolean z5, int i3) {
        COUIButtonBarLayout cOUIButtonBarLayout;
        int customViewContentHeight;
        ViewGroup viewGroup;
        View childAt;
        View view = this.mTopPanelLayout;
        int measuredHeight = view != null ? view.getMeasuredHeight() : 0;
        int iResolveCriticalHeightAvailableWidth = resolveCriticalHeightAvailableWidth(i3);
        if (z3 && this.mListPanel != null && iResolveCriticalHeightAvailableWidth > 0) {
            this.mListPanel.measure(View.MeasureSpec.makeMeasureSpec(iResolveCriticalHeightAvailableWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
        }
        if (z2) {
            COUIMaxHeightNestedScrollView cOUIMaxHeightNestedScrollView = this.mScrollViewMessage;
            if (cOUIMaxHeightNestedScrollView == null || cOUIMaxHeightNestedScrollView.getChildCount() <= 0) {
                childAt = this.mDialogMessage;
                if (childAt == null) {
                    childAt = null;
                }
            } else {
                childAt = this.mScrollViewMessage.getChildAt(0);
            }
            if (childAt != null) {
                measuredHeight += childAt.getMeasuredHeight();
            }
        }
        if (z3 && (viewGroup = this.mListPanel) != null) {
            measuredHeight += viewGroup.getMeasuredHeight();
        }
        if (z4 && this.mCustomView != null && (customViewContentHeight = getCustomViewContentHeight(iResolveCriticalHeightAvailableWidth)) > 0) {
            measuredHeight += customViewContentHeight;
        }
        return (!z5 || (cOUIButtonBarLayout = this.mButtonPanel) == null || cOUIButtonBarLayout.getMeasuredHeight() <= 0) ? measuredHeight : measuredHeight + this.mButtonPanel.getMeasuredHeight();
    }

    private void clearTitleTemplateMargin() {
        View view = this.mTitleTemplate;
        if (view != null && (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mTitleTemplate.getLayoutParams();
            marginLayoutParams.topMargin = 0;
            marginLayoutParams.bottomMargin = 0;
            marginLayoutParams.setMarginStart(0);
            marginLayoutParams.setMarginEnd(0);
            this.mTitleTemplate.setLayoutParams(marginLayoutParams);
        }
        View view2 = this.mTopPanelLayout;
        if (view2 == null || !(view2.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mTopPanelLayout.getLayoutParams();
        this.mOriginalTopPanelMarginBottom = marginLayoutParams2.bottomMargin;
        marginLayoutParams2.bottomMargin = 0;
        this.mTopPanelLayout.setLayoutParams(marginLayoutParams2);
    }

    private void dispatchBlurChromeToRootLayout(boolean z2) {
        ensureRootView();
        View view = this.mRootView;
        if (view instanceof COUIAlertDialogRootLayout) {
            COUIAlertDialogRootLayout cOUIAlertDialogRootLayout = (COUIAlertDialogRootLayout) view;
            cOUIAlertDialogRootLayout.setShadowEdgeCornerInfo(this.mRadius, this.mIsTiny);
            cOUIAlertDialogRootLayout.setBlurBackgroundWindow(z2);
        }
    }

    private void ensureDialogOverlayView() {
        if (this.mRootView == null) {
            this.mRootView = findViewById(R.id.rootView);
        }
        View view = this.mRootView;
        if (view == null || this.mDialogOverlay != null) {
            return;
        }
        View viewFindViewById = view.findViewById(R.id.dialogOverlay);
        this.mDialogOverlay = viewFindViewById;
        if (viewFindViewById instanceof ViewStub) {
            this.mDialogOverlay = ((ViewStub) viewFindViewById).inflate();
        }
        View view2 = this.mDialogOverlay;
        if (view2 != null) {
            view2.setClickable(false);
            this.mDialogOverlay.setFocusable(false);
            this.mDialogOverlay.setImportantForAccessibility(2);
        }
    }

    private void ensureRootView() {
        if (this.mRootView == null) {
            this.mRootView = findViewById(R.id.rootView);
        }
    }

    private int expandMeasureSpecToTarget(int i3, int i4) {
        if (i4 > 0) {
            int mode = View.MeasureSpec.getMode(i3);
            int size = View.MeasureSpec.getSize(i3);
            if (mode != 1073741824) {
                if (mode == 0) {
                    return View.MeasureSpec.makeMeasureSpec(i4, Integer.MIN_VALUE);
                }
                if (size <= 0) {
                    size = i4;
                }
                return View.MeasureSpec.makeMeasureSpec(Math.min(i4, size), Integer.MIN_VALUE);
            }
        }
        return i3;
    }

    private int getCustomViewContentHeight(int i3) {
        FrameLayout frameLayout = this.mCustomView;
        if (frameLayout == null) {
            return 0;
        }
        return Math.max(remeasureWrapContentHeight(frameLayout, i3), measureScrollViewDescendantContentHeight(this.mCustomView, i3));
    }

    private int getEffectiveBottomMargin() {
        if (this.mIsTiny) {
            return 0;
        }
        int i3 = this.mCustomDialogPaddingBottom;
        if (i3 > 0) {
            return i3;
        }
        int i4 = this.mAdjustDialogPaddingBottom;
        return i4 > 0 ? i4 : this.mDialogLayoutMarginBottom;
    }

    private int getHeightWithVerticalMargin(View view) {
        if (view == null) {
            return 0;
        }
        int measuredHeight = view.getMeasuredHeight();
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            return measuredHeight;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return measuredHeight + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private int getMergedDialogTargetHeight(int i3) {
        int i4 = i3 + this.mDialogLayoutMarginVerticalTotal;
        int i5 = this.mMaxHeight;
        return i5 > 0 ? Math.min(i5, i4) : i4;
    }

    private int getStatusBarHeightForTopMargin() {
        return this.mDialogLayoutMarginTopFreeForm;
    }

    private void layoutDialogOverlay() {
        View view = this.mDialogOverlay;
        if (view == null || this.mRootView == null || view.getVisibility() != 0) {
            return;
        }
        int width = this.mRootView.getWidth();
        int height = this.mRootView.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        this.mDialogOverlay.layout(0, 0, width, height);
    }

    private int maxMeasureSpec(int i3, int i4) {
        return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i3), i4), View.MeasureSpec.getMode(i3));
    }

    private int measureScrollViewChildContentHeight(ViewGroup viewGroup, int i3) {
        if (viewGroup.getChildCount() <= 0) {
            return 0;
        }
        View childAt = viewGroup.getChildAt(0);
        int paddingLeft = (i3 - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight();
        ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
        boolean z2 = layoutParams instanceof ViewGroup.MarginLayoutParams;
        if (z2) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            paddingLeft -= marginLayoutParams.getMarginStart() + marginLayoutParams.getMarginEnd();
        }
        if (paddingLeft > 0) {
            childAt.measure(View.MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        } else if (childAt.getMeasuredHeight() <= 0) {
            return 0;
        }
        int measuredHeight = childAt.getMeasuredHeight();
        if (z2) {
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams;
            measuredHeight += marginLayoutParams2.topMargin + marginLayoutParams2.bottomMargin;
        }
        return measuredHeight + viewGroup.getPaddingTop() + viewGroup.getPaddingBottom();
    }

    private int measureScrollViewDescendantContentHeight(View view, int i3) {
        if ((view instanceof ScrollView) || (view instanceof NestedScrollView)) {
            return measureScrollViewChildContentHeight((ViewGroup) view, i3);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i4 = 0; i4 < viewGroup.getChildCount(); i4++) {
                int iMeasureScrollViewDescendantContentHeight = measureScrollViewDescendantContentHeight(viewGroup.getChildAt(i4), i3);
                if (iMeasureScrollViewDescendantContentHeight > 0) {
                    return iMeasureScrollViewDescendantContentHeight;
                }
            }
        }
        return 0;
    }

    private void refreshOutlineClipForTheme() {
        if (!isAttachedToWindow() || this.mRadius <= 0) {
            return;
        }
        setClipToOutline(!shouldSkipOutlineClipForTheme());
        invalidateOutline();
    }

    private int remeasureWrapContentHeight(View view, int i3) {
        if (view != null && i3 > 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
            return view.getMeasuredHeight();
        }
        if (view != null) {
            return view.getMeasuredHeight();
        }
        return 0;
    }

    private int resolveAvailableHeight() {
        getWindowVisibleDisplayFrame(this.mTempRect);
        int iHeight = this.mTempRect.height();
        int i3 = this.mDialogLayoutMarginVerticalTotal;
        int i4 = iHeight - i3;
        int i5 = this.mMaxHeight - i3;
        if (i5 > 0 && i4 > 0) {
            return Math.min(i4, i5);
        }
        Activity activityContextToActivity = UIUtil.contextToActivity(getContext());
        if (activityContextToActivity != null) {
            activityContextToActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(this.mTempRect);
            int iHeight2 = this.mTempRect.height() - this.mDialogLayoutMarginVerticalTotal;
            if (i5 > 0 && iHeight2 > 0) {
                return Math.min(iHeight2, i5);
            }
        }
        int i6 = getResources().getDisplayMetrics().heightPixels - this.mDialogLayoutMarginVerticalTotal;
        return (i6 <= 0 || i5 <= 0) ? i5 : Math.min(i6, i5);
    }

    private int resolveCriticalHeightAvailableWidth(int i3) {
        int measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
        return measuredWidth <= 0 ? (View.MeasureSpec.getSize(i3) - getPaddingLeft()) - getPaddingRight() : measuredWidth;
    }

    private int resolveOutlineViewHeight() {
        int height = getHeight();
        return height > 0 ? height : getMeasuredHeight();
    }

    private int resolveOutlineViewWidth() {
        int width = getWidth();
        return width > 0 ? width : getMeasuredWidth();
    }

    private void restoreInsetInnerBackground() {
        if (this.mSavedInsetInnerDrawable == null) {
            return;
        }
        if (!(getBackground() instanceof InsetDrawable)) {
            this.mSavedInsetInnerDrawable = null;
            return;
        }
        ((InsetDrawable) getBackground()).getPadding(this.mTempRect);
        Drawable drawable = this.mSavedInsetInnerDrawable;
        Rect rect = this.mTempRect;
        setBackground(new InsetDrawable(drawable, rect.left, rect.top, rect.right, rect.bottom));
        this.mSavedInsetInnerDrawable = null;
    }

    private void restoreTitleTemplateMargin() {
        View view = this.mTitleTemplate;
        if (view != null && (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mTitleTemplate.getLayoutParams();
            int i3 = this.mOriginalTitleTemplateMarginTop;
            if (i3 >= 0) {
                marginLayoutParams.topMargin = i3;
            }
            int i4 = this.mOriginalTitleTemplateMarginBottom;
            if (i4 >= 0) {
                marginLayoutParams.bottomMargin = i4;
            }
            int i5 = this.mOriginalTitleTemplateMarginStart;
            if (i5 >= 0) {
                marginLayoutParams.setMarginStart(i5);
            }
            int i6 = this.mOriginalTitleTemplateMarginEnd;
            if (i6 >= 0) {
                marginLayoutParams.setMarginEnd(i6);
            }
            this.mTitleTemplate.setLayoutParams(marginLayoutParams);
        }
        View view2 = this.mTopPanelLayout;
        if (view2 == null || !(view2.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mTopPanelLayout.getLayoutParams();
        int i7 = this.mOriginalTopPanelMarginBottom;
        if (i7 > 0) {
            marginLayoutParams2.bottomMargin = i7;
            this.mTopPanelLayout.setLayoutParams(marginLayoutParams2);
        }
    }

    private void setMarginBottom() {
        if (getBackground() instanceof InsetDrawable) {
            int effectiveBottomMargin = getEffectiveBottomMargin();
            if (this.mIsTiny || effectiveBottomMargin > 0) {
                InsetDrawable insetDrawable = (InsetDrawable) getBackground();
                insetDrawable.getPadding(this.mTempRect);
                this.mTempRect.bottom = effectiveBottomMargin;
                int i3 = effectiveBottomMargin - this.mDialogLayoutMarginBottom;
                this.mMaxHeight = (this.mMaxHeight - this.mLastSetMarginBottomMaxHeightDelta) + i3;
                this.mLastSetMarginBottomMaxHeightDelta = i3;
                COUILog.d("DialogMaxLinearLayout", "refresh paddingBottom:" + this.mTempRect.top);
                Drawable drawable = insetDrawable.getDrawable();
                Rect rect = this.mTempRect;
                setBackground(new InsetDrawable(drawable, rect.left, rect.top, rect.right, rect.bottom));
                Rect rect2 = this.mTempRect;
                this.mDialogLayoutMarginVerticalTotal = rect2.top + rect2.bottom;
            }
        }
    }

    private void setMarginTop() {
        Insets insets;
        boolean zIsInFreeFormModeWindowMode = UIUtil.isInFreeFormModeWindowMode(getContext());
        Activity activityContextToActivity = UIUtil.contextToActivity(getContext());
        boolean zIsVisible = true;
        if (activityContextToActivity != null) {
            boolean z2 = (activityContextToActivity.getWindow().getDecorView().getRootWindowInsets() == null || (insets = activityContextToActivity.getWindow().getDecorView().getRootWindowInsets().getInsets(WindowInsetsCompat.Type.statusBars())) == null || insets.top != 0) ? zIsInFreeFormModeWindowMode : true;
            boolean z3 = (activityContextToActivity.getWindow().getAttributes().flags & 1024) == 1024;
            boolean z4 = (activityContextToActivity.getWindow().getDecorView().getSystemUiVisibility() & 4) == 4;
            if (!z3 && !z4) {
                zIsVisible = z2;
            }
        } else {
            zIsVisible = getRootView().getRootWindowInsets() != null ? true ^ getRootView().getRootWindowInsets().isVisible(WindowInsetsCompat.Type.statusBars()) : zIsInFreeFormModeWindowMode;
        }
        if (this.mNeedSetMarginTop != zIsVisible && (getBackground() instanceof InsetDrawable)) {
            InsetDrawable insetDrawable = (InsetDrawable) getBackground();
            insetDrawable.getPadding(this.mTempRect);
            if (zIsVisible) {
                int statusBarHeightForTopMargin = zIsInFreeFormModeWindowMode ? getStatusBarHeightForTopMargin() : UIUtil.getStatusBarHeight(getContext());
                this.mMaxHeight += statusBarHeightForTopMargin;
                this.mTempRect.top = statusBarHeightForTopMargin;
            } else {
                int i3 = this.mMaxHeight;
                Rect rect = this.mTempRect;
                this.mMaxHeight = i3 - rect.top;
                rect.top = 0;
            }
            COUILog.d("DialogMaxLinearLayout", "refresh paddingTop:" + this.mTempRect.top);
            Drawable drawable = insetDrawable.getDrawable();
            Rect rect2 = this.mTempRect;
            setBackground(new InsetDrawable(drawable, rect2.left, rect2.top, rect2.right, rect2.bottom));
            Rect rect3 = this.mTempRect;
            this.mDialogLayoutMarginVerticalTotal = rect3.top + rect3.bottom;
        }
        this.mNeedSetMarginTop = zIsVisible;
    }

    private void setOnApplyWindowInsetsListener(boolean z2) {
        Activity activityContextToActivity = UIUtil.contextToActivity(getContext());
        if (activityContextToActivity == null) {
            return;
        }
        ViewGroup viewGroup = (activityContextToActivity.getWindow() == null || !(activityContextToActivity.getWindow().getDecorView() instanceof ViewGroup)) ? null : (ViewGroup) activityContextToActivity.getWindow().getDecorView();
        if (viewGroup == null) {
            Log.e("DialogMaxLinearLayout", "setOnApplyWindowInsetsListener rootViewGroup == null");
            return;
        }
        View childAt = viewGroup.getChildAt(0);
        if (childAt == null) {
            Log.e("DialogMaxLinearLayout", "setOnApplyWindowInsetsListener decor == null");
        } else if (z2) {
            childAt.setOnApplyWindowInsetsListener(new AnonymousClass2());
        } else {
            childAt.setOnApplyWindowInsetsListener(null);
        }
    }

    private void setOutLineProvider() {
        if (this.mRadius == -1) {
            if (!this.mIsSupportSmoothRoundCorner) {
                this.mRadius = COUIContextUtil.getAttrDimens(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXXL);
            } else if (useOs15RoundCorner()) {
                this.mRadius = COUIContextUtil.getAttrDimens(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXXLRadius);
            } else if (RoundCornerUtil.getSmoothStyleType() != 1 || COUIVersionUtil.getOSVersionCode() <= 37) {
                this.mRadius = COUIContextUtil.getAttrDimens(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXXL);
            } else {
                this.mRadius = getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R.dimen.coui_round_corner_xxxxl_radius_large_16_1);
            }
        }
        if (this.mRadius > 0) {
            setClipToOutline(!shouldSkipOutlineClipForTheme());
            setOutlineProvider(new AnonymousClass1());
        }
        syncRootShadowEdgeCornerInfo();
    }

    private void setOutLineProviderInternal(Outline outline) {
        int i3;
        int effectiveBottomMargin = this.mDialogLayoutMarginVerticalTotal - getEffectiveBottomMargin();
        int iResolveOutlineViewWidth = resolveOutlineViewWidth();
        int iResolveOutlineViewHeight = resolveOutlineViewHeight();
        boolean z2 = this.mHasLoading;
        if (z2 || this.mIsTiny) {
            if (this.mIsTiny) {
                iResolveOutlineViewHeight += this.mRadius;
            } else if (z2) {
                iResolveOutlineViewHeight = Math.max(0, iResolveOutlineViewHeight - this.mDialogLayoutMarginVerticalTotal);
            }
            i3 = 0;
        } else {
            int i4 = this.mDialogLayoutMarginHorizontal;
            iResolveOutlineViewWidth -= i4 * 2;
            iResolveOutlineViewHeight -= this.mDialogLayoutMarginVerticalTotal;
            i3 = i4;
        }
        if (!RoundCornerUtil.supportSRCCompatibleBlur(this.mBlurBackgroundWindow)) {
            outline.setRoundRect(i3, effectiveBottomMargin, i3 + iResolveOutlineViewWidth, effectiveBottomMargin + iResolveOutlineViewHeight, this.mRadius);
        } else if (useOs15RoundCorner()) {
            new OplusOutline(outline).setSmoothRoundRect(i3, effectiveBottomMargin, i3 + iResolveOutlineViewWidth, effectiveBottomMargin + iResolveOutlineViewHeight, this.mRadius, COUIContextUtil.getFloat(getContext(), com.support.appcompat.R.dimen.coui_round_corner_xxxl_weight));
        } else if (RoundCornerUtil.getSmoothStyleType() == 1 && this.mIsSupportSmoothRoundCorner) {
            OplusOutlineAdapter oplusOutlineAdapter = new OplusOutlineAdapter(outline, RoundCornerUtil.getSmoothStyleType());
            if (COUIVersionUtil.getOSVersionCode() > 37) {
                oplusOutlineAdapter.setSmoothRoundRect(i3, effectiveBottomMargin, i3 + iResolveOutlineViewWidth, effectiveBottomMargin + iResolveOutlineViewHeight, this.mRadius, getContext().getResources().getFloat(com.support.appcompat.R.dimen.weight_16_1_large));
            } else {
                oplusOutlineAdapter.setSmoothRoundRect(i3, effectiveBottomMargin, i3 + iResolveOutlineViewWidth, effectiveBottomMargin + iResolveOutlineViewHeight, this.mRadius);
            }
        } else {
            outline.setRoundRect(i3, effectiveBottomMargin, i3 + iResolveOutlineViewWidth, effectiveBottomMargin + iResolveOutlineViewHeight, this.mRadius);
        }
        COUILog.i("DialogMaxLinearLayout", "getOutline: mBlurBackgroundWindow = " + this.mBlurBackgroundWindow + " isSupportRoundCornerWhenBlur=" + RoundCornerUtil.isSupportRoundCornerWhenBlur() + " mIsSupportSmoothRoundCorner=" + this.mIsSupportSmoothRoundCorner + " mRadius=" + this.mRadius);
    }

    private void syncRootShadowEdgeCornerInfo() {
        ensureRootView();
        View view = this.mRootView;
        if (view instanceof COUIAlertDialogRootLayout) {
            ((COUIAlertDialogRootLayout) view).setShadowEdgeCornerInfo(this.mRadius, this.mIsTiny);
        }
    }

    private void updateDialogBottomMargin() {
        if (this.mIsTiny) {
            this.mAdjustDialogPaddingBottom = 0;
            return;
        }
        if (this.mCustomDialogPaddingBottom > 0) {
            return;
        }
        if (UIUtil.isInFreeFormModeWindowMode(getContext())) {
            this.mAdjustDialogPaddingBottom = this.mDialogLayoutMarginBottom;
            return;
        }
        Context context = getContext();
        if (context == null) {
            return;
        }
        boolean zIsGestureNavigation = UIUtil.isGestureNavigation(context);
        int navigationBarHeightFromActivity = UIUtil.getNavigationBarHeightFromActivity(getContext());
        if (zIsGestureNavigation) {
            if (navigationBarHeightFromActivity <= 0) {
                this.mAdjustDialogPaddingBottom = 0;
                return;
            } else {
                this.mAdjustDialogPaddingBottom = Math.max(0, this.mDialogLayoutMarginBottom - navigationBarHeightFromActivity);
                return;
            }
        }
        if (navigationBarHeightFromActivity <= 0) {
            this.mAdjustDialogPaddingBottom = this.mDialogLayoutMarginBottom;
        } else {
            this.mAdjustDialogPaddingBottom = this.mDialogLayoutMarginBottomWithNavigationBar - navigationBarHeightFromActivity;
        }
    }

    private void updatePadding(boolean z2) {
        updateDialogBottomMargin();
        setMarginBottom();
        if (z2) {
            setMarginTop();
        }
        setOutLineProvider();
    }

    private boolean useOs15RoundCorner() {
        if (RoundCornerUtil.getSmoothStyleType() != 0) {
            return this.mBlurBackgroundWindow && COUIVersionUtil.getOSVersionCode() == 37;
        }
        return true;
    }

    public void bindDialogOverlay(COUIAlertDialogOverlayHelper cOUIAlertDialogOverlayHelper) {
        this.mOverlayHelper = cOUIAlertDialogOverlayHelper;
        ensureDialogOverlayView();
    }

    public void clearParentPanelBackgroundKeepInsets() {
        Drawable background = getBackground();
        if (!(background instanceof InsetDrawable)) {
            setBackground(null);
            return;
        }
        ((InsetDrawable) background).getPadding(this.mTempRect);
        ColorDrawable colorDrawable = new ColorDrawable(0);
        Rect rect = this.mTempRect;
        setBackground(new InsetDrawable((Drawable) colorDrawable, rect.left, rect.top, rect.right, rect.bottom));
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        setOnApplyWindowInsetsListener(true);
        updatePadding(true);
        super.onAttachedToWindow();
        syncRootShadowEdgeCornerInfo();
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mForceSplitAfterConfigurationChange = true;
        this.mScrollViewTitleOriginalMaxHeight = -1;
        this.mOriginalScrollViewTitleMaxHeight = -1;
        this.mOriginalScrollViewTitleMinHeight = -1;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        setOnApplyWindowInsetsListener(false);
        this.mIsMergedToTitle = false;
        this.mNeedsRemeasureAfterSplit = false;
        this.mOriginalScrollViewTitleMaxHeight = -1;
        this.mOriginalScrollViewTitleMinHeight = -1;
        super.onDetachedFromWindow();
        restoreInsetInnerBackground();
        this.mUseBackgroundBlurFeature = false;
        this.mOverlayHelper = null;
        this.mDialogOverlay = null;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        super.onLayout(z2, i3, i4, i5, i6);
        layoutDialogOverlay();
    }

    /* JADX WARN: Code duplicated, block: B:356:0x0624  */
    /* JADX WARN: Code duplicated, block: B:360:0x062f  */
    /* JADX WARN: Code duplicated, block: B:365:0x063d  */
    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        COUIMaxHeightScrollView cOUIMaxHeightScrollView;
        int lineCount;
        int lineCount2;
        int i5;
        boolean z2;
        COUIButtonBarLayout cOUIButtonBarLayout;
        boolean z3;
        COUIButtonBarLayout cOUIButtonBarLayout2;
        COUIButtonBarLayout cOUIButtonBarLayout3;
        int i6;
        int i7;
        COUIAlertDialogMessageView cOUIAlertDialogMessageView;
        FrameLayout frameLayout;
        ViewGroup viewGroup;
        COUIButtonBarLayout cOUIButtonBarLayout4;
        COUIAlertDialogMessageView cOUIAlertDialogMessageView2;
        View viewFindViewById;
        int i8 = this.mMaxWidth;
        int iMaxMeasureSpec = i8 > 0 ? maxMeasureSpec(i3, i8) : i3;
        int i9 = this.mMaxHeight;
        int iMaxMeasureSpec2 = i9 > 0 ? maxMeasureSpec(i4, i9) : i4;
        super.onMeasure(iMaxMeasureSpec, iMaxMeasureSpec2);
        int measuredHeight = getMeasuredHeight();
        if (this.mScrollViewTitle == null) {
            try {
                this.mRootView = findViewById(R.id.rootView);
                this.mTopPanelLayout = findViewById(R.id.topPanel);
                this.mCustomPanelLayout = (FrameLayout) findViewById(R.id.customPanel);
                this.mCustomView = (FrameLayout) findViewById(R.id.custom);
                this.mContentPanelLayout = (ViewGroup) findViewById(R.id.contentPanel);
                this.mListPanel = (ViewGroup) findViewById(R.id.listPanel);
                this.mDialogTitle = (COUIDialogTitle) findViewById(R.id.alertTitle);
                this.mDialogMessage = (COUIAlertDialogMessageView) findViewById(android.R.id.message);
                this.mScrollViewMessage = (COUIMaxHeightNestedScrollView) findViewById(R.id.scrollView);
                COUIMaxHeightScrollView cOUIMaxHeightScrollView2 = (COUIMaxHeightScrollView) findViewById(R.id.alert_title_scroll_view);
                this.mScrollViewTitle = cOUIMaxHeightScrollView2;
                if (this.mOriginalScrollViewTitleMaxHeight < 0 && cOUIMaxHeightScrollView2 != null) {
                    this.mOriginalScrollViewTitleMaxHeight = cOUIMaxHeightScrollView2.getMaxHeight();
                }
                if (this.mOriginalScrollViewTitleMinHeight < 0 && (cOUIMaxHeightScrollView = this.mScrollViewTitle) != null) {
                    this.mOriginalScrollViewTitleMinHeight = cOUIMaxHeightScrollView.getMinHeight();
                }
                this.mButtonPanel = (COUIButtonBarLayout) findViewById(R.id.buttonPanel);
                View viewFindViewById2 = findViewById(R.id.title_template);
                this.mTitleTemplate = viewFindViewById2;
                if (viewFindViewById2 != null && (viewFindViewById2.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mTitleTemplate.getLayoutParams();
                    this.mOriginalTitleTemplateMarginTop = marginLayoutParams.topMargin;
                    this.mOriginalTitleTemplateMarginBottom = marginLayoutParams.bottomMargin;
                    this.mOriginalTitleTemplateMarginStart = marginLayoutParams.getMarginStart();
                    this.mOriginalTitleTemplateMarginEnd = marginLayoutParams.getMarginEnd();
                }
            } catch (Exception e3) {
                Log.e("DialogMaxLinearLayout", "Failed to get type conversion. message e:" + e3.getMessage());
                this.mHasMessageMerge = false;
                this.mForceSplitAfterConfigurationChange = false;
                return;
            }
        }
        applyAiPaymentTextStyle();
        if (this.mDialogMessage != null) {
            lineCount = this.mDialogMessage.getLineCount();
            lineCount2 = this.mDialogTitle.getLineCount();
        } else {
            lineCount = 0;
            lineCount2 = 0;
        }
        int iResolveAvailableHeight = resolveAvailableHeight();
        int i10 = measuredHeight - this.mDialogLayoutMarginVerticalTotal;
        if (this.mNeedSetPaddingLayoutId != -1) {
            boolean z4 = lineCount2 > 1;
            boolean z5 = lineCount > 1;
            COUIButtonBarLayout cOUIButtonBarLayout5 = this.mButtonPanel;
            boolean z6 = cOUIButtonBarLayout5 != null && cOUIButtonBarLayout5.getButtonCount() > 1 && this.mButtonPanel.getOrientation() == 1;
            FrameLayout frameLayout2 = this.mCustomView;
            boolean z7 = frameLayout2 != null && frameLayout2.getMeasuredHeight() > this.mDialogCustomViewMinHeight;
            ViewGroup viewGroup2 = this.mListPanel;
            boolean z8 = viewGroup2 != null && viewGroup2.getChildCount() > 0;
            FrameLayout frameLayout3 = this.mCustomView;
            int i11 = (z8 || (frameLayout3 != null && frameLayout3.getChildCount() > 0)) ? this.mScrollPaddingBottomMessageHasTitleInAssignment : this.mMessagePaddingBottomWhenDialogTallDialog;
            if ((z4 || z5 || z6 || z7) && (viewFindViewById = findViewById(this.mNeedSetPaddingLayoutId)) != null && viewFindViewById.getPaddingTop() != this.mMessagePaddingTopWhenDialogIsTallDialog) {
                viewFindViewById.setPadding(viewFindViewById.getPaddingStart(), this.mMessagePaddingTopWhenDialogIsTallDialog, viewFindViewById.getPaddingEnd(), i11);
                super.onMeasure(iMaxMeasureSpec, iMaxMeasureSpec2);
            }
        }
        COUIAlertDialogMessageView cOUIAlertDialogMessageView3 = this.mDialogMessage;
        boolean z9 = (cOUIAlertDialogMessageView3 == null || TextUtils.isEmpty(cOUIAlertDialogMessageView3.getText())) ? false : true;
        FrameLayout frameLayout4 = this.mCustomView;
        boolean z10 = frameLayout4 != null && frameLayout4.getChildCount() > 0;
        ViewGroup viewGroup3 = this.mListPanel;
        boolean z11 = viewGroup3 != null && viewGroup3.getChildCount() > 0;
        COUIButtonBarLayout cOUIButtonBarLayout6 = this.mButtonPanel;
        boolean z12 = cOUIButtonBarLayout6 != null && cOUIButtonBarLayout6.getChildCount() > 0;
        COUIDialogTitle cOUIDialogTitle = this.mDialogTitle;
        if (cOUIDialogTitle != null && !TextUtils.isEmpty(cOUIDialogTitle.getText()) && ((z9 || z10 || z11 || z12) && this.mHasMessageMerge)) {
            i5 = iMaxMeasureSpec;
            this.mMergeAndSplitCriticalHeight = calculateMergeAndSplitCriticalHeight(z9, z11, z10, z12, iMaxMeasureSpec);
            int i12 = this.mMaxHeight;
            boolean z13 = iResolveAvailableHeight < this.mMergeAndSplitCriticalHeight || (i12 > 0 && measuredHeight >= i12 - getEffectiveBottomMargin());
            if (this.mForceSplitAfterConfigurationChange) {
                z13 = false;
            }
            if (this.mLinearLayoutTitle != null && ((((cOUIAlertDialogMessageView = this.mDialogMessage) != null && cOUIAlertDialogMessageView.getParent() == this.mLinearLayoutTitle) || (((frameLayout = this.mCustomView) != null && frameLayout.getParent() == this.mLinearLayoutTitle) || (((viewGroup = this.mListPanel) != null && viewGroup.getParent() == this.mLinearLayoutTitle) || ((cOUIButtonBarLayout4 = this.mButtonPanel) != null && cOUIButtonBarLayout4.getParent() == this.mLinearLayoutTitle)))) && !z13)) {
                restoreTitleTemplateMargin();
                COUIDialogTitle cOUIDialogTitle2 = this.mDialogTitle;
                if (cOUIDialogTitle2 != null) {
                    cOUIDialogTitle2.setPaddingRelative(0, cOUIDialogTitle2.getPaddingTop(), 0, this.mDialogTitle.getPaddingBottom());
                }
                this.mLinearLayoutTitle.setPadding(0, 0, 0, 0);
                COUIMaxHeightNestedScrollView cOUIMaxHeightNestedScrollView = this.mScrollViewMessage;
                if (cOUIMaxHeightNestedScrollView != null) {
                    cOUIMaxHeightNestedScrollView.setVisibility(0);
                }
                COUIMaxHeightScrollView cOUIMaxHeightScrollView3 = this.mScrollViewTitle;
                if (cOUIMaxHeightScrollView3 != null) {
                    if (this.mOriginalScrollViewTitleMaxHeight > 0) {
                        int maxHeight = cOUIMaxHeightScrollView3.getMaxHeight();
                        int i13 = this.mOriginalScrollViewTitleMaxHeight;
                        if (maxHeight != i13) {
                            this.mScrollViewTitle.setMaxHeight(i13);
                        }
                    }
                    if (this.mOriginalScrollViewTitleMinHeight >= 0) {
                        int minHeight = this.mScrollViewTitle.getMinHeight();
                        int i14 = this.mOriginalScrollViewTitleMinHeight;
                        if (minHeight != i14) {
                            this.mScrollViewTitle.setMinHeight(i14);
                        }
                    }
                    this.mScrollViewTitle.setVisibility(0);
                }
                if (this.mScrollViewMessage != null && (cOUIAlertDialogMessageView2 = this.mDialogMessage) != null) {
                    ViewParent parent = cOUIAlertDialogMessageView2.getParent();
                    LinearLayout linearLayout = this.mLinearLayoutTitle;
                    if (parent == linearLayout) {
                        linearLayout.removeView(this.mDialogMessage);
                        View view = this.mSpacingViewForMessage;
                        if (view != null) {
                            this.mLinearLayoutTitle.removeView(view);
                        }
                        View view2 = this.mSpacingViewForCustomView;
                        if (view2 != null) {
                            this.mLinearLayoutTitle.removeView(view2);
                        }
                        this.mScrollViewMessage.addView(this.mDialogMessage);
                    }
                }
                FrameLayout frameLayout5 = this.mCustomView;
                if (frameLayout5 != null) {
                    ViewParent parent2 = frameLayout5.getParent();
                    LinearLayout linearLayout2 = this.mLinearLayoutTitle;
                    if (parent2 == linearLayout2) {
                        linearLayout2.removeView(this.mCustomView);
                        View view3 = this.mSpacingViewForCustomView;
                        if (view3 != null) {
                            this.mLinearLayoutTitle.removeView(view3);
                        }
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(this.mCustomViewWidth, this.mCustomViewHeight);
                        layoutParams.setMarginStart(this.mCustomViewMarginStart);
                        this.mCustomView.setLayoutParams(layoutParams);
                        this.mCustomPanelLayout.addView(this.mCustomView);
                    }
                }
                ViewGroup viewGroup4 = this.mListPanel;
                if (viewGroup4 != null) {
                    ViewParent parent3 = viewGroup4.getParent();
                    LinearLayout linearLayout3 = this.mLinearLayoutTitle;
                    if (parent3 == linearLayout3) {
                        linearLayout3.removeView(this.mListPanel);
                        View view4 = this.mSpacingViewForListPanel;
                        if (view4 != null) {
                            this.mLinearLayoutTitle.removeView(view4);
                        }
                        if (this.mListPanel.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mListPanel.getLayoutParams();
                            marginLayoutParams2.setMarginStart(this.mListPanelMarginStart);
                            this.mListPanel.setLayoutParams(marginLayoutParams2);
                        }
                        this.mContentPanelLayout.addView(this.mListPanel);
                    }
                }
                if (this.mNeedResetButtomBarTopMargin && this.mButtonPanel != null && (this.mButtonPanel.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                    this.mButtonPanel.setTopMarginFlag(true);
                }
                COUIButtonBarLayout cOUIButtonBarLayout7 = this.mButtonPanel;
                if (cOUIButtonBarLayout7 != null) {
                    ViewParent parent4 = cOUIButtonBarLayout7.getParent();
                    LinearLayout linearLayout4 = this.mLinearLayoutTitle;
                    if (parent4 == linearLayout4 && this.mButtonPanelScrollView != null) {
                        linearLayout4.removeView(this.mButtonPanel);
                        ViewGroup.LayoutParams layoutParams2 = this.mButtonPanelOriginalParams;
                        if (layoutParams2 != null) {
                            this.mButtonPanelScrollView.addView(this.mButtonPanel, layoutParams2);
                        } else {
                            this.mButtonPanelScrollView.addView(this.mButtonPanel);
                        }
                        this.mButtonPanelScrollView.setVisibility(0);
                        this.mButtonPanelScrollView = null;
                        this.mButtonPanelOriginalParams = null;
                    }
                }
                this.mIsMergedToTitle = false;
                this.mNeedsRemeasureAfterSplit = true;
            }
            if (z13 && !this.mIsMergedToTitle) {
                COUIMaxHeightNestedScrollView cOUIMaxHeightNestedScrollView2 = this.mScrollViewMessage;
                int paddingTop = cOUIMaxHeightNestedScrollView2 != null ? cOUIMaxHeightNestedScrollView2.getPaddingTop() : 0;
                int i15 = this.mOriginalTitleTemplateMarginBottom;
                if (i15 < 0) {
                    i15 = 0;
                }
                int i16 = i15 + paddingTop;
                if (this.mLinearLayoutTitle == null) {
                    this.mLinearLayoutTitle = new LinearLayout(getContext());
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams3.gravity = 48;
                    this.mLinearLayoutTitle.setLayoutParams(layoutParams3);
                    this.mLinearLayoutTitle.setOrientation(1);
                    this.mScrollViewTitle.removeAllViews();
                    this.mScrollViewTitle.addView(this.mLinearLayoutTitle);
                    this.mLinearLayoutTitle.addView(this.mDialogTitle);
                    if (z9) {
                        this.mSpacingViewForMessage = new View(getContext());
                        this.mSpacingViewForMessage.setLayoutParams(new LinearLayout.LayoutParams(-1, i16));
                    }
                    if (z10) {
                        this.mSpacingViewForCustomView = new View(getContext());
                        if (z9) {
                            i7 = this.mScrollPaddingBottomMessageHasTitleInAssignment;
                        } else {
                            i7 = this.mOriginalTitleTemplateMarginBottom;
                            if (i7 < 0) {
                                i7 = 0;
                            }
                        }
                        this.mSpacingViewForCustomView.setLayoutParams(new LinearLayout.LayoutParams(-1, i7));
                    }
                    if (z11) {
                        this.mSpacingViewForListPanel = new View(getContext());
                        if (z9) {
                            i6 = this.mScrollPaddingBottomMessageHasTitleInAssignment;
                        } else {
                            i6 = this.mOriginalTitleTemplateMarginBottom;
                            if (i6 < 0) {
                                i6 = 0;
                            }
                        }
                        this.mSpacingViewForListPanel.setLayoutParams(new LinearLayout.LayoutParams(-1, i6));
                    }
                }
                clearTitleTemplateMargin();
                COUIDialogTitle cOUIDialogTitle3 = this.mDialogTitle;
                if (cOUIDialogTitle3 != null) {
                    int i17 = this.mOriginalTitleTemplateMarginStart;
                    if (i17 < 0) {
                        i17 = 0;
                    }
                    int i18 = this.mOriginalTitleTemplateMarginEnd;
                    if (i18 < 0) {
                        i18 = 0;
                    }
                    cOUIDialogTitle3.setPaddingRelative(i17, cOUIDialogTitle3.getPaddingTop(), i18, this.mDialogTitle.getPaddingBottom());
                }
                int heightWithVerticalMargin = (!z12 || (cOUIButtonBarLayout3 = this.mButtonPanel) == null) ? 0 : getHeightWithVerticalMargin(cOUIButtonBarLayout3);
                COUIDialogTitle cOUIDialogTitle4 = this.mDialogTitle;
                boolean z14 = (i10 - (cOUIDialogTitle4 != null ? cOUIDialogTitle4.getMeasuredHeight() : 0)) - heightWithVerticalMargin < this.mDialogLayoutMarginBottom * 2;
                this.mLinearLayoutTitle.setPadding(0, this.mOriginalTitleTemplateMarginTop, 0, (z11 || z10) ? 0 : this.mScrollPaddingBottomMessageTallDialog);
                COUIMaxHeightNestedScrollView cOUIMaxHeightNestedScrollView3 = this.mScrollViewMessage;
                if (cOUIMaxHeightNestedScrollView3 != null) {
                    cOUIMaxHeightNestedScrollView3.setVisibility(8);
                }
                if (z9 && this.mScrollViewMessage != null && this.mDialogMessage.getParent() != this.mLinearLayoutTitle) {
                    if (this.mSpacingViewForMessage.getLayoutParams() != null && this.mSpacingViewForMessage.getLayoutParams().height != i16) {
                        this.mSpacingViewForMessage.getLayoutParams().height = i16;
                    }
                    this.mScrollViewMessage.removeView(this.mDialogMessage);
                    this.mLinearLayoutTitle.addView(this.mSpacingViewForMessage);
                    this.mLinearLayoutTitle.addView(this.mDialogMessage);
                }
                if (z10 && this.mCustomView.getParent() != this.mLinearLayoutTitle) {
                    ViewGroup.LayoutParams layoutParams4 = this.mCustomView.getLayoutParams();
                    if (layoutParams4 instanceof ViewGroup.MarginLayoutParams) {
                        this.mCustomViewMarginStart = ((ViewGroup.MarginLayoutParams) layoutParams4).getMarginStart();
                    } else {
                        this.mCustomViewMarginStart = 0;
                    }
                    this.mCustomViewWidth = layoutParams4.width;
                    this.mCustomViewHeight = layoutParams4.height;
                    this.mCustomPanelLayout.removeView(this.mCustomView);
                    int i19 = this.mCustomViewWidth;
                    if (i19 == -1) {
                        i19 = -1;
                    }
                    int i20 = this.mCustomViewHeight;
                    if (i20 == -1) {
                        i20 = -2;
                    }
                    LinearLayoutCompat.LayoutParams layoutParams5 = new LinearLayoutCompat.LayoutParams(i19, i20);
                    layoutParams5.setMarginStart(Math.max(0, this.mCustomViewMarginStart - (this.mMessagePaddingStart - this.mCustomMarginExtra)));
                    if (this.mSpacingViewForCustomView.getParent() != null) {
                        this.mLinearLayoutTitle.removeView(this.mSpacingViewForCustomView);
                    }
                    this.mLinearLayoutTitle.addView(this.mSpacingViewForCustomView);
                    this.mLinearLayoutTitle.addView(this.mCustomView, layoutParams5);
                }
                if (z11 && this.mListPanel.getParent() != this.mLinearLayoutTitle) {
                    if (this.mListPanel.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                        this.mListPanelMarginStart = ((ViewGroup.MarginLayoutParams) this.mListPanel.getLayoutParams()).getMarginStart();
                    }
                    this.mContentPanelLayout.removeView(this.mListPanel);
                    LinearLayoutCompat.LayoutParams layoutParams6 = new LinearLayoutCompat.LayoutParams(-1, -2);
                    layoutParams6.setMarginStart(0);
                    if (this.mListPanel.getMeasuredHeight() > 0) {
                        ((LinearLayout.LayoutParams) layoutParams6).height = this.mListPanel.getMeasuredHeight();
                    }
                    if (this.mSpacingViewForListPanel.getParent() != null) {
                        this.mLinearLayoutTitle.removeView(this.mSpacingViewForListPanel);
                    }
                    this.mLinearLayoutTitle.addView(this.mSpacingViewForListPanel);
                    this.mLinearLayoutTitle.addView(this.mListPanel, layoutParams6);
                }
                if (z14 && (cOUIButtonBarLayout2 = this.mButtonPanel) != null && cOUIButtonBarLayout2.getParent() != this.mLinearLayoutTitle && (this.mButtonPanel.getParent() instanceof COUIMaxHeightNestedScrollView)) {
                    this.mButtonPanelScrollView = (COUIMaxHeightNestedScrollView) this.mButtonPanel.getParent();
                    this.mButtonPanelOriginalParams = this.mButtonPanel.getLayoutParams();
                    this.mButtonPanelScrollView.removeView(this.mButtonPanel);
                    int i21 = this.mScrollPaddingBottomMessageTallDialog;
                    LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams7.topMargin = i21;
                    this.mLinearLayoutTitle.addView(this.mButtonPanel, layoutParams7);
                    this.mButtonPanelScrollView.setVisibility(8);
                    this.mLinearLayoutTitle.setPadding(0, this.mOriginalTitleTemplateMarginTop, 0, 0);
                }
                if (this.mButtonPanel == null || !(this.mButtonPanel.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                    z2 = true;
                } else {
                    ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) this.mButtonPanel.getLayoutParams();
                    if (!z11 && !z10 && marginLayoutParams3.topMargin != this.mCouiBottomAlertDialogButtonbarMargintop) {
                        z2 = true;
                    } else if (marginLayoutParams3.topMargin != 0) {
                        marginLayoutParams3.topMargin = 0;
                        this.mButtonPanel.setLayoutParams(marginLayoutParams3);
                        z2 = true;
                        this.mNeedResetButtomBarTopMargin = true;
                        this.mButtonPanel.setTopMarginFlag(false);
                    } else {
                        z2 = true;
                    }
                }
                adjustMaxHeightOfScrollViewTitle(z9, z11, z10, iResolveAvailableHeight, z14);
                this.mIsMergedToTitle = z2;
            }
            if (this.mIsMergedToTitle) {
                cOUIButtonBarLayout = this.mButtonPanel;
                if (cOUIButtonBarLayout == null && cOUIButtonBarLayout.getParent() == this.mLinearLayoutTitle) {
                    z3 = z2;
                } else {
                    z3 = false;
                }
                adjustMaxHeightOfScrollViewTitle(z9, z11, z10, iResolveAvailableHeight, z3);
                iMaxMeasureSpec2 = expandMeasureSpecToTarget(iMaxMeasureSpec2, getMergedDialogTargetHeight(iResolveAvailableHeight));
            }
            if (this.mButtonPanel != null || this.mIsMergedToTitle) {
                super.onMeasure(i5, iMaxMeasureSpec2);
            } else {
                COUIMaxHeightNestedScrollView cOUIMaxHeightNestedScrollView4 = this.mButtonPanel.getParent() instanceof COUIMaxHeightNestedScrollView ? (COUIMaxHeightNestedScrollView) this.mButtonPanel.getParent() : null;
                if (cOUIMaxHeightNestedScrollView4 == null || this.mTopPanelLayout == null) {
                    super.onMeasure(i5, iMaxMeasureSpec2);
                } else {
                    if (this.mNeedsRemeasureAfterSplit) {
                        super.onMeasure(i5, iMaxMeasureSpec2);
                        this.mNeedsRemeasureAfterSplit = false;
                    }
                    cOUIMaxHeightNestedScrollView4.setMaxHeight(iResolveAvailableHeight - this.mTopPanelLayout.getMeasuredHeight());
                }
            }
            this.mForceSplitAfterConfigurationChange = false;
        }
        i5 = iMaxMeasureSpec;
        iResolveAvailableHeight = iResolveAvailableHeight;
        z2 = true;
        if (this.mIsMergedToTitle) {
            cOUIButtonBarLayout = this.mButtonPanel;
            if (cOUIButtonBarLayout == null) {
                z3 = false;
            } else {
                z3 = false;
            }
            adjustMaxHeightOfScrollViewTitle(z9, z11, z10, iResolveAvailableHeight, z3);
            iMaxMeasureSpec2 = expandMeasureSpecToTarget(iMaxMeasureSpec2, getMergedDialogTargetHeight(iResolveAvailableHeight));
        }
        if (this.mButtonPanel != null) {
            super.onMeasure(i5, iMaxMeasureSpec2);
        } else {
            super.onMeasure(i5, iMaxMeasureSpec2);
        }
        this.mForceSplitAfterConfigurationChange = false;
    }

    public void refreshDialogLayoutMargin() {
        updatePadding(true);
        requestLayout();
    }

    public void setAiPaymentTextStyleEnabled(boolean z2) {
        if (this.mAiPaymentTextStyleEnabled == z2) {
            return;
        }
        this.mAiPaymentTextStyleEnabled = z2;
        applyAiPaymentTextStyle();
    }

    public void setBlurBackgroundWindow(boolean z2) {
        this.mBlurBackgroundWindow = z2;
        dispatchBlurChromeToRootLayout(z2);
    }

    public void setCustomDialogPaddingBottom(int i3) {
        this.mCustomDialogPaddingBottom = i3;
    }

    public void setHasLoading(boolean z2) {
        this.mHasLoading = z2;
    }

    public void setHasMessageMerge(boolean z2) {
        this.mHasMessageMerge = z2;
    }

    @Deprecated
    public void setIsSupportRoundCornerWhenBlur(boolean z2) {
    }

    public void setIsTiny(boolean z2) {
        if (this.mIsTiny == z2) {
            return;
        }
        this.mIsTiny = z2;
        if (isAttachedToWindow()) {
            updatePadding(this.mSupportDynamicMarginTop);
        } else {
            syncRootShadowEdgeCornerInfo();
        }
    }

    public void setMaxHeight(int i3) {
        this.mMaxHeight = i3;
        this.mLastSetMarginBottomMaxHeightDelta = 0;
    }

    public void setMaxWidth(int i3) {
        this.mMaxWidth = i3;
    }

    public void setNeedSetPaddingLayoutId(int i3) {
        this.mNeedSetPaddingLayoutId = i3;
    }

    public void setSupportDynamicMarginTop(boolean z2) {
        this.mSupportDynamicMarginTop = z2;
    }

    public void setUseDialogOverlayFeature(boolean z2) {
        if (this.mUseBackgroundBlurFeature == z2) {
            return;
        }
        this.mUseBackgroundBlurFeature = z2;
        if (z2) {
            applyTransparentInsetInnerBackground();
        } else {
            restoreInsetInnerBackground();
        }
        refreshOutlineClipForTheme();
    }

    public boolean shouldSkipOutlineClipForTheme() {
        if (this.mUseBackgroundBlurFeature) {
            return false;
        }
        if (this.mIsThemeBitmapBackground == null) {
            Drawable drawable = getContext().getDrawable(R.drawable.coui_alert_dialog_background);
            this.mIsThemeBitmapBackground = Boolean.valueOf((drawable instanceof NinePatchDrawable) || (drawable instanceof BitmapDrawable));
        }
        return this.mIsThemeBitmapBackground.booleanValue() && !this.mIsTiny;
    }

    public void updateDialogOverlay(COUIAlertDialogOverlayHelper.OverlayMode overlayMode) {
        View view;
        ensureDialogOverlayView();
        COUIAlertDialogOverlayHelper cOUIAlertDialogOverlayHelper = this.mOverlayHelper;
        if (cOUIAlertDialogOverlayHelper == null || (view = this.mDialogOverlay) == null) {
            return;
        }
        cOUIAlertDialogOverlayHelper.applyOverlayState(view, overlayMode);
        if (this.mDialogOverlay.getVisibility() == 0) {
            layoutDialogOverlay();
        }
    }

    public COUIAlertDialogMaxLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = "DialogMaxLinearLayout";
        this.mTempRect = new Rect();
        this.mNeedSetPaddingLayoutId = -1;
        this.mHasMessageMerge = true;
        this.mCustomMarginExtra = 5;
        this.mScrollViewTitleOriginalMaxHeight = -1;
        this.mIsMergedToTitle = false;
        this.mOriginalScrollViewTitleMaxHeight = -1;
        this.mOriginalScrollViewTitleMinHeight = -1;
        this.mOriginalTitleTemplateMarginTop = -1;
        this.mOriginalTitleTemplateMarginBottom = -1;
        this.mOriginalTitleTemplateMarginStart = -1;
        this.mOriginalTitleTemplateMarginEnd = -1;
        this.mOriginalTopPanelMarginBottom = -1;
        this.mBlurBackgroundWindow = false;
        this.mIsSupportSmoothRoundCorner = false;
        this.mHasLoading = false;
        this.mIsTiny = false;
        this.mSupportDynamicMarginTop = false;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIAlertDialogMaxLinearLayout);
        this.mMaxWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIAlertDialogMaxLinearLayout_maxWidth, 0);
        this.mMaxHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIAlertDialogMaxLinearLayout_maxHeight, 0);
        this.mMessagePaddingTopWhenDialogIsTallDialog = getResources().getDimensionPixelSize(R.dimen.coui_alert_dialog_scroll_padding_top_message);
        this.mMessagePaddingBottomWhenDialogTallDialog = getResources().getDimensionPixelSize(R.dimen.coui_alert_dialog_scroll_padding_bottom_message);
        this.mDialogLayoutMarginBottom = getResources().getDimensionPixelSize(R.dimen.coui_dialog_layout_margin_vertical);
        this.mDialogLayoutMarginTopFreeForm = getResources().getDimensionPixelSize(R.dimen.coui_dialog_layout_margin_top_freeform);
        this.mDialogLayoutMarginBottomWithNavigationBar = getResources().getDimensionPixelSize(R.dimen.coui_dialog_layout_margin_bottom_with_navigation_bar);
        this.mDialogLayoutMarginVerticalTotal = this.mDialogLayoutMarginBottom;
        this.mDialogLayoutMarginHorizontal = getResources().getDimensionPixelSize(R.dimen.coui_dialog_layout_margin_horizontal);
        this.mDialogContentPanelLayoutMinHeight = getResources().getDimensionPixelSize(R.dimen.coui_dialog_layout_content_panel_layout_min_height);
        this.mDialogCustomViewMinHeight = getResources().getDimensionPixelSize(R.dimen.coui_dialog_layout_customview_min_height);
        Resources resources = getResources();
        int i3 = R.dimen.coui_alert_dialog_message_padding_left;
        this.mMessagePaddingStart = resources.getDimensionPixelSize(i3);
        this.mMessagePaddingEnd = getResources().getDimensionPixelSize(i3);
        this.mCouiBottomAlertDialogButtonbarMargintop = getResources().getDimensionPixelSize(R.dimen.coui_bottom_alert_dialog_buttonbar_margintop);
        this.mScrollPaddingBottomMessageHasTitleInAssignment = getResources().getDimensionPixelSize(R.dimen.coui_alert_dialog_scroll_padding_bottom_message_has_title_in_assignment);
        this.mScrollPaddingBottomMessageTallDialog = getResources().getDimensionPixelSize(R.dimen.coui_alert_dialog_scroll_padding_bottom_message_tallDialog);
        this.mIsSupportSmoothRoundCorner = RoundCornerUtil.isVersionSupport() && RoundCornerUtil.isSmoothRoundRectOn();
        this.mRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIAlertDialogMaxLinearLayout_clip_radius_root, -1);
        typedArrayObtainStyledAttributes.recycle();
    }
}