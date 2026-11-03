package com.coui.appcompat.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.coui.appcompat.button.COUIButton;

/* JADX INFO: loaded from: classes7.dex */
class COUITwoTextButton extends COUIButton {
    private static final int MAX_ALPHA = 255;
    private String mNextText;
    private float mNextTextAlpha;
    private TextPaint mNextTextPaint;
    private String mStartText;
    private float mStartTextAlpha;
    private TextPaint mStartTextPaint;

    public COUITwoTextButton(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.mStartText = "";
        this.mNextText = "";
    }

    private void drawText(Canvas canvas, String str, TextPaint textPaint, float f3) {
        int width = getWidth();
        String str2 = (String) TextUtils.ellipsize(str, textPaint, (width - getPaddingStart()) - getPaddingEnd(), TextUtils.TruncateAt.END);
        float fMeasureText = ((width - textPaint.measureText(str2)) / 2.0f) - ((getPaddingEnd() - getPaddingStart()) / 2.0f);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float measuredHeight = (((getMeasuredHeight() + (fontMetrics.bottom - fontMetrics.top)) / 2.0f) - fontMetrics.bottom) - ((getPaddingBottom() - getPaddingTop()) / 2.0f);
        textPaint.setAlpha((int) (f3 * 255.0f));
        canvas.drawText(str2, fMeasureText, measuredHeight, textPaint);
    }

    public float getNextTextAlpha() {
        return this.mNextTextAlpha;
    }

    public float getStartTextAlpha() {
        return this.mStartTextAlpha;
    }

    @Override // com.coui.appcompat.button.COUIButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mStartTextPaint == null) {
            this.mStartTextPaint = new TextPaint(getPaint());
        }
        if (this.mNextTextPaint == null) {
            this.mNextTextPaint = new TextPaint(getPaint());
        }
        int iSave = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        drawText(canvas, this.mStartText, this.mStartTextPaint, this.mStartTextAlpha);
        drawText(canvas, this.mNextText, this.mNextTextPaint, this.mNextTextAlpha);
        canvas.restoreToCount(iSave);
    }

    public void setNextText(CharSequence charSequence) {
        this.mNextText = (String) charSequence;
    }

    public void setNextTextAlpha(float f3) {
        this.mNextTextAlpha = f3;
    }

    public void setStartText(CharSequence charSequence) {
        this.mStartText = (String) charSequence;
    }

    public void setStartTextAlpha(float f3) {
        this.mStartTextAlpha = f3;
    }
}