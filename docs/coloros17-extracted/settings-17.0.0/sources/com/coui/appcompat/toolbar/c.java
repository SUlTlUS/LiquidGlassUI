package com.coui.appcompat.toolbar;

import android.view.ViewTreeObserver;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements ViewTreeObserver.OnGlobalLayoutListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppBarBlurOverlayBehavior f13624a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ CoordinatorLayout f13625b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ AppBarBlurOverlayView f13626c;

    public /* synthetic */ c(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior, CoordinatorLayout coordinatorLayout, AppBarBlurOverlayView appBarBlurOverlayView) {
        this.f13624a = appBarBlurOverlayBehavior;
        this.f13625b = coordinatorLayout;
        this.f13626c = appBarBlurOverlayView;
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public final void onGlobalLayout() {
        AppBarBlurOverlayBehavior.c(this.f13624a, this.f13625b, this.f13626c);
    }
}