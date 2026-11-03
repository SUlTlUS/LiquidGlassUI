package com.google.android.material.appbar;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class j implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICollapsingToolbarLayout f13731a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ CharSequence f13732b;

    public /* synthetic */ j(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout, CharSequence charSequence) {
        this.f13731a = cOUICollapsingToolbarLayout;
        this.f13732b = charSequence;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUICollapsingToolbarLayout.c(this.f13731a, this.f13732b);
    }
}