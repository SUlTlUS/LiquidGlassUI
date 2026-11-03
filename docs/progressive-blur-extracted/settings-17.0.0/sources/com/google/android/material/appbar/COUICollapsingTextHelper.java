package com.google.android.material.appbar;

import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.core.math.MathUtils;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import com.android.settings.security.SecuritySettings;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.resources.CancelableFontCallback;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TypefaceUtils;

/* JADX INFO: loaded from: classes.dex */
public final class COUICollapsingTextHelper {
    private static final boolean DEBUG_DRAW = false;
    private static final String ELLIPSIS_NORMAL = "…";
    private static final float FADE_MODE_THRESHOLD_FRACTION_RELATIVE = 0.5f;
    private static final String TAG = "CollapsingTextHelper";
    private boolean boundsChanged;
    public final Rect collapsedBounds;
    private float collapsedDrawX;
    private float collapsedDrawY;
    private CancelableFontCallback collapsedFontCallback;
    private float collapsedLetterSpacing;
    private ColorStateList collapsedShadowColor;
    private float collapsedShadowDx;
    private float collapsedShadowDy;
    private float collapsedShadowRadius;
    private float collapsedTextBlend;
    private ColorStateList collapsedTextColor;
    private float collapsedTextWidth;
    private Typeface collapsedTypeface;
    private Typeface collapsedTypefaceBold;
    private Typeface collapsedTypefaceDefault;
    public final RectF currentBounds;
    private float currentDrawX;
    private float currentDrawY;
    private float currentLetterSpacing;
    private int currentOffsetY;
    private int currentShadowColor;
    private float currentShadowDx;
    private float currentShadowDy;
    private float currentShadowRadius;
    private float currentTextSize;
    private Typeface currentTypeface;
    private boolean drawTitle;
    public final Rect expandedBounds;
    private float expandedDrawX;
    private float expandedDrawY;
    private CancelableFontCallback expandedFontCallback;
    private float expandedFraction;
    private float expandedLetterSpacing;
    private int expandedLineCount;
    private ColorStateList expandedShadowColor;
    private float expandedShadowDx;
    private float expandedShadowDy;
    private float expandedShadowRadius;
    private float expandedTextBlend;
    private ColorStateList expandedTextColor;
    private Bitmap expandedTitleTexture;
    private Typeface expandedTypeface;
    private Typeface expandedTypefaceBold;
    private Typeface expandedTypefaceDefault;
    private boolean fadeModeEnabled;
    private float fadeModeStartFraction;
    private float fadeModeThresholdFraction;
    private boolean isRtl;
    private TimeInterpolator mBlendInterpolator;
    private TimeInterpolator positionInterpolator;
    public float scale;
    private int[] state;
    private CharSequence text;
    public StaticLayout textLayout;
    public final TextPaint textPaint;
    private TimeInterpolator textSizeInterpolator;
    private CharSequence textToDraw;
    private CharSequence textToDrawCollapsed;
    private Paint texturePaint;
    private final TextPaint tmpPaint;
    private boolean useTexture;
    private final View view;
    private static final boolean USE_SCALING_TEXTURE = false;
    private static final Paint DEBUG_DRAW_PAINT = null;
    private int expandedTextGravity = 16;
    private int collapsedTextGravity = 16;
    private float expandedTextSize = 15.0f;
    private float collapsedTextSize = 15.0f;
    private boolean isRtlTextDirectionHeuristicsEnabled = true;
    private int maxLines = 1;
    private float lineSpacingAdd = 0.0f;
    private float lineSpacingMultiplier = 1.0f;
    private int hyphenationFrequency = StaticLayoutBuilderCompat.DEFAULT_HYPHENATION_FREQUENCY;
    private final Matrix mTmpOverlayTransformMatrix = new Matrix();
    private final float[] mTmpOverlayCorners = new float[8];

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUICollapsingTextHelper$1, reason: invalid class name */
    public class AnonymousClass1 implements CancelableFontCallback.ApplyFont {
        public AnonymousClass1() {
        }

        @Override // com.google.android.material.resources.CancelableFontCallback.ApplyFont
        public void apply(Typeface typeface) {
            COUICollapsingTextHelper.this.setCollapsedTypeface(typeface);
        }
    }

    /* JADX INFO: renamed from: com.google.android.material.appbar.COUICollapsingTextHelper$2, reason: invalid class name */
    public class AnonymousClass2 implements CancelableFontCallback.ApplyFont {
        public AnonymousClass2() {
        }

        @Override // com.google.android.material.resources.CancelableFontCallback.ApplyFont
        public void apply(Typeface typeface) {
            COUICollapsingTextHelper.this.setExpandedTypeface(typeface);
        }
    }

    public COUICollapsingTextHelper(View view) {
        this.view = view;
        TextPaint textPaint = new TextPaint(SecuritySettings.UNIFY_LOCK_CONFIRM_PROFILE_REQUEST);
        this.textPaint = textPaint;
        this.tmpPaint = new TextPaint(textPaint);
        this.collapsedBounds = new Rect();
        this.expandedBounds = new Rect();
        this.currentBounds = new RectF();
        this.fadeModeThresholdFraction = calculateFadeModeThresholdFraction();
        maybeUpdateFontWeightAdjustment(view.getContext().getResources().getConfiguration());
    }

    private static int blendARGB(int i3, int i4, float f3) {
        float f4 = 1.0f - f3;
        return Color.argb(Math.round((Color.alpha(i3) * f4) + (Color.alpha(i4) * f3)), Math.round((Color.red(i3) * f4) + (Color.red(i4) * f3)), Math.round((Color.green(i3) * f4) + (Color.green(i4) * f3)), Math.round((Color.blue(i3) * f4) + (Color.blue(i4) * f3)));
    }

    private static void boundingRectFromMappedQuad(float[] fArr, RectF rectF) {
        rectF.set(Math.min(Math.min(fArr[0], fArr[2]), Math.min(fArr[4], fArr[6])), Math.min(Math.min(fArr[1], fArr[3]), Math.min(fArr[5], fArr[7])), Math.max(Math.max(fArr[0], fArr[2]), Math.max(fArr[4], fArr[6])), Math.max(Math.max(fArr[1], fArr[3]), Math.max(fArr[5], fArr[7])));
    }

    private void calculateBaseOffsets(boolean z2) {
        StaticLayout staticLayout;
        calculateUsingTextSize(1.0f, z2);
        CharSequence charSequence = this.textToDraw;
        if (charSequence != null && (staticLayout = this.textLayout) != null) {
            this.textToDrawCollapsed = TextUtils.ellipsize(charSequence, this.textPaint, staticLayout.getWidth(), TextUtils.TruncateAt.END);
        }
        CharSequence charSequence2 = this.textToDrawCollapsed;
        float fMeasureTextWidth = 0.0f;
        if (charSequence2 != null) {
            this.collapsedTextWidth = measureTextWidth(this.textPaint, charSequence2);
        } else {
            this.collapsedTextWidth = 0.0f;
        }
        int absoluteGravity = GravityCompat.getAbsoluteGravity(this.collapsedTextGravity, this.isRtl ? 1 : 0);
        int i3 = absoluteGravity & 112;
        if (i3 == 48) {
            this.collapsedDrawY = this.collapsedBounds.top;
        } else if (i3 != 80) {
            this.collapsedDrawY = this.collapsedBounds.centerY() - ((this.textPaint.descent() - this.textPaint.ascent()) / 2.0f);
        } else {
            this.collapsedDrawY = this.collapsedBounds.bottom + this.textPaint.ascent();
        }
        int i4 = absoluteGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (i4 == 1) {
            this.collapsedDrawX = this.collapsedBounds.centerX() - (this.collapsedTextWidth / 2.0f);
        } else if (i4 != 5) {
            this.collapsedDrawX = this.collapsedBounds.left;
        } else {
            this.collapsedDrawX = this.collapsedBounds.right - this.collapsedTextWidth;
        }
        calculateUsingTextSize(0.0f, z2);
        StaticLayout staticLayout2 = this.textLayout;
        float height = staticLayout2 != null ? staticLayout2.getHeight() : 0.0f;
        StaticLayout staticLayout3 = this.textLayout;
        if (staticLayout3 == null || this.maxLines <= 1) {
            CharSequence charSequence3 = this.textToDraw;
            if (charSequence3 != null) {
                fMeasureTextWidth = measureTextWidth(this.textPaint, charSequence3);
            }
        } else {
            fMeasureTextWidth = staticLayout3.getWidth();
        }
        StaticLayout staticLayout4 = this.textLayout;
        this.expandedLineCount = staticLayout4 != null ? staticLayout4.getLineCount() : 0;
        int absoluteGravity2 = GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl ? 1 : 0);
        int i5 = absoluteGravity2 & 112;
        if (i5 == 48) {
            this.expandedDrawY = this.expandedBounds.top;
        } else if (i5 != 80) {
            this.expandedDrawY = this.expandedBounds.centerY() - (height / 2.0f);
        } else {
            this.expandedDrawY = (this.expandedBounds.bottom - height) + this.textPaint.descent();
        }
        int i6 = absoluteGravity2 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (i6 == 1) {
            this.expandedDrawX = this.expandedBounds.centerX() - (fMeasureTextWidth / 2.0f);
        } else if (i6 != 5) {
            this.expandedDrawX = this.expandedBounds.left;
        } else {
            this.expandedDrawX = this.expandedBounds.right - fMeasureTextWidth;
        }
        clearTexture();
        setInterpolatedTextSize(this.expandedFraction);
    }

    private void calculateCurrentOffsets() {
        calculateOffsets(this.expandedFraction);
    }

    private float calculateFadeModeTextAlpha(float f3) {
        float f4 = this.fadeModeThresholdFraction;
        return f3 <= f4 ? AnimationUtils.lerp(1.0f, 0.0f, this.fadeModeStartFraction, f4, f3) : AnimationUtils.lerp(0.0f, 1.0f, f4, 1.0f, f3);
    }

    private float calculateFadeModeThresholdFraction() {
        float f3 = this.fadeModeStartFraction;
        return f3 + ((1.0f - f3) * 0.5f);
    }

    private boolean calculateIsRtl(CharSequence charSequence) {
        boolean zIsDefaultIsRtl = isDefaultIsRtl();
        return this.isRtlTextDirectionHeuristicsEnabled ? isTextDirectionHeuristicsIsRtl(charSequence, zIsDefaultIsRtl) : zIsDefaultIsRtl;
    }

    private void calculateOffsets(float f3) {
        float interpolation;
        interpolateBounds(f3);
        if (!this.fadeModeEnabled) {
            this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, f3, this.positionInterpolator);
            this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, f3, this.positionInterpolator);
            setInterpolatedTextSize(f3);
            interpolation = f3;
        } else if (f3 < this.fadeModeThresholdFraction) {
            this.currentDrawX = this.expandedDrawX;
            this.currentDrawY = this.expandedDrawY;
            setInterpolatedTextSize(0.0f);
            interpolation = 0.0f;
        } else {
            this.currentDrawX = this.collapsedDrawX;
            this.currentDrawY = this.collapsedDrawY - Math.max(0, this.currentOffsetY);
            setInterpolatedTextSize(1.0f);
            interpolation = 1.0f;
        }
        TimeInterpolator timeInterpolator = this.mBlendInterpolator;
        if (timeInterpolator == null) {
            timeInterpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
        }
        TimeInterpolator timeInterpolator2 = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
        setCollapsedTextBlend(1.0f - lerp(0.0f, 1.0f, 1.0f - f3, timeInterpolator2));
        setExpandedTextBlend(lerp(1.0f, 0.0f, f3, timeInterpolator2));
        if (!this.fadeModeEnabled) {
            interpolation = timeInterpolator.getInterpolation(f3);
        }
        if (this.collapsedTextColor != this.expandedTextColor) {
            this.textPaint.setColor(blendARGB(getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), interpolation));
        } else {
            this.textPaint.setColor(getCurrentCollapsedTextColor());
        }
        float f4 = this.collapsedLetterSpacing;
        float f5 = this.expandedLetterSpacing;
        if (f4 != f5) {
            this.textPaint.setLetterSpacing(lerp(f5, f4, f3, timeInterpolator2));
        } else {
            this.textPaint.setLetterSpacing(f4);
        }
        this.currentShadowRadius = lerp(this.expandedShadowRadius, this.collapsedShadowRadius, f3, null);
        this.currentShadowDx = lerp(this.expandedShadowDx, this.collapsedShadowDx, f3, null);
        this.currentShadowDy = lerp(this.expandedShadowDy, this.collapsedShadowDy, f3, null);
        int iBlendARGB = blendARGB(getCurrentColor(this.expandedShadowColor), getCurrentColor(this.collapsedShadowColor), f3);
        this.currentShadowColor = iBlendARGB;
        this.textPaint.setShadowLayer(this.currentShadowRadius, this.currentShadowDx, this.currentShadowDy, iBlendARGB);
        if (this.fadeModeEnabled) {
            this.textPaint.setAlpha((int) (calculateFadeModeTextAlpha(f3) * this.textPaint.getAlpha()));
        }
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private void calculateUsingTextSize(float f3) {
        calculateUsingTextSize(f3, false);
    }

    private void clearTexture() {
        Bitmap bitmap = this.expandedTitleTexture;
        if (bitmap != null) {
            bitmap.recycle();
            this.expandedTitleTexture = null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0044  */
    private void computeMultilineOverlayBoundsUnionStrict(RectF rectF) {
        float f3 = this.currentDrawX;
        float f4 = this.currentDrawY;
        float lineStart = f3 - this.textLayout.getLineStart(0);
        int iMax = Math.max(this.textLayout.getWidth(), 1);
        int iMax2 = Math.max(this.textLayout.getHeight(), 1);
        this.mTmpOverlayTransformMatrix.reset();
        if (this.fadeModeEnabled) {
            this.mTmpOverlayTransformMatrix.postTranslate(lineStart, f4);
        } else {
            float f5 = this.scale;
            if (f5 <= 0.0f || f5 == 1.0f) {
                this.mTmpOverlayTransformMatrix.postTranslate(lineStart, f4);
            } else {
                this.mTmpOverlayTransformMatrix.postScale(f5, f5, f3, f4);
                this.mTmpOverlayTransformMatrix.preTranslate(lineStart, f4);
            }
        }
        float[] fArr = this.mTmpOverlayCorners;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        float f6 = iMax;
        fArr[2] = f6;
        fArr[3] = 0.0f;
        fArr[4] = f6;
        float f7 = iMax2;
        fArr[5] = f7;
        fArr[6] = 0.0f;
        fArr[7] = f7;
        this.mTmpOverlayTransformMatrix.mapPoints(fArr);
        boundingRectFromMappedQuad(this.mTmpOverlayCorners, rectF);
        CharSequence charSequence = this.textToDrawCollapsed;
        if (charSequence == null || charSequence.length() == 0) {
            return;
        }
        int lineBaseline = this.textLayout.getLineBaseline(0);
        this.textPaint.setTextSize(this.currentTextSize);
        Paint.FontMetrics fontMetrics = this.textPaint.getFontMetrics();
        TextPaint textPaint = this.textPaint;
        CharSequence charSequence2 = this.textToDrawCollapsed;
        float f8 = lineBaseline;
        unionLocalRectThroughMatrix(this.mTmpOverlayTransformMatrix, 0.0f, f8 + fontMetrics.ascent, Math.max(textPaint.measureText(charSequence2, 0, charSequence2.length()), 1.0f), f8 + fontMetrics.descent, rectF);
        if (this.fadeModeEnabled) {
            return;
        }
        String strTrim = this.textToDrawCollapsed.toString().trim();
        if (strTrim.endsWith(ELLIPSIS_NORMAL) && strTrim.length() > 1) {
            strTrim = strTrim.substring(0, strTrim.length() - 1);
        }
        int iMin = Math.min(strTrim.length(), this.textLayout.getLineEnd(0));
        if (iMin > 0) {
            unionLocalRectThroughMatrix(this.mTmpOverlayTransformMatrix, 0.0f, f8 + fontMetrics.ascent, Math.max(this.textPaint.measureText(strTrim, 0, iMin), 1.0f), f8 + fontMetrics.descent, rectF);
        }
    }

    private StaticLayout createStaticLayout(int i3, float f3, boolean z2) {
        StaticLayout staticLayoutBuild;
        try {
            staticLayoutBuild = StaticLayoutBuilderCompat.obtain(this.text, this.textPaint, (int) f3).setEllipsize(TextUtils.TruncateAt.END).setIsRtl(z2).setAlignment(i3 == 1 ? Layout.Alignment.ALIGN_NORMAL : getMultilineTextLayoutAlignment()).setIncludePad(false).setMaxLines(i3).setLineSpacing(this.lineSpacingAdd, this.lineSpacingMultiplier).setHyphenationFrequency(this.hyphenationFrequency).build();
        } catch (StaticLayoutBuilderCompat.StaticLayoutBuilderCompatException e3) {
            Log.e(TAG, e3.getCause().getMessage(), e3);
            staticLayoutBuild = null;
        }
        return (StaticLayout) Preconditions.checkNotNull(staticLayoutBuild);
    }

    private void drawMultilineTransition(Canvas canvas, float f3, float f4) {
        int alpha = this.textPaint.getAlpha();
        canvas.translate(f3, f4);
        float f5 = alpha;
        this.textPaint.setAlpha((int) (this.expandedTextBlend * f5));
        TextPaint textPaint = this.textPaint;
        textPaint.setShadowLayer(this.currentShadowRadius, this.currentShadowDx, this.currentShadowDy, MaterialColors.compositeARGBWithAlpha(this.currentShadowColor, textPaint.getAlpha()));
        this.textLayout.draw(canvas);
        this.textPaint.setAlpha((int) (this.collapsedTextBlend * f5));
        TextPaint textPaint2 = this.textPaint;
        textPaint2.setShadowLayer(this.currentShadowRadius, this.currentShadowDx, this.currentShadowDy, MaterialColors.compositeARGBWithAlpha(this.currentShadowColor, textPaint2.getAlpha()));
        int lineBaseline = this.textLayout.getLineBaseline(0);
        CharSequence charSequence = this.textToDrawCollapsed;
        float f6 = lineBaseline;
        canvas.drawText(charSequence, 0, charSequence.length(), 0.0f, f6, this.textPaint);
        this.textPaint.setShadowLayer(this.currentShadowRadius, this.currentShadowDx, this.currentShadowDy, this.currentShadowColor);
        if (this.fadeModeEnabled) {
            return;
        }
        String strTrim = this.textToDrawCollapsed.toString().trim();
        if (strTrim.endsWith(ELLIPSIS_NORMAL)) {
            strTrim = strTrim.substring(0, strTrim.length() - 1);
        }
        this.textPaint.setAlpha(alpha);
        canvas.drawText(strTrim, 0, Math.min(this.textLayout.getLineEnd(0), strTrim.length()), 0.0f, f6, (Paint) this.textPaint);
    }

    private void ensureExpandedTexture() {
        if (this.expandedTitleTexture != null || this.expandedBounds.isEmpty() || TextUtils.isEmpty(this.textToDraw)) {
            return;
        }
        calculateOffsets(0.0f);
        int width = this.textLayout.getWidth();
        int height = this.textLayout.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        this.expandedTitleTexture = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.textLayout.draw(new Canvas(this.expandedTitleTexture));
        if (this.texturePaint == null) {
            this.texturePaint = new Paint(3);
        }
    }

    private float getCollapsedTextLeftBound(int i3, int i4) {
        float f3;
        float f4;
        int i5;
        if (i4 == 17 || (i4 & 7) == 1) {
            f3 = i3 / 2.0f;
            f4 = this.collapsedTextWidth / 2.0f;
        } else if ((i4 & GravityCompat.END) == 8388613 || (i4 & 5) == 5) {
            if (this.isRtl) {
                i5 = this.collapsedBounds.left;
                return i5;
            }
            f3 = this.collapsedBounds.right;
            f4 = this.collapsedTextWidth;
        } else {
            if (!this.isRtl) {
                i5 = this.collapsedBounds.left;
                return i5;
            }
            f3 = this.collapsedBounds.right;
            f4 = this.collapsedTextWidth;
        }
        return f3 - f4;
    }

    private float getCollapsedTextRightBound(RectF rectF, int i3, int i4) {
        float f3;
        float f4;
        int i5;
        if (i4 == 17 || (i4 & 7) == 1) {
            f3 = i3 / 2.0f;
            f4 = this.collapsedTextWidth / 2.0f;
        } else if ((i4 & GravityCompat.END) == 8388613 || (i4 & 5) == 5) {
            if (!this.isRtl) {
                i5 = this.collapsedBounds.right;
                return i5;
            }
            f3 = rectF.left;
            f4 = this.collapsedTextWidth;
        } else {
            if (this.isRtl) {
                i5 = this.collapsedBounds.right;
                return i5;
            }
            f3 = rectF.left;
            f4 = this.collapsedTextWidth;
        }
        return f3 + f4;
    }

    private int getCurrentColor(ColorStateList colorStateList) {
        if (colorStateList == null) {
            return 0;
        }
        int[] iArr = this.state;
        return iArr != null ? colorStateList.getColorForState(iArr, 0) : colorStateList.getDefaultColor();
    }

    private int getCurrentExpandedTextColor() {
        return getCurrentColor(this.expandedTextColor);
    }

    private Layout.Alignment getMultilineTextLayoutAlignment() {
        int absoluteGravity = GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl ? 1 : 0) & 7;
        if (absoluteGravity == 1) {
            return Layout.Alignment.ALIGN_CENTER;
        }
        if (absoluteGravity != 5) {
            return this.isRtl ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
        }
        return this.isRtl ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
    }

    private void getTextPaintCollapsed(TextPaint textPaint) {
        textPaint.setTextSize(this.collapsedTextSize);
        textPaint.setTypeface(this.collapsedTypeface);
        textPaint.setLetterSpacing(this.collapsedLetterSpacing);
    }

    private void getTextPaintExpanded(TextPaint textPaint) {
        textPaint.setTextSize(this.expandedTextSize);
        textPaint.setTypeface(this.expandedTypeface);
        textPaint.setLetterSpacing(this.expandedLetterSpacing);
    }

    private void interpolateBounds(float f3) {
        if (this.fadeModeEnabled) {
            this.currentBounds.set(f3 < this.fadeModeThresholdFraction ? this.expandedBounds : this.collapsedBounds);
            return;
        }
        this.currentBounds.left = lerp(this.expandedBounds.left, this.collapsedBounds.left, f3, this.positionInterpolator);
        this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, f3, this.positionInterpolator);
        this.currentBounds.right = lerp(this.expandedBounds.right, this.collapsedBounds.right, f3, this.positionInterpolator);
        this.currentBounds.bottom = lerp(this.expandedBounds.bottom, this.collapsedBounds.bottom, f3, this.positionInterpolator);
    }

    private static boolean isClose(float f3, float f4) {
        return Math.abs(f3 - f4) < 1.0E-5f;
    }

    private boolean isDefaultIsRtl() {
        return ViewCompat.getLayoutDirection(this.view) == 1;
    }

    private boolean isTextDirectionHeuristicsIsRtl(CharSequence charSequence, boolean z2) {
        return (z2 ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(charSequence, 0, charSequence.length());
    }

    private static float lerp(float f3, float f4, float f5, TimeInterpolator timeInterpolator) {
        if (timeInterpolator != null) {
            f5 = timeInterpolator.getInterpolation(f5);
        }
        return AnimationUtils.lerp(f3, f4, f5);
    }

    private void mapBitmapCornersThroughDrawTransform(float f3, float f4, float f5, float f6, RectF rectF) {
        this.mTmpOverlayTransformMatrix.reset();
        if (!this.fadeModeEnabled) {
            float f7 = this.scale;
            if (f7 > 0.0f && f7 != 1.0f) {
                this.mTmpOverlayTransformMatrix.postScale(f7, f7, f3, f4);
            }
        }
        float[] fArr = this.mTmpOverlayCorners;
        fArr[0] = f3;
        fArr[1] = f4;
        float f8 = f5 + f3;
        fArr[2] = f8;
        fArr[3] = f4;
        fArr[4] = f8;
        float f9 = f4 + f6;
        fArr[5] = f9;
        fArr[6] = f3;
        fArr[7] = f9;
        this.mTmpOverlayTransformMatrix.mapPoints(fArr);
        boundingRectFromMappedQuad(this.mTmpOverlayCorners, rectF);
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0021  */
    private void mapTextLayoutCornersThroughDrawTransform(float f3, float f4, float f5, float f6, RectF rectF) {
        this.mTmpOverlayTransformMatrix.reset();
        if (this.fadeModeEnabled) {
            this.mTmpOverlayTransformMatrix.postTranslate(f3, f4);
        } else {
            float f7 = this.scale;
            if (f7 <= 0.0f || f7 == 1.0f) {
                this.mTmpOverlayTransformMatrix.postTranslate(f3, f4);
            } else {
                this.mTmpOverlayTransformMatrix.postScale(f7, f7, f3, f4);
                this.mTmpOverlayTransformMatrix.preTranslate(f3, f4);
            }
        }
        float[] fArr = this.mTmpOverlayCorners;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        fArr[2] = f5;
        fArr[3] = 0.0f;
        fArr[4] = f5;
        fArr[5] = f6;
        fArr[6] = 0.0f;
        fArr[7] = f6;
        this.mTmpOverlayTransformMatrix.mapPoints(fArr);
        boundingRectFromMappedQuad(this.mTmpOverlayCorners, rectF);
    }

    private float measureTextWidth(TextPaint textPaint, CharSequence charSequence) {
        return textPaint.measureText(charSequence, 0, charSequence.length());
    }

    private static boolean rectEquals(Rect rect, int i3, int i4, int i5, int i6) {
        return rect.left == i3 && rect.top == i4 && rect.right == i5 && rect.bottom == i6;
    }

    private void setCollapsedTextBlend(float f3) {
        this.collapsedTextBlend = f3;
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private boolean setCollapsedTypefaceInternal(Typeface typeface) {
        CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        if (this.collapsedTypefaceDefault == typeface) {
            return false;
        }
        this.collapsedTypefaceDefault = typeface;
        Typeface typefaceMaybeCopyWithFontWeightAdjustment = TypefaceUtils.maybeCopyWithFontWeightAdjustment(this.view.getContext().getResources().getConfiguration(), typeface);
        this.collapsedTypefaceBold = typefaceMaybeCopyWithFontWeightAdjustment;
        if (typefaceMaybeCopyWithFontWeightAdjustment == null) {
            typefaceMaybeCopyWithFontWeightAdjustment = this.collapsedTypefaceDefault;
        }
        this.collapsedTypeface = typefaceMaybeCopyWithFontWeightAdjustment;
        return true;
    }

    private void setExpandedTextBlend(float f3) {
        this.expandedTextBlend = f3;
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private boolean setExpandedTypefaceInternal(Typeface typeface) {
        CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        if (this.expandedTypefaceDefault == typeface) {
            return false;
        }
        this.expandedTypefaceDefault = typeface;
        Typeface typefaceMaybeCopyWithFontWeightAdjustment = TypefaceUtils.maybeCopyWithFontWeightAdjustment(this.view.getContext().getResources().getConfiguration(), typeface);
        this.expandedTypefaceBold = typefaceMaybeCopyWithFontWeightAdjustment;
        if (typefaceMaybeCopyWithFontWeightAdjustment == null) {
            typefaceMaybeCopyWithFontWeightAdjustment = this.expandedTypefaceDefault;
        }
        this.expandedTypeface = typefaceMaybeCopyWithFontWeightAdjustment;
        return true;
    }

    private void setInterpolatedTextSize(float f3) {
        calculateUsingTextSize(f3);
        boolean z2 = USE_SCALING_TEXTURE && this.scale != 1.0f;
        this.useTexture = z2;
        if (z2) {
            ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private boolean shouldDrawMultiline() {
        if (this.maxLines > 1) {
            return (!this.isRtl || this.fadeModeEnabled) && !this.useTexture;
        }
        return false;
    }

    private void unionLocalRectThroughMatrix(Matrix matrix, float f3, float f4, float f5, float f6, RectF rectF) {
        float[] fArr = this.mTmpOverlayCorners;
        fArr[0] = f3;
        fArr[1] = f4;
        fArr[2] = f5;
        fArr[3] = f4;
        fArr[4] = f5;
        fArr[5] = f6;
        fArr[6] = f3;
        fArr[7] = f6;
        matrix.mapPoints(fArr);
        float[] fArr2 = this.mTmpOverlayCorners;
        float fMin = Math.min(fArr2[0], fArr2[2]);
        float[] fArr3 = this.mTmpOverlayCorners;
        float fMin2 = Math.min(fMin, Math.min(fArr3[4], fArr3[6]));
        float[] fArr4 = this.mTmpOverlayCorners;
        float fMax = Math.max(fArr4[0], fArr4[2]);
        float[] fArr5 = this.mTmpOverlayCorners;
        float fMax2 = Math.max(fMax, Math.max(fArr5[4], fArr5[6]));
        float[] fArr6 = this.mTmpOverlayCorners;
        float fMin3 = Math.min(fArr6[1], fArr6[3]);
        float[] fArr7 = this.mTmpOverlayCorners;
        float fMin4 = Math.min(fMin3, Math.min(fArr7[5], fArr7[7]));
        float[] fArr8 = this.mTmpOverlayCorners;
        float fMax3 = Math.max(fArr8[1], fArr8[3]);
        float[] fArr9 = this.mTmpOverlayCorners;
        float fMax4 = Math.max(fMax3, Math.max(fArr9[5], fArr9[7]));
        rectF.union(fMin2, fMin4);
        rectF.union(fMax2, fMax4);
    }

    public boolean computeOverlayBoundsPreParentTransform(RectF rectF) {
        Bitmap bitmap;
        rectF.setEmpty();
        if (this.textToDraw == null || this.textLayout == null) {
            return false;
        }
        float f3 = this.currentDrawX;
        float f4 = this.currentDrawY;
        if (this.useTexture && (bitmap = this.expandedTitleTexture) != null) {
            mapBitmapCornersThroughDrawTransform(f3, f4, Math.max(bitmap.getWidth(), 1.0f), Math.max(this.expandedTitleTexture.getHeight(), 1.0f), rectF);
            return true;
        }
        if (!shouldDrawMultiline() || (this.fadeModeEnabled && this.expandedFraction <= this.fadeModeThresholdFraction)) {
            mapTextLayoutCornersThroughDrawTransform(f3, f4, Math.max(this.textLayout.getWidth(), 1), Math.max(this.textLayout.getHeight(), 1), rectF);
            return true;
        }
        computeMultilineOverlayBoundsUnionStrict(rectF);
        return !rectF.isEmpty();
    }

    public void draw(Canvas canvas) {
        int iSave = canvas.save();
        if (this.textToDraw == null || !this.drawTitle) {
            canvas.restoreToCount(iSave);
            return;
        }
        this.textPaint.setTextSize(this.currentTextSize);
        float f3 = this.currentDrawX;
        float f4 = this.currentDrawY;
        boolean z2 = this.useTexture && this.expandedTitleTexture != null;
        float f5 = this.scale;
        if (f5 != 1.0f && !this.fadeModeEnabled) {
            canvas.scale(f5, f5, f3, f4);
        }
        if (z2) {
            canvas.drawBitmap(this.expandedTitleTexture, f3, f4, this.texturePaint);
            canvas.restoreToCount(iSave);
            return;
        }
        if (!shouldDrawMultiline() || (this.fadeModeEnabled && this.expandedFraction <= this.fadeModeThresholdFraction)) {
            canvas.translate(f3, f4);
            this.textLayout.draw(canvas);
        } else {
            drawMultilineTransition(canvas, this.currentDrawX - this.textLayout.getLineStart(0), f4);
        }
        canvas.restoreToCount(iSave);
    }

    public void drawTextLayoutInOverlayBounds(Canvas canvas, RectF rectF, StaticLayout staticLayout, boolean z2) {
        staticLayout.getPaint();
        float f3 = z2 ? 0.0f : this.currentDrawX - rectF.left;
        float f4 = z2 ? 0.0f : this.currentDrawY - rectF.top;
        int iSave = canvas.save();
        canvas.translate(f3, f4);
        float f5 = this.scale;
        if (f5 != 1.0f && !this.fadeModeEnabled) {
            canvas.scale(f5, f5);
        }
        staticLayout.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    public void getCollapsedTextActualBounds(RectF rectF, int i3, int i4) {
        this.isRtl = calculateIsRtl(this.text);
        rectF.left = getCollapsedTextLeftBound(i3, i4);
        rectF.top = this.collapsedBounds.top;
        rectF.right = getCollapsedTextRightBound(rectF, i3, i4);
        rectF.bottom = this.collapsedBounds.top + getCollapsedTextHeight();
    }

    public ColorStateList getCollapsedTextColor() {
        return this.collapsedTextColor;
    }

    public int getCollapsedTextGravity() {
        return this.collapsedTextGravity;
    }

    public float getCollapsedTextHeight() {
        getTextPaintCollapsed(this.tmpPaint);
        return -this.tmpPaint.ascent();
    }

    public float getCollapsedTextSize() {
        return this.collapsedTextSize;
    }

    public float getCollapsedTextWidth() {
        return this.collapsedTextWidth;
    }

    public Typeface getCollapsedTypeface() {
        Typeface typeface = this.collapsedTypeface;
        return typeface != null ? typeface : Typeface.DEFAULT;
    }

    public int getCurrentCollapsedTextColor() {
        return getCurrentColor(this.collapsedTextColor);
    }

    public float getCurrentDrawX() {
        return this.currentDrawX;
    }

    public float getCurrentDrawY() {
        return this.currentDrawY;
    }

    public int getExpandedLineCount() {
        return this.expandedLineCount;
    }

    public ColorStateList getExpandedTextColor() {
        return this.expandedTextColor;
    }

    public float getExpandedTextFullHeight() {
        getTextPaintExpanded(this.tmpPaint);
        return (-this.tmpPaint.ascent()) + this.tmpPaint.descent();
    }

    public int getExpandedTextGravity() {
        return this.expandedTextGravity;
    }

    public float getExpandedTextHeight() {
        getTextPaintExpanded(this.tmpPaint);
        return -this.tmpPaint.ascent();
    }

    public float getExpandedTextSize() {
        return this.expandedTextSize;
    }

    public Typeface getExpandedTypeface() {
        Typeface typeface = this.expandedTypeface;
        return typeface != null ? typeface : Typeface.DEFAULT;
    }

    public float getExpansionFraction() {
        return this.expandedFraction;
    }

    public float getFadeModeThresholdFraction() {
        return this.fadeModeThresholdFraction;
    }

    public int getHyphenationFrequency() {
        return this.hyphenationFrequency;
    }

    public int getLineCount() {
        StaticLayout staticLayout = this.textLayout;
        if (staticLayout != null) {
            return staticLayout.getLineCount();
        }
        return 0;
    }

    public float getLineSpacingAdd() {
        return this.textLayout.getSpacingAdd();
    }

    public float getLineSpacingMultiplier() {
        return this.textLayout.getSpacingMultiplier();
    }

    public int getMaxLines() {
        return this.maxLines;
    }

    public TimeInterpolator getPositionInterpolator() {
        return this.positionInterpolator;
    }

    public CharSequence getText() {
        return this.text;
    }

    public float getTitleLayoutScale() {
        return this.scale;
    }

    public boolean isRtlTextDirectionHeuristicsEnabled() {
        return this.isRtlTextDirectionHeuristicsEnabled;
    }

    public final boolean isStateful() {
        ColorStateList colorStateList = this.collapsedTextColor;
        if (colorStateList != null && colorStateList.isStateful()) {
            return true;
        }
        ColorStateList colorStateList2 = this.expandedTextColor;
        return colorStateList2 != null && colorStateList2.isStateful();
    }

    public void maybeUpdateFontWeightAdjustment(Configuration configuration) {
        Typeface typeface = this.collapsedTypefaceDefault;
        if (typeface != null) {
            this.collapsedTypefaceBold = TypefaceUtils.maybeCopyWithFontWeightAdjustment(configuration, typeface);
        }
        Typeface typeface2 = this.expandedTypefaceDefault;
        if (typeface2 != null) {
            this.expandedTypefaceBold = TypefaceUtils.maybeCopyWithFontWeightAdjustment(configuration, typeface2);
        }
        Typeface typeface3 = this.collapsedTypefaceBold;
        if (typeface3 == null) {
            typeface3 = this.collapsedTypefaceDefault;
        }
        this.collapsedTypeface = typeface3;
        Typeface typeface4 = this.expandedTypefaceBold;
        if (typeface4 == null) {
            typeface4 = this.expandedTypefaceDefault;
        }
        this.expandedTypeface = typeface4;
        recalculate(true);
    }

    public void onBoundsChanged() {
        this.drawTitle = this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0;
    }

    public void recalculate() {
        recalculate(false);
    }

    public void setBlendInterpolator(TimeInterpolator timeInterpolator) {
        this.mBlendInterpolator = timeInterpolator;
        recalculate();
    }

    public void setCollapsedBounds(int i3, int i4, int i5, int i6) {
        if (rectEquals(this.collapsedBounds, i3, i4, i5, i6)) {
            return;
        }
        this.collapsedBounds.set(i3, i4, i5, i6);
        this.boundsChanged = true;
        onBoundsChanged();
    }

    public void setCollapsedTextAppearance(int i3) {
        TextAppearance textAppearance = new TextAppearance(this.view.getContext(), i3);
        if (textAppearance.getTextColor() != null) {
            this.collapsedTextColor = textAppearance.getTextColor();
        }
        if (textAppearance.getTextSize() != 0.0f) {
            this.collapsedTextSize = textAppearance.getTextSize();
        }
        ColorStateList colorStateList = textAppearance.shadowColor;
        if (colorStateList != null) {
            this.collapsedShadowColor = colorStateList;
        }
        this.collapsedShadowDx = textAppearance.shadowDx;
        this.collapsedShadowDy = textAppearance.shadowDy;
        this.collapsedShadowRadius = textAppearance.shadowRadius;
        this.collapsedLetterSpacing = textAppearance.letterSpacing;
        CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        this.collapsedFontCallback = new CancelableFontCallback(new AnonymousClass1(), textAppearance.getFallbackFont());
        textAppearance.getFontAsync(this.view.getContext(), this.collapsedFontCallback);
        recalculate();
    }

    public void setCollapsedTextColor(ColorStateList colorStateList) {
        if (this.collapsedTextColor != colorStateList) {
            this.collapsedTextColor = colorStateList;
            recalculate();
        }
    }

    public void setCollapsedTextGravity(int i3) {
        if (this.collapsedTextGravity != i3) {
            this.collapsedTextGravity = i3;
            recalculate();
        }
    }

    public void setCollapsedTextSize(float f3) {
        if (this.collapsedTextSize != f3) {
            this.collapsedTextSize = f3;
            recalculate();
        }
    }

    public void setCollapsedTypeface(Typeface typeface) {
        if (setCollapsedTypefaceInternal(typeface)) {
            recalculate();
        }
    }

    public void setCurrentOffsetY(int i3) {
        this.currentOffsetY = i3;
    }

    public void setExpandedBounds(int i3, int i4, int i5, int i6) {
        if (rectEquals(this.expandedBounds, i3, i4, i5, i6)) {
            return;
        }
        this.expandedBounds.set(i3, i4, i5, i6);
        this.boundsChanged = true;
        onBoundsChanged();
    }

    public void setExpandedLetterSpacing(float f3) {
        if (this.expandedLetterSpacing != f3) {
            this.expandedLetterSpacing = f3;
            recalculate();
        }
    }

    public void setExpandedTextAppearance(int i3) {
        TextAppearance textAppearance = new TextAppearance(this.view.getContext(), i3);
        if (textAppearance.getTextColor() != null) {
            this.expandedTextColor = textAppearance.getTextColor();
        }
        if (textAppearance.getTextSize() != 0.0f) {
            this.expandedTextSize = textAppearance.getTextSize();
        }
        ColorStateList colorStateList = textAppearance.shadowColor;
        if (colorStateList != null) {
            this.expandedShadowColor = colorStateList;
        }
        this.expandedShadowDx = textAppearance.shadowDx;
        this.expandedShadowDy = textAppearance.shadowDy;
        this.expandedShadowRadius = textAppearance.shadowRadius;
        this.expandedLetterSpacing = textAppearance.letterSpacing;
        CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        this.expandedFontCallback = new CancelableFontCallback(new AnonymousClass2(), textAppearance.getFallbackFont());
        textAppearance.getFontAsync(this.view.getContext(), this.expandedFontCallback);
        recalculate();
    }

    public void setExpandedTextColor(ColorStateList colorStateList) {
        if (this.expandedTextColor != colorStateList) {
            this.expandedTextColor = colorStateList;
            recalculate();
        }
    }

    public void setExpandedTextGravity(int i3) {
        if (this.expandedTextGravity != i3) {
            this.expandedTextGravity = i3;
            recalculate();
        }
    }

    public void setExpandedTextSize(float f3) {
        if (this.expandedTextSize != f3) {
            this.expandedTextSize = f3;
            recalculate();
        }
    }

    public void setExpandedTypeface(Typeface typeface) {
        if (setExpandedTypefaceInternal(typeface)) {
            recalculate();
        }
    }

    public void setExpansionFraction(float f3) {
        float fClamp = MathUtils.clamp(f3, 0.0f, 1.0f);
        if (fClamp != this.expandedFraction) {
            this.expandedFraction = fClamp;
            calculateCurrentOffsets();
        }
    }

    public void setFadeModeEnabled(boolean z2) {
        this.fadeModeEnabled = z2;
    }

    public void setFadeModeStartFraction(float f3) {
        this.fadeModeStartFraction = f3;
        this.fadeModeThresholdFraction = calculateFadeModeThresholdFraction();
    }

    public void setHyphenationFrequency(int i3) {
        this.hyphenationFrequency = i3;
    }

    public void setLineSpacingAdd(float f3) {
        this.lineSpacingAdd = f3;
    }

    public void setLineSpacingMultiplier(float f3) {
        this.lineSpacingMultiplier = f3;
    }

    public void setMaxLines(int i3) {
        if (i3 != this.maxLines) {
            this.maxLines = i3;
            clearTexture();
            recalculate();
        }
    }

    public void setPositionInterpolator(TimeInterpolator timeInterpolator) {
        this.positionInterpolator = timeInterpolator;
        recalculate();
    }

    public void setRtlTextDirectionHeuristicsEnabled(boolean z2) {
        this.isRtlTextDirectionHeuristicsEnabled = z2;
    }

    public final boolean setState(int[] iArr) {
        this.state = iArr;
        if (!isStateful()) {
            return false;
        }
        recalculate();
        return true;
    }

    public void setText(CharSequence charSequence) {
        if (charSequence == null || !TextUtils.equals(this.text, charSequence)) {
            this.text = charSequence;
            this.textToDraw = null;
            clearTexture();
            recalculate();
        }
    }

    public void setTextSizeInterpolator(TimeInterpolator timeInterpolator) {
        this.textSizeInterpolator = timeInterpolator;
        recalculate();
    }

    public void setTypefaces(Typeface typeface) {
        boolean collapsedTypefaceInternal = setCollapsedTypefaceInternal(typeface);
        boolean expandedTypefaceInternal = setExpandedTypefaceInternal(typeface);
        if (collapsedTypefaceInternal || expandedTypefaceInternal) {
            recalculate();
        }
    }

    private void calculateUsingTextSize(float f3, boolean z2) {
        boolean z3;
        float f4;
        float f5;
        boolean z4;
        if (this.text == null) {
            return;
        }
        float fWidth = this.collapsedBounds.width();
        float fWidth2 = this.expandedBounds.width();
        if (isClose(f3, 1.0f)) {
            f4 = this.collapsedTextSize;
            f5 = this.collapsedLetterSpacing;
            this.scale = 1.0f;
            Typeface typeface = this.currentTypeface;
            Typeface typeface2 = this.collapsedTypeface;
            if (typeface != typeface2) {
                this.currentTypeface = typeface2;
                z4 = true;
            } else {
                z4 = false;
            }
        } else {
            float f6 = this.expandedTextSize;
            float f7 = this.expandedLetterSpacing;
            Typeface typeface3 = this.currentTypeface;
            Typeface typeface4 = this.expandedTypeface;
            if (typeface3 != typeface4) {
                this.currentTypeface = typeface4;
                z3 = true;
            } else {
                z3 = false;
            }
            if (isClose(f3, 0.0f)) {
                this.scale = 1.0f;
            } else {
                this.scale = lerp(this.expandedTextSize, this.collapsedTextSize, f3, this.textSizeInterpolator) / this.expandedTextSize;
            }
            float f8 = this.collapsedTextSize / this.expandedTextSize;
            fWidth = (!z2 && fWidth2 * f8 > fWidth) ? Math.min(fWidth / f8, fWidth2) : fWidth2;
            f4 = f6;
            f5 = f7;
            z4 = z3;
        }
        if (fWidth > 0.0f) {
            z4 = ((this.currentTextSize > f4 ? 1 : (this.currentTextSize == f4 ? 0 : -1)) != 0) || ((this.currentLetterSpacing > f5 ? 1 : (this.currentLetterSpacing == f5 ? 0 : -1)) != 0) || this.boundsChanged || z4;
            this.currentTextSize = f4;
            this.currentLetterSpacing = f5;
            this.boundsChanged = false;
        }
        if (this.textToDraw == null || z4) {
            this.textPaint.setTextSize(this.currentTextSize);
            this.textPaint.setTypeface(this.currentTypeface);
            this.textPaint.setLetterSpacing(this.currentLetterSpacing);
            this.textPaint.setLinearText(this.scale != 1.0f);
            this.isRtl = calculateIsRtl(this.text);
            StaticLayout staticLayoutCreateStaticLayout = createStaticLayout(shouldDrawMultiline() ? this.maxLines : 1, fWidth, this.isRtl);
            this.textLayout = staticLayoutCreateStaticLayout;
            this.textToDraw = staticLayoutCreateStaticLayout.getText();
        }
    }

    public void recalculate(boolean z2) {
        if ((this.view.getHeight() <= 0 || this.view.getWidth() <= 0) && !z2) {
            return;
        }
        calculateBaseOffsets(z2);
        calculateCurrentOffsets();
    }

    public void setCollapsedBounds(Rect rect) {
        setCollapsedBounds(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void setExpandedBounds(Rect rect) {
        setExpandedBounds(rect.left, rect.top, rect.right, rect.bottom);
    }
}