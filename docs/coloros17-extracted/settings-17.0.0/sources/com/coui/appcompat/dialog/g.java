package com.coui.appcompat.dialog;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class g implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIGuideDialogButtonLayout f13361a;

    public /* synthetic */ g(COUIGuideDialogButtonLayout cOUIGuideDialogButtonLayout) {
        this.f13361a = cOUIGuideDialogButtonLayout;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIGuideDialogButtonLayout.b(this.f13361a, view);
    }
}