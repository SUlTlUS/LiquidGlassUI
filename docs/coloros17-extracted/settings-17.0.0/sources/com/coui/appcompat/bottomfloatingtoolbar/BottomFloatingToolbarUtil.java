package com.coui.appcompat.bottomfloatingtoolbar;

import com.coui.appcompat.version.COUIVersionUtil;

/* JADX INFO: loaded from: classes7.dex */
public final class BottomFloatingToolbarUtil {
    private static volatile int sOs17OrAboveCached = -1;

    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r1v2 */
    public static boolean isOs17OrAbove() {
        int i3 = sOs17OrAboveCached;
        if (i3 >= 0) {
            return i3 == 1;
        }
        ?? r2 = COUIVersionUtil.getOSVersionCode() >= 40 ? 1 : 0;
        sOs17OrAboveCached = r2;
        return r2;
    }
}