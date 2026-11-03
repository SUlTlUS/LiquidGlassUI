package com.coui.appcompat.tooltips;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/* JADX INFO: loaded from: classes7.dex */
public interface COUIIBubbleStyle {
    void dismissWindow();

    TextView getContentView();

    int[] getDefStyleParams();

    default ImageView getDismissIv() {
        return null;
    }

    int getLayoutId();

    int getMaxWidth();

    int getRealWidth(int i3, ViewGroup viewGroup);

    default void hideDismissView() {
    }

    void initBubbleStyle(ViewGroup viewGroup);

    void refreshBubbleStyle(ColorStateList colorStateList);

    default void refreshTextResources() {
    }

    void setContentText(CharSequence charSequence);

    void setContentTextColor(ColorStateList colorStateList);

    default void setContentTextRes(int i3) {
    }

    void setContentView(View view);

    default void setDismissTextRes(int i3) {
    }

    default void setTitleRes(int i3) {
    }

    void setToolTipsAction(IToolTipsAction iToolTipsAction, Context context, int i3);

    void sizeBubbleStyle(ViewGroup viewGroup, int i3);
}