package com.coui.appcompat.chip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.coui.appcompat.scrollview.COUIHorizontalScrollView;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000  2\u00020\u0001:\u0001 B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0007H\u0016J(\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0014J\u0015\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u0010H\u0000¢\u0006\u0002\b\u001eJ\u0010\u0010\u001f\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0007H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006!"}, d2 = {"Lcom/coui/appcompat/chip/ChipMaskHorizontalScrollView;", "Lcom/coui/appcompat/scrollview/COUIHorizontalScrollView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "fadeRect", "Landroid/graphics/RectF;", "fadeWidth", "", "leftFadePaint", "Landroid/graphics/Paint;", "mOffscreenLayerHandledByLayout", "", "rightFadePaint", "dispatchDraw", "", "canvas", "Landroid/graphics/Canvas;", "getHorizontalFadingEdgeLength", "onSizeChanged", "w", "h", "oldw", "oldh", "setOffscreenLayerHandledByLayout", "handledByLayout", "setOffscreenLayerHandledByLayout$coui_support_chip_release", "updateFadeShaders", "Companion", "coui-support-chip_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ChipMaskHorizontalScrollView extends COUIHorizontalScrollView {
    private static final float CLIP_CANVAS_EXPAND_BOTTOM = 100.0f;
    private static final float FADING_EDGE_WIDTH = 12.0f;
    private final RectF fadeRect;
    private final float fadeWidth;
    private final Paint leftFadePaint;
    private boolean mOffscreenLayerHandledByLayout;
    private final Paint rightFadePaint;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ChipMaskHorizontalScrollView(Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    private final void updateFadeShaders(int w2) {
        float paddingLeft = getPaddingLeft();
        float paddingLeft2 = getPaddingLeft() + this.fadeWidth;
        Paint paint = this.leftFadePaint;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        paint.setShader(new LinearGradient(paddingLeft, 0.0f, paddingLeft2, 0.0f, -1, 0, tileMode));
        this.rightFadePaint.setShader(new LinearGradient((w2 - getPaddingRight()) - this.fadeWidth, 0.0f, w2 - getPaddingRight(), 0.0f, 0, -1, tileMode));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        Canvas canvas2;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        int iSaveLayer = 0;
        View childAt = getChildAt(0);
        if (childAt == null) {
            super.dispatchDraw(canvas);
            return;
        }
        int iMax = Math.max(0, childAt.getWidth() - ((getWidth() - getPaddingLeft()) - getPaddingRight()));
        boolean z2 = getScrollX() > 0;
        boolean z3 = getScrollX() < iMax;
        if (this.mOffscreenLayerHandledByLayout) {
            canvas2 = canvas;
        } else {
            canvas2 = canvas;
            iSaveLayer = canvas2.saveLayer(getScrollX() - 100.0f, -100.0f, getScrollX() + getWidth() + 100.0f, getHeight() + 100.0f, null);
        }
        int iSave = canvas2.save();
        int scrollX = getScrollX();
        int paddingLeft = z2 ? scrollX + getPaddingLeft() : scrollX - 100;
        int scrollX2 = getScrollX() + getWidth();
        canvas2.clipRect(paddingLeft, -100, z3 ? scrollX2 - getPaddingRight() : scrollX2 + 100, (int) (getHeight() + 100.0f));
        super.dispatchDraw(canvas2);
        canvas2.restoreToCount(iSave);
        canvas2.translate(getScrollX(), 0.0f);
        if (z2) {
            this.fadeRect.set(getPaddingLeft(), -100.0f, getPaddingLeft() + this.fadeWidth, getHeight() + 100.0f);
            canvas2.drawRect(this.fadeRect, this.leftFadePaint);
        }
        if (z3) {
            this.fadeRect.set((getWidth() - getPaddingRight()) - this.fadeWidth, -100.0f, getWidth() - getPaddingRight(), getHeight() + 100.0f);
            canvas2.drawRect(this.fadeRect, this.rightFadePaint);
        }
        if (this.mOffscreenLayerHandledByLayout) {
            return;
        }
        canvas2.restoreToCount(iSaveLayer);
    }

    @Override // android.view.View
    public int getHorizontalFadingEdgeLength() {
        return (int) this.fadeWidth;
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.widget.HorizontalScrollView, android.view.View
    public void onSizeChanged(int w2, int h3, int oldw, int oldh) {
        super.onSizeChanged(w2, h3, oldw, oldh);
        updateFadeShaders(w2);
    }

    public final void setOffscreenLayerHandledByLayout$coui_support_chip_release(boolean handledByLayout) {
        if (this.mOffscreenLayerHandledByLayout == handledByLayout) {
            return;
        }
        this.mOffscreenLayerHandledByLayout = handledByLayout;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ChipMaskHorizontalScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ ChipMaskHorizontalScrollView(Context context, AttributeSet attributeSet, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i4 & 2) != 0 ? null : attributeSet, (i4 & 4) != 0 ? 0 : i3);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChipMaskHorizontalScrollView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        Intrinsics.checkNotNullParameter(context, "context");
        this.fadeWidth = TypedValue.applyDimension(1, 12.0f, getResources().getDisplayMetrics());
        Paint paint = new Paint(1);
        PorterDuff.Mode mode = PorterDuff.Mode.DST_OUT;
        paint.setXfermode(new PorterDuffXfermode(mode));
        this.leftFadePaint = paint;
        Paint paint2 = new Paint(1);
        paint2.setXfermode(new PorterDuffXfermode(mode));
        this.rightFadePaint = paint2;
        this.fadeRect = new RectF();
        setHorizontalFadingEdgeEnabled(false);
    }
}