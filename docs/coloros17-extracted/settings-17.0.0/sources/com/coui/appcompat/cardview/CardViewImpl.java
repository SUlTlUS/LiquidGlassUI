package com.coui.appcompat.cardview;

import android.content.Context;
import android.content.res.ColorStateList;

/* JADX INFO: loaded from: classes7.dex */
interface CardViewImpl {
    ColorStateList getBackgroundColor(CardViewDelegate cardViewDelegate);

    default float getCardRoundCornerRadius(CardViewDelegate cardViewDelegate) {
        return 0.0f;
    }

    float getElevation(CardViewDelegate cardViewDelegate);

    float getMaxElevation(CardViewDelegate cardViewDelegate);

    float getMinHeight(CardViewDelegate cardViewDelegate);

    float getMinWidth(CardViewDelegate cardViewDelegate);

    float getRadius(CardViewDelegate cardViewDelegate);

    float getWeight(CardViewDelegate cardViewDelegate);

    void initStatic();

    void initialize(CardViewDelegate cardViewDelegate, Context context, ColorStateList colorStateList, float f3, float f4, float f5, float f6, float f7);

    void onCompatPaddingChanged(CardViewDelegate cardViewDelegate);

    void onPreventCornerOverlapChanged(CardViewDelegate cardViewDelegate);

    void setBackgroundColor(CardViewDelegate cardViewDelegate, ColorStateList colorStateList);

    default void setCardRoundCornerRadius(CardViewDelegate cardViewDelegate, float f3) {
    }

    void setElevation(CardViewDelegate cardViewDelegate, float f3);

    void setMaxElevation(CardViewDelegate cardViewDelegate, float f3);

    void setRadius(CardViewDelegate cardViewDelegate, float f3);

    void setWeight(CardViewDelegate cardViewDelegate, float f3);

    void updatePadding(CardViewDelegate cardViewDelegate);
}