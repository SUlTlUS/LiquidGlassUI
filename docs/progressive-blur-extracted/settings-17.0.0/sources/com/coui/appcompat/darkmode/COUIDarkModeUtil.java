package com.coui.appcompat.darkmode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes.dex */
public class COUIDarkModeUtil {
    private static final int COLOR_DDDDDD = -2236963;
    private static final int ZERO = 0;

    private static ColorFilter getDarkFilter() {
        return new LightingColorFilter(COLOR_DDDDDD, 0);
    }

    public static boolean isCOUIDarkThemeOrNightMode(Context context) {
        if (context == null || context.getTheme() == null) {
            return false;
        }
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(R.styleable.COUITheme);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUITheme_isCOUIDarkTheme, false);
        boolean z3 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUITheme_isCOUILightTheme, false);
        typedArrayObtainStyledAttributes.recycle();
        if (z2) {
            return true;
        }
        if (z3) {
            return false;
        }
        return isNightMode(context);
    }

    public static boolean isNightMode(Context context) {
        return 32 == (context.getResources().getConfiguration().uiMode & 48);
    }

    public static int makeDark(int i3) {
        float[] fArr = new float[3];
        ColorUtils.colorToHSL(i3, fArr);
        float f3 = fArr[2];
        float f4 = 1.0f - f3;
        if (f4 >= f3) {
            return i3;
        }
        fArr[2] = f4;
        return ColorUtils.HSLToColor(fArr);
    }

    public static int makeDarkLimit(int i3, float f3) {
        float[] fArr = new float[3];
        ColorUtils.colorToHSL(i3, fArr);
        float fMax = Math.max(f3, 1.0f - fArr[2]);
        if (fMax >= fArr[2]) {
            return i3;
        }
        fArr[2] = fMax;
        return ColorUtils.HSLToColor(fArr);
    }

    public static void makeDrawableDark(Drawable drawable) {
        if (drawable != null) {
            drawable.setColorFilter(getDarkFilter());
        }
    }

    public static void makeImageViewDark(ImageView imageView) {
        if (imageView != null) {
            imageView.setColorFilter(getDarkFilter());
        }
    }

    public static int makeLight(int i3) {
        float[] fArr = new float[3];
        ColorUtils.colorToHSL(i3, fArr);
        float f3 = fArr[2];
        float f4 = 1.0f - f3;
        if (f4 <= f3) {
            return i3;
        }
        fArr[2] = f4;
        return ColorUtils.HSLToColor(fArr);
    }

    public static void setForceDarkAllow(View view, boolean z2) {
        view.setForceDarkAllowed(z2);
    }
}