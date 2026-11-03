package com.coui.appcompat.toolbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIToolbar f13660a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Runnable f13661b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ Runnable f13662c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ COUIToolbar.EditModeTitleExitStarter f13663d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ ToolbarGroupTransitionController f13664e;

    public /* synthetic */ m(COUIToolbar cOUIToolbar, Runnable runnable, Runnable runnable2, COUIToolbar.EditModeTitleExitStarter editModeTitleExitStarter, ToolbarGroupTransitionController toolbarGroupTransitionController) {
        this.f13660a = cOUIToolbar;
        this.f13661b = runnable;
        this.f13662c = runnable2;
        this.f13663d = editModeTitleExitStarter;
        this.f13664e = toolbarGroupTransitionController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        COUIToolbar.e(this.f13660a, this.f13661b, this.f13662c, this.f13663d, this.f13664e);
    }
}