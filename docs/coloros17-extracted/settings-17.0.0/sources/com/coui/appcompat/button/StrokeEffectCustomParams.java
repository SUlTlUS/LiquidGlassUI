package com.coui.appcompat.button;

import android.graphics.BlendMode;
import com.android.settings.inputmethod.UserDictionaryAddWordContents;
import com.oplus.settings.feature.language.localepicker.RegionPickerUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BA\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003¢\u0006\u0002\u0010\tJ\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003JE\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\n\u0010$\u001a\u0004\u0018\u00010\u000bH\u0007J\t\u0010%\u001a\u00020&HÖ\u0001J\u0012\u0010'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010\u000bH\u0007J\t\u0010*\u001a\u00020+HÖ\u0001R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000fR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000fR\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\r\"\u0004\b\u0015\u0010\u000fR\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\r\"\u0004\b\u0017\u0010\u000fR\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\r\"\u0004\b\u0019\u0010\u000f¨\u0006,"}, d2 = {"Lcom/coui/appcompat/button/StrokeEffectCustomParams;", "", "lineAlpha", "", "lineWidth", "lineAngle", "shadowFadeIn", "shadowFadeOut", "shadowFadeScale", "(FFFFFF)V", "blendMode", "Landroid/graphics/BlendMode;", "getLineAlpha", "()F", "setLineAlpha", "(F)V", "getLineAngle", "setLineAngle", "getLineWidth", "setLineWidth", "getShadowFadeIn", "setShadowFadeIn", "getShadowFadeOut", "setShadowFadeOut", "getShadowFadeScale", "setShadowFadeScale", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", RegionPickerUtils.OTHER_SYSTEM_REGION_KEY, "getBlendMode", "hashCode", "", "setBlendMode", "", UserDictionaryAddWordContents.EXTRA_MODE, "toString", "", "coui-support-core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class StrokeEffectCustomParams {
    private BlendMode blendMode;
    private float lineAlpha;
    private float lineAngle;
    private float lineWidth;
    private float shadowFadeIn;
    private float shadowFadeOut;
    private float shadowFadeScale;

    public StrokeEffectCustomParams() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 63, null);
    }

    public static /* synthetic */ StrokeEffectCustomParams copy$default(StrokeEffectCustomParams strokeEffectCustomParams, float f3, float f4, float f5, float f6, float f7, float f8, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            f3 = strokeEffectCustomParams.lineAlpha;
        }
        if ((i3 & 2) != 0) {
            f4 = strokeEffectCustomParams.lineWidth;
        }
        if ((i3 & 4) != 0) {
            f5 = strokeEffectCustomParams.lineAngle;
        }
        if ((i3 & 8) != 0) {
            f6 = strokeEffectCustomParams.shadowFadeIn;
        }
        if ((i3 & 16) != 0) {
            f7 = strokeEffectCustomParams.shadowFadeOut;
        }
        if ((i3 & 32) != 0) {
            f8 = strokeEffectCustomParams.shadowFadeScale;
        }
        float f9 = f7;
        float f10 = f8;
        return strokeEffectCustomParams.copy(f3, f4, f5, f6, f9, f10);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getLineAlpha() {
        return this.lineAlpha;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getLineWidth() {
        return this.lineWidth;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getLineAngle() {
        return this.lineAngle;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float getShadowFadeIn() {
        return this.shadowFadeIn;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float getShadowFadeOut() {
        return this.shadowFadeOut;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getShadowFadeScale() {
        return this.shadowFadeScale;
    }

    public final StrokeEffectCustomParams copy(float lineAlpha, float lineWidth, float lineAngle, float shadowFadeIn, float shadowFadeOut, float shadowFadeScale) {
        return new StrokeEffectCustomParams(lineAlpha, lineWidth, lineAngle, shadowFadeIn, shadowFadeOut, shadowFadeScale);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StrokeEffectCustomParams)) {
            return false;
        }
        StrokeEffectCustomParams strokeEffectCustomParams = (StrokeEffectCustomParams) other;
        return Float.compare(this.lineAlpha, strokeEffectCustomParams.lineAlpha) == 0 && Float.compare(this.lineWidth, strokeEffectCustomParams.lineWidth) == 0 && Float.compare(this.lineAngle, strokeEffectCustomParams.lineAngle) == 0 && Float.compare(this.shadowFadeIn, strokeEffectCustomParams.shadowFadeIn) == 0 && Float.compare(this.shadowFadeOut, strokeEffectCustomParams.shadowFadeOut) == 0 && Float.compare(this.shadowFadeScale, strokeEffectCustomParams.shadowFadeScale) == 0;
    }

    public final BlendMode getBlendMode() {
        return this.blendMode;
    }

    public final float getLineAlpha() {
        return this.lineAlpha;
    }

    public final float getLineAngle() {
        return this.lineAngle;
    }

    public final float getLineWidth() {
        return this.lineWidth;
    }

    public final float getShadowFadeIn() {
        return this.shadowFadeIn;
    }

    public final float getShadowFadeOut() {
        return this.shadowFadeOut;
    }

    public final float getShadowFadeScale() {
        return this.shadowFadeScale;
    }

    public int hashCode() {
        return (((((((((Float.hashCode(this.lineAlpha) * 31) + Float.hashCode(this.lineWidth)) * 31) + Float.hashCode(this.lineAngle)) * 31) + Float.hashCode(this.shadowFadeIn)) * 31) + Float.hashCode(this.shadowFadeOut)) * 31) + Float.hashCode(this.shadowFadeScale);
    }

    public final void setBlendMode(BlendMode mode) {
        this.blendMode = mode;
    }

    public final void setLineAlpha(float f3) {
        this.lineAlpha = f3;
    }

    public final void setLineAngle(float f3) {
        this.lineAngle = f3;
    }

    public final void setLineWidth(float f3) {
        this.lineWidth = f3;
    }

    public final void setShadowFadeIn(float f3) {
        this.shadowFadeIn = f3;
    }

    public final void setShadowFadeOut(float f3) {
        this.shadowFadeOut = f3;
    }

    public final void setShadowFadeScale(float f3) {
        this.shadowFadeScale = f3;
    }

    public String toString() {
        return "StrokeEffectCustomParams(lineAlpha=" + this.lineAlpha + ", lineWidth=" + this.lineWidth + ", lineAngle=" + this.lineAngle + ", shadowFadeIn=" + this.shadowFadeIn + ", shadowFadeOut=" + this.shadowFadeOut + ", shadowFadeScale=" + this.shadowFadeScale + ')';
    }

    public StrokeEffectCustomParams(float f3, float f4, float f5, float f6, float f7, float f8) {
        this.lineAlpha = f3;
        this.lineWidth = f4;
        this.lineAngle = f5;
        this.shadowFadeIn = f6;
        this.shadowFadeOut = f7;
        this.shadowFadeScale = f8;
    }

    public /* synthetic */ StrokeEffectCustomParams(float f3, float f4, float f5, float f6, float f7, float f8, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0.0f : f3, (i3 & 2) != 0 ? 0.0f : f4, (i3 & 4) != 0 ? 0.0f : f5, (i3 & 8) != 0 ? 0.0f : f6, (i3 & 16) != 0 ? 0.0f : f7, (i3 & 32) != 0 ? 0.0f : f8);
    }
}