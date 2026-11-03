package com.coui.appcompat;

import android.content.Context;
import android.graphics.RenderEffect;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.coui.appcompat.causticshadow.COUICausticShadowEffect;
import com.oplus.view.OplusViewBackgroundRenderEffect;
import com.oplus.view.material.OplusMaterialBaseParams;
import com.oplus.view.material.OplusMaterialCornerParams;
import com.oplus.view.material.OplusMaterialMetaBallParams;
import com.oplus.view.material.OplusMaterialUtil;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0011\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\n\u001a\u00020\u0004H\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0012\u0010\u000f\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0007J\u0012\u0010\u0010\u001a\u00020\u00112\b\b\u0001\u0010\u0012\u001a\u00020\u0011H\u0007J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001cH\u0007J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u001dH\u0007J\u0018\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u001dH\u0007J\b\u0010\u001f\u001a\u00020\u0004H\u0007J\b\u0010 \u001a\u00020\u0004H\u0007J\b\u0010!\u001a\u00020\u0004H\u0007J\u001c\u0010\"\u001a\u00020\f2\b\u0010#\u001a\u0004\u0018\u00010$2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0007J\u001a\u0010%\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u001b\u001a\u0004\u0018\u00010&H\u0007J\u001a\u0010'\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u001b\u001a\u0004\u0018\u00010(H\u0007J\u0016\u0010)\u001a\u00020\f2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u000e0+H\u0007J\u001e\u0010)\u001a\u00020\f2\u0006\u0010,\u001a\u00020\u000e2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u000e0+H\u0007J\u001a\u0010.\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u001b\u001a\u0004\u0018\u00010/H\u0007J\u001c\u00100\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\b\u0001\u0010\u0012\u001a\u00020\u0011H\u0007J2\u00101\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u00102\u001a\u00020\u00112\u0006\u00103\u001a\u00020\u00112\u0006\u00104\u001a\u00020\u00112\u0006\u00105\u001a\u00020\u0011H\u0007J\u001a\u00106\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u00107\u001a\u000208H\u0007JB\u00106\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u0002082\u0006\u0010:\u001a\u0002082\u0006\u0010;\u001a\u0002082\u0006\u0010<\u001a\u0002082\u0006\u0010=\u001a\u000208H\u0007J\u0018\u0010>\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020?H\u0007J2\u0010>\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010@\u001a\u00020?2\u0006\u0010A\u001a\u00020?2\u0006\u0010B\u001a\u00020?2\b\b\u0001\u0010C\u001a\u000208H\u0007J\u0018\u0010>\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u0018H\u0007J\u0018\u0010D\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u001cH\u0007J4\u0010D\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010@\u001a\u00020\u001c2\u0006\u0010A\u001a\u00020\u001c2\b\u0010E\u001a\u0004\u0018\u00010\u001a2\b\b\u0001\u0010C\u001a\u000208H\u0007J\u0018\u0010D\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u001dH\u0007J\u0011\u0010\u000b\u001a\u00020\u000e*\u00020\u000eH\u0007¢\u0006\u0002\bFJ\u0011\u0010\u000f\u001a\u00020\u000e*\u00020\u000eH\u0007¢\u0006\u0002\bGJ\u001b\u0010%\u001a\u00020\u000e*\u00020\u000e2\b\u0010\u001b\u001a\u0004\u0018\u00010&H\u0007¢\u0006\u0002\bHJ\u001b\u0010'\u001a\u00020\u000e*\u00020\u000e2\b\u0010\u001b\u001a\u0004\u0018\u00010(H\u0007¢\u0006\u0002\bIJ\u001b\u0010.\u001a\u00020\u000e*\u00020\u000e2\b\u0010\u001b\u001a\u0004\u0018\u00010/H\u0007¢\u0006\u0002\bJJ\u001b\u00100\u001a\u00020\u000e*\u00020\u000e2\b\b\u0001\u0010\u0012\u001a\u00020\u0011H\u0007¢\u0006\u0002\bKJ1\u00101\u001a\u00020\u000e*\u00020\u000e2\u0006\u00102\u001a\u00020\u00112\u0006\u00103\u001a\u00020\u00112\u0006\u00104\u001a\u00020\u00112\u0006\u00105\u001a\u00020\u0011H\u0007¢\u0006\u0002\bLJ\u0019\u00106\u001a\u00020\u000e*\u00020\u000e2\u0006\u00107\u001a\u000208H\u0007¢\u0006\u0002\bMJA\u00106\u001a\u00020\u000e*\u00020\u000e2\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u0002082\u0006\u0010:\u001a\u0002082\u0006\u0010;\u001a\u0002082\u0006\u0010<\u001a\u0002082\u0006\u0010=\u001a\u000208H\u0007¢\u0006\u0002\bMJ\u0019\u0010>\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u001b\u001a\u00020?H\u0007¢\u0006\u0002\bNJ3\u0010>\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010@\u001a\u00020?2\u0006\u0010A\u001a\u00020?2\u0006\u0010B\u001a\u00020?2\b\b\u0001\u0010C\u001a\u000208H\u0007¢\u0006\u0002\bNJ\u0019\u0010>\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u0018H\u0007¢\u0006\u0002\bNJ\u0019\u0010D\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u001cH\u0007¢\u0006\u0002\bOJ5\u0010D\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010@\u001a\u00020\u001c2\u0006\u0010A\u001a\u00020\u001c2\b\u0010E\u001a\u0004\u0018\u00010\u001a2\b\b\u0001\u0010C\u001a\u000208H\u0007¢\u0006\u0002\bOJ\u0019\u0010D\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u001dH\u0007¢\u0006\u0002\bOR\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000¨\u0006P"}, d2 = {"Lcom/coui/appcompat/COUIMaterialEffect;", "", "()V", "DEBUG", "", "getDEBUG$coui_support_effect_release", "()Z", "MATERIAL_WARRING_MSG", "", "TAG", "canApplyBackgroundRenderEffect", "clearBlurEffect", "", "view", "Landroid/view/View;", "clearCausticShadow", "convertCausticShadowColor", "", TypedValues.Custom.S_COLOR, "getBlurParamsByType", "Lcom/coui/appcompat/COUIMaterialBlurEffect$BlurEffectParams;", "context", "Landroid/content/Context;", "type", "Lcom/coui/appcompat/COUIMaterialBlurEffect$BlurEffectType;", "getStrokeEffectParamsHolder", "Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion$StrokeEffectParamsHolder;", "params", "Lcom/coui/appcompat/COUIMaterialStrokeEffect$Companion$StrokeEffectParams;", "Lcom/coui/appcompat/COUIMaterialStrokeEffect$StrokeEffectType;", "getStrokeParamsByType", "isCausticShadowVersion", "isMaterialVersion", "isRuntimeShaderVersion", "setBackgroundRenderEffect", "effect", "Landroid/graphics/RenderEffect;", "setBaseParams", "Lcom/oplus/view/material/OplusMaterialBaseParams;", "setCornerParams", "Lcom/oplus/view/material/OplusMaterialCornerParams;", "setMetaBallChain", "views", "Landroid/util/ArraySet;", "subjectView", "observerViews", "setMetaBallParams", "Lcom/oplus/view/material/OplusMaterialMetaBallParams;", "setOutlineCausticShadowColor", "setOutlineCausticShadowLayout", "leftOffset", "topOffset", "rightOffset", "bottomOffset", "setOutlineCausticShadowParams", "sdfWidth", "", "factor0", "factor1", "speed", "luminanceThreshold", "luminanceIntensity", "setViewBlurEffect", "Lcom/coui/appcompat/COUIMaterialBlurEffect$BaseBlurEffectParams;", "startParams", "endParams", "blendModeParams", "progress", "setViewStrokeEffect", "holder", "clearBlurEffectExt", "clearCausticShadowExt", "setBaseParamsExt", "setCornerParamsExt", "setMetaBallParamsExt", "setOutlineCausticShadowColorExt", "setOutlineCausticShadowLayoutExt", "setOutlineCausticShadowParamsExt", "setViewBlurEffectExt", "setViewStrokeEffectExt", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIMaterialEffect {
    private static final String MATERIAL_WARRING_MSG = "Material version check failed!";
    public static final COUIMaterialEffect INSTANCE = new COUIMaterialEffect();
    public static final String TAG = "COUIMaterialEffect";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    private COUIMaterialEffect() {
    }

    @JvmStatic
    public static final boolean canApplyBackgroundRenderEffect() {
        return COUIEffectVersionUtil.checkOPlusViewSubSDK(33, 34) && !COUIMaterialStateManager.INSTANCE.isLowAnimationLevel();
    }

    @JvmStatic
    public static final void clearBlurEffect(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        if (COUIEffectVersionUtil.isMaterialVersion()) {
            COUIMaterialBlurEffect.INSTANCE.clearBlurEffect$coui_support_effect_release(view);
        } else if (DEBUG) {
            Log.w(TAG, MATERIAL_WARRING_MSG);
        }
    }

    @JvmStatic
    public static final void clearCausticShadow(View view) {
        COUICausticShadowEffect.clearCausticShadow$coui_support_effect_release(view);
    }

    @JvmStatic
    public static final int convertCausticShadowColor(int color) {
        return COUICausticShadowEffect.convertCausticShadowColor$coui_support_effect_release(color);
    }

    @JvmStatic
    public static final COUIMaterialBlurEffect.BlurEffectParams getBlurParamsByType(Context context, COUIMaterialBlurEffect.BlurEffectType type) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(type, "type");
        return COUIMaterialBlurEffect.INSTANCE.getBlurParamsByType$coui_support_effect_release(context, type);
    }

    @JvmStatic
    public static final COUIMaterialStrokeEffect.Companion.StrokeEffectParamsHolder getStrokeEffectParamsHolder(Context context, COUIMaterialStrokeEffect.StrokeEffectType type) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(type, "type");
        return COUIMaterialStrokeEffect.INSTANCE.getStrokeEffectParamsHolder$coui_support_effect_release(context, type);
    }

    @JvmStatic
    public static final COUIMaterialStrokeEffect.Companion.StrokeEffectParams getStrokeParamsByType(Context context, COUIMaterialStrokeEffect.StrokeEffectType type) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(type, "type");
        return COUIMaterialStrokeEffect.INSTANCE.getStrokeParamsByType$coui_support_effect_release(context, type);
    }

    @JvmStatic
    public static final boolean isCausticShadowVersion() {
        return COUIEffectVersionUtil.isCausticShadowVersion();
    }

    @JvmStatic
    public static final boolean isMaterialVersion() {
        return COUIEffectVersionUtil.isMaterialVersion();
    }

    @JvmStatic
    public static final boolean isRuntimeShaderVersion() {
        return COUIEffectVersionUtil.isRuntimeShaderVersion();
    }

    @JvmStatic
    public static final void setBackgroundRenderEffect(RenderEffect effect, View view) {
        String message;
        String message2;
        if (view == null || !canApplyBackgroundRenderEffect()) {
            return;
        }
        try {
            OplusViewBackgroundRenderEffect.setBackgroundRenderEffect(effect, view);
        } catch (NoClassDefFoundError e3) {
            if (!DEBUG || (message2 = e3.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message2);
        } catch (NoSuchMethodError e4) {
            if (!DEBUG || (message = e4.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message);
        }
    }

    @JvmStatic
    public static final void setBaseParams(View view, OplusMaterialBaseParams params) {
        String message;
        String message2;
        Intrinsics.checkNotNullParameter(view, "view");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        try {
            OplusMaterialUtil.setBaseParams(view, params);
        } catch (NoClassDefFoundError e3) {
            if (!DEBUG || (message2 = e3.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message2);
        } catch (NoSuchMethodError e4) {
            if (!DEBUG || (message = e4.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message);
        }
    }

    @JvmStatic
    public static final void setCornerParams(View view, OplusMaterialCornerParams params) {
        String message;
        String message2;
        Intrinsics.checkNotNullParameter(view, "view");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        try {
            OplusMaterialUtil.setCornerParams(view, params);
        } catch (NoClassDefFoundError e3) {
            if (!DEBUG || (message2 = e3.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message2);
        } catch (NoSuchMethodError e4) {
            if (!DEBUG || (message = e4.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message);
        }
    }

    @JvmStatic
    public static final void setMetaBallChain(ArraySet<View> views) {
        String message;
        String message2;
        Intrinsics.checkNotNullParameter(views, "views");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        try {
            OplusMaterialUtil.setMetaBallChain(views);
        } catch (NoClassDefFoundError e3) {
            if (!DEBUG || (message2 = e3.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message2);
        } catch (NoSuchMethodError e4) {
            if (!DEBUG || (message = e4.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message);
        }
    }

    @JvmStatic
    public static final void setMetaBallParams(View view, OplusMaterialMetaBallParams params) {
        String message;
        String message2;
        Intrinsics.checkNotNullParameter(view, "view");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        try {
            OplusMaterialUtil.setMetaBallParams(view, params);
        } catch (NoClassDefFoundError e3) {
            if (!DEBUG || (message2 = e3.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message2);
        } catch (NoSuchMethodError e4) {
            if (!DEBUG || (message = e4.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message);
        }
    }

    @JvmStatic
    public static final void setOutlineCausticShadowColor(View view, int color) {
        COUICausticShadowEffect.setOutlineCausticShadowColor$coui_support_effect_release(view, color);
    }

    @JvmStatic
    public static final void setOutlineCausticShadowLayout(View view, int leftOffset, int topOffset, int rightOffset, int bottomOffset) {
        COUICausticShadowEffect.setOutlineCausticShadowLayout$coui_support_effect_release(view, leftOffset, topOffset, rightOffset, bottomOffset);
    }

    @JvmStatic
    public static final void setOutlineCausticShadowParams(View view, float sdfWidth) {
        COUICausticShadowEffect.setOutlineCausticShadowParams$coui_support_effect_release$default(view, sdfWidth, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 124, null);
    }

    @JvmStatic
    public static final void setViewBlurEffect(View view, COUIMaterialBlurEffect.BlurEffectType params) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(params, "params");
        if (COUIEffectVersionUtil.isMaterialVersion()) {
            COUIMaterialBlurEffect.INSTANCE.setViewBlurEffect$coui_support_effect_release(view, params);
        } else if (DEBUG) {
            Log.w(TAG, MATERIAL_WARRING_MSG);
        }
    }

    @JvmStatic
    public static final void setViewStrokeEffect(View view, COUIMaterialStrokeEffect.StrokeEffectType type) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(type, "type");
        if (COUIEffectVersionUtil.isMaterialVersion()) {
            COUIMaterialStrokeEffect.INSTANCE.setViewStrokeEffect$coui_support_effect_release(view, type);
        } else if (DEBUG) {
            Log.w(TAG, MATERIAL_WARRING_MSG);
        }
    }

    public final View clearBlurEffectExt(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        clearBlurEffect(view);
        return view;
    }

    public final View clearCausticShadowExt(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        clearCausticShadow(view);
        return view;
    }

    public final boolean getDEBUG$coui_support_effect_release() {
        return DEBUG;
    }

    public final View setBaseParamsExt(View view, OplusMaterialBaseParams oplusMaterialBaseParams) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        setBaseParams(view, oplusMaterialBaseParams);
        return view;
    }

    public final View setCornerParamsExt(View view, OplusMaterialCornerParams oplusMaterialCornerParams) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        setCornerParams(view, oplusMaterialCornerParams);
        return view;
    }

    public final View setMetaBallParamsExt(View view, OplusMaterialMetaBallParams oplusMaterialMetaBallParams) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        setMetaBallParams(view, oplusMaterialMetaBallParams);
        return view;
    }

    public final View setOutlineCausticShadowColorExt(View view, int i3) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        setOutlineCausticShadowColor(view, i3);
        return view;
    }

    public final View setOutlineCausticShadowLayoutExt(View view, int i3, int i4, int i5, int i6) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        setOutlineCausticShadowLayout(view, i3, i4, i5, i6);
        return view;
    }

    public final View setOutlineCausticShadowParamsExt(View view, float f3) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        setOutlineCausticShadowParams(view, f3);
        return view;
    }

    public final View setViewBlurEffectExt(View view, COUIMaterialBlurEffect.BlurEffectType params) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Intrinsics.checkNotNullParameter(params, "params");
        setViewBlurEffect(view, params);
        return view;
    }

    public final View setViewStrokeEffectExt(View view, COUIMaterialStrokeEffect.StrokeEffectType type) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Intrinsics.checkNotNullParameter(type, "type");
        setViewStrokeEffect(view, type);
        return view;
    }

    @JvmStatic
    public static final COUIMaterialStrokeEffect.Companion.StrokeEffectParamsHolder getStrokeEffectParamsHolder(Context context, COUIMaterialStrokeEffect.Companion.StrokeEffectParams params) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(params, "params");
        return COUIMaterialStrokeEffect.INSTANCE.getStrokeEffectParamsHolder$coui_support_effect_release(context, params);
    }

    @JvmStatic
    public static final void setOutlineCausticShadowParams(View view, float sdfWidth, float factor0, float factor1, float speed, float luminanceThreshold, float luminanceIntensity) {
        COUICausticShadowEffect.setOutlineCausticShadowParams$coui_support_effect_release(view, sdfWidth, factor0, factor1, speed, luminanceThreshold, luminanceIntensity);
    }

    public final View setOutlineCausticShadowParamsExt(View view, float f3, float f4, float f5, float f6, float f7, float f8) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        setOutlineCausticShadowParams(view, f3, f4, f5, f6, f7, f8);
        return view;
    }

    public final View setViewBlurEffectExt(View view, COUIMaterialBlurEffect.BaseBlurEffectParams startParams, COUIMaterialBlurEffect.BaseBlurEffectParams endParams, COUIMaterialBlurEffect.BaseBlurEffectParams blendModeParams, float f3) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Intrinsics.checkNotNullParameter(startParams, "startParams");
        Intrinsics.checkNotNullParameter(endParams, "endParams");
        Intrinsics.checkNotNullParameter(blendModeParams, "blendModeParams");
        setViewBlurEffect(view, startParams, endParams, blendModeParams, f3);
        return view;
    }

    public final View setViewStrokeEffectExt(View view, COUIMaterialStrokeEffect.Companion.StrokeEffectParams params) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Intrinsics.checkNotNullParameter(params, "params");
        setViewStrokeEffect(view, params);
        return view;
    }

    public final View setViewBlurEffectExt(View view, COUIMaterialBlurEffect.BaseBlurEffectParams params) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Intrinsics.checkNotNullParameter(params, "params");
        setViewBlurEffect(view, params);
        return view;
    }

    public final View setViewStrokeEffectExt(View view, COUIMaterialStrokeEffect.Companion.StrokeEffectParams startParams, COUIMaterialStrokeEffect.Companion.StrokeEffectParams endParams, COUIMaterialStrokeEffect.Companion.StrokeEffectParamsHolder strokeEffectParamsHolder, float f3) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Intrinsics.checkNotNullParameter(startParams, "startParams");
        Intrinsics.checkNotNullParameter(endParams, "endParams");
        setViewStrokeEffect(view, startParams, endParams, strokeEffectParamsHolder, f3);
        return view;
    }

    @JvmStatic
    public static final void setViewBlurEffect(View view, COUIMaterialBlurEffect.BaseBlurEffectParams startParams, COUIMaterialBlurEffect.BaseBlurEffectParams endParams, COUIMaterialBlurEffect.BaseBlurEffectParams blendModeParams, float progress) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(startParams, "startParams");
        Intrinsics.checkNotNullParameter(endParams, "endParams");
        Intrinsics.checkNotNullParameter(blendModeParams, "blendModeParams");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        COUIMaterialBlurEffect.INSTANCE.setViewBlurEffect$coui_support_effect_release(view, startParams, endParams, blendModeParams, progress);
    }

    @JvmStatic
    public static final void setViewStrokeEffect(View view, COUIMaterialStrokeEffect.Companion.StrokeEffectParams params) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(params, "params");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        COUIMaterialStrokeEffect.INSTANCE.setViewStrokeEffect$coui_support_effect_release(view, params);
    }

    @JvmStatic
    public static final void setMetaBallChain(View subjectView, ArraySet<View> observerViews) {
        String message;
        String message2;
        Intrinsics.checkNotNullParameter(subjectView, "subjectView");
        Intrinsics.checkNotNullParameter(observerViews, "observerViews");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        try {
            OplusMaterialUtil.setMetaBallChain(subjectView, observerViews);
        } catch (NoClassDefFoundError e3) {
            if (!DEBUG || (message2 = e3.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message2);
        } catch (NoSuchMethodError e4) {
            if (!DEBUG || (message = e4.getMessage()) == null) {
                return;
            }
            Log.e(TAG, message);
        }
    }

    @JvmStatic
    public static final void setViewBlurEffect(View view, COUIMaterialBlurEffect.BaseBlurEffectParams params) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(params, "params");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        COUIMaterialBlurEffect.INSTANCE.setViewBlurEffect$coui_support_effect_release(view, params);
    }

    @JvmStatic
    public static final void setViewStrokeEffect(View view, COUIMaterialStrokeEffect.Companion.StrokeEffectParams startParams, COUIMaterialStrokeEffect.Companion.StrokeEffectParams endParams, COUIMaterialStrokeEffect.Companion.StrokeEffectParamsHolder holder, float progress) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(startParams, "startParams");
        Intrinsics.checkNotNullParameter(endParams, "endParams");
        if (!COUIEffectVersionUtil.isMaterialVersion()) {
            if (DEBUG) {
                Log.w(TAG, MATERIAL_WARRING_MSG);
                return;
            }
            return;
        }
        COUIMaterialStrokeEffect.INSTANCE.setViewStrokeEffect$coui_support_effect_release(view, startParams, endParams, holder, progress);
    }
}