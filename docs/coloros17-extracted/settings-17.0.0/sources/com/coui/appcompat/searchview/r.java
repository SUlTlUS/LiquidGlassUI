package com.coui.appcompat.searchview;

import android.animation.TypeEvaluator;
import android.graphics.Insets;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class r implements TypeEvaluator {
    @Override // android.animation.TypeEvaluator
    public final Object evaluate(float f3, Object obj, Object obj2) {
        return CustomWindowInsetsAnimationControlListener.c(f3, (Insets) obj, (Insets) obj2);
    }
}