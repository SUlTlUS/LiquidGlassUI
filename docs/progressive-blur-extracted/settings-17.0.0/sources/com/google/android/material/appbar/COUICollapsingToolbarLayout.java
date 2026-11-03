package com.google.android.material.appbar;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.animation.COUIEaseInterpolator;
import com.coui.appcompat.animation.COUISpringInterpolator;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.searchview.COUISearchBar;
import com.coui.appcompat.searchview.COUISearchViewAnimate;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.coui.appcompat.toolbar.ToolbarTextTransitionController;
import com.coui.appcompat.uiutil.UIUtil;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.appbar.olivelink.COUICollapsableAppBarLayoutParams;
import com.google.android.material.appbar.olivelink.COUICollapsableAppBarLayoutParamsExtKt;
import com.google.android.material.elevation.ElevationOverlayProvider;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public class COUICollapsingToolbarLayout extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final long DEFAULT_FADE_IN_ALPHA_ANIMATION_DURATION = 200;
    private static final float DEFAULT_FADE_MODE_EXPANSION_FRACTION = 0.15f;
    private static final float DEFAULT_FADE_MODE_EXPANSION_FRACTION_ERROR = 0.02f;
    private static final float DEFAULT_FADE_MODE_START_FRACTION = 1.0f;
    private static final long DEFAULT_FADE_OUT_ALPHA_ANIMATION_DURATION = 150;
    private static final int DEFAULT_NORMAL_TEXT_COLOR_ALPHA = 255;
    private static final float DEFAULT_SCALE_MODE_START_FRACTION = 0.5f;
    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;
    private static final String ELLIPSIS_NORMAL = "…";
    private static final String ELLIPSIS_TWO_DOTS = "‥";
    private static final float FADE_MODE_EXPANSION_FRACTION_ERROR_WITH_SEARCH = 0.0075f;
    private static final float FADE_MODE_EXPANSION_FRACTION_WITH_SEARCH = 0.35f;
    private static final boolean IS_DEBUG_COUI = false;
    private static final String TAG_COUI = "COUICollapsingToolbar";
    private static final float TEXT_RENDER_EFFECT_MAX_VALUE = 15.0f;
    public static final int TITLE_COLLAPSE_MODE_FADE = 1;
    public static final int TITLE_COLLAPSE_MODE_SCALE = 0;
    private COUISpringAnimation mAlphaSpringAnimation;
    private Rect mCollapsedBounds;
    private int mCollapsedSubtitleMarginTopIfNotHidden;
    private ColorStateList mCollapsedTextColor;
    private final RectF mCollapsedTitleOverlayRectScratch;
    private float mCollapsedTitleTextSizeDefault;
    private float mCollapsedTitleTextSizeWithSubtitle;
    final COUICollapsingTextHelper mCollapsingTextHelper;
    private boolean mCollapsingTitleEnabled;
    private Drawable mContentScrim;
    private ColorDrawable mContentScrimDrawable;
    private COUIToolbar mCouiToolbar;
    private RectF mCurrentBounds;
    int mCurrentOffset;
    private boolean mDrawCollapsingTitle;
    private View mDummyView;
    final ElevationOverlayProvider mElevationOverlayProvider;
    private Rect mExpandedBounds;
    private int mExpandedMarginBottom;
    private int mExpandedMarginEnd;
    private int mExpandedMarginStart;
    private int mExpandedMarginTop;
    private int mExtraMultilineHeight;
    private boolean mExtraMultilineHeightEnabled;
    private float mFadeModeExpansionFraction;
    private float mFadeModeExpansionFractionError;
    private boolean mForceApplySystemWindowInsetTop;
    private View mIconView;
    private ViewTreeObserver.OnGlobalLayoutListener mIconViewFirstLayoutListener;
    private boolean mIsAllowFadeInAnimation;
    private boolean mIsAllowFadeOutAnimation;
    private boolean mIsContentSwitching;
    private boolean mIsInsideSideNavigationBar;
    WindowInsetsCompat mLastInsets;
    private COUIAppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    private ViewTreeObserver.OnPreDrawListener mPendingTitlePreDraw;
    private boolean mRefreshToolbar;
    private int mScrimAlpha;
    private long mScrimAnimationDuration;
    private ValueAnimator mScrimAnimator;
    private int mScrimVisibleHeightTrigger;
    private boolean mScrimsAreShown;
    Drawable mStatusBarScrim;
    private StaticLayout mTextLayout;
    private TextPaint mTextPaint;
    private ToolbarTextTransitionController mTextTransitionController;
    private int mTitleCollapseMode;
    private StaticLayout mTitleExitOverlayLayoutSnapshot;
    private boolean mTitleExitOverlayPrepared;
    private View mTitleExitOverlayViewCollapsed;
    private View mTitleExitOverlayViewExpanded;
    private float mTitleExtraAlpha;
    private float mTitleExtraScale;
    private float mTitleExtraTranslationY;
    private View mTitleOverlayInView;
    private boolean mTitleSwitchExitOldDirect;
    private boolean mTitleSwitchExitOldExpanded;
    private final RectF mTitleSwitchExitOldRect;
    private final float[] mTitleTransformPivotScratch;
    private int mTitleTranslateOffset;
    private final Rect mTmpRect;
    private ViewGroup mToolbar;
    private View mToolbarDirectChild;
    private int mToolbarId;
    private int mToolbarNormalPaddingEnd;
    private int mToolbarNormalPaddingStart;
    private int mTopInsetApplied;
    private static final int DEF_STYLE_RES = R.style.Widget_Design_CollapsingToolbar;
    private static final PathInterpolator TITLE_PATH_INTERPOLATOR = new COUIEaseInterpolator();

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUICollapsingToolbarLayout$1, reason: invalid class name */
    public class AnonymousClass1 implements ViewTreeObserver.OnGlobalLayoutListener {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (COUICollapsingToolbarLayout.access$000(COUICollapsingToolbarLayout.this) == null) {
                return;
            }
            COUICollapsingToolbarLayout.access$000(COUICollapsingToolbarLayout.this).getViewTreeObserver().removeOnGlobalLayoutListener(this);
            COUICollapsingToolbarLayout.this.updateIconViewLocationIfNeed();
        }
    }

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUICollapsingToolbarLayout$2, reason: invalid class name */
    public class AnonymousClass2 implements OnApplyWindowInsetsListener {
        public AnonymousClass2() {
        }

        @Override // androidx.core.view.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return COUICollapsingToolbarLayout.this.onWindowInsetChanged(windowInsetsCompat);
        }
    }

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUICollapsingToolbarLayout$3, reason: invalid class name */
    public class AnonymousClass3 implements ValueAnimator.AnimatorUpdateListener {
        public AnonymousClass3() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUICollapsingToolbarLayout.this.setScrimAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUICollapsingToolbarLayout$4, reason: invalid class name */
    /* JADX INFO: loaded from: classes8.dex */
    public class AnonymousClass4 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ View val$anchor;
        final /* synthetic */ CharSequence val$newTitle;

        public AnonymousClass4(View view, CharSequence charSequence) {
            this.val$anchor = view;
            this.val$newTitle = charSequence;
        }

        public static /* synthetic */ void a(AnonymousClass4 anonymousClass4) {
            COUICollapsingToolbarLayout.access$800(COUICollapsingToolbarLayout.this);
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ViewTreeObserver viewTreeObserver = this.val$anchor.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this);
            }
            COUICollapsingToolbarLayout.access$402(COUICollapsingToolbarLayout.this, null);
            if (!COUICollapsingToolbarLayout.access$500(COUICollapsingToolbarLayout.this)) {
                return true;
            }
            View viewAccess$600 = COUICollapsingToolbarLayout.access$600(COUICollapsingToolbarLayout.this);
            COUICollapsingToolbarLayout.this.setTitle(this.val$newTitle);
            TextView cOUITitleTextView = COUICollapsingToolbarLayout.access$200(COUICollapsingToolbarLayout.this) != null ? COUICollapsingToolbarLayout.access$200(COUICollapsingToolbarLayout.this).getCOUITitleTextView() : null;
            boolean z2 = false;
            boolean z3 = COUICollapsingToolbarLayout.access$200(COUICollapsingToolbarLayout.this) != null && COUICollapsingToolbarLayout.access$200(COUICollapsingToolbarLayout.this).getIsTitleCenterStyle();
            if (!COUICollapsingToolbarLayout.this.isExpanded() && cOUITitleTextView != null && z3) {
                z2 = true;
            }
            View viewAccess$700 = COUICollapsingToolbarLayout.access$700(COUICollapsingToolbarLayout.this, this.val$newTitle, z2, cOUITitleTextView);
            if (viewAccess$700 == null) {
                COUICollapsingToolbarLayout.access$800(COUICollapsingToolbarLayout.this);
                return true;
            }
            COUICollapsingToolbarLayout.access$900(COUICollapsingToolbarLayout.this);
            COUICollapsingToolbarLayout.access$1000(COUICollapsingToolbarLayout.this).startCrossFade(viewAccess$600, viewAccess$700, new k(this));
            return true;
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
    public static final class CollapsingTitleDrawOverlay extends View {
        private boolean mAlignDrawToBoundsOrigin;
        private final boolean mForExitOverlay;
        private final COUICollapsingToolbarLayout mHost;
        private final RectF mOverlayBoundsScratch;

        public CollapsingTitleDrawOverlay(Context context, COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, boolean z2) {
            super(context);
            this.mOverlayBoundsScratch = new RectF();
            this.mHost = cOUICollapsingToolbarLayout;
            this.mForExitOverlay = z2;
            setWillNotDraw(false);
            setVisibility(8);
            setImportantForAccessibility(2);
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            StaticLayout staticLayoutAccess$1100 = this.mForExitOverlay ? COUICollapsingToolbarLayout.access$1100(this.mHost) : this.mHost.getCollapsingTextHelper().textLayout;
            if (staticLayoutAccess$1100 == null) {
                return;
            }
            this.mOverlayBoundsScratch.set(getX(), getY(), getX() + getWidth(), getY() + getHeight());
            this.mHost.getCollapsingTextHelper().drawTextLayoutInOverlayBounds(canvas, this.mOverlayBoundsScratch, staticLayoutAccess$1100, this.mAlignDrawToBoundsOrigin);
        }

        public void setAlignDrawToBoundsOrigin(boolean z2) {
            this.mAlignDrawToBoundsOrigin = z2;
        }
    }

    public class OffsetUpdateListener implements COUIAppBarLayout.OnOffsetChangedListener {
        public OffsetUpdateListener() {
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.OnOffsetChangedListener, com.google.android.material.appbar.COUIAppBarLayout.BaseOnOffsetChangedListener
        public void onOffsetChanged(COUIAppBarLayout cOUIAppBarLayout, int i3) {
            COUICollapsingToolbarLayout cOUICollapsingToolbarLayout = COUICollapsingToolbarLayout.this;
            cOUICollapsingToolbarLayout.mCurrentOffset = i3;
            WindowInsetsCompat windowInsetsCompat = cOUICollapsingToolbarLayout.mLastInsets;
            int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
            int childCount = COUICollapsingToolbarLayout.this.getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = COUICollapsingToolbarLayout.this.getChildAt(i4);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                ViewOffsetHelper viewOffsetHelper = COUICollapsingToolbarLayout.getViewOffsetHelper(childAt);
                int i5 = layoutParams.collapseMode;
                if (i5 == 1) {
                    viewOffsetHelper.setTopAndBottomOffset(MathUtils.clamp(-i3, 0, COUICollapsingToolbarLayout.this.getMaxOffsetForPinChild(childAt)));
                } else if (i5 == 2) {
                    viewOffsetHelper.setTopAndBottomOffset(Math.round((-i3) * layoutParams.parallaxMult));
                }
            }
            COUICollapsingToolbarLayout.this.updateScrimVisibility();
            COUICollapsingToolbarLayout cOUICollapsingToolbarLayout2 = COUICollapsingToolbarLayout.this;
            if (cOUICollapsingToolbarLayout2.mStatusBarScrim != null && systemWindowInsetTop > 0) {
                ViewCompat.postInvalidateOnAnimation(cOUICollapsingToolbarLayout2);
            }
            int height = COUICollapsingToolbarLayout.this.getHeight();
            int minimumHeight = (height - ViewCompat.getMinimumHeight(COUICollapsingToolbarLayout.this)) - systemWindowInsetTop;
            float f3 = minimumHeight;
            COUICollapsingToolbarLayout.this.mCollapsingTextHelper.setFadeModeStartFraction(Math.min(1.0f, (height - COUICollapsingToolbarLayout.this.getScrimVisibleHeightTrigger()) / f3));
            COUICollapsingToolbarLayout cOUICollapsingToolbarLayout3 = COUICollapsingToolbarLayout.this;
            cOUICollapsingToolbarLayout3.mCollapsingTextHelper.setCurrentOffsetY(cOUICollapsingToolbarLayout3.mCurrentOffset + minimumHeight);
            COUICollapsingToolbarLayout.this.mCollapsingTextHelper.setExpansionFraction(Math.abs(i3) / f3);
            COUICollapsingToolbarLayout.access$100(COUICollapsingToolbarLayout.this);
            if (COUICollapsingToolbarLayout.access$200(COUICollapsingToolbarLayout.this) == null || !COUICollapsingToolbarLayout.access$200(COUICollapsingToolbarLayout.this).getIsTitleCenterStyle()) {
                COUICollapsingToolbarLayout.access$300(COUICollapsingToolbarLayout.this);
            } else {
                COUICollapsingToolbarLayout.this.fixCollapsingTitleAlpha();
                COUICollapsingToolbarLayout.this.mCollapsingTextHelper.recalculate();
            }
        }
    }

    /* JADX INFO: loaded from: classes8.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface TitleCollapseMode {
    }

    public COUICollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(TextView textView, int i3, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        textView.setAlpha(f3);
        float f5 = 1.0f - f3;
        textView.setTranslationY(i3 * f5);
        if (COUIMaterialStateManager.INSTANCE.isLowAnimationLevel()) {
            return;
        }
        float fMax = Math.max(0.0f, f5 * 15.0f);
        if (fMax <= 0.0f) {
            textView.setRenderEffect(null);
        } else {
            textView.setRenderEffect(RenderEffect.createBlurEffect(fMax, fMax, Shader.TileMode.CLAMP));
        }
    }

    public static /* synthetic */ View access$000(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        return cOUICollapsingToolbarLayout.mIconView;
    }

    public static /* synthetic */ void access$100(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        cOUICollapsingToolbarLayout.ensureCouiToolbar();
    }

    public static /* synthetic */ ToolbarTextTransitionController access$1000(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        return cOUICollapsingToolbarLayout.mTextTransitionController;
    }

    public static /* synthetic */ StaticLayout access$1100(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        return cOUICollapsingToolbarLayout.mTitleExitOverlayLayoutSnapshot;
    }

    public static /* synthetic */ COUIToolbar access$200(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        return cOUICollapsingToolbarLayout.mCouiToolbar;
    }

    public static /* synthetic */ void access$300(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        cOUICollapsingToolbarLayout.updateTextLayoutIfNeed();
    }

    public static /* synthetic */ ViewTreeObserver.OnPreDrawListener access$402(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, ViewTreeObserver.OnPreDrawListener onPreDrawListener) {
        cOUICollapsingToolbarLayout.mPendingTitlePreDraw = onPreDrawListener;
        return onPreDrawListener;
    }

    public static /* synthetic */ boolean access$500(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        return cOUICollapsingToolbarLayout.mIsContentSwitching;
    }

    public static /* synthetic */ View access$600(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        return cOUICollapsingToolbarLayout.prepareExitOverlayForTitleSwitch();
    }

    public static /* synthetic */ View access$700(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, CharSequence charSequence, boolean z2, TextView textView) {
        return cOUICollapsingToolbarLayout.prepareEnterOverlayForTitleSwitch(charSequence, z2, textView);
    }

    public static /* synthetic */ void access$800(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        cOUICollapsingToolbarLayout.resetContentSwitchState();
    }

    public static /* synthetic */ void access$900(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        cOUICollapsingToolbarLayout.ensureTextTransitionController();
    }

    private void addIconView() {
        View view = this.mIconView;
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof CollapsingToolbarLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) ((CollapsingToolbarLayout.LayoutParams) layoutParams)).gravity = 8388691;
        }
        addView(this.mIconView, getChildCount());
        this.mIconView.getViewTreeObserver().addOnGlobalLayoutListener(this.mIconViewFirstLayoutListener);
    }

    private void animateScrim(int i3) {
        ensureToolbar();
        ValueAnimator valueAnimator = this.mScrimAnimator;
        if (valueAnimator == null) {
            ValueAnimator valueAnimator2 = new ValueAnimator();
            this.mScrimAnimator = valueAnimator2;
            valueAnimator2.setInterpolator(i3 > this.mScrimAlpha ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR : AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
            this.mScrimAnimator.addUpdateListener(new AnonymousClass3());
        } else if (valueAnimator.isRunning()) {
            this.mScrimAnimator.cancel();
        }
        this.mScrimAnimator.setDuration(this.mScrimAnimationDuration);
        this.mScrimAnimator.setIntValues(this.mScrimAlpha, i3);
        this.mScrimAnimator.start();
    }

    private void attachEnterCanvasTitleOverlay(View view) {
        this.mCollapsedTitleOverlayRectScratch.setEmpty();
        if (!isExpanded()) {
            computeCollapsedTitleOverlayRectInto(this.mCollapsedTitleOverlayRectScratch);
            if (this.mCollapsedTitleOverlayRectScratch.isEmpty()) {
                COUIToolbar cOUIToolbar = this.mCouiToolbar;
                TextView cOUITitleTextView = cOUIToolbar != null ? cOUIToolbar.getCOUITitleTextView() : null;
                if (cOUITitleTextView != null) {
                    offsetTitleViewBoundsInto(this.mCollapsedTitleOverlayRectScratch, cOUITitleTextView);
                }
            }
        } else if (!this.mCurrentBounds.isEmpty()) {
            fillCollapsedCanvasTitleOverlayRect(this.mCollapsedTitleOverlayRectScratch);
            if (this.mCollapsedTitleOverlayRectScratch.isEmpty()) {
                this.mCollapsedTitleOverlayRectScratch.set(this.mCurrentBounds);
            }
        }
        if (this.mCollapsedTitleOverlayRectScratch.isEmpty()) {
            return;
        }
        COUIToolbar.attachTitleOverlayForCrossFade(this, view, this.mCollapsedTitleOverlayRectScratch, 0);
        view.invalidate();
    }

    private void attachTitleOverlayToSelfOverlay(View view) {
        getOverlay().remove(view);
        getOverlay().add(view);
    }

    public static /* synthetic */ void b(boolean[] zArr, Runnable runnable) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        if (runnable != null) {
            runnable.run();
        }
    }

    private void beginTitleContentSwitching() {
        this.mIsContentSwitching = true;
        this.mIsAllowFadeInAnimation = false;
        this.mIsAllowFadeOutAnimation = false;
        if (!this.mTitleExitOverlayPrepared || !this.mTitleSwitchExitOldDirect) {
            hideToolbarTitleTemporarily();
        }
        invalidate();
    }

    public static /* synthetic */ void c(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, CharSequence charSequence) {
        cOUICollapsingToolbarLayout.switchTitleContentWithAnimation(charSequence, null);
    }

    private int calculateCollapsedBound() {
        View viewFindSubtitleContentView;
        int measuredHeight;
        ViewParent parent = getParent();
        if (!(parent instanceof COUICollapsableAppBarLayout)) {
            return 0;
        }
        COUICollapsableAppBarLayout cOUICollapsableAppBarLayout = (COUICollapsableAppBarLayout) parent;
        if (cOUICollapsableAppBarLayout.isSubtitleHideEnable() || (viewFindSubtitleContentView = cOUICollapsableAppBarLayout.findSubtitleContentView()) == null || viewFindSubtitleContentView.getVisibility() == 8 || (measuredHeight = viewFindSubtitleContentView.getMeasuredHeight()) == 0) {
            return 0;
        }
        return ((this.mCollapsedBounds.height() - ((int) this.mCollapsingTextHelper.getCollapsedTextHeight())) / 2) - (measuredHeight + this.mCollapsedSubtitleMarginTopIfNotHidden);
    }

    private boolean calculateIsRtl(CharSequence charSequence) {
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        if (this.mCollapsingTextHelper.isRtlTextDirectionHeuristicsEnabled()) {
            return (z2 ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(charSequence, 0, charSequence.length());
        }
        return z2;
    }

    private void cancelEditModeTransition() {
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        if (cOUIToolbar != null) {
            cOUIToolbar.cancelTransition();
        }
        softCancelContentSwitchAnimation();
        COUISpringAnimation cOUISpringAnimation = this.mAlphaSpringAnimation;
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning()) {
            return;
        }
        this.mAlphaSpringAnimation.cancel();
        this.mAlphaSpringAnimation = null;
    }

    private void captureTitleExitOldPosition() {
        this.mTitleSwitchExitOldRect.setEmpty();
        boolean z2 = false;
        this.mTitleSwitchExitOldDirect = false;
        this.mTitleSwitchExitOldExpanded = false;
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        TextView cOUITitleTextView = cOUIToolbar != null ? cOUIToolbar.getCOUITitleTextView() : null;
        boolean zIsExpanded = isExpanded();
        COUIToolbar cOUIToolbar2 = this.mCouiToolbar;
        boolean z3 = cOUIToolbar2 != null && cOUIToolbar2.getIsTitleCenterStyle();
        if (!zIsExpanded && cOUITitleTextView != null && z3) {
            z2 = true;
        }
        if (!zIsExpanded) {
            this.mTitleSwitchExitOldDirect = z2;
            computeCollapsedTitleOverlayRectInto(this.mTitleSwitchExitOldRect);
        } else {
            if (this.mCurrentBounds.isEmpty()) {
                return;
            }
            fillCollapsedCanvasTitleOverlayRect(this.mTitleSwitchExitOldRect);
            if (this.mTitleSwitchExitOldRect.isEmpty()) {
                this.mTitleSwitchExitOldRect.set(this.mCurrentBounds);
            }
            this.mTitleSwitchExitOldExpanded = true;
        }
    }

    private void computeCollapsedTitleOverlayRectInto(RectF rectF) {
        rectF.setEmpty();
        if (isExpanded()) {
            return;
        }
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        TextView cOUITitleTextView = cOUIToolbar != null ? cOUIToolbar.getCOUITitleTextView() : null;
        COUIToolbar cOUIToolbar2 = this.mCouiToolbar;
        boolean z2 = cOUIToolbar2 != null && cOUIToolbar2.getIsTitleCenterStyle();
        if (cOUITitleTextView != null && z2) {
            offsetTitleViewBoundsInto(rectF, cOUITitleTextView);
            return;
        }
        if (this.mDrawCollapsingTitle) {
            fillCollapsedCanvasTitleOverlayRect(rectF);
            return;
        }
        if (!this.mCurrentBounds.isEmpty()) {
            fillCollapsedCanvasTitleOverlayRect(rectF);
            if (rectF.isEmpty()) {
                rectF.set(this.mCurrentBounds);
                return;
            }
            return;
        }
        if (cOUITitleTextView != null) {
            offsetTitleViewBoundsInto(rectF, cOUITitleTextView);
        }
        if (!rectF.isEmpty() || this.mCollapsingTextHelper.textLayout == null) {
            return;
        }
        fillCollapsedCanvasTitleOverlayRect(rectF);
    }

    private void computeTitleTransformPivot(float[] fArr) {
        StaticLayout staticLayout;
        float fCenterY = this.mCurrentBounds.centerY();
        if (!this.mCurrentBounds.isEmpty() && (staticLayout = this.mTextLayout) != null && staticLayout.getLineCount() > 0) {
            fArr[0] = this.mCurrentBounds.left + ((this.mTextLayout.getLineWidth(0) * this.mCollapsingTextHelper.scale) / 2.0f);
        } else if (this.mCurrentBounds.isEmpty()) {
            fArr[0] = getWidth() / 2.0f;
            fCenterY = getHeight() / 2.0f;
        } else {
            fArr[0] = this.mCurrentBounds.centerX();
        }
        fArr[1] = fCenterY;
    }

    private StaticLayout createCouiStaticLayout() {
        if (this.mTextPaint == null) {
            return null;
        }
        COUICollapsingTextHelper cOUICollapsingTextHelper = this.mCollapsingTextHelper;
        float f3 = cOUICollapsingTextHelper.scale;
        if (f3 != 1.0f && f3 != 0.0f) {
            CharSequence text = cOUICollapsingTextHelper.getText();
            try {
                return StaticLayoutBuilderCompat.obtain(text, this.mTextPaint, (int) (this.mCurrentBounds.width() / f3)).setEllipsize(TextUtils.TruncateAt.END).setIsRtl(calculateIsRtl(text)).setAlignment(Layout.Alignment.ALIGN_NORMAL).setIncludePad(false).setMaxLines(1).build();
            } catch (StaticLayoutBuilderCompat.StaticLayoutBuilderCompatException unused) {
            }
        }
        return null;
    }

    public static /* synthetic */ void d(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, Runnable runnable) {
        cOUICollapsingToolbarLayout.startParallelTitleExitForEditMode(runnable);
    }

    private void disableLiftOnScrollIfNeeded(COUIAppBarLayout cOUIAppBarLayout) {
        if (isTitleCollapseFadeMode()) {
            cOUIAppBarLayout.setLiftOnScroll(false);
        }
    }

    public static /* synthetic */ void e(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, Runnable runnable) {
        cOUICollapsingToolbarLayout.finishParallelTitleExitCleanup();
        runnable.run();
    }

    private void ensureCouiToolbar() {
        if (this.mCouiToolbar != null) {
            return;
        }
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIToolbar) {
                this.mCouiToolbar = (COUIToolbar) childAt;
                return;
            }
        }
    }

    private void ensureTextTransitionController() {
        if (this.mTextTransitionController == null) {
            this.mTextTransitionController = new ToolbarTextTransitionController();
        }
    }

    private void ensureTitleOverlayExitViews() {
        if (this.mTitleExitOverlayViewExpanded == null) {
            this.mTitleExitOverlayViewExpanded = new CollapsingTitleDrawOverlay(getContext(), this, true);
        }
        if (this.mTitleExitOverlayViewCollapsed == null) {
            this.mTitleExitOverlayViewCollapsed = new CollapsingTitleDrawOverlay(getContext(), this, true);
        }
    }

    private void ensureTitleOverlayInView() {
        if (this.mTitleOverlayInView == null) {
            this.mTitleOverlayInView = new CollapsingTitleDrawOverlay(getContext(), this, false);
        }
    }

    private void ensureToolbar() {
        if (this.mRefreshToolbar) {
            ViewGroup viewGroup = null;
            this.mToolbar = null;
            this.mToolbarDirectChild = null;
            int i3 = this.mToolbarId;
            if (i3 != -1) {
                ViewGroup viewGroup2 = (ViewGroup) findViewById(i3);
                this.mToolbar = viewGroup2;
                if (viewGroup2 != null) {
                    this.mToolbarDirectChild = findDirectChild(viewGroup2);
                }
            }
            if (this.mToolbar == null) {
                int childCount = getChildCount();
                for (int i4 = 0; i4 < childCount; i4++) {
                    View childAt = getChildAt(i4);
                    if (isToolbar(childAt)) {
                        viewGroup = (ViewGroup) childAt;
                        break;
                    }
                }
                this.mToolbar = viewGroup;
            }
            updateDummyView();
            this.mRefreshToolbar = false;
        }
    }

    public static /* synthetic */ void f(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        cOUICollapsingToolbarLayout.mAlphaSpringAnimation = null;
    }

    private void fillCollapsedCanvasTitleOverlayRect(RectF rectF) {
        if (this.mCollapsingTextHelper.computeOverlayBoundsPreParentTransform(rectF)) {
            mapCollapsedCanvasOverlayRectThroughTitleTransform(rectF);
            return;
        }
        float currentDrawX = this.mCollapsingTextHelper.getCurrentDrawX();
        float currentDrawY = this.mCollapsingTextHelper.getCurrentDrawY();
        COUICollapsingTextHelper cOUICollapsingTextHelper = this.mCollapsingTextHelper;
        StaticLayout staticLayout = cOUICollapsingTextHelper.textLayout;
        if (staticLayout != null) {
            int iMax = Math.max(staticLayout.getWidth(), 1);
            int iMax2 = Math.max(staticLayout.getHeight(), 1);
            float titleLayoutScale = this.mCollapsingTextHelper.getTitleLayoutScale();
            float fMax = iMax;
            float fMax2 = iMax2;
            if (!isTitleCollapseFadeMode() && titleLayoutScale > 0.0f && titleLayoutScale != 1.0f) {
                fMax = Math.max(fMax * titleLayoutScale, 1.0f);
                fMax2 = Math.max(fMax2 * titleLayoutScale, 1.0f);
            }
            rectF.set(currentDrawX, currentDrawY, fMax + currentDrawX, fMax2 + currentDrawY);
        } else {
            int iMax3 = Math.max(Math.round(cOUICollapsingTextHelper.getCollapsedTextWidth()), 1);
            Paint.FontMetrics fontMetrics = this.mCollapsingTextHelper.textPaint.getFontMetrics();
            rectF.set(currentDrawX, currentDrawY, iMax3 + currentDrawX, Math.max(Math.round(fontMetrics.descent - fontMetrics.ascent), 1) + currentDrawY);
        }
        mapCollapsedCanvasOverlayRectThroughTitleTransform(rectF);
    }

    private View findDirectChild(View view) {
        for (ViewParent parent = view.getParent(); parent != this && parent != null; parent = parent.getParent()) {
            if (parent instanceof View) {
                view = parent;
            }
        }
        return view;
    }

    private void finishParallelTitleExitCleanup() {
        this.mTitleExitOverlayPrepared = false;
        this.mTitleSwitchExitOldRect.setEmpty();
        this.mTitleSwitchExitOldDirect = false;
        this.mTitleSwitchExitOldExpanded = false;
        resetAllTitleCrossFadeOverlaysForReuse();
    }

    private static int getHeightWithMargins(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return view.getMeasuredHeight();
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        return view.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private float getTitleDrawExtraTranslationY() {
        if (this.mTitleExtraScale == 1.0f && this.mTitleExtraAlpha == 1.0f && this.mTitleExtraTranslationY == 0.0f) {
            return 0.0f;
        }
        return this.mTitleExtraTranslationY;
    }

    private static CharSequence getToolbarTitle(View view) {
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getTitle();
        }
        if (view instanceof android.widget.Toolbar) {
            return ((android.widget.Toolbar) view).getTitle();
        }
        return null;
    }

    public static ViewOffsetHelper getViewOffsetHelper(View view) {
        int i3 = R.id.view_offset_helper;
        ViewOffsetHelper viewOffsetHelper = (ViewOffsetHelper) view.getTag(i3);
        if (viewOffsetHelper != null) {
            return viewOffsetHelper;
        }
        ViewOffsetHelper viewOffsetHelper2 = new ViewOffsetHelper(view);
        view.setTag(i3, viewOffsetHelper2);
        return viewOffsetHelper2;
    }

    private boolean hasPersistentSubtitle() {
        View viewFindSubtitleContentView;
        ViewParent parent = getParent();
        if (!(parent instanceof COUICollapsableAppBarLayout)) {
            return false;
        }
        COUICollapsableAppBarLayout cOUICollapsableAppBarLayout = (COUICollapsableAppBarLayout) parent;
        return (cOUICollapsableAppBarLayout.isSubtitleHideEnable() || (viewFindSubtitleContentView = cOUICollapsableAppBarLayout.findSubtitleContentView()) == null || viewFindSubtitleContentView.getVisibility() == 8) ? false : true;
    }

    private void hideToolbarTitleTemporarily() {
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        if (cOUIToolbar == null || cOUIToolbar.getCOUITitleTextView() == null) {
            return;
        }
        this.mCouiToolbar.getCOUITitleTextView().setAlpha(0.0f);
    }

    private void initAlphaSpringAnimation(TextView textView, int i3, float f3, float f4) {
        COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(new FloatValueHolder());
        this.mAlphaSpringAnimation = cOUISpringAnimation;
        cOUISpringAnimation.setMinimumVisibleChange(0.00390625f);
        COUISpringForce cOUISpringForce = new COUISpringForce(f4);
        COUICollapsableAppBarLayoutParams cOUICollapsableAppBarLayoutParams = COUICollapsableAppBarLayoutParamsExtKt.get(COUICollapsableAppBarLayoutParams.INSTANCE);
        cOUISpringForce.setBounce(cOUICollapsableAppBarLayoutParams.getAlphaSpring().getBounce()).setResponse(cOUICollapsableAppBarLayoutParams.getAlphaSpring().getResponse());
        this.mAlphaSpringAnimation.setSpring(cOUISpringForce);
        this.mAlphaSpringAnimation.setStartValue(f3);
        this.mAlphaSpringAnimation.addUpdateListener(new f(textView, i3));
        this.mAlphaSpringAnimation.addEndListener(new g(this));
    }

    private void initCoui(AttributeSet attributeSet) {
        resetTextHelperInterpolator();
        TextPaint textPaint = this.mCollapsingTextHelper.textPaint;
        this.mTextPaint = textPaint;
        if (textPaint != null) {
            this.mTextPaint.setElegantTextHeight(getContext().getResources().getBoolean(com.support.appcompat.R.bool.coui_support_elegant_text_height));
        }
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, com.support.toolbar.R.styleable.COUICollapsingToolbarLayout);
            try {
                setIconView(typedArrayObtainStyledAttributes.getResourceId(com.support.toolbar.R.styleable.COUICollapsingToolbarLayout_iconView, 0));
            } finally {
                typedArrayObtainStyledAttributes.recycle();
            }
        }
    }

    private boolean isTitleCollapseFadeMode() {
        return this.mTitleCollapseMode == 1;
    }

    private static boolean isToolbar(View view) {
        return (view instanceof Toolbar) || (view instanceof android.widget.Toolbar);
    }

    private boolean isToolbarChild(View view) {
        View view2 = this.mToolbarDirectChild;
        if (view2 == null || view2 == this) {
            return view == this.mToolbar;
        }
        return view == view2;
    }

    private void mapCollapsedCanvasOverlayRectThroughTitleTransform(RectF rectF) {
        if (this.mTitleExtraScale == 1.0f && this.mTitleExtraTranslationY == 0.0f) {
            return;
        }
        computeTitleTransformPivot(this.mTitleTransformPivotScratch);
        float[] fArr = this.mTitleTransformPivotScratch;
        float f3 = fArr[0];
        float f4 = fArr[1];
        float f5 = this.mTitleExtraScale;
        float f6 = this.mTitleExtraTranslationY;
        float f7 = rectF.left;
        float f8 = rectF.top;
        float f9 = ((f7 - f3) * f5) + f3;
        float f10 = ((f8 - f4) * f5) + f4 + f6;
        float f11 = f3 + ((rectF.right - f3) * f5);
        float f12 = f4 + ((rectF.bottom - f4) * f5) + f6;
        rectF.set(Math.min(Math.min(f9, f11), Math.min(f9, f11)), Math.min(Math.min(f10, f10), Math.min(f12, f12)), Math.max(Math.max(f9, f11), Math.max(f9, f11)), Math.max(Math.max(f10, f10), Math.max(f12, f12)));
    }

    private void offsetTitleViewBoundsInto(RectF rectF, TextView textView) {
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        getLocationOnScreen(iArr);
        textView.getLocationOnScreen(iArr2);
        float f3 = iArr2[0] - iArr[0];
        float f4 = iArr2[1] - iArr[1];
        rectF.set(f3, f4, Math.max(textView.getWidth(), 1) + f3, Math.max(textView.getHeight(), 1) + f4);
    }

    private View prepareEnterOverlayForTitleSwitch(CharSequence charSequence, boolean z2, TextView textView) {
        if (z2) {
            if (textView != null) {
                textView.setAlpha(0.0f);
                textView.setText(charSequence);
            }
            return textView;
        }
        ensureTitleOverlayInView();
        View view = this.mTitleOverlayInView;
        if (view == null) {
            resetContentSwitchState();
            return null;
        }
        view.setAlpha(1.0f);
        attachEnterCanvasTitleOverlay(this.mTitleOverlayInView);
        this.mTitleOverlayInView.setVisibility(0);
        attachTitleOverlayToSelfOverlay(this.mTitleOverlayInView);
        return this.mTitleOverlayInView;
    }

    private void prepareExitOverlayBeforeStructureSwitch() {
        this.mTitleExitOverlayPrepared = false;
        if (TextUtils.isEmpty(getTitle()) || this.mTitleSwitchExitOldRect.isEmpty()) {
            return;
        }
        if (this.mTitleSwitchExitOldDirect) {
            this.mTitleExitOverlayPrepared = true;
            return;
        }
        ensureTitleOverlayExitViews();
        View viewResolveTitleExitOverlayForCurrentCapture = resolveTitleExitOverlayForCurrentCapture();
        if (viewResolveTitleExitOverlayForCurrentCapture == null) {
            return;
        }
        resetAllTitleCrossFadeOverlaysForReuse();
        COUIToolbar.attachTitleOverlayForCrossFade(this, viewResolveTitleExitOverlayForCurrentCapture, this.mTitleSwitchExitOldRect, 4);
        this.mTitleExitOverlayLayoutSnapshot = this.mCollapsingTextHelper.textLayout;
        if (viewResolveTitleExitOverlayForCurrentCapture instanceof CollapsingTitleDrawOverlay) {
            ((CollapsingTitleDrawOverlay) viewResolveTitleExitOverlayForCurrentCapture).setAlignDrawToBoundsOrigin(false);
        }
        viewResolveTitleExitOverlayForCurrentCapture.invalidate();
        this.mTitleExitOverlayPrepared = true;
    }

    private View prepareExitOverlayForTitleSwitch() {
        if (!this.mTitleExitOverlayPrepared || this.mTitleSwitchExitOldRect.isEmpty()) {
            return null;
        }
        if (!this.mTitleSwitchExitOldDirect) {
            View viewResolveTitleExitOverlayForCurrentCapture = resolveTitleExitOverlayForCurrentCapture();
            if (viewResolveTitleExitOverlayForCurrentCapture == null) {
                return null;
            }
            COUIToolbar.attachTitleOverlayForCrossFade(this, viewResolveTitleExitOverlayForCurrentCapture, this.mTitleSwitchExitOldRect, 0);
            viewResolveTitleExitOverlayForCurrentCapture.invalidate();
            return viewResolveTitleExitOverlayForCurrentCapture;
        }
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        TextView cOUITitleTextView = cOUIToolbar != null ? cOUIToolbar.getCOUITitleTextView() : null;
        if (cOUITitleTextView == null) {
            return null;
        }
        cOUITitleTextView.setVisibility(0);
        cOUITitleTextView.setAlpha(1.0f);
        return cOUITitleTextView;
    }

    private boolean prepareTitleSwitchState(CharSequence charSequence, Runnable runnable) {
        ensureCouiToolbar();
        if (charSequence == null) {
            if (runnable == null) {
                return false;
            }
            runnable.run();
            return false;
        }
        beginTitleContentSwitching();
        if (runnable == null) {
            return true;
        }
        runnable.run();
        return true;
    }

    private void registerTitleCrossFadePreDraw(CharSequence charSequence) {
        View viewResolveTitlePreDrawAnchor = resolveTitlePreDrawAnchor();
        this.mPendingTitlePreDraw = new AnonymousClass4(viewResolveTitlePreDrawAnchor, charSequence);
        viewResolveTitlePreDrawAnchor.getViewTreeObserver().addOnPreDrawListener(this.mPendingTitlePreDraw);
    }

    private void removePendingTitlePreDraw() {
        if (this.mPendingTitlePreDraw != null) {
            ViewTreeObserver viewTreeObserver = resolveTitlePreDrawAnchor().getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this.mPendingTitlePreDraw);
            }
            this.mPendingTitlePreDraw = null;
        }
    }

    private void resetAllTitleCrossFadeOverlaysForReuse() {
        View view = this.mTitleOverlayInView;
        if (view != null) {
            resetTitleCrossFadeOverlayForReuse(view);
        }
        View view2 = this.mTitleExitOverlayViewExpanded;
        if (view2 != null) {
            resetTitleCrossFadeOverlayForReuse(view2);
        }
        View view3 = this.mTitleExitOverlayViewCollapsed;
        if (view3 != null) {
            resetTitleCrossFadeOverlayForReuse(view3);
        }
        this.mTitleExitOverlayLayoutSnapshot = null;
    }

    private void resetContentSwitchState() {
        this.mIsContentSwitching = false;
        this.mTitleExitOverlayPrepared = false;
        this.mTitleSwitchExitOldRect.setEmpty();
        this.mCollapsedTitleOverlayRectScratch.setEmpty();
        this.mTitleSwitchExitOldDirect = false;
        this.mTitleSwitchExitOldExpanded = false;
        resetAllTitleCrossFadeOverlaysForReuse();
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        if (cOUIToolbar != null && cOUIToolbar.getCOUITitleTextView() != null) {
            TextView cOUITitleTextView = this.mCouiToolbar.getCOUITitleTextView();
            float f3 = 0.0f;
            cOUITitleTextView.setTranslationY(0.0f);
            if (!COUIMaterialStateManager.INSTANCE.isLowAnimationLevel()) {
                cOUITitleTextView.setRenderEffect(null);
            }
            if (!isExpanded() && this.mCouiToolbar.getIsTitleCenterStyle()) {
                f3 = 1.0f;
            }
            cOUITitleTextView.setAlpha(f3);
        }
        this.mIsAllowFadeInAnimation = isExpanded();
        this.mIsAllowFadeOutAnimation = !isExpanded();
        invalidate();
    }

    private void resetTextHelperInterpolator() {
        COUICollapsingTextHelper cOUICollapsingTextHelper = this.mCollapsingTextHelper;
        PathInterpolator pathInterpolator = TITLE_PATH_INTERPOLATOR;
        cOUICollapsingTextHelper.setTextSizeInterpolator(pathInterpolator);
        this.mCollapsingTextHelper.setPositionInterpolator(pathInterpolator);
        COUICollapsableAppBarLayoutParams cOUICollapsableAppBarLayoutParams = COUICollapsableAppBarLayoutParamsExtKt.get(COUICollapsableAppBarLayoutParams.INSTANCE);
        this.mCollapsingTextHelper.setBlendInterpolator(new COUISpringInterpolator(cOUICollapsableAppBarLayoutParams.getTitleAlphaSpring().getResponse(), cOUICollapsableAppBarLayoutParams.getTitleAlphaSpring().getBounce()));
    }

    private void resetTitleCrossFadeOverlayForReuse(View view) {
        view.clearAnimation();
        view.animate().cancel();
        getOverlay().remove(view);
        view.setAlpha(1.0f);
        view.setVisibility(8);
        if (view instanceof CollapsingTitleDrawOverlay) {
            ((CollapsingTitleDrawOverlay) view).setAlignDrawToBoundsOrigin(false);
        }
    }

    private void resetToolbarTextState(COUIToolbar cOUIToolbar) {
        TextView cOUITitleTextView = cOUIToolbar.getCOUITitleTextView();
        if (cOUITitleTextView == null) {
            return;
        }
        cOUITitleTextView.setAlpha(1.0f);
        cOUITitleTextView.setTranslationY(0.0f);
        if (COUIMaterialStateManager.INSTANCE.isLowAnimationLevel()) {
            return;
        }
        cOUITitleTextView.setRenderEffect(null);
    }

    private View resolveTitleExitOverlayForCurrentCapture() {
        return this.mTitleSwitchExitOldExpanded ? this.mTitleExitOverlayViewExpanded : this.mTitleExitOverlayViewCollapsed;
    }

    private View resolveTitlePreDrawAnchor() {
        ensureCouiToolbar();
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        return cOUIToolbar != null ? cOUIToolbar : this;
    }

    private void showCollapsedTitleAnimation() {
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        if (cOUIToolbar == null || !cOUIToolbar.getIsTitleCenterStyle() || this.mCouiToolbar.getCOUITitleTextView() == null) {
            return;
        }
        getCollapsingTextHelper().setFadeModeStartFraction(1.0f);
        float expansionFraction = getCollapsingTextHelper().getExpansionFraction();
        float f3 = this.mFadeModeExpansionFraction;
        if (expansionFraction <= f3) {
            startFadeAnimation(false, this.mCouiToolbar);
        } else if (expansionFraction > f3 + this.mFadeModeExpansionFractionError) {
            startFadeAnimation(true, this.mCouiToolbar);
        }
    }

    private void softCancelContentSwitchAnimation() {
        removePendingTitlePreDraw();
        ToolbarTextTransitionController toolbarTextTransitionController = this.mTextTransitionController;
        if (toolbarTextTransitionController != null) {
            toolbarTextTransitionController.cancelAll();
        }
        resetAllTitleCrossFadeOverlaysForReuse();
        this.mTitleExitOverlayPrepared = false;
        this.mCollapsedTitleOverlayRectScratch.setEmpty();
        this.mIsContentSwitching = false;
    }

    private void startFadeAnimation(boolean z2, COUIToolbar cOUIToolbar) {
        TextView cOUITitleTextView = cOUIToolbar.getCOUITitleTextView();
        int height = cOUIToolbar.getHeight() - cOUITitleTextView.getBottom();
        if (z2) {
            this.mIsAllowFadeOutAnimation = true;
            if (!this.mIsAllowFadeInAnimation) {
                return;
            }
        } else {
            this.mIsAllowFadeInAnimation = true;
            if (!this.mIsAllowFadeOutAnimation) {
                return;
            }
        }
        float f3 = z2 ? 0.0f : 1.0f;
        float f4 = z2 ? 1.0f : 0.0f;
        COUISpringAnimation cOUISpringAnimation = this.mAlphaSpringAnimation;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            if (z2) {
                this.mIsAllowFadeInAnimation = false;
            } else {
                this.mIsAllowFadeOutAnimation = false;
            }
            this.mAlphaSpringAnimation.animateToFinalPosition(f4);
            return;
        }
        cOUITitleTextView.setTranslationY(z2 ? height : 0.0f);
        if (this.mAlphaSpringAnimation == null) {
            initAlphaSpringAnimation(cOUITitleTextView, height, f3, f4);
        }
        if (z2) {
            this.mIsAllowFadeInAnimation = false;
        } else {
            this.mIsAllowFadeOutAnimation = false;
        }
        this.mAlphaSpringAnimation.start();
    }

    private void startParallelTitleExitForEditMode(Runnable runnable) {
        if (!this.mTitleExitOverlayPrepared) {
            runnable.run();
            return;
        }
        beginTitleContentSwitching();
        View viewPrepareExitOverlayForTitleSwitch = prepareExitOverlayForTitleSwitch();
        if (viewPrepareExitOverlayForTitleSwitch == null) {
            runnable.run();
        } else {
            ensureTextTransitionController();
            this.mTextTransitionController.startCrossFade(viewPrepareExitOverlayForTitleSwitch, null, new e(this, runnable));
        }
    }

    private int transform(Canvas canvas) {
        Canvas canvas2;
        if (!((this.mTitleExtraScale == 1.0f && this.mTitleExtraAlpha == 1.0f && this.mTitleExtraTranslationY == 0.0f) ? false : true)) {
            return -1;
        }
        computeTitleTransformPivot(this.mTitleTransformPivotScratch);
        float[] fArr = this.mTitleTransformPivotScratch;
        float f3 = fArr[0];
        float f4 = fArr[1];
        int iSave = canvas.save();
        float f5 = this.mTitleExtraAlpha;
        if (f5 != 1.0f) {
            canvas2 = canvas;
            canvas2.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), Math.round(f5 * 255.0f));
        } else {
            canvas2 = canvas;
        }
        float f6 = this.mTitleExtraScale;
        if (f6 != 1.0f) {
            canvas2.scale(f6, f6, f3, f4);
        }
        float f7 = this.mTitleExtraTranslationY;
        if (f7 != 0.0f) {
            canvas2.translate(0.0f, f7);
        }
        return iSave;
    }

    private void translateTitleIfNeed() {
        if (this.mCollapsedBounds == null) {
            return;
        }
        int iCalculateCollapsedBound = calculateCollapsedBound();
        this.mTitleTranslateOffset = iCalculateCollapsedBound;
        if (iCalculateCollapsedBound != 0) {
            this.mCollapsedBounds.offset(0, iCalculateCollapsedBound);
            this.mCollapsingTextHelper.recalculate();
        }
    }

    private void updateCollapsedBounds(boolean z2) {
        int titleMarginStart;
        int titleMarginBottom;
        int titleMarginEnd;
        int titleMarginTop;
        View view = this.mToolbarDirectChild;
        if (view == null) {
            view = this.mToolbar;
        }
        int maxOffsetForPinChild = getMaxOffsetForPinChild(view);
        DescendantOffsetUtils.getDescendantRect(this, this.mDummyView, this.mTmpRect);
        ViewGroup viewGroup = this.mToolbar;
        if (viewGroup instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) viewGroup;
            titleMarginStart = toolbar.getTitleMarginStart();
            titleMarginEnd = toolbar.getTitleMarginEnd();
            titleMarginTop = toolbar.getTitleMarginTop();
            titleMarginBottom = toolbar.getTitleMarginBottom();
        } else if (viewGroup instanceof android.widget.Toolbar) {
            android.widget.Toolbar toolbar2 = (android.widget.Toolbar) viewGroup;
            titleMarginStart = toolbar2.getTitleMarginStart();
            titleMarginEnd = toolbar2.getTitleMarginEnd();
            titleMarginTop = toolbar2.getTitleMarginTop();
            titleMarginBottom = toolbar2.getTitleMarginBottom();
        } else {
            titleMarginStart = 0;
            titleMarginBottom = 0;
            titleMarginEnd = 0;
            titleMarginTop = 0;
        }
        COUICollapsingTextHelper cOUICollapsingTextHelper = this.mCollapsingTextHelper;
        Rect rect = this.mTmpRect;
        int i3 = rect.left + (z2 ? titleMarginEnd : titleMarginStart);
        int i4 = rect.top + maxOffsetForPinChild + titleMarginTop;
        int i5 = rect.right;
        if (!z2) {
            titleMarginStart = titleMarginEnd;
        }
        cOUICollapsingTextHelper.setCollapsedBounds(i3, i4, i5 - titleMarginStart, (rect.bottom + maxOffsetForPinChild) - titleMarginBottom);
    }

    private void updateContentDescriptionFromTitle() {
        setContentDescription(getTitle());
    }

    private void updateContentScrimBounds(Drawable drawable, int i3, int i4) {
        updateContentScrimBounds(drawable, this.mToolbar, i3, i4);
    }

    private void updateDummyView() {
        View view;
        if (!this.mCollapsingTitleEnabled && (view = this.mDummyView) != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.mDummyView);
            }
        }
        if (!this.mCollapsingTitleEnabled || this.mToolbar == null) {
            return;
        }
        if (this.mDummyView == null) {
            this.mDummyView = new View(getContext());
        }
        if (this.mDummyView.getParent() == null) {
            this.mToolbar.addView(this.mDummyView, -1, -1);
        }
    }

    private void updateFadeModeExpansionFraction() {
        ViewParent parent = getParent();
        if (parent instanceof COUIAppBarLayout) {
            boolean z2 = false;
            int i3 = 0;
            while (true) {
                COUIAppBarLayout cOUIAppBarLayout = (COUIAppBarLayout) parent;
                if (i3 >= cOUIAppBarLayout.getChildCount()) {
                    break;
                }
                View childAt = cOUIAppBarLayout.getChildAt(i3);
                if ((childAt instanceof COUISearchBar) || (childAt instanceof COUISearchViewAnimate)) {
                    z2 = true;
                    break;
                }
                i3++;
            }
            this.mFadeModeExpansionFraction = z2 ? 0.35f : 0.15f;
            this.mFadeModeExpansionFractionError = z2 ? FADE_MODE_EXPANSION_FRACTION_ERROR_WITH_SEARCH : 0.02f;
        }
    }

    private void updateTextBounds(int i3, int i4, int i5, int i6, boolean z2) {
        View view;
        if (!this.mCollapsingTitleEnabled || (view = this.mDummyView) == null) {
            return;
        }
        boolean z3 = ViewCompat.isAttachedToWindow(view) && this.mDummyView.getVisibility() == 0;
        this.mDrawCollapsingTitle = z3;
        if (z3 || z2) {
            boolean z4 = ViewCompat.getLayoutDirection(this) == 1;
            updateCollapsedBounds(z4);
            this.mCollapsingTextHelper.setExpandedBounds(z4 ? this.mExpandedMarginEnd : this.mExpandedMarginStart, this.mTmpRect.top + this.mExpandedMarginTop, (i5 - i3) - (z4 ? this.mExpandedMarginStart : this.mExpandedMarginEnd), (i6 - i4) - this.mExpandedMarginBottom);
            this.mCollapsingTextHelper.setCollapsedTextSize(hasPersistentSubtitle() ? this.mCollapsedTitleTextSizeWithSubtitle : this.mCollapsedTitleTextSizeDefault);
            this.mCollapsingTextHelper.recalculate(z2);
        }
    }

    private void updateTextLayoutIfNeed() {
        StaticLayout staticLayout;
        StaticLayout staticLayoutCreateCouiStaticLayout;
        if (isExpanded() || isCollapsed() || getMaxLines() > 1 || (staticLayout = this.mTextLayout) == null) {
            return;
        }
        CharSequence text = staticLayout.getText();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if ((text.toString().contains(ELLIPSIS_TWO_DOTS) || text.toString().contains(ELLIPSIS_NORMAL)) && (staticLayoutCreateCouiStaticLayout = createCouiStaticLayout()) != null) {
            this.mCollapsingTextHelper.textLayout = staticLayoutCreateCouiStaticLayout;
            this.mTextLayout = staticLayoutCreateCouiStaticLayout;
        }
    }

    private void updateTitleFromToolbarIfNeeded() {
        if (this.mToolbar != null && this.mCollapsingTitleEnabled && TextUtils.isEmpty(this.mCollapsingTextHelper.getText())) {
            setTitle(getToolbarTitle(this.mToolbar));
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        ensureCouiToolbar();
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        if (cOUIToolbar != null && cOUIToolbar.getIsTitleCenterStyle()) {
            setScrimAlpha(255);
            showCollapsedTitleAnimation();
        }
        super.draw(canvas);
        if (this.mStatusBarScrim == null || this.mScrimAlpha <= 0) {
            return;
        }
        WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        if (systemWindowInsetTop > 0) {
            this.mStatusBarScrim.setBounds(0, -this.mCurrentOffset, getWidth(), systemWindowInsetTop - this.mCurrentOffset);
            this.mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mStatusBarScrim.draw(canvas);
        }
    }

    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View view, long j3) {
        boolean z2;
        if (this.mContentScrim == null || this.mScrimAlpha <= 0 || !isToolbarChild(view)) {
            z2 = false;
        } else {
            updateContentScrimBounds(this.mContentScrim, view, getWidth(), getHeight());
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
            z2 = true;
        }
        return super.drawChild(canvas, view, j3) || z2;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.mStatusBarScrim;
        boolean state = (drawable == null || !drawable.isStateful()) ? false : drawable.setState(drawableState);
        Drawable drawable2 = this.mContentScrim;
        if (drawable2 != null && drawable2.isStateful()) {
            state |= drawable2.setState(drawableState);
        }
        COUICollapsingTextHelper cOUICollapsingTextHelper = this.mCollapsingTextHelper;
        if (cOUICollapsingTextHelper != null) {
            state |= cOUICollapsingTextHelper.setState(drawableState);
        }
        if (state) {
            invalidate();
        }
    }

    public void fixCollapsingTitleAlpha() {
        this.mCollapsingTextHelper.setFadeModeStartFraction(1.0f);
        TextPaint textPaint = this.mTextPaint;
        if (textPaint != null) {
            textPaint.setAlpha(255);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return generateDefaultLayoutParams();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateLayoutParams(attributeSet);
    }

    public int getCollapsedTitleGravity() {
        return this.mCollapsingTextHelper.getCollapsedTextGravity();
    }

    public Typeface getCollapsedTitleTypeface() {
        return this.mCollapsingTextHelper.getCollapsedTypeface();
    }

    public COUICollapsingTextHelper getCollapsingTextHelper() {
        return this.mCollapsingTextHelper;
    }

    public StaticLayout getCollapsingTextLayout() {
        return this.mTextLayout;
    }

    public TextPaint getCollapsingTextPaint() {
        return this.mTextPaint;
    }

    public Drawable getContentScrim() {
        return this.mContentScrim;
    }

    public int getExpandedTitleGravity() {
        return this.mCollapsingTextHelper.getExpandedTextGravity();
    }

    public int getExpandedTitleMarginBottom() {
        return this.mExpandedMarginBottom;
    }

    public int getExpandedTitleMarginEnd() {
        return this.mExpandedMarginEnd;
    }

    public int getExpandedTitleMarginStart() {
        return this.mExpandedMarginStart;
    }

    public int getExpandedTitleMarginTop() {
        return this.mExpandedMarginTop;
    }

    public Typeface getExpandedTitleTypeface() {
        return this.mCollapsingTextHelper.getExpandedTypeface();
    }

    public int getHyphenationFrequency() {
        return this.mCollapsingTextHelper.getHyphenationFrequency();
    }

    public View getIconView() {
        return this.mIconView;
    }

    public int getLineCount() {
        return this.mCollapsingTextHelper.getLineCount();
    }

    public float getLineSpacingAdd() {
        return this.mCollapsingTextHelper.getLineSpacingAdd();
    }

    public float getLineSpacingMultiplier() {
        return this.mCollapsingTextHelper.getLineSpacingMultiplier();
    }

    public int getMaxLines() {
        return this.mCollapsingTextHelper.getMaxLines();
    }

    public final int getMaxOffsetForPinChild(View view) {
        return ((getHeight() - getViewOffsetHelper(view).getLayoutTop()) - view.getHeight()) - ((FrameLayout.LayoutParams) ((LayoutParams) view.getLayoutParams())).bottomMargin;
    }

    public int getScrimAlpha() {
        return this.mScrimAlpha;
    }

    public long getScrimAnimationDuration() {
        return this.mScrimAnimationDuration;
    }

    public int getScrimVisibleHeightTrigger() {
        int i3 = this.mScrimVisibleHeightTrigger;
        if (i3 >= 0) {
            return i3 + this.mTopInsetApplied + this.mExtraMultilineHeight;
        }
        WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        int minimumHeight = ViewCompat.getMinimumHeight(this);
        return minimumHeight > 0 ? Math.min((minimumHeight * 2) + systemWindowInsetTop, getHeight()) : getHeight() / 3;
    }

    public Drawable getStatusBarScrim() {
        return this.mStatusBarScrim;
    }

    public CharSequence getTitle() {
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getText();
        }
        return null;
    }

    public int getTitleCollapseMode() {
        return this.mTitleCollapseMode;
    }

    public TimeInterpolator getTitlePositionInterpolator() {
        return this.mCollapsingTextHelper.getPositionInterpolator();
    }

    public boolean isCollapsed() {
        return this.mCollapsingTextHelper.getExpansionFraction() == 1.0f;
    }

    public boolean isExpanded() {
        return this.mCollapsingTextHelper.getExpansionFraction() == 0.0f;
    }

    public boolean isExtraMultilineHeightEnabled() {
        return this.mExtraMultilineHeightEnabled;
    }

    public boolean isForceApplySystemWindowInsetTop() {
        return this.mForceApplySystemWindowInsetTop;
    }

    public boolean isRtlTextDirectionHeuristicsEnabled() {
        return this.mCollapsingTextHelper.isRtlTextDirectionHeuristicsEnabled();
    }

    public boolean isTitleEnabled() {
        return this.mCollapsingTitleEnabled;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof COUIAppBarLayout) {
            COUIAppBarLayout cOUIAppBarLayout = (COUIAppBarLayout) parent;
            disableLiftOnScrollIfNeeded(cOUIAppBarLayout);
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows(cOUIAppBarLayout));
            if (this.mOnOffsetChangedListener == null) {
                this.mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            cOUIAppBarLayout.addOnOffsetChangedListener(this.mOnOffsetChangedListener);
            ViewCompat.requestApplyInsets(this);
        }
        View view = this.mIconView;
        if (view != null) {
            bringChildToFront(view);
        }
        setTouchscreenBlocksFocus(false);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mCollapsingTextHelper.maybeUpdateFontWeightAdjustment(configuration);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        ViewParent parent = getParent();
        COUIAppBarLayout.OnOffsetChangedListener onOffsetChangedListener = this.mOnOffsetChangedListener;
        if (onOffsetChangedListener != null && (parent instanceof COUIAppBarLayout)) {
            ((COUIAppBarLayout) parent).removeOnOffsetChangedListener(onOffsetChangedListener);
        }
        super.onDetachedFromWindow();
        ToolbarTextTransitionController toolbarTextTransitionController = this.mTextTransitionController;
        if (toolbarTextTransitionController != null) {
            toolbarTextTransitionController.release();
            this.mTextTransitionController = null;
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Drawable drawable;
        super.onDraw(canvas);
        ensureToolbar();
        if (this.mToolbar == null && (drawable = this.mContentScrim) != null && this.mScrimAlpha > 0) {
            drawable.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
        }
        if (this.mCollapsingTitleEnabled && this.mDrawCollapsingTitle && !this.mIsContentSwitching) {
            int iTransform = transform(canvas);
            if (this.mToolbar == null || this.mContentScrim == null || this.mScrimAlpha <= 0 || !isTitleCollapseFadeMode() || this.mCollapsingTextHelper.getExpansionFraction() >= this.mCollapsingTextHelper.getFadeModeThresholdFraction()) {
                this.mCollapsingTextHelper.draw(canvas);
            } else {
                int iSave = canvas.save();
                canvas.clipRect(this.mContentScrim.getBounds(), Region.Op.DIFFERENCE);
                this.mCollapsingTextHelper.draw(canvas);
                canvas.restoreToCount(iSave);
            }
            if (iTransform != -1) {
                canvas.restoreToCount(iTransform);
            }
        }
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        updateFadeModeExpansionFraction();
        updateTextLayoutIfNeed();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        StaticLayout staticLayout;
        super.onLayout(z2, i3, i4, i5, i6);
        WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
        if (windowInsetsCompat != null) {
            int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
            int childCount = getChildCount();
            for (int i7 = 0; i7 < childCount; i7++) {
                View childAt = getChildAt(i7);
                if (!ViewCompat.getFitsSystemWindows(childAt) && childAt.getTop() < systemWindowInsetTop) {
                    ViewCompat.offsetTopAndBottom(childAt, systemWindowInsetTop);
                }
            }
        }
        int childCount2 = getChildCount();
        for (int i8 = 0; i8 < childCount2; i8++) {
            getViewOffsetHelper(getChildAt(i8)).onViewLayout();
        }
        updateTextBounds(i3, i4, i5, i6, true);
        updateTitleFromToolbarIfNeeded();
        updateScrimVisibility();
        int childCount3 = getChildCount();
        for (int i9 = 0; i9 < childCount3; i9++) {
            getViewOffsetHelper(getChildAt(i9)).applyOffsets();
        }
        this.mTextLayout = this.mCollapsingTextHelper.textLayout;
        if (this.mCurrentBounds.isEmpty()) {
            this.mCurrentBounds = this.mCollapsingTextHelper.currentBounds;
        }
        if (this.mCollapsedBounds.isEmpty()) {
            this.mCollapsedBounds = this.mCollapsingTextHelper.collapsedBounds;
        }
        if (this.mExpandedBounds.isEmpty()) {
            this.mExpandedBounds = this.mCollapsingTextHelper.expandedBounds;
        }
        translateTitleIfNeed();
        ensureCouiToolbar();
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        if (cOUIToolbar != null && cOUIToolbar.getIsTitleCenterStyle()) {
            this.mCollapsingTextHelper.setCollapsedBounds(this.mExpandedBounds);
            this.mCollapsingTextHelper.recalculate();
        }
        View view = this.mIconView;
        if (view != null && view.getVisibility() == 0 && (staticLayout = this.mTextLayout) != null) {
            if (calculateIsRtl(staticLayout.getText())) {
                this.mCollapsedBounds.left += this.mIconView.getMeasuredWidth();
                this.mExpandedBounds.left += this.mIconView.getMeasuredWidth();
            } else {
                this.mCollapsedBounds.right -= this.mIconView.getMeasuredWidth();
                this.mExpandedBounds.right -= this.mIconView.getMeasuredWidth();
            }
            this.mCollapsingTextHelper.recalculate();
        }
        updateIconViewLocationIfNeed();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        COUICollapsingToolbarLayout cOUICollapsingToolbarLayout;
        int dimensionPixelOffset;
        ensureToolbar();
        super.onMeasure(i3, i4);
        int mode = View.MeasureSpec.getMode(i4);
        WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
        int dimensionPixelOffset2 = 0;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        if ((mode == 0 || this.mForceApplySystemWindowInsetTop) && systemWindowInsetTop > 0) {
            this.mTopInsetApplied = systemWindowInsetTop;
            super.onMeasure(i3, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + systemWindowInsetTop, 1073741824));
        }
        if (!this.mExtraMultilineHeightEnabled || this.mCollapsingTextHelper.getMaxLines() <= 1) {
            cOUICollapsingToolbarLayout = this;
        } else {
            updateTitleFromToolbarIfNeeded();
            cOUICollapsingToolbarLayout = this;
            cOUICollapsingToolbarLayout.updateTextBounds(0, 0, getMeasuredWidth(), getMeasuredHeight(), true);
            int expandedLineCount = cOUICollapsingToolbarLayout.mCollapsingTextHelper.getExpandedLineCount();
            if (expandedLineCount > 1) {
                cOUICollapsingToolbarLayout.mExtraMultilineHeight = Math.round(cOUICollapsingToolbarLayout.mCollapsingTextHelper.getExpandedTextFullHeight()) * (expandedLineCount - 1);
                super.onMeasure(i3, View.MeasureSpec.makeMeasureSpec(cOUICollapsingToolbarLayout.getMeasuredHeight() + cOUICollapsingToolbarLayout.mExtraMultilineHeight, 1073741824));
            }
        }
        ViewGroup viewGroup = cOUICollapsingToolbarLayout.mToolbar;
        if (viewGroup != null) {
            View view = cOUICollapsingToolbarLayout.mToolbarDirectChild;
            if (view == null || view == cOUICollapsingToolbarLayout) {
                cOUICollapsingToolbarLayout.setMinimumHeight(getHeightWithMargins(viewGroup));
            } else {
                cOUICollapsingToolbarLayout.setMinimumHeight(getHeightWithMargins(view));
            }
        }
        if (cOUICollapsingToolbarLayout.mIsInsideSideNavigationBar) {
            cOUICollapsingToolbarLayout.setExpandedTitleMarginStart(cOUICollapsingToolbarLayout.mToolbarNormalPaddingStart);
            cOUICollapsingToolbarLayout.setExpandedTitleMarginEnd(cOUICollapsingToolbarLayout.mToolbarNormalPaddingEnd);
            return;
        }
        if (COUIResponsiveUtils.isSmallScreen(cOUICollapsingToolbarLayout.getContext(), cOUICollapsingToolbarLayout.getMeasuredWidth())) {
            dimensionPixelOffset2 = cOUICollapsingToolbarLayout.getContext().getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_title_expanded_margin_start_compat);
            dimensionPixelOffset = cOUICollapsingToolbarLayout.getContext().getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_title_expanded_margin_end_compat);
        } else if (COUIResponsiveUtils.isMediumScreen(cOUICollapsingToolbarLayout.getContext(), cOUICollapsingToolbarLayout.getMeasuredWidth(), UIUtil.getScreenHeightMetrics(cOUICollapsingToolbarLayout.getContext()))) {
            dimensionPixelOffset2 = cOUICollapsingToolbarLayout.getContext().getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_title_expanded_margin_start_medium);
            dimensionPixelOffset = cOUICollapsingToolbarLayout.getContext().getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_title_expanded_margin_end_medium);
        } else if (COUIResponsiveUtils.isLargeScreen(cOUICollapsingToolbarLayout.getContext(), cOUICollapsingToolbarLayout.getMeasuredWidth(), UIUtil.getScreenHeightMetrics(cOUICollapsingToolbarLayout.getContext()))) {
            dimensionPixelOffset2 = cOUICollapsingToolbarLayout.getContext().getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_title_expanded_margin_start_expanded);
            dimensionPixelOffset = cOUICollapsingToolbarLayout.getContext().getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_title_expanded_margin_end_expanded);
        } else {
            dimensionPixelOffset = 0;
        }
        cOUICollapsingToolbarLayout.setExpandedTitleMarginStart(dimensionPixelOffset2);
        cOUICollapsingToolbarLayout.setExpandedTitleMarginEnd(dimensionPixelOffset);
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        Drawable drawable = this.mContentScrim;
        if (drawable != null) {
            updateContentScrimBounds(drawable, i3, i4);
        }
    }

    public WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat windowInsetsCompat) {
        WindowInsetsCompat windowInsetsCompat2 = ViewCompat.getFitsSystemWindows(this) ? windowInsetsCompat : null;
        if (!ObjectsCompat.equals(this.mLastInsets, windowInsetsCompat2)) {
            this.mLastInsets = windowInsetsCompat2;
            requestLayout();
        }
        return windowInsetsCompat.consumeSystemWindowInsets();
    }

    public void performEditModeTransition(CharSequence charSequence, Runnable runnable) {
        ensureCouiToolbar();
        cancelEditModeTransition();
        h hVar = new h(new boolean[]{false}, runnable);
        captureTitleExitOldPosition();
        prepareExitOverlayBeforeStructureSwitch();
        if (charSequence == null) {
            charSequence = getTitle();
        }
        if (this.mCouiToolbar == null) {
            switchTitleContentWithAnimation(charSequence, hVar);
        } else {
            this.mCouiToolbar.editModeGroupTransition(hVar, this.mTitleExitOverlayPrepared ? new i(this) : null, new j(this, charSequence));
        }
    }

    public void setCollapsedTitleGravity(int i3) {
        this.mCollapsingTextHelper.setCollapsedTextGravity(i3);
    }

    public void setCollapsedTitleTextAppearance(int i3) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(i3);
    }

    public void setCollapsedTitleTextColor(int i3) {
        setCollapsedTitleTextColor(ColorStateList.valueOf(i3));
    }

    public void setCollapsedTitleTypeface(Typeface typeface) {
        this.mCollapsingTextHelper.setCollapsedTypeface(typeface);
    }

    public void setContentScrim(Drawable drawable) {
        Drawable drawable2 = this.mContentScrim;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            Drawable drawableMutate = drawable != null ? drawable.mutate() : null;
            this.mContentScrim = drawableMutate;
            if (drawableMutate != null) {
                updateContentScrimBounds(drawableMutate, getWidth(), getHeight());
                this.mContentScrim.setCallback(this);
                this.mContentScrim.setAlpha(this.mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setContentScrimColor(int i3) {
        setContentScrim(new ColorDrawable(i3));
    }

    public void setContentScrimResource(int i3) {
        setContentScrim(ContextCompat.getDrawable(getContext(), i3));
    }

    @Deprecated
    public void setEditStyle(boolean z2) {
        setIsTitleCenterStyle(z2);
    }

    public void setExpandedTitleColor(int i3) {
        setExpandedTitleTextColor(ColorStateList.valueOf(i3));
    }

    public void setExpandedTitleGravity(int i3) {
        this.mCollapsingTextHelper.setExpandedTextGravity(i3);
    }

    public void setExpandedTitleMargin(int i3, int i4, int i5, int i6) {
        this.mExpandedMarginStart = i3;
        this.mExpandedMarginTop = i4;
        this.mExpandedMarginEnd = i5;
        this.mExpandedMarginBottom = i6;
        requestLayout();
    }

    public void setExpandedTitleMarginBottom(int i3) {
        this.mExpandedMarginBottom = i3;
        requestLayout();
    }

    public void setExpandedTitleMarginEnd(int i3) {
        this.mExpandedMarginEnd = i3;
        requestLayout();
    }

    public void setExpandedTitleMarginStart(int i3) {
        this.mExpandedMarginStart = i3;
        requestLayout();
    }

    public void setExpandedTitleMarginTop(int i3) {
        this.mExpandedMarginTop = i3;
        requestLayout();
    }

    public void setExpandedTitleTextAppearance(int i3) {
        this.mCollapsingTextHelper.setExpandedTextAppearance(i3);
    }

    public void setExpandedTitleTextColor(ColorStateList colorStateList) {
        this.mCollapsingTextHelper.setExpandedTextColor(colorStateList);
    }

    public void setExpandedTitleTypeface(Typeface typeface) {
        this.mCollapsingTextHelper.setExpandedTypeface(typeface);
    }

    public void setExtraMultilineHeightEnabled(boolean z2) {
        this.mExtraMultilineHeightEnabled = z2;
    }

    public void setForceApplySystemWindowInsetTop(boolean z2) {
        this.mForceApplySystemWindowInsetTop = z2;
    }

    public void setHyphenationFrequency(int i3) {
        this.mCollapsingTextHelper.setHyphenationFrequency(i3);
    }

    public void setIconView(int i3) {
        setIconView(i3 == 0 ? null : LayoutInflater.from(getContext()).inflate(i3, (ViewGroup) this, false));
    }

    public void setIsInsideSideNavigationBar(boolean z2) {
        this.mIsInsideSideNavigationBar = z2;
        requestLayout();
    }

    public void setIsTitleCenterStyle(boolean z2) {
        setTitleCollapseMode(0);
        setContentScrim(this.mContentScrimDrawable);
        ensureCouiToolbar();
        COUIToolbar cOUIToolbar = this.mCouiToolbar;
        if (cOUIToolbar != null) {
            resetToolbarTextState(cOUIToolbar);
            this.mCouiToolbar.setIsTitleCenterStyle(z2);
            if (this.mCouiToolbar.getCOUITitleTextView() != null) {
                this.mCouiToolbar.getCOUITitleTextView().setAlpha(isExpanded() ? 0.0f : 1.0f);
            }
            if (z2) {
                getCollapsingTextHelper().setCollapsedTextColor(ColorStateList.valueOf(0));
            } else if (this.mCollapsedTextColor != null) {
                getCollapsingTextHelper().setCollapsedTextColor(this.mCollapsedTextColor);
            } else {
                getCollapsingTextHelper().setFadeModeStartFraction(0.5f);
            }
        }
        this.mIsAllowFadeInAnimation = isExpanded();
        this.mIsAllowFadeOutAnimation = !isExpanded();
    }

    public void setLineSpacingAdd(float f3) {
        this.mCollapsingTextHelper.setLineSpacingAdd(f3);
    }

    public void setLineSpacingMultiplier(float f3) {
        this.mCollapsingTextHelper.setLineSpacingMultiplier(f3);
    }

    public void setMaxLines(int i3) {
        this.mCollapsingTextHelper.setMaxLines(i3);
    }

    public void setRtlTextDirectionHeuristicsEnabled(boolean z2) {
        this.mCollapsingTextHelper.setRtlTextDirectionHeuristicsEnabled(z2);
    }

    public void setScrimAlpha(int i3) {
        ViewGroup viewGroup;
        if (i3 != this.mScrimAlpha) {
            if (this.mContentScrim != null && (viewGroup = this.mToolbar) != null) {
                ViewCompat.postInvalidateOnAnimation(viewGroup);
            }
            this.mScrimAlpha = i3;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setScrimAnimationDuration(long j3) {
        this.mScrimAnimationDuration = j3;
    }

    public void setScrimVisibleHeightTrigger(int i3) {
        if (this.mScrimVisibleHeightTrigger != i3) {
            this.mScrimVisibleHeightTrigger = i3;
            updateScrimVisibility();
        }
    }

    public void setScrimsShown(boolean z2) {
        setScrimsShown(z2, ViewCompat.isLaidOut(this) && !isInEditMode());
    }

    public void setStatusBarScrim(Drawable drawable) {
        Drawable drawable2 = this.mStatusBarScrim;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            Drawable drawableMutate = drawable != null ? drawable.mutate() : null;
            this.mStatusBarScrim = drawableMutate;
            if (drawableMutate != null) {
                if (drawableMutate.isStateful()) {
                    this.mStatusBarScrim.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.mStatusBarScrim, ViewCompat.getLayoutDirection(this));
                this.mStatusBarScrim.setVisible(getVisibility() == 0, false);
                this.mStatusBarScrim.setCallback(this);
                this.mStatusBarScrim.setAlpha(this.mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setStatusBarScrimColor(int i3) {
        setStatusBarScrim(new ColorDrawable(i3));
    }

    public void setStatusBarScrimResource(int i3) {
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), i3));
    }

    public void setTitle(CharSequence charSequence) {
        this.mCollapsingTextHelper.setText(charSequence);
        updateContentDescriptionFromTitle();
        View view = this.mIconView;
        if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this.mIconViewFirstLayoutListener);
        }
    }

    public void setTitleCollapseMode(int i3) {
        this.mTitleCollapseMode = i3;
        boolean zIsTitleCollapseFadeMode = isTitleCollapseFadeMode();
        this.mCollapsingTextHelper.setFadeModeEnabled(zIsTitleCollapseFadeMode);
        ViewParent parent = getParent();
        if (parent instanceof COUIAppBarLayout) {
            disableLiftOnScrollIfNeeded((COUIAppBarLayout) parent);
        }
        if (zIsTitleCollapseFadeMode && this.mContentScrim == null) {
            setContentScrimColor(this.mElevationOverlayProvider.compositeOverlayWithThemeSurfaceColorIfNeeded(getResources().getDimension(R.dimen.design_appbar_elevation)));
        }
    }

    public void setTitleEnabled(boolean z2) {
        if (z2 != this.mCollapsingTitleEnabled) {
            this.mCollapsingTitleEnabled = z2;
            updateContentDescriptionFromTitle();
            updateDummyView();
            requestLayout();
        }
    }

    public void setTitlePositionInterpolator(TimeInterpolator timeInterpolator) {
        this.mCollapsingTextHelper.setPositionInterpolator(timeInterpolator);
    }

    public void setTitleTransform(float f3, float f4, float f5) {
        boolean z2;
        boolean z3 = true;
        if (this.mTitleExtraScale != f3) {
            this.mTitleExtraScale = f3;
            z2 = true;
        } else {
            z2 = false;
        }
        if (this.mTitleExtraAlpha != f4) {
            this.mTitleExtraAlpha = f4;
            z2 = true;
        }
        if (this.mTitleExtraTranslationY != f5) {
            this.mTitleExtraTranslationY = f5;
        } else {
            z3 = z2;
        }
        if (z3) {
            invalidate();
        }
    }

    @Override // android.view.View
    public void setVisibility(int i3) {
        super.setVisibility(i3);
        boolean z2 = i3 == 0;
        Drawable drawable = this.mStatusBarScrim;
        if (drawable != null && drawable.isVisible() != z2) {
            this.mStatusBarScrim.setVisible(z2, false);
        }
        Drawable drawable2 = this.mContentScrim;
        if (drawable2 == null || drawable2.isVisible() == z2) {
            return;
        }
        this.mContentScrim.setVisible(z2, false);
    }

    public void switchTitleContentWithAnimation(CharSequence charSequence, Runnable runnable) {
        if (prepareTitleSwitchState(charSequence, runnable)) {
            removePendingTitlePreDraw();
            registerTitleCrossFadePreDraw(charSequence);
        }
    }

    public void updateIconViewLocationIfNeed() {
        View view;
        int measuredWidth;
        int i3;
        StaticLayout staticLayout = this.mTextLayout;
        if (staticLayout == null || this.mTextPaint == null) {
            return;
        }
        CharSequence text = staticLayout.getText();
        if (TextUtils.isEmpty(text) || (view = this.mIconView) == null || view.getVisibility() != 0) {
            return;
        }
        String string = text.toString();
        boolean z2 = string.contains(ELLIPSIS_TWO_DOTS) || string.contains(ELLIPSIS_NORMAL);
        Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
        float f3 = this.mCollapsingTextHelper.scale;
        boolean zCalculateIsRtl = calculateIsRtl(text);
        int i4 = (int) ((fontMetricsInt.descent - fontMetricsInt.ascent) * f3);
        int measuredHeight = (int) (((getMeasuredHeight() - this.mCurrentBounds.top) - i4) + (this.mTitleTranslateOffset * this.mCollapsingTextHelper.getExpansionFraction()));
        int measuredWidth2 = (getMeasuredWidth() - this.mIconView.getMeasuredWidth()) - getExpandedTitleMarginEnd();
        if (!z2) {
            measuredWidth2 = Math.min((int) ((zCalculateIsRtl ? getMeasuredWidth() - this.mCurrentBounds.right : this.mCurrentBounds.left) + ((int) (this.mTextLayout.getLineWidth(0) * f3))), measuredWidth2);
        } else if (!isExpanded()) {
            measuredWidth2 = (int) (zCalculateIsRtl ? this.mCurrentBounds.width() + getExpandedTitleMarginStart() : this.mCurrentBounds.right);
        }
        int measuredWidth3 = this.mIconView.getMeasuredWidth();
        if (measuredWidth3 <= 0 || getMeasuredHeight() <= 0) {
            return;
        }
        int measuredHeight2 = ((getMeasuredHeight() - getPaddingBottom()) - measuredHeight) - i4;
        int i5 = i4 + measuredHeight2;
        if (ViewCompat.getLayoutDirection(this) == 1) {
            measuredWidth = (getMeasuredWidth() - getPaddingRight()) - measuredWidth2;
            i3 = measuredWidth - measuredWidth3;
        } else {
            int paddingLeft = getPaddingLeft() + measuredWidth2;
            measuredWidth = measuredWidth3 + paddingLeft;
            i3 = paddingLeft;
        }
        this.mIconView.layout(i3, measuredHeight2, measuredWidth, i5);
    }

    public final void updateScrimVisibility() {
        if (this.mContentScrim == null && this.mStatusBarScrim == null) {
            return;
        }
        setScrimsShown(getHeight() + this.mCurrentOffset < getScrimVisibleHeightTrigger());
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mContentScrim || drawable == this.mStatusBarScrim;
    }

    public COUICollapsingToolbarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.collapsingToolbarLayoutStyle);
    }

    private void updateContentScrimBounds(Drawable drawable, View view, int i3, int i4) {
        if (isTitleCollapseFadeMode() && view != null && this.mCollapsingTitleEnabled) {
            i4 = view.getBottom();
        }
        drawable.setBounds(0, 0, i3, i4);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return generateDefaultLayoutParams();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return generateLayoutParams(layoutParams);
    }

    public void setCollapsedTitleTextColor(ColorStateList colorStateList) {
        this.mCollapsingTextHelper.setCollapsedTextColor(colorStateList);
        this.mCollapsedTextColor = colorStateList;
    }

    public void setScrimsShown(boolean z2, boolean z3) {
        if (this.mScrimsAreShown != z2) {
            if (z3) {
                animateScrim(z2 ? 255 : 0);
            } else {
                setScrimAlpha(z2 ? 255 : 0);
            }
            this.mScrimsAreShown = z2;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public COUICollapsingToolbarLayout(Context context, AttributeSet attributeSet, int i3) {
        int i4 = DEF_STYLE_RES;
        super(MaterialThemeOverlay.wrap(context, attributeSet, i3, i4), attributeSet, i3);
        this.mTitleTransformPivotScratch = new float[2];
        this.mRefreshToolbar = true;
        this.mTmpRect = new Rect();
        this.mScrimVisibleHeightTrigger = -1;
        this.mTopInsetApplied = 0;
        this.mExtraMultilineHeight = 0;
        this.mCollapsedSubtitleMarginTopIfNotHidden = 0;
        this.mToolbarNormalPaddingStart = 0;
        this.mToolbarNormalPaddingEnd = 0;
        this.mFadeModeExpansionFraction = 0.15f;
        this.mFadeModeExpansionFractionError = 0.02f;
        this.mCollapsedBounds = new Rect();
        this.mExpandedBounds = new Rect();
        this.mCurrentBounds = new RectF();
        this.mTitleSwitchExitOldRect = new RectF();
        this.mCollapsedTitleOverlayRectScratch = new RectF();
        this.mTitleExitOverlayPrepared = false;
        this.mIsContentSwitching = false;
        this.mIsAllowFadeInAnimation = false;
        this.mIsAllowFadeOutAnimation = false;
        this.mIsInsideSideNavigationBar = false;
        this.mTitleExtraScale = 1.0f;
        this.mTitleExtraAlpha = 1.0f;
        this.mTitleExtraTranslationY = 0.0f;
        this.mContentScrimDrawable = new ColorDrawable(0);
        this.mIconViewFirstLayoutListener = new AnonymousClass1();
        Context context2 = getContext();
        COUICollapsingTextHelper cOUICollapsingTextHelper = new COUICollapsingTextHelper(this);
        this.mCollapsingTextHelper = cOUICollapsingTextHelper;
        cOUICollapsingTextHelper.setRtlTextDirectionHeuristicsEnabled(false);
        this.mElevationOverlayProvider = new ElevationOverlayProvider(context2);
        TypedArray typedArrayObtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R.styleable.CollapsingToolbarLayout, i3, i4, new int[0]);
        cOUICollapsingTextHelper.setExpandedTextGravity(typedArrayObtainStyledAttributes.getInt(R.styleable.CollapsingToolbarLayout_expandedTitleGravity, 8388691));
        cOUICollapsingTextHelper.setCollapsedTextGravity(typedArrayObtainStyledAttributes.getInt(R.styleable.CollapsingToolbarLayout_collapsedTitleGravity, NavigationBarView.ITEM_GRAVITY_START_CENTER));
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
        this.mExpandedMarginBottom = dimensionPixelSize;
        this.mExpandedMarginEnd = dimensionPixelSize;
        this.mExpandedMarginTop = dimensionPixelSize;
        this.mExpandedMarginStart = dimensionPixelSize;
        int i5 = R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart;
        if (typedArrayObtainStyledAttributes.hasValue(i5)) {
            this.mExpandedMarginStart = typedArrayObtainStyledAttributes.getDimensionPixelSize(i5, 0);
        }
        int i6 = R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd;
        if (typedArrayObtainStyledAttributes.hasValue(i6)) {
            this.mExpandedMarginEnd = typedArrayObtainStyledAttributes.getDimensionPixelSize(i6, 0);
        }
        int i7 = R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop;
        if (typedArrayObtainStyledAttributes.hasValue(i7)) {
            this.mExpandedMarginTop = typedArrayObtainStyledAttributes.getDimensionPixelSize(i7, 0);
        }
        int i8 = R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom;
        if (typedArrayObtainStyledAttributes.hasValue(i8)) {
            this.mExpandedMarginBottom = typedArrayObtainStyledAttributes.getDimensionPixelSize(i8, 0);
        }
        this.mCollapsingTitleEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.CollapsingToolbarLayout_titleEnabled, true);
        setTitle(typedArrayObtainStyledAttributes.getText(R.styleable.CollapsingToolbarLayout_title));
        cOUICollapsingTextHelper.setExpandedTextAppearance(R.style.TextAppearance_Design_CollapsingToolbar_Expanded);
        cOUICollapsingTextHelper.setCollapsedTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
        int i9 = R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance;
        if (typedArrayObtainStyledAttributes.hasValue(i9)) {
            cOUICollapsingTextHelper.setExpandedTextAppearance(typedArrayObtainStyledAttributes.getResourceId(i9, 0));
        }
        int i10 = R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance;
        if (typedArrayObtainStyledAttributes.hasValue(i10)) {
            cOUICollapsingTextHelper.setCollapsedTextAppearance(typedArrayObtainStyledAttributes.getResourceId(i10, 0));
        }
        this.mCollapsedTitleTextSizeDefault = cOUICollapsingTextHelper.getCollapsedTextSize();
        int i11 = R.styleable.CollapsingToolbarLayout_expandedTitleTextColor;
        if (typedArrayObtainStyledAttributes.hasValue(i11)) {
            cOUICollapsingTextHelper.setExpandedTextColor(MaterialResources.getColorStateList(context2, typedArrayObtainStyledAttributes, i11));
        }
        int i12 = R.styleable.CollapsingToolbarLayout_collapsedTitleTextColor;
        if (typedArrayObtainStyledAttributes.hasValue(i12)) {
            cOUICollapsingTextHelper.setCollapsedTextColor(MaterialResources.getColorStateList(context2, typedArrayObtainStyledAttributes, i12));
        }
        this.mScrimVisibleHeightTrigger = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1);
        int i13 = R.styleable.CollapsingToolbarLayout_maxLines;
        if (typedArrayObtainStyledAttributes.hasValue(i13)) {
            cOUICollapsingTextHelper.setMaxLines(typedArrayObtainStyledAttributes.getInt(i13, 1));
        }
        this.mScrimAnimationDuration = typedArrayObtainStyledAttributes.getInt(R.styleable.CollapsingToolbarLayout_scrimAnimationDuration, 600);
        setContentScrim(typedArrayObtainStyledAttributes.getDrawable(R.styleable.CollapsingToolbarLayout_contentScrim));
        setStatusBarScrim(typedArrayObtainStyledAttributes.getDrawable(R.styleable.CollapsingToolbarLayout_statusBarScrim));
        setTitleCollapseMode(typedArrayObtainStyledAttributes.getInt(R.styleable.CollapsingToolbarLayout_titleCollapseMode, 0));
        this.mToolbarId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.CollapsingToolbarLayout_toolbarId, -1);
        this.mForceApplySystemWindowInsetTop = typedArrayObtainStyledAttributes.getBoolean(R.styleable.CollapsingToolbarLayout_forceApplySystemWindowInsetTop, false);
        this.mExtraMultilineHeightEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.CollapsingToolbarLayout_extraMultilineHeightEnabled, false);
        typedArrayObtainStyledAttributes.recycle();
        initCoui(attributeSet);
        this.mCollapsedSubtitleMarginTopIfNotHidden = context2.getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_subtitle_collapsed_margin_top);
        this.mCollapsedTitleTextSizeWithSubtitle = context2.getResources().getDimension(com.support.toolbar.R.dimen.coui_appbar_title_collapsed_with_subtitle_text_size);
        this.mToolbarNormalPaddingStart = getContext().getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_title_expanded_margin_start);
        this.mToolbarNormalPaddingEnd = getContext().getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_appbar_title_expanded_margin_end);
        this.mCollapsedTextColor = cOUICollapsingTextHelper.getCollapsedTextColor();
        setWillNotDraw(false);
        ViewCompat.setOnApplyWindowInsetsListener(this, new AnonymousClass2());
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public void setIconView(View view) {
        View view2 = this.mIconView;
        if (view2 == view) {
            return;
        }
        if (view == null) {
            if (view2 != null) {
                removeView(view2);
            }
            this.mIconView = null;
        } else {
            this.mIconView = view;
            addIconView();
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        public static final int COLLAPSE_MODE_OFF = 0;
        public static final int COLLAPSE_MODE_PARALLAX = 2;
        public static final int COLLAPSE_MODE_PIN = 1;
        private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5f;
        int collapseMode;
        float parallaxMult;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CollapsingToolbarLayout_Layout);
            this.collapseMode = typedArrayObtainStyledAttributes.getInt(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, 0);
            setParallaxMultiplier(typedArrayObtainStyledAttributes.getFloat(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, 0.5f));
            typedArrayObtainStyledAttributes.recycle();
        }

        public int getCollapseMode() {
            return this.collapseMode;
        }

        public float getParallaxMultiplier() {
            return this.parallaxMult;
        }

        public void setCollapseMode(int i3) {
            this.collapseMode = i3;
        }

        public void setParallaxMultiplier(float f3) {
            this.parallaxMult = f3;
        }

        public LayoutParams(int i3, int i4) {
            super(i3, i4);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
        }

        public LayoutParams(int i3, int i4, int i5) {
            super(i3, i4, i5);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
        }

        public LayoutParams(FrameLayout.LayoutParams layoutParams) {
            super(layoutParams);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
        }
    }
}