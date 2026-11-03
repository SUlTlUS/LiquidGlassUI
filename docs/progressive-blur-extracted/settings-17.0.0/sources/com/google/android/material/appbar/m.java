package com.google.android.material.appbar;

import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f13737a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ RecyclerView f13738b;

    public /* synthetic */ m(int i3, RecyclerView recyclerView) {
        this.f13737a = i3;
        this.f13738b = recyclerView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIFloatingScrollingViewBehavior.a(this.f13737a, this.f13738b);
    }
}