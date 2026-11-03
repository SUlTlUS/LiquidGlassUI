package com.coui.appcompat.snackbar;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class a implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUINotificationSnackBar f13558a;

    public /* synthetic */ a(COUINotificationSnackBar cOUINotificationSnackBar) {
        this.f13558a = cOUINotificationSnackBar;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUINotificationSnackBar.b(this.f13558a, view);
    }
}