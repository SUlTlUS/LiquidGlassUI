package com.coui.appcompat.toolbar;

import android.view.ViewTreeObserver;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements ViewTreeObserver.OnScrollChangedListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppBarBlurOverlayBehavior f13621a;

    public /* synthetic */ b(AppBarBlurOverlayBehavior appBarBlurOverlayBehavior) {
        this.f13621a = appBarBlurOverlayBehavior;
    }

    @Override // android.view.ViewTreeObserver.OnScrollChangedListener
    public final void onScrollChanged() {
        AppBarBlurOverlayBehavior.b(this.f13621a);
    }
}