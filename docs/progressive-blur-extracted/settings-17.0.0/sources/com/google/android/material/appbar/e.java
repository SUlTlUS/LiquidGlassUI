package com.google.android.material.appbar;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICollapsingToolbarLayout f13723a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13724b;

    public /* synthetic */ e(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, Runnable runnable) {
        this.f13723a = cOUICollapsingToolbarLayout;
        this.f13724b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUICollapsingToolbarLayout.e(this.f13723a, this.f13724b);
    }
}