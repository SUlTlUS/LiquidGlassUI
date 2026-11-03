package com.coui.appcompat.toolbar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0002\u0010\u0007J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0003H\u0017J\u0010\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0017H\u0014J\u0010\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u0003H\u0016J\u0012\u0010\u0019\u001a\u00020\u00112\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\u0018\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u000fH\u0002R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/coui/appcompat/toolbar/GradientBackgroundDrawable;", "Landroid/graphics/drawable/Drawable;", "baseColor", "", "positions", "", "alphas", "(I[F[F)V", "mGradient", "Landroid/graphics/LinearGradient;", "mPaint", "Landroid/graphics/Paint;", "applyAlphaToColor", TypedValues.Custom.S_COLOR, "alpha", "", "draw", "", "canvas", "Landroid/graphics/Canvas;", "getOpacity", "onBoundsChange", "bounds", "Landroid/graphics/Rect;", "setAlpha", "setColorFilter", "colorFilter", "Landroid/graphics/ColorFilter;", "updateGradient", "width", "height", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class GradientBackgroundDrawable extends Drawable {
    private final float[] alphas;
    private final int baseColor;
    private LinearGradient mGradient;
    private final Paint mPaint;
    private final float[] positions;

    public GradientBackgroundDrawable(int i3, float[] positions, float[] alphas) {
        Intrinsics.checkNotNullParameter(positions, "positions");
        Intrinsics.checkNotNullParameter(alphas, "alphas");
        this.baseColor = i3;
        this.positions = positions;
        this.alphas = alphas;
        this.mPaint = new Paint(1);
    }

    private final int applyAlphaToColor(int color, float alpha) {
        return (RangesKt.coerceIn((int) (alpha * 255), 0, 255) << 24) | (color & 16777215);
    }

    private final void updateGradient(float width, float height) {
        int length = this.positions.length;
        int[] iArr = new int[length];
        for (int i3 = 0; i3 < length; i3++) {
            iArr[i3] = applyAlphaToColor(this.baseColor, this.alphas[i3]);
        }
        LinearGradient linearGradient = new LinearGradient(0.0f, height, 0.0f, 0.0f, iArr, this.positions, Shader.TileMode.CLAMP);
        this.mGradient = linearGradient;
        this.mPaint.setShader(linearGradient);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Rect bounds = getBounds();
        Intrinsics.checkNotNullExpressionValue(bounds, "getBounds(...)");
        if (bounds.isEmpty()) {
            return;
        }
        if (this.mGradient == null) {
            updateGradient(bounds.width(), bounds.height());
        }
        canvas.drawRect(bounds, this.mPaint);
    }

    @Override // android.graphics.drawable.Drawable
    @Deprecated(message = "Deprecated in Java", replaceWith = @ReplaceWith(expression = "android.graphics.PixelFormat.TRANSLUCENT", imports = {"android.graphics.PixelFormat"}))
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        Intrinsics.checkNotNullParameter(bounds, "bounds");
        super.onBoundsChange(bounds);
        if (bounds.isEmpty()) {
            return;
        }
        updateGradient(bounds.width(), bounds.height());
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        int iCoerceIn = RangesKt.coerceIn(alpha, 0, 255);
        if (this.mPaint.getAlpha() == iCoerceIn) {
            return;
        }
        this.mPaint.setAlpha(iCoerceIn);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}