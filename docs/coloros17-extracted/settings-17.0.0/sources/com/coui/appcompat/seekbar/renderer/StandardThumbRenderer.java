package com.coui.appcompat.seekbar.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J \u0010\u0019\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u000eH\u0014J\u0018\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u000eH\u0014J\u0010\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\bH\u0016R\u0014\u0010\u0003\u001a\u00020\u0004X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\bX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000eX\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001f"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/StandardThumbRenderer;", "Lcom/coui/appcompat/seekbar/renderer/IThumbRenderer;", "()V", "mPaint", "Landroid/graphics/Paint;", "getMPaint", "()Landroid/graphics/Paint;", "mThumbColor", "", "getMThumbColor", "()I", "setMThumbColor", "(I)V", "mThumbRect", "Landroid/graphics/RectF;", "getMThumbRect", "()Landroid/graphics/RectF;", "calculateThumbPosition", "", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "drawThumb", "", "canvas", "Landroid/graphics/Canvas;", "drawThumbRoundRect", "rect", "fillThumbRect", "outRect", "setThumbColor", TypedValues.Custom.S_COLOR, "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class StandardThumbRenderer implements IThumbRenderer {
    private int mThumbColor;
    private final Paint mPaint = new Paint(1);
    private final RectF mThumbRect = new RectF();

    @Override // com.coui.appcompat.seekbar.renderer.IThumbRenderer
    public float calculateThumbPosition(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        return state.getThumbPosition();
    }

    @Override // com.coui.appcompat.seekbar.renderer.IThumbRenderer
    public void drawThumb(Canvas canvas, SeekBarState state) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if (state.getShowThumb()) {
            fillThumbRect(state, this.mThumbRect);
            drawThumbRoundRect(canvas, state, this.mThumbRect);
        }
    }

    public void drawThumbRoundRect(Canvas canvas, SeekBarState state, RectF rect) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(rect, "rect");
        float curThumbRadius = state.getCurThumbRadius();
        COUISeekBarCore view = state.getView();
        if (state.getThumbShadowRadiusSize() > 0 && view != null && view.isEnabled()) {
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setShadowLayer(state.getThumbShadowRadiusSize(), 0.0f, state.getThumbShadowOffsetY(), state.getThumbShadowColor());
        }
        this.mPaint.setColor(this.mThumbColor);
        this.mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rect, curThumbRadius, curThumbRadius, this.mPaint);
        if (state.getThumbShadowRadiusSize() <= 0 || view == null || !view.isEnabled()) {
            return;
        }
        this.mPaint.clearShadowLayer();
    }

    public void fillThumbRect(SeekBarState state, RectF outRect) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(outRect, "outRect");
        float fCalculateThumbPosition = calculateThumbPosition(state);
        float curThumbRadius = state.getCurThumbRadius();
        if (state.getIsVertical()) {
            outRect.set(state.getCenterX() - curThumbRadius, fCalculateThumbPosition - curThumbRadius, state.getCenterX() + curThumbRadius, fCalculateThumbPosition + curThumbRadius);
        } else {
            outRect.set(fCalculateThumbPosition - curThumbRadius, state.getCenterY() - curThumbRadius, fCalculateThumbPosition + curThumbRadius, state.getCenterY() + curThumbRadius);
        }
    }

    public final Paint getMPaint() {
        return this.mPaint;
    }

    public final int getMThumbColor() {
        return this.mThumbColor;
    }

    public final RectF getMThumbRect() {
        return this.mThumbRect;
    }

    public final void setMThumbColor(int i3) {
        this.mThumbColor = i3;
    }

    @Override // com.coui.appcompat.seekbar.renderer.IThumbRenderer
    public void setThumbColor(int color) {
        this.mThumbColor = color;
        this.mPaint.setColor(color);
    }
}