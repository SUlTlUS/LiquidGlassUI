package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIToolbar f13644a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ boolean f13645b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ CharSequence f13646c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ boolean f13647d;

    public /* synthetic */ i(COUIToolbar cOUIToolbar, boolean z2, CharSequence charSequence, boolean z3) {
        this.f13644a = cOUIToolbar;
        this.f13645b = z2;
        this.f13646c = charSequence;
        this.f13647d = z3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIToolbar.a(this.f13644a, this.f13645b, this.f13646c, this.f13647d);
    }
}