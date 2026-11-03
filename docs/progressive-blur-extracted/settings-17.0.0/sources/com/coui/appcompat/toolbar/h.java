package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class h implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ boolean[] f13640a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13641b;

    public /* synthetic */ h(boolean[] zArr, Runnable runnable) {
        this.f13640a = zArr;
        this.f13641b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIToolbar.g(this.f13640a, this.f13641b);
    }
}