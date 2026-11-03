package com.coui.appcompat;

import android.util.Log;
import com.oplus.os.OplusBuild;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\b\bÁ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0007J\b\u0010\u0016\u001a\u00020\u0004H\u0007J\u0006\u0010\u0017\u001a\u00020\u0004J\b\u0010\u0018\u001a\u00020\u0013H\u0007J\b\u0010\u0019\u001a\u00020\u0013H\u0007J\b\u0010\u001a\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lcom/coui/appcompat/COUIEffectVersionUtil;", "", "()V", "BACKGROUND_BLUR_MAJOR_VERSION", "", "BACKGROUND_BLUR_SUB_VERSION", "CAUSTIC_SHADOW_MAJOR_VERSION", "CAUSTIC_SHADOW_SUB_VERSION", "CROSS_WINDOW_BLUR_MAJOR_VERSION", "CROSS_WINDOW_BLUR_SUB_VERSION", "MATERIAL_MAJOR_VERSION", "MATERIAL_SUB_VERSION", "OSVersionCode", "TAG", "", "UNKNOWN", "USER_HANDLE_MAJOR_VERSION", "USER_HANDLE_SUB_VERSION", "checkOPlusViewSubSDK", "", "majorVersion", "sdkSubVersion", "getOSVersionCode", "getSDKSubVersion", "isCausticShadowVersion", "isMaterialVersion", "isRuntimeShaderVersion", "coui-support-effect_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class COUIEffectVersionUtil {
    public static final int BACKGROUND_BLUR_MAJOR_VERSION = 33;
    public static final int BACKGROUND_BLUR_SUB_VERSION = 34;
    public static final int CAUSTIC_SHADOW_MAJOR_VERSION = 40;
    public static final int CAUSTIC_SHADOW_SUB_VERSION = 12;
    public static final int CROSS_WINDOW_BLUR_MAJOR_VERSION = 34;
    public static final int CROSS_WINDOW_BLUR_SUB_VERSION = 2;
    public static final int MATERIAL_MAJOR_VERSION = 40;
    public static final int MATERIAL_SUB_VERSION = 2;
    private static final String TAG = "COUIEffectVersionUtil";
    public static final int UNKNOWN = 0;
    public static final int USER_HANDLE_MAJOR_VERSION = 27;
    public static final int USER_HANDLE_SUB_VERSION = 11;
    public static final COUIEffectVersionUtil INSTANCE = new COUIEffectVersionUtil();
    private static int OSVersionCode = -1;

    private COUIEffectVersionUtil() {
    }

    @JvmStatic
    public static final boolean checkOPlusViewSubSDK(int majorVersion, int sdkSubVersion) {
        int oSVersionCode = getOSVersionCode();
        if (oSVersionCode <= majorVersion) {
            return oSVersionCode == majorVersion && INSTANCE.getSDKSubVersion() >= sdkSubVersion;
        }
        return true;
    }

    @JvmStatic
    public static final int getOSVersionCode() {
        int i3;
        int i4 = OSVersionCode;
        if (i4 != -1) {
            return i4;
        }
        try {
            i3 = OplusBuild.VERSION.SDK_VERSION;
        } catch (Throwable th) {
            String message = th.getMessage();
            if (message != null) {
                Log.e(TAG, "getOSVersionCode error: " + message);
            }
            i3 = 0;
        }
        OSVersionCode = i3;
        return i3;
    }

    @JvmStatic
    public static final boolean isCausticShadowVersion() {
        return checkOPlusViewSubSDK(40, 12);
    }

    @JvmStatic
    public static final boolean isMaterialVersion() {
        return checkOPlusViewSubSDK(40, 2);
    }

    @JvmStatic
    public static final boolean isRuntimeShaderVersion() {
        return true;
    }

    public final int getSDKSubVersion() {
        try {
            return OplusBuild.VERSION.SDK_SUB_VERSION;
        } catch (Throwable th) {
            String message = th.getMessage();
            if (message == null) {
                return 0;
            }
            Log.e(TAG, "getSDKSubVersion error: " + message);
            return 0;
        }
    }
}