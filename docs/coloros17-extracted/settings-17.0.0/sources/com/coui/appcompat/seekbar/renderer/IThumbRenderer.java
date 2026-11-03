package com.coui.appcompat.seekbar.renderer;

import android.graphics.Canvas;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fH&¨\u0006\r"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/IThumbRenderer;", "", "calculateThumbPosition", "", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "drawThumb", "", "canvas", "Landroid/graphics/Canvas;", "setThumbColor", TypedValues.Custom.S_COLOR, "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface IThumbRenderer {
    float calculateThumbPosition(SeekBarState state);

    void drawThumb(Canvas canvas, SeekBarState state);

    void setThumbColor(int color);
}