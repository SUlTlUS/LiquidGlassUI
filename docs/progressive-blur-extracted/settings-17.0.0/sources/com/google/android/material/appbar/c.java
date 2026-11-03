package com.google.android.material.appbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class c implements COUIViewOffsetHelper.OnAnimateUpdateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIAppBarLayout.BaseBehavior f13719a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUIAppBarLayout f13720b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ CoordinatorLayout f13721c;

    public /* synthetic */ c(COUIAppBarLayout.BaseBehavior baseBehavior, COUIAppBarLayout cOUIAppBarLayout, CoordinatorLayout coordinatorLayout) {
        this.f13719a = baseBehavior;
        this.f13720b = cOUIAppBarLayout;
        this.f13721c = coordinatorLayout;
    }

    @Override // com.google.android.material.appbar.COUIViewOffsetHelper.OnAnimateUpdateListener
    public final void onAnimateUpdate(COUIViewOffsetHelper cOUIViewOffsetHelper, int i3) {
        COUIAppBarLayout.BaseBehavior.a(this.f13719a, this.f13720b, this.f13721c, cOUIViewOffsetHelper, i3);
    }
}