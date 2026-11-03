package com.coui.appcompat.bottomfloatingaccessorynavigationbar;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AccessorySideExpansionController f13253a;

    public /* synthetic */ c(AccessorySideExpansionController accessorySideExpansionController) {
        this.f13253a = accessorySideExpansionController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AccessorySideExpansionController.g(this.f13253a);
    }
}