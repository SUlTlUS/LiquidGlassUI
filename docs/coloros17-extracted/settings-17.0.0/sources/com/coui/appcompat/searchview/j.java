package com.coui.appcompat.searchview;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class j implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchBar f13492a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ImeInsetsAnimationCallback f13493b;

    public /* synthetic */ j(COUISearchBar cOUISearchBar, ImeInsetsAnimationCallback imeInsetsAnimationCallback) {
        this.f13492a = cOUISearchBar;
        this.f13493b = imeInsetsAnimationCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUISearchBar.k(this.f13492a, this.f13493b);
    }
}