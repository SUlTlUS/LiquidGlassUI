package com.coui.appcompat.theme;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.core.graphics.ColorUtils;

/* JADX INFO: loaded from: classes7.dex */
public class COUIThemeUtils {
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<>();
    public static final int[] DISABLED_STATE_SET = {-16842910};
    public static final int[] FOCUSED_STATE_SET = {16842908};
    public static final int[] ACTIVATED_STATE_SET = {R.attr.state_activated};
    public static final int[] PRESSED_STATE_SET = {16842919};
    public static final int[] CHECKED_STATE_SET = {R.attr.state_checked};
    public static final int[] SELECTED_STATE_SET = {16842913};
    public static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET = {-16842919, -16842908};
    static final int[] EMPTY_STATE_SET = new int[0];
    private static final int[] TEMP_ARRAY = new int[1];

    public static ColorStateList createDisabledStateList(int i3, int i4) {
        return new ColorStateList(new int[][]{DISABLED_STATE_SET, EMPTY_STATE_SET}, new int[]{i4, i3});
    }

    public static int getDisabledThemeAttrColor(Context context, int i3) {
        ColorStateList themeAttrColorStateList = getThemeAttrColorStateList(context, i3);
        if (themeAttrColorStateList != null && themeAttrColorStateList.isStateful()) {
            return themeAttrColorStateList.getColorForState(DISABLED_STATE_SET, themeAttrColorStateList.getDefaultColor());
        }
        TypedValue typedValue = getTypedValue();
        context.getTheme().resolveAttribute(R.attr.disabledAlpha, typedValue, true);
        return getThemeAttrColor(context, i3, typedValue.getFloat());
    }

    public static int getThemeAttrColor(Context context, int i3) {
        int[] iArr = TEMP_ARRAY;
        iArr[0] = i3;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes((AttributeSet) null, iArr);
        try {
            return typedArrayObtainStyledAttributes.getColor(0, 0);
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public static ColorStateList getThemeAttrColorStateList(Context context, int i3) {
        int[] iArr = TEMP_ARRAY;
        iArr[0] = i3;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes((AttributeSet) null, iArr);
        try {
            return typedArrayObtainStyledAttributes.getColorStateList(0);
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    private static TypedValue getTypedValue() {
        ThreadLocal<TypedValue> threadLocal = TL_TYPED_VALUE;
        TypedValue typedValue = threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }

    public static int getThemeAttrColor(Context context, int i3, float f3) {
        int themeAttrColor = getThemeAttrColor(context, i3);
        return ColorUtils.setAlphaComponent(themeAttrColor, Math.round(Color.alpha(themeAttrColor) * f3));
    }
}