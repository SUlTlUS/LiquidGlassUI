package com.coui.appcompat.theme;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.ImageView;
import androidx.collection.SieveCacheKt;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.tintimageview.COUITintUtil;
import com.coui.appcompat.version.COUICompatUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.settings.statistics.SettingsStatisticsConstant;
import com.support.appcompat.R;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import okhttp3.internal.ws.WebSocketProtocol;
import oplus.content.res.OplusExtraConfiguration;

/* JADX INFO: loaded from: classes.dex */
public class COUIThemeOverlay {
    private static final String BASE_CONFIG_NEW = "android.content.res.OplusBaseConfiguration";
    private static final String COLOR_MATERIAL_ENABLE = "color_material_enable";
    private static final int COMPAT_VERSION = 12000;
    private static final int COUI_CUSTOM_FALG = 131072;
    private static final int COUI_CUSTOM_THEME_FLAG = 256;
    private static final int COUI_ONLINE_FALG = 1048576;
    private static final int COUI_SELECT_FALG = 65535;
    private static final int COUI_SINGLE_FALG = 65536;
    private static final int COUI_THIRD_THEME_FLAG = 1;
    private static final int COUI_TYPE_FALG = 16711680;
    private static final int COUI_WALLPAPER_FALG = 262144;
    private static final String CUSTOM_THEME_PATH = "my_company/media/theme/";
    private static final String CUSTOM_THEME_PATH_SETTING = "custom_theme_path_setting";
    private static final String DATA_THEME_PATH = "data/theme/";
    private static final String TAG = "COUIThemeOverlay";
    private static final String THEME_VERSION_KEY = "ro.oplus.theme.version";
    private static final String WRAPPER_CLASS_NEW = "com.oplus.inner.content.res.ConfigurationWrapper";
    private static int mCompatVersion;
    private static boolean mThemeO;
    private static String mThemeOverlayName;
    private static boolean mThemeP;
    private static boolean mThemeR;
    private SparseIntArray themeOverlays = new SparseIntArray();
    private HashMap<String, WeakReference<Boolean>> mMetaCaches = new HashMap<>();

    public static class SingleTone {
        private static final COUIThemeOverlay INSTANCE = new COUIThemeOverlay();

        private SingleTone() {
        }

        public static /* synthetic */ COUIThemeOverlay access$000() {
            return INSTANCE;
        }
    }

    static {
        mThemeOverlayName = canReachFrameworkWrapper() ? WRAPPER_CLASS_NEW : COUICompatUtil.getInstance().getConfigurationName();
        mThemeO = isThemeO();
        mThemeR = isThemeR();
        mThemeP = isThemeP() && COUIVersionUtil.getOSVersionCode() > 0;
        mCompatVersion = getCompatVersion();
    }

    private boolean canReachBaseConfiguration() {
        try {
            Class.forName(BASE_CONFIG_NEW);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean canReachFrameworkWrapper() {
        try {
            Class.forName(WRAPPER_CLASS_NEW);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private boolean getBoolValue(Resources resources, int i3) {
        if (resources == null || i3 == 0) {
            return false;
        }
        return resources.getBoolean(i3);
    }

    private static int getCompatVersion() {
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod(SettingsStatisticsConstant.METHOD_GET, String.class);
            String str = (String) method.invoke(null, THEME_VERSION_KEY);
            int i3 = TextUtils.isEmpty(str) ? 0 : Integer.parseInt(str.trim());
            if (i3 != 0) {
                return i3;
            }
            String str2 = (String) method.invoke(null, COUICompatUtil.getInstance().getThemeVerisonName());
            return !TextUtils.isEmpty(str2) ? Integer.parseInt(str2.trim()) : i3;
        } catch (Exception e3) {
            COUILog.e(TAG, "getCompatVersion e: " + e3);
            return 0;
        }
    }

    private OplusExtraConfiguration getExtraConfig(Configuration configuration) {
        OplusBaseConfiguration oplusBaseConfiguration = (OplusBaseConfiguration) typeCasting(OplusBaseConfiguration.class, configuration);
        if (oplusBaseConfiguration == null) {
            return null;
        }
        return oplusBaseConfiguration.mOplusExtraConfiguration;
    }

    public static COUIThemeOverlay getInstance() {
        return SingleTone.access$000();
    }

    private int getResId(Context context, String str, String str2) {
        if (context.getResources() == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(context.getPackageName())) {
            return 0;
        }
        return context.getResources().getIdentifier(str, str2, context.getPackageName());
    }

    private int getThemeArrayId(Context context, int i3, int i4) {
        int resourceId = 0;
        if (i3 > 0 && context.getResources() != null) {
            Resources resources = context.getResources();
            int i5 = mCompatVersion;
            if (i5 > COMPAT_VERSION) {
                TypedArray typedArrayObtainTypedArray = resources.obtainTypedArray(R.array.coui_theme_arrays_ids);
                resourceId = typedArrayObtainTypedArray.length() >= i3 ? typedArrayObtainTypedArray.getResourceId(i3 - 1, 0) : 0;
                typedArrayObtainTypedArray.recycle();
                return resourceId;
            }
            if (i5 == COMPAT_VERSION) {
                int resId = getResId(context, mThemeR ? "coui_theme_arrays_ids_patch_r" : "coui_theme_arrays_ids_patch_o", "array");
                if (mThemeO && i4 == 1048576) {
                    resId = R.array.coui_theme_arrays_ids;
                }
                if (resId != 0) {
                    TypedArray typedArrayObtainTypedArray2 = resources.obtainTypedArray(resId);
                    resourceId = typedArrayObtainTypedArray2.length() >= i3 ? typedArrayObtainTypedArray2.getResourceId(i3 - 1, 0) : 0;
                    typedArrayObtainTypedArray2.recycle();
                    return resourceId;
                }
            } else {
                int resId2 = getResId(context, mThemeR ? "coui_theme_arrays_ids_repatch_r" : "coui_theme_arrays_ids_repatch_o", "array");
                if (resId2 != 0) {
                    TypedArray typedArrayObtainTypedArray3 = resources.obtainTypedArray(resId2);
                    resourceId = typedArrayObtainTypedArray3.length() >= i3 ? typedArrayObtainTypedArray3.getResourceId(i3 - 1, 0) : 0;
                    typedArrayObtainTypedArray3.recycle();
                }
            }
        }
        return resourceId;
    }

    private boolean hasCustomThemePkg(Context context) {
        String packageName = context.getPackageName();
        File file = new File(CUSTOM_THEME_PATH);
        if (file.exists() && !TextUtils.isEmpty(packageName)) {
            if (new File(file, packageName).exists()) {
                return true;
            }
            File[] fileArrListFiles = file.listFiles();
            if (fileArrListFiles != null && fileArrListFiles.length != 0) {
                String string = Settings.System.getString(context.getContentResolver(), CUSTOM_THEME_PATH_SETTING);
                if (!TextUtils.isEmpty(string)) {
                    return new File(string, packageName).exists();
                }
            }
        }
        return false;
    }

    private boolean hasDataThemePkg(Context context) {
        String packageName = context.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        OplusExtraConfiguration extraConfig = getExtraConfig(context.getResources().getConfiguration());
        int i3 = extraConfig != null ? extraConfig.mUserId : 0;
        String str = DATA_THEME_PATH;
        if (i3 > 0) {
            str = DATA_THEME_PATH + i3;
        }
        return new File(str, packageName).exists();
    }

    private boolean isCOUIEnable(Context context) {
        WeakReference<Boolean> weakReference = this.mMetaCaches.get(context.getPackageName());
        Boolean bool = weakReference != null ? weakReference.get() : null;
        if (bool != null) {
            return bool.booleanValue();
        }
        boolean z2 = false;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData.getBoolean(COLOR_MATERIAL_ENABLE)) {
                z2 = true;
            }
            this.mMetaCaches.put(context.getPackageName(), new WeakReference<>(Boolean.valueOf(z2)));
            return z2;
        } catch (PackageManager.NameNotFoundException e3) {
            COUILog.e(TAG, "isCOUIEnable e: " + e3);
            return z2;
        }
    }

    private static boolean isThemeO() {
        String str = Build.MANUFACTURER;
        return str.equals(String.valueOf(new char[]{'O', 'P', 'P', 'O'})) || str.equals(String.valueOf(new char[]{'O', 'p', 'p', 'o'}));
    }

    private static boolean isThemeP() {
        String str = Build.MANUFACTURER;
        return str.equals(String.valueOf(new char[]{'O', 'n', 'e', 'P', 'l', 'u', 's'})) || str.equals(String.valueOf(new char[]{'O', 'N', 'E', 'P', 'L', 'U', 'S'})) || str.equals(String.valueOf(new char[]{'G', 'A', 'L', 'I', 'L', 'E', 'I'})) || str.equals(String.valueOf(new char[]{'g', 'a', 'l', 'i', 'l', 'e', 'i'})) || str.equals(String.valueOf(new char[]{'F', 'A', 'R', 'A', 'D', 'A', 'Y'})) || str.equals(String.valueOf(new char[]{'f', 'a', 'r', 'a', 'd', 'a', 'y'}));
    }

    private static boolean isThemeR() {
        String str = Build.MANUFACTURER;
        return str.equals(String.valueOf(new char[]{'R', 'E', 'A', 'L', 'M', 'E'})) || str.equals(String.valueOf(new char[]{'R', 'e', 'a', 'l', 'm', 'e'})) || str.equals(String.valueOf(new char[]{'r', 'e', 'a', 'l', 'm', 'e'}));
    }

    private void resolveThemeStyle(Context context) {
        int themeArrayId;
        int resId;
        if (context == null || isRejectTheme(context)) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R.attr.couiThemeIdentifier});
        int integer = typedArrayObtainStyledAttributes.getInteger(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        long cOUITheme = getCOUITheme(context.getResources().getConfiguration());
        int i3 = (int) (WebSocketProtocol.PAYLOAD_SHORT_MAX & cOUITheme);
        int i4 = (int) (16711680 & cOUITheme);
        boolean z2 = mCompatVersion < COMPAT_VERSION;
        if (cOUITheme != 0) {
            if (i3 == 0 && i4 == 0) {
                return;
            }
            if (i4 == 131072) {
                setThemeOverlay(R.id.coui_global_theme, R.style.COUIOverlay_Theme_Single_First);
                return;
            }
            if (i4 != 65536) {
                if (i4 == 262144) {
                    themeArrayId = R.array.coui_theme_arrays_default_patch;
                } else if (i4 == 0 || i4 == 1048576) {
                    themeArrayId = getThemeArrayId(context, i3, i4);
                } else {
                    resId = 0;
                    i3 = -1;
                }
                int i5 = integer - 1;
                resId = themeArrayId;
                i3 = i5;
            } else if (mThemeP) {
                resId = getResId(context, z2 ? "coui_theme_arrays_single_repatch_p" : "coui_theme_arrays_single_patch_p", "array");
            } else {
                resId = R.array.coui_theme_arrays_single;
            }
            if (resId == 0 || i3 == -1) {
                return;
            }
            TypedArray typedArrayObtainTypedArray = context.getResources().obtainTypedArray(resId);
            if (typedArrayObtainTypedArray.length() > i3) {
                setThemeOverlay(R.id.coui_global_theme, typedArrayObtainTypedArray.getResourceId(i3, 0));
            }
            typedArrayObtainTypedArray.recycle();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T typeCasting(Class<T> cls, Object obj) {
        if (obj == 0 || !cls.isInstance(obj)) {
            return null;
        }
        return obj;
    }

    public void applyCOUITintIcon(Context context, ImageView imageView, boolean z2) {
        Drawable drawable;
        if (imageView == null || isRejectTheme(context)) {
            return;
        }
        if ((getInstance().isCOUITheme(context) || z2) && (drawable = imageView.getDrawable()) != null) {
            if (drawable instanceof LayerDrawable) {
                COUITintUtil.tintDrawable(((LayerDrawable) drawable).getDrawable(0), COUIContextUtil.getAttrColor(context, R.attr.couiColorPrimaryText));
            } else {
                COUITintUtil.tintDrawable(drawable, COUIContextUtil.getAttrColor(context, R.attr.couiColorPrimaryText));
            }
            COUIDarkModeUtil.setForceDarkAllow(imageView, false);
            imageView.setImageDrawable(drawable);
        }
    }

    public void applyThemeOverlays(Context context) {
        synchronized (this.themeOverlays) {
            try {
                clearThemeOverlays();
                resolveThemeStyle(context);
                for (int i3 = 0; i3 < this.themeOverlays.size(); i3++) {
                    context.setTheme(this.themeOverlays.valueAt(i3));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void clearThemeOverlays() {
        synchronized (this.themeOverlays) {
            this.themeOverlays.clear();
        }
    }

    public long getCOUITheme(Configuration configuration) {
        if (!canReachBaseConfiguration()) {
            return 0L;
        }
        OplusExtraConfiguration extraConfig = getExtraConfig(configuration);
        if (extraConfig != null) {
            return extraConfig.mMaterialColor;
        }
        try {
            Class<?> cls = Class.forName(mThemeOverlayName);
            if (cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]) != null) {
                return ((Long) cls.getMethod("getMaterialColor", Configuration.class).invoke(null, configuration)).longValue();
            }
            return 0L;
        } catch (Exception e3) {
            COUILog.e(TAG, "getCOUITheme e: " + e3);
            return 0L;
        }
    }

    public int getThemeOverlay(int i3) {
        int i4;
        synchronized (this.themeOverlays) {
            i4 = this.themeOverlays.get(i3);
        }
        return i4;
    }

    public boolean isCOUITheme(Context context) {
        long cOUITheme = getCOUITheme(context.getResources().getConfiguration());
        return cOUITheme > 0 && (cOUITheme & SieveCacheKt.NodeLinkMask) != 0;
    }

    public boolean isGreenMaterial(Context context) {
        long cOUITheme = getCOUITheme(context.getResources().getConfiguration());
        return cOUITheme > 0 && (cOUITheme & WebSocketProtocol.PAYLOAD_SHORT_MAX) == 5;
    }

    public boolean isRejectTheme(Context context) {
        OplusExtraConfiguration extraConfig;
        long jLongValue;
        boolean zHasCustomThemePkg;
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration == null || !canReachBaseConfiguration()) {
            return false;
        }
        try {
            extraConfig = getExtraConfig(context.getResources().getConfiguration());
            if (extraConfig != null) {
                try {
                    jLongValue = extraConfig.mThemeChangedFlags;
                } catch (Exception e3) {
                    e = e3;
                    Log.d(TAG, "get extra config failed : " + e.getMessage());
                    jLongValue = 0;
                }
            } else {
                jLongValue = 0;
            }
        } catch (Exception e4) {
            e = e4;
            extraConfig = null;
        }
        if (extraConfig == null) {
            try {
                Class<?> cls = Class.forName(mThemeOverlayName);
                if (cls.newInstance() != null) {
                    jLongValue = ((Long) cls.getMethod("getThemeChangedFlags", Configuration.class).invoke(null, configuration)).longValue();
                }
            } catch (Exception e5) {
                COUILog.e(TAG, "isRejectTheme e: " + e5);
            }
        }
        if ((1 & jLongValue) != 0) {
            zHasCustomThemePkg = (jLongValue & 256) != 0 ? hasCustomThemePkg(context) : hasDataThemePkg(context);
        } else {
            zHasCustomThemePkg = false;
        }
        return zHasCustomThemePkg && (configuration.uiMode & 48) != 32;
    }

    public void setThemeOverlay(int i3, int i4) {
        synchronized (this.themeOverlays) {
            this.themeOverlays.put(i3, i4);
        }
    }

    public boolean shouldResetTheme(Configuration configuration) {
        long cOUITheme = getCOUITheme(configuration);
        return (SieveCacheKt.NodeLinkMask & cOUITheme) == 0 || (cOUITheme & PlaybackStateCompat.ACTION_SET_REPEAT_MODE) != 0;
    }
}