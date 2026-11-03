package com.coui.appcompat.panel;

import android.animation.ValueAnimator;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPanelPressHelper f13399a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13400b;

    public /* synthetic */ f(COUIPanelPressHelper cOUIPanelPressHelper, View view) {
        this.f13399a = cOUIPanelPressHelper;
        this.f13400b = view;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIPanelPressHelper.b(this.f13399a, this.f13400b, valueAnimator);
    }
}