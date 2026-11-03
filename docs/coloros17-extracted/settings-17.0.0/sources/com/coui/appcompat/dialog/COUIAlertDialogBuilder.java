package com.coui.appcompat.dialog;

import android.app.Dialog;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.COUIRecycleListView;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.COUIDarkModeUtil;
import com.coui.appcompat.button.COUIButton;
import com.coui.appcompat.buttonBar.COUIButtonBarLayout;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.dialog.adapter.COUIListDialogAdapter;
import com.coui.appcompat.dialog.adapter.ChoiceListAdapter;
import com.coui.appcompat.dialog.adapter.SummaryAdapter;
import com.coui.appcompat.dialog.widget.COUIAlertDialogMaxLinearLayout;
import com.coui.appcompat.dialog.widget.COUIAlertDialogMaxScrollView;
import com.coui.appcompat.dialog.widget.COUIMaxHeightNestedScrollView;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.imageview.COUIRoundImageView;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.statement.COUIMaxHeightScrollView;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.settings.romupdate.RomUpdateListParser;

/* JADX INFO: loaded from: classes7.dex */
public class COUIAlertDialogBuilder extends AlertDialog.Builder {
    private static final int DEF_STYLE_ATTR = R.attr.alertDialogStyle;
    private static final int DEF_STYLE_RES = com.support.dialog.R.style.AlertDialogBuildStyle;
    private static final int DEF_WINDOW_ANIM = com.support.dialog.R.style.Animation_COUI_Dialog_Alpha;
    private static final int DEF_WINDOW_GRAVITY = 17;
    private static final int MAJOR_VERSION = 30;
    private static final int OPLUSFLAGS = 65536;
    private static final int SDK_SUBVERSION = 16;
    private static final String TAG = "COUIAlertDialogBuilder";
    private static final int UNSET_WIDTH = -1;
    private boolean hasAdapter;
    private boolean hasMessage;
    private boolean hasSetButton;
    private boolean hasSetView;
    private boolean hasTitle;
    private boolean isAssignMentLayout;
    private boolean mAccessibilityListenerAttached;
    private AccessibilityManager mAccessibilityManager;
    private AccessibilityManager.AccessibilityStateChangeListener mAccessibilityStateChangeListener;
    private boolean mAlwaysFollowHand;
    private View mAnchorView;
    private Point mAnchorViewTouchPoint;
    private COUIBackgroundBlurBuilder mBackgroundBlurBuilder;
    private Drawable mBackgroundDrawable;
    private boolean mBlurListenerAttached;
    private COUIBackgroundBlurBuilder.OnBlurryStateChangeListener mBlurryStateChangeListener;
    private boolean mButtonLayoutDynamicLayout;
    private COUIListDialogAdapter mCOUIListDialogAdapter;
    private ChoiceListAdapter mChoiceListAdapter;
    private ComponentCallbacks mComponentCallbacks;
    private Configuration mConfiguration;
    private int mContentMaxHeight;
    private int mContentMaxWidth;
    private View mContentView;
    private int mCustomContentLayoutRes;
    private int mCustomDialogPaddingBottom;
    private Drawable mCustomDrawable;
    private CharSequence mCustomMessage;
    private String mCustomTitle;
    private AlertDialog mDialog;
    private int mDialogStyle;
    private int mDialogWindowType;
    private Point mExtraOffsetPoint;
    private boolean mForcePhysicalDimensions;
    private int mGravity;
    private boolean mHasLoading;
    private boolean mHasMessageMerge;
    private Boolean mIsAccessibilityEnabled;
    private boolean mIsCustomStyle;
    private boolean mIsForceCenterInLargeScreen;
    private boolean mIsForceCenterStyleStatus;
    private boolean mIsNeedToAdaptMessageAndList;
    private boolean mIsTinyStyle;
    private DialogInterface.OnClickListener mItemClickListener;
    private CharSequence[] mItems;
    private int mOldConfigurationHeightDP;
    private int mOldConfigurationWidthDP;
    private int mOriginWidth;
    private COUIAlertDialogOverlayHelper mOverlayHelper;
    private int mRecommendButtonId;
    private COUIButton.ButtonType mRecommendButtonType;
    private boolean mRegisterConfigurationChangeCallBack;
    private SummaryAdapter mSummaryAdapter;
    private CharSequence[] mSummaryItems;
    private boolean mSupportDynamicMarginTop;
    public int[] mTextColor;
    private AccessibilityManager.TouchExplorationStateChangeListener mTouchExplorationStateChangeListener;
    private int mWindowAnimStyleRes;
    private int parentPanelMinHeight;

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIAlertDialogBuilder$1, reason: invalid class name */
    public class AnonymousClass1 implements View.OnAttachStateChangeListener {
        public AnonymousClass1() {
        }

        public static /* synthetic */ void a(AnonymousClass1 anonymousClass1, boolean z2) {
            COUIAlertDialogBuilder cOUIAlertDialogBuilder = COUIAlertDialogBuilder.this;
            COUIAlertDialogBuilder.access$200(cOUIAlertDialogBuilder, COUIAlertDialogBuilder.access$100(cOUIAlertDialogBuilder));
        }

        public static /* synthetic */ void b(AnonymousClass1 anonymousClass1, boolean z2) {
            COUIAlertDialogBuilder cOUIAlertDialogBuilder = COUIAlertDialogBuilder.this;
            COUIAlertDialogBuilder.access$200(cOUIAlertDialogBuilder, COUIAlertDialogBuilder.access$100(cOUIAlertDialogBuilder));
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            if (COUIAlertDialogBuilder.access$000(COUIAlertDialogBuilder.this) == null) {
                COUIAlertDialogBuilder cOUIAlertDialogBuilder = COUIAlertDialogBuilder.this;
                COUIAlertDialogBuilder.access$002(cOUIAlertDialogBuilder, (AccessibilityManager) COUIAlertDialogBuilder.access$100(cOUIAlertDialogBuilder).getContext().getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG));
            }
            COUIAlertDialogBuilder cOUIAlertDialogBuilder2 = COUIAlertDialogBuilder.this;
            COUIAlertDialogBuilder.access$200(cOUIAlertDialogBuilder2, COUIAlertDialogBuilder.access$100(cOUIAlertDialogBuilder2));
            COUIAlertDialogBuilder.access$302(COUIAlertDialogBuilder.this, new b(this));
            COUIAlertDialogBuilder.access$000(COUIAlertDialogBuilder.this).addAccessibilityStateChangeListener(COUIAlertDialogBuilder.access$300(COUIAlertDialogBuilder.this));
            COUIAlertDialogBuilder.access$402(COUIAlertDialogBuilder.this, new c(this));
            COUIAlertDialogBuilder.access$000(COUIAlertDialogBuilder.this).addTouchExplorationStateChangeListener(COUIAlertDialogBuilder.access$400(COUIAlertDialogBuilder.this));
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            COUIAlertDialogBuilder.access$502(COUIAlertDialogBuilder.this, false);
            COUIAlertDialogBuilder.access$600(COUIAlertDialogBuilder.this);
            view.removeOnAttachStateChangeListener(this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIAlertDialogBuilder$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnAttachStateChangeListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            COUIAlertDialogBuilder.access$700(COUIAlertDialogBuilder.this);
            try {
                COUIAlertDialogBuilder.access$800(COUIAlertDialogBuilder.this, view);
            } catch (Exception e3) {
                Log.e(COUIAlertDialogBuilder.TAG, "operateBlur error message:" + e3.getMessage());
            }
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            COUIAlertDialogBuilder.access$902(COUIAlertDialogBuilder.this, false);
            COUIAlertDialogBuilder.access$1000(COUIAlertDialogBuilder.this);
            COUIAlertDialogBuilder.access$1100(COUIAlertDialogBuilder.this).release();
            view.removeOnAttachStateChangeListener(this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIAlertDialogBuilder$3, reason: invalid class name */
    public class AnonymousClass3 implements COUIMaxHeightNestedScrollView.ConfigChangeListener {
        final /* synthetic */ ViewGroup val$scrollView;

        public AnonymousClass3(ViewGroup viewGroup) {
            this.val$scrollView = viewGroup;
        }

        @Override // com.coui.appcompat.dialog.widget.COUIMaxHeightNestedScrollView.ConfigChangeListener
        public void onChange() {
            this.val$scrollView.setPadding(0, COUIAlertDialogBuilder.this.getContext().getResources().getDimensionPixelOffset(com.support.dialog.R.dimen.bottom_dialog_scroll_padding_top), 0, COUIAlertDialogBuilder.this.getContext().getResources().getDimensionPixelOffset(com.support.dialog.R.dimen.bottom_dialog_scroll_padding_bottom));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIAlertDialogBuilder$4, reason: invalid class name */
    public class AnonymousClass4 implements View.OnTouchListener {
        final /* synthetic */ COUIMaxHeightScrollView val$titleMaxHeightScrollView;

        public AnonymousClass4(COUIMaxHeightScrollView cOUIMaxHeightScrollView) {
            this.val$titleMaxHeightScrollView = cOUIMaxHeightScrollView;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return this.val$titleMaxHeightScrollView.getHeight() < this.val$titleMaxHeightScrollView.getMaxHeight();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.dialog.COUIAlertDialogBuilder$5, reason: invalid class name */
    public class AnonymousClass5 implements ComponentCallbacks {
        public AnonymousClass5() {
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            if (COUIAlertDialogBuilder.access$1200(COUIAlertDialogBuilder.this)) {
                COUIAlertDialogBuilder.access$1302(COUIAlertDialogBuilder.this, configuration);
                COUIAlertDialogBuilder.this.updateGravityWhileConfigChange(configuration);
            }
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }
    }

    public static class OutsideTouchListener implements View.OnTouchListener {
        private final Dialog dialog;
        private final int prePieSlop;

        public OutsideTouchListener(Dialog dialog) {
            this.dialog = dialog;
            this.prePieSlop = ViewConfiguration.get(dialog.getContext()).getScaledWindowTouchSlop();
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            View viewFindViewById = view.findViewById(com.support.dialog.R.id.parentPanel);
            if (viewFindViewById == null) {
                COUILog.e(COUIAlertDialogBuilder.TAG, "parentPanel is null; Need to check whether the application has a layout that covers the coui's");
                return this.dialog.onTouchEvent(motionEvent);
            }
            if (new RectF(viewFindViewById.getLeft() + viewFindViewById.getPaddingLeft(), viewFindViewById.getTop() + viewFindViewById.getPaddingTop(), viewFindViewById.getRight() - viewFindViewById.getPaddingRight(), viewFindViewById.getBottom() - viewFindViewById.getPaddingBottom()).contains(motionEvent.getX(), motionEvent.getY())) {
                return false;
            }
            MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
            if (motionEvent.getAction() == 1) {
                motionEventObtain.setAction(4);
            }
            view.performClick();
            boolean zOnTouchEvent = this.dialog.onTouchEvent(motionEventObtain);
            motionEventObtain.recycle();
            return zOnTouchEvent;
        }
    }

    public COUIAlertDialogBuilder(Context context) {
        this(context, com.support.dialog.R.style.COUIAlertDialog_BottomWarning);
    }

    public static /* synthetic */ void a(COUIAlertDialogBuilder cOUIAlertDialogBuilder, COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout, View view, boolean z2) {
        boolean zHasOverlay = cOUIAlertDialogBuilder.mOverlayHelper.hasOverlay();
        boolean z3 = zHasOverlay || z2;
        if (cOUIAlertDialogMaxLinearLayout != null) {
            cOUIAlertDialogMaxLinearLayout.setBlurBackgroundWindow(z3);
            if (zHasOverlay) {
                cOUIAlertDialogMaxLinearLayout.updateDialogOverlay(COUIAlertDialogOverlayHelper.OverlayMode.BLUR_DEGRADED);
            }
        }
        if (view instanceof COUIButtonBarLayout) {
            ((COUIButtonBarLayout) view).setBlurBackgroundWindow(z3);
        }
        SummaryAdapter summaryAdapter = cOUIAlertDialogBuilder.mSummaryAdapter;
        if (summaryAdapter != null) {
            summaryAdapter.setBlurState(z3);
        }
        COUIBackgroundBlurBuilder.OnBlurryStateChangeListener onBlurryStateChangeListener = cOUIAlertDialogBuilder.mBlurryStateChangeListener;
        if (onBlurryStateChangeListener != null) {
            onBlurryStateChangeListener.onBlurryStateChanged(z2);
        }
    }

    public static /* synthetic */ AccessibilityManager access$000(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        return cOUIAlertDialogBuilder.mAccessibilityManager;
    }

    public static /* synthetic */ AccessibilityManager access$002(COUIAlertDialogBuilder cOUIAlertDialogBuilder, AccessibilityManager accessibilityManager) {
        cOUIAlertDialogBuilder.mAccessibilityManager = accessibilityManager;
        return accessibilityManager;
    }

    public static /* synthetic */ AlertDialog access$100(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        return cOUIAlertDialogBuilder.mDialog;
    }

    public static /* synthetic */ void access$1000(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        cOUIAlertDialogBuilder.releaseApplicationConfigChangeListener();
    }

    public static /* synthetic */ COUIBackgroundBlurBuilder access$1100(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        return cOUIAlertDialogBuilder.mBackgroundBlurBuilder;
    }

    public static /* synthetic */ boolean access$1200(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        return cOUIAlertDialogBuilder.mRegisterConfigurationChangeCallBack;
    }

    public static /* synthetic */ Configuration access$1302(COUIAlertDialogBuilder cOUIAlertDialogBuilder, Configuration configuration) {
        cOUIAlertDialogBuilder.mConfiguration = configuration;
        return configuration;
    }

    public static /* synthetic */ void access$200(COUIAlertDialogBuilder cOUIAlertDialogBuilder, AlertDialog alertDialog) {
        cOUIAlertDialogBuilder.updateAccessibilityState(alertDialog);
    }

    public static /* synthetic */ AccessibilityManager.AccessibilityStateChangeListener access$300(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        return cOUIAlertDialogBuilder.mAccessibilityStateChangeListener;
    }

    public static /* synthetic */ AccessibilityManager.AccessibilityStateChangeListener access$302(COUIAlertDialogBuilder cOUIAlertDialogBuilder, AccessibilityManager.AccessibilityStateChangeListener accessibilityStateChangeListener) {
        cOUIAlertDialogBuilder.mAccessibilityStateChangeListener = accessibilityStateChangeListener;
        return accessibilityStateChangeListener;
    }

    public static /* synthetic */ AccessibilityManager.TouchExplorationStateChangeListener access$400(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        return cOUIAlertDialogBuilder.mTouchExplorationStateChangeListener;
    }

    public static /* synthetic */ AccessibilityManager.TouchExplorationStateChangeListener access$402(COUIAlertDialogBuilder cOUIAlertDialogBuilder, AccessibilityManager.TouchExplorationStateChangeListener touchExplorationStateChangeListener) {
        cOUIAlertDialogBuilder.mTouchExplorationStateChangeListener = touchExplorationStateChangeListener;
        return touchExplorationStateChangeListener;
    }

    public static /* synthetic */ boolean access$502(COUIAlertDialogBuilder cOUIAlertDialogBuilder, boolean z2) {
        cOUIAlertDialogBuilder.mAccessibilityListenerAttached = z2;
        return z2;
    }

    public static /* synthetic */ void access$600(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        cOUIAlertDialogBuilder.unregisterAccessibilityListeners();
    }

    public static /* synthetic */ void access$700(COUIAlertDialogBuilder cOUIAlertDialogBuilder) {
        cOUIAlertDialogBuilder.registerApplicationConfigChangeListener();
    }

    public static /* synthetic */ void access$800(COUIAlertDialogBuilder cOUIAlertDialogBuilder, View view) {
        cOUIAlertDialogBuilder.operateBlur(view);
    }

    public static /* synthetic */ boolean access$902(COUIAlertDialogBuilder cOUIAlertDialogBuilder, boolean z2) {
        cOUIAlertDialogBuilder.mBlurListenerAttached = z2;
        return z2;
    }

    private void applyAiPaymentTextStyleIfNeeded() {
        AlertDialog alertDialog;
        if (!isRecommendAiPayment() || (alertDialog = this.mDialog) == null) {
            return;
        }
        View viewFindViewById = alertDialog.getWindow().findViewById(com.support.dialog.R.id.parentPanel);
        if (viewFindViewById instanceof COUIAlertDialogMaxLinearLayout) {
            ((COUIAlertDialogMaxLinearLayout) viewFindViewById).setAiPaymentTextStyleEnabled(true);
        }
        View viewFindViewById2 = this.mDialog.getWindow().findViewById(com.support.dialog.R.id.buttonPanel);
        if (viewFindViewById2 instanceof COUIButtonBarLayout) {
            viewFindViewById2.requestLayout();
        }
    }

    private void disabledTitleScroll(AlertDialog alertDialog) {
        View viewFindViewById = alertDialog.findViewById(com.support.dialog.R.id.alert_title_scroll_view);
        if (!(viewFindViewById instanceof COUIMaxHeightScrollView)) {
            COUILog.e(TAG, "alert_title_scroll_view is error; Need to check whether the application has a layout that covers the coui's");
        } else {
            COUIMaxHeightScrollView cOUIMaxHeightScrollView = (COUIMaxHeightScrollView) viewFindViewById;
            cOUIMaxHeightScrollView.setOnTouchListener(new AnonymousClass4(cOUIMaxHeightScrollView));
        }
    }

    private void initAccessibilityFeatures() {
        if (this.mAccessibilityListenerAttached) {
            return;
        }
        this.mAccessibilityListenerAttached = true;
        this.mDialog.getWindow().getDecorView().addOnAttachStateChangeListener(new AnonymousClass1());
    }

    private void initAttrs() {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, com.support.dialog.R.styleable.COUIAlertDialogBuilder, DEF_STYLE_ATTR, DEF_STYLE_RES);
        this.mGravity = typedArrayObtainStyledAttributes.getInt(com.support.dialog.R.styleable.COUIAlertDialogBuilder_android_gravity, 17);
        this.mWindowAnimStyleRes = typedArrayObtainStyledAttributes.getResourceId(com.support.dialog.R.styleable.COUIAlertDialogBuilder_windowAnimStyle, DEF_WINDOW_ANIM);
        this.mContentMaxWidth = typedArrayObtainStyledAttributes.getDimensionPixelOffset(com.support.dialog.R.styleable.COUIAlertDialogBuilder_contentMaxWidth, 0);
        this.mContentMaxHeight = typedArrayObtainStyledAttributes.getDimensionPixelOffset(com.support.dialog.R.styleable.COUIAlertDialogBuilder_contentMaxHeight, 0);
        this.mCustomContentLayoutRes = typedArrayObtainStyledAttributes.getResourceId(com.support.dialog.R.styleable.COUIAlertDialogBuilder_customContentLayout, 0);
        this.mIsNeedToAdaptMessageAndList = typedArrayObtainStyledAttributes.getBoolean(com.support.dialog.R.styleable.COUIAlertDialogBuilder_isNeedToAdaptMessageAndList, false);
        this.mIsTinyStyle = typedArrayObtainStyledAttributes.getBoolean(com.support.dialog.R.styleable.COUIAlertDialogBuilder_isTinyDialog, false);
        this.mHasLoading = typedArrayObtainStyledAttributes.getBoolean(com.support.dialog.R.styleable.COUIAlertDialogBuilder_hasLoading, false);
        this.isAssignMentLayout = typedArrayObtainStyledAttributes.getBoolean(com.support.dialog.R.styleable.COUIAlertDialogBuilder_isAssignMentLayout, false);
        this.mIsForceCenterInLargeScreen = typedArrayObtainStyledAttributes.getBoolean(com.support.dialog.R.styleable.COUIAlertDialogBuilder_isForceCenterStyleInLargeScreen, false);
        this.mIsCustomStyle = typedArrayObtainStyledAttributes.getBoolean(com.support.dialog.R.styleable.COUIAlertDialogBuilder_isCustomStyle, false);
        typedArrayObtainStyledAttributes.recycle();
    }

    private void initBlurBuilder(Context context) {
        this.mBackgroundBlurBuilder = new COUIBackgroundBlurBuilder(context);
        this.mOverlayHelper = new COUIAlertDialogOverlayHelper(context);
    }

    private void initBlurListener() {
        if (this.mBlurListenerAttached) {
            return;
        }
        this.mBlurListenerAttached = true;
        this.mDialog.getWindow().getDecorView().addOnAttachStateChangeListener(new AnonymousClass2());
    }

    private void initCOUIDialogTitle(View view) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = -1;
        view.setLayoutParams(layoutParams);
    }

    private void initContentMaxHeight(Window window) {
        if (this.mContentMaxHeight <= 0) {
            return;
        }
        View viewFindViewById = window.findViewById(com.support.dialog.R.id.parentPanel);
        if (viewFindViewById instanceof COUIAlertDialogMaxLinearLayout) {
            ((COUIAlertDialogMaxLinearLayout) viewFindViewById).setMaxHeight(this.mContentMaxHeight);
        } else if (viewFindViewById instanceof COUIAlertDialogMaxScrollView) {
            ((COUIAlertDialogMaxScrollView) viewFindViewById).setMaxHeight(this.mContentMaxHeight);
        } else {
            COUILog.e(TAG, "parentPanel is error; Need to check whether the application has a layout that covers the coui's");
        }
    }

    private void initContentMaxWidth(Window window) {
        if (this.mContentMaxWidth <= 0) {
            return;
        }
        View viewFindViewById = window.findViewById(com.support.dialog.R.id.parentPanel);
        if (viewFindViewById instanceof COUIAlertDialogMaxLinearLayout) {
            ((COUIAlertDialogMaxLinearLayout) viewFindViewById).setMaxWidth(this.mContentMaxWidth);
        } else if (viewFindViewById instanceof COUIAlertDialogMaxScrollView) {
            ((COUIAlertDialogMaxScrollView) viewFindViewById).setMaxWidth(this.mContentMaxWidth);
        } else {
            COUILog.e(TAG, "parentPanel is error; Need to check whether the application has a layout that covers the coui's");
        }
    }

    private void initContentPanelClipChildren() {
        CharSequence[] charSequenceArr;
        if (this.mDialog.getWindow() == null) {
            return;
        }
        LinearLayout linearLayout = (LinearLayout) this.mDialog.getWindow().findViewById(com.support.dialog.R.id.contentPanel);
        COUIRecycleListView cOUIRecycleListView = (COUIRecycleListView) this.mDialog.getWindow().findViewById(com.support.dialog.R.id.select_dialogx_listview);
        if (linearLayout == null || (charSequenceArr = this.mItems) == null || charSequenceArr.length == 0 || cOUIRecycleListView == null) {
            return;
        }
        cOUIRecycleListView.bindClipChildrenHost(linearLayout);
    }

    private void initContentSpacing(Window window) {
        if (this.mIsTinyStyle || this.mHasLoading) {
            return;
        }
        View viewFindViewById = window.findViewById(com.support.dialog.R.id.scrollView);
        View viewFindViewById2 = window.findViewById(com.support.dialog.R.id.listPanel);
        View viewFindViewById3 = window.findViewById(com.support.dialog.R.id.topPanel);
        boolean z2 = (viewFindViewById2 instanceof ViewGroup) && ((ViewGroup) viewFindViewById2).getChildCount() > 0;
        Resources resources = getContext().getResources();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(com.support.dialog.R.dimen.coui_alert_dialog_scroll_padding_bottom_message);
        int dimensionPixelOffset2 = resources.getDimensionPixelOffset(com.support.dialog.R.dimen.coui_alert_dialog_scroll_padding_bottom_message_has_title_in_assignment);
        boolean z3 = this.hasMessage;
        if (!z3 || z2 || this.hasSetView) {
            if (z3 && (z2 || this.hasSetView)) {
                if (viewFindViewById != null) {
                    viewFindViewById.setPadding(viewFindViewById.getPaddingLeft(), 0, viewFindViewById.getPaddingRight(), dimensionPixelOffset2);
                }
                if (z2 && !this.hasSetButton) {
                    viewFindViewById2.setPadding(0, 0, 0, dimensionPixelOffset);
                }
            }
        } else if (viewFindViewById != null) {
            viewFindViewById.setPadding(viewFindViewById.getPaddingLeft(), 0, viewFindViewById.getPaddingRight(), dimensionPixelOffset);
        }
        if (this.hasMessage || this.hasSetView || this.mItems != null || this.hasAdapter || viewFindViewById3 == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = viewFindViewById3.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = dimensionPixelOffset;
        }
    }

    private void initCustomPanel() {
        int i3;
        if (this.hasSetView || (i3 = this.mCustomContentLayoutRes) == 0) {
            return;
        }
        setView(i3);
    }

    private void initCustomPanelVisibility(Window window) {
        if (this.hasSetView) {
            View viewFindViewById = window.findViewById(com.support.dialog.R.id.customPanel);
            if (viewFindViewById != null) {
                viewFindViewById.setVisibility(0);
            }
            View viewFindViewById2 = window.findViewById(com.support.dialog.R.id.custom);
            if (viewFindViewById2 != null) {
                viewFindViewById2.setVisibility(0);
            }
        }
    }

    private void initListPanel(Window window) {
        View viewFindViewById = window.findViewById(com.support.dialog.R.id.listPanel);
        if (!(viewFindViewById instanceof ViewGroup)) {
            COUILog.e(TAG, "listPanel is error; Need to check whether the application has a layout that covers the coui's");
            return;
        }
        ViewGroup viewGroup = (ViewGroup) viewFindViewById;
        AlertDialog alertDialog = this.mDialog;
        ListView listView = alertDialog != null ? alertDialog.getListView() : null;
        if (listView != null) {
            listView.setScrollIndicators(0);
        }
        boolean z2 = (viewGroup == null || listView == null) ? false : true;
        if (z2) {
            if (listView.getParent() != null && (listView.getParent() instanceof ViewGroup)) {
                ((ViewGroup) listView.getParent()).removeView(listView);
            }
            viewGroup.addView(listView, new ViewGroup.LayoutParams(-1, -1));
        }
        ViewGroup viewGroup2 = (ViewGroup) window.findViewById(com.support.dialog.R.id.scrollView);
        if (viewGroup2 != null) {
            viewGroup2.setScrollIndicators(0);
            if (this.mIsNeedToAdaptMessageAndList && z2) {
                setViewHorizontalWeight(viewGroup2, 1);
                setViewHorizontalWeight(viewGroup, 1);
            }
            if (viewGroup2 instanceof COUIMaxHeightNestedScrollView) {
                boolean zIsSecondaryScreen = AppFeatureUtil.isSecondaryScreen(getContext());
                if (this.hasAdapter && !zIsSecondaryScreen) {
                    ((COUIMaxHeightNestedScrollView) viewGroup2).setMaxHeight(getContext().getResources().getDimensionPixelOffset(com.support.dialog.R.dimen.coui_alert_dialog_builder_content_max_height_with_adapter));
                }
                if (window.getAttributes().gravity != 80 || !this.hasMessage) {
                    return;
                }
                if (!this.mHasLoading && !this.mIsTinyStyle) {
                    return;
                } else {
                    ((COUIMaxHeightNestedScrollView) viewGroup2).setConfigChangeListener(new AnonymousClass3(viewGroup2));
                }
            } else {
                COUILog.e(TAG, "scrollView isn't instanceof COUIMaxHeightNestedScrollView; Need to check whether the application has a layout that covers the coui's");
            }
        }
        if (this.mItems == null && this.hasAdapter) {
            viewGroup.setPaddingRelative(viewGroup.getPaddingStart(), viewGroup.getPaddingTop(), viewGroup.getPaddingEnd(), getContext().getResources().getDimensionPixelSize(com.support.dialog.R.dimen.alert_dialog_list_item_padding_bottom));
        }
    }

    private void initMessagePadding() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null) {
            return;
        }
        int i3 = com.support.dialog.R.id.scrollView;
        View viewFindViewById = alertDialog.findViewById(i3);
        View viewFindViewById2 = this.mDialog.getWindow().findViewById(com.support.dialog.R.id.parentPanel);
        if (!(viewFindViewById2 instanceof COUIAlertDialogMaxLinearLayout)) {
            COUILog.e(TAG, "parentPanel is error; Need to check whether the application has a layout that covers the coui's");
            return;
        }
        COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout = (COUIAlertDialogMaxLinearLayout) viewFindViewById2;
        cOUIAlertDialogMaxLinearLayout.setHasLoading(this.mHasLoading);
        cOUIAlertDialogMaxLinearLayout.setIsTiny(this.mIsTinyStyle);
        cOUIAlertDialogMaxLinearLayout.setSupportDynamicMarginTop(this.mSupportDynamicMarginTop);
        if (this.mAnchorView != null) {
            this.mCustomDialogPaddingBottom = 0;
        }
        cOUIAlertDialogMaxLinearLayout.setCustomDialogPaddingBottom(this.mCustomDialogPaddingBottom);
        if (!this.mIsTinyStyle && !this.mHasLoading && this.hasMessage && viewFindViewById != null) {
            if (this.hasTitle && this.isAssignMentLayout) {
                viewFindViewById.setPadding(viewFindViewById.getPaddingLeft(), 0, viewFindViewById.getPaddingRight(), getContext().getResources().getDimensionPixelOffset(com.support.dialog.R.dimen.coui_alert_dialog_scroll_padding_bottom_message_has_title_in_assignment));
            }
            if (!this.mIsTinyStyle && !this.isAssignMentLayout) {
                cOUIAlertDialogMaxLinearLayout.setNeedSetPaddingLayoutId(i3);
            }
        }
        cOUIAlertDialogMaxLinearLayout.setHasMessageMerge(this.mHasMessageMerge);
    }

    private void initSingleContentPadding(Window window) {
        View viewFindViewById = window.findViewById(com.support.dialog.R.id.buttonPanel);
        CharSequence[] charSequenceArr = this.mItems;
        boolean z2 = this.hasTitle || this.hasMessage || this.hasSetView || this.hasAdapter || (charSequenceArr != null && charSequenceArr.length > 0);
        if (this.mIsTinyStyle) {
            if (viewFindViewById == null || z2) {
                return;
            }
            viewFindViewById.setPadding(viewFindViewById.getPaddingLeft(), getContext().getResources().getDimensionPixelOffset(com.support.dialog.R.dimen.coui_tiny_dialog_btn_bar_padding_vertical), viewFindViewById.getPaddingRight(), viewFindViewById.getPaddingBottom());
            return;
        }
        if (!(viewFindViewById instanceof COUIButtonBarLayout)) {
            COUILog.e(TAG, "buttonPanel is error; Need to check whether the application has a layout that covers the coui's");
            return;
        }
        COUIButtonBarLayout cOUIButtonBarLayout = (COUIButtonBarLayout) viewFindViewById;
        cOUIButtonBarLayout.setRecommendButtonId(this.mRecommendButtonId);
        cOUIButtonBarLayout.setRecommendButtonType(this.mRecommendButtonType);
        cOUIButtonBarLayout.setDynamicLayout(this.mButtonLayoutDynamicLayout);
        cOUIButtonBarLayout.setShowDividerWhenHasItems(this.mItems != null);
    }

    private void initTitle(Window window) {
        if (this.mIsTinyStyle || this.mHasLoading) {
            return;
        }
        View viewFindViewById = window.findViewById(com.support.dialog.R.id.title_template);
        if (viewFindViewById == null || !(viewFindViewById instanceof LinearLayout)) {
            COUILog.e(TAG, "title_template is error; Need to check whether the application has a layout that covers the coui's");
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewFindViewById.getLayoutParams();
        layoutParams.topMargin = getContext().getResources().getDimensionPixelOffset(com.support.dialog.R.dimen.coui_no_message_alert_dialog_title_margin_top);
        layoutParams.bottomMargin = getContext().getResources().getDimensionPixelOffset(com.support.dialog.R.dimen.coui_no_message_alert_dialog_title_margin_bottom);
        viewFindViewById.setLayoutParams(layoutParams);
        initTitleScrollView(window, window.findViewById(com.support.dialog.R.id.alert_title_scroll_view));
        initCOUIDialogTitle(window.findViewById(com.support.dialog.R.id.alertTitle));
    }

    private void initTitleScrollView(Window window, View view) {
        if (view == null || !(view instanceof COUIMaxHeightScrollView)) {
            COUILog.e(TAG, "alert_title_scroll_view is error; Need to check whether the application has a layout that covers the coui's");
        } else {
            ((COUIMaxHeightScrollView) view).setFillViewport(true);
        }
    }

    private void initWindow(Window window) {
        if (isFollowHandMode()) {
            COUIBottomAlertDialogAdjustUtil.adjustToFree(window, this.mAnchorView, this.mAnchorViewTouchPoint, this.mExtraOffsetPoint);
            window.getDecorView().setVisibility(4);
        } else {
            Configuration configuration = this.mConfiguration;
            if (configuration == null) {
                configuration = window.getContext().getResources().getConfiguration();
            }
            updateGravityAndAnimation(configuration);
        }
        window.getDecorView().setOnTouchListener(new OutsideTouchListener(this.mDialog));
        setWindowType(window);
        setWindowWidth(window);
    }

    private boolean isFollowHandMode() {
        return (this.mAnchorView == null && this.mAnchorViewTouchPoint == null) ? false : true;
    }

    private boolean isForceCenterStyleInLargeScreen(Configuration configuration) {
        return isLargeScreen(configuration) && this.mIsForceCenterInLargeScreen;
    }

    private boolean isLargeScreen(Configuration configuration) {
        int iPx2dip = configuration.screenWidthDp;
        int iPx2dip2 = configuration.screenHeightDp;
        if (this.mForcePhysicalDimensions) {
            iPx2dip = UIUtil.px2dip(getContext(), UIUtil.getScreenWidthRealSize(getContext()));
            iPx2dip2 = UIUtil.px2dip(getContext(), UIUtil.getScreenHeightRealSize(getContext()));
        }
        return COUIResponsiveUtils.isLargePadWindow(getContext(), iPx2dip, iPx2dip2);
    }

    private boolean isMiddleAndLargeScreen(Configuration configuration) {
        if (this.mAlwaysFollowHand) {
            return true;
        }
        return !COUIResponsiveUtils.isSmallScreenDp(configuration.screenWidthDp);
    }

    private boolean isRecommendAiPayment() {
        return this.mRecommendButtonType == COUIButton.ButtonType.AI_PAYMENT;
    }

    private void operateBlur(View view) {
        boolean zShouldSkipOutlineClipForTheme;
        COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout;
        float attrDimens;
        Drawable drawable;
        boolean zUseBackgroundBlur = this.mBackgroundBlurBuilder.useBackgroundBlur();
        boolean zHasOverlayConfig = this.mOverlayHelper.hasOverlayConfig();
        View viewFindViewById = this.mDialog.getWindow().findViewById(com.support.dialog.R.id.rootView);
        View viewFindViewById2 = this.mDialog.getWindow().findViewById(com.support.dialog.R.id.parentPanel);
        if (this.mBackgroundDrawable == null || zHasOverlayConfig || !(viewFindViewById2 instanceof COUIAlertDialogMaxLinearLayout)) {
            zShouldSkipOutlineClipForTheme = false;
        } else {
            COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout2 = (COUIAlertDialogMaxLinearLayout) viewFindViewById2;
            cOUIAlertDialogMaxLinearLayout2.clearParentPanelBackgroundKeepInsets();
            zShouldSkipOutlineClipForTheme = cOUIAlertDialogMaxLinearLayout2.shouldSkipOutlineClipForTheme();
            if (!zShouldSkipOutlineClipForTheme && viewFindViewById != null) {
                ViewCompat.setBackground(viewFindViewById, this.mBackgroundDrawable);
            }
        }
        if (zUseBackgroundBlur || zHasOverlayConfig) {
            View viewFindViewById3 = this.mDialog.getWindow().findViewById(com.support.dialog.R.id.buttonPanel);
            boolean z2 = true;
            if (viewFindViewById2 instanceof COUIAlertDialogMaxLinearLayout) {
                cOUIAlertDialogMaxLinearLayout = (COUIAlertDialogMaxLinearLayout) viewFindViewById2;
                if (zHasOverlayConfig) {
                    cOUIAlertDialogMaxLinearLayout.setUseDialogOverlayFeature(true);
                    cOUIAlertDialogMaxLinearLayout.bindDialogOverlay(this.mOverlayHelper);
                }
                if (isRecommendAiPayment()) {
                    cOUIAlertDialogMaxLinearLayout.setAiPaymentTextStyleEnabled(true);
                }
            } else {
                cOUIAlertDialogMaxLinearLayout = null;
            }
            if (!zUseBackgroundBlur) {
                if (cOUIAlertDialogMaxLinearLayout != null) {
                    cOUIAlertDialogMaxLinearLayout.setBlurBackgroundWindow(false);
                    cOUIAlertDialogMaxLinearLayout.updateDialogOverlay(COUIAlertDialogOverlayHelper.OverlayMode.OVERLAY_ONLY);
                }
                if (viewFindViewById3 instanceof COUIButtonBarLayout) {
                    ((COUIButtonBarLayout) viewFindViewById3).setBlurBackgroundWindow(false);
                }
                SummaryAdapter summaryAdapter = this.mSummaryAdapter;
                if (summaryAdapter != null) {
                    summaryAdapter.setBlurState(false);
                    return;
                }
                return;
            }
            if (!view.isHardwareAccelerated()) {
                COUILog.e(TAG, "Hardware accelerate is disabled! Set background blur failed.");
                if (!zHasOverlayConfig || cOUIAlertDialogMaxLinearLayout == null) {
                    return;
                }
                cOUIAlertDialogMaxLinearLayout.setBlurBackgroundWindow(false);
                cOUIAlertDialogMaxLinearLayout.updateDialogOverlay(COUIAlertDialogOverlayHelper.OverlayMode.BLUR_DEGRADED);
                if (viewFindViewById3 instanceof COUIButtonBarLayout) {
                    ((COUIButtonBarLayout) viewFindViewById3).setBlurBackgroundWindow(false);
                }
                SummaryAdapter summaryAdapter2 = this.mSummaryAdapter;
                if (summaryAdapter2 != null) {
                    summaryAdapter2.setBlurState(false);
                    return;
                }
                return;
            }
            if (!zShouldSkipOutlineClipForTheme && !zHasOverlayConfig && (drawable = this.mBackgroundDrawable) != null) {
                this.mBackgroundBlurBuilder.setOriginDrawable(drawable);
            }
            this.mBackgroundBlurBuilder.setTargetView(viewFindViewById);
            this.mBackgroundBlurBuilder.setRootView(view);
            boolean zIsBlurryState = this.mBackgroundBlurBuilder.isBlurryState();
            boolean zHasOverlay = this.mOverlayHelper.hasOverlay();
            if (!zHasOverlay && !zIsBlurryState) {
                z2 = false;
            }
            if (cOUIAlertDialogMaxLinearLayout != null) {
                cOUIAlertDialogMaxLinearLayout.setBlurBackgroundWindow(z2);
                if (zHasOverlay) {
                    cOUIAlertDialogMaxLinearLayout.updateDialogOverlay(COUIAlertDialogOverlayHelper.OverlayMode.BLUR_DEGRADED);
                }
            } else {
                COUILog.e(TAG, "operateBlur: parentPanel is not COUIAlertDialogMaxLinearLayout");
            }
            if (viewFindViewById3 instanceof COUIButtonBarLayout) {
                ((COUIButtonBarLayout) viewFindViewById3).setBlurBackgroundWindow(z2);
            } else {
                COUILog.e(TAG, "operateBlur: buttonPanel is not COUIButtonBarLayout");
            }
            SummaryAdapter summaryAdapter3 = this.mSummaryAdapter;
            if (summaryAdapter3 != null) {
                summaryAdapter3.setBlurState(z2);
            }
            this.mBackgroundBlurBuilder.setOnBlurryStateChangeListener(new a(this, cOUIAlertDialogMaxLinearLayout, viewFindViewById3));
            if (COUIVersionUtil.getOSVersionCode() > 37) {
                attrDimens = getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R.dimen.coui_round_corner_xxxxl_radius_large_16_1);
                this.mBackgroundBlurBuilder.setSmoothWeight(getContext().getResources().getFloat(com.support.appcompat.R.dimen.weight_16_1_large));
            } else {
                attrDimens = COUIContextUtil.getAttrDimens(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXXLRadius);
                this.mBackgroundBlurBuilder.setSmoothWeight(COUIContextUtil.getAttrFloat(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXXLWeight));
            }
            if (this.mIsTinyStyle) {
                this.mBackgroundBlurBuilder.setCornerRadius(attrDimens, attrDimens, 0.0f, 0.0f);
            } else {
                this.mBackgroundBlurBuilder.setCornerRadius(attrDimens);
            }
            this.mBackgroundBlurBuilder.setBlurRadius(zHasOverlay ? 0 : getContext().getResources().getInteger(com.support.appcompat.R.integer.coui_dialog_blur_radius));
            this.mBackgroundBlurBuilder.setMode(COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext()) ? 2 : 3);
            this.mBackgroundBlurBuilder.setMixColorLight(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), com.support.appcompat.R.color.coui_dialog_list_mix_blur_light)));
            this.mBackgroundBlurBuilder.setMixColorDark(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), com.support.appcompat.R.color.coui_dialog_list_mix_blur_dark)));
            this.mBackgroundBlurBuilder.setBlendColorLight(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), com.support.appcompat.R.color.coui_dialog_list_blend_blur_light)));
            this.mBackgroundBlurBuilder.setBlendColorDark(UIUtil.colorToFloats(COUIContextUtil.getColor(getContext(), com.support.appcompat.R.color.coui_dialog_list_blend_blur_dark)));
            this.mBackgroundBlurBuilder.applyBlurBackground();
        }
    }

    private void registerApplicationConfigChangeListener() {
        getContext().registerComponentCallbacks(this.mComponentCallbacks);
    }

    private void releaseApplicationConfigChangeListener() {
        if (this.mComponentCallbacks != null) {
            getContext().unregisterComponentCallbacks(this.mComponentCallbacks);
        }
    }

    private void setCustomLayout() {
        if (this.mIsCustomStyle) {
            if (this.mCustomDrawable != null) {
                View viewFindViewById = this.mDialog.findViewById(com.support.dialog.R.id.customImageview);
                if (viewFindViewById instanceof COUIRoundImageView) {
                    COUIRoundImageView cOUIRoundImageView = (COUIRoundImageView) viewFindViewById;
                    if (cOUIRoundImageView != null) {
                        cOUIRoundImageView.setImageDrawable(this.mCustomDrawable);
                        cOUIRoundImageView.setVisibility(0);
                    }
                } else {
                    COUILog.e(TAG, "customImageview is error; Need to check whether the application has a layout that covers the coui's");
                }
            }
            if (this.mCustomTitle != null) {
                View viewFindViewById2 = this.mDialog.findViewById(com.support.dialog.R.id.customTitle);
                if (viewFindViewById2 instanceof TextView) {
                    TextView textView = (TextView) viewFindViewById2;
                    if (textView != null) {
                        textView.setText(this.mCustomTitle);
                        textView.setVisibility(0);
                    }
                } else {
                    COUILog.e(TAG, "customTitle is error; Need to check whether the application has a layout that covers the coui's");
                }
            }
            if (this.mCustomMessage != null) {
                View viewFindViewById3 = this.mDialog.findViewById(com.support.dialog.R.id.customMessage);
                if (!(viewFindViewById3 instanceof TextView)) {
                    COUILog.e(TAG, "customMessage is error; Need to check whether the application has a layout that covers the coui's");
                    return;
                }
                TextView textView2 = (TextView) viewFindViewById3;
                if (textView2 != null) {
                    textView2.setText(this.mCustomMessage);
                    textView2.setVisibility(0);
                }
            }
        }
    }

    private void setHasContentMerge(Window window) {
        View viewFindViewById = window.findViewById(com.support.dialog.R.id.parentPanel);
        if (viewFindViewById instanceof COUIAlertDialogMaxLinearLayout) {
            ((COUIAlertDialogMaxLinearLayout) viewFindViewById).setHasMessageMerge(this.mHasMessageMerge);
        }
    }

    private void setViewHorizontalWeight(View view, int i3) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            layoutParams.height = 0;
            ((LinearLayout.LayoutParams) layoutParams).weight = i3;
            view.setLayoutParams(layoutParams);
        }
    }

    private void setWindowType(Window window) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        int i3 = this.mDialogWindowType;
        if (i3 > 0) {
            attributes.type = i3;
        }
        window.setAttributes(attributes);
    }

    private void setWindowWidth(Window window) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        Configuration configuration = this.mConfiguration;
        if (configuration == null) {
            configuration = window.getContext().getResources().getConfiguration();
        }
        int i3 = configuration.screenLayout;
        int iMin = ((i3 & 48) == 32 || (i3 & 15) == 1) ? -1 : Math.min(UIUtil.dip2px(getContext(), configuration.screenWidthDp), this.mContentMaxWidth);
        if (isFollowHandMode()) {
            iMin = -2;
        }
        attributes.width = iMin;
        window.setAttributes(attributes);
    }

    private void unregisterAccessibilityListeners() {
        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
        if (accessibilityManager == null) {
            return;
        }
        AccessibilityManager.AccessibilityStateChangeListener accessibilityStateChangeListener = this.mAccessibilityStateChangeListener;
        if (accessibilityStateChangeListener != null) {
            accessibilityManager.removeAccessibilityStateChangeListener(accessibilityStateChangeListener);
            this.mAccessibilityStateChangeListener = null;
        }
        AccessibilityManager.TouchExplorationStateChangeListener touchExplorationStateChangeListener = this.mTouchExplorationStateChangeListener;
        if (touchExplorationStateChangeListener != null) {
            this.mAccessibilityManager.removeTouchExplorationStateChangeListener(touchExplorationStateChangeListener);
            this.mTouchExplorationStateChangeListener = null;
        }
        this.mAccessibilityManager = null;
    }

    private void updateAccessibilityState(AlertDialog alertDialog) {
        AccessibilityManager accessibilityManager;
        if (alertDialog == null || !alertDialog.isShowing() || (accessibilityManager = this.mAccessibilityManager) == null) {
            return;
        }
        Boolean boolValueOf = Boolean.valueOf(accessibilityManager.isEnabled() && this.mAccessibilityManager.isTouchExplorationEnabled());
        this.mIsAccessibilityEnabled = boolValueOf;
        if (boolValueOf.booleanValue()) {
            alertDialog.setCanceledOnTouchOutside(false);
        }
    }

    private void updateGravityAndAnimation(Configuration configuration) {
        if (isForceCenterStyleInLargeScreen(configuration)) {
            this.mIsForceCenterStyleStatus = true;
            this.mDialog.getWindow().setGravity(17);
            this.mDialog.getWindow().setWindowAnimations(DEF_WINDOW_ANIM);
        } else {
            this.mIsForceCenterStyleStatus = false;
            this.mDialog.getWindow().setGravity(this.mGravity);
            this.mDialog.getWindow().setWindowAnimations(this.mWindowAnimStyleRes);
        }
    }

    public static Context wrapColorContext(Context context, int i3, int i4) {
        return new ContextThemeWrapper(new ContextThemeWrapper(context, i3), i4);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog create() {
        initCustomPanel();
        initAdapter();
        AlertDialog alertDialogCreate = super.create();
        this.mDialog = alertDialogCreate;
        SummaryAdapter summaryAdapter = this.mSummaryAdapter;
        if (summaryAdapter != null) {
            summaryAdapter.setHostDialog(alertDialogCreate);
        }
        if (COUIVersionUtil.checkOPlusViewSubSDK(30, 16)) {
            this.mDialog.getWindow().getAttributes();
        }
        initWindow(this.mDialog.getWindow());
        return this.mDialog;
    }

    public AlertDialog createWithExtraOffset(View view, int i3, int i4) {
        return createWithExtraOffset(view, 0, 0, i3, i4);
    }

    public void enforceChangeScreenWidth(int i3) {
        if (i3 < 0) {
            Log.d(TAG, "enforceChangeScreenWidth : given value not satisfy : preferWidth =" + i3);
            return;
        }
        try {
            Resources resources = getContext().getResources();
            Configuration configuration = resources.getConfiguration();
            this.mOriginWidth = configuration.screenWidthDp;
            configuration.screenWidthDp = i3;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(TAG, "enforceChangeScreenWidth : OriginWidth=" + this.mOriginWidth + " ,PreferWidth:" + i3);
        } catch (Exception unused) {
            Log.d(TAG, "enforceChangeScreenWidth : failed to updateConfiguration");
        }
    }

    public View getAnchorView() {
        return this.mAnchorView;
    }

    public COUIBackgroundBlurBuilder getBackgroundBlurBuilder() {
        return this.mBackgroundBlurBuilder;
    }

    public int getBottomAlertDialogWindowAnimStyle(Context context) {
        return (isMiddleAndLargeScreen(context.getResources().getConfiguration()) && isFollowHandMode()) ? com.support.dialog.R.style.Animation_COUI_DialogListWindow : this.mWindowAnimStyleRes;
    }

    public int getBottomAlertDialogWindowGravity(Context context) {
        if (isMiddleAndLargeScreen(context.getResources().getConfiguration()) && isFollowHandMode()) {
            return 51;
        }
        return this.mGravity;
    }

    public void initAdapter() {
        CharSequence[] charSequenceArr;
        COUIListDialogAdapter cOUIListDialogAdapter = this.mCOUIListDialogAdapter;
        if (cOUIListDialogAdapter != null) {
            cOUIListDialogAdapter.setIsTop((this.hasTitle || this.hasMessage) ? false : true);
            this.mCOUIListDialogAdapter.setIsBottom((this.hasSetView || this.hasSetButton) ? false : true);
        }
        ChoiceListAdapter choiceListAdapter = this.mChoiceListAdapter;
        if (choiceListAdapter != null) {
            choiceListAdapter.setIsTop((this.hasTitle || this.hasMessage) ? false : true);
            this.mChoiceListAdapter.setIsBottom((this.hasSetView || this.hasSetButton) ? false : true);
        }
        if (this.hasAdapter || (charSequenceArr = this.mItems) == null || charSequenceArr.length <= 0) {
            return;
        }
        SummaryAdapter summaryAdapter = new SummaryAdapter(getContext(), (this.hasTitle || this.hasMessage) ? false : true, (this.hasSetView || this.hasSetButton) ? false : true, this.mItems, this.mSummaryItems, this.mTextColor, this.mItemClickListener);
        this.mSummaryAdapter = summaryAdapter;
        setAdapter((ListAdapter) summaryAdapter, this.mItemClickListener);
    }

    public void restoreScreenWidth() {
        if (this.mOriginWidth == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.screenWidthDp = this.mOriginWidth;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(TAG, "restoreScreenWidth : OriginWidth=" + this.mOriginWidth);
            this.mOriginWidth = -1;
        } catch (Exception unused) {
            Log.d(TAG, "restoreScreenWidth : failed to updateConfiguration");
        }
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setAdapter(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
        return setAdapter(listAdapter, onClickListener);
    }

    public COUIAlertDialogBuilder setAlwaysFollowHand(boolean z2) {
        this.mAlwaysFollowHand = z2;
        return this;
    }

    public COUIAlertDialogBuilder setAnchorView(View view) {
        this.mAnchorView = view;
        return this;
    }

    public COUIAlertDialogBuilder setAnchorViewTouchPoint(Point point) {
        this.mAnchorViewTouchPoint = point;
        return this;
    }

    public COUIAlertDialogBuilder setBackgroundDrawable(Drawable drawable) {
        if (this.mBackgroundDrawable != drawable && drawable != null) {
            this.mBackgroundDrawable = drawable;
        }
        return this;
    }

    public COUIAlertDialogBuilder setBlurBackgroundDrawable(boolean z2) {
        setBlurBackgroundDrawable(z2, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN);
        return this;
    }

    public void setBlurryStateChangeListener(COUIBackgroundBlurBuilder.OnBlurryStateChangeListener onBlurryStateChangeListener) {
        this.mBlurryStateChangeListener = onBlurryStateChangeListener;
        onBlurryStateChangeListener.onBlurryStateChanged(this.mBackgroundBlurBuilder.isBlurryState());
    }

    public COUIAlertDialogBuilder setButtonLayoutDynamicLayout(boolean z2) {
        this.mButtonLayoutDynamicLayout = z2;
        return this;
    }

    public COUIAlertDialogBuilder setCustomDialogPaddingBottom(int i3) {
        this.mCustomDialogPaddingBottom = i3;
        return this;
    }

    public COUIAlertDialogBuilder setCustomDrawable(Drawable drawable) {
        this.mCustomDrawable = drawable;
        return this;
    }

    public COUIAlertDialogBuilder setCustomMessage(CharSequence charSequence) {
        this.mCustomMessage = charSequence;
        return this;
    }

    public COUIAlertDialogBuilder setCustomTitle(String str) {
        this.mCustomTitle = str;
        return this;
    }

    public COUIAlertDialogBuilder setDialogOverlay(Drawable drawable) {
        this.mOverlayHelper.setDialogOverlay(drawable);
        return this;
    }

    public COUIAlertDialogBuilder setExtraOffsetPoint(Point point) {
        this.mExtraOffsetPoint = point;
        return this;
    }

    public COUIAlertDialogBuilder setForcePhysicalDimensions(boolean z2) {
        this.mForcePhysicalDimensions = z2;
        return this;
    }

    public COUIAlertDialogBuilder setHasMessageMerge(boolean z2) {
        this.mHasMessageMerge = z2;
        return this;
    }

    public void setHasSetButton(boolean z2) {
        this.hasSetButton = z2;
    }

    public COUIAlertDialogBuilder setIsForceCenterInLargeScreen(boolean z2) {
        this.mIsForceCenterInLargeScreen = z2;
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setItems(int i3, DialogInterface.OnClickListener onClickListener) {
        return setItems(i3, onClickListener);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setMessage(int i3) {
        return setMessage(i3);
    }

    public COUIAlertDialogBuilder setNeedToAdaptMessageAndList(boolean z2) {
        this.mIsNeedToAdaptMessageAndList = z2;
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setNegativeButton(int i3, DialogInterface.OnClickListener onClickListener) {
        return setNegativeButton(i3, onClickListener);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setNeutralButton(int i3, DialogInterface.OnClickListener onClickListener) {
        return setNeutralButton(i3, onClickListener);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setPositiveButton(int i3, DialogInterface.OnClickListener onClickListener) {
        return setPositiveButton(i3, onClickListener);
    }

    public COUIAlertDialogBuilder setRecommendButtonType(COUIButton.ButtonType buttonType) {
        this.mRecommendButtonType = buttonType;
        return this;
    }

    public COUIAlertDialogBuilder setRegisterConfigurationChangeCallBack(boolean z2) {
        this.mRegisterConfigurationChangeCallBack = z2;
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setSingleChoiceItems(ListAdapter listAdapter, int i3, DialogInterface.OnClickListener onClickListener) {
        return setSingleChoiceItems(listAdapter, i3, onClickListener);
    }

    public COUIAlertDialogBuilder setSummaryItems(int i3) {
        this.mSummaryItems = getContext().getResources().getTextArray(i3);
        return this;
    }

    public void setSupportDynamicMarginTop(boolean z2) {
        this.mSupportDynamicMarginTop = z2;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setTitle(int i3) {
        return setTitle(i3);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setView(int i3) {
        this.hasSetView = true;
        return super.setView(i3);
    }

    public COUIAlertDialogBuilder setWindowAnimStyle(int i3) {
        this.mWindowAnimStyleRes = i3;
        return this;
    }

    public COUIAlertDialogBuilder setWindowGravity(int i3) {
        this.mGravity = i3;
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog show() {
        AlertDialog alertDialogShow = super.show();
        disabledTitleScroll(alertDialogShow);
        updateViewAfterShown();
        return alertDialogShow;
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0034  */
    /* JADX WARN: Code duplicated, block: B:14:0x0038  */
    /* JADX WARN: Code duplicated, block: B:16:0x0044  */
    /* JADX WARN: Code duplicated, block: B:17:0x0058  */
    /* JADX WARN: Code duplicated, block: B:20:0x0061  */
    /* JADX WARN: Code duplicated, block: B:22:0x006d  */
    /* JADX WARN: Code duplicated, block: B:23:0x0081  */
    public void updateGravityWhileConfigChange(Configuration configuration) {
        View viewFindViewById;
        View viewFindViewById2;
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            int i3 = this.mOldConfigurationWidthDP;
            int i4 = configuration.screenWidthDp;
            if (i3 == i4 || i4 != alertDialog.getContext().getResources().getConfiguration().screenWidthDp) {
                int i5 = this.mOldConfigurationHeightDP;
                int i6 = configuration.screenHeightDp;
                if (i5 != i6 && i6 == this.mDialog.getContext().getResources().getConfiguration().screenHeightDp) {
                    if (this.hasTitle) {
                        viewFindViewById2 = this.mDialog.findViewById(com.support.dialog.R.id.alert_title_scroll_view);
                        if (viewFindViewById2 instanceof COUIMaxHeightScrollView) {
                            ((COUIMaxHeightScrollView) viewFindViewById2).setMaxHeight(getContext().getResources().getDimensionPixelSize(com.support.dialog.R.dimen.coui_alert_dialog_builder_title_scroll_max_height));
                        } else {
                            COUILog.e(TAG, "alert_title_scroll_view is error; Need to check whether the application has a layout that covers the coui's");
                        }
                    }
                    if (this.hasMessage) {
                        viewFindViewById = this.mDialog.findViewById(com.support.dialog.R.id.scrollView);
                        if (viewFindViewById instanceof COUIMaxHeightNestedScrollView) {
                            ((COUIMaxHeightNestedScrollView) viewFindViewById).setMaxHeight(getContext().getResources().getDimensionPixelSize(com.support.dialog.R.dimen.coui_alert_dialog_builder_content_max_height));
                        } else {
                            COUILog.e(TAG, "scrollView is error; Need to check whether the application has a layout that covers the coui's");
                        }
                    }
                }
            } else {
                if (this.hasTitle) {
                    viewFindViewById2 = this.mDialog.findViewById(com.support.dialog.R.id.alert_title_scroll_view);
                    if (viewFindViewById2 instanceof COUIMaxHeightScrollView) {
                        ((COUIMaxHeightScrollView) viewFindViewById2).setMaxHeight(getContext().getResources().getDimensionPixelSize(com.support.dialog.R.dimen.coui_alert_dialog_builder_title_scroll_max_height));
                    } else {
                        COUILog.e(TAG, "alert_title_scroll_view is error; Need to check whether the application has a layout that covers the coui's");
                    }
                }
                if (this.hasMessage) {
                    viewFindViewById = this.mDialog.findViewById(com.support.dialog.R.id.scrollView);
                    if (viewFindViewById instanceof COUIMaxHeightNestedScrollView) {
                        ((COUIMaxHeightNestedScrollView) viewFindViewById).setMaxHeight(getContext().getResources().getDimensionPixelSize(com.support.dialog.R.dimen.coui_alert_dialog_builder_content_max_height));
                    } else {
                        COUILog.e(TAG, "scrollView is error; Need to check whether the application has a layout that covers the coui's");
                    }
                }
            }
            this.mOldConfigurationWidthDP = configuration.screenWidthDp;
            this.mOldConfigurationHeightDP = configuration.screenHeightDp;
            if (!isFollowHandMode()) {
                if (this.mIsForceCenterStyleStatus != isForceCenterStyleInLargeScreen(configuration)) {
                    updateGravityAndAnimation(configuration);
                }
                setWindowWidth(this.mDialog.getWindow());
                return;
            }
            this.mAnchorViewTouchPoint = null;
            this.mAnchorView = null;
            if (this.mContentView != null) {
                View viewFindViewById3 = this.mDialog.getWindow().findViewById(com.support.dialog.R.id.custom);
                if (viewFindViewById3 instanceof FrameLayout) {
                    ((FrameLayout) viewFindViewById3).removeView(this.mContentView);
                } else {
                    COUILog.e(TAG, "custom is error; Need to check whether the application has a layout that covers the coui's");
                }
            }
            this.mDialog.dismiss();
            show();
        }
    }

    public void updateViewAfterShown() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null) {
            return;
        }
        initTitle(alertDialog.getWindow());
        initMessagePadding();
        initCustomPanelVisibility(this.mDialog.getWindow());
        initListPanel(this.mDialog.getWindow());
        initContentSpacing(this.mDialog.getWindow());
        initContentMaxWidth(this.mDialog.getWindow());
        initContentMaxHeight(this.mDialog.getWindow());
        initSingleContentPadding(this.mDialog.getWindow());
        initContentPanelClipChildren();
        setCustomLayout();
        setHasContentMerge(this.mDialog.getWindow());
        initBlurListener();
        setWindowWidth(this.mDialog.getWindow());
        initAccessibilityFeatures();
        applyAiPaymentTextStyleIfNeeded();
    }

    public COUIAlertDialogBuilder(Context context, int i3) {
        super(new ContextThemeWrapper(context, i3));
        this.mIsAccessibilityEnabled = Boolean.FALSE;
        this.mAccessibilityListenerAttached = false;
        this.mBlurListenerAttached = false;
        this.hasTitle = false;
        this.hasMessage = false;
        this.hasAdapter = false;
        this.mHasLoading = false;
        this.isAssignMentLayout = false;
        this.mCOUIListDialogAdapter = null;
        this.hasSetView = false;
        this.mDialogWindowType = 0;
        this.mChoiceListAdapter = null;
        this.mSummaryAdapter = null;
        this.hasSetButton = false;
        this.mAnchorView = null;
        this.mAnchorViewTouchPoint = null;
        this.mExtraOffsetPoint = null;
        this.mOriginWidth = -1;
        this.mButtonLayoutDynamicLayout = true;
        this.mIsTinyStyle = false;
        this.mRegisterConfigurationChangeCallBack = true;
        this.mIsForceCenterInLargeScreen = false;
        this.mForcePhysicalDimensions = false;
        this.mRecommendButtonId = -1;
        this.mRecommendButtonType = COUIButton.ButtonType.PRIMARY;
        this.mIsCustomStyle = false;
        this.mHasMessageMerge = true;
        this.mSupportDynamicMarginTop = true;
        this.mComponentCallbacks = new AnonymousClass5();
        this.mDialogStyle = i3;
        initAttrs();
        initBlurBuilder(context);
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(RomUpdateListParser.ACCESSIBILITY_TAG);
    }

    public AlertDialog createWithExtraOffset(View view, int i3, int i4, int i5, int i6) {
        if (isMiddleAndLargeScreen(getContext().getResources().getConfiguration())) {
            this.mAnchorView = view;
            if (i3 != 0 || i4 != 0) {
                Point point = new Point();
                this.mAnchorViewTouchPoint = point;
                point.set(i3, i4);
            }
            if (i5 != 0 || i6 != 0) {
                Point point2 = new Point();
                this.mExtraOffsetPoint = point2;
                point2.set(i5, i6);
            }
        }
        return create();
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setAdapter(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
        this.hasAdapter = listAdapter != null;
        if (listAdapter instanceof COUIListDialogAdapter) {
            this.mCOUIListDialogAdapter = (COUIListDialogAdapter) listAdapter;
        }
        super.setAdapter(listAdapter, onClickListener);
        return this;
    }

    public COUIAlertDialogBuilder setBlurBackgroundDrawable(boolean z2, AnimLevel animLevel) {
        this.mBackgroundBlurBuilder.setUseBackgroundBlur(z2, animLevel);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
        return setItems(charSequenceArr, onClickListener);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setMessage(CharSequence charSequence) {
        return setMessage(charSequence);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        return setNegativeButton(charSequence, onClickListener);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        return setNeutralButton(charSequence, onClickListener);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        return setPositiveButton(charSequence, onClickListener);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setSingleChoiceItems(ListAdapter listAdapter, int i3, DialogInterface.OnClickListener onClickListener) {
        this.hasAdapter = listAdapter != null;
        super.setSingleChoiceItems(listAdapter, i3, onClickListener);
        return this;
    }

    public COUIAlertDialogBuilder setSummaryItems(CharSequence[] charSequenceArr) {
        this.mSummaryItems = charSequenceArr;
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public /* bridge */ /* synthetic */ AlertDialog.Builder setTitle(CharSequence charSequence) {
        return setTitle(charSequence);
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setItems(int i3, DialogInterface.OnClickListener onClickListener) {
        this.mItems = getContext().getResources().getTextArray(i3);
        this.mItemClickListener = onClickListener;
        super.setItems(i3, onClickListener);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setMessage(CharSequence charSequence) {
        this.hasMessage = !TextUtils.isEmpty(charSequence);
        super.setMessage(charSequence);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setNegativeButton(int i3, DialogInterface.OnClickListener onClickListener) {
        super.setNegativeButton(i3, onClickListener);
        setHasSetButton(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setNeutralButton(int i3, DialogInterface.OnClickListener onClickListener) {
        super.setNeutralButton(i3, onClickListener);
        setHasSetButton(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setPositiveButton(int i3, DialogInterface.OnClickListener onClickListener) {
        super.setPositiveButton(i3, onClickListener);
        setHasSetButton(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setTitle(CharSequence charSequence) {
        this.hasTitle = !TextUtils.isEmpty(charSequence);
        super.setTitle(charSequence);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setView(View view) {
        this.hasSetView = true;
        this.mContentView = view;
        return super.setView(view);
    }

    public AlertDialog show(View view) {
        if (!isMiddleAndLargeScreen(getContext().getResources().getConfiguration())) {
            view = null;
        }
        this.mAnchorView = view;
        return show();
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setMessage(int i3) {
        this.hasMessage = !TextUtils.isEmpty(getContext().getString(i3));
        super.setMessage(i3);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        super.setNegativeButton(charSequence, onClickListener);
        setHasSetButton(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        super.setNeutralButton(charSequence, onClickListener);
        setHasSetButton(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        super.setPositiveButton(charSequence, onClickListener);
        setHasSetButton(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setTitle(int i3) {
        this.hasTitle = !TextUtils.isEmpty(getContext().getString(i3));
        super.setTitle(i3);
        return this;
    }

    public COUIAlertDialogBuilder setWindowType(int i3) {
        this.mDialogWindowType = i3;
        return this;
    }

    public COUIAlertDialogBuilder setItems(int i3, DialogInterface.OnClickListener onClickListener, int[] iArr) {
        this.mItems = getContext().getResources().getTextArray(i3);
        this.mItemClickListener = onClickListener;
        this.mTextColor = iArr;
        super.setItems(i3, onClickListener);
        return this;
    }

    public COUIAlertDialogBuilder setNegativeButton(int i3, DialogInterface.OnClickListener onClickListener, boolean z2) {
        super.setNegativeButton(i3, onClickListener);
        setHasSetButton(true);
        if (z2) {
            this.mRecommendButtonId = android.R.id.button2;
        }
        return this;
    }

    public COUIAlertDialogBuilder setNeutralButton(int i3, DialogInterface.OnClickListener onClickListener, boolean z2) {
        super.setNeutralButton(i3, onClickListener);
        setHasSetButton(true);
        if (z2) {
            this.mRecommendButtonId = android.R.id.button3;
        }
        return this;
    }

    public COUIAlertDialogBuilder setPositiveButton(int i3, DialogInterface.OnClickListener onClickListener, boolean z2) {
        super.setPositiveButton(i3, onClickListener);
        setHasSetButton(true);
        if (z2) {
            this.mRecommendButtonId = android.R.id.button1;
        }
        return this;
    }

    public AlertDialog create(View view) {
        if (!isMiddleAndLargeScreen(getContext().getResources().getConfiguration())) {
            view = null;
        }
        this.mAnchorView = view;
        return create();
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public COUIAlertDialogBuilder setItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
        this.mItems = charSequenceArr;
        this.mItemClickListener = onClickListener;
        super.setItems(charSequenceArr, onClickListener);
        return this;
    }

    public COUIAlertDialogBuilder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener, boolean z2) {
        super.setNegativeButton(charSequence, onClickListener);
        setHasSetButton(true);
        if (z2) {
            this.mRecommendButtonId = android.R.id.button2;
        }
        return this;
    }

    public COUIAlertDialogBuilder setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener, boolean z2) {
        super.setNeutralButton(charSequence, onClickListener);
        setHasSetButton(true);
        if (z2) {
            this.mRecommendButtonId = android.R.id.button3;
        }
        return this;
    }

    public COUIAlertDialogBuilder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener, boolean z2) {
        super.setPositiveButton(charSequence, onClickListener);
        setHasSetButton(true);
        if (z2) {
            this.mRecommendButtonId = android.R.id.button1;
        }
        return this;
    }

    public AlertDialog create(View view, Point point) {
        return create(view, point.x, point.y);
    }

    public AlertDialog create(View view, int i3, int i4) {
        return createWithExtraOffset(view, i3, i4, 0, 0);
    }

    public COUIAlertDialogBuilder(Context context, int i3, int i4) {
        super(wrapColorContext(context, i3, i4));
        this.mIsAccessibilityEnabled = Boolean.FALSE;
        this.mAccessibilityListenerAttached = false;
        this.mBlurListenerAttached = false;
        this.hasTitle = false;
        this.hasMessage = false;
        this.hasAdapter = false;
        this.mHasLoading = false;
        this.isAssignMentLayout = false;
        this.mCOUIListDialogAdapter = null;
        this.hasSetView = false;
        this.mDialogWindowType = 0;
        this.mChoiceListAdapter = null;
        this.mSummaryAdapter = null;
        this.hasSetButton = false;
        this.mAnchorView = null;
        this.mAnchorViewTouchPoint = null;
        this.mExtraOffsetPoint = null;
        this.mOriginWidth = -1;
        this.mButtonLayoutDynamicLayout = true;
        this.mIsTinyStyle = false;
        this.mRegisterConfigurationChangeCallBack = true;
        this.mIsForceCenterInLargeScreen = false;
        this.mForcePhysicalDimensions = false;
        this.mRecommendButtonId = -1;
        this.mRecommendButtonType = COUIButton.ButtonType.PRIMARY;
        this.mIsCustomStyle = false;
        this.mHasMessageMerge = true;
        this.mSupportDynamicMarginTop = true;
        this.mComponentCallbacks = new AnonymousClass5();
        initAttrs();
        initBlurBuilder(context);
    }
}