package com.coui.appcompat.reddot;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIRedDotFrameLayout f13467a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ COUIHintRedDot f13468b;

    public /* synthetic */ b(COUIRedDotFrameLayout cOUIRedDotFrameLayout, COUIHintRedDot cOUIHintRedDot) {
        this.f13467a = cOUIRedDotFrameLayout;
        this.f13468b = cOUIHintRedDot;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIRedDotFrameLayout.a(this.f13467a, this.f13468b);
    }
}