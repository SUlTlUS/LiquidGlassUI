package com.coui.appcompat.chip;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class m implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipTabGroup f13328a;

    public /* synthetic */ m(COUIChipTabGroup cOUIChipTabGroup) {
        this.f13328a = cOUIChipTabGroup;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIChipTabGroup.f(this.f13328a, view);
    }
}