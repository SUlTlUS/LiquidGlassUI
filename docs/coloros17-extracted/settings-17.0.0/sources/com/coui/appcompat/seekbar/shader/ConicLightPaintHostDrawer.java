package com.coui.appcompat.seekbar.shader;

import android.graphics.Canvas;
import android.text.TextUtils;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0007\b\u0007\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\u0005¢\u0006\u0002\u0010\u0002J0\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\fH\u0016J\b\u0010\u0010\u001a\u00020\u0004H\u0016J\u0010\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\fH\u0016J\u0010\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\fH\u0016J\u0010\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\fH\u0016J\u0010\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\fH\u0016J\u0010\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\fH\u0016J\u0010\u0010\u001a\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\fH\u0016J\u0010\u0010\u001b\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\fH\u0016J\u0010\u0010\u001c\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/coui/appcompat/seekbar/shader/ConicLightPaintHostDrawer;", "Lcom/coui/appcompat/seekbar/shader/IConicLightDrawer;", "()V", "mIsRtlLocale", "", "paintHost", "Lcom/coui/appcompat/seekbar/shader/ConicLightPaintHost;", "draw", "", "canvas", "Landroid/graphics/Canvas;", "width", "", "height", "process", "rectWidth", "isSupported", "setAlpha1", "value", "setAlpha2", "setConeK", "setConicLightColor", TypedValues.Custom.S_COLOR, "", "setHeight", "setHeightFalloff", "setRtl", "setSecondConeK", "setW0", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ConicLightPaintHostDrawer implements IConicLightDrawer {
    private static final int COLOR_CHANNEL_MASK = 255;
    private static final int COLOR_SHIFT_G = 8;
    private static final int COLOR_SHIFT_R = 16;
    private static final float DEFAULT_COLOR_B = 96.0f;
    private static final float DEFAULT_COLOR_G = 119.0f;
    private static final float DEFAULT_COLOR_R = 248.0f;
    private static final float DEFAULT_HEIGHT = 0.1f;
    private static final float DEFAULT_SPOT_RADIUS = 500.0f;
    private static final float DEFAULT_THRESHOLD = 0.2f;
    private static final float DEFAULT_W0 = 0.008f;
    private final boolean mIsRtlLocale;
    private final ConicLightPaintHost paintHost;

    public ConicLightPaintHostDrawer() {
        ConicLightPaintHost conicLightPaintHost = new ConicLightPaintHost();
        this.paintHost = conicLightPaintHost;
        this.mIsRtlLocale = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
        conicLightPaintHost.getParams().setAlpha(1.0f);
        conicLightPaintHost.getParams().setStartColor(DEFAULT_COLOR_R, DEFAULT_COLOR_G, DEFAULT_COLOR_B);
        conicLightPaintHost.getParams().setEndColor(DEFAULT_COLOR_R, DEFAULT_COLOR_G, DEFAULT_COLOR_B);
        conicLightPaintHost.getParams().setSpotRadius(DEFAULT_SPOT_RADIUS);
        conicLightPaintHost.getParams().setW0(DEFAULT_W0);
        conicLightPaintHost.getParams().setAxialFalloff(2.0f);
        conicLightPaintHost.getParams().setRadialFalloff(1.0f);
        conicLightPaintHost.getParams().setLightBlendMode(1);
        conicLightPaintHost.getParams().setThreshold(0.2f);
        conicLightPaintHost.getParams().setHeight(0.1f);
        conicLightPaintHost.getParams().setHeightFalloff(0.0f);
        conicLightPaintHost.getParams().setConeK(0.0f);
        conicLightPaintHost.getParams().setSecondConeK(0.0f);
        conicLightPaintHost.getParams().setAlpha1(0.0f);
        conicLightPaintHost.getParams().setAlpha2(0.0f);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void draw(Canvas canvas, float width, float height, float process, float rectWidth) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        this.paintHost.getParams().setProcess(process);
        this.paintHost.getParams().setRectWidth(rectWidth);
        this.paintHost.draw(canvas, width, height);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public boolean isSupported() {
        return this.paintHost.isSupported();
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setAlpha1(float value) {
        this.paintHost.getParams().setAlpha1(value);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setAlpha2(float value) {
        this.paintHost.getParams().setAlpha2(value);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setConeK(float value) {
        this.paintHost.getParams().setConeK(value);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setConicLightColor(int color) {
        float f3 = (color >> 16) & 255;
        float f4 = (color >> 8) & 255;
        float f5 = color & 255;
        this.paintHost.getParams().setStartColor(f3, f4, f5);
        this.paintHost.getParams().setEndColor(f3, f4, f5);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setHeight(float value) {
        this.paintHost.getParams().setHeight(value);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setHeightFalloff(float value) {
        this.paintHost.getParams().setHeightFalloff(value);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setRtl(float value) {
        this.paintHost.getParams().setRtl(value);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setSecondConeK(float value) {
        this.paintHost.getParams().setSecondConeK(value);
    }

    @Override // com.coui.appcompat.seekbar.shader.IConicLightDrawer
    public void setW0(float value) {
        this.paintHost.getParams().setW0(value);
    }
}