package com.coui.appcompat.poplist;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class e implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIPopupListWindow f13427a;

    public /* synthetic */ e(COUIPopupListWindow cOUIPopupListWindow) {
        this.f13427a = cOUIPopupListWindow;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIPopupListWindow.c(this.f13427a, view);
    }
}