package com.coui.appcompat.toolbar;

import android.content.Context;
import com.coui.appcompat.COUIMaterialBlurEffect;
import com.coui.appcompat.COUIMaterialStrokeEffect;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.support.appcompat.R;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J\u0010\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J\u0010\u0010\f\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J\u0018\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0007\u001a\u00020\u0004H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/coui/appcompat/toolbar/ToolbarMaterialEffectResolver;", "", "()V", "BACKGROUND_COLOR", "", "getBlurEffectType", "Lcom/coui/appcompat/COUIMaterialBlurEffect$BlurEffectType;", "preset", "getCapsuleStrokeEffectType", "Lcom/coui/appcompat/COUIMaterialStrokeEffect$StrokeEffectType;", "getCapsuleStrokeEffectTypeInWindow", "getCircleStrokeEffectType", "getCircleStrokeEffectTypeInWindow", "getToolbarBackgroundColor", "context", "Landroid/content/Context;", "coui-support-toolbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ToolbarMaterialEffectResolver {
    private static final int BACKGROUND_COLOR = 1304083130;
    public static final ToolbarMaterialEffectResolver INSTANCE = new ToolbarMaterialEffectResolver();

    private ToolbarMaterialEffectResolver() {
    }

    @JvmStatic
    public static final COUIMaterialBlurEffect.BlurEffectType getBlurEffectType(int preset) {
        if (preset != 1) {
            return preset != 2 ? COUIMaterialBlurEffect.BlurEffectType.TYPE_FRAMEWORK_TOP_BAR : COUIMaterialBlurEffect.BlurEffectType.TYPE_FRAMEWORK_TOP_BAR_BLUR;
        }
        return COUIMaterialBlurEffect.BlurEffectType.TYPE_FRAMEWORK_TOP_BAR1;
    }

    @JvmStatic
    public static final COUIMaterialStrokeEffect.StrokeEffectType getCapsuleStrokeEffectType(int preset) {
        return preset == 1 ? COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_7 : COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_1;
    }

    @JvmStatic
    public static final COUIMaterialStrokeEffect.StrokeEffectType getCapsuleStrokeEffectTypeInWindow(int preset) {
        return preset == 1 ? COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_9 : COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CAPSULE_8;
    }

    @JvmStatic
    public static final COUIMaterialStrokeEffect.StrokeEffectType getCircleStrokeEffectType(int preset) {
        return preset == 1 ? COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_3 : COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_1;
    }

    @JvmStatic
    public static final COUIMaterialStrokeEffect.StrokeEffectType getCircleStrokeEffectTypeInWindow(int preset) {
        return preset == 1 ? COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_5 : COUIMaterialStrokeEffect.StrokeEffectType.TYPE_FRAMEWORK_CIRCLE_4;
    }

    @JvmStatic
    public static final int getToolbarBackgroundColor(Context context, int preset) {
        Intrinsics.checkNotNullParameter(context, "context");
        return (preset == 0 || preset == 2) ? COUIContextUtil.getAttrColor(context, R.attr.couiColorBar) : BACKGROUND_COLOR;
    }
}