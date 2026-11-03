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
import com.oplus.graphics.OplusCanvas;
import com.oplus.graphics.OplusPathAdapter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 *2\u00020\u0001:\u0001*B\u0005¢\u0006\u0002\u0010\u0002J8\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0011H\u0002J0\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\nH\u0002J\u0018\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J(\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\u0011H\u0002J\u0010\u0010 \u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0006H\u0002J8\u0010!\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\n2\u0006\u0010#\u001a\u00020\n2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u0011H\u0002J\u0010\u0010'\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020)H\u0016J\u0010\u0010'\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020\nH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006+"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/SectionProgressRenderer;", "Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;", "()V", "mOplusCanvas", "Lcom/oplus/graphics/OplusCanvas;", "mOplusCanvasRef", "Landroid/graphics/Canvas;", "mPaint", "Landroid/graphics/Paint;", "mProgressColor", "", "mProgressRect", "Landroid/graphics/RectF;", "drawMarkIfVisible", "", "canvas", "markPosition", "", "centerY", "radius", "thumbLeft", "thumbRight", "drawMarks", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "backgroundLeft", "width", "drawProgress", "drawProgressRoundRect", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "progressRadius", "ensureOplusCanvas", "markPositionAt", "max", "index", "isRtl", "", "deformOffset", "setProgressColor", TypedValues.Custom.S_COLOR, "Landroid/content/res/ColorStateList;", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SectionProgressRenderer implements IProgressRenderer {
    private static final float DEFAULT_SECTION_MARK_RADIUS = 3.0f;
    private OplusCanvas mOplusCanvas;
    private Canvas mOplusCanvasRef;
    private int mProgressColor;
    private final Paint mPaint = new Paint(1);
    private final RectF mProgressRect = new RectF();

    private final void drawMarkIfVisible(Canvas canvas, float markPosition, float centerY, float radius, float thumbLeft, float thumbRight) {
        float f3 = markPosition + radius;
        if (thumbLeft > markPosition - radius || thumbRight < f3) {
            canvas.drawCircle(markPosition, centerY, radius, this.mPaint);
        }
    }

    /* JADX WARN: Code duplicated, block: B:37:0x00ae  */
    /* JADX WARN: Code duplicated, block: B:39:0x00bf A[LOOP:0: B:27:0x007d->B:39:0x00bf, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:59:0x00c4 A[EDGE_INSN: B:59:0x00c4->B:41:0x00c4 BREAK  A[LOOP:0: B:27:0x007d->B:39:0x00bf], SYNTHETIC] */
    private final void drawMarks(Canvas canvas, SeekBarState state, float backgroundLeft, float width, int centerY) {
        float f3;
        if (state.getMax() <= 0) {
            return;
        }
        float thumbPosition = state.getThumbPosition() - state.getCurThumbRadius();
        float thumbPosition2 = state.getThumbPosition() + state.getCurThumbRadius();
        float heightTopDeformedUpValue = 0.0f;
        float sectionMarkRadius = state.getSectionMarkRadius() > 0.0f ? state.getSectionMarkRadius() : 3.0f;
        float f4 = centerY;
        if (state.getIsSupportDeformation()) {
            heightTopDeformedUpValue = state.getHeightTopDeformedUpValue() - state.getHeightBottomDeformedDownValue();
            if (state.getIsRtl()) {
                heightTopDeformedUpValue = -heightTopDeformedUpValue;
            }
        }
        float f5 = heightTopDeformedUpValue;
        this.mPaint.setStyle(Paint.Style.FILL);
        int sectionInactiveMarkColor = state.getIsRtl() ? state.getSectionInactiveMarkColor() : state.getSectionActiveMarkColor();
        int sectionActiveMarkColor = state.getIsRtl() ? state.getSectionActiveMarkColor() : state.getSectionInactiveMarkColor();
        this.mPaint.setColor(sectionInactiveMarkColor);
        int max = state.getMax();
        if (max >= 0) {
            int i3 = 0;
            while (true) {
                float fMarkPositionAt = markPositionAt(backgroundLeft, width, state.getMax(), i3, state.getIsRtl(), f5);
                int i4 = i3;
                f3 = f5;
                if (!state.getShowProgress()) {
                    float f6 = thumbPosition;
                    float f7 = thumbPosition2;
                    float f8 = sectionMarkRadius;
                    float f9 = f4;
                    drawMarkIfVisible(canvas, fMarkPositionAt, f9, f8, f6, f7);
                    f4 = f9;
                    sectionMarkRadius = f8;
                    thumbPosition = f6;
                    thumbPosition2 = f7;
                    if (i4 == max) {
                        break;
                    }
                    i3 = i4 + 1;
                    f5 = f3;
                } else if (!state.getIsRtl()) {
                    if (fMarkPositionAt <= state.getThumbPosition()) {
                        float f10 = thumbPosition;
                        float f11 = thumbPosition2;
                        float f12 = sectionMarkRadius;
                        float f13 = f4;
                        drawMarkIfVisible(canvas, fMarkPositionAt, f13, f12, f10, f11);
                        f4 = f13;
                        sectionMarkRadius = f12;
                        thumbPosition = f10;
                        thumbPosition2 = f11;
                        if (i4 == max) {
                            break;
                            break;
                        } else {
                            i3 = i4 + 1;
                            f5 = f3;
                        }
                    } else {
                        break;
                    }
                } else if (fMarkPositionAt >= state.getThumbPosition()) {
                    float f14 = thumbPosition;
                    float f15 = thumbPosition2;
                    float f16 = sectionMarkRadius;
                    float f17 = f4;
                    drawMarkIfVisible(canvas, fMarkPositionAt, f17, f16, f14, f15);
                    f4 = f17;
                    sectionMarkRadius = f16;
                    thumbPosition = f14;
                    thumbPosition2 = f15;
                    if (i4 == max) {
                        break;
                        break;
                    } else {
                        i3 = i4 + 1;
                        f5 = f3;
                    }
                } else {
                    break;
                }
            }
        } else {
            f3 = f5;
        }
        if (state.getShowProgress()) {
            this.mPaint.setColor(sectionActiveMarkColor);
            int max2 = state.getMax();
            while (-1 < max2) {
                float fMarkPositionAt2 = markPositionAt(backgroundLeft, width, state.getMax(), max2, state.getIsRtl(), f3);
                int i5 = max2;
                if (state.getIsRtl()) {
                    if (fMarkPositionAt2 >= state.getThumbPosition()) {
                        return;
                    }
                } else if (fMarkPositionAt2 <= state.getThumbPosition()) {
                    return;
                }
                float f18 = thumbPosition;
                float f19 = thumbPosition2;
                float f20 = sectionMarkRadius;
                float f21 = f4;
                drawMarkIfVisible(canvas, fMarkPositionAt2, f21, f20, f18, f19);
                f4 = f21;
                sectionMarkRadius = f20;
                thumbPosition = f18;
                thumbPosition2 = f19;
                max2 = i5 - 1;
            }
        }
    }

    private final void drawProgressRoundRect(Canvas canvas, COUISeekBarCore seekBar, SeekBarState state, float progressRadius) {
        int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
        if (smoothStyleType == 0) {
            if (state.getProgressRoundCornerWeight() == 0.0f || seekBar.getMProgressSmoothRoundCornerHelper() == null) {
                canvas.drawRoundRect(this.mProgressRect, progressRadius, progressRadius, this.mPaint);
                return;
            } else {
                ensureOplusCanvas(canvas).drawSmoothRoundRect(this.mProgressRect, progressRadius, progressRadius, this.mPaint, state.getProgressRoundCornerWeight());
                return;
            }
        }
        if (smoothStyleType != 1) {
            canvas.drawRoundRect(this.mProgressRect, progressRadius, progressRadius, this.mPaint);
            return;
        }
        COUISeekBarCore.SmoothRoundCornerHelper mProgressSmoothRoundCornerHelper = seekBar.getMProgressSmoothRoundCornerHelper();
        OplusPathAdapter pathAdapter = mProgressSmoothRoundCornerHelper != null ? mProgressSmoothRoundCornerHelper.getPathAdapter() : null;
        Path mProgressPath = seekBar.getMProgressPath();
        if (mProgressSmoothRoundCornerHelper == null || pathAdapter == null || mProgressPath == null) {
            canvas.drawRoundRect(this.mProgressRect, progressRadius, progressRadius, this.mPaint);
            return;
        }
        mProgressPath.reset();
        canvas.save();
        pathAdapter.addSmoothRoundRect(this.mProgressRect, progressRadius, progressRadius, Path.Direction.CCW);
        canvas.clipPath(mProgressPath);
        canvas.drawColor(state.getProgressColor());
        canvas.restore();
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

    private final float markPositionAt(float backgroundLeft, float width, int max, int index, boolean isRtl, float deformOffset) {
        return (isRtl ? (backgroundLeft + width) - ((index * width) / max) : backgroundLeft + ((index * width) / max)) + deformOffset;
    }

    @Override // com.coui.appcompat.seekbar.renderer.IProgressRenderer
    public void drawProgress(Canvas canvas, SeekBarState state) {
        float sectionThumbX;
        float heightBottomDeformedDownValue;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        COUISeekBarCore view = state.getView();
        if (view == null) {
            return;
        }
        if (state.getSectionThumbX() < 0.0f) {
            return;
        }
        float progressStart = view.getProgressStart();
        float seekBarLength = view.getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        int seekBarCenterY = view.getSeekBarCenterY();
        float f3 = 2;
        float curProgressHeight = (state.getCurProgressHeight() / f3) - (state.getIsSupportDeformation() ? state.getWidthDeformedValue() : 0.0f);
        if (state.getIsRtl()) {
            sectionThumbX = progressStart + seekBarLength;
            heightBottomDeformedDownValue = sectionThumbX - state.getSectionThumbX();
            if (state.getIsSupportDeformation()) {
                heightBottomDeformedDownValue = (heightBottomDeformedDownValue - state.getHeightTopDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
                sectionThumbX = (sectionThumbX - state.getHeightBottomDeformedUpValue()) + state.getHeightBottomDeformedDownValue();
            }
        } else {
            sectionThumbX = state.getSectionThumbX() + progressStart;
            if (state.getIsSupportDeformation()) {
                heightBottomDeformedDownValue = (progressStart - state.getHeightBottomDeformedDownValue()) + state.getHeightBottomDeformedUpValue();
                sectionThumbX = (sectionThumbX + state.getHeightTopDeformedUpValue()) - state.getHeightBottomDeformedDownValue();
            } else {
                heightBottomDeformedDownValue = progressStart;
            }
        }
        float f4 = seekBarCenterY;
        this.mProgressRect.set(heightBottomDeformedDownValue, f4 - curProgressHeight, sectionThumbX, f4 + curProgressHeight);
        float curProgressHeight2 = state.getCurProgressHeight() / f3;
        RectF rectF = this.mProgressRect;
        rectF.left -= curProgressHeight2;
        rectF.right += curProgressHeight2;
        if (state.getShowProgress()) {
            RectF rectF2 = this.mProgressRect;
            if (rectF2.right > rectF2.left) {
                this.mPaint.setColor(state.getProgressColor());
                this.mPaint.setStyle(Paint.Style.FILL);
                drawProgressRoundRect(canvas, view, state, curProgressHeight2);
            }
        }
        drawMarks(canvas, state, progressStart, seekBarLength, seekBarCenterY);
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