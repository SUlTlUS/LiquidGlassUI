package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f0 implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int[] f13635a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13636b;

    public /* synthetic */ f0(int[] iArr, Runnable runnable) {
        this.f13635a = iArr;
        this.f13636b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ToolbarTextTransitionController.a(this.f13635a, this.f13636b);
    }
}