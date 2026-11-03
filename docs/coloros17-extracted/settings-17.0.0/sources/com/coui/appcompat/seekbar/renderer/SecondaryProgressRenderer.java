package com.coui.appcompat.seekbar.renderer;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import com.coui.appcompat.seekbar.handler.IOrientationHandler;
import com.oplus.graphics.OplusCanvas;
import com.oplus.graphics.OplusPathAdapter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0006H\u0002J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\nH\u0016J\u0010\u0010\u0016\u001a\u00020\u000e2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015J\u000e\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/SecondaryProgressRenderer;", "Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;", "()V", "mOplusCanvas", "Lcom/oplus/graphics/OplusCanvas;", "mOplusCanvasRef", "Landroid/graphics/Canvas;", "mPaint", "Landroid/graphics/Paint;", "mSecondaryProgressColor", "", "mSecondaryProgressRect", "Landroid/graphics/RectF;", "drawProgress", "", "canvas", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "ensureOplusCanvas", "setProgressColor", TypedValues.Custom.S_COLOR, "Landroid/content/res/ColorStateList;", "setSecondaryProgressColor", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SecondaryProgressRenderer implements IProgressRenderer {
    private OplusCanvas mOplusCanvas;
    private Canvas mOplusCanvasRef;
    private final Paint mPaint;
    private int mSecondaryProgressColor;
    private final RectF mSecondaryProgressRect;

    public SecondaryProgressRenderer() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.FILL);
        this.mPaint = paint;
        this.mSecondaryProgressRect = new RectF();
    }

    private final OplusCanvas ensureOplusCanvas(Canvas canvas) {
        if (canvas != this.mOplusCanvasRef || this.mOplusCanvas == null) {
            this.mOplusCanvas = new OplusCanvas(canvas);
            this.mOplusCanvasRef = canvas;
        }
        OplusCanvas oplusCanvas = this.mOplusCanvas;
        Intrinsics.checkNotNull(oplusCanvas);
        return oplusCanvas;
    }

    @Override // com.coui.appcompat.seekbar.renderer.IProgressRenderer
    public void drawProgress(Canvas canvas, SeekBarState state) {
        COUISeekBarCore view;
        IOrientationHandler mOrientationHandler;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if (!state.getShowProgress() || !state.getShowSecondaryProgress() || state.getSecondaryProgress() <= state.getMin() || (view = state.getView()) == null || (mOrientationHandler = view.getMOrientationHandler()) == null) {
            return;
        }
        int max = state.getMax() - state.getMin();
        state.setSecondaryProgressScale(max > 0 ? (state.getSecondaryProgress() - state.getMin()) / max : 0.0f);
        state.setSecondaryProgressScale(RangesKt.coerceIn(state.getSecondaryProgressScale(), 0.0f, 1.0f));
        mOrientationHandler.calculateSecondaryProgressRect(this.mSecondaryProgressRect, state);
        this.mPaint.setColor(state.getSecondaryProgressColor());
        float progressRectHeight = view.getProgressRectHeight(state) / 2;
        int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
        if (smoothStyleType == 0) {
            if (state.getProgressRoundCornerWeight() == 0.0f) {
                canvas.drawRoundRect(this.mSecondaryProgressRect, progressRectHeight, progressRectHeight, this.mPaint);
                return;
            } else {
                ensureOplusCanvas(canvas).drawSmoothRoundRect(this.mSecondaryProgressRect, progressRectHeight, progressRectHeight, this.mPaint, state.getProgressRoundCornerWeight());
                return;
            }
        }
        if (smoothStyleType != 1) {
            canvas.drawRoundRect(this.mSecondaryProgressRect, progressRectHeight, progressRectHeight, this.mPaint);
            return;
        }
        COUISeekBarCore.SmoothRoundCornerHelper mProgressSmoothRoundCornerHelper = view.getMProgressSmoothRoundCornerHelper();
        OplusPathAdapter pathAdapter = mProgressSmoothRoundCornerHelper != null ? mProgressSmoothRoundCornerHelper.getPathAdapter() : null;
        Path mProgressPath = view.getMProgressPath();
        if (pathAdapter == null || mProgressPath == null) {
            canvas.drawRoundRect(this.mSecondaryProgressRect, progressRectHeight, progressRectHeight, this.mPaint);
            return;
        }
        mProgressPath.reset();
        canvas.save();
        pathAdapter.addSmoothRoundRect(this.mSecondaryProgressRect, progressRectHeight, progressRectHeight, Path.Direction.CCW);
        canvas.clipPath(mProgressPath);
        canvas.drawColor(state.getSecondaryProgressColor());
        canvas.restore();
    }

    @Override // com.coui.appcompat.seekbar.renderer.IProgressRenderer
    public void setProgressColor(int color) {
        this.mSecondaryProgressColor = color;
        this.mPaint.setColor(color);
    }

    public final void setSecondaryProgressColor(int color) {
        setProgressColor(color);
    }

    public final void setSecondaryProgressColor(ColorStateList color) {
        if (color != null) {
            setProgressColor(color);
        }
    }

    @Override // com.coui.appcompat.seekbar.renderer.IProgressRenderer
    public void setProgressColor(ColorStateList color) {
        Intrinsics.checkNotNullParameter(color, "color");
        int defaultColor = color.getDefaultColor();
        this.mSecondaryProgressColor = defaultColor;
        this.mPaint.setColor(defaultColor);
    }
}