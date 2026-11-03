package com.coui.appcompat.cardview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.version.COUIVersionUtil;
import com.oplus.graphics.OplusOutline;
import com.oplus.graphics.OplusOutlineAdapter;

/* JADX INFO: loaded from: classes7.dex */
class RoundRectDrawable extends Drawable {
    private ColorStateList mBackground;
    private final RectF mBoundsF;
    private final Rect mBoundsI;
    private float mCardRoundCornerRadius;
    private boolean mInsetForPadding;
    private boolean mInsetForRadius;
    private boolean mIsSupportSmoothRoundCorner;
    private float mPadding;
    private final Paint mPaint;
    private float mRadius;
    private ColorStateList mTint;
    private PorterDuffColorFilter mTintFilter;
    private PorterDuff.Mode mTintMode;
    private float mWeight;

    public RoundRectDrawable(ColorStateList colorStateList, float f3, float f4) {
        this(colorStateList, f3, 0.0f, f4);
    }

    private PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
    }

    private boolean execute15SRC() {
        return isAdaptedOn15() || isAdaptedOn16();
    }

    private boolean execute16SRC() {
        return RoundCornerUtil.getSmoothStyleType() >= 1 && this.mCardRoundCornerRadius != 0.0f;
    }

    private Context getContextFromCallback() {
        Drawable.Callback callback = getCallback();
        if (callback instanceof View) {
            return ((View) callback).getContext();
        }
        return null;
    }

    private boolean isAdaptedOn15() {
        return (RoundCornerUtil.getSmoothStyleType() != 0 || this.mRadius == 0.0f || this.mWeight == 0.0f) ? false : true;
    }

    private boolean isAdaptedOn16() {
        return RoundCornerUtil.getSmoothStyleType() == 1 && this.mCardRoundCornerRadius == 0.0f && this.mRadius != 0.0f && this.mWeight != 0.0f;
    }

    private boolean isUseSmoothRoundRect() {
        return this.mIsSupportSmoothRoundCorner && isWeightValid();
    }

    private boolean isWeightValid() {
        return this.mWeight != 0.0f;
    }

    private void setBackground(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        this.mBackground = colorStateList;
        this.mPaint.setColor(colorStateList.getColorForState(getState(), this.mBackground.getDefaultColor()));
    }

    private void updateBounds(Rect rect) {
        if (rect == null) {
            rect = getBounds();
        }
        this.mBoundsF.set(rect.left, rect.top, rect.right, rect.bottom);
        this.mBoundsI.set(rect);
        if (this.mInsetForPadding) {
            this.mBoundsI.inset((int) Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius)), (int) Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius)));
            this.mBoundsF.set(this.mBoundsI);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        boolean z2;
        Paint paint = this.mPaint;
        if (this.mTintFilter == null || paint.getColorFilter() != null) {
            z2 = false;
        } else {
            paint.setColorFilter(this.mTintFilter);
            z2 = true;
        }
        canvas.drawColor(paint.getColor());
        if (z2) {
            paint.setColorFilter(null);
        }
    }

    public float getCardRoundCornerRadius() {
        return this.mCardRoundCornerRadius;
    }

    public ColorStateList getColor() {
        return this.mBackground;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        if (!execute16SRC()) {
            if (execute15SRC()) {
                new OplusOutline(outline).setSmoothRoundRect(this.mBoundsI, this.mRadius, this.mWeight);
                return;
            }
            Rect rect = this.mBoundsI;
            float f3 = this.mCardRoundCornerRadius;
            if (f3 == 0.0f) {
                f3 = this.mRadius;
            }
            outline.setRoundRect(rect, f3);
            return;
        }
        OplusOutlineAdapter oplusOutlineAdapter = new OplusOutlineAdapter(outline, 1);
        if (COUIVersionUtil.getOSVersionCode() <= 37) {
            oplusOutlineAdapter.setSmoothRoundRect(this.mBoundsI, this.mCardRoundCornerRadius);
            return;
        }
        Context contextFromCallback = getContextFromCallback();
        if (contextFromCallback != null) {
            oplusOutlineAdapter.setSmoothRoundRect(this.mBoundsI, RoundCornerUtil.getRoundCornerForOS17(contextFromCallback, this.mCardRoundCornerRadius), 3.0f);
        } else {
            oplusOutlineAdapter.setSmoothRoundRect(this.mBoundsI, this.mCardRoundCornerRadius);
        }
    }

    public float getPadding() {
        return this.mPadding;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public float getWeight() {
        return this.mWeight;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList = this.mTint;
        if (colorStateList != null && colorStateList.isStateful()) {
            return true;
        }
        ColorStateList colorStateList2 = this.mBackground;
        return (colorStateList2 != null && colorStateList2.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        updateBounds(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        PorterDuff.Mode mode;
        ColorStateList colorStateList = this.mBackground;
        int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
        boolean z2 = colorForState != this.mPaint.getColor();
        if (z2) {
            this.mPaint.setColor(colorForState);
        }
        ColorStateList colorStateList2 = this.mTint;
        if (colorStateList2 == null || (mode = this.mTintMode) == null) {
            return z2;
        }
        this.mTintFilter = createTintFilter(colorStateList2, mode);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.mPaint.setAlpha(i3);
    }

    public void setCardRoundCornerRadius(float f3) {
        if (f3 == this.mCardRoundCornerRadius) {
            return;
        }
        this.mCardRoundCornerRadius = f3;
        updateBounds(null);
        invalidateSelf();
    }

    public void setColor(ColorStateList colorStateList) {
        setBackground(colorStateList);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public void setPadding(float f3, boolean z2, boolean z3) {
        if (f3 == this.mPadding && this.mInsetForPadding == z2 && this.mInsetForRadius == z3) {
            return;
        }
        this.mPadding = f3;
        this.mInsetForPadding = z2;
        this.mInsetForRadius = z3;
        updateBounds(null);
        invalidateSelf();
    }

    public void setRadius(float f3) {
        if (f3 == this.mRadius) {
            return;
        }
        this.mRadius = f3;
        updateBounds(null);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        this.mTint = colorStateList;
        this.mTintFilter = createTintFilter(colorStateList, this.mTintMode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        this.mTintFilter = createTintFilter(this.mTint, mode);
        invalidateSelf();
    }

    public void setWeight(float f3) {
        if (f3 == this.mWeight) {
            return;
        }
        this.mWeight = f3;
        updateBounds(null);
        invalidateSelf();
    }

    public RoundRectDrawable(ColorStateList colorStateList, float f3, float f4, float f5) {
        this.mInsetForPadding = false;
        this.mInsetForRadius = true;
        this.mTintMode = PorterDuff.Mode.SRC_IN;
        this.mIsSupportSmoothRoundCorner = false;
        this.mRadius = f3;
        this.mWeight = f4;
        this.mCardRoundCornerRadius = f5;
        this.mPaint = new Paint(5);
        setBackground(colorStateList);
        this.mBoundsF = new RectF();
        this.mBoundsI = new Rect();
        this.mIsSupportSmoothRoundCorner = RoundCornerUtil.isVersionSupport();
    }
}