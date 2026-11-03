package com.coui.appcompat.toolbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppBarBlurOverlayBehavior f13628a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ CoordinatorLayout f13629b;

    public /* synthetic */ d(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior, CoordinatorLayout coordinatorLayout) {
        this.f13628a = appBarBlurOverlayBehavior;
        this.f13629b = coordinatorLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AppBarBlurOverlayBehavior.d(this.f13628a, this.f13629b);
    }
}