package com.google.android.material.appbar;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICollapsableAppBarLayout f13722a;

    public /* synthetic */ d(COUICollapsableAppBarLayout cOUICollapsableAppBarLayout) {
        this.f13722a = cOUICollapsableAppBarLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f13722a.updateSubtitle();
    }
}