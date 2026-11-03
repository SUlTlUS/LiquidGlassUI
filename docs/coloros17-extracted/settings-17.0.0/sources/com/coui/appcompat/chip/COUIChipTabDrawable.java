package com.coui.appcompat.chip;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import androidx.dynamicanimation.animation.FloatValueHolder;
import com.coui.appcompat.COUIMaterialStateManager;
import com.coui.appcompat.OnMaterialStateChangeListener;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.UIUtil;
import com.support.chip.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIChipTabDrawable extends COUIChipDrawable implements OnMaterialStateChangeListener {
    private static final int ALPHA_100_PERCENT = 255;
    private static final float ALPHA_ANIMATION_FULL = 1.0f;
    private static final float CHECKED_STROKE_WIDTH_DP = 0.6f;
    private static final boolean DEBUG;
    private static final ArgbEvaluator EVALUATOR = new ArgbEvaluator();
    private static final String TAG = "COUIChipTabDrawable";
    private OnCheckedLayerAlphaChangeListener mAlphaChangeListener;
    private int mCachedCheckedBackground;
    private int mCachedCheckedStroke;
    private int mCachedColorContainer4;
    private Paint mCheckedBackgroundPaint;
    private float mCheckedLayerAlpha;
    private CheckedLayerAlphaAnimation mCheckedLayerAlphaAnimation;
    private Paint mCheckedStrokePaint;
    private int mCheckedSurface;
    private float mChipEndPaddingDefault;
    private float mChipEndPaddingWithRedDot;
    private final Context mContextForTheme;
    private boolean mIsBlurEffectEnabled;
    private boolean mIsSpotLightEffectEnabled;
    private boolean mIsStrokeEffectEnabled;
    public boolean mLastCheckedState;
    private final RectF mStrokeBounds;
    private final RectF mTabBackgroundBounds;
    private final Path mTabBackgroundPath;
    private Paint mUncheckedBackgroundPaint;
    private float mUncheckedLayerAlpha;
    private UncheckedLayerAlphaAnimation mUncheckedLayerAlphaAnimation;
    private int mUncheckedSurface;
    private int mUseMaterialEffectAttr;

    public final class CheckedLayerAlphaAnimation extends LayerAlphaAnimation {
        private static final float BOUNCE = 0.0f;
        private static final float RESPONSE = 0.2f;

        public CheckedLayerAlphaAnimation() {
            super(0.2f, 0.0f, 0.0f);
        }

        @Override // com.coui.appcompat.chip.COUIChipTabDrawable.LayerAlphaAnimation
        public void updateValue(float f3) {
            super.updateValue(f3);
            COUIChipTabDrawable.access$002(COUIChipTabDrawable.this, getValue());
            if (COUIChipTabDrawable.access$100(COUIChipTabDrawable.this) != null) {
                COUIChipTabDrawable.access$100(COUIChipTabDrawable.this).onAlphaChanged(COUIChipTabDrawable.access$000(COUIChipTabDrawable.this));
            }
        }
    }

    public class LayerAlphaAnimation {
        private static final float ALPHA_MIN_VISIBLE_CHANGE = 0.00390625f;
        private final COUISpringAnimation mAnimation;
        private float mCurrentValue;
        COUISpringForce mSpringForce;

        public LayerAlphaAnimation(float f3, float f4, float f5) {
            COUISpringForce cOUISpringForce = new COUISpringForce();
            this.mSpringForce = cOUISpringForce;
            cOUISpringForce.setResponse(f3);
            this.mSpringForce.setBounce(f4);
            this.mCurrentValue = f5;
            COUISpringAnimation cOUISpringAnimation = new COUISpringAnimation(new FloatValueHolder(this.mCurrentValue));
            this.mAnimation = cOUISpringAnimation;
            cOUISpringAnimation.setSpring(this.mSpringForce);
            cOUISpringAnimation.setMinimumVisibleChange(0.00390625f);
            cOUISpringAnimation.addUpdateListener(new l(this));
        }

        public static /* synthetic */ void a(LayerAlphaAnimation layerAlphaAnimation, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
            layerAlphaAnimation.updateValue(f3);
        }

        public void animateToFinalPosition(float f3) {
            Drawable.Callback callback = COUIChipTabDrawable.this.getCallback();
            if (callback instanceof View) {
                View view = (View) callback;
                if (view.isLaidOut() && view.getVisibility() == 0) {
                    this.mAnimation.animateToFinalPosition(f3);
                    return;
                }
            }
            setValue(f3);
        }

        public void cancel() {
            if (this.mAnimation.isRunning()) {
                this.mAnimation.cancel();
            }
        }

        public float getValue() {
            return this.mCurrentValue;
        }

        public void setValue(float f3) {
            updateValue(f3);
            if (this.mAnimation.isRunning()) {
                this.mAnimation.cancel();
            }
        }

        public void updateValue(float f3) {
            this.mCurrentValue = f3;
            COUIChipTabDrawable.this.invalidateSelf();
        }
    }

    public interface OnCheckedLayerAlphaChangeListener {
        void onAlphaChanged(float f3);
    }

    public final class UncheckedLayerAlphaAnimation extends LayerAlphaAnimation {
        private static final float BOUNCE = 0.0f;
        private static final float RESPONSE = 0.2f;

        public UncheckedLayerAlphaAnimation() {
            super(0.2f, 0.0f, 1.0f);
        }

        @Override // com.coui.appcompat.chip.COUIChipTabDrawable.LayerAlphaAnimation
        public void updateValue(float f3) {
            super.updateValue(f3);
            COUIChipTabDrawable.access$202(COUIChipTabDrawable.this, getValue());
        }
    }

    static {
        DEBUG = COUILog.LOG_DEBUG || COUILog.isLoggable(TAG, 3);
    }

    public COUIChipTabDrawable(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mTabBackgroundBounds = new RectF();
        this.mTabBackgroundPath = new Path();
        this.mStrokeBounds = new RectF();
        this.mLastCheckedState = false;
        this.mUseMaterialEffectAttr = COUIChipTab.MaterialEffectState.FOLLOW_SYSTEM.getValue();
        this.mIsBlurEffectEnabled = false;
        this.mIsStrokeEffectEnabled = false;
        this.mIsSpotLightEffectEnabled = false;
        this.mCheckedLayerAlpha = 0.0f;
        this.mUncheckedLayerAlpha = 1.0f;
        this.mCheckedLayerAlphaAnimation = null;
        this.mUncheckedLayerAlphaAnimation = null;
        this.mChipEndPaddingWithRedDot = -1.0f;
        this.mChipEndPaddingDefault = -1.0f;
        this.mContextForTheme = context;
        parseUseMaterialEffectAttr(context, attributeSet, i3, i4);
        configureForTab();
        initTabPaint();
        updateCachedThemeColors();
    }

    public static /* synthetic */ float access$000(COUIChipTabDrawable cOUIChipTabDrawable) {
        return cOUIChipTabDrawable.mCheckedLayerAlpha;
    }

    public static /* synthetic */ float access$002(COUIChipTabDrawable cOUIChipTabDrawable, float f3) {
        cOUIChipTabDrawable.mCheckedLayerAlpha = f3;
        return f3;
    }

    public static /* synthetic */ OnCheckedLayerAlphaChangeListener access$100(COUIChipTabDrawable cOUIChipTabDrawable) {
        return cOUIChipTabDrawable.mAlphaChangeListener;
    }

    public static /* synthetic */ float access$202(COUIChipTabDrawable cOUIChipTabDrawable, float f3) {
        cOUIChipTabDrawable.mUncheckedLayerAlpha = f3;
        return f3;
    }

    private void configureForTab() {
        setCloseIconVisible(false);
        this.mCheckedLayerAlpha = 0.0f;
        this.mUncheckedLayerAlpha = 1.0f;
    }

    public static COUIChipTabDrawable create(Context context) {
        return createFromAttributes(context, (AttributeSet) null, R.attr.couiChipStyle, R.style.Widget_COUI_Chip);
    }

    private Context createColorContext() {
        Configuration configuration = new Configuration(this.mContextForTheme.getResources().getConfiguration());
        configuration.uiMode = (COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(this.mContextForTheme) ? 32 : 16) | (configuration.uiMode & (-49));
        return this.mContextForTheme.createConfigurationContext(configuration);
    }

    public static COUIChipTabDrawable createFromAttributes(Context context, AttributeSet attributeSet, int i3, int i4) {
        return new COUIChipTabDrawable(context, attributeSet, i3, i4);
    }

    private void drawContentOnly(Canvas canvas, Rect rect) {
        drawChipIcon(canvas, rect);
        if (shouldDrawText()) {
            drawText(canvas, rect);
        }
        drawCount(canvas, rect);
        drawRedDot(canvas, rect);
        if (DEBUG) {
            drawDebug(canvas, rect);
        }
    }

    private void drawDualLayerBackground(Canvas canvas, Rect rect) {
        float chipCornerRadius = getChipCornerRadius();
        if (chipCornerRadius < 0.0f) {
            chipCornerRadius = Math.min(rect.width(), rect.height()) / 2.0f;
        }
        this.mTabBackgroundBounds.set(rect);
        int iSave = canvas.save();
        try {
            this.mTabBackgroundPath.reset();
            this.mTabBackgroundPath.addRoundRect(this.mTabBackgroundBounds, chipCornerRadius, chipCornerRadius, Path.Direction.CW);
            canvas.clipPath(this.mTabBackgroundPath);
            if (!isBlurEffectEnabled()) {
                this.mUncheckedBackgroundPaint.setColor(((Integer) EVALUATOR.evaluate(this.mUncheckedLayerAlpha, Integer.valueOf(this.mCheckedSurface), Integer.valueOf(this.mUncheckedSurface))).intValue());
                canvas.drawPath(this.mTabBackgroundPath, this.mUncheckedBackgroundPaint);
                float f3 = this.mUncheckedLayerAlpha;
                if (f3 > 0.0f) {
                    this.mUncheckedBackgroundPaint.setColor(applyAlphaToColor(this.mCachedColorContainer4, f3));
                    canvas.drawPath(this.mTabBackgroundPath, this.mUncheckedBackgroundPaint);
                }
            }
            if (this.mCheckedLayerAlpha > 0.0f) {
                drawLightOSCheckedBackground(canvas, chipCornerRadius);
            }
        } finally {
            canvas.restoreToCount(iSave);
        }
    }

    private void drawLightOSCheckedBackground(Canvas canvas, float f3) {
        if (DEBUG) {
            Log.d(TAG, "drawLightOSCheckedBackground: isBlurEffectEnabled = " + isBlurEffectEnabled() + ", isStrokeEffectEnabled = " + isStrokeEffectEnabled());
        }
        if (!isBlurEffectEnabled()) {
            this.mCheckedBackgroundPaint.setColor(applyAlphaToColor(this.mCachedCheckedBackground, this.mCheckedLayerAlpha));
            canvas.drawPath(this.mTabBackgroundPath, this.mCheckedBackgroundPaint);
        }
        if (isStrokeEffectEnabled()) {
            return;
        }
        int iApplyAlphaToColor = applyAlphaToColor(this.mCachedCheckedStroke, this.mCheckedLayerAlpha);
        float f4 = this.mContextForTheme.getResources().getDisplayMetrics().density * 0.6f;
        this.mCheckedStrokePaint.setColor(iApplyAlphaToColor);
        this.mCheckedStrokePaint.setStrokeWidth(f4);
        float f5 = f4 / 2.0f;
        this.mStrokeBounds.set(this.mTabBackgroundBounds);
        this.mStrokeBounds.inset(f5, f5);
        Path path = new Path();
        float fMax = Math.max(0.0f, f3 - f5);
        path.addRoundRect(this.mStrokeBounds, fMax, fMax, Path.Direction.CW);
        canvas.drawPath(path, this.mCheckedStrokePaint);
    }

    private void initTabPaint() {
        Paint paint = new Paint(1);
        this.mUncheckedBackgroundPaint = paint;
        Paint.Style style = Paint.Style.FILL;
        paint.setStyle(style);
        Paint paint2 = new Paint(1);
        this.mCheckedBackgroundPaint = paint2;
        paint2.setStyle(style);
        Paint paint3 = new Paint(1);
        this.mCheckedStrokePaint = paint3;
        paint3.setStyle(Paint.Style.STROKE);
        this.mCheckedStrokePaint.setAlpha(255);
    }

    private boolean isBlurEffectEnabled() {
        if (this.mUseMaterialEffectAttr == COUIChipTab.MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        if (this.mUseMaterialEffectAttr == COUIChipTab.MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return this.mIsBlurEffectEnabled;
    }

    private boolean isSpotLightEffectEnabled() {
        if (this.mUseMaterialEffectAttr == COUIChipTab.MaterialEffectState.FORCE_ENABLE.getValue()) {
            return COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
        }
        return this.mUseMaterialEffectAttr != COUIChipTab.MaterialEffectState.FORCE_DISABLE.getValue() && this.mIsSpotLightEffectEnabled && COUIMaterialStateManager.INSTANCE.isFeatureEnabled();
    }

    private boolean isStrokeEffectEnabled() {
        if (this.mUseMaterialEffectAttr == COUIChipTab.MaterialEffectState.FORCE_ENABLE.getValue()) {
            return true;
        }
        if (this.mUseMaterialEffectAttr == COUIChipTab.MaterialEffectState.FORCE_DISABLE.getValue()) {
            return false;
        }
        return this.mIsStrokeEffectEnabled;
    }

    private void parseUseMaterialEffectAttr(Context context, AttributeSet attributeSet, int i3, int i4) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.COUIChip, i3, i4);
        try {
            if (UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
                COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
                if (cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(context)) {
                    this.mUseMaterialEffectAttr = typedArrayObtainStyledAttributes.getInteger(R.styleable.COUIChip_useMaterialEffect, COUIChipTab.MaterialEffectState.FOLLOW_SYSTEM.getValue());
                }
            }
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    private void updateCachedThemeColors() {
        this.mCheckedSurface = COUIContextUtil.getAttrColor(this.mContextForTheme, com.support.appcompat.R.attr.couiColorBackgroundElevated);
        Context contextCreateColorContext = createColorContext();
        if (Build.VERSION.SDK_INT <= 36) {
            this.mUncheckedSurface = COUIContextUtil.getColor(contextCreateColorContext, R.color.coui_chip_tab_surface_unchecked_surface);
        } else {
            this.mUncheckedSurface = ColorUtils.setAlphaComponent(this.mCheckedSurface, 0);
        }
        this.mCachedColorContainer4 = COUIContextUtil.getAttrColor(this.mContextForTheme, com.support.appcompat.R.attr.couiColorContainer4);
        this.mCachedCheckedBackground = COUIContextUtil.getColor(this.mContextForTheme, R.color.coui_color_checked_background_gradient_start);
        this.mCachedCheckedStroke = COUIContextUtil.getColor(contextCreateColorContext, R.color.coui_color_checked_background_stroke);
    }

    private void updateIntrinsicWidthCloseIconInvisible() {
        float fMin = Math.min(Math.max(Math.round(this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + getTextWidth() + this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f) + (isShowRedDot() ? calculateRedDotWidth() : 0.0f) + this.mChipEndPadding), this.mMinWidth), this.mMaxWidth);
        this.mChipBackgroundWidthCloseIconInvisible = fMin;
        this.mChipBackgroundCurrentWidth = fMin;
    }

    public int applyAlphaToColor(int i3, float f3) {
        return Color.argb((int) (Color.alpha(i3) * f3), Color.red(i3), Color.green(i3), Color.blue(i3));
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable
    public void calculateCountBounds(Rect rect, RectF rectF) {
        CharSequence charSequence;
        rectF.setEmpty();
        if (!showsCount() || (charSequence = this.mText) == null || TextUtils.isEmpty(charSequence.toString()) || TextUtils.isEmpty(getCountText())) {
            return;
        }
        float fMeasureCountTextWidth = measureCountTextWidth(getCountText());
        float textWidth = getTextWidth() + this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + this.mCountMarginStart;
        if (isRTL()) {
            float f3 = rect.right - textWidth;
            rectF.right = f3;
            rectF.left = f3 - fMeasureCountTextWidth;
        } else {
            float f4 = rect.left + textWidth;
            rectF.left = f4;
            rectF.right = f4 + fMeasureCountTextWidth;
        }
        rectF.top = rect.top;
        rectF.bottom = rect.bottom;
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable
    public void calculateTextBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (this.mText != null) {
            float fMeasureText = this.mTextDrawableHelper.getTextPaint().measureText(getText().toString());
            float fCalculateChipIconWidth = this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding;
            float fCalculateCountWidth = this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f) + (isShowRedDot() ? calculateRedDotWidth() : 0.0f) + this.mChipEndPadding;
            if (!TextUtils.isEmpty(getText().toString()) && (rect.width() - fCalculateCountWidth) - fCalculateChipIconWidth > fMeasureText) {
                float fWidth = (((rect.width() - fCalculateCountWidth) - fCalculateChipIconWidth) - fMeasureText) / 2.0f;
                fCalculateChipIconWidth += fWidth;
                fCalculateCountWidth += fWidth;
            }
            if (isRTL()) {
                rectF.left = rect.left + fCalculateCountWidth;
                rectF.right = rect.right - fCalculateChipIconWidth;
            } else {
                rectF.left = rect.left + fCalculateChipIconWidth;
                rectF.right = rect.right - fCalculateCountWidth;
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty() || getAlpha() == 0) {
            return;
        }
        int iSaveLayer = canvas.saveLayer(bounds.left, bounds.top, bounds.right, bounds.bottom, null);
        try {
            drawDualLayerBackground(canvas, bounds);
            if (this.mChipBackgroundCurrentWidth == -1.0f) {
                updateIntrinsicWidthCloseIconInvisible();
            }
            if (!isSpotLightEffectEnabled()) {
                drawStateEffects(canvas, bounds);
            }
            drawContentOnly(canvas, bounds);
        } finally {
            canvas.restoreToCount(iSaveLayer);
        }
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable
    public void getDeterminateChipSize(int[] iArr) {
        iArr[0] = Math.min(Math.max(Math.round(this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + getTextWidth() + this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f) + (isShowRedDot() ? calculateRedDotWidth() : 0.0f) + this.mChipEndPadding), this.mMinWidth), this.mMaxWidth);
        iArr[1] = getIntrinsicHeight();
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        updateIntrinsicWidthCloseIconInvisible();
        int iMin = Math.min(Math.max(Math.round(this.mChipStartPadding + (showsChipIcon() ? calculateChipIconWidth() : 0.0f) + this.mTextStartPadding + getTextWidth() + this.mTextEndPadding + (showsCount() ? calculateCountWidth() : 0.0f) + (isShowRedDot() ? calculateRedDotWidth() : 0.0f) + this.mChipEndPadding), this.mMinWidth), this.mMaxWidth);
        this.mIntrinsicWidth = iMin;
        updateChipTextOffsetRange(iMin);
        return this.mIntrinsicWidth;
    }

    public int getUseMaterialEffectAttr() {
        return this.mUseMaterialEffectAttr;
    }

    public boolean isTabSelected() {
        return isCheckable() && isChecked();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public CharSequence onDump() {
        return toString();
    }

    @Override // com.coui.appcompat.OnMaterialStateChangeListener
    public void onMaterialStateChange(int i3, boolean z2) {
        if (i3 == 1) {
            this.mIsStrokeEffectEnabled = z2;
        } else if (i3 == 2) {
            this.mIsBlurEffectEnabled = z2;
        } else if (i3 == 4) {
            this.mIsSpotLightEffectEnabled = z2;
        }
        invalidateSelf();
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable, android.graphics.drawable.Drawable, com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public boolean onStateChange(int[] iArr) {
        boolean z2;
        boolean zOnStateChange = super.onStateChange(iArr);
        int length = iArr.length;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                z2 = false;
                break;
            }
            if (iArr[i3] == 16842912) {
                z2 = true;
                break;
            }
            i3++;
        }
        if (this.mCheckedLayerAlphaAnimation == null) {
            this.mCheckedLayerAlphaAnimation = new CheckedLayerAlphaAnimation();
            this.mUncheckedLayerAlphaAnimation = new UncheckedLayerAlphaAnimation();
            if (z2) {
                this.mCheckedLayerAlphaAnimation.setValue(1.0f);
                this.mUncheckedLayerAlphaAnimation.setValue(0.0f);
                this.mLastCheckedState = true;
            } else {
                this.mCheckedLayerAlphaAnimation.setValue(0.0f);
                this.mUncheckedLayerAlphaAnimation.setValue(1.0f);
                this.mLastCheckedState = false;
            }
        }
        if (this.mLastCheckedState == z2) {
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("状态未变化，保持: ");
                sb.append(z2 ? "选中" : "未选中");
                Log.d(TAG, sb.toString());
            }
            return zOnStateChange;
        }
        if (z2) {
            this.mCheckedLayerAlphaAnimation.animateToFinalPosition(1.0f);
            this.mUncheckedLayerAlphaAnimation.animateToFinalPosition(0.0f);
        } else {
            this.mCheckedLayerAlphaAnimation.animateToFinalPosition(0.0f);
            this.mUncheckedLayerAlphaAnimation.animateToFinalPosition(1.0f);
        }
        this.mLastCheckedState = z2;
        return zOnStateChange;
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable
    public void refresh(Context context) {
        super.refresh(context);
        updateCachedThemeColors();
    }

    public void registerObservers() {
        COUIMaterialStateManager.INSTANCE.addOnMaterialStateChangedListener(this);
    }

    public void setChipTabIconApplyTint(boolean z2) {
        this.mChipIconApplyTint = z2;
        invalidateSelf();
    }

    public void setOnCheckedLayerAlphaChangeListener(OnCheckedLayerAlphaChangeListener onCheckedLayerAlphaChangeListener) {
        this.mAlphaChangeListener = onCheckedLayerAlphaChangeListener;
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable
    public void setShowRedDot(boolean z2) {
        if (this.mChipEndPaddingDefault < 0.0f) {
            this.mChipEndPaddingDefault = getChipEndPadding();
            this.mChipEndPaddingWithRedDot = this.mContextForTheme.getResources().getDimensionPixelSize(R.dimen.coui_chip_tab_end_padding_with_red_dot);
        }
        float f3 = z2 ? this.mChipEndPaddingWithRedDot : this.mChipEndPaddingDefault;
        if (getChipEndPadding() != f3) {
            setChipEndPadding(f3);
        }
        super.setShowRedDot(z2);
        onSizeChange();
    }

    public void setTabSelected(boolean z2) {
        invalidateSelf();
    }

    public void setUncheckedBackgroundColorFromTheme(Context context) {
        try {
            setUncheckedBackgroundColor(ColorUtils.compositeColors(COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorContainer4), COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorBackgroundWithCard)));
        } catch (Exception e3) {
            if (DEBUG) {
                Log.e(TAG, "Failed to get couiColorContainer4", e3);
            }
            setUncheckedBackgroundColor(0);
        }
    }

    public void setUseMaterialEffectAttr(int i3) {
        if (UIUtil.confirmLevelAnim(UIUtil.ANIM_LEVEL_SUPPORT_BLUR_MIN)) {
            COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
            if (cOUIMaterialStateManager.materialVersionCheck() && cOUIMaterialStateManager.checkThemeEnable(this.mContextForTheme) && this.mUseMaterialEffectAttr != i3) {
                this.mUseMaterialEffectAttr = i3;
                invalidateSelf();
            }
        }
    }

    public void unregisterObservers() {
        COUIMaterialStateManager.INSTANCE.removeOnMaterialStateChangedListener(this);
    }

    @Override // com.coui.appcompat.chip.COUIChipDrawable
    public void updateChipTextOffsetRange(int i3) {
        float textWidth = getTextWidth();
        float fCalculateChipIconWidth = showsChipIcon() ? calculateChipIconWidth() : 0.0f;
        float fCalculateCountWidth = showsCount() ? calculateCountWidth() : 0.0f;
        float fCalculateRedDotWidth = isShowRedDot() ? calculateRedDotWidth() : 0.0f;
        float f3 = i3;
        float f4 = this.mChipStartPadding;
        float f5 = (((f3 - f4) - fCalculateChipIconWidth) - fCalculateCountWidth) - fCalculateRedDotWidth;
        float f6 = this.mTextStartPadding;
        float fMax = f4 + f6 + fCalculateChipIconWidth + (Math.max((((f5 - f6) - this.mTextEndPadding) - this.mChipEndPadding) - textWidth, 0.0f) / 2.0f);
        this.mChipTextPaddingStartCloseIconInvisible = fMax;
        this.mChipTextPaddingEndCloseIconInvisible = Math.max(this.mChipEndPadding, (f3 - textWidth) - fMax);
    }
}