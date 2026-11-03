package com.coui.appcompat.bottomfloatingtoolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ItemViewAnimatorImpl f13284a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13285b;

    public /* synthetic */ f(ItemViewAnimatorImpl itemViewAnimatorImpl, Runnable runnable) {
        this.f13284a = itemViewAnimatorImpl;
        this.f13285b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ItemViewAnimatorImpl.d(this.f13284a, this.f13285b);
    }
}