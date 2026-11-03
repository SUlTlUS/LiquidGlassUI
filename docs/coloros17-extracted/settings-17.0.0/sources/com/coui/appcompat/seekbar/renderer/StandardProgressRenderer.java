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
import com.oplus.graphics.OplusPath;
import com.oplus.graphics.OplusPathAdapter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0010\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0006H\u0002J\u0010\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\nH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/StandardProgressRenderer;", "Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;", "()V", "mOplusCanvas", "Lcom/oplus/graphics/OplusCanvas;", "mOplusCanvasRef", "Landroid/graphics/Canvas;", "mPaint", "Landroid/graphics/Paint;", "mProgressColor", "", "mProgressRect", "Landroid/graphics/RectF;", "mVerticalClipPath", "Landroid/graphics/Path;", "mVerticalClipRect", "drawProgress", "", "canvas", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "ensureOplusCanvas", "setProgressColor", TypedValues.Custom.S_COLOR, "Landroid/content/res/ColorStateList;", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class StandardProgressRenderer implements IProgressRenderer {
    private static final float PROGRESS_DRAW_SCALE_FACTOR = 2.0f;
    private OplusCanvas mOplusCanvas;
    private Canvas mOplusCanvasRef;
    private int mProgressColor;
    private final Paint mPaint = new Paint(1);
    private final RectF mProgressRect = new RectF();
    private final RectF mVerticalClipRect = new RectF();
    private final Path mVerticalClipPath = new Path();

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
        if (!state.getShowProgress() || (view = state.getView()) == null || (mOrientationHandler = view.getMOrientationHandler()) == null) {
            return;
        }
        mOrientationHandler.calculateProgressRect(this.mProgressRect, state);
        this.mPaint.setColor(state.getProgressColor());
        this.mPaint.setStyle(Paint.Style.FILL);
        boolean zIsVertical = view.isVertical();
        if (zIsVertical && !state.getShowThumb()) {
            float curProgressRadius = state.getCurProgressRadius();
            float realScale = view.getRealScale(state.getDrawProgressScale());
            RectF rectF = this.mProgressRect;
            float f3 = rectF.top + (2.0f * curProgressRadius * (1.0f - realScale));
            float f4 = rectF.bottom;
            if (f3 >= f4) {
                return;
            }
            mOrientationHandler.calculateBackgroundRect(this.mVerticalClipRect, view, state);
            this.mVerticalClipPath.reset();
            if (!RoundCornerUtil.isVersionSupport() || state.getProgressRoundCornerWeight() == 0.0f) {
                Path path = this.mVerticalClipPath;
                RectF rectF2 = this.mVerticalClipRect;
                path.addRoundRect(rectF2.left, rectF2.top, rectF2.right, rectF2.bottom, curProgressRadius, curProgressRadius, Path.Direction.CCW);
            } else {
                OplusPath oplusPath = new OplusPath(this.mVerticalClipPath);
                RectF rectF3 = this.mVerticalClipRect;
                oplusPath.addSmoothRoundRect(rectF3.left, rectF3.top, rectF3.right, rectF3.bottom, curProgressRadius, curProgressRadius, state.getProgressRoundCornerWeight(), Path.Direction.CCW);
            }
            canvas.save();
            canvas.clipPath(this.mVerticalClipPath);
            RectF rectF4 = this.mProgressRect;
            canvas.drawRect(rectF4.left, f3, rectF4.right, f4, this.mPaint);
            canvas.restore();
            return;
        }
        if (zIsVertical && state.getShowThumb()) {
            RectF rectF5 = this.mProgressRect;
            canvas.drawRect(rectF5.left, rectF5.top, rectF5.right, rectF5.bottom, this.mPaint);
            return;
        }
        float progressRectHeight = view.getProgressRectHeight(state) / 2;
        int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
        if (smoothStyleType == 0) {
            if (state.getProgressRoundCornerWeight() == 0.0f) {
                canvas.drawRoundRect(this.mProgressRect, progressRectHeight, progressRectHeight, this.mPaint);
                return;
            } else {
                ensureOplusCanvas(canvas).drawSmoothRoundRect(this.mProgressRect, progressRectHeight, progressRectHeight, this.mPaint, state.getProgressRoundCornerWeight());
                return;
            }
        }
        if (smoothStyleType != 1) {
            canvas.drawRoundRect(this.mProgressRect, progressRectHeight, progressRectHeight, this.mPaint);
            return;
        }
        COUISeekBarCore.SmoothRoundCornerHelper mProgressSmoothRoundCornerHelper = view.getMProgressSmoothRoundCornerHelper();
        OplusPathAdapter pathAdapter = mProgressSmoothRoundCornerHelper != null ? mProgressSmoothRoundCornerHelper.getPathAdapter() : null;
        if (pathAdapter == null) {
            canvas.drawRoundRect(this.mProgressRect, progressRectHeight, progressRectHeight, this.mPaint);
            return;
        }
        Path mProgressPath = view.getMProgressPath();
        mProgressPath.reset();
        canvas.save();
        pathAdapter.addSmoothRoundRect(this.mProgressRect, progressRectHeight, progressRectHeight, Path.Direction.CCW);
        canvas.clipPath(mProgressPath);
        canvas.drawColor(state.getProgressColor());
        canvas.restore();
    }

    @Override // com.coui.appcompat.seekbar.renderer.IProgressRenderer
    public void setProgressColor(int color) {
        this.mProgressColor = color;
        this.mPaint.setColor(color);
    }

    @Override // com.coui.appcompat.seekbar.renderer.IProgressRenderer
    public void setProgressColor(ColorStateList color) {
        Intrinsics.checkNotNullParameter(color, "color");
        int defaultColor = color.getDefaultColor();
        this.mProgressColor = defaultColor;
        this.mPaint.setColor(defaultColor);
    }
}