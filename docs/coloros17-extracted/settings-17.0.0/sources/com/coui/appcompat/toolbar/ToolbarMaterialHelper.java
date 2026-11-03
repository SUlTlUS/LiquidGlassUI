package com.coui.appcompat.toolbar;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import com.coui.appcompat.version.COUIVersionUtil;
import com.support.toolbar.R;

/* JADX INFO: loaded from: classes.dex */
public class ToolbarMaterialHelper {

    public static final class MaterialEffectOsHolder {
        static final boolean SUPPORTED;

        static {
            SUPPORTED = Build.VERSION.SDK_INT > 36 || ToolbarMaterialHelper.checkOplusOSVersion(40);
        }

        private MaterialEffectOsHolder() {
        }
    }

    private ToolbarMaterialHelper() {
    }

    public static void applyIconButtonSize(View view) {
        Context context = view.getContext();
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.coui_action_menu_item_min_width);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.coui_action_menu_item_min_height);
        int dimensionPixelSize3 = context.getResources().getDimensionPixelSize(R.dimen.coui_toolbar_menu_bg_padding_horizontal);
        view.setMinimumWidth(dimensionPixelSize);
        view.setMinimumHeight(dimensionPixelSize2);
        view.setPadding(dimensionPixelSize3, dimensionPixelSize3, dimensionPixelSize3, dimensionPixelSize3);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = -2;
            layoutParams.height = -2;
            view.setLayoutParams(layoutParams);
        }
    }

    public static void applyTextButtonSize(View view) {
        Context context = view.getContext();
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.coui_action_text_menu_item_min_width);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.coui_action_menu_item_min_height);
        int dimensionPixelSize3 = context.getResources().getDimensionPixelSize(R.dimen.coui_toolbar_text_menu_bg_padding_horizontal);
        view.setMinimumWidth(dimensionPixelSize);
        view.setMinimumHeight(dimensionPixelSize2);
        view.setPadding(dimensionPixelSize3, 0, dimensionPixelSize3, 0);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = -2;
            layoutParams.height = -2;
            view.setLayoutParams(layoutParams);
        }
    }

    public static boolean checkOplusOSVersion(int i3) {
        return COUIVersionUtil.getOSVersionCode() >= i3;
    }

    public static boolean isMaterialEffectOsSupported() {
        return MaterialEffectOsHolder.SUPPORTED;
    }
}