package com.coui.appcompat.seekbar.renderer;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u000bH&¨\u0006\f"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;", "", "drawProgress", "", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "setProgressColor", TypedValues.Custom.S_COLOR, "Landroid/content/res/ColorStateList;", "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface IProgressRenderer {
    void drawProgress(Canvas canvas, SeekBarState state);

    void setProgressColor(int color);

    void setProgressColor(ColorStateList color);
}