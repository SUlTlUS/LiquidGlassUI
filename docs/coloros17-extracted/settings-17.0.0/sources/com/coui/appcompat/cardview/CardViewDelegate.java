package com.coui.appcompat.cardview;

import android.graphics.drawable.Drawable;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
interface CardViewDelegate {
    Drawable getCardBackground();

    View getCardView();

    boolean getPreventCornerOverlap();

    boolean getUseCompatPadding();

    void setCardBackground(Drawable drawable);

    void setMinWidthHeightInternal(int i3, int i4);

    void setShadowPadding(int i3, int i4, int i5, int i6);
}