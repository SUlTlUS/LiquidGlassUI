package com.coui.appcompat.panel;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder;
import com.coui.appcompat.uiutil.UIUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oplus.settings.appfunctions.SystemUpdateScreenStateConfig;
import com.support.panel.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private static final float PHYSICS_UNSET = Float.MIN_VALUE;
    private static final String SAVE_ALWAY_BLUR_KEY = "SAVE_ALWAY_BLUR_KEY";
    private static final String SAVE_BACK_EVENT_PASS_THROUGH_WHILE_DISMISSING = "SAVE_BACK_EVENT_PASS_THROUGH_WHILE_DISMISSING";
    private static final String SAVE_BLUR_BACKGROUND_KEY = "SAVE_BLUR_BACKGROUND_KEY";
    private static final String SAVE_CAN_PULL_UP_KEY = "SAVE_CAN_PULL_UP_KEY_DRAGGABLE_KEY";
    private static final String SAVE_DRAGGABLE_KEY = "SAVE_DRAGGABLE_KEY";
    private static final String SAVE_FIRST_SHOW_COLLAPSED_KEY = "SAVE_FIRST_SHOW_COLLAPSED_KEY";
    private static final String SAVE_FOLLOW_HAND_FALLBACK_BOTTOM_CENTER_KEY = "SAVE_FOLLOW_HAND_FALLBACK_BOTTOM_CENTER_KEY";
    private static final String SAVE_FOLLOW_HAND_PLACEMENT_ALLOWED_FLAGS_KEY = "SAVE_FOLLOW_HAND_PLACEMENT_ALLOWED_FLAGS_KEY";
    private static final String SAVE_FOLLOW_HAND_USE_ANCHOR_ACTUAL_DISPLAY_RECT_KEY = "SAVE_FOLLOW_HAND_USE_ANCHOR_ACTUAL_DISPLAY_RECT_KEY";
    private static final String SAVE_FRAME_RATE_KEY = "SAVE_FRAME_RATE_KEY";
    private static final String SAVE_HAS_SET_PEEK_HEIGHT_KEY = "SAVE_HAS_SET_PEEK_HEIGHT_KEY";
    private static final String SAVE_HAS_SET_SKIP_COLLAPSED_KEY = "SAVE_HAS_SET_SKIP_COLLAPSED_KEY";
    private static final String SAVE_IS_HANDLE_PANEL_KEY = "SAVE_IS_HANDLE_PANEL_KEY";
    private static final String SAVE_IS_IN_TINY_SCREEN_PANEL_KEY = "SAVE_IS_IN_TINY_SCREEN_PANEL_KEY";
    private static final String SAVE_MANUAL_APPEARANCE_LIGHT_NAVIGATION_BAR = "SAVE_MANUAL_APPEARANCE_LIGHT_NAVIGATION_BAR";
    private static final String SAVE_PANEL_EDGE_TO_EDGE = "SAVE_PANEL_EDGE_TO_EDGE";
    private static final String SAVE_PANEL_HEIGHT_KEY = "SAVE_PANEL_HEIGHT_KEY";
    private static final String SAVE_PANEL_WIDTH_KEY = "SAVE_PANEL_WIDTH_KEY";
    private static final String SAVE_PEEK_HEIGHT_KEY = "SAVE_PEEK_HEIGHT_KEY";
    private static final String SAVE_REGISTER_CONFIGURATION_KEY = "SAVE_REGISTER_CONFIGURATION_KEY";
    private static final String SAVE_SHOW_IN_MAX_HEIGHT_KEY = "SAVE_SHOW_IN_MAX_HEIGHT_KEY";
    private static final String SAVE_SKIP_COLLAPSED_KEY = "SAVE_SKIP_COLLAPSED_KEY";
    private static final String SAVE_SUPPORT_EXIT_BLOCKING_ANIMATION = "SAVE_SUPPORT_EXIT_BLOCKING_ANIMATION";
    private static final String SAVE_USE_NORMAL_SMOOTH_CORNER = "SAVE_USE_NORMAL_SMOOTH_CORNER";
    private static final String TAG = "COUIBottomSheetDialogFragment";
    private static final int UNSET_WIDTH = -1;

    @Deprecated
    private long mAlphaAnimDuration;
    private boolean mAlwayBlur;
    private View mAnchorView;
    private boolean mBackEventPassThroughWhileDismissing;
    private boolean mBackgroundBlur;
    private BottomSheetBehavior<FrameLayout> mBehavior;
    private boolean mBluring;
    private COUIBottomSheetDialog mBottomSheetDialog;
    private COUIBottomSheetDialog.BottomSheetDialogAnimatorListener mBottomSheetDialogAnimatorListener;
    private boolean mCanPullUp;
    private boolean mCouiPanelEdgeToEdgeEnable;
    private COUIPanelFragment mCurrentPanelFragment;
    private int mCurrentPanelHeight;
    private View mDialogFragmentView;
    private boolean mDisableSubExpand;

    @Deprecated
    private int mFinalNavColorAfterDismiss;
    private boolean mFirstShowCollapsed;
    private boolean mFollowHandFallbackVerticalEdgeWhenInsufficientSpace;
    private int mFollowHandPlacementAllowedFlags;
    private boolean mFollowHandUseAnchorActualDisplayRect;
    private boolean mFrameRate;
    private boolean mGlobalDrag;
    private boolean mHasSetPeekHeight;
    private boolean mHasSetSkipCollapsed;
    private int mHideDragViewHeight;
    private InputMethodManager mInputMethodManager;
    private boolean mIsDraggable;

    @Deprecated
    private boolean mIsExecuteNavColorAnimAfterDismiss;
    private boolean mIsFullScreenInTinyScreen;
    private boolean mIsGestureNavigation;
    private boolean mIsHandlePanel;
    private boolean mIsInTinyScreen;
    private boolean mIsSavedInstanceState;
    private boolean mIsShowInMaxHeight;
    private boolean mManualAppearanceLightNavigationBars;
    private OnDismissListener mOnDismissListener;
    private View mOutSideView;
    private ViewGroup mPanelContainer;
    private int mPanelHeight;
    private int mPanelWidth;
    private int mPeekHeight;
    private int mPendingPanelRemoveGeneration;
    private COUIPanelFragment mPendingRemovePanelFragment;
    private final Runnable mPendingRemoveRunnable;
    private int mPendingRemoveRunnableGeneration;
    private float mPhysicsDampingRatio;
    private float mPhysicsFrequency;
    private int mPreferWidth;
    private boolean mRegisterConfigurationChangeCallBack;
    private boolean mShouldRegisterWindowInsetsListener;
    private boolean mSkipCollapsed;
    private boolean mSkipStatusBarTransparentAndFont;
    private boolean mSupportExitBlockingAnimation;
    private boolean mUseNormalSmoothCorner;

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialogFragment$1, reason: invalid class name */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIBottomSheetDialogFragment.access$000(COUIBottomSheetDialogFragment.this) != COUIBottomSheetDialogFragment.access$100(COUIBottomSheetDialogFragment.this)) {
                return;
            }
            if (!COUIBottomSheetDialogFragment.this.isAdded() || COUIBottomSheetDialogFragment.this.getView() == null) {
                COUIBottomSheetDialogFragment.access$202(COUIBottomSheetDialogFragment.this, null);
                return;
            }
            COUIPanelFragment cOUIPanelFragmentAccess$200 = COUIBottomSheetDialogFragment.access$200(COUIBottomSheetDialogFragment.this);
            if (cOUIPanelFragmentAccess$200 == null) {
                return;
            }
            if (!cOUIPanelFragmentAccess$200.isAdded() || !cOUIPanelFragmentAccess$200.isHidden() || cOUIPanelFragmentAccess$200 == COUIBottomSheetDialogFragment.access$300(COUIBottomSheetDialogFragment.this)) {
                COUIBottomSheetDialogFragment.access$202(COUIBottomSheetDialogFragment.this, null);
                return;
            }
            cOUIPanelFragmentAccess$200.onAbandon(Boolean.FALSE);
            COUIBottomSheetDialogFragment.this.getChildFragmentManager().beginTransaction().remove(cOUIPanelFragmentAccess$200).commitAllowingStateLoss();
            if (COUIBottomSheetDialogFragment.access$200(COUIBottomSheetDialogFragment.this) == cOUIPanelFragmentAccess$200) {
                COUIBottomSheetDialogFragment.access$202(COUIBottomSheetDialogFragment.this, null);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialogFragment$2, reason: invalid class name */
    public class AnonymousClass2 implements COUIBackgroundBlurBuilder.OnBlurryStateChangeListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder.OnBlurryStateChangeListener
        public void onBlurryStateChanged(boolean z2) {
            if (COUIBottomSheetDialogFragment.access$400(COUIBottomSheetDialogFragment.this) != z2) {
                COUIBottomSheetDialogFragment.access$402(COUIBottomSheetDialogFragment.this, z2);
            }
            if (COUIBottomSheetDialogFragment.access$300(COUIBottomSheetDialogFragment.this) != null) {
                COUIBottomSheetDialogFragment.access$300(COUIBottomSheetDialogFragment.this).setIsBluring(z2);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialogFragment$3, reason: invalid class name */
    public class AnonymousClass3 implements Runnable {

        /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialogFragment$3$1, reason: invalid class name */
        public class AnonymousClass1 implements View.OnTouchListener {
            public AnonymousClass1() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent != null && motionEvent.getAction() == 1) {
                    COUIBottomSheetDialogFragment.access$500(COUIBottomSheetDialogFragment.this).dismiss();
                }
                return true;
            }
        }

        public AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIBottomSheetDialogFragment.access$300(COUIBottomSheetDialogFragment.this) == null || COUIBottomSheetDialogFragment.access$500(COUIBottomSheetDialogFragment.this) == null) {
                return;
            }
            COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment = COUIBottomSheetDialogFragment.this;
            COUIBottomSheetDialogFragment.access$602(cOUIBottomSheetDialogFragment, COUIBottomSheetDialogFragment.access$500(cOUIBottomSheetDialogFragment).findViewById(R.id.touch_outside));
            if (COUIBottomSheetDialogFragment.access$600(COUIBottomSheetDialogFragment.this) != null) {
                COUIBottomSheetDialogFragment.access$600(COUIBottomSheetDialogFragment.this).setOnTouchListener(new AnonymousClass1());
            }
            COUIBottomSheetDialogFragment.access$702(COUIBottomSheetDialogFragment.this, false);
            COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment2 = COUIBottomSheetDialogFragment.this;
            COUIBottomSheetDialogFragment.access$800(cOUIBottomSheetDialogFragment2, COUIBottomSheetDialogFragment.access$300(cOUIBottomSheetDialogFragment2));
            COUIBottomSheetDialogFragment.access$500(COUIBottomSheetDialogFragment.this).setDragableLinearLayout(COUIBottomSheetDialogFragment.access$300(COUIBottomSheetDialogFragment.this).getDraggableLinearLayout(), false);
            COUIBottomSheetDialogFragment.access$300(COUIBottomSheetDialogFragment.this).onShow(Boolean.TRUE);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialogFragment$4, reason: invalid class name */
    public class AnonymousClass4 implements COUIPanelFragment.PanelFragmentAnimationListener {
        final /* synthetic */ COUIPanelFragment val$f;
        final /* synthetic */ int val$removeGeneration;

        public AnonymousClass4(int i3, COUIPanelFragment cOUIPanelFragment) {
            this.val$removeGeneration = i3;
            this.val$f = cOUIPanelFragment;
        }

        @Override // com.coui.appcompat.panel.COUIPanelFragment.PanelFragmentAnimationListener
        public void onAnimationEnd() {
            if (this.val$removeGeneration == COUIBottomSheetDialogFragment.access$100(COUIBottomSheetDialogFragment.this) && this.val$f.isAdded() && this.val$f.isHidden() && this.val$f != COUIBottomSheetDialogFragment.access$300(COUIBottomSheetDialogFragment.this)) {
                COUIBottomSheetDialogFragment.access$900(COUIBottomSheetDialogFragment.this, this.val$f, this.val$removeGeneration);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialogFragment$5, reason: invalid class name */
    public class AnonymousClass5 implements Runnable {
        public AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIBottomSheetDialogFragment.this.setCancelable(true);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialogFragment$6, reason: invalid class name */
    public class AnonymousClass6 extends BottomSheetBehavior.BottomSheetCallback {
        public AnonymousClass6() {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
        public void onSlide(View view, float f3) {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
        public void onStateChanged(View view, int i3) {
            if (i3 == 5) {
                COUIBottomSheetDialogFragment.this.dismissAllowingStateLoss();
            }
            if (i3 == 2 && ((COUIBottomSheetBehavior) COUIBottomSheetDialogFragment.access$1000(COUIBottomSheetDialogFragment.this)).isCanHideKeyboard()) {
                COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment = COUIBottomSheetDialogFragment.this;
                COUIBottomSheetDialogFragment.access$1200(cOUIBottomSheetDialogFragment, COUIBottomSheetDialogFragment.access$1100(cOUIBottomSheetDialogFragment));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialogFragment$7, reason: invalid class name */
    public class AnonymousClass7 implements Runnable {
        final /* synthetic */ COUIPanelFragment val$panelFragment;

        public AnonymousClass7(COUIPanelFragment cOUIPanelFragment) {
            this.val$panelFragment = cOUIPanelFragment;
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment = COUIBottomSheetDialogFragment.this;
            COUIBottomSheetDialogFragment.access$1302(cOUIBottomSheetDialogFragment, COUIBottomSheetDialogFragment.access$1400(cOUIBottomSheetDialogFragment, this.val$panelFragment));
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public COUIBottomSheetDialogFragment() {
        this.mAlphaAnimDuration = 100L;
        this.mPendingPanelRemoveGeneration = 0;
        this.mPendingRemoveRunnable = new AnonymousClass1();
        this.mIsSavedInstanceState = false;
        this.mPeekHeight = 0;
        this.mSkipCollapsed = true;
        this.mFirstShowCollapsed = false;
        this.mIsDraggable = true;
        this.mCanPullUp = true;
        this.mIsShowInMaxHeight = false;
        this.mBackgroundBlur = false;
        this.mAlwayBlur = false;
        this.mBluring = false;
        this.mRegisterConfigurationChangeCallBack = true;
        this.mPhysicsFrequency = Float.MIN_VALUE;
        this.mPhysicsDampingRatio = Float.MIN_VALUE;
        this.mAnchorView = null;
        this.mFollowHandPlacementAllowedFlags = 15;
        this.mBottomSheetDialogAnimatorListener = null;
        this.mDisableSubExpand = false;
        this.mShouldRegisterWindowInsetsListener = true;
        this.mSkipStatusBarTransparentAndFont = false;
        this.mPreferWidth = -1;
        this.mHasSetPeekHeight = false;
        this.mHasSetSkipCollapsed = false;
        this.mIsHandlePanel = false;
        this.mHideDragViewHeight = 0;
        this.mFrameRate = true;
        this.mManualAppearanceLightNavigationBars = false;
    }

    public static /* synthetic */ int access$000(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mPendingRemoveRunnableGeneration;
    }

    public static /* synthetic */ int access$100(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mPendingPanelRemoveGeneration;
    }

    public static /* synthetic */ BottomSheetBehavior access$1000(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mBehavior;
    }

    public static /* synthetic */ View access$1100(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mDialogFragmentView;
    }

    public static /* synthetic */ void access$1200(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, View view) {
        cOUIBottomSheetDialogFragment.hideKeyboard(view);
    }

    public static /* synthetic */ int access$1302(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, int i3) {
        cOUIBottomSheetDialogFragment.mCurrentPanelHeight = i3;
        return i3;
    }

    public static /* synthetic */ int access$1400(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, Fragment fragment) {
        return cOUIBottomSheetDialogFragment.getFragmentHeight(fragment);
    }

    public static /* synthetic */ COUIPanelFragment access$200(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mPendingRemovePanelFragment;
    }

    public static /* synthetic */ COUIPanelFragment access$202(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, COUIPanelFragment cOUIPanelFragment) {
        cOUIBottomSheetDialogFragment.mPendingRemovePanelFragment = cOUIPanelFragment;
        return cOUIPanelFragment;
    }

    public static /* synthetic */ COUIPanelFragment access$300(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mCurrentPanelFragment;
    }

    public static /* synthetic */ boolean access$400(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mBluring;
    }

    public static /* synthetic */ boolean access$402(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, boolean z2) {
        cOUIBottomSheetDialogFragment.mBluring = z2;
        return z2;
    }

    public static /* synthetic */ COUIBottomSheetDialog access$500(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mBottomSheetDialog;
    }

    public static /* synthetic */ View access$600(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment) {
        return cOUIBottomSheetDialogFragment.mOutSideView;
    }

    public static /* synthetic */ View access$602(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, View view) {
        cOUIBottomSheetDialogFragment.mOutSideView = view;
        return view;
    }

    public static /* synthetic */ boolean access$702(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, boolean z2) {
        cOUIBottomSheetDialogFragment.mIsSavedInstanceState = z2;
        return z2;
    }

    public static /* synthetic */ void access$800(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, COUIPanelFragment cOUIPanelFragment) {
        cOUIBottomSheetDialogFragment.setPanelListener(cOUIPanelFragment);
    }

    public static /* synthetic */ void access$900(COUIBottomSheetDialogFragment cOUIBottomSheetDialogFragment, COUIPanelFragment cOUIPanelFragment, int i3) {
        cOUIBottomSheetDialogFragment.schedulePendingPanelRemove(cOUIPanelFragment, i3);
    }

    private void cancelPendingPanelRemoveRunnable() {
        this.mPendingPanelRemoveGeneration++;
        this.mPendingRemovePanelFragment = null;
        ViewGroup viewGroup = this.mPanelContainer;
        if (viewGroup != null) {
            viewGroup.removeCallbacks(this.mPendingRemoveRunnable);
        }
    }

    private int getFragmentHeight(Fragment fragment) {
        if (fragment == null || fragment.getView() == null) {
            return 0;
        }
        return fragment.getView().getHeight();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = this.mInputMethodManager;
        if (inputMethodManager == null || !inputMethodManager.isActive()) {
            return;
        }
        this.mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initBottomSheetDialogSize() {
        int i3 = this.mPanelWidth;
        if (i3 != 0) {
            this.mBottomSheetDialog.setWidth(i3);
        }
        int i4 = this.mPanelHeight;
        if (i4 != 0) {
            this.mBottomSheetDialog.setHeight(i4);
            setCurrentPanelHeight(this.mPanelHeight);
        }
    }

    private void initFragment() {
        COUIPanelFragment cOUIPanelFragment = this.mCurrentPanelFragment;
        if (cOUIPanelFragment != null) {
            cOUIPanelFragment.setIsBluring(this.mBluring);
            if (!this.mIsSavedInstanceState) {
                getChildFragmentManager().beginTransaction().replace(R.id.first_panel_container, this.mCurrentPanelFragment).commitNow();
            }
            COUIPanelFragment cOUIPanelFragment2 = this.mCurrentPanelFragment;
            Boolean bool = Boolean.TRUE;
            cOUIPanelFragment2.setShowOnFirstPanel(bool);
            this.mCurrentPanelFragment.onAdd(bool);
            setUpViewHeight(this.mPanelContainer, this.mIsShowInMaxHeight);
        }
        ViewGroup viewGroup = this.mPanelContainer;
        if (viewGroup != null) {
            viewGroup.post(new AnonymousClass3());
        }
    }

    private void invalidatePendingPanelRemove() {
        cancelPendingPanelRemoveRunnable();
        if (isAdded()) {
            for (Fragment fragment : getChildFragmentManager().getFragments()) {
                if (fragment instanceof COUIPanelFragment) {
                    ((COUIPanelFragment) fragment).setPanelFragmentAnimationListener(null);
                }
            }
        }
    }

    private void preShowPanel(COUIPanelFragment cOUIPanelFragment) {
        cOUIPanelFragment.setIsBluring(this.mBluring);
        if (getChildFragmentManager().getFragments().contains(cOUIPanelFragment) || cOUIPanelFragment.isAdded()) {
            getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.coui_open_panel_fragment_enter, COUIPanelFragment.CUSTOM_ANIMATION_EXIT, R.anim.coui_close_panel_fragment_enter, R.anim.coui_close_panel_fragment_exit).hide(this.mCurrentPanelFragment).show(cOUIPanelFragment).addToBackStack(null).commit();
        } else {
            getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.coui_open_panel_fragment_enter, COUIPanelFragment.CUSTOM_ANIMATION_EXIT, R.anim.coui_close_panel_fragment_enter, R.anim.coui_close_panel_fragment_exit).hide(this.mCurrentPanelFragment).add(R.id.first_panel_container, cOUIPanelFragment).show(cOUIPanelFragment).commit();
            cOUIPanelFragment.onAdd(Boolean.FALSE);
        }
        getChildFragmentManager().executePendingTransactions();
        if (!cOUIPanelFragment.isAdded() || cOUIPanelFragment.getView() == null) {
            COUILog.e(TAG, "isAdded:" + cOUIPanelFragment.isAdded() + ",view:" + cOUIPanelFragment.getView());
            return;
        }
        if (cOUIPanelFragment.isHidden()) {
            getChildFragmentManager().beginTransaction().show(cOUIPanelFragment).commitNow();
        }
        if (this.mBottomSheetDialog.getAdjustResizeHelper() != null) {
            this.mBottomSheetDialog.getAdjustResizeHelper().recoveryScrollingParentViewPaddingBottom(this.mCurrentPanelFragment.getDraggableLinearLayout());
        }
        COUIPanelFragment cOUIPanelFragment2 = this.mCurrentPanelFragment;
        cOUIPanelFragment2.onHide(cOUIPanelFragment2.getShowOnFirstPanel());
        this.mCurrentPanelFragment = cOUIPanelFragment;
        this.mBottomSheetDialog.setDragableLinearLayout(cOUIPanelFragment.getDraggableLinearLayout(), true);
        this.mCurrentPanelFragment.onShow(Boolean.FALSE);
        setPanelListener(this.mCurrentPanelFragment);
    }

    private void schedulePendingPanelRemove(COUIPanelFragment cOUIPanelFragment, int i3) {
        if (this.mPanelContainer == null || !isAdded()) {
            return;
        }
        this.mPendingRemovePanelFragment = cOUIPanelFragment;
        this.mPendingRemoveRunnableGeneration = i3;
        this.mPanelContainer.removeCallbacks(this.mPendingRemoveRunnable);
        this.mPanelContainer.post(this.mPendingRemoveRunnable);
    }

    private void setDialogOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setOnKeyListener(onKeyListener);
        }
    }

    private void setOnBackInvokedLocalListener(COUIBottomSheetDialog.OnBackInvokedLocalListener onBackInvokedLocalListener) {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setOnBackInvokedLocalListener(onBackInvokedLocalListener);
        }
    }

    private void setOnTouchOutSideViewListener(View.OnTouchListener onTouchListener) {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setOutSideViewTouchListener(onTouchListener);
        }
    }

    private void setPanelDragListener(COUIPanelDragListener cOUIPanelDragListener) {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog == null || !(cOUIBottomSheetDialog.getBehavior() instanceof COUIBottomSheetBehavior)) {
            return;
        }
        ((COUIBottomSheetBehavior) this.mBottomSheetDialog.getBehavior()).setPanelDragListener(cOUIPanelDragListener);
    }

    private void setPanelListener(COUIPanelFragment cOUIPanelFragment) {
        if (cOUIPanelFragment != null) {
            setPanelDragListener(cOUIPanelFragment.getDragPanelListener());
            setOnTouchOutSideViewListener(cOUIPanelFragment.getOutSideViewOnTouchListener());
            setDialogOnKeyListener(cOUIPanelFragment.getDialogOnKeyListener());
            setOnBackInvokedLocalListener(cOUIPanelFragment.getOnBackInvokedLocalListener());
        }
    }

    private void setUpViewHeight(View view, boolean z2) {
        if (view != null) {
            int i3 = (z2 || this.mPanelHeight != 0) ? -1 : -2;
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = i3;
            view.setLayoutParams(layoutParams);
        }
    }

    public void backToFirstPanel() {
        if (this.mCurrentPanelFragment != null) {
            invalidatePendingPanelRemove();
            setCancelable(false);
            hideKeyboard(this.mDialogFragmentView);
            int iIndexOf = getChildFragmentManager().getFragments().indexOf(this.mCurrentPanelFragment);
            COUIPanelFragment cOUIPanelFragment = this.mCurrentPanelFragment;
            if (iIndexOf > 0) {
                COUIPanelFragment cOUIPanelFragment2 = (COUIPanelFragment) getChildFragmentManager().getFragments().get(iIndexOf - 1);
                getChildFragmentManager().beginTransaction().setCustomAnimations(COUIPanelFragment.CUSTOM_ANIMATION_ENTER, R.anim.coui_close_panel_fragment_exit).hide(this.mCurrentPanelFragment).show(cOUIPanelFragment2).commitNow();
                COUIPanelContentLayout draggableLinearLayout = this.mCurrentPanelFragment.getDraggableLinearLayout();
                if (this.mBottomSheetDialog.getAdjustResizeHelper() != null) {
                    this.mBottomSheetDialog.getAdjustResizeHelper().recoveryScrollingParentViewPaddingBottom(draggableLinearLayout);
                }
                this.mCurrentPanelFragment.onHide(Boolean.FALSE);
                this.mCurrentPanelFragment = cOUIPanelFragment2;
                cOUIPanelFragment2.setIsBluring(this.mBluring);
                this.mBottomSheetDialog.setDragableLinearLayout(this.mCurrentPanelFragment.getDraggableLinearLayout(), true);
                COUIPanelFragment cOUIPanelFragment3 = this.mCurrentPanelFragment;
                cOUIPanelFragment3.onShow(cOUIPanelFragment3.getShowOnFirstPanel());
                setPanelListener(this.mCurrentPanelFragment);
                this.mCurrentPanelFragment.setPanelFragmentAnimationListener(new AnonymousClass4(this.mPendingPanelRemoveGeneration, cOUIPanelFragment));
            }
            ViewGroup viewGroup = this.mPanelContainer;
            if (viewGroup != null) {
                viewGroup.post(new AnonymousClass5());
            }
        }
    }

    public void delPreferWidth() {
        this.mPreferWidth = -1;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.delPreferWidth();
        }
        Log.d(TAG, "delPreferWidth");
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialogFragment, androidx.fragment.app.DialogFragment
    public void dismiss() {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.dismiss();
            if (this.mPreferWidth != -1) {
                this.mBottomSheetDialog.delPreferWidth();
                return;
            }
            return;
        }
        try {
            super.dismiss();
        } catch (Exception e3) {
            Log.e(TAG, e3.getMessage(), e3);
        }
    }

    public void doFeedbackAnimation() {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.doFeedbackAnimation();
        }
    }

    @Deprecated
    public long getAlphaAnimDuration() {
        return this.mAlphaAnimDuration;
    }

    public COUIBottomSheetDialog getBottomSheetDialog() {
        return this.mBottomSheetDialog;
    }

    public int getCurrentPanelHeight() {
        return this.mCurrentPanelHeight;
    }

    public boolean getIsHandlePanel() {
        return this.mIsHandlePanel;
    }

    public int getPeekHeight() {
        return this.mPeekHeight;
    }

    public boolean isFirstShowCollapsed() {
        return this.mFirstShowCollapsed;
    }

    public boolean isSkipCollapsed() {
        return this.mSkipCollapsed;
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mBottomSheetDialog == null || this.mCurrentPanelHeight == 0 || getContext() == null) {
            return;
        }
        int i3 = this.mPanelHeight;
        int iMin = i3 != 0 ? i3 : this.mCurrentPanelHeight;
        if (this.mAnchorView == null || i3 == 0) {
            iMin = Math.min(iMin, COUIPanelMultiWindowUtils.getPanelMaxHeight(getContext(), configuration));
        }
        this.mBottomSheetDialog.setHeight(iMin);
        this.mBottomSheetDialog.updateLayoutWhileConfigChange(configuration);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialogFragment, androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        if (bundle != null) {
            this.mIsSavedInstanceState = true;
            this.mIsInTinyScreen = bundle.getBoolean(SAVE_IS_IN_TINY_SCREEN_PANEL_KEY, false);
            this.mIsDraggable = bundle.getBoolean(SAVE_DRAGGABLE_KEY, true);
            this.mPeekHeight = bundle.getInt(SAVE_PEEK_HEIGHT_KEY, 0);
            this.mSkipCollapsed = bundle.getBoolean(SAVE_SKIP_COLLAPSED_KEY, true);
            this.mFirstShowCollapsed = bundle.getBoolean(SAVE_FIRST_SHOW_COLLAPSED_KEY, false);
            this.mCanPullUp = bundle.getBoolean(SAVE_CAN_PULL_UP_KEY, true);
            this.mIsShowInMaxHeight = bundle.getBoolean(SAVE_SHOW_IN_MAX_HEIGHT_KEY, false);
            this.mBackgroundBlur = bundle.getBoolean(SAVE_BLUR_BACKGROUND_KEY, false);
            this.mAlwayBlur = bundle.getBoolean(SAVE_ALWAY_BLUR_KEY, false);
            this.mRegisterConfigurationChangeCallBack = bundle.getBoolean(SAVE_REGISTER_CONFIGURATION_KEY, true);
            this.mIsHandlePanel = bundle.getBoolean(SAVE_IS_HANDLE_PANEL_KEY, false);
            this.mHasSetPeekHeight = bundle.getBoolean(SAVE_HAS_SET_PEEK_HEIGHT_KEY, false);
            this.mHasSetSkipCollapsed = bundle.getBoolean(SAVE_HAS_SET_SKIP_COLLAPSED_KEY, false);
            this.mFrameRate = bundle.getBoolean(SAVE_FRAME_RATE_KEY, true);
            this.mCouiPanelEdgeToEdgeEnable = bundle.getBoolean(SAVE_PANEL_EDGE_TO_EDGE, false);
            this.mSupportExitBlockingAnimation = bundle.getBoolean(SAVE_SUPPORT_EXIT_BLOCKING_ANIMATION, false);
            this.mBackEventPassThroughWhileDismissing = bundle.getBoolean(SAVE_BACK_EVENT_PASS_THROUGH_WHILE_DISMISSING, false);
            this.mUseNormalSmoothCorner = bundle.getBoolean(SAVE_USE_NORMAL_SMOOTH_CORNER, false);
            this.mManualAppearanceLightNavigationBars = bundle.getBoolean(SAVE_MANUAL_APPEARANCE_LIGHT_NAVIGATION_BAR, false);
            this.mFollowHandFallbackVerticalEdgeWhenInsufficientSpace = bundle.getBoolean(SAVE_FOLLOW_HAND_FALLBACK_BOTTOM_CENTER_KEY, false);
            this.mFollowHandUseAnchorActualDisplayRect = bundle.getBoolean(SAVE_FOLLOW_HAND_USE_ANCHOR_ACTUAL_DISPLAY_RECT_KEY, false);
            this.mFollowHandPlacementAllowedFlags = bundle.getInt(SAVE_FOLLOW_HAND_PLACEMENT_ALLOWED_FLAGS_KEY, 15);
        }
        boolean z2 = COUINavigationBarUtil.isGestureNavigation(requireContext()) || UIUtil.isInFreeFormModeWindowMode(requireContext());
        this.mIsGestureNavigation = z2;
        if (this.mIsHandlePanel) {
            if (!this.mHasSetPeekHeight) {
                if (z2) {
                    this.mPeekHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_panel_default_peek_height_in_gesture);
                } else {
                    this.mPeekHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_panel_default_peek_height);
                }
            }
            if (!this.mHasSetSkipCollapsed) {
                this.mSkipCollapsed = false;
            }
        }
        if (getActivity() != null) {
            COUIBottomSheetDialog cOUIBottomSheetDialog = new COUIBottomSheetDialog(getActivity(), R.style.DefaultBottomSheetDialog, this.mPhysicsFrequency, this.mPhysicsDampingRatio);
            this.mBottomSheetDialog = cOUIBottomSheetDialog;
            View view = this.mAnchorView;
            if (view != null) {
                cOUIBottomSheetDialog.setAnchorView(view);
            }
            this.mBottomSheetDialog.setIsInTinyScreen(this.mIsInTinyScreen, this.mIsFullScreenInTinyScreen);
            this.mBottomSheetDialog.setDisableSubExpand(this.mDisableSubExpand);
            this.mBottomSheetDialog.setBottomSheetDialogAnimatorListener(this.mBottomSheetDialogAnimatorListener);
            this.mBottomSheetDialog.setShouldRegisterWindowInsetsListener(this.mShouldRegisterWindowInsetsListener);
            this.mBottomSheetDialog.setSkipStatusBarTransparentAndFont(this.mSkipStatusBarTransparentAndFont);
            this.mBottomSheetDialog.setShowInDialogFragment(true);
            this.mBottomSheetDialog.setPeekHeight(this.mPeekHeight);
            this.mBottomSheetDialog.setIsHandlePanel(this.mIsHandlePanel);
            this.mBottomSheetDialog.setSkipCollapsed(this.mSkipCollapsed);
            this.mBottomSheetDialog.setFirstShowCollapsed(this.mFirstShowCollapsed);
            this.mBottomSheetDialog.setCanPullUp(this.mCanPullUp);
            this.mBottomSheetDialog.setIsShowInMaxHeight(this.mIsShowInMaxHeight);
            if (this.mBackgroundBlur) {
                this.mBottomSheetDialog.setBlurryStateChangeListener(new AnonymousClass2());
            }
            this.mBottomSheetDialog.setBackgroundBlur(this.mBackgroundBlur);
            this.mBottomSheetDialog.setFrameRate(this.mFrameRate);
            this.mBottomSheetDialog.setCouiPanelEdgeToEdgeEnable(this.mCouiPanelEdgeToEdgeEnable);
            this.mBottomSheetDialog.setSupportExitBlockingAnimation(this.mSupportExitBlockingAnimation);
            this.mBottomSheetDialog.setBackEventPassThroughWhileDismissing(this.mBackEventPassThroughWhileDismissing);
            this.mBottomSheetDialog.setUseNormalSmoothCorner(this.mUseNormalSmoothCorner);
            if (this.mManualAppearanceLightNavigationBars) {
                this.mBottomSheetDialog.setNavigationBarIconFollowPanel();
            }
            this.mBottomSheetDialog.setRegisterConfigurationChangeCallBack(this.mRegisterConfigurationChangeCallBack);
            this.mBottomSheetDialog.setFollowHandVerticalEdgeWhenInsufficientSpace(this.mFollowHandFallbackVerticalEdgeWhenInsufficientSpace);
            this.mBottomSheetDialog.setFollowHandUseAnchorActualDisplayRect(this.mFollowHandUseAnchorActualDisplayRect);
            this.mBottomSheetDialog.setFollowHandPlacementAllowedFlags(this.mFollowHandPlacementAllowedFlags);
            this.mBottomSheetDialog.setHideDragViewHeight(this.mHideDragViewHeight);
            int i3 = this.mPreferWidth;
            if (i3 != -1) {
                this.mBottomSheetDialog.setPreferWidth(i3);
            }
            initBottomSheetDialogSize();
            BottomSheetBehavior<FrameLayout> behavior = this.mBottomSheetDialog.getBehavior();
            this.mBehavior = behavior;
            behavior.setDraggable(this.mIsDraggable);
            BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.mBehavior;
            if (bottomSheetBehavior instanceof COUIBottomSheetBehavior) {
                ((COUIBottomSheetBehavior) bottomSheetBehavior).setGlobalDrag(this.mGlobalDrag);
            }
        }
        return this.mBottomSheetDialog;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.mIsShowInMaxHeight) {
            this.mDialogFragmentView = View.inflate(getActivity(), R.layout.coui_bottom_sheet_dialog_max_height, null);
        } else {
            this.mDialogFragmentView = View.inflate(getActivity(), R.layout.coui_bottom_sheet_dialog, null);
        }
        return this.mDialogFragmentView;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        invalidatePendingPanelRemove();
        this.mPanelContainer = null;
        super.onDestroyView();
        COUIPanelFragment cOUIPanelFragment = this.mCurrentPanelFragment;
        if (cOUIPanelFragment != null) {
            cOUIPanelFragment.onAbandon(cOUIPanelFragment.getShowOnFirstPanel());
        }
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setOnKeyListener(null);
            this.mBottomSheetDialog.setOnBackInvokedLocalListener(null);
            this.mBottomSheetDialog.setOutSideViewTouchListener(null);
            OnDismissListener onDismissListener = this.mOnDismissListener;
            if (onDismissListener != null) {
                onDismissListener.onDismiss();
            }
        }
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.mBehavior;
        if (bottomSheetBehavior instanceof COUIBottomSheetBehavior) {
            ((COUIBottomSheetBehavior) bottomSheetBehavior).setPanelDragListener(null);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(SAVE_PANEL_HEIGHT_KEY, this.mPanelHeight);
        bundle.putInt(SAVE_PANEL_WIDTH_KEY, this.mPanelWidth);
        bundle.putBoolean(SAVE_DRAGGABLE_KEY, this.mIsDraggable);
        bundle.putInt(SAVE_PEEK_HEIGHT_KEY, this.mPeekHeight);
        bundle.putBoolean(SAVE_SKIP_COLLAPSED_KEY, this.mSkipCollapsed);
        bundle.putBoolean(SAVE_FIRST_SHOW_COLLAPSED_KEY, this.mFirstShowCollapsed);
        bundle.putBoolean(SAVE_CAN_PULL_UP_KEY, this.mCanPullUp);
        bundle.putBoolean(SAVE_SHOW_IN_MAX_HEIGHT_KEY, this.mIsShowInMaxHeight);
        bundle.putBoolean(SAVE_BLUR_BACKGROUND_KEY, this.mBackgroundBlur);
        bundle.putBoolean(SAVE_ALWAY_BLUR_KEY, this.mAlwayBlur);
        bundle.putBoolean(SAVE_IS_IN_TINY_SCREEN_PANEL_KEY, this.mIsInTinyScreen);
        bundle.putBoolean(SAVE_REGISTER_CONFIGURATION_KEY, this.mRegisterConfigurationChangeCallBack);
        bundle.putBoolean(SAVE_IS_HANDLE_PANEL_KEY, this.mIsHandlePanel);
        bundle.putBoolean(SAVE_HAS_SET_PEEK_HEIGHT_KEY, this.mHasSetPeekHeight);
        bundle.putBoolean(SAVE_HAS_SET_SKIP_COLLAPSED_KEY, this.mHasSetSkipCollapsed);
        bundle.putBoolean(SAVE_FRAME_RATE_KEY, this.mFrameRate);
        bundle.putBoolean(SAVE_PANEL_EDGE_TO_EDGE, this.mCouiPanelEdgeToEdgeEnable);
        bundle.putBoolean(SAVE_SUPPORT_EXIT_BLOCKING_ANIMATION, this.mSupportExitBlockingAnimation);
        bundle.putBoolean(SAVE_BACK_EVENT_PASS_THROUGH_WHILE_DISMISSING, this.mBackEventPassThroughWhileDismissing);
        bundle.putBoolean(SAVE_USE_NORMAL_SMOOTH_CORNER, this.mUseNormalSmoothCorner);
        bundle.putBoolean(SAVE_MANUAL_APPEARANCE_LIGHT_NAVIGATION_BAR, this.mManualAppearanceLightNavigationBars);
        bundle.putBoolean(SAVE_FOLLOW_HAND_FALLBACK_BOTTOM_CENTER_KEY, this.mFollowHandFallbackVerticalEdgeWhenInsufficientSpace);
        bundle.putBoolean(SAVE_FOLLOW_HAND_USE_ANCHOR_ACTUAL_DISPLAY_RECT_KEY, this.mFollowHandUseAnchorActualDisplayRect);
        bundle.putInt(SAVE_FOLLOW_HAND_PLACEMENT_ALLOWED_FLAGS_KEY, this.mFollowHandPlacementAllowedFlags);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.mBehavior;
        if (bottomSheetBehavior instanceof COUIBottomSheetBehavior) {
            bottomSheetBehavior.addBottomSheetCallback(new AnonymousClass6());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getActivity() != null) {
            this.mInputMethodManager = (InputMethodManager) getActivity().getSystemService(SystemUpdateScreenStateConfig.KEY_INPUT_METHOD);
        }
        ViewGroup viewGroup = (ViewGroup) this.mDialogFragmentView.findViewById(R.id.first_panel_container);
        this.mPanelContainer = viewGroup;
        if (viewGroup == null) {
            return;
        }
        if (bundle != null) {
            this.mIsSavedInstanceState = true;
            this.mPanelHeight = bundle.getInt(SAVE_PANEL_HEIGHT_KEY, 0);
            this.mPanelWidth = bundle.getInt(SAVE_PANEL_WIDTH_KEY, 0);
            initBottomSheetDialogSize();
        }
        initFragment();
    }

    public void replacePanelFragment(COUIPanelFragment cOUIPanelFragment) {
        if (cOUIPanelFragment == null || this.mPanelContainer == null) {
            return;
        }
        invalidatePendingPanelRemove();
        if (this.mBottomSheetDialog.getAdjustResizeHelper() != null) {
            this.mBottomSheetDialog.getAdjustResizeHelper().setIgnoreHideKeyboardAnim(true);
        }
        hideKeyboard(this.mDialogFragmentView);
        preShowPanel(cOUIPanelFragment);
    }

    @Deprecated
    public void setAlphaAnimDuration(long j3) {
        this.mAlphaAnimDuration = j3;
    }

    public void setAnchorView(View view) {
        this.mAnchorView = view;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog == null || view == null) {
            return;
        }
        cOUIBottomSheetDialog.setAnchorView(view);
    }

    public void setBackEventPassThroughWhileDismissing(boolean z2) {
        this.mBackEventPassThroughWhileDismissing = z2;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setBackEventPassThroughWhileDismissing(z2);
        }
    }

    public void setBlurBackground(boolean z2) {
        this.mBackgroundBlur = z2;
    }

    public void setBottomSheetDialogAnimatorListener(COUIBottomSheetDialog.BottomSheetDialogAnimatorListener bottomSheetDialogAnimatorListener) {
        this.mBottomSheetDialogAnimatorListener = bottomSheetDialogAnimatorListener;
    }

    public void setCanPullUp(boolean z2) {
        this.mCanPullUp = z2;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setCanPullUp(z2);
        }
    }

    public void setCouiPanelEdgeToEdgeEnable(boolean z2) {
        this.mCouiPanelEdgeToEdgeEnable = z2;
    }

    public void setCurrentPanelHeight(int i3) {
        this.mCurrentPanelHeight = i3;
    }

    public void setDisableSubExpand(boolean z2) {
        this.mDisableSubExpand = z2;
    }

    public void setDraggable(boolean z2) {
        if (this.mIsDraggable != z2) {
            this.mIsDraggable = z2;
            BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.mBehavior;
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setDraggable(z2);
            }
        }
    }

    @Deprecated
    public void setExecuteNavColorAnimAfterDismiss(boolean z2) {
        this.mIsExecuteNavColorAnimAfterDismiss = z2;
        if (getDialog() instanceof COUIBottomSheetDialog) {
            ((COUIBottomSheetDialog) getDialog()).setExecuteNavColorAnimAfterDismiss(z2);
        }
    }

    @Deprecated
    public void setFinalNavColorAfterDismiss(int i3) {
        this.mFinalNavColorAfterDismiss = i3;
        if (getDialog() instanceof COUIBottomSheetDialog) {
            ((COUIBottomSheetDialog) getDialog()).setFinalNavColorAfterDismiss(i3);
        }
    }

    public void setFirstShowCollapsed(boolean z2) {
        this.mFirstShowCollapsed = z2;
    }

    public void setFollowHandPlacementAllowedFlags(int i3) {
        this.mFollowHandPlacementAllowedFlags = i3;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setFollowHandPlacementAllowedFlags(i3);
        }
    }

    public void setFollowHandUseAnchorActualDisplayRect(boolean z2) {
        this.mFollowHandUseAnchorActualDisplayRect = z2;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setFollowHandUseAnchorActualDisplayRect(z2);
        }
    }

    public void setFollowHandVerticalEdgeWhenInsufficientSpace(boolean z2) {
        this.mFollowHandFallbackVerticalEdgeWhenInsufficientSpace = z2;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setFollowHandVerticalEdgeWhenInsufficientSpace(z2);
        }
    }

    public void setFrameRate(boolean z2) {
        this.mFrameRate = z2;
    }

    public void setGlobalDrag(boolean z2) {
        this.mGlobalDrag = z2;
    }

    public void setHeight(int i3) {
        this.mPanelHeight = i3;
        if (this.mBottomSheetDialog != null) {
            initBottomSheetDialogSize();
        }
        if (this.mCurrentPanelFragment != null) {
            setUpViewHeight(this.mPanelContainer, this.mIsShowInMaxHeight);
        }
    }

    public void setHideDragViewHeight(int i3) {
        this.mHideDragViewHeight = i3;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setHideDragViewHeight(i3);
        }
    }

    public void setIsHandlePanel(boolean z2) {
        this.mIsHandlePanel = z2;
    }

    public void setIsInTinyScreen(boolean z2, boolean z3) {
        this.mIsInTinyScreen = z2;
        this.mIsFullScreenInTinyScreen = z3;
        if (z2) {
            setIsShowInMaxHeight(true);
        }
    }

    public void setIsShowInMaxHeight(boolean z2) {
        this.mIsShowInMaxHeight = z2;
    }

    public void setMainPanelFragment(COUIPanelFragment cOUIPanelFragment) {
        this.mCurrentPanelFragment = cOUIPanelFragment;
        cOUIPanelFragment.setIsBluring(this.mBluring);
    }

    public void setNavigationBarIconFollowPanel() {
        this.mManualAppearanceLightNavigationBars = true;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setNavigationBarIconFollowPanel();
        }
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setPanelFragment(COUIPanelFragment cOUIPanelFragment, Boolean bool) {
        this.mCurrentPanelFragment = cOUIPanelFragment;
        cOUIPanelFragment.setIsBluring(this.mBluring);
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setDragableLinearLayout(this.mCurrentPanelFragment.getDraggableLinearLayout(), true);
        }
        ViewGroup viewGroup = this.mPanelContainer;
        if (viewGroup != null) {
            viewGroup.post(new AnonymousClass7(cOUIPanelFragment));
            setUpViewHeight(this.mPanelContainer, this.mIsShowInMaxHeight);
        }
    }

    public void setPeekHeight(int i3) {
        this.mPeekHeight = i3;
        this.mHasSetPeekHeight = true;
    }

    public void setPreferWidth(int i3) {
        this.mPreferWidth = i3;
        Log.d(TAG, "setPreferWidth =：" + this.mPreferWidth);
    }

    public void setShouldRegisterWindowInsetsListener(boolean z2) {
        this.mShouldRegisterWindowInsetsListener = z2;
    }

    public void setSkipCollapsed(boolean z2) {
        this.mSkipCollapsed = z2;
        this.mHasSetSkipCollapsed = true;
    }

    public void setSkipStatusBarTransparentAndFont(boolean z2) {
        this.mSkipStatusBarTransparentAndFont = z2;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.setSkipStatusBarTransparentAndFont(z2);
        }
    }

    public void setSupportExitBlockingAnimation(boolean z2) {
        if (this.mSupportExitBlockingAnimation != z2) {
            this.mSupportExitBlockingAnimation = z2;
            COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
            if (cOUIBottomSheetDialog != null) {
                cOUIBottomSheetDialog.setSupportExitBlockingAnimation(z2);
            }
        }
    }

    public void setUseNormalSmoothCorner(boolean z2) {
        this.mUseNormalSmoothCorner = z2;
    }

    public void setWidth(int i3) {
        this.mPanelWidth = i3;
        if (this.mBottomSheetDialog != null) {
            initBottomSheetDialogSize();
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    public void show(FragmentManager fragmentManager, String str) {
        show(fragmentManager, str, null);
    }

    public boolean updateFollowHandPanelLocation() {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog == null) {
            return false;
        }
        return cOUIBottomSheetDialog.updateFollowHandPanelLocation();
    }

    public void setBlurBackground(boolean z2, boolean z3) {
        this.mBackgroundBlur = z2;
        this.mAlwayBlur = z3;
    }

    public void show(FragmentManager fragmentManager, String str, View view) {
        COUIBottomSheetDialog cOUIBottomSheetDialog;
        if (isAdded()) {
            return;
        }
        int i3 = this.mPreferWidth;
        if (i3 != -1 && (cOUIBottomSheetDialog = this.mBottomSheetDialog) != null) {
            cOUIBottomSheetDialog.setPreferWidth(i3);
        }
        if (this.mCurrentPanelFragment == null) {
            this.mCurrentPanelFragment = new COUIPanelFragment();
        }
        this.mCurrentPanelFragment.setIsBluring(this.mBluring);
        this.mCurrentPanelFragment.setIsInTinyScreen(this.mIsInTinyScreen);
        this.mAnchorView = view;
        super.show(fragmentManager, str);
    }

    public void dismiss(boolean z2) {
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.mBottomSheetDialog;
        if (cOUIBottomSheetDialog != null) {
            cOUIBottomSheetDialog.dismiss(z2);
            if (this.mPreferWidth != -1) {
                this.mBottomSheetDialog.delPreferWidth();
                return;
            }
            return;
        }
        try {
            super.dismiss();
        } catch (Exception e3) {
            Log.e(TAG, e3.getMessage(), e3);
        }
    }

    public COUIBottomSheetDialogFragment(float f3, float f4) {
        this.mAlphaAnimDuration = 100L;
        this.mPendingPanelRemoveGeneration = 0;
        this.mPendingRemoveRunnable = new AnonymousClass1();
        this.mIsSavedInstanceState = false;
        this.mPeekHeight = 0;
        this.mSkipCollapsed = true;
        this.mFirstShowCollapsed = false;
        this.mIsDraggable = true;
        this.mCanPullUp = true;
        this.mIsShowInMaxHeight = false;
        this.mBackgroundBlur = false;
        this.mAlwayBlur = false;
        this.mBluring = false;
        this.mRegisterConfigurationChangeCallBack = true;
        this.mAnchorView = null;
        this.mFollowHandPlacementAllowedFlags = 15;
        this.mBottomSheetDialogAnimatorListener = null;
        this.mDisableSubExpand = false;
        this.mShouldRegisterWindowInsetsListener = true;
        this.mSkipStatusBarTransparentAndFont = false;
        this.mPreferWidth = -1;
        this.mHasSetPeekHeight = false;
        this.mHasSetSkipCollapsed = false;
        this.mIsHandlePanel = false;
        this.mHideDragViewHeight = 0;
        this.mFrameRate = true;
        this.mManualAppearanceLightNavigationBars = false;
        this.mPhysicsFrequency = f3;
        this.mPhysicsDampingRatio = f4;
    }
}