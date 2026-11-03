package com.coui.appcompat.button;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SingleButtonWrap f13305a;

    public /* synthetic */ f(SingleButtonWrap singleButtonWrap) {
        this.f13305a = singleButtonWrap;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SingleButtonWrap.b(this.f13305a);
    }
}