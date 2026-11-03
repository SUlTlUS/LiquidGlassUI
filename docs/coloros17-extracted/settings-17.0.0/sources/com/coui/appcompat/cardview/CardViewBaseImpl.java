package com.coui.appcompat.cardview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.coui.appcompat.roundRect.COUIRoundRectUtil;

/* JADX INFO: loaded from: classes7.dex */
class CardViewBaseImpl implements CardViewImpl {
    private final RectF mCornerRect = new RectF();

    /* JADX INFO: renamed from: com.coui.appcompat.cardview.CardViewBaseImpl$1, reason: invalid class name */
    public class AnonymousClass1 implements RoundRectDrawableWithShadow.RoundRectHelper {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.cardview.RoundRectDrawableWithShadow.RoundRectHelper
        public void drawRoundRect(Canvas canvas, RectF rectF, float f3, Paint paint) {
            canvas.drawPath(COUIRoundRectUtil.getInstance().getPath(rectF, f3), paint);
        }
    }

    private RoundRectDrawableWithShadow createBackground(Context context, ColorStateList colorStateList, float f3, float f4, float f5) {
        return new RoundRectDrawableWithShadow(context.getResources(), colorStateList, f3, f4, f5);
    }

    private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate cardViewDelegate) {
        return (RoundRectDrawableWithShadow) cardViewDelegate.getCardBackground();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public ColorStateList getBackgroundColor(CardViewDelegate cardViewDelegate) {
        return getShadowBackground(cardViewDelegate).getColor();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getElevation(CardViewDelegate cardViewDelegate) {
        return getShadowBackground(cardViewDelegate).getShadowSize();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getMaxElevation(CardViewDelegate cardViewDelegate) {
        return getShadowBackground(cardViewDelegate).getMaxShadowSize();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getMinHeight(CardViewDelegate cardViewDelegate) {
        return getShadowBackground(cardViewDelegate).getMinHeight();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getMinWidth(CardViewDelegate cardViewDelegate) {
        return getShadowBackground(cardViewDelegate).getMinWidth();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getRadius(CardViewDelegate cardViewDelegate) {
        return getShadowBackground(cardViewDelegate).getCornerRadius();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getWeight(CardViewDelegate cardViewDelegate) {
        return 0.0f;
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void initStatic() {
        RoundRectDrawableWithShadow.setRoundRectHelper(new AnonymousClass1());
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void initialize(CardViewDelegate cardViewDelegate, Context context, ColorStateList colorStateList, float f3, float f4, float f5, float f6, float f7) {
        RoundRectDrawableWithShadow roundRectDrawableWithShadowCreateBackground = createBackground(context, colorStateList, f3, f4, f5);
        roundRectDrawableWithShadowCreateBackground.setAddPaddingForCorners(cardViewDelegate.getPreventCornerOverlap());
        cardViewDelegate.setCardBackground(roundRectDrawableWithShadowCreateBackground);
        updatePadding(cardViewDelegate);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void onCompatPaddingChanged(CardViewDelegate cardViewDelegate) {
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void onPreventCornerOverlapChanged(CardViewDelegate cardViewDelegate) {
        getShadowBackground(cardViewDelegate).setAddPaddingForCorners(cardViewDelegate.getPreventCornerOverlap());
        updatePadding(cardViewDelegate);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setBackgroundColor(CardViewDelegate cardViewDelegate, ColorStateList colorStateList) {
        getShadowBackground(cardViewDelegate).setColor(colorStateList);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setElevation(CardViewDelegate cardViewDelegate, float f3) {
        getShadowBackground(cardViewDelegate).setShadowSize(f3);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setMaxElevation(CardViewDelegate cardViewDelegate, float f3) {
        getShadowBackground(cardViewDelegate).setMaxShadowSize(f3);
        updatePadding(cardViewDelegate);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setRadius(CardViewDelegate cardViewDelegate, float f3) {
        getShadowBackground(cardViewDelegate).setCornerRadius(f3);
        updatePadding(cardViewDelegate);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setWeight(CardViewDelegate cardViewDelegate, float f3) {
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void updatePadding(CardViewDelegate cardViewDelegate) {
        Rect rect = new Rect();
        getShadowBackground(cardViewDelegate).getMaxShadowAndCornerPadding(rect);
        cardViewDelegate.setMinWidthHeightInternal((int) Math.ceil(getMinWidth(cardViewDelegate)), (int) Math.ceil(getMinHeight(cardViewDelegate)));
        cardViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }
}