package com.coui.appcompat.chip;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class h implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipGroup f13323a;

    public /* synthetic */ h(COUIChipGroup cOUIChipGroup) {
        this.f13323a = cOUIChipGroup;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIChipGroup.e(this.f13323a, view);
    }
}