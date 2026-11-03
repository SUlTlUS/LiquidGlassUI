package com.coui.appcompat.cardview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
class CardViewApi21Impl implements CardViewImpl {
    private RoundRectDrawable getCardBackground(CardViewDelegate cardViewDelegate) {
        return (RoundRectDrawable) cardViewDelegate.getCardBackground();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public ColorStateList getBackgroundColor(CardViewDelegate cardViewDelegate) {
        return getCardBackground(cardViewDelegate).getColor();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getCardRoundCornerRadius(CardViewDelegate cardViewDelegate) {
        return getCardBackground(cardViewDelegate).getCardRoundCornerRadius();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getElevation(CardViewDelegate cardViewDelegate) {
        return cardViewDelegate.getCardView().getElevation();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getMaxElevation(CardViewDelegate cardViewDelegate) {
        return getCardBackground(cardViewDelegate).getPadding();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getMinHeight(CardViewDelegate cardViewDelegate) {
        return getRadius(cardViewDelegate) * 2.0f;
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getMinWidth(CardViewDelegate cardViewDelegate) {
        return getRadius(cardViewDelegate) * 2.0f;
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getRadius(CardViewDelegate cardViewDelegate) {
        return getCardBackground(cardViewDelegate).getRadius();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public float getWeight(CardViewDelegate cardViewDelegate) {
        return getCardBackground(cardViewDelegate).getWeight();
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void initStatic() {
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void initialize(CardViewDelegate cardViewDelegate, Context context, ColorStateList colorStateList, float f3, float f4, float f5, float f6, float f7) {
        cardViewDelegate.setCardBackground(new RoundRectDrawable(colorStateList, f3, f6, f7));
        View cardView = cardViewDelegate.getCardView();
        cardView.setClipToOutline(true);
        cardView.setElevation(f4);
        setMaxElevation(cardViewDelegate, f5);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void onCompatPaddingChanged(CardViewDelegate cardViewDelegate) {
        setMaxElevation(cardViewDelegate, getMaxElevation(cardViewDelegate));
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void onPreventCornerOverlapChanged(CardViewDelegate cardViewDelegate) {
        setMaxElevation(cardViewDelegate, getMaxElevation(cardViewDelegate));
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setBackgroundColor(CardViewDelegate cardViewDelegate, ColorStateList colorStateList) {
        getCardBackground(cardViewDelegate).setColor(colorStateList);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setCardRoundCornerRadius(CardViewDelegate cardViewDelegate, float f3) {
        getCardBackground(cardViewDelegate).setCardRoundCornerRadius(f3);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setElevation(CardViewDelegate cardViewDelegate, float f3) {
        cardViewDelegate.getCardView().setElevation(f3);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setMaxElevation(CardViewDelegate cardViewDelegate, float f3) {
        getCardBackground(cardViewDelegate).setPadding(f3, cardViewDelegate.getUseCompatPadding(), cardViewDelegate.getPreventCornerOverlap());
        updatePadding(cardViewDelegate);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setRadius(CardViewDelegate cardViewDelegate, float f3) {
        getCardBackground(cardViewDelegate).setRadius(f3);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void setWeight(CardViewDelegate cardViewDelegate, float f3) {
        getCardBackground(cardViewDelegate).setWeight(f3);
    }

    @Override // com.coui.appcompat.cardview.CardViewImpl
    public void updatePadding(CardViewDelegate cardViewDelegate) {
        if (!cardViewDelegate.getUseCompatPadding()) {
            cardViewDelegate.setShadowPadding(0, 0, 0, 0);
            return;
        }
        float maxElevation = getMaxElevation(cardViewDelegate);
        float radius = getRadius(cardViewDelegate);
        int iCeil = (int) Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(maxElevation, radius, cardViewDelegate.getPreventCornerOverlap()));
        int iCeil2 = (int) Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(maxElevation, radius, cardViewDelegate.getPreventCornerOverlap()));
        cardViewDelegate.setShadowPadding(iCeil, iCeil2, iCeil, iCeil2);
    }
}