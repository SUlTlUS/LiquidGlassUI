package com.google.android.material.appbar;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class p implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SettingsCollapsableAppBarLayout f13741a;

    public /* synthetic */ p(SettingsCollapsableAppBarLayout settingsCollapsableAppBarLayout) {
        this.f13741a = settingsCollapsableAppBarLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f13741a.updateSubtitle();
    }
}