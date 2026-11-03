package com.coui.appcompat.searchview;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class p implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISearchViewAnimate f13499a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ImeInsetsAnimationCallback f13500b;

    public /* synthetic */ p(COUISearchViewAnimate cOUISearchViewAnimate, ImeInsetsAnimationCallback imeInsetsAnimationCallback) {
        this.f13499a = cOUISearchViewAnimate;
        this.f13500b = imeInsetsAnimationCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUISearchViewAnimate.c(this.f13499a, this.f13500b);
    }
}