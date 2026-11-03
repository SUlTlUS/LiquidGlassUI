package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class g implements COUIToolbar.EditModeTitleExitStarter {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIToolbar f13637a;

    public /* synthetic */ g(COUIToolbar cOUIToolbar) {
        this.f13637a = cOUIToolbar;
    }

    @Override // com.coui.appcompat.toolbar.COUIToolbar.EditModeTitleExitStarter
    public final void startTitleExit(Runnable runnable) {
        COUIToolbar.h(this.f13637a, runnable);
    }
}