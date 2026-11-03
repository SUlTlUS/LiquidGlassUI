package com.coui.appcompat.panel;

import android.animation.ValueAnimator;
import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements ValueAnimator.AnimatorUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPanelPressHelper f13397a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13398b;

    public /* synthetic */ e(COUIPanelPressHelper cOUIPanelPressHelper, View view) {
        this.f13397a = cOUIPanelPressHelper;
        this.f13398b = view;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        COUIPanelPressHelper.a(this.f13397a, this.f13398b, valueAnimator);
    }
}