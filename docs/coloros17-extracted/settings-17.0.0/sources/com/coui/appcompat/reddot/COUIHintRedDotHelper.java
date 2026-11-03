package com.coui.appcompat.reddot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.core.content.ContextCompat;
import com.coui.appcompat.roundRect.COUIRoundRectUtil;
import com.support.reddot.R;

/* JADX INFO: loaded from: classes.dex */
public class COUIHintRedDotHelper {
    public static final int CONSTANT_VALUE_0 = 0;
    public static final int CONSTANT_VALUE_10 = 10;
    public static final int CONSTANT_VALUE_100 = 100;
    public static final int CONSTANT_VALUE_1000 = 1000;
    private static final int MAX_ALPHA_VALUE = 255;
    private static final int RATIO = 2;
    private int mBgColor;
    private Paint mBgPaint;
    private int mCornerRadius;
    private int mDotCornerRadius;
    private int mDotDiameter;
    private int mEllipsisDiameter;
    private int mEllipsisSpacing;
    private int mLargeWidth;
    private int mMediumWidth;
    private int mNaviSmallWidth;
    private int mSmallWidth;
    private int mStrokeColor;
    private Paint mStrokePaint;
    private int mStrokeWidth;
    private int mTextColor;
    private TextPaint mTextPaint;
    private int mTextSize;
    private int mViewHeight;

    public COUIHintRedDotHelper(Context context, AttributeSet attributeSet, int[] iArr, int i3, int i4) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i3, i4);
        this.mBgColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIHintRedDot_couiHintRedDotColor, 0);
        this.mTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.COUIHintRedDot_couiHintRedDotTextColor, 0);
        this.mTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHintRedDot_couiHintTextSize, 0);
        this.mSmallWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHintRedDot_couiSmallWidth, 0);
        this.mMediumWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHintRedDot_couiMediumWidth, 0);
        this.mLargeWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHintRedDot_couiLargeWidth, 0);
        this.mViewHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHintRedDot_couiHeight, 0);
        this.mCornerRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHintRedDot_couiCornerRadius, 0);
        this.mDotDiameter = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHintRedDot_couiDotDiameter, 0);
        this.mEllipsisDiameter = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUIHintRedDot_couiEllipsisDiameter, 0);
        typedArrayObtainStyledAttributes.recycle();
        this.mDotCornerRadius = context.getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_rect_radius);
        this.mNaviSmallWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_navi_small_width);
        this.mEllipsisSpacing = context.getResources().getDimensionPixelSize(R.dimen.coui_hint_red_dot_ellipsis_spacing);
        this.mStrokeWidth = context.getResources().getDimensionPixelSize(R.dimen.coui_dot_stroke_width);
        this.mStrokeColor = ContextCompat.getColor(context, com.support.appcompat.R.color.coui_color_white);
        TextPaint textPaint = new TextPaint();
        this.mTextPaint = textPaint;
        textPaint.setAntiAlias(true);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mTextPaint.setTypeface(Typeface.create("sans-serif-medium", 0));
        Paint paint = new Paint();
        this.mBgPaint = paint;
        paint.setAntiAlias(true);
        this.mBgPaint.setColor(this.mBgColor);
        Paint paint2 = this.mBgPaint;
        Paint.Style style = Paint.Style.FILL;
        paint2.setStyle(style);
        Paint paint3 = new Paint();
        this.mStrokePaint = paint3;
        paint3.setAntiAlias(true);
        this.mStrokePaint.setColor(this.mStrokeColor);
        this.mStrokePaint.setStyle(style);
    }

    private void drawNumber(Canvas canvas, int i3, int i4, RectF rectF, boolean z2) {
        if (i3 <= 0) {
            return;
        }
        if (z2) {
            this.mTextPaint.setAlpha(Math.max(0, Math.min(255, i4)));
        }
        if (i3 < 1000) {
            String strValueOf = String.valueOf(i3);
            Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
            int iMeasureText = (int) this.mTextPaint.measureText(strValueOf);
            float f3 = rectF.left;
            canvas.drawText(strValueOf, f3 + (((rectF.right - f3) - iMeasureText) / 2.0f), (((rectF.top + rectF.bottom) - fontMetricsInt.ascent) - fontMetricsInt.descent) / 2.0f, this.mTextPaint);
        } else {
            float f4 = (rectF.left + rectF.right) / 2.0f;
            float f5 = (rectF.top + rectF.bottom) / 2.0f;
            for (int i5 = -1; i5 <= 1; i5++) {
                int i6 = this.mEllipsisSpacing;
                int i7 = this.mEllipsisDiameter;
                canvas.drawCircle(((i6 + i7) * i5) + f4, f5, i7 / 2.0f, this.mTextPaint);
            }
        }
        this.mTextPaint.setColor(this.mTextColor);
    }

    private void drawPointOnly(Canvas canvas, RectF rectF) {
        float f3 = rectF.bottom;
        float f4 = rectF.top;
        float f5 = (f3 - f4) / 2.0f;
        canvas.drawCircle(rectF.left + f5, f4 + f5, f5, this.mBgPaint);
    }

    private void drawPointStroke(Canvas canvas, RectF rectF) {
        float f3 = rectF.bottom;
        float f4 = rectF.top;
        float f5 = (f3 - f4) / 2.0f;
        canvas.drawCircle(rectF.left + f5, f4 + f5, f5 - this.mStrokeWidth, this.mBgPaint);
    }

    private void drawPointWithNumber(Canvas canvas, Object obj, RectF rectF) {
        Path path;
        boolean z2 = obj instanceof String;
        if (z2) {
            if (TextUtils.isEmpty((CharSequence) obj)) {
                return;
            }
        } else {
            if (!(obj instanceof Integer)) {
                throw new IllegalArgumentException("params 'number' must be String or Integer!");
            }
            if (((Integer) obj).intValue() <= 0) {
                return;
            }
        }
        if (Math.min(rectF.right - rectF.left, rectF.bottom - rectF.top) < this.mCornerRadius * 2) {
            path = COUIRoundRectUtil.getInstance().getPath(rectF, ((int) Math.min(rectF.right - rectF.left, rectF.bottom - rectF.top)) / 2);
        } else {
            path = COUIRoundRectUtil.getInstance().getPath(rectF, this.mCornerRadius);
        }
        canvas.drawPath(path, this.mBgPaint);
        if (z2) {
            drawText(canvas, (String) obj, rectF);
        } else {
            drawNumber(canvas, ((Integer) obj).intValue(), 255, rectF, false);
        }
    }

    private void drawPointWithStroke(Canvas canvas, Object obj, RectF rectF) {
        boolean z2 = obj instanceof String;
        if (z2) {
            if (TextUtils.isEmpty((CharSequence) obj)) {
                return;
            }
        } else {
            if (!(obj instanceof Integer)) {
                throw new IllegalArgumentException("params 'number' must be String or Integer!");
            }
            if (((Integer) obj).intValue() <= 0) {
                return;
            }
        }
        RectF rectF2 = new RectF();
        rectF2.left = 0.0f;
        float f3 = rectF.right;
        int i3 = this.mStrokeWidth;
        float f4 = f3 - (i3 * 2);
        rectF2.right = f4;
        rectF2.top = 0.0f;
        float f5 = rectF.bottom - (i3 * 2);
        rectF2.bottom = f5;
        int iMin = ((int) Math.min(f4 - 0.0f, f5 - 0.0f)) / 2;
        canvas.drawPath(COUIRoundRectUtil.getInstance().getPath(rectF, this.mCornerRadius), this.mStrokePaint);
        canvas.save();
        int i4 = this.mStrokeWidth;
        canvas.translate(i4, i4);
        canvas.drawPath(COUIRoundRectUtil.getInstance().getPath(rectF2, iMin), this.mBgPaint);
        canvas.restore();
        if (z2) {
            drawText(canvas, (String) obj, rectF);
        } else {
            drawNumber(canvas, ((Integer) obj).intValue(), 255, rectF, false);
        }
    }

    private void drawText(Canvas canvas, String str, RectF rectF) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        float fMeasureText = this.mTextPaint.measureText(str);
        if (fMeasureText < this.mTextPaint.measureText(String.valueOf(1000))) {
            Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
            float f3 = rectF.left;
            canvas.drawText(str, f3 + (((rectF.right - f3) - fMeasureText) / 2.0f), (((rectF.top + rectF.bottom) - fontMetricsInt.ascent) - fontMetricsInt.descent) / 2.0f, this.mTextPaint);
            return;
        }
        float f4 = (rectF.left + rectF.right) / 2.0f;
        float f5 = (rectF.top + rectF.bottom) / 2.0f;
        for (int i3 = -1; i3 <= 1; i3++) {
            int i4 = this.mEllipsisSpacing;
            int i5 = this.mEllipsisDiameter;
            canvas.drawCircle(((i4 + i5) * i3) + f4, f5, i5 / 2.0f, this.mTextPaint);
        }
    }

    private int getBgHeight() {
        return this.mViewHeight;
    }

    private int getBgWidth(int i3) {
        if (i3 < 10) {
            return Math.max(this.mSmallWidth, this.mViewHeight);
        }
        if (i3 < 100) {
            return Math.max(this.mMediumWidth, this.mViewHeight);
        }
        return i3 < 1000 ? Math.max(this.mLargeWidth, this.mViewHeight) : Math.max(this.mMediumWidth, this.mViewHeight);
    }

    private int getNaviBgWidth(int i3) {
        if (i3 < 10) {
            return this.mNaviSmallWidth;
        }
        return i3 < 100 ? this.mSmallWidth : this.mMediumWidth;
    }

    private boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int length = str.length();
        do {
            length--;
            if (length < 0) {
                return true;
            }
        } while (Character.isDigit(str.charAt(length)));
        return false;
    }

    public void drawPointWithFadeNumber(Canvas canvas, int i3, int i4, int i5, int i6, RectF rectF) {
        canvas.drawPath(COUIRoundRectUtil.getInstance().getPath(rectF, this.mCornerRadius), this.mBgPaint);
        if (i4 > i6) {
            drawNumber(canvas, i3, i4, rectF, true);
            drawNumber(canvas, i5, i6, rectF, true);
        } else {
            drawNumber(canvas, i5, i6, rectF, true);
            drawNumber(canvas, i3, i4, rectF, true);
        }
    }

    public void drawRedPoint(Canvas canvas, int i3, Object obj, RectF rectF) {
        if (i3 == 1) {
            drawPointOnly(canvas, rectF);
            return;
        }
        if (i3 == 2 || i3 == 3) {
            drawPointWithNumber(canvas, obj, rectF);
        } else if (i3 == 4) {
            drawPointStroke(canvas, rectF);
        } else {
            if (i3 != 5) {
                return;
            }
            drawPointWithStroke(canvas, obj, rectF);
        }
    }

    public int getViewHeight(int i3) {
        if (i3 != 1) {
            if (i3 != 2 && i3 != 3) {
                if (i3 != 4) {
                    if (i3 != 5) {
                        return 0;
                    }
                }
            }
            return getBgHeight();
        }
        return this.mDotDiameter;
    }

    public int getViewWidth(int i3, int i4) {
        if (i3 != 1) {
            if (i3 != 2) {
                if (i3 == 3) {
                    return getNaviBgWidth(i4);
                }
                if (i3 != 4) {
                    if (i3 != 5) {
                        return 0;
                    }
                }
            }
            return getBgWidth(i4);
        }
        return this.mDotDiameter;
    }

    public void setBgColor(int i3) {
        this.mBgColor = i3;
        this.mBgPaint.setColor(i3);
    }

    public void setCornerRadius(int i3) {
        this.mCornerRadius = i3;
    }

    public void setDotDiameter(int i3) {
        this.mDotDiameter = i3;
    }

    public void setEllipsisDiameter(int i3) {
        this.mEllipsisDiameter = i3;
    }

    public void setLargeWidth(int i3) {
        this.mLargeWidth = i3;
    }

    public void setMediumWidth(int i3) {
        this.mMediumWidth = i3;
    }

    public void setSmallWidth(int i3) {
        this.mSmallWidth = i3;
    }

    public void setTextColor(int i3) {
        this.mTextColor = i3;
        this.mTextPaint.setColor(i3);
    }

    public void setTextSize(int i3) {
        this.mTextSize = i3;
    }

    public void setViewHeight(int i3) {
        this.mViewHeight = i3;
        setCornerRadius(i3 / 2);
    }

    private int getNaviBgWidth(String str) {
        float fMeasureText = (int) this.mTextPaint.measureText(str);
        if (fMeasureText < this.mTextPaint.measureText(String.valueOf(10))) {
            return this.mNaviSmallWidth;
        }
        if (fMeasureText < this.mTextPaint.measureText(String.valueOf(100))) {
            return this.mMediumWidth;
        }
        if (fMeasureText < this.mTextPaint.measureText(String.valueOf(1000))) {
            return this.mLargeWidth;
        }
        return this.mMediumWidth;
    }

    public int getViewWidth(int i3, String str) {
        if (i3 != 1) {
            if (i3 != 2) {
                if (i3 == 3) {
                    return getNaviBgWidth(str);
                }
                if (i3 != 4) {
                    if (i3 != 5) {
                        return 0;
                    }
                }
            }
            return getBgWidth(str);
        }
        return this.mDotDiameter;
    }

    private int getBgWidth(String str) {
        if (TextUtils.isEmpty(str)) {
            return this.mSmallWidth;
        }
        if (isNumeric(str)) {
            return getBgWidth(Integer.parseInt(str));
        }
        float fMeasureText = (int) this.mTextPaint.measureText(str);
        if (fMeasureText < this.mTextPaint.measureText(String.valueOf(10))) {
            return Math.max(this.mSmallWidth, this.mViewHeight);
        }
        if (fMeasureText < this.mTextPaint.measureText(String.valueOf(100))) {
            return Math.max(this.mMediumWidth, this.mViewHeight);
        }
        if (fMeasureText < this.mTextPaint.measureText(String.valueOf(1000))) {
            return Math.max(this.mLargeWidth, this.mViewHeight);
        }
        return Math.max(this.mMediumWidth, this.mViewHeight);
    }
}