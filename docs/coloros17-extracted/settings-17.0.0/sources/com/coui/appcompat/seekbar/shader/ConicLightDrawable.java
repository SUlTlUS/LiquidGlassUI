package com.coui.appcompat.seekbar.shader;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RuntimeShader;
import android.graphics.drawable.Drawable;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0017J\b\u0010\u0017\u001a\u00020\u0014H\u0016J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u0016H\u0016J\u0012\u0010\u001a\u001a\u00020\u000f2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u001d"}, d2 = {"Lcom/coui/appcompat/seekbar/shader/ConicLightDrawable;", "Landroid/graphics/drawable/Drawable;", "Lcom/coui/appcompat/seekbar/shader/BaseShaderInterface;", "()V", "paint", "Landroid/graphics/Paint;", "params", "Lcom/coui/appcompat/seekbar/shader/ConicLightShaderParams;", "getParams", "()Lcom/coui/appcompat/seekbar/shader/ConicLightShaderParams;", "runtimeShader", "Landroid/graphics/RuntimeShader;", "getRuntimeShader", "()Landroid/graphics/RuntimeShader;", "draw", "", "canvas", "Landroid/graphics/Canvas;", "flushIfDirty", "force", "", "getOpacity", "", "isSupported", "setAlpha", "alpha", "setColorFilter", "colorFilter", "Landroid/graphics/ColorFilter;", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ConicLightDrawable extends Drawable implements BaseShaderInterface {
    private final Paint paint;
    private final ConicLightShaderParams params;

    public ConicLightDrawable() {
        ConicLightShaderParams conicLightShaderParams = new ConicLightShaderParams();
        this.params = conicLightShaderParams;
        Paint paint = new Paint(1);
        paint.setShader(conicLightShaderParams.getRuntimeShader());
        this.paint = paint;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Rect bounds = getBounds();
        Intrinsics.checkNotNullExpressionValue(bounds, "getBounds(...)");
        if (bounds.isEmpty()) {
            return;
        }
        this.params.setResolution(bounds.width(), bounds.height());
        BaseShaderInterface.DefaultImpls.flushIfDirty$default(this.params, false, 1, null);
        canvas.drawRect(bounds, this.paint);
    }

    @Override // com.coui.appcompat.seekbar.shader.BaseShaderInterface
    public void flushIfDirty(boolean force) {
        this.params.flushIfDirty(force);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    @Deprecated(message = "Deprecated in Java")
    public int getOpacity() {
        return -3;
    }

    public final ConicLightShaderParams getParams() {
        return this.params;
    }

    @Override // com.coui.appcompat.seekbar.shader.BaseShaderInterface
    public RuntimeShader getRuntimeShader() {
        return this.params.getRuntimeShader();
    }

    @Override // com.coui.appcompat.seekbar.shader.BaseShaderInterface
    public boolean isSupported() {
        return this.params.isSupported();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        invalidateSelf();
    }
}