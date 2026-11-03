package com.coui.appcompat.searchview;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatImageView;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.animation.touchanimation.COUIViewTouchAnimator;
import com.coui.appcompat.animation.touchanimation.TouchMotionParamConfig;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.shadowedge.COUIShadowEdgeDrawable;
import com.coui.appcompat.shadowedge.IShadowEdgeEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffect;
import com.coui.appcompat.spotlight.COUISpotLightEffectDrawable;
import com.coui.appcompat.state.COUIMaskEffectDrawable;
import com.coui.appcompat.state.COUIMaskRippleDrawable;
import com.coui.appcompat.state.COUIStateEffectDrawable;
import com.coui.appcompat.toolbar.ToolbarMaterialEffectResolver;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class SearchBarIconButton extends AppCompatImageView {
    private static final float CORNER_RADIUS_PARAM = 60.0f;
    private static final float CORNER_WEIGHT_OVAL = 2.0f;
    private static final int SHADOW_EDGE_CORNER_TYPE_G2 = 2;
    private static final float SHADOW_EDGE_CORNER_WEIGHT = 2.0f;
    private final ColorDrawable mBackgroundColor;
    private boolean mBlurEffective;
    private final RectF mBounds;
    private OplusMaterialCornerParams mCornerParams;
    private int mDefaultBackgroundColor;
    private boolean mIsDrawableMaterialEffect;
    private COUIMaskEffectDrawable mMaskEffectDrawable;
    private COUIMaskRippleDrawable mMaskRippleDrawable;
    private int mMaterialType;
    private COUIShadowEdgeDrawable mNoBlurShadowEdge;
    private int mNoBlurSolidColor;
    private float mNoBlurStrokeProgress;
    private int mNormalBackgroundColorAlpha;
    private final Path mPath;
    private final COUISpotLightEffectDrawable mSpotLight;
    private COUISpotLightEffect.SpotLightType mSpotLightType;
    private boolean mSpotlightEnabled;
    private boolean mStrokeEffectForceDark;
    private boolean mStrokeEnabled;
    private COUIMaterialStrokeEffect.StrokeEffectType mStrokeType;
    private final COUIViewTouchAnimator mTouchAnimator;

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.SearchBarIconButton$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }

    public SearchBarIconButton(Context context, boolean z2, boolean z3) {
        this(context);
        this.mIsDrawableMaterialEffect = z2;
        this.mStrokeEffectForceDark = z3;
        refreshMaterialSurfaceEffects();
    }

    private void applyFrameworkMaterialSurface() {
        this.mBackgroundColor.setColor(this.mDefaultBackgroundColor);
        if (this.mBlurEffective) {
            COUIMaterialEffect.setViewBlurEffect(this, ToolbarMaterialEffectResolver.getBlurEffectType(this.mMaterialType));
        } else {
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
        }
        if (this.mStrokeEnabled) {
            COUIMaterialEffect.setViewStrokeEffect(this, ToolbarMaterialEffectResolver.getCircleStrokeEffectType(this.mMaterialType));
        } else {
            COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
        }
        setSpotLightEnabled(this.mSpotlightEnabled);
        setBackgroundColorTransparent(this.mBlurEffective);
        setClipToOutline(!this.mBlurEffective);
        setCornerRadius(getMeasuredHeight() / 2.0f);
    }

    private void applyNoBlurMaterialSurface() {
        COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
        COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
        this.mBackgroundColor.setColor(this.mNoBlurSolidColor);
        setBackgroundColorTransparent(false);
        setClipToOutline(true);
        setCornerRadius(getMeasuredHeight() / 2.0f);
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck() && this.mStrokeEnabled) {
            applyNoBlurShadowInterpolated();
        } else {
            releaseNoBlurShadowEdge();
        }
        invalidate();
    }

    private void applyNoBlurShadowInterpolated() {
        ensureNoBlurShadowEdge();
        if (this.mNoBlurShadowEdge == null) {
            return;
        }
        setStrokeEffectType(this.mStrokeType, null);
    }

    private void drawNoBlurShadowEdge(Canvas canvas) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable == null || !cOUIShadowEdgeDrawable.getMDrawableEnabled()) {
            return;
        }
        int iRound = Math.round(this.mBounds.width());
        int iRound2 = Math.round(this.mBounds.height());
        if (iRound <= 0 || iRound2 <= 0) {
            return;
        }
        float fMin = Math.min(iRound, iRound2) / 2.0f;
        this.mNoBlurShadowEdge.setBounds(Math.round(this.mBounds.left), Math.round(this.mBounds.top), Math.round(this.mBounds.right), Math.round(this.mBounds.bottom));
        this.mNoBlurShadowEdge.setCornerRadius(fMin);
        this.mNoBlurShadowEdge.draw(canvas);
    }

    private void ensureNoBlurShadowEdge() {
        if (this.mNoBlurShadowEdge != null) {
            return;
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = new COUIShadowEdgeDrawable();
        this.mNoBlurShadowEdge = cOUIShadowEdgeDrawable;
        cOUIShadowEdgeDrawable.setCallback(this);
        this.mNoBlurShadowEdge.setEdgeEnable(true).setShadowEnable(true).setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_1).setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_1).setCornerType(2).setWeight(2.0f).setIsDarkMode(resolveShadowEdgeIsDarkMode());
        this.mNoBlurShadowEdge.setPaintBlendMode(BlendMode.SRC_OVER);
    }

    private void refreshMaterialSurfaceEffects() {
        if (this.mIsDrawableMaterialEffect) {
            applyNoBlurMaterialSurface();
        } else {
            applyFrameworkMaterialSurface();
        }
    }

    private void releaseNoBlurShadowEdge() {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setCallback(null);
            this.mNoBlurShadowEdge = null;
        }
    }

    private boolean resolveShadowEdgeIsDarkMode() {
        return this.mStrokeEffectForceDark || COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext());
    }

    private void setCornerRadius(float f3) {
        if (this.mBlurEffective) {
            if (this.mCornerParams == null) {
                this.mCornerParams = new OplusMaterialCornerParams(CORNER_RADIUS_PARAM, 2.0f);
            }
            this.mCornerParams.setRadius(f3);
            if (this.mIsDrawableMaterialEffect) {
                return;
            }
            COUIMaterialEffect.setCornerParams(this, this.mCornerParams);
        }
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.mSpotLight.onTouchEvent(motionEvent);
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mSpotLight.setCallback(this);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mSpotLight.setCallback(null);
        this.mSpotLight.cancel();
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        if (this.mSpotLight.getMEnabled()) {
            canvas.save();
            canvas.clipPath(this.mPath);
            this.mSpotLight.draw(canvas);
            canvas.restore();
        }
        super.onDraw(canvas);
        if (this.mIsDrawableMaterialEffect) {
            drawNoBlurShadowEdge(canvas);
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        COUIViewTouchAnimator cOUIViewTouchAnimator = this.mTouchAnimator;
        if (cOUIViewTouchAnimator != null) {
            cOUIViewTouchAnimator.onSizeChange(i3, i4);
        }
        this.mSpotLight.setBounds(0, 0, i3, i4);
        setCornerRadius(getMeasuredWidth() / 2.0f);
        this.mBounds.set(0.0f, 0.0f, getWidth(), getHeight());
        this.mPath.reset();
        this.mPath.addOval(this.mBounds, Path.Direction.CW);
    }

    public final void refreshThemeColors() {
        this.mDefaultBackgroundColor = ToolbarMaterialEffectResolver.getToolbarBackgroundColor(getContext(), this.mMaterialType);
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setIsDarkMode(resolveShadowEdgeIsDarkMode());
        }
        refreshMaterialSurfaceEffects();
    }

    public final void setBackgroundColorTransparent(boolean z2) {
        if (this.mMaskEffectDrawable == null) {
            COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(getContext(), 1);
            this.mMaskEffectDrawable = cOUIMaskEffectDrawable;
            cOUIMaskEffectDrawable.setMaskPath(this.mPath);
        }
        COUIMaskEffectDrawable cOUIMaskEffectDrawable2 = this.mMaskEffectDrawable;
        Drawable[] drawableArr = {cOUIMaskEffectDrawable2};
        if (!z2) {
            drawableArr = new Drawable[]{this.mBackgroundColor, cOUIMaskEffectDrawable2};
        }
        COUIStateEffectDrawable cOUIStateEffectDrawable = new COUIStateEffectDrawable(drawableArr);
        if (COUIMaterialStateManager.INSTANCE.materialVersionCheck()) {
            setBackground(cOUIStateEffectDrawable);
            return;
        }
        if (this.mMaskRippleDrawable == null) {
            COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(getContext());
            this.mMaskRippleDrawable = cOUIMaskRippleDrawable;
            cOUIMaskRippleDrawable.setCustomRippleMask();
        }
        setBackground(this.mMaskRippleDrawable);
    }

    public final void setBlurEffectEnable(boolean z2) {
        this.mBlurEffective = z2;
        refreshMaterialSurfaceEffects();
    }

    public final void setCrossWindowBlurEffect(boolean z2, boolean z3, int i3) {
        if (this.mIsDrawableMaterialEffect == z2 && this.mStrokeEffectForceDark == z3 && this.mNoBlurSolidColor == i3) {
            return;
        }
        this.mIsDrawableMaterialEffect = z2;
        if (!z2) {
            releaseNoBlurShadowEdge();
        }
        this.mStrokeEffectForceDark = z3;
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setIsDarkMode(resolveShadowEdgeIsDarkMode());
        }
        this.mNoBlurSolidColor = i3;
        if (this.mIsDrawableMaterialEffect) {
            this.mBackgroundColor.setColor(i3);
            refreshMaterialSurfaceEffects();
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        setSpotLightEnabled(this.mSpotlightEnabled);
    }

    public final void setMaterialEffectType(int i3) {
        if (this.mMaterialType == i3) {
            return;
        }
        this.mMaterialType = i3;
        this.mDefaultBackgroundColor = ToolbarMaterialEffectResolver.getToolbarBackgroundColor(getContext(), this.mMaterialType);
        refreshMaterialSurfaceEffects();
    }

    public final void setNoBlurStrokeProgress(float f3) {
        this.mNoBlurStrokeProgress = Math.max(0.0f, Math.min(f3, 1.0f));
        if (this.mIsDrawableMaterialEffect && this.mStrokeEnabled) {
            applyNoBlurShadowInterpolated();
            invalidate();
        }
    }

    public final void setSpotLightEffectType(COUISpotLightEffect.SpotLightType spotLightType, Context context) {
        if (spotLightType == null) {
            this.mSpotLightType = COUISpotLightEffect.SpotLightType.TYPE_OPAQUE_MEDIUM_2;
        } else {
            this.mSpotLightType = spotLightType;
        }
        if (context == null) {
            context = getContext();
        }
        COUISpotLightEffectDrawable cOUISpotLightEffectDrawable = this.mSpotLight;
        if (cOUISpotLightEffectDrawable == null || context == null) {
            return;
        }
        cOUISpotLightEffectDrawable.setSpotLightType(context, this.mSpotLightType);
    }

    public void setSpotLightEnabled(boolean z2) {
        this.mSpotlightEnabled = z2;
        this.mSpotLight.setEnabled(z2 && isEnabled());
    }

    public final void setStrokeEffectEnable(boolean z2) {
        this.mStrokeEnabled = z2;
        refreshMaterialSurfaceEffects();
    }

    public final void setStrokeEffectType(COUIMaterialStrokeEffect.StrokeEffectType strokeEffectType, Context context) {
        if (strokeEffectType == null) {
            this.mStrokeType = ToolbarMaterialEffectResolver.getCircleStrokeEffectTypeInWindow(this.mMaterialType);
        } else {
            this.mStrokeType = strokeEffectType;
        }
        if (context == null) {
            context = getContext();
        }
        if (this.mNoBlurShadowEdge == null || context == null) {
            return;
        }
        COUIMaterialStrokeEffect.Companion.StrokeEffectParams strokeParamsByType = COUIMaterialEffect.getStrokeParamsByType(context, this.mStrokeType);
        float f3 = this.mNoBlurStrokeProgress;
        this.mNoBlurShadowEdge.setLineAlpha(strokeParamsByType.getEdgeAlpha() * f3);
        this.mNoBlurShadowEdge.setLineWidth(strokeParamsByType.getEdgeWidth(context) * f3);
        this.mNoBlurShadowEdge.setLineAngle(strokeParamsByType.getEdgeAngle());
        this.mNoBlurShadowEdge.setFadeAlpha(strokeParamsByType.getShadowFadeIn() * f3, strokeParamsByType.getShadowFadeOut() * f3);
        this.mNoBlurShadowEdge.setFadeWidthScale(strokeParamsByType.getShadowFadeScale() * f3);
        this.mNoBlurShadowEdge.setDrawableEnabled(true);
    }

    public void setTouchAnimationEnable(boolean z2) {
        COUIViewTouchAnimator cOUIViewTouchAnimator = this.mTouchAnimator;
        if (cOUIViewTouchAnimator != null) {
            cOUIViewTouchAnimator.setDeformationEnable(z2).setRubberEnable(z2).setPressScaleEnable(z2);
        }
    }

    public void setTouchAnimationReset(boolean z2) {
        COUIViewTouchAnimator cOUIViewTouchAnimator = this.mTouchAnimator;
        if (cOUIViewTouchAnimator != null) {
            cOUIViewTouchAnimator.reset(z2);
        }
    }

    public SearchBarIconButton(Context context) {
        super(context);
        this.mSpotLight = new COUISpotLightEffectDrawable();
        ColorDrawable colorDrawable = new ColorDrawable(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorBar));
        this.mBackgroundColor = colorDrawable;
        this.mPath = new Path();
        this.mBounds = new RectF();
        this.mCornerParams = null;
        this.mIsDrawableMaterialEffect = false;
        this.mNoBlurSolidColor = 0;
        this.mBlurEffective = false;
        this.mStrokeEnabled = false;
        this.mNoBlurStrokeProgress = 1.0f;
        this.mSpotlightEnabled = false;
        this.mMaterialType = 0;
        this.mStrokeType = null;
        this.mSpotLightType = null;
        setSpotLightEffectType(null, context);
        setOutlineProvider(new AnonymousClass1());
        setClipToOutline(!this.mBlurEffective);
        UIUtil.setViewShadowClippingEnabled(this, true);
        COUIViewTouchAnimator cOUIViewTouchAnimator = new COUIViewTouchAnimator(getContext(), this);
        this.mTouchAnimator = cOUIViewTouchAnimator;
        cOUIViewTouchAnimator.setDeformationEnable(true).setRubberEnable(true).setPressScaleEnable(true);
        cOUIViewTouchAnimator.onSizeChange(getWidth(), getHeight());
        cOUIViewTouchAnimator.config(new TouchMotionParamConfig());
        int toolbarBackgroundColor = ToolbarMaterialEffectResolver.getToolbarBackgroundColor(context, this.mMaterialType);
        this.mDefaultBackgroundColor = toolbarBackgroundColor;
        colorDrawable.setColor(toolbarBackgroundColor);
    }
}