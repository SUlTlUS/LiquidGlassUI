package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.decouple.injector.adaptor.BaseConfig;
import com.support.progressbar.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIHorizontalProgressBar extends ProgressBar {
    private static final int DEFAULT_BACKGROUND_COLOR = Color.argb(12, 0, 0, 0);
    private static final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#FF2AD181");
    private static final int[] mAttrArray = {R.attr.couiSeekBarProgressColorDisabled};
    private ColorStateList mBackgroundColor;
    private RectF mBackgroundRect;
    private Context mContext;
    private boolean mNeedRadius;
    private Paint mPaint;
    private Path mPath;
    private ColorStateList mProgressColor;
    private Path mProgressPath;
    private RectF mProgressRect;
    private int mRadius;
    private int mStyle;

    public COUIHorizontalProgressBar(Context context) {
        this(context, null);
    }

    private int getStateColor(ColorStateList colorStateList, int i3) {
        return colorStateList == null ? i3 : colorStateList.getColorForState(getDrawableState(), i3);
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onDraw(Canvas canvas) {
        this.mProgressPath.reset();
        this.mPath.reset();
        int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
        this.mPaint.setColor(getStateColor(this.mBackgroundColor, DEFAULT_BACKGROUND_COLOR));
        this.mBackgroundRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        Path path = this.mPath;
        RectF rectF = this.mBackgroundRect;
        int i3 = this.mRadius;
        Path.Direction direction = Path.Direction.CCW;
        path.addRoundRect(rectF, i3, i3, direction);
        canvas.clipPath(this.mPath);
        RectF rectF2 = this.mBackgroundRect;
        int i4 = this.mRadius;
        canvas.drawRoundRect(rectF2, i4, i4, this.mPaint);
        float progress = getProgress() / getMax();
        if (isLayoutRtl()) {
            int iRound = Math.round((getWidth() - getPaddingRight()) - (progress * width));
            this.mProgressRect.set(iRound, getPaddingTop(), iRound + width, getHeight() - getPaddingBottom());
        } else {
            int iRound2 = Math.round(getPaddingLeft() - ((1.0f - progress) * width));
            this.mProgressRect.set(iRound2, getPaddingTop(), iRound2 + width, getHeight() - getPaddingBottom());
        }
        this.mPaint.setColor(getStateColor(this.mProgressColor, DEFAULT_PROGRESS_COLOR));
        this.mProgressPath.addRoundRect(this.mProgressRect, this.mRadius, 0.0f, direction);
        this.mProgressPath.op(this.mPath, Path.Op.INTERSECT);
        canvas.drawPath(this.mProgressPath, this.mPaint);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        int paddingRight = (i3 - getPaddingRight()) - getPaddingLeft();
        int paddingTop = (i4 - getPaddingTop()) - getPaddingBottom();
        if (this.mNeedRadius) {
            this.mRadius = paddingRight >= paddingTop ? paddingTop / 2 : paddingRight / 2;
        } else {
            this.mRadius = 0;
        }
    }

    public void refresh() {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(mAttrArray);
        typedArrayObtainStyledAttributes.getColor(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArrayObtainStyledAttributes2 = null;
        if ("attr".equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes2 = this.mContext.obtainStyledAttributes(null, R.styleable.COUIHorizontalProgressBar, this.mStyle, 0);
        } else if (BaseConfig.KEY_STYLE.equals(resourceTypeName)) {
            typedArrayObtainStyledAttributes2 = this.mContext.obtainStyledAttributes(null, R.styleable.COUIHorizontalProgressBar, 0, this.mStyle);
        }
        if (typedArrayObtainStyledAttributes2 != null) {
            this.mBackgroundColor = typedArrayObtainStyledAttributes2.getColorStateList(R.styleable.COUIHorizontalProgressBar_couiHorizontalProgressBarBackgroundColor);
            this.mProgressColor = typedArrayObtainStyledAttributes2.getColorStateList(R.styleable.COUIHorizontalProgressBar_couiHorizontalProgressBarProgressColor);
            typedArrayObtainStyledAttributes2.recycle();
        }
    }

    public void setBackgroundColor(ColorStateList colorStateList) {
        this.mBackgroundColor = colorStateList;
    }

    public void setProgressColor(ColorStateList colorStateList) {
        this.mProgressColor = colorStateList;
    }

    public COUIHorizontalProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.couiHorizontalProgressBarStyle);
    }

    public COUIHorizontalProgressBar(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, R.style.COUIProgressHorizontal);
    }

    public COUIHorizontalProgressBar(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.mPaint = new Paint();
        this.mBackgroundRect = new RectF();
        this.mProgressRect = new RectF();
        this.mRadius = Integer.MAX_VALUE;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i3;
        }
        this.mContext = context;
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(mAttrArray);
        typedArrayObtainStyledAttributes.getColor(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.COUIHorizontalProgressBar, i3, i4);
        this.mBackgroundColor = typedArrayObtainStyledAttributes2.getColorStateList(R.styleable.COUIHorizontalProgressBar_couiHorizontalProgressBarBackgroundColor);
        this.mProgressColor = typedArrayObtainStyledAttributes2.getColorStateList(R.styleable.COUIHorizontalProgressBar_couiHorizontalProgressBarProgressColor);
        this.mNeedRadius = typedArrayObtainStyledAttributes2.getBoolean(R.styleable.COUIHorizontalProgressBar_couiHorizontalProgressNeedRadius, true);
        typedArrayObtainStyledAttributes2.recycle();
        this.mPaint.setDither(true);
        this.mPaint.setAntiAlias(true);
        setLayerType(1, this.mPaint);
        this.mPath = new Path();
        this.mProgressPath = new Path();
    }
}