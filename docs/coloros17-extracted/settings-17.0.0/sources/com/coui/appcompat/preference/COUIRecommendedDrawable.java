package com.coui.appcompat.preference;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.coui.appcompat.roundRect.COUIRoundRectUtil;
import com.google.android.material.shape.MaterialShapeDrawable;

/* JADX INFO: loaded from: classes7.dex */
public class COUIRecommendedDrawable extends MaterialShapeDrawable {
    private int mColor;
    private Paint mPaint = new Paint(1);
    private Path mPath = new Path();
    private float mRadius;

    public COUIRecommendedDrawable(float f3, int i3) {
        this.mRadius = f3;
        this.mColor = i3;
        this.mPaint.setColor(this.mColor);
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mPath.reset();
        Path path = COUIRoundRectUtil.getInstance().getPath(getBounds(), this.mRadius);
        this.mPath = path;
        canvas.drawPath(path, this.mPaint);
    }
}