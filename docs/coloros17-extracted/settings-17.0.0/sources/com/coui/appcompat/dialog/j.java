package com.coui.appcompat.dialog;

import android.window.OnBackInvokedCallback;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class j implements OnBackInvokedCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUISecurityAlertDialogBuilder f13364a;

    public /* synthetic */ j(COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder) {
        this.f13364a = cOUISecurityAlertDialogBuilder;
    }

    @Override // android.window.OnBackInvokedCallback
    public final void onBackInvoked() {
        COUISecurityAlertDialogBuilder.b(this.f13364a);
    }
}