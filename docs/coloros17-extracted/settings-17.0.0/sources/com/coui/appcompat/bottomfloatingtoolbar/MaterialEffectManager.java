package com.coui.appcompat.bottomfloatingtoolbar;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.view.View;
import com.coui.appcompat.COUIMaterialEffect;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusPathAdapter;
import com.support.bottomnavigation.R;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes7.dex */
class MaterialEffectManager {
    private static final int ALPHA_VALUE = 255;
    public static final int SDK_SUB_VERSION = 34;
    MaterialEffectConfig.BlurConfig.Builder mBlurBuilder;
    private MaterialEffectConfig.BlurConfig mBlurConfig;
    private final WeakReference<Context> mContextRef;
    private GradientStroke mGradientStroke;
    MaterialEffectConfig.InnerShadowConfig.Builder mInnerBuilder;
    private MaterialEffectConfig.InnerShadowConfig mInnerShadowConfig;
    private final RectF mInnerShadowLayerRect;
    private final Path mInnerShadowPath;
    private COUIBottomFloatingToolbar.SmoothRoundCornerHelper mInnerShadowSmoothRoundPathHelper;
    private final Paint mShadowLayerPaint0;
    private final Paint mShadowLayerPaint1;
    MaterialEffectConfig.StrokeConfig.Builder mStrokeBuilder;
    private MaterialEffectConfig.StrokeConfig mStrokeConfig;
    private final Paint mStrokePaint;

    public MaterialEffectManager(Context context) {
        this(context, new MaterialEffectConfig.StrokeConfig.Builder(), new MaterialEffectConfig.InnerShadowConfig.Builder(), new MaterialEffectConfig.BlurConfig.Builder());
    }

    public static boolean checkSdkVersion() {
        return COUIVersionUtil.checkOPlusViewBackgroundRenderEffectSupport();
    }

    private RenderEffect createBackgroundRenderEffect() {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        float f3 = this.mBlurConfig.mBlurRadius;
        RenderEffect renderEffectCreateBlurEffect = RenderEffect.createBlurEffect(f3, f3, Shader.TileMode.MIRROR);
        if (COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context)) {
            return RenderEffect.createColorFilterEffect(new BlendModeColorFilter(COUIContextUtil.getColor(getContext(), R.color.coui_floating_toolbar_menu_background_color1), BlendMode.LUMINOSITY), RenderEffect.createColorFilterEffect(new BlendModeColorFilter(COUIContextUtil.getColor(getContext(), R.color.coui_floating_toolbar_menu_background_color0), BlendMode.OVERLAY), renderEffectCreateBlurEffect));
        }
        return RenderEffect.createColorFilterEffect(new BlendModeColorFilter(COUIContextUtil.getColor(getContext(), R.color.coui_floating_toolbar_menu_background_color1), BlendMode.COLOR_DODGE), RenderEffect.createColorFilterEffect(new BlendModeColorFilter(COUIContextUtil.getColor(getContext(), R.color.coui_floating_toolbar_menu_background_color0), BlendMode.LUMINOSITY), renderEffectCreateBlurEffect));
    }

    private Context getContext() {
        return this.mContextRef.get();
    }

    private void initInnerShadowLayerPaint() {
        this.mInnerShadowSmoothRoundPathHelper = new COUIBottomFloatingToolbar.SmoothRoundCornerHelper(this.mInnerShadowPath);
        updateInnerShadowPaint();
    }

    private void initStrokeParams() {
        this.mGradientStroke = new GradientStroke();
        if (this.mInnerShadowSmoothRoundPathHelper.getSmoothStyleType() == 1) {
            GradientStroke gradientStroke = this.mGradientStroke;
            GradientStroke.CornerType cornerType = GradientStroke.CornerType.SMOOTH;
            MaterialEffectConfig.StrokeConfig strokeConfig = this.mStrokeConfig;
            gradientStroke.setCorner(cornerType, strokeConfig.mCorner, strokeConfig.mWeight);
        } else {
            GradientStroke gradientStroke2 = this.mGradientStroke;
            GradientStroke.CornerType cornerType2 = GradientStroke.CornerType.FULL;
            MaterialEffectConfig.StrokeConfig strokeConfig2 = this.mStrokeConfig;
            gradientStroke2.setCorner(cornerType2, strokeConfig2.mCorner, strokeConfig2.mWeight);
        }
        this.mGradientStroke.setColor(this.mStrokeConfig.mColor);
        this.mGradientStroke.setRatio(this.mStrokeConfig.mRatio);
        GradientStroke gradientStroke3 = this.mGradientStroke;
        MaterialEffectConfig.StrokeConfig strokeConfig3 = this.mStrokeConfig;
        gradientStroke3.setWidth(Math.max(strokeConfig3.mNearLineWidth, strokeConfig3.mFarLineWidth) * 2.0f);
        this.mGradientStroke.setNearLineAlpha(this.mStrokeConfig.mNearLineAlpha);
        this.mGradientStroke.setNearLineWidth(this.mStrokeConfig.mNearLineWidth);
        this.mGradientStroke.setNearLineFadeToSides1(this.mStrokeConfig.mNearLineFadeToSides1);
        this.mGradientStroke.setNearLineFadeToSides2(this.mStrokeConfig.mNearLineFadeToSides2);
        this.mGradientStroke.setNearLineFadeTowardCenter1(this.mStrokeConfig.mNearLineFadeTowardCenter1);
        this.mGradientStroke.setNearLineFadeTowardCenter2(this.mStrokeConfig.mNearLineFadeTowardCenter2);
        this.mGradientStroke.setFarLineAlpha(this.mStrokeConfig.mFarLineAlpha);
        this.mGradientStroke.setFarLineWidth(this.mStrokeConfig.mFarLineWidth);
        this.mGradientStroke.setFarLineFadeToSides1(this.mStrokeConfig.mFarLineFadeToSides1);
        this.mGradientStroke.setFarLineFadeToSides2(this.mStrokeConfig.mFarLineFadeToSides2);
        this.mGradientStroke.setFarLineFadeTowardCenter1(this.mStrokeConfig.mFarLineFadeTowardCenter1);
        this.mGradientStroke.setFarLineFadeTowardCenter2(this.mStrokeConfig.mFarLineFadeTowardCenter2);
        this.mStrokePaint.setShader(this.mGradientStroke.getShader());
        this.mStrokePaint.setAntiAlias(true);
        this.mStrokePaint.setStyle(Paint.Style.STROKE);
        Paint paint = this.mStrokePaint;
        MaterialEffectConfig.StrokeConfig strokeConfig4 = this.mStrokeConfig;
        paint.setStrokeWidth(Math.max(strokeConfig4.mNearLineWidth, strokeConfig4.mFarLineWidth) * 2.0f);
        this.mStrokePaint.setAlpha(255);
        this.mStrokePaint.setBlendMode(BlendMode.OVERLAY);
    }

    private void updateInnerShadowPaint() {
        this.mInnerShadowConfig = this.mInnerBuilder.setShadow0Color(COUIContextUtil.getColor(getContext(), R.color.coui_floating_toolbar_menu_inner_shadow_color0)).setShadow1Color(COUIContextUtil.getColor(getContext(), R.color.coui_floating_toolbar_menu_inner_shadow_color1)).build();
        this.mShadowLayerPaint0.setColor(0);
        Paint paint = this.mShadowLayerPaint0;
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        this.mShadowLayerPaint0.setStrokeWidth(this.mInnerShadowConfig.mShadow0Width);
        Paint paint2 = this.mShadowLayerPaint0;
        MaterialEffectConfig.InnerShadowConfig innerShadowConfig = this.mInnerShadowConfig;
        paint2.setShadowLayer(innerShadowConfig.mShadow0Radius, innerShadowConfig.mShadow0Dx, innerShadowConfig.mShadow0Dy, Color.argb((int) (innerShadowConfig.mShadow0Alpha * 255.0f), Color.red(innerShadowConfig.mShadow0Color), Color.green(this.mInnerShadowConfig.mShadow0Color), Color.blue(this.mInnerShadowConfig.mShadow0Color)));
        this.mShadowLayerPaint1.setColor(0);
        this.mShadowLayerPaint1.setStyle(style);
        this.mShadowLayerPaint1.setStrokeWidth(this.mInnerShadowConfig.mShadow1Width);
        Paint paint3 = this.mShadowLayerPaint1;
        MaterialEffectConfig.InnerShadowConfig innerShadowConfig2 = this.mInnerShadowConfig;
        paint3.setShadowLayer(innerShadowConfig2.mShadow1Radius, innerShadowConfig2.mShadow1Dx, innerShadowConfig2.mShadow1Dy, Color.argb((int) (innerShadowConfig2.mShadow1Alpha * 255.0f), Color.red(innerShadowConfig2.mShadow1Color), Color.green(this.mInnerShadowConfig.mShadow1Color), Color.blue(this.mInnerShadowConfig.mShadow1Color)));
        if (COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(getContext())) {
            Paint paint4 = this.mShadowLayerPaint0;
            BlendMode blendMode = BlendMode.COLOR_DODGE;
            paint4.setBlendMode(blendMode);
            this.mShadowLayerPaint1.setBlendMode(blendMode);
            return;
        }
        Paint paint5 = this.mShadowLayerPaint0;
        BlendMode blendMode2 = BlendMode.OVERLAY;
        paint5.setBlendMode(blendMode2);
        this.mShadowLayerPaint1.setBlendMode(blendMode2);
    }

    private void updateStrokeAndShaderPath(RectF rectF, float f3, float f4) {
        GradientStroke gradientStroke = this.mGradientStroke;
        if (gradientStroke != null) {
            gradientStroke.setResolution((int) ((rectF.right - rectF.left) + (f3 * 2.0f)), (int) ((rectF.bottom - rectF.top) + (f4 * 2.0f)));
            this.mGradientStroke.setPadding(f3, f4);
            this.mGradientStroke.setRatio((float) (((((double) rectF.width()) * 1.5d) + ((double) rectF.height())) / ((double) ((rectF.width() + rectF.height()) * 2.0f))));
        }
        RectF rectF2 = this.mInnerShadowLayerRect;
        float f5 = rectF.left;
        MaterialEffectConfig.InnerShadowConfig innerShadowConfig = this.mInnerShadowConfig;
        float f6 = innerShadowConfig.mShadowLayerStretchDx;
        rectF2.left = f5 - f6;
        float f7 = rectF.top;
        float f8 = innerShadowConfig.mShadowLayerStretchDy;
        rectF2.top = f7 - f8;
        rectF2.right = rectF.right + f6;
        rectF2.bottom = rectF.bottom + f8;
        this.mInnerShadowPath.reset();
        if (this.mInnerShadowSmoothRoundPathHelper.getSmoothStyleType() == 1) {
            OplusPathAdapter pathAdapter = this.mInnerShadowSmoothRoundPathHelper.getPathAdapter();
            RectF rectF3 = this.mInnerShadowLayerRect;
            pathAdapter.addSmoothRoundRect(rectF3, rectF3.height() / 2.0f, this.mInnerShadowLayerRect.height() / 2.0f, Path.Direction.CCW);
        } else {
            Path path = this.mInnerShadowPath;
            RectF rectF4 = this.mInnerShadowLayerRect;
            path.addRoundRect(rectF4, rectF4.height() / 2.0f, this.mInnerShadowLayerRect.height() / 2.0f, Path.Direction.CCW);
        }
    }

    public void applyMaterialBackground(View view) {
        if (view == null) {
            return;
        }
        view.setBackgroundColor(0);
        if (!COUIMaterialEffect.canApplyBackgroundRenderEffect()) {
            view.setBackgroundColor(COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorBar));
        } else {
            COUIMaterialEffect.setBackgroundRenderEffect(createBackgroundRenderEffect(), view);
            view.requestLayout();
        }
    }

    public void drawMaterialEffects(Canvas canvas, RectF rectF, Path path, float f3, float f4) {
        updateStrokeAndShaderPath(rectF, f3, f4);
        canvas.drawPath(path, this.mStrokePaint);
        canvas.drawPath(this.mInnerShadowPath, this.mShadowLayerPaint0);
        canvas.drawPath(this.mInnerShadowPath, this.mShadowLayerPaint1);
    }

    public MaterialEffectManager(Context context, MaterialEffectConfig.StrokeConfig.Builder builder, MaterialEffectConfig.InnerShadowConfig.Builder builder2, MaterialEffectConfig.BlurConfig.Builder builder3) {
        this.mStrokePaint = new Paint();
        this.mInnerShadowPath = new Path();
        this.mShadowLayerPaint0 = new Paint();
        this.mShadowLayerPaint1 = new Paint();
        this.mInnerShadowLayerRect = new RectF();
        this.mContextRef = new WeakReference<>(context);
        this.mStrokeBuilder = builder;
        this.mInnerBuilder = builder2;
        this.mBlurBuilder = builder3;
        this.mStrokeConfig = builder.build();
        this.mInnerShadowConfig = builder2.build();
        this.mBlurConfig = builder3.build();
        initInnerShadowLayerPaint();
        initStrokeParams();
    }
}