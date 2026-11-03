package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class j implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIToolbar f13653a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13654b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ Runnable f13655c;

    public /* synthetic */ j(COUIToolbar cOUIToolbar, Runnable runnable, Runnable runnable2) {
        this.f13653a = cOUIToolbar;
        this.f13654b = runnable;
        this.f13655c = runnable2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIToolbar.b(this.f13653a, this.f13654b, this.f13655c);
    }
}