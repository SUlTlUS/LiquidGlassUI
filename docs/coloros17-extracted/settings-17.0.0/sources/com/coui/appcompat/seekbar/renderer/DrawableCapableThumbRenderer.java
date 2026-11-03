package com.coui.appcompat.seekbar.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\u00062\b\u0010\f\u001a\u0004\u0018\u00010\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/DrawableCapableThumbRenderer;", "Lcom/coui/appcompat/seekbar/renderer/StandardThumbRenderer;", "()V", "mThumbBitmap", "Landroid/graphics/Bitmap;", "drawThumb", "", "canvas", "Landroid/graphics/Canvas;", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "setThumbBitmap", "bitmap", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DrawableCapableThumbRenderer extends StandardThumbRenderer {
    private Bitmap mThumbBitmap;

    @Override // com.coui.appcompat.seekbar.renderer.StandardThumbRenderer, com.coui.appcompat.seekbar.renderer.IThumbRenderer
    public void drawThumb(Canvas canvas, SeekBarState state) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if (state.getShowThumb()) {
            fillThumbRect(state, getMThumbRect());
            Bitmap bitmap = this.mThumbBitmap;
            if (bitmap == null || bitmap.isRecycled()) {
                drawThumbRoundRect(canvas, state, getMThumbRect());
            } else {
                canvas.drawBitmap(bitmap, (Rect) null, getMThumbRect(), getMPaint());
            }
        }
    }

    public final void setThumbBitmap(Bitmap bitmap) {
        this.mThumbBitmap = bitmap;
    }
}