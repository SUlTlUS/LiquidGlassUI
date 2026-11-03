package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final class PopupMenuRtAnimations {
    private static final boolean ENABLE_POPUP_RT_ANIMATION = false;

    private PopupMenuRtAnimations() {
    }

    public static boolean canUseFullViewRtPopupMotion(View view, View view2) {
        return (!isAnimateKitRtContextAvailable() || view == null || resolveRtAlphaTarget(view, view2) == null) ? false : true;
    }

    public static boolean isAnimateKitRtContextAvailable() {
        return isRtWriteCapabilityAvailable();
    }

    public static boolean isRtCapabilityAvailable() {
        return isRtWriteCapabilityAvailable();
    }

    public static boolean isRtWriteCapabilityAvailable() {
        return false;
    }

    public static View resolveRtAlphaTarget(View view, View view2) {
        if (view == null) {
            return view2;
        }
        return (((view instanceof RoundFrameLayout) && ((RoundFrameLayout) view).getUseBackgroundBlur()) || view2 == null) ? view : view2;
    }

    public static boolean usesBlurBackground(View view) {
        return (view instanceof RoundFrameLayout) && ((RoundFrameLayout) view).getUseBackgroundBlur();
    }
}