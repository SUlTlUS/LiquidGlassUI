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
import androidx.appcompat.widget.AppCompatTextView;
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
import com.coui.appcompat.uiutil.ShadowUtils;
import com.coui.appcompat.uiutil.UIUtil;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes.dex */
public class SearchBarFunctionButton extends AppCompatTextView {
    private static final float CORNER_RADIUS_PARAM = 60.0f;
    private static final int SHADOW_EDGE_CORNER_TYPE_G2 = 2;
    private static final float SHADOW_EDGE_CORNER_WEIGHT_DRAWABLE = 1.0f;
    private static final float SMOOTH_CORNER_WEIGHT = 1.0f;
    private final ColorDrawable mBackgroundColor;
    private final COUISearchBar.SmoothRoundCornerHelper mBackgroundSmoothRoundCornerHelper;
    private boolean mBlurEffective;
    private OplusMaterialCornerParams mCornerParams;
    private int mDefaultBackgroundColor;
    private final int mInstantPaddingH;
    private final int mInstantSearchButtonHeight;
    private boolean mIsDrawableMaterialEffect;
    private COUIMaskEffectDrawable mMaskEffectDrawable;
    private COUIMaskRippleDrawable mMaskRippleDrawable;
    private int mMaterialType;
    private COUIShadowEdgeDrawable mNoBlurShadowEdge;
    private int mNoBlurSolidColor;
    private float mNoBlurStrokeProgress;
    private final int mNonInstantPaddingH;
    private final int mNonInstantPaddingV;
    private int mSearchViewType;
    private final Path mSmoothRoundPath;
    private final RectF mSmoothRoundRectF;
    private final COUISpotLightEffectDrawable mSpotLight;
    private COUISpotLightEffect.SpotLightType mSpotLightType;
    private boolean mSpotlightEnabled;
    private boolean mStrokeEffectForceDark;
    private boolean mStrokeEnabled;
    private COUIMaterialStrokeEffect.StrokeEffectType mStrokeType;
    private COUIViewTouchAnimator mTouchAnimator;

    /* JADX INFO: renamed from: com.coui.appcompat.searchview.SearchBarFunctionButton$1, reason: invalid class name */
    public class AnonymousClass1 extends ViewOutlineProvider {
        public AnonymousClass1() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (SearchBarFunctionButton.access$000(SearchBarFunctionButton.this)) {
                outline.setPath(SearchBarFunctionButton.access$100(SearchBarFunctionButton.this));
            } else {
                outline.setRoundRect((int) SearchBarFunctionButton.access$200(SearchBarFunctionButton.this).left, (int) SearchBarFunctionButton.access$200(SearchBarFunctionButton.this).top, (int) SearchBarFunctionButton.access$200(SearchBarFunctionButton.this).right, (int) SearchBarFunctionButton.access$200(SearchBarFunctionButton.this).bottom, SearchBarFunctionButton.access$200(SearchBarFunctionButton.this).height() / 2.0f);
            }
        }
    }

    public SearchBarFunctionButton(Context context) {
        super(context);
        Path path = new Path();
        this.mSmoothRoundPath = path;
        this.mSmoothRoundRectF = new RectF();
        this.mSpotLight = new COUISpotLightEffectDrawable();
        this.mBackgroundColor = new ColorDrawable(COUIContextUtil.getAttrColor(getContext(), R.attr.couiColorBar));
        this.mCornerParams = null;
        this.mSearchViewType = 0;
        this.mIsDrawableMaterialEffect = false;
        this.mNoBlurSolidColor = 0;
        this.mBlurEffective = false;
        this.mStrokeEnabled = false;
        this.mNoBlurStrokeProgress = 1.0f;
        this.mSpotlightEnabled = false;
        this.mMaterialType = 0;
        this.mStrokeType = null;
        this.mSpotLightType = null;
        this.mDefaultBackgroundColor = ToolbarMaterialEffectResolver.getToolbarBackgroundColor(getContext(), this.mMaterialType);
        setSpotLightEffectType(this.mSpotLightType, context);
        this.mBackgroundSmoothRoundCornerHelper = new COUISearchBar.SmoothRoundCornerHelper(path);
        setGravity(17);
        this.mInstantSearchButtonHeight = context.getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_search_bar_normal_background_height);
        this.mInstantPaddingH = getResources().getDimensionPixelOffset(com.support.toolbar.R.dimen.coui_search_bar_functional_button_text_padding_horizontal);
        this.mNonInstantPaddingV = getResources().getDimensionPixelOffset(R.dimen.text_ripple_bg_padding_vertical);
        this.mNonInstantPaddingH = getResources().getDimensionPixelOffset(R.dimen.text_ripple_bg_padding_horizontal);
        applyInstantSearchButtonInsets();
        setOutlineProvider(new AnonymousClass1());
        setClipToOutline(!this.mBlurEffective);
        UIUtil.setViewShadowClippingEnabled(this, true);
        this.mTouchAnimator = new COUIViewTouchAnimator(getContext(), this);
        setTouchAnimationEnable(true);
        this.mTouchAnimator.onSizeChange(getWidth(), getHeight());
        this.mTouchAnimator.config(new TouchMotionParamConfig());
    }

    public static /* synthetic */ boolean access$000(SearchBarFunctionButton searchBarFunctionButton) {
        return searchBarFunctionButton.isSmoothOn();
    }

    public static /* synthetic */ Path access$100(SearchBarFunctionButton searchBarFunctionButton) {
        return searchBarFunctionButton.mSmoothRoundPath;
    }

    public static /* synthetic */ RectF access$200(SearchBarFunctionButton searchBarFunctionButton) {
        return searchBarFunctionButton.mSmoothRoundRectF;
    }

    private void applyFrameworkInstantSearchSurface() {
        this.mBackgroundColor.setColor(this.mDefaultBackgroundColor);
        if (this.mBlurEffective) {
            COUIMaterialEffect.setViewBlurEffect(this, ToolbarMaterialEffectResolver.getBlurEffectType(this.mMaterialType));
        } else {
            COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
        }
        if (this.mStrokeEnabled) {
            COUIMaterialEffect.setViewStrokeEffect(this, ToolbarMaterialEffectResolver.getCapsuleStrokeEffectType(this.mMaterialType));
        } else {
            COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
        }
        setBackgroundColorTransparent(this.mBlurEffective);
        setClipToOutline(!this.mBlurEffective);
        setCornerRadius(getMeasuredHeight() / 2.0f);
    }

    private void applyInstantSearchButtonInsets() {
        int i3 = this.mInstantPaddingH;
        setPadding(i3, 0, i3, 0);
    }

    private void applyNoBlurInstantSearchSurface() {
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

    private void applyNonInstantSearchButtonInsets() {
        int i3 = this.mNonInstantPaddingH;
        int i4 = this.mNonInstantPaddingV;
        setPadding(i3, i4, i3, i4);
    }

    private void drawNoBlurShadowEdge(Canvas canvas) {
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable == null || !cOUIShadowEdgeDrawable.getMDrawableEnabled()) {
            return;
        }
        int iRound = Math.round(this.mSmoothRoundRectF.width());
        int iRound2 = Math.round(this.mSmoothRoundRectF.height());
        if (iRound <= 0 || iRound2 <= 0 || this.mSmoothRoundPath.isEmpty()) {
            return;
        }
        this.mNoBlurShadowEdge.setBounds(Math.round(this.mSmoothRoundRectF.left), Math.round(this.mSmoothRoundRectF.top), Math.round(this.mSmoothRoundRectF.right), Math.round(this.mSmoothRoundRectF.bottom));
        this.mNoBlurShadowEdge.setCornerRadius(iRound2 / 2.0f);
        this.mNoBlurShadowEdge.draw(canvas);
    }

    private void ensureNoBlurShadowEdge() {
        if (this.mNoBlurShadowEdge != null) {
            return;
        }
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = new COUIShadowEdgeDrawable();
        this.mNoBlurShadowEdge = cOUIShadowEdgeDrawable;
        cOUIShadowEdgeDrawable.setCallback(this);
        this.mNoBlurShadowEdge.setEdgeEnable(true).setShadowEnable(true).setEdgeStyle(IShadowEdgeEffect.EdgeStyle.EDGE_STYLE_0).setShadowStyle(IShadowEdgeEffect.ShadowStyle.SHADOW_STYLE_0).setCornerType(2).setWeight(1.0f).setIsDarkMode(resolveShadowEdgeIsDarkMode());
        this.mNoBlurShadowEdge.setPaintBlendMode(BlendMode.SRC_OVER);
    }

    private boolean isNoBlurMaterialActive() {
        return this.mSearchViewType == 0 && this.mIsDrawableMaterialEffect;
    }

    private boolean isSmoothOn() {
        return this.mBackgroundSmoothRoundCornerHelper.getSmoothStyleType() == 1;
    }

    private void refreshInstantSearchMaterialSurface() {
        if (this.mSearchViewType != 0) {
            return;
        }
        if (isNoBlurMaterialActive()) {
            applyNoBlurInstantSearchSurface();
        } else {
            applyFrameworkInstantSearchSurface();
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
                this.mCornerParams = new OplusMaterialCornerParams(CORNER_RADIUS_PARAM, 1.0f);
            }
            this.mCornerParams.setRadius(f3);
            if (isNoBlurMaterialActive()) {
                return;
            }
            COUIMaterialEffect.setCornerParams(this, this.mCornerParams);
        }
    }

    private void updateSmoothRoundPath() {
        this.mSmoothRoundPath.reset();
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        float f3 = height;
        this.mSmoothRoundRectF.set(0.0f, 0.0f, width, f3);
        float f4 = f3 / 2.0f;
        if (isSmoothOn()) {
            this.mBackgroundSmoothRoundCornerHelper.getPathAdapter().addSmoothRoundRect(this.mSmoothRoundRectF, f4, f4, 1.0f, Path.Direction.CCW);
        } else {
            this.mSmoothRoundPath.addRoundRect(this.mSmoothRoundRectF, f4, f4, Path.Direction.CW);
        }
        invalidateOutline();
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.mTouchAnimator != null) {
            if (motionEvent.getAction() == 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            this.mTouchAnimator.handleTouchEvent(motionEvent);
        }
        this.mSpotLight.onTouchEvent(motionEvent);
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mSpotLight.setCallback(this);
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mSpotLight.setCallback(null);
        this.mSpotLight.cancel();
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateSmoothRoundPath();
        if (this.mSpotLight.getMEnabled()) {
            canvas.save();
            canvas.clipPath(this.mSmoothRoundPath);
            this.mSpotLight.draw(canvas);
            canvas.restore();
        }
        if (isNoBlurMaterialActive()) {
            drawNoBlurShadowEdge(canvas);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView, android.view.View
    public void onMeasure(int i3, int i4) {
        if (this.mSearchViewType == 0) {
            i4 = View.MeasureSpec.makeMeasureSpec(this.mInstantSearchButtonHeight, 1073741824);
        }
        super.onMeasure(i3, i4);
    }

    @Override // android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        COUIViewTouchAnimator cOUIViewTouchAnimator = this.mTouchAnimator;
        if (cOUIViewTouchAnimator != null) {
            cOUIViewTouchAnimator.onSizeChange(i3, i4);
        }
        this.mSpotLight.setBounds(0, 0, i3, i4);
        if (this.mSearchViewType == 0) {
            setCornerRadius(getMeasuredHeight() / 2.0f);
        }
    }

    public final void refreshThemeColors() {
        this.mDefaultBackgroundColor = ToolbarMaterialEffectResolver.getToolbarBackgroundColor(getContext(), this.mMaterialType);
        COUIShadowEdgeDrawable cOUIShadowEdgeDrawable = this.mNoBlurShadowEdge;
        if (cOUIShadowEdgeDrawable != null) {
            cOUIShadowEdgeDrawable.setIsDarkMode(resolveShadowEdgeIsDarkMode());
        }
        refreshInstantSearchMaterialSurface();
    }

    public final void setBackgroundColorTransparent(boolean z2) {
        if (this.mMaskEffectDrawable == null) {
            COUIMaskEffectDrawable cOUIMaskEffectDrawable = new COUIMaskEffectDrawable(getContext(), 1);
            this.mMaskEffectDrawable = cOUIMaskEffectDrawable;
            cOUIMaskEffectDrawable.setMaskPath(this.mSmoothRoundPath);
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
        if (this.mSearchViewType == 0) {
            refreshInstantSearchMaterialSurface();
        }
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
        if (isNoBlurMaterialActive() && this.mSearchViewType == 0) {
            this.mBackgroundColor.setColor(this.mNoBlurSolidColor);
            refreshInstantSearchMaterialSurface();
        }
    }

    @Override // android.widget.TextView, android.view.View
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
        if (this.mSearchViewType == 0) {
            refreshInstantSearchMaterialSurface();
        }
    }

    public final void setNoBlurStrokeProgress(float f3) {
        this.mNoBlurStrokeProgress = Math.max(0.0f, Math.min(f3, 1.0f));
        if (isNoBlurMaterialActive() && this.mStrokeEnabled) {
            applyNoBlurShadowInterpolated();
            invalidate();
        }
    }

    public final void setSearchAnimateType(int i3) {
        this.mSearchViewType = i3;
        if (i3 == 0) {
            applyInstantSearchButtonInsets();
            refreshInstantSearchMaterialSurface();
            setSpotLightEnabled(this.mSpotlightEnabled);
            return;
        }
        applyNonInstantSearchButtonInsets();
        COUIMaskRippleDrawable cOUIMaskRippleDrawable = new COUIMaskRippleDrawable(getContext());
        cOUIMaskRippleDrawable.setCustomRippleMask();
        setBackground(cOUIMaskRippleDrawable);
        COUIMaterialEffect.clearBlurEffect(this);
        COUIMaterialEffect.setViewBlurEffect(this, COUIMaterialBlurEffect.BlurEffectType.TYPE_NO_EFFECT);
        COUIMaterialEffect.setViewStrokeEffect(this, COUIMaterialStrokeEffect.StrokeEffectType.TYPE_NO_EFFECT);
        releaseNoBlurShadowEdge();
        setTouchAnimationEnable(false);
        ShadowUtils.clearShadow(this);
        setSpotLightEnabled(this.mSpotlightEnabled);
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

    public final void setSpotLightEnabled(boolean z2) {
        this.mSpotlightEnabled = z2;
        this.mSpotLight.setEnabled(z2 && isEnabled() && this.mSearchViewType == 0);
    }

    public final void setStrokeEffectEnable(boolean z2) {
        this.mStrokeEnabled = z2;
        if (this.mSearchViewType == 0) {
            refreshInstantSearchMaterialSurface();
        }
    }

    public final void setStrokeEffectType(COUIMaterialStrokeEffect.StrokeEffectType strokeEffectType, Context context) {
        if (strokeEffectType == null) {
            this.mStrokeType = ToolbarMaterialEffectResolver.getCapsuleStrokeEffectTypeInWindow(this.mMaterialType);
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

    public final void setTouchAnimationEnable(boolean z2) {
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
}