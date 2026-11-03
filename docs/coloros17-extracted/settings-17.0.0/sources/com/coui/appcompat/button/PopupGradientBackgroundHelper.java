package com.coui.appcompat.button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;

/* JADX INFO: loaded from: classes7.dex */
final class PopupGradientBackgroundHelper {
    private static final float CENTER_X_RATIO = 0.5f;
    private static final float DISABLED_ALPHA = 0.3f;
    private static final float KX_DARK = 0.80444443f;
    private static final float KX_LIGHT = 1.0951731f;
    private static final float KY_DARK = 1.0f;
    private static final float KY_LIGHT = 1.3613929f;
    private static final float LIGHT_CENTER_Y_RATIO = -0.11321429f;
    private static final int LIGHT_RADIAL_COLOR_0 = -16777216;
    private static final float LIGHT_RADIAL_STOP_0 = 0.040197f;
    private static final int LIGHT_RADIAL_STOP_7 = 22698;
    private static final float RADIAL_STOP_END = 1.0f;
    private static final float RADIAL_STOP_LEADING = 0.040197f;
    private static final int LIGHT_RADIAL_COLOR_1 = -16776183;
    private static final int LIGHT_RADIAL_COLOR_2 = -16774895;
    private static final int LIGHT_RADIAL_COLOR_3 = -16772573;
    private static final int LIGHT_RADIAL_COLOR_4 = -16767931;
    private static final int LIGHT_RADIAL_COLOR_5 = -16764577;
    private static final int LIGHT_RADIAL_COLOR_6 = -16761224;
    private static final int LIGHT_RADIAL_COLOR_7 = -16754518;
    private static final int[] LIGHT_COLOR_STOPS = {-16777216, LIGHT_RADIAL_COLOR_1, LIGHT_RADIAL_COLOR_2, LIGHT_RADIAL_COLOR_3, LIGHT_RADIAL_COLOR_4, LIGHT_RADIAL_COLOR_5, LIGHT_RADIAL_COLOR_6, LIGHT_RADIAL_COLOR_7};
    private static final float LIGHT_RADIAL_STOP_1 = 0.12349f;
    private static final float LIGHT_RADIAL_STOP_2 = 0.20679f;
    private static final float LIGHT_RADIAL_STOP_3 = 0.37339f;
    private static final float LIGHT_RADIAL_STOP_4 = 0.70658f;
    private static final float LIGHT_RADIAL_STOP_5 = 0.77993f;
    private static final float LIGHT_RADIAL_STOP_6 = 0.85329f;
    private static final float[] LIGHT_RADIAL_STOPS = {0.040197f, LIGHT_RADIAL_STOP_1, LIGHT_RADIAL_STOP_2, LIGHT_RADIAL_STOP_3, LIGHT_RADIAL_STOP_4, LIGHT_RADIAL_STOP_5, LIGHT_RADIAL_STOP_6, 22698.0f};
    private static final float[] DARK_RADIAL_STOPS = {0.040197f, 1.0f};
    private static final int NIGHT_RADIAL_COLOR_0 = -1511681;
    private static final int NIGHT_RADIAL_COLOR_1 = -2759681;
    private static final int[] DARK_COLOR_STOPS = {NIGHT_RADIAL_COLOR_0, NIGHT_RADIAL_COLOR_1};

    public static final class GradientParams {
        int[] colors;
        boolean dark;
        int[] disabledColors;
        int[] enabledColors;
        RadialGradient gradient;
        int height;
        Matrix localMatrix;
        int width;
    }

    private PopupGradientBackgroundHelper() {
    }

    private static int applyAlpha(int i3, float f3) {
        return Color.argb((int) (Color.alpha(i3) * f3), Color.red(i3), Color.green(i3), Color.blue(i3));
    }

    private static RadialGradient createRadialGradient(RectF rectF, int[] iArr, float[] fArr, boolean z2, Matrix matrix) {
        float fHeight;
        float fWidth;
        float fHeight2;
        float fWidth2 = rectF.width() * 0.5f;
        if (z2) {
            fHeight = rectF.top;
            fWidth = rectF.width() * KX_DARK;
            fHeight2 = rectF.height() * 1.0f;
        } else {
            fHeight = rectF.top + (rectF.height() * LIGHT_CENTER_Y_RATIO);
            fWidth = rectF.width() * KX_LIGHT;
            fHeight2 = rectF.height() * KY_LIGHT;
        }
        float f3 = fHeight2;
        float f4 = fHeight;
        RadialGradient radialGradient = new RadialGradient(fWidth2, f4, f3, iArr, fArr, Shader.TileMode.CLAMP);
        float f5 = fWidth / f3;
        if (f5 != 1.0f) {
            if (matrix == null) {
                matrix = new Matrix();
            }
            matrix.reset();
            matrix.setScale(f5, 1.0f, fWidth2, f4);
            radialGradient.setLocalMatrix(matrix);
        }
        return radialGradient;
    }

    public static void drawRadialGradient(Canvas canvas, RectF rectF, Paint paint, Context context, boolean z2, GradientParams gradientParams) {
        boolean zIsCOUIDarkThemeOrNightMode = COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(context);
        ensureGradientColors(context, gradientParams, zIsCOUIDarkThemeOrNightMode);
        int[] iArr = z2 ? gradientParams.enabledColors : gradientParams.disabledColors;
        int iWidth = (int) rectF.width();
        int iHeight = (int) rectF.height();
        if (gradientParams.gradient == null || gradientParams.width != iWidth || gradientParams.height != iHeight || gradientParams.dark != zIsCOUIDarkThemeOrNightMode || gradientParams.colors != iArr || gradientParams.localMatrix == null) {
            gradientParams.gradient = createRadialGradient(rectF, zIsCOUIDarkThemeOrNightMode ? DARK_COLOR_STOPS : LIGHT_COLOR_STOPS, zIsCOUIDarkThemeOrNightMode ? DARK_RADIAL_STOPS : LIGHT_RADIAL_STOPS, zIsCOUIDarkThemeOrNightMode, gradientParams.localMatrix);
            gradientParams.width = iWidth;
            gradientParams.height = iHeight;
            gradientParams.dark = zIsCOUIDarkThemeOrNightMode;
            gradientParams.colors = iArr;
            gradientParams.localMatrix = new Matrix();
        }
        paint.setShader(gradientParams.gradient);
        canvas.drawRect(rectF, paint);
        paint.setShader(null);
    }

    private static void ensureGradientColors(Context context, GradientParams gradientParams, boolean z2) {
        if (gradientParams.enabledColors != null && gradientParams.dark == z2) {
            return;
        }
        int[] iArr = z2 ? DARK_COLOR_STOPS : LIGHT_COLOR_STOPS;
        gradientParams.enabledColors = iArr;
        gradientParams.disabledColors = new int[iArr.length];
        int i3 = 0;
        while (true) {
            int[] iArr2 = gradientParams.enabledColors;
            if (i3 >= iArr2.length) {
                gradientParams.gradient = null;
                return;
            } else {
                gradientParams.disabledColors[i3] = applyAlpha(iArr2[i3], 0.3f);
                i3++;
            }
        }
    }
}