package com.coui.appcompat.cardview;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import com.support.cardview.R;

/* JADX INFO: loaded from: classes7.dex */
class RoundRectDrawableWithShadow extends Drawable {
    private static final double COS_45 = Math.cos(Math.toRadians(45.0d));
    private static final float SHADOW_MULTIPLIER = 1.5f;
    private static RoundRectHelper sRoundRectHelper;
    private ColorStateList mBackground;
    private final RectF mCardBounds;
    private float mCornerRadius;
    private Paint mCornerShadowPaint;
    private Path mCornerShadowPath;
    private Paint mEdgeShadowPaint;
    private final int mInsetShadow;
    private float mRawMaxShadowSize;
    private float mRawShadowSize;
    private final int mShadowEndColor;
    private float mShadowSize;
    private final int mShadowStartColor;
    private boolean mDirty = true;
    private boolean mAddPaddingForCorners = true;
    private boolean mPrintedShadowClipWarning = false;
    private Paint mPaint = new Paint(5);

    public interface RoundRectHelper {
        void drawRoundRect(Canvas canvas, RectF rectF, float f3, Paint paint);
    }

    public RoundRectDrawableWithShadow(Resources resources, ColorStateList colorStateList, float f3, float f4, float f5) {
        this.mShadowStartColor = resources.getColor(R.color.cardview_shadow_start_color);
        this.mShadowEndColor = resources.getColor(R.color.cardview_shadow_end_color);
        this.mInsetShadow = resources.getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        setBackground(colorStateList);
        Paint paint = new Paint(5);
        this.mCornerShadowPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mCornerRadius = (int) (f3 + 0.5f);
        this.mCardBounds = new RectF();
        Paint paint2 = new Paint(this.mCornerShadowPaint);
        this.mEdgeShadowPaint = paint2;
        paint2.setAntiAlias(false);
        setShadowSize(f4, f5);
    }

    private void buildComponents(Rect rect) {
        float f3 = this.mRawMaxShadowSize;
        float f4 = 1.5f * f3;
        this.mCardBounds.set(rect.left + f3, rect.top + f4, rect.right - f3, rect.bottom - f4);
        buildShadowCorners();
    }

    private void buildShadowCorners() {
        float f3 = this.mCornerRadius;
        RectF rectF = new RectF(-f3, -f3, f3, f3);
        RectF rectF2 = new RectF(rectF);
        float f4 = this.mShadowSize;
        rectF2.inset(-f4, -f4);
        Path path = this.mCornerShadowPath;
        if (path == null) {
            this.mCornerShadowPath = new Path();
        } else {
            path.reset();
        }
        this.mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0f);
        this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0f);
        this.mCornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.mCornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.mCornerShadowPath.close();
        float f5 = this.mCornerRadius;
        float f6 = f5 / (this.mShadowSize + f5);
        Paint paint = this.mCornerShadowPaint;
        float f7 = this.mCornerRadius + this.mShadowSize;
        int i3 = this.mShadowStartColor;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        paint.setShader(new RadialGradient(0.0f, 0.0f, f7, new int[]{i3, i3, this.mShadowEndColor}, new float[]{0.0f, f6, 1.0f}, tileMode));
        Paint paint2 = this.mEdgeShadowPaint;
        float f8 = this.mCornerRadius;
        float f9 = this.mShadowSize;
        float f10 = (-f8) + f9;
        float f11 = (-f8) - f9;
        int i4 = this.mShadowStartColor;
        paint2.setShader(new LinearGradient(0.0f, f10, 0.0f, f11, new int[]{i4, i4, this.mShadowEndColor}, new float[]{0.0f, 0.5f, 1.0f}, tileMode));
        this.mEdgeShadowPaint.setAntiAlias(false);
    }

    public static float calculateHorizontalPadding(float f3, float f4, boolean z2) {
        return z2 ? (float) (((double) f3) + ((1.0d - COS_45) * ((double) f4))) : f3;
    }

    public static float calculateVerticalPadding(float f3, float f4, boolean z2) {
        return z2 ? (float) (((double) (f3 * 1.5f)) + ((1.0d - COS_45) * ((double) f4))) : f3 * 1.5f;
    }

    private void drawShadow(Canvas canvas) {
        Canvas canvas2;
        float f3 = this.mCornerRadius;
        float f4 = (-f3) - this.mShadowSize;
        float f5 = f3 + this.mInsetShadow + (this.mRawShadowSize / 2.0f);
        float f6 = 2.0f * f5;
        boolean z2 = this.mCardBounds.width() - f6 > 0.0f;
        boolean z3 = this.mCardBounds.height() - f6 > 0.0f;
        int iSave = canvas.save();
        RectF rectF = this.mCardBounds;
        canvas.translate(rectF.left + f5, rectF.top + f5);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z2) {
            canvas2 = canvas;
            canvas2.drawRect(0.0f, f4, this.mCardBounds.width() - f6, -this.mCornerRadius, this.mEdgeShadowPaint);
        } else {
            canvas2 = canvas;
        }
        canvas2.restoreToCount(iSave);
        int iSave2 = canvas2.save();
        RectF rectF2 = this.mCardBounds;
        canvas2.translate(rectF2.right - f5, rectF2.bottom - f5);
        canvas2.rotate(180.0f);
        canvas2.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z2) {
            canvas2.drawRect(0.0f, f4, this.mCardBounds.width() - f6, (-this.mCornerRadius) + this.mShadowSize, this.mEdgeShadowPaint);
        }
        canvas2.restoreToCount(iSave2);
        int iSave3 = canvas2.save();
        RectF rectF3 = this.mCardBounds;
        canvas2.translate(rectF3.left + f5, rectF3.bottom - f5);
        canvas2.rotate(270.0f);
        canvas2.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z3) {
            canvas2.drawRect(0.0f, f4, this.mCardBounds.height() - f6, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas2.restoreToCount(iSave3);
        int iSave4 = canvas2.save();
        RectF rectF4 = this.mCardBounds;
        canvas2.translate(rectF4.right - f5, rectF4.top + f5);
        canvas2.rotate(90.0f);
        canvas2.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z3) {
            canvas2.drawRect(0.0f, f4, this.mCardBounds.height() - f6, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas2.restoreToCount(iSave4);
    }

    private void setBackground(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        this.mBackground = colorStateList;
        this.mPaint.setColor(colorStateList.getColorForState(getState(), this.mBackground.getDefaultColor()));
    }

    public static void setRoundRectHelper(RoundRectHelper roundRectHelper) {
        sRoundRectHelper = roundRectHelper;
    }

    private void setShadowSize(float f3, float f4) {
        if (f3 < 0.0f) {
            throw new IllegalArgumentException("Invalid shadow size " + f3 + ". Must be >= 0");
        }
        if (f4 < 0.0f) {
            throw new IllegalArgumentException("Invalid max shadow size " + f4 + ". Must be >= 0");
        }
        float even = toEven(f3);
        float even2 = toEven(f4);
        if (even > even2) {
            if (!this.mPrintedShadowClipWarning) {
                this.mPrintedShadowClipWarning = true;
            }
            even = even2;
        }
        if (this.mRawShadowSize == even && this.mRawMaxShadowSize == even2) {
            return;
        }
        this.mRawShadowSize = even;
        this.mRawMaxShadowSize = even2;
        this.mShadowSize = (int) ((even * 1.5f) + this.mInsetShadow + 0.5f);
        this.mDirty = true;
        invalidateSelf();
    }

    private int toEven(float f3) {
        int i3 = (int) (f3 + 0.5f);
        return i3 % 2 == 1 ? i3 - 1 : i3;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.mDirty) {
            buildComponents(getBounds());
            this.mDirty = false;
        }
        canvas.translate(0.0f, this.mRawShadowSize / 2.0f);
        drawShadow(canvas);
        canvas.translate(0.0f, (-this.mRawShadowSize) / 2.0f);
        sRoundRectHelper.drawRoundRect(canvas, this.mCardBounds, this.mCornerRadius, this.mPaint);
    }

    public ColorStateList getColor() {
        return this.mBackground;
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public void getMaxShadowAndCornerPadding(Rect rect) {
        getPadding(rect);
    }

    public float getMaxShadowSize() {
        return this.mRawMaxShadowSize;
    }

    public float getMinHeight() {
        float f3 = this.mRawMaxShadowSize;
        return (Math.max(f3, this.mCornerRadius + this.mInsetShadow + ((f3 * 1.5f) / 2.0f)) * 2.0f) + (((this.mRawMaxShadowSize * 1.5f) + this.mInsetShadow) * 2.0f);
    }

    public float getMinWidth() {
        float f3 = this.mRawMaxShadowSize;
        return (Math.max(f3, this.mCornerRadius + this.mInsetShadow + (f3 / 2.0f)) * 2.0f) + ((this.mRawMaxShadowSize + this.mInsetShadow) * 2.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        int iCeil = (int) Math.ceil(calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        int iCeil2 = (int) Math.ceil(calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        rect.set(iCeil2, iCeil, iCeil2, iCeil);
        return true;
    }

    public float getShadowSize() {
        return this.mRawShadowSize;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList = this.mBackground;
        return (colorStateList != null && colorStateList.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mDirty = true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        ColorStateList colorStateList = this.mBackground;
        int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
        if (this.mPaint.getColor() == colorForState) {
            return false;
        }
        this.mPaint.setColor(colorForState);
        this.mDirty = true;
        invalidateSelf();
        return true;
    }

    public void setAddPaddingForCorners(boolean z2) {
        this.mAddPaddingForCorners = z2;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.mPaint.setAlpha(i3);
        this.mCornerShadowPaint.setAlpha(i3);
        this.mEdgeShadowPaint.setAlpha(i3);
    }

    public void setColor(ColorStateList colorStateList) {
        setBackground(colorStateList);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public void setCornerRadius(float f3) {
        if (f3 < 0.0f) {
            throw new IllegalArgumentException("Invalid radius " + f3 + ". Must be >= 0");
        }
        float f4 = (int) (f3 + 0.5f);
        if (this.mCornerRadius == f4) {
            return;
        }
        this.mCornerRadius = f4;
        this.mDirty = true;
        invalidateSelf();
    }

    public void setMaxShadowSize(float f3) {
        setShadowSize(this.mRawShadowSize, f3);
    }

    public void setShadowSize(float f3) {
        setShadowSize(f3, this.mRawMaxShadowSize);
    }
}