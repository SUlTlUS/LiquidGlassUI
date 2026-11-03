package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class l implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIToolbar f13658a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13659b;

    public /* synthetic */ l(COUIToolbar cOUIToolbar, Runnable runnable) {
        this.f13658a = cOUIToolbar;
        this.f13659b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIToolbar.d(this.f13658a, this.f13659b);
    }
}