package com.google.android.material.appbar;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class h implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ boolean[] f13728a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13729b;

    public /* synthetic */ h(boolean[] zArr, Runnable runnable) {
        this.f13728a = zArr;
        this.f13729b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUICollapsingToolbarLayout.b(this.f13728a, this.f13729b);
    }
}