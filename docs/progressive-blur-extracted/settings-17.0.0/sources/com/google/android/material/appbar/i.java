package com.google.android.material.appbar;

import com.coui.appcompat.toolbar.COUIToolbar;

/* JADX INFO: loaded from: classes8.dex */
public final /* synthetic */ class i implements COUIToolbar.EditModeTitleExitStarter {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUICollapsingToolbarLayout f13730a;

    public /* synthetic */ i(COUICollapsingToolbarLayout cOUICollapsingToolbarLayout) {
        this.f13730a = cOUICollapsingToolbarLayout;
    }

    @Override // com.coui.appcompat.toolbar.COUIToolbar.EditModeTitleExitStarter
    public final void startTitleExit(Runnable runnable) {
        COUICollapsingToolbarLayout.d(this.f13730a, runnable);
    }
}