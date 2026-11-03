package com.coui.appcompat.seekbar.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.seekbar.core.COUISeekBarCore;
import com.coui.appcompat.seekbar.core.SeekBarState;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/coui/appcompat/seekbar/renderer/IntentThumbRenderer;", "Lcom/coui/appcompat/seekbar/renderer/IThumbRenderer;", "()V", "mPaint", "Landroid/graphics/Paint;", "mThumbColor", "", "calculateThumbPosition", "", "state", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "drawThumb", "", "canvas", "Landroid/graphics/Canvas;", "setThumbColor", TypedValues.Custom.S_COLOR, "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class IntentThumbRenderer implements IThumbRenderer {
    private static final String TAG = "IntentThumbRenderer";
    private final Paint mPaint = new Paint(1);
    private int mThumbColor;

    public IntentThumbRenderer() {
        COUILog.i(TAG, "IntentThumbRenderer created");
    }

    @Override // com.coui.appcompat.seekbar.renderer.IThumbRenderer
    public float calculateThumbPosition(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        return state.getThumbPosition();
    }

    @Override // com.coui.appcompat.seekbar.renderer.IThumbRenderer
    public void drawThumb(Canvas canvas, SeekBarState state) {
        float centerX;
        float centerX2;
        float centerY;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        Intrinsics.checkNotNullParameter(state, "state");
        if (state.getShowThumb()) {
            float fCalculateThumbPosition = calculateThumbPosition(state);
            float curThumbRadius = state.getCurThumbRadius();
            if (state.getIsVertical()) {
                centerX = state.getCenterX() - curThumbRadius;
                centerX2 = state.getCenterX() + curThumbRadius;
                centerY = fCalculateThumbPosition - curThumbRadius;
            } else {
                centerX = fCalculateThumbPosition - curThumbRadius;
                centerX2 = fCalculateThumbPosition + curThumbRadius;
                centerY = state.getCenterY() - curThumbRadius;
                fCalculateThumbPosition = state.getCenterY();
            }
            float f3 = fCalculateThumbPosition + curThumbRadius;
            float f4 = centerX2;
            float f5 = centerX;
            COUISeekBarCore view = state.getView();
            if (state.getThumbShadowRadiusSize() > 0 && view != null && view.isEnabled()) {
                this.mPaint.setStyle(Paint.Style.FILL);
                this.mPaint.setShadowLayer(state.getThumbShadowRadiusSize(), 0.0f, state.getThumbShadowOffsetY(), state.getThumbShadowColor());
            }
            this.mPaint.setColor(this.mThumbColor);
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(f5, centerY, f4, f3, curThumbRadius, curThumbRadius, this.mPaint);
            if (state.getThumbShadowRadiusSize() <= 0 || view == null || !view.isEnabled()) {
                return;
            }
            this.mPaint.clearShadowLayer();
        }
    }

    @Override // com.coui.appcompat.seekbar.renderer.IThumbRenderer
    public void setThumbColor(int color) {
        this.mThumbColor = color;
        this.mPaint.setColor(color);
    }
}