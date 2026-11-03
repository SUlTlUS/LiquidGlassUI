package com.coui.appcompat.chip;

import android.view.View;

/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class i implements View.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ COUIChipGroup f13324a;

    public /* synthetic */ i(COUIChipGroup cOUIChipGroup) {
        this.f13324a = cOUIChipGroup;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        COUIChipGroup.d(this.f13324a, view);
    }
}