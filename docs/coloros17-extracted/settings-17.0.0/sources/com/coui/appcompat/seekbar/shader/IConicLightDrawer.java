package com.coui.appcompat.seekbar.shader;

import android.graphics.Canvas;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0007H&J\b\u0010\u000b\u001a\u00020\fH&J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\u0010\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\u0012\u0010\u0011\u001a\u00020\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\u0010\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\u0010\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\u0010\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\u0010\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0007H\u0016¨\u0006\u0019"}, d2 = {"Lcom/coui/appcompat/seekbar/shader/IConicLightDrawer;", "", "draw", "", "canvas", "Landroid/graphics/Canvas;", "width", "", "height", "process", "rectWidth", "isSupported", "", "setAlpha1", "value", "setAlpha2", "setConeK", "setConicLightColor", TypedValues.Custom.S_COLOR, "", "setHeight", "setHeightFalloff", "setRtl", "setSecondConeK", "setW0", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface IConicLightDrawer {

    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static void setAlpha1(IConicLightDrawer iConicLightDrawer, float f3) {
        }

        public static void setAlpha2(IConicLightDrawer iConicLightDrawer, float f3) {
        }

        public static void setConeK(IConicLightDrawer iConicLightDrawer, float f3) {
        }

        public static void setConicLightColor(IConicLightDrawer iConicLightDrawer, int i3) {
        }

        public static void setHeight(IConicLightDrawer iConicLightDrawer, float f3) {
        }

        public static void setHeightFalloff(IConicLightDrawer iConicLightDrawer, float f3) {
        }

        public static void setRtl(IConicLightDrawer iConicLightDrawer, float f3) {
        }

        public static void setSecondConeK(IConicLightDrawer iConicLightDrawer, float f3) {
        }

        public static void setW0(IConicLightDrawer iConicLightDrawer, float f3) {
        }
    }

    void draw(Canvas canvas, float width, float height, float process, float rectWidth);

    boolean isSupported();

    void setAlpha1(float value);

    void setAlpha2(float value);

    void setConeK(float value);

    void setConicLightColor(int color);

    void setHeight(float value);

    void setHeightFalloff(float value);

    void setRtl(float value);

    void setSecondConeK(float value);

    void setW0(float value);
}