package com.coui.appcompat.seekbar.handler;

import android.graphics.RectF;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.oplus.dmp.sdk.search.SearchProtocol;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0018\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH&J\u0018\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH&J \u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011H&J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\u0007H&¨\u0006\u0016"}, d2 = {"Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;", "", "calculateBackgroundRect", "", "rect", "Landroid/graphics/RectF;", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "calculateProgressRect", "calculateSecondaryProgressRect", "calculateThumbPosition", "", "progress", SearchProtocol.QUERY_LENGTH, "isRtl", "", "getCenterCoordinate", "", "getProgressStart", "getSeekBarLength", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface IOrientationHandler {
    void calculateBackgroundRect(RectF rect, COUISeekBarCore seekBar, SeekBarState state);

    void calculateProgressRect(RectF rect, SeekBarState state);

    void calculateSecondaryProgressRect(RectF rect, SeekBarState state);

    float calculateThumbPosition(float progress, float length, boolean isRtl);

    int getCenterCoordinate(COUISeekBarCore seekBar);

    float getProgressStart(COUISeekBarCore seekBar);

    float getSeekBarLength(COUISeekBarCore seekBar);
}