package com.coui.appcompat.causticshadow;

import android.graphics.Color;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.coui.appcompat.COUIEffectVersionUtil;
import com.oplus.view.material.OplusMaterialUtil;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004H\u0002J\u0017\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0001¢\u0006\u0002\b\u0015J\u0017\u0010\u0016\u001a\u00020\u000f2\b\b\u0001\u0010\u0017\u001a\u00020\u000fH\u0001¢\u0006\u0002\b\u0018J!\u0010\u0019\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\b\u0001\u0010\u0017\u001a\u00020\u000fH\u0001¢\u0006\u0002\b\u001aJ7\u0010\u001b\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u001f\u001a\u00020\u000fH\u0001¢\u0006\u0002\b JQ\u0010!\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\"\u001a\u00020\u00042\b\b\u0002\u0010#\u001a\u00020\u00042\b\b\u0002\u0010$\u001a\u00020\u00042\b\b\u0002\u0010%\u001a\u00020\u00042\b\b\u0002\u0010&\u001a\u00020\u00042\b\b\u0002\u0010'\u001a\u00020\u0004H\u0001¢\u0006\u0002\b(R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lcom/coui/appcompat/causticshadow/COUICausticShadowEffect;", "", "()V", "DEFAULT_FACTOR_0", "", "DEFAULT_FACTOR_1", "DEFAULT_LUMINANCE_INTENSITY", "DEFAULT_LUMINANCE_THRESHOLD", "DEFAULT_SPEED", "LUMA_BLUE_FACTOR", "LUMA_GREEN_FACTOR", "LUMA_RED_FACTOR", "LUMA_TARGET_BRIGHTNESS", "MAX_HEX_COLOR_FLOAT", "clampRGB", "", "value", "clearCausticShadow", "", "view", "Landroid/view/View;", "clearCausticShadow$coui_support_effect_release", "convertCausticShadowColor", TypedValues.Custom.S_COLOR, "convertCausticShadowColor$coui_support_effect_release", "setOutlineCausticShadowColor", "setOutlineCausticShadowColor$coui_support_effect_release", "setOutlineCausticShadowLayout", "leftOffset", "topOffset", "rightOffset", "bottomOffset", "setOutlineCausticShadowLayout$coui_support_effect_release", "setOutlineCausticShadowParams", "sdfWidth", "factor0", "factor1", "speed", "luminanceThreshold", "luminanceIntensity", "setOutlineCausticShadowParams$coui_support_effect_release", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUICausticShadowEffect {
    private static final float DEFAULT_FACTOR_0 = 0.6f;
    private static final float DEFAULT_FACTOR_1 = 0.6f;
    private static final float DEFAULT_LUMINANCE_INTENSITY = 0.6f;
    private static final float DEFAULT_LUMINANCE_THRESHOLD = 0.45f;
    private static final float DEFAULT_SPEED = 18.0f;
    public static final COUICausticShadowEffect INSTANCE = new COUICausticShadowEffect();
    private static final float LUMA_BLUE_FACTOR = 0.114f;
    private static final float LUMA_GREEN_FACTOR = 0.587f;
    private static final float LUMA_RED_FACTOR = 0.299f;
    private static final float LUMA_TARGET_BRIGHTNESS = 80.0f;
    private static final float MAX_HEX_COLOR_FLOAT = 255.0f;

    private COUICausticShadowEffect() {
    }

    private final int clampRGB(float value) {
        return (int) Math.max(0.0f, Math.min(value, 255.0f));
    }

    @JvmStatic
    public static final void clearCausticShadow$coui_support_effect_release(View view) {
        setOutlineCausticShadowColor$coui_support_effect_release(view, 0);
    }

    @JvmStatic
    public static final int convertCausticShadowColor$coui_support_effect_release(int color) {
        int iRed = Color.red(color);
        float f3 = iRed;
        float fGreen = Color.green(color);
        float fBlue = Color.blue(color);
        float f4 = (LUMA_RED_FACTOR * f3) + (LUMA_GREEN_FACTOR * fGreen) + (LUMA_BLUE_FACTOR * fBlue);
        if (f4 == 0.0f) {
            return Color.parseColor("#828282");
        }
        if (f4 == 255.0f) {
            return Color.parseColor("#0B0B0B");
        }
        COUICausticShadowEffect cOUICausticShadowEffect = INSTANCE;
        return Color.rgb(cOUICausticShadowEffect.clampRGB((f3 * LUMA_TARGET_BRIGHTNESS) / f4), cOUICausticShadowEffect.clampRGB((fGreen * LUMA_TARGET_BRIGHTNESS) / f4), cOUICausticShadowEffect.clampRGB((fBlue * LUMA_TARGET_BRIGHTNESS) / f4));
    }

    @JvmStatic
    public static final void setOutlineCausticShadowColor$coui_support_effect_release(View view, int color) {
        if (!COUIEffectVersionUtil.isCausticShadowVersion() || view == null) {
            return;
        }
        OplusMaterialUtil.setOutlineCausticShadowColor(view, color);
    }

    @JvmStatic
    public static final void setOutlineCausticShadowLayout$coui_support_effect_release(View view, int leftOffset, int topOffset, int rightOffset, int bottomOffset) {
        if (!COUIEffectVersionUtil.isCausticShadowVersion() || view == null) {
            return;
        }
        OplusMaterialUtil.setOutlineCausticShadowLayout(view, leftOffset, topOffset, rightOffset, bottomOffset);
    }

    @JvmStatic
    public static final void setOutlineCausticShadowParams$coui_support_effect_release(View view, float sdfWidth, float factor0, float factor1, float speed, float luminanceThreshold, float luminanceIntensity) {
        if (!COUIEffectVersionUtil.isCausticShadowVersion() || view == null) {
            return;
        }
        OplusMaterialUtil.setOutlineCausticShadowParams(view, sdfWidth, factor0, factor1, speed, luminanceThreshold, luminanceIntensity);
    }

    public static /* synthetic */ void setOutlineCausticShadowParams$coui_support_effect_release$default(View view, float f3, float f4, float f5, float f6, float f7, float f8, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            f4 = 0.6f;
        }
        if ((i3 & 8) != 0) {
            f5 = 0.6f;
        }
        if ((i3 & 16) != 0) {
            f6 = 18.0f;
        }
        if ((i3 & 32) != 0) {
            f7 = 0.45f;
        }
        if ((i3 & 64) != 0) {
            f8 = 0.6f;
        }
        setOutlineCausticShadowParams$coui_support_effect_release(view, f3, f4, f5, f6, f7, f8);
    }
}