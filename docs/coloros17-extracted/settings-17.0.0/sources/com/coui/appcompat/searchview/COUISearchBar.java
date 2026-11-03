package com.coui.appcompat.searchview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.CollapsibleActionView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.COUIMoveEaseInterpolator;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.rippleutil.COUIRippleDrawableUtil;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.searchview.params.IconCollapseToNormalParams;
import com.coui.appcompat.searchview.params.IconExpandToEditParams;
import com.coui.appcompat.searchview.params.StateChangedToEditParams;
import com.coui.appcompat.searchview.params.StateChangedToNormalParams;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.coui.appcompat.toolbar.COUIActionMenuItemView;
import com.coui.appcompat.toolbar.COUIActionMenuView;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.coui.appcompat.toolbar.ToolbarMaterialEffectDelegate;
import com.coui.appcompat.toolbar.ToolbarMaterialEffectResolver;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.google.android.material.appbar.COUICollapsingToolbarLayout;
import com.oplus.graphics.OplusPathAdapter;
import com.oplus.settings.appfunctions.SystemUpdateScreenStateConfig;
import com.oplus.view.material.OplusMaterialMetaBallParams;
import com.support.toolbar.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class COUISearchBar extends ViewGroup implements CollapsibleActionView, ImeInsetsAnimationCallback.OnImeAnimationListener, OnMaterialStateChangeListener {
    private static final float ANIMATE_ALPHA0_RANGE = 0.0f;
    private static final float ANIMATE_ALPHA1_RANGE = 1000.0f;
    public static final float ANIMATE_ICON_COLLAPSE_RANGE = 0.0f;
    public static final float ANIMATE_ICON_EXPAND_RANGE = 1000.0f;
    private static final float ANIMATE_TO_EDIT_RANGE = 1000.0f;
    private static final float ANIMATE_TO_NORMAL_RANGE = 0.0f;
    private static final float BLUR_PROGRESS_MID = 0.5f;
    private static final float BLUR_RADIUS_MAX = 30.0f;
    private static final int BLUR_WINDOW_VIEW_BG_COLOR_DARK = -13421773;
    private static final int BLUR_WINDOW_VIEW_BG_COLOR_IMMERSIVE = 654311423;
    private static final int BLUR_WINDOW_VIEW_BG_COLOR_LIGHT = -986896;
    private static final float BUTTON_SHADOW_FADE_START = 0.2f;
    private static final float CLAMP_ANIMATION_PERCENT = 0.3f;
    private static final float COLLAPSE_TAIL_PROGRESS = 0.05f;
    private static final float CONTENT_SHADOW_FADE_START = 0.8f;
    private static final int DEFAULT_MAX_LINES = 1;
    private static final long DEFAULT_SEARCH_VIEW_OFFSET_CHANGE_DURATION = 450;
    private static final float END_PROGRESS_CANCEL_BUTTON_HIDE = 0.05f;
    private static final int EXTRA_PADDING = 20;
    private static final float FLOAT_TWO = 2.0f;
    public static final int FOLLOW_SYSTEM = -1;
    public static final int FORCE_DISABLE = 0;
    public static final int FORCE_ENABLE = 1;
    private static final float FUNC_BTN_INIT_SCALE = 0.5f;
    private static final float MELT_BALL_PARAM_FIFTH = 2.0f;
    private static final float MELT_BALL_PARAM_FOURTH = 1.5f;
    private static final int MELT_BALL_PARAM_SECOND = 0;
    private static final float MELT_BALL_PARAM_THIRD_DIP = 4.0f;
    private static final int MELT_BALL_VIEW_1 = 1;
    private static final int MELT_BALL_VIEW_2 = 2;
    private static final boolean OLIVELINK_ENABLE = false;
    private static final int RESPONSIVE_WIDTH_TYPE_COMPAT = 0;
    private static final int RESPONSIVE_WIDTH_TYPE_EXPANDED = 2;
    private static final int RESPONSIVE_WIDTH_TYPE_MEDIUM = 1;
    private static final int SHADOW_CLIP_EXPAND_SIZE_PX = 120;
    public static final int STATE_EDIT = 1;
    public static final int STATE_NORMAL = 0;
    private static final String TAG = "COUISearchBar";
    public static final int TYPE_INSTANT_SEARCH = 0;
    public static final int TYPE_NON_INSTANT_SEARCH = 1;
    private static final int WAY_AT_BEHIND = 1;
    private static final int WAY_AT_FRONT = 2;
    private static final int WAY_NONE = 0;
    private static final float Z_OFFSET = 1000.0f;
    private int mAddToToolbarWay;
    private AttributeSet mAttrs;
    private ColorDrawable mBackgroundColor;
    private final Rect mBackgroundRect;
    private ArraySet<View> mBallArraySet;
    private float mButtonOffsetAnimationDistance;
    private StateChangedToEditParams mChangedToEditParams;
    private StateChangedToNormalParams mChangedToNormalParams;
    private int mCollapsedMinHeight;
    private final COUISpringForce mExpandSpringForceX;
    private COUISpringAnimation mFadeInFadeOutAnimation;
    private final FloatValueHolder mFadeInFadeOutValue;
    private final COUISpringForce mFadeInSpringForceX;
    private boolean mFadeInToolbar;
    private boolean mFadeOutToolbar;
    private boolean mFunBtnAnimEnded;
    private final FloatValueHolder mFunBtnAnimationValue;
    private COUISpringAnimation mFunBtnSpringAnimation;
    private final COUISpringForce mFunBtnSpringForce;
    private int mFunctionButtonMaxWidth;
    private int mFunctionButtonStartGap;
    private SearchBarFunctionButton mFunctionalButton;
    private float mFunctionalButtonOffsetX;
    private int mFunctionalButtonStartX;
    private int mFunctionalButtonTargetX;
    private ColorStateList mFunctionalButtonTextColor;
    private float mFunctionalInitScale;
    private float mFunctionalInitScaleExpand;
    private int mGravityInToolBar;
    private boolean mHasAddedToToolbar;
    private boolean mHideFunctionalButton;
    private IconCollapseToNormalParams mIconCollapseToNormalParams;
    private COUISpringAnimation mIconExpandAnimation;
    private final FloatValueHolder mIconExpandCenterAnimationRange;
    private int mIconExpandStartX;
    private int mIconExpandTargetX;
    private IconExpandToEditParams mIconExpandToEditParams;
    private float mIconInitialWidth;
    private int mIconMaxHeight;
    private int mIconMaxWidth;
    private boolean mIconStartCollapse;
    private boolean mIconStartExpand;
    private boolean mIgnoreTitleViewAlpha;
    private int mInitSearchBarHeight;
    private int mInnerIconSize;
    private boolean mInputMethodAnimationEnabled;
    private boolean mIsAtLeastR;
    private boolean mIsCollapseAnimating;
    private boolean mIsDrawableMaterialEffect;
    private boolean mIsExpandAnimating;
    private boolean mIsFadeInAnimating;
    private int mLastOrientation;
    private int mLastScreenWidthDp;
    private int mLastSmallestScreenWidthDp;
    private float mLastStateAnimEndValue;
    private int mMaterialType;
    private MenuItem mMenuItem;
    private int mNavigationButtonEndGap;
    private int mNavigationButtonWidth;
    private SearchBarIconButton mNavigationView;
    private int mNoBlurSolidColor;
    private boolean mNoMaterialInContent;
    private boolean mNoShadowInNormalState;
    private int mNormalBackgroundColor;
    private int mNormalBackgroundHeight;
    private OnAnimationListener mOnAnimationListener;
    private OnSearchBarBackgroundBoundsChangedListener mOnSearchBarBackgroundBoundsChangedListener;
    private List<OnStateChangeListener> mOnStateChangeListeners;
    private boolean mOuterBtnAnimEnded;
    private final FloatValueHolder mOuterBtnAnimationValue;
    private COUISpringAnimation mOuterBtnSpringAnimation;
    private final COUISpringForce mOuterBtnSpringForce;
    private int mOuterButtonGap;
    private float mOuterButtonInitScale;
    private int mOuterButtonWidth;
    private SearchBarIconButton mOuterPrimaryButton;
    private SearchBarIconButton mOuterSecondaryButton;
    private SearchBarParamProvider mParamProvider;
    private int[] mResponsiveHorizontalPadding;
    private int mResponsiveWidthSize;
    private SearchBarBackgroundView mSearchBarBackgroundView;
    private String mSearchBarContentDesc;
    private float mSearchBarFinalWidth;
    private String mSearchBarHintTitleDesc;
    private int mSearchViewBackgroundMaxWidth;
    private int mSearchViewBackgroundMinWidth;
    private int mSearchViewType;
    private int mShowImeAnimDuration;
    private Interpolator mShowImeInterpolator;
    private final AtomicInteger mState;
    private boolean mStateAnimEnded;
    private final COUISpringForce mStateAnimationSpringForceX;
    private final FloatValueHolder mStateAnimationValue;
    private COUISpringAnimation mStateChangedAnimation;
    private boolean mStateToEditAnimation;
    private boolean mStateToNormalAnimation;
    private boolean mStrokeEffectForceDark;
    private int mStyle;
    private boolean mSystemBlurState;
    private boolean mSystemCausticShadowState;
    private boolean mSystemSpotLightState;
    private boolean mSystemStrokeState;
    private COUIToolbar mToolBar;
    private float mToolbarIconScaleEnd;
    private COUISpringAnimation mToolbarIconSpringAnimation;
    private final COUISpringForce mToolbarIconSpringForce;
    private final FloatValueHolder mToolbarIconSpringValue;
    private View mToolbarSearchIcon;
    private boolean mTouchAnimationEnabled;
    private int mUseMaterialEffectAttr;
    private boolean mUseResponsivePadding;
    private static final Interpolator DEFAULT_SEARCH_VIEW_OFFSET_CHANGE_INTERPOLATOR = new COUIMoveEaseInterpolator();
    private static final String[] TYPE_NAME = {"TYPE_INSTANT_SEARCH", "TYPE_NON_INSTANT_SEARCH"};

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchBar$1, reason: invalid class name */
    public class AnonymousClass1 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            float f5 = f3 / 1000.0f;
            if (COUISearchBar.access$000(COUISearchBar.this) != null) {
                COUISearchBar.access$000(COUISearchBar.this).onUpdate(f5);
            }
            if (COUISearchBar.access$100(COUISearchBar.this)) {
                COUISearchBar.this.updateDropShadow(f5);
            }
            COUISearchBar cOUISearchBar = COUISearchBar.this;
            COUISearchBar.access$200(cOUISearchBar, cOUISearchBar, f5);
            float f6 = 1.0f - f5;
            COUISearchBar.access$300(COUISearchBar.this, f6);
            COUISearchBar.access$400(COUISearchBar.this, f6);
        }
    }

    @Deprecated
    public static class COUISavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<COUISavedState> CREATOR = new AnonymousClass1();
        float mCollapsingHeightPercent;

        /* JADX INFO: renamed from: com.coui.appcompat.searchview.COUISearchBar$COUISavedState$1, reason: invalid class name */
        public class AnonymousClass1 implements Parcelable.Creator<COUISavedState> {
            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ COUISavedState createFromParcel(Parcel parcel) {
                return createFromParcel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ COUISavedState[] newArray(int i3) {
                return newArray(i3);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public COUISavedState createFromParcel(Parcel parcel) {
                return new COUISavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public COUISavedState[] newArray(int i3) {
                return new COUISavedState[i3];
            }
        }

        public COUISavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public void readFromParcel(Parcel parcel) {
            this.mCollapsingHeightPercent = parcel.readFloat();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeFloat(this.mCollapsingHeightPercent);
        }

        public COUISavedState(Parcel parcel) {
            super(parcel);
            this.mCollapsingHeightPercent = parcel.readFloat();
        }
    }

    public interface OnAnimationListener {
        void onAnimationEnd(int i3);

        void onAnimationStart(int i3);

        void onUpdate(float f3);

        @Deprecated
        default void onUpdate(int i3, ValueAnimator valueAnimator) {
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public interface OnSearchBarBackgroundBoundsChangedListener {
        void onBackgroundBoundsChanged(int i3, int i4, int i5, int i6);
    }

    public interface OnStateChangeListener {
        void onStateChange(int i3, int i4);
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchViewMaterial {
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchViewState {
    }

    /* JADX INFO: loaded from: classes7.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchViewType {
    }

    public static class SmoothRoundCornerHelper {
        private OplusPathAdapter mPathAdapter;
        private final int mSmoothStyleType;

        public SmoothRoundCornerHelper(Path path) {
            this.mPathAdapter = null;
            int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
            this.mSmoothStyleType = smoothStyleType;
            if (smoothStyleType == 1) {
                this.mPathAdapter = new OplusPathAdapter(path, smoothStyleType);
            }
        }

        public OplusPathAdapter getPathAdapter() {
            return this.mPathAdapter;
        }

        public int getSmoothStyleType() {
            return this.mSmoothStyleType;
        }
    }

    public COUISearchBar(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUISearchBar.mFunBtnAnimEnded = true;
        cOUISearchBar.checkAllStateAnimationsEnded();
    }

    public static /* synthetic */ OnAnimationListener access$000(COUISearchBar cOUISearchBar) {
        return cOUISearchBar.mOnAnimationListener;
    }

    public static /* synthetic */ boolean access$100(COUISearchBar cOUISearchBar) {
        return cOUISearchBar.mNoShadowInNormalState;
    }

    public static /* synthetic */ void access$200(COUISearchBar cOUISearchBar, View view, float f3) {
        cOUISearchBar.setAlpha2Child(view, f3);
    }

    public static /* synthetic */ void access$300(COUISearchBar cOUISearchBar, float f3) {
        cOUISearchBar.setToolBarAlpha(f3);
    }

    public static /* synthetic */ void access$400(COUISearchBar cOUISearchBar, float f3) {
        cOUISearchBar.setToolBarScale(f3);
    }

    private void applyMenuViewAlpha(View view, float f3) {
        ViewGroup viewGroup;
        int childCount;
        if (!(view instanceof ViewGroup) || (childCount = (viewGroup = (ViewGroup) view).getChildCount()) <= 0) {
            view.setAlpha(f3);
            return;
        }
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if (childAt != this && childAt != null && childAt.getVisibility() == 0) {
                childAt.setAlpha(f3);
            }
        }
    }

    private void applyMenuViewScale(View view, float f3) {
        ViewGroup viewGroup;
        int childCount;
        if (!(view instanceof ViewGroup) || (childCount = (viewGroup = (ViewGroup) view).getChildCount()) <= 0) {
            applyToolbarIconExternalScale(null, view, f3);
            return;
        }
        ToolbarMaterialEffectDelegate overflowButtonEffectDelegate = view instanceof COUIActionMenuView ? ((COUIActionMenuView) view).getOverflowButtonEffectDelegate() : null;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if (childAt != this && childAt != null && childAt.getVisibility() == 0) {
                if (childAt instanceof COUIActionMenuItemView) {
                    applyToolbarIconExternalScale(((COUIActionMenuItemView) childAt).getEffectDelegate(), childAt, f3);
                } else if (overflowButtonEffectDelegate == null) {
                    applyToolbarIconExternalScale(null, childAt, f3);
                }
            }
        }
        applyToolbarIconExternalScale(overflowButtonEffectDelegate, null, f3);
    }

    private static void applyToolbarIconExternalScale(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate, View view, float f3) {
        if (toolbarMaterialEffectDelegate != null) {
            toolbarMaterialEffectDelegate.setExternalScale(f3);
        } else if (view != null) {
            view.setScaleX(f3);
            view.setScaleY(f3);
        }
    }

    public static /* synthetic */ void b(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUISearchBar.cancelToolbarIconSpringAnimation();
        if (cOUISearchBar.mState.get() == 1) {
            cOUISearchBar.setToolBarChildVisibility(8);
            cOUISearchBar.onIconExpandAnimationEnd();
            OnAnimationListener onAnimationListener = cOUISearchBar.mOnAnimationListener;
            if (onAnimationListener != null) {
                onAnimationListener.onAnimationEnd(1);
            }
        } else {
            cOUISearchBar.mIsCollapseAnimating = false;
            cOUISearchBar.mIsExpandAnimating = false;
            cOUISearchBar.setToolbarSearchIconAlpha(1.0f);
            cOUISearchBar.onIconCollapseAnimationEnd();
            OnAnimationListener onAnimationListener2 = cOUISearchBar.mOnAnimationListener;
            if (onAnimationListener2 != null) {
                onAnimationListener2.onAnimationEnd(0);
            }
        }
        cOUISearchBar.updateSearchAndButtonTouchDeformation(true);
        cOUISearchBar.clearBlurRenderEffect(cOUISearchBar.mSearchBarBackgroundView);
        cOUISearchBar.clearBlurRenderEffect(cOUISearchBar.mFunctionalButton);
    }

    private void bindCollapseTailPressHook() {
        ToolbarMaterialEffectDelegate effectDelegate;
        View view = this.mToolbarSearchIcon;
        if ((view instanceof COUIActionMenuItemView) && (effectDelegate = ((COUIActionMenuItemView) view).getEffectDelegate()) != null) {
            effectDelegate.setOnPressWhileInvisibleListener(new a(this));
        }
    }

    public static /* synthetic */ void c(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        float f5 = 1.0f - (f3 / 1000.0f);
        cOUISearchBar.setToolBarAlpha(f5);
        cOUISearchBar.setToolBarScale(f5);
        cOUISearchBar.setToolbarSearchIconAlpha(0.0f);
    }

    private void calculateExpandLocation(boolean z2) {
        if (this.mToolbarSearchIcon == null) {
            return;
        }
        float measuredHeight = this.mSearchBarBackgroundView.getMeasuredHeight();
        this.mIconInitialWidth = measuredHeight;
        int[] iArr = new int[2];
        getLayoutLocationInWindow(this.mToolbarSearchIcon, iArr);
        int[] iArr2 = new int[2];
        getLayoutLocationInWindow(this, iArr2);
        float width = iArr[0] + (this.mToolbarSearchIcon.getWidth() / 2.0f);
        this.mSearchBarFinalWidth = this.mSearchBarBackgroundView.getMeasuredWidth();
        ImageView searchIconView = this.mSearchBarBackgroundView.getSearchIconView();
        if (searchIconView != null) {
            int[] iArr3 = new int[2];
            getLayoutLocationInWindow(searchIconView, iArr3);
            this.mIconExpandStartX = (int) (width - (iArr3[0] + (searchIconView.getWidth() / 2.0f)));
        } else {
            this.mIconExpandStartX = iArr[0] - iArr2[0];
        }
        this.mIconExpandTargetX = 0;
        if (z2) {
            this.mSearchBarBackgroundView.startIconExpandClipping(measuredHeight);
        }
        calculateFunBtnPosition(this.mToolbarSearchIcon, iArr, z2);
    }

    private int calculateRelativeTop(int i3, int i4, int i5) {
        return i3 + ((i4 - i5) / 2);
    }

    private void cancelToolbarIconSpringAnimation() {
        COUISpringAnimation cOUISpringAnimation = this.mToolbarIconSpringAnimation;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mToolbarIconSpringAnimation.cancel();
    }

    private void checkAllStateAnimationsEnded() {
        if (this.mStateAnimEnded && this.mFunBtnAnimEnded && this.mOuterBtnAnimEnded) {
            updateButtonTouchDeformation(true);
            clearBlurRenderEffect(this.mSearchBarBackgroundView);
            float f3 = this.mLastStateAnimEndValue;
            if (f3 >= 500.0f) {
                endAnimateToEditState();
                this.mStateToEditAnimation = false;
                this.mStateToNormalAnimation = false;
                OnAnimationListener onAnimationListener = this.mOnAnimationListener;
                if (onAnimationListener != null) {
                    onAnimationListener.onAnimationEnd(1);
                }
            } else if (f3 < 500.0f) {
                endAnimateToNormalState();
                this.mStateToEditAnimation = false;
                this.mStateToNormalAnimation = false;
                OnAnimationListener onAnimationListener2 = this.mOnAnimationListener;
                if (onAnimationListener2 != null) {
                    onAnimationListener2.onAnimationEnd(0);
                }
                resetBallChain();
            }
            this.mSearchBarBackgroundView.doAnimateEnd();
        }
    }

    private void clearAccessibilityFocus(View view) {
        if (view == null || !view.isAccessibilityFocused()) {
            return;
        }
        view.performAccessibilityAction(128, null);
    }

    private void clearBlurRenderEffect(View view) {
        if (view == null) {
            return;
        }
        view.setRenderEffect(null);
    }

    private void clearCausticShadow() {
        clearCausticShadow(this.mNavigationView);
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            clearCausticShadow(searchBarFunctionButton);
        }
        clearCausticShadow(this.mOuterPrimaryButton);
        clearCausticShadow(this.mOuterSecondaryButton);
        clearCausticShadow(this.mSearchBarBackgroundView);
    }

    private void clearCollapseTailPressHook(View view) {
        ToolbarMaterialEffectDelegate effectDelegate;
        if ((view instanceof COUIActionMenuItemView) && (effectDelegate = ((COUIActionMenuItemView) view).getEffectDelegate()) != null) {
            effectDelegate.setOnPressWhileInvisibleListener(null);
        }
    }

    private void configImageViewDrawable(ImageView imageView, Drawable drawable, int i3) {
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
            imageView.setClickable(true);
            if (drawable != null) {
                int intrinsicWidth = (i3 - drawable.getIntrinsicWidth()) / 2;
                imageView.setPadding(intrinsicWidth, 0, intrinsicWidth, 0);
            }
        }
    }

    private void configResponsive() {
        if (COUIResponsiveUtils.isSmallScreen(getContext(), getMeasuredWidth())) {
            this.mResponsiveWidthSize = 0;
        } else if (COUIResponsiveUtils.isMediumScreen(getContext(), getMeasuredWidth(), UIUtil.getScreenHeightMetrics(getContext()))) {
            this.mResponsiveWidthSize = 1;
        } else if (COUIResponsiveUtils.isLargeScreen(getContext(), getMeasuredWidth(), UIUtil.getScreenHeightMetrics(getContext()))) {
            this.mResponsiveWidthSize = 2;
        }
    }

    private void configureToolbarIconSpringForCollapse() {
        this.mToolbarIconSpringForce.setBounce(this.mIconCollapseToNormalParams.getToolbarIconSpringBounce());
        this.mToolbarIconSpringForce.setResponse(this.mIconCollapseToNormalParams.getToolbarIconSpringResponse());
        if (this.mToolbarIconSpringAnimation == null) {
            this.mToolbarIconSpringAnimation = new COUISpringAnimation(this.mToolbarIconSpringValue);
        }
        this.mToolbarIconSpringAnimation.setMinimumVisibleChange(this.mIconCollapseToNormalParams.getToolbarIconMinimumVisibleChange());
        this.mToolbarIconSpringAnimation.setSpring(this.mToolbarIconSpringForce);
        this.mToolbarIconScaleEnd = this.mIconCollapseToNormalParams.getToolbarInitScale();
    }

    private void configureToolbarIconSpringForExpand() {
        this.mToolbarIconSpringForce.setBounce(this.mIconExpandToEditParams.getToolbarIconSpringBounce());
        this.mToolbarIconSpringForce.setResponse(this.mIconExpandToEditParams.getToolbarIconSpringResponse());
        if (this.mToolbarIconSpringAnimation == null) {
            this.mToolbarIconSpringAnimation = new COUISpringAnimation(this.mToolbarIconSpringValue);
        }
        this.mToolbarIconSpringAnimation.setMinimumVisibleChange(this.mIconExpandToEditParams.getToolbarIconMinimumVisibleChange());
        this.mToolbarIconSpringAnimation.setSpring(this.mToolbarIconSpringForce);
        this.mToolbarIconScaleEnd = this.mIconExpandToEditParams.getToolbarEndScale();
    }

    public static /* synthetic */ void d(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        float f5 = f3 / 1000.0f;
        float f6 = cOUISearchBar.mOuterButtonInitScale;
        float f7 = 1.0f - f5;
        float f8 = f6 + ((1.0f - f6) * f7);
        if (cOUISearchBar.shouldLayoutOut(cOUISearchBar.mOuterPrimaryButton)) {
            cOUISearchBar.mOuterPrimaryButton.setAlpha(f7);
            cOUISearchBar.mOuterPrimaryButton.setScaleX(f8);
            cOUISearchBar.mOuterPrimaryButton.setScaleY(f8);
            cOUISearchBar.updateBlurWithAnimationProgress(cOUISearchBar.mOuterPrimaryButton, f5);
            if (!COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
                cOUISearchBar.mOuterPrimaryButton.setTranslationX((-f5) * cOUISearchBar.mButtonOffsetAnimationDistance);
            }
        }
        if (cOUISearchBar.shouldLayoutOut(cOUISearchBar.mOuterSecondaryButton)) {
            cOUISearchBar.mOuterSecondaryButton.setAlpha(f7);
            cOUISearchBar.mOuterSecondaryButton.setScaleX(f8);
            cOUISearchBar.mOuterSecondaryButton.setScaleY(f8);
            cOUISearchBar.updateBlurWithAnimationProgress(cOUISearchBar.mOuterSecondaryButton, f5);
            if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
                return;
            }
            cOUISearchBar.mOuterSecondaryButton.setTranslationX((-f5) * cOUISearchBar.mButtonOffsetAnimationDistance);
        }
    }

    private void dispatchMaterialEffectType() {
        SearchBarBackgroundView searchBarBackgroundView = this.mSearchBarBackgroundView;
        if (searchBarBackgroundView != null) {
            searchBarBackgroundView.setMaterialEffectType(this.mMaterialType);
        }
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setMaterialEffectType(this.mMaterialType);
        }
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setMaterialEffectType(this.mMaterialType);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setMaterialEffectType(this.mMaterialType);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setMaterialEffectType(this.mMaterialType);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    public static /* synthetic */ void e(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        float f5 = f3 / 1000.0f;
        if (cOUISearchBar.functionalButtonShouldShow()) {
            float f6 = cOUISearchBar.mFunctionalInitScale;
            float f7 = f6 + ((1.0f - f6) * f5);
            if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
                cOUISearchBar.mFunctionalButton.setTranslationX(cOUISearchBar.mFunctionalButtonOffsetX * (1.0f - f5));
            } else {
                cOUISearchBar.mFunctionalButton.setTranslationX((1.0f - f5) * cOUISearchBar.mButtonOffsetAnimationDistance);
            }
            if (cOUISearchBar.mIsDrawableMaterialEffect || !cOUISearchBar.isBlurEnable()) {
                cOUISearchBar.mFunctionalButton.setAlpha(f5);
            } else {
                cOUISearchBar.updateFunctionalButtonAlpha(f5);
            }
            cOUISearchBar.mFunctionalButton.setScaleX(f7);
            cOUISearchBar.mFunctionalButton.setScaleY(f7);
            cOUISearchBar.updateBlurWithAnimationProgress(cOUISearchBar.mFunctionalButton, 1.0f - f5);
        }
    }

    private void endAnimateToEditState() {
        int i3 = this.mSearchViewType;
        if (i3 != 0) {
            if (i3 == 1) {
                this.mSearchBarBackgroundView.setDividerAlpha(1.0f);
                if (functionalButtonShouldShow()) {
                    resetViewState(this.mFunctionalButton);
                    restoreFunctionalButtonTextColor();
                    return;
                }
                return;
            }
            return;
        }
        if (shouldLayoutOut(this.mOuterPrimaryButton)) {
            resetViewState(this.mOuterPrimaryButton);
        }
        if (shouldLayoutOut(this.mOuterSecondaryButton)) {
            resetViewState(this.mOuterSecondaryButton);
        }
        if (functionalButtonShouldShow()) {
            resetViewState(this.mFunctionalButton);
            restoreFunctionalButtonTextColor();
        }
        setOuterButtonVisibility(8);
    }

    private void endAnimateToNormalState() {
        int i3 = this.mSearchViewType;
        if (i3 == 0) {
            if (shouldLayoutOut(this.mOuterPrimaryButton)) {
                resetViewState(this.mOuterPrimaryButton);
            }
            if (shouldLayoutOut(this.mOuterSecondaryButton)) {
                resetViewState(this.mOuterSecondaryButton);
            }
        } else if (i3 == 1) {
            this.mSearchBarBackgroundView.setDividerAlpha(0.0f);
        }
        if (functionalButtonShouldShow()) {
            resetViewState(this.mFunctionalButton);
            restoreFunctionalButtonTextColor();
            this.mFunctionalButton.setVisibility(8);
        }
        requestLayout();
    }

    private void ensureAddedToToolBar() {
        if (this.mHasAddedToToolbar) {
            return;
        }
        this.mHasAddedToToolbar = true;
        if (this.mToolBar != null) {
            removeLast();
            COUIToolbar.LayoutParams layoutParams = new COUIToolbar.LayoutParams(-1, this.mToolBar.getHeight() - this.mToolBar.getPaddingTop());
            layoutParams.gravity = this.mGravityInToolBar;
            this.mToolBar.setSearchView(this, layoutParams);
        }
    }

    private ImageView ensureImageView(Drawable drawable, boolean z2, boolean z3, int i3) {
        if (drawable == null) {
            return null;
        }
        ImageView imageView = new ImageView(getContext());
        if (!z3) {
            imageView.setClickable(false);
            imageView.setFocusable(false);
        }
        if (z2 && z3) {
            COUIRippleDrawableUtil.setIconPressRippleDrawable(imageView, i3);
        }
        this.mSearchBarBackgroundView.addView(imageView);
        return imageView;
    }

    private void ensureNavigationView() {
        if (this.mNavigationView == null) {
            SearchBarIconButton searchBarIconButton = new SearchBarIconButton(getContext(), this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark);
            this.mNavigationView = searchBarIconButton;
            searchBarIconButton.setMaterialEffectType(this.mMaterialType);
            addView(this.mNavigationView);
            syncIconButtonMaterialEffects(this.mNavigationView);
        }
    }

    private void ensureToolbarSearchIcon() {
        COUIToolbar cOUIToolbar = this.mToolBar;
        COUIActionMenuView menuView = cOUIToolbar != null ? cOUIToolbar.getMenuView() : null;
        View view = this.mToolbarSearchIcon;
        if (view != null && view.isAttachedToWindow() && this.mToolbarSearchIcon.getParent() == menuView) {
            return;
        }
        updateToolbarSearchIconRef(findToolbarSearchIcon());
    }

    public static /* synthetic */ void f(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUISearchBar.mOuterBtnAnimEnded = true;
        cOUISearchBar.checkAllStateAnimationsEnded();
    }

    private View findToolbarSearchIcon() {
        MenuItem menuItem;
        if (this.mToolBar != null && (menuItem = this.mMenuItem) != null) {
            View actionView = menuItem.getActionView();
            if (actionView != null && actionView != this) {
                return actionView;
            }
            COUIActionMenuView menuView = this.mToolBar.getMenuView();
            if (menuView != null) {
                int childCount = menuView.getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    View childAt = menuView.getChildAt(i3);
                    if ((childAt.getTag() != null && childAt.getTag().equals(this.mMenuItem)) || childAt.getId() == this.mMenuItem.getItemId()) {
                        return childAt;
                    }
                }
            }
        }
        return null;
    }

    private void funBtnAnimateIconExpand(float f3) {
        SearchBarFunctionButton searchBarFunctionButton;
        if (!functionalButtonShouldShow() || (searchBarFunctionButton = this.mFunctionalButton) == null) {
            return;
        }
        if (f3 <= 0.05f) {
            searchBarFunctionButton.setTranslationX(this.mFunctionalButtonStartX);
            this.mFunctionalButton.setScaleX(0.0f);
            this.mFunctionalButton.setScaleY(0.0f);
            this.mFunctionalButton.setAlpha(0.0f);
            return;
        }
        int i3 = this.mFunctionalButtonStartX;
        searchBarFunctionButton.setTranslationX(i3 + ((this.mFunctionalButtonTargetX - i3) * f3));
        float f4 = this.mFunctionalInitScaleExpand;
        float f5 = f4 + ((1.0f - f4) * f3);
        this.mFunctionalButton.setScaleX(f5);
        this.mFunctionalButton.setScaleY(f5);
        this.mFunctionalButton.setAlpha(f3);
        updateBlurWithAnimationProgress(this.mFunctionalButton, 1.0f - f3);
    }

    private boolean functionalButtonShouldShow() {
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton == null) {
            return false;
        }
        return (searchBarFunctionButton.getVisibility() == 8 && this.mHideFunctionalButton) ? false : true;
    }

    public static /* synthetic */ void g(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        float f5 = f3 / 1000.0f;
        OnAnimationListener onAnimationListener = cOUISearchBar.mOnAnimationListener;
        if (onAnimationListener != null) {
            onAnimationListener.onUpdate(f5);
        }
        if (cOUISearchBar.mSearchViewType == 0) {
            if (cOUISearchBar.mNoShadowInNormalState) {
                cOUISearchBar.updateDropShadow(f5);
            }
            if (cOUISearchBar.mNoMaterialInContent) {
                cOUISearchBar.updateMaterial(f5);
            }
        }
        cOUISearchBar.mSearchBarBackgroundView.doWidthChangedAnimate(cOUISearchBar.mSearchViewBackgroundMaxWidth, cOUISearchBar.mSearchViewBackgroundMinWidth, f5);
    }

    private ColorStateList getFunctionalButtonBaseTextColor() {
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton == null) {
            return null;
        }
        if (this.mFunctionalButtonTextColor == null) {
            this.mFunctionalButtonTextColor = searchBarFunctionButton.getTextColors();
        }
        return this.mFunctionalButtonTextColor;
    }

    private int getInternalPaddingEnd() {
        return this.mUseResponsivePadding ? this.mResponsiveHorizontalPadding[this.mResponsiveWidthSize] : getPaddingEnd();
    }

    private int getInternalPaddingStart() {
        return this.mUseResponsivePadding ? this.mResponsiveHorizontalPadding[this.mResponsiveWidthSize] : getPaddingStart();
    }

    private static void getLayoutLocationInWindow(View view, int[] iArr) {
        view.getLocationInWindow(iArr);
        float pivotX = (-view.getTranslationX()) + (view.getPivotX() * (view.getScaleX() - 1.0f));
        float pivotY = (-view.getTranslationY()) + (view.getPivotY() * (view.getScaleY() - 1.0f));
        iArr[0] = iArr[0] + Math.round(pivotX);
        iArr[1] = iArr[1] + Math.round(pivotY);
    }

    private int getMeltBallParamThirdPx() {
        return Math.round(TypedValue.applyDimension(1, 4.0f, getResources().getDisplayMetrics()));
    }

    private int getOuterButtonCount() {
        return (shouldLayoutOut(this.mOuterPrimaryButton) ? 1 : 0) + (shouldLayoutOut(this.mOuterSecondaryButton) ? 1 : 0);
    }

    private View getSearchEditOrAnimationLayout() {
        return this.mSearchBarBackgroundView.getHintLayoutMayBeNull() != null ? this.mSearchBarBackgroundView.getHintLayoutMayBeNull() : this.mSearchBarBackgroundView.getSearchEditText();
    }

    private float getToolbarTitleBaseAlpha() {
        COUIToolbar cOUIToolbar = this.mToolBar;
        if (cOUIToolbar == null) {
            return 1.0f;
        }
        ViewParent parent = cOUIToolbar.getParent();
        if (parent instanceof COUICollapsingToolbarLayout) {
            return (!this.mToolBar.getIsTitleCenterStyle() || ((COUICollapsingToolbarLayout) parent).isExpanded()) ? 0.0f : 1.0f;
        }
        return 1.0f;
    }

    public static /* synthetic */ void h(COUISearchBar cOUISearchBar) {
        cOUISearchBar.openSoftInput(true);
    }

    private void handleStateChangedAnimation() {
        if (isRtl()) {
            SearchBarBackgroundView searchBarBackgroundView = this.mSearchBarBackgroundView;
            int iMax = (this.mBackgroundRect.right - Math.max(this.mSearchViewBackgroundMaxWidth, this.mSearchViewBackgroundMinWidth)) - 20;
            Rect rect = this.mBackgroundRect;
            searchBarBackgroundView.layout(iMax, rect.top, rect.right, rect.bottom);
        } else {
            SearchBarBackgroundView searchBarBackgroundView2 = this.mSearchBarBackgroundView;
            Rect rect2 = this.mBackgroundRect;
            int i3 = rect2.left;
            searchBarBackgroundView2.layout(i3, rect2.top, Math.max(this.mSearchViewBackgroundMaxWidth, this.mSearchViewBackgroundMinWidth) + i3 + 20, this.mBackgroundRect.bottom);
        }
        if (isRtl()) {
            SearchBarBackgroundView searchBarBackgroundView3 = this.mSearchBarBackgroundView;
            Rect rect3 = this.mBackgroundRect;
            int iWidth = rect3.right - rect3.width();
            Rect rect4 = this.mBackgroundRect;
            searchBarBackgroundView3.initDrawingBounds(iWidth, 0, rect4.right, rect4.bottom - rect4.top);
        } else {
            SearchBarBackgroundView searchBarBackgroundView4 = this.mSearchBarBackgroundView;
            Rect rect5 = this.mBackgroundRect;
            searchBarBackgroundView4.initDrawingBounds(0, 0, rect5.right - rect5.left, rect5.bottom - rect5.top);
        }
        layoutOuterButtonBackToNormal();
        if (this.mFunctionalButton != null) {
            if (isRtl()) {
                this.mFunctionalButtonOffsetX = this.mSearchBarBackgroundView.getLeft() - this.mFunctionalButton.getLeft();
            } else {
                this.mFunctionalButtonOffsetX = this.mSearchBarBackgroundView.getRight() - this.mFunctionalButton.getRight();
            }
        }
        this.mSearchBarBackgroundView.doBeforeAnimate(this.mSearchViewBackgroundMaxWidth, this.mSearchViewBackgroundMinWidth, this.mStateToEditAnimation, this.mStateToNormalAnimation);
        this.mStateAnimEnded = false;
        this.mFunBtnAnimEnded = !functionalButtonShouldShow();
        this.mOuterBtnAnimEnded = !outerButtonShouldAnimate();
        if (this.mStateToEditAnimation) {
            if (shouldLayoutOut(this.mFunctionalButton)) {
                this.mFunctionalButton.setScaleX(0.0f);
                this.mFunctionalButton.setScaleY(0.0f);
            }
            this.mStateChangedAnimation.animateToFinalPosition(1000.0f);
            if (functionalButtonShouldShow()) {
                this.mFunBtnSpringAnimation.animateToFinalPosition(1000.0f);
            }
            if (outerButtonShouldAnimate()) {
                this.mOuterBtnSpringAnimation.animateToFinalPosition(1000.0f);
                return;
            }
            return;
        }
        if (this.mStateToNormalAnimation) {
            this.mStateChangedAnimation.animateToFinalPosition(0.0f);
            if (functionalButtonShouldShow()) {
                this.mFunBtnSpringAnimation.animateToFinalPosition(0.0f);
            }
            if (outerButtonShouldAnimate()) {
                this.mOuterBtnSpringAnimation.animateToFinalPosition(0.0f);
            }
            if (shouldLayoutOut(this.mOuterPrimaryButton)) {
                this.mOuterPrimaryButton.setScaleX(0.0f);
                this.mOuterPrimaryButton.setScaleY(0.0f);
            }
        }
    }

    public static /* synthetic */ void i(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        float f5 = f3 / 1000.0f;
        OnAnimationListener onAnimationListener = cOUISearchBar.mOnAnimationListener;
        if (onAnimationListener != null) {
            onAnimationListener.onUpdate(f5);
        }
        float f6 = cOUISearchBar.mIconInitialWidth;
        float f7 = cOUISearchBar.mSearchBarFinalWidth;
        cOUISearchBar.mSearchBarBackgroundView.updateIconExpandClippingWidth(f6, f7, Math.max(f6, ((f7 - f6) * f5) + f6));
        cOUISearchBar.setToolbarSearchIconAlpha(0.0f);
        int i3 = cOUISearchBar.mIconExpandStartX;
        cOUISearchBar.mSearchBarBackgroundView.setTranslationX(i3 + ((cOUISearchBar.mIconExpandTargetX - i3) * f5));
        cOUISearchBar.updateBlurWithAnimationProgress(cOUISearchBar.mSearchBarBackgroundView, 1.0f - (Math.abs((1.0f - f5) - 0.5f) * 2.0f));
        if (cOUISearchBar.mNoShadowInNormalState) {
            cOUISearchBar.updateDropShadow(f5);
        }
        View searchEditOrAnimationLayout = cOUISearchBar.getSearchEditOrAnimationLayout();
        if (searchEditOrAnimationLayout != null) {
            searchEditOrAnimationLayout.setAlpha(f5);
        }
        cOUISearchBar.funBtnAnimateIconExpand(f5);
    }

    private void iconExpandEndListener() {
        this.mIconExpandAnimation.addEndListener(new e(this));
    }

    private void init(Context context, AttributeSet attributeSet, int i3, int i4) {
        this.mIsAtLeastR = true;
        initSearchBarBackgroundView();
        loadHideFunctionButtonAttr(context, attributeSet, i3, i4);
        initFunctionButton();
        this.mAttrs = attributeSet;
        if (attributeSet != null) {
            this.mStyle = attributeSet.getStyleAttribute();
        }
        if (this.mStyle == 0) {
            this.mStyle = i3;
        }
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);
        this.mCollapsedMinHeight = context.getResources().getDimensionPixelSize(R.dimen.coui_search_view_collapsed_min_height);
        this.mInitSearchBarHeight = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_height);
        this.mNormalBackgroundHeight = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_normal_background_height);
        this.mInnerIconSize = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_inner_search_icon_size);
        this.mOuterButtonWidth = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_outer_button_width);
        this.mNavigationButtonWidth = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_navigation_button_width);
        this.mOuterButtonGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_outer_button_gap_between_background);
        this.mFunctionButtonMaxWidth = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_functional_button_max_width);
        this.mFunctionButtonStartGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_functional_button_start_gap);
        this.mButtonOffsetAnimationDistance = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_functional_button_offset_distance);
        Resources resources = context.getResources();
        int i5 = R.dimen.coui_search_view_icon_size;
        this.mIconMaxWidth = resources.getDimensionPixelSize(i5);
        this.mIconMaxHeight = context.getResources().getDimensionPixelSize(i5);
        this.mNavigationButtonEndGap = context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_navigation_button_end_gap);
        this.mResponsiveHorizontalPadding = new int[]{context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_responsive_horizontal_padding_compat), context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_responsive_horizontal_padding_medium), context.getResources().getDimensionPixelOffset(R.dimen.coui_search_bar_responsive_horizontal_padding_expanded)};
        loadAttr(context, attributeSet, i3, i4);
        this.mSearchBarBackgroundView.loadAttr(context, attributeSet, i3, i4);
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            this.mNormalBackgroundColor = ToolbarMaterialEffectResolver.getToolbarBackgroundColor(context, this.mMaterialType);
        } else {
            this.mNormalBackgroundColor = ResourcesCompat.getColor(getContext().getResources(), R.color.coui_search_view_selector_color_normal, getContext().getTheme());
        }
        this.mSearchBarBackgroundView.setButtonDividerColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorDivider));
        this.mSearchBarBackgroundView.setCurrentBackgroundColor(this.mNormalBackgroundColor);
        this.mSearchBarContentDesc = getResources().getString(R.string.coui_search_edit_box_description);
        this.mSearchBarHintTitleDesc = getResources().getString(R.string.support_abc_searchview_description_search);
        this.mBackgroundColor = new ColorDrawable(this.mNormalBackgroundColor);
        syncNoBlurMaterialToChildren();
    }

    private void initEditNormalSateAnimation() {
        initOvLinkEditParams();
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.mStateAnimationValue);
        this.mStateChangedAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setMinimumVisibleChange(this.mChangedToEditParams.getStateSpringMinimumVisibleChange());
        this.mStateChangedAnimation.setSpring(this.mStateAnimationSpringForceX);
        this.mStateChangedAnimation.addUpdateListener(new f(this));
        this.mStateChangedAnimation.addEndListener(new g(this));
        initFunBtnAnimate();
        initOuterBtnAnimate();
    }

    private void initFunBtnAnimate() {
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.mFunBtnAnimationValue);
        this.mFunBtnSpringAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setMinimumVisibleChange(this.mChangedToEditParams.getFunBtnMinimumVisibleChange());
        this.mFunBtnSpringAnimation.setSpring(this.mFunBtnSpringForce);
        this.mFunBtnSpringAnimation.addUpdateListener(new m(this));
        this.mFunBtnSpringAnimation.addEndListener(new b(this));
    }

    private void initFunctionButton() {
        if (this.mHideFunctionalButton) {
            COUILog.i(TAG, "mHideFunctionalButton is true");
            return;
        }
        SearchBarFunctionButton searchBarFunctionButton = new SearchBarFunctionButton(getContext());
        this.mFunctionalButton = searchBarFunctionButton;
        searchBarFunctionButton.setMaxLines(1);
        this.mFunctionalButton.setEllipsize(TextUtils.TruncateAt.END);
        this.mFunctionalButton.setTextAppearance(getContext(), com.support.appcompat.R.style.couiTextAppearanceButton);
        this.mFunctionalButton.setText(R.string.coui_search_view_cancel);
        this.mFunctionalButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.coui_searchview_cancel_button_color, getContext().getTheme()));
        this.mFunctionalButtonTextColor = this.mFunctionalButton.getTextColors();
        this.mFunctionalButton.setClickable(true);
        this.mFunctionalButton.setFocusable(true);
        this.mFunctionalButton.setAlpha(0.0f);
        this.mFunctionalButton.setVisibility(8);
        this.mFunctionalButton.setTextSize(0, COUIChangeTextUtil.getSuitableFontSize(this.mFunctionalButton.getTextSize(), getContext().getResources().getConfiguration().fontScale, 2));
        addView(this.mFunctionalButton);
        this.mSearchBarBackgroundView.initFunctionButton(this.mFunctionalButton);
    }

    private void initIconExpandAnimation() {
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.mIconExpandCenterAnimationRange);
        this.mIconExpandAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setMinimumVisibleChange(this.mIconExpandToEditParams.getIconExpandSpringMinimumVisibleChange());
        this.mIconExpandAnimation.setSpring(this.mExpandSpringForceX);
        this.mToolbarIconSpringAnimation = new COUISpringAnimation(this.mToolbarIconSpringValue);
        initExpandData();
        this.mToolbarIconSpringAnimation.addUpdateListener(new k(this));
        this.mIconExpandAnimation.addUpdateListener(new l(this));
        iconExpandEndListener();
    }

    private void initParams() {
        if (isOliveLinkEnable()) {
            this.mParamProvider = new SearchBarParamProvider.OlivelinkDefault(getContext());
        } else {
            this.mParamProvider = new SearchBarParamProvider.Default(getContext());
        }
        this.mChangedToEditParams = this.mParamProvider.getStateChangedToEditParams();
        this.mChangedToNormalParams = this.mParamProvider.getStateChangedToNormalParams();
        this.mIconExpandToEditParams = this.mParamProvider.getIconExpandToEditParams();
        this.mIconCollapseToNormalParams = this.mParamProvider.getIconCollapseToNormalParams();
    }

    private void initSearchBarBackgroundView() {
        SearchBarBackgroundView searchBarBackgroundView = new SearchBarBackgroundView(getContext());
        this.mSearchBarBackgroundView = searchBarBackgroundView;
        addView(searchBarBackgroundView);
    }

    private void initToolbarAnimation() {
        initFadeInData();
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.mFadeInFadeOutValue);
        this.mFadeInFadeOutAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setMinimumVisibleChange(this.mIconExpandToEditParams.getFadeSpringMinimumVisibleChange());
        this.mFadeInFadeOutAnimation.setSpring(this.mFadeInSpringForceX);
        this.mFadeInFadeOutAnimation.addUpdateListener(new AnonymousClass1());
        this.mFadeInFadeOutAnimation.addEndListener(new i(this));
    }

    private boolean isBlurEnable() {
        return (this.mSystemBlurState && this.mUseMaterialEffectAttr != 0) || this.mUseMaterialEffectAttr == 1;
    }

    private boolean isCausticShadowEnable() {
        return (this.mSystemCausticShadowState && this.mUseMaterialEffectAttr != 0) || (this.mUseMaterialEffectAttr == 1 && COUIMaterialStateManager.INSTANCE.isFeatureEnabled());
    }

    private boolean isInMultiWindowMode() {
        if (getContext() instanceof Activity) {
            return ((Activity) getContext()).isInMultiWindowMode();
        }
        return false;
    }

    private boolean isOliveLinkEnable() {
        return false;
    }

    private boolean isRtl() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private boolean isSpotLightEnable() {
        return (this.mSystemSpotLightState && this.mUseMaterialEffectAttr != 0) || (this.mUseMaterialEffectAttr == 1 && COUIMaterialStateManager.INSTANCE.isFeatureEnabled());
    }

    private boolean isStrokeEnable() {
        return (this.mSystemStrokeState && this.mUseMaterialEffectAttr != 0) || this.mUseMaterialEffectAttr == 1;
    }

    private boolean isSupportBlur() {
        COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
        return !cOUIMaterialStateManager.isLowAnimationLevel() && cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(getContext());
    }

    private boolean isTransientToolbarExpandCollapseRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mIconExpandAnimation;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            return true;
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mFadeInFadeOutAnimation;
        return cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning();
    }

    public static /* synthetic */ void j(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        if (cOUISearchBar.mState.get() == 1) {
            cOUISearchBar.mIsFadeInAnimating = false;
            cOUISearchBar.setSearchBarToolbarFadeAnimating(false);
            cOUISearchBar.setVisibility(0);
            cOUISearchBar.setAlpha2Child(cOUISearchBar, 1.0f);
            cOUISearchBar.setToolBarAlpha(0.0f);
            cOUISearchBar.setToolBarScale(0.0f);
            cOUISearchBar.setToolBarChildVisibility(8);
            cOUISearchBar.endAnimateToEditState();
            OnAnimationListener onAnimationListener = cOUISearchBar.mOnAnimationListener;
            if (onAnimationListener != null) {
                onAnimationListener.onAnimationEnd(1);
            }
        } else {
            cOUISearchBar.mIsFadeInAnimating = false;
            cOUISearchBar.setSearchBarToolbarFadeAnimating(false);
            cOUISearchBar.setVisibility(8);
            cOUISearchBar.setAlpha2Child(cOUISearchBar, 1.0f);
            cOUISearchBar.setToolBarAlpha(1.0f);
            cOUISearchBar.setToolBarScale(1.0f);
            cOUISearchBar.setToolBarChildVisibility(0);
            cOUISearchBar.endAnimateToNormalState();
            OnAnimationListener onAnimationListener2 = cOUISearchBar.mOnAnimationListener;
            if (onAnimationListener2 != null) {
                onAnimationListener2.onAnimationEnd(0);
            }
        }
        cOUISearchBar.updateSearchAndButtonTouchDeformation(true);
        cOUISearchBar.requestLayout();
    }

    public static /* synthetic */ void k(COUISearchBar cOUISearchBar, ImeInsetsAnimationCallback imeInsetsAnimationCallback) {
        ViewCompat.setWindowInsetsAnimationCallback(cOUISearchBar.mSearchBarBackgroundView.getSearchEditText().getRootView(), imeInsetsAnimationCallback);
    }

    public static /* synthetic */ void l(COUISearchBar cOUISearchBar) {
        cOUISearchBar.onToolbarSearchIconDownWhileInvisible();
    }

    private void layoutBackground() {
        int right;
        int iWidth;
        int measuredWidth;
        int internalPaddingStart;
        int iCalculateRelativeTop = calculateRelativeTop(0, getMeasuredHeight(), this.mBackgroundRect.height());
        int iHeight = this.mBackgroundRect.height() + iCalculateRelativeTop;
        if (isRtl()) {
            if (shouldLayoutOut(this.mNavigationView)) {
                measuredWidth = this.mNavigationView.getLeft();
                internalPaddingStart = this.mNavigationButtonEndGap;
            } else {
                measuredWidth = getMeasuredWidth();
                internalPaddingStart = getInternalPaddingStart();
            }
            iWidth = measuredWidth - internalPaddingStart;
            right = iWidth - this.mBackgroundRect.width();
        } else {
            right = shouldLayoutOut(this.mNavigationView) ? this.mNavigationView.getRight() + this.mNavigationButtonEndGap : getInternalPaddingStart();
            iWidth = this.mBackgroundRect.width() + right;
        }
        OnSearchBarBackgroundBoundsChangedListener onSearchBarBackgroundBoundsChangedListener = this.mOnSearchBarBackgroundBoundsChangedListener;
        if (onSearchBarBackgroundBoundsChangedListener != null) {
            Rect rect = this.mBackgroundRect;
            if (right != rect.left && iCalculateRelativeTop != rect.top && iWidth != rect.right && iHeight != rect.bottom) {
                onSearchBarBackgroundBoundsChangedListener.onBackgroundBoundsChanged(right, iCalculateRelativeTop, iWidth, iHeight);
            }
        }
        this.mBackgroundRect.set(right, iCalculateRelativeTop, iWidth, iHeight);
        this.mSearchBarBackgroundView.layout(right, iCalculateRelativeTop, iWidth, iHeight);
    }

    private void layoutBackgroundArea() {
        layoutBackground();
    }

    private void layoutBackgroundLeft() {
        if (this.mSearchViewType == 1) {
            layoutNavigationButton();
        }
    }

    private void layoutBackgroundRight() {
        int i3 = this.mSearchViewType;
        if (i3 != 0) {
            if (i3 == 1) {
                layoutOuterButton();
                return;
            }
            return;
        }
        int internalPaddingEnd = isRtl() ? getInternalPaddingEnd() : getMeasuredWidth() - getInternalPaddingEnd();
        if (shouldLayoutOut(this.mFunctionalButton)) {
            Rect rect = this.mBackgroundRect;
            int iCalculateRelativeTop = calculateRelativeTop(rect.top, rect.height(), this.mFunctionalButton.getMeasuredHeight());
            if (isRtl()) {
                SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
                searchBarFunctionButton.layout(internalPaddingEnd, iCalculateRelativeTop, searchBarFunctionButton.getMeasuredWidth() + internalPaddingEnd, this.mFunctionalButton.getMeasuredHeight() + iCalculateRelativeTop);
            } else {
                SearchBarFunctionButton searchBarFunctionButton2 = this.mFunctionalButton;
                searchBarFunctionButton2.layout(internalPaddingEnd - searchBarFunctionButton2.getMeasuredWidth(), iCalculateRelativeTop, internalPaddingEnd, this.mFunctionalButton.getMeasuredHeight() + iCalculateRelativeTop);
            }
        }
        if (getOuterButtonCount() == 1) {
            layoutOuterButton();
        }
    }

    private void layoutIconExpandBackgroundWithExtraPadding() {
        if (!isRtl()) {
            SearchBarBackgroundView searchBarBackgroundView = this.mSearchBarBackgroundView;
            Rect rect = this.mBackgroundRect;
            int i3 = rect.left;
            searchBarBackgroundView.layout(i3, rect.top, rect.width() + i3 + 20, this.mBackgroundRect.bottom);
            return;
        }
        SearchBarBackgroundView searchBarBackgroundView2 = this.mSearchBarBackgroundView;
        Rect rect2 = this.mBackgroundRect;
        int iWidth = (rect2.right - rect2.width()) - 20;
        Rect rect3 = this.mBackgroundRect;
        searchBarBackgroundView2.layout(iWidth, rect3.top, rect3.right, rect3.bottom);
    }

    private void layoutNavigationButton() {
        if (shouldLayoutOut(this.mNavigationView)) {
            int iCalculateRelativeTop = calculateRelativeTop(0, getMeasuredHeight(), this.mNavigationView.getMeasuredHeight());
            if (isRtl()) {
                int measuredWidth = getMeasuredWidth() - getInternalPaddingEnd();
                SearchBarIconButton searchBarIconButton = this.mNavigationView;
                searchBarIconButton.layout(measuredWidth - searchBarIconButton.getMeasuredWidth(), iCalculateRelativeTop, measuredWidth, this.mNavigationView.getMeasuredHeight() + iCalculateRelativeTop);
            } else {
                int internalPaddingStart = getInternalPaddingStart();
                SearchBarIconButton searchBarIconButton2 = this.mNavigationView;
                searchBarIconButton2.layout(internalPaddingStart, iCalculateRelativeTop, searchBarIconButton2.getMeasuredWidth() + internalPaddingStart, this.mNavigationView.getMeasuredHeight() + iCalculateRelativeTop);
            }
        }
    }

    private void layoutOuterButton() {
        if (!isRtl()) {
            int right = this.mSearchBarBackgroundView.getRight() + this.mOuterButtonGap;
            if (shouldLayoutOut(this.mOuterPrimaryButton)) {
                int iCalculateRelativeTop = calculateRelativeTop(0, getMeasuredHeight(), this.mOuterPrimaryButton.getMeasuredHeight());
                SearchBarIconButton searchBarIconButton = this.mOuterPrimaryButton;
                searchBarIconButton.layout(right, iCalculateRelativeTop, searchBarIconButton.getMeasuredWidth() + right, this.mOuterPrimaryButton.getMeasuredHeight() + iCalculateRelativeTop);
                right += this.mOuterPrimaryButton.getMeasuredWidth() + this.mOuterButtonGap;
            }
            if (shouldLayoutOut(this.mOuterSecondaryButton)) {
                int iCalculateRelativeTop2 = calculateRelativeTop(0, getMeasuredHeight(), this.mOuterSecondaryButton.getMeasuredHeight());
                SearchBarIconButton searchBarIconButton2 = this.mOuterSecondaryButton;
                searchBarIconButton2.layout(right, iCalculateRelativeTop2, searchBarIconButton2.getMeasuredWidth() + right, this.mOuterSecondaryButton.getMeasuredHeight() + iCalculateRelativeTop2);
                return;
            }
            return;
        }
        int left = this.mSearchBarBackgroundView.getLeft() - this.mOuterButtonGap;
        if (shouldLayoutOut(this.mOuterPrimaryButton)) {
            int iCalculateRelativeTop3 = calculateRelativeTop(0, getMeasuredHeight(), this.mOuterPrimaryButton.getMeasuredHeight());
            SearchBarIconButton searchBarIconButton3 = this.mOuterPrimaryButton;
            searchBarIconButton3.layout(left - searchBarIconButton3.getMeasuredWidth(), iCalculateRelativeTop3, left, this.mOuterPrimaryButton.getMeasuredHeight() + iCalculateRelativeTop3);
            left -= this.mOuterPrimaryButton.getMeasuredWidth();
        }
        if (shouldLayoutOut(this.mOuterSecondaryButton)) {
            int i3 = left - this.mOuterButtonGap;
            int iCalculateRelativeTop4 = calculateRelativeTop(0, getMeasuredHeight(), this.mOuterSecondaryButton.getMeasuredHeight());
            SearchBarIconButton searchBarIconButton4 = this.mOuterSecondaryButton;
            searchBarIconButton4.layout(i3 - searchBarIconButton4.getMeasuredWidth(), iCalculateRelativeTop4, i3, this.mOuterSecondaryButton.getMeasuredHeight() + iCalculateRelativeTop4);
        }
    }

    private void layoutOuterButtonBackToNormal() {
        if (!isRtl()) {
            int left = this.mSearchBarBackgroundView.getLeft() + this.mSearchViewBackgroundMaxWidth + this.mOuterButtonGap;
            if (shouldLayoutOut(this.mOuterPrimaryButton)) {
                int iCalculateRelativeTop = calculateRelativeTop(0, getMeasuredHeight(), this.mOuterPrimaryButton.getMeasuredHeight());
                SearchBarIconButton searchBarIconButton = this.mOuterPrimaryButton;
                searchBarIconButton.layout(left, iCalculateRelativeTop, searchBarIconButton.getMeasuredWidth() + left, this.mOuterPrimaryButton.getMeasuredHeight() + iCalculateRelativeTop);
                left += this.mOuterPrimaryButton.getMeasuredWidth() + this.mOuterButtonGap;
            }
            if (shouldLayoutOut(this.mOuterSecondaryButton)) {
                int iCalculateRelativeTop2 = calculateRelativeTop(0, getMeasuredHeight(), this.mOuterSecondaryButton.getMeasuredHeight());
                SearchBarIconButton searchBarIconButton2 = this.mOuterSecondaryButton;
                searchBarIconButton2.layout(left, iCalculateRelativeTop2, searchBarIconButton2.getMeasuredWidth() + left, this.mOuterSecondaryButton.getMeasuredHeight() + iCalculateRelativeTop2);
                return;
            }
            return;
        }
        int right = (this.mSearchBarBackgroundView.getRight() - this.mSearchViewBackgroundMaxWidth) - this.mOuterButtonGap;
        if (shouldLayoutOut(this.mOuterPrimaryButton)) {
            int iCalculateRelativeTop3 = calculateRelativeTop(0, getMeasuredHeight(), this.mOuterPrimaryButton.getMeasuredHeight());
            SearchBarIconButton searchBarIconButton3 = this.mOuterPrimaryButton;
            searchBarIconButton3.layout(right - searchBarIconButton3.getMeasuredWidth(), iCalculateRelativeTop3, right, this.mOuterPrimaryButton.getMeasuredHeight() + iCalculateRelativeTop3);
            right -= this.mOuterPrimaryButton.getMeasuredWidth();
        }
        if (shouldLayoutOut(this.mOuterSecondaryButton)) {
            int i3 = right - this.mOuterButtonGap;
            int iCalculateRelativeTop4 = calculateRelativeTop(0, getMeasuredHeight(), this.mOuterSecondaryButton.getMeasuredHeight());
            SearchBarIconButton searchBarIconButton4 = this.mOuterSecondaryButton;
            searchBarIconButton4.layout(i3 - searchBarIconButton4.getMeasuredWidth(), iCalculateRelativeTop4, i3, this.mOuterSecondaryButton.getMeasuredHeight() + iCalculateRelativeTop4);
        }
    }

    private void loadAttr(Context context, AttributeSet attributeSet, int i3, int i4) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUISearchBar, i3, i4);
        int i5 = R.styleable.COUISearchBar_functionalButtonText;
        if (typedArrayObtainStyledAttributes.hasValue(i5) && functionalButtonShouldShow()) {
            this.mFunctionalButton.setText(typedArrayObtainStyledAttributes.getString(i5));
        }
        int i6 = R.styleable.COUISearchBar_functionalButtonTextColor;
        if (typedArrayObtainStyledAttributes.hasValue(i6) && functionalButtonShouldShow()) {
            ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(i6);
            this.mFunctionalButton.setTextColor(colorStateList);
            this.mFunctionalButtonTextColor = colorStateList;
        }
        this.mIsDrawableMaterialEffect = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISearchBar_couiSearchBarNoBlurEffect, false);
        this.mStrokeEffectForceDark = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISearchBar_couiSearchBarStrokeEffectForceDark, false);
        int i7 = R.styleable.COUISearchBar_supportMaterialType;
        if (typedArrayObtainStyledAttributes.hasValue(i7)) {
            this.mMaterialType = typedArrayObtainStyledAttributes.getInteger(i7, 0);
            dispatchMaterialEffectType();
        }
        if (this.mMaterialType == 1) {
            this.mNoBlurSolidColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUISearchBar_couiSearchBarNoBlurSolidColor, BLUR_WINDOW_VIEW_BG_COLOR_IMMERSIVE);
        } else if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            this.mNoBlurSolidColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUISearchBar_couiSearchBarNoBlurSolidColor, COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext()) ? BLUR_WINDOW_VIEW_BG_COLOR_DARK : BLUR_WINDOW_VIEW_BG_COLOR_LIGHT);
        } else {
            this.mNoBlurSolidColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUISearchBar_couiSearchBarNoBlurSolidColor, ResourcesCompat.getColor(getContext().getResources(), R.color.coui_search_view_selector_color_normal, getContext().getTheme()));
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    private void loadHideFunctionButtonAttr(Context context, AttributeSet attributeSet, int i3, int i4) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUISearchBar, i3, i4);
        this.mHideFunctionalButton = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISearchBar_couiHideFunctionalButton, false);
        typedArrayObtainStyledAttributes.recycle();
    }

    public static /* synthetic */ void m(COUISearchBar cOUISearchBar, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUISearchBar.mLastStateAnimEndValue = f3;
        cOUISearchBar.mStateAnimEnded = true;
        cOUISearchBar.checkAllStateAnimationsEnded();
    }

    private int measureBackground(int i3) {
        int measuredWidth;
        measureFunctionalButton();
        int i4 = this.mSearchViewType;
        if (i4 != 0) {
            if (i4 == 1) {
                measureRect(this.mBackgroundRect, i3, (int) Float.max(this.mCollapsedMinHeight, this.mNormalBackgroundHeight));
                measureView(this.mSearchBarBackgroundView, View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mNormalBackgroundHeight, 1073741824));
            }
            return i3;
        }
        int internalPaddingEnd = !functionalButtonShouldShow() ? getInternalPaddingEnd() : this.mFunctionalButton.getMeasuredWidth() + this.mFunctionButtonStartGap + getInternalPaddingEnd();
        if (this.mState.get() == 1 || this.mIconStartCollapse || this.mIconStartExpand || this.mFadeInToolbar || this.mFadeOutToolbar || this.mIconExpandAnimation.isRunning() || this.mFadeInFadeOutAnimation.isRunning()) {
            measuredWidth = (getMeasuredWidth() - getInternalPaddingStart()) - internalPaddingEnd;
            this.mSearchViewBackgroundMinWidth = measuredWidth;
        } else {
            measuredWidth = i3;
        }
        this.mSearchViewBackgroundMaxWidth = i3;
        measureRect(this.mBackgroundRect, measuredWidth, (int) Float.max(this.mCollapsedMinHeight, this.mNormalBackgroundHeight));
        measureView(this.mSearchBarBackgroundView, View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mNormalBackgroundHeight, 1073741824));
        return measuredWidth;
    }

    private void measureFunctionalButton() {
        if (shouldLayoutOut(this.mFunctionalButton)) {
            measureView(this.mFunctionalButton, View.MeasureSpec.makeMeasureSpec(this.mFunctionButtonMaxWidth, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
        }
    }

    private int measureNavigationButton(int i3) {
        return (shouldLayoutOut(this.mNavigationView) && this.mSearchViewType == 1) ? (i3 - measureView(this.mNavigationView, View.MeasureSpec.makeMeasureSpec(this.mNavigationButtonWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mNavigationButtonWidth, 1073741824))) - this.mNavigationButtonEndGap : i3;
    }

    private int measureOuterButton(int i3) {
        int outerButtonCount = getOuterButtonCount();
        if (this.mSearchViewType != 1 && outerButtonCount != 1) {
            return i3;
        }
        if (shouldLayoutOut(this.mOuterPrimaryButton)) {
            i3 = (i3 - measureView(this.mOuterPrimaryButton, View.MeasureSpec.makeMeasureSpec(this.mOuterButtonWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mOuterButtonWidth, 1073741824))) - this.mOuterButtonGap;
        }
        return shouldLayoutOut(this.mOuterSecondaryButton) ? (i3 - measureView(this.mOuterSecondaryButton, View.MeasureSpec.makeMeasureSpec(this.mOuterButtonWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mOuterButtonWidth, 1073741824))) - this.mOuterButtonGap : i3;
    }

    private int measureOutsideBackground() {
        return measureOuterButton(measureNavigationButton((getMeasuredWidth() - getInternalPaddingStart()) - getInternalPaddingEnd()));
    }

    private void measureRect(Rect rect, int i3, int i4) {
        if (rect != null) {
            rect.set(0, 0, i3, i4);
        }
    }

    private int measureView(View view, int i3, int i4) {
        view.measure(i3, i4);
        return view.getMeasuredWidth();
    }

    private void notifyOnStateChange(int i3, int i4) {
        this.mState.set(i4);
        this.mSearchBarBackgroundView.setState(i4);
        updateAccessibilityImportance(i4);
        List<OnStateChangeListener> list = this.mOnStateChangeListeners;
        if (list != null) {
            for (OnStateChangeListener onStateChangeListener : list) {
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStateChange(i3, i4);
                }
            }
        }
    }

    private void onIconCollapseAnimationEnd() {
        SearchBarFunctionButton searchBarFunctionButton;
        this.mSearchBarBackgroundView.endIconExpandClipping();
        this.mSearchBarBackgroundView.setTranslationX(0.0f);
        View searchEditOrAnimationLayout = getSearchEditOrAnimationLayout();
        if (searchEditOrAnimationLayout != null) {
            searchEditOrAnimationLayout.setAlpha(1.0f);
        }
        if (functionalButtonShouldShow() && (searchBarFunctionButton = this.mFunctionalButton) != null) {
            searchBarFunctionButton.setTranslationX(0.0f);
            this.mFunctionalButton.setScaleX(1.0f);
            this.mFunctionalButton.setScaleY(1.0f);
            this.mFunctionalButton.setAlpha(1.0f);
            this.mFunctionalButton.setRenderEffect(null);
        }
        setVisibility(8);
        setAlpha(1.0f);
        setToolBarAlpha(1.0f);
        setToolBarScale(1.0f);
        setToolBarChildVisibility(0);
        requestLayout();
    }

    private void onIconExpandAnimationEnd() {
        SearchBarFunctionButton searchBarFunctionButton;
        this.mIsExpandAnimating = false;
        this.mSearchBarBackgroundView.endIconExpandClipping();
        this.mSearchBarBackgroundView.setTranslationX(0.0f);
        View searchEditOrAnimationLayout = getSearchEditOrAnimationLayout();
        if (searchEditOrAnimationLayout != null) {
            searchEditOrAnimationLayout.setAlpha(1.0f);
        }
        if (functionalButtonShouldShow() && (searchBarFunctionButton = this.mFunctionalButton) != null) {
            searchBarFunctionButton.setTranslationX(0.0f);
            this.mFunctionalButton.setScaleX(1.0f);
            this.mFunctionalButton.setScaleY(1.0f);
            this.mFunctionalButton.setAlpha(1.0f);
        }
        requestLayout();
    }

    private void onToolbarSearchIconDownWhileInvisible() {
        COUISpringAnimation cOUISpringAnimation;
        if (this.mIsCollapseAnimating && this.mIconExpandCenterAnimationRange.getValue() / 1000.0f <= 0.05f && (cOUISpringAnimation = this.mIconExpandAnimation) != null && cOUISpringAnimation.isRunning()) {
            this.mIconExpandAnimation.cancelComplete();
        }
    }

    private boolean outerButtonShouldAnimate() {
        return shouldLayoutOut(this.mOuterPrimaryButton) || shouldLayoutOut(this.mOuterSecondaryButton);
    }

    private void refreshChildButtonThemeColors() {
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.refreshThemeColors();
        }
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.refreshThemeColors();
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.refreshThemeColors();
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.refreshThemeColors();
        }
    }

    private static float remapAlphaProgress(float f3, float f4) {
        if (f3 < f4 && f4 > 0.0f) {
            return f3 / f4;
        }
        return 1.0f;
    }

    private static float remapShadowProgress(float f3) {
        if (f3 <= 0.8f) {
            return 0.0f;
        }
        return (f3 - 0.8f) / 0.19999999f;
    }

    private void removeLast() {
        int childCount = this.mToolBar.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            if (getClass().isInstance(this.mToolBar.getChildAt(i3))) {
                this.mToolBar.removeViewAt(i3);
                return;
            }
        }
    }

    private void resetBallChain() {
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setScaleX(0.0f);
            this.mFunctionalButton.setScaleY(0.0f);
        }
        COUIMaterialEffect.setMetaBallParams(this.mSearchBarBackgroundView, null);
        if (this.mBallArraySet == null) {
            this.mBallArraySet = new ArraySet<>();
        }
        this.mBallArraySet.clear();
        this.mBallArraySet.add(this.mSearchBarBackgroundView);
        COUIMaterialEffect.setMetaBallChain(this.mBallArraySet);
    }

    private static COUIMaterialStrokeEffect.StrokeEffectType resolveNoEffectBaseType(COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeEffectParams) {
        if (strokeEffectParams.getStrokeType() == 2) {
            return COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT_BASE_2;
        }
        return strokeEffectParams.getStrokeType() == 1 ? COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT_BASE_1 : COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT;
    }

    private void restoreFunctionalButtonTextColor() {
        ColorStateList functionalButtonBaseTextColor = getFunctionalButtonBaseTextColor();
        if (functionalButtonBaseTextColor == null) {
            return;
        }
        this.mFunctionalButton.setTextColor(functionalButtonBaseTextColor);
    }

    private void restoreTouchDragDeformationAfterPreferenceTurnedOn() {
        if (isTransientToolbarExpandCollapseRunning()) {
            return;
        }
        COUISpringAnimation cOUISpringAnimation = this.mStateChangedAnimation;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            updateSearchBackgroundTouchDeformation(true);
            return;
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mFunBtnSpringAnimation;
        if (cOUISpringAnimation2 != null && cOUISpringAnimation2.isRunning()) {
            updateSearchBackgroundTouchDeformation(true);
            return;
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mOuterBtnSpringAnimation;
        if (cOUISpringAnimation3 == null || !cOUISpringAnimation3.isRunning()) {
            updateSearchAndButtonTouchDeformation(true);
        } else {
            updateSearchBackgroundTouchDeformation(true);
        }
    }

    private void setAlpha2Child(View view, float f3) {
        ViewGroup viewGroup;
        int childCount;
        if (!(view instanceof ViewGroup) || (childCount = (viewGroup = (ViewGroup) view).getChildCount()) <= 0) {
            view.setAlpha(f3);
            return;
        }
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if (childAt != null && childAt.getVisibility() == 0) {
                childAt.setAlpha(f3);
            }
        }
    }

    private void setBallChain() {
        if (this.mSearchViewType == 0 && !this.mIsDrawableMaterialEffect && this.mFunctionalButton != null && this.mUseMaterialEffectAttr != 0 && isSupportBlur() && isBlurEnable()) {
            int meltBallParamThirdPx = getMeltBallParamThirdPx();
            COUIMaterialEffect.setMetaBallParams(this.mSearchBarBackgroundView, new OplusMaterialMetaBallParams(1, 0, meltBallParamThirdPx, 1.5f, 2.0f));
            COUIMaterialEffect.setMetaBallParams(this.mFunctionalButton, new OplusMaterialMetaBallParams(2, 0, meltBallParamThirdPx, 1.5f, 2.0f));
            if (this.mBallArraySet == null) {
                this.mBallArraySet = new ArraySet<>();
            }
            this.mBallArraySet.clear();
            this.mBallArraySet.add(this.mSearchBarBackgroundView);
            this.mBallArraySet.add(this.mFunctionalButton);
            COUIMaterialEffect.setMetaBallChain(this.mBallArraySet);
        }
    }

    private void setBlurEffectEnable(boolean z2) {
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setBlurEffectEnable(z2);
        }
        this.mSearchBarBackgroundView.setBlurEffectEnable(z2);
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setBlurEffectEnable(z2);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setBlurEffectEnable(z2);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setBlurEffectEnable(z2);
        }
        if (this.mNoMaterialInContent && this.mState.get() == 0) {
            updateMaterial(0.0f);
        }
    }

    private void setCausticShadow(View view, float f3) {
        ShadowUtils.setElevationToView(view, 6, f3);
        int color = getResources().getColor(com.coui.appcompat.effect.R.color.coui_caustic_color_1);
        COUIMaterialEffect.setOutlineCausticShadowColor(view, Color.argb((int) (Color.alpha(color) * f3), Color.red(color), Color.green(color), Color.blue(color)));
        COUIMaterialEffect.setOutlineCausticShadowLayout(view, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_l_level2), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_t_level2), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_r_level2), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_b_level2));
        COUIMaterialEffect.setOutlineCausticShadowParams(view, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_sdf_level2));
    }

    private void setCausticShadowEffect() {
        if (this.mNoShadowInNormalState && this.mState.get() == 0) {
            updateDropShadow(0.0f);
        } else {
            updateDropShadow(1.0f);
        }
    }

    private void setFunctionalButtonTextAlpha(float f3) {
        ColorStateList functionalButtonBaseTextColor = getFunctionalButtonBaseTextColor();
        if (functionalButtonBaseTextColor == null) {
            return;
        }
        int colorForState = functionalButtonBaseTextColor.getColorForState(this.mFunctionalButton.getDrawableState(), functionalButtonBaseTextColor.getDefaultColor());
        this.mFunctionalButton.setTextColor(ColorUtils.setAlphaComponent(colorForState, Math.round(Color.alpha(colorForState) * Math.max(0.0f, Math.min(1.0f, f3)))));
    }

    private void setMenuItem(MenuItem menuItem) {
        this.mMenuItem = menuItem;
        if (menuItem == null || menuItem.getActionView() != this) {
            return;
        }
        this.mMenuItem.setActionView((View) null);
    }

    private void setOuterButtonNoBlurConfig() {
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setCrossWindowBlurEffect(this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark, this.mNoBlurSolidColor);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setCrossWindowBlurEffect(this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark, this.mNoBlurSolidColor);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setCrossWindowBlurEffect(this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark, this.mNoBlurSolidColor);
        }
    }

    private void setOuterButtonVisibility(int i3) {
        if (i3 == 0 || i3 == 4 || i3 == 8) {
            SearchBarIconButton searchBarIconButton = this.mOuterPrimaryButton;
            if (searchBarIconButton != null) {
                searchBarIconButton.setVisibility(i3);
            }
            SearchBarIconButton searchBarIconButton2 = this.mOuterSecondaryButton;
            if (searchBarIconButton2 != null) {
                searchBarIconButton2.setVisibility(i3);
            }
        }
    }

    private void setSearchBarToolbarFadeAnimating(boolean z2) {
        this.mSearchBarBackgroundView.setToolbarFadeAnimating(z2);
    }

    private void setSpotlightEffectEnable(boolean z2) {
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setSpotLightEnabled(z2);
        }
        this.mSearchBarBackgroundView.setSpotLightEnabled(z2);
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setSpotLightEnabled(z2);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setSpotLightEnabled(z2);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setSpotLightEnabled(z2);
        }
    }

    private void setStrokeEffectEnable(boolean z2) {
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setStrokeEffectEnable(z2);
        }
        this.mSearchBarBackgroundView.setStrokeEffectEnable(z2);
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setStrokeEffectEnable(z2);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setStrokeEffectEnable(z2);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setStrokeEffectEnable(z2);
        }
        if (this.mNoMaterialInContent && this.mState.get() == 0) {
            updateMaterial(0.0f);
        }
    }

    private void setToolBarAlpha(float f3) {
        COUIToolbar cOUIToolbar = this.mToolBar;
        if (cOUIToolbar != null) {
            View viewFindViewById = cOUIToolbar.findViewById(R.id.coui_toolbar_back_view);
            if (viewFindViewById != null) {
                viewFindViewById.setAlpha(f3);
            }
            COUIActionMenuView menuView = this.mToolBar.getMenuView();
            if (menuView != null) {
                applyMenuViewAlpha(menuView, f3);
            }
            View titleView = this.mToolBar.getTitleView();
            if (titleView == null || this.mIgnoreTitleViewAlpha) {
                return;
            }
            titleView.setAlpha(getToolbarTitleBaseAlpha() * f3);
        }
    }

    private void setToolBarChildVisibility(int i3) {
        COUIToolbar cOUIToolbar = this.mToolBar;
        if (cOUIToolbar != null) {
            int childCount = cOUIToolbar.getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = this.mToolBar.getChildAt(i4);
                if (childAt != this) {
                    childAt.setVisibility(i3);
                }
            }
        }
    }

    private void setToolBarScale(float f3) {
        COUIToolbar cOUIToolbar = this.mToolBar;
        if (cOUIToolbar != null) {
            float f4 = this.mToolbarIconScaleEnd;
            float f5 = f4 + ((1.0f - f4) * f3);
            applyToolbarIconExternalScale(cOUIToolbar.getNavButtonEffectDelegate(), this.mToolBar.findViewById(R.id.coui_toolbar_back_view), f5);
            COUIActionMenuView menuView = this.mToolBar.getMenuView();
            if (menuView != null) {
                applyMenuViewScale(menuView, f5);
            }
        }
    }

    private void setToolbarSearchIconAlpha(float f3) {
        ensureToolbarSearchIcon();
        View view = this.mToolbarSearchIcon;
        if (view != null) {
            view.setAlpha(f3);
        }
    }

    private boolean shouldBlockToolbarTouchPassThrough() {
        if (this.mToolBar == null || getVisibility() != 0) {
            return false;
        }
        return this.mIsExpandAnimating || this.mIsFadeInAnimating;
    }

    private boolean shouldLayoutOut(View view) {
        if (view == null) {
            return false;
        }
        if (view instanceof ImageView) {
            return (((ImageView) view).getDrawable() == null || view.getVisibility() == 8) ? false : true;
        }
        return view.getVisibility() != 8;
    }

    private void showSoftInput() {
        CustomWindowInsetsAnimationControlListener customWindowInsetsAnimationControlListener = new CustomWindowInsetsAnimationControlListener(true, this.mShowImeAnimDuration, this.mShowImeInterpolator);
        if (this.mSearchBarBackgroundView.getSearchEditText().getWindowInsetsController() != null) {
            this.mSearchBarBackgroundView.getSearchEditText().getWindowInsetsController().controlWindowInsetsAnimation(WindowInsets.Type.ime(), this.mShowImeAnimDuration, this.mShowImeInterpolator, null, customWindowInsetsAnimationControlListener);
        }
    }

    private void startAnimateToEditState() {
        openSoftInputAndNotifyStateChanged(1);
        if (functionalButtonShouldShow()) {
            this.mFunctionalButton.setVisibility(0);
            bringChildToFront(this.mFunctionalButton);
        }
        bringChildToFront(this.mSearchBarBackgroundView);
        requestLayout();
    }

    private void startAnimateToNormalState() {
        openSoftInputAndNotifyStateChanged(0);
        if (this.mSearchViewType == 0) {
            setOuterButtonVisibility(0);
        }
        requestLayout();
    }

    private void startIconExpandAnimation() {
        this.mIsExpandAnimating = true;
        this.mIsCollapseAnimating = false;
        layoutIconExpandBackgroundWithExtraPadding();
        calculateExpandLocation(true);
        this.mSearchBarBackgroundView.setTranslationX(this.mIconExpandStartX);
        setVisibility(0);
        setAlpha(1.0f);
        this.mIconExpandAnimation.setStartValue(0.0f);
        setExpandData();
        this.mIconExpandAnimation.animateToFinalPosition(1000.0f);
        updateStrokeForExpand(true);
        this.mToolbarIconSpringAnimation.setStartValue(0.0f);
        this.mToolbarIconSpringAnimation.animateToFinalPosition(1000.0f);
        OnAnimationListener onAnimationListener = this.mOnAnimationListener;
        if (onAnimationListener != null) {
            onAnimationListener.onAnimationStart(1);
        }
        bringToFront();
    }

    private void syncIconButtonMaterialEffects(SearchBarIconButton searchBarIconButton) {
        if (searchBarIconButton == null) {
            return;
        }
        searchBarIconButton.setCrossWindowBlurEffect(this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark, this.mNoBlurSolidColor);
        searchBarIconButton.setBlurEffectEnable(isBlurEnable());
        searchBarIconButton.setStrokeEffectEnable(isStrokeEnable());
        searchBarIconButton.setSpotLightEnabled(isSpotLightEnable());
    }

    private void syncLayoutDirectionFromToolbar() {
        int layoutDirection;
        COUIToolbar cOUIToolbar = this.mToolBar;
        if (cOUIToolbar == null || ViewCompat.getLayoutDirection(this) == (layoutDirection = ViewCompat.getLayoutDirection(cOUIToolbar))) {
            return;
        }
        ViewCompat.setLayoutDirection(this, layoutDirection);
    }

    private void syncNoBlurMaterialToChildren() {
        this.mSearchBarBackgroundView.setCrossWindowBlurEffect(this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark, this.mNoBlurSolidColor);
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setCrossWindowBlurEffect(this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark, this.mNoBlurSolidColor);
        }
        setOuterButtonNoBlurConfig();
    }

    private void updateAccessibilityImportance(int i3) {
        EditText searchEditText = this.mSearchBarBackgroundView.getSearchEditText();
        if (i3 == 1) {
            searchEditText.setImportantForAccessibility(1);
            searchEditText.performAccessibilityAction(64, null);
        } else if (i3 == 0) {
            clearAccessibilityFocus(this.mFunctionalButton);
            searchEditText.setImportantForAccessibility(2);
        }
    }

    private void updateBlurWithAnimationProgress(View view, float f3) {
        if (view != null && isBlurEnable()) {
            float fMax = Math.max(f3, 0.0f) * 30.0f;
            view.setRenderEffect(RenderEffect.createBlurEffect(fMax, fMax, Shader.TileMode.DECAL));
        }
    }

    private void updateButtonTouchDeformation(boolean z2) {
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        boolean z3 = false;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setTouchAnimationEnable(z2 && this.mTouchAnimationEnabled);
        }
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setTouchAnimationEnable(z2 && this.mTouchAnimationEnabled);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setTouchAnimationEnable(z2 && this.mTouchAnimationEnabled);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            if (z2 && this.mTouchAnimationEnabled) {
                z3 = true;
            }
            searchBarIconButton3.setTouchAnimationEnable(z3);
        }
    }

    private void updateCausticShadow(float f3) {
        SearchBarFunctionButton searchBarFunctionButton;
        setCausticShadow(this.mNavigationView, f3);
        if (this.mSearchViewType == 0 && (searchBarFunctionButton = this.mFunctionalButton) != null) {
            setCausticShadow(searchBarFunctionButton, f3);
        }
        setCausticShadow(this.mOuterPrimaryButton, f3);
        setCausticShadow(this.mOuterSecondaryButton, f3);
        setCausticShadow(this.mSearchBarBackgroundView, f3);
    }

    private void updateFunctionalButtonAlpha(float f3) {
        float fMax = Math.max(0.0f, Math.min(f3, 1.0f));
        this.mFunctionalButton.setAlpha(remapAlphaProgress(fMax, 0.2f));
        setFunctionalButtonTextAlpha(fMax);
    }

    private void updateLayerInToolBar(boolean z2) {
        ViewCompat.setZ(this, z2 ? 1000.0f : -1000.0f);
    }

    private void updateMaterialWithResolvedTypes(View view, float f3, COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeEffectParams, COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeEffectParams2) {
        if (view == null) {
            COUILog.e(TAG, "updateMaterial view is null");
            return;
        }
        float fMax = Math.max(0.0f, Math.min(f3, 1.0f));
        if (isBlurEnable()) {
            COUIMaterialBlurEffect.BlurEffectParams blurParamsByType = COUIMaterialEffect.getBlurParamsByType(view.getContext(), ToolbarMaterialEffectResolver.getBlurEffectType(this.mMaterialType));
            COUIMaterialEffect.setViewBlurEffect(view, COUIMaterialEffect.getBlurParamsByType(view.getContext(), COUIMaterialBlurEffect.BlurEffectType.TYPE_SEARCH_BAR_NO_MATERIAL), blurParamsByType, blurParamsByType, fMax);
        }
        if (!isStrokeEnable() || strokeEffectParams == null || strokeEffectParams2 == null) {
            return;
        }
        COUIMaterialEffect.setViewStrokeEffect(view, strokeEffectParams, strokeEffectParams2, null, fMax);
    }

    private void updateNoBlurStrokeProgress(float f3) {
        SearchBarFunctionButton searchBarFunctionButton;
        this.mSearchBarBackgroundView.setNoBlurStrokeProgress(f3);
        if (functionalButtonShouldShow() && (searchBarFunctionButton = this.mFunctionalButton) != null) {
            searchBarFunctionButton.setNoBlurStrokeProgress(f3);
        }
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setNoBlurStrokeProgress(f3);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setNoBlurStrokeProgress(f3);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setNoBlurStrokeProgress(f3);
        }
    }

    private void updateSearchAndButtonTouchDeformation(boolean z2) {
        updateSearchBackgroundTouchDeformation(z2);
        updateButtonTouchDeformation(z2);
    }

    private void updateSearchBackgroundTouchDeformation(boolean z2) {
        SearchBarBackgroundView searchBarBackgroundView = this.mSearchBarBackgroundView;
        if (searchBarBackgroundView != null) {
            searchBarBackgroundView.setTouchAnimationEnable(z2 && this.mTouchAnimationEnabled);
        }
    }

    private void updateStrokeForExpand(boolean z2) {
        if (!this.mIsDrawableMaterialEffect && COUIMaterialStateManager.INSTANCE.materialVersionCheck() && isStrokeEnable()) {
            if (z2) {
                COUIMaterialEffect.setViewStrokeEffect(this.mSearchBarBackgroundView, ToolbarMaterialEffectResolver.getCapsuleStrokeEffectType(this.mMaterialType));
            } else {
                COUIMaterialEffect.setViewStrokeEffect(this.mSearchBarBackgroundView, ToolbarMaterialEffectResolver.getCircleStrokeEffectType(this.mMaterialType));
            }
        }
    }

    private void updateToolbarSearchIconRef(View view) {
        View view2 = this.mToolbarSearchIcon;
        if (view2 == view) {
            return;
        }
        clearCollapseTailPressHook(view2);
        this.mToolbarSearchIcon = view;
        bindCollapseTailPressHook();
    }

    private void updateViewShadow(float f3) {
        SearchBarFunctionButton searchBarFunctionButton;
        ShadowUtils.setElevationToView(this.mNavigationView, 5, f3);
        if (this.mSearchViewType == 0 && (searchBarFunctionButton = this.mFunctionalButton) != null) {
            ShadowUtils.setElevationToView(searchBarFunctionButton, 5, f3);
        }
        ShadowUtils.setElevationToView(this.mOuterPrimaryButton, 5, f3);
        ShadowUtils.setElevationToView(this.mOuterSecondaryButton, 5, f3);
        ShadowUtils.setElevationToView(this.mSearchBarBackgroundView, 5, f3);
    }

    private Drawable zoomDrawable(Drawable drawable, int i3, int i4) {
        return new BitmapDrawable(Bitmap.createScaledBitmap(drawableToBitmap(drawable), i3, i4, true));
    }

    public void addOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        if (this.mOnStateChangeListeners == null) {
            this.mOnStateChangeListeners = new ArrayList();
        }
        this.mOnStateChangeListeners.add(onStateChangeListener);
    }

    public void calculateFunBtnPosition(View view, int[] iArr, boolean z2) {
        if (!functionalButtonShouldShow() || this.mFunctionalButton == null) {
            return;
        }
        float width = iArr[0] + (view.getWidth() / 2.0f);
        int[] iArr2 = new int[2];
        getLayoutLocationInWindow(this.mFunctionalButton, iArr2);
        int width2 = (int) (width - (iArr2[0] + (this.mFunctionalButton.getWidth() / 2.0f)));
        this.mFunctionalButtonStartX = width2;
        this.mFunctionalButtonTargetX = 0;
        if (z2) {
            this.mFunctionalButton.setTranslationX(width2);
            this.mFunctionalButton.setScaleX(0.0f);
            this.mFunctionalButton.setScaleY(0.0f);
        }
    }

    public void cancelToolBarAnimation() {
        COUISpringAnimation cOUISpringAnimation = this.mIconExpandAnimation;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            this.mIconExpandAnimation.cancelComplete();
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mFadeInFadeOutAnimation;
        if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
            return;
        }
        this.mFadeInFadeOutAnimation.cancelComplete();
    }

    public void changeState(int i3, boolean z2) {
        if (z2) {
            changeStateWithAnimation(i3);
        } else {
            changeStateImmediately(i3);
        }
    }

    public void changeStateImmediately(int i3) {
        if (this.mState.get() == i3) {
            COUILog.d(TAG, "changeStateImmediately: same state , return. targetState = " + i3);
            return;
        }
        if (this.mState.get() == 1) {
            startAnimateToNormalState();
            endAnimateToNormalState();
        } else if (this.mState.get() == 0) {
            startAnimateToEditState();
            endAnimateToEditState();
        }
    }

    public void changeStateWithAnimation(int i3) {
        if (this.mState.get() == i3) {
            COUILog.d(TAG, "changeStateWithAnimation: same state , return. targetState = " + i3);
            return;
        }
        if (this.mSearchViewType == 1) {
            changeStateImmediately(i3);
            return;
        }
        setBallChain();
        updateButtonTouchDeformation(false);
        if (i3 == 0) {
            if (!this.mStateChangedAnimation.isRunning() && !this.mFunBtnSpringAnimation.isRunning() && !this.mOuterBtnSpringAnimation.isRunning()) {
                initOvLinkNormalParams();
                this.mStateToNormalAnimation = true;
                startToNormalAnimator();
                OnAnimationListener onAnimationListener = this.mOnAnimationListener;
                if (onAnimationListener != null) {
                    onAnimationListener.onAnimationStart(0);
                    return;
                }
                return;
            }
            openSoftInputAndNotifyStateChanged(0);
            this.mStateAnimEnded = false;
            this.mFunBtnAnimEnded = !functionalButtonShouldShow();
            this.mOuterBtnAnimEnded = !outerButtonShouldAnimate();
            this.mStateChangedAnimation.animateToFinalPosition(0.0f);
            if (functionalButtonShouldShow()) {
                this.mFunBtnSpringAnimation.animateToFinalPosition(0.0f);
            }
            if (outerButtonShouldAnimate()) {
                this.mOuterBtnSpringAnimation.animateToFinalPosition(0.0f);
            }
            OnAnimationListener onAnimationListener2 = this.mOnAnimationListener;
            if (onAnimationListener2 != null) {
                onAnimationListener2.onAnimationStart(0);
                return;
            }
            return;
        }
        if (i3 == 1) {
            if (!this.mStateChangedAnimation.isRunning() && !this.mFunBtnSpringAnimation.isRunning() && !this.mOuterBtnSpringAnimation.isRunning()) {
                initOvLinkEditParams();
                this.mStateToEditAnimation = true;
                startAnimateToEditState();
                OnAnimationListener onAnimationListener3 = this.mOnAnimationListener;
                if (onAnimationListener3 != null) {
                    onAnimationListener3.onAnimationStart(1);
                    return;
                }
                return;
            }
            openSoftInputAndNotifyStateChanged(1);
            this.mStateAnimEnded = false;
            this.mFunBtnAnimEnded = !functionalButtonShouldShow();
            this.mOuterBtnAnimEnded = !outerButtonShouldAnimate();
            this.mStateChangedAnimation.animateToFinalPosition(1000.0f);
            if (functionalButtonShouldShow()) {
                this.mFunBtnSpringAnimation.animateToFinalPosition(1000.0f);
            }
            if (outerButtonShouldAnimate()) {
                this.mOuterBtnSpringAnimation.animateToFinalPosition(1000.0f);
            }
            OnAnimationListener onAnimationListener4 = this.mOnAnimationListener;
            if (onAnimationListener4 != null) {
                onAnimationListener4.onAnimationStart(1);
            }
        }
    }

    public void controlImeShowAnim(int i3, Interpolator interpolator) {
        this.mShowImeAnimDuration = i3;
        this.mShowImeInterpolator = interpolator;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent) || shouldBlockToolbarTouchPassThrough();
    }

    public TextView getFunctionalButton() {
        return this.mFunctionalButton;
    }

    public COUIHintAnimationLayout getHintAnimationLayout() {
        return this.mSearchBarBackgroundView.getHintAnimationLayout();
    }

    public View getInnerPrimaryButton() {
        return this.mSearchBarBackgroundView.getInnerPrimaryButton();
    }

    public View getInnerSecondaryButton() {
        return this.mSearchBarBackgroundView.getInnerSecondaryButton();
    }

    public boolean getInputMethodAnimationEnabled() {
        return this.mInputMethodAnimationEnabled;
    }

    public View getNavigationView() {
        return this.mNavigationView;
    }

    public View getOuterPrimaryButton() {
        return this.mOuterPrimaryButton;
    }

    public View getOuterSecondaryButton() {
        return this.mOuterSecondaryButton;
    }

    public View getQuickDeleteButton() {
        return this.mSearchBarBackgroundView.getQuickDeleteButton();
    }

    public EditText getSearchEditText() {
        return this.mSearchBarBackgroundView.getSearchEditText();
    }

    public int getSearchState() {
        return this.mState.get();
    }

    @Deprecated
    public float getSearchViewAnimateHeightPercent() {
        return 1.0f;
    }

    public void hideInToolBar() {
        if (this.mToolBar == null) {
            return;
        }
        syncLayoutDirectionFromToolbar();
        this.mSearchBarBackgroundView.setTouchAnimationReset(false);
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setTouchAnimationReset(false);
        }
        updateSearchAndButtonTouchDeformation(false);
        if (this.mFadeInFadeOutAnimation.isRunning()) {
            updateLayerInToolBar(false);
            openSoftInputAndNotifyStateChanged(0);
            this.mIsFadeInAnimating = false;
            setSearchBarToolbarFadeAnimating(true);
            this.mFadeInFadeOutAnimation.animateToFinalPosition(0.0f);
            return;
        }
        if (this.mIconExpandAnimation.isRunning()) {
            updateLayerInToolBar(false);
            openSoftInputAndNotifyStateChanged(0);
            setCollapseData();
            this.mIsCollapseAnimating = true;
            this.mIsExpandAnimating = false;
            setToolbarSearchIconAlpha(0.0f);
            this.mIconExpandAnimation.animateToFinalPosition(0.0f);
            updateStrokeForExpand(false);
            this.mToolbarIconSpringAnimation.animateToFinalPosition(0.0f);
            return;
        }
        openSoftInputAndNotifyStateChanged(0);
        updateToolbarSearchIconRef(findToolbarSearchIcon());
        if (isBlurEnable()) {
            updateLayerInToolBar(false);
            this.mIsExpandAnimating = false;
            setToolBarChildVisibility(0);
            this.mIconStartCollapse = true;
            initCollapseData();
            requestLayout();
        } else {
            updateLayerInToolBar(false);
            setVisibility(0);
            setToolBarChildVisibility(0);
            this.mFadeOutToolbar = true;
            this.mIsFadeInAnimating = false;
            setSearchBarToolbarFadeAnimating(true);
            initFadeOutData();
            requestLayout();
        }
        this.mSearchBarBackgroundView.getSearchEditText().setText((CharSequence) null);
    }

    public void ignoreToolbarTitleViewAlpha(boolean z2) {
        this.mIgnoreTitleViewAlpha = z2;
    }

    public void initCollapseData() {
        setCollapseData();
    }

    public void initExpandData() {
        setExpandData();
    }

    public void initFadeInData() {
        this.mFadeInSpringForceX.setResponse(this.mIconExpandToEditParams.getResponse());
        this.mFadeInSpringForceX.setBounce(this.mIconExpandToEditParams.getBounce());
        this.mToolbarIconScaleEnd = this.mIconExpandToEditParams.getToolbarEndScale();
        COUISpringAnimation cOUISpringAnimation = this.mFadeInFadeOutAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setMinimumVisibleChange(this.mIconExpandToEditParams.getFadeSpringMinimumVisibleChange());
        }
    }

    public void initFadeOutData() {
        this.mFadeInSpringForceX.setResponse(this.mIconCollapseToNormalParams.getResponse());
        this.mFadeInSpringForceX.setBounce(this.mIconCollapseToNormalParams.getBounce());
        this.mToolbarIconScaleEnd = this.mIconCollapseToNormalParams.getToolbarInitScale();
        COUISpringAnimation cOUISpringAnimation = this.mFadeInFadeOutAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setMinimumVisibleChange(this.mIconCollapseToNormalParams.getFadeSpringMinimumVisibleChange());
        }
    }

    public void initOuterBtnAnimate() {
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(this.mOuterBtnAnimationValue);
        this.mOuterBtnSpringAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setMinimumVisibleChange(this.mChangedToEditParams.getOuterBtnMinimumVisibleChange());
        this.mOuterBtnSpringAnimation.setSpring(this.mOuterBtnSpringForce);
        this.mOuterBtnSpringAnimation.addUpdateListener(new c(this));
        this.mOuterBtnSpringAnimation.addEndListener(new d(this));
    }

    public void initOvLinkEditParams() {
        this.mStateAnimationSpringForceX.setResponse(this.mChangedToEditParams.getResponse());
        this.mStateAnimationSpringForceX.setBounce(this.mChangedToEditParams.getBounce());
        this.mFunctionalInitScale = this.mChangedToEditParams.getFunctionalButtonInitScale();
        this.mOuterButtonInitScale = this.mChangedToEditParams.getOuterButtonEndScale();
        this.mFunBtnSpringForce.setResponse(this.mChangedToEditParams.getFunBtnResponse());
        this.mFunBtnSpringForce.setBounce(this.mChangedToEditParams.getFunBtnBounce());
        this.mOuterBtnSpringForce.setResponse(this.mChangedToEditParams.getOuterBtnResponse());
        this.mOuterBtnSpringForce.setBounce(this.mChangedToEditParams.getOuterBtnBounce());
        COUISpringAnimation cOUISpringAnimation = this.mStateChangedAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setMinimumVisibleChange(this.mChangedToEditParams.getStateSpringMinimumVisibleChange());
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mFunBtnSpringAnimation;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.setMinimumVisibleChange(this.mChangedToEditParams.getFunBtnMinimumVisibleChange());
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mOuterBtnSpringAnimation;
        if (cOUISpringAnimation3 != null) {
            cOUISpringAnimation3.setMinimumVisibleChange(this.mChangedToEditParams.getOuterBtnMinimumVisibleChange());
        }
    }

    public void initOvLinkNormalParams() {
        this.mStateAnimationSpringForceX.setResponse(this.mChangedToNormalParams.getResponse());
        this.mStateAnimationSpringForceX.setBounce(this.mChangedToNormalParams.getBounce());
        this.mFunctionalInitScale = this.mChangedToNormalParams.getFunctionalButtonEndScale();
        this.mOuterButtonInitScale = this.mChangedToNormalParams.getOuterButtonInitScale();
        this.mFunBtnSpringForce.setResponse(this.mChangedToNormalParams.getFunBtnResponse());
        this.mFunBtnSpringForce.setBounce(this.mChangedToNormalParams.getFunBtnBounce());
        this.mOuterBtnSpringForce.setResponse(this.mChangedToNormalParams.getOuterBtnResponse());
        this.mOuterBtnSpringForce.setBounce(this.mChangedToNormalParams.getOuterBtnBounce());
        COUISpringAnimation cOUISpringAnimation = this.mStateChangedAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setMinimumVisibleChange(this.mChangedToNormalParams.getStateSpringMinimumVisibleChange());
        }
        COUISpringAnimation cOUISpringAnimation2 = this.mFunBtnSpringAnimation;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.setMinimumVisibleChange(this.mChangedToNormalParams.getFunBtnMinimumVisibleChange());
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mOuterBtnSpringAnimation;
        if (cOUISpringAnimation3 != null) {
            cOUISpringAnimation3.setMinimumVisibleChange(this.mChangedToNormalParams.getOuterBtnMinimumVisibleChange());
        }
    }

    public final boolean isFadeAnimateRunning() {
        COUISpringAnimation cOUISpringAnimation = this.mFadeInFadeOutAnimation;
        return cOUISpringAnimation != null && cOUISpringAnimation.isRunning();
    }

    public boolean isStateChangedAnimRunning() {
        return this.mStateChangedAnimation.isRunning() || this.mFunBtnSpringAnimation.isRunning() || this.mOuterBtnSpringAnimation.isRunning();
    }

    @Override // android.view.CollapsibleActionView
    public void onActionViewCollapsed() {
    }

    @Override // android.view.CollapsibleActionView
    public void onActionViewExpanded() {
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        boolean z2 = this.mLastOrientation != configuration.orientation;
        boolean z3 = (this.mLastScreenWidthDp == configuration.screenWidthDp && this.mLastSmallestScreenWidthDp == configuration.smallestScreenWidthDp) ? false : true;
        if (z2 || z3) {
            this.mIconExpandAnimation.cancelComplete();
            this.mStateChangedAnimation.cancelComplete();
            this.mFunBtnSpringAnimation.cancelComplete();
            this.mOuterBtnSpringAnimation.cancelComplete();
            this.mFadeInFadeOutAnimation.cancelComplete();
        }
        this.mLastOrientation = configuration.orientation;
        this.mLastScreenWidthDp = configuration.screenWidthDp;
        this.mLastSmallestScreenWidthDp = configuration.smallestScreenWidthDp;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        clearCollapseTailPressHook(this.mToolbarSearchIcon);
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // com.coui.appcompat.searchview.ImeInsetsAnimationCallback.OnImeAnimationListener
    public void onImeAnimStart() {
        if (this.mSearchBarBackgroundView.getSearchEditText().getRootWindowInsets() == null || !this.mSearchBarBackgroundView.getSearchEditText().getRootWindowInsets().isVisible(WindowInsets.Type.ime())) {
            return;
        }
        changeStateWithAnimation(1);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mSearchBarBackgroundView.getSearchEditText().getImportantForAccessibility() == 1) {
            return;
        }
        String str = "";
        if (!TextUtils.isEmpty(this.mSearchBarContentDesc)) {
            str = "" + this.mSearchBarContentDesc;
        }
        if (this.mSearchBarBackgroundView.getHintLayoutMayBeNull() != null && this.mSearchBarBackgroundView.getHintLayoutMayBeNull().getCurrentHintTextView() != null) {
            str = str + "," + this.mSearchBarHintTitleDesc + ((Object) this.mSearchBarBackgroundView.getHintLayoutMayBeNull().getCurrentHintTextView().getText());
        }
        accessibilityNodeInfo.setContentDescription(str);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        if (this.mStateChangedAnimation.isRunning() || this.mFunBtnSpringAnimation.isRunning() || this.mOuterBtnSpringAnimation.isRunning() || this.mIconExpandAnimation.isRunning() || this.mToolbarIconSpringAnimation.isRunning() || this.mFadeInFadeOutAnimation.isRunning()) {
            return;
        }
        layoutBackgroundLeft();
        layoutBackgroundArea();
        layoutBackgroundRight();
        SearchBarBackgroundView searchBarBackgroundView = this.mSearchBarBackgroundView;
        Rect rect = this.mBackgroundRect;
        searchBarBackgroundView.initDrawingBounds(0, 0, rect.right - rect.left, rect.bottom - rect.top);
        if (this.mStateToNormalAnimation || this.mStateToEditAnimation) {
            handleStateChangedAnimation();
        }
        if (this.mIconStartExpand) {
            this.mIconStartExpand = false;
            setToolbarSearchIconAlpha(0.0f);
            startIconExpandAnimation();
        } else if (this.mIconStartCollapse) {
            layoutIconExpandBackgroundWithExtraPadding();
            calculateExpandLocation(false);
            this.mIconStartCollapse = false;
            SearchBarBackgroundView searchBarBackgroundView2 = this.mSearchBarBackgroundView;
            float f3 = this.mIconInitialWidth;
            float f4 = this.mSearchBarFinalWidth;
            searchBarBackgroundView2.updateIconExpandClippingWidth(f3, f4, f4);
            this.mSearchBarBackgroundView.setIconExpandClip(true);
            this.mSearchBarBackgroundView.setTranslationX(0.0f);
            setCollapseData();
            this.mIsCollapseAnimating = true;
            this.mIsExpandAnimating = false;
            setToolbarSearchIconAlpha(0.0f);
            this.mIconExpandAnimation.setStartValue(1000.0f);
            this.mIconExpandAnimation.animateToFinalPosition(0.0f);
            updateStrokeForExpand(false);
            this.mToolbarIconSpringAnimation.animateToFinalPosition(0.0f);
            OnAnimationListener onAnimationListener = this.mOnAnimationListener;
            if (onAnimationListener != null) {
                onAnimationListener.onAnimationStart(0);
            }
        } else if (this.mFadeInToolbar) {
            this.mFadeInToolbar = false;
            this.mIsFadeInAnimating = true;
            setSearchBarToolbarFadeAnimating(true);
            this.mIsCollapseAnimating = false;
            this.mFadeInFadeOutAnimation.animateToFinalPosition(1000.0f);
            OnAnimationListener onAnimationListener2 = this.mOnAnimationListener;
            if (onAnimationListener2 != null) {
                onAnimationListener2.onAnimationStart(1);
            }
        } else if (this.mFadeOutToolbar) {
            this.mFadeOutToolbar = false;
            this.mIsFadeInAnimating = false;
            setSearchBarToolbarFadeAnimating(true);
            this.mFadeInFadeOutAnimation.animateToFinalPosition(0.0f);
            OnAnimationListener onAnimationListener3 = this.mOnAnimationListener;
            if (onAnimationListener3 != null) {
                onAnimationListener3.onAnimationStart(0);
            }
        }
        boolean z3 = this.mNoShadowInNormalState;
        if (!z3) {
            updateDropShadow(1.0f);
        } else if (z3 && !this.mStateToNormalAnimation && !this.mStateToEditAnimation && this.mState.get() == 0) {
            updateDropShadow(0.0f);
        }
        if (this.mNoMaterialInContent && this.mState.get() == 0) {
            updateMaterial(0.0f);
        }
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            this.mSystemStrokeState = z2;
            setStrokeEffectEnable(isStrokeEnable());
            return;
        }
        if (i3 == 2) {
            this.mSystemBlurState = z2;
            setBlurEffectEnable(isBlurEnable());
        } else if (i3 == 4) {
            this.mSystemSpotLightState = z2;
            setSpotlightEffectEnable(isSpotLightEnable());
        } else if (i3 == 8) {
            this.mSystemCausticShadowState = z2;
            setCausticShadowEffect();
        }
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        setMeasuredDimension(View.MeasureSpec.getSize(i3), View.MeasureSpec.getSize(i4));
        configResponsive();
        measureBackground(measureOutsideBackground());
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (shouldBlockToolbarTouchPassThrough()) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void openSoftInput(boolean z2) {
        if (this.mSearchBarBackgroundView.getSearchEditText() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(SystemUpdateScreenStateConfig.KEY_INPUT_METHOD);
            COUILog.d(TAG, "openSoftInput: " + z2);
            if (!z2) {
                this.mSearchBarBackgroundView.getSearchEditText().clearFocus();
                if (inputMethodManager == null || !inputMethodManager.isActive()) {
                    return;
                }
                inputMethodManager.hideSoftInputFromWindow(this.mSearchBarBackgroundView.getSearchEditText().getWindowToken(), 0);
                return;
            }
            this.mSearchBarBackgroundView.getSearchEditText().requestFocus();
            if (inputMethodManager != null) {
                if (this.mIsAtLeastR && this.mShowImeAnimDuration != 0 && !isInMultiWindowMode()) {
                    showSoftInput();
                    return;
                }
                WindowInsetsController windowInsetsController = this.mSearchBarBackgroundView.getSearchEditText().getWindowInsetsController();
                if (windowInsetsController != null) {
                    windowInsetsController.show(WindowInsets.Type.ime());
                }
            }
        }
    }

    public void openSoftInputAndNotifyStateChanged(int i3) {
        if (i3 != 1) {
            if (i3 == 0) {
                this.mSearchBarBackgroundView.getSearchEditText().setText((CharSequence) null);
                if (this.mInputMethodAnimationEnabled) {
                    openSoftInput(false);
                }
                notifyOnStateChange(1, 0);
                return;
            }
            return;
        }
        if (this.mInputMethodAnimationEnabled) {
            if (isBlurEnable() && !this.mIsDrawableMaterialEffect && this.mIsExpandAnimating) {
                post(new h(this));
            } else {
                openSoftInput(true);
            }
        }
        notifyOnStateChange(0, 1);
    }

    public void prepareForToolbarStructureChange() {
        if (this.mToolBar != null && isTransientToolbarExpandCollapseRunning()) {
            COUISpringAnimation cOUISpringAnimation = this.mIconExpandAnimation;
            if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
                this.mIconExpandAnimation.cancelComplete();
            }
            COUISpringAnimation cOUISpringAnimation2 = this.mFadeInFadeOutAnimation;
            if (cOUISpringAnimation2 == null || !cOUISpringAnimation2.isRunning()) {
                return;
            }
            this.mFadeInFadeOutAnimation.cancelComplete();
        }
    }

    public void refresh() {
        Context context = getContext();
        int i3 = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? R.style.Widget_COUI_COUISearchBar_Dark : R.style.Widget_COUI_COUISearchBar;
        loadAttr(context, this.mAttrs, this.mStyle, i3);
        this.mSearchBarBackgroundView.loadAttr(context, this.mAttrs, this.mStyle, i3);
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            this.mNormalBackgroundColor = ToolbarMaterialEffectResolver.getToolbarBackgroundColor(context, this.mMaterialType);
        } else {
            this.mNormalBackgroundColor = ResourcesCompat.getColor(context.getResources(), R.color.coui_search_view_selector_color_normal, context.getTheme());
        }
        this.mSearchBarBackgroundView.setCurrentBackgroundColor(this.mNormalBackgroundColor);
        this.mSearchBarBackgroundView.setButtonDividerColor(COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorDivider));
        this.mBackgroundColor.setColor(this.mNormalBackgroundColor);
        syncNoBlurMaterialToChildren();
        this.mSearchBarBackgroundView.refresh();
        refreshChildButtonThemeColors();
    }

    public void removeOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        List<OnStateChangeListener> list = this.mOnStateChangeListeners;
        if (list == null) {
            return;
        }
        list.remove(onStateChangeListener);
    }

    public void resetViewState(View view) {
        view.setAlpha(1.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setTranslationX(0.0f);
        view.setRenderEffect(null);
    }

    public void setAtBehindToolBar(COUIToolbar cOUIToolbar, int i3, MenuItem menuItem) {
        this.mToolBar = cOUIToolbar;
        this.mGravityInToolBar = i3;
        this.mAddToToolbarWay = 1;
        this.mSearchBarBackgroundView.setAddToToolbarAtBehind(true);
        setMenuItem(menuItem);
        setVisibility(8);
        COUIToolbar cOUIToolbar2 = this.mToolBar;
        if (cOUIToolbar2 != null) {
            cOUIToolbar2.setSearchFullWidthFlag(true);
        }
        updateToolbarSearchIconRef(findToolbarSearchIcon());
    }

    public void setAtFrontToolBar(COUIToolbar cOUIToolbar, int i3, MenuItem menuItem) {
        this.mToolBar = cOUIToolbar;
        this.mGravityInToolBar = i3;
        this.mAddToToolbarWay = 2;
        this.mSearchBarBackgroundView.setAddToToolbarAtBehind(false);
        setMenuItem(menuItem);
        ensureAddedToToolBar();
        menuItem.setVisible(false);
    }

    public void setCollapseData() {
        this.mExpandSpringForceX.setResponse(this.mIconCollapseToNormalParams.getResponse());
        this.mExpandSpringForceX.setBounce(this.mIconCollapseToNormalParams.getBounce());
        this.mFunctionalInitScaleExpand = this.mIconCollapseToNormalParams.getFunctionalButtonEndScale();
        configureToolbarIconSpringForCollapse();
        COUISpringAnimation cOUISpringAnimation = this.mIconExpandAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setMinimumVisibleChange(this.mIconCollapseToNormalParams.getIconExpandSpringMinimumVisibleChange());
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setEnabled(z2);
        }
        this.mSearchBarBackgroundView.setEnabled(z2);
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setEnabled(z2);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setEnabled(z2);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setEnabled(z2);
        }
    }

    public void setExpandData() {
        this.mExpandSpringForceX.setResponse(this.mIconExpandToEditParams.getResponse());
        this.mExpandSpringForceX.setBounce(this.mIconExpandToEditParams.getBounce());
        this.mFunctionalInitScaleExpand = this.mIconExpandToEditParams.getFunctionalButtonInitScale();
        configureToolbarIconSpringForExpand();
        COUISpringAnimation cOUISpringAnimation = this.mIconExpandAnimation;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.setMinimumVisibleChange(this.mIconExpandToEditParams.getIconExpandSpringMinimumVisibleChange());
        }
    }

    @Deprecated
    public void setExtraActivateMarginTop(int i3) {
    }

    public void setFunctionalButtonText(String str) {
        if (functionalButtonShouldShow()) {
            this.mFunctionalButton.setText(str);
        }
    }

    public void setHideFunctionalButton(boolean z2) {
        if (this.mHideFunctionalButton != z2) {
            this.mHideFunctionalButton = z2;
            if (z2) {
                this.mFunctionalButton.setVisibility(8);
            } else {
                if (this.mFunctionalButton == null) {
                    initFunctionButton();
                }
                this.mFunctionalButton.setVisibility(0);
            }
            requestLayout();
        }
    }

    public void setImeInsetsAnimationCallback() {
        if (this.mIsAtLeastR) {
            this.mShowImeAnimDuration = 450;
            this.mShowImeInterpolator = DEFAULT_SEARCH_VIEW_OFFSET_CHANGE_INTERPOLATOR;
            ImeInsetsAnimationCallback imeInsetsAnimationCallback = new ImeInsetsAnimationCallback();
            imeInsetsAnimationCallback.setImeAnimationListener(this);
            this.mSearchBarBackgroundView.getSearchEditText().post(new j(this, imeInsetsAnimationCallback));
        }
    }

    public void setInnerPrimaryButton(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int i3 = this.mIconMaxWidth;
        if (intrinsicWidth > i3) {
            drawable = zoomDrawable(drawable, (int) (i3 * getResources().getDisplayMetrics().density), (int) (this.mIconMaxHeight * getResources().getDisplayMetrics().density));
        }
        if (this.mSearchBarBackgroundView.getInnerPrimaryButton() == null) {
            this.mSearchBarBackgroundView.setInnerPrimaryButton(ensureImageView(drawable, true, true, this.mInnerIconSize / 2));
        }
        if (this.mSearchBarBackgroundView.getInnerPrimaryButton() != null) {
            configImageViewDrawable(this.mSearchBarBackgroundView.getInnerPrimaryButton(), drawable, this.mInnerIconSize);
        }
    }

    public void setInnerSecondaryButton(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int i3 = this.mIconMaxWidth;
        if (intrinsicWidth > i3) {
            drawable = zoomDrawable(drawable, (int) (i3 * getResources().getDisplayMetrics().density), (int) (this.mIconMaxHeight * getResources().getDisplayMetrics().density));
        }
        if (this.mSearchBarBackgroundView.getInnerSecondaryButton() == null) {
            this.mSearchBarBackgroundView.setInnerSecondaryButton(ensureImageView(drawable, true, true, this.mInnerIconSize / 2));
        }
        if (this.mSearchBarBackgroundView.getInnerSecondaryButton() != null) {
            configImageViewDrawable(this.mSearchBarBackgroundView.getInnerSecondaryButton(), drawable, this.mInnerIconSize);
        }
    }

    public void setInputMethodAnimationEnabled(boolean z2) {
        this.mInputMethodAnimationEnabled = z2;
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        if (this.mIsDrawableMaterialEffect == z2) {
            return;
        }
        this.mIsDrawableMaterialEffect = z2;
        syncNoBlurMaterialToChildren();
    }

    public void setMaterialEffectType(int i3) {
        if (this.mMaterialType == i3) {
            return;
        }
        this.mMaterialType = i3;
        dispatchMaterialEffectType();
    }

    @Deprecated
    public void setMaterialForceState(int i3) {
        setUseMaterialEffectAttr(i3);
    }

    public void setNavigationViewDrawable(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int i3 = this.mIconMaxWidth;
        if (intrinsicWidth > i3) {
            drawable = zoomDrawable(drawable, (int) (i3 * getResources().getDisplayMetrics().density), (int) (this.mIconMaxHeight * getResources().getDisplayMetrics().density));
        }
        ensureNavigationView();
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            configImageViewDrawable(searchBarIconButton, drawable, this.mOuterButtonWidth);
        }
    }

    @Deprecated
    public void setNoBlurEffect(boolean z2) {
        setIsDrawableMaterialEffect(z2);
    }

    public void setNoBlurSolidColor(int i3) {
        if (this.mNoBlurSolidColor == i3) {
            return;
        }
        this.mNoBlurSolidColor = i3;
        if (this.mIsDrawableMaterialEffect) {
            syncNoBlurMaterialToChildren();
        }
    }

    public void setNoMaterialInContent(boolean z2) {
        this.mNoMaterialInContent = z2;
    }

    public void setNoShadowInNormalState(boolean z2) {
        this.mNoShadowInNormalState = z2;
    }

    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
        this.mOnAnimationListener = onAnimationListener;
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mSearchBarBackgroundView.setOnClickListener(onClickListener);
    }

    public void setOnSearchBarBackgroundBoundsChangedListener(OnSearchBarBackgroundBoundsChangedListener onSearchBarBackgroundBoundsChangedListener) {
        this.mOnSearchBarBackgroundBoundsChangedListener = onSearchBarBackgroundBoundsChangedListener;
    }

    public void setOuterPrimaryButton(Drawable drawable) {
        if (drawable == null) {
            SearchBarIconButton searchBarIconButton = this.mOuterPrimaryButton;
            if (searchBarIconButton != null) {
                removeView(searchBarIconButton);
                this.mOuterPrimaryButton = null;
                return;
            }
            return;
        }
        if (this.mOuterPrimaryButton == null) {
            SearchBarIconButton searchBarIconButton2 = new SearchBarIconButton(getContext(), this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark);
            this.mOuterPrimaryButton = searchBarIconButton2;
            searchBarIconButton2.setMaterialEffectType(this.mMaterialType);
            addView(this.mOuterPrimaryButton);
            syncIconButtonMaterialEffects(this.mOuterPrimaryButton);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterPrimaryButton;
        if (searchBarIconButton3 != null) {
            configImageViewDrawable(searchBarIconButton3, drawable, this.mOuterButtonWidth);
            setOuterButtonNoBlurConfig();
        }
    }

    public void setOuterSecondaryButton(Drawable drawable) {
        if (drawable == null) {
            SearchBarIconButton searchBarIconButton = this.mOuterSecondaryButton;
            if (searchBarIconButton != null) {
                removeView(searchBarIconButton);
                this.mOuterSecondaryButton = null;
                return;
            }
            return;
        }
        if (this.mOuterSecondaryButton == null) {
            SearchBarIconButton searchBarIconButton2 = new SearchBarIconButton(getContext(), this.mIsDrawableMaterialEffect, this.mStrokeEffectForceDark);
            this.mOuterSecondaryButton = searchBarIconButton2;
            searchBarIconButton2.setMaterialEffectType(this.mMaterialType);
            addView(this.mOuterSecondaryButton);
            syncIconButtonMaterialEffects(this.mOuterSecondaryButton);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            configImageViewDrawable(searchBarIconButton3, drawable, this.mOuterButtonWidth);
            setOuterButtonNoBlurConfig();
        }
    }

    public void setSearchAnimateType(int i3) {
        if (this.mState.get() == 1) {
            COUILog.d(TAG, "setSearchAnimateType to " + TYPE_NAME[i3] + " is not allowed in STATE_EDIT");
            return;
        }
        this.mSearchViewType = i3;
        this.mSearchBarBackgroundView.setSearchAnimateType(i3);
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            if (this.mSearchViewType == 0) {
                this.mSearchBarBackgroundView.removeView(searchBarFunctionButton);
                removeView(this.mFunctionalButton);
                addView(this.mFunctionalButton);
            } else {
                this.mSearchBarBackgroundView.removeView(searchBarFunctionButton);
                removeView(this.mFunctionalButton);
                this.mSearchBarBackgroundView.addView(this.mFunctionalButton);
            }
            this.mFunctionalButton.setSearchAnimateType(this.mSearchViewType);
        }
        requestLayout();
    }

    public void setSearchBackgroundColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            int i3 = this.mNormalBackgroundColor;
            this.mNormalBackgroundColor = colorStateList.getDefaultColor();
            if (this.mSearchBarBackgroundView.getCurrentBackgroundColor() == i3) {
                this.mSearchBarBackgroundView.setCurrentBackgroundColor(this.mNormalBackgroundColor);
            }
            this.mBackgroundColor.setColor(this.mNormalBackgroundColor);
            if (this.mIsDrawableMaterialEffect) {
                setNoBlurSolidColor(i3);
            }
            invalidate();
        }
    }

    @Deprecated
    public void setSearchViewAnimateHeightPercent(float f3) {
    }

    public void setSearchViewIcon(Drawable drawable) {
        configImageViewDrawable(this.mSearchBarBackgroundView.getSearchIconView(), drawable, this.mInnerIconSize);
    }

    public void setSpotLightEffectType(COUISpotLightEffect.SpotLightType spotLightType, Context context) {
        this.mSearchBarBackgroundView.setSpotLightEffectType(spotLightType, context);
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setSpotLightEffectType(spotLightType, context);
        }
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setSpotLightEffectType(spotLightType, context);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setSpotLightEffectType(spotLightType, context);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setSpotLightEffectType(spotLightType, context);
        }
    }

    public void setStrokeEffectForceDark(boolean z2) {
        if (this.mStrokeEffectForceDark == z2) {
            return;
        }
        this.mStrokeEffectForceDark = z2;
        syncNoBlurMaterialToChildren();
    }

    public void setStrokeEffectType(COUIMaterialStrokeEffect.StrokeEffectType strokeEffectType, COUIMaterialStrokeEffect.StrokeEffectType strokeEffectType2, Context context) {
        this.mSearchBarBackgroundView.setStrokeEffectType(strokeEffectType, context);
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setStrokeEffectType(strokeEffectType, context);
        }
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setStrokeEffectType(strokeEffectType2, context);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setStrokeEffectType(strokeEffectType2, context);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setStrokeEffectType(strokeEffectType2, context);
        }
    }

    public void setTouchAnimationEnabled(boolean z2) {
        this.mTouchAnimationEnabled = z2;
        if (z2) {
            restoreTouchDragDeformationAfterPreferenceTurnedOn();
            return;
        }
        SearchBarBackgroundView searchBarBackgroundView = this.mSearchBarBackgroundView;
        if (searchBarBackgroundView != null) {
            searchBarBackgroundView.setTouchAnimationReset(false);
            this.mSearchBarBackgroundView.setTouchAnimationEnable(false);
        }
        SearchBarFunctionButton searchBarFunctionButton = this.mFunctionalButton;
        if (searchBarFunctionButton != null) {
            searchBarFunctionButton.setTouchAnimationReset(false);
            this.mFunctionalButton.setTouchAnimationEnable(false);
        }
        SearchBarIconButton searchBarIconButton = this.mNavigationView;
        if (searchBarIconButton != null) {
            searchBarIconButton.setTouchAnimationReset(false);
            this.mNavigationView.setTouchAnimationEnable(false);
        }
        SearchBarIconButton searchBarIconButton2 = this.mOuterPrimaryButton;
        if (searchBarIconButton2 != null) {
            searchBarIconButton2.setTouchAnimationReset(false);
            this.mOuterPrimaryButton.setTouchAnimationEnable(false);
        }
        SearchBarIconButton searchBarIconButton3 = this.mOuterSecondaryButton;
        if (searchBarIconButton3 != null) {
            searchBarIconButton3.setTouchAnimationReset(false);
            this.mOuterSecondaryButton.setTouchAnimationEnable(false);
        }
    }

    public void setUseMaterialEffectAttr(int i3) {
        if (isSupportBlur() && this.mUseMaterialEffectAttr != i3) {
            this.mUseMaterialEffectAttr = i3;
            setStrokeEffectEnable(isStrokeEnable());
            setBlurEffectEnable(isBlurEnable());
            setSpotlightEffectEnable(isSpotLightEnable());
            setCausticShadowEffect();
        }
    }

    public void setUseResponsivePadding(boolean z2) {
        this.mUseResponsivePadding = z2;
        requestLayout();
    }

    public void showInToolBar() {
        if (this.mToolBar == null) {
            return;
        }
        ensureAddedToToolBar();
        setBallChain();
        updateSearchAndButtonTouchDeformation(false);
        syncLayoutDirectionFromToolbar();
        if (this.mIconExpandAnimation.isRunning()) {
            updateLayerInToolBar(true);
            openSoftInputAndNotifyStateChanged(1);
            setExpandData();
            this.mIsExpandAnimating = true;
            this.mIsCollapseAnimating = false;
            setToolbarSearchIconAlpha(0.0f);
            this.mIconExpandAnimation.animateToFinalPosition(1000.0f);
            updateStrokeForExpand(true);
            this.mToolbarIconSpringAnimation.animateToFinalPosition(1000.0f);
            return;
        }
        if (this.mFadeInFadeOutAnimation.isRunning()) {
            updateLayerInToolBar(true);
            openSoftInputAndNotifyStateChanged(1);
            this.mIsFadeInAnimating = true;
            setSearchBarToolbarFadeAnimating(true);
            this.mIsCollapseAnimating = false;
            this.mFadeInFadeOutAnimation.animateToFinalPosition(1000.0f);
            return;
        }
        updateToolbarSearchIconRef(findToolbarSearchIcon());
        if (isBlurEnable()) {
            setVisibility(0);
            this.mIconStartExpand = true;
            this.mIsExpandAnimating = true;
            this.mIsCollapseAnimating = false;
            initExpandData();
            updateLayerInToolBar(true);
        } else {
            updateLayerInToolBar(true);
            setVisibility(0);
            setToolBarChildVisibility(0);
            this.mFadeInToolbar = true;
            this.mIsFadeInAnimating = true;
            setSearchBarToolbarFadeAnimating(true);
            this.mIsCollapseAnimating = false;
            initFadeInData();
            if (functionalButtonShouldShow()) {
                this.mFunctionalButton.setVisibility(0);
            }
            setAlpha2Child(this, 0.0f);
        }
        startAnimateToEditState();
    }

    public void startToNormalAnimator() {
        if (this.mState.get() != 1) {
            return;
        }
        startAnimateToNormalState();
    }

    public void updateDropShadow(float f3) {
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            float fMax = Math.max(0.0f, Math.min(f3, 1.0f));
            if (this.mNoShadowInNormalState) {
                fMax = remapShadowProgress(fMax);
            }
            if (isCausticShadowEnable()) {
                updateCausticShadow(fMax);
            } else {
                clearCausticShadow();
                updateViewShadow(fMax);
            }
        }
    }

    public void updateMaterial(float f3) {
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType;
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType2;
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType3;
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType4;
        float fMax = Math.max(0.0f, Math.min(f3, 1.0f));
        if (this.mIsDrawableMaterialEffect) {
            updateNoBlurStrokeProgress(fMax);
            return;
        }
        Context context = getContext();
        if (isStrokeEnable()) {
            strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(context, ToolbarMaterialEffectResolver.getCapsuleStrokeEffectType(this.mMaterialType));
            strokeParamsByType3 = COUIMaterialEffect.getStrokeParamsByType(context, resolveNoEffectBaseType(strokeParamsByType));
            strokeParamsByType4 = COUIMaterialEffect.getStrokeParamsByType(context, ToolbarMaterialEffectResolver.getCircleStrokeEffectType(this.mMaterialType));
            strokeParamsByType2 = COUIMaterialEffect.getStrokeParamsByType(context, resolveNoEffectBaseType(strokeParamsByType4));
        } else {
            strokeParamsByType = null;
            strokeParamsByType2 = null;
            strokeParamsByType3 = null;
            strokeParamsByType4 = null;
        }
        updateMaterialWithResolvedTypes(this.mSearchBarBackgroundView, fMax, strokeParamsByType3, strokeParamsByType);
        if (functionalButtonShouldShow()) {
            updateMaterialWithResolvedTypes(this.mFunctionalButton, fMax, strokeParamsByType3, strokeParamsByType);
        }
        updateMaterialWithResolvedTypes(this.mOuterPrimaryButton, fMax, strokeParamsByType2, strokeParamsByType4);
        updateMaterialWithResolvedTypes(this.mOuterSecondaryButton, fMax, strokeParamsByType2, strokeParamsByType4);
    }

    public COUISearchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiSearchBarStyle);
    }

    public COUISearchBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? R.style.Widget_COUI_COUISearchBar_Dark : R.style.Widget_COUI_COUISearchBar);
    }

    public COUISearchBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mBackgroundRect = new Rect();
        this.mState = new AtomicInteger(0);
        this.mStateAnimationValue = new FloatValueHolder();
        this.mFadeInFadeOutValue = new FloatValueHolder();
        this.mFunBtnAnimationValue = new FloatValueHolder();
        this.mFunBtnSpringForce = new COUISpringForce();
        this.mOuterBtnAnimationValue = new FloatValueHolder();
        this.mOuterBtnSpringForce = new COUISpringForce();
        this.mIconExpandCenterAnimationRange = new FloatValueHolder();
        this.mToolbarIconSpringValue = new FloatValueHolder();
        this.mStateAnimationSpringForceX = new COUISpringForce();
        this.mFadeInSpringForceX = new COUISpringForce();
        this.mExpandSpringForceX = new COUISpringForce();
        this.mToolbarIconSpringForce = new COUISpringForce();
        this.mMaterialType = 0;
        this.mResponsiveWidthSize = 0;
        this.mAddToToolbarWay = 0;
        this.mGravityInToolBar = 48;
        this.mUseResponsivePadding = true;
        this.mStyle = 0;
        this.mAttrs = null;
        this.mInputMethodAnimationEnabled = true;
        this.mShowImeAnimDuration = 0;
        this.mShowImeInterpolator = null;
        this.mSearchViewType = 0;
        this.mHideFunctionalButton = false;
        this.mTouchAnimationEnabled = true;
        this.mStateAnimEnded = true;
        this.mFunBtnAnimEnded = true;
        this.mOuterBtnAnimEnded = true;
        this.mLastStateAnimEndValue = 0.0f;
        this.mIconStartExpand = false;
        this.mIconStartCollapse = false;
        this.mIsCollapseAnimating = false;
        this.mIsExpandAnimating = false;
        this.mIsFadeInAnimating = false;
        this.mIconExpandStartX = 0;
        this.mIconExpandTargetX = 0;
        this.mFunctionalButtonStartX = 0;
        this.mFunctionalButtonTargetX = 0;
        this.mIconInitialWidth = 0.0f;
        this.mSearchBarFinalWidth = 0.0f;
        this.mNoShadowInNormalState = false;
        this.mNoMaterialInContent = false;
        this.mIsDrawableMaterialEffect = false;
        this.mUseMaterialEffectAttr = -1;
        this.mSystemStrokeState = false;
        this.mSystemBlurState = false;
        this.mSystemSpotLightState = false;
        this.mSystemCausticShadowState = false;
        this.mNoBlurSolidColor = 0;
        this.mButtonOffsetAnimationDistance = 0.0f;
        this.mLastOrientation = 0;
        this.mLastScreenWidthDp = -1;
        this.mLastSmallestScreenWidthDp = -1;
        this.mIgnoreTitleViewAlpha = false;
        initParams();
        init(context, attributeSet, i3, i4);
        initEditNormalSateAnimation();
        initToolbarAnimation();
        initIconExpandAnimation();
        UIUtil.setViewClipExpandSize(this, 120, 120, 120, 120);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        ViewCompat.setImportantForAccessibility(this, 1);
    }

    private void clearCausticShadow(View view) {
        COUIMaterialEffect.clearCausticShadow(view);
    }
}