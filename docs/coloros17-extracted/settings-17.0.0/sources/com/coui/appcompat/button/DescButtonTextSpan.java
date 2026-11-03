package com.coui.appcompat.button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import com.coui.appcompat.textutil.COUIChangeTextUtil;
import com.support.appcompat.R;

/* JADX INFO: loaded from: classes7.dex */
public class DescButtonTextSpan extends ReplacementSpan {
    private Context mContext;
    private TextPaint mDescPaint;
    private String mDescText;
    private int mDescTextWidth;
    private int mHeight;
    private boolean mIsRtl;
    private int mMaxWidth;
    private String mText;
    private int mTextColor;
    private TextPaint mTextPaint;
    private int mTextWidth;
    private int mTopMargin;
    private int mWidth;

    public DescButtonTextSpan(Context context, String str, String str2, int i3, int i4, int i5, int i6, Paint paint, boolean z2) {
        this.mText = str;
        this.mDescText = str2;
        this.mMaxWidth = i3;
        this.mWidth = i4 < 0 ? 0 : i4;
        this.mContext = context;
        this.mHeight = i5;
        this.mTextColor = i6;
        this.mIsRtl = z2;
        this.mTextPaint = new TextPaint(paint);
        initPaint();
        handleSubString();
    }

    private int getEmptyWidth() {
        return ((int) this.mTextPaint.measureText(" ")) / 2;
    }

    private int getStartText() {
        return Math.abs(this.mTextWidth - this.mDescTextWidth) / 2;
    }

    private String getSubStringByWidth(String str, int i3, TextPaint textPaint) {
        return (TextUtils.isEmpty(str) || i3 < 0) ? "" : StaticLayout.Builder.obtain(str, 0, str.length(), textPaint, i3).setMaxLines(1).setEllipsize(TextUtils.TruncateAt.END).build().getText().toString();
    }

    private int getWidth() {
        if (TextUtils.isEmpty(this.mDescText) || TextUtils.isEmpty(this.mText)) {
            return 0;
        }
        return Math.max(this.mDescTextWidth, this.mTextWidth);
    }

    private void handleSubString() {
        int i3 = this.mDescTextWidth;
        int i4 = this.mMaxWidth;
        if (i3 > i4) {
            String subStringByWidth = getSubStringByWidth(this.mDescText, i4, this.mDescPaint);
            this.mDescText = subStringByWidth;
            this.mDescTextWidth = (int) this.mDescPaint.measureText(subStringByWidth);
        }
        int i5 = this.mTextWidth;
        int i6 = this.mMaxWidth;
        if (i5 > i6) {
            String subStringByWidth2 = getSubStringByWidth(this.mText, i6, this.mTextPaint);
            this.mText = subStringByWidth2;
            this.mTextWidth = (int) this.mTextPaint.measureText(subStringByWidth2);
        }
    }

    private void initPaint() {
        float f3 = this.mContext.getResources().getConfiguration().fontScale;
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_btn_desc_text_size);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_btn_desc_sub_text_size);
        int suitableFontSize = (int) COUIChangeTextUtil.getSuitableFontSize(dimensionPixelSize, f3, 2);
        int suitableFontSize2 = (int) COUIChangeTextUtil.getSuitableFontSize(dimensionPixelSize2, f3, 2);
        this.mTextPaint.setTextSize(suitableFontSize);
        this.mTextPaint.setColor(this.mTextColor);
        TextPaint textPaint = new TextPaint(this.mTextPaint);
        this.mDescPaint = textPaint;
        textPaint.setTextSize(suitableFontSize2);
        this.mDescPaint.setColor(this.mTextColor);
        this.mDescTextWidth = (int) this.mDescPaint.measureText(this.mDescText);
        this.mTextWidth = (int) this.mTextPaint.measureText(this.mText);
        this.mTopMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.coui_btn_desc_top_margin);
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int i3, int i4, float f3, int i5, int i6, int i7, Paint paint) {
        Paint.FontMetricsInt fontMetricsInt = this.mDescPaint.getFontMetricsInt();
        Paint.FontMetricsInt fontMetricsInt2 = this.mTextPaint.getFontMetricsInt();
        int i8 = fontMetricsInt.descent;
        int i9 = fontMetricsInt.ascent;
        int i10 = fontMetricsInt.leading;
        int i11 = i6 - ((((i8 - i9) + i10) + this.mTopMargin) / 2);
        int iAbs = fontMetricsInt2.bottom + i11 + i10 + Math.abs(i9) + this.mTopMargin;
        int emptyWidth = getEmptyWidth();
        int startText = getStartText();
        if (this.mIsRtl) {
            emptyWidth = -emptyWidth;
        }
        float f4 = f3 - emptyWidth;
        if (this.mTextWidth > this.mDescTextWidth) {
            canvas.drawText(this.mText, f4, i11, this.mTextPaint);
            canvas.drawText(this.mDescText, f4 + startText, iAbs, this.mDescPaint);
        } else {
            canvas.drawText(this.mText, startText + f4, i11, this.mTextPaint);
            canvas.drawText(this.mDescText, f4, iAbs, this.mDescPaint);
        }
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int i3, int i4, Paint.FontMetricsInt fontMetricsInt) {
        return getWidth();
    }
}