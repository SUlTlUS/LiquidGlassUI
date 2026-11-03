package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIToolbar f13633a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13634b;

    public /* synthetic */ f(COUIToolbar cOUIToolbar, Runnable runnable) {
        this.f13633a = cOUIToolbar;
        this.f13634b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIToolbar.c(this.f13633a, this.f13634b);
    }
}