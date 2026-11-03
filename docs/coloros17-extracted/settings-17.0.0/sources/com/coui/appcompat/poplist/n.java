package com.coui.appcompat.poplist;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class n implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUITouchRecyclerView f13440a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ float f13441b;

    public /* synthetic */ n(COUITouchRecyclerView cOUITouchRecyclerView, float f3) {
        this.f13440a = cOUITouchRecyclerView;
        this.f13441b = f3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIPopupListWindow.k(this.f13440a, this.f13441b);
    }
}