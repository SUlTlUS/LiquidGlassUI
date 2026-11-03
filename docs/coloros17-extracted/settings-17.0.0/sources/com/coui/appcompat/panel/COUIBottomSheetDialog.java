package com.coui.appcompat.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.OplusBaseLayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.animation.COUIInEaseInterpolator;
import com.coui.appcompat.animation.COUIOutEaseInterpolator;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.edittext.COUIInputView;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.coui.appcompat.uiutil.AnimLevel;
import com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.oplus.dynamicframerate.AnimationVelocityCalculator;
import com.oplus.dynamicframerate.DynamicFrameRateManager;
import com.oplus.settings.appfunctions.SystemUpdateScreenStateConfig;
import com.oplus.util.OplusTypeCastingHelper;
import com.support.dialog.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes7.dex */
public class COUIBottomSheetDialog extends BottomSheetDialog implements DynamicAnimation.OnAnimationUpdateListener, DynamicAnimation.OnAnimationEndListener, OnMaterialStateChangeListener {
    private static final float ALPHA_OPAQUE = 1.0f;
    private static final float ALPHA_TRANSPARENT = 0.0f;
    private static final int ANIMATION_TYPE_DIALOG_ALPHA = 8;
    public static final int ANIMATION_TYPE_ID = 10101;
    private static final int ANIMATION_TYPE_OUTSIDE_ALPHA = 2;
    private static final int ANIMATION_TYPE_SCALE = 4;
    private static final int ANIMATION_TYPE_TRANSLATION = 1;
    private static final String BRANCH_FOLLOW_HAND_ABOVE_ANCHOR = "ABOVE_ANCHOR";
    private static final String BRANCH_FOLLOW_HAND_BELOW_ANCHOR = "BELOW_ANCHOR";
    private static final String BRANCH_FOLLOW_HAND_SIDE_ANCHOR = "SIDE_ANCHOR";
    private static final String BRANCH_FOLLOW_HAND_SIDE_CLAMP_OVERFLOW = "SIDE_CLAMP_OVERFLOW";
    private static final String BRANCH_FOLLOW_HAND_UNKNOWN = "unknown";
    private static final boolean DEBUG;
    private static final float DEFAULT_ALPHA_HIDE_SPRING_RESPONSE = 0.18f;
    private static final float DEFAULT_ALPHA_HIDING_ANIMATOR_DURATION = 183.0f;
    private static final float DEFAULT_ALPHA_SHOW_SPRING_RESPONSE = 0.25f;
    private static final float DEFAULT_CENTER_HIDE_SPRING_RESPONSE = 0.25f;
    private static final float DEFAULT_CENTER_SHOW_SPRING_RESPONSE = 0.25f;
    private static final float DEFAULT_MINIMUM_VISIBLE_CHANGE_DISMISS = 10.0f;
    private static final float DEFAULT_MINIMUM_VISIBLE_CHANGE_SHOW = 1.0f;
    private static final int DEFAULT_MOVE_PX = 1;
    private static final float DEFAULT_SPRING_DAMPING_RATIO = 0.7f;
    private static final int DEFAULT_SPRING_FACTOR = 10000;
    private static final float DEFAULT_SPRING_STIFFNESS = 200.0f;
    private static final float DEFAULT_TRANSLATE_HIDING_ANIMATOR_DURATION = 333.0f;
    static final float DEFAULT_TRANSLATION_HIDE_SPRING_RESPONSE_LARGE = 0.37f;
    static final float DEFAULT_TRANSLATION_HIDE_SPRING_RESPONSE_SMALL = 0.18f;
    private static final float DEFAULT_TRANSLATION_SHOW_SPRING_RESPONSE_LARGE = 0.45f;
    private static final float DEFAULT_TRANSLATION_SHOW_SPRING_RESPONSE_SMALL = 0.25f;
    private static final float DEFAULT_TRANSLATION_SPRING_BOUNCE = 0.0f;
    private static final float DIALOG_SHOW_SCALE_DELTA = 0.2f;
    private static final float DIALOG_SHOW_SCALE_START = 0.8f;
    private static final Interpolator DISMISS_ALPHA_ANIM_INTERPOLATOR;
    private static final float ELEVATION_VALUE = 24.0f;
    private static final int FINAL_POSITION = 100;
    private static final float FIRST_TIER_ALPHA = 0.6f;
    private static final int FLAG_NAVIGATION_AUTO_COLOR_REVERSAL_DISABLE = 32768;
    private static final float FLOAT_ONE = 1.0f;
    private static final float FLOAT_POINT_FIVE = 0.5f;
    private static final int FOLLOW_HAND_PLACEMENT_ABOVE = 2;
    private static final int FOLLOW_HAND_PLACEMENT_BELOW = 1;
    public static final int FOLLOW_HAND_PLACEMENT_FLAG_ALL = 15;
    public static final int FOLLOW_HAND_PLACEMENT_FLAG_BOTTOM = 1;
    public static final int FOLLOW_HAND_PLACEMENT_FLAG_LEFT = 4;
    private static final int FOLLOW_HAND_PLACEMENT_FLAG_MASK = 15;
    public static final int FOLLOW_HAND_PLACEMENT_FLAG_RIGHT = 8;
    public static final int FOLLOW_HAND_PLACEMENT_FLAG_TOP = 2;
    public static final int FOLLOW_HAND_PLACEMENT_FLAG_VERTICAL_ONLY = 3;
    private static final int FOLLOW_HAND_PLACEMENT_LEFT = 3;
    private static final int FOLLOW_HAND_PLACEMENT_RIGHT = 4;
    private static final int FOLLOW_HAND_PLACEMENT_UNSET = 0;
    private static final int FOLLOW_HAND_SIDE_VERT_MIN_Y = 1;
    private static final int FOLLOW_HAND_SIDE_VERT_NORMAL = 0;
    private static final int HUNDRED = 100;
    private static final int INT_TWO = 2;
    private static final float MAX_ALPHA = 255.0f;
    private static final long NAV_COLOR_ANIM_DURATION = 200;
    private static final float NO_ELEVATION_VALUE = 0.0f;
    private static final Interpolator OUTSIDE_ALPHA_ANIM_INTERPOLATOR;
    private static final float PHYSICS_UNSET = Float.MIN_VALUE;
    private static final float PULL_UP_FRICTION = 0.8f;
    private static final int PULL_UP_REBOUND_BOUNCINESS = 6;
    private static final int PULL_UP_REBOUND_SPEED = 42;
    private static final int SDK_SUB_VERSION_FOR_COMPUTE = 10;
    private static final int SDK_SUB_VERSION_FOR_FRAME_RATE = 10;
    private static final int SDK_VERSION_FOR_COMPUTE = 34;
    private static final int SHADOW_EDGE_CORNER_TYPE = 2;
    private static final float SHADOW_EDGE_FADE_ALPHA_IN_DARK = 0.1f;
    private static final float SHADOW_EDGE_FADE_ALPHA_IN_LIGHT = 0.4f;
    private static final float SHADOW_EDGE_FADE_ALPHA_OUT_DARK = 0.0f;
    private static final float SHADOW_EDGE_FADE_ALPHA_OUT_LIGHT = 0.0f;
    private static final float SHADOW_EDGE_FADE_WIDTH_SCALE = 1.0f;
    private static final float SHADOW_EDGE_LINE_ALPHA_DARK = 0.15f;
    private static final float SHADOW_EDGE_LINE_ALPHA_LIGHT = 0.8f;
    private static final float SHADOW_EDGE_LINE_ANGLE = 0.0f;
    private static final float SHOW_HEIGHT_ANIM_DURATION_IN_TINY_SCREEN = 167.0f;
    private static final Interpolator SHOW_HEIGHT_ANIM_INTERPOLATOR;
    private static final float SPRING_ANIM_CONTENT_CHANGE_BOUNCE = 0.0f;
    private static final float SPRING_ANIM_CONTENT_CHANGE_MINIMUM_VISIBLE_CHANGE = 95.0f;
    private static final float SPRING_ANIM_CONTENT_CHANGE_RESPONSE = 0.4f;
    private static final String STATE_FOCUS_CHANGES = "state_focus_changes";
    private static final String STATE_LAST_STATIC_CHANGES = "last_static_state";
    private static final String TAG = "COUIBottomSheetDialog";
    private static final double THREE_POINT_EIGHT = 3.8d;
    private static final double TWENTY = 20.0d;
    private static final int UNSET_SIZE = -1;
    private static final double ZERO = 0.0d;
    protected boolean isLargeScreenLimitMaxSize;
    private int mADFRFeatureType;
    private WeakReference<Activity> mActivityWeakReference;
    private ViewGroup mAdjustLayout;
    private boolean mAdjustResizeEnable;
    private COUIPanelAdjustResizeHelper mAdjustResizeHelper;
    private final ViewTreeObserver.OnPreDrawListener mAdjustResizeOnPreDrawListener;
    private COUISpringAnimation mAlphaSpringAnimation;
    private COUIDynamicAnimation.OnAnimationEndListener mAlphaSpringEndListener;
    private COUIDynamicAnimation.OnAnimationUpdateListener mAlphaSpringUpdateListener;
    private boolean mAlwayBlur;
    private View mAnchorView;
    private int mAnimationFlag;
    private OnAnimationListener mAnimationListener;
    private float mAppearDampingRatio;
    private SpringAnimation mAppearSpringAnim;
    private SpringForce mAppearSpringForce;
    private float mAppearStiffness;
    private boolean mAppearanceLightNavigationBars;
    private WindowInsets mApplyWindowInsets;
    private boolean mBackEventPassThroughWhileDismissing;
    private COUIBackgroundBlurBuilder mBackgroundBlurBuilder;
    private float[] mBlendColorDark;
    private float[] mBlendColorLight;
    private int mBlurRadius;
    private COUIBackgroundBlurBuilder.OnBlurryStateChangeListener mBlurryStateChangeListener;
    private View mBottomSheetDialogAlphaView;
    private BottomSheetDialogAnimatorListener mBottomSheetDialogAnimatorListener;
    private boolean mCanPerformHapticFeedback;
    private boolean mCanPullUp;
    private boolean mCancelable;
    private boolean mCanceledOnTouchOutside;
    private int mColorMask;
    private ComponentCallbacks mComponentCallbacks;
    private Configuration mConfiguration;
    private IgnoreWindowInsetsFrameLayout mContainerFrameLayout;
    private View mContentView;
    private View mCoordinatorLayout;
    protected int mCoordinatorLayoutMinInsetsTop;
    private int mCoordinatorLayoutPaddingExtra;
    private int mCornerRadius;
    private boolean mCouiPanelEdgeToEdgeEnable;
    private float mCurrentOutSideAlphaStateHidden;
    private float mCurrentOutSideAlphaStateShow;
    private float mCurrentOutsideAlpha;
    private float mCurrentParentViewTranslationY;
    private int mCurrentSpringTotalOffset;
    private int mDefaultPaddingBottom;
    private COUIPanelPercentFrameLayout mDesignBottomSheetFrameLayout;
    private DialogOffsetListener mDialogOffsetListener;
    private Spring mDisableFastCloseFeedbackSpring;
    private boolean mDisableSubExpand;
    protected COUIPanelContentLayout mDraggableConstraintLayout;
    private float mEndValueOfTranslateAnimation;
    private View mFeedBackView;

    @Deprecated
    private int mFinalNavColorAfterDismiss;
    private boolean mFirstShowCollapsed;
    private Boolean mFocusChange;
    private final Rect mFollowHandAnchorRectInScreen;
    private final int[] mFollowHandCoordinatorOnScreen;
    private String mFollowHandCurrentBranch;
    private COUIBottomSheetBehavior<?> mFollowHandDetachedBottomSheetBehavior;
    private boolean mFollowHandFallbackVerticalEdgeWhenInsufficientSpace;
    private final int[] mFollowHandInvalidatePositionOut;
    private int mFollowHandOriginalMaxHeightPx;
    private int mFollowHandPlacementAllowedFlags;
    private int mFollowHandRecordedNavigationBottomInsetPx;
    private int mFollowHandRecordedPlacement;
    private int mFollowHandRecordedSideVerticalMode;
    private int mFollowHandSafeMaxHeightPx;
    private int mFollowHandScreenEdgeMarginPx;
    private boolean mFollowHandUseAnchorActualDisplayRect;
    private final int[] mFollowHandViewLocationOnScreenTemp;
    private boolean mFollowHandWaitingForSafeHeightLayout;
    private boolean mFrameRate;
    private boolean mGlobalDrag;
    private GradientDrawable mGradientDrawable;
    private boolean mHandleViewHasPressAnim;
    private int mHideDragViewHeight;
    private InputMethodManager mInputMethodManager;
    private boolean mIsAnimationInFirst;
    private boolean mIsAppearSpringAnimStared;
    private boolean mIsDraggable;
    private boolean mIsEntering;

    @Deprecated
    private boolean mIsExecuteNavColorAnimAfterDismiss;
    private boolean mIsExecutingDismissAnim;
    private boolean mIsFullScreenInTinyScreen;
    private boolean mIsGestureNavigation;
    private boolean mIsHandlePanel;
    private boolean mIsInTinyScreen;
    private boolean mIsInWindowFloatingMode;
    private boolean mIsInterruptingAnim;
    private boolean mIsNeedOutsideViewAnim;
    private boolean mIsNeedShowKeyboard;
    private boolean mIsRevertAnimationFromSettlingAnimation;
    private boolean mIsShowInDialogFragment;
    private boolean mIsShowInMaxHeight;
    private boolean mIsVSdk;
    private int mLastStaticState;
    private boolean mManualAppearanceLightNavigationBars;
    private float[] mMixColorDark;
    private float[] mMixColorLight;
    private int mNavColor;
    private View mNavigationCustomView;
    private boolean mNotFocusableAddedForDismiss;
    private com.oplus.wrapper.view.ViewTreeObserver.OnComputeInternalInsetsListener mOSDKComputeListener;
    private com.oplus.wrapper.view.ViewTreeObserver mOSDKViewTreeObserver;
    private View.OnAttachStateChangeListener mOnAttatchStateChangeListener;
    private OnBackInvokedCallback mOnBackInvokedCallback;
    private OnBackInvokedLocalListener mOnBackInvokedLocalListener;
    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener;
    private int mOriginWidth;
    private View.OnTouchListener mOutSideViewTouchListener;
    private View mOutsideView;
    private float mOutsideViewBackgroundAlpha;
    private Drawable mPanelBackground;
    private int mPanelBackgroundTintColor;
    private COUIPanelBarView mPanelBarView;
    private Drawable mPanelDragViewDrawable;
    private int mPanelDragViewDrawableTintColor;
    private int mPanelHeight;
    private int mPanelPaddingBottom;
    private COUIPanelPullUpListener mPanelPullUpListener;
    private float mPanelRatio;
    private Spring mPanelSpringBackAnim;
    private AnimatorSet mPanelViewTranslationAnimationSet;
    private int mPanelWidth;
    private int mParentViewPaddingBottom;
    private int mPeekHeight;
    private ViewGroup mPendingAdjustResizeLayout;
    private WindowInsets mPendingAdjustResizeWindowInsets;
    private float mPhysicsDampingRatio;
    private float mPhysicsFrequency;
    private int mPreferWidth;
    private WindowInsets mProgressWindowInsets;
    private int mPullUpMaxOffset;
    private COUIBottomSheetBehavior.PullUpToDismissPanelListener mPullUpToDismissPanelListener;
    private View mPulledUpView;
    private boolean mRegisterConfigurationChangeCallBack;
    private COUIShadowEdgeDrawable mShadowEdgeDrawable;
    private boolean mShouldRegisterWindowInsetsListener;
    private boolean mSkipCollapsed;
    private boolean mSkipStatusBarTransparentAndFont;
    private int mSnapStartBottom;
    private COUISpringForce mSpringForceAlpha;
    private COUISpringForce mSpringForceTranslationAndScale;
    private float mStartValueOfTranslateAnimation;
    private int mStatusBarHeight;
    private boolean mStokeEnable;
    private boolean mSupportExitBlockingAnimation;
    private final Rect mTemtRect;
    private float mTranslateHidingDuration;
    private COUIDynamicAnimation.OnAnimationEndListener mTranslationAndScaleEndListener;
    private COUISpringAnimation mTranslationAndScaleSpringAnimation;
    private COUIDynamicAnimation.OnAnimationUpdateListener mTranslationAndScaleUpdateListener;
    private boolean mUseNormalSmoothCorner;
    private boolean mWindowInsetsAnimEnable;
    private int mWindowInsetsLeft;
    private View.OnApplyWindowInsetsListener mWindowInsetsListener;
    private int mWindowInsetsTop;

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$1, reason: invalid class name */
    public class AnonymousClass1 implements ViewTreeObserver.OnPreDrawListener {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ViewGroup viewGroupAccess$000 = COUIBottomSheetDialog.access$000(COUIBottomSheetDialog.this);
            WindowInsets windowInsetsAccess$100 = COUIBottomSheetDialog.access$100(COUIBottomSheetDialog.this);
            COUIBottomSheetDialog.access$200(COUIBottomSheetDialog.this);
            if (viewGroupAccess$000 == null || windowInsetsAccess$100 == null) {
                return true;
            }
            COUIBottomSheetDialog.access$300(COUIBottomSheetDialog.this, windowInsetsAccess$100, viewGroupAccess$000);
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$10, reason: invalid class name */
    public class AnonymousClass10 implements View.OnAttachStateChangeListener {
        public AnonymousClass10() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            COUIBottomSheetDialog.access$1800(COUIBottomSheetDialog.this);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            COUIBottomSheetDialog.access$1900(COUIBottomSheetDialog.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$11, reason: invalid class name */
    public class AnonymousClass11 implements View.OnApplyWindowInsetsListener {
        public AnonymousClass11() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            if (view == null || view.getLayoutParams() == null) {
                return windowInsets;
            }
            if (windowInsets.equals(COUIBottomSheetDialog.access$2000(COUIBottomSheetDialog.this))) {
                COUILog.d(COUIBottomSheetDialog.TAG, "Window inset is not change, return!");
                return windowInsets;
            }
            COUIBottomSheetDialog.access$2100(COUIBottomSheetDialog.this, windowInsets);
            COUIBottomSheetDialog.access$2200(COUIBottomSheetDialog.this, windowInsets);
            if (COUIBottomSheetDialog.access$2300(COUIBottomSheetDialog.this) == null) {
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                COUIBottomSheetDialog.access$2302(cOUIBottomSheetDialog, (InputMethodManager) cOUIBottomSheetDialog.getContext().getSystemService(SystemUpdateScreenStateConfig.KEY_INPUT_METHOD));
            }
            COUIBottomSheetDialog cOUIBottomSheetDialog2 = COUIBottomSheetDialog.this;
            COUIBottomSheetDialog.access$2500(cOUIBottomSheetDialog2, windowInsets, COUIBottomSheetDialog.access$2400(cOUIBottomSheetDialog2));
            COUIBottomSheetDialog.access$2600(COUIBottomSheetDialog.this);
            if (!COUIBottomSheetDialog.access$1600(COUIBottomSheetDialog.this) && COUIBottomSheetDialog.access$2700(COUIBottomSheetDialog.this, windowInsets)) {
                COUIBottomSheetDialog cOUIBottomSheetDialog3 = COUIBottomSheetDialog.this;
                COUIBottomSheetDialog.access$2900(cOUIBottomSheetDialog3, COUIBottomSheetDialog.access$2800(cOUIBottomSheetDialog3), windowInsets);
            }
            COUIBottomSheetDialog.access$2002(COUIBottomSheetDialog.this, windowInsets);
            view.onApplyWindowInsets(COUIBottomSheetDialog.access$2000(COUIBottomSheetDialog.this));
            if (!COUIBottomSheetDialog.access$3000(COUIBottomSheetDialog.this)) {
                COUIBottomSheetDialog.access$3100(COUIBottomSheetDialog.this);
            }
            return COUIBottomSheetDialog.access$2000(COUIBottomSheetDialog.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$12, reason: invalid class name */
    public class AnonymousClass12 implements Runnable {
        public AnonymousClass12() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIBottomSheetDialog.access$3200(COUIBottomSheetDialog.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$13, reason: invalid class name */
    public class AnonymousClass13 extends AnimatorListenerAdapter {
        public AnonymousClass13() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            COUIBottomSheetDialog.access$1602(COUIBottomSheetDialog.this, false);
            COUIBottomSheetDialog.access$3500(COUIBottomSheetDialog.this);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (COUIBottomSheetDialog.access$3600(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$3600(COUIBottomSheetDialog.this).onBottomSheetDialogCollapsed();
            }
            COUIBottomSheetDialog.access$1602(COUIBottomSheetDialog.this, false);
            COUIBottomSheetDialog.access$3700(COUIBottomSheetDialog.this);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            COUIBottomSheetDialog.access$1602(COUIBottomSheetDialog.this, true);
            COUIBottomSheetDialog.access$3300(COUIBottomSheetDialog.this);
            if (COUIBottomSheetDialog.access$3400(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$3400(COUIBottomSheetDialog.this).onDismissAnimationStart();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$14, reason: invalid class name */
    public class AnonymousClass14 extends AnimatorListenerAdapter {
        public AnonymousClass14() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIBottomSheetDialog.access$3802(COUIBottomSheetDialog.this, 0.0f);
            COUIBottomSheetDialog.access$3500(COUIBottomSheetDialog.this);
            super.onAnimationCancel(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUIBottomSheetDialog.access$3600(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$3600(COUIBottomSheetDialog.this).onBottomSheetDialogCollapsed();
            }
            COUIBottomSheetDialog.access$3802(COUIBottomSheetDialog.this, 0.0f);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIBottomSheetDialog.access$1602(COUIBottomSheetDialog.this, true);
            COUIBottomSheetDialog.access$3300(COUIBottomSheetDialog.this);
            if (COUIBottomSheetDialog.access$3400(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$3400(COUIBottomSheetDialog.this).onDismissAnimationStart();
            }
            super.onAnimationStart(animator);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$15, reason: invalid class name */
    public class AnonymousClass15 implements COUIDynamicAnimation.OnAnimationEndListener {
        final /* synthetic */ Animator.AnimatorListener val$listener;

        public AnonymousClass15(Animator.AnimatorListener animatorListener) {
            this.val$listener = animatorListener;
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            if (z2) {
                this.val$listener.onAnimationCancel(null);
            } else {
                this.val$listener.onAnimationEnd(null);
            }
            COUIBottomSheetDialog.access$4000(COUIBottomSheetDialog.this).removeEndListener(COUIBottomSheetDialog.access$3900(COUIBottomSheetDialog.this));
            COUIBottomSheetDialog.access$4000(COUIBottomSheetDialog.this).removeUpdateListener(COUIBottomSheetDialog.access$4100(COUIBottomSheetDialog.this));
            if (COUIBottomSheetDialog.access$4200(COUIBottomSheetDialog.this) == null || !COUIBottomSheetDialog.access$4200(COUIBottomSheetDialog.this).isRunning()) {
                return;
            }
            COUIBottomSheetDialog.access$4200(COUIBottomSheetDialog.this).cancelComplete();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$16, reason: invalid class name */
    public class AnonymousClass16 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass16() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            float fAccess$4400 = COUIBottomSheetDialog.access$4300(COUIBottomSheetDialog.this) != COUIBottomSheetDialog.access$4400(COUIBottomSheetDialog.this) ? (f3 - COUIBottomSheetDialog.access$4400(COUIBottomSheetDialog.this)) / (COUIBottomSheetDialog.access$4300(COUIBottomSheetDialog.this) - COUIBottomSheetDialog.access$4400(COUIBottomSheetDialog.this)) : 0.0f;
            if (COUIBottomSheetDialog.access$4500(COUIBottomSheetDialog.this, 1)) {
                COUIBottomSheetDialog.access$4600(COUIBottomSheetDialog.this, f3);
            }
            if (!COUIBottomSheetDialog.access$4500(COUIBottomSheetDialog.this, 4) || COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) == null) {
                return;
            }
            float f5 = COUIBottomSheetDialog.access$4700(COUIBottomSheetDialog.this) ? (fAccess$4400 * 0.2f) + 0.8f : ((1.0f - fAccess$4400) * 0.2f) + 0.8f;
            COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).setScaleX(f5);
            COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).setScaleY(f5);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$17, reason: invalid class name */
    public class AnonymousClass17 implements COUIDynamicAnimation.OnAnimationEndListener {
        final /* synthetic */ Animator.AnimatorListener val$listener;

        public AnonymousClass17(Animator.AnimatorListener animatorListener) {
            this.val$listener = animatorListener;
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            if (z2) {
                this.val$listener.onAnimationCancel(null);
            } else {
                this.val$listener.onAnimationEnd(null);
            }
            COUIBottomSheetDialog.access$4200(COUIBottomSheetDialog.this).removeEndListener(COUIBottomSheetDialog.access$4800(COUIBottomSheetDialog.this));
            COUIBottomSheetDialog.access$4200(COUIBottomSheetDialog.this).removeUpdateListener(COUIBottomSheetDialog.access$4900(COUIBottomSheetDialog.this));
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$18, reason: invalid class name */
    public class AnonymousClass18 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass18() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            float fAccess$4400 = COUIBottomSheetDialog.access$4300(COUIBottomSheetDialog.this) != COUIBottomSheetDialog.access$4400(COUIBottomSheetDialog.this) ? (f3 - COUIBottomSheetDialog.access$4400(COUIBottomSheetDialog.this)) / (COUIBottomSheetDialog.access$4300(COUIBottomSheetDialog.this) - COUIBottomSheetDialog.access$4400(COUIBottomSheetDialog.this)) : 0.0f;
            if (COUIBottomSheetDialog.access$4500(COUIBottomSheetDialog.this, 2)) {
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                COUIBottomSheetDialog.access$5000(cOUIBottomSheetDialog, fAccess$4400, COUIBottomSheetDialog.access$4700(cOUIBottomSheetDialog));
            }
            if (!COUIBottomSheetDialog.access$4500(COUIBottomSheetDialog.this, 8) || COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) == null) {
                return;
            }
            COUIBottomSheetDialog cOUIBottomSheetDialog2 = COUIBottomSheetDialog.this;
            if (!COUIBottomSheetDialog.access$4700(cOUIBottomSheetDialog2)) {
                fAccess$4400 = Math.max(0.0f, 1.0f - fAccess$4400);
            }
            COUIBottomSheetDialog.access$5100(cOUIBottomSheetDialog2, fAccess$4400);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$19, reason: invalid class name */
    public class AnonymousClass19 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass19() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            if (z2 && COUIBottomSheetDialog.access$4500(COUIBottomSheetDialog.this, 8) && COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$5100(COUIBottomSheetDialog.this, COUIBottomSheetDialog.access$4700(COUIBottomSheetDialog.this) ? 1.0f : 0.0f);
            }
            COUIBottomSheetDialog.access$4200(COUIBottomSheetDialog.this).removeEndListener(this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$2, reason: invalid class name */
    public class AnonymousClass2 implements COUIBottomSheetBehavior.PullUpToDismissPanelListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.panel.COUIBottomSheetBehavior.PullUpToDismissPanelListener
        public void onPullUpDismiss() {
            COUIBottomSheetDialog.access$400(COUIBottomSheetDialog.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$20, reason: invalid class name */
    public class AnonymousClass20 implements ViewTreeObserver.OnGlobalLayoutListener {
        final /* synthetic */ Animator.AnimatorListener val$enterListener;
        final /* synthetic */ View val$panel;

        public AnonymousClass20(View view, Animator.AnimatorListener animatorListener) {
            this.val$panel = view;
            this.val$enterListener = animatorListener;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            int measuredWidth = this.val$panel.getMeasuredWidth();
            int measuredHeight = this.val$panel.getMeasuredHeight();
            if (measuredWidth == 0 || measuredHeight == 0) {
                return;
            }
            if (this.val$panel.getViewTreeObserver().isAlive()) {
                this.val$panel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            COUIBottomSheetDialog.access$5200(COUIBottomSheetDialog.this, this.val$enterListener);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$21, reason: invalid class name */
    public class AnonymousClass21 implements ViewTreeObserver.OnGlobalLayoutListener {
        final /* synthetic */ Animator.AnimatorListener val$enterListener;
        final /* synthetic */ boolean val$haveEnoughSpace;
        final /* synthetic */ View val$panel;

        public AnonymousClass21(View view, Animator.AnimatorListener animatorListener, boolean z2) {
            this.val$panel = view;
            this.val$enterListener = animatorListener;
            this.val$haveEnoughSpace = z2;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!COUIBottomSheetDialog.this.isShowing()) {
                if (this.val$panel.getViewTreeObserver().isAlive()) {
                    this.val$panel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                COUIBottomSheetDialog.access$5302(COUIBottomSheetDialog.this, false);
                return;
            }
            if (this.val$panel.getMeasuredWidth() == 0 || this.val$panel.getMeasuredHeight() == 0 || COUIBottomSheetDialog.access$5400(COUIBottomSheetDialog.this)) {
                return;
            }
            if (this.val$panel.getViewTreeObserver().isAlive()) {
                this.val$panel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            COUIBottomSheetDialog.access$5302(COUIBottomSheetDialog.this, false);
            if (COUIBottomSheetDialog.access$900()) {
                Log.d(COUIBottomSheetDialog.TAG, "[follow_hand_position] safeHeightLayoutReady: measuredHeight=" + this.val$panel.getMeasuredHeight() + ", safeMaxHeight=" + COUIBottomSheetDialog.access$5500(COUIBottomSheetDialog.this) + ", startAnimation=true");
            }
            COUIBottomSheetDialog.access$5600(COUIBottomSheetDialog.this, this.val$enterListener, this.val$haveEnoughSpace);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$22, reason: invalid class name */
    public class AnonymousClass22 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass22() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIBottomSheetDialog.access$4600(COUIBottomSheetDialog.this, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$23, reason: invalid class name */
    public class AnonymousClass23 extends AnimatorListenerAdapter {
        public AnonymousClass23() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUILog.d(COUIBottomSheetDialog.TAG, "LEVEL_HIGH_PRECISION onAnimatorEnd: DynamicFrameRateManager.FRAME_RATE_END");
            DynamicFrameRateManager.setFrameRate(COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this), COUIBottomSheetDialog.ANIMATION_TYPE_ID, -2, (Bundle) null);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$24, reason: invalid class name */
    public class AnonymousClass24 extends AnimatorListenerAdapter {
        public AnonymousClass24() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUILog.d(COUIBottomSheetDialog.TAG, "LEVEL_LOW_PRECISION onAnimatorEnd: DynamicFrameRateManager.FRAME_RATE_END");
            DynamicFrameRateManager.setFrameRate(COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this), COUIBottomSheetDialog.ANIMATION_TYPE_ID, -2, (Bundle) null);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            COUILog.d(COUIBottomSheetDialog.TAG, "LEVEL_LOW_PRECISION onAnimatorStart: DynamicFrameRateManager.LOW_PRECISION_FRAME_RATE");
            DynamicFrameRateManager.setFrameRate(COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this), COUIBottomSheetDialog.ANIMATION_TYPE_ID, -1, (Bundle) null);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$25, reason: invalid class name */
    public class AnonymousClass25 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass25() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUILog.d(COUIBottomSheetDialog.TAG, "COUISpringAnimation LEVEL_HIGH_PRECISION onAnimatorEnd: DynamicFrameRateManager.FRAME_RATE_END");
            DynamicFrameRateManager.setFrameRate(COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this), COUIBottomSheetDialog.ANIMATION_TYPE_ID, -2, (Bundle) null);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$26, reason: invalid class name */
    public class AnonymousClass26 implements COUIDynamicAnimation.OnAnimationEndListener {
        public AnonymousClass26() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            COUILog.d(COUIBottomSheetDialog.TAG, "COUISpringAnimation LEVEL_LOW_PRECISION onAnimatorEnd: DynamicFrameRateManager.FRAME_RATE_END");
            DynamicFrameRateManager.setFrameRate(COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this), COUIBottomSheetDialog.ANIMATION_TYPE_ID, -2, (Bundle) null);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$27, reason: invalid class name */
    public class AnonymousClass27 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ float val$endAlphaValue;
        final /* synthetic */ boolean val$isShow;
        final /* synthetic */ float val$startAlphaValue;

        public AnonymousClass27(float f3, float f4, boolean z2) {
            this.val$startAlphaValue = f3;
            this.val$endAlphaValue = f4;
            this.val$isShow = z2;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            float f3 = this.val$startAlphaValue;
            float f4 = this.val$endAlphaValue;
            COUIBottomSheetDialog.access$5000(COUIBottomSheetDialog.this, f3 != f4 ? (fFloatValue - f3) / (f4 - f3) : 0.0f, this.val$isShow);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$28, reason: invalid class name */
    public class AnonymousClass28 extends AnimatorListenerAdapter {
        public AnonymousClass28() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) != null && COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getAlpha() == 0.0f) {
                COUIBottomSheetDialog.access$5100(COUIBottomSheetDialog.this, 1.0f);
            }
            COUIBottomSheetDialog.access$5702(COUIBottomSheetDialog.this, false);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$29, reason: invalid class name */
    public class AnonymousClass29 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ Window val$window;

        public AnonymousClass29(Window window) {
            this.val$window = window;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.val$window.setNavigationBarColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$3, reason: invalid class name */
    public class AnonymousClass3 implements ComponentCallbacks {
        public AnonymousClass3() {
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            if (COUIBottomSheetDialog.access$500(COUIBottomSheetDialog.this)) {
                COUIBottomSheetDialog.this.updateLayoutWhileConfigChange(configuration);
            }
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$30, reason: invalid class name */
    public class AnonymousClass30 implements SpringListener {
        public AnonymousClass30() {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringActivate(Spring spring) {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringAtRest(Spring spring) {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringEndStateChange(Spring spring) {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringUpdate(Spring spring) {
            if (COUIBottomSheetDialog.access$5800(COUIBottomSheetDialog.this) == null || COUIBottomSheetDialog.access$5900(COUIBottomSheetDialog.this) == null) {
                return;
            }
            int currentValue = (int) spring.getCurrentValue();
            if (currentValue >= 100) {
                COUIBottomSheetDialog.access$5800(COUIBottomSheetDialog.this).setEndValue(0.0d);
            }
            COUIBottomSheetDialog.access$5900(COUIBottomSheetDialog.this).setTranslationY(currentValue);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$31, reason: invalid class name */
    public class AnonymousClass31 implements ViewTreeObserver.OnPreDrawListener {
        public AnonymousClass31() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            COUIBottomSheetDialog.access$6000(COUIBottomSheetDialog.this);
            if (COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) == null) {
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                COUIBottomSheetDialog.access$6800(cOUIBottomSheetDialog, 0, COUIBottomSheetDialog.access$6700(cOUIBottomSheetDialog));
                return true;
            }
            COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).setVisibility(0);
            int iAccess$6100 = COUIBottomSheetDialog.access$6100(COUIBottomSheetDialog.this);
            if (COUIBottomSheetDialog.access$6200(COUIBottomSheetDialog.this)) {
                iAccess$6100 = COUIBottomSheetDialog.access$6300(COUIBottomSheetDialog.this);
            }
            COUIPanelContentLayout cOUIPanelContentLayout = COUIBottomSheetDialog.this.mDraggableConstraintLayout;
            if ((cOUIPanelContentLayout == null || cOUIPanelContentLayout.findFocus() == null) && !COUIBottomSheetDialog.access$3000(COUIBottomSheetDialog.this) && !COUIBottomSheetDialog.access$6400(COUIBottomSheetDialog.this)) {
                COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).setTranslationY(iAccess$6100);
            }
            if (COUIBottomSheetDialog.access$6500(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$6500(COUIBottomSheetDialog.this).setAlpha(0.0f);
            }
            if (COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getRatio() == 2.0f) {
                COUIBottomSheetDialog cOUIBottomSheetDialog2 = COUIBottomSheetDialog.this;
                COUIBottomSheetDialog.access$6800(cOUIBottomSheetDialog2, COUIBottomSheetDialog.access$6600(cOUIBottomSheetDialog2).getHeight() / 2, COUIBottomSheetDialog.access$6700(COUIBottomSheetDialog.this));
            } else {
                COUIBottomSheetDialog cOUIBottomSheetDialog3 = COUIBottomSheetDialog.this;
                COUIBottomSheetDialog.access$6800(cOUIBottomSheetDialog3, 0, COUIBottomSheetDialog.access$6700(cOUIBottomSheetDialog3));
            }
            COUIBottomSheetDialog cOUIBottomSheetDialog4 = COUIBottomSheetDialog.this;
            COUIBottomSheetDialog.access$6902(cOUIBottomSheetDialog4, COUIBottomSheetDialog.access$1400(cOUIBottomSheetDialog4).getRatio());
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$32, reason: invalid class name */
    public class AnonymousClass32 extends AnimatorListenerAdapter {
        public AnonymousClass32() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIBottomSheetDialog.access$7100(COUIBottomSheetDialog.this);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIBottomSheetDialog.access$7000(COUIBottomSheetDialog.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$33, reason: invalid class name */
    public class AnonymousClass33 implements COUIPanelPullUpListener {
        private int mLastPosition = -1;

        public AnonymousClass33() {
        }

        @Override // com.coui.appcompat.panel.COUIPanelPullUpListener
        public void onCancel() {
            COUIBottomSheetDialog.access$7500(COUIBottomSheetDialog.this, 0);
        }

        @Override // com.coui.appcompat.panel.COUIPanelPullUpListener
        public int onDragging(int i3, int i4) {
            if (COUIBottomSheetDialog.access$7200(COUIBottomSheetDialog.this) != null && COUIBottomSheetDialog.access$7200(COUIBottomSheetDialog.this).getVelocity() != 0.0d) {
                COUIBottomSheetDialog.access$7200(COUIBottomSheetDialog.this).setAtRest();
                return COUIBottomSheetDialog.access$7300(COUIBottomSheetDialog.this);
            }
            int iClamp = MathUtils.clamp((int) (COUIBottomSheetDialog.access$7300(COUIBottomSheetDialog.this) - (i3 * 0.19999999f)), 0, Math.min(COUIBottomSheetDialog.access$7400(COUIBottomSheetDialog.this), Math.max(0, COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getTop())));
            if (COUIBottomSheetDialog.access$7300(COUIBottomSheetDialog.this) != iClamp) {
                COUIBottomSheetDialog.access$7302(COUIBottomSheetDialog.this, iClamp);
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                COUIBottomSheetDialog.access$7500(cOUIBottomSheetDialog, COUIBottomSheetDialog.access$7300(cOUIBottomSheetDialog));
            }
            return COUIBottomSheetDialog.access$7300(COUIBottomSheetDialog.this);
        }

        @Override // com.coui.appcompat.panel.COUIPanelPullUpListener
        public void onDraggingPanel() {
            COUIBottomSheetDialog.access$8500(COUIBottomSheetDialog.this);
        }

        @Override // com.coui.appcompat.panel.COUIPanelPullUpListener
        public void onOffsetChanged(float f3) {
            if (COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) == null) {
                return;
            }
            if (this.mLastPosition == -1) {
                this.mLastPosition = COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getHeight();
            }
            if (COUIBottomSheetDialog.access$7700(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$7700(COUIBottomSheetDialog.this).onDialogOffsetChanged(COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getTop());
            }
            if (COUIBottomSheetDialog.access$7800(COUIBottomSheetDialog.this) && !COUIBottomSheetDialog.access$1600(COUIBottomSheetDialog.this)) {
                float fMax = Math.max(0.0f, COUIBottomSheetDialog.this.getOutsideViewAlpha(f3));
                if (COUIBottomSheetDialog.access$6500(COUIBottomSheetDialog.this) != null) {
                    COUIBottomSheetDialog.access$6500(COUIBottomSheetDialog.this).setAlpha(fMax);
                }
                COUIBottomSheetDialog.access$7902(COUIBottomSheetDialog.this, fMax);
                if (!COUIPanelMultiWindowUtils.isSmallScreen(COUIBottomSheetDialog.this.getContext(), null) && COUINavigationBarUtil.isNavigationBarShow(COUIBottomSheetDialog.this.getContext()) && ((!COUIBottomSheetDialog.access$8000(COUIBottomSheetDialog.this) || COUIBottomSheetDialog.access$8100(COUIBottomSheetDialog.this)) && COUIBottomSheetDialog.this.getWindow() != null && ((int) (COUIBottomSheetDialog.access$8200(COUIBottomSheetDialog.this) * f3)) != 0 && !COUINavigationBarUtil.isGestureNavigation(COUIBottomSheetDialog.this.getContext()))) {
                    COUIBottomSheetDialog.access$8300(COUIBottomSheetDialog.this, fMax);
                }
            }
            if (COUIBottomSheetDialog.access$8400(COUIBottomSheetDialog.this) == null || f3 == 1.0f || !COUIBottomSheetDialog.access$8500(COUIBottomSheetDialog.this)) {
                return;
            }
            COUIBottomSheetDialog.access$8400(COUIBottomSheetDialog.this).setPanelOffset(this.mLastPosition - ((int) (COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getHeight() * f3)));
            this.mLastPosition = (int) (COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getHeight() * f3);
        }

        @Override // com.coui.appcompat.panel.COUIPanelPullUpListener
        public void onReleased(int i3) {
            COUIBottomSheetDialog.this.setCanPullUp(false);
            int top = COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getTop() - (i3 - COUIBottomSheetDialog.access$7300(COUIBottomSheetDialog.this));
            COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
            COUIBottomSheetDialog.access$7600(cOUIBottomSheetDialog, COUIBottomSheetDialog.access$7300(cOUIBottomSheetDialog) - top);
        }

        @Override // com.coui.appcompat.panel.COUIPanelPullUpListener
        public void onReleasedDrag() {
            COUIBottomSheetDialog.access$8500(COUIBottomSheetDialog.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$34, reason: invalid class name */
    public class AnonymousClass34 implements SpringListener {
        final /* synthetic */ int val$offset;

        public AnonymousClass34(int i3) {
            this.val$offset = i3;
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringActivate(Spring spring) {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringAtRest(Spring spring) {
            if ((COUIBottomSheetDialog.this.getBehavior() instanceof COUIBottomSheetBehavior) && COUIBottomSheetDialog.access$8700(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$7302(COUIBottomSheetDialog.this, 0);
                COUIBottomSheetDialog.access$7500(COUIBottomSheetDialog.this, 0);
                ((COUIBottomSheetBehavior) COUIBottomSheetDialog.this.getBehavior()).setStateInternal(3);
            }
            COUIBottomSheetDialog.this.setCanPullUp(true);
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringEndStateChange(Spring spring) {
        }

        @Override // com.facebook.rebound.SpringListener
        public void onSpringUpdate(Spring spring) {
            if (COUIBottomSheetDialog.access$7200(COUIBottomSheetDialog.this) == null || COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) == null) {
                return;
            }
            if (spring.wasAtRest() && spring.getVelocity() == 0.0d) {
                COUIBottomSheetDialog.access$7200(COUIBottomSheetDialog.this).setAtRest();
                return;
            }
            int currentValue = (int) spring.getCurrentValue();
            COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).offsetTopAndBottom(currentValue - COUIBottomSheetDialog.access$8600(COUIBottomSheetDialog.this));
            COUIBottomSheetDialog.access$8602(COUIBottomSheetDialog.this, currentValue);
            COUIBottomSheetDialog.access$7500(COUIBottomSheetDialog.this, this.val$offset - currentValue);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$35, reason: invalid class name */
    public class AnonymousClass35 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ ViewTreeObserver val$observer;

        public AnonymousClass35(ViewTreeObserver viewTreeObserver) {
            this.val$observer = viewTreeObserver;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            if (this.val$observer.isAlive()) {
                this.val$observer.removeOnPreDrawListener(this);
            }
            if (!COUIBottomSheetDialog.this.isShowing()) {
                return true;
            }
            COUIBottomSheetDialog.access$8800(COUIBottomSheetDialog.this);
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$36, reason: invalid class name */
    public class AnonymousClass36 implements View.OnAttachStateChangeListener {
        public AnonymousClass36() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            COUIBottomSheetDialog.access$8900(COUIBottomSheetDialog.this, view);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            view.removeOnAttachStateChangeListener(this);
            if (COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                COUIBottomSheetDialog.access$9000(cOUIBottomSheetDialog, COUIBottomSheetDialog.access$1400(cOUIBottomSheetDialog));
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$37, reason: invalid class name */
    public class AnonymousClass37 implements OnBackInvokedCallback {
        public AnonymousClass37() {
        }

        @Override // android.window.OnBackInvokedCallback
        public void onBackInvoked() {
            if (COUIBottomSheetDialog.access$9100(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$9100(COUIBottomSheetDialog.this).onBackInvokedLocal();
            } else {
                COUIBottomSheetDialog.this.onBackPressed();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$38, reason: invalid class name */
    public class AnonymousClass38 implements COUIDynamicAnimation.OnAnimationEndListener {
        final /* synthetic */ Animator.AnimatorListener val$listener;

        public AnonymousClass38(Animator.AnimatorListener animatorListener) {
            this.val$listener = animatorListener;
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
            if (z2) {
                this.val$listener.onAnimationCancel(null);
            } else {
                this.val$listener.onAnimationEnd(null);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$4, reason: invalid class name */
    public class AnonymousClass4 implements COUIBackgroundBlurBuilder.OnBlurryStateChangeListener {
        public AnonymousClass4() {
        }

        @Override // com.coui.appcompat.uiutil.COUIBackgroundBlurBuilder.OnBlurryStateChangeListener
        public void onBlurryStateChanged(boolean z2) {
            COUIBottomSheetDialog.access$600(COUIBottomSheetDialog.this);
            if (COUIBottomSheetDialog.access$700(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$700(COUIBottomSheetDialog.this).onBlurryStateChanged(z2);
            }
            COUIBottomSheetDialog.access$800(COUIBottomSheetDialog.this);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$5, reason: invalid class name */
    public class AnonymousClass5 extends COUIBottomSheetBehavior.COUIBottomSheetCallback {
        public AnonymousClass5() {
        }

        @Override // com.coui.appcompat.panel.COUIBottomSheetBehavior.COUIBottomSheetCallback
        public void onSlide(View view, float f3) {
        }

        @Override // com.coui.appcompat.panel.COUIBottomSheetBehavior.COUIBottomSheetCallback
        public void onStateChanged(View view, int i3) {
            if (COUIBottomSheetDialog.access$900()) {
                Log.d(COUIBottomSheetDialog.TAG, "onStateChanged: newState=" + i3);
            }
            COUIBottomSheetDialog.access$1000(COUIBottomSheetDialog.this, view, i3);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$6, reason: invalid class name */
    public class AnonymousClass6 implements View.OnClickListener {
        public AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIBottomSheetDialog.access$1100(COUIBottomSheetDialog.this) && COUIBottomSheetDialog.this.isShowing() && COUIBottomSheetDialog.access$1200(COUIBottomSheetDialog.this)) {
                COUIBottomSheetDialog.this.cancel();
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$7, reason: invalid class name */
    public class AnonymousClass7 implements View.OnLayoutChangeListener {
        public AnonymousClass7() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            if (COUIBottomSheetDialog.access$1300(COUIBottomSheetDialog.this) != null) {
                COUIBottomSheetDialog.access$1300(COUIBottomSheetDialog.this).setBounds(0, 0, i5 - i3, i6 - i4);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$8, reason: invalid class name */
    public class AnonymousClass8 implements View.OnAttachStateChangeListener {
        public AnonymousClass8() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
            COUIBottomSheetDialog.access$1500(cOUIBottomSheetDialog, COUIBottomSheetDialog.access$1400(cOUIBottomSheetDialog));
            COUIBottomSheetDialog.this.doBlurInternal();
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            Log.d(COUIBottomSheetDialog.TAG, "mDesignBottomSheetFrameLayout onViewDetachedFromWindow");
            COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).removeOnAttachStateChangeListener(this);
            COUIBottomSheetDialog.access$1500(COUIBottomSheetDialog.this, null);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.panel.COUIBottomSheetDialog$9, reason: invalid class name */
    public class AnonymousClass9 implements com.oplus.wrapper.view.ViewTreeObserver.OnComputeInternalInsetsListener {
        public AnonymousClass9() {
        }

        public void onComputeInternalInsets(com.oplus.wrapper.view.ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            internalInsetsInfo.setTouchableInsets(com.oplus.wrapper.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_REGION);
            if (COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this) == null) {
                Log.e(COUIBottomSheetDialog.TAG, "onComputeInternalInsets mDesignBottomSheetFrameLayout == null");
                internalInsetsInfo.getTouchableRegion().set(0, 0, 0, 0);
            } else if (!COUIBottomSheetDialog.access$1600(COUIBottomSheetDialog.this)) {
                internalInsetsInfo.getTouchableRegion().set(0, 0, COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getRootView().getWidth(), COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getRootView().getHeight());
            } else {
                COUIBottomSheetDialog.access$1400(COUIBottomSheetDialog.this).getGlobalVisibleRect(COUIBottomSheetDialog.access$1700(COUIBottomSheetDialog.this));
                internalInsetsInfo.getTouchableRegion().set(COUIBottomSheetDialog.access$1700(COUIBottomSheetDialog.this));
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    public interface BottomSheetDialogAnimatorListener {
        void onBottomSheetDialogCollapsed();

        void onBottomSheetDialogExpanded();
    }

    public interface DialogOffsetListener {
        void onDialogOffsetChanged(float f3);
    }

    public interface OnAnimationListener {
        default void onDismissAnimationEnd() {
        }

        default void onDismissAnimationStart() {
        }

        default void onShowAnimationEnd() {
        }

        default void onShowAnimationStart() {
        }
    }

    public interface OnBackInvokedLocalListener {
        void onBackInvokedLocal();
    }

    static {
        COUIInEaseInterpolator cOUIInEaseInterpolator = new COUIInEaseInterpolator();
        SHOW_HEIGHT_ANIM_INTERPOLATOR = cOUIInEaseInterpolator;
        OUTSIDE_ALPHA_ANIM_INTERPOLATOR = new COUIEaseInterpolator();
        DISMISS_ALPHA_ANIM_INTERPOLATOR = cOUIInEaseInterpolator;
        DEBUG = COUILog.LOG_DEBUG || Log.isLoggable(TAG, 3);
    }

    public COUIBottomSheetDialog(Context context) {
        this(context, 0);
    }

    public static /* synthetic */ ViewGroup access$000(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mPendingAdjustResizeLayout;
    }

    public static /* synthetic */ WindowInsets access$100(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mPendingAdjustResizeWindowInsets;
    }

    public static /* synthetic */ void access$1000(COUIBottomSheetDialog cOUIBottomSheetDialog, View view, int i3) {
        cOUIBottomSheetDialog.handleBehaviorStateChange(view, i3);
    }

    public static /* synthetic */ boolean access$1100(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mCancelable;
    }

    public static /* synthetic */ boolean access$1200(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mCanceledOnTouchOutside;
    }

    public static /* synthetic */ COUIShadowEdgeDrawable access$1300(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mShadowEdgeDrawable;
    }

    public static /* synthetic */ COUIPanelPercentFrameLayout access$1400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mDesignBottomSheetFrameLayout;
    }

    public static /* synthetic */ void access$1500(COUIBottomSheetDialog cOUIBottomSheetDialog, View view) {
        cOUIBottomSheetDialog.setShadowEdgeDrawableCallback(view);
    }

    public static /* synthetic */ boolean access$1600(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mIsExecutingDismissAnim;
    }

    public static /* synthetic */ boolean access$1602(COUIBottomSheetDialog cOUIBottomSheetDialog, boolean z2) {
        cOUIBottomSheetDialog.mIsExecutingDismissAnim = z2;
        return z2;
    }

    public static /* synthetic */ Rect access$1700(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mTemtRect;
    }

    public static /* synthetic */ void access$1800(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.addOSDKViewTreeObserver();
    }

    public static /* synthetic */ void access$1900(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.removeOSDKViewTreeObserver();
    }

    public static /* synthetic */ void access$200(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.clearPendingAdjustResize();
    }

    public static /* synthetic */ WindowInsets access$2000(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mApplyWindowInsets;
    }

    public static /* synthetic */ WindowInsets access$2002(COUIBottomSheetDialog cOUIBottomSheetDialog, WindowInsets windowInsets) {
        cOUIBottomSheetDialog.mApplyWindowInsets = windowInsets;
        return windowInsets;
    }

    public static /* synthetic */ void access$2100(COUIBottomSheetDialog cOUIBottomSheetDialog, WindowInsets windowInsets) {
        cOUIBottomSheetDialog.initCoordinateInsets(windowInsets);
    }

    public static /* synthetic */ void access$2200(COUIBottomSheetDialog cOUIBottomSheetDialog, WindowInsets windowInsets) {
        cOUIBottomSheetDialog.initMaxHeight(windowInsets);
    }

    public static /* synthetic */ InputMethodManager access$2300(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mInputMethodManager;
    }

    public static /* synthetic */ InputMethodManager access$2302(COUIBottomSheetDialog cOUIBottomSheetDialog, InputMethodManager inputMethodManager) {
        cOUIBottomSheetDialog.mInputMethodManager = inputMethodManager;
        return inputMethodManager;
    }

    public static /* synthetic */ boolean access$2400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mAdjustResizeEnable;
    }

    public static /* synthetic */ void access$2500(COUIBottomSheetDialog cOUIBottomSheetDialog, WindowInsets windowInsets, boolean z2) {
        cOUIBottomSheetDialog.adjustResize(windowInsets, z2);
    }

    public static /* synthetic */ void access$2600(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.largeScreenLimitMaxSize();
    }

    public static /* synthetic */ boolean access$2700(COUIBottomSheetDialog cOUIBottomSheetDialog, WindowInsets windowInsets) {
        return cOUIBottomSheetDialog.shouldUpdatePanelMarginBottom(windowInsets);
    }

    public static /* synthetic */ Configuration access$2800(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mConfiguration;
    }

    public static /* synthetic */ void access$2900(COUIBottomSheetDialog cOUIBottomSheetDialog, Configuration configuration, WindowInsets windowInsets) {
        cOUIBottomSheetDialog.updatePanelMarginBottom(configuration, windowInsets);
    }

    public static /* synthetic */ void access$300(COUIBottomSheetDialog cOUIBottomSheetDialog, WindowInsets windowInsets, ViewGroup viewGroup) {
        cOUIBottomSheetDialog.adjustResizeInternal(windowInsets, viewGroup);
    }

    public static /* synthetic */ boolean access$3000(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.isFollowHand();
    }

    public static /* synthetic */ void access$3100(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.initOrRefreshNavigationView();
    }

    public static /* synthetic */ void access$3200(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.syncFollowHandCoordinatorScreenTopOffset();
    }

    public static /* synthetic */ void access$3300(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.enableBackEventPassThroughForDismiss();
    }

    public static /* synthetic */ OnAnimationListener access$3400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mAnimationListener;
    }

    public static /* synthetic */ void access$3500(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.restoreWindowFocusAfterDismiss();
    }

    public static /* synthetic */ BottomSheetDialogAnimatorListener access$3600(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mBottomSheetDialogAnimatorListener;
    }

    public static /* synthetic */ void access$3700(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.superDismiss();
    }

    public static /* synthetic */ float access$3802(COUIBottomSheetDialog cOUIBottomSheetDialog, float f3) {
        cOUIBottomSheetDialog.mCurrentOutSideAlphaStateHidden = f3;
        return f3;
    }

    public static /* synthetic */ COUIDynamicAnimation.OnAnimationEndListener access$3900(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mTranslationAndScaleEndListener;
    }

    public static /* synthetic */ void access$400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.dismissWithAlphaAnim();
    }

    public static /* synthetic */ COUISpringAnimation access$4000(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mTranslationAndScaleSpringAnimation;
    }

    public static /* synthetic */ COUIDynamicAnimation.OnAnimationUpdateListener access$4100(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mTranslationAndScaleUpdateListener;
    }

    public static /* synthetic */ COUISpringAnimation access$4200(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mAlphaSpringAnimation;
    }

    public static /* synthetic */ float access$4300(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mEndValueOfTranslateAnimation;
    }

    public static /* synthetic */ float access$4400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mStartValueOfTranslateAnimation;
    }

    public static /* synthetic */ boolean access$4500(COUIBottomSheetDialog cOUIBottomSheetDialog, int i3) {
        return cOUIBottomSheetDialog.hasAnimationFlag(i3);
    }

    public static /* synthetic */ void access$4600(COUIBottomSheetDialog cOUIBottomSheetDialog, float f3) {
        cOUIBottomSheetDialog.translateUpdate(f3);
    }

    public static /* synthetic */ boolean access$4700(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mIsEntering;
    }

    public static /* synthetic */ COUIDynamicAnimation.OnAnimationEndListener access$4800(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mAlphaSpringEndListener;
    }

    public static /* synthetic */ COUIDynamicAnimation.OnAnimationUpdateListener access$4900(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mAlphaSpringUpdateListener;
    }

    public static /* synthetic */ boolean access$500(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mRegisterConfigurationChangeCallBack;
    }

    public static /* synthetic */ void access$5000(COUIBottomSheetDialog cOUIBottomSheetDialog, float f3, boolean z2) {
        cOUIBottomSheetDialog.outsideAlphaChange(f3, z2);
    }

    public static /* synthetic */ void access$5100(COUIBottomSheetDialog cOUIBottomSheetDialog, float f3) {
        cOUIBottomSheetDialog.setAlphaViewAlpha(f3);
    }

    public static /* synthetic */ void access$5200(COUIBottomSheetDialog cOUIBottomSheetDialog, Animator.AnimatorListener animatorListener) {
        cOUIBottomSheetDialog.startFollowHandShowingAnim(animatorListener);
    }

    public static /* synthetic */ boolean access$5302(COUIBottomSheetDialog cOUIBottomSheetDialog, boolean z2) {
        cOUIBottomSheetDialog.mFollowHandWaitingForSafeHeightLayout = z2;
        return z2;
    }

    public static /* synthetic */ boolean access$5400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.shouldWaitForFollowHandSafeHeightLayout();
    }

    public static /* synthetic */ int access$5500(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mFollowHandSafeMaxHeightPx;
    }

    public static /* synthetic */ void access$5600(COUIBottomSheetDialog cOUIBottomSheetDialog, Animator.AnimatorListener animatorListener, boolean z2) {
        cOUIBottomSheetDialog.completeFollowHandShowingAnim(animatorListener, z2);
    }

    public static /* synthetic */ boolean access$5702(COUIBottomSheetDialog cOUIBottomSheetDialog, boolean z2) {
        cOUIBottomSheetDialog.mIsNeedShowKeyboard = z2;
        return z2;
    }

    public static /* synthetic */ Spring access$5800(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mDisableFastCloseFeedbackSpring;
    }

    public static /* synthetic */ View access$5900(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mFeedBackView;
    }

    public static /* synthetic */ void access$600(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.updateBottomSheetDialogAlphaView();
    }

    public static /* synthetic */ void access$6000(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.removeOnPreDrawListener();
    }

    public static /* synthetic */ int access$6100(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.getContentViewHeightWithMargins();
    }

    public static /* synthetic */ boolean access$6200(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mFirstShowCollapsed;
    }

    public static /* synthetic */ int access$6300(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mPeekHeight;
    }

    public static /* synthetic */ boolean access$6400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.isFadeInCenter();
    }

    public static /* synthetic */ View access$6500(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mOutsideView;
    }

    public static /* synthetic */ View access$6600(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mCoordinatorLayout;
    }

    public static /* synthetic */ Animator.AnimatorListener access$6700(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.getPanelShowAnimListener();
    }

    public static /* synthetic */ void access$6800(COUIBottomSheetDialog cOUIBottomSheetDialog, int i3, Animator.AnimatorListener animatorListener) {
        cOUIBottomSheetDialog.doParentViewTranslationShowingAnim(i3, animatorListener);
    }

    public static /* synthetic */ float access$6902(COUIBottomSheetDialog cOUIBottomSheetDialog, float f3) {
        cOUIBottomSheetDialog.mPanelRatio = f3;
        return f3;
    }

    public static /* synthetic */ COUIBackgroundBlurBuilder.OnBlurryStateChangeListener access$700(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mBlurryStateChangeListener;
    }

    public static /* synthetic */ void access$7000(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.animationStart();
    }

    public static /* synthetic */ void access$7100(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.animationEnd();
    }

    public static /* synthetic */ Spring access$7200(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mPanelSpringBackAnim;
    }

    public static /* synthetic */ int access$7300(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mParentViewPaddingBottom;
    }

    public static /* synthetic */ int access$7302(COUIBottomSheetDialog cOUIBottomSheetDialog, int i3) {
        cOUIBottomSheetDialog.mParentViewPaddingBottom = i3;
        return i3;
    }

    public static /* synthetic */ int access$7400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mPullUpMaxOffset;
    }

    public static /* synthetic */ void access$7500(COUIBottomSheetDialog cOUIBottomSheetDialog, int i3) {
        cOUIBottomSheetDialog.setPulledUpViewPaddingBottom(i3);
    }

    public static /* synthetic */ void access$7600(COUIBottomSheetDialog cOUIBottomSheetDialog, int i3) {
        cOUIBottomSheetDialog.doSpringBackReboundAnim(i3);
    }

    public static /* synthetic */ DialogOffsetListener access$7700(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mDialogOffsetListener;
    }

    public static /* synthetic */ boolean access$7800(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mIsNeedOutsideViewAnim;
    }

    public static /* synthetic */ float access$7902(COUIBottomSheetDialog cOUIBottomSheetDialog, float f3) {
        cOUIBottomSheetDialog.mCurrentOutsideAlpha = f3;
        return f3;
    }

    public static /* synthetic */ void access$800(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.setStrokeEffectEnable();
    }

    public static /* synthetic */ boolean access$8000(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mIsHandlePanel;
    }

    public static /* synthetic */ boolean access$8100(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.shouldHandlePanelUpdateNavBarColor();
    }

    public static /* synthetic */ float access$8200(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mOutsideViewBackgroundAlpha;
    }

    public static /* synthetic */ void access$8300(COUIBottomSheetDialog cOUIBottomSheetDialog, float f3) {
        cOUIBottomSheetDialog.setNavigationBarColorAlpha(f3);
    }

    public static /* synthetic */ COUIPanelBarView access$8400(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mPanelBarView;
    }

    public static /* synthetic */ boolean access$8500(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mIsInTinyScreen;
    }

    public static /* synthetic */ int access$8600(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mCurrentSpringTotalOffset;
    }

    public static /* synthetic */ int access$8602(COUIBottomSheetDialog cOUIBottomSheetDialog, int i3) {
        cOUIBottomSheetDialog.mCurrentSpringTotalOffset = i3;
        return i3;
    }

    public static /* synthetic */ View access$8700(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mPulledUpView;
    }

    public static /* synthetic */ void access$8800(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        cOUIBottomSheetDialog.applyFollowHandBottomCenterFromMeasuredLayout();
    }

    public static /* synthetic */ void access$8900(COUIBottomSheetDialog cOUIBottomSheetDialog, View view) {
        cOUIBottomSheetDialog.registerBackCallback(view);
    }

    public static /* synthetic */ boolean access$900() {
        return DEBUG;
    }

    public static /* synthetic */ void access$9000(COUIBottomSheetDialog cOUIBottomSheetDialog, View view) {
        cOUIBottomSheetDialog.stopListeningForBackCallbacks(view);
    }

    public static /* synthetic */ OnBackInvokedLocalListener access$9100(COUIBottomSheetDialog cOUIBottomSheetDialog) {
        return cOUIBottomSheetDialog.mOnBackInvokedLocalListener;
    }

    private void addAnimationFlag(int i3) {
        this.mAnimationFlag = i3 | this.mAnimationFlag;
    }

    private void addOSDKViewTreeObserver() {
        if (this.mOSDKViewTreeObserver != null || this.mOutsideView == null) {
            return;
        }
        com.oplus.wrapper.view.ViewTreeObserver viewTreeObserver = new com.oplus.wrapper.view.ViewTreeObserver(this.mOutsideView.getViewTreeObserver());
        this.mOSDKViewTreeObserver = viewTreeObserver;
        viewTreeObserver.addOnComputeInternalInsetsListener(this.mOSDKComputeListener);
    }

    private void adjustResize(WindowInsets windowInsets, boolean z2) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        if (!z2 || (cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout) == null) {
            return;
        }
        adjustResizeInternal(windowInsets, cOUIPanelPercentFrameLayout);
    }

    private void adjustResizeInternal(WindowInsets windowInsets, ViewGroup viewGroup) {
        if (viewGroup != null && viewGroup.isLayoutRequested()) {
            scheduleAdjustResizeAfterLayout(windowInsets, viewGroup);
        } else {
            clearPendingAdjustResize();
            getAdjustResizeHelper().adjustResize(getContext(), viewGroup, windowInsets, this.mCoordinatorLayout, getFocusChange());
        }
    }

    private void animationEnd() {
        if (this.mDesignBottomSheetFrameLayout != null) {
            if (!isFollowHand() && !isFadeInCenter()) {
                this.mDesignBottomSheetFrameLayout.setTranslationY(this.mCurrentParentViewTranslationY);
            }
            if (getBehavior() != null && getBehavior().getState() == 3 && this.mCanPerformHapticFeedback) {
                this.mDesignBottomSheetFrameLayout.performHapticFeedback(14);
            }
        }
        OnAnimationListener onAnimationListener = this.mAnimationListener;
        if (onAnimationListener != null) {
            onAnimationListener.onShowAnimationEnd();
        }
    }

    private void animationStart() {
        if (getBehavior() != null && getBehavior().getState() == 5) {
            ((COUIBottomSheetBehavior) getBehavior()).setPanelState(this.mLastStaticState);
        }
        OnAnimationListener onAnimationListener = this.mAnimationListener;
        if (onAnimationListener != null) {
            onAnimationListener.onShowAnimationStart();
        }
    }

    private void applyAppearanceLightNavigationBars() {
        Window window;
        if (this.mManualAppearanceLightNavigationBars && (window = getWindow()) != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            OplusBaseLayoutParams oplusBaseLayoutParams = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, attributes);
            if (oplusBaseLayoutParams != null) {
                oplusBaseLayoutParams.oplusFlags |= 32768;
                window.setAttributes(attributes);
            }
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(134217728);
            window.setNavigationBarContrastEnforced(false);
            WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, window.getDecorView());
            if (insetsController != null) {
                insetsController.setAppearanceLightNavigationBars(this.mAppearanceLightNavigationBars);
            }
        }
    }

    private void applyBlurParams() {
        COUIBackgroundBlurBuilder cOUIBackgroundBlurBuilder = this.mBackgroundBlurBuilder;
        if (cOUIBackgroundBlurBuilder == null) {
            return;
        }
        cOUIBackgroundBlurBuilder.setMixColorLight(this.mMixColorLight);
        this.mBackgroundBlurBuilder.setMixColorDark(this.mMixColorDark);
        this.mBackgroundBlurBuilder.setBlendColorLight(this.mBlendColorLight);
        this.mBackgroundBlurBuilder.setBlendColorDark(this.mBlendColorDark);
        this.mBackgroundBlurBuilder.setBlurRadius(this.mBlurRadius);
        this.mBackgroundBlurBuilder.setMode(COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext()) ? 2 : 3);
    }

    private void applyFollowHandBottomCenterFromMeasuredLayout() {
        Context context;
        int iMax;
        if (this.mCoordinatorLayout == null || this.mDesignBottomSheetFrameLayout == null || (context = getContext()) == null) {
            return;
        }
        int measuredWidth = this.mCoordinatorLayout.getMeasuredWidth();
        int measuredHeight = this.mCoordinatorLayout.getMeasuredHeight();
        int measuredWidth2 = this.mDesignBottomSheetFrameLayout.getMeasuredWidth();
        int measuredHeight2 = this.mDesignBottomSheetFrameLayout.getMeasuredHeight();
        int followHandEffectiveDialogHeight = getFollowHandEffectiveDialogHeight(measuredHeight2);
        if (measuredWidth <= 0 || measuredHeight <= 0 || measuredWidth2 <= 0 || followHandEffectiveDialogHeight <= 0) {
            return;
        }
        if (followHandEffectiveDialogHeight != measuredHeight2) {
            this.mDesignBottomSheetFrameLayout.setTop(0);
            this.mDesignBottomSheetFrameLayout.setBottom(followHandEffectiveDialogHeight);
        }
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_bottom);
        int iComputeFollowHandInsufficientSpaceBottomInsetPx = computeFollowHandInsufficientSpaceBottomInsetPx(context);
        if (this.mIsGestureNavigation) {
            iComputeFollowHandInsufficientSpaceBottomInsetPx = Math.max(iComputeFollowHandInsufficientSpaceBottomInsetPx, getFollowHandEffectiveNavigationBottomInsetPx(ViewCompat.getRootWindowInsets(this.mCoordinatorLayout)) + dimensionPixelOffset);
        }
        int followHandSafeTopInCoordinator = getFollowHandSafeTopInCoordinator(context);
        View view = this.mAnchorView;
        if (view == null || !view.isAttachedToWindow()) {
            iMax = Math.max(this.mFollowHandScreenEdgeMarginPx, (measuredWidth - measuredWidth2) / 2);
        } else {
            syncFollowHandAnchorRectInScreen(this.mAnchorView);
            Rect rect = this.mFollowHandAnchorRectInScreen;
            iMax = MathUtils.clamp((((rect.left + rect.right) / 2) - this.mFollowHandCoordinatorOnScreen[0]) - (measuredWidth2 / 2), this.mFollowHandScreenEdgeMarginPx, Math.max(0, (measuredWidth - measuredWidth2) - this.mFollowHandScreenEdgeMarginPx));
        }
        int iMax2 = this.mFollowHandRecordedPlacement == 2 ? followHandSafeTopInCoordinator : Math.max(followHandSafeTopInCoordinator, (measuredHeight - iComputeFollowHandInsufficientSpaceBottomInsetPx) - followHandEffectiveDialogHeight);
        if (DEBUG) {
            Log.d(TAG, "[follow_hand_position] insufficientSpaceFallback: measuredHeight=" + measuredHeight2 + ", effectiveHeight=" + followHandEffectiveDialogHeight + ", safeMaxHeight=" + this.mFollowHandSafeMaxHeightPx + ", safeTopMargin=" + dimensionPixelOffset + ", minTargetY=" + followHandSafeTopInCoordinator + ", bottomInset=" + iComputeFollowHandInsufficientSpaceBottomInsetPx + ", parentHeight=" + measuredHeight + ", final=(" + iMax + "," + iMax2 + ")");
        }
        applyFollowHandBottomCenterVisualPosition(iMax, iMax2);
    }

    private void applyFollowHandBottomCenterVisualPosition(int i3, int i4) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        if (this.mCoordinatorLayout == null || (cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout) == null) {
            return;
        }
        cOUIPanelPercentFrameLayout.setX(i3);
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout2 = this.mDesignBottomSheetFrameLayout;
        cOUIPanelPercentFrameLayout2.setTranslationY(i4 - cOUIPanelPercentFrameLayout2.getTop());
    }

    private void applyFollowHandHaveEnoughSpaceSuccess(int i3) {
        this.mDesignBottomSheetFrameLayout.setTop(0);
        this.mDesignBottomSheetFrameLayout.setBottom(i3);
        setElevation();
        View view = this.mOutsideView;
        if (view != null) {
            view.setAlpha(0.0f);
        }
        setCanPullUp(false);
        getBehavior().setDraggable(false);
    }

    private int applyFollowHandSideHorizontalPosition(int i3, Rect rect, Rect rect2, int i4, int i5, int i6, int i7) {
        int i8 = rect2.left + i6;
        int i9 = rect2.right - i6;
        int i10 = rect.left;
        int i11 = (i10 - i8) - i5;
        int i12 = rect.right;
        int i13 = (i9 - i12) - i5;
        boolean z2 = (i7 & 4) != 0;
        boolean z3 = (i7 & 8) != 0;
        if (!z2 || i4 > i11) {
            return (!z3 || i4 > i13) ? i3 : Math.min(i9 - i4, i12 + i5);
        }
        return Math.max(i8, (i10 - i4) - i5);
    }

    private void applyFollowHandTransparentNavigationBar() {
        if (getWindow() == null) {
            return;
        }
        setNavigationBarColor(0);
        getWindow().setNavigationBarContrastEnforced(false);
        if (this.mIsGestureNavigation) {
            return;
        }
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | 512);
    }

    private void applyShadowEdgeParams() {
        if (this.mShadowEdgeDrawable == null) {
            return;
        }
        boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
        this.mShadowEdgeDrawable.setWeight(getContext().getResources().getFloat(com.support.appcompat.R.dimen.weight_16_1_large));
        this.mShadowEdgeDrawable.setCornerType(2);
        this.mShadowEdgeDrawable.setIsDarkMode(zIsCOUIDarkThemeOrNightMode);
        this.mShadowEdgeDrawable.setLineAngle(0.0f);
        this.mShadowEdgeDrawable.setFadeWidthScale(1.0f);
        this.mShadowEdgeDrawable.setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_2);
        this.mShadowEdgeDrawable.setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_2);
        if (zIsCOUIDarkThemeOrNightMode) {
            this.mShadowEdgeDrawable.setPaintBlendMode(BlendMode.SRC_OVER);
            this.mShadowEdgeDrawable.setLineWidth(getContext().getResources().getDimensionPixelSize(com.support.panel.R.dimen.coui_panel_shadow_edge_width_dark));
            this.mShadowEdgeDrawable.setLineAlpha(0.15f);
            this.mShadowEdgeDrawable.setFadeAlpha(0.1f, 0.0f);
            return;
        }
        this.mShadowEdgeDrawable.setPaintBlendMode(BlendMode.SRC_OVER);
        this.mShadowEdgeDrawable.setLineWidth(getContext().getResources().getDimensionPixelSize(com.support.panel.R.dimen.coui_panel_shadow_edge_width_light));
        this.mShadowEdgeDrawable.setLineAlpha(0.8f);
        this.mShadowEdgeDrawable.setFadeAlpha(0.4f, 0.0f);
    }

    private static float[] argbToRgbaFloats(int i3) {
        return new float[]{Color.red(i3) / 255.0f, Color.green(i3) / 255.0f, Color.blue(i3) / 255.0f, Color.alpha(i3) / 255.0f};
    }

    public static /* synthetic */ void b(COUIBottomSheetDialog cOUIBottomSheetDialog, AnimationVelocityCalculator animationVelocityCalculator, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        float fCalculator = animationVelocityCalculator.calculator(cOUIBottomSheetDialog.mDesignBottomSheetFrameLayout.getHeight(), valueAnimator);
        COUILog.d(TAG, "DynamicFrameRateManager.getSuggestFrameRate: v " + fCalculator + " frame " + DynamicFrameRateManager.getSuggestFrameRate(fCalculator, 2));
        DynamicFrameRateManager.setFrameRate(cOUIBottomSheetDialog.mDesignBottomSheetFrameLayout, ANIMATION_TYPE_ID, (int) fCalculator, (Bundle) null);
    }

    public static /* synthetic */ void c(COUIBottomSheetDialog cOUIBottomSheetDialog, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        cOUIBottomSheetDialog.getClass();
        COUILog.d(TAG, "COUISpringAnimation DynamicFrameRateManager.getSuggestFrameRate: v " + Math.abs(f4) + " frame " + DynamicFrameRateManager.getSuggestFrameRate(f4, 2));
        DynamicFrameRateManager.setFrameRate(cOUIBottomSheetDialog.mDesignBottomSheetFrameLayout, ANIMATION_TYPE_ID, Math.abs((int) f4), (Bundle) null);
    }

    /* JADX WARN: Code duplicated, block: B:43:0x0142 A[PHI: r1 r2 r4 r6 r7 r15
  0x0142: PHI (r1v58 int) = (r1v54 int), (r1v69 int) binds: [B:54:0x0196, B:42:0x0140] A[DONT_GENERATE, DONT_INLINE]
  0x0142: PHI (r2v31 int) = (r2v29 int), (r2v34 int) binds: [B:54:0x0196, B:42:0x0140] A[DONT_GENERATE, DONT_INLINE]
  0x0142: PHI (r4v14 int) = (r4v12 int), (r4v15 int) binds: [B:54:0x0196, B:42:0x0140] A[DONT_GENERATE, DONT_INLINE]
  0x0142: PHI (r6v9 int) = (r6v7 int), (r6v10 int) binds: [B:54:0x0196, B:42:0x0140] A[DONT_GENERATE, DONT_INLINE]
  0x0142: PHI (r7v10 int) = (r7v8 int), (r7v11 int) binds: [B:54:0x0196, B:42:0x0140] A[DONT_GENERATE, DONT_INLINE]
  0x0142: PHI (r15v11 int) = (r15v9 int), (r15v12 int) binds: [B:54:0x0196, B:42:0x0140] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:76:0x0249  */
    private void calculateFinalLocationForRecordedPlacement(View view, int[] iArr) {
        View childAt;
        int i3;
        int i4;
        int i5;
        int i6;
        int iMax;
        int i7;
        int i8;
        int i9;
        int iMax2;
        COUIBottomSheetDialog cOUIBottomSheetDialog = this;
        cOUIBottomSheetDialog.syncFollowHandCoordinatorScreenTopOffset();
        syncFollowHandAnchorRectInScreen(view);
        View rootView = view.getRootView();
        if (!(rootView instanceof ViewGroup) || (childAt = ((ViewGroup) rootView).getChildAt(0)) == null) {
            childAt = rootView;
        }
        Rect locationRectInScreen = childAt != null ? cOUIBottomSheetDialog.getLocationRectInScreen(childAt) : new Rect(0, 0, cOUIBottomSheetDialog.getContext().getResources().getDisplayMetrics().widthPixels, cOUIBottomSheetDialog.getContext().getResources().getDisplayMetrics().heightPixels);
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(rootView);
        if (rootWindowInsets != null) {
            cOUIBottomSheetDialog.mWindowInsetsTop = rootWindowInsets.getSystemWindowInsetTop();
            cOUIBottomSheetDialog.mWindowInsetsLeft = rootWindowInsets.getSystemWindowInsetLeft();
        }
        int systemWindowInsetBottom = rootWindowInsets == null ? -1 : rootWindowInsets.getSystemWindowInsetBottom();
        int measuredWidth = cOUIBottomSheetDialog.mDesignBottomSheetFrameLayout.getMeasuredWidth();
        int followHandEffectiveDialogHeight = cOUIBottomSheetDialog.getFollowHandEffectiveDialogHeight(cOUIBottomSheetDialog.mDesignBottomSheetFrameLayout.getMeasuredHeight());
        int dimensionPixelOffset = cOUIBottomSheetDialog.getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_bottom);
        int dimensionPixelOffset2 = cOUIBottomSheetDialog.getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_left);
        boolean zIsFollowHandNavigationBarVisible = cOUIBottomSheetDialog.isFollowHandNavigationBarVisible(rootWindowInsets);
        int followHandNavigationBottomInsetPx = cOUIBottomSheetDialog.getFollowHandNavigationBottomInsetPx(rootWindowInsets);
        int followHandEffectiveNavigationBottomInsetPx = cOUIBottomSheetDialog.getFollowHandEffectiveNavigationBottomInsetPx(rootWindowInsets);
        int iMax3 = Math.max(cOUIBottomSheetDialog.mStatusBarHeight, cOUIBottomSheetDialog.mWindowInsetsTop);
        int i10 = iMax3 + dimensionPixelOffset;
        int iMax4 = Math.max(0, i10 - cOUIBottomSheetDialog.mFollowHandCoordinatorOnScreen[1]);
        int i11 = locationRectInScreen.bottom - followHandEffectiveDialogHeight;
        int i12 = cOUIBottomSheetDialog.mFollowHandCoordinatorOnScreen[1];
        int i13 = ((i11 - i12) - followHandEffectiveNavigationBottomInsetPx) - dimensionPixelOffset;
        int iMax5 = Math.max(i12, i10);
        int i14 = ((locationRectInScreen.bottom - followHandEffectiveDialogHeight) - followHandEffectiveNavigationBottomInsetPx) - dimensionPixelOffset;
        int i15 = cOUIBottomSheetDialog.mFollowHandRecordedPlacement;
        int i16 = followHandEffectiveNavigationBottomInsetPx;
        if (i15 != 1) {
            if (i15 == 2) {
                i3 = iMax4;
                i4 = i13;
                Rect rect = cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen;
                int iNormalizePoints = cOUIBottomSheetDialog.normalizePoints(((rect.left + rect.right) / 2) - (measuredWidth / 2), locationRectInScreen.right - measuredWidth);
                i6 = cOUIBottomSheetDialog.mFollowHandScreenEdgeMarginPx;
                if (iNormalizePoints > i6) {
                    int i17 = iNormalizePoints + measuredWidth + i6;
                    int i18 = locationRectInScreen.right;
                    if (i17 >= i18) {
                        i5 = followHandEffectiveDialogHeight;
                        i6 = (i18 - i6) - measuredWidth;
                    } else {
                        i5 = followHandEffectiveDialogHeight;
                        i6 = iNormalizePoints;
                    }
                }
                i5 = followHandEffectiveDialogHeight;
                iMax = Math.max(i3, Math.min(((cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen.top - dimensionPixelOffset) - i5) - i12, i4));
            } else if (i15 == 3) {
                iMax5 = iMax5;
                i3 = iMax4;
                i4 = i13;
                i16 = i16;
                int iMax6 = Math.max(locationRectInScreen.left + cOUIBottomSheetDialog.mFollowHandScreenEdgeMarginPx, (cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen.left - measuredWidth) - dimensionPixelOffset2);
                int i19 = cOUIBottomSheetDialog.mFollowHandScreenEdgeMarginPx;
                if (iMax6 <= i19) {
                    i5 = followHandEffectiveDialogHeight;
                    i9 = i19;
                } else {
                    int i20 = iMax6 + measuredWidth + i19;
                    int i21 = locationRectInScreen.right;
                    if (i20 >= i21) {
                        i5 = followHandEffectiveDialogHeight;
                        i9 = (i21 - i19) - measuredWidth;
                    } else {
                        i5 = followHandEffectiveDialogHeight;
                        i9 = iMax6;
                    }
                }
                if (cOUIBottomSheetDialog.mFollowHandRecordedSideVerticalMode == 1) {
                    i7 = i9;
                    iMax = i3;
                } else {
                    Rect rect2 = cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen;
                    iMax2 = Math.max(i3, Math.min(MathUtils.clamp(((rect2.bottom + rect2.top) / 2) - (i5 / 2), iMax5, i14) - i12, i4));
                    i7 = i9;
                    iMax = iMax2;
                }
            } else if (i15 != 4) {
                Rect rect3 = cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen;
                int iNormalizePoints2 = cOUIBottomSheetDialog.normalizePoints(((rect3.left + rect3.right) / 2) - (measuredWidth / 2), locationRectInScreen.right - measuredWidth);
                int i22 = cOUIBottomSheetDialog.mFollowHandScreenEdgeMarginPx;
                if (iNormalizePoints2 <= i22) {
                    i7 = i22;
                } else {
                    int i23 = iNormalizePoints2 + measuredWidth + i22;
                    int i24 = locationRectInScreen.right;
                    if (i23 >= i24) {
                        iNormalizePoints2 = (i24 - i22) - measuredWidth;
                    }
                    i7 = iNormalizePoints2;
                }
                iMax5 = iMax5;
                i3 = iMax4;
                i4 = i13;
                cOUIBottomSheetDialog = this;
                i5 = followHandEffectiveDialogHeight;
                iMax = cOUIBottomSheetDialog.resolveFollowHandVerticalY(locationRectInScreen.bottom, i5, i16, iMax3, dimensionPixelOffset, i3, i4);
                i16 = i16;
            } else {
                iMax5 = iMax5;
                i3 = iMax4;
                i4 = i13;
                i16 = i16;
                int iMin = Math.min((locationRectInScreen.right - cOUIBottomSheetDialog.mFollowHandScreenEdgeMarginPx) - measuredWidth, cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen.right + dimensionPixelOffset2);
                int i25 = cOUIBottomSheetDialog.mFollowHandScreenEdgeMarginPx;
                if (iMin <= i25) {
                    i5 = followHandEffectiveDialogHeight;
                    i9 = i25;
                } else {
                    int i26 = iMin + measuredWidth + i25;
                    int i27 = locationRectInScreen.right;
                    if (i26 >= i27) {
                        i5 = followHandEffectiveDialogHeight;
                        i9 = (i27 - i25) - measuredWidth;
                    } else {
                        i5 = followHandEffectiveDialogHeight;
                        i9 = iMin;
                    }
                }
                if (cOUIBottomSheetDialog.mFollowHandRecordedSideVerticalMode == 1) {
                    i7 = i9;
                    iMax = i3;
                } else {
                    Rect rect4 = cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen;
                    iMax2 = Math.max(i3, Math.min(MathUtils.clamp(((rect4.bottom + rect4.top) / 2) - (i5 / 2), iMax5, i14) - i12, i4));
                    i7 = i9;
                    iMax = iMax2;
                }
            }
            i8 = i7 - cOUIBottomSheetDialog.mFollowHandCoordinatorOnScreen[0];
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("[follow_hand_position] calculateRecorded: placement=");
                sb.append(i15);
                sb.append(", sideMode=");
                sb.append(cOUIBottomSheetDialog.mFollowHandRecordedSideVerticalMode);
                sb.append(", allowedFlags=");
                sb.append(cOUIBottomSheetDialog.mFollowHandPlacementAllowedFlags);
                sb.append(", anchorRect=");
                sb.append(cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen);
                sb.append(", contentRect=");
                sb.append(locationRectInScreen);
                sb.append(", coordinatorOrigin=(");
                sb.append(cOUIBottomSheetDialog.mFollowHandCoordinatorOnScreen[0]);
                sb.append(",");
                sb.append(cOUIBottomSheetDialog.mFollowHandCoordinatorOnScreen[1]);
                sb.append("), dialog=");
                sb.append(measuredWidth);
                sb.append("x");
                sb.append(i5);
                sb.append(", currentNavigationHeight=");
                sb.append(followHandNavigationBottomInsetPx);
                sb.append(", recordedNavigationHeight=");
                sb.append(cOUIBottomSheetDialog.mFollowHandRecordedNavigationBottomInsetPx);
                sb.append(", effectiveNavigationHeight=");
                sb.append(i16);
                sb.append(", insetTop=");
                sb.append(cOUIBottomSheetDialog.mWindowInsetsTop);
                sb.append(", insetBottom=");
                sb.append(systemWindowInsetBottom);
                sb.append(", gestureNavigation=");
                sb.append(cOUIBottomSheetDialog.mIsGestureNavigation);
                sb.append(", navigationBarVisible=");
                sb.append(zIsFollowHandNavigationBarVisible);
                sb.append(", minY=");
                sb.append(i3);
                sb.append(", maxY=");
                sb.append(i4);
                sb.append(", minDialogTopScreen=");
                sb.append(iMax5);
                sb.append(", maxDialogTopScreen=");
                sb.append(i14);
                sb.append(", final=(");
                sb.append(i8);
                sb.append(",");
                iMax = iMax;
                sb.append(iMax);
                sb.append(")");
                Log.d(TAG, sb.toString());
            }
            iArr[0] = i8;
            iArr[1] = iMax;
        }
        i3 = iMax4;
        i4 = i13;
        Rect rect5 = cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen;
        int iNormalizePoints3 = cOUIBottomSheetDialog.normalizePoints(((rect5.left + rect5.right) / 2) - (measuredWidth / 2), locationRectInScreen.right - measuredWidth);
        i6 = cOUIBottomSheetDialog.mFollowHandScreenEdgeMarginPx;
        if (iNormalizePoints3 > i6) {
            int i28 = iNormalizePoints3 + measuredWidth + i6;
            int i29 = locationRectInScreen.right;
            if (i28 >= i29) {
                i5 = followHandEffectiveDialogHeight;
                i6 = (i29 - i6) - measuredWidth;
            } else {
                i5 = followHandEffectiveDialogHeight;
                i6 = iNormalizePoints3;
            }
        }
        i5 = followHandEffectiveDialogHeight;
        iMax = Math.max(i3, Math.min((cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen.bottom + dimensionPixelOffset) - i12, i4));
        i7 = i6;
        i8 = i7 - cOUIBottomSheetDialog.mFollowHandCoordinatorOnScreen[0];
        if (DEBUG) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[follow_hand_position] calculateRecorded: placement=");
            sb2.append(i15);
            sb2.append(", sideMode=");
            sb2.append(cOUIBottomSheetDialog.mFollowHandRecordedSideVerticalMode);
            sb2.append(", allowedFlags=");
            sb2.append(cOUIBottomSheetDialog.mFollowHandPlacementAllowedFlags);
            sb2.append(", anchorRect=");
            sb2.append(cOUIBottomSheetDialog.mFollowHandAnchorRectInScreen);
            sb2.append(", contentRect=");
            sb2.append(locationRectInScreen);
            sb2.append(", coordinatorOrigin=(");
            sb2.append(cOUIBottomSheetDialog.mFollowHandCoordinatorOnScreen[0]);
            sb2.append(",");
            sb2.append(cOUIBottomSheetDialog.mFollowHandCoordinatorOnScreen[1]);
            sb2.append("), dialog=");
            sb2.append(measuredWidth);
            sb2.append("x");
            sb2.append(i5);
            sb2.append(", currentNavigationHeight=");
            sb2.append(followHandNavigationBottomInsetPx);
            sb2.append(", recordedNavigationHeight=");
            sb2.append(cOUIBottomSheetDialog.mFollowHandRecordedNavigationBottomInsetPx);
            sb2.append(", effectiveNavigationHeight=");
            sb2.append(i16);
            sb2.append(", insetTop=");
            sb2.append(cOUIBottomSheetDialog.mWindowInsetsTop);
            sb2.append(", insetBottom=");
            sb2.append(systemWindowInsetBottom);
            sb2.append(", gestureNavigation=");
            sb2.append(cOUIBottomSheetDialog.mIsGestureNavigation);
            sb2.append(", navigationBarVisible=");
            sb2.append(zIsFollowHandNavigationBarVisible);
            sb2.append(", minY=");
            sb2.append(i3);
            sb2.append(", maxY=");
            sb2.append(i4);
            sb2.append(", minDialogTopScreen=");
            sb2.append(iMax5);
            sb2.append(", maxDialogTopScreen=");
            sb2.append(i14);
            sb2.append(", final=(");
            sb2.append(i8);
            sb2.append(",");
            iMax = iMax;
            sb2.append(iMax);
            sb2.append(")");
            Log.d(TAG, sb2.toString());
        }
        iArr[0] = i8;
        iArr[1] = iMax;
    }

    private void calculateFinalLocationOnScreen(View view, int[] iArr) {
        View childAt;
        int iApplyFollowHandSideHorizontalPosition;
        int i3;
        int i4;
        Rect rect;
        Rect rect2;
        int i5;
        syncFollowHandCoordinatorScreenTopOffset();
        syncFollowHandAnchorRectInScreen(view);
        Rect rect3 = this.mFollowHandAnchorRectInScreen;
        View rootView = view.getRootView();
        if (!(rootView instanceof ViewGroup) || (childAt = ((ViewGroup) rootView).getChildAt(0)) == null) {
            childAt = rootView;
        }
        Rect locationRectInScreen = childAt != null ? getLocationRectInScreen(childAt) : new Rect(0, 0, getContext().getResources().getDisplayMetrics().widthPixels, getContext().getResources().getDisplayMetrics().heightPixels);
        Rect locationRectInScreen2 = getLocationRectInScreen(this.mDesignBottomSheetFrameLayout);
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(rootView);
        if (rootWindowInsets != null) {
            this.mWindowInsetsTop = rootWindowInsets.getSystemWindowInsetTop();
            this.mWindowInsetsLeft = rootWindowInsets.getSystemWindowInsetLeft();
        }
        int systemWindowInsetBottom = rootWindowInsets == null ? -1 : rootWindowInsets.getSystemWindowInsetBottom();
        int measuredWidth = this.mDesignBottomSheetFrameLayout.getMeasuredWidth();
        int followHandEffectiveDialogHeight = getFollowHandEffectiveDialogHeight(this.mDesignBottomSheetFrameLayout.getMeasuredHeight());
        int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_bottom);
        int dimensionPixelOffset2 = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_left);
        int i6 = locationRectInScreen.right - measuredWidth;
        Rect rect4 = this.mFollowHandAnchorRectInScreen;
        int iNormalizePoints = normalizePoints(((rect4.left + rect4.right) / 2) - (measuredWidth / 2), i6);
        int i7 = this.mFollowHandScreenEdgeMarginPx;
        if (iNormalizePoints <= i7) {
            iApplyFollowHandSideHorizontalPosition = i7;
        } else {
            int i8 = iNormalizePoints + measuredWidth + i7;
            int i9 = locationRectInScreen.right;
            if (i8 >= i9) {
                iNormalizePoints = (i9 - i7) - measuredWidth;
            }
            iApplyFollowHandSideHorizontalPosition = iNormalizePoints;
        }
        boolean zIsFollowHandNavigationBarVisible = isFollowHandNavigationBarVisible(rootWindowInsets);
        int followHandNavigationBottomInsetPx = getFollowHandNavigationBottomInsetPx(rootWindowInsets);
        int followHandEffectiveNavigationBottomInsetPx = getFollowHandEffectiveNavigationBottomInsetPx(rootWindowInsets);
        int iMax = Math.max(this.mStatusBarHeight, this.mWindowInsetsTop);
        int iMax2 = Math.max(0, (iMax + dimensionPixelOffset) - this.mFollowHandCoordinatorOnScreen[1]);
        int i10 = locationRectInScreen.bottom;
        int i11 = (((i10 - followHandEffectiveDialogHeight) - this.mFollowHandCoordinatorOnScreen[1]) - followHandEffectiveNavigationBottomInsetPx) - dimensionPixelOffset;
        int iResolveFollowHandVerticalY = resolveFollowHandVerticalY(i10, followHandEffectiveDialogHeight, followHandEffectiveNavigationBottomInsetPx, iMax, dimensionPixelOffset, iMax2, i11);
        if ((this.mFollowHandPlacementAllowedFlags & 12) == 0 || !(BRANCH_FOLLOW_HAND_SIDE_ANCHOR.equals(this.mFollowHandCurrentBranch) || BRANCH_FOLLOW_HAND_SIDE_CLAMP_OVERFLOW.equals(this.mFollowHandCurrentBranch))) {
            i3 = measuredWidth;
            i4 = dimensionPixelOffset2;
            rect = rect3;
            rect2 = locationRectInScreen;
            i5 = iMax2;
        } else {
            i5 = iMax2;
            rect = rect3;
            rect2 = locationRectInScreen;
            iApplyFollowHandSideHorizontalPosition = applyFollowHandSideHorizontalPosition(iApplyFollowHandSideHorizontalPosition, rect, rect2, measuredWidth, dimensionPixelOffset2, this.mFollowHandScreenEdgeMarginPx, this.mFollowHandPlacementAllowedFlags);
            i3 = measuredWidth;
            i4 = dimensionPixelOffset2;
        }
        int iClassifyFollowHandPlacement = classifyFollowHandPlacement(i3, rect, rect2, i4, this.mFollowHandScreenEdgeMarginPx, this.mFollowHandPlacementAllowedFlags);
        boolean zEquals = BRANCH_FOLLOW_HAND_SIDE_CLAMP_OVERFLOW.equals(this.mFollowHandCurrentBranch);
        int i12 = iApplyFollowHandSideHorizontalPosition - this.mFollowHandCoordinatorOnScreen[0];
        if (DEBUG) {
            Log.d(TAG, "[follow_hand_position] calculateInitial: branch=" + this.mFollowHandCurrentBranch + "\n anchorViewLocationRect = " + rect + ", \n anchorContentViewLocationRect = " + rect2 + ", \n dialogViewLocalRect = " + locationRectInScreen2 + "\n -> final : x = " + i12 + ", y = " + iResolveFollowHandVerticalY + ", placement=" + iClassifyFollowHandPlacement + ", sideVertMode=" + (zEquals ? 1 : 0) + "\n -> coordinatorOrigin=(" + this.mFollowHandCoordinatorOnScreen[0] + "," + this.mFollowHandCoordinatorOnScreen[1] + "), insetTop=" + this.mWindowInsetsTop + ", insetBottom=" + systemWindowInsetBottom + ", currentNavigationHeight=" + followHandNavigationBottomInsetPx + ", recordedNavigationHeight=" + this.mFollowHandRecordedNavigationBottomInsetPx + ", effectiveNavigationHeight=" + followHandEffectiveNavigationBottomInsetPx + ", gestureNavigation=" + this.mIsGestureNavigation + ", navigationBarVisible=" + zIsFollowHandNavigationBarVisible + ", minY=" + i5 + ", maxY=" + i11 + ", allowedFlags=" + this.mFollowHandPlacementAllowedFlags);
        }
        iArr[0] = i12;
        iArr[1] = iResolveFollowHandVerticalY;
        iArr[2] = iClassifyFollowHandPlacement;
        iArr[3] = zEquals ? 1 : 0;
        iArr[4] = 
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x021e: APUT (r22v0 'iArr' int[]), (4 ??[int, float, short, byte, char]), (r3v14 int) in method: com.coui.appcompat.panel.COUIBottomSheetDialog.calculateFinalLocationOnScreen(android.view.View, int[]):void, file: classes7.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:310)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:273)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:94)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:291)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:270)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:420)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:345)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:299)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
            	at java.base/java.util.ArrayList.forEach(Unknown Source)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r3v14 int
            	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:236)
            */
        /*
            Method dump skipped, instruction units count: 545
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.coui.appcompat.panel.COUIBottomSheetDialog.calculateFinalLocationOnScreen(android.view.View, int[]):void");
    }

    private void cancelAnim(Animator animator) {
        if (animator == null || !animator.isRunning()) {
            return;
        }
        animator.end();
    }

    private void centerFollowHandPanelOnScreen() {
        View view = this.mCoordinatorLayout;
        if (view == null || this.mDesignBottomSheetFrameLayout == null) {
            return;
        }
        int measuredHeight = view.getMeasuredHeight();
        int measuredHeight2 = this.mDesignBottomSheetFrameLayout.getMeasuredHeight();
        int followHandEffectiveDialogHeight = getFollowHandEffectiveDialogHeight(measuredHeight2);
        if (measuredHeight <= 0 || followHandEffectiveDialogHeight <= 0) {
            return;
        }
        if (followHandEffectiveDialogHeight != measuredHeight2) {
            this.mDesignBottomSheetFrameLayout.setTop(0);
            this.mDesignBottomSheetFrameLayout.setBottom(followHandEffectiveDialogHeight);
        }
        int followHandSafeTopInCoordinator = getFollowHandSafeTopInCoordinator(getContext());
        this.mDesignBottomSheetFrameLayout.setY(MathUtils.clamp((measuredHeight - followHandEffectiveDialogHeight) / 2, followHandSafeTopInCoordinator, Math.max(followHandSafeTopInCoordinator, (measuredHeight - getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_bottom)) - followHandEffectiveDialogHeight)));
        this.mDesignBottomSheetFrameLayout.setTranslationX(0.0f);
    }

    private void checkInitState() {
        if (this.mContainerFrameLayout == null) {
            throw new IllegalArgumentException("container can not be null");
        }
        if (this.mCoordinatorLayout == null) {
            throw new IllegalArgumentException("coordinator can not be null");
        }
        if (this.mOutsideView == null) {
            throw new IllegalArgumentException("panel_outside can not be null");
        }
        if (this.mDesignBottomSheetFrameLayout == null) {
            throw new IllegalArgumentException("design_bottom_sheet can not be null");
        }
    }

    private int classifyFollowHandPlacement(int i3, Rect rect, Rect rect2, int i4, int i5, int i6) {
        if (BRANCH_FOLLOW_HAND_BELOW_ANCHOR.equals(this.mFollowHandCurrentBranch)) {
            return 1;
        }
        if (BRANCH_FOLLOW_HAND_ABOVE_ANCHOR.equals(this.mFollowHandCurrentBranch)) {
            return 2;
        }
        boolean z2 = (i6 & 4) != 0;
        boolean z3 = (i6 & 8) != 0;
        if (!z2 && !z3) {
            return 1;
        }
        int i7 = rect2.left + i5;
        int i8 = rect2.right - i5;
        int i9 = (rect.left - i7) - i4;
        int i10 = (i8 - rect.right) - i4;
        if (z2 && i3 <= i9) {
            return 3;
        }
        if (!z3 || i3 > i10) {
            return z2 ? 3 : 1;
        }
        return 4;
    }

    private void clearAppearanceLightNavigationBars() {
        Window window;
        if (this.mManualAppearanceLightNavigationBars && (window = getWindow()) != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            OplusBaseLayoutParams oplusBaseLayoutParams = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, attributes);
            if (oplusBaseLayoutParams != null) {
                oplusBaseLayoutParams.oplusFlags &= -32769;
                window.setAttributes(attributes);
            }
        }
    }

    private void clearFollowHandRecordedPlacement() {
        this.mFollowHandRecordedPlacement = 0;
        this.mFollowHandRecordedSideVerticalMode = 0;
        this.mFollowHandRecordedNavigationBottomInsetPx = -1;
    }

    private void clearPendingAdjustResize() {
        ViewGroup viewGroup = this.mPendingAdjustResizeLayout;
        if (viewGroup != null) {
            ViewTreeObserver viewTreeObserver = viewGroup.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this.mAdjustResizeOnPreDrawListener);
            }
        }
        this.mPendingAdjustResizeLayout = null;
        this.mPendingAdjustResizeWindowInsets = null;
    }

    private void completeFollowHandShowingAnim(Animator.AnimatorListener animatorListener, boolean z2) {
        if (z2) {
            invalidateFollowHandPosition();
            applyFollowHandTransparentNavigationBar();
            addAnimationFlag(8);
            addAnimationFlag(4);
        } else {
            if (this.mFollowHandFallbackVerticalEdgeWhenInsufficientSpace) {
                positionFollowHandPanelBottomCenterWhenInsufficientSpace();
            } else {
                centerFollowHandPanelOnScreen();
            }
            addAnimationFlag(8);
            addAnimationFlag(4);
            addAnimationFlag(2);
        }
        this.mDesignBottomSheetFrameLayout.setVisibility(0);
        this.mIsAnimationInFirst = true;
        doTranslationAndScaleSpringAnimaion(animatorListener);
        doAlphaSpringAnimaion(null);
    }

    private int computeFollowHandInsufficientSpaceBottomInsetPx(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(com.support.panel.R.dimen.coui_bottom_sheet_follow_hand_fallback_screen_bottom);
        int dimensionPixelOffset2 = resources.getDimensionPixelOffset(com.support.panel.R.dimen.coui_bottom_sheet_follow_hand_fallback_three_key_nav_gap);
        int followHandFallbackNavigationBottomInsetPx = getFollowHandFallbackNavigationBottomInsetPx();
        return followHandFallbackNavigationBottomInsetPx <= dimensionPixelOffset ? dimensionPixelOffset - followHandFallbackNavigationBottomInsetPx : dimensionPixelOffset2;
    }

    public static COUISpringAnimation createContentChangeSpringAnimation() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.4f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        spring.setMinimumVisibleChange(SPRING_ANIM_CONTENT_CHANGE_MINIMUM_VISIBLE_CHANGE);
        return spring;
    }

    private ValueAnimator createNavigationColorAnimation(int i3) {
        if (COUINavigationBarUtil.isNavigationBarShow(getContext()) && getWindow() != null) {
            Window window = getWindow();
            int navigationBarColor = window.getNavigationBarColor();
            if (Color.alpha(i3) == 0) {
                i3 = Color.argb(1, Color.red(i3), Color.green(i3), Color.blue(i3));
            }
            if (navigationBarColor != i3) {
                ValueAnimator valueAnimatorOfObject = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(navigationBarColor), Integer.valueOf(i3));
                valueAnimatorOfObject.setDuration(200L);
                valueAnimatorOfObject.addUpdateListener(new AnonymousClass29(window));
                return valueAnimatorOfObject;
            }
        }
        return null;
    }

    private ValueAnimator createOutsideAlphaAnimation(boolean z2, float f3, PathInterpolator pathInterpolator) {
        float f4 = this.mCurrentOutsideAlpha;
        float f5 = z2 ? 1.0f : 0.0f;
        if (f4 == f5) {
            COUILog.w(TAG, "StartAlphaValue == endAlphaValue, No need to perform transparency animation anymore");
            return null;
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(f4, f5);
        valueAnimatorOfFloat.setDuration((long) f3);
        valueAnimatorOfFloat.setInterpolator(pathInterpolator);
        valueAnimatorOfFloat.addUpdateListener(new AnonymousClass27(f4, f5, z2));
        valueAnimatorOfFloat.addListener(new AnonymousClass28());
        return valueAnimatorOfFloat;
    }

    private void createPanelConstraintLayout() {
        COUIPanelContentLayout cOUIPanelContentLayout = (COUIPanelContentLayout) getLayoutInflater().inflate(this.mIsInTinyScreen ? com.support.panel.R.layout.coui_panel_view_layout_tiny : com.support.panel.R.layout.coui_panel_view_layout, (ViewGroup) null);
        Drawable drawable = this.mPanelDragViewDrawable;
        if (drawable != null) {
            drawable.setTint(this.mPanelDragViewDrawableTintColor);
            cOUIPanelContentLayout.setDragViewDrawable(this.mPanelDragViewDrawable);
        }
        if (this.mHandleViewHasPressAnim) {
            cOUIPanelContentLayout.setDragViewPressAnim(true);
        }
        WindowInsets windowInsets = this.mApplyWindowInsets;
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        cOUIPanelContentLayout.setNavigationMargin(null, windowInsets, cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getRatio() == 1.0f, this.mCouiPanelEdgeToEdgeEnable);
        this.mDraggableConstraintLayout = cOUIPanelContentLayout;
        if (this.mIsHandlePanel) {
            return;
        }
        hideDragView();
    }

    private ValueAnimator createPanelTranslateAnimation(float f3, float f4, float f5, PathInterpolator pathInterpolator) {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(f3, f4);
        valueAnimatorOfFloat.setDuration((long) f5);
        valueAnimatorOfFloat.setInterpolator(pathInterpolator);
        valueAnimatorOfFloat.addUpdateListener(new AnonymousClass22());
        setFrameRate(valueAnimatorOfFloat);
        return valueAnimatorOfFloat;
    }

    private void dismissWithAlphaAnim() {
        this.mIsEntering = false;
        this.mIsRevertAnimationFromSettlingAnimation = false;
        AnonymousClass14 anonymousClass14 = new AnonymousClass14();
        stopCurrentRunningViewTranslationAnim();
        this.mCurrentOutSideAlphaStateHidden = this.mCurrentOutsideAlpha;
        resetAnimationFlag();
        addAnimationFlag(2);
        doAlphaSpringAnimaion(anonymousClass14);
    }

    private void dismissWithInterruptibleAnim() {
        doParentViewTranslationHidingAnim(new AnonymousClass13());
    }

    private void doAlphaSpringAnimaion(Animator.AnimatorListener animatorListener) {
        View view;
        if (this.mAlphaSpringAnimation == null) {
            this.mAlphaSpringAnimation = new COUISpringAnimation(new FloatValueHolder());
            COUISpringForce cOUISpringForce = new COUISpringForce();
            this.mSpringForceAlpha = cOUISpringForce;
            cOUISpringForce.setBounce(0.0f);
            this.mAlphaSpringAnimation.setSpring(this.mSpringForceAlpha);
        }
        if (this.mAlphaSpringAnimation.isRunning()) {
            this.mAlphaSpringAnimation.cancel();
        }
        if (hasAnimationFlag(2)) {
            if (!isFadeInCenter()) {
                this.mSpringForceAlpha.setResponse(getTranslationResponse());
            } else if (this.mIsEntering) {
                this.mSpringForceAlpha.setResponse(0.25f);
            } else {
                this.mSpringForceAlpha.setResponse(0.18f);
            }
        }
        if (animatorListener != null) {
            this.mAlphaSpringAnimation.removeEndListener(this.mAlphaSpringEndListener);
            AnonymousClass17 anonymousClass17 = new AnonymousClass17(animatorListener);
            this.mAlphaSpringEndListener = anonymousClass17;
            this.mAlphaSpringAnimation.addEndListener(anonymousClass17);
            animatorListener.onAnimationStart(null);
        }
        this.mAlphaSpringAnimation.removeUpdateListener(this.mAlphaSpringUpdateListener);
        this.mAlphaSpringUpdateListener = new AnonymousClass18();
        if (!this.mIsEntering && isFollowHand()) {
            ShadowUtils.clearShadow(this.mDesignBottomSheetFrameLayout);
        }
        this.mAlphaSpringAnimation.removeEndListener(this.mAlphaSpringEndListener);
        AnonymousClass19 anonymousClass19 = new AnonymousClass19();
        this.mAlphaSpringEndListener = anonymousClass19;
        this.mAlphaSpringAnimation.addEndListener(anonymousClass19);
        if (!this.mIsRevertAnimationFromSettlingAnimation || (view = this.mOutsideView) == null) {
            this.mCurrentOutSideAlphaStateShow = 0.0f;
        } else {
            this.mCurrentOutSideAlphaStateShow = view.getAlpha();
        }
        this.mAlphaSpringAnimation.addUpdateListener(this.mAlphaSpringUpdateListener);
        this.mAlphaSpringAnimation.setStartValue(this.mStartValueOfTranslateAnimation);
        this.mAlphaSpringAnimation.animateToFinalPosition(this.mEndValueOfTranslateAnimation);
    }

    private void doBlur() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null) {
            Log.e(TAG, "doBlur mDesignBottomSheetFrameLayout == null");
            return;
        }
        if (!cOUIPanelPercentFrameLayout.isAttachedToWindow()) {
            this.mDesignBottomSheetFrameLayout.addOnAttachStateChangeListener(new AnonymousClass8());
            return;
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(this.mDesignBottomSheetFrameLayout);
        }
        doBlurInternal();
    }

    private void doFeedbackAnimation(View view) {
        if (view == null) {
            return;
        }
        if (this.mDisableFastCloseFeedbackSpring == null || this.mFeedBackView != view) {
            this.mFeedBackView = view;
            Spring springCreateSpring = SpringSystem.create().createSpring();
            this.mDisableFastCloseFeedbackSpring = springCreateSpring;
            springCreateSpring.setSpringConfig(SpringConfig.fromBouncinessAndSpeed(THREE_POINT_EIGHT, TWENTY));
            this.mDisableFastCloseFeedbackSpring.addListener(new AnonymousClass30());
        }
        this.mDisableFastCloseFeedbackSpring.setEndValue(100.0d);
    }

    private void doParentViewTranslationHidingAnim(Animator.AnimatorListener animatorListener) {
        if (reversalAnimation(animatorListener, false)) {
            this.mIsExecutingDismissAnim = true;
            enableBackEventPassThroughForDismiss();
            return;
        }
        this.mIsEntering = false;
        stopCurrentRunningViewTranslationAnim();
        resetAnimationFlag();
        if (getDialogMaxHeight() == 0) {
            Log.d(TAG, "doParentViewTranslationHidingAnim return directly for dialogMaxHeight is 0, but call superDismiss");
            superDismiss();
            return;
        }
        this.mPanelViewTranslationAnimationSet = new AnimatorSet();
        if (this.mIsInTinyScreen) {
            this.mStartValueOfTranslateAnimation = (int) this.mCurrentParentViewTranslationY;
            float translationDistance = getTranslationDistance();
            this.mEndValueOfTranslateAnimation = translationDistance;
            startReleaseAnimInTinyScreen(this.mStartValueOfTranslateAnimation, translationDistance, this.mTranslateHidingDuration, animatorListener);
            return;
        }
        if (isFollowHand()) {
            setDefaultSpringStartEndValue();
            if (this.mDesignBottomSheetFrameLayout.getAlpha() != 1.0f) {
                setAlphaViewAlpha(1.0f);
            }
            if (haveEnoughSpace()) {
                addAnimationFlag(8);
                addAnimationFlag(4);
            } else {
                addAnimationFlag(8);
                addAnimationFlag(2);
                addAnimationFlag(4);
            }
        } else if (isFadeInCenter()) {
            addAnimationFlag(4);
            addAnimationFlag(2);
            addAnimationFlag(8);
            setDefaultSpringStartEndValue();
        } else {
            addAnimationFlag(1);
            addAnimationFlag(2);
            this.mStartValueOfTranslateAnimation = (int) this.mCurrentParentViewTranslationY;
            this.mEndValueOfTranslateAnimation = getTranslationDistance();
        }
        this.mIsAnimationInFirst = false;
        doTranslationAndScaleSpringAnimaion(animatorListener);
        doAlphaSpringAnimaion(null);
    }

    private void doParentViewTranslationShowingAnim(int i3, Animator.AnimatorListener animatorListener) {
        this.mCurrentOutSideAlphaStateShow = 0.0f;
        if (reversalAnimation(animatorListener, true)) {
            this.mIsExecutingDismissAnim = false;
            return;
        }
        if (((COUIBottomSheetBehavior) getBehavior()).isPanelHeightChangeAnimRunning()) {
            this.mIsRevertAnimationFromSettlingAnimation = true;
            ((COUIBottomSheetBehavior) getBehavior()).stopSettlingAnimationIfRunning();
            this.mIsExecutingDismissAnim = false;
        }
        stopCurrentRunningViewTranslationAnim();
        resetAnimationFlag();
        if (getDialogMaxHeight() == 0) {
            Log.d(TAG, "doParentViewTranslationShowingAnim return directly for dialogMaxHeight is 0");
            return;
        }
        this.mIsEntering = true;
        this.mPanelViewTranslationAnimationSet = new AnimatorSet();
        if (this.mIsInTinyScreen) {
            startShowingAnimInTinyScreen(i3, animatorListener);
            return;
        }
        if (isFollowHand()) {
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
            if (cOUIPanelPercentFrameLayout != null) {
                cOUIPanelPercentFrameLayout.setVisibility(4);
                setAlphaViewAlpha(0.0f);
                this.mDesignBottomSheetFrameLayout.setScaleX(0.8f);
                this.mDesignBottomSheetFrameLayout.setScaleY(0.8f);
                this.mDesignBottomSheetFrameLayout.setHasAnchor(true);
            }
            setDefaultSpringStartEndValue();
            scheduleFollowHandShowingAnimAfterLayoutStable(animatorListener);
            return;
        }
        if (isFadeInCenter()) {
            if (this.mDesignBottomSheetFrameLayout != null) {
                setAlphaViewAlpha(0.0f);
                this.mDesignBottomSheetFrameLayout.setScaleX(0.8f);
                this.mDesignBottomSheetFrameLayout.setScaleY(0.8f);
            }
            addAnimationFlag(4);
            addAnimationFlag(2);
            addAnimationFlag(8);
            setDefaultSpringStartEndValue();
        } else {
            if (this.mDesignBottomSheetFrameLayout != null) {
                setAlphaViewAlpha(1.0f);
                this.mDesignBottomSheetFrameLayout.setScaleX(1.0f);
                this.mDesignBottomSheetFrameLayout.setScaleY(1.0f);
            }
            addAnimationFlag(1);
            addAnimationFlag(2);
            this.mStartValueOfTranslateAnimation = getTranslationDistance();
            this.mEndValueOfTranslateAnimation = 0.0f;
            if (this.mIsRevertAnimationFromSettlingAnimation) {
                this.mStartValueOfTranslateAnimation = this.mDesignBottomSheetFrameLayout.getTop();
            }
        }
        this.mIsAnimationInFirst = true;
        doTranslationAndScaleSpringAnimaion(animatorListener);
        doAlphaSpringAnimaion(null);
    }

    private void doSpringBackReboundAnim(int i3) {
        Spring springCreateSpring = SpringSystem.create().createSpring();
        this.mPanelSpringBackAnim = springCreateSpring;
        springCreateSpring.setSpringConfig(SpringConfig.fromBouncinessAndSpeed(6.0d, 42.0d));
        this.mCurrentSpringTotalOffset = 0;
        this.mPanelSpringBackAnim.addListener(new AnonymousClass34(i3));
        this.mPanelSpringBackAnim.setEndValue(i3);
    }

    private void doTranslationAndScaleSpringAnimaion(Animator.AnimatorListener animatorListener) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        initTranslationAndScaleSpringAnimation();
        if (this.mTranslationAndScaleSpringAnimation.isRunning()) {
            this.mTranslationAndScaleSpringAnimation.cancel();
        }
        if (hasAnimationFlag(1)) {
            this.mSpringForceTranslationAndScale.setResponse(getTranslationResponse());
        } else if (hasAnimationFlag(4)) {
            if (this.mIsEntering) {
                this.mSpringForceTranslationAndScale.setResponse(0.25f);
            } else {
                this.mSpringForceTranslationAndScale.setResponse(0.25f);
            }
        }
        this.mTranslationAndScaleSpringAnimation.removeEndListener(this.mTranslationAndScaleEndListener);
        AnonymousClass15 anonymousClass15 = new AnonymousClass15(animatorListener);
        this.mTranslationAndScaleEndListener = anonymousClass15;
        this.mTranslationAndScaleSpringAnimation.addEndListener(anonymousClass15);
        animatorListener.onAnimationStart(null);
        this.mTranslationAndScaleSpringAnimation.removeUpdateListener(this.mTranslationAndScaleUpdateListener);
        this.mTranslationAndScaleUpdateListener = new AnonymousClass16();
        if (!this.mSupportExitBlockingAnimation && (cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout) != null && cOUIPanelPercentFrameLayout.getRatio() != 2.0f) {
            if (this.mIsEntering) {
                this.mTranslationAndScaleSpringAnimation.setMinimumVisibleChange(1.0f);
            } else {
                this.mTranslationAndScaleSpringAnimation.setMinimumVisibleChange(10.0f);
            }
        }
        this.mTranslationAndScaleSpringAnimation.addUpdateListener(this.mTranslationAndScaleUpdateListener);
        setFrameRate(this.mTranslationAndScaleSpringAnimation);
        this.mTranslationAndScaleSpringAnimation.setStartValue(this.mStartValueOfTranslateAnimation);
        this.mTranslationAndScaleSpringAnimation.animateToFinalPosition(this.mIsEntering ? this.mEndValueOfTranslateAnimation : this.mEndValueOfTranslateAnimation + 1.0f);
    }

    private void enableBackEventPassThroughForDismiss() {
        Window window;
        if (!this.mBackEventPassThroughWhileDismissing || this.mNotFocusableAddedForDismiss || (window = getWindow()) == null || (window.getAttributes().flags & 8) != 0) {
            return;
        }
        window.addFlags(8);
        this.mNotFocusableAddedForDismiss = true;
    }

    private void enforceChangeScreenWidth() {
        if (this.mPreferWidth == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            Configuration configuration = resources.getConfiguration();
            this.mOriginWidth = configuration.screenWidthDp;
            configuration.screenWidthDp = this.mPreferWidth;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(TAG, "enforceChangeScreenWidth : OriginWidth=" + this.mOriginWidth + " ,PreferWidth:" + this.mPreferWidth);
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
            if (cOUIPanelPercentFrameLayout != null) {
                cOUIPanelPercentFrameLayout.setPreferWidth(this.mPreferWidth);
            }
        } catch (Exception unused) {
            Log.d(TAG, "enforceChangeScreenWidth : failed to updateConfiguration");
        }
    }

    private void ensureDraggableContentLayout() {
        if (this.mDraggableConstraintLayout == null) {
            createPanelConstraintLayout();
        }
    }

    private void fillAnchorActualDisplayRectInScreen(View view, Rect rect) {
        float width = view.getWidth() > 0 ? view.getWidth() : view.getMeasuredWidth();
        float height = view.getHeight() > 0 ? view.getHeight() : view.getMeasuredHeight();
        if (width <= 0.0f || height <= 0.0f) {
            rect.setEmpty();
            return;
        }
        float scaleX = 1.0f;
        View view2 = view;
        float scaleY = 1.0f;
        while (view2 != null) {
            scaleX *= view2.getScaleX();
            scaleY *= view2.getScaleY();
            Object parent = view2.getParent();
            view2 = parent instanceof View ? (View) parent : null;
        }
        view.getLocationOnScreen(this.mFollowHandViewLocationOnScreenTemp);
        int[] iArr = this.mFollowHandViewLocationOnScreenTemp;
        int i3 = iArr[0];
        int i4 = iArr[1];
        rect.set(i3, i4, Math.round(width * scaleX) + i3, Math.round(height * scaleY) + i4);
    }

    private int getContentViewHeightWithMargins() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            return cOUIPanelPercentFrameLayout.getMeasuredHeight() + COUIViewMarginUtil.getMargin(this.mDesignBottomSheetFrameLayout, 3);
        }
        return 0;
    }

    private boolean getFocusChange() {
        Boolean bool = this.mFocusChange;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    private int getFollowHandEffectiveDialogHeight(int i3) {
        int i4 = this.mFollowHandSafeMaxHeightPx;
        return i4 == -1 ? i3 : Math.min(i3, i4);
    }

    private int getFollowHandEffectiveNavigationBottomInsetPx(WindowInsetsCompat windowInsetsCompat) {
        int i3;
        return (this.mFollowHandRecordedPlacement == 0 || (i3 = this.mFollowHandRecordedNavigationBottomInsetPx) == -1) ? getFollowHandNavigationBottomInsetPx(windowInsetsCompat) : i3;
    }

    private int getFollowHandFallbackNavigationBottomInsetPx() {
        View decorView;
        WindowInsets rootWindowInsets = this.mApplyWindowInsets;
        if (rootWindowInsets == null && getWindow() != null && (decorView = getWindow().getDecorView()) != null) {
            rootWindowInsets = decorView.getRootWindowInsets();
        }
        if (rootWindowInsets != null && rootWindowInsets.isVisible(WindowInsets.Type.navigationBars())) {
            return rootWindowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        }
        return 0;
    }

    private int getFollowHandNavigationBottomInsetPx(WindowInsetsCompat windowInsetsCompat) {
        if (isFollowHandNavigationBarVisible(windowInsetsCompat)) {
            return windowInsetsCompat != null ? windowInsetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom : COUINavigationBarUtil.getNavigationBarHeight(getContext());
        }
        return 0;
    }

    private int getFollowHandSafeTopInCoordinator(Context context) {
        syncFollowHandCoordinatorScreenTopOffset();
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(this.mCoordinatorLayout);
        int iMax = this.mStatusBarHeight;
        if (rootWindowInsets != null) {
            iMax = Math.max(iMax, rootWindowInsets.getSystemWindowInsetTop());
        }
        return Math.max(0, (iMax + context.getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_bottom)) - this.mFollowHandCoordinatorOnScreen[1]);
    }

    private Rect getLocationRectInScreen(View view) {
        view.getLocationOnScreen(this.mFollowHandViewLocationOnScreenTemp);
        int[] iArr = this.mFollowHandViewLocationOnScreenTemp;
        int i3 = iArr[0];
        return new Rect(i3, iArr[1], view.getMeasuredWidth() + i3, this.mFollowHandViewLocationOnScreenTemp[1] + view.getMeasuredHeight());
    }

    private int getNavColor() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        int i3 = this.mNavColor;
        if (i3 != Integer.MAX_VALUE) {
            return i3;
        }
        return (this.mIsHandlePanel || ((cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout) != null && cOUIPanelPercentFrameLayout.getRatio() == 1.0f)) ? this.mPanelBackgroundTintColor : this.mColorMask;
    }

    private Drawable getNavigationDrawable(int i3) {
        if (this.mGradientDrawable == null) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            this.mGradientDrawable = gradientDrawable;
            gradientDrawable.setShape(0);
            this.mGradientDrawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
        }
        this.mGradientDrawable.setColors(new int[]{i3, getSpecifiedTransparencyColor(i3, 0.6f), 0}, new float[]{0.0f, 0.6f, 1.0f});
        GradientDrawable gradientDrawable2 = this.mGradientDrawable;
        return new LayerDrawable(new Drawable[]{gradientDrawable2, gradientDrawable2});
    }

    private COUIPanelPullUpListener getPanelPullUpListener() {
        return new AnonymousClass33();
    }

    private Animator.AnimatorListener getPanelShowAnimListener() {
        return new AnonymousClass32();
    }

    private float getRevertAnimationFinalPositionToHide() {
        return this.mIsAnimationInFirst ? this.mStartValueOfTranslateAnimation : this.mEndValueOfTranslateAnimation;
    }

    private float getRevertAnimationFinalPositionToShow() {
        return this.mIsAnimationInFirst ? this.mEndValueOfTranslateAnimation : this.mStartValueOfTranslateAnimation;
    }

    private int getSpecifiedTransparencyColor(int i3, float f3) {
        return Color.argb((int) ((Color.alpha(i3) / 255.0f) * f3 * 255.0f), Color.red(i3), Color.green(i3), Color.blue(i3));
    }

    private int getTranslationDistance() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null) {
            Log.e(TAG, "getTranslationDistance mDesignBottomSheetFrameLayout == null");
            return UIUtil.getScreenHeightMetrics(getContext());
        }
        int height = cOUIPanelPercentFrameLayout.getHeight() - this.mPanelPaddingBottom;
        InputMethodManager inputMethodManager = this.mInputMethodManager;
        boolean z2 = inputMethodManager != null && inputMethodManager.isAcceptingText();
        if (!this.mIsEntering && z2) {
            this.mDesignBottomSheetFrameLayout.getGlobalVisibleRect(this.mTemtRect);
            height = Math.max(height, UIUtil.getScreenHeightMetrics(getContext()) - this.mTemtRect.top);
        }
        COUIBottomSheetBehavior cOUIBottomSheetBehavior = (COUIBottomSheetBehavior) getBehavior();
        if (cOUIBottomSheetBehavior.getState() != 3) {
            if (cOUIBottomSheetBehavior.getState() == 6) {
                return this.mCoordinatorLayout.getMeasuredHeight() - cOUIBottomSheetBehavior.halfExpandedOffset;
            }
            if (cOUIBottomSheetBehavior.getState() == 4) {
                return this.mPeekHeight;
            }
        }
        return height;
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0086  */
    /* JADX WARN: Code duplicated, block: B:24:0x0088  */
    private float getTranslationResponse() {
        float f3;
        int screenHeightMetrics;
        float fMax;
        float fMax2;
        float f4;
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        float f5 = 0.25f;
        if (cOUIPanelPercentFrameLayout == null) {
            Log.e(TAG, "getTranslationResponse mDesignBottomSheetFrameLayout == null");
            return 0.25f;
        }
        if (this.mIsEntering) {
            f3 = 0.45f;
        } else {
            f5 = 0.18f;
            f3 = DEFAULT_TRANSLATION_HIDE_SPRING_RESPONSE_LARGE;
        }
        float height = cOUIPanelPercentFrameLayout.getHeight() - this.mPanelPaddingBottom;
        if (getBehavior().getState() != 4) {
            if (getBehavior().getState() == 6) {
                height = this.mCoordinatorLayout.getHeight() * getBehavior().getHalfExpandedRatio();
            } else if (getBehavior().getState() != 3) {
                screenHeightMetrics = UIUtil.getScreenHeightMetrics(getContext()) - this.mDesignBottomSheetFrameLayout.getTop();
            }
            fMax = Math.max(this.mCoordinatorLayout.getHeight() - this.mPeekHeight, 0.0f);
            fMax2 = Math.max(height - this.mPeekHeight, 0.0f);
            if (fMax != 0.0f) {
                f4 = fMax2 / fMax;
            } else {
                f4 = 1.0f;
            }
            return f5 + ((f3 - f5) * Math.max(0.0f, f4));
        }
        screenHeightMetrics = this.mPeekHeight;
        height = screenHeightMetrics;
        fMax = Math.max(this.mCoordinatorLayout.getHeight() - this.mPeekHeight, 0.0f);
        fMax2 = Math.max(height - this.mPeekHeight, 0.0f);
        if (fMax != 0.0f) {
            f4 = fMax2 / fMax;
        } else {
            f4 = 1.0f;
        }
        return f5 + ((f3 - f5) * Math.max(0.0f, f4));
    }

    private Drawable getTypedArrayDrawable(TypedArray typedArray, int i3, int i4) {
        Drawable drawable = typedArray != null ? typedArray.getDrawable(i3) : null;
        return drawable == null ? getContext().getResources().getDrawable(i4, getContext().getTheme()) : drawable;
    }

    private void handleBehaviorStateChange(View view, int i3) {
        if (i3 == 3 || i3 == 6 || i3 == 4) {
            this.mLastStaticState = i3;
        }
        if (i3 == 2) {
            if (needHideKeyboardWhenSettling()) {
                hideKeyboard();
            }
        } else if (i3 == 3) {
            this.mAdjustResizeEnable = true;
            this.mWindowInsetsAnimEnable = false;
        } else if (i3 == 5 && !this.mIsRevertAnimationFromSettlingAnimation) {
            dismiss();
        }
    }

    private boolean hasAnimationFlag(int i3) {
        return (this.mAnimationFlag & i3) > 0;
    }

    private boolean hasEditText(ViewGroup viewGroup) {
        for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if ((childAt instanceof EditText) || (childAt instanceof COUIInputView)) {
                return true;
            }
            if ((childAt instanceof ViewGroup) && hasEditText((ViewGroup) childAt)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:148:0x042b  */
    /* JADX WARN: Code duplicated, block: B:150:0x0434  */
    /* JADX WARN: Code duplicated, block: B:152:0x0438  */
    /* JADX WARN: Code duplicated, block: B:154:0x043c  */
    /* JADX WARN: Code duplicated, block: B:156:0x0446  */
    /* JADX WARN: Code duplicated, block: B:157:0x044c  */
    /* JADX WARN: Code duplicated, block: B:160:? A[RETURN, SYNTHETIC] */
    private boolean haveEnoughSpace() {
        View childAt;
        int i3;
        Rect rect;
        int i4;
        boolean z2;
        boolean z3;
        View view;
        boolean z4;
        View view2;
        if (this.mDesignBottomSheetFrameLayout == null || this.mAnchorView == null) {
            return false;
        }
        syncFollowHandCoordinatorScreenTopOffset();
        syncFollowHandAnchorRectInScreen(this.mAnchorView);
        Rect rect2 = this.mFollowHandAnchorRectInScreen;
        int measuredWidth = this.mDesignBottomSheetFrameLayout.getMeasuredWidth();
        int measuredHeight = this.mDesignBottomSheetFrameLayout.getMeasuredHeight();
        View rootView = this.mAnchorView.getRootView();
        if (!(rootView instanceof ViewGroup) || (childAt = ((ViewGroup) rootView).getChildAt(0)) == null) {
            childAt = rootView;
        }
        Rect locationRectInScreen = getLocationRectInScreen(childAt);
        int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_bottom);
        int dimensionPixelOffset2 = getContext().getResources().getDimensionPixelOffset(R.dimen.coui_bottom_sheet_dialog_follow_hand_margin_right);
        int i5 = locationRectInScreen.left;
        int i6 = this.mFollowHandScreenEdgeMarginPx;
        int i7 = i5 + i6;
        int i8 = locationRectInScreen.right - i6;
        int iMax = this.mStatusBarHeight;
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(rootView);
        boolean zIsFollowHandNavigationBarVisible = isFollowHandNavigationBarVisible(rootWindowInsets);
        int followHandNavigationBottomInsetPx = getFollowHandNavigationBottomInsetPx(rootWindowInsets);
        int followHandEffectiveNavigationBottomInsetPx = getFollowHandEffectiveNavigationBottomInsetPx(rootWindowInsets);
        if (rootWindowInsets != null) {
            iMax = Math.max(iMax, rootWindowInsets.getSystemWindowInsetTop());
        }
        int systemWindowInsetBottom = rootWindowInsets == null ? -1 : rootWindowInsets.getSystemWindowInsetBottom();
        int iMax2 = Math.max(this.mFollowHandCoordinatorOnScreen[1], iMax + dimensionPixelOffset);
        int iMin = (locationRectInScreen.bottom - followHandEffectiveNavigationBottomInsetPx) - dimensionPixelOffset;
        if (this.mFollowHandFallbackVerticalEdgeWhenInsufficientSpace && this.mIsGestureNavigation && (view2 = this.mCoordinatorLayout) != null) {
            iMin = Math.min(iMin, (this.mFollowHandCoordinatorOnScreen[1] + view2.getMeasuredHeight()) - computeFollowHandInsufficientSpaceBottomInsetPx(getContext()));
        }
        int i9 = locationRectInScreen.bottom;
        int i10 = iMin;
        int i11 = i9 - i10;
        int i12 = i10 - iMax2;
        int i13 = iMax;
        boolean z5 = (rect2.left - measuredWidth) - dimensionPixelOffset2 >= i7;
        boolean z6 = (rect2.right + measuredWidth) + dimensionPixelOffset2 <= i8;
        int i14 = this.mFollowHandPlacementAllowedFlags;
        int i15 = dimensionPixelOffset * 2;
        int i16 = i14 & 2;
        boolean z7 = (i16 != 0 && ((rect2.top - measuredHeight) - dimensionPixelOffset >= (locationRectInScreen.top + i13) + dimensionPixelOffset)) || ((i14 & 1) != 0 && (((rect2.bottom + measuredHeight) + followHandEffectiveNavigationBottomInsetPx) + i15 < i9));
        int i17 = i14 & 4;
        boolean z8 = (i17 != 0 && z5) || ((i14 & 8) != 0 && z6);
        int i18 = this.mFollowHandRecordedPlacement;
        updateFollowHandSafeMaxHeight(i12, ((i18 == 3 && z5) || (i18 == 4 && z6)) || ((i14 & 12) != 0 && (i14 & 3) == 0) || (!z7 && z8));
        int followHandEffectiveDialogHeight = getFollowHandEffectiveDialogHeight(measuredHeight);
        if (isFollowHand() && this.mFollowHandRecordedPlacement != 0) {
            boolean zIsRecordedFollowHandPlacementSpaceSufficient = isRecordedFollowHandPlacementSpaceSufficient(measuredWidth, followHandEffectiveDialogHeight, locationRectInScreen, followHandEffectiveNavigationBottomInsetPx, dimensionPixelOffset, dimensionPixelOffset2, i13);
            if (!zIsRecordedFollowHandPlacementSpaceSufficient) {
                updateFollowHandSafeMaxHeight(i12, true);
                followHandEffectiveDialogHeight = getFollowHandEffectiveDialogHeight(followHandEffectiveDialogHeight);
            }
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("[follow_hand_position] recordedSpaceCheck: sufficient=");
                sb.append(zIsRecordedFollowHandPlacementSpaceSufficient);
                sb.append(", placement=");
                sb.append(this.mFollowHandRecordedPlacement);
                sb.append(", sideMode=");
                sb.append(this.mFollowHandRecordedSideVerticalMode);
                sb.append(", allowedFlags=");
                sb.append(this.mFollowHandPlacementAllowedFlags);
                sb.append(", gestureNavigation=");
                sb.append(this.mIsGestureNavigation);
                sb.append(", navigationBarVisible=");
                sb.append(zIsFollowHandNavigationBarVisible);
                sb.append(", currentNavigationHeight=");
                sb.append(followHandNavigationBottomInsetPx);
                sb.append(", recordedNavigationHeight=");
                sb.append(this.mFollowHandRecordedNavigationBottomInsetPx);
                sb.append(", effectiveNavigationHeight=");
                sb.append(followHandEffectiveNavigationBottomInsetPx);
                sb.append(", rootInsetBottom=");
                sb.append(systemWindowInsetBottom);
                sb.append(", anchorRect=");
                sb.append(this.mFollowHandAnchorRectInScreen);
                sb.append(", contentRect=");
                sb.append(locationRectInScreen);
                sb.append(", measuredDialogHeight=");
                sb.append(measuredHeight);
                sb.append(", effectiveDialogHeight=");
                sb.append(followHandEffectiveDialogHeight);
                sb.append(", availableVertical=");
                sb.append(i12);
                sb.append(", safeMaxHeight=");
                sb.append(this.mFollowHandSafeMaxHeightPx);
                sb.append(", sidePlacementAllowsVerticalOverflow=");
                int i19 = this.mFollowHandRecordedPlacement;
                sb.append(i19 == 3 || i19 == 4);
                sb.append(", coordinatorOrigin=(");
                sb.append(this.mFollowHandCoordinatorOnScreen[0]);
                sb.append(",");
                z4 = true;
                sb.append(this.mFollowHandCoordinatorOnScreen[1]);
                sb.append(")");
                Log.d(TAG, sb.toString());
            } else {
                z4 = true;
            }
            if (!zIsRecordedFollowHandPlacementSpaceSufficient) {
                return false;
            }
            applyFollowHandHaveEnoughSpaceSuccess(followHandEffectiveDialogHeight);
            return z4;
        }
        if (DEBUG) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[follow_hand] haveEnoughSpace:\n anchorViewLocationRect=");
            sb2.append(this.mFollowHandAnchorRectInScreen);
            sb2.append("\n anchorContentViewLocationRect=");
            sb2.append(locationRectInScreen);
            sb2.append("\n dialogViewWidth=");
            sb2.append(measuredWidth);
            sb2.append(", measuredDialogHeight=");
            sb2.append(measuredHeight);
            sb2.append(", effectiveDialogHeight=");
            sb2.append(followHandEffectiveDialogHeight);
            sb2.append("\n anchorHorizontalMargin=");
            sb2.append(dimensionPixelOffset2);
            sb2.append(", bottomMargin=");
            sb2.append(dimensionPixelOffset);
            sb2.append(", currentNavigationHeight=");
            sb2.append(followHandNavigationBottomInsetPx);
            sb2.append(", recordedNavigationHeight=");
            sb2.append(this.mFollowHandRecordedNavigationBottomInsetPx);
            sb2.append(", effectiveNavigationHeight=");
            sb2.append(followHandEffectiveNavigationBottomInsetPx);
            sb2.append(", rootInsetBottom=");
            sb2.append(systemWindowInsetBottom);
            sb2.append(", gestureNavigation=");
            sb2.append(this.mIsGestureNavigation);
            sb2.append(", navigationBarVisible=");
            sb2.append(zIsFollowHandNavigationBarVisible);
            sb2.append(", allowedFlags=");
            sb2.append(this.mFollowHandPlacementAllowedFlags);
            sb2.append(", availableVertical=");
            i3 = i12;
            sb2.append(i3);
            sb2.append(", safeBottomInset=");
            sb2.append(i11);
            sb2.append(", safeMaxHeight=");
            sb2.append(this.mFollowHandSafeMaxHeightPx);
            sb2.append(", sidePlacementAllowsVerticalOverflow=");
            sb2.append((this.mFollowHandPlacementAllowedFlags & 12) != 0);
            sb2.append("\n mFollowHandCoordinatorOnScreen[1]=");
            sb2.append(this.mFollowHandCoordinatorOnScreen[1]);
            sb2.append(", statusBarTopInset=");
            i4 = i13;
            sb2.append(i4);
            sb2.append(", mFollowHandScreenEdgeMarginPx=");
            sb2.append(this.mFollowHandScreenEdgeMarginPx);
            sb2.append("\n leftFit=");
            rect = rect2;
            sb2.append((rect.left - measuredWidth) - dimensionPixelOffset2 >= i7);
            sb2.append(", rightFit=");
            sb2.append((rect.right + measuredWidth) + dimensionPixelOffset2 <= i8);
            sb2.append(", topFit=");
            sb2.append((rect.top - followHandEffectiveDialogHeight) - dimensionPixelOffset >= (locationRectInScreen.top + i4) + dimensionPixelOffset);
            sb2.append(", bottomFit=");
            sb2.append(((rect.bottom + followHandEffectiveDialogHeight) + followHandEffectiveNavigationBottomInsetPx) + dimensionPixelOffset < locationRectInScreen.bottom);
            Log.d(TAG, sb2.toString());
        } else {
            i3 = i12;
            rect = rect2;
            i4 = i13;
        }
        boolean z9 = (rect.top - followHandEffectiveDialogHeight) - dimensionPixelOffset >= (locationRectInScreen.top + i4) + dimensionPixelOffset;
        boolean z10 = ((rect.bottom + followHandEffectiveDialogHeight) + followHandEffectiveNavigationBottomInsetPx) + i15 < locationRectInScreen.bottom;
        if ((i17 == 0 || !z5) && (((i14 & 8) == 0 || !z6) && (i16 == 0 || !z9))) {
            z2 = true;
            if ((i14 & 1) == 0 || !z10) {
                z3 = false;
            }
            if (!z3) {
                updateFollowHandSafeMaxHeight(i3, z2);
                followHandEffectiveDialogHeight = getFollowHandEffectiveDialogHeight(followHandEffectiveDialogHeight);
            }
            if (z3) {
                applyFollowHandHaveEnoughSpaceSuccess(followHandEffectiveDialogHeight);
                return z2;
            }
            if (this.mColorMask != 0) {
                setElevation();
                return false;
            }
            this.mDesignBottomSheetFrameLayout.setElevation(0.0f);
            view = this.mOutsideView;
            if (view != null) {
                return false;
            }
            view.setAlpha(1.0f);
            return false;
        }
        z2 = true;
        z3 = z2;
        if (!z3) {
            updateFollowHandSafeMaxHeight(i3, z2);
            followHandEffectiveDialogHeight = getFollowHandEffectiveDialogHeight(followHandEffectiveDialogHeight);
        }
        if (z3) {
            applyFollowHandHaveEnoughSpaceSuccess(followHandEffectiveDialogHeight);
            return z2;
        }
        if (this.mColorMask != 0) {
            setElevation();
            return false;
        }
        this.mDesignBottomSheetFrameLayout.setElevation(0.0f);
        view = this.mOutsideView;
        if (view != null) {
            return false;
        }
        view.setAlpha(1.0f);
        return false;
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = this.mInputMethodManager;
        if (inputMethodManager == null || !inputMethodManager.isActive()) {
            return;
        }
        if (getWindow() != null) {
            this.mAdjustResizeEnable = false;
        }
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            this.mInputMethodManager.hideSoftInputFromWindow(cOUIPanelPercentFrameLayout.getWindowToken(), 0);
        }
    }

    private void initBehavior() {
        int i3;
        boolean z2;
        if (!(getBehavior() instanceof COUIBottomSheetBehavior)) {
            throw new IllegalArgumentException("Must use COUIBottomSheetBehavior, check value of bottom_sheet_behavior in strings.xml");
        }
        COUIBottomSheetBehavior cOUIBottomSheetBehavior = (COUIBottomSheetBehavior) getBehavior();
        cOUIBottomSheetBehavior.applyPhysics(this.mPhysicsFrequency, this.mPhysicsDampingRatio);
        cOUIBottomSheetBehavior.setGlobalDrag(this.mGlobalDrag);
        cOUIBottomSheetBehavior.setIsInTinyScreen(this.mIsInTinyScreen);
        cOUIBottomSheetBehavior.setPanelPeekHeight(this.mPeekHeight);
        cOUIBottomSheetBehavior.setPanelSkipCollapsed(this.mSkipCollapsed);
        cOUIBottomSheetBehavior.setIsHandlePanel(this.mIsHandlePanel);
        cOUIBottomSheetBehavior.setLayoutAtMaxHeight(this.mIsShowInMaxHeight);
        cOUIBottomSheetBehavior.setPanelPaddingBottom(this.mPanelPaddingBottom);
        if (this.mIsHandlePanel) {
            if (COUIPanelMultiWindowUtils.isNormalLandScreen(getContext(), this.mConfiguration)) {
                i3 = 4;
                z2 = true;
            } else {
                i3 = 6;
                z2 = false;
            }
            cOUIBottomSheetBehavior.setFitToContents(z2);
            cOUIBottomSheetBehavior.setGestureInsetBottomIgnored(true);
            setIsNeedOutsideViewAnim(false);
        } else {
            i3 = 3;
        }
        int i4 = this.mFirstShowCollapsed ? 4 : i3;
        cOUIBottomSheetBehavior.setPanelState(i4);
        this.mLastStaticState = i4;
        cOUIBottomSheetBehavior.addBottomSheetCallback(new AnonymousClass5());
        if (DEBUG) {
            Log.d(TAG, "initBehavior: peekHeight=" + this.mPeekHeight + " mSkipCollapsed=" + this.mSkipCollapsed + " mIsHandlePanel=" + this.mIsHandlePanel + " mFirstShowCollapsed=" + this.mFirstShowCollapsed + " state=" + i4);
        }
    }

    private void initBlurParams() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        int color = COUIContextUtil.getColor(getContext(), com.support.panel.R.color.coui_bottomdialog_list_mix_blur_light);
        int color2 = COUIContextUtil.getColor(context, com.support.panel.R.color.coui_bottomdialog_list_mix_blur_dark);
        int color3 = COUIContextUtil.getColor(context, com.support.panel.R.color.coui_bottomdialog_list_blend_blur_light);
        int color4 = COUIContextUtil.getColor(context, com.support.panel.R.color.coui_bottomdialog_list_blend_blur_dark);
        int integer = context.getResources().getInteger(com.support.panel.R.integer.coui_bottomdialog_blur_radius);
        this.mMixColorLight = argbToRgbaFloats(color);
        this.mMixColorDark = argbToRgbaFloats(color2);
        this.mBlendColorLight = argbToRgbaFloats(color3);
        this.mBlendColorDark = argbToRgbaFloats(color4);
        this.mBlurRadius = integer;
    }

    private void initCoordinateInsets(WindowInsets windowInsets) {
        View view = this.mCoordinatorLayout;
        if (view == null || windowInsets == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (isFollowHand()) {
            if (layoutParams.topMargin != 0) {
                layoutParams.topMargin = 0;
                this.mCoordinatorLayout.setLayoutParams(layoutParams);
            }
            this.mCoordinatorLayout.post(new AnonymousClass12());
        } else {
            boolean z2 = getContext().getResources().getConfiguration().smallestScreenWidthDp < 600;
            this.mCoordinatorLayoutMinInsetsTop = (int) getContext().getResources().getDimension(com.support.panel.R.dimen.coui_bottom_sheet_margin_top_default);
            if (z2 && windowInsets.getSystemWindowInsetTop() > 0) {
                this.mCoordinatorLayoutMinInsetsTop = windowInsets.getSystemWindowInsetTop();
            }
            if (this.mIsInTinyScreen) {
                if (this.mIsFullScreenInTinyScreen) {
                    this.mCoordinatorLayoutMinInsetsTop = (int) getContext().getResources().getDimension(com.support.panel.R.dimen.coui_panel_min_padding_top_tiny_screen);
                } else {
                    this.mCoordinatorLayoutMinInsetsTop = (int) getContext().getResources().getDimension(com.support.panel.R.dimen.coui_panel_normal_padding_top_tiny_screen);
                }
            }
            int i3 = layoutParams.topMargin;
            int i4 = this.mCoordinatorLayoutMinInsetsTop;
            if (i3 != i4) {
                layoutParams.topMargin = i4;
                this.mCoordinatorLayout.setLayoutParams(layoutParams);
            }
        }
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout != null) {
            Configuration configuration = this.mConfiguration;
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
            cOUIPanelContentLayout.setNavigationMargin(configuration, windowInsets, cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getRatio() == 1.0f, this.mCouiPanelEdgeToEdgeEnable);
        }
    }

    private void initDraggableConstraintLayoutSize() {
        setPanelWidth();
        setPanelHeight();
    }

    private void initMaxHeight(WindowInsets windowInsets) {
        boolean z2 = this.mPanelHeight >= COUIPanelMultiWindowUtils.getPanelMaxHeight(getContext(), null, windowInsets, this.mIsHandlePanel, this.mCouiPanelEdgeToEdgeEnable);
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            cOUIPanelPercentFrameLayout.getLayoutParams().height = (this.mIsShowInMaxHeight || z2) ? -1 : -2;
        }
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout != null) {
            if (this.mIsShowInMaxHeight || z2) {
                cOUIPanelContentLayout.getLayoutParams().height = -1;
            }
        }
    }

    private void initOrRefreshNavigationView() {
        if (unNeedNavigationCustomView()) {
            if (this.mNavigationCustomView == null || !(this.mContainerFrameLayout.getParent() instanceof FrameLayout)) {
                return;
            }
            FrameLayout frameLayout = (FrameLayout) this.mContainerFrameLayout.getParent();
            if (frameLayout.indexOfChild(this.mNavigationCustomView) != -1) {
                frameLayout.removeView(this.mNavigationCustomView);
            }
            this.mNavigationCustomView = null;
            return;
        }
        if (this.mNavigationCustomView == null) {
            View view = new View(getContext());
            this.mNavigationCustomView = view;
            if (this.mCouiPanelEdgeToEdgeEnable) {
                view.setAlpha(0.0f);
            }
        }
        if (isFollowHandAnchorMode()) {
            applyFollowHandTransparentNavigationBar();
        } else {
            setNavigationBarColor(getNavColor());
        }
        if (this.mContainerFrameLayout.getParent() instanceof FrameLayout) {
            FrameLayout frameLayout2 = (FrameLayout) this.mContainerFrameLayout.getParent();
            if (frameLayout2.indexOfChild(this.mNavigationCustomView) != -1) {
                setNavigationCustomViewHeight(this.mApplyWindowInsets);
            } else {
                frameLayout2.addView(this.mNavigationCustomView, new FrameLayout.LayoutParams(-1, this.mApplyWindowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom, 80));
            }
        }
    }

    private void initPeekHeight() {
        if (this.mIsGestureNavigation) {
            this.mPeekHeight = getContext().getResources().getDimensionPixelOffset(com.support.panel.R.dimen.coui_panel_default_peek_height_in_gesture);
        } else if (this.mCouiPanelEdgeToEdgeEnable) {
            this.mPeekHeight = getContext().getResources().getDimensionPixelOffset(com.support.panel.R.dimen.coui_panel_default_peek_height_panel_extend_to_navi);
        } else {
            this.mPeekHeight = getContext().getResources().getDimensionPixelOffset(com.support.panel.R.dimen.coui_panel_default_peek_height);
        }
    }

    private void initPhysics() {
        if (this.mAppearStiffness == Float.MIN_VALUE) {
            this.mAppearStiffness = 200.0f;
        }
        if (this.mAppearDampingRatio == Float.MIN_VALUE) {
            this.mAppearDampingRatio = 0.7f;
        }
        this.mAppearSpringForce = new SpringForce(0.0f).setStiffness(this.mAppearStiffness).setDampingRatio(this.mAppearDampingRatio);
        SpringAnimation spring = new SpringAnimation(new FloatValueHolder()).setSpring(this.mAppearSpringForce);
        this.mAppearSpringAnim = spring;
        spring.addUpdateListener(this);
        this.mAppearSpringAnim.addEndListener(this);
    }

    private void initShadowEdgeDrawable() {
        this.mShadowEdgeDrawable = new COUIShadowEdgeDrawable();
        refreshShadowEdgeDrawableCorner();
        applyShadowEdgeParams();
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        this.mShadowEdgeDrawable.setDrawableEnabled(cOUIMaterialStateManager.materialVersionCheck() ? cOUIMaterialStateManager.isStrokeEffectEnable() : true);
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            cOUIPanelPercentFrameLayout.setShadowEdgeDrawable(this.mShadowEdgeDrawable);
            this.mDesignBottomSheetFrameLayout.addOnLayoutChangeListener(new AnonymousClass7());
        }
    }

    private void initThemeResources(int i3) {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, com.support.panel.R.styleable.COUIBottomSheetDialog, com.support.panel.R.attr.couiBottomSheetDialogStyle, i3);
        this.mPanelDragViewDrawable = getTypedArrayDrawable(typedArrayObtainStyledAttributes, com.support.panel.R.styleable.COUIBottomSheetDialog_panelDragViewIcon, com.support.panel.R.drawable.coui_panel_drag_view);
        int color = typedArrayObtainStyledAttributes.getColor(com.support.panel.R.styleable.COUIBottomSheetDialog_panelDragViewTintColor, COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorControls));
        this.mPanelDragViewDrawableTintColor = color;
        this.mPanelDragViewDrawable.setTint(color);
        this.mPanelBackground = getTypedArrayDrawable(typedArrayObtainStyledAttributes, com.support.panel.R.styleable.COUIBottomSheetDialog_panelBackground, com.support.panel.R.drawable.coui_default_panel_bg_without_shadow);
        this.mPanelBackgroundTintColor = typedArrayObtainStyledAttributes.getColor(com.support.panel.R.styleable.COUIBottomSheetDialog_panelBackgroundTintColor, COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorSurface));
        this.mHandleViewHasPressAnim = typedArrayObtainStyledAttributes.getBoolean(com.support.panel.R.styleable.COUIBottomSheetDialog_couiHandleViewHasPressAnim, true);
        this.mIsShowInMaxHeight = typedArrayObtainStyledAttributes.getBoolean(com.support.panel.R.styleable.COUIBottomSheetDialog_couiShowMaxHeight, true);
        this.mIsHandlePanel = typedArrayObtainStyledAttributes.getBoolean(com.support.panel.R.styleable.COUIBottomSheetDialog_couiIsHandlePanel, false);
        this.mDefaultPaddingBottom = typedArrayObtainStyledAttributes.getDimensionPixelSize(com.support.panel.R.styleable.COUIBottomSheetDialog_couiPanelPaddingBottom, getContext().getResources().getDimensionPixelSize(com.support.panel.R.dimen.coui_panel_padding_bottom));
        this.mCouiPanelEdgeToEdgeEnable = typedArrayObtainStyledAttributes.getBoolean(com.support.panel.R.styleable.COUIBottomSheetDialog_couiPanelEdgeToEdge, false);
        this.mSupportExitBlockingAnimation = typedArrayObtainStyledAttributes.getBoolean(com.support.panel.R.styleable.COUIBottomSheetDialog_couiPanelSupportExitBlockingAnimation, false);
        getAdjustResizeHelper().setCouiPanelEdgeToEdgeEnable(this.mCouiPanelEdgeToEdgeEnable);
        if (this.mIsHandlePanel && this.mSkipCollapsed) {
            this.mSkipCollapsed = false;
        }
        typedArrayObtainStyledAttributes.recycle();
        Drawable drawable = this.mPanelBackground;
        if (drawable != null) {
            drawable.setTint(this.mPanelBackgroundTintColor);
        }
    }

    private void initTranslationAndScaleSpringAnimation() {
        if (this.mTranslationAndScaleSpringAnimation == null) {
            this.mTranslationAndScaleSpringAnimation = new COUISpringAnimation(new FloatValueHolder());
            COUISpringForce cOUISpringForce = new COUISpringForce();
            this.mSpringForceTranslationAndScale = cOUISpringForce;
            cOUISpringForce.setBounce(0.0f);
            this.mTranslationAndScaleSpringAnimation.setSpring(this.mSpringForceTranslationAndScale);
        }
    }

    private void initValueResources() {
        this.mPullUpMaxOffset = (int) getContext().getResources().getDimension(com.support.panel.R.dimen.coui_panel_pull_up_max_offset);
        this.mCoordinatorLayoutMinInsetsTop = (int) getContext().getResources().getDimension(com.support.panel.R.dimen.coui_panel_min_padding_top);
        this.mCoordinatorLayoutPaddingExtra = getContext().getResources().getDimensionPixelOffset(com.support.panel.R.dimen.coui_panel_normal_padding_top);
        refreshFollowHandScreenEdgeMarginPx();
        refreshColorMask();
        this.mIsGestureNavigation = COUINavigationBarUtil.isGestureNavigation(getContext()) || UIUtil.isInFreeFormModeWindowMode(getContext());
        initPeekHeight();
    }

    private void initView() {
        IgnoreWindowInsetsFrameLayout ignoreWindowInsetsFrameLayout = (IgnoreWindowInsetsFrameLayout) findViewById(com.support.panel.R.id.container);
        this.mContainerFrameLayout = ignoreWindowInsetsFrameLayout;
        if (ignoreWindowInsetsFrameLayout != null) {
            ignoreWindowInsetsFrameLayout.setCouiPanelEdgeToEdgeEnable(this.mCouiPanelEdgeToEdgeEnable);
        }
        this.mOutsideView = findViewById(com.support.panel.R.id.panel_outside);
        operateBlockingAnimation();
        this.mCoordinatorLayout = findViewById(com.support.panel.R.id.coordinator);
        this.mDesignBottomSheetFrameLayout = (COUIPanelPercentFrameLayout) findViewById(com.support.appcompat.R.id.design_bottom_sheet);
        syncFollowHandCoordinatorBottomSheetBehavior();
        this.mDesignBottomSheetFrameLayout.setIsInTinyScreen(this.mIsInTinyScreen);
        this.mDesignBottomSheetFrameLayout.setUseNormalSmoothCorner(this.mUseNormalSmoothCorner);
        this.mDesignBottomSheetFrameLayout.setIsHandlePanel(this.mIsHandlePanel);
        this.mPanelBarView = (COUIPanelBarView) findViewById(com.support.panel.R.id.panel_drag_bar);
        ViewGroup.LayoutParams layoutParams = this.mDesignBottomSheetFrameLayout.getLayoutParams();
        boolean z2 = this.mIsShowInMaxHeight;
        layoutParams.height = z2 ? -1 : -2;
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout != null) {
            cOUIPanelContentLayout.setLayoutAtMaxHeight(z2);
        }
        this.mPulledUpView = this.mDesignBottomSheetFrameLayout;
        checkInitState();
        this.mOutsideView.setOnClickListener(new AnonymousClass6());
        if (!this.mBackgroundBlurBuilder.useBackgroundBlur()) {
            this.mDesignBottomSheetFrameLayout.setBackground(this.mPanelBackground);
        }
        updatePaddingBottom();
    }

    private void initWindow() {
        Window window = getWindow();
        if (window != null) {
            window.setDimAmount(0.0f);
            window.setLayout(-1, -1);
            window.setGravity(80);
        }
    }

    private void initWindowInsetsListener() {
        if (this.mShouldRegisterWindowInsetsListener && getWindow() != null && this.mWindowInsetsListener == null) {
            View decorView = getWindow().getDecorView();
            AnonymousClass11 anonymousClass11 = new AnonymousClass11();
            this.mWindowInsetsListener = anonymousClass11;
            decorView.setOnApplyWindowInsetsListener(anonymousClass11);
        }
    }

    private void invalidateFollowHandPosition() {
        View view;
        if (this.mDesignBottomSheetFrameLayout == null || (view = this.mAnchorView) == null) {
            return;
        }
        int i3 = this.mFollowHandRecordedPlacement;
        boolean z2 = i3 != 0;
        if (i3 != 0) {
            calculateFinalLocationForRecordedPlacement(view, this.mFollowHandInvalidatePositionOut);
            this.mDesignBottomSheetFrameLayout.setX(this.mFollowHandInvalidatePositionOut[0]);
            this.mDesignBottomSheetFrameLayout.setY(this.mFollowHandInvalidatePositionOut[1]);
        } else {
            calculateFinalLocationOnScreen(view, this.mFollowHandInvalidatePositionOut);
            this.mDesignBottomSheetFrameLayout.setX(this.mFollowHandInvalidatePositionOut[0]);
            this.mDesignBottomSheetFrameLayout.setY(this.mFollowHandInvalidatePositionOut[1]);
            int[] iArr = this.mFollowHandInvalidatePositionOut;
            tryRecordFollowHandFirstShowPlacement(iArr[2], iArr[3], iArr[4]);
        }
        this.mCurrentParentViewTranslationY = this.mDesignBottomSheetFrameLayout.getY();
        if (DEBUG) {
            Log.d(TAG, "[follow_hand_position] applyPosition: useRecorded=" + z2 + ", allowedFlags=" + this.mFollowHandPlacementAllowedFlags + ", recordedPlacement=" + this.mFollowHandRecordedPlacement + ", recordedSideMode=" + this.mFollowHandRecordedSideVerticalMode + ", recordedNavigationHeight=" + this.mFollowHandRecordedNavigationBottomInsetPx + ", target=(" + this.mFollowHandInvalidatePositionOut[0] + "," + this.mFollowHandInvalidatePositionOut[1] + "), panelLayout=(left=" + this.mDesignBottomSheetFrameLayout.getLeft() + ", top=" + this.mDesignBottomSheetFrameLayout.getTop() + "), panelVisual=(x=" + this.mDesignBottomSheetFrameLayout.getX() + ", y=" + this.mDesignBottomSheetFrameLayout.getY() + ")");
        }
    }

    private boolean isBluring() {
        return this.mBackgroundBlurBuilder.isBlurryState();
    }

    private boolean isFadeInCenter() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null || cOUIPanelPercentFrameLayout.getRatio() != 2.0f) {
            return false;
        }
        if (getBehavior() != null) {
            return (getBehavior() == null || getBehavior().getState() == 4) ? false : true;
        }
        return true;
    }

    private boolean isFadeInCenterAllState() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        return cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getRatio() == 2.0f;
    }

    private boolean isFollowHand() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        return this.mAnchorView != null && (cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout) != null && cOUIPanelPercentFrameLayout.getRatio() == 2.0f && this.mAnchorView.isAttachedToWindow();
    }

    private boolean isFollowHandAnchorMode() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        return isFollowHand() && (cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout) != null && cOUIPanelPercentFrameLayout.getHasAnchor();
    }

    private boolean isFollowHandNavigationBarVisible(WindowInsetsCompat windowInsetsCompat) {
        return windowInsetsCompat != null ? windowInsetsCompat.isVisible(WindowInsetsCompat.Type.navigationBars()) : COUINavigationBarUtil.isNavigationBarShow(getContext());
    }

    private boolean isInMultiWindowMode() {
        WeakReference<Activity> weakReference = this.mActivityWeakReference;
        return (weakReference == null || weakReference.get() == null || !COUIPanelMultiWindowUtils.isInMultiWindowMode(this.mActivityWeakReference.get())) ? false : true;
    }

    private boolean isRecordedFollowHandPlacementSpaceSufficient(int i3, int i4, Rect rect, int i5, int i6, int i7, int i8) {
        int i9 = rect.left;
        int i10 = this.mFollowHandScreenEdgeMarginPx;
        int i11 = i9 + i10;
        int i12 = rect.right - i10;
        int i13 = this.mFollowHandPlacementAllowedFlags;
        int i14 = this.mFollowHandRecordedPlacement;
        if (i14 == 1) {
            return (i13 & 1) != 0 && ((this.mFollowHandAnchorRectInScreen.bottom + i4) + i5) + i6 < rect.bottom;
        }
        if (i14 == 2) {
            return (i13 & 2) != 0 && (this.mFollowHandAnchorRectInScreen.top - i4) - i6 >= (rect.top + i8) + i6;
        }
        if (i14 != 3) {
            return i14 == 4 && (i13 & 8) != 0 && (this.mFollowHandAnchorRectInScreen.right + i3) + i7 <= i12;
        }
        return (i13 & 4) != 0 && (this.mFollowHandAnchorRectInScreen.left - i3) - i7 >= i11;
    }

    private void largeScreenLimitMaxSize() {
        if (this.mDesignBottomSheetFrameLayout == null) {
            return;
        }
        int i3 = getContext().getResources().getConfiguration().screenWidthDp;
        int i4 = getContext().getResources().getConfiguration().screenHeightDp;
        if (!this.isLargeScreenLimitMaxSize || !COUIResponsiveUtils.isLargePadWindow(getContext(), i3, i4) || COUIPanelMultiWindowUtils.isInMultiWindowMode(COUIPanelMultiWindowUtils.contextToActivity(getContext()))) {
            this.mDesignBottomSheetFrameLayout.restoreDefaultMaxSize();
            this.mDesignBottomSheetFrameLayout.setMaxHeight(getContext().getResources().getDimensionPixelOffset(com.support.panel.R.dimen.coui_panel_max_height));
            return;
        }
        int iMin = Math.min(UIUtil.getScreenHeightRealSize(getContext()), UIUtil.getScreenWidthRealSize(getContext()));
        int iMax = Math.max(UIUtil.getScreenHeightRealSize(getContext()), UIUtil.getScreenWidthRealSize(getContext()));
        this.mDesignBottomSheetFrameLayout.setMaxSize((int) COUIResponsiveUtils.calculateWidth(iMax, iMin, this.mDesignBottomSheetFrameLayout.getGridNumber(), this.mDesignBottomSheetFrameLayout.getPaddingType(), this.mDesignBottomSheetFrameLayout.getPaddingSize(), getContext()), iMin - (this.mCoordinatorLayoutMinInsetsTop * 2));
    }

    private boolean needHideKeyboardWhenSettling() {
        return ((COUIBottomSheetBehavior) getBehavior()).isCanHideKeyboard();
    }

    private int normalizePoints(int i3, int i4) {
        return Math.max(0, Math.min(i3, i4));
    }

    private void operateBlockingAnimation() {
        View view = this.mOutsideView;
        if (view == null) {
            return;
        }
        if (!this.mSupportExitBlockingAnimation) {
            if (view != null) {
                view.removeOnAttachStateChangeListener(this.mOnAttatchStateChangeListener);
                return;
            }
            return;
        }
        if (this.mOSDKComputeListener == null) {
            this.mOSDKComputeListener = new AnonymousClass9();
        }
        if (this.mOnAttatchStateChangeListener == null) {
            this.mOnAttatchStateChangeListener = new AnonymousClass10();
        }
        View view2 = this.mOutsideView;
        if (view2 != null) {
            view2.addOnAttachStateChangeListener(this.mOnAttatchStateChangeListener);
        }
    }

    private void outsideAlphaChange(float f3, boolean z2) {
        View view;
        View viewFindFocus;
        InputMethodManager inputMethodManager;
        float f4 = this.mCurrentOutSideAlphaStateHidden;
        if (f4 <= 0.0f) {
            f4 = 1.0f;
        }
        float f5 = this.mCurrentOutSideAlphaStateShow;
        if (f5 <= 0.0f) {
            f5 = 0.0f;
        }
        float outsideViewAlpha = getOutsideViewAlpha(f3);
        this.mCurrentOutsideAlpha = outsideViewAlpha;
        float fMax = z2 ? f5 + (outsideViewAlpha * (1.0f - f5)) : Math.max(0.0f, 1.0f - outsideViewAlpha) * f4;
        View view2 = this.mOutsideView;
        if (view2 != null) {
            view2.setAlpha(fMax);
        }
        boolean z3 = isFollowHand() || isFadeInCenterAllState() || shouldHandlePanelUpdateNavBarColor();
        if (this.mOutsideView == null || !COUIPanelMultiWindowUtils.isVirtualNavigation(getContext()) || !z3 || this.mIsInTinyScreen) {
            if (this.mCouiPanelEdgeToEdgeEnable && (view = this.mNavigationCustomView) != null) {
                if (!this.mIsEntering) {
                    f3 = Math.max(0.0f, 1.0f - f3);
                }
                view.setAlpha(f3);
            }
        } else if (isFollowHandAnchorMode()) {
            applyFollowHandTransparentNavigationBar();
        } else {
            setNavigationBarColorAlpha(fMax);
        }
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout == null || !this.mIsNeedShowKeyboard || (viewFindFocus = cOUIPanelContentLayout.findFocus()) == null || !z2 || (inputMethodManager = this.mInputMethodManager) == null) {
            return;
        }
        inputMethodManager.showSoftInput(viewFindFocus, 0);
    }

    private void positionFollowHandPanelBottomCenterWhenInsufficientSpace() {
        View view = this.mCoordinatorLayout;
        if (view == null || this.mDesignBottomSheetFrameLayout == null) {
            return;
        }
        if (!view.isLayoutRequested() && !this.mDesignBottomSheetFrameLayout.isLayoutRequested()) {
            applyFollowHandBottomCenterFromMeasuredLayout();
            return;
        }
        ViewTreeObserver viewTreeObserver = this.mCoordinatorLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnPreDrawListener(new AnonymousClass35(viewTreeObserver));
        } else {
            applyFollowHandBottomCenterFromMeasuredLayout();
        }
    }

    private void refresBlurCorner() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null) {
            Log.e(TAG, "refresBlurCorner mDesignBottomSheetFrameLayout == null");
        } else {
            if (cOUIPanelPercentFrameLayout.getRatio() == 2.0f) {
                this.mBackgroundBlurBuilder.setCornerRadius(this.mCornerRadius);
                return;
            }
            COUIBackgroundBlurBuilder cOUIBackgroundBlurBuilder = this.mBackgroundBlurBuilder;
            int i3 = this.mCornerRadius;
            cOUIBackgroundBlurBuilder.setCornerRadius(i3, i3, 0.0f, 0.0f);
        }
    }

    private void refreshColorMask() {
        if (this.mColorMask != 0) {
            int color = getContext().getResources().getColor(com.support.appcompat.R.color.coui_color_mask);
            this.mColorMask = color;
            this.mOutsideViewBackgroundAlpha = Color.alpha(color);
        }
    }

    private void refreshFollowHandScreenEdgeMarginPx() {
        this.mFollowHandScreenEdgeMarginPx = getContext().getResources().getDimensionPixelOffset(com.support.responsiveui.R.dimen.layout_grid_margin_small);
    }

    private void refreshParams() {
        if (COUIPanelMultiWindowUtils.isVirtualNavigation(getContext())) {
            return;
        }
        resetParentViewStyle(getContext().getResources().getConfiguration());
        resetNavigationBarColor();
    }

    private void refreshShadowEdgeDrawableCorner() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        if (this.mShadowEdgeDrawable == null || (cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout) == null) {
            return;
        }
        if (cOUIPanelPercentFrameLayout.getRatio() == 2.0f) {
            this.mShadowEdgeDrawable.setCornerRadius(this.mCornerRadius);
            return;
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        int i3 = this.mCornerRadius;
        cOUIShadowEdgeDrawable.setCornerRadius(i3, i3, 0.0f, 0.0f);
    }

    private void registerApplicationConfigChangeListener() {
        getContext().registerComponentCallbacks(this.mComponentCallbacks);
    }

    private void registerBackCallback(View view) {
        OnBackInvokedDispatcher onBackInvokedDispatcherFindOnBackInvokedDispatcher = view.findOnBackInvokedDispatcher();
        if (onBackInvokedDispatcherFindOnBackInvokedDispatcher == null) {
            COUILog.e(TAG, "OnBackInvokedDispatcher is null！");
            return;
        }
        OnBackInvokedCallback onBackInvokedCallback = this.mOnBackInvokedCallback;
        if (onBackInvokedCallback != null) {
            try {
                onBackInvokedDispatcherFindOnBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInvokedCallback);
            } catch (Exception e3) {
                COUILog.e(TAG, "unregisterOnBackInvokedCallback fail: " + e3.getMessage());
            }
            this.mOnBackInvokedCallback = null;
        }
        AnonymousClass37 anonymousClass37 = new AnonymousClass37();
        this.mOnBackInvokedCallback = anonymousClass37;
        try {
            onBackInvokedDispatcherFindOnBackInvokedDispatcher.registerOnBackInvokedCallback(0, anonymousClass37);
        } catch (Exception e4) {
            COUILog.e(TAG, "registerOnBackInvokedCallback fail: " + e4.getMessage());
        }
    }

    private void registerBehaviorPullUpListener() {
        if (getBehavior() instanceof COUIBottomSheetBehavior) {
            this.mPanelPullUpListener = this.mCanPullUp ? getPanelPullUpListener() : null;
            ((COUIBottomSheetBehavior) getBehavior()).setPullUpListener(this.mPanelPullUpListener);
        }
    }

    private void registerPreDrawListener() {
        View view = this.mOutsideView;
        if (view != null) {
            view.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        }
    }

    private void registerPullUpToDismissPanelListener() {
        if (getBehavior() instanceof COUIBottomSheetBehavior) {
            ((COUIBottomSheetBehavior) getBehavior()).setPullUpToDismissPanelListener(this.mPullUpToDismissPanelListener);
        }
    }

    private void releaseApplicationConfigChangeListener() {
        if (this.mComponentCallbacks != null) {
            getContext().unregisterComponentCallbacks(this.mComponentCallbacks);
        }
    }

    private void releaseApplyWindowInsetsListener() {
        Window window = getWindow();
        if (window != null) {
            window.getDecorView().setOnApplyWindowInsetsListener(null);
            this.mWindowInsetsListener = null;
        }
    }

    private void releaseBehaviorPullUpListener() {
        if (getBehavior() instanceof COUIBottomSheetBehavior) {
            ((COUIBottomSheetBehavior) getBehavior()).setPullUpListener(null);
            this.mPanelPullUpListener = null;
        }
    }

    private void releasePullUpToDismissPanelListener() {
        if (getBehavior() instanceof COUIBottomSheetBehavior) {
            ((COUIBottomSheetBehavior) getBehavior()).setPullUpToDismissPanelListener(null);
        }
    }

    private void releaseResizeHelper() {
        clearPendingAdjustResize();
        COUIPanelAdjustResizeHelper cOUIPanelAdjustResizeHelper = this.mAdjustResizeHelper;
        if (cOUIPanelAdjustResizeHelper != null) {
            cOUIPanelAdjustResizeHelper.releaseData();
            this.mAdjustResizeHelper = null;
        }
    }

    private void removeOSDKViewTreeObserver() {
        com.oplus.wrapper.view.ViewTreeObserver viewTreeObserver = this.mOSDKViewTreeObserver;
        if (viewTreeObserver != null) {
            viewTreeObserver.removeOnComputeInternalInsetsListener(this.mOSDKComputeListener);
            this.mOSDKViewTreeObserver = null;
        }
    }

    private void removeOnPreDrawListener() {
        View view = this.mOutsideView;
        if (view != null) {
            view.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        }
    }

    private void resetAnimationFlag() {
        this.mAnimationFlag = 0;
    }

    private void resetNavigationBarColor() {
        setNavigationBarColor(getNavColor());
    }

    private void resetParentViewStyle(Configuration configuration) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null) {
            return;
        }
        COUIViewMarginUtil.setMargin(cOUIPanelPercentFrameLayout, 3, 0);
    }

    private void resetWindowImeAnimFlags() {
        this.mAdjustResizeEnable = true;
        int i3 = 0;
        this.mIsNeedShowKeyboard = false;
        Window window = getWindow();
        getAdjustResizeHelper().setWindowType(window.getAttributes().type);
        int i4 = window.getAttributes().softInputMode & 15;
        if (i4 != 5 || isInMultiWindowMode() || this.mIsInWindowFloatingMode) {
            i3 = i4;
        } else {
            this.mIsNeedShowKeyboard = true;
        }
        window.setSoftInputMode(i3 | 48);
    }

    public static int resolveDialogTheme(Context context, int i3) {
        if (((i3 >>> 24) & 255) >= 1) {
            return i3;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(com.support.panel.R.attr.couiBottomSheetDialogStyle, typedValue, true);
        return typedValue.resourceId;
    }

    private int resolveFollowHandVerticalY(int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        int i10 = this.mFollowHandCoordinatorOnScreen[1];
        int iMax = Math.max(i10, i6 + i7);
        int i11 = ((i3 - i4) - i5) - i7;
        int i12 = this.mFollowHandPlacementAllowedFlags;
        boolean z2 = (i12 & 1) != 0;
        boolean z3 = (i12 & 2) != 0;
        boolean z4 = (i12 & 4) != 0;
        boolean z5 = (i12 & 8) != 0;
        Rect rect = this.mFollowHandAnchorRectInScreen;
        int i13 = rect.bottom;
        int i14 = i13 + i7;
        if (z2 && i14 >= iMax && i14 <= i11) {
            this.mFollowHandCurrentBranch = BRANCH_FOLLOW_HAND_BELOW_ANCHOR;
            return i14 - i10;
        }
        int i15 = rect.top;
        int i16 = (i15 - i7) - i4;
        if (z3 && i16 >= iMax && i16 <= i11) {
            this.mFollowHandCurrentBranch = BRANCH_FOLLOW_HAND_ABOVE_ANCHOR;
            return i16 - i10;
        }
        if (z4 || z5) {
            if (i11 < iMax) {
                this.mFollowHandCurrentBranch = BRANCH_FOLLOW_HAND_SIDE_CLAMP_OVERFLOW;
                return i8;
            }
            this.mFollowHandCurrentBranch = BRANCH_FOLLOW_HAND_SIDE_ANCHOR;
            return Math.max(i8, Math.min(MathUtils.clamp(((i13 + i15) / 2) - (i4 / 2), iMax, i11) - i10, i9));
        }
        if (z2) {
            this.mFollowHandCurrentBranch = BRANCH_FOLLOW_HAND_BELOW_ANCHOR;
            return Math.max(i8, Math.min(MathUtils.clamp(i14, iMax, i11) - i10, i9));
        }
        if (z3) {
            this.mFollowHandCurrentBranch = BRANCH_FOLLOW_HAND_ABOVE_ANCHOR;
            return Math.max(i8, Math.min(MathUtils.clamp(i16, iMax, i11) - i10, i9));
        }
        this.mFollowHandCurrentBranch = BRANCH_FOLLOW_HAND_BELOW_ANCHOR;
        return i8;
    }

    private void restoreFollowHandBottomSheetBehaviorIfDetached() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null || this.mFollowHandDetachedBottomSheetBehavior == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = cOUIPanelPercentFrameLayout.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            this.mFollowHandDetachedBottomSheetBehavior = null;
            return;
        }
        CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) layoutParams;
        if (layoutParams2.getBehavior() == null) {
            layoutParams2.setBehavior(this.mFollowHandDetachedBottomSheetBehavior);
            View view = this.mCoordinatorLayout;
            if (view != null) {
                view.requestLayout();
            }
        }
        this.mFollowHandDetachedBottomSheetBehavior = null;
    }

    private void restoreFollowHandSafeMaxHeight() {
        int i3;
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null && (i3 = this.mFollowHandOriginalMaxHeightPx) != -1) {
            cOUIPanelPercentFrameLayout.setMaxHeight(i3);
        }
        this.mFollowHandSafeMaxHeightPx = -1;
        this.mFollowHandOriginalMaxHeightPx = -1;
    }

    private void restoreScreenWidth() {
        if (this.mOriginWidth == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.screenWidthDp = this.mOriginWidth;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(TAG, "restoreScreenWidth : PreferWidth=" + this.mPreferWidth + " ,OriginWidth=" + this.mOriginWidth);
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
            if (cOUIPanelPercentFrameLayout != null) {
                cOUIPanelPercentFrameLayout.delPreferWidth();
            }
        } catch (Exception unused) {
            Log.d(TAG, "restoreScreenWidth : failed to updateConfiguration");
        }
    }

    private void restoreWindowFocusAfterDismiss() {
        if (this.mNotFocusableAddedForDismiss) {
            Window window = getWindow();
            if (window != null) {
                window.clearFlags(8);
            }
            this.mNotFocusableAddedForDismiss = false;
        }
    }

    private boolean reversalAnimation(Animator.AnimatorListener animatorListener, boolean z2) {
        COUISpringAnimation cOUISpringAnimation;
        COUISpringAnimation cOUISpringAnimation2;
        if (this.mDesignBottomSheetFrameLayout == null || (cOUISpringAnimation = this.mTranslationAndScaleSpringAnimation) == null || !cOUISpringAnimation.isRunning() || (cOUISpringAnimation2 = this.mAlphaSpringAnimation) == null || !cOUISpringAnimation2.isRunning()) {
            return false;
        }
        if (z2) {
            this.mTranslationAndScaleSpringAnimation.animateToFinalPosition(getRevertAnimationFinalPositionToShow());
            this.mAlphaSpringAnimation.animateToFinalPosition(getRevertAnimationFinalPositionToShow());
        } else {
            float revertAnimationFinalPositionToHide = getRevertAnimationFinalPositionToHide();
            if (this.mDesignBottomSheetFrameLayout.getRatio() == 1.0f) {
                revertAnimationFinalPositionToHide = Math.max(getRevertAnimationFinalPositionToHide(), getTranslationDistance());
            }
            this.mTranslationAndScaleSpringAnimation.animateToFinalPosition(revertAnimationFinalPositionToHide);
            this.mAlphaSpringAnimation.animateToFinalPosition(revertAnimationFinalPositionToHide);
            OnAnimationListener onAnimationListener = this.mAnimationListener;
            if (onAnimationListener != null) {
                onAnimationListener.onDismissAnimationStart();
            }
            ShadowUtils.clearShadow(this.mDesignBottomSheetFrameLayout);
        }
        this.mTranslationAndScaleSpringAnimation.removeEndListener(this.mTranslationAndScaleEndListener);
        AnonymousClass38 anonymousClass38 = new AnonymousClass38(animatorListener);
        this.mTranslationAndScaleEndListener = anonymousClass38;
        this.mTranslationAndScaleSpringAnimation.addEndListener(anonymousClass38);
        return true;
    }

    private static int sanitizeFollowHandPlacementFlags(int i3) {
        int i4 = i3 & 15;
        if (i4 != 0) {
            return i4;
        }
        return 15;
    }

    private void saveActivityContextToGetMultiWindowInfo(Context context) {
        Activity activityContextToActivity = UIUtil.contextToActivity(context);
        if (activityContextToActivity != null) {
            this.mActivityWeakReference = new WeakReference<>(activityContextToActivity);
        }
    }

    private void scheduleAdjustResizeAfterLayout(WindowInsets windowInsets, ViewGroup viewGroup) {
        if (this.mPendingAdjustResizeLayout != viewGroup) {
            clearPendingAdjustResize();
            this.mPendingAdjustResizeLayout = viewGroup;
            viewGroup.getViewTreeObserver().addOnPreDrawListener(this.mAdjustResizeOnPreDrawListener);
        }
        this.mPendingAdjustResizeWindowInsets = windowInsets;
    }

    private void scheduleFollowHandShowingAnimAfterLayoutStable(Animator.AnimatorListener animatorListener) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null) {
            return;
        }
        ViewTreeObserver viewTreeObserver = cOUIPanelPercentFrameLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            if (this.mIsShowInDialogFragment) {
                viewTreeObserver.addOnGlobalLayoutListener(new AnonymousClass20(cOUIPanelPercentFrameLayout, animatorListener));
            } else {
                startFollowHandShowingAnim(animatorListener);
            }
        }
    }

    private void scheduleFollowHandShowingAnimAfterSafeHeightLayout(Animator.AnimatorListener animatorListener, boolean z2) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null || this.mFollowHandWaitingForSafeHeightLayout) {
            return;
        }
        ViewTreeObserver viewTreeObserver = cOUIPanelPercentFrameLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            this.mFollowHandWaitingForSafeHeightLayout = true;
            if (DEBUG) {
                Log.d(TAG, "[follow_hand_position] waitSafeHeightLayout: measuredHeight=" + cOUIPanelPercentFrameLayout.getMeasuredHeight() + ", safeMaxHeight=" + this.mFollowHandSafeMaxHeightPx + ", layoutRequested=" + cOUIPanelPercentFrameLayout.isLayoutRequested());
            }
            viewTreeObserver.addOnGlobalLayoutListener(new AnonymousClass21(cOUIPanelPercentFrameLayout, animatorListener, z2));
        }
    }

    private void setAlphaViewAlpha(float f3) {
        View view = this.mBottomSheetDialogAlphaView;
        if (view != null) {
            view.setAlpha(f3);
        }
        if (isBluring()) {
            updateBlurAlphaParams(f3);
            COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
            if (cOUIShadowEdgeDrawable != null) {
                cOUIShadowEdgeDrawable.setAlpha((int) (f3 * 255.0f));
            }
        }
    }

    private void setContentViewLocal(View view) {
        if (this.mIsShowInDialogFragment) {
            super.setContentView(view);
        } else {
            ensureDraggableContentLayout();
            this.mDraggableConstraintLayout.removeContentView();
            this.mDraggableConstraintLayout.addContentView(view);
            super.setContentView(this.mDraggableConstraintLayout);
        }
        this.mContentView = view;
    }

    private void setDefaultSpringStartEndValue() {
        this.mStartValueOfTranslateAnimation = 0.0f;
        this.mEndValueOfTranslateAnimation = 100.0f;
    }

    private void setElevation() {
        ShadowUtils.setElevationToView(this.mDesignBottomSheetFrameLayout, 3, getContext().getResources().getDimensionPixelOffset(com.support.panel.R.dimen.coui_bottom_sheet_dialog_elevation), ContextCompat.getColor(getContext(), com.support.panel.R.color.coui_panel_follow_hand_spot_shadow_color));
    }

    private void setFocusChangeFalseIfHasnotEdittext() {
        if (this.mFocusChange == null && hasEditText((ViewGroup) getWindow().getDecorView().getRootView())) {
            this.mFocusChange = Boolean.TRUE;
        }
    }

    private void setFrameRate(ValueAnimator valueAnimator) {
        if (!this.mIsVSdk || this.mDesignBottomSheetFrameLayout == null) {
            return;
        }
        int i3 = this.mADFRFeatureType;
        if (i3 == 2) {
            valueAnimator.addUpdateListener(new b(this, new AnimationVelocityCalculator(valueAnimator), valueAnimator));
            valueAnimator.addListener(new AnonymousClass23());
        } else if (i3 == 1) {
            valueAnimator.addListener(new AnonymousClass24());
        } else if (i3 == 0) {
            COUILog.d(TAG, "LEVEL_DEFAULT do nothing");
        }
    }

    private void setNavigation() {
        if (!this.mIsGestureNavigation) {
            getWindow().setDecorFitsSystemWindows(false);
            getWindow().setNavigationBarContrastEnforced(false);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | 512);
            getWindow().setNavigationBarContrastEnforced(false);
            setNavigationBarColor(0);
        }
    }

    private void setNavigationBarColor(int i3) {
        if (unNeedNavigationCustomView()) {
            getWindow().setNavigationBarColor(i3);
        } else {
            getWindow().setNavigationBarColor(0);
        }
        setNavigationCustomViewColor(i3);
        COUILog.d(TAG, "setNavigationBarColor color: " + Integer.toHexString(i3));
    }

    private void setNavigationBarColorAlpha(float f3) {
        if (this.mColorMask == 0) {
            return;
        }
        int i3 = (int) (f3 * this.mOutsideViewBackgroundAlpha);
        if (i3 > 0) {
            setNavigationBarColor(Color.argb(i3, 0, 0, 0));
        } else {
            setNavigationBarColor(0);
            getWindow().setNavigationBarContrastEnforced(false);
        }
    }

    private void setNavigationCustomViewColor(int i3) {
        View view;
        if (unNeedNavigationCustomView() || (view = this.mNavigationCustomView) == null) {
            return;
        }
        if (this.mCouiPanelEdgeToEdgeEnable) {
            view.setBackground(getNavigationDrawable(i3));
        } else {
            view.setBackgroundColor(i3);
            this.mNavigationCustomView.setAlpha(1.0f);
        }
    }

    private void setNavigationCustomViewHeight(WindowInsets windowInsets) {
        if (unNeedNavigationCustomView() || windowInsets == null || this.mNavigationCustomView == null) {
            return;
        }
        int i3 = windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        this.mNavigationCustomView.getLayoutParams().height = Math.max(0, i3);
    }

    private void setPanelHeight() {
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout != null) {
            ViewGroup.LayoutParams layoutParams = cOUIPanelContentLayout.getLayoutParams();
            int i3 = this.mPanelHeight;
            if (i3 != 0) {
                layoutParams.height = i3;
            }
            this.mDraggableConstraintLayout.setLayoutParams(layoutParams);
        }
        WindowInsets windowInsets = this.mApplyWindowInsets;
        if (windowInsets != null) {
            initMaxHeight(windowInsets);
        }
    }

    private void setPanelWidth() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            ViewGroup.LayoutParams layoutParams = cOUIPanelPercentFrameLayout.getLayoutParams();
            int i3 = this.mPanelWidth;
            if (i3 != 0) {
                layoutParams.width = i3;
            }
            this.mDesignBottomSheetFrameLayout.setLayoutParams(layoutParams);
        }
    }

    private void setPulledUpViewPaddingBottom(int i3) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        if (this.mPulledUpView == null || (cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout) == null) {
            return;
        }
        if (TextUtils.equals(cOUIPanelPercentFrameLayout.getClass().getSimpleName(), this.mPulledUpView.getClass().getSimpleName())) {
            i3 += this.mPanelPaddingBottom;
        }
        View view = this.mPulledUpView;
        view.setPadding(view.getPaddingLeft(), this.mPulledUpView.getPaddingTop(), this.mPulledUpView.getPaddingRight(), i3);
    }

    private void setShadowEdgeDrawableCallback(View view) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(view);
        }
    }

    private void setSpringStartPosition(float f3) {
        this.mAppearSpringAnim.setStartValue(f3);
    }

    private void setStatusBarTransparentAndFont(Window window) {
        int i3;
        if (window == null) {
            return;
        }
        View decorView = window.getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        int i4 = systemUiVisibility | 1024;
        window.setStatusBarColor(0);
        window.addFlags(Integer.MIN_VALUE);
        if (COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext())) {
            i3 = i4 & (-8193);
            if (!this.mManualAppearanceLightNavigationBars) {
                i3 = i4 & (-8209);
            }
        } else {
            i3 = systemUiVisibility | 9472;
        }
        decorView.setSystemUiVisibility(i3);
    }

    private void setStrokeEffectEnable() {
        if (this.mDesignBottomSheetFrameLayout == null) {
            return;
        }
        if (this.mShadowEdgeDrawable == null) {
            initShadowEdgeDrawable();
        }
        if (this.mAlwayBlur) {
            this.mShadowEdgeDrawable.setDrawableEnabled(true);
            return;
        }
        if (!isBluring()) {
            this.mShadowEdgeDrawable.setDrawableEnabled(false);
        } else if (this.mStokeEnable) {
            this.mShadowEdgeDrawable.setDrawableEnabled(true);
        } else {
            this.mShadowEdgeDrawable.setDrawableEnabled(false);
        }
    }

    private boolean shouldHandlePanelUpdateNavBarColor() {
        if (this.mIsHandlePanel) {
            return COUIPanelMultiWindowUtils.isNormalLandScreen(getContext(), this.mConfiguration);
        }
        return false;
    }

    private boolean shouldUpdatePanelMarginBottom(WindowInsets windowInsets) {
        return windowInsets == null || this.mApplyWindowInsets == null || this.mDesignBottomSheetFrameLayout == null || windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom != this.mApplyWindowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom || this.mPanelRatio != this.mDesignBottomSheetFrameLayout.getRatio();
    }

    private boolean shouldWaitForFollowHandSafeHeightLayout() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        return (cOUIPanelPercentFrameLayout == null || this.mFollowHandSafeMaxHeightPx == -1 || cOUIPanelPercentFrameLayout.getMeasuredHeight() <= this.mFollowHandSafeMaxHeightPx) ? false : true;
    }

    private void snapToTop() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            this.mSnapStartBottom = cOUIPanelPercentFrameLayout.getBottom();
        }
        this.mIsAppearSpringAnimStared = true;
        this.mAppearSpringAnim.start();
    }

    private void startFollowHandShowingAnim(Animator.AnimatorListener animatorListener) {
        syncFollowHandCoordinatorScreenTopOffset();
        boolean zHaveEnoughSpace = haveEnoughSpace();
        if (!shouldWaitForFollowHandSafeHeightLayout()) {
            completeFollowHandShowingAnim(animatorListener, zHaveEnoughSpace);
        } else {
            this.mDesignBottomSheetFrameLayout.setVisibility(4);
            scheduleFollowHandShowingAnimAfterSafeHeightLayout(animatorListener, zHaveEnoughSpace);
        }
    }

    private void startListeningForBackCallbacks(View view) {
        if (view.isAttachedToWindow()) {
            registerBackCallback(view);
        } else {
            view.addOnAttachStateChangeListener(new AnonymousClass36());
        }
    }

    private void startReleaseAnim(Animator.AnimatorListener animatorListener) {
        if (animatorListener != null) {
            this.mPanelViewTranslationAnimationSet.addListener(animatorListener);
        }
        this.mPanelViewTranslationAnimationSet.start();
    }

    private void startReleaseAnimInTinyScreen(float f3, float f4, float f5, Animator.AnimatorListener animatorListener) {
        this.mPanelViewTranslationAnimationSet.playTogether(createPanelTranslateAnimation(f3, f4, this.mTranslateHidingDuration, new COUIOutEaseInterpolator()), createOutsideAlphaAnimation(false, DEFAULT_ALPHA_HIDING_ANIMATOR_DURATION, new COUIEaseInterpolator()));
        startReleaseAnim(animatorListener);
    }

    private void startShowingAnim(Animator.AnimatorListener animatorListener) {
        if (animatorListener != null) {
            this.mPanelViewTranslationAnimationSet.addListener(animatorListener);
        }
        this.mPanelViewTranslationAnimationSet.start();
    }

    private void startShowingAnimInTinyScreen(int i3, Animator.AnimatorListener animatorListener) {
        this.mPanelViewTranslationAnimationSet.playTogether(createOutsideAlphaAnimation(true, SHOW_HEIGHT_ANIM_DURATION_IN_TINY_SCREEN, (PathInterpolator) OUTSIDE_ALPHA_ANIM_INTERPOLATOR));
        setSpringStartPosition(this.mFirstShowCollapsed ? this.mPeekHeight : getContentViewHeightWithMargins() + i3);
        snapToTop();
        startShowingAnim(animatorListener);
    }

    private void stopCurrentRunningViewTranslationAnim() {
        AnimatorSet animatorSet = this.mPanelViewTranslationAnimationSet;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.mIsInterruptingAnim = true;
            this.mPanelViewTranslationAnimationSet.end();
        }
        COUISpringAnimation cOUISpringAnimation = this.mTranslationAndScaleSpringAnimation;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mTranslationAndScaleSpringAnimation.cancel();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mAlphaSpringAnimation;
        if (cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning()) {
            this.mAlphaSpringAnimation.cancel();
        }
        if (this.mIsInTinyScreen && this.mIsAppearSpringAnimStared) {
            this.mAppearSpringAnim.cancel();
        }
    }

    private void stopFeedbackAnimation() {
        Spring spring = this.mDisableFastCloseFeedbackSpring;
        if (spring == null || spring.getVelocity() == 0.0d) {
            return;
        }
        this.mDisableFastCloseFeedbackSpring.setAtRest();
        this.mDisableFastCloseFeedbackSpring = null;
    }

    private void stopListeningForBackCallbacks(View view) {
        OnBackInvokedCallback onBackInvokedCallback;
        OnBackInvokedDispatcher onBackInvokedDispatcherFindOnBackInvokedDispatcher = view.findOnBackInvokedDispatcher();
        if (onBackInvokedDispatcherFindOnBackInvokedDispatcher == null || (onBackInvokedCallback = this.mOnBackInvokedCallback) == null) {
            return;
        }
        onBackInvokedDispatcherFindOnBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInvokedCallback);
        this.mOnBackInvokedCallback = null;
    }

    private void superDismiss() {
        if (DEBUG) {
            Log.d(TAG, "superDismiss");
        }
        try {
            this.mIsRevertAnimationFromSettlingAnimation = false;
            this.mFollowHandWaitingForSafeHeightLayout = false;
            clearFollowHandRecordedPlacement();
            restoreFollowHandSafeMaxHeight();
            restoreFollowHandBottomSheetBehaviorIfDetached();
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
            if (cOUIPanelPercentFrameLayout != null) {
                cOUIPanelPercentFrameLayout.setTranslationY(0.0f);
                this.mDesignBottomSheetFrameLayout.setVisibility(8);
            }
            super.dismiss();
            OnAnimationListener onAnimationListener = this.mAnimationListener;
            if (onAnimationListener != null) {
                onAnimationListener.onDismissAnimationEnd();
            }
            this.mIsExecutingDismissAnim = false;
            clearAppearanceLightNavigationBars();
        } catch (Exception e3) {
            Log.e(TAG, e3.getMessage(), e3);
        }
    }

    private void syncFollowHandAnchorRectInScreen(View view) {
        if (this.mFollowHandUseAnchorActualDisplayRect) {
            fillAnchorActualDisplayRectInScreen(view, this.mFollowHandAnchorRectInScreen);
            if (DEBUG) {
                Log.d(TAG, "[follow_hand_position] anchorRect: mode=actualDisplay, rect=" + this.mFollowHandAnchorRectInScreen + ", scale=(" + view.getScaleX() + "," + view.getScaleY() + "), pivot=(" + view.getPivotX() + "," + view.getPivotY() + ")");
                return;
            }
            return;
        }
        view.getLocationOnScreen(this.mFollowHandViewLocationOnScreenTemp);
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        float scaleX = view.getScaleX();
        float scaleY = view.getScaleY();
        float pivotX = view.getPivotX();
        float pivotY = view.getPivotY();
        int iRound = Math.round(this.mFollowHandViewLocationOnScreenTemp[0] + ((scaleX - 1.0f) * pivotX));
        int iRound2 = Math.round(this.mFollowHandViewLocationOnScreenTemp[1] + ((scaleY - 1.0f) * pivotY));
        this.mFollowHandAnchorRectInScreen.set(iRound, iRound2, iRound + measuredWidth, iRound2 + measuredHeight);
        if (DEBUG) {
            Log.d(TAG, "[follow_hand_position] anchorRect: mode=layout, visualOrigin=(" + this.mFollowHandViewLocationOnScreenTemp[0] + "," + this.mFollowHandViewLocationOnScreenTemp[1] + "), rect=" + this.mFollowHandAnchorRectInScreen + ", measured=" + measuredWidth + "x" + measuredHeight + ", scale=(" + scaleX + "," + scaleY + "), pivot=(" + pivotX + "," + pivotY + ")");
        }
    }

    private void syncFollowHandCoordinatorBottomSheetBehavior() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = cOUIPanelPercentFrameLayout.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) layoutParams;
            if (!isFollowHand()) {
                if (this.mFollowHandDetachedBottomSheetBehavior != null) {
                    restoreFollowHandBottomSheetBehaviorIfDetached();
                    return;
                }
                return;
            }
            CoordinatorLayout.Behavior behavior = layoutParams2.getBehavior();
            if ((behavior != null || this.mFollowHandDetachedBottomSheetBehavior == null) && (behavior instanceof COUIBottomSheetBehavior)) {
                if (this.mFollowHandDetachedBottomSheetBehavior == null) {
                    this.mFollowHandDetachedBottomSheetBehavior = (COUIBottomSheetBehavior) behavior;
                }
                layoutParams2.setBehavior(null);
                View view = this.mCoordinatorLayout;
                if (view != null) {
                    view.requestLayout();
                }
            }
        }
    }

    private void syncFollowHandCoordinatorScreenTopOffset() {
        View view;
        if (!isFollowHand() || (view = this.mCoordinatorLayout) == null) {
            int[] iArr = this.mFollowHandCoordinatorOnScreen;
            iArr[0] = 0;
            iArr[1] = 0;
            if (DEBUG) {
                Log.d(TAG, "[follow_hand_position] coordinatorOrigin: reset, isFollowHand=" + isFollowHand() + ", coordinator=" + this.mCoordinatorLayout);
                return;
            }
            return;
        }
        view.getLocationOnScreen(this.mFollowHandCoordinatorOnScreen);
        if (DEBUG) {
            Log.d(TAG, "[follow_hand_position] coordinatorOrigin: screen=(" + this.mFollowHandCoordinatorOnScreen[0] + "," + this.mFollowHandCoordinatorOnScreen[1] + "), layout=(left=" + this.mCoordinatorLayout.getLeft() + ", top=" + this.mCoordinatorLayout.getTop() + "), size=" + this.mCoordinatorLayout.getMeasuredWidth() + "x" + this.mCoordinatorLayout.getMeasuredHeight());
        }
    }

    private void translateUpdate(float f3) {
        if (this.mDesignBottomSheetFrameLayout != null) {
            if (!isBluring() || Color.alpha(this.mColorMask) <= 0) {
                this.mDesignBottomSheetFrameLayout.setTranslationY(f3);
            } else {
                this.mDesignBottomSheetFrameLayout.setTranslationY(Math.round(f3));
            }
            if (!this.mIsInterruptingAnim) {
                this.mCurrentParentViewTranslationY = f3;
            }
            this.mIsInterruptingAnim = false;
        }
    }

    private void tryRecordFollowHandFirstShowPlacement(int i3, int i4, int i5) {
        if (isFollowHand() && this.mDesignBottomSheetFrameLayout != null && this.mFollowHandRecordedPlacement == 0) {
            this.mFollowHandRecordedPlacement = i3;
            this.mFollowHandRecordedSideVerticalMode = i4;
            this.mFollowHandRecordedNavigationBottomInsetPx = i5;
        }
    }

    private boolean unNeedNavigationCustomView() {
        return this.mIsGestureNavigation || this.mDesignBottomSheetFrameLayout == null;
    }

    private void updateBlurAlphaParams(float f3) {
        float fMax = Math.max(0.0f, Math.min(1.0f, f3));
        float[] fArr = this.mMixColorLight;
        float[] fArr2 = {fArr[0], fArr[1], fArr[2], fArr[3] * fMax};
        float[] fArr3 = this.mMixColorDark;
        float[] fArr4 = {fArr3[0], fArr3[1], fArr3[2], fArr3[3] * fMax};
        float[] fArr5 = this.mBlendColorLight;
        float[] fArr6 = {fArr5[0], fArr5[1], fArr5[2], fArr5[3] * fMax};
        float[] fArr7 = this.mBlendColorDark;
        float[] fArr8 = {fArr7[0], fArr7[1], fArr7[2], fArr7[3] * fMax};
        int iRound = Math.round(this.mBlurRadius * fMax);
        this.mBackgroundBlurBuilder.setMixColorLight(fArr2);
        this.mBackgroundBlurBuilder.setMixColorDark(fArr4);
        this.mBackgroundBlurBuilder.setBlendColorLight(fArr6);
        this.mBackgroundBlurBuilder.setBlendColorDark(fArr8);
        this.mBackgroundBlurBuilder.setBlurRadius(iRound);
        this.mBackgroundBlurBuilder.refreshBlurParams();
    }

    private void updateBottomSheetCenterVertical() {
        View view = this.mCoordinatorLayout;
        if (view == null) {
            Log.w(TAG, "updateBottomSheetCenterVertical: directly return for mCoordinatorLayout is null");
            return;
        }
        if (this.mDesignBottomSheetFrameLayout == null) {
            Log.i(TAG, "updateBottomSheetCenterVertical: directly return for mDesignBottomSheetFrameLayout is null");
            return;
        }
        int measuredHeight = view.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = this.mDesignBottomSheetFrameLayout.getLayoutParams();
        int iMax = (int) Math.max(0.0f, ((measuredHeight - (layoutParams instanceof ViewGroup.MarginLayoutParams ? ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin : 0)) / this.mDesignBottomSheetFrameLayout.getRatio()) - (this.mDesignBottomSheetFrameLayout.getHeight() / this.mDesignBottomSheetFrameLayout.getRatio()));
        if (this.mDesignBottomSheetFrameLayout.getBottom() + iMax <= measuredHeight) {
            this.mDesignBottomSheetFrameLayout.setY(iMax);
        }
    }

    private void updateBottomSheetDialogAlphaView() {
        if (!isBluring()) {
            this.mBottomSheetDialogAlphaView = this.mDesignBottomSheetFrameLayout;
            return;
        }
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            this.mBottomSheetDialogAlphaView = cOUIPanelPercentFrameLayout.getChildAt(0);
        }
    }

    private void updateFitToContents() {
        if (this.mIsHandlePanel) {
            if (this.mDesignBottomSheetFrameLayout == null) {
                Log.e(TAG, "updateFitToContents: mDesignBottomSheetFrameLayout is null");
                return;
            }
            BottomSheetBehavior<FrameLayout> behavior = getBehavior();
            if (behavior instanceof COUIBottomSheetBehavior) {
                ((COUIBottomSheetBehavior) behavior).setFitToContents(COUIPanelMultiWindowUtils.isNormalLandScreen(getContext(), this.mConfiguration));
            }
        }
    }

    private void updateFollowHandSafeMaxHeight(int i3, boolean z2) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null) {
            return;
        }
        if (!z2 || i3 <= 0) {
            restoreFollowHandSafeMaxHeight();
            return;
        }
        int maxHeight = this.mFollowHandOriginalMaxHeightPx;
        if (maxHeight == -1) {
            maxHeight = cOUIPanelPercentFrameLayout.getMaxHeight();
        }
        if (maxHeight > 0) {
            i3 = Math.min(maxHeight, i3);
        }
        if (this.mFollowHandSafeMaxHeightPx != -1 || this.mDesignBottomSheetFrameLayout.getMeasuredHeight() > i3) {
            if (this.mFollowHandOriginalMaxHeightPx == -1) {
                this.mFollowHandOriginalMaxHeightPx = maxHeight;
            }
            this.mFollowHandSafeMaxHeightPx = i3;
            this.mDesignBottomSheetFrameLayout.setMaxHeight(i3);
        }
    }

    private void updateListeningForBackCallbacks() {
        if (this.mCancelable) {
            startListeningForBackCallbacks(this.mDesignBottomSheetFrameLayout);
        } else {
            stopListeningForBackCallbacks(this.mDesignBottomSheetFrameLayout);
        }
    }

    private void updatePaddingBottom() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            this.mPanelPaddingBottom = cOUIPanelPercentFrameLayout.getRatio() == 2.0f ? 0 : this.mDefaultPaddingBottom;
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout2 = this.mDesignBottomSheetFrameLayout;
            cOUIPanelPercentFrameLayout2.setPaddingRelative(cOUIPanelPercentFrameLayout2.getPaddingStart(), this.mDesignBottomSheetFrameLayout.getPaddingTop(), this.mDesignBottomSheetFrameLayout.getPaddingEnd(), this.mPanelPaddingBottom);
            ((COUIBottomSheetBehavior) getBehavior()).setPanelPaddingBottom(this.mPanelPaddingBottom);
        }
    }

    private void updatePanelMarginBottom(Configuration configuration, WindowInsets windowInsets) {
        if (windowInsets == null || configuration == null || this.mDesignBottomSheetFrameLayout == null) {
            return;
        }
        int panelMarginBottom = COUIPanelMultiWindowUtils.getPanelMarginBottom(getContext(), configuration, windowInsets, this.mIsHandlePanel, this.mCouiPanelEdgeToEdgeEnable);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) this.mDesignBottomSheetFrameLayout.getLayoutParams();
        if (((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin != panelMarginBottom) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = panelMarginBottom;
        }
    }

    public boolean canPullUp() {
        return this.mCanPullUp;
    }

    public void delPreferWidth() {
        restoreScreenWidth();
        this.mPreferWidth = -1;
        this.mOriginWidth = -1;
        Log.d(TAG, "delPreferWidth");
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        stopFeedbackAnimation();
        dismiss(true);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        COUIPanelContentLayout cOUIPanelContentLayout;
        int action = motionEvent.getAction();
        if ((action == 1 || action == 3) && (cOUIPanelContentLayout = this.mDraggableConstraintLayout) != null && cOUIPanelContentLayout.mIsTurnOnAnim) {
            cOUIPanelContentLayout.mIsTurnOnAnim = false;
            cOUIPanelContentLayout.dragBgEndAnim();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void doBlurInternal() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null) {
            Log.e(TAG, "doBlurInternal mDesignBottomSheetFrameLayout == null");
            return;
        }
        cOUIPanelPercentFrameLayout.setUseBackgroundBlur(this.mBackgroundBlurBuilder.useBackgroundBlur());
        if (this.mBackgroundBlurBuilder.useBackgroundBlur()) {
            this.mBackgroundBlurBuilder.setTargetView(this.mDesignBottomSheetFrameLayout);
            applyBlurParams();
            if (COUIVersionUtil.getOSVersionCode() <= 37) {
                this.mBackgroundBlurBuilder.setSmoothWeight(COUIContextUtil.getAttrFloat(getContext(), com.support.appcompat.R.attr.couiRoundCornerXXXXLWeight));
            } else {
                this.mBackgroundBlurBuilder.setSmoothWeight(getContext().getResources().getFloat(com.support.appcompat.R.dimen.weight_16_1_large));
            }
            refresBlurCorner();
            this.mBackgroundBlurBuilder.applyBlurBackground();
            this.mBackgroundBlurBuilder.setOriginDrawable(this.mPanelBackground);
        }
    }

    public COUIPanelAdjustResizeHelper getAdjustResizeHelper() {
        if (this.mAdjustResizeHelper == null) {
            this.mAdjustResizeHelper = new COUIPanelAdjustResizeHelper();
        }
        return this.mAdjustResizeHelper;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog
    public BottomSheetBehavior<FrameLayout> getBehavior() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null && this.mFollowHandDetachedBottomSheetBehavior != null) {
            ViewGroup.LayoutParams layoutParams = cOUIPanelPercentFrameLayout.getLayoutParams();
            if ((layoutParams instanceof CoordinatorLayout.LayoutParams) && ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior() == null) {
                return this.mFollowHandDetachedBottomSheetBehavior;
            }
        }
        return super.getBehavior();
    }

    public boolean getCanPerformHapticFeedback() {
        return this.mCanPerformHapticFeedback;
    }

    public Button getCenterButton() {
        if (getWindow() != null) {
            return (Button) getWindow().findViewById(android.R.id.button3);
        }
        return null;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public int getDialogHeight() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            return cOUIPanelPercentFrameLayout.getHeight();
        }
        return 0;
    }

    public int getDialogMaxHeight() {
        View view = this.mCoordinatorLayout;
        if (view != null) {
            return view.getMeasuredHeight();
        }
        return 0;
    }

    public COUIPanelContentLayout getDragableLinearLayout() {
        return this.mDraggableConstraintLayout;
    }

    public boolean getIsHandlePanel() {
        return this.mIsHandlePanel;
    }

    public boolean getIsInWindowFloatingMode() {
        return this.mIsInWindowFloatingMode;
    }

    public Button getLeftButton() {
        if (getWindow() != null) {
            return (Button) getWindow().findViewById(android.R.id.button2);
        }
        return null;
    }

    public float getOutsideViewAlpha(float f3) {
        return !this.mIsInTinyScreen ? f3 : Math.max(0.0f, f3 - 0.5f) * 2.0f;
    }

    public int getPeekHeight() {
        return this.mPeekHeight;
    }

    public Button getRightButton() {
        if (getWindow() != null) {
            return (Button) getWindow().findViewById(android.R.id.button1);
        }
        return null;
    }

    @Override // android.app.Dialog
    public void hide() {
        COUIPanelContentLayout cOUIPanelContentLayout;
        if (!this.mIsShowInDialogFragment || (cOUIPanelContentLayout = this.mDraggableConstraintLayout) == null || cOUIPanelContentLayout.findFocus() == null) {
            super.hide();
        }
    }

    public void hideDragView() {
        COUIPanelBarView cOUIPanelBarView = this.mPanelBarView;
        if (cOUIPanelBarView != null) {
            cOUIPanelBarView.setVisibility(4);
        }
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout == null || cOUIPanelContentLayout.getDrawLayout() == null) {
            return;
        }
        setHideDragViewHeight();
        this.mDraggableConstraintLayout.getDrawLayout().setVisibility(4);
        if (this.mDraggableConstraintLayout.getDragBgView() != null) {
            this.mDraggableConstraintLayout.getDragBgView().setVisibility(8);
        }
    }

    public boolean isFirstShowCollapsed() {
        return this.mFirstShowCollapsed;
    }

    public boolean isPanelHeightChangeAnimRunning() {
        return ((COUIBottomSheetBehavior) getBehavior()).isPanelHeightChangeAnimRunning();
    }

    public boolean isSkipCollapsed() {
        return this.mSkipCollapsed;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z2, float f3, float f4) {
        this.mIsAppearSpringAnimStared = false;
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null && this.mSnapStartBottom != -1) {
            cOUIPanelPercentFrameLayout.layout(cOUIPanelPercentFrameLayout.getLeft(), this.mDesignBottomSheetFrameLayout.getTop(), this.mDesignBottomSheetFrameLayout.getRight(), this.mSnapStartBottom);
        }
        this.mSnapStartBottom = -1;
        BottomSheetDialogAnimatorListener bottomSheetDialogAnimatorListener = this.mBottomSheetDialogAnimatorListener;
        if (bottomSheetDialogAnimatorListener != null) {
            bottomSheetDialogAnimatorListener.onBottomSheetDialogExpanded();
        }
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
    public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f3, float f4) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout == null || this.mSnapStartBottom == -1) {
            return;
        }
        if (f3 < 0.0f) {
            cOUIPanelPercentFrameLayout.layout(cOUIPanelPercentFrameLayout.getLeft(), this.mDesignBottomSheetFrameLayout.getTop(), this.mDesignBottomSheetFrameLayout.getRight(), (int) (this.mSnapStartBottom - f3));
        }
        this.mDesignBottomSheetFrameLayout.setTranslationY(Math.round(f3));
        if (!this.mIsInterruptingAnim) {
            this.mCurrentParentViewTranslationY = this.mDesignBottomSheetFrameLayout.getTranslationY();
        }
        this.mIsInterruptingAnim = false;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        enforceChangeScreenWidth();
        refreshParams();
        resetWindowImeAnimFlags();
        if (!this.mSkipStatusBarTransparentAndFont) {
            setStatusBarTransparentAndFont(getWindow());
        }
        registerPreDrawListener();
        registerApplicationConfigChangeListener();
        registerBehaviorPullUpListener();
        registerPullUpToDismissPanelListener();
        initWindowInsetsListener();
        setNavigation();
        if (this.mDesignBottomSheetFrameLayout != null) {
            updateListeningForBackCallbacks();
        }
        Configuration configuration = getContext().getResources().getConfiguration();
        this.mConfiguration = configuration;
        if (!this.mIsExecutingDismissAnim) {
            updatePanelMarginBottom(configuration, this.mApplyWindowInsets);
        }
        doBlur();
        updateBottomSheetDialogAlphaView();
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
    }

    @Override // androidx.activity.ComponentDialog, android.app.Dialog
    public void onBackPressed() {
        WeakReference<Activity> weakReference = this.mActivityWeakReference;
        if (weakReference != null && weakReference.get() != null && this.mIsExecutingDismissAnim) {
            this.mActivityWeakReference.get().onBackPressed();
        }
        super.onBackPressed();
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, androidx.appcompat.app.AppCompatDialog, androidx.activity.ComponentDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mConfiguration = getContext().getResources().getConfiguration();
        int identifier = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            this.mStatusBarHeight = getContext().getResources().getDimensionPixelSize(identifier);
        }
        if (this.mIsInTinyScreen) {
            initPhysics();
        }
        initBehavior();
        initWindow();
        initDraggableConstraintLayoutSize();
        if (this.mFrameRate && COUIVersionUtil.checkOPlusViewSubSDK(34, 10)) {
            this.mADFRFeatureType = DynamicFrameRateManager.getDynamicFrameRateType();
            this.mIsVSdk = true;
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        releaseResizeHelper();
        releaseApplyWindowInsetsListener();
        cancelAnim(this.mPanelViewTranslationAnimationSet);
        cancelAnim(this.mTranslationAndScaleSpringAnimation);
        cancelAnim(this.mAlphaSpringAnimation);
        releaseApplicationConfigChangeListener();
        releaseBehaviorPullUpListener();
        releasePullUpToDismissPanelListener();
        restoreScreenWidth();
        setShadowEdgeDrawableCallback(null);
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        super.onDetachedFromWindow();
        this.mBackgroundBlurBuilder.release();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        StringBuilder sb = new StringBuilder();
        sb.append("COUIBottomSheetDialog: strokeEnabled=");
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        sb.append(cOUIShadowEdgeDrawable != null && cOUIShadowEdgeDrawable.getMDrawableEnabled());
        return sb.toString();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            this.mStokeEnable = z2;
            setStrokeEffectEnable();
        }
    }

    @Override // android.app.Dialog
    public void onRestoreInstanceState(Bundle bundle) {
        this.mFocusChange = Boolean.valueOf(bundle.getBoolean(STATE_FOCUS_CHANGES, getFocusChange()));
        this.mLastStaticState = bundle.getInt(STATE_LAST_STATIC_CHANGES, 3);
        super.onRestoreInstanceState(bundle);
    }

    @Override // androidx.activity.ComponentDialog, android.app.Dialog
    public Bundle onSaveInstanceState() {
        Bundle bundleOnSaveInstanceState = super.onSaveInstanceState();
        bundleOnSaveInstanceState.putBoolean(STATE_FOCUS_CHANGES, getFocusChange());
        bundleOnSaveInstanceState.putInt(STATE_LAST_STATIC_CHANGES, this.mLastStaticState);
        return bundleOnSaveInstanceState;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z2) {
        if (z2) {
            setFocusChangeFalseIfHasnotEdittext();
        }
        super.onWindowFocusChanged(z2);
    }

    public void refresh() {
        if (this.mDraggableConstraintLayout == null || this.mDesignBottomSheetFrameLayout == null) {
            Log.e(TAG, "refresh mDraggableConstraintLayout == null || mDesignBottomSheetFrameLayout == null");
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, com.support.panel.R.styleable.COUIBottomSheetDialog, 0, com.support.panel.R.style.DefaultBottomSheetDialog);
        this.mPanelDragViewDrawable = getTypedArrayDrawable(typedArrayObtainStyledAttributes, com.support.panel.R.styleable.COUIBottomSheetDialog_panelDragViewIcon, com.support.panel.R.drawable.coui_panel_drag_view);
        this.mPanelDragViewDrawableTintColor = typedArrayObtainStyledAttributes.getColor(com.support.panel.R.styleable.COUIBottomSheetDialog_panelDragViewTintColor, COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorControls));
        this.mPanelBackground = getTypedArrayDrawable(typedArrayObtainStyledAttributes, com.support.panel.R.styleable.COUIBottomSheetDialog_panelBackground, com.support.panel.R.drawable.coui_default_panel_bg_without_shadow);
        this.mPanelBackgroundTintColor = typedArrayObtainStyledAttributes.getColor(com.support.panel.R.styleable.COUIBottomSheetDialog_panelBackgroundTintColor, COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorSurface));
        typedArrayObtainStyledAttributes.recycle();
        Drawable drawable = this.mPanelDragViewDrawable;
        if (drawable != null && this.mDraggableConstraintLayout != null) {
            drawable.setTint(this.mPanelDragViewDrawableTintColor);
            this.mDraggableConstraintLayout.setDragViewDrawable(this.mPanelDragViewDrawable);
            this.mDraggableConstraintLayout.refresh();
        }
        if (this.mPanelBackground != null) {
            if (getWindow() != null && !COUINavigationBarUtil.isGestureNavigation(getContext())) {
                setNavigationBarColor(getNavColor());
            }
            this.mPanelBackground.setTint(this.mPanelBackgroundTintColor);
            if (isBluring()) {
                applyBlurParams();
                if (this.mDesignBottomSheetFrameLayout.isAttachedToWindow()) {
                    doBlurInternal();
                }
            } else {
                COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
                if (cOUIPanelPercentFrameLayout != null) {
                    cOUIPanelPercentFrameLayout.setBackground(this.mPanelBackground);
                }
                COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
                if (cOUIPanelContentLayout != null) {
                    cOUIPanelContentLayout.setBackground(this.mIsShowInDialogFragment ? this.mPanelBackground : null);
                }
            }
        }
        this.mBackgroundBlurBuilder.setOriginDrawable(this.mPanelBackground);
        if (this.mShadowEdgeDrawable != null) {
            applyShadowEdgeParams();
        }
    }

    public void setAnchorView(View view) {
        if (view != null) {
            this.mAnchorView = view;
            clearFollowHandRecordedPlacement();
            getBehavior().setDraggable(false);
        }
    }

    public void setAnimationListener(OnAnimationListener onAnimationListener) {
        this.mAnimationListener = onAnimationListener;
    }

    public void setBackEventPassThroughWhileDismissing(boolean z2) {
        this.mBackEventPassThroughWhileDismissing = z2;
        if (z2 && this.mIsExecutingDismissAnim) {
            enableBackEventPassThroughForDismiss();
        } else {
            if (z2) {
                return;
            }
            restoreWindowFocusAfterDismiss();
        }
    }

    public void setBackgroundBlur(boolean z2, AnimLevel animLevel) {
        setBackgroundBlur(z2, animLevel, false);
    }

    public void setBlurryStateChangeListener(COUIBackgroundBlurBuilder.OnBlurryStateChangeListener onBlurryStateChangeListener) {
        this.mBlurryStateChangeListener = onBlurryStateChangeListener;
        onBlurryStateChangeListener.onBlurryStateChanged(isBluring());
    }

    public void setBottomButtonBar(boolean z2, String str, View.OnClickListener onClickListener, String str2, View.OnClickListener onClickListener2, String str3, View.OnClickListener onClickListener3) {
        ensureDraggableContentLayout();
        this.mDraggableConstraintLayout.setUpBottomBar(z2, str, onClickListener, str2, onClickListener2, str3, onClickListener3);
    }

    public void setBottomSheetDialogAnimatorListener(BottomSheetDialogAnimatorListener bottomSheetDialogAnimatorListener) {
        this.mBottomSheetDialogAnimatorListener = bottomSheetDialogAnimatorListener;
    }

    public void setCanPerformHapticFeedback(boolean z2) {
        this.mCanPerformHapticFeedback = z2;
    }

    public void setCanPullUp(boolean z2) {
        if (this.mCanPullUp != z2) {
            this.mCanPullUp = z2;
            if (getBehavior() instanceof COUIBottomSheetBehavior) {
                this.mPanelPullUpListener = this.mCanPullUp ? getPanelPullUpListener() : null;
                ((COUIBottomSheetBehavior) getBehavior()).setPullUpListener(this.mPanelPullUpListener);
            }
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, android.app.Dialog
    public void setCancelable(boolean z2) {
        super.setCancelable(z2);
        if (this.mCancelable != z2) {
            this.mCancelable = z2;
            if (this.mDesignBottomSheetFrameLayout == null || getWindow() == null) {
                return;
            }
            updateListeningForBackCallbacks();
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z2) {
        super.setCanceledOnTouchOutside(z2);
        if (z2 && !this.mCancelable) {
            this.mCancelable = true;
            if (this.mDesignBottomSheetFrameLayout != null && getWindow() != null) {
                startListeningForBackCallbacks(this.mDesignBottomSheetFrameLayout);
            }
        }
        this.mCanceledOnTouchOutside = z2;
    }

    public void setCenterButton(String str, View.OnClickListener onClickListener) {
        ensureDraggableContentLayout();
        this.mDraggableConstraintLayout.setCenterButton(str, onClickListener);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, androidx.appcompat.app.AppCompatDialog, androidx.activity.ComponentDialog, android.app.Dialog
    public void setContentView(int i3) {
        setContentView(getLayoutInflater().inflate(i3, (ViewGroup) null));
    }

    public void setCouiPanelEdgeToEdgeEnable(boolean z2) {
        if (this.mCouiPanelEdgeToEdgeEnable != z2) {
            this.mCouiPanelEdgeToEdgeEnable = z2;
            initPeekHeight();
            getAdjustResizeHelper().setCouiPanelEdgeToEdgeEnable(this.mCouiPanelEdgeToEdgeEnable);
        }
    }

    public void setDialogOffsetListener(DialogOffsetListener dialogOffsetListener) {
        this.mDialogOffsetListener = dialogOffsetListener;
    }

    public void setDisableSubExpand(boolean z2) {
        this.mDisableSubExpand = z2;
    }

    public void setDragableLinearLayout(COUIPanelContentLayout cOUIPanelContentLayout) {
        setDragableLinearLayout(cOUIPanelContentLayout, false);
    }

    public void setDraggable(boolean z2) {
        if (this.mIsDraggable != z2) {
            this.mIsDraggable = z2;
            getBehavior().setDraggable(this.mIsDraggable);
        }
    }

    @Deprecated
    public void setExecuteNavColorAnimAfterDismiss(boolean z2) {
        this.mIsExecuteNavColorAnimAfterDismiss = z2;
    }

    @Deprecated
    public void setFinalNavColorAfterDismiss(int i3) {
        this.mFinalNavColorAfterDismiss = i3;
    }

    public void setFirstShowCollapsed(boolean z2) {
        this.mFirstShowCollapsed = z2;
    }

    public void setFollowHandPlacementAllowedFlags(int i3) {
        int iSanitizeFollowHandPlacementFlags = sanitizeFollowHandPlacementFlags(i3);
        if (DEBUG) {
            Log.d(TAG, "[follow_hand_position] setAllowedFlags: requested=" + i3 + ", sanitized=" + iSanitizeFollowHandPlacementFlags + ", previous=" + this.mFollowHandPlacementAllowedFlags);
        }
        if (this.mFollowHandPlacementAllowedFlags != iSanitizeFollowHandPlacementFlags) {
            restoreFollowHandSafeMaxHeight();
            this.mFollowHandPlacementAllowedFlags = iSanitizeFollowHandPlacementFlags;
            clearFollowHandRecordedPlacement();
        }
    }

    public void setFollowHandUseAnchorActualDisplayRect(boolean z2) {
        if (this.mFollowHandUseAnchorActualDisplayRect != z2) {
            this.mFollowHandUseAnchorActualDisplayRect = z2;
            clearFollowHandRecordedPlacement();
        }
    }

    public void setFollowHandVerticalEdgeWhenInsufficientSpace(boolean z2) {
        this.mFollowHandFallbackVerticalEdgeWhenInsufficientSpace = z2;
    }

    public void setFollowWindowChange(boolean z2) {
        this.mFocusChange = Boolean.valueOf(z2);
    }

    public void setGlobalDrag(boolean z2) {
        this.mGlobalDrag = z2;
    }

    public void setHandleViewHasPressAnim(boolean z2) {
        if (this.mHandleViewHasPressAnim != z2) {
            this.mHandleViewHasPressAnim = z2;
            COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
            if (cOUIPanelContentLayout == null) {
                return;
            }
            if (z2) {
                cOUIPanelContentLayout.setDragViewPressAnim(true);
            } else {
                cOUIPanelContentLayout.removeDragViewPressAnim();
            }
        }
    }

    public void setHeight(int i3) {
        this.mPanelHeight = i3;
        setPanelHeight();
    }

    public void setHeightChangeAnim(boolean z2) {
        ((COUIBottomSheetBehavior) getBehavior()).setHeightChangeAnim(z2);
    }

    public void setHideDragViewHeight(int i3) {
        COUIPanelContentLayout cOUIPanelContentLayout;
        this.mHideDragViewHeight = i3;
        if (this.mIsHandlePanel || (cOUIPanelContentLayout = this.mDraggableConstraintLayout) == null || cOUIPanelContentLayout.getDrawLayout() == null) {
            return;
        }
        setHideDragViewHeight();
    }

    public void setIsHandlePanel(boolean z2) {
        if (this.mIsHandlePanel != z2) {
            this.mIsHandlePanel = z2;
            if (this.mDraggableConstraintLayout == null) {
                return;
            }
            if (z2) {
                showDragView();
            } else {
                hideDragView();
            }
        }
    }

    public void setIsInTinyScreen(boolean z2, boolean z3) {
        this.mIsInTinyScreen = z2;
        this.mIsFullScreenInTinyScreen = z3;
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            cOUIPanelPercentFrameLayout.setIsInTinyScreen(z2);
        }
    }

    public void setIsInWindowFloatingMode(boolean z2) {
        this.mIsInWindowFloatingMode = z2;
    }

    public void setIsNeedOutsideViewAnim(boolean z2) {
        this.mIsNeedOutsideViewAnim = z2;
    }

    public void setIsShowInMaxHeight(boolean z2) {
        this.mIsShowInMaxHeight = z2;
        int i3 = z2 ? -1 : -2;
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout != null) {
            cOUIPanelContentLayout.setLayoutAtMaxHeight(z2);
        }
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
        if (cOUIPanelPercentFrameLayout != null) {
            ViewGroup.LayoutParams layoutParams = cOUIPanelPercentFrameLayout.getLayoutParams();
            layoutParams.height = i3;
            this.mDesignBottomSheetFrameLayout.setLayoutParams(layoutParams);
            ((COUIBottomSheetBehavior) getBehavior()).setLayoutAtMaxHeight(this.mIsShowInMaxHeight);
        }
    }

    public void setLeftButton(String str, View.OnClickListener onClickListener) {
        ensureDraggableContentLayout();
        this.mDraggableConstraintLayout.setLeftButton(str, onClickListener);
    }

    public void setNavColor(int i3) {
        this.mNavColor = i3;
        if (getWindow() != null) {
            setNavigationBarColor(getNavColor());
        }
    }

    public void setNavigationBarIconFollowPanel() {
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            this.mManualAppearanceLightNavigationBars = true;
            this.mAppearanceLightNavigationBars = true ^ COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
        }
    }

    public void setOnBackInvokedLocalListener(OnBackInvokedLocalListener onBackInvokedLocalListener) {
        this.mOnBackInvokedLocalListener = onBackInvokedLocalListener;
    }

    public void setOnPanelHeightChangeAnimListener(COUIBottomSheetBehavior.OnPanelHeightChangeAnimListener onPanelHeightChangeAnimListener) {
        ((COUIBottomSheetBehavior) getBehavior()).setOnPanelHeightChangeAnimListener(onPanelHeightChangeAnimListener);
    }

    public void setOutSideViewTouchListener(View.OnTouchListener onTouchListener) {
        if (this.mOutsideView == null) {
            this.mOutsideView = findViewById(com.support.panel.R.id.panel_outside);
        }
        this.mOutSideViewTouchListener = onTouchListener;
        View view = this.mOutsideView;
        if (view != null) {
            view.setOnTouchListener(onTouchListener);
        }
    }

    public void setOutsideMaskColor(int i3) {
        this.mColorMask = i3;
        View view = this.mOutsideView;
        if (view != null) {
            view.setBackgroundColor(i3);
        }
    }

    public void setPanelBackground(Drawable drawable) {
        if (this.mDesignBottomSheetFrameLayout == null || drawable == null || this.mPanelBackground == drawable) {
            return;
        }
        this.mPanelBackground = drawable;
        if (this.mBackgroundBlurBuilder.useBackgroundBlur()) {
            this.mBackgroundBlurBuilder.setOriginDrawable(this.mPanelBackground);
            return;
        }
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout != null) {
            cOUIPanelContentLayout.setBackground(this.mIsShowInDialogFragment ? this.mPanelBackground : null);
        }
        this.mDesignBottomSheetFrameLayout.setBackground(this.mPanelBackground);
    }

    public void setPanelBackgroundTintColor(int i3) {
        if (this.mDesignBottomSheetFrameLayout == null || this.mPanelBackground == null || this.mPanelBackgroundTintColor == i3) {
            return;
        }
        this.mPanelBackgroundTintColor = i3;
        if (getWindow() != null && !COUINavigationBarUtil.isGestureNavigation(getContext())) {
            setNavigationBarColor(getNavColor());
        }
        this.mPanelBackground.setTint(this.mPanelBackgroundTintColor);
        if (this.mBackgroundBlurBuilder.useBackgroundBlur()) {
            this.mBackgroundBlurBuilder.setOriginDrawable(this.mPanelBackground);
            return;
        }
        this.mDesignBottomSheetFrameLayout.setBackground(this.mPanelBackground);
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout != null) {
            cOUIPanelContentLayout.setBackground(this.mIsShowInDialogFragment ? this.mPanelBackground : null);
        }
    }

    public void setPanelBarViewColor(int i3) {
        COUIPanelBarView cOUIPanelBarView = this.mPanelBarView;
        if (cOUIPanelBarView != null) {
            cOUIPanelBarView.setBarColor(i3);
        }
    }

    public void setPanelDismissTranslateDuration(float f3) {
        this.mTranslateHidingDuration = f3;
    }

    public void setPanelDragViewDrawable(Drawable drawable) {
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout == null || drawable == null || this.mPanelDragViewDrawable == drawable) {
            return;
        }
        this.mPanelDragViewDrawable = drawable;
        cOUIPanelContentLayout.setDragViewDrawable(drawable);
    }

    public void setPanelDragViewDrawableTintColor(int i3) {
        Drawable drawable;
        if (this.mDraggableConstraintLayout == null || (drawable = this.mPanelDragViewDrawable) == null || this.mPanelDragViewDrawableTintColor == i3) {
            return;
        }
        this.mPanelDragViewDrawableTintColor = i3;
        drawable.setTint(i3);
        this.mDraggableConstraintLayout.setDragViewDrawable(this.mPanelDragViewDrawable);
    }

    public void setPeekHeight(int i3) {
        this.mPeekHeight = i3;
    }

    public void setPhysicsParams(float f3, float f4) {
        this.mAppearStiffness = f3;
        this.mAppearDampingRatio = f4;
    }

    public void setPreferWidth(int i3) {
        this.mPreferWidth = i3;
        Log.d(TAG, "setPreferWidth =：" + this.mPreferWidth);
    }

    public void setRegisterConfigurationChangeCallBack(boolean z2) {
        this.mRegisterConfigurationChangeCallBack = z2;
    }

    public void setRightButton(String str, View.OnClickListener onClickListener) {
        ensureDraggableContentLayout();
        this.mDraggableConstraintLayout.setRightButton(str, onClickListener);
    }

    public void setShouldRegisterWindowInsetsListener(boolean z2) {
        this.mShouldRegisterWindowInsetsListener = z2;
    }

    public void setShowInDialogFragment(boolean z2) {
        this.mIsShowInDialogFragment = z2;
    }

    public void setSkipCollapsed(boolean z2) {
        this.mSkipCollapsed = z2;
    }

    public void setSkipStatusBarTransparentAndFont(boolean z2) {
        this.mSkipStatusBarTransparentAndFont = z2;
    }

    public void setSupportExitBlockingAnimation(boolean z2) {
        if (!COUIVersionUtil.checkOPlusViewSubSDK(34, 10) || this.mSupportExitBlockingAnimation == z2) {
            return;
        }
        this.mSupportExitBlockingAnimation = z2;
        operateBlockingAnimation();
    }

    public void setUseNormalSmoothCorner(boolean z2) {
        if (RoundCornerUtil.isPathSupportSingleCorner() && RoundCornerUtil.isSmoothRoundRectOn() && COUIVersionUtil.getOSVersionCode() > 37) {
            this.mUseNormalSmoothCorner = z2;
        }
    }

    public void setWidth(int i3) {
        this.mPanelWidth = i3;
        setPanelWidth();
    }

    @Override // android.app.Dialog
    public void show() {
        restoreWindowFocusAfterDismiss();
        if (isShowing() && this.mIsExecutingDismissAnim && this.mSupportExitBlockingAnimation) {
            doParentViewTranslationShowingAnim(0, getPanelShowAnimListener());
        } else {
            applyAppearanceLightNavigationBars();
            super.show();
        }
    }

    public void showDragView() {
        COUIPanelBarView cOUIPanelBarView = this.mPanelBarView;
        if (cOUIPanelBarView != null) {
            cOUIPanelBarView.setVisibility(0);
        }
        COUIPanelContentLayout cOUIPanelContentLayout = this.mDraggableConstraintLayout;
        if (cOUIPanelContentLayout == null || cOUIPanelContentLayout.getDrawLayout() == null) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mDraggableConstraintLayout.getDrawLayout().getLayoutParams();
        marginLayoutParams.height = getContext().getResources().getDimensionPixelSize(com.support.panel.R.dimen.coui_panel_drag_view_height);
        marginLayoutParams.topMargin = getContext().getResources().getDimensionPixelSize(com.support.panel.R.dimen.coui_panel_drag_view_shadow_margin_top);
        this.mDraggableConstraintLayout.getDrawLayout().setLayoutParams(marginLayoutParams);
        this.mDraggableConstraintLayout.getDrawLayout().setVisibility(0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T typeCasting(Class<T> cls, Object obj) {
        if (obj == 0 || !cls.isInstance(obj)) {
            return null;
        }
        return obj;
    }

    public boolean updateFollowHandPanelLocation() {
        if (this.mDesignBottomSheetFrameLayout == null) {
            Log.e(TAG, "update follow hand panel while config change error.");
            return false;
        }
        boolean zIsFollowHand = isFollowHand();
        this.mDesignBottomSheetFrameLayout.setHasAnchor(zIsFollowHand);
        if (!zIsFollowHand) {
            clearFollowHandRecordedPlacement();
            restoreFollowHandSafeMaxHeight();
        }
        boolean zHaveEnoughSpace = haveEnoughSpace();
        if (zIsFollowHand && zHaveEnoughSpace) {
            View view = this.mOutsideView;
            if (view != null) {
                view.setAlpha(0.0f);
            }
            this.mCurrentOutsideAlpha = 0.0f;
            invalidateFollowHandPosition();
            applyFollowHandTransparentNavigationBar();
        } else if (!zIsFollowHand) {
            updateBottomSheetCenterVertical();
            this.mDesignBottomSheetFrameLayout.setTranslationY(0.0f);
            this.mDesignBottomSheetFrameLayout.setTranslationX(0.0f);
        } else if (this.mFollowHandFallbackVerticalEdgeWhenInsufficientSpace) {
            positionFollowHandPanelBottomCenterWhenInsufficientSpace();
        } else {
            centerFollowHandPanelOnScreen();
        }
        if (this.mColorMask == 0) {
            setElevation();
            return true;
        }
        this.mDesignBottomSheetFrameLayout.setElevation(0.0f);
        View view2 = this.mOutsideView;
        if (view2 != null) {
            view2.setAlpha(1.0f);
        }
        this.mCurrentOutsideAlpha = 1.0f;
        return true;
    }

    public void updateLayoutWhileConfigChange(Configuration configuration) {
        refreshColorMask();
        enforceChangeScreenWidth(configuration);
        this.mConfiguration = configuration;
        refreshFollowHandScreenEdgeMarginPx();
        this.mIsGestureNavigation = COUINavigationBarUtil.isGestureNavigation(getContext()) || UIUtil.isInFreeFormModeWindowMode(getContext());
        getAdjustResizeHelper().resetInnerStatus();
        if (this.mDesignBottomSheetFrameLayout != null) {
            restoreFollowHandSafeMaxHeight();
            largeScreenLimitMaxSize();
            this.mDesignBottomSheetFrameLayout.updateLayoutWhileConfigChange(configuration);
            if (!this.mIsHandlePanel || COUIPanelMultiWindowUtils.isNormalScreen(getContext(), this.mConfiguration)) {
                resetNavigationBarColor();
            }
            setNavigation();
            if (this.mDesignBottomSheetFrameLayout.getRatio() != this.mPanelRatio) {
                if (this.mBackgroundBlurBuilder.useBackgroundBlur()) {
                    refresBlurCorner();
                    this.mBackgroundBlurBuilder.refreshCornerRadius();
                    refreshShadowEdgeDrawableCorner();
                }
                if (this.mDesignBottomSheetFrameLayout.getRatio() == 1.0f && this.mAnchorView != null) {
                    superDismiss();
                }
            }
            this.mPanelRatio = this.mDesignBottomSheetFrameLayout.getRatio();
        }
        updateFitToContents();
        updatePaddingBottom();
        initCoordinateInsets(this.mApplyWindowInsets);
        if (!this.mIsExecutingDismissAnim) {
            updatePanelMarginBottom(this.mConfiguration, this.mApplyWindowInsets);
        }
        clearFollowHandRecordedPlacement();
    }

    public COUIBottomSheetDialog(Context context, boolean z2, DialogInterface.OnCancelListener onCancelListener) {
        this(context, com.support.panel.R.style.DefaultBottomSheetDialog);
        setCancelable(z2);
        setOnCancelListener(onCancelListener);
    }

    public void setBackgroundBlur(boolean z2) {
        setBackgroundBlur(z2, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN, false);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, androidx.appcompat.app.AppCompatDialog, androidx.activity.ComponentDialog, android.app.Dialog
    public void setContentView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("ContentView can't be null");
        }
        COUIThemeOverlay.getInstance().applyThemeOverlays(getContext());
        setContentViewLocal(view);
        initView();
    }

    public void setDragableLinearLayout(COUIPanelContentLayout cOUIPanelContentLayout, boolean z2) {
        this.mDraggableConstraintLayout = cOUIPanelContentLayout;
        if (!this.mIsHandlePanel) {
            hideDragView();
        }
        if (cOUIPanelContentLayout != null) {
            View view = this.mDesignBottomSheetFrameLayout;
            if (view == null) {
                view = (ViewGroup) this.mDraggableConstraintLayout.getParent();
            }
            this.mPulledUpView = view;
            cOUIPanelContentLayout.setLayoutAtMaxHeight(this.mIsShowInMaxHeight);
            if (this.mHandleViewHasPressAnim) {
                cOUIPanelContentLayout.setDragViewPressAnim(true);
            }
            cOUIPanelContentLayout.setDragViewDrawable(this.mPanelDragViewDrawable);
        }
        if (z2) {
            refresh();
        } else if (cOUIPanelContentLayout != null) {
            WindowInsets windowInsets = this.mApplyWindowInsets;
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
            cOUIPanelContentLayout.setNavigationMargin(null, windowInsets, cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getRatio() == 1.0f, this.mCouiPanelEdgeToEdgeEnable);
        }
        initDraggableConstraintLayoutSize();
    }

    private void cancelAnim(COUISpringAnimation cOUISpringAnimation) {
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        cOUISpringAnimation.cancel();
    }

    public void dismiss(boolean z2) {
        if (isShowing() && z2 && !this.mIsExecutingDismissAnim) {
            hideKeyboard();
            if (getBehavior().getState() != 5) {
                dismissWithInterruptibleAnim();
                return;
            }
            return;
        }
        superDismiss();
    }

    public void setBackgroundBlur(boolean z2, boolean z3) {
        setBackgroundBlur(z2, UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN, z3);
    }

    private void setBackgroundBlur(boolean z2, AnimLevel animLevel, boolean z3) {
        if (!z2 || COUIVersionUtil.getOSVersionCode() < 40) {
            return;
        }
        if (UIUtil.confirmLevelAnim(animLevel)) {
            this.mAlwayBlur = z3;
            this.mBackgroundBlurBuilder.setAlwayBlur(z3);
        }
        this.mBackgroundBlurBuilder.setUseBackgroundBlur(z2, animLevel);
    }

    private void setHideDragViewHeight() {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mDraggableConstraintLayout.getDrawLayout().getLayoutParams();
        int i3 = this.mHideDragViewHeight;
        if (i3 > 0) {
            marginLayoutParams.height = i3;
        } else {
            marginLayoutParams.height = getContext().getResources().getDimensionPixelSize(com.support.panel.R.dimen.coui_panel_drag_view_hide_height);
        }
        marginLayoutParams.topMargin = 0;
        this.mDraggableConstraintLayout.getDrawLayout().setLayoutParams(marginLayoutParams);
    }

    public COUIBottomSheetDialog(Context context, int i3, float f3, float f4) {
        this(context, i3);
        this.mPhysicsFrequency = f3;
        this.mPhysicsDampingRatio = f4;
    }

    public COUIBottomSheetDialog(Context context, int i3) {
        super(context, resolveDialogTheme(context, i3));
        this.mTemtRect = new Rect();
        this.mAdjustResizeOnPreDrawListener = new AnonymousClass1();
        this.mHandleViewHasPressAnim = true;
        this.mIsShowInDialogFragment = false;
        this.mCancelable = true;
        this.mCanceledOnTouchOutside = true;
        this.mCanPullUp = true;
        this.mCurrentSpringTotalOffset = 0;
        this.mCoordinatorLayoutMinInsetsTop = 0;
        this.mCoordinatorLayoutPaddingExtra = 0;
        this.mPeekHeight = 0;
        this.mSkipCollapsed = true;
        this.mFirstShowCollapsed = false;
        this.mCurrentParentViewTranslationY = 0.0f;
        this.mCurrentOutsideAlpha = 0.0f;
        this.mIsInterruptingAnim = false;
        this.mWindowInsetsListener = null;
        this.mPanelPullUpListener = null;
        this.mNavColor = Integer.MAX_VALUE;
        this.mWindowInsetsAnimEnable = false;
        this.mIsInWindowFloatingMode = false;
        this.mCanPerformHapticFeedback = false;
        this.mRegisterConfigurationChangeCallBack = true;
        this.mIsNeedShowKeyboard = false;
        this.mIsNeedOutsideViewAnim = true;
        this.mFocusChange = null;
        this.mIsDraggable = true;
        this.mTranslateHidingDuration = DEFAULT_TRANSLATE_HIDING_ANIMATOR_DURATION;
        this.mPanelBarView = null;
        this.mBottomSheetDialogAnimatorListener = null;
        this.mDisableSubExpand = false;
        this.mGlobalDrag = true;
        this.mPhysicsFrequency = Float.MIN_VALUE;
        this.mPhysicsDampingRatio = Float.MIN_VALUE;
        this.mAnchorView = null;
        this.mStatusBarHeight = 0;
        this.mSnapStartBottom = -1;
        this.mAppearStiffness = Float.MIN_VALUE;
        this.mAppearDampingRatio = Float.MIN_VALUE;
        this.mIsAppearSpringAnimStared = false;
        this.mShouldRegisterWindowInsetsListener = true;
        this.mSkipStatusBarTransparentAndFont = false;
        this.mFollowHandCoordinatorOnScreen = new int[2];
        this.mFollowHandRecordedPlacement = 0;
        this.mFollowHandRecordedSideVerticalMode = 0;
        this.mFollowHandRecordedNavigationBottomInsetPx = -1;
        this.mFollowHandSafeMaxHeightPx = -1;
        this.mFollowHandOriginalMaxHeightPx = -1;
        this.mFollowHandAnchorRectInScreen = new Rect();
        this.mFollowHandViewLocationOnScreenTemp = new int[2];
        this.mFollowHandInvalidatePositionOut = new int[5];
        this.mFollowHandPlacementAllowedFlags = 15;
        this.mPreferWidth = -1;
        this.mOriginWidth = -1;
        this.isLargeScreenLimitMaxSize = false;
        this.mIsHandlePanel = false;
        this.mIsGestureNavigation = true;
        this.mHideDragViewHeight = 0;
        this.mFrameRate = true;
        this.mAnimationFlag = 0;
        this.mCurrentOutSideAlphaStateHidden = 0.0f;
        this.mCurrentOutSideAlphaStateShow = 0.0f;
        this.mPullUpToDismissPanelListener = new AnonymousClass2();
        this.mIsAnimationInFirst = false;
        this.mOnAttatchStateChangeListener = null;
        this.mLastStaticState = 3;
        this.mPanelRatio = 1.0f;
        this.mStokeEnable = false;
        this.mAlwayBlur = false;
        this.mFollowHandCurrentBranch = "unknown";
        this.mManualAppearanceLightNavigationBars = false;
        this.mAppearanceLightNavigationBars = false;
        this.mComponentCallbacks = new AnonymousClass3();
        this.mOnPreDrawListener = new AnonymousClass31();
        COUIBackgroundBlurBuilder cOUIBackgroundBlurBuilder = new COUIBackgroundBlurBuilder(getContext());
        this.mBackgroundBlurBuilder = cOUIBackgroundBlurBuilder;
        cOUIBackgroundBlurBuilder.setOnBlurryStateChangeListener(new AnonymousClass4());
        initThemeResources(i3);
        initValueResources();
        saveActivityContextToGetMultiWindowInfo(context);
        initBlurParams();
        applyBlurParams();
        if (COUIVersionUtil.getOSVersionCode() > 37) {
            this.mCornerRadius = getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R.dimen.coui_round_corner_xxxxl_radius_large_16_1);
        } else {
            this.mCornerRadius = getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R.dimen.coui_round_corner_xxxxl);
        }
    }

    private void setFrameRate(COUISpringAnimation cOUISpringAnimation) {
        if (!this.mIsVSdk || this.mDesignBottomSheetFrameLayout == null) {
            return;
        }
        int i3 = this.mADFRFeatureType;
        if (i3 == 2) {
            cOUISpringAnimation.addUpdateListener(new a(this));
            cOUISpringAnimation.addEndListener(new AnonymousClass25());
        } else if (i3 == 1) {
            COUILog.d(TAG, "COUISpringAnimation LEVEL_LOW_PRECISION onAnimatorStart: DynamicFrameRateManager.LOW_PRECISION_FRAME_RATE");
            DynamicFrameRateManager.setFrameRate(this.mDesignBottomSheetFrameLayout, ANIMATION_TYPE_ID, -1, (Bundle) null);
            cOUISpringAnimation.addEndListener(new AnonymousClass26());
        } else if (i3 == 0) {
            COUILog.d(TAG, "COUISpringAnimation LEVEL_DEFAULT do nothing");
        }
    }

    public void doFeedbackAnimation() {
        if (this.mDesignBottomSheetFrameLayout != null) {
            AnimatorSet animatorSet = this.mPanelViewTranslationAnimationSet;
            if (animatorSet == null || !animatorSet.isRunning()) {
                COUISpringAnimation cOUISpringAnimation = this.mTranslationAndScaleSpringAnimation;
                if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
                    doFeedbackAnimation(this.mDesignBottomSheetFrameLayout);
                }
            }
        }
    }

    private void enforceChangeScreenWidth(Configuration configuration) {
        if (this.mPreferWidth == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            this.mOriginWidth = configuration.screenWidthDp;
            configuration.screenWidthDp = this.mPreferWidth;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(TAG, "enforceChangeScreenWidth : OriginWidth=" + this.mOriginWidth + " ,PreferWidth:" + this.mPreferWidth);
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.mDesignBottomSheetFrameLayout;
            if (cOUIPanelPercentFrameLayout != null) {
                cOUIPanelPercentFrameLayout.setPreferWidth(this.mPreferWidth);
            }
        } catch (Exception unused) {
            Log.d(TAG, "enforceChangeScreenWidth : failed to updateConfiguration");
        }
    }

    public void setFrameRate(boolean z2) {
        this.mFrameRate = z2;
    }
}