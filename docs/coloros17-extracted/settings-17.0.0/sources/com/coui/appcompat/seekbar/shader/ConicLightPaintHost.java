package com.coui.appcompat.seekbar.shader;

import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RuntimeShader;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014J\u0010\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0018H\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a"}, d2 = {"Lcom/coui/appcompat/seekbar/shader/ConicLightPaintHost;", "Lcom/coui/appcompat/seekbar/shader/BaseShaderInterface;", "()V", "paint", "Landroid/graphics/Paint;", "getPaint", "()Landroid/graphics/Paint;", "params", "Lcom/coui/appcompat/seekbar/shader/ConicLightShaderParams;", "getParams", "()Lcom/coui/appcompat/seekbar/shader/ConicLightShaderParams;", "runtimeShader", "Landroid/graphics/RuntimeShader;", "getRuntimeShader", "()Landroid/graphics/RuntimeShader;", "draw", "", "canvas", "Landroid/graphics/Canvas;", "width", "", "height", "flushIfDirty", "force", "", "isSupported", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ConicLightPaintHost implements BaseShaderInterface {
    private final Paint paint;
    private final ConicLightShaderParams params;

    public ConicLightPaintHost() {
        ConicLightShaderParams conicLightShaderParams = new ConicLightShaderParams();
        this.params = conicLightShaderParams;
        Paint paint = new Paint(1);
        paint.setShader(conicLightShaderParams.getRuntimeShader());
        paint.setBlendMode(BlendMode.SCREEN);
        this.paint = paint;
    }

    public final void draw(Canvas canvas, float width, float height) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        this.params.setResolution(width, height);
        BaseShaderInterface.DefaultImpls.flushIfDirty$default(this.params, false, 1, null);
        canvas.drawRect(0.0f, 0.0f, width, height, this.paint);
    }

    @Override // com.coui.appcompat.seekbar.shader.BaseShaderInterface
    public void flushIfDirty(boolean force) {
        this.params.flushIfDirty(force);
    }

    public final Paint getPaint() {
        return this.paint;
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
}