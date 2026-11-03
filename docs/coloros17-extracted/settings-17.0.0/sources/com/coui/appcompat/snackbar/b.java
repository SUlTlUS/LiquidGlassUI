package com.coui.appcompat.snackbar;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class b implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUINotificationSnackBar f13559a;

    public /* synthetic */ b(COUINotificationSnackBar cOUINotificationSnackBar) {
        this.f13559a = cOUINotificationSnackBar;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUINotificationSnackBar.a(this.f13559a, view);
    }
}