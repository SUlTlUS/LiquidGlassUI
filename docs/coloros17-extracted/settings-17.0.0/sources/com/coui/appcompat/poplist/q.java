package com.coui.appcompat.poplist;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class q implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupMenuRootView f13445a;

    public /* synthetic */ q(COUIPopupMenuRootView cOUIPopupMenuRootView) {
        this.f13445a = cOUIPopupMenuRootView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIPopupMenuRootView.a(this.f13445a);
    }
}