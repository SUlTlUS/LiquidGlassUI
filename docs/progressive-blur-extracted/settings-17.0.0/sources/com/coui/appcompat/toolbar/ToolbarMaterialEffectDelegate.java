package com.coui.appcompat.toolbar;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.COUIViewTouchAnimator;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.effect.R;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.COUIAppBarLayout;
import com.oplus.graphics.OplusPathAdapter;
import com.oplus.view.material.OplusMaterialCornerParams;

/* JADX INFO: loaded from: classes.dex */
public class ToolbarMaterialEffectDelegate implements OnMaterialStateChangeListener {
    private static final float BLUR_WINDOW_CAPSULE_WEIGHT = 1.0f;
    private static final int BLUR_WINDOW_CORNER_TYPE = 2;
    private static final float BLUR_WINDOW_LINE_ALPHA = 1.0f;
    private static final float BLUR_WINDOW_LINE_ANGLE = 1.0f;
    private static final float BLUR_WINDOW_LINE_WIDTH_DP = 3.0f;
    private static final float BLUR_WINDOW_SHADOW_FADE_IN_DARK = 0.0f;
    private static final float BLUR_WINDOW_SHADOW_FADE_IN_LIGHT = 1.0f;
    private static final float BLUR_WINDOW_SHADOW_FADE_OUT_DARK = 0.5f;
    private static final float BLUR_WINDOW_SHADOW_FADE_OUT_LIGHT = 1.0f;
    private static final float BLUR_WINDOW_SHADOW_FADE_SCALE = 1.0f;
    private static final int BLUR_WINDOW_VIEW_BG_COLOR_DARK = -13421773;
    private static final int BLUR_WINDOW_VIEW_BG_COLOR_IMMERSIVE = 654311423;
    private static final int BLUR_WINDOW_VIEW_BG_COLOR_LIGHT = -986896;
    private static final float BLUR_WINDOW_WEIGHT = 2.0f;
    private static final float COLOR_ALPHA_MAX_F = 255.0f;
    private static final int COLOR_ARGB_OPAQUE = 255;
    private static final int COLOR_CHANNEL_MAX = 255;
    private static final int COLOR_CHANNEL_MIN = 0;
    private static final float CORNER_RADIUS_CAPSULE_WEIGHT = 1.0f;
    private static final float CORNER_RADIUS_DIVISOR = 2.0f;
    private static final float CORNER_RADIUS_WEIGHT = 2.0f;
    private static final String LOG_TAG_BLUR_WINDOW = "BlurWindow";
    private static final float MASK_MIN_PROGRESS_TOUCH_ENTER = 0.0f;
    private static final int MATERIAL_EFFECT_FOLLOW_SYSTEM = 0;
    private static final int MATERIAL_EFFECT_FORCE_DISABLE = -1;
    private static final int MATERIAL_EFFECT_FORCE_ENABLE = 1;
    public static final int NO_CUSTOM_NON_BLUR_FALLBACK_COLOR = 0;
    private static final int PANEL_FALLBACK_OVERLAY_COLOR_DARK = 872415231;
    private static final int PANEL_FALLBACK_OVERLAY_COLOR_LIGHT = 436207616;
    private static final int SHADOW_CLIP_EXPAND = 120;
    private static final String TAG = "MaterialEffectDelegate";
    private View.OnAttachStateChangeListener mAttachStateListener;
    private final RectF mBackgroundBounds;
    private final Path mBackgroundPath;
    private final SmoothRoundCornerHelper mBackgroundSmoothRoundCornerHelper;
    private boolean mBlurAllowed;
    private boolean mBlurOn;
    private COUIShadowEdgeDrawable mBlurWindowShadowEdge;
    private ViewCategory mCategory;
    private boolean mCausticShadowAllowed;
    private boolean mCausticShadowOn;
    private OplusMaterialCornerParams mCornerParams;
    private COUIMaterialBlurEffect.BaseBlurEffectParams mCustomBlurParams;
    private int mCustomNonBlurFallbackColor;
    private boolean mEnabled;
    private float mExternalScale;
    private View.OnTouchListener mExternalTouchListener;
    private boolean mIsDrawableMaterialEffect;
    private final RectF mItemRect = new RectF();
    private float mLastPressScaleX;
    private float mLastPressScaleY;
    private View.OnLayoutChangeListener mLayoutChangeListener;
    private boolean mLowAnim;
    private COUIMaskEffectDrawable mMaskEffectDrawable;
    private int mMaterialType;
    private boolean mPressAnimOn;
    private OnPressWhileInvisibleListener mPressWhileInvisibleListener;
    private COUISpotLightEffectDrawable mSpotLight;
    private boolean mSpotLightAllowed;
    private boolean mSpotLightOn;
    private COUIStateEffectDrawable mStateEffectBackground;
    private boolean mStrokeAllowed;
    private boolean mStrokeOn;
    private COUIViewTouchAnimator mTouchAnimator;
    private int mUseMaterialEffectAttr;
    private View mView;

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.ToolbarMaterialEffectDelegate$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (ToolbarMaterialEffectDelegate.access$000(ToolbarMaterialEffectDelegate.this) && ToolbarMaterialEffectDelegate.access$100(ToolbarMaterialEffectDelegate.this) && !ToolbarMaterialEffectDelegate.access$200(ToolbarMaterialEffectDelegate.this).isEmpty()) {
                outline.setPath(ToolbarMaterialEffectDelegate.access$200(ToolbarMaterialEffectDelegate.this));
            } else {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getHeight() / 2.0f);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.toolbar.ToolbarMaterialEffectDelegate$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnAttachStateChangeListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            ToolbarMaterialEffectDelegate.access$300(ToolbarMaterialEffectDelegate.this);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(ToolbarMaterialEffectDelegate.this);
            ToolbarMaterialEffectDelegate.access$400(ToolbarMaterialEffectDelegate.this);
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public static final class BlendModeColorDrawable extends Drawable {
        private final Paint mPaint;

        public BlendModeColorDrawable(int i3, BlendMode blendMode) {
            Paint paint = new Paint(1);
            this.mPaint = paint;
            paint.setColor(i3);
            paint.setBlendMode(blendMode);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            canvas.drawRect(getBounds(), this.mPaint);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i3) {
            this.mPaint.setAlpha(i3);
            invalidateSelf();
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.mPaint.setColorFilter(colorFilter);
            invalidateSelf();
        }
    }

    /* JADX INFO: loaded from: classes7.dex */
    public interface OnPressWhileInvisibleListener {
        void onDownWhileInvisible();
    }

    /* JADX INFO: loaded from: classes7.dex */
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

    public enum ViewCategory {
        MENU_ITEM(false),
        MENU_OVERFLOW_BUTTON(true),
        TOOLBAR_BUTTON(true);

        final boolean mUseOverlay;

        ViewCategory(boolean z2) {
            this.mUseOverlay = z2;
        }
    }

    private ToolbarMaterialEffectDelegate() {
        Path path = new Path();
        this.mBackgroundPath = path;
        this.mBackgroundBounds = new RectF();
        this.mBackgroundSmoothRoundCornerHelper = new SmoothRoundCornerHelper(path);
        this.mEnabled = true;
        this.mBlurOn = true;
        this.mStrokeOn = true;
        this.mSpotLightOn = true;
        this.mCausticShadowOn = true;
        this.mPressAnimOn = true;
        this.mCustomNonBlurFallbackColor = 0;
        this.mMaterialType = 0;
        this.mIsDrawableMaterialEffect = false;
        this.mUseMaterialEffectAttr = 0;
        this.mExternalScale = 1.0f;
        this.mLastPressScaleX = 1.0f;
        this.mLastPressScaleY = 1.0f;
    }

    public static /* synthetic */ void a(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate, View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        toolbarMaterialEffectDelegate.getClass();
        int i11 = i5 - i3;
        int i12 = i6 - i4;
        if (i11 == i9 - i7 && i12 == i10 - i8) {
            return;
        }
        toolbarMaterialEffectDelegate.onViewSizeChanged(i11, i12);
    }

    public static /* synthetic */ boolean access$000(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate) {
        return toolbarMaterialEffectDelegate.isCapsule();
    }

    public static /* synthetic */ boolean access$100(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate) {
        return toolbarMaterialEffectDelegate.isSmoothOn();
    }

    public static /* synthetic */ Path access$200(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate) {
        return toolbarMaterialEffectDelegate.mBackgroundPath;
    }

    public static /* synthetic */ void access$300(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate) {
        toolbarMaterialEffectDelegate.onAttachedSync();
    }

    public static /* synthetic */ void access$400(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate) {
        toolbarMaterialEffectDelegate.clearSpotLight();
    }

    private void applyBlurEffect() {
        if (this.mView == null) {
            return;
        }
        applyStateEffectBackground(true);
        COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams = this.mCustomBlurParams;
        if (baseBlurEffectParams != null) {
            COUIMaterialEffect.setViewBlurEffect(this.mView, baseBlurEffectParams);
        } else {
            COUIMaterialEffect.setViewBlurEffect(this.mView, ToolbarMaterialEffectResolver.getBlurEffectType(this.mMaterialType));
        }
    }

    private void applyBlurWindowBackground() {
        View view = this.mView;
        if (view != null) {
            Context context = view.getContext();
            boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context);
            if (this.mBlurWindowShadowEdge == null) {
                this.mBlurWindowShadowEdge = new COUIShadowEdgeDrawable();
            }
            COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(context, ToolbarMaterialEffectResolver.getCircleStrokeEffectTypeInWindow(this.mMaterialType));
            if (isCapsule()) {
                strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(context, ToolbarMaterialEffectResolver.getCapsuleStrokeEffectTypeInWindow(this.mMaterialType));
            }
            COUIShadowEdgeDrawable weight = this.mBlurWindowShadowEdge.setEdgeEnable(true).setShadowEnable(true).setEdgeStyle(isCapsule() ? IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_0 : IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_1).setShadowStyle(isCapsule() ? IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_0 : IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_1).setLineAlpha(strokeParamsByType.getEdgeAlpha()).setLineWidth(strokeParamsByType.getEdgeWidth(context)).setLineAngle(strokeParamsByType.getEdgeAngle()).setFadeAlpha(strokeParamsByType.getShadowFadeIn(), strokeParamsByType.getShadowFadeOut()).setFadeWidthScale(strokeParamsByType.getShadowFadeScale()).setIsDarkMode(zIsCOUIDarkThemeOrNightMode).setCornerType(2).setWeight(isCapsule() ? 1.0f : 2.0f);
            BlendMode blendMode = BlendMode.SRC_OVER;
            weight.setPaintBlendMode(blendMode);
            int i3 = zIsCOUIDarkThemeOrNightMode ? BLUR_WINDOW_VIEW_BG_COLOR_DARK : BLUR_WINDOW_VIEW_BG_COLOR_LIGHT;
            if (this.mMaterialType == 1) {
                i3 = BLUR_WINDOW_VIEW_BG_COLOR_IMMERSIVE;
            }
            this.mView.setBackground(new LayerDrawable(new Drawable[]{new BlendModeColorDrawable(i3, blendMode), this.mBlurWindowShadowEdge}));
            int width = this.mView.getWidth();
            int height = this.mView.getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }
            this.mBlurWindowShadowEdge.setCornerRadius(height / 2.0f);
        }
    }

    private void applyCausticShadowEffect() {
        if (this.mView == null) {
            return;
        }
        if (!COUIMaterialEffect.isCausticShadowVersion()) {
            clearCausticShadowEffectAndApplyOuterShadow();
            return;
        }
        ShadowUtils.setElevationToView(this.mView, 6);
        View view = this.mView;
        COUIMaterialEffect.setOutlineCausticShadowColor(view, view.getResources().getColor(R.color.coui_caustic_color_1));
        View view2 = this.mView;
        COUIMaterialEffect.setOutlineCausticShadowLayout(view2, view2.getResources().getDimensionPixelSize(R.dimen.coui_caustic_layout_l_level2), this.mView.getResources().getDimensionPixelSize(R.dimen.coui_caustic_layout_t_level2), this.mView.getResources().getDimensionPixelSize(R.dimen.coui_caustic_layout_r_level2), this.mView.getResources().getDimensionPixelSize(R.dimen.coui_caustic_layout_b_level2));
        View view3 = this.mView;
        COUIMaterialEffect.setOutlineCausticShadowParams(view3, view3.getResources().getDimensionPixelSize(R.dimen.coui_caustic_layout_sdf_level2));
    }

    private void applyComposedScale() {
        View view = this.mView;
        if (view == null) {
            return;
        }
        view.setScaleX(this.mLastPressScaleX * this.mExternalScale);
        this.mView.setScaleY(this.mLastPressScaleY * this.mExternalScale);
    }

    private void applyPanelBackground() {
        if (this.mView == null) {
            return;
        }
        if (resolvedUseLayerDrawable()) {
            applyBlurWindowBackground();
        } else {
            this.mBlurWindowShadowEdge = null;
            applyStateEffectBackground(false);
        }
        if (wantCausticShadowOn()) {
            applyCausticShadowEffect();
        } else {
            clearCausticShadowEffectAndApplyOuterShadow();
        }
    }

    private void applyShadowClipExpandAlongAncestorChain(ViewParent viewParent, ViewGroup viewGroup) {
        while (viewParent instanceof ViewGroup) {
            ViewGroup viewGroup2 = (ViewGroup) viewParent;
            applyShadowClipExpandForAncestor(viewGroup2);
            if (viewGroup2 == viewGroup) {
                return;
            } else {
                viewParent = viewGroup2.getParent();
            }
        }
    }

    private void applyShadowClipExpandForAncestor(ViewGroup viewGroup) {
        UIUtil.setViewClipExpandSize(viewGroup, 120, 120, 120, 120);
        if (viewGroup.getClipToPadding()) {
            viewGroup.setClipToPadding(false);
        }
    }

    private void applyStateEffectBackground(boolean z2) {
        View view = this.mView;
        if (view == null) {
            return;
        }
        view.setDefaultFocusHighlightEnabled(false);
        int width = this.mView.getWidth();
        int height = this.mView.getHeight();
        float fMin = Math.min(width, height) / 2.0f;
        this.mMaskEffectDrawable = new COUIMaskEffectDrawable(this.mView.getContext(), 1);
        this.mItemRect.set(0.0f, 0.0f, width, height);
        this.mMaskEffectDrawable.setMaskRect(this.mItemRect, fMin, fMin);
        this.mMaskEffectDrawable.enableSelectedState(false);
        this.mMaskEffectDrawable.setMinProgressForTouchEnterAnimation(0.0f);
        this.mMaskEffectDrawable.setNativeStateEnabled(16, true);
        Drawable background = this.mView.getBackground();
        if (!((background instanceof ColorDrawable) && Color.alpha(((ColorDrawable) background).getColor()) != 0)) {
            background = (z2 || !this.mEnabled) ? new ColorDrawable(0) : new ColorDrawable(resolveNonBlurFallbackColor());
        }
        COUIStateEffectDrawable cOUIStateEffectDrawable = new COUIStateEffectDrawable(new Drawable[]{background, this.mMaskEffectDrawable});
        this.mStateEffectBackground = cOUIStateEffectDrawable;
        this.mView.setBackground(cOUIStateEffectDrawable);
    }

    private void applyStroke() {
        View view = this.mView;
        if (view == null) {
            return;
        }
        int width = view.getWidth();
        int height = this.mView.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        if (isCapsule()) {
            COUIMaterialEffect.setViewStrokeEffect(this.mView, ToolbarMaterialEffectResolver.getCapsuleStrokeEffectType(this.mMaterialType));
        } else {
            COUIMaterialEffect.setViewStrokeEffect(this.mView, ToolbarMaterialEffectResolver.getCircleStrokeEffectType(this.mMaterialType));
        }
    }

    private void applyStructural() {
        View view = this.mView;
        if (view == null) {
            return;
        }
        view.setOutlineProvider(new AnonymousClass1());
        this.mView.setClipToOutline(true);
    }

    private void attachSpotLightToView() {
        if (!wantSpotLightOn() || this.mView == null) {
            return;
        }
        initSpotLight();
        if (this.mSpotLight == null) {
            return;
        }
        syncSpotLightBounds();
        if (this.mCategory.mUseOverlay) {
            this.mView.getOverlay().add(this.mSpotLight);
        } else {
            this.mSpotLight.setCallback(this.mView);
        }
    }

    public static /* synthetic */ boolean b(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate, View view, MotionEvent motionEvent) {
        return toolbarMaterialEffectDelegate.handleTouch(view, motionEvent);
    }

    private static int blendSrcOver(int i3, int i4) {
        float fAlpha = Color.alpha(i3) / 255.0f;
        float f3 = 1.0f - fAlpha;
        return Color.argb(255, clamp255(Math.round((Color.red(i3) * fAlpha) + (Color.red(i4) * f3))), clamp255(Math.round((Color.green(i3) * fAlpha) + (Color.green(i4) * f3))), clamp255(Math.round((Color.blue(i3) * fAlpha) + (Color.blue(i4) * f3))));
    }

    public static /* synthetic */ void c(ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        toolbarMaterialEffectDelegate.getClass();
        toolbarMaterialEffectDelegate.mLastPressScaleX = contributeSink.getScaleX();
        toolbarMaterialEffectDelegate.mLastPressScaleY = contributeSink.getScaleY();
        toolbarMaterialEffectDelegate.applyComposedScale();
    }

    private static int clamp255(int i3) {
        if (i3 < 0) {
            return 0;
        }
        if (i3 > 255) {
            return 255;
        }
        return i3;
    }

    private void clearBlurEffect() {
        if (this.mView == null) {
            return;
        }
        applyStateEffectBackground(false);
        COUIMaterialEffect.setViewBlurEffect(this.mView, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
    }

    private void clearCausticShadowEffectAndApplyOuterShadow() {
        View view = this.mView;
        if (view == null) {
            return;
        }
        ShadowUtils.setElevationToView(view, 5);
        COUIMaterialEffect.clearCausticShadow(this.mView);
    }

    private void clearSpotLight() {
        View view;
        if (this.mSpotLight != null) {
            if (this.mCategory.mUseOverlay && (view = this.mView) != null) {
                view.getOverlay().remove(this.mSpotLight);
            }
            this.mSpotLight.setCallback(null);
            this.mSpotLight.cancel();
            this.mSpotLight = null;
        }
    }

    private void clearStroke() {
        View view = this.mView;
        if (view == null) {
            return;
        }
        COUIMaterialEffect.setViewStrokeEffect(view, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT_BASE_2);
    }

    private static ToolbarMaterialEffectDelegate create(View view, ViewCategory viewCategory) {
        ToolbarMaterialEffectDelegate toolbarMaterialEffectDelegate = new ToolbarMaterialEffectDelegate();
        toolbarMaterialEffectDelegate.mCategory = viewCategory;
        toolbarMaterialEffectDelegate.init(view);
        return toolbarMaterialEffectDelegate;
    }

    private void ensureCornerParams(float f3) {
        if (this.mCornerParams == null) {
            this.mCornerParams = new OplusMaterialCornerParams(f3, isCapsule() ? 1.0f : 2.0f);
        }
    }

    private void expandClipToToolbar() {
        ViewGroup viewGroupFindToolbarShadowClipUpperBound;
        View view = this.mView;
        if (view == null || (viewGroupFindToolbarShadowClipUpperBound = findToolbarShadowClipUpperBound(view)) == null) {
            return;
        }
        applyShadowClipExpandAlongAncestorChain(this.mView.getParent(), viewGroupFindToolbarShadowClipUpperBound);
    }

    private ViewGroup findToolbarShadowClipUpperBound(View view) {
        ViewParent parent = view.getParent();
        COUIToolbar cOUIToolbar = null;
        ViewGroup viewGroup = null;
        while (parent instanceof ViewGroup) {
            ViewGroup viewGroup2 = (ViewGroup) parent;
            if (cOUIToolbar == null) {
                if (viewGroup2 instanceof COUIToolbar) {
                    cOUIToolbar = (COUIToolbar) viewGroup2;
                }
                parent = viewGroup2.getParent();
            } else {
                if ((viewGroup2 instanceof COUIAppBarLayout) || (viewGroup2 instanceof AppBarLayout)) {
                    return viewGroup2;
                }
                if (viewGroup2 instanceof CoordinatorLayout) {
                    return viewGroup != null ? viewGroup : cOUIToolbar;
                }
                viewGroup = viewGroup2;
                parent = viewGroup2.getParent();
            }
        }
        return cOUIToolbar;
    }

    public static ToolbarMaterialEffectDelegate forMenuItem(View view) {
        return create(view, ViewCategory.MENU_ITEM);
    }

    public static ToolbarMaterialEffectDelegate forMenuOverflowButton(View view) {
        return create(view, ViewCategory.MENU_OVERFLOW_BUTTON);
    }

    public static ToolbarMaterialEffectDelegate forToolbarButton(View view) {
        return create(view, ViewCategory.TOOLBAR_BUTTON);
    }

    private boolean handleTouch(View view, MotionEvent motionEvent) {
        View view2;
        OnPressWhileInvisibleListener onPressWhileInvisibleListener;
        if (!this.mEnabled) {
            View.OnTouchListener onTouchListener = this.mExternalTouchListener;
            return onTouchListener != null && onTouchListener.onTouch(view, motionEvent);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.onTouchEvent(motionEvent);
        }
        if (this.mTouchAnimator != null) {
            if (motionEvent.getActionMasked() == 0 && (view2 = this.mView) != null && view2.getAlpha() == 0.0f && (onPressWhileInvisibleListener = this.mPressWhileInvisibleListener) != null) {
                onPressWhileInvisibleListener.onDownWhileInvisible();
            }
            if (motionEvent.getAction() == 0 && view.getParent() != null) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            this.mTouchAnimator.handleTouchEvent(motionEvent);
        }
        View.OnTouchListener onTouchListener2 = this.mExternalTouchListener;
        if (onTouchListener2 != null) {
            return onTouchListener2.onTouch(view, motionEvent);
        }
        return false;
    }

    private void init(View view) {
        this.mView = view;
        COUIDarkModeUtil.setForceDarkAllow(view, false);
        this.mLowAnim = COUIMaterialStateManager.INSTANCE.isLowAnimationLevel();
        applyStructural();
        initPressAnimation();
        clearCausticShadowEffectAndApplyOuterShadow();
        UIUtil.setViewShadowClippingEnabled(this.mView, true);
        setupListeners();
    }

    private void initPressAnimation() {
        if (this.mView == null || this.mTouchAnimator != null) {
            return;
        }
        this.mLastPressScaleX = 1.0f;
        this.mLastPressScaleY = 1.0f;
        COUIViewTouchAnimator cOUIViewTouchAnimator = new COUIViewTouchAnimator(this.mView.getContext(), this.mView);
        this.mTouchAnimator = cOUIViewTouchAnimator;
        cOUIViewTouchAnimator.setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(true);
        this.mTouchAnimator.addOnAnimationUpdateCallback(new c0(this));
        applyComposedScale();
    }

    private void initSpotLight() {
        if (this.mView == null || this.mSpotLight != null) {
            return;
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = new COUISpotLightEffectDrawable();
        this.mSpotLight = cOUISpotLightEffectDrawable;
        cOUISpotLightEffectDrawable.setSpotLightType(this.mView.getContext(), COUISpotLightEffect.SpotLightType.TYPE_OPAQUE_SMALL_2);
        syncSpotLightBounds();
    }

    private boolean isCapsule() {
        View view = this.mView;
        if (view == null) {
            return false;
        }
        int width = view.getWidth();
        int height = this.mView.getHeight();
        return width > 0 && height > 0 && width != height;
    }

    private boolean isSmoothOn() {
        return this.mBackgroundSmoothRoundCornerHelper.getSmoothStyleType() == 1;
    }

    private void onAttachedSync() {
        if (!this.mEnabled || this.mView == null) {
            return;
        }
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
        syncBlurAndBackground();
        if (wantSpotLightOn()) {
            attachSpotLightToView();
        }
        expandClipToToolbar();
    }

    private void onViewSizeChanged(int i3, int i4) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        if (this.mEnabled) {
            if (isCapsule()) {
                updateSmoothRoundPath();
            }
            COUIViewTouchAnimator cOUIViewTouchAnimator = this.mTouchAnimator;
            if (cOUIViewTouchAnimator != null) {
                cOUIViewTouchAnimator.onSizeChange(i3, i4);
            }
            COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
            if (cOUISpotLightEffectDrawable != null) {
                cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
            }
            float fMin = Math.min(i3, i4) / 2.0f;
            syncMaterialCornerParamsFromViewBounds();
            if (wantStrokeOn() && i3 > 0 && i4 > 0) {
                applyStroke();
            }
            if (this.mMaskEffectDrawable != null) {
                this.mItemRect.set(0.0f, 0.0f, i3, i4);
                this.mMaskEffectDrawable.setMaskRect(this.mItemRect, fMin, fMin);
            }
            if (!this.mIsDrawableMaterialEffect || !resolvedUseLayerDrawable() || (cOUIShadowEdgeDrawable = this.mBlurWindowShadowEdge) == null || i3 <= 0 || i4 <= 0) {
                return;
            }
            cOUIShadowEdgeDrawable.setCornerRadius(i4 / 2.0f);
        }
    }

    private void reapplyAll() {
        View view;
        syncBlurAndBackground();
        if (wantStrokeOn()) {
            applyStroke();
        }
        if (wantCausticShadowOn()) {
            applyCausticShadowEffect();
        } else {
            clearCausticShadowEffectAndApplyOuterShadow();
        }
        if (wantSpotLightOn() && (view = this.mView) != null && view.isAttachedToWindow()) {
            attachSpotLightToView();
        }
        if (this.mPressAnimOn && this.mTouchAnimator == null) {
            initPressAnimation();
        }
        UIUtil.setViewShadowClippingEnabled(this.mView, true);
        expandClipToToolbar();
    }

    private void removeAllEffects() {
        clearBlurEffect();
        clearStroke();
        clearSpotLight();
        this.mTouchAnimator = null;
        View view = this.mView;
        if (view != null) {
            ShadowUtils.clearShadow(view);
            COUIMaterialEffect.clearCausticShadow(this.mView);
        }
    }

    private int resolveNonBlurFallbackColor() {
        int i3 = this.mCustomNonBlurFallbackColor;
        if (i3 != 0) {
            return i3;
        }
        Context context = this.mView.getContext();
        if (!this.mIsDrawableMaterialEffect) {
            return ToolbarMaterialEffectResolver.getToolbarBackgroundColor(context, this.mMaterialType);
        }
        if (this.mUseMaterialEffectAttr == -1) {
            if (this.mMaterialType == 1) {
                return BLUR_WINDOW_VIEW_BG_COLOR_IMMERSIVE;
            }
            return COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorFillSolid) : COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorBar);
        }
        if (this.mMaterialType == 1) {
            return BLUR_WINDOW_VIEW_BG_COLOR_IMMERSIVE;
        }
        return blendSrcOver(COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context) ? PANEL_FALLBACK_OVERLAY_COLOR_DARK : PANEL_FALLBACK_OVERLAY_COLOR_LIGHT, COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorSurfaceWithCard));
    }

    private boolean resolvedUseLayerDrawable() {
        int i3 = this.mUseMaterialEffectAttr;
        if (i3 == -1) {
            return false;
        }
        if (i3 != 1) {
            return this.mBlurAllowed;
        }
        return true;
    }

    private void setupListeners() {
        View view = this.mView;
        if (view == null) {
            return;
        }
        view.setOnTouchListener(new d0(this));
        e0 e0Var = new e0(this);
        this.mLayoutChangeListener = e0Var;
        this.mView.addOnLayoutChangeListener(e0Var);
        AnonymousClass2 anonymousClass2 = new AnonymousClass2();
        this.mAttachStateListener = anonymousClass2;
        this.mView.addOnAttachStateChangeListener(anonymousClass2);
        if (this.mView.isAttachedToWindow()) {
            onAttachedSync();
        }
        int width = this.mView.getWidth();
        int height = this.mView.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        onViewSizeChanged(width, height);
    }

    private void syncBlurAndBackground() {
        View view;
        if (!this.mEnabled || (view = this.mView) == null) {
            return;
        }
        if (this.mIsDrawableMaterialEffect) {
            COUIMaterialEffect.setViewBlurEffect(view, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
            COUIMaterialEffect.setViewStrokeEffect(this.mView, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT_BASE_2);
            applyPanelBackground();
        } else if (wantBlurOn()) {
            applyBlurEffect();
        } else {
            clearBlurEffect();
        }
    }

    private void syncMaterialCornerParamsFromViewBounds() {
        View view = this.mView;
        if (view == null) {
            return;
        }
        int width = view.getWidth();
        int height = this.mView.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        float fMin = Math.min(width, height) / 2.0f;
        if (wantBlurOn() || wantStrokeOn()) {
            ensureCornerParams(fMin);
            this.mCornerParams.setRadius(fMin);
            this.mCornerParams.setWeight(isCapsule() ? 1.0f : 2.0f);
            COUIMaterialEffect.setCornerParams(this.mView, this.mCornerParams);
        }
    }

    private void syncSpotLightBounds() {
        View view;
        if (this.mSpotLight == null || (view = this.mView) == null) {
            return;
        }
        int width = view.getWidth();
        int height = this.mView.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        this.mSpotLight.setBounds(0, 0, width, height);
    }

    private void updateSmoothRoundPath() {
        View view = this.mView;
        if (view == null) {
            return;
        }
        int width = view.getWidth();
        int height = this.mView.getHeight();
        float f3 = height;
        float f4 = f3 / 2.0f;
        this.mBackgroundBounds.set(0.0f, 0.0f, width, f3);
        this.mBackgroundPath.reset();
        if (width <= 0 || height <= 0) {
            return;
        }
        if (isCapsule() && isSmoothOn() && this.mBackgroundSmoothRoundCornerHelper.getPathAdapter() != null) {
            this.mBackgroundSmoothRoundCornerHelper.getPathAdapter().addSmoothRoundRect(this.mBackgroundBounds, f4, f4, Path.Direction.CCW);
        } else {
            this.mBackgroundPath.addRoundRect(this.mBackgroundBounds, f4, f4, Path.Direction.CCW);
        }
        this.mView.invalidateOutline();
    }

    private boolean wantBlurOn() {
        return !this.mIsDrawableMaterialEffect && this.mBlurOn && this.mBlurAllowed;
    }

    private boolean wantCausticShadowOn() {
        return this.mCausticShadowOn && this.mCausticShadowAllowed;
    }

    private boolean wantSpotLightOn() {
        return this.mSpotLightOn && this.mSpotLightAllowed;
    }

    private boolean wantStrokeOn() {
        return !this.mIsDrawableMaterialEffect && this.mStrokeOn && this.mStrokeAllowed;
    }

    public void detach() {
        View view = this.mView;
        if (view != null) {
            view.setOnTouchListener(null);
            View.OnLayoutChangeListener onLayoutChangeListener = this.mLayoutChangeListener;
            if (onLayoutChangeListener != null) {
                this.mView.removeOnLayoutChangeListener(onLayoutChangeListener);
            }
            View.OnAttachStateChangeListener onAttachStateChangeListener = this.mAttachStateListener;
            if (onAttachStateChangeListener != null) {
                this.mView.removeOnAttachStateChangeListener(onAttachStateChangeListener);
            }
            COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        }
        removeAllEffects();
        this.mView = null;
        this.mLayoutChangeListener = null;
        this.mAttachStateListener = null;
        this.mExternalTouchListener = null;
        this.mPressWhileInvisibleListener = null;
    }

    public void drawSpotLight(Canvas canvas) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable;
        if (this.mCategory.mUseOverlay || (cOUISpotLightEffectDrawable = this.mSpotLight) == null || !this.mEnabled || this.mView == null) {
            return;
        }
        cOUISpotLightEffectDrawable.draw(canvas);
    }

    public ViewCategory getCategory() {
        return this.mCategory;
    }

    public COUIMaskEffectDrawable getMaskEffectDrawable() {
        return this.mMaskEffectDrawable;
    }

    public COUIStateEffectDrawable getStateEffectBackground() {
        return this.mStateEffectBackground;
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        View view = this.mView;
        if (view != null) {
            return view.toString();
        }
        return null;
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (!this.mEnabled || this.mView == null) {
            return;
        }
        if (i3 == 2) {
            boolean zWantBlurOn = wantBlurOn();
            boolean z3 = this.mIsDrawableMaterialEffect && resolvedUseLayerDrawable();
            this.mBlurAllowed = z2;
            if (this.mIsDrawableMaterialEffect) {
                if (z3 != resolvedUseLayerDrawable()) {
                    applyPanelBackground();
                }
            } else if (zWantBlurOn != wantBlurOn()) {
                if (wantBlurOn()) {
                    applyBlurEffect();
                } else {
                    clearBlurEffect();
                }
            }
            syncMaterialCornerParamsFromViewBounds();
            return;
        }
        if (i3 == 1) {
            boolean zWantStrokeOn = wantStrokeOn();
            this.mStrokeAllowed = z2;
            if (zWantStrokeOn != wantStrokeOn()) {
                if (wantStrokeOn()) {
                    applyStroke();
                } else {
                    clearStroke();
                }
            }
            syncMaterialCornerParamsFromViewBounds();
            return;
        }
        if (i3 == 4) {
            boolean zWantSpotLightOn = wantSpotLightOn();
            this.mSpotLightAllowed = z2;
            if (zWantSpotLightOn != wantSpotLightOn()) {
                if (wantSpotLightOn()) {
                    attachSpotLightToView();
                    return;
                } else {
                    clearSpotLight();
                    return;
                }
            }
            return;
        }
        if (i3 == 8) {
            boolean zWantCausticShadowOn = wantCausticShadowOn();
            this.mCausticShadowAllowed = z2;
            if (zWantCausticShadowOn != wantCausticShadowOn()) {
                if (wantCausticShadowOn()) {
                    applyCausticShadowEffect();
                } else {
                    clearCausticShadowEffectAndApplyOuterShadow();
                }
            }
        }
    }

    public void refreshRipple(Context context) {
        COUIStateEffectDrawable cOUIStateEffectDrawable = this.mStateEffectBackground;
        if (cOUIStateEffectDrawable != null) {
            cOUIStateEffectDrawable.refresh(context);
        }
        syncBlurAndBackground();
    }

    public void setBlurOn(boolean z2) {
        if (this.mBlurOn == z2) {
            return;
        }
        boolean zWantBlurOn = wantBlurOn();
        this.mBlurOn = z2;
        if (zWantBlurOn != wantBlurOn()) {
            if (wantBlurOn()) {
                applyBlurEffect();
            } else {
                clearBlurEffect();
            }
        }
    }

    public void setCausticShadowOn(boolean z2) {
        if (this.mCausticShadowOn == z2) {
            return;
        }
        boolean zWantCausticShadowOn = wantCausticShadowOn();
        this.mCausticShadowOn = z2;
        if (zWantCausticShadowOn != wantCausticShadowOn()) {
            if (wantCausticShadowOn()) {
                applyCausticShadowEffect();
            } else {
                clearCausticShadowEffectAndApplyOuterShadow();
            }
        }
    }

    public void setCustomBlurParams(COUIMaterialBlurEffect.BaseBlurEffectParams baseBlurEffectParams) {
        this.mCustomBlurParams = baseBlurEffectParams;
        if (wantBlurOn()) {
            applyBlurEffect();
        }
    }

    public void setCustomNonBlurFallbackColor(int i3) {
        if (this.mCustomNonBlurFallbackColor == i3) {
            return;
        }
        this.mCustomNonBlurFallbackColor = i3;
        syncBlurAndBackground();
    }

    public void setEnabled(boolean z2) {
        if (this.mEnabled == z2) {
            return;
        }
        this.mEnabled = z2;
        if (z2) {
            reapplyAll();
        } else {
            removeAllEffects();
        }
    }

    public void setExternalScale(float f3) {
        this.mExternalScale = f3;
        applyComposedScale();
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        if (this.mIsDrawableMaterialEffect == z2) {
            return;
        }
        this.mIsDrawableMaterialEffect = z2;
        if (this.mView == null) {
            return;
        }
        if (z2) {
            syncBlurAndBackground();
            return;
        }
        this.mBlurWindowShadowEdge = null;
        syncBlurAndBackground();
        if (wantStrokeOn()) {
            applyStroke();
        }
        if (wantCausticShadowOn()) {
            applyCausticShadowEffect();
        } else {
            clearCausticShadowEffectAndApplyOuterShadow();
        }
    }

    public void setItemOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mExternalTouchListener = onTouchListener;
    }

    public void setMaterialEffectType(int i3) {
        if (this.mMaterialType == i3) {
            return;
        }
        this.mMaterialType = i3;
        syncBlurAndBackground();
        if (wantStrokeOn()) {
            applyStroke();
        }
    }

    public void setOnPressWhileInvisibleListener(OnPressWhileInvisibleListener onPressWhileInvisibleListener) {
        this.mPressWhileInvisibleListener = onPressWhileInvisibleListener;
    }

    public void setPressAnimationOn(boolean z2) {
        if (this.mPressAnimOn == z2) {
            return;
        }
        this.mPressAnimOn = z2;
        if (z2) {
            if (this.mTouchAnimator == null) {
                initPressAnimation();
            }
        } else {
            this.mTouchAnimator = null;
            this.mLastPressScaleX = 1.0f;
            this.mLastPressScaleY = 1.0f;
            applyComposedScale();
        }
    }

    public void setSpotLightOn(boolean z2) {
        if (this.mSpotLightOn == z2) {
            return;
        }
        boolean zWantSpotLightOn = wantSpotLightOn();
        this.mSpotLightOn = z2;
        if (zWantSpotLightOn != wantSpotLightOn()) {
            if (wantSpotLightOn()) {
                attachSpotLightToView();
            } else {
                clearSpotLight();
            }
        }
    }

    public void setStrokeOn(boolean z2) {
        if (this.mStrokeOn == z2) {
            return;
        }
        boolean zWantStrokeOn = wantStrokeOn();
        this.mStrokeOn = z2;
        if (zWantStrokeOn != wantStrokeOn()) {
            if (wantStrokeOn()) {
                applyStroke();
            } else {
                clearStroke();
            }
        }
    }

    public void setUseMaterialEffectAttr(int i3) {
        if (this.mUseMaterialEffectAttr == i3) {
            return;
        }
        this.mUseMaterialEffectAttr = i3;
        if (this.mView == null || !this.mIsDrawableMaterialEffect) {
            return;
        }
        applyPanelBackground();
    }
}