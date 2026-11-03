package com.coui.appcompat.bottomfloatingtoolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class p implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MenuViewAnimatorImpl f13292a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ boolean f13293b;

    public /* synthetic */ p(MenuViewAnimatorImpl menuViewAnimatorImpl, boolean z2) {
        this.f13292a = menuViewAnimatorImpl;
        this.f13293b = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MenuViewAnimatorImpl.d(this.f13292a, this.f13293b);
    }
}