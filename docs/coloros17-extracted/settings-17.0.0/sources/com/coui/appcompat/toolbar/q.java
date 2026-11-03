package com.coui.appcompat.toolbar;

import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class q implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ToolbarGroupTransitionController f13668a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int[] f13669b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ List f13670c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ Runnable f13671d;

    public /* synthetic */ q(ToolbarGroupTransitionController toolbarGroupTransitionController, int[] iArr, List list, Runnable runnable) {
        this.f13668a = toolbarGroupTransitionController;
        this.f13669b = iArr;
        this.f13670c = list;
        this.f13671d = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ToolbarGroupTransitionController.f(this.f13668a, this.f13669b, this.f13670c, this.f13671d);
    }
}