package com.coui.appcompat.bottomfloatingtoolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class g implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ItemViewAnimatorImpl f13286a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13287b;

    public /* synthetic */ g(ItemViewAnimatorImpl itemViewAnimatorImpl, Runnable runnable) {
        this.f13286a = itemViewAnimatorImpl;
        this.f13287b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ItemViewAnimatorImpl.b(this.f13286a, this.f13287b);
    }
}