package com.coui.appcompat.bottomfloatingnavigationbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.animation.touchanimation.COUIIComposeMotionUnit;
import com.coui.appcompat.animation.touchanimation.COUITouchMotionAnimator;
import com.coui.appcompat.animation.touchanimation.OnAnimationEndListener;
import com.coui.appcompat.bottomfloatingtoolbar.COUIBottomFloatingToolbarItemView;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.graphics.OplusPathAdapter;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.bottomnavigation.R;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public abstract class BaseBottomFloatingMenuView extends ViewGroup implements OnMaterialStateChangeListener {
    private static final float BLUR_WINDOW_WEIGHT = 1.0f;
    private static final float COLOR_CHANNEL_MAX = 255.0f;
    private static final int PANEL_FALLBACK_OVERLAY_COLOR_DARK = -13421773;
    private static final int PANEL_FALLBACK_OVERLAY_COLOR_LIGHT = -986896;
    private static final boolean SHADER_TOUCH_FEEDBACK_ENABLED = false;
    private static final int SHADOW_EDGE_CORNER_TYPE_G2 = 2;
    private static final float SMOOTH_CORNER_FACTOR = 1.0f;
    private static final float TOUCH_EFFECT_ALPHA_OPAQUE = 255.0f;
    private static final float TOUCH_EFFECT_ALPHA_SPRING_BOUNCE = 0.0f;
    private static final float TOUCH_EFFECT_ALPHA_SPRING_RESPONSE = 0.2f;
    private static final int TOUCH_EFFECT_CENTER_ALPHA = 637534208;
    private static final int TOUCH_EFFECT_LIGHT_COLOR = -1;
    protected OnAnimationSyncListener mAnimationSyncListener;
    protected final RectF mBackgroundBounds;
    protected final Path mBackgroundPath;
    protected final Rect mBackgroundRect;
    protected final SmoothRoundCornerHelper mBackgroundSmoothRoundCornerHelper;
    private View mChildUnderOnTouchDown;
    protected final RectF mDrawingBounds;
    protected int mFixedVisibleHeight;
    protected int mInnerPadding;
    protected boolean mIsDrawableMaterialEffect;
    protected int mItemMaxWidth;
    protected int mItemSpace;
    private OnDrawingBoundsChangedListener mOnDrawingBoundsChangedListener;
    protected OnItemClickListener mOnItemClickListener;
    private OplusMaterialCornerParams mOplusMaterialCornerParams;
    private ColorDrawable mPanelFallbackOverlayDrawable;
    protected float mRadius;
    private COUIShadowEdgeDrawable mShadowEdgeDrawable;
    private COUISpotLightEffectDrawable mSpotLightEffectDrawable;
    protected COUISpringAnimation mTouchEffectAlphaAnim;
    protected Matrix mTouchEffectMatrix;
    protected Paint mTouchEffectPaint;
    protected float mTouchEffectRadius;
    protected Shader mTouchEffectShader;
    protected COUITouchMotionAnimator mTouchMotionAnimator;
    protected float mTouchX;
    protected float mTouchY;
    private ColorDrawable mTransparentBackgroundDrawable;
    private ColorStateList mUniformItemTitleTextColor;
    private int mUseMaterialEffectAttr;

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (BaseBottomFloatingMenuView.access$000(BaseBottomFloatingMenuView.this)) {
                outline.setPath(BaseBottomFloatingMenuView.this.mBackgroundPath);
                return;
            }
            BaseBottomFloatingMenuView baseBottomFloatingMenuView = BaseBottomFloatingMenuView.this;
            Rect rect = baseBottomFloatingMenuView.mBackgroundRect;
            RectF rectF = baseBottomFloatingMenuView.mBackgroundBounds;
            rect.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
            BaseBottomFloatingMenuView baseBottomFloatingMenuView2 = BaseBottomFloatingMenuView.this;
            outline.setRoundRect(baseBottomFloatingMenuView2.mBackgroundRect, baseBottomFloatingMenuView2.mRadius);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView$2, reason: invalid class name */
    public class AnonymousClass2 implements OnAnimationEndListener {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.animation.touchanimation.OnAnimationEndListener
        public void onAnimationEnd(COUIIComposeMotionUnit cOUIIComposeMotionUnit, boolean z2, float f3, float f4) {
            BaseBottomFloatingMenuView.access$100(BaseBottomFloatingMenuView.this, false);
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.bottomfloatingnavigationbar.BaseBottomFloatingMenuView$3, reason: invalid class name */
    public class AnonymousClass3 implements COUIDynamicAnimation.OnAnimationUpdateListener {
        public AnonymousClass3() {
        }

        @Override // com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            Paint paint = BaseBottomFloatingMenuView.this.mTouchEffectPaint;
            if (paint != null) {
                paint.setAlpha((int) f3);
                BaseBottomFloatingMenuView.this.invalidate();
            }
        }
    }

    public enum MaterialEffectState {
        FORCE_ENABLE(1),
        FORCE_DISABLE(-1),
        FOLLOW_SYSTEM(0);

        private final int mValue;

        MaterialEffectState(int i3) {
            this.mValue = i3;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    public interface OnAnimationSyncListener {
        void onAlphaAnimUpdate(float f3);

        void onForegroundBlurRadiusAnimUpdate(float f3);

        void onScaleAnimUpdate(float f3);

        void onTouchAnimUpdate(float f3, float f4, float f5, float f6);
    }

    public interface OnDrawingBoundsChangedListener {
        void onDrawingBoundsChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, BaseBottomFloatingItem baseBottomFloatingItem, int i3);
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

    public BaseBottomFloatingMenuView(Context context) {
        this(context, null);
    }

    public static /* synthetic */ void a(BaseBottomFloatingMenuView baseBottomFloatingMenuView, COUIIComposeMotionUnit.ContributeSink contributeSink) {
        baseBottomFloatingMenuView.getClass();
        baseBottomFloatingMenuView.setTranslationX(contributeSink.getTranslateX());
        baseBottomFloatingMenuView.setTranslationY(contributeSink.getTranslateY());
        baseBottomFloatingMenuView.setScaleX(contributeSink.getScaleX());
        baseBottomFloatingMenuView.setScaleY(contributeSink.getScaleY());
        OnAnimationSyncListener onAnimationSyncListener = baseBottomFloatingMenuView.mAnimationSyncListener;
        if (onAnimationSyncListener != null) {
            onAnimationSyncListener.onTouchAnimUpdate(contributeSink.getTranslateX(), contributeSink.getTranslateY(), contributeSink.getScaleX(), contributeSink.getScaleY());
        }
    }

    public static /* synthetic */ boolean access$000(BaseBottomFloatingMenuView baseBottomFloatingMenuView) {
        return baseBottomFloatingMenuView.isSmoothOn();
    }

    public static /* synthetic */ void access$100(BaseBottomFloatingMenuView baseBottomFloatingMenuView, boolean z2) {
        baseBottomFloatingMenuView.setItemTextPreventsShakeForAllChildren(z2);
    }

    private void applyPanelBackground() {
        if (this.mPanelFallbackOverlayDrawable == null) {
            this.mPanelFallbackOverlayDrawable = new ColorDrawable(COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext()) ? PANEL_FALLBACK_OVERLAY_COLOR_DARK : PANEL_FALLBACK_OVERLAY_COLOR_LIGHT);
        }
        setBackground(this.mPanelFallbackOverlayDrawable);
    }

    private void applySmoothCornerOutlineState() {
        if (this.mIsDrawableMaterialEffect) {
            setClipToOutline(true);
            return;
        }
        if (!isMaterialQualityEnable()) {
            setClipToOutline(true);
            this.mOplusMaterialCornerParams = null;
            return;
        }
        setClipToOutline(false);
        OplusMaterialCornerParams oplusMaterialCornerParams = this.mOplusMaterialCornerParams;
        if (oplusMaterialCornerParams == null) {
            this.mOplusMaterialCornerParams = new OplusMaterialCornerParams(this.mRadius, 1.0f);
        } else {
            oplusMaterialCornerParams.setRadius(this.mRadius);
        }
        COUIMaterialEffect.setCornerParams(this, this.mOplusMaterialCornerParams);
    }

    private void applySpotLightColor(COUISpotLightEffectDrawable cOUISpotLightEffectDrawable, int i3) {
        cOUISpotLightEffectDrawable.setLightColor(Color.red(i3) / 255.0f, Color.green(i3) / 255.0f, Color.blue(i3) / 255.0f);
    }

    private void applyTouchEffect(MotionEvent motionEvent) {
        if (COUIMaterialStateManager.INSTANCE.isFeatureEnabled() && isMenuTouchFeedbackEnabled() && isSpotLightEffectEnable()) {
            handleTouchEffectDrawableEvent(motionEvent);
        }
    }

    private RadialGradient createTouchEffectRadialGradient(int i3) {
        int i4 = i3 & 16777215;
        return new RadialGradient(0.0f, 0.0f, this.mTouchEffectRadius, i4 | TOUCH_EFFECT_CENTER_ALPHA, i4, Shader.TileMode.CLAMP);
    }

    private void drawPanelStroke(Canvas canvas) {
        if (isStrokeEffectEnabled() && this.mIsDrawableMaterialEffect && this.mShadowEdgeDrawable != null) {
            float fWidth = this.mBackgroundBounds.width();
            float fHeight = this.mBackgroundBounds.height();
            if (fWidth <= 0.0f || fHeight <= 0.0f) {
                return;
            }
            this.mShadowEdgeDrawable.setBounds(Math.round(this.mBackgroundBounds.left), Math.round(this.mBackgroundBounds.top), Math.round(this.mBackgroundBounds.left + fWidth), Math.round(this.mBackgroundBounds.top + fHeight));
            this.mShadowEdgeDrawable.setCornerRadius(fHeight / 2.0f);
            canvas.translate(getScrollX(), getScrollY());
            this.mShadowEdgeDrawable.draw(canvas);
            canvas.translate(-getScrollX(), -getScrollY());
        }
    }

    private void drawTouchEffectDrawable(Canvas canvas) {
        if (!isSpotLightEffectEnable() || this.mSpotLightEffectDrawable == null) {
            return;
        }
        int iSave = canvas.save();
        canvas.clipPath(this.mBackgroundPath);
        this.mSpotLightEffectDrawable.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    private int getLastNonGoneChildIndex() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            if (getChildAt(childCount).getVisibility() != 8) {
                return childCount;
            }
        }
        return -1;
    }

    private int getNonGoneChildCount() {
        int childCount = getChildCount();
        int i3 = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            if (getChildAt(i4).getVisibility() != 8) {
                i3++;
            }
        }
        return i3;
    }

    private void handleTouchEffectDrawableEvent(MotionEvent motionEvent) {
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.onTouchEvent(motionEvent);
        }
    }

    private void handleTouchEffectShaderEvent(MotionEvent motionEvent) {
        Paint paint;
        COUISpringAnimation cOUISpringAnimation;
        int action = motionEvent.getAction();
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        boolean zIsTouchInBounds = isTouchInBounds(x2, y2);
        if (action != 0) {
            if (action == 2) {
                updateTouchPosition(x2, y2);
            }
        } else {
            if (!zIsTouchInBounds || (paint = this.mTouchEffectPaint) == null || (cOUISpringAnimation = this.mTouchEffectAlphaAnim) == null) {
                return;
            }
            cOUISpringAnimation.setStartValue(paint.getAlpha());
            this.mTouchEffectAlphaAnim.animateToFinalPosition(255.0f);
            updateTouchPosition(x2, y2);
        }
    }

    private void initAnim() {
        initTouchEffectAlphaAnim();
        initTouchMotionAnim();
    }

    private void initDimensions() {
        this.mItemMaxWidth = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_max_width);
        this.mFixedVisibleHeight = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_height);
        this.mInnerPadding = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_padding);
        this.mItemSpace = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_item_space);
        this.mTouchEffectRadius = getResources().getDimensionPixelSize(R.dimen.coui_bottom_floating_navigation_bar_touch_effect_radius);
    }

    private void initShadowEdgeDrawableIfNeeded() {
        if (COUIMaterialEffect.isMaterialVersion() && !COUIMaterialStateManager.INSTANCE.isLowAnimationLevel() && this.mShadowEdgeDrawable == null) {
            this.mShadowEdgeDrawable = new COUIShadowEdgeDrawable();
            Context context = getContext();
            boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context);
            COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(context, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_12);
            this.mShadowEdgeDrawable.setEdgeEnable(true).setShadowEnable(true).setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_0).setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_0).setLineAlpha(strokeParamsByType.getEdgeAlpha()).setLineWidth(strokeParamsByType.getEdgeWidth(context)).setLineAngle(strokeParamsByType.getEdgeAngle()).setFadeAlpha(strokeParamsByType.getShadowFadeIn(), strokeParamsByType.getShadowFadeOut()).setFadeWidthScale(strokeParamsByType.getShadowFadeScale()).setIsDarkMode(zIsCOUIDarkThemeOrNightMode).setCornerType(2).setWeight(1.0f).setPaintBlendMode(BlendMode.SRC_OVER);
        }
    }

    private void initTexture() {
        updateOuterShadow();
        updateStrokeEffect();
        updateBlurEffect();
    }

    private void initTouchEffectAlphaAnim() {
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.2f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mTouchEffectAlphaAnim = spring;
        spring.addUpdateListener(new AnonymousClass3());
    }

    private void initTouchEffectDrawable() {
        if (this.mSpotLightEffectDrawable != null) {
            return;
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = new COUISpotLightEffectDrawable();
        this.mSpotLightEffectDrawable = cOUISpotLightEffectDrawable;
        cOUISpotLightEffectDrawable.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_OPAQUE_MEDIUM_2);
        applySpotLightColor(this.mSpotLightEffectDrawable, resolveTouchEffectLightColor());
    }

    private void initTouchEffectShader() {
        if (this.mTouchEffectPaint != null) {
            return;
        }
        this.mTouchEffectPaint = new Paint(1);
        this.mTouchEffectMatrix = new Matrix();
        RadialGradient radialGradientCreateTouchEffectRadialGradient = createTouchEffectRadialGradient(resolveTouchEffectLightColor());
        this.mTouchEffectShader = radialGradientCreateTouchEffectRadialGradient;
        this.mTouchEffectPaint.setShader(radialGradientCreateTouchEffectRadialGradient);
        this.mTouchEffectPaint.setAlpha(0);
    }

    private void initTouchMotionAnim() {
        COUITouchMotionAnimator cOUITouchMotionAnimator = new COUITouchMotionAnimator(getContext());
        this.mTouchMotionAnimator = cOUITouchMotionAnimator;
        cOUITouchMotionAnimator.setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(true).addOnAnimationUpdateCallback(new a(this));
        this.mTouchMotionAnimator.addEndListener(new AnonymousClass2());
    }

    private boolean isBlurEffectEnabled() {
        if (!this.mIsDrawableMaterialEffect) {
            return COUIMaterialStateManager.INSTANCE.isBlurEffectEnable();
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return COUIMaterialStateManager.INSTANCE.isBlurEffectEnable();
    }

    private boolean isCausticShadowEffectEnabled() {
        if (!this.mIsDrawableMaterialEffect) {
            return COUIMaterialStateManager.INSTANCE.isCausticShadowEffectEnable();
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return COUIMaterialStateManager.INSTANCE.isCausticShadowEffectEnable();
    }

    private boolean isMaterialQualityEnable() {
        return isBlurEffectEnabled();
    }

    private boolean isSmoothOn() {
        return this.mBackgroundSmoothRoundCornerHelper.getSmoothStyleType() == 1;
    }

    private boolean isSpotLightEffectEnable() {
        if (!this.mIsDrawableMaterialEffect) {
            return COUIMaterialStateManager.INSTANCE.isSpotLightEffectEnable();
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return COUIMaterialStateManager.INSTANCE.isSpotLightEffectEnable();
    }

    private boolean isStrokeEffectEnabled() {
        if (!this.mIsDrawableMaterialEffect) {
            return COUIMaterialStateManager.INSTANCE.isStrokeEffectEnable();
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        if (this.mUseMaterialEffectAttr == MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return COUIMaterialStateManager.INSTANCE.isStrokeEffectEnable();
    }

    private void measureAllChildrenExactlyZero() {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 1073741824);
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            getChildAt(i3).measure(iMakeMeasureSpec, iMakeMeasureSpec);
        }
    }

    private void performClickOnChildUnder(float f3, float f4) {
        View view = this.mChildUnderOnTouchDown;
        this.mChildUnderOnTouchDown = null;
        if (shouldPerformClickOnTouchUp()) {
            View viewFindChildViewUnder = findChildViewUnder(f3, f4);
            if ((viewFindChildViewUnder == null || viewFindChildViewUnder != view) && viewFindChildViewUnder != null && viewFindChildViewUnder.isEnabled()) {
                viewFindChildViewUnder.performClick();
            }
        }
    }

    private int resolveTouchEffectLightColor() {
        return -1;
    }

    private void setItemTextPreventsShakeForAllChildren(boolean z2) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIBottomFloatingToolbarItemView) {
                ((COUIBottomFloatingToolbarItemView) childAt).setItemTextPreventsShake(z2);
            }
        }
    }

    private void setTouchMotionAnim(MotionEvent motionEvent) {
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.handleTouchEvent(motionEvent);
        }
    }

    private void updateBlurEffect() {
        if (this.mIsDrawableMaterialEffect) {
            UIUtil.setViewShadowClippingEnabled(this, true);
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
            applyPanelBackground();
        } else {
            if (!isMaterialQualityEnable()) {
                if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
                    COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
                }
                UIUtil.setViewShadowClippingEnabled(this, false);
                setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.coui_tab_navigation_view_bg));
                return;
            }
            UIUtil.setViewShadowClippingEnabled(this, true);
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_FRAMEWORK_BOTTOM_BAR);
            if (this.mTransparentBackgroundDrawable == null) {
                this.mTransparentBackgroundDrawable = new ColorDrawable(0);
            }
            setBackground(this.mTransparentBackgroundDrawable);
        }
    }

    private void updateEdgeDrawable() {
        COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT_BASE_1);
        if (this.mShadowEdgeDrawable == null) {
            initShadowEdgeDrawableIfNeeded();
        }
        invalidate();
    }

    private void updateMaterialRoundedCorners() {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable;
        float measuredHeight = ((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) / 2.0f;
        if (isStrokeEffectEnabled() && this.mIsDrawableMaterialEffect && (cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable) != null) {
            cOUIShadowEdgeDrawable.setCornerRadius(measuredHeight);
            this.mShadowEdgeDrawable.setBounds(0, 0, getWidth(), getHeight());
        } else if (isMaterialQualityEnable()) {
            if (this.mOplusMaterialCornerParams == null) {
                this.mOplusMaterialCornerParams = new OplusMaterialCornerParams(this.mRadius, 1.0f);
            }
            this.mOplusMaterialCornerParams.setRadius(measuredHeight);
            COUIMaterialEffect.setCornerParams(this, this.mOplusMaterialCornerParams);
        }
    }

    private void updateOuterShadow() {
        if (isCausticShadowEffectEnabled()) {
            setCausticShadowEffect();
        } else {
            ShadowUtils.setElevationToView(this, 2, getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R.dimen.support_shadow_size_level_three), COUIContextUtil.getColor(getContext(), R.color.coui_bottom_floating_navigation_bar_menu_shadow_color));
            COUIMaterialEffect.clearCausticShadow(this);
        }
    }

    private void updateStrokeEffect() {
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            if (this.mIsDrawableMaterialEffect) {
                updateEdgeDrawable();
            } else if (isStrokeEffectEnabled()) {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_3);
            } else {
                COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
            }
        }
    }

    private void updateTouchEffect() {
        if (COUIMaterialStateManager.INSTANCE.isFeatureEnabled()) {
            isSpotLightEffectEnable();
            initTouchEffectDrawable();
            refreshMenuTouchFeedback();
        }
    }

    private void updateTouchEffectThemeColors() {
        int iResolveTouchEffectLightColor = resolveTouchEffectLightColor();
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setSpotLightType(getContext(), COUISpotLightEffect.SpotLightType.TYPE_OPAQUE_MEDIUM_2);
            applySpotLightColor(this.mSpotLightEffectDrawable, iResolveTouchEffectLightColor);
        }
    }

    public abstract void addItems(List<? extends BaseBottomFloatingItem> list);

    public void applyUniformItemTitleTextColors() {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof COUIBottomFloatingToolbarItemView) {
                ((COUIBottomFloatingToolbarItemView) childAt).setTitleTextColor(this.mUniformItemTitleTextColor);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        isSpotLightEffectEnable();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        boolean zIsTouchInBounds = isTouchInBounds(x2, y2);
        if (action == 0 && !zIsTouchInBounds) {
            return false;
        }
        setTouchMotionAnim(motionEvent);
        applyTouchEffect(motionEvent);
        if (action == 0) {
            this.mChildUnderOnTouchDown = findChildViewUnder(x2, y2);
            if (zIsTouchInBounds && isMenuTouchFeedbackEnabled()) {
                setItemTextPreventsShakeForAllChildren(true);
            }
        } else if (action == 2) {
            onMenuTouchMove(x2, y2, zIsTouchInBounds);
        } else if (action == 1) {
            performClickOnChildUnder(x2, y2);
            handleTouchRelease();
        } else if (action == 3) {
            this.mChildUnderOnTouchDown = null;
            handleTouchRelease();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public View findChildViewUnder(float f3, float f4) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8 && f3 >= childAt.getLeft() && f3 <= childAt.getRight() && f4 >= childAt.getTop() && f4 <= childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    public RectF getDrawingBounds() {
        return this.mDrawingBounds;
    }

    public int getTotalItemSpace() {
        int nonGoneChildCount = getNonGoneChildCount();
        if (nonGoneChildCount > 1) {
            return (nonGoneChildCount - 1) * this.mItemSpace;
        }
        return 0;
    }

    public int getVisibleChildCount() {
        int i3 = 0;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            if (getChildAt(i4).getVisibility() == 0) {
                i3++;
            }
        }
        return i3;
    }

    public void handleTouchRelease() {
        isSpotLightEffectEnable();
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    public boolean isMenuTouchFeedbackEnabled() {
        return true;
    }

    public boolean isTouchInBounds(float f3, float f4) {
        if (this.mDrawingBounds.isEmpty()) {
            int width = getWidth();
            int height = getHeight();
            return width > 0 && height > 0 && f3 >= 0.0f && f3 <= ((float) width) && f4 >= 0.0f && f4 <= ((float) height);
        }
        float x2 = getX() + f3;
        float y2 = getY() + f4;
        RectF rectF = this.mDrawingBounds;
        return x2 >= rectF.left && x2 <= rectF.right && y2 >= rectF.top && y2 <= rectF.bottom;
    }

    public void notifyDrawingBoundsChanged() {
        OnDrawingBoundsChangedListener onDrawingBoundsChangedListener = this.mOnDrawingBoundsChangedListener;
        if (onDrawingBoundsChangedListener != null) {
            onDrawingBoundsChangedListener.onDrawingBoundsChanged();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(this);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mShadowEdgeDrawable;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(null);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setCallback(null);
            this.mSpotLightEffectDrawable.cancel();
        }
        COUISpringAnimation cOUISpringAnimation = this.mTouchEffectAlphaAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.cancel();
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateBackgroundBounds();
        updateSmoothRoundPath();
        drawPanelStroke(canvas);
        drawTouchEffectDrawable(canvas);
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
        int i7 = i6 - i4;
        int i8 = i5 - i3;
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        int lastNonGoneChildIndex = getLastNonGoneChildIndex();
        if (lastNonGoneChildIndex < 0) {
            updateMaterialRoundedCorners();
            return;
        }
        int i9 = isLayoutRtl() ? i8 - this.mInnerPadding : this.mInnerPadding;
        int i10 = 0;
        while (i10 < childCount) {
            View childAt = getChildAt(i10);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i11 = (i7 - measuredHeight) / 2;
                int i12 = isLayoutRtl() ? i9 - measuredWidth : i9;
                childAt.layout(i12, i11, i12 + measuredWidth, measuredHeight + i11);
                boolean z3 = i10 < lastNonGoneChildIndex;
                if (isLayoutRtl()) {
                    i9 -= measuredWidth;
                    if (z3) {
                        i9 -= this.mItemSpace;
                    }
                } else {
                    i9 += measuredWidth;
                    if (z3) {
                        i9 += this.mItemSpace;
                    }
                }
            }
            i10++;
        }
        updateMaterialRoundedCorners();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            updateStrokeEffect();
            return;
        }
        if (i3 == 2) {
            updateBlurEffect();
            applySmoothCornerOutlineState();
        } else if (i3 == 4) {
            updateTouchEffect();
        } else if (i3 == 8) {
            updateOuterShadow();
        }
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        int i5;
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mFixedVisibleHeight, Integer.MIN_VALUE);
        int childCount = getChildCount();
        int nonGoneChildCount = getNonGoneChildCount();
        int size = View.MeasureSpec.getSize(i3);
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        if (nonGoneChildCount == 0) {
            measureAllChildrenExactlyZero();
            setMeasuredDimension(0, 0);
            return;
        }
        int totalItemSpace = (this.mItemMaxWidth * nonGoneChildCount) + (this.mInnerPadding * 2) + getTotalItemSpace();
        if (totalItemSpace <= size) {
            i5 = this.mItemMaxWidth;
        } else {
            int totalItemSpace2 = ((size - (this.mInnerPadding * 2)) - getTotalItemSpace()) / nonGoneChildCount;
            totalItemSpace = size;
            i5 = totalItemSpace2;
        }
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i5, 1073741824);
        int iMakeMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(0, 1073741824);
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                childAt.measure(iMakeMeasureSpec2, iMakeMeasureSpec);
            } else {
                childAt.measure(iMakeMeasureSpec3, iMakeMeasureSpec3);
            }
        }
        setMeasuredDimension(totalItemSpace, this.mFixedVisibleHeight);
    }

    public void onMenuTouchMove(float f3, float f4, boolean z2) {
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            cOUITouchMotionAnimator.onSizeChange(i3, i4);
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setBounds(0, 0, i3, i4);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public void refreshMenuTouchFeedback() {
        boolean zIsMenuTouchFeedbackEnabled = isMenuTouchFeedbackEnabled();
        COUITouchMotionAnimator cOUITouchMotionAnimator = this.mTouchMotionAnimator;
        if (cOUITouchMotionAnimator != null) {
            if (zIsMenuTouchFeedbackEnabled) {
                cOUITouchMotionAnimator.enable();
            } else {
                cOUITouchMotionAnimator.disable();
            }
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLightEffectDrawable;
        if (cOUISpotLightEffectDrawable != null) {
            cOUISpotLightEffectDrawable.setEnabled(zIsMenuTouchFeedbackEnabled && isSpotLightEffectEnable());
        }
    }

    public void refreshThemeAppearance() {
        updateOuterShadow();
        updateBlurEffect();
        updateStrokeEffect();
        updateTouchEffectThemeColors();
        refreshMenuTouchFeedback();
        applySmoothCornerOutlineState();
        invalidate();
    }

    public void setCausticShadowEffect() {
        ShadowUtils.setElevationToView(this, 7);
        COUIMaterialEffect.setOutlineCausticShadowColor(this, getResources().getColor(com.coui.appcompat.effect.R.color.coui_caustic_color_2));
        COUIMaterialEffect.setOutlineCausticShadowLayout(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_l_level4), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_t_level4), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_r_level4), getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_b_level4));
        COUIMaterialEffect.setOutlineCausticShadowParams(this, getResources().getDimensionPixelOffset(com.coui.appcompat.effect.R.dimen.coui_caustic_layout_sdf_level4));
    }

    public void setIsDrawableMaterialEffect(boolean z2) {
        if (this.mIsDrawableMaterialEffect == z2) {
            return;
        }
        this.mIsDrawableMaterialEffect = z2;
        refreshThemeAppearance();
    }

    public void setItemEnable(int i3, boolean z2) {
        View viewFindViewById = findViewById(i3);
        if (viewFindViewById != null) {
            viewFindViewById.setEnabled(z2);
        }
    }

    public void setItemsTitleTextColor(ColorStateList colorStateList) {
        this.mUniformItemTitleTextColor = colorStateList;
        applyUniformItemTitleTextColors();
    }

    public void setOnAnimationSyncListener(OnAnimationSyncListener onAnimationSyncListener) {
        this.mAnimationSyncListener = onAnimationSyncListener;
    }

    public void setOnDrawingBoundsChangedListener(OnDrawingBoundsChangedListener onDrawingBoundsChangedListener) {
        this.mOnDrawingBoundsChangedListener = onDrawingBoundsChangedListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setRedDot(int i3, int i4, int i5) {
        View viewFindViewById = findViewById(i3);
        if (viewFindViewById instanceof COUIBottomFloatingToolbarItemView) {
            ((COUIBottomFloatingToolbarItemView) viewFindViewById).setRedDot(i4, i5);
        }
    }

    public void setUseMaterialEffectAttr(MaterialEffectState materialEffectState) {
        int value = materialEffectState.getValue();
        if (this.mUseMaterialEffectAttr != value && UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
            if (cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(getContext())) {
                this.mUseMaterialEffectAttr = value;
                if (this.mIsDrawableMaterialEffect) {
                    refreshThemeAppearance();
                }
            }
        }
    }

    public abstract void setupItemAccessibility(View view, int i3);

    public boolean shouldPerformClickOnTouchUp() {
        return true;
    }

    public void updateBackgroundBounds() {
        if (isLayoutRtl()) {
            this.mBackgroundBounds.set(getWidth() - this.mDrawingBounds.width(), getHeight() - this.mDrawingBounds.height(), getWidth(), getHeight());
        } else {
            this.mBackgroundBounds.set(0.0f, 0.0f, this.mDrawingBounds.width(), this.mDrawingBounds.height());
        }
    }

    public void updateSmoothRoundPath() {
        this.mRadius = ((getHeight() - getPaddingTop()) - getPaddingBottom()) / 2.0f;
        this.mBackgroundPath.reset();
        if (isSmoothOn()) {
            OplusPathAdapter pathAdapter = this.mBackgroundSmoothRoundCornerHelper.getPathAdapter();
            RectF rectF = this.mBackgroundBounds;
            float f3 = this.mRadius;
            pathAdapter.addSmoothRoundRect(rectF, f3, f3, Path.Direction.CCW);
        } else {
            Path path = this.mBackgroundPath;
            RectF rectF2 = this.mBackgroundBounds;
            float f4 = this.mRadius;
            path.addRoundRect(rectF2, f4, f4, Path.Direction.CCW);
        }
        invalidateOutline();
    }

    public void updateTouchPosition(float f3, float f4) {
        this.mTouchX = f3;
        this.mTouchY = f4;
        invalidate();
    }

    public BaseBottomFloatingMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BaseBottomFloatingMenuView(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0);
    }

    public BaseBottomFloatingMenuView(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        Path path = new Path();
        this.mBackgroundPath = path;
        this.mBackgroundBounds = new RectF();
        this.mBackgroundRect = new Rect();
        this.mDrawingBounds = new RectF();
        this.mUseMaterialEffectAttr = MaterialEffectState.FOLLOW_SYSTEM.getValue();
        this.mIsDrawableMaterialEffect = false;
        initDimensions();
        this.mBackgroundSmoothRoundCornerHelper = new SmoothRoundCornerHelper(path);
        setOutlineProvider(new AnonymousClass1());
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);
        ViewCompat.setImportantForAccessibility(this, 1);
        applySmoothCornerOutlineState();
        initTexture();
        updateTouchEffect();
        initAnim();
        initShadowEdgeDrawableIfNeeded();
    }
}