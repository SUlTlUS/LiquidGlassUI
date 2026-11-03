package com.coui.appcompat.toolbar;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i0 implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarTextTransitionController f13648a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ View f13649b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ float f13650c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ boolean f13651d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ Runnable f13652e;

    public /* synthetic */ i0(ToolbarTextTransitionController toolbarTextTransitionController, View view, float f3, boolean z2, Runnable runnable) {
        this.f13648a = toolbarTextTransitionController;
        this.f13649b = view;
        this.f13650c = f3;
        this.f13651d = z2;
        this.f13652e = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ToolbarTextTransitionController.b(this.f13648a, this.f13649b, this.f13650c, this.f13651d, this.f13652e);
    }
}