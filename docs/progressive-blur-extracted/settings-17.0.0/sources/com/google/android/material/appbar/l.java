package com.google.android.material.appbar;

import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class l implements View.OnLayoutChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIFloatingScrollingViewBehavior f13734a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ CoordinatorLayout f13735b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ View f13736c;

    public /* synthetic */ l(COUIFloatingScrollingViewBehavior cOUIFloatingScrollingViewBehavior, CoordinatorLayout coordinatorLayout, View view) {
        this.f13734a = cOUIFloatingScrollingViewBehavior;
        this.f13735b = coordinatorLayout;
        this.f13736c = view;
    }

    @Override // android.view.View.OnLayoutChangeListener
    public final void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        COUIFloatingScrollingViewBehavior.b(this.f13734a, this.f13735b, this.f13736c, view, i3, i4, i5, i6, i7, i8, i9, i10);
    }
}