package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class k implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int[] f13656a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13657b;

    public /* synthetic */ k(int[] iArr, Runnable runnable) {
        this.f13656a = iArr;
        this.f13657b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIToolbar.i(this.f13656a, this.f13657b);
    }
}