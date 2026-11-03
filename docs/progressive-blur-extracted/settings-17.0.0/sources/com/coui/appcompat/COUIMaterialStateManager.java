package com.coui.appcompat;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.coui.appcompat.effect.R;
import com.oplus.coreapp.appfeature.AppFeatureProviderUtils;
import com.oplus.settings.statistics.SettingsStatisticsConstant;
import com.oplus.wrapper.os.UserHandle;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b$\bÇ\u0002\u0018\u00002\u00020\u0001:\u0003rstB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020@J\u000e\u0010H\u001a\u00020\u000b2\u0006\u0010I\u001a\u000204J\u0006\u0010J\u001a\u00020\u000bJ\u0019\u0010K\u001a\u00020F2\u000e\b\u0004\u0010L\u001a\b\u0012\u0004\u0012\u00020F0MH\u0082\bJ\u0006\u0010N\u001a\u00020FJ\u001d\u0010O\u001a\u00020F2\u0012\u0010P\u001a\u000e\u0012\u0004\u0012\u00020@\u0012\u0004\u0012\u00020F0QH\u0082\bJ\b\u0010R\u001a\u00020\u0004H\u0002J\u0006\u0010S\u001a\u00020\u0004J(\u0010T\u001a\u00020\u000b2\u0006\u0010I\u001a\u0002042\u0006\u0010U\u001a\u00020\u00042\u0006\u0010V\u001a\u00020\r2\u0006\u0010W\u001a\u00020\u000bH\u0002J\u0010\u0010X\u001a\u00020\u00042\u0006\u0010Y\u001a\u00020\u0004H\u0002J\u0006\u0010Z\u001a\u00020\u000bJ\u0006\u0010[\u001a\u00020\u000bJ\u0006\u0010\\\u001a\u00020\u000bJ\u0006\u0010]\u001a\u00020\u000bJ\u0006\u0010^\u001a\u00020\u000bJ\b\u0010_\u001a\u00020\u000bH\u0002J\u0006\u0010`\u001a\u00020\u000bJ\u0006\u0010a\u001a\u00020\u000bJ\u0006\u0010b\u001a\u00020\u000bJ\u0006\u0010c\u001a\u00020\u000bJ\u000e\u0010d\u001a\u00020F2\u0006\u0010I\u001a\u000204J\u0016\u0010e\u001a\u00020F2\u0006\u0010I\u001a\u0002042\u0006\u0010Y\u001a\u00020\u0004J\u0006\u0010f\u001a\u00020FJ\u000e\u0010g\u001a\u00020F2\u0006\u0010G\u001a\u00020@J\u000e\u0010h\u001a\u00020F2\u0006\u0010Y\u001a\u00020\u0004J \u0010i\u001a\u00020F2\u0006\u0010I\u001a\u0002042\u0006\u0010Y\u001a\u00020\u00042\u0006\u0010j\u001a\u00020\u000bH\u0002J\b\u0010k\u001a\u00020FH\u0002J\u0010\u0010l\u001a\u00020F2\u0006\u0010m\u001a\u00020\u0004H\u0002J\u001d\u0010n\u001a\u00020F2\u0006\u0010I\u001a\u0002042\u0006\u0010Y\u001a\u00020\u0004H\u0000¢\u0006\u0002\boJ \u0010p\u001a\u00020F2\u0006\u0010I\u001a\u0002042\u0006\u0010Y\u001a\u00020\u00042\u0006\u0010j\u001a\u00020\u000bH\u0002J\u0010\u0010q\u001a\u00020F2\u0006\u0010I\u001a\u000204H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u001a\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001d\u001a\u00020\u000b8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR\u000e\u0010 \u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010#\u001a\u00020\u000b8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b$\u0010\u001fR\u000e\u0010%\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010*\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010,\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000202X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u00103\u001a\u0004\u0018\u000104X\u0082\u000e¢\u0006\u0002\n\u0000R\u001b\u00105\u001a\u0002068BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b9\u0010:\u001a\u0004\b7\u00108R\u000e\u0010;\u001a\u00020<X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020@0?0>X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u000200X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020<X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006u"}, d2 = {"Lcom/coui/appcompat/COUIMaterialStateManager;", "", "()V", "ANIM_LEVEL_INVALID", "", "ANIM_LEVEL_MID_END", "BLUR_FLAGS", "BLUR_URI", "Landroid/net/Uri;", "kotlin.jvm.PlatformType", "DEBUG", "", "DISABLE_WINDOW_BLURS", "", "DISABLE_WINDOW_BLURS_URI", "FLAG_ALL_EFFECT", "FLAG_MATERIAL_BLUR", "FLAG_MATERIAL_CAUSTIC_SHADOW", "FLAG_MATERIAL_CROSS_WINDOW_BLUR", "FLAG_MATERIAL_SPOTLIGHT", "FLAG_MATERIAL_STROKE", "IS_LOW_POWER_MODE", "IS_SUPER_POWER_SAVE_MODE", "IS_THEME_ENABLE", "IS_WINDOW_BLUR_DISABLED", "LOW_POWER_MODE", "LOW_POWER_MODE_URI", "MATERIAL_BLUR", "MATERIAL_BLUR_ENABLE", "MATERIAL_CAUSTIC_SHADOW_ENABLE", "getMATERIAL_CAUSTIC_SHADOW_ENABLE$coui_support_effect_release", "()Z", "MATERIAL_EFFECT_FLAGS", "MATERIAL_FEATURE", "MATERIAL_FEATURE_ENABLE", "MATERIAL_SPOTLIGHT_ENABLE", "getMATERIAL_SPOTLIGHT_ENABLE$coui_support_effect_release", "MATERIAL_STROKE", "MATERIAL_STROKE_ENABLE", "PRIVATE_FLAG_DISABLE_WINDOW_BLURS", "PRIVATE_FLAG_LOW_POWER_MODE", "PRIVATE_FLAG_SUPER_POWER_SAVE_MODE", "STROKE_URI", "SUPER_POWER_SAVE_MODE", "SUPER_POWER_SAVE_MODE_URI", "TAG", "WINDOW_BACKGROUND_BLUR_ENABLE", "blurObserver", "Lcom/coui/appcompat/COUIMaterialStateManager$MaterialStateContentObserver;", "disableWindowBlursObserver", "Lcom/coui/appcompat/COUIMaterialStateManager$DisableWindowBlursContentObserver;", "globalContext", "Landroid/content/Context;", "handler", "Landroid/os/Handler;", "getHandler", "()Landroid/os/Handler;", "handler$delegate", "Lkotlin/Lazy;", "lowPowerObserver", "Lcom/coui/appcompat/COUIMaterialStateManager$SystemPowerStateContentObserver;", "materialStateListeners", "Ljava/util/concurrent/CopyOnWriteArrayList;", "Ljava/lang/ref/WeakReference;", "Lcom/coui/appcompat/OnMaterialStateChangeListener;", "registeredFlags", "sCurrentAnimLevel", "strokeObserver", "superPowerSaveObserver", "addOnMaterialStateChangedListener", "", "listener", "checkThemeEnable", "context", "crossWindowBlurVersionCheck", "dispatchOnMainThread", "block", "Lkotlin/Function0;", "dump", "forEachMaterialStateListener", "action", "Lkotlin/Function1;", "getAnimLevelVersion", "getAnimationLevel", "getBooleanCompat", "compiledResId", "resourceName", "fallbackValue", "getSupportedEffectFlags", "flags", "isBlurEffectEnable", "isCausticShadowEffectEnable", "isFeatureEnabled", "isLowAnimationLevel", "isLowPowerMode", "isMainThread", "isSpotLightEffectEnable", "isStrokeEffectEnable", "isWindowBackgroundBlurEnable", "materialVersionCheck", "onConfigurationChange", "registerMaterialStateObserver", "releaseContext", "removeOnMaterialStateChangedListener", "unregisterMaterialStateObserver", "updateBlurStates", "forceDisableEffect", "updateConfidentialFeature", "updatePowerState", "flag", "updateState", "updateState$coui_support_effect_release", "updateStrokeState", "updateWindowBackgroundBlurDisabledState", "DisableWindowBlursContentObserver", "MaterialStateContentObserver", "SystemPowerStateContentObserver", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIMaterialStateManager {
    private static final int ANIM_LEVEL_INVALID = -1;
    private static final int ANIM_LEVEL_MID_END = 2;
    private static final int BLUR_FLAGS = 18;
    private static final String DISABLE_WINDOW_BLURS = "disable_window_blurs";
    public static final int FLAG_ALL_EFFECT = 31;
    public static final int FLAG_MATERIAL_BLUR = 2;
    public static final int FLAG_MATERIAL_CAUSTIC_SHADOW = 8;
    public static final int FLAG_MATERIAL_CROSS_WINDOW_BLUR = 16;
    public static final int FLAG_MATERIAL_SPOTLIGHT = 4;
    public static final int FLAG_MATERIAL_STROKE = 1;
    private static boolean IS_LOW_POWER_MODE = false;
    private static boolean IS_SUPER_POWER_SAVE_MODE = false;
    private static boolean IS_THEME_ENABLE = false;
    private static boolean IS_WINDOW_BLUR_DISABLED = false;
    private static boolean MATERIAL_BLUR_ENABLE = false;
    private static final int MATERIAL_EFFECT_FLAGS = 15;
    private static final String MATERIAL_FEATURE = "com.oplus.appcompat.texture_effect.Enable";
    private static boolean MATERIAL_FEATURE_ENABLE = false;
    private static boolean MATERIAL_STROKE_ENABLE = false;
    private static final int PRIVATE_FLAG_DISABLE_WINDOW_BLURS = 1024;
    private static final int PRIVATE_FLAG_LOW_POWER_MODE = 256;
    private static final int PRIVATE_FLAG_SUPER_POWER_SAVE_MODE = 512;
    private static final String TAG = "MaterialStateManager";
    private static boolean WINDOW_BACKGROUND_BLUR_ENABLE;
    private static Context globalContext;
    private static int registeredFlags;
    public static final COUIMaterialStateManager INSTANCE = new COUIMaterialStateManager();
    private static final boolean DEBUG = Log.isLoggable(COUIMaterialEffect.TAG, 3);
    private static final String MATERIAL_STROKE = "system_material_stroke_enable";
    private static final Uri STROKE_URI = Settings.System.getUriFor(MATERIAL_STROKE);
    private static final String MATERIAL_BLUR = "system_material_blur_enable";
    private static final Uri BLUR_URI = Settings.System.getUriFor(MATERIAL_BLUR);
    private static final String LOW_POWER_MODE = "low_power";
    private static final Uri LOW_POWER_MODE_URI = Settings.Global.getUriFor(LOW_POWER_MODE);
    private static final String SUPER_POWER_SAVE_MODE = "super_powersave_mode_state";
    private static final Uri SUPER_POWER_SAVE_MODE_URI = Settings.System.getUriFor(SUPER_POWER_SAVE_MODE);
    private static final Uri DISABLE_WINDOW_BLURS_URI = Settings.Global.getUriFor("disable_window_blurs");
    private static final MaterialStateContentObserver blurObserver = new MaterialStateContentObserver(new Handler(Looper.getMainLooper()));
    private static final MaterialStateContentObserver strokeObserver = new MaterialStateContentObserver(new Handler(Looper.getMainLooper()));
    private static final SystemPowerStateContentObserver lowPowerObserver = new SystemPowerStateContentObserver(new Handler(Looper.getMainLooper()));
    private static final SystemPowerStateContentObserver superPowerSaveObserver = new SystemPowerStateContentObserver(new Handler(Looper.getMainLooper()));
    private static final DisableWindowBlursContentObserver disableWindowBlursObserver = new DisableWindowBlursContentObserver(new Handler(Looper.getMainLooper()));
    private static final CopyOnWriteArrayList<WeakReference<OnMaterialStateChangeListener>> materialStateListeners = new CopyOnWriteArrayList<>();

    /* JADX INFO: renamed from: handler$delegate, reason: from kotlin metadata */
    private static final Lazy handler = LazyKt.lazy(COUIMaterialStateManager$handler$2.INSTANCE);
    private static int sCurrentAnimLevel = -1;

    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016¨\u0006\u000b"}, d2 = {"Lcom/coui/appcompat/COUIMaterialStateManager$DisableWindowBlursContentObserver;", "Landroid/database/ContentObserver;", "handler", "Landroid/os/Handler;", "(Landroid/os/Handler;)V", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class DisableWindowBlursContentObserver extends ContentObserver {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DisableWindowBlursContentObserver(Handler handler) {
            super(handler);
            Intrinsics.checkNotNullParameter(handler, "handler");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Context contextAccess$getGlobalContext$p = COUIMaterialStateManager.access$getGlobalContext$p();
            if (contextAccess$getGlobalContext$p == null || (COUIMaterialStateManager.access$getRegisteredFlags$p() & 16) != 16) {
                return;
            }
            COUIMaterialStateManager.INSTANCE.updateState$coui_support_effect_release(contextAccess$getGlobalContext$p, 16);
        }
    }

    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016¨\u0006\u000b"}, d2 = {"Lcom/coui/appcompat/COUIMaterialStateManager$MaterialStateContentObserver;", "Landroid/database/ContentObserver;", "handler", "Landroid/os/Handler;", "(Landroid/os/Handler;)V", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class MaterialStateContentObserver extends ContentObserver {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MaterialStateContentObserver(Handler handler) {
            super(handler);
            Intrinsics.checkNotNullParameter(handler, "handler");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Context contextAccess$getGlobalContext$p = COUIMaterialStateManager.access$getGlobalContext$p();
            if (contextAccess$getGlobalContext$p != null) {
                if (COUIMaterialStateManager.access$getSTROKE_URI$p().equals(uri)) {
                    COUIMaterialStateManager.INSTANCE.updateState$coui_support_effect_release(contextAccess$getGlobalContext$p, 1);
                } else if (COUIMaterialStateManager.access$getBLUR_URI$p().equals(uri)) {
                    COUIMaterialStateManager.INSTANCE.updateState$coui_support_effect_release(contextAccess$getGlobalContext$p, COUIMaterialStateManager.access$getRegisteredFlags$p() & 18);
                }
            }
        }
    }

    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016¨\u0006\u000b"}, d2 = {"Lcom/coui/appcompat/COUIMaterialStateManager$SystemPowerStateContentObserver;", "Landroid/database/ContentObserver;", "handler", "Landroid/os/Handler;", "(Landroid/os/Handler;)V", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class SystemPowerStateContentObserver extends ContentObserver {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SystemPowerStateContentObserver(Handler handler) {
            super(handler);
            Intrinsics.checkNotNullParameter(handler, "handler");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Context contextAccess$getGlobalContext$p = COUIMaterialStateManager.access$getGlobalContext$p();
            if (contextAccess$getGlobalContext$p != null) {
                if (COUIMaterialStateManager.access$getSUPER_POWER_SAVE_MODE_URI$p().equals(uri)) {
                    COUIMaterialStateManager.access$updatePowerState(COUIMaterialStateManager.INSTANCE, 512);
                } else if (COUIMaterialStateManager.access$getLOW_POWER_MODE_URI$p().equals(uri)) {
                    COUIMaterialStateManager.access$updatePowerState(COUIMaterialStateManager.INSTANCE, 256);
                }
                COUIMaterialStateManager cOUIMaterialStateManager = COUIMaterialStateManager.INSTANCE;
                cOUIMaterialStateManager.updateState$coui_support_effect_release(contextAccess$getGlobalContext$p, 1);
                cOUIMaterialStateManager.updateState$coui_support_effect_release(contextAccess$getGlobalContext$p, COUIMaterialStateManager.access$getRegisteredFlags$p() & 18);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.COUIMaterialStateManager$dispatchOnMainThread$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes7.dex */
    @Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements Runnable {
        final /* synthetic */ Function0<Unit> $block;

        public AnonymousClass1(Function0<Unit> function0) {
            this.$block = function0;
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.$block.invoke();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.COUIMaterialStateManager$removeOnMaterialStateChangedListener$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u001a\u0010\u0002\u001a\u0016\u0012\u0004\u0012\u00020\u0004 \u0005*\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, d2 = {"<anonymous>", "", "it", "Ljava/lang/ref/WeakReference;", "Lcom/coui/appcompat/OnMaterialStateChangeListener;", "kotlin.jvm.PlatformType", "invoke", "(Ljava/lang/ref/WeakReference;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class C13491 extends Lambda implements Function1<WeakReference<OnMaterialStateChangeListener>, Boolean> {
        final /* synthetic */ OnMaterialStateChangeListener $listener;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public C13491(OnMaterialStateChangeListener onMaterialStateChangeListener) {
            super(1);
            this.$listener = onMaterialStateChangeListener;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Boolean invoke2(WeakReference<OnMaterialStateChangeListener> weakReference) {
            return Boolean.valueOf(weakReference.get() == null || weakReference.get() == this.$listener);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Boolean invoke(WeakReference<OnMaterialStateChangeListener> weakReference) {
            return invoke2(weakReference);
        }
    }

    private COUIMaterialStateManager() {
    }

    public static final /* synthetic */ Uri access$getBLUR_URI$p() {
        return BLUR_URI;
    }

    public static final /* synthetic */ Context access$getGlobalContext$p() {
        return globalContext;
    }

    public static final /* synthetic */ Uri access$getLOW_POWER_MODE_URI$p() {
        return LOW_POWER_MODE_URI;
    }

    public static final /* synthetic */ boolean access$getMATERIAL_BLUR_ENABLE$p() {
        return MATERIAL_BLUR_ENABLE;
    }

    public static final /* synthetic */ boolean access$getMATERIAL_STROKE_ENABLE$p() {
        return MATERIAL_STROKE_ENABLE;
    }

    public static final /* synthetic */ CopyOnWriteArrayList access$getMaterialStateListeners$p() {
        return materialStateListeners;
    }

    public static final /* synthetic */ int access$getRegisteredFlags$p() {
        return registeredFlags;
    }

    public static final /* synthetic */ Uri access$getSTROKE_URI$p() {
        return STROKE_URI;
    }

    public static final /* synthetic */ Uri access$getSUPER_POWER_SAVE_MODE_URI$p() {
        return SUPER_POWER_SAVE_MODE_URI;
    }

    public static final /* synthetic */ boolean access$getWINDOW_BACKGROUND_BLUR_ENABLE$p() {
        return WINDOW_BACKGROUND_BLUR_ENABLE;
    }

    public static final /* synthetic */ void access$updatePowerState(COUIMaterialStateManager cOUIMaterialStateManager, int i3) {
        cOUIMaterialStateManager.updatePowerState(i3);
    }

    private final void dispatchOnMainThread(Function0<Unit> block) {
        if (isMainThread()) {
            block.invoke();
        } else {
            getHandler().post(new AnonymousClass1(block));
        }
    }

    private final void forEachMaterialStateListener(Function1<? super OnMaterialStateChangeListener, Unit> action) {
        for (WeakReference weakReference : access$getMaterialStateListeners$p()) {
            OnMaterialStateChangeListener onMaterialStateChangeListener = (OnMaterialStateChangeListener) weakReference.get();
            if (onMaterialStateChangeListener == null) {
                access$getMaterialStateListeners$p().remove(weakReference);
            } else {
                action.invoke(onMaterialStateChangeListener);
            }
        }
    }

    private final int getAnimLevelVersion() {
        try {
            Object objInvoke = Class.forName("android.os.SystemProperties").getMethod(SettingsStatisticsConstant.METHOD_GET, String.class).invoke(null, "persist.sys.oplus.anim_level");
            Intrinsics.checkNotNull(objInvoke, "null cannot be cast to non-null type kotlin.String");
            String str = (String) objInvoke;
            if (TextUtils.isEmpty(str)) {
                return -1;
            }
            int length = str.length() - 1;
            int i3 = 0;
            boolean z2 = false;
            while (i3 <= length) {
                boolean z3 = Intrinsics.compare((int) str.charAt(!z2 ? i3 : length), 32) <= 0;
                if (z2) {
                    if (!z3) {
                        break;
                    }
                    length--;
                } else if (z3) {
                    i3++;
                } else {
                    z2 = true;
                }
            }
            return Integer.parseInt(str.subSequence(i3, length + 1).toString());
        } catch (Exception e3) {
            Log.e(TAG, "getAnimLevelVersion e: " + e3);
            return Integer.MIN_VALUE;
        }
    }

    private final boolean getBooleanCompat(Context context, int compiledResId, String resourceName, boolean fallbackValue) {
        Resources resources = context.getResources();
        try {
            return resources.getBoolean(compiledResId);
        } catch (Resources.NotFoundException e3) {
            Log.w(TAG, "Invalid compiled resource ID: " + compiledResId, e3);
            int identifier = resources.getIdentifier(resourceName, "bool", context.getPackageName());
            if (identifier == 0) {
                Log.w(TAG, "Resource ID: " + compiledResId + " not found!");
                return fallbackValue;
            }
            try {
                return resources.getBoolean(identifier);
            } catch (Resources.NotFoundException e4) {
                Log.w(TAG, "Invalid runtime resource: " + resourceName, e4);
                return fallbackValue;
            }
        }
    }

    private final Handler getHandler() {
        return (Handler) handler.getValue();
    }

    private final int getSupportedEffectFlags(int flags) {
        if (materialVersionCheck()) {
            return flags;
        }
        return ((flags & 16) == 16 && crossWindowBlurVersionCheck()) ? 16 : 0;
    }

    private final boolean isMainThread() {
        return Intrinsics.areEqual(Looper.myLooper(), Looper.getMainLooper());
    }

    private final void updateBlurStates(Context context, int flags, boolean forceDisableEffect) {
        boolean z2 = false;
        boolean z3 = (flags & 2) == 2 && materialVersionCheck();
        boolean z4 = (flags & 16) == 16 && crossWindowBlurVersionCheck();
        if (z3 || z4) {
            boolean z5 = Settings.System.getInt(context.getContentResolver(), MATERIAL_BLUR, 0) == 1;
            if (z3) {
                boolean z6 = !forceDisableEffect && z5;
                if (z6 != MATERIAL_BLUR_ENABLE) {
                    MATERIAL_BLUR_ENABLE = z6;
                    boolean zIsFeatureEnabled = isFeatureEnabled();
                    if (isMainThread()) {
                        for (WeakReference weakReference : access$getMaterialStateListeners$p()) {
                            OnMaterialStateChangeListener onMaterialStateChangeListener = (OnMaterialStateChangeListener) weakReference.get();
                            if (onMaterialStateChangeListener == null) {
                                access$getMaterialStateListeners$p().remove(weakReference);
                            } else {
                                onMaterialStateChangeListener.onMaterialStateChange(2, z6);
                                if (zIsFeatureEnabled) {
                                    onMaterialStateChangeListener.onMaterialStateChange(4, z6);
                                }
                            }
                        }
                    } else {
                        getHandler().post(new COUIMaterialStateManager$updateBlurStates$$inlined$dispatchOnMainThread$1(z6, zIsFeatureEnabled));
                    }
                }
            }
            if (z4) {
                updateWindowBackgroundBlurDisabledState(context);
                if (!forceDisableEffect && z5 && !IS_WINDOW_BLUR_DISABLED) {
                    z2 = true;
                }
                if (z2 != WINDOW_BACKGROUND_BLUR_ENABLE) {
                    WINDOW_BACKGROUND_BLUR_ENABLE = z2;
                    if (!isMainThread()) {
                        getHandler().post(new COUIMaterialStateManager$updateBlurStates$$inlined$dispatchOnMainThread$2(z2));
                        return;
                    }
                    for (WeakReference weakReference2 : access$getMaterialStateListeners$p()) {
                        OnMaterialStateChangeListener onMaterialStateChangeListener2 = (OnMaterialStateChangeListener) weakReference2.get();
                        if (onMaterialStateChangeListener2 == null) {
                            access$getMaterialStateListeners$p().remove(weakReference2);
                        } else {
                            onMaterialStateChangeListener2.onMaterialStateChange(16, z2);
                        }
                    }
                }
            }
        }
    }

    private final void updateConfidentialFeature() {
        Context context = globalContext;
        if (context != null) {
            MATERIAL_FEATURE_ENABLE = AppFeatureProviderUtils.getBoolean(context.getContentResolver(), MATERIAL_FEATURE, false);
        }
    }

    private final void updatePowerState(int flag) {
        Context context = globalContext;
        if (context != null) {
            if ((flag & 512) == 512) {
                IS_SUPER_POWER_SAVE_MODE = false;
                if (COUIEffectVersionUtil.checkOPlusViewSubSDK(27, 11)) {
                    int iMyUserId = UserHandle.myUserId();
                    int i3 = UserHandle.USER_SYSTEM;
                    if (iMyUserId == i3) {
                        try {
                            IS_SUPER_POWER_SAVE_MODE = com.oplus.wrapper.provider.Settings.System.getIntForUser(context.getContentResolver(), SUPER_POWER_SAVE_MODE, 0, i3) == 1;
                        } catch (SecurityException e3) {
                            Log.e(TAG, "updatePowerState: permission denied for current user, fallback to default state.", e3);
                        }
                    }
                }
            }
            if ((flag & 256) == 256) {
                IS_LOW_POWER_MODE = Settings.Global.getInt(context.getContentResolver(), LOW_POWER_MODE, 0) == 1;
            }
        }
    }

    private final void updateStrokeState(Context context, int flags, boolean forceDisableEffect) {
        if ((flags & 1) == 1 && materialVersionCheck()) {
            boolean z2 = false;
            boolean z3 = !forceDisableEffect && Settings.System.getInt(context.getContentResolver(), MATERIAL_STROKE, 0) == 1;
            if (z3 == MATERIAL_STROKE_ENABLE) {
                return;
            }
            MATERIAL_STROKE_ENABLE = z3;
            if (isFeatureEnabled() && COUIEffectVersionUtil.isCausticShadowVersion()) {
                z2 = true;
            }
            if (!isMainThread()) {
                getHandler().post(new COUIMaterialStateManager$updateStrokeState$$inlined$dispatchOnMainThread$1(z3, z2));
                return;
            }
            for (WeakReference weakReference : access$getMaterialStateListeners$p()) {
                OnMaterialStateChangeListener onMaterialStateChangeListener = (OnMaterialStateChangeListener) weakReference.get();
                if (onMaterialStateChangeListener == null) {
                    access$getMaterialStateListeners$p().remove(weakReference);
                } else {
                    onMaterialStateChangeListener.onMaterialStateChange(1, z3);
                    if (z2) {
                        onMaterialStateChangeListener.onMaterialStateChange(8, z3);
                    }
                }
            }
        }
    }

    private final void updateWindowBackgroundBlurDisabledState(Context context) {
        IS_WINDOW_BLUR_DISABLED = Settings.Global.getInt(context.getContentResolver(), "disable_window_blurs", 0) == 1;
    }

    public final void addOnMaterialStateChangedListener(OnMaterialStateChangeListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        CopyOnWriteArrayList<WeakReference<OnMaterialStateChangeListener>> copyOnWriteArrayList = materialStateListeners;
        if (copyOnWriteArrayList == null || !copyOnWriteArrayList.isEmpty()) {
            Iterator<T> it = copyOnWriteArrayList.iterator();
            while (it.hasNext()) {
                if (((WeakReference) it.next()).get() == listener) {
                    return;
                }
            }
        }
        materialStateListeners.add(new WeakReference<>(listener));
        if (!isMainThread()) {
            getHandler().post(new COUIMaterialStateManager$addOnMaterialStateChangedListener$$inlined$dispatchOnMainThread$1(listener));
            return;
        }
        CopyOnWriteArrayList copyOnWriteArrayListAccess$getMaterialStateListeners$p = access$getMaterialStateListeners$p();
        if (copyOnWriteArrayListAccess$getMaterialStateListeners$p == null || !copyOnWriteArrayListAccess$getMaterialStateListeners$p.isEmpty()) {
            Iterator it2 = copyOnWriteArrayListAccess$getMaterialStateListeners$p.iterator();
            while (it2.hasNext()) {
                if (((WeakReference) it2.next()).get() == listener) {
                    if ((access$getRegisteredFlags$p() & 15) == 0) {
                        listener.onMaterialStateChange(1, access$getMATERIAL_STROKE_ENABLE$p());
                        listener.onMaterialStateChange(2, access$getMATERIAL_BLUR_ENABLE$p());
                        COUIMaterialStateManager cOUIMaterialStateManager = INSTANCE;
                        listener.onMaterialStateChange(4, cOUIMaterialStateManager.getMATERIAL_SPOTLIGHT_ENABLE$coui_support_effect_release());
                        listener.onMaterialStateChange(8, cOUIMaterialStateManager.getMATERIAL_CAUSTIC_SHADOW_ENABLE$coui_support_effect_release());
                    } else {
                        if ((access$getRegisteredFlags$p() & 1) == 1) {
                            listener.onMaterialStateChange(1, access$getMATERIAL_STROKE_ENABLE$p());
                            listener.onMaterialStateChange(8, INSTANCE.getMATERIAL_CAUSTIC_SHADOW_ENABLE$coui_support_effect_release());
                        }
                        if ((access$getRegisteredFlags$p() & 2) == 2) {
                            listener.onMaterialStateChange(2, access$getMATERIAL_BLUR_ENABLE$p());
                            listener.onMaterialStateChange(4, INSTANCE.getMATERIAL_SPOTLIGHT_ENABLE$coui_support_effect_release());
                        }
                    }
                    if (access$getRegisteredFlags$p() == 0 || (access$getRegisteredFlags$p() & 16) == 16) {
                        listener.onMaterialStateChange(16, access$getWINDOW_BACKGROUND_BLUR_ENABLE$p());
                        return;
                    }
                    return;
                }
            }
        }
    }

    public final boolean checkThemeEnable(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        boolean z2 = getBooleanCompat(context, R.bool.coui_blur_enable, "coui_blur_enable", true) && !getBooleanCompat(context, R.bool.is_immersive_theme, "is_immersive_theme", false);
        IS_THEME_ENABLE = z2;
        return z2;
    }

    public final boolean crossWindowBlurVersionCheck() {
        return COUIEffectVersionUtil.checkOPlusViewSubSDK(34, 2);
    }

    public final void dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("stroke enable=");
        sb.append(MATERIAL_STROKE_ENABLE);
        sb.append("\n");
        sb.append("blur enable=");
        sb.append(MATERIAL_BLUR_ENABLE);
        sb.append("\n");
        sb.append("window background blur enable=");
        sb.append(WINDOW_BACKGROUND_BLUR_ENABLE);
        sb.append("\n");
        sb.append("window blur disabled=");
        sb.append(IS_WINDOW_BLUR_DISABLED);
        sb.append("\n");
        sb.append("spot light enable=");
        sb.append(getMATERIAL_SPOTLIGHT_ENABLE$coui_support_effect_release());
        sb.append("\n");
        sb.append("caustic shadow enable=");
        sb.append(getMATERIAL_CAUSTIC_SHADOW_ENABLE$coui_support_effect_release());
        sb.append("\n");
        sb.append("lowPower=");
        sb.append(IS_LOW_POWER_MODE);
        sb.append("\n");
        sb.append("superSavePower=");
        sb.append(IS_SUPER_POWER_SAVE_MODE);
        sb.append("\n");
        sb.append("context=");
        sb.append(globalContext);
        sb.append("\n");
        sb.append("feature=");
        sb.append(isFeatureEnabled());
        sb.append("\n");
        sb.append("versionCheck=");
        sb.append(materialVersionCheck());
        sb.append("\n");
        sb.append("currentAnimLevel=");
        sb.append(sCurrentAnimLevel);
        sb.append("\n");
        sb.append("globalThemeCheck=");
        sb.append(IS_THEME_ENABLE);
        sb.append("\n");
        sb.append("registeredFlags=");
        sb.append(registeredFlags);
        sb.append("\n");
        sb.append("listeners:[");
        sb.append("\n");
        for (WeakReference weakReference : access$getMaterialStateListeners$p()) {
            OnMaterialStateChangeListener onMaterialStateChangeListener = (OnMaterialStateChangeListener) weakReference.get();
            if (onMaterialStateChangeListener == null) {
                access$getMaterialStateListeners$p().remove(weakReference);
            } else {
                sb.append(onMaterialStateChangeListener.onDump());
                sb.append("\n");
            }
        }
        sb.append("]");
        Log.i(TAG, sb.toString());
    }

    public final int getAnimationLevel() {
        if (sCurrentAnimLevel == -1) {
            sCurrentAnimLevel = getAnimLevelVersion();
        }
        return sCurrentAnimLevel;
    }

    public final boolean getMATERIAL_CAUSTIC_SHADOW_ENABLE$coui_support_effect_release() {
        return MATERIAL_STROKE_ENABLE && isFeatureEnabled();
    }

    public final boolean getMATERIAL_SPOTLIGHT_ENABLE$coui_support_effect_release() {
        return MATERIAL_BLUR_ENABLE && isFeatureEnabled();
    }

    public final boolean isBlurEffectEnable() {
        return MATERIAL_BLUR_ENABLE;
    }

    public final boolean isCausticShadowEffectEnable() {
        return getMATERIAL_CAUSTIC_SHADOW_ENABLE$coui_support_effect_release() && COUIEffectVersionUtil.isCausticShadowVersion();
    }

    public final boolean isFeatureEnabled() {
        return MATERIAL_FEATURE_ENABLE;
    }

    public final boolean isLowAnimationLevel() {
        int animationLevel = getAnimationLevel();
        boolean z2 = false;
        if (1 <= animationLevel && animationLevel < 3) {
            z2 = true;
        }
        return !z2;
    }

    public final boolean isLowPowerMode() {
        return IS_LOW_POWER_MODE || IS_SUPER_POWER_SAVE_MODE;
    }

    public final boolean isSpotLightEffectEnable() {
        return getMATERIAL_SPOTLIGHT_ENABLE$coui_support_effect_release();
    }

    public final boolean isStrokeEffectEnable() {
        return MATERIAL_STROKE_ENABLE;
    }

    public final boolean isWindowBackgroundBlurEnable() {
        return WINDOW_BACKGROUND_BLUR_ENABLE;
    }

    public final boolean materialVersionCheck() {
        return COUIEffectVersionUtil.checkOPlusViewSubSDK(40, 2);
    }

    public final void onConfigurationChange(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Context applicationContext = context.getApplicationContext();
        globalContext = applicationContext;
        if (applicationContext != null) {
            INSTANCE.updateState$coui_support_effect_release(context, registeredFlags);
        }
    }

    public final void registerMaterialStateObserver(Context context, int flags) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (isLowAnimationLevel()) {
            Log.e(TAG, "Animate level " + getAnimationLevel() + " lower than MID END. Material effect disabled.");
            return;
        }
        int supportedEffectFlags = getSupportedEffectFlags(flags);
        if (supportedEffectFlags == 0) {
            Log.w(TAG, "Version check failed for flags=" + flags + '.');
            return;
        }
        globalContext = context.getApplicationContext();
        updateConfidentialFeature();
        if ((registeredFlags & 256) != 256) {
            context.getContentResolver().registerContentObserver(LOW_POWER_MODE_URI, false, lowPowerObserver);
            updatePowerState(256);
            registeredFlags |= 256;
        }
        if ((registeredFlags & 512) != 512) {
            context.getContentResolver().registerContentObserver(SUPER_POWER_SAVE_MODE_URI, false, superPowerSaveObserver);
            updatePowerState(512);
            registeredFlags |= 512;
        }
        if ((supportedEffectFlags & 1) == 1 && (registeredFlags & 1) == 0) {
            context.getContentResolver().registerContentObserver(STROKE_URI, false, strokeObserver);
            updateState$coui_support_effect_release(context, 1);
            registeredFlags |= 1;
        }
        int i3 = supportedEffectFlags & 18;
        int i4 = registeredFlags;
        int i5 = i3 & (~i4);
        if (i5 != 0) {
            if ((i4 & 18) == 0) {
                context.getContentResolver().registerContentObserver(BLUR_URI, false, blurObserver);
            }
            if ((i5 & 16) == 16 && (registeredFlags & 1024) != 1024) {
                context.getContentResolver().registerContentObserver(DISABLE_WINDOW_BLURS_URI, false, disableWindowBlursObserver);
                registeredFlags |= 1024;
            }
            updateState$coui_support_effect_release(context, i5);
            registeredFlags |= i5;
        }
    }

    public final void releaseContext() {
        globalContext = null;
    }

    public final void removeOnMaterialStateChangedListener(OnMaterialStateChangeListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        CollectionsKt.removeAll((List) materialStateListeners, (Function1) new C13491(listener));
    }

    public final void unregisterMaterialStateObserver(int flags) {
        Context context;
        Context context2 = globalContext;
        if (context2 != null) {
            if ((flags & 1) == 1 && (registeredFlags & 1) == 1) {
                context2.getContentResolver().unregisterContentObserver(strokeObserver);
            }
            int i3 = registeredFlags;
            if ((i3 & 18) != 0 && (i3 & 18 & (~flags)) == 0) {
                context2.getContentResolver().unregisterContentObserver(blurObserver);
            }
            if ((flags & 16) == 16) {
                int i4 = registeredFlags;
                if ((i4 & 16) == 16 && (i4 & 1024) == 1024) {
                    context2.getContentResolver().unregisterContentObserver(disableWindowBlursObserver);
                    registeredFlags &= -1025;
                }
            }
            registeredFlags &= ~flags;
        }
        int i5 = registeredFlags;
        if ((i5 & 19) != 0 || (i5 & 768) == 0 || (context = globalContext) == null) {
            return;
        }
        context.getContentResolver().unregisterContentObserver(lowPowerObserver);
        context.getContentResolver().unregisterContentObserver(superPowerSaveObserver);
        registeredFlags &= -769;
        globalContext = null;
    }

    public final void updateState$coui_support_effect_release(Context context, int flags) {
        boolean z2;
        Intrinsics.checkNotNullParameter(context, "context");
        if (isLowAnimationLevel()) {
            Log.w(TAG, "Animate level " + getAnimationLevel() + " lower than MID END. Material effect disabled.");
            return;
        }
        if (globalContext == null) {
            globalContext = context;
            updateConfidentialFeature();
        }
        boolean z3 = true;
        if (checkThemeEnable(context)) {
            z2 = false;
        } else {
            Log.w(TAG, "Theme check failed.");
            z2 = true;
        }
        if (isLowPowerMode()) {
            Log.w(TAG, "Low power mode, check failed.");
        } else {
            z3 = z2;
        }
        updateStrokeState(context, flags, z3);
        updateBlurStates(context, flags, z3);
        if (DEBUG) {
            dump();
        }
    }
}